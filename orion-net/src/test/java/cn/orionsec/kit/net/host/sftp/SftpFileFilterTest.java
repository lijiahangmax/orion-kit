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
package cn.orionsec.kit.net.host.sftp;

import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Pattern;

/**
 * SftpFileFilter 单元测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class SftpFileFilterTest {

    private SftpFile fileOf(String path) {
        SftpFile file = new SftpFile();
        file.setPath(path);
        return file;
    }

    @Test
    public void testSuffixFilter() {
        SftpFileFilter filter = SftpFileFilter.suffix(".txt");
        Assert.assertTrue(filter.test(this.fileOf("/home/a.txt")));
        Assert.assertFalse(filter.test(this.fileOf("/home/a.log")));
    }

    @Test
    public void testSuffixFilterIgnoreCase() {
        SftpFileFilter filter = SftpFileFilter.suffix(".TXT");
        Assert.assertTrue(filter.test(this.fileOf("/home/a.txt")));
        Assert.assertTrue(filter.test(this.fileOf("/home/A.TXT")));
    }

    @Test
    public void testContainsFilter() {
        SftpFileFilter filter = SftpFileFilter.contains("log");
        Assert.assertTrue(filter.test(this.fileOf("/var/mylog.txt")));
        Assert.assertTrue(filter.test(this.fileOf("/var/LOG-2023.gz")));
        Assert.assertFalse(filter.test(this.fileOf("/var/data.txt")));
    }

    @Test
    public void testContainsFilterOnlyMatchName() {
        // 仅匹配文件名 不匹配父目录
        SftpFileFilter filter = SftpFileFilter.contains("var");
        Assert.assertFalse(filter.test(this.fileOf("/var/data.txt")));
    }

    @Test
    public void testMatchesFilter() {
        SftpFileFilter filter = SftpFileFilter.matches(Pattern.compile("\\d+\\.log"));
        Assert.assertTrue(filter.test(this.fileOf("/var/20231117.log")));
        Assert.assertFalse(filter.test(this.fileOf("/var/app.log")));
        Assert.assertFalse(filter.test(this.fileOf("/var/123.txt")));
    }

    @Test
    public void testLambdaFilter() {
        SftpFileFilter filter = file -> file.getSize() > 100;
        SftpFile big = this.fileOf("/a");
        big.setSize(1024);
        SftpFile small = this.fileOf("/b");
        small.setSize(10);
        Assert.assertTrue(filter.test(big));
        Assert.assertFalse(filter.test(small));
    }

    @Test
    public void testPredicateCompose() {
        // 继承 Predicate 支持 and / negate
        SftpFileFilter suffix = SftpFileFilter.suffix(".log");
        SftpFileFilter contains = SftpFileFilter.contains("app");
        Assert.assertTrue(suffix.and(contains).test(this.fileOf("/var/app-error.log")));
        Assert.assertFalse(suffix.and(contains).test(this.fileOf("/var/sys-error.log")));
        Assert.assertTrue(suffix.negate().test(this.fileOf("/var/app.txt")));
    }

}
