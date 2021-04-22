package com.orion.remote.connection.sftp;

import ch.ethz.ssh2.SFTPv3Client;
import ch.ethz.ssh2.SFTPv3DirectoryEntry;
import ch.ethz.ssh2.SFTPv3FileAttributes;
import ch.ethz.ssh2.SFTPv3FileHandle;
import com.orion.able.SafeCloseable;
import com.orion.constant.Const;
import com.orion.lang.StreamEntry;
import com.orion.remote.connection.sftp.bigfile.SftpDownload;
import com.orion.remote.connection.sftp.bigfile.SftpUpload;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.collect.Lists;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * SFTP 执行器
 * <p>
 * 文件路必须是绝对路径 可以包含 ../ ./
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/29 18:02
 */
public class SftpExecutor implements SafeCloseable {

    /**
     * 默认8进制权限
     */
    private static final Integer DEFAULT_PERMISSIONS = 0700;

    /**
     * 分隔符
     */
    private static final String SEPARATOR = Const.SLASH;

    /**
     * SFTP客户端
     */
    private SFTPv3Client client;

    /**
     * 编码格式
     */
    private String charset;

    /**
     * 默认缓冲区大小 因为并行数为1 并且默认执行一次读写操作最大字节为32768
     */
    private int bufferSize;

    public SftpExecutor(SFTPv3Client client) {
        this(client, Const.UTF_8);
    }

    public SftpExecutor(SFTPv3Client client, String charset) {
        this.client = client;
        this.charset = charset;
        this.bufferSize = Const.BUFFER_KB_32;
        // 默认并行数必须为1
        this.client.setRequestParallelism(1);
        try {
            this.client.setCharset(charset);
        } catch (IOException e) {
            throw Exceptions.unsupportedEncoding(e);
        }
    }

    /**
     * 设置缓冲区大小
     *
     * @param bufferSize 缓冲区大小
     * @return this
     */
    public SftpExecutor bufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }

    /**
     * 设置编码格式
     *
     * @param charset 编码格式
     */
    public SftpExecutor charset(String charset) {
        try {
            client.setCharset(charset);
        } catch (IOException e) {
            throw Exceptions.unsupportedEncoding(e);
        }
        return this;
    }

    /**
     * 设置请求并行数量
     *
     * @param parallelism 请求并行数量
     */
    public SftpExecutor requestParallelism(int parallelism) {
        client.setRequestParallelism(parallelism);
        return this;
    }

    /**
     * 检查文件是否存在
     *
     * @param path 文件绝对路径
     * @return true 存在
     */
    public boolean isExist(String path) {
        try {
            return client.canonicalPath(path) != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查文件是否存在
     *
     * @param path 文件绝对路径
     * @return 文件绝对路径 null文件不存在
     */
    public String getPath(String path) {
        try {
            return client.canonicalPath(path);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取连接文件的源文件
     *
     * @param path 连接文件的绝对路径
     * @return 源文件的绝对路径 null文件不是连接文件
     */
    public String getLinkPath(String path) {
        try {
            return client.readLink(path);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 设置文件属性
     *
     * @param path 文件绝对路径
     * @param attr 属性
     * @return ignore
     */
    public boolean setFileAttribute(String path, SftpFile attr) {
        try {
            client.setstat(path, attr.getAttrs());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取文件属性
     *
     * @param path 文件绝对路径
     * @return 属性
     */
    public SftpFile getFile(String path) {
        return this.getFile(path, false);
    }

    /**
     * 获取文件属性
     *
     * @param path           文件绝对路径
     * @param followSymbolic 如果是连接文件是否返回原文件属性
     * @return 属性
     */
    public SftpFile getFile(String path, boolean followSymbolic) {
        try {
            SFTPv3FileAttributes attr;
            if (followSymbolic) {
                attr = client.stat(path);
            } else {
                attr = client.lstat(path);
            }
            return new SftpFile(path, attr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 清空文件, 没有则创建
     *
     * @param path 文件绝对路径
     * @return ignore
     */
    public boolean clear(String path) {
        try {
            SFTPv3FileHandle clear = client.createFileTruncate(path);
            clear.getClient().closeFile(clear);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 获取文件大小 如果不存在返回-1
     *
     * @param path 文件目录
     * @return 文件大小
     */
    public long getSize(String path) {
        try {
            SFTPv3FileAttributes attr = client.stat(path);
            if (!attr.isDirectory()) {
                return attr.size;
            } else {
                return -1;
            }
        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * 创建文件夹
     *
     * @param path 文件夹绝对路径
     * @return ignore
     */
    public boolean mkdirs(String path) {
        return this.mkdirs(path, DEFAULT_PERMISSIONS);
    }

    /**
     * 创建文件夹
     *
     * @param path        文件夹绝对路径
     * @param permissions 权限
     * @return ignore
     */
    public boolean mkdirs(String path, int permissions) {
        SftpFile p = this.getFile(path, false);
        if (p != null && p.isDirectory()) {
            return true;
        }
        List<String> parentPaths = Files1.getParentPaths(path);
        parentPaths.add(path);
        for (int i = 1, size = parentPaths.size(); i < size; i++) {
            String parentPath = parentPaths.get(i);
            SftpFile parentAttr = this.getFile(parentPath, false);
            if (parentAttr == null || !parentAttr.isDirectory()) {
                try {
                    client.mkdir(parentPath, permissions);
                } catch (Exception e1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 删除一个空的文件夹
     *
     * @param path 文件夹绝对路径
     * @return ignore
     */
    public boolean rmdir(String path) {
        try {
            client.rmdir(path);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除一个普通文件
     *
     * @param path 文件绝对路径
     * @return ignore
     */
    public boolean rmFile(String path) {
        try {
            client.rm(path);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 递归删除文件或文件夹
     *
     * @param path 文件绝对路径
     * @return ignore
     */
    public boolean rm(String path) {
        try {
            SftpFile file = this.getFile(path);
            if (file == null) {
                return true;
            }
            if (file.isDirectory()) {
                List<SftpFile> files = this.ll(path);
                for (SftpFile f : files) {
                    if (f.isDirectory()) {
                        this.rm(f.getPath());
                    } else {
                        client.rm(f.getPath());
                    }
                }
                client.rmdir(path);
            } else {
                client.rm(path);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 创建文件
     *
     * @param path 文件绝对路径
     * @return ignore
     */
    public boolean touch(String path) {
        return this.touch(path, false);
    }

    /**
     * 创建文件 如果存在则清空
     *
     * @param path 文件绝对路径
     * @return ignore
     */
    public boolean touchTruncate(String path) {
        return this.touch(path, true);
    }

    /**
     * 创建文件
     *
     * @param path     文件绝对路径
     * @param truncate 是否截断
     * @return ignore
     */
    public boolean touch(String path, boolean truncate) {
        try {
            if (this.mkdirs(Files1.getParentPath(path), DEFAULT_PERMISSIONS)) {
                SFTPv3FileHandle handle;
                if (truncate) {
                    handle = client.createFileTruncate(path);
                } else {
                    handle = client.createFile(path);
                }
                handle.getClient().closeFile(handle);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 创建连接文件
     *
     * @param sourceFile     被连接文件绝对路径
     * @param targetLinkFile 连接文件绝对路径
     * @return true成功
     */
    public boolean touchLink(String sourceFile, String targetLinkFile) {
        try {
            if (this.mkdirs(Files1.getParentPath(targetLinkFile), DEFAULT_PERMISSIONS)) {
                client.createSymlink(targetLinkFile, sourceFile);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 移动文件
     *
     * @param source 源文件
     * @param target 目标文件 绝对路径,相对路径都可以
     * @return true成功
     */
    public boolean mv(String source, String target) {
        try {
            source = Files1.getPath(source);
            target = Files1.getPath(target);
            if (target.charAt(0) == '/') {
                // 绝对路径
                client.mv(source, Files1.normalize(target));
            } else {
                // 相对路径
                client.mv(source, Files1.normalize(Files1.getPath(source + "/../" + target)));
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // -------------------- read --------------------

    public int read(String path, byte[] bs) throws IOException {
        return this.read(path, 0, bs, 0, bs.length);
    }

    public int read(SFTPv3FileHandle handle, byte[] bs) throws IOException {
        return this.read(handle, 0, bs, 0, bs.length);
    }

    public int read(String path, long skip, byte[] bs) throws IOException {
        return this.read(path, skip, bs, 0, bs.length);
    }

    public int read(SFTPv3FileHandle handle, long skip, byte[] bs) throws IOException {
        return this.read(handle, skip, bs, 0, bs.length);
    }

    public int read(String path, byte[] bs, int offset, int len) throws IOException {
        return this.read(path, 0, bs, offset, len);
    }

    public int read(SFTPv3FileHandle handle, byte[] bs, int offset, int len) throws IOException {
        return this.read(handle, 0, bs, offset, len);
    }

    /**
     * 读取字节
     *
     * @param path   文件绝对路径
     * @param skip   跳过字数
     * @param bs     数组
     * @param offset 偏移量
     * @param len    读取长度
     * @return 已读取长度
     * @throws IOException IOException
     */
    public int read(String path, long skip, byte[] bs, int offset, int len) throws IOException {
        SFTPv3FileHandle handle = null;
        try {
            handle = client.openFileRO(path);
            return this.read(handle, skip, bs, offset, len);
        } catch (Exception e) {
            throw Exceptions.io("cannot read file " + path, e);
        } finally {
            if (handle != null) {
                handle.getClient().closeFile(handle);
            }
        }
    }

    /**
     * 读取字节
     *
     * @param handle handle
     * @param skip   跳过字数
     * @param bs     数组
     * @param offset 偏移量
     * @param len    读取长度
     * @return 已读取长度
     * @throws IOException IOException
     */
    public int read(SFTPv3FileHandle handle, long skip, byte[] bs, int offset, int len) throws IOException {
        return client.read(handle, skip, bs, offset, len);
    }

    // -------------------- transfer --------------------

    public long transfer(String path, String file) throws IOException {
        Files1.touch(file);
        return this.transfer(path, Files1.openOutputStream(file), 0, -1, true);
    }

    public long transfer(SFTPv3FileHandle handle, String file) throws IOException {
        Files1.touch(file);
        return this.transfer(handle, Files1.openOutputStream(file), 0, -1, true);
    }

    public long transfer(String path, File file) throws IOException {
        Files1.touch(file);
        return this.transfer(path, Files1.openOutputStream(file), 0, -1, true);
    }

    public long transfer(SFTPv3FileHandle handle, File file) throws IOException {
        Files1.touch(file);
        return this.transfer(handle, Files1.openOutputStream(file), 0, -1, true);
    }

    public long transfer(String path, OutputStream out) throws IOException {
        return this.transfer(path, out, 0, -1, false);
    }

    public long transfer(SFTPv3FileHandle handle, OutputStream out) throws IOException {
        return this.transfer(handle, out, 0, -1, false);
    }

    public long transfer(String path, OutputStream out, long skip) throws IOException {
        return this.transfer(path, out, skip, -1, false);
    }

    public long transfer(SFTPv3FileHandle handle, OutputStream out, long skip) throws IOException {
        return this.transfer(handle, out, skip, -1, false);
    }

    public long transfer(String path, OutputStream out, long skip, int size) throws IOException {
        return this.transfer(path, out, skip, size, false);
    }

    public long transfer(SFTPv3FileHandle handle, OutputStream out, long skip, int size) throws IOException {
        return this.transfer(handle, out, skip, size, false);
    }

    /**
     * 读取字节到输出流
     *
     * @param path  文件绝对路径
     * @param out   输出流
     * @param skip  跳过字数
     * @param size  读取长度
     * @param close 是否关闭
     * @return 已读取长度
     * @throws IOException IOException
     */
    public long transfer(String path, OutputStream out, long skip, int size, boolean close) throws IOException {
        if (size != -1 && size <= 0) {
            throw Exceptions.runtime("read size must > 0 or -1");
        }
        SFTPv3FileHandle handle = null;
        try {
            handle = client.openFileRO(path);
            return this.transfer(handle, out, skip, size, close);
        } catch (Exception e) {
            throw Exceptions.io("cannot read file " + path, e);
        } finally {
            if (handle != null) {
                handle.getClient().closeFile(handle);
            }
        }
    }

    /**
     * 读取字节到输出流
     *
     * @param handle handle
     * @param out    输出流
     * @param skip   跳过字数
     * @param size   读取长度
     * @param close  是否关闭
     * @return 已读取长度
     * @throws IOException IOException
     */
    public long transfer(SFTPv3FileHandle handle, OutputStream out, long skip, int size, boolean close) throws IOException {
        if (size != -1 && size <= 0) {
            throw Exceptions.runtime("read size must > 0 or -1");
        }
        try {
            long total = 0, curr = skip;
            byte[] bs = new byte[this.bufferSize];
            if (size != -1) {
                int last = size % this.bufferSize;
                long readTimes = Long.max(size / this.bufferSize, 1);
                if (last == 0) {
                    readTimes++;
                }
                for (long i = 0; i < readTimes; i++) {
                    int read;
                    if (i == readTimes - 1) {
                        read = client.read(handle, curr, bs, 0, last);
                    } else {
                        read = client.read(handle, curr, bs, 0, this.bufferSize);
                    }
                    if (read != -1) {
                        out.write(bs, 0, read);
                        total += read;
                        curr += read;
                    } else {
                        break;
                    }
                }
            } else {
                int read;
                while ((read = client.read(handle, curr, bs, 0, this.bufferSize)) != -1) {
                    out.write(bs, 0, read);
                    total += read;
                    curr += read;
                }
            }
            return total;
        } finally {
            if (close) {
                Streams.close(out);
            }
        }
    }

    // -------------------- write --------------------

    public void write(String path, InputStream in) throws IOException {
        this.write(path, 0, in, null, null, 1);
    }

    public void write(String path, byte[] bs) throws IOException {
        this.write(path, 0, null, new StreamEntry(bs), null, 1);
    }

    public void write(String path, byte[] bs, int off, int len) throws IOException {
        this.write(path, 0, null, new StreamEntry(bs, off, len), null, 1);
    }

    public void writeLine(String path, String line) throws IOException {
        this.write(path, 0, null, null, Lists.singleton(line), 1);
    }

    public void writeLines(String path, List<String> lines) throws IOException {
        this.write(path, 0, null, null, lines, 1);
    }

    // -------------------- replace --------------------

    public void replace(String path, InputStream in) throws IOException {
        this.write(path, 0, in, null, null, 2);
    }

    public void replace(String path, long fileOffset, InputStream in) throws IOException {
        this.write(path, fileOffset, in, null, null, 2);
    }

    public void replace(String path, byte[] bs) throws IOException {
        this.write(path, 0, null, new StreamEntry(bs), null, 2);
    }

    public void replace(String path, byte[] bs, int off, int len) throws IOException {
        this.write(path, 0, null, new StreamEntry(bs, off, len), null, 2);
    }

    public void replace(String path, long fileOffset, byte[] bs) throws IOException {
        this.write(path, fileOffset, null, new StreamEntry(bs), null, 2);
    }

    public void replace(String path, long fileOffset, byte[] bs, int off, int len) throws IOException {
        this.write(path, fileOffset, null, new StreamEntry(bs, off, len), null, 2);
    }

    public void replaceLine(String path, String line) throws IOException {
        this.write(path, 0, null, null, Lists.singleton(line), 2);
    }

    public void replaceLine(String path, long fileOffset, String line) throws IOException {
        this.write(path, fileOffset, null, null, Lists.singleton(line), 2);
    }

    public void replaceLines(String path, List<String> lines) throws IOException {
        this.write(path, 0, null, null, lines, 2);
    }

    public void replaceLines(String path, long fileOffset, List<String> lines) throws IOException {
        this.write(path, fileOffset, null, null, lines, 2);
    }

    // -------------------- append --------------------

    public void append(String path, InputStream in) throws IOException {
        this.write(path, 0, in, null, null, 3);
    }

    public void append(String path, byte[] bs) throws IOException {
        this.write(path, 0, null, new StreamEntry(bs), null, 3);
    }

    public void append(String path, byte[] bs, int off, int len) throws IOException {
        this.write(path, 0, null, new StreamEntry(bs, off, len), null, 3);
    }

    public void appendLine(String path, String line) throws IOException {
        this.write(path, 0, null, null, Lists.singleton(line), 3);
    }

    public void appendLines(String path, List<String> lines) throws IOException {
        this.write(path, 0, null, null, lines, 3);
    }

    /**
     * 拼接/替换/写入到文件
     *
     * @param path       文件绝对路径
     * @param in         输入流
     * @param fileOffset 文件偏移量
     * @param entry      写入信息
     * @param lines      行
     * @param type       1write  2replace  3append
     * @throws IOException IOException
     */
    private void write(String path, long fileOffset, InputStream in, StreamEntry entry, List<String> lines, int type) throws IOException {
        SFTPv3FileHandle handle = null;
        try {
            if (!this.touch(path, type == 1)) {
                throw Exceptions.sftp("touch file error: " + path);
            }
            if (type == 3) {
                handle = client.openFileRWAppend(path);
            } else {
                handle = client.openFileRW(path);
            }
            this.write(handle, fileOffset, in, entry, lines);
        } catch (Exception e) {
            throw Exceptions.io("cannot write file " + path, e);
        } finally {
            if (handle != null) {
                handle.getClient().closeFile(handle);
            }
        }
    }

    // -------------------- write handler --------------------

    public void write(SFTPv3FileHandle handle, InputStream in) throws IOException {
        this.write(handle, 0, in, null, null);
    }

    public void write(SFTPv3FileHandle handle, long fileOffset, InputStream in) throws IOException {
        this.write(handle, fileOffset, in, null, null);
    }

    public void write(SFTPv3FileHandle handle, byte[] bs) throws IOException {
        this.write(handle, 0, null, new StreamEntry(bs), null);
    }

    public void write(SFTPv3FileHandle handle, byte[] bs, int off, int len) throws IOException {
        this.write(handle, 0, null, new StreamEntry(bs, off, len), null);
    }

    public void write(SFTPv3FileHandle handle, long fileOffset, byte[] bs) throws IOException {
        this.write(handle, fileOffset, null, new StreamEntry(bs), null);
    }

    public void write(SFTPv3FileHandle handle, long fileOffset, byte[] bs, int off, int len) throws IOException {
        this.write(handle, fileOffset, null, new StreamEntry(bs, off, len), null);
    }

    public void writeLine(SFTPv3FileHandle handle, String line) throws IOException {
        this.write(handle, 0, null, null, Lists.singleton(line));
    }

    public void writeLine(SFTPv3FileHandle handle, long fileOffset, String line) throws IOException {
        this.write(handle, fileOffset, null, null, Lists.singleton(line));
    }

    public void writeLines(SFTPv3FileHandle handle, List<String> lines) throws IOException {
        this.write(handle, 0, null, null, lines);
    }

    public void writeLines(SFTPv3FileHandle handle, long fileOffset, List<String> lines) throws IOException {
        this.write(handle, fileOffset, null, null, lines);
    }

    /**
     * 拼接/替换/写入到文件
     *
     * @param handle     handle
     * @param in         输入流
     * @param fileOffset 文件偏移量
     * @param entry      写入信息
     * @param lines      行
     * @throws IOException IOException
     */
    private void write(SFTPv3FileHandle handle, long fileOffset, InputStream in, StreamEntry entry, List<String> lines) throws IOException {
        if (in != null) {
            byte[] bs = new byte[bufferSize];
            int read;
            while ((read = in.read(bs)) != -1) {
                client.write(handle, fileOffset, bs, 0, read);
                fileOffset += read;
            }
        } else if (entry != null) {
            client.write(handle, fileOffset, entry.getBytes(), entry.getOff(), entry.getLen());
        } else if (lines != null) {
            for (String line : lines) {
                byte[] bytes;
                bytes = Strings.bytes((line + Const.LF), charset);
                client.write(handle, fileOffset, bytes, 0, bytes.length);
                fileOffset += bytes.length;
            }
        }
    }

    // -------------------- upload --------------------

    public void uploadFile(String remoteFile, String localFile) throws IOException {
        this.uploadFile(remoteFile, Files1.openInputStreamSafe(localFile), true);
    }

    public void uploadFile(String remoteFile, File localFile) throws IOException {
        this.uploadFile(remoteFile, Files1.openInputStreamSafe(localFile), true);
    }

    public void uploadFile(String remoteFile, InputStream in) throws IOException {
        this.uploadFile(remoteFile, in, false);
    }

    /**
     * 上传文件
     *
     * @param remoteFile 远程文件
     * @param in         input
     * @param close      close
     * @throws IOException IOException
     */
    public void uploadFile(String remoteFile, InputStream in, boolean close) throws IOException {
        BufferedInputStream buffer = null;
        try {
            this.write(remoteFile, buffer = new BufferedInputStream(in, bufferSize));
        } finally {
            if (close) {
                Streams.close(in);
                Streams.close(buffer);
            }
        }
    }

    public void uploadDir(String remoteDir, File localDir) throws IOException {
        this.uploadDir(remoteDir, localDir.getAbsolutePath(), true);
    }

    public void uploadDir(String remoteDir, String localDir) throws IOException {
        this.uploadDir(remoteDir, localDir, true);
    }

    public void uploadDir(String remoteDir, File localDir, boolean child) throws IOException {
        this.uploadDir(remoteDir, localDir.getAbsolutePath(), child);
    }

    /**
     * 上传文件夹
     *
     * @param remoteDir 远程文件夹
     * @param localDir  本地文件夹 上传时不包含此文件夹
     * @param child     是否遍历上传
     * @throws IOException IOException
     */
    public void uploadDir(String remoteDir, String localDir, boolean child) throws IOException {
        localDir = Files1.getPath(localDir);
        List<File> dirs = Files1.listDirs(localDir, child);
        List<File> files = Files1.listFiles(localDir, child);
        for (File dir : dirs) {
            this.mkdirs(Files1.getPath(remoteDir + SEPARATOR + (dir.getAbsolutePath().substring(localDir.length()))));
        }
        for (File file : files) {
            String path = Files1.getPath(remoteDir + SEPARATOR + (file.getAbsolutePath().substring(localDir.length())));
            this.uploadFile(path, file);
        }
    }

    // -------------------- download --------------------

    public void downloadFile(String remoteFile, String localFile) throws IOException {
        Files1.touch(localFile);
        this.downloadFile(remoteFile, Files1.openOutputStreamSafe(localFile), true);
    }

    public void downloadFile(String remoteFile, File localFile) throws IOException {
        Files1.touch(localFile);
        this.downloadFile(remoteFile, Files1.openOutputStreamSafe(localFile), true);
    }

    public void downloadFile(String remoteFile, OutputStream out) throws IOException {
        this.downloadFile(remoteFile, out, false);
    }

    /**
     * 下载文件
     *
     * @param remoteFile 远程文件路径
     * @param out        output
     * @param close      是否自动关闭
     * @throws IOException IOException
     */
    public void downloadFile(String remoteFile, OutputStream out, boolean close) throws IOException {
        try {
            this.transfer(remoteFile, out);
        } finally {
            if (close) {
                Streams.close(out);
            }
        }
    }

    public void downloadDir(String remoteDir, File localDir) throws IOException {
        this.downloadDir(remoteDir, localDir.getAbsolutePath(), true);
    }

    public void downloadDir(String remoteDir, String localDir) throws IOException {
        this.downloadDir(remoteDir, localDir, true);
    }

    public void downloadDir(String remoteDir, File localDir, boolean child) throws IOException {
        this.downloadDir(remoteDir, localDir.getAbsolutePath(), child);
    }

    /**
     * 下载文件夹
     *
     * @param remoteDir 远程文件夹
     * @param localDir  本地文件夹
     * @param child     是否递归子文件夹下载
     * @throws IOException pending
     */
    public void downloadDir(String remoteDir, String localDir, boolean child) throws IOException {
        remoteDir = Files1.getPath(remoteDir);
        if (!child) {
            List<SftpFile> list = this.listFiles(remoteDir, false);
            for (SftpFile s : list) {
                this.downloadFile(s.getPath(), Files1.getPath(localDir + SEPARATOR + Files1.getFileName(s.getPath())));
            }
        } else {
            List<SftpFile> list = this.listDirs(remoteDir, true);
            for (SftpFile s : list) {
                Files1.mkdirs(Files1.getPath(localDir + SEPARATOR + s.getPath().substring(remoteDir.length())));
            }
            list = this.listFiles(remoteDir, true);
            for (SftpFile s : list) {
                this.downloadFile(s.getPath(), Files1.getPath(localDir + SEPARATOR + s.getPath().substring(remoteDir.length())));
            }
        }
    }

    // -------------------- big file --------------------

    public SftpUpload upload(String remote, String local) {
        return new SftpUpload(this, remote, local);
    }

    /**
     * 获取文件上传器
     *
     * @param remote 远程文件绝对路径
     * @param local  本地文件
     * @return 文件上传器
     */
    public SftpUpload upload(String remote, File local) {
        return new SftpUpload(this, remote, local);
    }

    public SftpDownload download(String remote, String local) {
        return new SftpDownload(this, remote, local);
    }

    /**
     * 获取文件下载器
     *
     * @param remote 远程文件绝对路径
     * @param local  本地文件
     * @return 文件上传器
     */
    public SftpDownload download(String remote, File local) {
        return new SftpDownload(this, remote, local);
    }

    // -------------------- list --------------------

    /**
     * 获取目录文件属性
     *
     * @param path 文件夹绝对路径
     * @return 属性
     */
    public List<SftpFile> ll(String path) {
        try {
            List<SftpFile> list = new ArrayList<>();
            List<SFTPv3DirectoryEntry> files = client.ls(path);
            for (SFTPv3DirectoryEntry l : files) {
                String filename = l.filename;
                if (".".equals(filename) || "..".equals(filename)) {
                    continue;
                }
                list.add(new SftpFile(Files1.getPath(path + SEPARATOR + filename), l.longEntry, l.attributes));
            }
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    public List<SftpFile> listFiles(String path) {
        return this.listFiles(path, false, false);
    }

    public List<SftpFile> listFiles(String path, boolean child) {
        return this.listFiles(path, child, false);
    }

    /**
     * 文件列表
     *
     * @param path  文件夹
     * @param child 是否递归子文件夹
     * @param dir   是否添加文件夹
     * @return 文件列表
     */
    public List<SftpFile> listFiles(String path, boolean child, boolean dir) {
        List<SftpFile> list = new ArrayList<>();
        try {
            List<SftpFile> ls = this.ll(path);
            for (SftpFile l : ls) {
                String fn = l.getName();
                if (l.isDirectory()) {
                    if (dir) {
                        list.add(l);
                    }
                    if (child) {
                        list.addAll(this.listFiles(Files1.getPath(path + SEPARATOR + fn), true, dir));
                    }
                } else {
                    list.add(l);
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return list;
    }

    public List<SftpFile> listDirs(String path) {
        return this.listDirs(path, false);
    }

    /**
     * 文件夹列表
     *
     * @param path  文件夹
     * @param child 是否递归子文件夹
     * @return 文件列表
     */
    public List<SftpFile> listDirs(String path, boolean child) {
        List<SftpFile> list = new ArrayList<>();
        try {
            List<SftpFile> ls = this.ll(path);
            for (SftpFile l : ls) {
                String fn = l.getName();
                if (l.isDirectory()) {
                    list.add(l);
                    if (child) {
                        list.addAll(this.listDirs(Files1.getPath(path + SEPARATOR + fn), true));
                    }
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return list;
    }

    public List<SftpFile> listFilesSuffix(String path, String suffix) {
        return this.listFilesSuffix(path, suffix, false, false);
    }

    public List<SftpFile> listFilesSuffix(String path, String suffix, boolean child) {
        return this.listFilesSuffix(path, suffix, child, false);
    }

    /**
     * 搜索文件 后缀
     *
     * @param path   文件夹
     * @param suffix 后缀
     * @param child  是否递归子文件夹
     * @param dir    是否添加文件夹
     * @return 文件
     */
    public List<SftpFile> listFilesSuffix(String path, String suffix, boolean child, boolean dir) {
        return this.listFilesSearch(path, FileAttributeFilter.suffix(suffix), child, dir);
    }

    public List<SftpFile> listFilesMatch(String path, String match) {
        return this.listFilesMatch(path, match, false, false);
    }

    public List<SftpFile> listFilesMatch(String path, String match, boolean child) {
        return this.listFilesMatch(path, match, child, false);
    }

    /**
     * 搜索文件 文件名
     *
     * @param path  文件夹
     * @param match 匹配
     * @param child 是否递归子文件夹
     * @param dir   是否添加文件夹
     * @return 文件
     */
    public List<SftpFile> listFilesMatch(String path, String match, boolean child, boolean dir) {
        return this.listFilesSearch(path, FileAttributeFilter.match(match), child, dir);
    }

    public List<SftpFile> listFilesPattern(String path, Pattern pattern) {
        return this.listFilesPattern(path, pattern, false, false);
    }

    public List<SftpFile> listFilesPattern(String path, Pattern pattern, boolean child) {
        return this.listFilesPattern(path, pattern, child, false);
    }

    /**
     * 搜索文件 正则
     *
     * @param path    文件夹
     * @param pattern 正则
     * @param child   是否递归子文件夹
     * @return 文件
     */
    public List<SftpFile> listFilesPattern(String path, Pattern pattern, boolean child, boolean dir) {
        return this.listFilesSearch(path, FileAttributeFilter.pattern(pattern), child, dir);
    }

    public List<SftpFile> listFilesFilter(String path, FileAttributeFilter filter) {
        return this.listFilesFilter(path, filter, false, false);
    }

    public List<SftpFile> listFilesFilter(String path, FileAttributeFilter filter, boolean child) {
        return this.listFilesFilter(path, filter, child, false);
    }

    /**
     * 搜索文件 过滤器
     *
     * @param path   文件夹
     * @param filter 过滤器
     * @param child  是否递归子文件夹子文件夹
     * @param dir    是否添加文件夹
     * @return 文件
     */
    public List<SftpFile> listFilesFilter(String path, FileAttributeFilter filter, boolean child, boolean dir) {
        return this.listFilesSearch(path, filter, child, dir);
    }

    /**
     * 搜索文件
     *
     * @param path   文件夹
     * @param filter 过滤器
     * @param child  是否递归子文件夹
     * @param dir    是否添加文件夹
     * @return 文件
     */
    private List<SftpFile> listFilesSearch(String path, FileAttributeFilter filter, boolean child, boolean dir) {
        List<SftpFile> list = new ArrayList<>();
        try {
            List<SftpFile> files = this.ll(path);
            for (SftpFile l : files) {
                String fn = l.getName();
                boolean isDir = l.isDirectory();
                if (!isDir || dir) {
                    if (filter.accept(l)) {
                        list.add(l);
                    }
                }
                if (isDir && child) {
                    list.addAll(this.listFilesSearch(Files1.getPath(path + SEPARATOR + fn), filter, true, dir));
                }
            }
        } catch (Exception e) {
            return list;
        }
        return list;
    }

    // -------------------- get --------------------

    public SFTPv3FileHandle openReadFileHandler(String path) {
        return this.openFileHandler(path, 1);
    }

    public SFTPv3FileHandle openWriteFileHandler(String path) {
        return this.openFileHandler(path, 2);
    }

    public SFTPv3FileHandle openWriteAppendFileHandler(String path) {
        return this.openFileHandler(path, 3);
    }

    public SFTPv3FileHandle openReadWriteAppendFileHandler(String path) {
        return this.openFileHandler(path, 4);
    }

    /**
     * 打开文件处理器
     *
     * @param path path
     * @param type 1读 2写 3拼接 4读拼接
     * @return SFTPv3FileHandle
     */
    public SFTPv3FileHandle openFileHandler(String path, int type) {
        try {
            this.mkdirs(Files1.getParentPath(path));
            switch (type) {
                case 1:
                    return client.openFileRO(path);
                case 2:
                    return client.openFileRW(path);
                case 3:
                    return client.openFileWAppend(path);
                case 4:
                    return client.openFileRWAppend(path);
                default:
                    throw Exceptions.argument("type " + type + " is unsupported");
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 关闭文件
     *
     * @param handle 文件处理器
     * @return true成功
     */
    public boolean closeFile(SFTPv3FileHandle handle) {
        try {
            client.closeFile(handle);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取版本
     *
     * @return 版本
     */
    public int getVersion() {
        return client.getProtocolVersion();
    }

    /**
     * 获取编码格式
     *
     * @return 编码格式
     */
    public String getCharset() {
        return client.getCharset();
    }

    /**
     * 连接是否活跃
     *
     * @return true活跃
     */
    public boolean isConnected() {
        return client.isConnected();
    }

    @Override
    public void close() {
        client.close();
    }

    /**
     * 获取client
     *
     * @return client
     */
    public SFTPv3Client getClient() {
        return client;
    }

    /**
     * 获取缓冲区大小
     *
     * @return 缓冲区大小
     */
    public int getBufferSize() {
        return bufferSize;
    }

}
