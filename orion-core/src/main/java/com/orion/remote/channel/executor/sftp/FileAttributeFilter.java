package com.orion.remote.channel.executor.sftp;

/**
 * SFTP 文件属性过滤器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/11 22:35
 */
@FunctionalInterface
public interface FileAttributeFilter {

    /**
     * accept
     *
     * @param attr 文件属性
     * @param path 文件夹路径
     * @return 是否可用
     */
    boolean accept(FileAttribute attr, String path);

}
