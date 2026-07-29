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
package cn.orionsec.kit.net.ftp.client;

import org.apache.commons.net.ftp.FTPFile;
import org.junit.Test;

import java.util.Calendar;
import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * FtpFileFilter 测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class FtpFileFilterTest {

    /**
     * 构建 FtpFile
     */
    private static FtpFile createFile(String name) {
        FTPFile file = new FTPFile();
        file.setName(name);
        file.setSize(1L);
        file.setType(FTPFile.FILE_TYPE);
        file.setTimestamp(Calendar.getInstance());
        file.setRawListing("-rw-r--r-- 1 root root 1 Mar 12 09:50 " + name);
        return new FtpFile("/" + name, file);
    }

    @Test
    public void testSuffix() {
        FtpFileFilter filter = FtpFileFilter.suffix(".txt");
        assertTrue(filter.test(createFile("a.txt")));
        assertTrue(filter.test(createFile("A.TXT")));
        assertFalse(filter.test(createFile("a.jpg")));
    }

    @Test
    public void testContains() {
        FtpFileFilter filter = FtpFileFilter.contains("log");
        assertTrue(filter.test(createFile("error.log.1")));
        assertTrue(filter.test(createFile("LOG-2024.txt")));
        assertFalse(filter.test(createFile("data.txt")));
    }

    @Test
    public void testMatches() {
        FtpFileFilter filter = FtpFileFilter.matches(Pattern.compile("^\\d+\\.txt$"));
        assertTrue(filter.test(createFile("123.txt")));
        assertFalse(filter.test(createFile("a123.txt")));
    }

    @Test
    public void testLambdaAndPredicate() {
        FtpFileFilter filter = f -> f.getSize() > 0;
        assertTrue(filter.test(createFile("a.txt")));
        // Predicate 组合
        assertFalse(filter.and(f -> f.getName().endsWith(".jpg")).test(createFile("a.txt")));
        assertTrue(filter.negate().test(createFile("a.txt")) == false);
        assertTrue(filter.or(f -> false).test(createFile("a.txt")));
    }

}
