package com.orion.net.remote.connection.sftp;

import ch.ethz.ssh2.SFTPv3FileHandle;

/**
 * connection sftp 执行器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/15 14:49
 */
public interface IConnectionSftpExecutor {

    /**
     * 设置请求并行数量
     *
     * @param parallelism 请求并行数量
     */
    void requestParallelism(int parallelism);

    /**
     * 创建文件夹
     *
     * @param path        path
     * @param permissions 8进制权限
     */
    void makeDirectory(String path, int permissions);

    /**
     * 创建文件夹 递归
     *
     * @param path        path
     * @param permissions 8进制权限
     */
    void makeDirectories(String path, int permissions);

    /**
     * 关闭文件
     *
     * @param handle 文件处理器
     * @return true成功
     */
    boolean closeFile(SFTPv3FileHandle handle);

}
