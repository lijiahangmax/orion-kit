package com.orion.remote.sftp;

/**
 * SFTP 文件属性过滤器
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/9 17:27
 */
@FunctionalInterface
public interface FileAttributeFilter {

    /**
     * @param attr 文件属性
     * @param path 文件夹路径
     * @return 是否可用
     */
    boolean accept(FileAttribute attr, String path);

}
