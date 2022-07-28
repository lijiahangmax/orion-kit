package com.orion.net.remote.channel.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.orion.lang.constant.Const;
import com.orion.lang.define.StreamEntry;
import com.orion.lang.utils.Charsets;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.Streams;
import com.orion.net.base.file.sftp.BaseSftpExecutor;
import com.orion.net.base.file.sftp.SftpErrorMessage;
import com.orion.net.base.file.sftp.SftpFile;
import com.orion.net.remote.channel.ChannelConnector;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * sftp 执行器
 * <p>
 * 文件路必须是绝对路径 可以包含 ../ ./
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/5/18 15:22
 */
public class SftpExecutor extends BaseSftpExecutor implements ChannelConnector {

    private final ChannelSftp channel;

    public SftpExecutor(ChannelSftp channel) {
        this(channel, Const.UTF_8);
    }

    public SftpExecutor(ChannelSftp channel, String charset) {
        this.channel = channel;
        this.charset = charset;
    }

    @Override
    public void bufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override
    public void charset(String charset) {
        try {
            channel.setFilenameEncoding(Charsets.of(charset));
            this.charset = charset;
        } catch (Exception e) {
            throw Exceptions.sftp("set sftp charset error", e);
        }
    }

    @Override
    public boolean isExist(String path) {
        try {
            return channel.lstat(path) != null;
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
            return channel.realpath(path);
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
            return channel.readlink(path);
        } catch (Exception e) {
            if (SftpErrorMessage.NO_SUCH_FILE.isCause(e)) {
                return null;
            } else if (SftpErrorMessage.BAD_MESSAGE.isCause(e)) {
                return null;
            } else {
                throw Exceptions.sftp(e);
            }
        }
    }

    @Override
    public SftpFile getFile(String path) {
        return this.getFile(path, false);
    }

    @Override
    public SftpFile getFile(String path, boolean followSymbolic) {
        try {
            SftpATTRS attr;
            if (followSymbolic) {
                attr = channel.stat(path);
            } else {
                attr = channel.lstat(path);
            }
            return new SftpFileWrapper(path, attr);
        } catch (Exception e) {
            if (SftpErrorMessage.NO_SUCH_FILE.isCause(e)) {
                return null;
            } else {
                throw Exceptions.sftp(e);
            }
        }
    }

    @Override
    public long getSize(String path) {
        try {
            SftpFile file = this.getFile(path, false);
            if (file == null) {
                return -1;
            }
            return file.getSize();
        } catch (Exception e) {
            if (SftpErrorMessage.NO_SUCH_FILE.isCause(e)) {
                return -1;
            }
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public void setFileAttribute(SftpFile attribute) {
        try {
            channel.setStat(attribute.getPath(), ((SftpFileWrapper) attribute).getAttrs());
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public void setModifyTime(String path, Date date) {
        try {
            channel.setMtime(path, (int) (date.getTime() / Const.MS_S_1));
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public void chmod(String file, int permission) {
        try {
            channel.chmod(Files1.permission10to8(permission), file);
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public void chown(String file, int uid) {
        try {
            channel.chown(uid, file);
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public void chgrp(String file, int gid) {
        try {
            channel.chgrp(gid, file);
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public void mkdir(String path) {
        try {
            channel.mkdir(path);
        } catch (SftpException e) {
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public void removeDir(String path) {
        try {
            channel.rmdir(path);
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
            channel.rm(path);
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
        OutputStream out = null;
        try {
            if (truncate) {
                out = this.openOutputStream(path, 0);
            } else {
                out = this.openOutputStream(path, 1);
            }
            out.flush();
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        } finally {
            Streams.close(out);
        }
    }

    @Override
    public void truncate(String path) {
        try {
            this.touchTruncate(path);
        } catch (Exception e) {
            if (SftpErrorMessage.NO_SUCH_FILE.isCause(e)) {
                return;
            }
            throw Exceptions.sftp(e);
        }
    }

    /**
     * 创建软连接
     * <p>
     * 原始文件不存在 则会报错
     * 连接文件存在 则会报错
     *
     * @param source source
     * @param target target
     */
    public void touchHardLink(String source, String target) {
        this.touchLink(source, target, true);
    }

    /**
     * 创建软连接文件
     * <p>
     * 原始文件不存在 则会报错
     * 连接文件存在 则会报错
     *
     * @param source source
     * @param target target
     */
    public void touchSymLink(String source, String target) {
        this.touchLink(source, target, false);
    }

    @Override
    public void touchLink(String source, String target) {
        this.touchLink(source, target, false);
    }

    /**
     * 创建连接文件
     * <p>
     * 原始文件不存在 则会报错
     * 连接文件存在 则会报错
     *
     * @param source source
     * @param target target
     * @param hard   是否为硬链接
     */
    public void touchLink(String source, String target, boolean hard) {
        try {
            // 检查是否需要创建目标文件目录
            if (!this.isSameParentPath(source, target)) {
                this.mkdirs(Files1.getParentPath(target));
            }
            if (hard) {
                channel.hardlink(source, target);
            } else {
                channel.symlink(source, target);
            }
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
    }

    @Override
    protected void doMove(String source, String target) {
        try {
            channel.rename(source, target);
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
    }

    // -------------------- stream --------------------

    public InputStream openInputStream(String path) throws IOException {
        return this.openInputStream(path, 0L);
    }

    /**
     * 打开存在文件的输入流
     *
     * @param path 文件路径
     * @param skip 跳过的字节
     * @return InputStream
     * @throws IOException IOException
     */
    public InputStream openInputStream(String path, long skip) throws IOException {
        try {
            return channel.get(path, null, skip);
        } catch (Exception e) {
            throw Exceptions.io("could open file input stream " + path, e);
        }
    }

    public OutputStream openOutputStreamWriter(String path) throws IOException {
        return this.openOutputStream(path, 0);
    }

    public OutputStream openOutputStreamAppend(String path) throws IOException {
        return this.openOutputStream(path, 2);
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
    public OutputStream openOutputStream(String path, int mode) throws IOException {
        try {
            this.mkdirs(Files1.getParentPath(path));
            return channel.put(path, mode);
        } catch (Exception e) {
            throw Exceptions.io("could open file output stream " + path, e);
        }
    }

    // -------------------- read --------------------

    @Override
    public int read(String path, long skip, byte[] bs, int offset, int len) throws IOException {
        InputStream in = null;
        try {
            in = this.openInputStream(path, skip);
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
            in = this.openInputStream(path, skip);
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
            in = this.openInputStream(path, skip);
            reader = new BufferedReader(new InputStreamReader(in, charset));
            List<String> list = new ArrayList<>();
            String line;
            if (lines > 0) {
                for (int i = 0; i < lines && (line = reader.readLine()) != null; i++) {
                    list.add(line);
                }
            } else {
                while ((line = reader.readLine()) != null) {
                    list.add(line);
                }
            }
            return list;
        } finally {
            Streams.close(in);
            Streams.close(reader);
        }
    }

    // -------------------- write --------------------

    @Override
    protected long doTransfer(String path, OutputStream out, long skip, int size, boolean close) throws IOException {
        long r = 0;
        byte[] bs = new byte[bufferSize];
        InputStream in = null;
        try {
            in = this.openInputStream(path, skip);
            if (size != -1) {
                boolean mod = size % bufferSize == 0;
                long readTimes = size / bufferSize;
                if (mod || readTimes == 0) {
                    readTimes++;
                }
                for (int i = 0; i < readTimes; i++) {
                    if (readTimes == 1) {
                        int read = in.read(bs, 0, size);
                        out.write(bs, 0, read);
                        r += read;
                    } else {
                        int read = in.read(bs, 0, bufferSize);
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
                while ((read = in.read(bs, 0, bufferSize)) != -1) {
                    out.write(bs, 0, read);
                    r += read;
                }
            }
        } finally {
            Streams.close(in);
            if (close) {
                Streams.close(out);
            }
        }
        return r;
    }

    @Override
    protected void doWrite(String path, InputStream in, StreamEntry entry, List<String> lines) throws IOException {
        this.write(path, in, entry, lines, 0);
    }

    @Override
    protected void doAppend(String path, InputStream in, StreamEntry entry, List<String> lines) throws IOException {
        this.write(path, in, entry, lines, 2);
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
        OutputStream out = null;
        try {
            out = this.openOutputStream(path, mode);
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
        List<SftpFile> list = new ArrayList<>();
        try {
            Vector<?> files = channel.ls(path);
            for (Object file : files) {
                ChannelSftp.LsEntry entity = (ChannelSftp.LsEntry) file;
                String filename = entity.getFilename();
                if (".".equals(filename) || "..".equals(filename)) {
                    continue;
                }
                list.add(new SftpFileWrapper(Files1.getPath(path, filename), entity.getAttrs()));
            }
        } catch (Exception e) {
            if (SftpErrorMessage.NO_SUCH_FILE.isCause(e)) {
                return new ArrayList<>();
            }
            throw Exceptions.sftp(e);
        }
        return list;
    }

    /**
     * 获取根目录
     *
     * @return 根目录
     */
    public String getHome() {
        try {
            return channel.getHome();
        } catch (SftpException e) {
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public void connect() {
        try {
            channel.connect();
        } catch (Exception e) {
            throw Exceptions.connection(e);
        }
        this.charset(charset);
    }

    @Override
    public void connect(int timeout) {
        try {
            channel.connect(timeout);
        } catch (Exception e) {
            throw Exceptions.connection(e);
        }
        this.charset(charset);
    }

    @Override
    public boolean isConnected() {
        return channel.isConnected();
    }

    @Override
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

}
