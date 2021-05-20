package com.orion.remote.channel.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.orion.constant.Const;
import com.orion.lang.StreamEntry;
import com.orion.remote.channel.BaseExecutor;
import com.orion.remote.channel.sftp.bigfile.SftpDownload;
import com.orion.remote.channel.sftp.bigfile.SftpUpload;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.Valid;
import com.orion.utils.collect.Lists;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import java.io.*;
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
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/7 19:52
 */
public class SftpExecutor extends BaseExecutor {

    /**
     * 分隔符
     */
    private static final String SEPARATOR = Const.SLASH;

    /**
     * 默认缓冲区大小
     */
    private int bufferSize;

    private ChannelSftp channel;

    /**
     * 根目录
     */
    private String home;

    /**
     * 编码格式
     */
    private String charset;

    public SftpExecutor(ChannelSftp channel) {
        this(channel, Const.UTF_8);
    }

    public SftpExecutor(ChannelSftp channel, String charset) {
        super(channel);
        Valid.notNull(charset, "charset is empty");
        this.channel = channel;
        this.bufferSize = Const.BUFFER_KB_8;
        this.charset = charset;
    }

    @Override
    public SftpExecutor connect() {
        super.connect();
        try {
            this.home = channel.getHome();
            this.filenameEncoding(charset);
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
            this.filenameEncoding(charset);
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
    public SftpExecutor bufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }

    /**
     * 设置文件名称编码格式
     *
     * @param charset 编码格式
     * @return ignore
     */
    public boolean filenameEncoding(String charset) {
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
            SftpATTRS attr;
            if (followSymbolic) {
                attr = channel.stat(path);
            } else {
                attr = channel.lstat(path);
            }
            return new SftpFile(path, attr);
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
    public boolean setFileAttribute(SftpFile attribute) {
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
            channel.setMtime(path, (int) (date.getTime() / Const.MS_S_1));
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
     * @param uid  用户id
     * @return ignore
     */
    public boolean chown(String file, int uid) {
        try {
            channel.chown(uid, file);
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
            SftpFile attr = getFile(path, false);
            if (attr == null || attr.isDirectory()) {
                return -1;
            }
            return attr.getSize();
        } catch (Exception e) {
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
        SftpFile p = this.getFile(path, false);
        if (p != null && p.isDirectory()) {
            return true;
        }
        List<String> parentPaths = Files1.getParentPaths(path);
        parentPaths.add(path);
        boolean check = true;
        for (int i = 1, size = parentPaths.size(); i < size; i++) {
            String parentPath = parentPaths.get(i);
            if (check) {
                SftpFile parentAttr = this.getFile(parentPath, false);
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
                    t = this.getOutputStream(path, 0);
                } else {
                    t = this.getOutputStream(path, 1);
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
     * @param target 连接文件绝对路径
     * @return ignore
     */
    public boolean touchLink(String source, String target) {
        return this.touchLink(source, target, false);
    }

    /**
     * 创建连接文件
     *
     * @param source 原文件绝对路径
     * @param target 连接文件绝对路径
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

    // -------------------- stream --------------------

    public InputStream getInputStream(String path) throws IOException {
        return this.getInputStream(path, 0L);
    }

    /**
     * 打开存在文件的输入流
     *
     * @param path 文件路径
     * @param skip 跳过的字节
     * @return InputStream
     * @throws IOException IOException
     */
    public InputStream getInputStream(String path, long skip) throws IOException {
        try {
            return channel.get(path, null, skip);
        } catch (Exception e) {
            throw Exceptions.io("could open file input stream " + path, e);
        }
    }

    public OutputStream getOutputStreamWriter(String path) throws IOException {
        return this.getOutputStream(path, 0);
    }

    public OutputStream getOutputStreamAppend(String path) throws IOException {
        return this.getOutputStream(path, 2);
    }

    /**
     * 打开存在文件的输出流
     *
     * @param path 文件路径
     * @param mode 0 完全覆盖模式
     *             1 恢复模式
     *             2 追加模式
     * @return OutputStream
     * @throws IOException IOException
     */
    public OutputStream getOutputStream(String path, int mode) throws IOException {
        try {
            this.mkdirs(Files1.getParentPath(path));
            return channel.put(path, mode);
        } catch (Exception e) {
            throw Exceptions.io("could open file output stream " + path, e);
        }
    }

    // -------------------- read --------------------

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
            in = this.getInputStream(path, skip);
            return in.read(bs, offset, len);
        } finally {
            Streams.close(in);
        }
    }

    public String readLine(String path) throws IOException {
        return this.readLine(path, 0L);
    }

    /**
     * 读取一行
     *
     * @param path path
     * @param skip skip
     * @return line
     * @throws IOException IOException
     */
    public String readLine(String path, long skip) throws IOException {
        InputStream in = null;
        BufferedReader reader = null;
        try {
            in = this.getInputStream(path, skip);
            reader = new BufferedReader(new InputStreamReader(in, charset));
            return reader.readLine();
        } finally {
            Streams.close(in);
            Streams.close(reader);
        }
    }

    public List<String> readLines(String path) throws IOException {
        return this.readLines(path, 0L, 0);
    }

    public List<String> readLines(String path, long skip) throws IOException {
        return this.readLines(path, skip, 0);
    }

    public List<String> readLines(String path, int lines) throws IOException {
        return this.readLines(path, 0L, lines);
    }

    /**
     * 读取多行
     *
     * @param path  path
     * @param skip  skip
     * @param lines 行数
     * @return lines
     * @throws IOException IOException
     */
    public List<String> readLines(String path, long skip, int lines) throws IOException {
        InputStream in = null;
        BufferedReader reader = null;
        try {
            in = this.getInputStream(path, skip);
            reader = new BufferedReader(new InputStreamReader(in, charset));
            List<String> list = new ArrayList<>();
            String line;
            if (lines > 0) {
                for (int i = 0; i < lines && null != (line = reader.readLine()); i++) {
                    list.add(line);
                }
            } else {
                while (null != (line = reader.readLine())) {
                    list.add(line);
                }
            }
            return list;
        } finally {
            Streams.close(in);
            Streams.close(reader);
        }
    }

    // -------------------- transfer --------------------

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
            in = this.getInputStream(path, skip);
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
        } finally {
            Streams.close(in);
        }
        return r;
    }

    // -------------------- write --------------------

    public void write(String path, InputStream in) throws IOException {
        this.write(path, in, null, null, 0);
    }

    public void write(String path, byte[] bs) throws IOException {
        this.write(path, null, new StreamEntry(bs), null, 0);
    }

    public void write(String path, byte[] bs, int off, int len) throws IOException {
        this.write(path, null, new StreamEntry(bs, off, len), null, 0);
    }

    public void writeLine(String path, String line) throws IOException {
        this.write(path, null, null, Lists.singleton(line), 0);
    }

    public void writeLines(String path, List<String> lines) throws IOException {
        this.write(path, null, null, lines, 0);
    }

    // -------------------- append --------------------

    public void append(String path, InputStream in) throws IOException {
        this.write(path, in, null, null, 2);
    }

    public void append(String path, byte[] bs) throws IOException {
        this.write(path, null, new StreamEntry(bs), null, 2);
    }

    public void append(String path, byte[] bs, int off, int len) throws IOException {
        this.write(path, null, new StreamEntry(bs, off, len), null, 2);
    }

    public void appendLine(String path, String line) throws IOException {
        this.write(path, null, null, Lists.singleton(line), 2);
    }

    public void appendLines(String path, List<String> lines) throws IOException {
        this.write(path, null, null, lines, 2);
    }

    /**
     * 拼接/写入到文件
     *
     * @param path  文件绝对路径
     * @param in    输入流
     * @param entry 写入信息
     * @param lines 行
     * @param mode  0 完全覆盖模式
     *              1 恢复模式 如果文件正在传输时, 由于网络等原因导致传输中断, 则下一次传输相同的文件, 会从上一次中断的地方续传
     *              2 追加模式
     * @throws IOException IOException
     */
    private void write(String path, InputStream in, StreamEntry entry, List<String> lines, int mode) throws IOException {
        this.touch(path, mode == 0);
        OutputStream out = this.getOutputStream(path, mode);
        try {
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
                    out.write(Strings.bytes(line + Const.LF, charset));
                }
            }
            out.flush();
        } finally {
            Streams.close(out);
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

    public SftpUpload upload(String remote, File local) {
        return new SftpUpload(this, remote, local);
    }

    /**
     * 获取文件上传器
     *
     * @param remote 远程文件绝对路径
     * @param local  本地文件
     * @return 文件上传器
     */
    public SftpUpload upload(String remote, String local) {
        return new SftpUpload(this, remote, local);
    }

    public SftpDownload download(String remote, File local) {
        return new SftpDownload(this, remote, local);
    }

    /**
     * 获取文件下载器
     *
     * @param remote 远程文件绝对路径
     * @param local  本地文件
     * @return 文件下载器
     */
    public SftpDownload download(String remote, String local) {
        return new SftpDownload(this, remote, local);
    }

    // -------------------- list --------------------

    /**
     * 获取目录文件属性
     *
     * @param path 文件绝对路径
     * @return 属性
     */
    public List<SftpFile> ll(String path) {
        List<SftpFile> list = new ArrayList<>();
        try {
            Vector<?> files = channel.ls(path);
            for (Object l : files) {
                ChannelSftp.LsEntry ls = (ChannelSftp.LsEntry) l;
                String filename = ls.getFilename();
                if (".".equals(filename) || "..".equals(filename)) {
                    continue;
                }
                list.add(new SftpFile(Files1.getPath(path + SEPARATOR + filename), ls.getLongname(), ls.getAttrs()));
            }
        } catch (Exception e) {
            // ignore
        }
        return list;
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
     * @param path  文件夹绝对路径
     * @param child 是否递归子文件夹
     * @param dir   是否添加文件夹
     * @return 文件列表
     */
    public List<SftpFile> listFiles(String path, boolean child, boolean dir) {
        List<SftpFile> list = new ArrayList<>();
        try {
            List<SftpFile> ls = this.ll(path);
            for (SftpFile l : ls) {
                if (l.isDirectory()) {
                    if (dir) {
                        list.add(l);
                    }
                    if (child) {
                        list.addAll(this.listFiles(Files1.getPath(path + SEPARATOR + l.getName()), true, dir));
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
     * @param path  文件夹绝对路径
     * @param child 是否递归
     * @return 文件列表
     */
    public List<SftpFile> listDirs(String path, boolean child) {
        List<SftpFile> list = new ArrayList<>();
        try {
            List<SftpFile> ls = this.ll(path);
            for (SftpFile l : ls) {
                if (l.isDirectory()) {
                    list.add(l);
                    if (child) {
                        list.addAll(this.listDirs(Files1.getPath(path + SEPARATOR + l.getName()), true));
                    }
                }
            }
        } catch (Exception e) {
            // ingore
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
     * @param path   文件夹绝对路径
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
     * @param path  文件夹绝对路径
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
     * @param path    文件夹绝对路径
     * @param pattern 正则
     * @param child   是否递归子文件夹
     * @param dir     是否添加文件夹
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
     * @param path   文件夹绝对路径
     * @param filter 过滤器
     * @param child  是否递归子文件夹
     * @param dir    是否添加文件夹
     * @return 文件
     */
    public List<SftpFile> listFilesFilter(String path, FileAttributeFilter filter, boolean child, boolean dir) {
        return this.listFilesSearch(path, filter, child, dir);
    }

    /**
     * 搜索文件
     *
     * @param path   文件夹绝对路径
     * @param filter 过滤器
     * @param child  是否递归
     * @param dir    是否添加文件夹
     * @return 文件
     */
    private List<SftpFile> listFilesSearch(String path, FileAttributeFilter filter, boolean child, boolean dir) {
        List<SftpFile> list = new ArrayList<>();
        try {
            List<SftpFile> ls = this.ll(path);
            for (SftpFile l : ls) {
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
            // ignore
        }
        return list;
    }

    // -------------------- option --------------------

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

    public String getCharset() {
        return charset;
    }

}
