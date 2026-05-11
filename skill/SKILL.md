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

| Module          | ArtifactId        | Description                                                                  |
|-----------------|-------------------|------------------------------------------------------------------------------|
| orion-lang      | `orion-lang`      | Core: strings, collections, IO, crypto, dates, reflection, encoding, threads |
| orion-ext       | `orion-ext`       | Extensions: IP geolocation, email, process, file tailing, git                |
| orion-office    | `orion-office`    | CSV and Excel import/export                                                  |
| orion-http      | `orion-http`      | OkHttp, HttpClient, Jsoup wrappers                                           |
| orion-net       | `orion-net`       | SSH, SFTP, FTP, TCP/UDP socket                                               |
| orion-generator | `orion-generator` | Random data generators (names, addresses, IDs, etc.)                         |
| orion-web       | `orion-web`       | Servlet utilities                                                            |
| orion-spring    | `orion-spring`    | Spring container utilities                                                   |

## Naming Convention

Utility classes follow the pattern `职能 + s`. If a JDK class exists, suffix with `1`:

| Need              | Class      |
|-------------------|------------|
| String operations | `Strings`  |
| List operations   | `Lists`    |
| Map operations    | `Maps`     |
| Date operations   | `Dates`    |
| File operations   | `Files1`   |
| Stream operations | `Streams`  |
| Object operations | `Objects1` |
| Array operations  | `Arrays1`  |

## Quick Reference by Use Case

### String Processing

```text
import cn.orionsec.kit.lang.utils.Strings;

Strings.isBlank(null)          // true
Strings.isNotBlank("hello")   // true
Strings.ifBlank(str, "default")
Strings.format("Hello {}", map)
Strings.join(list, ",")
Strings.omit("longtext", 10)  // truncate with "..."
```

### Collection Operations

```text
import cn.orionsec.kit.lang.utils.collect.*;

Lists.of(1, 2, 3)
Lists.partition(list, 100)    // split into chunks
Lists.map(list, mapper)       // transform
Maps.of("key", "value")
Maps.isEmpty(map)
Collections.diff(set1, set2)  // set difference
```

### Date/Time

```text
import cn.orionsec.kit.lang.utils.time.Dates;

Dates.format(new Date(), "yyyy-MM-dd")
Dates.parse("2024-01-01", "yyyy-MM-dd")
Dates.ago(date)               // "3 hours ago"
Dates.isLeapYear()
```

### File Operations

```text
import cn.orionsec.kit.lang.utils.io.Files1;

Files1.touch("/path/file.txt")
Files1.copy(src, dest)
Files1.readAllLines(file)     // via FileReaders
Files1.getSize(file)
Files1.md5(file)
```

### IO / Streams

```text
import cn.orionsec.kit.lang.utils.io.Streams;

Streams.toByteArray(inputStream)
Streams.toString(inputStream, "UTF-8")
Streams.transfer(input, output)
Streams.close(closeable)
```

### Cryptography

```text
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

## API References (Markdown)

Full API documentation in searchable Markdown format under `~/.claude/skills/orion-kit-docs/references/`.

### How to Find a Class

The file path follows Java package convention: `references/{module}/{package/path}/{ClassName}.md`

| Class                                             | File Path                                                                    |
|---------------------------------------------------|------------------------------------------------------------------------------|
| `cn.orionsec.kit.lang.utils.Strings`              | `references/orion-lang/cn/orionsec/kit/lang/utils/Strings.md`                |
| `cn.orionsec.kit.lang.utils.crypto.AES`           | `references/orion-lang/cn/orionsec/kit/lang/utils/crypto/AES.md`             |
| `cn.orionsec.kit.net.host.ssh.SshExecutor`        | `references/orion-net/cn/orionsec/kit/net/host/ssh/SshExecutor.md`           |
| `cn.orionsec.kit.office.csv.reader.CsvBeanReader` | `references/orion-office/cn/orionsec/kit/office/csv/reader/CsvBeanReader.md` |

### Search Patterns

When the user asks about a class or method, use these patterns:

**Find a class by name:**

```
Glob pattern="**/{ClassName}.md" path="~/.claude/skills/orion-kit-docs/references"
```

**Search for a method across all classes:**

```
Grep pattern="methodName" path="~/.claude/skills/orion-kit-docs/references" glob="*.md"
```

**Search for classes in a specific domain:**

```
Grep pattern="# ClassName" path="~/.claude/skills/orion-kit-docs/references/{module}" glob="*.md"
```

**Read a specific class API:**

```
Read file="~/.claude/skills/orion-kit-docs/references/{module}/{package/path}/{ClassName}.md"
```

### Module Index

- `references/orion-lang/` - Core: strings, collections, IO, crypto, dates, reflection, encoding, threads
- `references/orion-ext/` - Extensions: IP geolocation, email, process, file tailing, git
- `references/orion-office/` - CSV and Excel import/export
- `references/orion-http/` - OkHttp, HttpClient, Jsoup wrappers
- `references/orion-net/` - SSH, SFTP, FTP, TCP/UDP socket
- `references/orion-web/` - Servlet utilities
- `references/orion-spring/` - Spring container utilities
- `references/orion-generator/` - Random data generators (names, addresses, IDs, etc.)

