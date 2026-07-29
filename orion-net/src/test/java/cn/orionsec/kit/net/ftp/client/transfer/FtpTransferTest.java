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
package cn.orionsec.kit.net.ftp.client.transfer;

import cn.orionsec.kit.net.ftp.client.Ftps;
import cn.orionsec.kit.net.ftp.client.config.FtpConfig;
import cn.orionsec.kit.net.ftp.client.instance.FtpInstance;
import cn.orionsec.kit.net.ftp.server.FtpServerInstance;
import cn.orionsec.kit.net.ftp.server.FtpUser;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * FtpUploader / FtpDownloader 集成测试 (内嵌 FTP 服务器)
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class FtpTransferTest {

    private static final String USERNAME = "orion";

    private static final String PASSWORD = "123456";

    @ClassRule
    public static final TemporaryFolder HOME = new TemporaryFolder();

    @ClassRule
    public static final TemporaryFolder LOCAL = new TemporaryFolder();

    private static FtpServerInstance server;

    private static FtpInstance instance;

    @BeforeClass
    public static void startServer() throws IOException {
        int port;
        try (ServerSocket socket = new ServerSocket(0)) {
            port = socket.getLocalPort();
        }
        FtpUser user = new FtpUser(USERNAME, PASSWORD, HOME.getRoot().getAbsolutePath());
        user.setWritePermission(true);
        server = new FtpServerInstance(port)
                .addUser(user)
                .listener()
                .start();
        FtpConfig config = new FtpConfig("127.0.0.1", port).auth(USERNAME, PASSWORD);
        instance = (FtpInstance) Ftps.createInstance(config);
    }

    @AfterClass
    public static void stopServer() {
        if (instance != null) {
            instance.close();
        }
        if (server != null) {
            server.stop();
        }
    }

    /**
     * 生成测试数据
     */
    private static byte[] createData(int size) {
        byte[] data = new byte[size];
        Arrays.fill(data, (byte) 'x');
        return data;
    }

    @Test(timeout = 30000)
    public void testUpload() throws IOException {
        byte[] data = createData(20000);
        File localFile = new File(LOCAL.getRoot(), "upload-src.txt");
        Files.write(localFile.toPath(), data);

        FtpUploader uploader = new FtpUploader(instance, "/transfer/up.txt", localFile);
        assertSame(instance, uploader.getInstance());
        uploader.run();

        // 校验远程文件
        assertEquals(20000L, instance.getSize("/transfer/up.txt"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        instance.transfer("/transfer/up.txt", out);
        assertArrayEquals(data, out.toByteArray());
    }

    @Test(timeout = 30000)
    public void testUploadByLocalPath() throws IOException {
        File localFile = new File(LOCAL.getRoot(), "upload-path.txt");
        Files.write(localFile.toPath(), "path-upload".getBytes(StandardCharsets.UTF_8));

        FtpUploader uploader = new FtpUploader(instance, "/transfer/up-path.txt", localFile.getAbsolutePath());
        uploader.run();
        assertEquals(11L, instance.getSize("/transfer/up-path.txt"));
    }

    @Test(timeout = 30000)
    public void testDownload() throws IOException {
        byte[] data = createData(20000);
        instance.write("/transfer/down.txt", data);

        File localFile = new File(LOCAL.getRoot(), "download-dst.txt");
        FtpDownloader downloader = new FtpDownloader(instance, "/transfer/down.txt", localFile);
        assertSame(instance, downloader.getInstance());
        downloader.run();

        // 校验本地文件
        assertArrayEquals(data, Files.readAllBytes(localFile.toPath()));
    }

    @Test(timeout = 30000)
    public void testDownloadByLocalPath() throws IOException {
        instance.write("/transfer/down-path.txt", "path-download");

        File localFile = new File(LOCAL.getRoot(), "download-path.txt");
        FtpDownloader downloader = new FtpDownloader(instance, "/transfer/down-path.txt", localFile.getAbsolutePath());
        downloader.run();
        assertEquals("path-download", new String(Files.readAllBytes(localFile.toPath()), StandardCharsets.UTF_8));
    }

    @Test(timeout = 30000)
    public void testDownloadNotFound() {
        File localFile = new File(LOCAL.getRoot(), "download-none.txt");
        FtpDownloader downloader = new FtpDownloader(instance, "/transfer/none.txt", localFile);
        // 远程文件不存在报错
        assertThrows(RuntimeException.class, downloader::run);
    }

}
