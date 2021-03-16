package com.orion.ftp.client;

import com.orion.able.SafeCloseable;
import com.orion.constant.Const;
import com.orion.constant.Letters;
import com.orion.ftp.client.bigfile.FtpDownload;
import com.orion.ftp.client.bigfile.FtpUpload;
import com.orion.ftp.client.config.FtpConfig;
import com.orion.ftp.client.pool.FtpClientPool;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.collect.Lists;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * FTP操作实例
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/17 16:10
 */
@SuppressWarnings("ALL")
public class FtpInstance implements SafeCloseable {

    /**
     * 分隔符
     */
    private static final String SEPARATOR = Const.SLASH;

    /**
     * FTP连接
     */
    private FTPClient client;

    /**
     * FTP配置
     */
    private FtpConfig config;

    /**
     * FTP连接池
     */
    private FtpClientPool pool;

    public FtpInstance(FtpClientPool pool) {
        this.pool = pool;
        this.client = pool.getClient();
        this.config = pool.getConfig();
    }

    public FtpInstance(FTPClient client, FtpConfig config) {
        this.client = client;
        this.config = config;
        try {
            client.changeWorkingDirectory(this.serverCharset(config.getRemoteRootDir()));
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 切换至根目录
     */
    public void change() {
        try {
            client.changeWorkingDirectory(this.serverCharset(config.getRemoteRootDir()));
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 切换目录
     *
     * @param dir dir
     */
    public void change(String dir) {
        try {
            if (!client.changeWorkingDirectory(this.serverCharset(config.getRemoteRootDir() + dir))) {
                mkdirs(dir);
                client.changeWorkingDirectory(this.serverCharset(config.getRemoteRootDir() + dir));
            }
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 获取上一次命令的replyCode
     *
     * @return replyCode
     */
    public int replyCode() {
        return client.getReplyCode();
    }

    /**
     * 获取上一次命令的replyCode
     *
     * @return 信息
     */
    public String replyMsg() {
        return FtpMessage.REPLY_CODE.get(client.getReplyCode());
    }

    /**
     * 获取上一次命令的reply
     *
     * @return true执行成功
     */
    public boolean reply() {
        return FTPReply.isPositiveCompletion(client.getReplyCode());
    }

    /**
     * 文件是否存在
     *
     * @param file 文件
     * @return true存在
     */
    public boolean exist(String file) {
        String parentPath = Files1.getParentPath(file);
        List<FtpFile> list = this.listFiles(parentPath, false, true);
        for (FtpFile s : list) {
            if (Files1.getFileName(s.getPath()).equals(Files1.getFileName(file.trim()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件属性
     *
     * @param file 文件
     * @return 未找到返回null
     */
    public FtpFile getFile(String file) {
        String parentPath = Files1.getParentPath(file);
        List<FtpFile> files = this.listFiles(parentPath, false, true);
        for (FtpFile ftpFile : files) {
            if (ftpFile.getName().equals(Files1.getFileName(file))) {
                return ftpFile;
            }
        }
        return null;
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file file
     */
    public void rm(String file) {
        FtpFile ftpFile = this.getFile(file);
        if (ftpFile == null) {
            return;
        }
        if (ftpFile.isDirectory()) {
            this.deleteDir(file);
        } else {
            this.delete(file);
        }
    }

    /**
     * 删除文件
     *
     * @param file 文件
     */
    public void delete(String file) {
        try {
            client.deleteFile(this.serverCharset(config.getRemoteRootDir() + file));
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 删除目录及文件
     *
     * @param dir 目录
     */
    public void deleteDir(String dir) {
        try {
            String d = serverCharset(config.getRemoteRootDir() + dir);
            List<FtpFile> list = this.listFiles(dir);
            for (FtpFile s : list) {
                client.deleteFile(this.serverCharset(Files1.getPath(config.getRemoteRootDir() + s.getPath())));
            }
            list = this.listDirs(dir, true);
            for (FtpFile s : list) {
                this.deleteDir(s.getPath());
            }
            client.removeDirectory(d);
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 创建文件夹
     *
     * @param dir 文件夹
     */
    public void mkdirs(String dir) {
        try {
            String[] dirs = Files1.getPath(dir).split(SEPARATOR);
            String base = config.getRemoteRootDir();
            for (String d : dirs) {
                if (null == d || Strings.EMPTY.equals(d)) {
                    continue;
                }
                base = serverCharset(base + SEPARATOR + d);
                if (!client.changeWorkingDirectory(base)) {
                    client.makeDirectory(base);
                    client.changeWorkingDirectory(base);
                }
            }
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 创建文件
     *
     * @param file 文件
     */
    public void touch(String file) {
        String filePath = serverCharset(config.getRemoteRootDir() + file);
        String parentPath = Files1.getParentPath(Files1.getPath(file));
        mkdirs(parentPath);
        for (FtpFile s : listFiles(parentPath, false)) {
            if (Files1.getFileName(s.getPath()).endsWith(file.trim())) {
                return;
            }
        }
        this.change(parentPath);
        try {
            client.storeFile(this.serverCharset(filePath), new ByteArrayInputStream(new byte[]{}));
        } catch (IOException e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 文件移动
     *
     * @param file 原文件名称
     * @param name 移动后的名称 如果不加目录为重命名
     */
    public void mv(String file, String name) {
        try {
            this.mkdirs(Files1.getParentPath(name));
            this.change(Files1.getParentPath(Files1.getPath(file)));
            String target = serverCharset(config.getRemoteRootDir() + name);
            String source = serverCharset(Files1.getFileName(file));
            client.rename(source, target);
        } catch (Exception e) {
            throw Exceptions.ftp(e);
        }
    }

    // -------------------- stream --------------------

    public InputStream getInputStream(String file) throws IOException {
        try {
            return client.retrieveFileStream(this.serverCharset(config.getRemoteRootDir() + file));
        } catch (Exception e) {
            throw Exceptions.io("cannot get file input stream " + file, e);
        }
    }

    /**
     * 获取文件输入流
     * <p>
     * 使用完毕需要调用 client.completePendingCommand();
     * 这个操作在关闭io之后
     *
     * @param file 文件
     * @param skip 跳过字节数
     * @return InputStream
     * @throws IOException IOException
     */
    public InputStream getInputStream(String file, long skip) throws IOException {
        try {
            client.setRestartOffset(skip);
            return this.getInputStream(file);
        } finally {
            client.setRestartOffset(0);
        }
    }

    /**
     * 获取文件拼接流
     * <p>
     * 使用完毕需要调用 client.completePendingCommand();
     * 这个操作在关闭io之后
     *
     * @param file 文件
     * @return OutputStream
     * @throws IOException IOException
     */
    public OutputStream getOutputStreamAppend(String file) throws IOException {
        this.mkdirs(Files1.getParentPath(file));
        try {
            return client.appendFileStream(this.serverCharset(config.getRemoteRootDir() + file));
        } catch (Exception e) {
            throw Exceptions.io("cannot get file out stream " + file, e);
        }
    }

    /**
     * 获取文写入流
     * <p>
     * 使用完毕需要调用 client.completePendingCommand();
     * 这个操作在关闭io之后
     *
     * @param file 文件
     * @return OutputStream
     * @throws IOException IOException
     */
    public OutputStream getOutputStreamWriter(String file) throws IOException {
        this.mkdirs(Files1.getParentPath(file));
        try {
            return client.storeFileStream(this.serverCharset(config.getRemoteRootDir() + file));
        } catch (Exception e) {
            throw Exceptions.io("cannot get file out stream " + file, e);
        }
    }

    /**
     * 读取文件到流
     *
     * @param file 文件
     * @param out  输出流
     */
    public void readStream(String file, OutputStream out) throws IOException {
        this.mkdirs(Files1.getParentPath(file));
        try {
            client.retrieveFile(this.serverCharset(config.getRemoteRootDir() + file), out);
        } catch (Exception e) {
            throw Exceptions.io("cannot write to stream " + file, e);
        }
    }

    /**
     * 拼接流到文件
     *
     * @param file 文件
     * @param in   输入流
     */
    public void appendStream(String file, InputStream in) throws IOException {
        this.mkdirs(Files1.getParentPath(file));
        try {
            client.appendFile(this.serverCharset(config.getRemoteRootDir() + file), in);
        } catch (Exception e) {
            throw Exceptions.io("cannot write to stream " + file, e);
        }
    }

    /**
     * 写入流到文件
     *
     * @param file 文件
     * @param in   输入流
     */
    public void writeStream(String file, InputStream in) throws IOException {
        this.mkdirs(Files1.getParentPath(file));
        try {
            client.storeFile(this.serverCharset(config.getRemoteRootDir() + file), in);
        } catch (Exception e) {
            throw Exceptions.io("cannot write to stream " + file, e);
        }
    }

    // -------------------- read --------------------

    public int read(String file, byte[] bs) throws IOException {
        return read(file, 0, bs, 0, bs.length);
    }

    public int read(String file, long skip, byte[] bs) throws IOException {
        return read(file, skip, bs, 0, bs.length);
    }

    public int read(String file, byte[] bs, int off, int len) throws IOException {
        return read(file, 0, bs, off, len);
    }

    /**
     * 读取文件到数组
     *
     * @param file 文件
     * @param skip 跳过字节数
     * @param bs   数组
     * @param off  偏移量
     * @param len  长度
     * @return 读取的长度
     * @throws IOException IOException
     */
    public int read(String file, long skip, byte[] bs, int off, int len) throws IOException {
        InputStream in = null;
        try {
            client.setRestartOffset(skip);
            in = this.getInputStream(file);
            return in.read(bs, off, len);
        } finally {
            Streams.close(in);
            client.setRestartOffset(0);
            if (in != null) {
                client.completePendingCommand();
            }
        }
    }

    public String readLine(String file) throws IOException {
        return readLine(file, 0);
    }

    /**
     * 读取一行
     *
     * @param file 文件
     * @param skip 跳过字节数
     * @return 行
     * @throws IOException IOException
     */
    public String readLine(String file, long skip) throws IOException {
        BufferedReader in = null;
        try {
            client.setRestartOffset(skip);
            in = new BufferedReader(new InputStreamReader(this.getInputStream(file)));
            return in.readLine();
        } finally {
            Streams.close(in);
            client.setRestartOffset(0);
            if (in != null) {
                client.completePendingCommand();
            }
        }
    }

    public List<String> readLines(String file, long skip) throws IOException {
        return readLines(file, skip, 0);
    }

    public List<String> readLines(String file, int lines) throws IOException {
        return readLines(file, 0, lines);
    }

    /**
     * 读取多行
     *
     * @param file  文件
     * @param skip  跳过字节数
     * @param lines 读取行数
     * @return 行
     * @throws IOException IOException
     */
    public List<String> readLines(String file, long skip, int lines) throws IOException {
        BufferedReader in = null;
        try {
            client.setRestartOffset(skip);
            in = new BufferedReader(new InputStreamReader(this.getInputStream(file)));
            List<String> list = new ArrayList<>();
            if (lines > 0) {
                String line;
                for (int i = 0; i < lines && null != (line = in.readLine()); i++) {
                    list.add(line);
                }
            } else {
                String line;
                while (null != (line = in.readLine())) {
                    list.add(line);
                }
            }
            return list;
        } finally {
            Streams.close(in);
            client.setRestartOffset(0);
            if (in != null) {
                client.completePendingCommand();
            }
        }
    }

    // -------------------- write --------------------

    /**
     * 拼接字节数组到文件
     *
     * @param file 文件
     * @param bs   字节数组
     * @throws IOException IOException
     */
    public void append(String file, byte[] bs) throws IOException {
        this.append(file, bs, 0, bs.length);
    }

    /**
     * 拼接字节数组到文件
     *
     * @param file 文件
     * @param bs   字节数组
     * @param off  偏移量
     * @param len  长度
     * @throws IOException IOException
     */
    public void append(String file, byte[] bs, int off, int len) throws IOException {
        OutputStream out = null;
        try {
            this.mkdirs(Files1.getParentPath(file));
            out = this.getOutputStreamAppend(file);
            out.write(bs, off, len);
        } finally {
            Streams.close(out);
            if (out != null) {
                client.completePendingCommand();
            }
        }
    }

    /**
     * 拼接一行
     *
     * @param file 文件
     * @param line 行
     * @throws IOException IOException
     */
    public void appendLine(String file, String line) throws IOException {
        this.mkdirs(Files1.getParentPath(file));
        this.appendLines(file, Lists.singleton(line));
    }

    /**
     * 拼接多行
     *
     * @param file  文件
     * @param lines 行
     * @throws IOException IOException
     */
    public void appendLines(String file, List<String> lines) throws IOException {
        OutputStream out = null;
        try {
            this.mkdirs(Files1.getParentPath(file));
            out = this.getOutputStreamAppend(file);
            for (String line : lines) {
                out.write(Strings.bytes(line));
                out.write(Letters.LF);
            }
        } catch (Exception e) {
            throw Exceptions.io("cannot write file " + file, e);
        } finally {
            Streams.close(out);
            if (out != null) {
                client.completePendingCommand();
            }
        }
    }

    /**
     * 写入字节数组到文件
     *
     * @param file 文件
     * @param bs   字节数组
     * @throws IOException IOException
     */
    public void write(String file, byte[] bs) throws IOException {
        this.write(file, bs, 0, bs.length);
    }

    /**
     * 写入字节数组到文件
     *
     * @param file 文件
     * @param bs   字节数组
     * @param off  偏移量
     * @param len  长度
     * @throws IOException IOException
     */
    public void write(String file, byte[] bs, int off, int len) throws IOException {
        OutputStream out = null;
        try {
            this.mkdirs(Files1.getParentPath(file));
            out = this.getOutputStreamWriter(file);
            out.write(bs, off, len);
        } catch (Exception e) {
            throw Exceptions.io("cannot write file " + file, e);
        } finally {
            Streams.close(out);
            if (out != null) {
                client.completePendingCommand();
            }
        }
    }

    /**
     * 写入一行
     *
     * @param file 文件
     * @param line 行
     * @throws IOException IOException
     */
    public void writeLine(String file, String line) throws IOException {
        this.writeLines(file, Lists.singleton(line));
    }

    /**
     * 写入多行
     *
     * @param file  文件
     * @param lines 行
     * @throws IOException IOException
     */
    public void writeLines(String file, List<String> lines) throws IOException {
        OutputStream out = null;
        try {
            this.mkdirs(Files1.getParentPath(file));
            out = this.getOutputStreamWriter(file);
            for (String line : lines) {
                out.write(Strings.bytes(line));
                out.write(13);
            }
        } finally {
            Streams.close(out);
            if (out != null) {
                client.completePendingCommand();
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
            String parentPath = Files1.getParentPath(remoteFile);
            this.mkdirs(parentPath);
            client.storeFile(this.serverCharset(config.getRemoteRootDir() + remoteFile), buffer = new BufferedInputStream(in));
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
            this.change(Files1.getParentPath(path));
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
     * @throws IOException pending
     */
    public void downloadFile(String remoteFile, OutputStream out, boolean close) throws IOException {
        InputStream in = null;
        try {
            client.setRestartOffset(0);
            in = this.getInputStream(remoteFile);
            if (in == null) {
                throw Exceptions.ftp("not found file " + remoteFile);
            }
            Streams.transfer(in, out);
        } finally {
            if (close) {
                Streams.close(out);
            }
            Streams.close(in);
            client.setRestartOffset(0);
            if (in != null) {
                client.completePendingCommand();
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
            List<FtpFile> list = this.listFiles(remoteDir, false);
            for (FtpFile s : list) {
                this.downloadFile(s.getPath(), Files1.getPath(localDir + SEPARATOR + Files1.getFileName(s.getPath())));
            }
        } else {
            List<FtpFile> list = this.listDirs(remoteDir, true);
            for (FtpFile s : list) {
                Files1.mkdirs(Files1.getPath(localDir + SEPARATOR + s.getPath().substring(remoteDir.length())));
            }
            list = this.listFiles(remoteDir, true);
            for (FtpFile s : list) {
                this.downloadFile(s.getPath(), Files1.getPath(localDir + SEPARATOR + s.getPath().substring(remoteDir.length())));
            }
        }
    }

    // -------------------- big file --------------------

    public FtpUpload upload(String remote, String local) {
        return new FtpUpload(this, remote, local);
    }

    /**
     * 获取大文件上传器
     *
     * @param remote 远程文件
     * @param local  本地文件
     * @return FtpUpload
     */
    public FtpUpload upload(String remote, File local) {
        return new FtpUpload(this, remote, local);
    }

    public FtpDownload download(String remote, String local) {
        return new FtpDownload(this, remote, local);
    }

    /**
     * 获取大文件下载器
     *
     * @param remote 远程文件
     * @param local  本地文件
     * @return FtpDownload
     */
    public FtpDownload download(String remote, File local) {
        return new FtpDownload(this, remote, local);
    }

    // -------------------- list --------------------

    public List<FtpFile> listFiles() {
        return this.listFiles(Strings.EMPTY, false, false);
    }

    public List<FtpFile> listFiles(boolean child) {
        return this.listFiles(Strings.EMPTY, child, false);
    }

    public List<FtpFile> listFiles(boolean child, boolean dir) {
        return this.listFiles(Strings.EMPTY, child, dir);
    }

    public List<FtpFile> listFiles(String path) {
        return this.listFiles(path, false, false);
    }

    public List<FtpFile> listFiles(String path, boolean child) {
        return this.listFiles(path, child, false);
    }

    /**
     * 文件和文件夹列表
     *
     * @param path  路径
     * @param child 是否遍历子目录
     * @param dir   是否添加文件夹
     * @return 文件列表
     */
    private List<FtpFile> listFiles(String path, boolean child, boolean dir) {
        String base = config.getRemoteRootDir();
        List<FtpFile> list = new ArrayList<>();
        try {
            FTPFile[] files = client.listFiles(this.serverCharset(base + path));
            for (FTPFile file : files) {
                String t = Files1.getPath(this.serverCharset(path + SEPARATOR + file.getName()));
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
            Exceptions.printStacks(e);
        }
        return list;
    }

    public List<FtpFile> listDirs() {
        return this.listDirs(Strings.EMPTY, false);
    }

    public List<FtpFile> listDirs(boolean child) {
        return this.listDirs(Strings.EMPTY, child);
    }

    public List<FtpFile> listDirs(String dir) {
        return this.listDirs(dir, false);
    }

    /**
     * 列出文件夹
     *
     * @param path  路径
     * @param child 是否遍历
     * @return 文件夹
     */
    public List<FtpFile> listDirs(String path, boolean child) {
        String base = config.getRemoteRootDir();
        List<FtpFile> list = new ArrayList<>();
        try {
            FTPFile[] files = client.listFiles(this.serverCharset(base + path));
            for (FTPFile file : files) {
                String t = Files1.getPath(path + SEPARATOR + file.getName());
                if (file.isDirectory()) {
                    list.add(new FtpFile(t, file));
                    if (child) {
                        list.addAll(this.listDirs(Files1.getPath(t + SEPARATOR), true));
                    }
                }
            }
        } catch (IOException e) {
            Exceptions.printStacks(e);
        }
        return list;
    }

    public List<FtpFile> listFilesSuffix(String suffix) {
        return this.listFilesSuffix(Strings.EMPTY, suffix, false, false);
    }

    public List<FtpFile> listFilesSuffix(String suffix, boolean child) {
        return this.listFilesSuffix(Strings.EMPTY, suffix, child, false);
    }

    public List<FtpFile> listFilesSuffix(String suffix, boolean child, boolean dir) {
        return this.listFilesSuffix(Strings.EMPTY, suffix, child, dir);
    }

    public List<FtpFile> listFilesSuffix(String path, String suffix) {
        return this.listFilesSuffix(path, suffix, false, false);
    }

    public List<FtpFile> listFilesSuffix(String path, String suffix, boolean child) {
        return this.listFilesSuffix(path, suffix, child, false);
    }

    /**
     * 列出目录下的文件
     *
     * @param path   目录
     * @param suffix 后缀
     * @param child  是否递归子文件夹
     * @param dir    是否添加文件夹
     * @return 文件
     */
    public List<FtpFile> listFilesSuffix(String path, String suffix, boolean child, boolean dir) {
        return this.listFilesSearch(path, FtpFileFilter.suffix(suffix), child, dir);
    }

    public List<FtpFile> listFilesMatch(String match) {
        return this.listFilesMatch(Strings.EMPTY, match, false, false);
    }

    public List<FtpFile> listFilesMatch(String match, boolean child) {
        return this.listFilesMatch(Strings.EMPTY, match, child, false);
    }

    public List<FtpFile> listFilesMatch(String match, boolean child, boolean dir) {
        return this.listFilesMatch(Strings.EMPTY, match, child, dir);
    }

    public List<FtpFile> listFilesMatch(String path, String match) {
        return this.listFilesMatch(path, match, false, false);
    }

    public List<FtpFile> listFilesMatch(String path, String match, boolean child) {
        return this.listFilesMatch(path, match, child, false);
    }

    /**
     * 列出目录下的文件
     *
     * @param path  目录
     * @param match 名称
     * @param child 是否递归子文件夹
     * @param dir   是否添加文件夹
     * @return 文件
     */
    public List<FtpFile> listFilesMatch(String path, String match, boolean child, boolean dir) {
        return this.listFilesSearch(path, FtpFileFilter.match(match), child, dir);
    }

    public List<FtpFile> listFilesPattern(Pattern pattern) {
        return this.listFilesPattern(Strings.EMPTY, pattern, false, false);
    }

    public List<FtpFile> listFilesPattern(Pattern pattern, boolean child) {
        return this.listFilesPattern(Strings.EMPTY, pattern, child, false);
    }

    public List<FtpFile> listFilesPattern(Pattern pattern, boolean child, boolean dir) {
        return this.listFilesPattern(Strings.EMPTY, pattern, child, dir);
    }

    public List<FtpFile> listFilesPattern(String path, Pattern pattern) {
        return this.listFilesPattern(path, pattern, false, false);
    }

    public List<FtpFile> listFilesPattern(String path, Pattern pattern, boolean child) {
        return this.listFilesPattern(path, pattern, child, false);
    }

    /**
     * 列出目录下的文件
     *
     * @param path    目录
     * @param pattern 正则
     * @param child   是否递归子文件夹
     * @param dir     是否添加文件夹
     * @return 文件
     */
    public List<FtpFile> listFilesPattern(String path, Pattern pattern, boolean child, boolean dir) {
        return this.listFilesSearch(path, FtpFileFilter.pattern(pattern), child, dir);
    }

    public List<FtpFile> listFilesFilter(FtpFileFilter filter) {
        return this.listFilesFilter(Strings.EMPTY, filter, false, false);
    }

    public List<FtpFile> listFilesFilter(FtpFileFilter filter, boolean child) {
        return this.listFilesFilter(Strings.EMPTY, filter, child, false);
    }

    public List<FtpFile> listFilesFilter(FtpFileFilter filter, boolean child, boolean dir) {
        return this.listFilesFilter(Strings.EMPTY, filter, child, dir);
    }

    public List<FtpFile> listFilesFilter(String path, FtpFileFilter filter) {
        return this.listFilesFilter(path, filter, false, false);
    }

    public List<FtpFile> listFilesFilter(String path, FtpFileFilter filter, boolean child) {
        return this.listFilesFilter(path, filter, child, false);
    }

    /**
     * 列出目录下的文件
     *
     * @param path   目录
     * @param filter 过滤器
     * @param child  是否递归子文件夹
     * @param dir    是否添加文件夹
     * @return 文件
     */
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
    private List<FtpFile> listFilesSearch(String path, FtpFileFilter filter, boolean child, boolean dir) {
        String base = config.getRemoteRootDir();
        List<FtpFile> list = new ArrayList<>();
        try {
            FTPFile[] files = client.listFiles(this.serverCharset(Files1.getPath(base + path)));
            for (FTPFile file : files) {
                String fn = file.getName();
                String t = Files1.getPath(path + SEPARATOR + fn);
                boolean isDir = file.isDirectory();
                if (!isDir || dir) {
                    FtpFile f = new FtpFile(t, file);
                    if (filter.accept(f)) {
                        list.add(f);
                    }
                }
                if (isDir && child) {
                    list.addAll(this.listFilesSearch(t + SEPARATOR, filter, true, dir));
                }
            }
        } catch (IOException e) {
            Exceptions.printStacks(e);
        }
        return list;
    }

    // -------------------- option --------------------

    /**
     * 等待处理命令完毕 事务
     *
     * @return 是否完成
     * @throws IOException IOException
     */
    public boolean pending() throws IOException {
        return client.completePendingCommand();
    }

    /**
     * 设置偏移量
     *
     * @param offset offset
     */
    public void restartOffset(long offset) {
        client.setRestartOffset(offset);
    }

    /**
     * 重置io
     */
    public void reset() {
        client.setRestartOffset(0);
    }

    /**
     * 获取系统类型
     *
     * @return 系统类型
     */
    public String getSystemType() {
        try {
            return client.getSystemType();
        } catch (IOException e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 获取文件夹状态
     *
     * @return 状态
     */
    public String getStatus() {
        try {
            return client.getStatus();
        } catch (IOException e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 获取文件夹状态 会获取文件列表
     *
     * @param path 文件夹
     * @return 状态
     */
    public String getStatus(String path) {
        try {
            return client.getStatus(new String(Strings.bytes(Files1.getPath(config.getRemoteRootDir() + path)), config.getRemoteFileNameCharset()));
        } catch (IOException e) {
            throw Exceptions.ftp(e);
        }
    }

    /**
     * 获取链接
     *
     * @return client
     */
    public FTPClient client() {
        return client;
    }

    /**
     * 获取配置
     *
     * @return config
     */
    public FtpConfig config() {
        return config;
    }

    /**
     * 获取连接池
     *
     * @return 连接池
     */
    public FtpClientPool getPool() {
        return pool;
    }

    /**
     * 发送心跳
     *
     * @return true存活
     * @throws IOException IOException
     */
    public boolean sendNoop() throws IOException {
        return client.sendNoOp();
    }

    /**
     * ftp编码
     *
     * @param chars chars
     * @return 编码
     */
    public String serverCharset(String chars) {
        try {
            return new String(Strings.bytes(Files1.getPath(chars)), config.getRemoteFileNameCharset());
        } catch (UnsupportedEncodingException e) {
            throw Exceptions.unsupportedEncoding(e);
        }
    }

    /**
     * 本地编码
     *
     * @param chars chars
     * @return 编码
     */
    public String localCharset(String chars) {
        try {
            return new String(Strings.bytes(Files1.getPath(chars)), config.getLocalFileNameCharset());
        } catch (UnsupportedEncodingException e) {
            throw Exceptions.unsupportedEncoding(e);
        }
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
