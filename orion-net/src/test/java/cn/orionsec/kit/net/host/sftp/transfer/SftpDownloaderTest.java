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
package cn.orionsec.kit.net.host.sftp.transfer;

import cn.orionsec.kit.lang.exception.argument.InvalidArgumentException;
import cn.orionsec.kit.lang.exception.argument.NullArgumentException;
import cn.orionsec.kit.net.host.sftp.SftpExecutor;
import com.jcraft.jsch.ChannelSftp;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.File;

/**
 * SftpDownloader 单元测试
 * <p>
 * 仅测试对象构建和参数校验 不建立任何真实 SSH 连接
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class SftpDownloaderTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private SftpExecutor executor;

    @Before
    public void setup() {
        this.executor = new SftpExecutor(new ChannelSftp());
    }

    @Test
    public void testConstructor() {
        // 本地文件不要求存在
        File local = new File(folder.getRoot(), "download.txt");
        SftpDownloader downloader = new SftpDownloader(executor, "/remote/file.txt", local);
        Assert.assertSame(executor, downloader.getExecutor());
        Assert.assertNotNull(downloader.getProgress());
    }

    @Test
    public void testConstructorStringLocal() {
        String local = new File(folder.getRoot(), "download.txt").getAbsolutePath();
        SftpDownloader downloader = new SftpDownloader(executor, "/remote/file.txt", local);
        Assert.assertSame(executor, downloader.getExecutor());
    }

    @Test(expected = NullArgumentException.class)
    public void testNullExecutor() {
        new SftpDownloader(null, "/remote/file.txt", new File(folder.getRoot(), "d.txt"));
    }

    @Test(expected = NullArgumentException.class)
    public void testNullRemote() {
        new SftpDownloader(executor, null, new File(folder.getRoot(), "d.txt"));
    }

    @Test(expected = InvalidArgumentException.class)
    public void testEmptyRemote() {
        new SftpDownloader(executor, "", new File(folder.getRoot(), "d.txt"));
    }

    @Test(expected = NullArgumentException.class)
    public void testNullLocal() {
        new SftpDownloader(executor, "/remote/file.txt", (File) null);
    }

    @Test
    public void testCloseSafe() {
        // 未初始化输入流时 close 安全
        File local = new File(folder.getRoot(), "download.txt");
        SftpDownloader downloader = new SftpDownloader(executor, "/remote/file.txt", local);
        downloader.close();
    }

    @Test
    @Ignore("需要真实 SSH 服务器 无法在单元测试中执行下载")
    public void testRun() {
        File local = new File(folder.getRoot(), "download.txt");
        new SftpDownloader(executor, "/remote/file.txt", local).run();
    }

}
