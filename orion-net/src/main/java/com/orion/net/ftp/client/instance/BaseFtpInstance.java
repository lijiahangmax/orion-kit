package com.orion.net.ftp.client.instance;

import com.orion.lang.constant.Const;
import com.orion.lang.define.StreamEntry;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.io.Files1;
import com.orion.net.ftp.client.FtpFile;
import com.orion.net.ftp.client.FtpMessage;
import com.orion.net.ftp.client.Ftps;
import com.orion.net.ftp.client.config.FtpConfig;
import com.orion.net.ftp.client.pool.FtpClientPool;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * ftp 操作实例 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/5/20 11:18
 */
public abstract class BaseFtpInstance implements IFtpInstance {

    /**
     * 分隔符
     */
    protected static final String SEPARATOR = Const.SLASH;

    /**
     * FTP 连接
     */
    protected FTPClient client;

    /**
     * FTP 配置
     */
    protected FtpConfig config;

    /**
     * FTP 连接池
     */
    protected FtpClientPool pool;

    protected int bufferSize;

    public BaseFtpInstance(FtpClientPool pool) {
        this.pool = pool;
        this.client = pool.getClient();
        this.config = pool.getConfig();
        this.bufferSize = Const.BUFFER_KB_8;
    }

    public BaseFtpInstance(FTPClient client, FtpConfig config) {
        this.client = client;
        this.config = config;
        this.bufferSize = Const.BUFFER_KB_8;
    }

    @Override
    public int replyCode() {
        return client.getReplyCode();
    }

    @Override
    public String replyMsg() {
        return FtpMessage.REPLY_CODE.get(client.getReplyCode());
    }

    @Override
    public boolean reply() {
        return FTPReply.isPositiveCompletion(client.getReplyCode());
    }

    // -------------------- transfer --------------------

    @Override
    public long transfer(String path, String file, long skip, int size) throws IOException {
        return this.doTransfer(path, Files1.openOutputStreamFast(file), skip, size, true);
    }

    @Override
    public long transfer(String path, OutputStream out, long skip, int size) throws IOException {
        return this.doTransfer(path, out, skip, size, false);
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

    // -------------------- write --------------------

    @Override
    public void write(String path, InputStream in) throws IOException {
        this.doWrite(path, in, null);
    }

    @Override
    public void write(String path, byte[] bs, int off, int len) throws IOException {
        this.doWrite(path, null, new StreamEntry(bs, off, len));
    }

    /**
     * 执行写入
     *
     * @param path  path
     * @param in    in
     * @param entry entry
     * @throws IOException IOException
     */
    protected abstract void doWrite(String path, InputStream in, StreamEntry entry) throws IOException;

    // -------------------- append --------------------

    @Override
    public void append(String path, InputStream in) throws IOException {
        this.doAppend(path, in, null);
    }

    @Override
    public void append(String path, byte[] bs, int off, int len) throws IOException {
        this.doAppend(path, null, new StreamEntry(bs, off, len));
    }

    /**
     * 执行拼接
     *
     * @param path  path
     * @param in    in
     * @param entry entry
     * @throws IOException IOException
     */
    protected abstract void doAppend(String path, InputStream in, StreamEntry entry) throws IOException;

    // -------------------- upload --------------------

    @Override
    public void uploadFile(String remoteFile, String localFile) throws IOException {
        this.uploadFile(remoteFile, Files1.openInputStreamSafe(localFile), true);
    }

    // -------------------- download --------------------

    @Override
    public void downloadFile(String remoteFile, String localFile) throws IOException {
        this.downloadFile(remoteFile, Files1.openOutputStreamFast(localFile), true);
    }

    // -------------------- list --------------------

    @Override
    public List<FtpFile> listFiles(String path, boolean child, boolean dir) {
        String base = config.getRemoteRootDir();
        List<FtpFile> list = new ArrayList<>();
        try {
            FTPFile[] files = client.listFiles(this.serverCharset(base + path));
            for (FTPFile file : files) {
                String t = this.serverCharset(Files1.getPath(path, file.getName()));
                if (file.isFile()) {
                    list.add(new FtpFile(t, file));
                } else if (file.isDirectory()) {
                    if (dir) {
                        list.add(new FtpFile(t, file));
                    }
                    if (child) {
                        list.addAll(this.listFiles(t + SEPARATOR, true, dir));
                    }
                }
            }
        } catch (IOException e) {
            throw Exceptions.ftp(e);
        }
        return list;
    }

    @Override
    public List<FtpFile> listDirs(String path, boolean child) {
        String base = config.getRemoteRootDir();
        List<FtpFile> list = new ArrayList<>();
        try {
            FTPFile[] files = client.listFiles(this.serverCharset(base + path));
            for (FTPFile file : files) {
                String t = Files1.getPath(path, file.getName());
                if (file.isDirectory()) {
                    list.add(new FtpFile(t, file));
                    if (child) {
                        list.addAll(this.listDirs(Files1.getPath(t + SEPARATOR), true));
                    }
                }
            }
        } catch (IOException e) {
            throw Exceptions.ftp(e);
        }
        return list;
    }

    // -------------------- option --------------------

    @Override
    public boolean pending() throws IOException {
        return client.completePendingCommand();
    }

    @Override
    public void restartOffset(long offset) {
        client.setRestartOffset(offset);
    }

    @Override
    public void reset() {
        client.setRestartOffset(0);
    }

    @Override
    public String getSystemType() {
        try {
            return client.getSystemType();
        } catch (IOException e) {
            throw Exceptions.ftp(e);
        }
    }

    @Override
    public String getStatus() {
        try {
            return client.getStatus();
        } catch (IOException e) {
            throw Exceptions.ftp(e);
        }
    }

    @Override
    public String getStatus(String path) {
        try {
            return client.getStatus(new String(Strings.bytes(Files1.getPath(config.getRemoteRootDir(), path)), config.getRemoteFileNameCharset()));
        } catch (IOException e) {
            throw Exceptions.ftp(e);
        }
    }

    @Override
    public FTPClient getClient() {
        return client;
    }

    @Override
    public FtpConfig getConfig() {
        return config;
    }

    @Override
    public FtpClientPool getPool() {
        return pool;
    }

    @Override
    public boolean sendNoop() throws IOException {
        return client.sendNoOp();
    }

    @Override
    public String serverCharset(String chars) {
        return new String(Strings.bytes(Files1.getPath(chars)), config.getRemoteFileNameCharset());
    }

    @Override
    public String localCharset(String chars) {
        return new String(Strings.bytes(Files1.getPath(chars)), config.getLocalFileNameCharset());
    }

    @Override
    public void destroy() {
        Ftps.destroy(client);
    }

    @Override
    public void close() {
        if (pool != null) {
            pool.returnClient(client);
        } else {
            Ftps.destroy(client);
        }
    }

}
