package com.orion.remote.sftp;

import ch.ethz.ssh2.*;
import com.orion.lang.StreamEntry;
import com.orion.utils.Exceptions;
import com.orion.utils.Matches;
import com.orion.utils.Streams;
import com.orion.utils.Strings;
import com.orion.utils.io.Files1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * SFTP 执行器
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/29 18:02
 */
public class SftpExecutor {

    // --------------- 大文件断点读写 ---------------

    /**
     * 默认权限
     */
    private static final Integer DEFAULT_POSIX_PERMISSIONS = 0700;

    /**
     * SFTP客户端
     */
    private SFTPv3Client client;

    /**
     * 一次 写入/读取 最大长度
     */
    private final int WRITE_MAX_SIZE = 32768;

    /**
     * 默认缓冲区大小 因为并行数为1 并且默认执行一次读写操作最大字节为32768
     */
    private int bufferSize = WRITE_MAX_SIZE;

    public SftpExecutor(Connection connection) {
        try {
            this.client = new SFTPv3Client(connection);
            // 默认并行数必须为1
            client.setRequestParallelism(1);
        } catch (IOException e) {
            throw Exceptions.ioRuntime("Init SFTP Client Error", e);
        }
    }

    public SftpExecutor(SFTPv3Client client) {
        this.client = client;
        // 默认并行数必须为1
        client.setRequestParallelism(1);
    }

    /**
     * 检查文件是否存在
     *
     * @param path 路径 可以包含 ./ ../
     * @return 绝对路径 null文件不存在
     */
    public String isExist(String path) {
        try {
            return this.client.canonicalPath(path);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取连接文件的源文件
     *
     * @param path 连接文件的路径
     * @return 源文件的路径 null文件不是连接文件
     */
    public String getLinkPath(String path) {
        try {
            return this.client.readLink(path);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 设置文件属性
     *
     * @param path 路径
     * @param attr 属性
     * @return true成功
     */
    public boolean setFileAttribute(String path, SFTPv3FileAttributes attr) {
        return this.setFileAttribute(path, null, null, attr);
    }

    /**
     * 设置文件属性
     *
     * @param path 路径
     * @param attr 属性
     * @return true成功
     */
    public boolean setFileAttribute(String path, FileAttribute attr) {
        return this.setFileAttribute(path, null, attr, null);
    }

    /**
     * 设置文件属性
     *
     * @param handle 文件处理器
     * @param attr   属性
     * @return true成功
     */
    public boolean setFileAttribute(SFTPv3FileHandle handle, FileAttribute attr) {
        return this.setFileAttribute(null, handle, attr, null);
    }

    /**
     * 设置文件属性
     *
     * @param handle 文件处理器
     * @param attr   属性
     * @return true成功
     */
    public boolean setFileAttribute(SFTPv3FileHandle handle, SFTPv3FileAttributes attr) {
        return this.setFileAttribute(null, handle, null, attr);
    }

    /**
     * 设置文件属性
     *
     * @param path       路径
     * @param handle     文件处理器
     * @param attr       属性
     * @param attributes 属性
     * @return true成功
     */
    private boolean setFileAttribute(String path, SFTPv3FileHandle handle, FileAttribute attr, SFTPv3FileAttributes attributes) {
        try {
            if (attributes == null) {
                attributes = this.toAttr(attr);
            }
            if (handle != null) {
                this.client.fsetstat(handle, attributes);
            } else {
                this.client.setstat(path, attributes);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取文件属性
     *
     * @param handle 文件处理器
     * @return 属性
     */
    public FileAttribute getFileAttribute(SFTPv3FileHandle handle) {
        try {
            return this.toAttr("", null, this.client.fstat(handle));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取文件属性
     *
     * @param path 路径
     * @return 属性
     */
    public FileAttribute getFileAttribute(String path) {
        return this.getFileAttribute(path, false);
    }

    /**
     * 获取文件属性
     *
     * @param path           路径
     * @param followSymbolic 是否遵循服务器上的符号链接
     * @return 属性
     */
    public FileAttribute getFileAttribute(String path, boolean followSymbolic) {
        try {
            SFTPv3FileAttributes attr;
            if (followSymbolic) {
                attr = this.client.stat(path);
            } else {
                attr = this.client.lstat(path);
            }
            return this.toAttr(path, null, attr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 清空文件, 没有则创建
     *
     * @param path 文件路径
     * @return 是否成功
     */
    public boolean clear(String path) {
        try {
            SFTPv3FileHandle clear = this.client.createFileTruncate(path);
            clear.getClient().closeFile(clear);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取文件大小 如果不存在则创建
     *
     * @param path 文件目录
     * @return 文件大小
     * @throws IOException 创建文件失败
     */
    public long getSize(String path) throws IOException {
        try {
            SFTPv3FileAttributes attr = this.client.stat(path);
            if (!attr.isDirectory()) {
                return attr.size;
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            SFTPv3FileHandle file = this.client.createFile(path);
            file.getClient().closeFile(file);
            return 0;
        }
    }

    /**
     * 获取目录文件属性
     *
     * @param path 路径
     * @return 属性
     */
    public List<FileAttribute> ll(String path) {
        try {
            List<FileAttribute> list = new ArrayList<>();
            List<SFTPv3DirectoryEntry> files = this.client.ls(path);
            for (SFTPv3DirectoryEntry l : files) {
                String filename = l.filename;
                if (".".equals(filename) || "..".equals(filename)) {
                    continue;
                }
                list.add(toAttr(Files1.getPath(path + "/" + filename), l.longEntry, l.attributes));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建文件夹
     *
     * @param path 路径
     * @return true成功
     */
    public boolean mkdir(String path) {
        return mkdirs(path, DEFAULT_POSIX_PERMISSIONS);
    }

    /**
     * 创建文件夹
     *
     * @param path             路径
     * @param posixPermissions 权限
     * @return true成功
     */
    public boolean mkdir(String path, int posixPermissions) {
        return mkdirs(path, posixPermissions);
    }

    /**
     * 创建文件夹
     *
     * @param path             路径
     * @param posixPermissions 权限
     * @return true成功
     */
    private boolean mkdirs(String path, int posixPermissions) {
        return mkdirs(this.client, path, posixPermissions);
    }

    /**
     * 创建文件夹
     *
     * @param client           client
     * @param path             路径
     * @param posixPermissions 权限
     * @return true成功
     */
    public static boolean mkdirs(SFTPv3Client client, String path, int posixPermissions) {
        try {
            SFTPv3FileAttributes p = client.stat(path);
            if (p.isDirectory()) {
                return true;
            } else {
                try {
                    client.mkdir(path, posixPermissions);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        } catch (Exception e) {
            // ignore
        }
        try {
            List<String> parentPaths = Files1.getParentPaths(path);
            String lastPath = parentPaths.get(parentPaths.size() - 1);
            int start = 0;
            try {
                SFTPv3FileAttributes lastFile = client.stat(lastPath);
                if (!lastFile.isDirectory()) {
                    start = parentPaths.size() - 1;
                } else {
                    start = parentPaths.size();
                }
            } catch (Exception e1) {
                // ignore
            }
            parentPaths.add(path);
            for (int size = parentPaths.size(); start < size; start++) {
                try {
                    client.mkdir(parentPaths.get(start), posixPermissions);
                } catch (Exception e1) {
                    if (start + 1 == size) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e1) {
            return false;
        }
    }

    /**
     * 删除一个空的文件夹
     *
     * @param path 路径
     * @return true成功
     */
    public boolean rmdir(String path) {
        try {
            this.client.rmdir(path);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除一个普通文件
     *
     * @param path 路径
     * @return true成功
     */
    public boolean rmFile(String path) {
        try {
            this.client.rm(path);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 递归删除文件或文件夹
     *
     * @param path 路径
     * @return true成功
     */
    public boolean rm(String path) {
        try {
            SFTPv3FileAttributes file = this.client.stat(path);
            if (file.isDirectory()) {
                List<SFTPv3DirectoryEntry> files = this.client.ls(path);
                for (SFTPv3DirectoryEntry f : files) {
                    if (".".equals(f.filename) || "..".equals(f.filename)) {
                        continue;
                    }
                    if (f.attributes.isDirectory()) {
                        this.rm(Files1.getPath(path + "/" + f.filename));
                    } else {
                        this.client.rm(Files1.getPath(path + "/" + f.filename));
                    }
                }
                this.client.rmdir(path);
            } else {
                this.client.rm(path);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 创建并打开文件 如果文件存在则直接打开
     *
     * @param path 文件路径
     * @return 文件处理器
     */
    public SFTPv3FileHandle touch(String path) {
        return this.touch(path, null, null, false);
    }

    /**
     * 创建并打开文件 如果文件存在则直接打开
     *
     * @param path 文件路径
     * @param attr 属性
     * @return 文件处理器
     */
    public SFTPv3FileHandle touch(String path, SFTPv3FileAttributes attr) {
        return this.touch(path, attr, null, false);
    }

    /**
     * 创建并打开文件 如果文件存在则直接打开
     *
     * @param path 文件路径
     * @param attr 属性
     * @return 文件处理器
     */
    public SFTPv3FileHandle touch(String path, FileAttribute attr) {
        return this.touch(path, null, attr, false);
    }

    /**
     * 创建并打开文件 如果文件存在则清除内容
     *
     * @param path 文件路径
     * @return 文件处理器
     */
    public SFTPv3FileHandle touchTruncate(String path) {
        return this.touch(path, null, null, true);

    }

    /**
     * 创建并打开文件 如果文件存在则清除内容
     *
     * @param path 文件路径
     * @param attr 属性
     * @return 文件处理器
     */
    public SFTPv3FileHandle touchTruncate(String path, SFTPv3FileAttributes attr) {
        return this.touch(path, attr, null, true);
    }

    /**
     * 创建并打开文件 如果文件存在则清除内容
     *
     * @param path 文件路径
     * @param attr 属性
     * @return 文件处理器
     */
    public SFTPv3FileHandle touchTruncate(String path, FileAttribute attr) {
        return this.touch(path, null, attr, true);
    }

    /**
     * 创建文件
     *
     * @param path       文件路径
     * @param attributes 属性
     * @param attr       属性
     * @param truncate   是否截断
     * @return 文件处理器
     */
    private SFTPv3FileHandle touch(String path, SFTPv3FileAttributes attributes, FileAttribute attr, boolean truncate) {
        try {
            if (this.mkdirs(Files1.getParentPath(path), DEFAULT_POSIX_PERMISSIONS)) {
                if (attr == null && attributes == null) {
                    if (truncate) {
                        this.client.createFileTruncate(path);
                    } else {
                        this.client.createFile(path);
                    }
                } else {
                    if (attributes == null) {
                        attributes = this.toAttr(attr);
                    }
                    if (truncate) {
                        this.client.createFileTruncate(path, attributes);
                    } else {
                        this.client.createFile(path, attributes);
                    }
                }
                return this.client.createFileTruncate(path);
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    /**
     * 创建连接文件
     *
     * @param linkFile   连接文件路径
     * @param targetFile 被连接文件路径
     * @return true成功
     */
    public boolean touchLink(String linkFile, String targetFile) {
        try {
            if (this.mkdirs(Files1.getParentPath(linkFile), DEFAULT_POSIX_PERMISSIONS)) {
                this.client.createSymlink(linkFile, targetFile);
                return true;
            }
        } catch (Exception e) {
            // ignore
        }
        return false;
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
                this.client.mv(source, Files1.normalize(target));
            } else {
                this.client.mv(source, Files1.normalize(Files1.getPath(source + "/../" + target)));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * SFTPv3FileAttributes -> FileAttribute
     *
     * @param path      文件路径
     * @param longEntry 文件信息
     * @param attr      文件属性
     * @return FileAttribute
     */
    private FileAttribute toAttr(String path, String longEntry, SFTPv3FileAttributes attr) {
        return new FileAttribute()
                .setAccessTime(attr.atime).setModifyTime(attr.mtime)
                .setSize(attr.size).setPath(path)
                .setUid(attr.uid).setGid(attr.gid)
                .setPermission(attr.permissions)
                .setDirectory(attr.isDirectory())
                .setLinkFile(attr.isSymlink())
                .setRegularFile(attr.isRegularFile())
                .setLongEntry(longEntry);
    }

    /**
     * FileAttribute -> SFTPv3FileAttributes
     *
     * @param attr 文件属性
     * @return SFTPv3FileAttributes
     */
    private SFTPv3FileAttributes toAttr(FileAttribute attr) {
        SFTPv3FileAttributes a = new SFTPv3FileAttributes();
        Integer gid = attr.getGid();
        Integer uid = attr.getUid();
        Long size = attr.getSize();
        Integer accessTime = attr.getAccessTime();
        Integer modifyTime = attr.getModifyTime();
        Integer permission = attr.getPermission();
        if (gid != null) {
            a.gid = gid;
        }
        if (uid != null) {
            a.uid = uid;
        }
        if (size != null) {
            a.size = size;
        }
        if (accessTime != null) {
            a.atime = accessTime;
        }
        if (modifyTime != null) {
            a.mtime = modifyTime;
        }
        if (permission != null) {
            a.permissions = permission;
        }
        return a;
    }

    // --------------- open ---------------

    /**
     * 获取文件操作处理器
     *
     * @param path  文件路径
     * @param flags 操作
     * @return 操作处理器
     */
    public SFTPv3FileHandle getFileHandler(String path, int flags) {
        return this.getFileHandler(path, flags, null, null);
    }

    /**
     * 获取文件操作处理器
     *
     * @param path  文件路径
     * @param flags 操作
     * @param attr  属性
     * @return 操作处理器
     */
    public SFTPv3FileHandle getFileHandler(String path, int flags, FileAttribute attr) {
        return this.getFileHandler(path, flags, attr, null);
    }

    /**
     * 获取文件操作处理器
     *
     * @param path  文件路径
     * @param flags 操作
     * @param attr  属性
     * @return 操作处理器
     */
    public SFTPv3FileHandle getFileHandler(String path, int flags, SFTPv3FileAttributes attr) {
        return this.getFileHandler(path, flags, null, attr);
    }

    /**
     * 获取文件操作处理器
     *
     * @param path       文件路径
     * @param flags      操作
     * @param attr       属性
     * @param attributes 属性
     * @return 操作处理器
     */
    private SFTPv3FileHandle getFileHandler(String path, int flags, FileAttribute attr, SFTPv3FileAttributes attributes) {
        try {
            return this.client.openFile(path, flags, attr == null ? attributes : this.toAttr(attr));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 打开文件夹处理器
     *
     * @param path 路径
     * @return 文件夹处理器
     */
    public SFTPv3FileHandle getDirectoryHandler(String path) {
        try {
            return this.client.openDirectory(path);
        } catch (Exception e) {
            return null;
        }
    }

    // --------------- handler ---------------

    /**
     * 获取文件读处理器
     *
     * @param path 文件
     * @return 文件处理器
     */
    public SFTPv3FileHandle getReadHandler(String path) {
        try {
            return this.client.openFileRO(path);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取文件写处理器
     *
     * @param path 文件
     * @return 文件处理器
     */
    public SFTPv3FileHandle getWriterHandler(String path) {
        try {
            return this.client.openFileRW(path);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取文件拼接处理器
     *
     * @param path 文件
     * @return 文件处理器
     */
    public SFTPv3FileHandle getAppendHandler(String path) {
        try {
            return this.client.openFileRWAppend(path);
        } catch (Exception e) {
            return null;
        }
    }

    // --------------- read ---------------

    public int read(String path, byte[] bs) throws IOException {
        return this.read(null, path, 0, bs, 0, bs.length);
    }

    public int read(String path, long skip, byte[] bs) throws IOException {
        return this.read(null, path, skip, bs, 0, bs.length);
    }

    public int read(String path, byte[] bs, int offset, int len) throws IOException {
        return this.read(null, path, 0, bs, offset, len);
    }

    public int read(String path, long skip, byte[] bs, int offset, int len) throws IOException {
        return this.read(null, path, skip, bs, offset, len);
    }

    public int read(SFTPv3FileHandle handle, byte[] bs) throws IOException {
        return this.read(handle, null, 0, bs, 0, bs.length);
    }

    public int read(SFTPv3FileHandle handle, long skip, byte[] bs) throws IOException {
        return this.read(handle, null, skip, bs, 0, bs.length);
    }

    public int read(SFTPv3FileHandle handle, byte[] bs, int offset, int len) throws IOException {
        return this.read(handle, null, 0, bs, offset, len);
    }

    public int read(SFTPv3FileHandle handle, long skip, byte[] bs, int offset, int len) throws IOException {
        return this.read(handle, null, skip, bs, offset, len);
    }

    /**
     * 读取字节
     *
     * @param handle 文件处理器
     * @param path   文件路径
     * @param skip   跳过字数
     * @param bs     数组
     * @param offset 偏移量
     * @param len    读取长度
     * @return 已读取长度
     * @throws IOException IOException
     */
    private int read(SFTPv3FileHandle handle, String path, long skip, byte[] bs, int offset, int len) throws IOException {
        boolean close = false;
        if (handle == null) {
            handle = this.client.openFileRO(path);
            close = true;
        }
        int read = this.client.read(handle, skip, bs, offset, len);
        if (close) {
            handle.getClient().closeFile(handle);
        }
        return read;
    }

    // --------------- transfer ---------------

    public long transfer(String path, OutputStream out, long size) throws IOException {
        return this.transfer(null, path, out, 0, size);
    }

    public long transfer(String path, OutputStream out, long skip, long size) throws IOException {
        return this.transfer(null, path, out, skip, size);
    }

    public long transfer(SFTPv3FileHandle handle, OutputStream out, long size) throws IOException {
        return this.transfer(handle, null, out, 0, size);
    }

    public long transfer(SFTPv3FileHandle handle, OutputStream out, long skip, long size) throws IOException {
        return this.transfer(handle, null, out, skip, size);
    }

    /**
     * 读取字节到输出流
     *
     * @param handle 文件处理器
     * @param path   文件路径
     * @param out    输出流
     * @param skip   跳过字数
     * @param size   读取长度
     * @return 已读取长度
     * @throws IOException IOException
     */
    private long transfer(SFTPv3FileHandle handle, String path, OutputStream out, long skip, long size) throws IOException {
        boolean close = false;
        if (handle == null) {
            handle = this.client.openFileRO(path);
            close = true;
        }
        long start = skip;
        byte[] bs = new byte[this.bufferSize];
        boolean mod = size % this.bufferSize == 0;
        long readTimes = size / this.bufferSize;
        if (mod || readTimes == 0) {
            readTimes++;
        }
        for (int i = 0; i < readTimes; i++) {
            int read = this.client.read(handle, skip, bs, 0, this.bufferSize);
            if (read != -1) {
                out.write(bs, 0, read);
                skip += read;
            } else {
                break;
            }
        }
        if (close) {
            handle.getClient().closeFile(handle);
        }
        return skip - start;
    }

    public long transfer(String path, String file) throws IOException {
        Files1.touch(file);
        OutputStream out = null;
        try {
            return this.transfer(null, path, out = Files1.openOutputStream(file));
        } finally {
            Streams.closeQuietly(out);
        }
    }

    public long transfer(SFTPv3FileHandle handle, String file) throws IOException {
        Files1.touch(file);
        OutputStream out = null;
        try {
            return this.transfer(handle, null, out = Files1.openOutputStream(file));
        } finally {
            Streams.closeQuietly(out);
        }
    }

    public long transfer(String path, File file) throws IOException {
        Files1.touch(file);
        OutputStream out = null;
        try {
            return this.transfer(null, path, out = Files1.openOutputStream(file));
        } finally {
            Streams.closeQuietly(out);
        }
    }

    public long transfer(SFTPv3FileHandle handle, File file) throws IOException {
        Files1.touch(file);
        OutputStream out = null;
        try {
            return this.transfer(handle, null, out = Files1.openOutputStream(file));
        } finally {
            Streams.closeQuietly(out);
        }
    }

    public long transfer(String path, OutputStream out) throws IOException {
        return this.transfer(null, path, out);
    }

    public long transfer(SFTPv3FileHandle handle, OutputStream out) throws IOException {
        return this.transfer(handle, null, out);
    }

    /**
     * 读取字节到输出流
     *
     * @param handle 文件处理器
     * @param path   路径
     * @param out    outputStream
     */
    private long transfer(SFTPv3FileHandle handle, String path, OutputStream out) throws IOException {
        boolean close = false;
        if (handle == null) {
            handle = this.client.openFileRO(path);
            close = true;
        }
        long skip = 0;
        byte[] bs = new byte[this.bufferSize];
        while (true) {
            int read = this.client.read(handle, skip, bs, 0, this.bufferSize);
            if (read != -1) {
                out.write(bs, 0, read);
                skip += read;
            } else {
                break;
            }
        }
        if (close) {
            handle.getClient().closeFile(handle);
        }
        return skip;
    }

    // --------------- write ---------------

    public void write(SFTPv3FileHandle handle, InputStream in) throws IOException {
        this.write(handle, null, 0, in, null);
    }

    public void write(String path, InputStream in) throws IOException {
        this.write(null, path, 0, in, null);
    }

    public void write(SFTPv3FileHandle handle, InputStream in, long fileOffset) throws IOException {
        this.write(handle, null, fileOffset, in, null);
    }

    public void write(String path, InputStream in, long fileOffset) throws IOException {
        this.write(null, path, fileOffset, in, null);
    }

    public void write(SFTPv3FileHandle handle, byte[] bs) throws IOException {
        this.write(handle, null, 0, null, new StreamEntry(bs));
    }

    public void write(String path, byte[] bs) throws IOException {
        this.write(null, path, 0, null, new StreamEntry(bs));
    }

    public void write(SFTPv3FileHandle handle, byte[] bs, int off, int len) throws IOException {
        this.write(handle, null, 0, null, new StreamEntry(bs, off, len));
    }

    public void write(String path, byte[] bs, int off, int len) throws IOException {
        this.write(null, path, 0, null, new StreamEntry(bs, off, len));
    }

    public void write(SFTPv3FileHandle handle, long fileOffset, byte[] bs) throws IOException {
        this.write(handle, null, fileOffset, null, new StreamEntry(bs));
    }

    public void write(String path, long fileOffset, byte[] bs) throws IOException {
        this.write(null, path, fileOffset, null, new StreamEntry(bs));
    }

    public void write(SFTPv3FileHandle handle, long fileOffset, byte[] bs, int off, int len) throws IOException {
        this.write(handle, null, fileOffset, null, new StreamEntry(bs, off, len));
    }

    public void write(String path, long fileOffset, byte[] bs, int off, int len) throws IOException {
        this.write(null, path, fileOffset, null, new StreamEntry(bs, off, len));
    }

    public void writeLine(SFTPv3FileHandle handle, String line) throws IOException {
        this.write(handle, null, 0, null, new StreamEntry((line + "\n").getBytes()));
    }

    public void writeLine(String path, String line) throws IOException {
        this.write(null, path, 0, null, new StreamEntry((line + "\n").getBytes()));
    }

    public void writeLine(SFTPv3FileHandle handle, long fileOffset, String line) throws IOException {
        this.write(handle, null, fileOffset, null, new StreamEntry((line + "\n").getBytes()));
    }

    public void writeLine(String path, long fileOffset, String line) throws IOException {
        this.write(null, path, fileOffset, null, new StreamEntry((line + "\n").getBytes()));
    }

    public void writeLines(SFTPv3FileHandle handle, List<String> lines) throws IOException {
        this.write(handle, null, 0, null, new StreamEntry((Strings.join(lines, "\n")).getBytes()));
    }

    public void writeLines(String path, List<String> lines) throws IOException {
        this.write(null, path, 0, null, new StreamEntry((Strings.join(lines, "\n")).getBytes()));
    }

    public void writeLines(SFTPv3FileHandle handle, long fileOffset, List<String> lines) throws IOException {
        this.write(handle, null, fileOffset, null, new StreamEntry((Strings.join(lines, "\n")).getBytes()));
    }

    public void writeLines(String path, long fileOffset, List<String> lines) throws IOException {
        this.write(null, path, fileOffset, null, new StreamEntry((Strings.join(lines, "\n")).getBytes()));
    }

    public void append(String path, InputStream in) throws IOException {
        this.write(null, path, -1, in, null);
    }

    public void append(String path, byte[] bs) throws IOException {
        this.write(null, path, -1, null, new StreamEntry(bs));
    }

    public void append(String path, byte[] bs, int off, int len) throws IOException {
        this.write(null, path, -1, null, new StreamEntry(bs, off, len));
    }

    public void appendLine(String path, String line) throws IOException {
        this.write(null, path, -1, null, new StreamEntry((line + "\n").getBytes()));
    }

    public void appendLines(String path, List<String> lines) throws IOException {
        this.write(null, path, -1, null, new StreamEntry((Strings.join(lines, "\n")).getBytes()));
    }

    /**
     * 拼接/写入到文件 不会清除只会覆盖
     *
     * @param handle     处理器
     * @param path       文件路径
     * @param in         输入流
     * @param fileOffset 文件偏移量
     * @param entry      写入信息
     * @throws IOException IOException
     */
    private void write(SFTPv3FileHandle handle, String path, long fileOffset, InputStream in, StreamEntry entry) throws IOException {
        boolean close = false;
        if (handle == null) {
            if (fileOffset == -1) {
                SFTPv3FileAttributes attr = this.client.stat(path);
                fileOffset = attr.size;
            }
            handle = this.client.openFileRW(path);
            close = true;
        }
        if (in == null) {
            this.client.write(handle, fileOffset, entry.getBytes(), entry.getOff(), entry.getLen());
        } else {
            byte[] bs = new byte[WRITE_MAX_SIZE];
            int read;
            while ((read = in.read(bs)) != -1) {
                this.client.write(handle, fileOffset, bs, 0, read);
                fileOffset += read;
            }
        }
        if (close) {
            handle.getClient().closeFile(handle);
        }
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
            List<SFTPv3DirectoryEntry> ls = this.client.ls(path);
            for (SFTPv3DirectoryEntry l : ls) {
                if (".".equals(l.filename) || "..".equals(l.filename)) {
                    continue;
                }
                if (l.attributes.isDirectory()) {
                    if (dir) {
                        list.add(toAttr(Files1.getPath(path + "/" + l.filename), l.longEntry, l.attributes));
                    }
                    if (child) {
                        list.addAll(this.listFiles(Files1.getPath(path + "/" + l.filename), true, dir));
                    }
                } else {
                    list.add(toAttr(Files1.getPath(path + "/" + l.filename), l.longEntry, l.attributes));
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
            List<SFTPv3DirectoryEntry> ls = this.client.ls(path);
            for (SFTPv3DirectoryEntry l : ls) {
                if (".".equals(l.filename) || "..".equals(l.filename)) {
                    continue;
                }
                if (l.attributes.isDirectory()) {
                    list.add(toAttr(Files1.getPath(path + "/" + l.filename), l.longEntry, l.attributes));
                    if (child) {
                        list.addAll(this.listDirs(Files1.getPath(path + "/" + l.filename), true));
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
            List<SFTPv3DirectoryEntry> ls = this.client.ls(path);
            for (SFTPv3DirectoryEntry l : ls) {
                if (".".equals(l.filename) || "..".equals(l.filename)) {
                    continue;
                }
                boolean isDir = l.attributes.isDirectory();
                if (!isDir || dir) {
                    String fn = l.filename;
                    boolean add = false;
                    if (type == 1 && fn.toLowerCase().endsWith(search.toLowerCase())) {
                        add = true;
                    } else if (type == 2 && fn.toLowerCase().contains(search.toLowerCase())) {
                        add = true;
                    } else if (type == 3 && Matches.test(fn, pattern)) {
                        add = true;
                    } else if (type == 4) {
                        FileAttribute attr = toAttr(Files1.getPath(path + "/" + l.filename), l.longEntry, l.attributes);
                        if (filter.accept(attr, path)) {
                            list.add(attr);
                        }
                    }
                    if (add) {
                        list.add(toAttr(Files1.getPath(path + "/" + l.filename), l.longEntry, l.attributes));
                    }
                }
                if (isDir && child) {
                    list.addAll(this.listFilesSearch(Files1.getPath(path + "/" + l.filename), search, pattern, filter, type, true, dir));
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
            this.client.closeFile(handle);
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
        return this.client.getProtocolVersion();
    }

    /**
     * 获取编码格式
     *
     * @return 编码格式
     */
    public String getCharset() {
        return this.client.getCharset();
    }

    /**
     * 设置编码格式
     *
     * @param charset 编码格式
     * @throws IOException IOException
     */
    public SftpExecutor setCharset(String charset) throws IOException {
        this.client.setCharset(charset);
        return this;
    }

    /**
     * 设置请求并行数量
     *
     * @param parallelism 请求并行数量
     */
    public SftpExecutor setRequestParallelism(int parallelism) {
        this.client.setRequestParallelism(parallelism);
        return this;
    }

    /**
     * 连接是否活跃
     *
     * @return true活跃
     */
    public boolean isConnected() {
        return this.client.isConnected();
    }

    /**
     * 关闭连接
     */
    public void close() {
        this.client.close();
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

}
