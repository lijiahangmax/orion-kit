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

import cn.orionsec.kit.lang.define.StopWatch;
import cn.orionsec.kit.lang.utils.io.FileReaders;
import cn.orionsec.kit.lang.utils.io.FileWriters;
import cn.orionsec.kit.lang.utils.io.Files1;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 添加 license 头
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/10/11 12:55
 */
public class AddLicenseHeader {

    private static final String LICENSE = "/*\n" +
            " * Copyright (c) 2019 - present Jiahang Li, All rights reserved.\n" +
            " *\n" +
            " *   https://kit.orionsec.cn\n" +
            " *\n" +
            " * Members:\n" +
            " *   Jiahang Li - ljh1553488six@139.com - author\n" +
            " *\n" +
            " * The MIT License (MIT)\n" +
            " * Permission is hereby granted, free of charge, to any person obtaining a copy of\n" +
            " * this software and associated documentation files (the \"Software\"), to deal in\n" +
            " * the Software without restriction, including without limitation the rights to\n" +
            " * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of\n" +
            " * the Software, and to permit persons to whom the Software is furnished to do so,\n" +
            " * subject to the following conditions:\n" +
            " *\n" +
            " * The above copyright notice and this permission notice shall be included in all\n" +
            " * copies or substantial portions of the Software.\n" +
            " *\n" +
            " * THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
            " * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS\n" +
            " * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR\n" +
            " * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER\n" +
            " * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN\n" +
            " * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.\n" +
            " */";

    private static final String PATH = new File("").getAbsolutePath();

    public static void main(String[] args) {
        StopWatch sw = StopWatch.begin();
        // 扫描文件
        List<File> files = Files1.listFilesFilter(PATH, file -> file.isFile()
                && (file.getName().endsWith(".java") || file.getName().endsWith(".java.vm"))
                && !file.getAbsolutePath().contains("generated-sources"), true, false);
        sw.tag("list");
        // 添加头
        files.forEach(AddLicenseHeader::addLicenseToFile);
        sw.tag(" add");
        sw.stop();
        System.out.println();
        System.out.println(sw);
    }

    /**
     * 添加 license
     *
     * @param file file
     */
    private static void addLicenseToFile(File file) {
        String path = file.getAbsolutePath().substring(PATH.length());
        try {
            String line = FileReaders.readLine(file);
            if (line != null && line.trim().equals("/*")) {
                System.out.println("Exists " + path);
                return;
            }
            // 读取原始文件内容
            byte[] bytes = FileReaders.readAllBytesFast(file);
            // 在头部添加许可证
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write(LICENSE.getBytes(StandardCharsets.UTF_8));
            out.write('\n');
            out.write(new String(bytes).replaceAll("\r\n", "\n").getBytes(StandardCharsets.UTF_8));
            // 写入
            FileWriters.writeFast(file, out.toByteArray());
            System.out.println("Added  " + path);
        } catch (IOException e) {
            System.err.println("Failed " + path);
        }
    }

}
