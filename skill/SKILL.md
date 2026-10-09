---
name: orion-kit
description: orion-kit Java utility library API reference. Invoke when the user asks about orion-kit, cn.orionsec.kit, or needs utility classes.
---

## Decision Table

Route user requests by domain:

| Domain                                                                 | Module            | Key Classes                                                                                                         |
|------------------------------------------------------------------------|-------------------|---------------------------------------------------------------------------------------------------------------------|
| Strings, collections, IO, crypto, dates, reflection, encoding, threads | `orion-lang`      | `Strings`, `Lists`, `Maps`, `Sets`, `Dates`, `Files1`, `Streams`, `Arrays1`, `Objects1`, `AES`, `RSA`, `Signatures` |
| IP geolocation, email, process, file tailing, git                      | `orion-ext`       | `IPs`, `Emails`, `Processes`, `GitPush`                                                                             |
| CSV and Excel import/export                                            | `orion-office`    | `CsvBeanReader`, `CsvBeanWriter`, `ExcelReads`, `ExcelWrites`                                                       |
| OkHttp, HttpClient, Jsoup                                              | `orion-http`      | `Https`, `HttpClients`, `JsoupDocument`                                                                             |
| SSH, SFTP, FTP, Telnet, TCP/UDP                                        | `orion-net`       | `SshExecutor`, `SftpExecutor`, `FtpExecutor`, `TelnetClient`, `SocketChannel`                                        |
| Servlet utilities                                                      | `orion-web`       | `Servlets`, `Cookies`                                                                                               |
| Spring container utilities                                             | `orion-spring`    | `ApplicationContexts`                                                                                               |
| Random data generators                                                 | `orion-generator` | `AddressGenerator`, `NameGenerator`, `IdCardGenerator`                                                              |

## Naming Convention

Pattern: `{function}s`. If JDK class exists, suffix with `1`:

| Need    | Class                          |
|---------|--------------------------------|
| Strings | `Strings`                      |
| Lists   | `Lists`                        |
| Maps    | `Maps`                         |
| Dates   | `Dates`                        |
| Files   | `Files1` (JDK has `File`)      |
| Streams | `Streams`                      |
| Objects | `Objects1` (JDK has `Objects`) |
| Arrays  | `Arrays1` (JDK has `Arrays`)   |

## Maven

```xml
<dependency>
    <groupId>cn.orionsec.kit</groupId>
    <artifactId>orion-all</artifactId>
    <version>3.0.0</version>
</dependency>
```

JDK 21+ required. Individual modules: `orion-lang`, `orion-ext`, `orion-office`, `orion-http`, `orion-net`, `orion-web`,
`orion-spring`, `orion-redis`, `orion-generator`.

## Query Strategy

### 1. CodeGraph (preferred)

Use CodeGraph when available.`~/.claude/skills/orion-kit/references/codegraph.db`

```
codegraph_search(query="ClassName")           # find definition
codegraph_node(symbol="FQN", includeCode=true) # signature + source
codegraph_explore(query="A B C")              # batch related symbols
codegraph_callers(symbol="method")            # who calls this
codegraph_trace(from="A", to="B")             # call path A→B
codegraph_impact(symbol="method")             # what breaks if changed
```

### 2. Markdown Fallback

Use when CodeGraph is unavailable. Path: `~/.claude/skills/orion-kit/references/{module}/{package/path}/{ClassName}.md`

```
Glob pattern="**/{ClassName}.md" path="~/.claude/skills/orion-kit/references"
Grep pattern="methodName" path="~/.claude/skills/orion-kit/references" glob="*.md"
Read file="~/.claude/skills/orion-kit/references/{module}/{pkg}/{Class}.md"
```

## Quick Answers

For common questions, answer directly without lookup:

- `Strings.isBlank(null)` → `true`
- `Lists.of(1,2,3)` → returns `ArrayList`
- `Dates.format(date, "yyyy-MM-dd")` → formatted string
- `AES.encrypt(plain, key)` / `AES.decrypt(cipher, key)` → symmetric crypto
- `RSA.sign(data, privateKey)` / `RSA.verify(data, publicKey, sig)` → asymmetric
- `Files1.touch(path)` → create file + parents
- `Streams.toByteArray(is)` → `byte[]`

## Concurrency (JDK 21)

| Need                                  | API                                                                                                              |
|:--------------------------------------|:-----------------------------------------------------------------------------------------------------------------|
| Run task on virtual thread            | `Threads.startVirtual(r)` / `Threads.callVirtual(c)`                                                             |
| Virtual thread pool                   | `Threads.VIRTUAL_EXECUTOR`, `Threads.newVirtualThreadPool(prefix)`                                               |
| Virtual thread pool builder           | `VirtualExecutorBuilder.create().namedThreadFactory("x-").build()`, `ExecutorBuilder.create().virtual().build()` |
| Interruptible sleep                   | `Threads.sleepInterruptibly(ms)`                                                                                 |
| Signal-able delay wait                | `new Waiter().await(ms)` / `signal()` (replaces polling sleep)                                                   |
| Tail / watch on virtual thread        | `Tracker.startVirtual()`, `FileWatcher.startVirtual()`, `FolderWatcher.startVirtual()`                           |
| Tail / watch on platform pool         | `Tracker.start()`, `FileWatcher.start()`, `FolderWatcher.start()`, 或 `start(Executor)` 指定线程池                     |
| Process read stream on virtual thread | `ProcessAwaitExecutor.virtualScheduler()`                                                                        |
| TCP accept on virtual thread          | `TcpReceive.virtualAcceptThreadPool()`                                                                           |
| Current thread is virtual             | `Threads.isVirtualThread()`                                                                                      |

Notes: virtual threads are opt-in; do not pool them (use `Semaphore` to limit concurrency); keep CPU-bound and scheduled
tasks on platform threads; on JDK 21-23 `synchronized` blocks pin virtual threads (prefer `ReentrantLock`).
