# AGENTS.md

## Project Overview

orion-kit is a Java utility library (`cn.orionsec.kit`) targeting JDK 8+, published to Maven Central under group `cn.orionsec.kit`. Current version: `2.0.7`.
Licensed under MIT.

## Build Commands

```bash
# Full build (compile + javadoc, skip tests)
mvn clean install -DskipTests

# Build skipping javadoc generation
mvn clean install -DskipTests -P skip-docs

# Run all tests
mvn test

# Run a single test class
mvn test -pl orion-lang -Dtest=cn.orionsec.kit.test.encrypt.AesTests

# Generate aggregated javadoc
mvn javadoc:aggregate -P !skip-docs

# Deploy release to Maven Central (requires GPG + Sonatype credentials)
mvn -U clean deploy -P release -P !skip-docs -DskipTests
```

## Module Dependency Graph

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

All modules share a single parent POM at root. `orion-lang` is the foundation; every other module depends on it.

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

The library uses a `KitConfig` static registry for configurable defaults (patterns, codes, limits). Each module has a `Kit{Module}Configuration` class that
initializes these defaults in static blocks.

### Key Dependencies

| Library                                 | Usage                                 |
|-----------------------------------------|---------------------------------------|
| fastjson 2.x                            | JSON serialization                    |
| Bouncy Castle                           | Extended crypto (SM4, etc.)           |
| Apache Commons (lang3, compress, codec) | String/compression/encoding utilities |
| dom4j                                   | XML processing                        |
| SnakeYAML                               | YAML processing                       |
| POI 4.1.2                               | Excel read/write                      |
| JSch (mwiede fork) 2.x                  | SSH/SFTP                              |
| JGit                                    | Git operations                        |
| OkHttp 3.x / HttpClient 4.x             | HTTP clients                          |
| Jsoup                                   | HTML parsing                          |

### Java Version

Source and target: Java 8. Do not use Java 9+ APIs.

### Editor Config

- UTF-8 encoding, spaces for indentation, LF line endings (CRLF for `.cmd`/`.bat`/`.ps1`)
- YAML files use indent size 2
