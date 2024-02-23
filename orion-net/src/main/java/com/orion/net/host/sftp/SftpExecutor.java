package com.orion.net.host.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.orion.lang.constant.Const;
import com.orion.lang.define.StreamEntry;
import com.orion.lang.utils.Charsets;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.Streams;
import com.orion.net.host.HostConnector;
import com.orion.net.host.sftp.transfer.SftpDownloader;
import com.orion.net.host.sftp.transfer.SftpUploader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
public class SftpExecutor extends BaseSftpExecutor implements HostConnector {

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
    public void sendSignal(String signal) {
        try {
            channel.sendSignal(signal);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    @Override
    public String getHome() {
        try {
            return channel.getHome();
        } catch (SftpException e) {
            throw Exceptions.sftp(e);
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
        SftpFile file = this.getFile(path, false);
        if (file == null) {
            return -1;
        }
        return file.getSize();
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
    public void changeMode(String file, int permission) {
        try {
            channel.chmod(Files1.permission10to8(permission), file);
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public void changeOwner(String file, int uid) {
        try {
            channel.chown(uid, file);
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public void changeGroup(String file, int gid) {
        try {
            channel.chgrp(gid, file);
        } catch (Exception e) {
            throw Exceptions.sftp(e);
        }
    }

    @Override
    public void makeDirectory(String path) {
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

    @Override
    public void hardLink(String source, String target) {
        this.link(source, target, true);
    }

    @Override
    public void symLink(String source, String target) {
        this.link(source, target, false);
    }

    @Override
    public void link(String source, String target, boolean hard) {
        try {
            // 检查是否需要创建目标文件目录
            if (!this.isSameParentPath(source, target)) {
                this.makeDirectories(Files1.getParentPath(target));
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

    // -------------------- open --------------------

    @Override
    public InputStream openInputStream(String path, long skip) throws IOException {
        try {
            return channel.get(path, null, skip);
        } catch (Exception e) {
            throw Exceptions.io("could open file input stream " + path, e);
        }
    }

    @Override
    public OutputStream openOutputStream(String path, boolean append) throws IOException {
        if (append) {
            return this.openOutputStream(path, 2);
        } else {
            return this.openOutputStream(path, 0);
        }
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
            this.makeDirectories(Files1.getParentPath(path));
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
    protected void doWrite(String path, InputStream in, StreamEntry entry) throws IOException {
        this.write(path, in, entry, 0);
    }

    @Override
    protected void doAppend(String path, InputStream in, StreamEntry entry) throws IOException {
        this.write(path, in, entry, 2);
    }

    /**
     * 拼接/写入到文件
     *
     * @param path  文件绝对路径
     * @param in    输入流
     * @param entry 写入信息
     * @param mode  0 完全覆盖模式
     *              1 恢复模式 如果文件正在传输时, 由于网络等原因导致传输中断, 则下一次传输相同的文件, 会从上一次中断的地方续传
     *              2 追加模式
     * @throws IOException IOException
     */
    private void write(String path, InputStream in, StreamEntry entry, int mode) throws IOException {
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
    public List<SftpFile> list(String path) {
        List<SftpFile> list = new ArrayList<>();
        try {
            // 检查是否为目录
            SftpATTRS stat = channel.stat(path);
            if (!stat.isDir()) {
                return new ArrayList<>();
            }
            // 查询列表
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
