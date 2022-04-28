### orion-kit 是什么

`orion-kit` 是一个功能强大, 覆盖面广的工具类库。让你的开发无须编写重复的底层代码, 提高开发效率, 让你的代码更加简练、易读, 稳定且优雅。它是我学习以及时工作用到的工具重构, 补全注释, 统一规范后的产物。

<br/>

### 工程模块

模块             | 数量
:---            | :---
orion-lang      | 底层核心模块  集合、IO、反射、转换、日期、异常、加密、编码, 函数等
orion-ext       | 拓展模块  IP位置、邮件、process、tail、git, watch等    
orion-office    | 数据处理模块  csv, excel导入导出以及相互转化等
orion-log       | 日志模块 (忽略)
orion-http      | http模块  OkHttp、HttpClient、jsup的统一封装, UA生成器等
orion-net       | 网络交互模块  SSH、SFTP、FTP, Socket的简单封装
orion-web       | servlet模块 (忽略)
orion-redis     | redis模块 (忽略)
orion-spring    | spring模块 (忽略)
orion-generator | 信息生成器模块 身份证、银行卡、地址、公司、学习、行业等假数据生成器
orion-all       | 全部模块聚合 包含了上述所有模块的引用

<br/>

### 如何使用
``` pom.xml
# 当前最新版本 1.0.1
<dependency>
    <groupId>io.github.lijiahangmax</groupId>
    <artifactId>模块名称</artifactId>
    <version>最新版本</version>
</dependency>

# 如果想引用所有可以直接引入 orion-all
<dependency>
    <groupId>io.github.lijiahangmax</groupId>
    <artifactId>orion-all</artifactId>
    <version>1.0.1</version>
</dependency>
```

<br/>

### 如何寻找需要的工具类

可以使用英文直译的方式寻找, 工具命名规则为 `职能 + s`, 如过存在原生工具如`Arrays`, `Objects`, `Files` 则命名为 `Arrays1`, `Objects1`, `Files1`  
> 示例 

描述                 | 方法
:---                    | :---
字符串判空      |Strings.isBlank
集合判空         | List.isEmpty  Maps.isEmpty    
读取文件列表  | Files1.listFiles
读取文件内容   | FileReaders.readAllLines
文件压缩         | Compresses.zip
Input转byte[]  |Streams.toByteArray
时间格式化      | Dates.format
反射读取类字段 | Fields.getFieldByMethod
反射读取类方法 | Methods.getAccessibleMethod
生成二维码 | new QRCodes().encodeBase64
生成一维码  | new BarCodes().encodeBase64
并发执行线程 | Threads.concurrent
MD5签名   | Signatures.md5

<br/>

### orion-lang 模块职能

```
┌─able                      对象职能接口
├─constant                  自定义常量
├─exception                 自定义异常
├─function                  自定义函数
├─id                        全局id生成器
├─lang              
│  ├─builder                通用构造器
│  ├─cache                  缓存
│  ├─collect                自定义集合
│  ├─io                     自定义io流
│  ├─iterator               自定义迭代器
│  ├─mutable                可变对象
│  ├─thread                 线程工具包
│  └─wrapper                消息载体
│          
└─utils          
    │  Arrays1              数组工具类
    │  Booleans             布尔值工具类
    │  Chars                字符工具类
    │  Charsets             编码工具类
    │  Colors               颜色工具类
    │  Compares             比较工具类
    │  Enums                枚举工具类
    │  Exceptions           异常工具类
    │  Moneys               金额工具类
    │  Objects1             对象工具类
    │  References           引用工具类
    │  Spells               拼音工具类
    │  Strings              字符串工具类
    │  Systems              系统工具类
    │  Threads              线程工具类
    │  Valid                验证工具类
    │
    ├─awt
    │  Clipboards           剪切板工具类
    │  Fonts                字体工具类
    │  ImageDrawStream      流式绘图工具类
    │  ImageExecutorStream  流式图片处理
    │  ImageIcons           icon 生成器
    │  ImageMargins         图片边距处理器
    │  Images               图片工具类
    │
    ├─code
    │  BarCodes             一维码生成器
    │  QRCodes              二维码生成器
    │ 
    ├─codec 
    │  Base32s              base32 工具类
    │  Base62s              base62 工具类
    │  Base64s              base64 工具类
    │ 
    ├─collect 
    │  Collections          集合工具类
    │  Lists                list 工具类
    │  Maps                 map 工具类
    │  Queues               queue 工具类
    │  Sets                 set 工具类
    │ 
    ├─convert               对象转化工具包
    │ 
    ├─crypto 
    │  │  AES               AES 工具类
    │  │  Caesars           凯撒密码工具类          
    │  │  DES               DES 工具类
    │  │  DES3              3DES 工具类
    │  │  Keys              秘钥工具类
    │  │  RC4               RC4 工具类
    │  │  RSA               RSA 工具类
    │  │  Signatures        签名工具类
    │  │  SM4               SM4 工具类
    │  │ 
    │  ├─enums              工作模式, 填充方式等枚举
    │  │ 
    │  └─symmetric          对称加密实现
    │ 
    ├─ext 
    │  │  EmojiExt          emoji 工具类
    │  │  PropertiesExt     properties 工具类
    │  │ 
    │  ├─dom                xml 工具类
    │  │ 
    │  └─yml                yml 工具类
    │ 
    ├─hash                  哈希算法工具类
    │ 
    ├─identity 
    │      CreditCodes      社会统一信用代码工具类
    │      IdCards          身份证工具类
    │ 
    ├─io 
    │  │  Buffers           buffer 工具类
    │  │  Files1            文件操作工具类
    │  │  FileEncodes       文件编码工具类
    │  │  FileLocks         文件锁工具类
    │  │  FileReaders       文件读取工具类
    │  │  FileWriters       文件写入工具类
    │  │  FileTypes         文件类型工具类
    │  │  Streams           IO流工具类
    │  │  StreamReaders     IO读取工具类
    │  │  StreamWriters     IO写入工具类
    │  │ 
    │  ├─compress           文件压缩工具类
    │  │ 
    │  ├─crypto             文件加解密工具类
    │  │ 
    │  └─split              文件分切合并工具类
    │ 
    ├─json                  json 工具类
    │ 
    ├─math 
    │      BigDecimals      BigDecimal 工具类 
    │      BigIntegers      BigInteger 工具类
    │      Hex              16进制工具类
    │      Numbers          数字工具类
    │ 
    ├─net 
    │      IPs              IP工具类
    │      Ports            端口工具类
    │ 
    ├─random 
    │      Randoms          随机数工具类
    │      RndGenerator     伪随机数发生器
    │ 
    ├─reflect 
    │      Annotations      注解工具类
    │      BeanMap          Bean 转Map 工具类
    │      BeanWrapper      Bean 转化工具类
    │      Classes          Class 工具类
    │      Constructors     构造方法工具类
    │      Fields           字段工具类
    │      Generics         泛型工具类
    │      Methods          字段工具类
    │      Jars             jar 文件工具类
    │      Packages         包工具类
    │      PackageScanner   包扫描器
    │      ResourceScanner  资源扫描器
    │      TypeInfer        类型推断器
    │      Types            类型工具类
    │ 
    ├─regexp                正则工具类
    │ 
    ├─script                脚本工具类
    │ 
    ├─time 
    │  │  Birthdays         生日工具类
    │  │  DateRanges        时间区间工具类
    │  │  Dates             时间工具类(Date)
    │  │  Dates8            时间工具类(jdk1.8)
    │  │  DateStream        流式时间处理器
    │  │  LunarCalendar     农历工具类
    │  │  Zodiacs           生肖工具类
    │  │ 
    │  ├─ago                时间对比工具类
    │  │ 
    │  └─cron               cron 工具类
    │ 
    └─unit 
            LengthUnit      长度单位
            WeightUnit      重量单位
```
<br/>

### orion-ext 模块职能

```
┌─location                 ip 地理位置工具类
│
├─mail                     邮件工具类
│
├─nginx                    nginx 解析工具类
│
├─process
│   ProcessAsyncExecutor   进程异步执行处理器
│   ProcessAwaitExecutor   进程同步执行处理器
│   Processes              进程工具类
│
├─tail                     文件 tail 工具类
│
├─vcs
│  └─git                   git 工具类
│
└─watch
    ├─file                 文件监听工具类
    │
    └─folder               文件夹监听工具类
```
<br/>

### orion-office 模块职能

```
┌─csv
│  │  CsvExt               csv提取器
│  │
│  ├─annotation            csv导入导出注解
│  │
│  ├─convert               csv转化器
│  │  │
│  │  └─adapter            csv适配器
│  │
│  ├─core                  csv处理器核心
│  │
│  ├─merge                 csv合并工具
│  │
│  ├─option                csv配置项
│  │
│  ├─reader
│  │   CsvArrayReader      csv array读取器
│  │   CsvBeanReader       csv bean读取器
│  │   CsvLambdaReader     csv lamdba读取器
│  │   CsvMapReader        csv map读取器
│  │   CsvRawReader        csv 行读取器
│  │   CsvReaderIterator   csv 行迭代器
│  │
│  ├─split
│  │   CsvColumnSplit      csv列分割器
│  │   CsvRowSplit         csv行分割器
│  │
│  └─writer
│      CsvArrayWriter      csv array写入器
│      CsvBeanWriter       csv bean写入器
│      CsvLambdaWriter     csv lambda写入器
│      CsvMapWriter        csv map写入器
│
└──excel
   │  ExcelExt             excel 提取器
   │  Excels               excel 工具类
   │
   ├─annotation            excel 导入导出注解
   │
   ├─convert               excel 转换器
   │  │ 
   │  └─adapter            excel 适配器
   │
   ├─copy
   │   SheetCopier         excel sheet 拷贝工具
   │
   ├─merge
   │   ExcelMerge          excel 合并工具
   │
   ├─option                excel 配置项
   │
   ├─picture
   │   PictureParser       excel 图片解析器
   │
   ├─reader
   │   ExcelArrayReader    excel array读取器
   │   ExcelBeanReader     excel bean读取器
   │   ExcelLambdaReader   excel lambda读取器
   │   ExcelMapReader      excel map读取器
   │   ExcelReaderIterator excel 迭代器
   │
   ├─split
   │   ExcelColumnMultiSheetSplit  excel 多列拆分多 sheet 工具类
   │   ExcelColumnMultiSplit       excel 多列拆分工具类
   │   ExcelColumnSingleSplit      excel 单列拆分工具类
   │   ExcelRowSplit               excel 行拆分工具类
   │
   ├─style
   │   FontStream             字体样式流式处理器
   │   PrintStream            打印流式处理器
   │   StyleStream            样式流式处理器
   │  
   ├─type                     excel 枚举
   │
   └─writer
       │  ExcelArrayWriter    excel array写入器 
       │  ExcelBeanWriter     excel bean写入器
       │  ExcelLambdaWriter   excel lambda写入器
       │  ExcelMapWriter      excel map写入器
       │ 
       └─exporting            excel 注解写入器   
```
<br/>

### orion-http 模块职能

```
┌─apache                    httpClient 二次封装                 
│  │
│  └─file                   httpClient 文件上传下载
│
├─ok                        okHttp 二次封装
│  │
│  ├─file                   okHttp 文件上传下载
│  │
│  └─ws                     websocket 处理器           
│
├─parse                     jsup 二次封装
│
├─support                   http 常量
│
└─useragent
    StandardUserAgent       UA 常量
    UserAgentGenerators     UA 生成器
```
<br/>

### orion-net 模块职能

```
┌─ftp
│  ├─client                 FTPClient 工具类
│  │  │
│  │  ├─bigfile
│  │  │   FtpDownload       FTPClient 大文件下载
│  │  │   FtpUpload         FTPClient 大文件上传
│  │  │
│  │  ├─config              FTPClient 配置
│  │  │
│  │  └─pool                FTPClient 连接池
│  │
│  └─server                 FTPServer 工具类
│
├─remote
│  │
│  ├─channel                远程机器连接工具 jsch
│  │  │
│  │  ├─sftp                sftp 执行器
│  │  │  │
│  │  │  └─bigfile
│  │  │     SftpDownload    sftp 大文件下载
│  │  │     SftpUpload      sftp 大文件上传
│  │  │
│  │  └─ssh
│  │       CommandExecutor  远程命令执行器
│  │       ShellExecutor    shell 执行器
│  │
│  └─connection             远程机器连接工具 ssh2
│      │
│      ├─scp                scp 执行器
│      │
│      ├─sftp               sftp 执行器
│      │  │
│      │  └─bigfile        
│      │    SftpDownload    sftp 大文件下载
│      │    SftpUpload      sftp 大文件上传
│      │
│      └─ssh
│           CommandExecutor 远程命令执行器
│           ShellExecutor   shell 执行器
│
└─socket
     Sockets                socker 工具类
     TcpReceive             tcp 接收器
     TcpSend                tcp 发送器
     UdpReceive             udp 接收器
     UdpSend                upd 发送器
```
<br/>

### orion-generator 模块职能

```
┌─addres           地址生成器
│
├─bank             银行卡/信用卡生成器
│
├─company          公司名称生成器
│ 
├─education        学历生成器
│
├─email            邮箱生成器
│
├─faker            伪数据生成器工具
│
├─idcard           身份证生成器
│
├─industry         工作生成器
│
├─mobile           手机号生成器
│
├─name             中英文名字生成器
│
└─plate            车牌号生成器
```

