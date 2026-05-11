---
name: "orion-kit-docs"
description: "orion-kit Java utility library API reference and usage guide. Invoke when the user asks about orion-kit, cn.orionsec.kit, or needs to use utility classes for strings, collections, crypto, IO, HTTP, SSH, SFTP, FTP, Excel, CSV, or random data generation in Java projects."
---

# orion-kit Documentation

A comprehensive Java utility library (JDK 8+) covering collections, IO, crypto, HTTP, networking, office document processing, and random data generation.

**GroupId:** `cn.orionsec.kit` | **Version:** `2.0.7`

## Maven Dependency

```xml
<!-- All modules -->
<dependency>
    <groupId>cn.orionsec.kit</groupId>
    <artifactId>orion-all</artifactId>
    <version>2.0.7</version>
</dependency>

<!-- Or individual modules -->
<dependency>
    <groupId>cn.orionsec.kit</groupId>
    <artifactId>orion-lang</artifactId>
    <version>2.0.7</version>
</dependency>
```

## Module Overview

| Module | ArtifactId | Description |
|--------|-----------|-------------|
| orion-lang | `orion-lang` | Core: strings, collections, IO, crypto, dates, reflection, encoding, threads |
| orion-ext | `orion-ext` | Extensions: IP geolocation, email, process, file tailing, git |
| orion-office | `orion-office` | CSV and Excel import/export |
| orion-http | `orion-http` | OkHttp, HttpClient, Jsoup wrappers |
| orion-net | `orion-net` | SSH, SFTP, FTP, TCP/UDP socket |
| orion-generator | `orion-generator` | Random data generators (names, addresses, IDs, etc.) |
| orion-web | `orion-web` | Servlet utilities |
| orion-spring | `orion-spring` | Spring container utilities |

## Naming Convention

Utility classes follow the pattern `职能 + s`. If a JDK class exists, suffix with `1`:

| Need | Class |
|------|-------|
| String operations | `Strings` |
| List operations | `Lists` |
| Map operations | `Maps` |
| Date operations | `Dates` |
| File operations | `Files1` (because `java.io.Files` exists) |
| Stream operations | `Streams` |
| Object operations | `Objects1` |
| Array operations | `Arrays1` |

## Quick Reference by Use Case

### String Processing
```java
import cn.orionsec.kit.lang.utils.Strings;

Strings.isBlank(null)          // true
Strings.isNotBlank("hello")   // true
Strings.ifBlank(str, "default")
Strings.format("Hello {}", map)
Strings.join(list, ",")
Strings.omit("longtext", 10)  // truncate with "..."
```

### Collection Operations
```java
import cn.orionsec.kit.lang.utils.collect.*;

Lists.of(1, 2, 3)
Lists.partition(list, 100)    // split into chunks
Lists.map(list, mapper)       // transform
Maps.of("key", "value")
Maps.isEmpty(map)
Collections.diff(set1, set2)  // set difference
```

### Date/Time
```java
import cn.orionsec.kit.lang.utils.time.Dates;

Dates.format(new Date(), "yyyy-MM-dd")
Dates.parse("2024-01-01", "yyyy-MM-dd")
Dates.ago(date)               // "3 hours ago"
Dates.isLeapYear()
```

### File Operations
```java
import cn.orionsec.kit.lang.utils.io.Files1;

Files1.touch("/path/file.txt")
Files1.copy(src, dest)
Files1.readAllLines(file)     // via FileReaders
Files1.getSize(file)
Files1.md5(file)
```

### IO / Streams
```java
import cn.orionsec.kit.lang.utils.io.Streams;

Streams.toByteArray(inputStream)
Streams.toString(inputStream, "UTF-8")
Streams.transfer(input, output)
Streams.close(closeable)
```

### Cryptography
```java
import cn.orionsec.kit.lang.utils.crypto.*;

// Hash
Signatures.md5("data")
Signatures.sha256("data")
Signatures.hmacSha256("data", "key")

// AES (ECB/CBC/GCM)
AES.encrypt("plain", "key")
AES.decrypt(encrypted, "key")

// RSA
RSA.encrypt("plain", publicKey)
RSA.decrypt(encrypted, privateKey)
RSA.sign("data", privateKey)
RSA.verify("data", publicKey, signature)
```

### HTTP Requests
```java
import cn.orionsec.kit.http.ok.OkRequests;

// Simple GET/POST
OkResponse resp = OkRequests.get("https://api.example.com");
OkResponse resp = OkRequests.post("https://api.example.com", jsonData);

// Fluent builder
OkResponse resp = new OkRequest("https://api.example.com")
    .header("Authorization", "Bearer xxx")
    .body(json)
    .contentType(HttpContentType.JSON)
    .await();
```

### SSH / SFTP
```java
import cn.orionsec.kit.net.host.ssh.*;
import cn.orionsec.kit.net.host.sftp.*;

// SSH command execution
SshExecutor executor = new SshExecutor(host, port);
executor.auth(username, password);
executor.connect();
executor.exec("ls -la");
executor.transfer(System.out);

// SFTP file operations
SftpExecutor sftp = new SftpExecutor(channel);
sftp.upload("/remote/file", localFile);
sftp.download("/remote/file", localFile);
sftp.list("/remote/dir");
```

### CSV Import/Export
```java
import cn.orionsec.kit.office.csv.reader.CsvBeanReader;
import cn.orionsec.kit.office.csv.writer.CsvBeanWriter;

// Read CSV to beans
CsvBeanReader<User> reader = new CsvBeanReader<>(csvReader, User.class);
reader.skip(1).read();
List<User> users = reader.getRows();

// Write beans to CSV
CsvBeanWriter.create("output.csv", User.class)
    .headers("Name", "Age")
    .addRows(userList)
    .flush();
```

### Excel Import/Export
```java
import cn.orionsec.kit.office.excel.reader.ExcelBeanReader;
import cn.orionsec.kit.office.excel.writer.ExcelBeanWriter;
import cn.orionsec.kit.office.excel.Excels;

// Read Excel
Workbook wb = Excels.openWorkbook("data.xlsx");
Sheet sheet = wb.getSheetAt(0);
ExcelBeanReader<User> reader = ExcelBeanReader.create(wb, sheet, User.class);
reader.skip(1).read();

// Write Excel
ExcelBeanWriter<User> writer = new ExcelBeanWriter<>(wb, sheet, User.class);
writer.headers("Name", "Age").addRows(userList);
Excels.write(wb, "output.xlsx");
```

### Random Data Generation
```java
import cn.orionsec.kit.generator.faker.Faker;
import cn.orionsec.kit.generator.name.NameGenerator;
import cn.orionsec.kit.generator.idcard.IdCardGenerator;

// Generate complete fake profile
FakerInfo info = Faker.generator(FakerType.ALL);

// Individual generators
NameGenerator.generatorName()           // random Chinese name
IdCardGenerator.generator()             // random ID card
MobileGenerator.generateMobile()        // random phone
EmailGenerator.generatorEmail()         // random email
AddressGenerator.generatorAddress()     // random address
```

## API References (Markdown)

Full API documentation in searchable Markdown format. Use Grep to search for specific classes or methods:

```bash
# Search for a specific class
Grep pattern="class Strings" path="references/"

# Search for a specific method
Grep pattern="static.*isBlank" path="references/"

# Find all methods in a class
Read file="references/orion-lang/cn/orionsec/kit/lang/utils/Strings.md"
```

Module index files:
- [orion-lang](references/orion-lang/) - Core utilities
- [orion-ext](references/orion-ext/) - Extensions
- [orion-office](references/orion-office/) - CSV and Excel
- [orion-http](references/orion-http/) - HTTP clients
- [orion-net](references/orion-net/) - SSH, SFTP, FTP
- [orion-web](references/orion-net/) - Servlet
- [orion-spring](references/orion-net/) - Spring containers
- [orion-generator](references/orion-generator/) - Random generators

