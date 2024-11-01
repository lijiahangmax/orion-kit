/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.net.ftp.client.instance;

import cn.orionsec.kit.lang.able.Destroyable;
import cn.orionsec.kit.lang.able.SafeCloseable;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.net.ftp.client.FtpFile;
import cn.orionsec.kit.net.ftp.client.FtpFileFilter;
import cn.orionsec.kit.net.ftp.client.config.FtpConfig;
import cn.orionsec.kit.net.ftp.client.pool.FtpClientPool;
import cn.orionsec.kit.net.specification.transfer.IFileDownloader;
import cn.orionsec.kit.net.specification.transfer.IFileUploader;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

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
     * 文件不存在返回 -1
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
    void remove(String file);

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
    void makeDirectories(String dir);

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
    void move(String source, String target);

    // -------------------- stream --------------------

    /**
     * 获取文件输入流
     * <p>
     * 文件不存在则报错
     * 使用完毕需要调用 client.completePendingCommand();
     * 这个操作在关闭 io 之后 | finally 中
     *
     * @param file 文件
     * @return InputStream
     * @throws IOException IOException
     */
    default InputStream openInputStream(String file) throws IOException {
        return this.openInputStream(file, 0L);
    }

    /**
     * 获取文件输入流
     * <p>
     * 文件不存在则报错
     * 使用完毕需要调用 client.completePendingCommand();
     * 这个操作在关闭 io 之后 | finally 中
     *
     * @param file 文件
     * @param skip 跳过字节数
     * @return InputStream
     * @throws IOException IOException
     */
    InputStream openInputStream(String file, long skip) throws IOException;

    /**
     * 获取文件输出流
     * <p>
     * 文件存在则自动创建
     * 使用完毕需要调用 client.completePendingCommand();
     * 这个操作在关闭 io 之后 | finally 中
     *
     * @param file 文件
     * @return OutputStream
     * @throws IOException IOException
     */
    default OutputStream openOutputStream(String file) throws IOException {
        return this.openOutputStream(file, false);
    }

    /**
     * 获取文件输出流
     * <p>
     * 文件存在则自动创建
     * 使用完毕需要调用 client.completePendingCommand();
     * 这个操作在关闭 io 之后 | finally 中
     *
     * @param file   文件
     * @param append 是否为拼接
     * @return OutputStream
     * @throws IOException IOException
     */
    OutputStream openOutputStream(String file, boolean append) throws IOException;

    // -------------------- read --------------------

    /**
     * 读取文件到数组
     * <p>
     * 文件不存在则报错
     *
     * @param file 文件
     * @param bs   数组
     * @return 读取的长度
     * @throws IOException IOException
     */
    default int read(String file, byte[] bs) throws IOException {
        return this.read(file, 0L, bs, 0, bs.length);
    }

    /**
     * 读取文件到数组
     * <p>
     * 文件不存在则报错
     *
     * @param file 文件
     * @param skip 跳过字节数
     * @param bs   数组
     * @return 读取的长度
     * @throws IOException IOException
     */
    default int read(String file, long skip, byte[] bs) throws IOException {
        return this.read(file, skip, bs, 0, bs.length);
    }

    /**
     * 读取文件到数组
     * <p>
     * 文件不存在则报错
     *
     * @param file 文件
     * @param bs   数组
     * @param off  偏移量
     * @param len  长度
     * @return 读取的长度
     * @throws IOException IOException
     */
    default int read(String file, byte[] bs, int off, int len) throws IOException {
        return this.read(file, 0L, bs, off, len);
    }

    /**
     * 读取文件到数组
     * <p>
     * 文件不存在则报错
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

    // -------------------- transfer --------------------

    /**
     * 读取字节到文件
     * <p>
     * 文件不存在则报错
     *
     * @param path 文件绝对路径
     * @param file 输出流
     * @return 已读取长度
     * @throws IOException IOException
     */
    default long transfer(String path, File file) throws IOException {
        return this.transfer(path, file.getAbsolutePath(), 0, -1);
    }

    /**
     * 读取字节到文件
     * <p>
     * 文件不存在则报错
     *
     * @param path 文件绝对路径
     * @param file 输出流
     * @param skip 跳过字数
     * @return 已读取长度
     * @throws IOException IOException
     */
    default long transfer(String path, File file, long skip) throws IOException {
        return this.transfer(path, file.getAbsolutePath(), skip, -1);
    }

    /**
     * 读取字节到文件
     * <p>
     * 文件不存在则报错
     *
     * @param path 文件绝对路径
     * @param file 输出流
     * @return 已读取长度
     * @throws IOException IOException
     */
    default long transfer(String path, String file) throws IOException {
        return this.transfer(path, file, 0, -1);
    }

    /**
     * 读取字节到文件
     * <p>
     * 文件不存在则报错
     *
     * @param path 文件绝对路径
     * @param file 输出流
     * @param skip 跳过字数
     * @return 已读取长度
     * @throws IOException IOException
     */
    default long transfer(String path, String file, long skip) throws IOException {
        return this.transfer(path, file, skip, -1);
    }

    /**
     * 读取字节到文件
     * <p>
     * 文件不存在则报错
     *
     * @param path 文件绝对路径
     * @param file 输出流
     * @param skip 跳过字数
     * @param size 读取长度 -1 读取全部
     * @return 已读取长度
     * @throws IOException IOException
     */
    long transfer(String path, String file, long skip, int size) throws IOException;

    /**
     * 读取字节到输出流
     * <p>
     * 文件不存在则报错
     *
     * @param path 文件绝对路径
     * @param out  输出流
     * @return 已读取长度
     * @throws IOException IOException
     */
    default long transfer(String path, OutputStream out) throws IOException {
        return this.transfer(path, out, 0, -1);
    }

    /**
     * 读取字节到输出流
     * <p>
     * 文件不存在则报错
     *
     * @param path 文件绝对路径
     * @param out  输出流
     * @param skip 跳过字数
     * @return 已读取长度
     * @throws IOException IOException
     */
    default long transfer(String path, OutputStream out, long skip) throws IOException {
        return this.transfer(path, out, skip, -1);
    }

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
     * <p>
     * 文件不存在则自动创建
     *
     * @param file 文件
     * @param in   in
     * @throws IOException IOException
     */
    void write(String file, InputStream in) throws IOException;

    /**
     * 写入字符
     * <p>
     * 文件不存在则自动创建
     *
     * @param file 文件
     * @param str  str
     * @throws IOException IOException
     */
    default void write(String file, String str) throws IOException {
        this.write(file, Strings.bytes(str));
    }

    /**
     * 写入字节数组到文件
     * <p>
     * 文件不存在则自动创建
     *
     * @param file 文件
     * @param bs   字节数组
     * @throws IOException IOException
     */
    default void write(String file, byte[] bs) throws IOException {
        this.write(file, bs, 0, bs.length);
    }

    /**
     * 写入字节数组到文件
     * <p>
     * 文件不存在则自动创建
     *
     * @param file 文件
     * @param bs   字节数组
     * @param off  偏移量
     * @param len  长度
     * @throws IOException IOException
     */
    void write(String file, byte[] bs, int off, int len) throws IOException;

    // -------------------- append --------------------

    /**
     * 拼接流到文件
     * <p>
     * 文件不存在则自动创建
     *
     * @param file 文件
     * @param in   in
     * @throws IOException IOException
     */
    void append(String file, InputStream in) throws IOException;

    /**
     * 拼接字符
     * <p>
     * 文件不存在则自动创建
     *
     * @param file 文件
     * @param str  str
     * @throws IOException IOException
     */
    default void append(String file, String str) throws IOException {
        this.append(file, Strings.bytes(str));
    }

    /**
     * 拼接字节数组到文件
     * <p>
     * 文件不存在则自动创建
     *
     * @param file 文件
     * @param bs   字节数组
     * @throws IOException IOException
     */
    default void append(String file, byte[] bs) throws IOException {
        this.append(file, bs, 0, bs.length);
    }

    /**
     * 拼接字节数组到文件
     * <p>
     * 文件不存在则自动创建
     *
     * @param file 文件
     * @param bs   字节数组
     * @param off  偏移量
     * @param len  长度
     * @throws IOException IOException
     */
    void append(String file, byte[] bs, int off, int len) throws IOException;

    // -------------------- upload --------------------

    /**
     * 上传文件
     * <p>
     * 远程文件存在则会覆盖
     * <p>
     * 远程文件是个文件夹则会报错
     *
     * @param remoteFile 远程文件
     * @param localFile  本地文件
     * @throws IOException IOException
     */
    default void uploadFile(String remoteFile, File localFile) throws IOException {
        this.uploadFile(remoteFile, localFile.getAbsolutePath());
    }

    /**
     * 上传文件
     * <p>
     * 远程文件存在则会覆盖
     * <p>
     * 远程文件是个文件夹则会报错
     *
     * @param remoteFile 远程文件
     * @param localFile  本地文件
     * @throws IOException IOException
     */
    void uploadFile(String remoteFile, String localFile) throws IOException;

    /**
     * 上传文件
     * <p>
     * 远程文件存在则会覆盖
     * <p>
     * 远程文件是个文件夹则会报错
     *
     * @param remoteFile 远程文件
     * @param in         input
     * @throws IOException IOException
     */
    default void uploadFile(String remoteFile, InputStream in) throws IOException {
        this.uploadFile(remoteFile, in, false);
    }

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
     * @throws IOException IOException
     */
    default void uploadDir(String remoteDir, File localDir) throws IOException {
        this.uploadDir(remoteDir, localDir.getAbsolutePath(), true);
    }

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
     * @throws IOException IOException
     */
    default void uploadDir(String remoteDir, String localDir) throws IOException {
        this.uploadDir(remoteDir, localDir, true);
    }

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
    default void uploadDir(String remoteDir, File localDir, boolean child) throws IOException {
        this.uploadDir(remoteDir, localDir.getAbsolutePath(), child);
    }

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

    /**
     * 下载文件
     *
     * @param remoteFile 远程文件路径
     * @param localFile  本地文件路径
     * @throws IOException pending
     */
    default void downloadFile(String remoteFile, File localFile) throws IOException {
        this.downloadFile(remoteFile, localFile.getAbsolutePath());
    }

    /**
     * 下载文件
     *
     * @param remoteFile 远程文件路径
     * @param localFile  本地文件
     * @throws IOException pending
     */
    void downloadFile(String remoteFile, String localFile) throws IOException;

    /**
     * 下载文件
     *
     * @param remoteFile 远程文件路径
     * @param out        output
     * @throws IOException pending
     */
    default void downloadFile(String remoteFile, OutputStream out) throws IOException {
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
    void downloadFile(String remoteFile, OutputStream out, boolean close) throws IOException;

    /**
     * 下载文件夹
     *
     * @param remoteDir 远程文件夹
     * @param localDir  本地文件夹
     * @throws IOException pending
     */
    default void downloadDir(String remoteDir, File localDir) throws IOException {
        this.downloadDir(remoteDir, localDir.getAbsolutePath(), true);
    }

    /**
     * 下载文件夹
     *
     * @param remoteDir 远程文件夹
     * @param localDir  本地文件夹
     * @throws IOException pending
     */
    default void downloadDir(String remoteDir, String localDir) throws IOException {
        this.downloadDir(remoteDir, localDir, true);
    }

    /**
     * 下载文件夹
     *
     * @param remoteDir 远程文件夹
     * @param localDir  本地文件夹
     * @param child     是否递归子文件夹下载
     * @throws IOException pending
     */
    default void downloadDir(String remoteDir, File localDir, boolean child) throws IOException {
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
    void downloadDir(String remoteDir, String localDir, boolean child) throws IOException;

    // -------------------- file transfer --------------------

    /**
     * 获取大文件下载器
     *
     * @param remote 远程文件
     * @param local  本地文件
     * @return FtpDownload
     */
    IFileUploader upload(String remote, String local);

    /**
     * 获取大文件上传器
     *
     * @param remote 远程文件
     * @param local  本地文件
     * @return FtpUpload
     */
    IFileUploader upload(String remote, File local);

    /**
     * 获取大文件下载器
     *
     * @param remote 远程文件
     * @param local  本地文件
     * @return FtpDownload
     */
    IFileDownloader download(String remote, String local);

    /**
     * 获取大文件下载器
     *
     * @param remote 远程文件
     * @param local  本地文件
     * @return FtpDownload
     */
    IFileDownloader download(String remote, File local);

    // -------------------- list --------------------

    /**
     * 列出目录下的文件
     *
     * @param path 目录
     * @return 文件
     */
    default List<FtpFile> listFiles(String path) {
        return this.listFiles(path, false, false);
    }

    /**
     * 列出目录下的文件
     *
     * @param path  目录
     * @param child 是否递归子文件夹
     * @return 文件
     */
    default List<FtpFile> listFiles(String path, boolean child) {
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
    List<FtpFile> listFiles(String path, boolean child, boolean dir);

    /**
     * 列出目录下的文件
     *
     * @param path 目录
     * @return 文件
     */
    default List<FtpFile> listDirs(String path) {
        return this.listDirs(path, false);
    }

    /**
     * 列出文件夹
     *
     * @param path  路径
     * @param child 是否遍历
     * @return 文件夹
     */
    List<FtpFile> listDirs(String path, boolean child);

    /**
     * 列出目录下的文件
     *
     * @param path   目录
     * @param filter 过滤器
     * @return 文件
     */
    default List<FtpFile> listFilesFilter(String path, FtpFileFilter filter) {
        return this.listFilesFilter(path, filter, false, false);
    }

    /**
     * 列出目录下的文件
     *
     * @param path   目录
     * @param filter 过滤器
     * @param child  是否递归子文件夹
     * @return 文件
     */
    default List<FtpFile> listFilesFilter(String path, FtpFileFilter filter, boolean child) {
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
     * 获取连接
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
     * ftp 服务文件编码
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
