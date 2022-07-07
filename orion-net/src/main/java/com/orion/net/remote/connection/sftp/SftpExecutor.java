package com.orion.net.remote.connection.sftp;

import ch.ethz.ssh2.SFTPv3Client;
import ch.ethz.ssh2.SFTPv3DirectoryEntry;
import ch.ethz.ssh2.SFTPv3FileAttributes;
import ch.ethz.ssh2.SFTPv3FileHandle;
import com.orion.lang.constant.Const;
import com.orion.lang.define.StreamEntry;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.Streams;
import com.orion.net.base.file.sftp.BaseSftpExecutor;
import com.orion.net.base.file.sftp.SftpErrorMessage;
import com.orion.net.base.file.sftp.SftpFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * sftp 执行器
 * <p>
 * 文件路必须是绝对路径 可以包含 ../ ./
 * <p>
 * 默认执行一次读写操作最大字节为32768
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/5/18 16:38
 */
public class SftpExecutor extends BaseSftpExecutor {

    /**
     * 默认8进制权限
     */
    private static final Integer DEFAULT_PERMISSIONS = Files1.permission10to8(644);

    /**
     * sftp 连接
     */
    private final SFTPv3Client client;

    public SftpExecutor(SFTPv3Client client) {
        this(client, Const.UTF_8);
    }

    public SftpExecutor(SFTPv3Client client, String charset) {
        this.client = client;
        this.charset = charset;
        try {
            client.setCharset(charset);
        } catch (IOException e) {
            throw Exceptions.unsupportedEncoding(e);
        }
    }

    @Override
    public void bufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override
    public void charset(String charset) {
        try {
            client.setCharset(charset);
            this.charset = charset;
        } catch (IOException e) {
            throw Exceptions.unsupportedEncoding(e);
        }
    }

    /**
     * 设置请求并行数量
     *
     * @param parallelism 请求并行数量
     */
    public void requestParallelism(int parallelism) {
        client.setRequestParallelism(parallelism);
    }

    @Override
    public boolean isExist(String path) {
        try {
            return client.lstat(path) != null;
        } catch (Exception e) {
            if (SftpErrorMessage.NO_SUCH_FILE.isCause(e)) {
                return false;
            }
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public String getPath(String path) {
        try {
            return client.canonicalPath(path);
        } catch (Exception e) {
            if (SftpErrorMessage.NO_SUCH_FILE.isCause(e)) {
                return null;
            }
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public String getLinkPath(String path) {
        try {
            return client.readLink(path);
        } catch (Exception e) {
            if (SftpErrorMessage.NO_SUCH_FILE.isCause(e)) {
                return null;
            } else if (SftpErrorMessage.BAD_MESSAGE.isCause(e)) {
                return null;
            }
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public SftpFile getFile(String path) {
        return this.getFile(path, false);
    }

    @Override
    public SftpFile getFile(String path, boolean followSymbolic) {
        try {
            SFTPv3FileAttributes attr;
            if (followSymbolic) {
                attr = client.stat(path);
            } else {
                attr = client.lstat(path);
            }
            return new SftpFileWrapper(path, attr);
        } catch (Exception e) {
            if (SftpErrorMessage.NO_SUCH_FILE.isCause(e)) {
                return null;
            }
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public long getSize(String path) {
        try {
            SFTPv3FileAttributes attr = client.stat(path);
            if (attr == null) {
                return -1;
            }
            return attr.size;
        } catch (IOException e) {
            if (SftpErrorMessage.NO_SUCH_FILE.isCause(e)) {
                return -1;
            }
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public void setFileAttribute(SftpFile attribute) {
        try {
            client.setstat(attribute.getPath(), ((SftpFileWrapper) attribute).getAttrs());
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public void setModifyTime(String path, Date date) {
        try {
            SFTPv3FileAttributes attr = client.stat(path);
            attr.mtime = (int) (date.getTime() / Const.MS_S_1);
            client.setstat(path, attr);
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public void chmod(String file, int permission) {
        try {
            SFTPv3FileAttributes attr = new SFTPv3FileAttributes();
            attr.permissions = Files1.permission10to8(permission);
            client.setstat(file, attr);
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public void chown(String file, int uid) {
        try {
            SFTPv3FileAttributes attr = client.stat(file);
            attr.uid = uid;
            client.setstat(file, attr);
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public void chgrp(String file, int gid) {
        try {
            SFTPv3FileAttributes attr = client.stat(file);
            attr.gid = gid;
            client.setstat(file, attr);
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public void truncate(String path) {
        this.touch(path, true);
    }

    @Override
    public void mkdir(String path) {
        this.mkdir(path, DEFAULT_PERMISSIONS);
    }

    /**
     * 创建文件夹
     *
     * @param path        path
     * @param permissions 权限
     */
    public void mkdir(String path, int permissions) {
        try {
            client.mkdir(path, permissions);
        } catch (IOException e) {
            throw Exceptions.sftp(e);
        }
    }

    /**
     * 创建文件夹 递归
     *
     * @param path        path
     * @param permissions 权限
     */
    public void mkdirs(String path, int permissions) {
        super.doMakeDirs(path, p -> mkdir(path, permissions));
    }

    @Override
    public void removeDir(String path) {
        try {
            client.rmdir(path);
        } catch (Exception e) {
            if (SftpErrorMessage.NO_SUCH_FILE.isCause(e)) {
                return;
            }
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public void removeFile(String path) {
        try {
            client.rm(path);
        } catch (Exception e) {
            if (SftpErrorMessage.NO_SUCH_FILE.isCause(e)) {
                return;
            }
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public void touch(String path) {
        this.touch(path, false);
    }

    @Override
    public void touchTruncate(String path) {
        this.touch(path, true);
    }

    @Override
    public void touch(String path, boolean truncate) {
        try {
            this.mkdirs(Files1.getParentPath(path));
            SFTPv3FileHandle handle;
            if (truncate) {
                handle = client.createFileTruncate(path);
            } else {
                handle = client.createFile(path);
            }
            this.closeFile(handle);
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public void touchLink(String source, String target) {
        try {
            // 检查是否需要创建目标文件目录
            if (!this.isSameParentPath(source, target)) {
                this.mkdirs(Files1.getParentPath(target));
            }
            client.createSymlink(target, source);
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
    }

    @Override
    protected void doMove(String source, String target) {
        try {
            client.mv(source, target);
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
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
            throw Exceptions.sftp(e);
        }
    }

    /**
     * 关闭文件
     *
     * @param handle 文件处理器
     * @return true成功
     */
    public boolean closeFile(SFTPv3FileHandle handle) {
        if (handle == null) {
            return true;
        }
        try {
            handle.getClient().closeFile(handle);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // -------------------- read --------------------

    public int read(SFTPv3FileHandle handle, byte[] bs) throws IOException {
        return this.read(handle, 0, bs, 0, bs.length);
    }

    public int read(SFTPv3FileHandle handle, long skip, byte[] bs) throws IOException {
        return this.read(handle, skip, bs, 0, bs.length);
    }

    public int read(SFTPv3FileHandle handle, byte[] bs, int offset, int len) throws IOException {
        return this.read(handle, 0, bs, offset, len);
    }

    public int read(SFTPv3FileHandle handle, long skip, byte[] bs, int offset, int len) throws IOException {
        return client.read(handle, skip, bs, offset, len);
    }

    @Override
    public int read(String path, long skip, byte[] bs, int offset, int len) throws IOException {
        SFTPv3FileHandle handle = null;
        try {
            handle = client.openFileRO(path);
            return this.read(handle, skip, bs, offset, len);
        } catch (Exception e) {
            throw Exceptions.io("cannot read file " + path, e);
        } finally {
            this.closeFile(handle);
        }
    }

    // -------------------- transfer --------------------

    @Override
    protected long doTransfer(String path, OutputStream out, long skip, int size, boolean close) throws IOException {
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
            this.closeFile(handle);
        }
    }

    public long transfer(SFTPv3FileHandle handle, String file) throws IOException {
        Files1.touch(file);
        return this.transfer(handle, Files1.openOutputStream(file), 0, -1, true);
    }

    public long transfer(SFTPv3FileHandle handle, File file) throws IOException {
        Files1.touch(file);
        return this.transfer(handle, Files1.openOutputStream(file), 0, -1, true);
    }

    public long transfer(SFTPv3FileHandle handle, OutputStream out) throws IOException {
        return this.transfer(handle, out, 0, -1, false);
    }

    public long transfer(SFTPv3FileHandle handle, OutputStream out, long skip) throws IOException {
        return this.transfer(handle, out, skip, -1, false);
    }

    public long transfer(SFTPv3FileHandle handle, OutputStream out, long skip, int size) throws IOException {
        return this.transfer(handle, out, skip, size, false);
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
            byte[] bs = new byte[bufferSize];
            if (size != -1) {
                int last = size % bufferSize;
                long readTimes = Long.max(size / bufferSize, 1);
                if (last == 0) {
                    readTimes++;
                }
                for (long i = 0; i < readTimes; i++) {
                    int read;
                    if (i == readTimes - 1) {
                        read = client.read(handle, curr, bs, 0, last);
                    } else {
                        read = client.read(handle, curr, bs, 0, bufferSize);
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
                while ((read = client.read(handle, curr, bs, 0, bufferSize)) != -1) {
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

    @Override
    protected void doWrite(String path, InputStream in, StreamEntry entry, List<String> lines) throws IOException {
        this.write(path, 0, in, entry, lines, 1);
    }

    @Override
    protected void doAppend(String path, InputStream in, StreamEntry entry, List<String> lines) throws IOException {
        this.write(path, 0, in, entry, lines, 3);
    }

    // -------------------- override --------------------

    public void override(String path, InputStream in) throws IOException {
        this.write(path, 0, in, null, null, 2);
    }

    public void override(String path, long fileOffset, InputStream in) throws IOException {
        this.write(path, fileOffset, in, null, null, 2);
    }

    public void override(String path, byte[] bs) throws IOException {
        this.write(path, 0, null, new StreamEntry(bs), null, 2);
    }

    public void override(String path, byte[] bs, int off, int len) throws IOException {
        this.write(path, 0, null, new StreamEntry(bs, off, len), null, 2);
    }

    public void override(String path, long fileOffset, byte[] bs) throws IOException {
        this.write(path, fileOffset, null, new StreamEntry(bs), null, 2);
    }

    public void override(String path, long fileOffset, byte[] bs, int off, int len) throws IOException {
        this.write(path, fileOffset, null, new StreamEntry(bs, off, len), null, 2);
    }

    public void overrideLine(String path, String line) throws IOException {
        this.write(path, 0, null, null, Lists.singleton(line), 2);
    }

    public void overrideLine(String path, long fileOffset, String line) throws IOException {
        this.write(path, fileOffset, null, null, Lists.singleton(line), 2);
    }

    public void overrideLines(String path, List<String> lines) throws IOException {
        this.write(path, 0, null, null, lines, 2);
    }

    public void overrideLines(String path, long fileOffset, List<String> lines) throws IOException {
        this.write(path, fileOffset, null, null, lines, 2);
    }

    /**
     * 拼接/替换/写入到文件
     *
     * @param path       文件绝对路径
     * @param in         输入流
     * @param fileOffset 文件偏移量
     * @param entry      写入信息
     * @param lines      行
     * @param type       1write  2override  3append
     * @throws IOException IOException
     */
    private void write(String path, long fileOffset, InputStream in, StreamEntry entry, List<String> lines, int type) throws IOException {
        SFTPv3FileHandle handle = null;
        try {
            this.touch(path, type == 1);
            if (type == 3) {
                handle = client.openFileRWAppend(path);
            } else {
                handle = client.openFileRW(path);
            }
            this.write(handle, fileOffset, in, entry, lines);
        } catch (Exception e) {
            throw Exceptions.io("cannot write file " + path, e);
        } finally {
            this.closeFile(handle);
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

    // -------------------- bigfile --------------------

    @Override
    public SftpUploader upload(String remote, File local) {
        return new SftpUploader(this, remote, local);
    }

    @Override
    public SftpUploader upload(String remote, String local) {
        return new SftpUploader(this, remote, local);
    }

    @Override
    public SftpDownloader download(String remote, File local) {
        return new SftpDownloader(this, remote, local);
    }

    @Override
    public SftpDownloader download(String remote, String local) {
        return new SftpDownloader(this, remote, local);
    }

    // -------------------- list --------------------

    @Override
    public List<SftpFile> ll(String path) {
        try {
            List<SftpFile> list = new ArrayList<>();
            List<SFTPv3DirectoryEntry> files = client.ls(path);
            for (SFTPv3DirectoryEntry l : files) {
                String filename = l.filename;
                if (".".equals(filename) || "..".equals(filename)) {
                    continue;
                }
                list.add(new SftpFileWrapper(Files1.getPath(path, filename), l.attributes));
            }
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean isConnected() {
        return client.isConnected();
    }

    @Override
    public int getServerVersion() {
        return client.getProtocolVersion();
    }

    @Override
    public void close() {
        client.close();
    }

    public SFTPv3Client getClient() {
        return client;
    }

}
