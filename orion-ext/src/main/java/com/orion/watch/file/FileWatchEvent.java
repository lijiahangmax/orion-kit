package com.orion.watch.file;

/**
 * 文件监控事件
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/27 14:20
 */
public enum FileWatchEvent {

    /**
     * 文件被访问
     */
    ACCESS,

    /**
     * 文件被修改
     */
    MODIFIED,

    /**
     * 文件被创建
     */
    CREATE,

    /**
     * 文件被删除
     */
    DELETE;

    public static FileWatchEvent[] ALL = {ACCESS, MODIFIED, CREATE, DELETE};

}
