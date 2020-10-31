package com.orion.remote.channel.executor.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.orion.lang.StreamEntry;
import com.orion.remote.channel.executor.BaseExecutor;
import com.orion.remote.channel.executor.sftp.bigfile.SftpDownload;
import com.orion.remote.channel.executor.sftp.bigfile.SftpUpload;
import com.orion.utils.Exceptions;
import com.orion.utils.Matches;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * SFTP执行器
 * <p>
 * 文件路必须是绝对路径 可以包含 ../ ./
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/7 19:52
 */
public class SftpExecutor extends BaseExecutor {

    /**
     * 默认缓冲区大小
     */
    private int bufferSize = 8 * 1024;

    private ChannelSftp channel;

    /**
     * 根目录
     */
    private String home;

    public SftpExecutor(ChannelSftp channel) {
        this(channel, null);
    }

    public SftpExecutor(ChannelSftp channel, String fileNameEncoding) {
        super(channel);
        this.channel = channel;
        if (fileNameEncoding != null) {
            try {
                channel.setFilenameEncoding(fileNameEncoding);
            } catch (SftpException e) {
                throw Exceptions.sftp("could not set fileNameEncoding", e);
            }
        }
    }

    @Override
    public SftpExecutor connect() {
        super.connect();
        try {
            this.home = channel.getHome();
        } catch (SftpException e) {
            throw Exceptions.sftp("could not read home path", e);
        }
        return this;
    }

    @Override
    public SftpExecutor connect(int timeout) {
        super.connect(timeout);
        try {
            this.home = channel.getHome();
        } catch (SftpException e) {
            throw Exceptions.sftp("could not read home path", e);
        }
        return this;
    }

    @Override
    public void exec() {
        this.connect();
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
     * 设置文件名称编码格式
     *
     * @param charset 编码格式
     * @return ignore
     */
    public boolean setFilenameEncoding(String charset) {
        try {
            channel.setFilenameEncoding(charset);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查文件是否存在
     *
     * @param path 文件绝对路径
     * @return true 存在
     */
    public boolean isExist(String path) {
        try {
            channel.realpath(path);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取文件路径
     *
     * @param path 文件绝对路径
     * @return 文件绝对路径 null文件不存在
     */
    public String getPath(String path) {
        try {
            return channel.realpath(path);
        } catch (Exception e) {
            if (SftpErrorMessage.NO_SUCH_FILE.getMessage().equals(e.getMessage())) {
                return null;
            } else {
                throw Exceptions.sftp(e);
            }
        }
    }

    /**
     * 获取连接文件的源文件
     *
     * @param path 连接文件的绝对路径
     * @return 源文件的路径 null文件不是连接文件
     */
    public String getLinkPath(String path) {
        try {
            return channel.readlink(path);
        } catch (Exception e) {
            if (SftpErrorMessage.NO_SUCH_FILE.getMessage().equals(e.getMessage())) {
                return null;
            } else if (SftpErrorMessage.BAD_MESSAGE.getMessage().equals(e.getMessage())) {
                return null;
            } else {
                throw Exceptions.sftp(e);
            }
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
            SftpATTRS attr;
            if (followSymbolic) {
                attr = channel.lstat(path);
            } else {
                attr = channel.stat(path);
            }
            return new FileAttribute(path, attr);
        } catch (Exception e) {
            if (SftpErrorMessage.NO_SUCH_FILE.getMessage().equals(e.getMessage())) {
                return null;
            } else {
                throw Exceptions.sftp(e);
            }
        }
    }

    /**
     * 设置文件属性
     *
     * @param attribute 文件属性
     * @return ignore
     */
    public boolean setFileAttribute(FileAttribute attribute) {
        try {
            channel.setStat(attribute.getPath(), attribute.getAttrs());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 设置文件修改时间
     *
     * @param path 文件绝对路径
     * @param date 修改时间
     * @return ignore
     */
    public boolean setModifyTime(String path, Date date) {
        try {
            channel.setMtime(path, (int) (date.getTime() / 1000));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 修改文件权限
     *
     * @param file       文件绝对路径
     * @param permission 8进制权限
     * @return ignore
     */
    public boolean chmod(String file, int permission) {
        try {
            channel.chmod(permission, file);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 修改文件所有人
     *
     * @param file 文件绝对路径
     * @param gid  用户id
     * @return ignore
     */
    public boolean chown(String file, int gid) {
        try {
            channel.chown(gid, file);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 修改文件所有组
     *
     * @param file 文件绝对路径
     * @param gid  组id
     * @return ignore
     */
    public boolean chgrp(String file, int gid) {
        try {
            channel.chgrp(gid, file);
            return true;
        } catch (Exception e) {
            return false;
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
            return this.touchTruncate(path);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取文件大小 如果不存在返回-1
     *
     * @param path 文件绝对路径
     * @return 文件大小
     */
    public long getSize(String path) {
        try {
            FileAttribute attr = getFileAttribute(path, false);
            if (attr == null || attr.isDirectory()) {
                return -1;
            }
            return attr.getSize();
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 获取目录文件属性
     *
     * @param path 文件绝对路径
     * @return 属性
     */
    public List<FileAttribute> ll(String path) {
        List<FileAttribute> list = new ArrayList<>();
        try {
            Vector files = channel.ls(path);
            for (Object l : files) {
                ChannelSftp.LsEntry ls = (ChannelSftp.LsEntry) l;
                String filename = ls.getFilename();
                if (".".equals(filename) || "..".equals(filename)) {
                    continue;
                }
                list.add(new FileAttribute(Files1.getPath(path + "/" + filename), ls.getLongname(), ls.getAttrs()));
            }
        } catch (Exception e) {
            // ignore
        }
        return list;
    }

    /**
     * 创建文件夹
     *
     * @param path 文件夹绝对路径
     * @return ignore
     */
    public boolean mkdirs(String path) {
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
                    channel.mkdir(parentPath);
                } catch (Exception e1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 创建文件
     *
     * @param path 文件绝对路径
     * @return ignore
     */
    public boolean touch(String path) {
        return touch(path, false);
    }

    /**
     * 创建文件 如果文件存在则截断
     *
     * @param path 文件绝对路径
     * @return ignore
     */
    public boolean touchTruncate(String path) {
        return touch(path, true);
    }

    /**
     * 创建文件
     *
     * @param path     文件绝对路径
     * @param truncate 如果文件存在是否截断
     * @return ignore
     */
    public boolean touch(String path, boolean truncate) {
        if (this.mkdirs(Files1.getParentPath(path))) {
            try {
                OutputStream t;
                if (truncate) {
                    t = channel.put(path, 0);
                } else {
                    t = channel.put(path, 1);
                }
                t.flush();
                Streams.close(t);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 创建硬连接文件
     *
     * @param source 原文件绝对路径
     * @param target 新文件绝对路径
     * @return ignore
     */
    public boolean touchHardLink(String source, String target) {
        return this.touchLink(source, target, true);
    }

    /**
     * 创建软连接文件
     *
     * @param source 原文件绝对路径
     * @param target 新文件绝对路径
     * @return ignore
     */
    public boolean touchSymLink(String source, String target) {
        return this.touchLink(source, target, false);
    }

    /**
     * 创建连接文件
     *
     * @param source 原文件绝对路径
     * @param target 新文件绝对路径
     * @param hard   true硬链接 false软连接
     * @return ignore
     */
    public boolean touchLink(String source, String target, boolean hard) {
        try {
            if (this.mkdirs(Files1.getParentPath(target))) {
                if (hard) {
                    channel.hardlink(source, target);
                } else {
                    channel.symlink(source, target);
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除一个空的文件夹
     *
     * @param path 绝对路径
     * @return ignore
     */
    public boolean rmdir(String path) {
        try {
            channel.rmdir(path);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除一个普通文件
     *
     * @param path 绝对路径
     * @return ignore
     */
    public boolean rmFile(String path) {
        try {
            channel.rm(path);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 递归删除文件或文件夹
     *
     * @param path 路径
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
                        channel.rm(f.getPath());
                    }
                }
                channel.rmdir(path);
            } else {
                channel.rm(path);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 移动文件
     *
     * @param source 原文件绝对路径
     * @param target 目标文件 绝对路径 相对路径都可以
     * @return ignore
     */
    public boolean mv(String source, String target) {
        try {
            source = Files1.getPath(source);
            target = Files1.getPath(target);
            if (target.charAt(0) == '/') {
                if (this.mkdirs(Files1.getParentPath(target))) {
                    channel.rename(source, Files1.normalize(target));
                } else {
                    return false;
                }
            } else {
                channel.rename(source, Files1.normalize(Files1.getPath(source + "/../" + target)));
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
     * 读取文件
     *
     * @param path   文件绝对路径
     * @param skip   跳过字节数
     * @param bs     字节数组
     * @param offset offset
     * @param len    len
     * @return read len
     * @throws IOException IOException
     */
    public int read(String path, long skip, byte[] bs, int offset, int len) throws IOException {
        InputStream in = null;
        try {
            in = channel.get(path, null, skip);
            return in.read(bs, offset, len);
        } catch (SftpException e) {
            throw Exceptions.sftp(e);
        } finally {
            Streams.close(in);
        }
    }

    // --------------- transfer ---------------

    public long transfer(String path, OutputStream out) throws IOException {
        return this.transfer(path, out, 0, -1);
    }

    public long transfer(String path, OutputStream out, long skip) throws IOException {
        return this.transfer(path, out, skip, -1);
    }

    public long transfer(String path, String file) throws IOException {
        Files1.touch(file);
        OutputStream out = null;
        try {
            return this.transfer(path, out = Files1.openOutputStream(file), 0, -1);
        } finally {
            Streams.close(out);
        }
    }

    public long transfer(String path, String file, long skip) throws IOException {
        Files1.touch(file);
        OutputStream out = null;
        try {
            return this.transfer(path, out = Files1.openOutputStream(file), skip, -1);
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

    public long transfer(String path, File file, long skip) throws IOException {
        Files1.touch(file);
        OutputStream out = null;
        try {
            return this.transfer(path, out = Files1.openOutputStream(file), skip, -1);
        } finally {
            Streams.close(out);
        }
    }

    /**
     * 读取字节到输出流
     *
     * @param path 文件绝对路径
     * @param out  输出流
     * @param skip 跳过字数
     * @param size 读取长度 -1 读取全部
     * @return 已读取长度
     * @throws IOException IOException
     */
    public long transfer(String path, OutputStream out, long skip, int size) throws IOException {
        long r = 0;
        byte[] bs = new byte[this.bufferSize];
        InputStream in = null;
        try {
            in = channel.get(path, null, skip);
            if (size != -1) {
                boolean mod = size % this.bufferSize == 0;
                long readTimes = size / this.bufferSize;
                if (mod || readTimes == 0) {
                    readTimes++;
                }
                for (int i = 0; i < readTimes; i++) {
                    if (readTimes == 1) {
                        int read = in.read(bs, 0, size);
                        out.write(bs, 0, read);
                        r += read;
                    } else {
                        int read = in.read(bs, 0, this.bufferSize);
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
                while ((read = in.read(bs, 0, this.bufferSize)) != -1) {
                    out.write(bs, 0, read);
                    r += read;
                }
            }
        } catch (SftpException e) {
            throw Exceptions.sftp(e);
        } finally {
            Streams.close(in);
        }
        return r;
    }

    // --------------- write ---------------

    public void write(String path, InputStream in) throws IOException {
        this.write(path, in, null, null, null, 0);
    }

    public void write(String path, byte[] bs) throws IOException {
        this.write(path, null, new StreamEntry(bs), null, null, 0);
    }

    public void write(String path, byte[] bs, int off, int len) throws IOException {
        this.write(path, null, new StreamEntry(bs, off, len), null, null, 0);
    }

    public void writeLine(String path, String line) throws IOException {
        this.write(path, null, new StreamEntry((line + "\n").getBytes()), null, null, 0);
    }

    public void writeLine(String path, String line, String charset) throws IOException {
        this.write(path, null, new StreamEntry((line + "\n").getBytes(charset)), null, null, 0);
    }

    public void writeLines(String path, List<String> lines) throws IOException {
        this.write(path, null, null, lines, null, 0);
    }

    public void writeLines(String path, List<String> lines, String charset) throws IOException {
        this.write(path, null, null, lines, charset, 0);
    }

    public void append(String path, InputStream in) throws IOException {
        this.write(path, in, null, null, null, 2);
    }

    public void append(String path, byte[] bs) throws IOException {
        this.write(path, null, new StreamEntry(bs), null, null, 2);
    }

    public void append(String path, byte[] bs, int off, int len) throws IOException {
        this.write(path, null, new StreamEntry(bs, off, len), null, null, 2);
    }

    public void appendLine(String path, String line) throws IOException {
        this.write(path, null, new StreamEntry((line + "\n").getBytes()), null, null, 2);
    }

    public void appendLine(String path, String line, String charset) throws IOException {
        this.write(path, null, new StreamEntry((line + "\n").getBytes(charset)), null, null, 2);
    }

    public void appendLines(String path, List<String> lines) throws IOException {
        this.write(path, null, null, lines, null, 2);
    }

    public void appendLines(String path, List<String> lines, String charset) throws IOException {
        this.write(path, null, null, lines, charset, 2);
    }

    /**
     * 拼接/写入到文件
     *
     * @param path    文件绝对路径
     * @param in      输入流
     * @param entry   写入信息
     * @param lines   行
     * @param charset 行编码
     * @param mode    0 完全覆盖模式
     *                1 恢复模式 如果文件正在传输时, 由于网络等原因导致传输中断, 则下一次传输相同的文件, 会从上一次中断的地方续传
     *                2 追加模式
     * @throws IOException IOException
     */
    private void write(String path, InputStream in, StreamEntry entry, List<String> lines, String charset, int mode) throws IOException {
        OutputStream out;
        try {
            out = channel.put(path, mode);
        } catch (SftpException e) {
            throw Exceptions.sftp();
        }
        if (in != null) {
            byte[] bs = new byte[bufferSize];
            int read;
            while ((read = in.read(bs)) != -1) {
                out.write(bs, 0, read);
            }
        } else if (entry != null) {
            out.write(entry.getBytes(), entry.getOff(), entry.getLen());
        } else if (lines != null) {
            for (String line : lines) {
                if (charset == null) {
                    out.write((line + "\n").getBytes());
                } else {
                    out.write((line + "\n").getBytes(charset));
                }
            }
        }
        out.flush();
        Streams.close(out);
    }

    /**
     * 获取文件上传器
     *
     * @param remote 远程文件绝对路径
     * @param local  本地文件
     * @return 文件上传器
     */
    public SftpUpload upload(String remote, File local) {
        return new SftpUpload(channel, remote, local);
    }

    /**
     * 获取文件上传器
     *
     * @param remote 远程文件绝对路径
     * @param local  本地文件
     * @return 文件上传器
     */
    public SftpUpload upload(String remote, String local) {
        return new SftpUpload(channel, remote, local);
    }

    /**
     * 获取文件下载器
     *
     * @param remote 远程文件绝对路径
     * @param local  本地文件
     * @return 文件上传器
     */
    public SftpDownload download(String remote, File local) {
        return new SftpDownload(channel, remote, local);
    }

    /**
     * 获取文件下载器
     *
     * @param remote 远程文件绝对路径
     * @param local  本地文件
     * @return 文件下载器
     */
    public SftpDownload download(String remote, String local) {
        return new SftpDownload(channel, remote, local);
    }

    // --------------- list ---------------

    /**
     * 文件列表 递归
     *
     * @param path 文件夹
     * @return 文件列表
     */
    public List<FileAttribute> listFiles(String path) {
        return this.listFiles(path, false, false);
    }

    /**
     * 文件列表
     *
     * @param path  文件夹绝对路径
     * @param child 是否递归子文件夹
     * @return 文件列表
     */
    public List<FileAttribute> listFiles(String path, boolean child) {
        return this.listFiles(path, child, false);
    }

    /**
     * 文件列表
     *
     * @param path  文件夹绝对路径
     * @param child 是否递归子文件夹
     * @param dir   是否添加文件夹
     * @return 文件列表
     */
    public List<FileAttribute> listFiles(String path, boolean child, boolean dir) {
        List<FileAttribute> list = new ArrayList<>();
        try {
            List<FileAttribute> ls = this.ll(path);
            for (FileAttribute l : ls) {
                if (l.isDirectory()) {
                    if (dir) {
                        list.add(l);
                    }
                    if (child) {
                        list.addAll(this.listFiles(Files1.getPath(path + "/" + l.getFileName()), true, dir));
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
     * @param path 文件夹绝对路径
     * @return 文件列表
     */
    public List<FileAttribute> listDirs(String path) {
        return this.listDirs(path, true);
    }

    /**
     * 文件夹列表
     *
     * @param path  文件夹绝对路径
     * @param child 是否递归
     * @return 文件列表
     */
    public List<FileAttribute> listDirs(String path, boolean child) {
        List<FileAttribute> list = new ArrayList<>();
        try {
            List<FileAttribute> ls = this.ll(path);
            for (FileAttribute l : ls) {
                if (l.isDirectory()) {
                    list.add(l);
                    if (child) {
                        list.addAll(this.listDirs(Files1.getPath(path + "/" + l.getFileName()), true));
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
     * @param path   文件夹绝对路径
     * @param suffix 后缀
     * @return 文件
     */
    public List<FileAttribute> listFilesSuffix(String path, String suffix) {
        return this.listFilesSearch(path, suffix, null, null, 1, false, false);
    }

    /**
     * 搜索文件
     *
     * @param path   文件夹绝对路径
     * @param suffix 后缀
     * @param child  是否递归子文件夹
     * @return 文件
     */
    public List<FileAttribute> listFilesSuffix(String path, String suffix, boolean child) {
        return this.listFilesSearch(path, suffix, null, null, 1, child, false);
    }

    /**
     * 搜索文件
     *
     * @param path   文件夹绝对路径
     * @param suffix 后缀
     * @param child  是否递归子文件夹
     * @param dir    是否添加文件夹
     * @return 文件
     */
    public List<FileAttribute> listFilesSuffix(String path, String suffix, boolean child, boolean dir) {
        return this.listFilesSearch(path, suffix, null, null, 1, child, dir);
    }

    /**
     * 搜索文件
     *
     * @param path  文件夹绝对路径
     * @param match 匹配
     * @return 文件
     */
    public List<FileAttribute> listFilesMatch(String path, String match) {
        return this.listFilesSearch(path, match, null, null, 2, false, false);
    }

    /**
     * 搜索文件
     *
     * @param path  文件夹绝对路径
     * @param match 匹配
     * @param child 是否递归子文件夹
     * @return 文件
     */
    public List<FileAttribute> listFilesMatch(String path, String match, boolean child) {
        return this.listFilesSearch(path, match, null, null, 2, child, false);
    }

    /**
     * 搜索文件
     *
     * @param path  文件夹绝对路径
     * @param match 匹配
     * @param child 是否递归子文件夹
     * @param dir   是否添加文件夹
     * @return 文件
     */
    public List<FileAttribute> listFilesMatch(String path, String match, boolean child, boolean dir) {
        return this.listFilesSearch(path, match, null, null, 2, child, dir);
    }

    /**
     * 搜索文件
     *
     * @param path    文件夹绝对路径
     * @param pattern 正则
     * @return 文件
     */
    public List<FileAttribute> listFilesPattern(String path, Pattern pattern) {
        return this.listFilesSearch(path, null, pattern, null, 3, false, false);
    }

    /**
     * 搜索文件
     *
     * @param path    文件夹绝对路径
     * @param pattern 正则
     * @param child   是否递归子文件夹
     * @return 文件
     */
    public List<FileAttribute> listFilesPattern(String path, Pattern pattern, boolean child) {
        return this.listFilesSearch(path, null, pattern, null, 3, child, false);
    }

    /**
     * 搜索文件
     *
     * @param path    文件夹绝对路径
     * @param pattern 正则
     * @param child   是否递归子文件夹
     * @param dir     是否添加文件夹
     * @return 文件
     */
    public List<FileAttribute> listFilesPattern(String path, Pattern pattern, boolean child, boolean dir) {
        return this.listFilesSearch(path, null, pattern, null, 3, child, dir);
    }

    /**
     * 搜索文件
     *
     * @param path   文件夹绝对路径
     * @param filter 过滤器
     * @return 文件
     */
    public List<FileAttribute> listFilesFilter(String path, FileAttributeFilter filter) {
        return this.listFilesSearch(path, null, null, filter, 4, false, false);
    }

    /**
     * 搜索文件
     *
     * @param path   文件夹绝对路径
     * @param filter 过滤器
     * @param child  是否递归子文件夹
     * @return 文件
     */
    public List<FileAttribute> listFilesFilter(String path, FileAttributeFilter filter, boolean child) {
        return this.listFilesSearch(path, null, null, filter, 4, child, false);
    }

    /**
     * 搜索文件
     *
     * @param path   文件夹绝对路径
     * @param filter 过滤器
     * @param child  是否递归子文件夹
     * @param dir    是否添加文件夹
     * @return 文件
     */
    public List<FileAttribute> listFilesFilter(String path, FileAttributeFilter filter, boolean child, boolean dir) {
        return this.listFilesSearch(path, null, null, filter, 4, child, dir);
    }

    /**
     * 搜索文件
     *
     * @param path    文件夹绝对路径
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
            List<FileAttribute> ls = this.ll(path);
            for (FileAttribute l : ls) {
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

    /**
     * 获取客户端版本
     *
     * @return 客户端版本
     */
    public String getClientVersion() {
        return channel.version();
    }

    /**
     * 获取服务端版本
     *
     * @return 服务端版本
     */
    public int getServerVersion() {
        try {
            return channel.getServerVersion();
        } catch (SftpException e) {
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public void close() {
        channel.disconnect();
    }

    @Override
    public ChannelSftp getChannel() {
        return channel;
    }

    public String getHome() {
        return home;
    }

    public int getBufferSize() {
        return bufferSize;
    }

}
