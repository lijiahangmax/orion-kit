package com.orion.net.ftp.client.instance;

import com.orion.constant.Const;
import com.orion.lang.StreamEntry;
import com.orion.net.ftp.client.FtpFile;
import com.orion.net.ftp.client.FtpFileFilter;
import com.orion.net.ftp.client.FtpMessage;
import com.orion.net.ftp.client.Ftps;
import com.orion.net.ftp.client.config.FtpConfig;
import com.orion.net.ftp.client.pool.FtpClientPool;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.collect.Lists;
import com.orion.utils.io.Files1;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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

    // -------------------- read --------------------

    @Override
    public int read(String file, byte[] bs) throws IOException {
        return this.read(file, 0, bs, 0, bs.length);
    }

    @Override
    public int read(String file, long skip, byte[] bs) throws IOException {
        return this.read(file, skip, bs, 0, bs.length);
    }

    @Override
    public int read(String file, byte[] bs, int off, int len) throws IOException {
        return this.read(file, 0, bs, off, len);
    }

    @Override
    public String readLine(String file) throws IOException {
        return this.readLine(file, 0);
    }

    @Override
    public List<String> readLines(String file) throws IOException {
        return this.readLines(file, 0, -1);
    }

    @Override
    public List<String> readLines(String file, long skip) throws IOException {
        return this.readLines(file, skip, -1);
    }

    @Override
    public List<String> readLines(String file, int lines) throws IOException {
        return this.readLines(file, 0, lines);
    }

    // -------------------- transfer --------------------

    @Override
    public long transfer(String path, OutputStream out) throws IOException {
        return this.doTransfer(path, out, 0, -1, false);
    }

    @Override
    public long transfer(String path, OutputStream out, long skip) throws IOException {
        return this.doTransfer(path, out, skip, -1, false);
    }

    @Override
    public long transfer(String path, OutputStream out, long skip, int size) throws IOException {
        return this.doTransfer(path, out, skip, size, false);
    }

    @Override
    public long transfer(String path, String file) throws IOException {
        Files1.touch(file);
        return this.doTransfer(path, Files1.openOutputStream(file), 0, -1, true);
    }

    @Override
    public long transfer(String path, File file) throws IOException {
        Files1.touch(file);
        return this.doTransfer(path, Files1.openOutputStream(file), 0, -1, true);
    }

    @Override
    public long transfer(String path, String file, long skip) throws IOException {
        Files1.touch(file);
        return this.doTransfer(path, Files1.openOutputStream(file), skip, -1, true);
    }

    @Override
    public long transfer(String path, File file, long skip) throws IOException {
        Files1.touch(file);
        return this.doTransfer(path, Files1.openOutputStream(file), skip, -1, true);
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
        this.doWrite(path, in, null, null);
    }

    @Override
    public void write(String path, byte[] bs) throws IOException {
        this.doWrite(path, null, new StreamEntry(bs), null);
    }

    @Override
    public void write(String path, byte[] bs, int off, int len) throws IOException {
        this.doWrite(path, null, new StreamEntry(bs, off, len), null);
    }

    @Override
    public void writeLine(String path, String line) throws IOException {
        this.doWrite(path, null, null, Lists.singleton(line));
    }

    @Override
    public void writeLines(String path, List<String> lines) throws IOException {
        this.doWrite(path, null, null, lines);
    }

    /**
     * 执行写入
     *
     * @param path  path
     * @param in    in
     * @param entry entry
     * @param lines lines
     * @throws IOException IOException
     */
    protected abstract void doWrite(String path, InputStream in, StreamEntry entry, List<String> lines) throws IOException;

    // -------------------- append --------------------

    @Override
    public void append(String path, InputStream in) throws IOException {
        this.doAppend(path, in, null, null);
    }

    @Override
    public void append(String path, byte[] bs) throws IOException {
        this.doAppend(path, null, new StreamEntry(bs), null);
    }

    @Override
    public void append(String path, byte[] bs, int off, int len) throws IOException {
        this.doAppend(path, null, new StreamEntry(bs, off, len), null);
    }

    @Override
    public void appendLine(String path, String line) throws IOException {
        this.doAppend(path, null, null, Lists.singleton(line));
    }

    @Override
    public void appendLines(String path, List<String> lines) throws IOException {
        this.doAppend(path, null, null, lines);
    }

    /**
     * 执行拼接
     *
     * @param path  path
     * @param in    in
     * @param entry entry
     * @param lines lines
     * @throws IOException IOException
     */
    protected abstract void doAppend(String path, InputStream in, StreamEntry entry, List<String> lines) throws IOException;

    // -------------------- upload --------------------

    @Override
    public void uploadFile(String remoteFile, String localFile) throws IOException {
        this.uploadFile(remoteFile, Files1.openInputStreamSafe(localFile), true);
    }

    @Override
    public void uploadFile(String remoteFile, File localFile) throws IOException {
        this.uploadFile(remoteFile, Files1.openInputStreamSafe(localFile), true);
    }

    @Override
    public void uploadFile(String remoteFile, InputStream in) throws IOException {
        this.uploadFile(remoteFile, in, false);
    }

    @Override
    public void uploadDir(String remoteDir, File localDir) throws IOException {
        this.uploadDir(remoteDir, localDir.getAbsolutePath(), true);
    }

    @Override
    public void uploadDir(String remoteDir, String localDir) throws IOException {
        this.uploadDir(remoteDir, localDir, true);
    }

    @Override
    public void uploadDir(String remoteDir, File localDir, boolean child) throws IOException {
        this.uploadDir(remoteDir, localDir.getAbsolutePath(), child);
    }

    // -------------------- download --------------------

    @Override
    public void downloadFile(String remoteFile, String localFile) throws IOException {
        Files1.touch(localFile);
        this.downloadFile(remoteFile, Files1.openOutputStreamSafe(localFile), true);
    }

    @Override
    public void downloadFile(String remoteFile, File localFile) throws IOException {
        Files1.touch(localFile);
        this.downloadFile(remoteFile, Files1.openOutputStreamSafe(localFile), true);
    }

    @Override
    public void downloadFile(String remoteFile, OutputStream out) throws IOException {
        this.downloadFile(remoteFile, out, false);
    }

    @Override
    public void downloadDir(String remoteDir, File localDir) throws IOException {
        this.downloadDir(remoteDir, localDir.getAbsolutePath(), true);
    }

    @Override
    public void downloadDir(String remoteDir, String localDir) throws IOException {
        this.downloadDir(remoteDir, localDir, true);
    }

    @Override
    public void downloadDir(String remoteDir, File localDir, boolean child) throws IOException {
        this.downloadDir(remoteDir, localDir.getAbsolutePath(), child);
    }

    // -------------------- list --------------------

    @Override
    public List<FtpFile> listFiles() {
        return this.listFiles(Strings.EMPTY, false, false);
    }

    @Override
    public List<FtpFile> listFiles(boolean child) {
        return this.listFiles(Strings.EMPTY, child, false);
    }

    @Override
    public List<FtpFile> listFiles(boolean child, boolean dir) {
        return this.listFiles(Strings.EMPTY, child, dir);
    }

    @Override
    public List<FtpFile> listFiles(String path) {
        return this.listFiles(path, false, false);
    }

    @Override
    public List<FtpFile> listFiles(String path, boolean child) {
        return this.listFiles(path, child, false);
    }

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
    public List<FtpFile> listDirs() {
        return this.listDirs(Strings.EMPTY, false);
    }

    @Override
    public List<FtpFile> listDirs(boolean child) {
        return this.listDirs(Strings.EMPTY, child);
    }

    @Override
    public List<FtpFile> listDirs(String dir) {
        return this.listDirs(dir, false);
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

    @Override
    public List<FtpFile> listFilesSuffix(String suffix) {
        return this.listFilesSuffix(Strings.EMPTY, suffix, false, false);
    }

    @Override
    public List<FtpFile> listFilesSuffix(String suffix, boolean child) {
        return this.listFilesSuffix(Strings.EMPTY, suffix, child, false);
    }

    @Override
    public List<FtpFile> listFilesSuffix(String suffix, boolean child, boolean dir) {
        return this.listFilesSuffix(Strings.EMPTY, suffix, child, dir);
    }

    @Override
    public List<FtpFile> listFilesSuffix(String path, String suffix) {
        return this.listFilesSuffix(path, suffix, false, false);
    }

    @Override
    public List<FtpFile> listFilesSuffix(String path, String suffix, boolean child) {
        return this.listFilesSuffix(path, suffix, child, false);
    }

    @Override
    public List<FtpFile> listFilesSuffix(String path, String suffix, boolean child, boolean dir) {
        return this.listFilesSearch(path, FtpFileFilter.suffix(suffix), child, dir);
    }

    @Override
    public List<FtpFile> listFilesMatch(String match) {
        return this.listFilesMatch(Strings.EMPTY, match, false, false);
    }

    @Override
    public List<FtpFile> listFilesMatch(String match, boolean child) {
        return this.listFilesMatch(Strings.EMPTY, match, child, false);
    }

    @Override
    public List<FtpFile> listFilesMatch(String match, boolean child, boolean dir) {
        return this.listFilesMatch(Strings.EMPTY, match, child, dir);
    }

    @Override
    public List<FtpFile> listFilesMatch(String path, String match) {
        return this.listFilesMatch(path, match, false, false);
    }

    @Override
    public List<FtpFile> listFilesMatch(String path, String match, boolean child) {
        return this.listFilesMatch(path, match, child, false);
    }

    @Override
    public List<FtpFile> listFilesMatch(String path, String match, boolean child, boolean dir) {
        return this.listFilesSearch(path, FtpFileFilter.match(match), child, dir);
    }

    @Override
    public List<FtpFile> listFilesPattern(Pattern pattern) {
        return this.listFilesPattern(Strings.EMPTY, pattern, false, false);
    }

    @Override
    public List<FtpFile> listFilesPattern(Pattern pattern, boolean child) {
        return this.listFilesPattern(Strings.EMPTY, pattern, child, false);
    }

    @Override
    public List<FtpFile> listFilesPattern(Pattern pattern, boolean child, boolean dir) {
        return this.listFilesPattern(Strings.EMPTY, pattern, child, dir);
    }

    @Override
    public List<FtpFile> listFilesPattern(String path, Pattern pattern) {
        return this.listFilesPattern(path, pattern, false, false);
    }

    @Override
    public List<FtpFile> listFilesPattern(String path, Pattern pattern, boolean child) {
        return this.listFilesPattern(path, pattern, child, false);
    }

    @Override
    public List<FtpFile> listFilesPattern(String path, Pattern pattern, boolean child, boolean dir) {
        return this.listFilesSearch(path, FtpFileFilter.pattern(pattern), child, dir);
    }

    @Override
    public List<FtpFile> listFilesFilter(FtpFileFilter filter) {
        return this.listFilesFilter(Strings.EMPTY, filter, false, false);
    }

    @Override
    public List<FtpFile> listFilesFilter(FtpFileFilter filter, boolean child) {
        return this.listFilesFilter(Strings.EMPTY, filter, child, false);
    }

    @Override
    public List<FtpFile> listFilesFilter(FtpFileFilter filter, boolean child, boolean dir) {
        return this.listFilesFilter(Strings.EMPTY, filter, child, dir);
    }

    @Override
    public List<FtpFile> listFilesFilter(String path, FtpFileFilter filter) {
        return this.listFilesFilter(path, filter, false, false);
    }

    @Override
    public List<FtpFile> listFilesFilter(String path, FtpFileFilter filter, boolean child) {
        return this.listFilesFilter(path, filter, child, false);
    }

    @Override
    public List<FtpFile> listFilesFilter(String path, FtpFileFilter filter, boolean child, boolean dir) {
        return this.listFilesSearch(path, filter, child, dir);
    }

    /**
     * 文件列表搜索
     *
     * @param path   列表
     * @param filter 过滤器
     * @param child  是否递归子文件夹
     * @param dir    是否添加文件夹
     * @return 匹配的列表
     */
    protected abstract List<FtpFile> listFilesSearch(String path, FtpFileFilter filter, boolean child, boolean dir);

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
