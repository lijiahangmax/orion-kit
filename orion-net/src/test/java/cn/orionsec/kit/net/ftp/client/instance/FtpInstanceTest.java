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
package cn.orionsec.kit.net.ftp.client.instance;

import cn.orionsec.kit.net.ftp.client.FtpFile;
import cn.orionsec.kit.net.ftp.client.FtpFileFilter;
import cn.orionsec.kit.net.ftp.client.Ftps;
import cn.orionsec.kit.net.ftp.client.config.FtpConfig;
import cn.orionsec.kit.net.ftp.server.FtpServerInstance;
import cn.orionsec.kit.net.ftp.server.FtpUser;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.*;

/**
 * FtpInstance 集成测试 (内嵌 FTP 服务器)
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class FtpInstanceTest {

    private static final String USERNAME = "orion";

    private static final String PASSWORD = "123456";

    @ClassRule
    public static final TemporaryFolder HOME = new TemporaryFolder();

    @ClassRule
    public static final TemporaryFolder LOCAL = new TemporaryFolder();

    private static int port;

    private static FtpServerInstance server;

    private static IFtpInstance instance;

    @BeforeClass
    public static void startServer() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            port = socket.getLocalPort();
        }
        FtpUser user = new FtpUser(USERNAME, PASSWORD, HOME.getRoot().getAbsolutePath());
        user.setWritePermission(true);
        user.setMaxIdleTime(300);
        server = new FtpServerInstance(port)
                .addUser(user)
                .listener()
                .start();
        FtpConfig config = new FtpConfig("127.0.0.1", port).auth(USERNAME, PASSWORD);
        instance = Ftps.createInstance(config);
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

    @Test(timeout = 30000)
    public void testConnectAndReply() throws IOException {
        assertTrue(instance.sendNoop());
        assertTrue(instance.reply());
        assertTrue(instance.replyCode() >= 200);
        instance.change();
        assertEquals("/", instance.getWorkDirectory());
        assertNotNull(instance.getSystemType());
        assertNotNull(instance.getStatus());
        assertNotNull(instance.getStatus("/"));
    }

    @Test(timeout = 30000)
    public void testWriteAndRead() throws IOException {
        instance.write("/rw/test.txt", "hello world");
        assertTrue(instance.isExist("/rw/test.txt"));
        assertEquals(11L, instance.getSize("/rw/test.txt"));

        // 读取到数组
        byte[] bs = new byte[32];
        int read = instance.read("/rw/test.txt", bs);
        assertEquals("hello world", new String(bs, 0, read, StandardCharsets.UTF_8));

        // 跳过读取
        byte[] skipBs = new byte[32];
        int skipRead = instance.read("/rw/test.txt", 6L, skipBs);
        assertEquals("world", new String(skipBs, 0, skipRead, StandardCharsets.UTF_8));
    }

    @Test(timeout = 30000)
    public void testWriteBytesAndStream() throws IOException {
        instance.write("/wb/bytes.txt", "orion-kit".getBytes(StandardCharsets.UTF_8));
        assertEquals(9L, instance.getSize("/wb/bytes.txt"));

        instance.write("/wb/stream.txt", new ByteArrayInputStream("stream".getBytes(StandardCharsets.UTF_8)));
        assertEquals(6L, instance.getSize("/wb/stream.txt"));
    }

    @Test(timeout = 30000)
    public void testAppend() throws IOException {
        instance.write("/ap/a.txt", "hello");
        instance.append("/ap/a.txt", " world");
        assertEquals(11L, instance.getSize("/ap/a.txt"));

        instance.append("/ap/a.txt", new ByteArrayInputStream("!".getBytes(StandardCharsets.UTF_8)));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        instance.transfer("/ap/a.txt", out);
        assertEquals("hello world!", new String(out.toByteArray(), StandardCharsets.UTF_8));
    }

    @Test(timeout = 30000)
    public void testTransfer() throws IOException {
        instance.write("/tf/data.txt", "0123456789");

        // 全部读取
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        long len = instance.transfer("/tf/data.txt", out);
        assertEquals(10L, len);
        assertEquals("0123456789", new String(out.toByteArray(), StandardCharsets.UTF_8));

        // 跳过读取
        ByteArrayOutputStream skipOut = new ByteArrayOutputStream();
        instance.transfer("/tf/data.txt", skipOut, 5L);
        assertEquals("56789", new String(skipOut.toByteArray(), StandardCharsets.UTF_8));

        // 限制长度读取
        ByteArrayOutputStream sizeOut = new ByteArrayOutputStream();
        instance.transfer("/tf/data.txt", sizeOut, 0L, 5);
        assertEquals("01234", new String(sizeOut.toByteArray(), StandardCharsets.UTF_8));

        // 读取到本地文件
        File localFile = new File(LOCAL.getRoot(), "transfer.txt");
        instance.transfer("/tf/data.txt", localFile);
        assertEquals("0123456789", new String(Files.readAllBytes(localFile.toPath()), StandardCharsets.UTF_8));
    }

    @Test(timeout = 30000)
    public void testOpenStream() throws IOException {
        instance.write("/st/s.txt", "stream-data");
        InputStream in = null;
        try {
            in = instance.openInputStream("/st/s.txt");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] bs = new byte[16];
            int read;
            while ((read = in.read(bs)) != -1) {
                out.write(bs, 0, read);
            }
            assertEquals("stream-data", new String(out.toByteArray(), StandardCharsets.UTF_8));
        } finally {
            if (in != null) {
                in.close();
                instance.pending();
            }
        }

        OutputStream out = null;
        try {
            out = instance.openOutputStream("/st/o.txt");
            out.write("out-data".getBytes(StandardCharsets.UTF_8));
            out.flush();
        } finally {
            if (out != null) {
                out.close();
                instance.pending();
            }
        }
        assertEquals(8L, instance.getSize("/st/o.txt"));
    }

    @Test(timeout = 30000)
    public void testOpenInputStreamNotFound() {
        assertThrows(IOException.class, () -> instance.openInputStream("/notfound/xxx.txt"));
    }

    @Test(timeout = 30000)
    public void testTouchAndRemoveFile() {
        instance.touch("/tc/touch.txt");
        assertTrue(instance.isExist("/tc/touch.txt"));
        // 重复 touch 不报错
        instance.touch("/tc/touch.txt");
        assertEquals(0L, instance.getSize("/tc/touch.txt"));

        instance.removeFile("/tc/touch.txt");
        assertFalse(instance.isExist("/tc/touch.txt"));
    }

    @Test(timeout = 30000)
    public void testMakeDirectoriesAndList() throws IOException {
        instance.makeDirectories("/mk/d1/d2");
        assertTrue(instance.isExist("/mk/d1/d2"));

        instance.write("/mk/d1/f1.txt", "f1");
        instance.write("/mk/d1/d2/f2.log", "f2");

        // 非递归文件
        List<FtpFile> files = instance.listFiles("/mk/d1");
        assertEquals(1, files.size());
        assertEquals("f1.txt", files.get(0).getName());

        // 递归文件
        List<FtpFile> childFiles = instance.listFiles("/mk", true);
        assertEquals(2, childFiles.size());

        // 递归含文件夹
        List<FtpFile> filesWithDir = instance.listFiles("/mk", true, true);
        assertEquals(4, filesWithDir.size());

        // 文件夹列表
        List<FtpFile> dirs = instance.listDirs("/mk");
        assertEquals(1, dirs.size());
        assertTrue(dirs.get(0).isDirectory());
        List<FtpFile> childDirs = instance.listDirs("/mk", true);
        assertEquals(2, childDirs.size());

        // 过滤器
        List<FtpFile> logs = instance.listFilesFilter("/mk", FtpFileFilter.suffix(".log"), true);
        assertEquals(1, logs.size());
        assertEquals("f2.log", logs.get(0).getName());
        List<FtpFile> contains = instance.listFilesFilter("/mk", FtpFileFilter.contains("f1"), true, false);
        assertEquals(1, contains.size());
    }

    @Test(timeout = 30000)
    public void testGetFile() throws IOException {
        instance.write("/gf/a.txt", "a");
        FtpFile file = instance.getFile("/gf/a.txt");
        assertNotNull(file);
        assertEquals("a.txt", file.getName());
        assertTrue(file.isFile());
        assertNotNull(file.getModifyTime());
        assertNotNull(file.getPermissionString());

        // 不存在的文件
        assertNull(instance.getFile("/gf/none.txt"));
        assertEquals(-1L, instance.getSize("/gf/none.txt"));
        assertFalse(instance.isExist("/gf/none.txt"));
    }

    @Test(timeout = 30000)
    public void testMove() throws IOException {
        instance.write("/mv/a.txt", "move");
        // 重命名
        instance.move("/mv/a.txt", "b.txt");
        assertFalse(instance.isExist("/mv/a.txt"));
        assertTrue(instance.isExist("/mv/b.txt"));

        // 绝对路径移动
        instance.move("/mv/b.txt", "/mv2/c.txt");
        assertFalse(instance.isExist("/mv/b.txt"));
        assertTrue(instance.isExist("/mv2/c.txt"));
    }

    @Test(timeout = 30000)
    public void testRemove() throws IOException {
        // 删除文件
        instance.write("/rm/f.txt", "rm");
        instance.remove("/rm/f.txt");
        assertFalse(instance.isExist("/rm/f.txt"));

        // 递归删除文件夹
        instance.write("/rm/dir/d1/f1.txt", "f1");
        instance.write("/rm/dir/f2.txt", "f2");
        instance.remove("/rm/dir");
        assertFalse(instance.isExist("/rm/dir"));

        // 删除不存在的文件不报错
        instance.remove("/rm/none.txt");
        instance.removeDir("/rm/none-dir");
    }

    @Test(timeout = 30000)
    public void testTruncate() throws IOException {
        instance.write("/tr/t.txt", "truncate-data");
        assertEquals(13L, instance.getSize("/tr/t.txt"));
        instance.truncate("/tr/t.txt");
        assertEquals(0L, instance.getSize("/tr/t.txt"));
    }

    @Test(timeout = 30000)
    public void testUploadAndDownloadFile() throws IOException {
        // 上传
        File localFile = new File(LOCAL.getRoot(), "upload.txt");
        Files.write(localFile.toPath(), "upload-content".getBytes(StandardCharsets.UTF_8));
        instance.uploadFile("/ud/upload.txt", localFile);
        assertEquals(14L, instance.getSize("/ud/upload.txt"));

        // 流上传
        instance.uploadFile("/ud/upload2.txt", new ByteArrayInputStream("up2".getBytes(StandardCharsets.UTF_8)), true);
        assertEquals(3L, instance.getSize("/ud/upload2.txt"));

        // 下载
        File downloadFile = new File(LOCAL.getRoot(), "download.txt");
        instance.downloadFile("/ud/upload.txt", downloadFile);
        assertEquals("upload-content", new String(Files.readAllBytes(downloadFile.toPath()), StandardCharsets.UTF_8));

        // 下载到流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        instance.downloadFile("/ud/upload2.txt", out);
        assertEquals("up2", new String(out.toByteArray(), StandardCharsets.UTF_8));
    }

    @Test(timeout = 30000)
    public void testUploadAndDownloadDir() throws IOException {
        // 构建本地目录
        File srcDir = LOCAL.newFolder("src-dir");
        Files.write(new File(srcDir, "f1.txt").toPath(), "f1".getBytes(StandardCharsets.UTF_8));
        File subDir = new File(srcDir, "sub");
        assertTrue(subDir.mkdirs());
        Files.write(new File(subDir, "f2.txt").toPath(), "f2".getBytes(StandardCharsets.UTF_8));

        // 上传目录
        instance.uploadDir("/ud-dir", srcDir);
        assertTrue(instance.isExist("/ud-dir/f1.txt"));
        assertTrue(instance.isExist("/ud-dir/sub/f2.txt"));

        // 下载目录
        File dstDir = LOCAL.newFolder("dst-dir");
        instance.downloadDir("/ud-dir", dstDir);
        assertEquals("f1", new String(Files.readAllBytes(new File(dstDir, "f1.txt").toPath()), StandardCharsets.UTF_8));
        assertEquals("f2", new String(Files.readAllBytes(new File(dstDir, "sub/f2.txt").toPath()), StandardCharsets.UTF_8));

        // 非递归下载
        File dstDir2 = LOCAL.newFolder("dst-dir2");
        instance.downloadDir("/ud-dir", dstDir2, false);
        assertTrue(new File(dstDir2, "f1.txt").exists());
        assertFalse(new File(dstDir2, "sub/f2.txt").exists());
    }

    @Test(timeout = 30000)
    public void testChangeDirectory() throws IOException {
        instance.makeDirectories("/cd/dir");
        instance.change("/cd/dir");
        assertEquals("/cd/dir", instance.getWorkDirectory());
        // 切换至根目录
        instance.change();
        assertEquals("/", instance.getWorkDirectory());
        // 切换不存在的目录会自动创建
        instance.change("/cd/auto");
        assertTrue(instance.isExist("/cd/auto"));
        instance.change();
    }

    @Test(timeout = 30000)
    public void testOption() throws IOException {
        instance.restartOffset(10L);
        instance.reset();
        assertNotNull(instance.getClient());
        assertNotNull(instance.getConfig());
        assertEquals(port, instance.getConfig().getPort());
        assertNull(instance.getPool());
        assertEquals("/a/b", instance.serverCharset("/a\\b"));
        assertEquals("/a/b", instance.localCharset("/a\\b"));
    }

    @Test(timeout = 30000)
    public void testCreateInstanceLoginError() {
        FtpConfig badConfig = new FtpConfig("127.0.0.1", port).auth("nobody", "wrong");
        assertThrows(RuntimeException.class, () -> Ftps.createInstance(badConfig));
    }

}
