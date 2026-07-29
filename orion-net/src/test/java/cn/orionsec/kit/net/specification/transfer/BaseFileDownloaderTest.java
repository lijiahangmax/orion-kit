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
package cn.orionsec.kit.net.specification.transfer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * BaseFileDownloader 测试 (基于内存实现的匿名子类)
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class BaseFileDownloaderTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    /**
     * 构建测试数据
     */
    private static byte[] buildData(int size) {
        byte[] data = new byte[size];
        for (int i = 0; i < size; i++) {
            data[i] = (byte) (i % 127);
        }
        return data;
    }

    /**
     * 基于内存的下载器实现
     */
    private static class TestDownloader extends BaseFileDownloader {

        private final byte[] data;

        private ByteArrayInputStream in;

        private boolean initCalled;

        private boolean breakPoint;

        private long skip;

        private boolean finishCalled;

        private int remoteSizeCalls;

        private TestDownloader(byte[] data, String remote, File local) {
            super(remote, local, "lock", 1024);
            this.data = data;
        }

        @Override
        protected long getRemoteFileSize() {
            remoteSizeCalls++;
            return data == null ? -1 : data.length;
        }

        @Override
        protected void initDownload(boolean breakPoint, long skip) throws IOException {
            this.initCalled = true;
            this.breakPoint = breakPoint;
            this.skip = skip;
            this.in = new ByteArrayInputStream(data);
            long ignore = in.skip(skip);
        }

        @Override
        protected int read(byte[] bs) {
            return in.read(bs, 0, bs.length);
        }

        @Override
        protected void transferFinish() {
            this.finishCalled = true;
        }

        @Override
        public void run() {
            try {
                this.startDownload();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public void abort() {
        }

        @Override
        public void close() {
        }
    }

    @Test
    public void testConstructorValidation() throws IOException {
        File local = folder.newFile();
        // remote 为空
        try {
            new TestDownloader(new byte[0], "", local);
            fail("expect exception");
        } catch (RuntimeException e) {
            // ignore
        }
        // local 为 null
        try {
            new TestDownloader(new byte[0], "/remote/file", null);
            fail("expect exception");
        } catch (RuntimeException e) {
            // ignore
        }
    }

    @Test
    public void testGetRemoteFileLengthCached() throws IOException {
        File local = new File(folder.getRoot(), "cache-target");
        TestDownloader downloader = new TestDownloader(buildData(10), "/remote/file", local);
        assertEquals(10, downloader.getRemoteFileLength());
        assertEquals(10, downloader.getRemoteFileLength());
        // 第二次走缓存
        assertEquals(1, downloader.remoteSizeCalls);
    }

    @Test
    public void testCheckRemoteFilePresentSizeEqual() throws IOException {
        File local = folder.newFile();
        Files.write(local.toPath(), buildData(10));
        // 大小相同
        assertTrue(new TestDownloader(buildData(10), "/remote/file", local).checkRemoteFilePresentSizeEqual());
        // 大小不同
        assertFalse(new TestDownloader(buildData(20), "/remote/file", local).checkRemoteFilePresentSizeEqual());
        // 远程文件不存在 (-1)
        assertTrue(new TestDownloader(null, "/remote/file", local).checkRemoteFilePresentSizeEqual());
    }

    @Test
    public void testDownloadNewFile() throws IOException {
        byte[] data = buildData(3000);
        File local = new File(folder.getRoot(), "download-target");
        TestDownloader downloader = new TestDownloader(data, "/remote/file", local);
        downloader.run();
        assertTrue(downloader.initCalled);
        assertFalse(downloader.breakPoint);
        assertEquals(0, downloader.skip);
        assertTrue(downloader.finishCalled);
        assertArrayEquals(data, Files.readAllBytes(local.toPath()));
        // 进度条
        assertEquals(data.length, downloader.getProgress().getCurrent());
        // 锁已释放
        assertFalse(downloader.lock.isLocked());
    }

    @Test
    public void testSkipWhenFileSizeEqual() throws IOException {
        byte[] data = buildData(100);
        File local = folder.newFile();
        Files.write(local.toPath(), data);
        TestDownloader downloader = new TestDownloader(data, "/remote/file", local);
        downloader.run();
        // 大小相同 跳过下载
        assertFalse(downloader.initCalled);
        assertTrue(downloader.finishCalled);
        assertArrayEquals(data, Files.readAllBytes(local.toPath()));
    }

    @Test
    public void testFileSizeEqualOverride() throws IOException {
        byte[] data = buildData(100);
        File local = folder.newFile();
        // 大小相同 内容不同
        Files.write(local.toPath(), new byte[100]);
        TestDownloader downloader = new TestDownloader(data, "/remote/file", local);
        downloader.fileSizeEqualOverride(true);
        downloader.run();
        // 重新下载
        assertTrue(downloader.initCalled);
        assertFalse(downloader.breakPoint);
        assertArrayEquals(data, Files.readAllBytes(local.toPath()));
    }

    @Test
    public void testForceOverride() throws IOException {
        byte[] data = buildData(200);
        File local = folder.newFile();
        Files.write(local.toPath(), buildData(50));
        TestDownloader downloader = new TestDownloader(data, "/remote/file", local);
        // 创建锁文件 (若非强制覆盖则会走断点续传)
        assertTrue(downloader.lock.tryLock());
        downloader.forceOverride(true);
        downloader.run();
        // 强制覆盖 不走断点续传
        assertTrue(downloader.initCalled);
        assertFalse(downloader.breakPoint);
        assertEquals(0, downloader.skip);
        assertArrayEquals(data, Files.readAllBytes(local.toPath()));
        assertFalse(downloader.lock.isLocked());
    }

    @Test
    public void testBreakPointResume() throws IOException {
        byte[] data = buildData(200);
        File local = folder.newFile();
        // 本地已下载前 80 字节
        Files.write(local.toPath(), Arrays.copyOfRange(data, 0, 80));
        TestDownloader downloader = new TestDownloader(data, "/remote/file", local);
        // 锁定 触发断点续传
        assertTrue(downloader.lock.tryLock());
        downloader.run();
        assertTrue(downloader.initCalled);
        assertTrue(downloader.breakPoint);
        assertEquals(80, downloader.skip);
        assertArrayEquals(data, Files.readAllBytes(local.toPath()));
        assertEquals(data.length, downloader.getProgress().getCurrent());
        assertFalse(downloader.lock.isLocked());
    }

}
