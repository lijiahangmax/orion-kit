package com.orion.remote.connection.sftp;

import ch.ethz.ssh2.SFTPv3Client;
import ch.ethz.ssh2.SFTPv3DirectoryEntry;
import ch.ethz.ssh2.SFTPv3FileAttributes;
import ch.ethz.ssh2.SFTPv3FileHandle;
import com.orion.able.SafeCloseable;
import com.orion.lang.StreamEntry;
import com.orion.remote.connection.sftp.bigfile.SftpDownload;
import com.orion.remote.connection.sftp.bigfile.SftpUpload;
import com.orion.utils.Exceptions;
import com.orion.utils.Matches;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
     * SFTP客户端
     */
    private SFTPv3Client client;

    /**
     * 默认缓冲区大小 因为并行数为1 并且默认执行一次读写操作最大字节为32768
     */
    private int bufferSize = 32 * 1024;

    public SftpExecutor(SFTPv3Client client) {
        this(client, null);
    }

    public SftpExecutor(SFTPv3Client client, String fileNameCharset) {
        this.client = client;
        // 默认并行数必须为1
        this.client.setRequestParallelism(1);
        if (fileNameCharset != null) {
            try {
                this.client.setCharset(fileNameCharset);
            } catch (IOException e) {
                throw Exceptions.unCoding(e);
            }
        }
    }

    /**
     * 设置缓冲区大小
     *
     * @param bufferSize 缓冲区大小
     * @return this
     */
    public SftpExecutor setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }

    /**
     * 设置编码格式
     *
     * @param charset 编码格式
     */
    public SftpExecutor setCharset(String charset) {
        try {
            client.setCharset(charset);
        } catch (IOException e) {
            throw Exceptions.unCoding(e);
        }
        return this;
    }

    /**
     * 设置请求并行数量
     *
     * @param parallelism 请求并行数量
     */
    public SftpExecutor setRequestParallelism(int parallelism) {
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
    public boolean setFileAttribute(String path, FileAttribute attr) {
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
    public FileAttribute getFileAttribute(String path) {
        return this.getFileAttribute(path, false);
    }

    /**
     * 获取文件属性
     *
     * @param path           文件绝对路径
     * @param followSymbolic 如果是连接文件是否返回连接文件属性
     * @return 属性
     */
    public FileAttribute getFileAttribute(String path, boolean followSymbolic) {
        try {
            SFTPv3FileAttributes attr;
            if (followSymbolic) {
                attr = client.lstat(path);
            } else {
                attr = client.stat(path);
            }
            return new FileAttribute(path, attr);
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
     * 获取目录文件属性
     *
     * @param path 文件夹绝对路径
     * @return 属性
     */
    public List<FileAttribute> ll(String path) {
        try {
            List<FileAttribute> list = new ArrayList<>();
            List<SFTPv3DirectoryEntry> files = client.ls(path);
            for (SFTPv3DirectoryEntry l : files) {
                String filename = l.filename;
                if (".".equals(filename) || "..".equals(filename)) {
                    continue;
                }
                list.add(new FileAttribute(Files1.getPath(path + "/" + filename), l.longEntry, l.attributes));
            }
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 创建文件夹
     *
     * @param path 文件夹绝对路径
     * @return ignore
     */
    public boolean mkdirs(String path) {
        return mkdirs(path, DEFAULT_PERMISSIONS);
    }

    /**
     * 创建文件夹
     *
     * @param path        文件夹绝对路径
     * @param permissions 权限
     * @return ignore
     */
    public boolean mkdirs(String path, int permissions) {
        FileAttribute p = this.getFileAttribute(path, false);
        if (p != null && p.isDirectory()) {
            return true;
        }
        List<String> parentPaths = Files1.getParentPaths(path);
        parentPaths.add(path);
        boolean check = true;
        for (int i = 1, size = parentPaths.size(); i < size; i++) {
            String parentPath = parentPaths.get(i);
            if (check) {
                FileAttribute parentAttr = this.getFileAttribute(parentPath, false);
                if (parentAttr == null || !parentAttr.isDirectory()) {
                    check = false;
                }
            }
            if (!check) {
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
            FileAttribute file = this.getFileAttribute(path);
            if (file == null) {
                return true;
            }
            if (file.isDirectory()) {
                List<FileAttribute> files = this.ll(path);
                for (FileAttribute f : files) {
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
     * 创建文件
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
    private boolean touch(String path, boolean truncate) {
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
     * @param linkFile   连接文件绝对路径
     * @param targetFile 被连接文件绝对路径
     * @return true成功
     */
    public boolean touchLink(String linkFile, String targetFile) {
        try {
            if (this.mkdirs(Files1.getParentPath(linkFile), DEFAULT_PERMISSIONS)) {
                client.createSymlink(linkFile, targetFile);
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
                client.mv(source, Files1.normalize(target));
            } else {
                client.mv(source, Files1.normalize(Files1.getPath(source + "/../" + target)));
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // --------------- read ---------------

    public int read(String path, byte[] bs) throws IOException {
        return this.read(path, 0, bs, 0, bs.length);
    }

    public int read(String path, long skip, byte[] bs) throws IOException {
        return this.read(path, skip, bs, 0, bs.length);
    }

    public int read(String path, byte[] bs, int offset, int len) throws IOException {
        return this.read(path, 0, bs, offset, len);
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
        SFTPv3FileHandle handle = client.openFileRO(path);
        int read = client.read(handle, skip, bs, offset, len);
        handle.getClient().closeFile(handle);
        return read;
    }

    // --------------- transfer ---------------

    public long transfer(String path, String file) throws IOException {
        Files1.touch(file);
        OutputStream out = null;
        try {
            return this.transfer(path, out = Files1.openOutputStream(file), 0, -1);
        } finally {
            Streams.close(out);
        }
    }

    public long transfer(String path, File file) throws IOException {
        Files1.touch(file);
        OutputStream out = null;
        try {
            return this.transfer(path, out = Files1.openOutputStream(file), 0, -1);
        } finally {
            Streams.close(out);
        }
    }

    public long transfer(String path, OutputStream out) throws IOException {
        return this.transfer(path, out, 0, -1);
    }

    public long transfer(String path, OutputStream out, long skip) throws IOException {
        return this.transfer(path, out, skip, -1);
    }

    /**
     * 读取字节到输出流
     *
     * @param path 文件绝对路径
     * @param out  输出流
     * @param skip 跳过字数
     * @param size 读取长度
     * @return 已读取长度
     * @throws IOException IOException
     */
    public long transfer(String path, OutputStream out, long skip, int size) throws IOException {
        SFTPv3FileHandle handle = client.openFileRO(path);
        long r = 0;
        byte[] bs = new byte[this.bufferSize];
        if (size != -1) {
            boolean mod = size % this.bufferSize == 0;
            long readTimes = size / this.bufferSize;
            if (mod || readTimes == 0) {
                readTimes++;
            }
            for (int i = 0; i < readTimes; i++) {
                if (readTimes == 1) {
                    int read = client.read(handle, skip, bs, 0, size);
                    out.write(bs, 0, read);
                    r += read;
                } else {
                    int read = client.read(handle, skip, bs, 0, this.bufferSize);
                    if (read != -1) {
                        out.write(bs, 0, read);
                        r += read;
                    } else {
                        break;
                    }
                }
            }
        } else {
            int read;
            while ((read = client.read(handle, r, bs, 0, this.bufferSize)) != -1) {
                out.write(bs, 0, read);
                r += read;
            }
        }
        handle.getClient().closeFile(handle);
        return r;
    }

    // --------------- write ---------------

    public void write(String path, InputStream in) throws IOException {
        this.write(path, 0, in, null, null, null, false);
    }

    public void write(String path, InputStream in, long fileOffset) throws IOException {
        this.write(path, fileOffset, in, null, null, null, false);
    }

    public void write(String path, byte[] bs) throws IOException {
        this.write(path, 0, null, new StreamEntry(bs), null, null, false);
    }

    public void write(String path, byte[] bs, int off, int len) throws IOException {
        this.write(path, 0, null, new StreamEntry(bs, off, len), null, null, false);
    }

    public void write(String path, long fileOffset, byte[] bs) throws IOException {
        this.write(path, fileOffset, null, new StreamEntry(bs), null, null, false);
    }

    public void write(String path, long fileOffset, byte[] bs, int off, int len) throws IOException {
        this.write(path, fileOffset, null, new StreamEntry(bs, off, len), null, null, false);
    }

    public void writeLine(String path, String line) throws IOException {
        this.write(path, 0, null, new StreamEntry((line + "\n").getBytes()), null, null, false);
    }

    public void writeLine(String path, String line, String charset) throws IOException {
        this.write(path, 0, null, new StreamEntry((line + "\n").getBytes(charset)), null, null, false);
    }

    public void writeLine(String path, long fileOffset, String line) throws IOException {
        this.write(path, fileOffset, null, new StreamEntry((line + "\n").getBytes()), null, null, false);
    }

    public void writeLine(String path, long fileOffset, String line, String charset) throws IOException {
        this.write(path, fileOffset, null, new StreamEntry((line + "\n").getBytes(charset)), null, null, false);
    }

    public void writeLines(String path, List<String> lines) throws IOException {
        this.write(path, 0, null, null, lines, null, false);
    }

    public void writeLines(String path, List<String> lines, String charset) throws IOException {
        this.write(path, 0, null, null, lines, charset, false);
    }

    public void writeLines(String path, long fileOffset, List<String> lines) throws IOException {
        this.write(path, fileOffset, null, null, lines, null, false);
    }

    public void writeLines(String path, long fileOffset, List<String> lines, String charset) throws IOException {
        this.write(path, fileOffset, null, null, lines, charset, false);
    }

    public void append(String path, InputStream in) throws IOException {
        this.write(path, 0, in, null, null, null, true);
    }

    public void append(String path, byte[] bs) throws IOException {
        this.write(path, 0, null, new StreamEntry(bs), null, null, true);
    }

    public void append(String path, byte[] bs, int off, int len) throws IOException {
        this.write(path, 0, null, new StreamEntry(bs, off, len), null, null, true);
    }

    public void appendLine(String path, String line) throws IOException {
        this.write(path, 0, null, new StreamEntry((line + "\n").getBytes()), null, null, true);
    }

    public void appendLine(String path, String line, String charset) throws IOException {
        this.write(path, 0, null, new StreamEntry((line + "\n").getBytes(charset)), null, null, true);
    }

    public void appendLines(String path, List<String> lines) throws IOException {
        this.write(path, 0, null, null, lines, null, true);
    }

    public void appendLines(String path, List<String> lines, String charset) throws IOException {
        this.write(path, 0, null, null, lines, charset, true);
    }

    /**
     * 拼接/写入到文件 不会清除只会覆盖
     *
     * @param path       文件绝对路径
     * @param in         输入流
     * @param fileOffset 文件偏移量
     * @param entry      写入信息
     * @param lines      行
     * @param charset    行编码
     * @param append     是否拼接
     * @throws IOException IOException
     */
    private void write(String path, long fileOffset, InputStream in, StreamEntry entry, List<String> lines, String charset, boolean append) throws IOException {
        SFTPv3FileHandle handle;
        if (append) {
            handle = client.openFileRWAppend(path);
        } else {
            handle = client.openFileRW(path);
        }
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
                if (charset == null) {
                    bytes = (line + "\n").getBytes();
                } else {
                    bytes = (line + "\n").getBytes(charset);
                }
                client.write(handle, fileOffset, bytes, 0, bytes.length);
                fileOffset += bytes.length;
            }
        }
        handle.getClient().closeFile(handle);
    }

    /**
     * 获取文件上传器
     *
     * @param remote 远程文件绝对路径
     * @param local  本地文件
     * @return 文件上传器
     */
    public SftpUpload upload(String remote, File local) {
        return new SftpUpload(client, remote, local);
    }

    /**
     * 获取文件上传器
     *
     * @param remote 远程文件绝对路径
     * @param local  本地文件
     * @return 文件上传器
     */
    public SftpUpload upload(String remote, String local) {
        return new SftpUpload(client, remote, local);
    }

    /**
     * 获取文件下载器
     *
     * @param remote 远程文件绝对路径
     * @param local  本地文件
     * @return 文件上传器
     */
    public SftpDownload download(String remote, File local) {
        return new SftpDownload(client, remote, local);
    }

    /**
     * 获取文件下载器
     *
     * @param remote 远程文件绝对路径
     * @param local  本地文件
     * @return 文件下载器
     */
    public SftpDownload download(String remote, String local) {
        return new SftpDownload(client, remote, local);
    }

    // --------------- list ---------------

    /**
     * 文件列表 递归
     *
     * @param path 文件夹
     * @return 文件列表
     */
    public List<FileAttribute> listFiles(String path) {
        return this.listFiles(path, true, false);
    }

    /**
     * 文件列表
     *
     * @param path  文件夹
     * @param child 是否递归
     * @return 文件列表
     */
    public List<FileAttribute> listFiles(String path, boolean child) {
        return this.listFiles(path, child, false);
    }

    /**
     * 文件和文件夹列表 递归
     *
     * @param path 文件夹
     * @return 文件列表
     */
    public List<FileAttribute> listFilesAndDir(String path) {
        return this.listFiles(path, true, true);
    }

    /**
     * 文件和文件夹列表
     *
     * @param path  文件夹
     * @param child 是否递归
     * @return 文件列表
     */
    public List<FileAttribute> listFilesAndDir(String path, boolean child) {
        return this.listFiles(path, child, true);
    }

    /**
     * 文件列表
     *
     * @param path  文件夹
     * @param child 是否递归
     * @param dir   是否添加文件夹
     * @return 文件列表
     */
    private List<FileAttribute> listFiles(String path, boolean child, boolean dir) {
        List<FileAttribute> list = new ArrayList<>();
        try {
            List<FileAttribute> ls = this.ll(path);
            for (FileAttribute l : ls) {
                String fn = l.getFileName();
                if (l.isDirectory()) {
                    if (dir) {
                        list.add(l);
                    }
                    if (child) {
                        list.addAll(this.listFiles(Files1.getPath(path + "/" + fn), true, dir));
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

    /**
     * 文件夹列表
     *
     * @param path 文件夹
     * @return 文件列表
     */
    public List<FileAttribute> listDirs(String path) {
        return this.listDirs(path, true);
    }

    /**
     * 文件夹列表
     *
     * @param path  文件夹
     * @param child 是否递归
     * @return 文件列表
     */
    public List<FileAttribute> listDirs(String path, boolean child) {
        List<FileAttribute> list = new ArrayList<>();
        try {
            List<FileAttribute> ls = this.ll(path);
            for (FileAttribute l : ls) {
                String fn = l.getFileName();
                if (l.isDirectory()) {
                    list.add(l);
                    if (child) {
                        list.addAll(this.listDirs(Files1.getPath(path + "/" + fn), true));
                    }
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return list;
    }

    /**
     * 搜索文件
     *
     * @param path   文件夹
     * @param suffix 后缀
     * @return 文件
     */
    public List<FileAttribute> listFilesSuffix(String path, String suffix) {
        return this.listFilesSearch(path, suffix, null, null, 1, true, false);
    }

    /**
     * 搜索文件
     *
     * @param path   文件夹
     * @param suffix 后缀
     * @param child  是否递归
     * @return 文件
     */
    public List<FileAttribute> listFilesSuffix(String path, String suffix, boolean child) {
        return this.listFilesSearch(path, suffix, null, null, 1, child, false);
    }

    /**
     * 搜索文件和文件夹
     *
     * @param path   文件夹
     * @param suffix 后缀
     * @return 文件
     */
    public List<FileAttribute> listFilesAndDirSuffix(String path, String suffix) {
        return this.listFilesSearch(path, suffix, null, null, 1, true, true);
    }

    /**
     * 搜索文件和文件夹
     *
     * @param path   文件夹
     * @param suffix 后缀
     * @param child  是否递归
     * @return 文件
     */
    public List<FileAttribute> listFilesAndDirSuffix(String path, String suffix, boolean child) {
        return this.listFilesSearch(path, suffix, null, null, 1, child, true);
    }

    /**
     * 搜索文件
     *
     * @param path  文件夹
     * @param match 匹配
     * @return 文件
     */
    public List<FileAttribute> listFilesMatch(String path, String match) {
        return this.listFilesSearch(path, match, null, null, 2, true, false);
    }

    /**
     * 搜索文件
     *
     * @param path  文件夹
     * @param match 匹配
     * @param child 是否递归
     * @return 文件
     */
    public List<FileAttribute> listFilesMatch(String path, String match, boolean child) {
        return this.listFilesSearch(path, match, null, null, 2, child, false);
    }

    /**
     * 搜索文件和文件夹
     *
     * @param path  文件夹
     * @param match 匹配
     * @return 文件
     */
    public List<FileAttribute> listFilesAndDirMatch(String path, String match) {
        return this.listFilesSearch(path, match, null, null, 2, true, true);
    }

    /**
     * 搜索文件和文件夹
     *
     * @param path  文件夹
     * @param match 匹配
     * @param child 是否递归
     * @return 文件
     */
    public List<FileAttribute> listFilesAndDirMatch(String path, String match, boolean child) {
        return this.listFilesSearch(path, match, null, null, 2, child, true);
    }

    /**
     * 搜索文件
     *
     * @param path    文件夹
     * @param pattern 正则
     * @return 文件
     */
    public List<FileAttribute> listFilesPattern(String path, Pattern pattern) {
        return this.listFilesSearch(path, null, pattern, null, 3, true, false);
    }

    /**
     * 搜索文件
     *
     * @param path    文件夹
     * @param pattern 正则
     * @param child   是否递归
     * @return 文件
     */
    public List<FileAttribute> listFilesPattern(String path, Pattern pattern, boolean child) {
        return this.listFilesSearch(path, null, pattern, null, 3, child, false);
    }

    /**
     * 搜索文件和文件夹
     *
     * @param path    文件夹
     * @param pattern 正则
     * @return 文件
     */
    public List<FileAttribute> listFilesAndDirPattern(String path, Pattern pattern) {
        return this.listFilesSearch(path, null, pattern, null, 3, true, true);
    }

    /**
     * 搜索文件和文件夹
     *
     * @param path    文件夹
     * @param pattern 正则
     * @param child   是否递归
     * @return 文件
     */
    public List<FileAttribute> listFilesAndDirPattern(String path, Pattern pattern, boolean child) {
        return this.listFilesSearch(path, null, pattern, null, 3, child, true);
    }

    /**
     * 搜索文件
     *
     * @param path   文件夹
     * @param filter 过滤器
     * @return 文件
     */
    public List<FileAttribute> listFilesFilter(String path, FileAttributeFilter filter) {
        return this.listFilesSearch(path, null, null, filter, 4, true, false);
    }

    /**
     * 搜索文件
     *
     * @param path   文件夹
     * @param filter 过滤器
     * @param child  是否递归
     * @return 文件
     */
    public List<FileAttribute> listFilesFilter(String path, FileAttributeFilter filter, boolean child) {
        return this.listFilesSearch(path, null, null, filter, 4, child, false);
    }

    /**
     * 搜索文件和文件夹
     *
     * @param path   文件夹
     * @param filter 过滤器
     * @return 文件
     */
    public List<FileAttribute> listFilesAndDirFilter(String path, FileAttributeFilter filter) {
        return this.listFilesSearch(path, null, null, filter, 4, true, true);
    }

    /**
     * 搜索文件和文件夹
     *
     * @param path   文件夹
     * @param filter 过滤器
     * @param child  是否递归
     * @return 文件
     */
    public List<FileAttribute> listFilesAndDirFilter(String path, FileAttributeFilter filter, boolean child) {
        return this.listFilesSearch(path, null, null, filter, 4, child, true);
    }

    /**
     * 搜索文件
     *
     * @param path    文件夹
     * @param search  搜索
     * @param pattern 正则
     * @param filter  过滤器
     * @param type    类型 1后缀 2匹配 3正则 4过滤器
     * @param child   是否递归
     * @param dir     是否添加文件夹
     * @return 文件
     */
    private List<FileAttribute> listFilesSearch(String path, String search, Pattern pattern, FileAttributeFilter filter, int type, boolean child, boolean dir) {
        List<FileAttribute> list = new ArrayList<>();
        try {
            List<FileAttribute> files = this.ll(path);
            for (FileAttribute l : files) {
                String fn = l.getFileName();
                boolean isDir = l.isDirectory();
                if (!isDir || dir) {
                    boolean add = false;
                    if (type == 1 && fn.toLowerCase().endsWith(search.toLowerCase())) {
                        add = true;
                    } else if (type == 2 && fn.toLowerCase().contains(search.toLowerCase())) {
                        add = true;
                    } else if (type == 3 && Matches.test(fn, pattern)) {
                        add = true;
                    } else if (type == 4) {
                        if (filter.accept(l, path)) {
                            list.add(l);
                        }
                    }
                    if (add) {
                        list.add(l);
                    }
                }
                if (isDir && child) {
                    list.addAll(this.listFilesSearch(Files1.getPath(path + "/" + fn), search, pattern, filter, type, true, dir));
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return list;
    }

    // --------------- get ---------------

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

    /**
     * 关闭连接
     */
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
