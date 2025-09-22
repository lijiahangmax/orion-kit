/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit;

import cn.orionsec.kit.lang.utils.io.FileReaders;
import cn.orionsec.kit.lang.utils.io.FileWriters;
import cn.orionsec.kit.lang.utils.io.Files1;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

/**
 * 替换版本号
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/12/23 10:21
 */
public class ReplaceVersion {

    private static final String TARGET_VERSION = "2.0.3";

    private static final String REPLACE_VERSION = "2.0.4";

    private static final String PATH = new File("").getAbsolutePath();

    private static final String[] POM_FILES = new String[]{
            "README.md",
            "pom.xml",
            "orion-all/pom.xml",
            "orion-web/pom.xml",
            "orion-ext/pom.xml",
            "orion-spring/pom.xml",
            "orion-redis/pom.xml",
            "orion-office/pom.xml",
            "orion-net/pom.xml",
            "orion-lang/pom.xml",
            "orion-log/pom.xml",
            "orion-http/pom.xml",
            "orion-generator/pom.xml",
    };

    private static final String KIT_VERSION_FILE = "pom.xml";

    private static final String APP_CONST_FILE = "orion-lang/src/main/java/cn/orionsec/kit/lang/constant/OrionConst.java";

    public static void main(String[] args) {
        replacePomFiles();
        replaceKitVersionFile();
        replaceAppConst();
    }

    /**
     * 替换 pom 文件
     */
    private static void replacePomFiles() {
        for (String file : POM_FILES) {
            readAndWrite(file, s -> s.replaceAll("<version>" + TARGET_VERSION + "</version>", "<version>" + REPLACE_VERSION + "</version>"));
        }
    }

    /**
     * 替换 pom kit version 文件
     */
    private static void replaceKitVersionFile() {
        readAndWrite(KIT_VERSION_FILE, s -> s.replaceAll("<orion.kit.version>" + TARGET_VERSION + "</orion.kit.version>", "<orion.kit.version>" + REPLACE_VERSION + "</orion.kit.version>"));
    }

    /**
     * 替换 OrionConst 文件
     */
    private static void replaceAppConst() {
        readAndWrite(APP_CONST_FILE, s -> s.replaceAll("String ORION_KIT_VERSION = \"" + TARGET_VERSION + "\"", "String ORION_KIT_VERSION = \"" + REPLACE_VERSION + "\""));
    }

    /**
     * 读取并且写入
     *
     * @param path    path
     * @param mapping mapping
     */
    private static void readAndWrite(String path, Function<String, String> mapping) {
        String filePath = Files1.getPath(PATH, path);
        try {
            // 读取文件内容
            byte[] bytes = FileReaders.readAllBytesFast(filePath);
            // 写入文件内容
            FileWriters.writeFast(filePath, mapping.apply(new String(bytes)).getBytes(StandardCharsets.UTF_8));
            System.out.println("OK:  " + path);
        } catch (Exception e) {
            System.err.println("ERR: " + path);
        }
    }

}
