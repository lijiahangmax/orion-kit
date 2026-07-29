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
package cn.orionsec.kit.net.ftp.clint;

import cn.orionsec.kit.net.ftp.client.Ftps;
import cn.orionsec.kit.net.ftp.client.config.FtpConfig;
import cn.orionsec.kit.net.ftp.client.instance.IFtpInstance;
import cn.orionsec.kit.net.ftp.server.FtpServerInstance;
import cn.orionsec.kit.net.ftp.server.FtpUser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * FTP 客户端测试
 * <p>
 * 使用 orion 内嵌 Apache FtpServer 启动本地 FTP 服务, 再用 orion FtpClient 连接操作, 自包含无需外部服务器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/11 11:36
 */
public class FtpClintTests {

    private static final String USER = "user";

    private static final String PASSWORD = "123";

    private FtpServerInstance server;

    private IFtpInstance e;

    private File home;

    @Before
    public void init() throws Exception {
        int port = freePort();
        this.home = Files.createTempDirectory("orion-ftp-home").toFile();
        FtpUser user = new FtpUser();
        user.setUsername(USER);
        user.setPassword(PASSWORD);
        user.setHomePath(home.getAbsolutePath());
        user.setWritePermission(true);
        this.server = new FtpServerInstance()
                .host("127.0.0.1")
                .port(port)
                .addUser(user)
                .listener();
        this.server.start();
        FtpConfig config = new FtpConfig("127.0.0.1", port).auth(USER, PASSWORD);
        this.e = Ftps.createInstance(config);
    }

    @After
    public void destroy() {
        if (e != null) {
            e.close();
        }
        if (server != null) {
            server.stop();
        }
    }

    @Test
    public void testTouchAndExist() {
        e.touch("/a.txt");
        Assert.assertTrue(e.isExist("/a.txt"));
        Assert.assertFalse(e.isExist("/not-exist.txt"));
    }

    @Test
    public void testMakeDirectories() {
        e.makeDirectories("/d1/d2/d3");
        e.touch("/d1/d2/d3/x.txt");
        Assert.assertTrue(e.isExist("/d1/d2/d3/x.txt"));
    }

    @Test
    public void testListFiles() {
        e.touch("/list/1.txt");
        e.touch("/list/2.txt");
        Assert.assertEquals(2, e.listFiles("/list").size());
    }

    @Test
    public void testUploadDownload() throws Exception {
        File local = File.createTempFile("orion-ftp-up", ".txt");
        Files.write(local.toPath(), "ftp-content".getBytes(StandardCharsets.UTF_8));
        e.upload("/up/file.txt", local).run();
        Assert.assertTrue(e.isExist("/up/file.txt"));

        File download = File.createTempFile("orion-ftp-down", ".txt");
        e.download("/up/file.txt", download).run();
        String content = new String(Files.readAllBytes(download.toPath()), StandardCharsets.UTF_8);
        Assert.assertEquals("ftp-content", content);
    }

    private static int freePort() throws Exception {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

}
