# CLAUDE.md

> 本文件面向自动化代理与协作者：项目事实、改造边界、执行规则与验证要求。
> `CLAUDE.md` 与 `AGENTS.md` 内容保持一致（同一份文档），修改时请同步更新两份。

## Project Overview

orion-kit 是 Java 工具库（`cn.orionsec.kit`），基线 **JDK 21**（`maven.compiler.release=21`），发布至 Maven Central（group `cn.orionsec.kit`），当前版本 `3.0.0`，MIT 许可。

### 模块

```
orion-lang          (core: collections, IO, crypto, dates, reflection, encoding, threads)
  ├── orion-ext     (extensions: IP location, mail, process, tail, git, file watch)
  ├── orion-office  (CSV/Excel import-export)
  ├── orion-http    (OkHttp, Apache HttpClient, Jsoup wrappers)
  ├── orion-net     (SSH/SFTP via JSch, FTP, TCP/UDP sockets)
  ├── orion-web     (Servlet filters and utilities)
  ├── orion-spring  (Spring container helpers)
  ├── orion-redis   (Redis distributed locks)
  └── orion-generator (random data generators: name, address, ID card, bank, etc.)

orion-log           (logging config, standalone)
orion-all           (aggregator POM, depends on all above)
```

所有模块共用根 parent POM；`orion-lang` 是基础模块，其余模块都依赖它。

### 关键依赖

| Library                                 | Usage                                 |
|-----------------------------------------|---------------------------------------|
| fastjson 2.x                            | JSON serialization                    |
| Bouncy Castle                           | Extended crypto (SM4, etc.)           |
| Apache Commons (lang3, compress, codec) | String/compression/encoding utilities |
| dom4j                                   | XML processing                        |
| SnakeYAML                               | YAML processing                       |
| POI 5.5.1                               | Excel read/write                      |
| JSch (mwiede fork) 2.x                  | SSH/SFTP                              |
| JGit                                    | Git operations                        |
| OkHttp 5.x / HttpClient 5.x             | HTTP clients                          |
| Jsoup                                   | HTML parsing                          |
| jspecify 1.0.0                          | `@NonNull` / `@Nullable` 空安全注解   |

## Scope & Rules（硬性约束）

1. 仓库为 **Java 21 工具库**（`3.0.0`）。任何改动必须：可在 JDK 21 下编译、**无预览特性**、**不引入 JDK 22+ API**。
2. `LICENSE` 版权归属必须与源文件 MIT 头一致（`Copyright (c) 2019 - present Jiahang Li`），修改需在提交信息中说明依据；不得删除 `.codebuddy/` 目录。
3. 公共 API 变更必须同步：Javadoc、`README.md`、`skill/SKILL.md`。
4. 新增依赖必须：写入根 `pom.xml` 版本属性、说明用途、避免与现有依赖重复或冲突；新增测试依赖使用 `test` 作用域。
5. 破坏性变更（`record` 化 wrapper、`Dates8` 收敛、传输类接口加锁方法等）**必须**走大版本（4.0）并保留 `@Deprecated` 兼容层，禁止随小版本合并。
6. 保持 UTF-8 编码与现有代码风格；不要使用 `@SuppressWarnings("ALL")` 掩盖新代码问题。

## Build & Environment

### Pre-flight（执行任何构建/测试前）

```powershell
$env:JAVA_HOME='D:\Profile\jdk-21.0.8'; $env:Path="$env:JAVA_HOME\bin;$env:Path"
java -version     # 必须为 21.0.x
```

- 默认 `JAVA_HOME` 是 JDK 8，未切换会直接构建失败。
- 最小验证：`mvn -pl <module> test -Dtest=<TestClass> -DfailIfNoSpecifiedTests=false -P skip-docs`
- 全量验证：`mvn clean install -DskipTests -P skip-docs` 然后 `mvn test`
- 离线加速：依赖已在本机仓库时可加 `-o`

### Common Commands

```bash
# 编译全部模块（跳过测试与文档）
mvn -B -ntp clean install -DskipTests -P skip-docs

# 跑某个模块的全部测试
mvn -B -ntp -pl orion-lang test -P skip-docs

# 跑指定测试类（跨模块）
mvn -B -ntp -pl orion-net,orion-ext test -P skip-docs \
    "-Dtest=TcpSocketTest,FtpClientPoolTest,ProcessAwaitExecutorTest" -DfailIfNoSpecifiedTests=false

# 虚拟线程专项回归（建议每次并发改动后执行）
mvn -B -ntp -pl orion-lang test -P skip-docs \
    "-Dtest=ThreadsVirtualTest,WaiterTest,SnowFlakeIdWorkerTest,UUIdsTest" -DfailIfNoSpecifiedTests=false

# 生成聚合 javadoc
mvn javadoc:aggregate -P !skip-docs

# 发布到 Maven Central（需要 GPG + Sonatype 凭据）
mvn -U clean deploy -P release -P !skip-docs -DskipTests

# pinning 诊断（JDK 21-23）
-Djdk.tracePinnedThreads=short
jfr print --events jdk.VirtualThreadPinned <file.jfr>
```

## Code Conventions

### Naming

- Utility class pattern: `{Function}s` (e.g., `Strings`, `Dates`, `Lists`, `Maps`)
- When a JDK class with the same name exists, suffix with `1`: `Arrays1`, `Objects1`, `Files1`
- Package root: `cn.orionsec.kit.{module}`

### File Header

Every source file must carry the MIT license header:

```java
/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * The MIT License (MIT)
 * ...
 */
```

### Configuration Pattern

库使用 `KitConfig` 静态注册表管理可配置默认值（pattern、code、limit）；每个模块的 `Kit{Module}Configuration` 类在静态块中初始化这些默认值。

### Editor Config

- UTF-8 encoding, spaces for indentation, LF line endings (CRLF for `.cmd`/`.bat`/`.ps1`)
- YAML files use indent size 2

## Concurrency Policy（重点，违反需评审）

1. 平台线程池（`Threads.GLOBAL_EXECUTOR` / `CACHE_EXECUTOR`）为默认；虚拟线程必须显式选择（`Threads.VIRTUAL_EXECUTOR` 或 `newVirtualThreadPool`），库不静默切换。
2. **禁止池化虚拟线程**（`newThreadPerTaskExecutor` 语义）；并发上限使用 `Semaphore` 或既有连接池，不得用线程池大小限流。
3. **禁止把 CPU 密集型任务放到虚拟线程**（crypto/hash/Excel/图片/序列化/生成器）。
4. **禁止新增 `synchronized` 阻塞段**（JDK 21-23 pinning）；已有 `synchronized` 逐步替换为 `ReentrantLock`，且替换时必须保持锁对象语义（跨实例共享的锁不能改为实例私有锁）；FTP/SFTP 传输类的 `synchronized(instance/executor)` 已标注注释，传输任务建议使用平台线程。
5. **持锁期间禁止阻塞等待**（如连接池 `poll(timeout)`）；等待必须在锁外进行。
6. 定时/周期/常驻任务禁止使用虚拟线程（会让 JVM 无法退出），保留平台线程（`SystemClock`、缓存过期检查、心跳保活）。
7. 新增 `ThreadLocal` 必须说明生命周期；虚拟线程下每任务一个实例。
8. 阻塞型任务必须支持注入 `Executor`/`ExecutorService`，并提供虚拟线程便捷方法（参考 `virtualScheduler()`、`virtualAcceptThreadPool()`）；虚拟线程池统一通过 `VirtualExecutorBuilder`（或 `ExecutorBuilder.create().virtual()`）构建，不要在业务代码里直接 `Executors.newThreadPerTaskExecutor`。
9. 全局 `Threads.VIRTUAL_EXECUTOR` 为共享资源，任何关闭入口必须防御误关闭（参考 `TcpReceive.closePool()` 的判断）。

## JDK 21 改造速查

| 能力 | 入口 | 说明 |
|:--|:--|:--|
| 可唤醒延时等待 | `cn.orionsec.kit.lang.define.thread.Waiter` | 替代轮询 `sleep`（`await`/`signal`） |
| tail/watch 虚拟线程 | `Tracker.startVirtual()`、`FileWatcher.startVirtual()`、`FolderWatcher.startVirtual()` | 配合 `stop()` 立即唤醒 |
| 进程虚拟线程读流 | `ProcessAwaitExecutor.virtualScheduler()` | 阻塞输出流读取 |
| TCP 虚拟线程 accept | `TcpReceive.virtualAcceptThreadPool()` | 阻塞 accept |
| 显式锁 | `UUIds`、`SnowFlakeIdWorker`、`FtpClientPool`、`TimedCacheChecker`、`TimeoutCheckerImpl` | 替代 `synchronized`，避免 JDK21 pinning |
| switch 模式匹配 | `Arrays1.wrap/unWrap`、`Objects1.toString` | 语法现代化先例 |

## Files requiring review（高风险文件）

- `orion-lang/.../config/KitConfig.java`（全局静态配置）
- `orion-lang/.../utils/Threads.java`、`define/thread/*`（并发基座）
- `orion-lang/.../id/SnowFlakeIdWorker.java`（ID 唯一性）
- `orion-lang/.../define/collect/ConcurrentReferenceHashMap.java`（自研并发容器，暂未改造）
- `orion-lang/.../define/cache/*`、`support/timeout/*`（轮询与过期语义）
- `orion-net/.../ftp/client/pool/FtpClientPool.java`（连接池并发语义）
- `orion-ext/.../tail/*`、`watch/*`（阻塞与停止语义）

## Testing

- 框架：**JUnit 4.13.2**（`org.junit.Test` + `org.junit.Assert`），暂不引入 JUnit 5 / Mockito
- 命名：`XxxTest` / `XxxTests` 混用（保持现状）
- 每个模块目录结构镜像 main 包名；`orion-lang` 另有一套 `cn.orionsec.kit.test.*` 旧式测试
- 新增/修改的公共能力必须有单元测试；并发相关必须包含**虚拟线程并发用例**与**停止/关闭立即性用例**
- 并发/虚拟线程用例参考：`ThreadsVirtualTest`、`VirtualExecutorBuilderTest`、`WaiterTest`、`SnowFlakeIdWorkerTest#testNextIdConcurrentUnique`、`FtpClientPoolTest#testPoolConcurrentGetReturn`、`TcpSocketTest#testVirtualThreadAccept`、`ProcessAwaitExecutorTest#testVirtualSchedulerReadStream`
- 环境依赖型用例（`CompressTests`、`FileSplitMergeTests`、`RsaTests#pfx`、orion-ext 的 `ProcessAsyncTests` 等硬编码 `C:\Users\Administrator\orion-kit-test\` 素材的集成用例）需要本地测试素材，缺失时通过 `Assume` 自动跳过，不计入回归；新增依赖本地素材的用例须沿用该守卫方式
- 提交门槛：至少通过受影响模块的全量测试 + `orion-lang` 全量测试
- 修改并发代码后建议附加 JFR/线程转储验证（无 pinning 事件、无死锁）

## Known Limitations

1. 默认 `JAVA_HOME` 为 JDK 8，命令行构建需切换（见 Pre-flight）
2. `orion-web` / `orion-spring` / `orion-redis` / `orion-log` 无测试覆盖；`orion-http` 仅 3 个测试
3. JDK 17+ 强封装：对 JDK 内部 API/`Unsafe` 的反射访问需要 `--add-opens`，`utils/reflect` 包改造需注意
4. FTP/SFTP 传输类的 `synchronized` 在 JDK 21-23 上会 pin 虚拟线程（接口锁定改造列入 4.0）
5. `orion-http` 的 main 源码 `OkWebSocketServer` 基于 MockWebServer（继承 `org.junit.rules.ExternalResource`），该模块 main 编译需要 junit（compile scope），属测试设施泄漏，列入 4.0 清理
6. `excel-streaming-reader 5.2.0` 与 POI 5.5.1 的兼容性、`cglib 3.3.0`（provided）在 Spring 7 下的使用未做运行时验证
7. 无 `module-info.java`（未采用 JPMS）
8. 语法现代化（record 化 wrapper、`Converts` 拆分、`Files1` 现代化）尚未开展，列入 P2 backlog

## Documentation

- `README.md`：用户手册与模块职能
- `CLAUDE.md` / `AGENTS.md`：本文件（两份内容保持一致，修改时同步）
- `skill/SKILL.md`：供助手检索 API 的技能定义
- 修改版本号时同步：根 `pom.xml`、`README.md`、`skill/SKILL.md`、`CLAUDE.md`/`AGENTS.md`
