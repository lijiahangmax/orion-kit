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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * BaseFileUploader 测试 (基于内存实现的匿名子类)
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class BaseFileUploaderTest {

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
     * 基于内存的上传器实现
     */
    private static class TestUploader extends BaseFileUploader {

        private final long remoteSize;

        private final ByteArrayOutputStream received = new ByteArrayOutputStream();

        private boolean initCalled;

        private boolean breakPoint;

        private long skip;

        private boolean finishCalled;

        private int remoteSizeCalls;

        private TestUploader(long remoteSize, String remote, File local) {
            super(remote, local, "lock", 1024);
            this.remoteSize = remoteSize;
        }

        @Override
        protected long getRemoteFileSize() {
            remoteSizeCalls++;
            return remoteSize;
        }

        @Override
        protected void initUpload(boolean breakPoint, long skip) {
            this.initCalled = true;
            this.breakPoint = breakPoint;
            this.skip = skip;
        }

        @Override
        protected void write(byte[] bs, int len) {
            received.write(bs, 0, len);
        }

        @Override
        protected void transferFinish() {
            this.finishCalled = true;
        }

        @Override
        public void run() {
            try {
                this.startUpload();
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
            new TestUploader(-1, "", local);
            fail("expect exception");
        } catch (RuntimeException e) {
            // ignore
        }
        // local 为 null
        try {
            new TestUploader(-1, "/remote/file", null);
            fail("expect exception");
        } catch (RuntimeException e) {
            // ignore
        }
        // local 不存在
        try {
            new TestUploader(-1, "/remote/file", new File(folder.getRoot(), "not-exists"));
            fail("expect exception");
        } catch (RuntimeException e) {
            // ignore
        }
        // local 是目录
        try {
            new TestUploader(-1, "/remote/file", folder.newFolder());
            fail("expect exception");
        } catch (RuntimeException e) {
            // ignore
        }
    }

    @Test
    public void testGetRemoteFileLengthCached() throws IOException {
        File local = folder.newFile();
        TestUploader uploader = new TestUploader(10, "/remote/file", local);
        assertEquals(10, uploader.getRemoteFileLength());
        assertEquals(10, uploader.getRemoteFileLength());
        // 第二次走缓存
        assertEquals(1, uploader.remoteSizeCalls);
    }

    @Test
    public void testCheckRemoteFilePresentSizeEqual() throws IOException {
        File local = folder.newFile();
        Files.write(local.toPath(), buildData(10));
        // 大小相同
        assertTrue(new TestUploader(10, "/remote/file", local).checkRemoteFilePresentSizeEqual());
        // 大小不同
        assertFalse(new TestUploader(20, "/remote/file", local).checkRemoteFilePresentSizeEqual());
        // 远程文件不存在 (-1)
        assertTrue(new TestUploader(-1, "/remote/file", local).checkRemoteFilePresentSizeEqual());
    }

    @Test
    public void testUploadWhenRemoteNotExist() throws IOException {
        byte[] data = buildData(3000);
        File local = folder.newFile();
        Files.write(local.toPath(), data);
        TestUploader uploader = new TestUploader(-1, "/remote/file", local);
        uploader.run();
        assertTrue(uploader.initCalled);
        assertFalse(uploader.breakPoint);
        assertEquals(0, uploader.skip);
        assertTrue(uploader.finishCalled);
        assertArrayEquals(data, uploader.received.toByteArray());
        // 进度条
        assertEquals(data.length, uploader.getProgress().getCurrent());
        // 锁已释放
        assertFalse(uploader.lock.isLocked());
    }

    @Test
    public void testSkipWhenFileSizeEqual() throws IOException {
        byte[] data = buildData(100);
        File local = folder.newFile();
        Files.write(local.toPath(), data);
        TestUploader uploader = new TestUploader(100, "/remote/file", local);
        uploader.run();
        // 大小相同 跳过上传
        assertFalse(uploader.initCalled);
        assertTrue(uploader.finishCalled);
        assertEquals(0, uploader.received.size());
    }

    @Test
    public void testFileSizeEqualOverride() throws IOException {
        byte[] data = buildData(100);
        File local = folder.newFile();
        Files.write(local.toPath(), data);
        TestUploader uploader = new TestUploader(100, "/remote/file", local);
        uploader.fileSizeEqualOverride(true);
        uploader.run();
        // 重新上传
        assertTrue(uploader.initCalled);
        assertFalse(uploader.breakPoint);
        assertArrayEquals(data, uploader.received.toByteArray());
    }

    @Test
    public void testForceOverride() throws IOException {
        byte[] data = buildData(100);
        File local = folder.newFile();
        Files.write(local.toPath(), data);
        TestUploader uploader = new TestUploader(100, "/remote/file", local);
        uploader.forceOverride(true);
        uploader.run();
        // 强制覆盖 不检测远程文件大小
        assertEquals(0, uploader.remoteSizeCalls);
        assertTrue(uploader.initCalled);
        assertFalse(uploader.breakPoint);
        assertArrayEquals(data, uploader.received.toByteArray());
    }

    @Test
    public void testBreakPointResume() throws IOException {
        byte[] data = buildData(200);
        File local = folder.newFile();
        Files.write(local.toPath(), data);
        // 远程已上传前 80 字节
        TestUploader uploader = new TestUploader(80, "/remote/file", local);
        // 锁定 触发断点续传
        assertTrue(uploader.lock.tryLock());
        uploader.run();
        assertTrue(uploader.initCalled);
        assertTrue(uploader.breakPoint);
        assertEquals(80, uploader.skip);
        // 仅上传剩余部分
        assertArrayEquals(Arrays.copyOfRange(data, 80, 200), uploader.received.toByteArray());
        assertEquals(data.length, uploader.getProgress().getCurrent());
        assertFalse(uploader.lock.isLocked());
    }

}
