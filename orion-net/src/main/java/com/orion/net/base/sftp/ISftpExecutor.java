package com.orion.net.base.sftp;

import com.orion.lang.able.SafeCloseable;
import com.orion.lang.utils.collect.Lists;
import com.orion.net.base.sftp.transfer.IFileDownloader;
import com.orion.net.base.sftp.transfer.IFileUploader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 * sftp 执行器 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/5/18 14:49
 */
public interface ISftpExecutor extends SafeCloseable {

    /**
     * 设置缓冲区大小
     *
     * @param bufferSize 缓冲区大小
     */
    void bufferSize(int bufferSize);

    /**
     * 设置文件名称编码格式
     *
     * @param charset 编码格式
     */
    void charset(String charset);

    /**
     * 检查文件是否存在
     * <p>
     * 文件不存在不会报错
     *
     * @param path 文件绝对路径
     * @return true 存在
     */
    boolean isExist(String path);

    /**
     * 获取文件规范路径
     * <p>
     * 文件不存在返回null
     *
     * @param path 文件路径
     * @return 文件绝对路径
     */
    String getPath(String path);

    /**
     * 获取连接文件的源文件
     * <p>
     * 文件不存在 不会报错
     * 不是连接文件 不会报错
     *
     * @param path 连接文件的绝对路径
     * @return 源文件的路径 null文件不是连接文件
     */
    String getLinkPath(String path);

    /**
     * 获取文件属性
     * <p>
     * 文件不存在返回 null
     *
     * @param path 文件绝对路径
     * @return 属性
     */
    SftpFile getFile(String path);

    /**
     * 获取文件属性
     * <p>
     * 文件不存在返回 null
     *
     * @param path           文件绝对路径
     * @param followSymbolic 如果是连接文件是否返回原文件属性
     * @return 属性
     */
    SftpFile getFile(String path, boolean followSymbolic);

    /**
     * 获取文件大小
     * <p>
     * 如果不存在返回 -1
     *
     * @param path 文件绝对路径
     * @return 文件大小
     */
    long getSize(String path);

    /**
     * 设置文件属性
     * <p>
     * 文件不存在则会报错
     *
     * @param attribute 文件属性
     */
    void setFileAttribute(SftpFile attribute);

    /**
     * 设置文件修改时间
     * <p>
     * 文件不存在则会报错
     *
     * @param path 文件绝对路径
     * @param date 修改时间
     */
    void setModifyTime(String path, Date date);

    /**
     * 修改文件权限
     * <p>
     * 文件不存在则会报错
     *
     * @param file       文件绝对路径
     * @param permission 10进制表示的 8进制权限 如: 777
     */
    void changeMode(String file, int permission);

    /**
     * 修改文件所有人
     * <p>
     * 文件不存在则会报错
     *
     * @param file 文件绝对路径
     * @param uid  用户id
     */
    void changeOwner(String file, int uid);

    /**
     * 修改文件所有组
     * <p>
     * 文件不存在则会报错
     *
     * @param file 文件绝对路径
     * @param gid  组id
     */
    void changeGroup(String file, int gid);

    /**
     * 创建文件夹
     * <p>
     * 文件夹存在 路径是文件并且已存在 父文件夹不存 则会报错
     *
     * @param path 文件夹绝对路径
     */
    void makeDirectory(String path);

    /**
     * 创建文件夹 递归
     * <p>
     * 文件夹存在 父文件夹不存 不会报错
     * 路径是文件并且已存在 则会报错
     *
     * @param path 文件夹绝对路径
     */
    void makeDirectories(String path);

    /**
     * 删除一个空的文件夹
     * <p>
     * 文件夹不存在 不会报错
     * 删除的是文件 不会报错 不会删除
     * 不是空文件夹 会报错
     *
     * @param path 绝对路径
     */
    void removeDir(String path);

    /**
     * 删除一个普通文件
     * <p>
     * 文件不存在 不会报错
     * 删除的是文件夹 会报错
     *
     * @param path 绝对路径
     */
    void removeFile(String path);

    /**
     * 递归删除文件或文件夹
     *
     * @param path 路径
     */
    void remove(String path);

    /**
     * 创建文件
     * <p>
     * 文件是与存在的文件夹则会报错
     *
     * @param path 文件绝对路径
     */
    void touch(String path);

    /**
     * 创建文件 如果文件存在则截断
     * <p>
     * 文件是与存在的文件夹则会报错
     *
     * @param path 文件绝对路径
     */
    void touchTruncate(String path);

    /**
     * 创建文件
     * <p>
     * 文件是与存在的文件夹则会报错
     *
     * @param path     文件绝对路径
     * @param truncate 如果文件存在是否截断
     */
    void touch(String path, boolean truncate);

    /**
     * 清空文件, 没有则创建
     * <p>
     * 文件是与存在的文件夹则会报错
     *
     * @param path 文件绝对路径
     */
    void truncate(String path);

    /**
     * 创建连接文件
     * <p>
     * 原始文件不存在 则会报错
     * 连接文件存在 则会报错
     *
     * @param source 原文件绝对路径
     * @param target 连接文件绝对路径
     */
    void touchLink(String source, String target);

    /**
     * 移动文件 / 改名
     * <p>
     * 原始文件存在 则会报错
     * 目标文件存在 不会报错
     *
     * @param source 原文件绝对路径
     * @param target 目标文件(绝对路径 相对路径) / 文件名称
     */
    void move(String source, String target);

    // -------------------- read --------------------

    /**
     * 读取文件
     * <p>
     * 文件不存在则报错
     *
     * @param path 文件绝对路径
     * @param bs   字节数组
     * @return read len
     * @throws IOException IOException
     */
    default int read(String path, byte[] bs) throws IOException {
        return this.read(path, 0, bs, 0, bs.length);
    }

    /**
     * 读取文件
     * <p>
     * 文件不存在则报错
     *
     * @param path   文件绝对路径
     * @param bs     字节数组
     * @param offset offset
     * @param len    len
     * @return read len
     * @throws IOException IOException
     */
    default int read(String path, byte[] bs, int offset, int len) throws IOException {
        return this.read(path, 0, bs, offset, len);
    }

    /**
     * 读取文件
     * <p>
     * 文件不存在则报错
     *
     * @param path 文件绝对路径
     * @param skip 跳过字节数
     * @param bs   字节数组
     * @return read len
     * @throws IOException IOException
     */
    default int read(String path, long skip, byte[] bs) throws IOException {
        return this.read(path, skip, bs, 0, bs.length);
    }

    /**
     * 读取文件
     * <p>
     * 文件不存在则报错
     *
     * @param path   文件绝对路径
     * @param skip   跳过字节数
     * @param bs     字节数组
     * @param offset offset
     * @param len    len
     * @return read len
     * @throws IOException IOException
     */
    int read(String path, long skip, byte[] bs, int offset, int len) throws IOException;

    // -------------------- transfer --------------------

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

    /**
     * 读取文件到本地
     * <p>
     * 文件不存在则报错
     *
     * @param path 文件绝对路径
     * @param file 本地文件
     * @return 已读取长度
     * @throws IOException IOException
     */
    default long transfer(String path, File file) throws IOException {
        return transfer(path, file.getAbsolutePath(), 0);
    }

    /**
     * 读取文件到本地
     * <p>
     * 文件不存在则报错
     *
     * @param path 文件绝对路径
     * @param file 本地文件
     * @param skip 跳过字数
     * @return 已读取长度
     * @throws IOException IOException
     */
    default long transfer(String path, File file, long skip) throws IOException {
        return transfer(path, file.getAbsolutePath(), skip);

    }

    /**
     * 读取文件到本地
     * <p>
     * 文件不存在则报错
     *
     * @param path 文件绝对路径
     * @param file 本地文件路径
     * @return 已读取长度
     * @throws IOException IOException
     */
    default long transfer(String path, String file) throws IOException {
        return transfer(path, file, 0);
    }

    /**
     * 读取文件到本地
     * <p>
     * 文件不存在则报错
     *
     * @param path 文件绝对路径
     * @param file 本地文件路径
     * @param skip 跳过字数
     * @return 已读取长度
     * @throws IOException IOException
     */
    long transfer(String path, String file, long skip) throws IOException;

    // -------------------- write --------------------

    /**
     * 写入流
     * <p>
     * 文件不存在则报错
     *
     * @param path path
     * @param in   in
     * @throws IOException IOException
     */
    void write(String path, InputStream in) throws IOException;

    /**
     * 写入
     * <p>
     * 文件不存在则报错
     *
     * @param path path
     * @param bs   bs
     * @throws IOException IOException
     */
    default void write(String path, byte[] bs) throws IOException {
        this.write(path, bs, 0, bs.length);
    }

    /**
     * 写入
     * <p>
     * 文件不存在则报错
     *
     * @param path path
     * @param bs   bs
     * @param off  off
     * @param len  len
     * @throws IOException IOException
     */
    void write(String path, byte[] bs, int off, int len) throws IOException;

    /**
     * 写入一行
     * <p>
     * 文件不存在则报错
     *
     * @param path path
     * @param line line
     * @throws IOException IOException
     */
    default void writeLine(String path, String line) throws IOException {
        this.writeLines(path, Lists.singleton(line));
    }

    /**
     * 写入多行
     * <p>
     * 文件不存在则报错
     *
     * @param path  path
     * @param lines lines
     * @throws IOException IOException
     */
    void writeLines(String path, List<String> lines) throws IOException;

    // -------------------- append --------------------

    /**
     * 拼接流
     * <p>
     * 文件不存在则报错
     *
     * @param path path
     * @param in   in
     * @throws IOException IOException
     */
    void append(String path, InputStream in) throws IOException;

    /**
     * 拼接
     * <p>
     * 文件不存在则报错
     *
     * @param path path
     * @param bs   bs
     * @throws IOException IOException
     */
    default void append(String path, byte[] bs) throws IOException {
        this.append(path, bs, 0, bs.length);
    }

    /**
     * 拼接
     * <p>
     * 文件不存在则报错
     *
     * @param path path
     * @param bs   bs
     * @param off  off
     * @param len  len
     * @throws IOException IOException
     */
    void append(String path, byte[] bs, int off, int len) throws IOException;

    /**
     * 拼接一行
     * <p>
     * 文件不存在则报错
     *
     * @param path path
     * @param line line
     * @throws IOException IOException
     */
    default void appendLine(String path, String line) throws IOException {
        this.appendLines(path, Lists.singleton(line));
    }

    /**
     * 拼接多行
     * <p>
     * 文件不存在则报错
     *
     * @param path  path
     * @param lines lines
     * @throws IOException IOException
     */
    void appendLines(String path, List<String> lines) throws IOException;

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

    void downloadFile(String remoteFile, String localFile) throws IOException;

    void downloadFile(String remoteFile, File localFile) throws IOException;

    void downloadFile(String remoteFile, OutputStream out) throws IOException;

    /**
     * 下载文件
     *
     * @param remoteFile 远程文件路径
     * @param out        output
     * @param close      是否自动关闭
     * @throws IOException IOException
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

    /**
     * 获取文件上传器
     *
     * @param remote 远程文件绝对路径
     * @param local  本地文件
     * @return 文件上传器
     */
    IFileUploader upload(String remote, File local);

    /**
     * 获取文件上传器
     *
     * @param remote 远程文件绝对路径
     * @param local  本地文件
     * @return 文件上传器
     */
    IFileUploader upload(String remote, String local);

    /**
     * 获取文件上传器
     *
     * @param remote 远程文件绝对路径
     * @param local  本地文件
     * @return 文件上传器
     */
    IFileDownloader download(String remote, File local);

    /**
     * 获取文件下载器
     *
     * @param remote 远程文件绝对路径
     * @param local  本地文件
     * @return 文件下载器
     */
    IFileDownloader download(String remote, String local);

    // -------------------- list --------------------

    /**
     * 获取目录文件属性
     * <p>
     * 文件夹不存在则返回空
     *
     * @param path 文件绝对路径
     * @return 属性
     */
    List<SftpFile> list(String path);

    /**
     * 文件列表
     * <p>
     * 文件夹不存在则返回空
     *
     * @param path 文件夹绝对路径
     * @return 文件列表
     */
    default List<SftpFile> listFiles(String path) {
        return this.listFiles(path, false, false);
    }

    /**
     * 文件列表
     * <p>
     * 文件夹不存在则返回空
     *
     * @param path  文件夹绝对路径
     * @param child 是否递归子文件夹
     * @return 文件列表
     */
    default List<SftpFile> listFiles(String path, boolean child) {
        return this.listFiles(path, child, false);
    }

    /**
     * 文件列表
     * <p>
     * 文件夹不存在则返回空集合
     *
     * @param path  文件夹绝对路径
     * @param child 是否递归子文件夹
     * @param dir   是否添加文件夹
     * @return 文件列表
     */
    List<SftpFile> listFiles(String path, boolean child, boolean dir);

    /**
     * 文件夹列表
     * <p>
     * 文件夹不存在则返回空集合
     *
     * @param path 文件夹绝对路径
     * @return 文件列表
     */
    default List<SftpFile> listDirs(String path) {
        return this.listDirs(path, false);
    }

    /**
     * 文件夹列表
     * <p>
     * 文件夹不存在则返回空集合
     *
     * @param path  文件夹绝对路径
     * @param child 是否递归
     * @return 文件列表
     */
    List<SftpFile> listDirs(String path, boolean child);

    /**
     * 查询文件列表
     * <p>
     * 文件夹不存在则返回空集合
     *
     * @param path   文件夹绝对路径
     * @param filter 过滤器
     * @return 文件
     */
    default List<SftpFile> listFilesFilter(String path, FileFilter filter) {
        return this.listFilesFilter(path, filter, false, false);
    }

    /**
     * 查询文件列表
     * <p>
     * 文件夹不存在则返回空集合
     *
     * @param path   文件夹绝对路径
     * @param filter 过滤器
     * @param child  是否递归子文件夹
     * @return 文件
     */
    default List<SftpFile> listFilesFilter(String path, FileFilter filter, boolean child) {
        return this.listFilesFilter(path, filter, child, false);
    }

    /**
     * 查询文件列表
     * <p>
     * 文件夹不存在则返回空集合
     *
     * @param path   文件夹绝对路径
     * @param filter 过滤器
     * @param child  是否递归子文件夹
     * @param dir    是否添加文件夹
     * @return 文件
     */
    List<SftpFile> listFilesFilter(String path, FileFilter filter, boolean child, boolean dir);

    // -------------------- other --------------------

    /**
     * 会话是否已连接
     *
     * @return 是否已连接
     */
    boolean isConnected();

    /**
     * 获取服务端版本
     *
     * @return 服务端版本
     */
    int getServerVersion();

    /**
     * 获取缓冲区大小
     *
     * @return bufferSize
     */
    int getBufferSize();

    /**
     * 获取文件名编码
     *
     * @return 文件名编码
     */
    String getCharset();

}
