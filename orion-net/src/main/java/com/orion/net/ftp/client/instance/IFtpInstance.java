package com.orion.net.ftp.client.instance;

import com.orion.able.Destroyable;
import com.orion.able.SafeCloseable;
import com.orion.net.base.file.transfer.IFileTransfer;
import com.orion.net.ftp.client.FtpFile;
import com.orion.net.ftp.client.FtpFileFilter;
import com.orion.net.ftp.client.config.FtpConfig;
import com.orion.net.ftp.client.pool.FtpClientPool;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * ftp 实例 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/5/20 11:18
 */
public interface IFtpInstance extends SafeCloseable, Destroyable {

    /**
     * 切换至根目录
     */
    void change();

    /**
     * 切换目录
     * <p>
     * 目标目录不存在不会报错
     *
     * @param dir dir
     */
    void change(String dir);

    /**
     * 获取工作目录
     *
     * @return 当前工作目录
     */
    String getWorkDirectory();

    /**
     * 获取上一次命令的 replyCode
     *
     * @return replyCode
     */
    int replyCode();

    /**
     * 获取上一次命令的 replyCode 对应的 message
     *
     * @return 信息
     */
    String replyMsg();

    /**
     * 获取上一次命令的reply
     *
     * @return true执行成功
     */
    boolean reply();

    /**
     * 文件是否存在
     * <p>
     * 文件不存在不会报错
     *
     * @param file 文件
     * @return true存在
     */
    boolean isExist(String file);

    /**
     * 获取文件属性
     * <p>
     * 文件不存在不会报错
     *
     * @param file 文件
     * @return 未找到返回null
     */
    FtpFile getFile(String file);

    /**
     * 获取文件大小
     * <p>
     * 文件不存在返回-1
     *
     * @param file file
     * @return size
     */
    long getSize(String file);

    /**
     * 获取文件最后修改时间
     * <p>
     * 文件不存在返回 -1
     *
     * @param file file
     * @return time
     */
    long getModifyTime(String file);

    /**
     * 设置文件最后修改时间
     * <p>
     * 文件不存在则会报错
     *
     * @param file file
     * @param time time
     */
    void setModifyTime(String file, Date time);

    /**
     * 清空文件
     * <p>
     * 如果文件不存在不会报错
     * 如果是文件夹则会报错
     *
     * @param file file
     * @throws IOException IOException
     */
    void truncate(String file) throws IOException;

    /**
     * 删除文件或递归删除文件及目录
     * <p>
     * 文件不存在不会报错
     *
     * @param file file
     */
    void rm(String file);

    /**
     * 删除文件
     * <p>
     * 文件不存在不会报错
     * 删除的是文件夹不会报错
     *
     * @param file 文件
     */
    void removeFile(String file);

    /**
     * 递归删除目录及文件
     * <p>
     * 文件不存在不会报错
     * 删除的是文件不会报错
     *
     * @param dir 目录
     */
    void removeDir(String dir);

    /**
     * 创建文件夹
     * <p>
     * 文件夹已存在或存在的为文件不会报错
     *
     * @param dir 文件夹
     */
    void mkdirs(String dir);

    /**
     * 创建文件
     * <p>
     * 文件已存在不会报错
     *
     * @param file 文件
     */
    void touch(String file);

    /**
     * 文件移动/重命名
     * <p>
     * 文件不存在不会报错
     * 目标文件存在不会报错
     *
     * @param source 原文件路径
     * @param target 移动后的路径 可以为相对路径 如果不加目录为重命名
     */
    void mv(String source, String target);

    // -------------------- stream --------------------

    InputStream openInputStream(String file) throws IOException;

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
    InputStream openInputStream(String file, long skip) throws IOException;

    OutputStream openOutputStream(String file) throws IOException;

    /**
     * 获取文件输出流
     * <p>
     * 使用完毕需要调用 client.completePendingCommand();
     * 这个操作在关闭io之后
     *
     * @param file   文件
     * @param append 是否为拼接
     * @return OutputStream
     * @throws IOException IOException
     */
    OutputStream openOutputStream(String file, boolean append) throws IOException;

    /**
     * 读取文件到流
     *
     * @param file 文件
     * @param out  输出流
     */
    void readFromFile(String file, OutputStream out) throws IOException;

    /**
     * 拼接流到文件
     *
     * @param file 文件
     * @param in   输入流
     */
    void appendToFile(String file, InputStream in) throws IOException;

    /**
     * 写入流到文件
     *
     * @param file 文件
     * @param in   输入流
     */
    void writeToFile(String file, InputStream in) throws IOException;

    // -------------------- read --------------------

    int read(String file, byte[] bs) throws IOException;

    int read(String file, long skip, byte[] bs) throws IOException;

    int read(String file, byte[] bs, int off, int len) throws IOException;

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
    int read(String file, long skip, byte[] bs, int off, int len) throws IOException;

    String readLine(String file) throws IOException;

    /**
     * 读取一行
     *
     * @param file 文件
     * @param skip 跳过字节数
     * @return 行
     * @throws IOException IOException
     */
    String readLine(String file, long skip) throws IOException;

    List<String> readLines(String file) throws IOException;

    List<String> readLines(String file, long skip) throws IOException;

    List<String> readLines(String file, int lines) throws IOException;

    /**
     * 读取多行
     *
     * @param file  文件
     * @param skip  跳过字节数
     * @param lines 读取行数 -1 读取所有行
     * @return 行
     * @throws IOException IOException
     */
    List<String> readLines(String file, long skip, int lines) throws IOException;

    // -------------------- transfer --------------------

    long transfer(String path, OutputStream out) throws IOException;

    long transfer(String path, OutputStream out, long skip) throws IOException;

    long transfer(String path, String file) throws IOException;

    long transfer(String path, String file, long skip) throws IOException;

    long transfer(String path, File file) throws IOException;

    long transfer(String path, File file, long skip) throws IOException;

    /**
     * 读取字节到输出流
     * <p>
     * 文件不存在则报错
     *
     * @param path 文件绝对路径
     * @param out  输出流
     * @param skip 跳过字数
     * @param size 读取长度 -1 读取全部
     * @return 已读取长度
     * @throws IOException IOException
     */
    long transfer(String path, OutputStream out, long skip, int size) throws IOException;

    // -------------------- write --------------------

    /**
     * 写入流到文件
     *
     * @param file 文件
     * @param in   in
     * @throws IOException IOException
     */
    void write(String file, InputStream in) throws IOException;

    /**
     * 写入字节数组到文件
     *
     * @param file 文件
     * @param bs   字节数组
     * @throws IOException IOException
     */
    void write(String file, byte[] bs) throws IOException;

    /**
     * 写入字节数组到文件
     *
     * @param file 文件
     * @param bs   字节数组
     * @param off  偏移量
     * @param len  长度
     * @throws IOException IOException
     */
    void write(String file, byte[] bs, int off, int len) throws IOException;

    /**
     * 写入一行
     *
     * @param file 文件
     * @param line 行
     * @throws IOException IOException
     */
    void writeLine(String file, String line) throws IOException;

    /**
     * 写入多行
     *
     * @param file  文件
     * @param lines 行
     * @throws IOException IOException
     */
    void writeLines(String file, List<String> lines) throws IOException;

    // -------------------- append --------------------

    /**
     * 拼接流到文件
     *
     * @param file 文件
     * @param in   in
     * @throws IOException IOException
     */
    void append(String file, InputStream in) throws IOException;

    /**
     * 拼接字节数组到文件
     *
     * @param file 文件
     * @param bs   字节数组
     * @throws IOException IOException
     */
    void append(String file, byte[] bs) throws IOException;

    /**
     * 拼接字节数组到文件
     *
     * @param file 文件
     * @param bs   字节数组
     * @param off  偏移量
     * @param len  长度
     * @throws IOException IOException
     */
    void append(String file, byte[] bs, int off, int len) throws IOException;

    /**
     * 拼接一行
     *
     * @param file 文件
     * @param line 行
     * @throws IOException IOException
     */
    void appendLine(String file, String line) throws IOException;

    /**
     * 拼接多行
     *
     * @param file  文件
     * @param lines 行
     * @throws IOException IOException
     */
    void appendLines(String file, List<String> lines) throws IOException;

    // -------------------- upload --------------------

    void uploadFile(String remoteFile, String localFile) throws IOException;

    void uploadFile(String remoteFile, File localFile) throws IOException;

    void uploadFile(String remoteFile, InputStream in) throws IOException;

    /**
     * 上传文件
     * <p>
     * 远程文件存在则会覆盖
     * <p>
     * 远程文件是个文件夹则会报错
     *
     * @param remoteFile 远程文件
     * @param in         input
     * @param close      close
     * @throws IOException IOException
     */
    void uploadFile(String remoteFile, InputStream in, boolean close) throws IOException;

    void uploadDir(String remoteDir, File localDir) throws IOException;

    void uploadDir(String remoteDir, String localDir) throws IOException;

    void uploadDir(String remoteDir, File localDir, boolean child) throws IOException;

    /**
     * 上传文件夹
     * <p>
     * /root/target > /home/ljh/target
     * 如果远程文件夹不设置 /target 则会将 /home/ljh/target 的文件传输到 /root
     * <p>
     * 远程文件夹不存在不会报错
     * 远程文件夹是个文件则会报错
     *
     * @param remoteDir 远程文件夹
     * @param localDir  本地文件夹 上传时不包含此文件夹
     * @param child     是否遍历上传
     * @throws IOException IOException
     */
    void uploadDir(String remoteDir, String localDir, boolean child) throws IOException;

    // -------------------- download --------------------

    void downloadFile(String remoteFile, String localFile) throws IOException;

    void downloadFile(String remoteFile, File localFile) throws IOException;

    void downloadFile(String remoteFile, OutputStream out) throws IOException;

    /**
     * 下载文件
     *
     * @param remoteFile 远程文件路径
     * @param out        output
     * @param close      是否自动关闭
     * @throws IOException pending
     */
    void downloadFile(String remoteFile, OutputStream out, boolean close) throws IOException;

    void downloadDir(String remoteDir, File localDir) throws IOException;

    void downloadDir(String remoteDir, String localDir) throws IOException;

    void downloadDir(String remoteDir, File localDir, boolean child) throws IOException;

    /**
     * 下载文件夹
     *
     * @param remoteDir 远程文件夹
     * @param localDir  本地文件夹
     * @param child     是否递归子文件夹下载
     * @throws IOException pending
     */
    void downloadDir(String remoteDir, String localDir, boolean child) throws IOException;

    // -------------------- big file --------------------

    IFileTransfer upload(String remote, String local);

    /**
     * 获取大文件上传器
     *
     * @param remote 远程文件
     * @param local  本地文件
     * @return FtpUpload
     */
    IFileTransfer upload(String remote, File local);

    IFileTransfer download(String remote, String local);

    /**
     * 获取大文件下载器
     *
     * @param remote 远程文件
     * @param local  本地文件
     * @return FtpDownload
     */
    IFileTransfer download(String remote, File local);

    // -------------------- list --------------------

    List<FtpFile> listFiles();

    List<FtpFile> listFiles(boolean child);

    List<FtpFile> listFiles(boolean child, boolean dir);

    List<FtpFile> listFiles(String path);

    List<FtpFile> listFiles(String path, boolean child);

    /**
     * 文件和文件夹列表
     *
     * @param path  路径
     * @param child 是否遍历子目录
     * @param dir   是否添加文件夹
     * @return 文件列表
     */
    List<FtpFile> listFiles(String path, boolean child, boolean dir);

    List<FtpFile> listDirs();

    List<FtpFile> listDirs(boolean child);

    List<FtpFile> listDirs(String dir);

    /**
     * 列出文件夹
     *
     * @param path  路径
     * @param child 是否遍历
     * @return 文件夹
     */
    List<FtpFile> listDirs(String path, boolean child);

    List<FtpFile> listFilesSuffix(String suffix);

    List<FtpFile> listFilesSuffix(String suffix, boolean child);

    List<FtpFile> listFilesSuffix(String suffix, boolean child, boolean dir);

    List<FtpFile> listFilesSuffix(String path, String suffix);

    List<FtpFile> listFilesSuffix(String path, String suffix, boolean child);

    /**
     * 列出目录下的文件
     *
     * @param path   目录
     * @param suffix 后缀
     * @param child  是否递归子文件夹
     * @param dir    是否添加文件夹
     * @return 文件
     */
    List<FtpFile> listFilesSuffix(String path, String suffix, boolean child, boolean dir);

    List<FtpFile> listFilesMatch(String match);

    List<FtpFile> listFilesMatch(String match, boolean child);

    List<FtpFile> listFilesMatch(String match, boolean child, boolean dir);

    List<FtpFile> listFilesMatch(String path, String match);

    List<FtpFile> listFilesMatch(String path, String match, boolean child);

    /**
     * 列出目录下的文件
     *
     * @param path  目录
     * @param match 名称
     * @param child 是否递归子文件夹
     * @param dir   是否添加文件夹
     * @return 文件
     */
    List<FtpFile> listFilesMatch(String path, String match, boolean child, boolean dir);

    List<FtpFile> listFilesPattern(Pattern pattern);

    List<FtpFile> listFilesPattern(Pattern pattern, boolean child);

    List<FtpFile> listFilesPattern(Pattern pattern, boolean child, boolean dir);

    List<FtpFile> listFilesPattern(String path, Pattern pattern);

    List<FtpFile> listFilesPattern(String path, Pattern pattern, boolean child);

    /**
     * 列出目录下的文件
     *
     * @param path    目录
     * @param pattern 正则
     * @param child   是否递归子文件夹
     * @param dir     是否添加文件夹
     * @return 文件
     */
    List<FtpFile> listFilesPattern(String path, Pattern pattern, boolean child, boolean dir);

    List<FtpFile> listFilesFilter(FtpFileFilter filter);

    List<FtpFile> listFilesFilter(FtpFileFilter filter, boolean child);

    List<FtpFile> listFilesFilter(FtpFileFilter filter, boolean child, boolean dir);

    List<FtpFile> listFilesFilter(String path, FtpFileFilter filter);

    List<FtpFile> listFilesFilter(String path, FtpFileFilter filter, boolean child);

    /**
     * 列出目录下的文件
     *
     * @param path   目录
     * @param filter 过滤器
     * @param child  是否递归子文件夹
     * @param dir    是否添加文件夹
     * @return 文件
     */
    List<FtpFile> listFilesFilter(String path, FtpFileFilter filter, boolean child, boolean dir);

    // -------------------- option --------------------

    /**
     * 等待处理命令完毕 事务
     *
     * @return 是否完成
     * @throws IOException IOException
     */
    boolean pending() throws IOException;

    /**
     * 设置偏移量
     *
     * @param offset offset
     */
    void restartOffset(long offset);

    /**
     * 重置 io
     */
    void reset();

    /**
     * 获取系统类型
     *
     * @return 系统类型
     */
    String getSystemType();

    /**
     * 获取文件夹状态
     *
     * @return 状态
     */
    String getStatus();

    /**
     * 获取文件夹状态 会获取文件列表
     *
     * @param path 文件夹
     * @return 状态
     */
    String getStatus(String path);

    /**
     * 获取链接
     *
     * @return client
     */
    FTPClient getClient();

    /**
     * 获取配置
     *
     * @return config
     */
    FtpConfig getConfig();

    /**
     * 获取连接池
     *
     * @return 连接池
     */
    FtpClientPool getPool();

    /**
     * 发送心跳
     *
     * @return true存活
     * @throws IOException IOException
     */
    boolean sendNoop() throws IOException;

    /**
     * ftp服务文件编码
     *
     * @param chars chars
     * @return 编码
     */
    String serverCharset(String chars);

    /**
     * 本地编码
     *
     * @param chars chars
     * @return 编码
     */
    String localCharset(String chars);

}
