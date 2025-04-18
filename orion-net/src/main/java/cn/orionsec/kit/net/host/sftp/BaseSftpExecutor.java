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
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * sftp 执行器 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/5/18 15:23
 */
public abstract class BaseSftpExecutor implements ISftpExecutor {

    /**
     * 编码格式
     */
    protected String charset;

    /**
     * 默认缓冲区大小
     */
    protected int bufferSize;

    protected BaseSftpExecutor() {
        this.bufferSize = Const.BUFFER_KB_32;
    }

    @Override
    public void makeDirectories(String path) {
        this.doMakeDir(path, this::makeDirectory);
    }

    @Override
    public void remove(String path) {
        try {
            SftpFile file = this.getFile(path);
            if (file == null) {
                return;
            }
            if (file.isDirectory()) {
                List<SftpFile> files = this.list(path);
                for (SftpFile f : files) {
                    if (f.isDirectory()) {
                        this.remove(f.getPath());
                    } else {
                        this.removeFile(f.getPath());
                    }
                }
                this.removeDir(path);
            } else {
                this.removeFile(path);
            }
        } catch (Exception e) {
            if (SftpErrorMessage.NO_SUCH_FILE.isCause(e)) {
                return;
            }
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public void move(String source, String target) {
        try {
            source = Files1.getPath(source);
            target = Files1.getPath(target);
            if (target.charAt(0) == '/') {
                // 检查是否需要创建目标文件目录
                if (!this.isSameParentPath(source, target)) {
                    this.makeDirectories(Files1.getParentPath(target));
                }
                // 绝对路径
                this.doMove(source, Files1.getPath(Files1.normalize(target)));
            } else {
                // 相对路径
                this.doMove(source, Files1.getPath(Files1.normalize(Files1.getPath(source + "/../" + target))));
            }
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
    }

    // -------------------- transfer --------------------

    @Override
    public long transfer(String path, OutputStream out, long skip, int size) throws IOException {
        return this.doTransfer(path, out, skip, size, false);
    }

    @Override
    public long transfer(String path, String file, long skip) throws IOException {
        return this.doTransfer(path, Files1.openOutputStreamFast(file), skip, -1, true);
    }

    // -------------------- write --------------------

    @Override
    public void write(String path, InputStream in) throws IOException {
        this.doWrite(path, in, null);
    }

    @Override
    public void write(String path, byte[] bs, int off, int len) throws IOException {
        this.doWrite(path, null, new StreamEntry(bs, off, len));
    }

    // -------------------- append --------------------

    @Override
    public void append(String path, InputStream in) throws IOException {
        this.doAppend(path, in, null);
    }

    @Override
    public void append(String path, byte[] bs, int off, int len) throws IOException {
        this.doAppend(path, null, new StreamEntry(bs, off, len));
    }

    // -------------------- upload --------------------

    @Override
    public void uploadFile(String remoteFile, String localFile) throws IOException {
        this.uploadFile(remoteFile, Files1.openInputStreamFast(localFile), true);
    }

    @Override
    public void uploadFile(String remoteFile, InputStream in, boolean close) throws IOException {
        BufferedInputStream buffer = null;
        try {
            this.doWrite(remoteFile, buffer = new BufferedInputStream(in, bufferSize), null);
        } finally {
            if (close) {
                Streams.close(in);
                Streams.close(buffer);
            }
        }
    }

    @Override
    public void uploadDir(String remoteDir, String localDir, boolean child) throws IOException {
        localDir = Files1.getPath(localDir);
        List<File> dirs = Files1.listDirs(localDir, child);
        List<File> files = Files1.listFiles(localDir, child);
        for (File dir : dirs) {
            this.makeDirectories(Files1.getPath(remoteDir, dir.getAbsolutePath().substring(localDir.length())));
        }
        for (File file : files) {
            String path = Files1.getPath(remoteDir, file.getAbsolutePath().substring(localDir.length()));
            this.uploadFile(path, file);
        }
    }

    // -------------------- download --------------------

    @Override
    public void downloadFile(String remoteFile, String localFile) throws IOException {
        this.downloadFile(remoteFile, Files1.openOutputStreamFast(localFile), true);
    }

    @Override
    public void downloadFile(String remoteFile, OutputStream out, boolean close) throws IOException {
        this.doTransfer(remoteFile, out, 0, -1, close);
    }

    @Override
    public void downloadDir(String remoteDir, String localDir, boolean child) throws IOException {
        remoteDir = Files1.getPath(remoteDir);
        if (this.getFile(remoteDir) == null) {
            throw Exceptions.sftp("not found file " + remoteDir);
        }
        if (!child) {
            List<SftpFile> list = this.listFiles(remoteDir, false);
            for (SftpFile s : list) {
                this.downloadFile(s.getPath(), Files1.getPath(localDir, Files1.getFileName(s.getPath())));
            }
        } else {
            List<SftpFile> list = this.listDirs(remoteDir, true);
            for (SftpFile s : list) {
                Files1.mkdirs(Files1.getPath(localDir, s.getPath().substring(remoteDir.length())));
            }
            list = this.listFiles(remoteDir, true);
            for (SftpFile s : list) {
                this.downloadFile(s.getPath(), Files1.getPath(localDir, s.getPath().substring(remoteDir.length())));
            }
        }
    }

    // -------------------- list --------------------

    @Override
    public List<SftpFile> listFiles(String path, boolean child, boolean dir) {
        List<SftpFile> list = new ArrayList<>();
        try {
            List<SftpFile> ls = this.list(path);
            for (SftpFile l : ls) {
                if (l.isDirectory()) {
                    if (dir) {
                        list.add(l);
                    }
                    if (child) {
                        list.addAll(this.listFiles(Files1.getPath(path, l.getName()), true, dir));
                    }
                } else {
                    list.add(l);
                }
            }
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
        return list;
    }

    @Override
    public List<SftpFile> listDirs(String path, boolean child) {
        List<SftpFile> list = new ArrayList<>();
        try {
            List<SftpFile> ls = this.list(path);
            for (SftpFile l : ls) {
                if (l.isDirectory()) {
                    list.add(l);
                    if (child) {
                        list.addAll(this.listDirs(Files1.getPath(path, l.getName()), true));
                    }
                }
            }
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
        return list;
    }

    @Override
    public List<SftpFile> listFilesFilter(String path, SftpFileFilter filter, boolean child, boolean dir) {
        return this.doListFiles(path, filter, child, dir);
    }

    // -------------------- abstract --------------------

    /**
     * 移动文件
     *
     * @param source 原文件绝对路径
     * @param target 目标文件 绝对路径 相对路径都可以
     */
    protected abstract void doMove(String source, String target);

    /**
     * 执行创建文件夹
     *
     * @param path     path
     * @param creative 创建器
     */
    protected void doMakeDir(String path, Consumer<String> creative) {
        SftpFile file = this.getFile(path, false);
        if (file != null && file.isDirectory()) {
            return;
        }
        List<String> parentPaths = Files1.getParentPaths(path);
        parentPaths.add(path);
        boolean check = true;
        for (String parentPath : parentPaths) {
            if (check) {
                SftpFile parent = this.getFile(parentPath, false);
                if (parent == null || !parent.isDirectory()) {
                    check = false;
                }
            }
            if (!check) {
                try {
                    creative.accept(parentPath);
                } catch (Exception e) {
                    throw Exceptions.sftp(e);
                }
            }
        }
    }

    /**
     * 执行传输
     *
     * @param path  path
     * @param out   out
     * @param skip  skip
     * @param size  size
     * @param close close
     * @return read
     * @throws IOException IOException
     */
    protected abstract long doTransfer(String path, OutputStream out, long skip, int size, boolean close) throws IOException;

    /**
     * 执行写入
     *
     * @param path  path
     * @param in    in
     * @param entry entry
     * @throws IOException IOException
     */
    protected abstract void doWrite(String path, InputStream in, StreamEntry entry) throws IOException;

    /**
     * 执行拼接
     *
     * @param path  path
     * @param in    in
     * @param entry entry
     * @throws IOException IOException
     */
    protected abstract void doAppend(String path, InputStream in, StreamEntry entry) throws IOException;

    /**
     * 查询文件列表
     *
     * @param path   path
     * @param filter filter
     * @param child  是否递归
     * @param dir    是否添加文件夹
     * @return files
     */
    private List<SftpFile> doListFiles(String path, SftpFileFilter filter, boolean child, boolean dir) {
        List<SftpFile> list = new ArrayList<>();
        try {
            List<SftpFile> ls = this.list(path);
            for (SftpFile l : ls) {
                String fn = l.getName();
                boolean isDir = l.isDirectory();
                if (!isDir || dir) {
                    if (filter.test(l)) {
                        list.add(l);
                    }
                }
                if (isDir && child) {
                    list.addAll(this.doListFiles(Files1.getPath(path, fn), filter, true, dir));
                }
            }
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
        return list;
    }

    /**
     * 检查是否为相同父级目录
     *
     * @param source source
     * @param target target
     * @return 是否为父级目录
     */
    protected boolean isSameParentPath(String source, String target) {
        return Files1.getParentPath(source).equals(Files1.getParentPath(target));
    }

    // -------------------- other --------------------

    @Override
    public int getBufferSize() {
        return bufferSize;
    }

    @Override
    public String getCharset() {
        return charset;
    }

}
