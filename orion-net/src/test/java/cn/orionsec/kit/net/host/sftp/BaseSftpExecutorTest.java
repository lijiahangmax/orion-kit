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

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.define.StreamEntry;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.io.FileReaders;
import cn.orionsec.kit.lang.utils.io.FileWriters;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.net.specification.transfer.IFileDownloader;
import cn.orionsec.kit.net.specification.transfer.IFileUploader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.util.*;

/**
 * BaseSftpExecutor 单元测试
 * <p>
 * 使用内存文件系统实现 不建立任何真实 SSH 连接
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class BaseSftpExecutorTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private InMemorySftpExecutor executor;

    @Before
    public void setup() {
        this.executor = new InMemorySftpExecutor();
    }

    /**
     * 内存 sftp 执行器 用于测试基类通用逻辑
     */
    private static class InMemorySftpExecutor extends BaseSftpExecutor {

        private final Map<String, byte[]> files = new LinkedHashMap<>();

        private final Set<String> dirs = new LinkedHashSet<>();

        private InMemorySftpExecutor() {
            this.charset = Const.UTF_8;
        }

        /**
         * 真实 sftp 服务器视 /a/ 与 /a 等价 此处去除尾部分隔符
         */
        private String normalize(String path) {
            String p = Files1.getPath(path);
            if (p.length() > 1 && p.endsWith("/")) {
                return p.substring(0, p.length() - 1);
            }
            return p;
        }

        private SftpFile toSftpFile(String path, boolean dir, long size) {
            SftpFile file = new SftpFile();
            file.setPath(path);
            file.setSize(size);
            file.setPermissionString(dir ? "drwxr-xr-x" : "-rw-r--r--");
            return file;
        }

        @Override
        public SftpFile getFile(String path) {
            return this.getFile(path, false);
        }

        @Override
        public SftpFile getFile(String path, boolean followSymbolic) {
            path = this.normalize(path);
            if (dirs.contains(path)) {
                return this.toSftpFile(path, true, 0);
            }
            byte[] content = files.get(path);
            if (content != null) {
                return this.toSftpFile(path, false, content.length);
            }
            return null;
        }

        @Override
        public List<SftpFile> list(String path) {
            String p = this.normalize(path);
            String prefix = p.endsWith("/") ? p : p + "/";
            List<SftpFile> list = new ArrayList<>();
            for (String dir : dirs) {
                if (dir.startsWith(prefix) && dir.length() > prefix.length()
                        && !dir.substring(prefix.length()).contains("/")) {
                    list.add(this.toSftpFile(dir, true, 0));
                }
            }
            for (Map.Entry<String, byte[]> entry : files.entrySet()) {
                String key = entry.getKey();
                if (key.startsWith(prefix) && !key.substring(prefix.length()).contains("/")) {
                    list.add(this.toSftpFile(key, false, entry.getValue().length));
                }
            }
            return list;
        }

        @Override
        public void makeDirectory(String path) {
            dirs.add(this.normalize(path));
        }

        @Override
        public void removeDir(String path) {
            dirs.remove(this.normalize(path));
        }

        @Override
        public void removeFile(String path) {
            files.remove(this.normalize(path));
        }

        @Override
        protected void doMove(String source, String target) {
            byte[] content = files.remove(this.normalize(source));
            files.put(this.normalize(target), content);
        }

        @Override
        protected long doTransfer(String path, OutputStream out, long skip, int size, boolean close) throws IOException {
            try {
                byte[] content = files.get(this.normalize(path));
                if (content == null) {
                    throw Exceptions.sftp("no such file");
                }
                int start = (int) skip;
                if (start >= content.length) {
                    return 0;
                }
                int len = size == -1 ? content.length - start : Math.min(size, content.length - start);
                out.write(content, start, len);
                out.flush();
                return len;
            } finally {
                if (close) {
                    Streams.close(out);
                }
            }
        }

        @Override
        protected void doWrite(String path, InputStream in, StreamEntry entry) throws IOException {
            files.put(this.normalize(path), this.readBytes(in, entry));
        }

        @Override
        protected void doAppend(String path, InputStream in, StreamEntry entry) throws IOException {
            String key = this.normalize(path);
            byte[] append = this.readBytes(in, entry);
            byte[] before = files.getOrDefault(key, new byte[0]);
            byte[] merge = new byte[before.length + append.length];
            System.arraycopy(before, 0, merge, 0, before.length);
            System.arraycopy(append, 0, merge, before.length, append.length);
            files.put(key, merge);
        }

        private byte[] readBytes(InputStream in, StreamEntry entry) throws IOException {
            if (entry != null) {
                return Arrays.copyOfRange(entry.getBytes(), entry.getOff(), entry.getOff() + entry.getLen());
            }
            return Streams.toByteArray(in);
        }

        @Override
        public void bufferSize(int bufferSize) {
            this.bufferSize = bufferSize;
        }

        @Override
        public void charset(String charset) {
            this.charset = charset;
        }

        @Override
        public boolean isConnected() {
            return true;
        }

        @Override
        public int getServerVersion() {
            return 3;
        }

        @Override
        public void close() {
            // 内存实现无资源需要释放
        }

        // -------------------- 以下方法测试基类逻辑时不会使用 --------------------

        @Override
        public void sendSignal(String signal) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getHome() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isExist(String path) {
            return this.getFile(path) != null;
        }

        @Override
        public String getPath(String path) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLinkPath(String path) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getSize(String path) {
            SftpFile file = this.getFile(path);
            return file == null ? -1 : file.getSize();
        }

        @Override
        public void setFileAttribute(SftpFile attribute) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setModifyTime(String path, Date date) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void changeMode(String file, int permission) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void changeOwner(String file, int uid) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void changeGroup(String file, int gid) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void touch(String path) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void touchTruncate(String path) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void touch(String path, boolean truncate) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void truncate(String path) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void link(String source, String target, boolean hard) {
            throw new UnsupportedOperationException();
        }

        @Override
        public InputStream openInputStream(String path, long skip) {
            throw new UnsupportedOperationException();
        }

        @Override
        public OutputStream openOutputStream(String path, boolean append) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int read(String path, long skip, byte[] bs, int offset, int len) {
            throw new UnsupportedOperationException();
        }

        @Override
        public IFileUploader upload(String remote, File local) {
            throw new UnsupportedOperationException();
        }

        @Override
        public IFileUploader upload(String remote, String local) {
            throw new UnsupportedOperationException();
        }

        @Override
        public IFileDownloader download(String remote, File local) {
            throw new UnsupportedOperationException();
        }

        @Override
        public IFileDownloader download(String remote, String local) {
            throw new UnsupportedOperationException();
        }
    }

    // -------------------- tests --------------------

    @Test
    public void testDefaultBufferSize() {
        Assert.assertEquals(Const.BUFFER_KB_32, executor.getBufferSize());
        executor.bufferSize(1024);
        Assert.assertEquals(1024, executor.getBufferSize());
    }

    @Test
    public void testGetCharset() {
        Assert.assertEquals(Const.UTF_8, executor.getCharset());
        executor.charset("GBK");
        Assert.assertEquals("GBK", executor.getCharset());
    }

    @Test
    public void testMakeDirectories() {
        executor.makeDirectories("/a/b/c");
        Assert.assertTrue(executor.dirs.contains("/a"));
        Assert.assertTrue(executor.dirs.contains("/a/b"));
        Assert.assertTrue(executor.dirs.contains("/a/b/c"));
    }

    @Test
    public void testMakeDirectoriesExists() {
        executor.makeDirectories("/a/b");
        int size = executor.dirs.size();
        // 已存在不重复创建
        executor.makeDirectories("/a/b");
        Assert.assertEquals(size, executor.dirs.size());
    }

    @Test
    public void testWriteAndTransfer() throws IOException {
        executor.write("/data/f.txt", "hello world");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        long read = executor.transfer("/data/f.txt", out);
        Assert.assertEquals(11L, read);
        Assert.assertEquals("hello world", out.toString());
    }

    @Test
    public void testTransferSkip() throws IOException {
        executor.write("/data/f.txt", "hello world");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        long read = executor.transfer("/data/f.txt", out, 6);
        Assert.assertEquals(5L, read);
        Assert.assertEquals("world", out.toString());
    }

    @Test
    public void testTransferSkipSize() throws IOException {
        executor.write("/data/f.txt", "hello world");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        long read = executor.transfer("/data/f.txt", out, 0, 5);
        Assert.assertEquals(5L, read);
        Assert.assertEquals("hello", out.toString());
    }

    @Test
    public void testWriteBytes() throws IOException {
        byte[] bs = "0123456789".getBytes();
        executor.write("/data/b.bin", bs, 2, 5);
        Assert.assertArrayEquals("23456".getBytes(), executor.files.get("/data/b.bin"));
    }

    @Test
    public void testWriteInputStream() throws IOException {
        executor.write("/data/in.txt", new ByteArrayInputStream("stream data".getBytes()));
        Assert.assertArrayEquals("stream data".getBytes(), executor.files.get("/data/in.txt"));
    }

    @Test
    public void testAppend() throws IOException {
        executor.write("/data/a.txt", "hello");
        executor.append("/data/a.txt", " world");
        Assert.assertArrayEquals("hello world".getBytes(), executor.files.get("/data/a.txt"));
    }

    @Test
    public void testAppendStream() throws IOException {
        executor.write("/data/a.txt", "1");
        executor.append("/data/a.txt", new ByteArrayInputStream("23".getBytes()));
        Assert.assertArrayEquals("123".getBytes(), executor.files.get("/data/a.txt"));
    }

    @Test
    public void testRemoveFile() {
        executor.files.put("/data/x.txt", "x".getBytes());
        executor.remove("/data/x.txt");
        Assert.assertFalse(executor.files.containsKey("/data/x.txt"));
    }

    @Test
    public void testRemoveDirRecursive() {
        executor.dirs.add("/d");
        executor.dirs.add("/d/sub");
        executor.files.put("/d/x.txt", "x".getBytes());
        executor.files.put("/d/sub/y.txt", "y".getBytes());
        executor.remove("/d");
        Assert.assertTrue(executor.files.isEmpty());
        Assert.assertTrue(executor.dirs.isEmpty());
    }

    @Test
    public void testRemoveNotExists() {
        // 文件不存在不抛出异常
        executor.remove("/not/exists");
    }

    @Test
    public void testMoveSameDir() {
        executor.files.put("/m/a.txt", "a".getBytes());
        executor.move("/m/a.txt", "/m/b.txt");
        Assert.assertFalse(executor.files.containsKey("/m/a.txt"));
        Assert.assertArrayEquals("a".getBytes(), executor.files.get("/m/b.txt"));
    }

    @Test
    public void testMoveToOtherDir() {
        executor.files.put("/m/a.txt", "a".getBytes());
        executor.move("/m/a.txt", "/n/b.txt");
        // 目标目录自动创建
        Assert.assertTrue(executor.dirs.contains("/n"));
        Assert.assertArrayEquals("a".getBytes(), executor.files.get("/n/b.txt"));
    }

    @Test
    public void testMoveRelative() {
        executor.files.put("/m/a.txt", "a".getBytes());
        // 相对路径为重命名
        executor.move("/m/a.txt", "renamed.txt");
        Assert.assertArrayEquals("a".getBytes(), executor.files.get("/m/renamed.txt"));
    }

    @Test
    public void testListFiles() {
        executor.dirs.add("/data");
        executor.dirs.add("/data/sub");
        executor.files.put("/data/a.txt", "a".getBytes());
        executor.files.put("/data/sub/b.txt", "b".getBytes());

        // 不递归 不含文件夹
        List<SftpFile> list = executor.listFiles("/data");
        Assert.assertEquals(1, list.size());
        Assert.assertEquals("/data/a.txt", list.get(0).getPath());

        // 递归 不含文件夹
        list = executor.listFiles("/data", true);
        Assert.assertEquals(2, list.size());

        // 递归 含文件夹
        list = executor.listFiles("/data", true, true);
        Assert.assertEquals(3, list.size());
    }

    @Test
    public void testListDirs() {
        executor.dirs.add("/data");
        executor.dirs.add("/data/sub");
        executor.dirs.add("/data/sub/deep");
        executor.files.put("/data/a.txt", "a".getBytes());

        List<SftpFile> list = executor.listDirs("/data");
        Assert.assertEquals(1, list.size());
        Assert.assertEquals("/data/sub", list.get(0).getPath());

        list = executor.listDirs("/data", true);
        Assert.assertEquals(2, list.size());
    }

    @Test
    public void testListFilesFilter() {
        executor.dirs.add("/data");
        executor.dirs.add("/data/sub");
        executor.files.put("/data/a.txt", "a".getBytes());
        executor.files.put("/data/b.log", "b".getBytes());
        executor.files.put("/data/sub/c.log", "c".getBytes());

        List<SftpFile> list = executor.listFilesFilter("/data", SftpFileFilter.suffix(".log"));
        Assert.assertEquals(1, list.size());
        Assert.assertEquals("/data/b.log", list.get(0).getPath());

        list = executor.listFilesFilter("/data", SftpFileFilter.suffix(".log"), true);
        Assert.assertEquals(2, list.size());
    }

    @Test
    public void testUploadFile() throws IOException {
        File local = folder.newFile("upload.txt");
        FileWriters.write(local, "upload content".getBytes());
        executor.uploadFile("/remote/upload.txt", local);
        Assert.assertArrayEquals("upload content".getBytes(), executor.files.get("/remote/upload.txt"));
    }

    @Test
    public void testUploadFileStream() throws IOException {
        executor.uploadFile("/remote/s.txt", new ByteArrayInputStream("via stream".getBytes()), true);
        Assert.assertArrayEquals("via stream".getBytes(), executor.files.get("/remote/s.txt"));
    }

    @Test
    public void testDownloadFile() throws IOException {
        executor.files.put("/remote/d.txt", "download content".getBytes());
        File local = new File(folder.getRoot(), "download.txt");
        executor.downloadFile("/remote/d.txt", local);
        Assert.assertArrayEquals("download content".getBytes(), FileReaders.readAllBytes(local));
    }

    @Test
    public void testDownloadFileToStream() throws IOException {
        executor.files.put("/remote/d.txt", "abc".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        executor.downloadFile("/remote/d.txt", out, true);
        Assert.assertEquals("abc", out.toString());
    }

    @Test
    public void testDownloadDir() throws IOException {
        executor.dirs.add("/remote");
        executor.dirs.add("/remote/sub");
        executor.files.put("/remote/a.txt", "a".getBytes());
        executor.files.put("/remote/sub/b.txt", "b".getBytes());

        File localDir = folder.newFolder("down");
        executor.downloadDir("/remote", localDir.getAbsolutePath(), true);
        Assert.assertArrayEquals("a".getBytes(), FileReaders.readAllBytes(new File(localDir, "a.txt")));
        Assert.assertArrayEquals("b".getBytes(), FileReaders.readAllBytes(new File(localDir, "sub/b.txt")));
    }

    @Test
    public void testDownloadDirNotFound() {
        try {
            executor.downloadDir("/not/exists", folder.getRoot().getAbsolutePath(), false);
            Assert.fail("should throw exception");
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("not found file"));
        }
    }

    @Test
    public void testUploadDir() throws IOException {
        File localDir = folder.newFolder("up");
        File sub = new File(localDir, "sub");
        Assert.assertTrue(sub.mkdirs());
        FileWriters.write(new File(localDir, "a.txt"), "a".getBytes());
        FileWriters.write(new File(sub, "b.txt"), "b".getBytes());

        executor.uploadDir("/remote", localDir.getAbsolutePath(), true);
        Assert.assertArrayEquals("a".getBytes(), executor.files.get("/remote/a.txt"));
        Assert.assertArrayEquals("b".getBytes(), executor.files.get("/remote/sub/b.txt"));
        Assert.assertTrue(executor.dirs.contains("/remote/sub"));
    }

    @Test
    public void testIsSameParentPath() {
        Assert.assertTrue(executor.isSameParentPath("/a/b.txt", "/a/c.txt"));
        Assert.assertFalse(executor.isSameParentPath("/a/b.txt", "/d/c.txt"));
    }

}
