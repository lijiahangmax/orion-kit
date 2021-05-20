package com.orion.watch.file.handler;

import com.orion.utils.io.FileAttribute;

import java.io.File;

/**
 * 事件处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/31 0:18
 */
public interface EventHandler {

    /**
     * 文件被访问
     *
     * @param file    文件
     * @param before  上次文件属性
     * @param current 当前文件属性
     */
    void onAccess(File file, FileAttribute before, FileAttribute current);

    /**
     * 文件被修改
     *
     * @param file    文件
     * @param before  上次文件属性
     * @param current 当前文件属性
     */
    void onModified(File file, FileAttribute before, FileAttribute current);

    /**
     * 文件被创建
     *
     * @param file    文件
     * @param current 当前文件属性
     */
    void onCreate(File file, FileAttribute current);

    /**
     * 文件被删除
     *
     * @param file   文件
     * @param before 上次文件属性
     */
    void onDelete(File file, FileAttribute before);

}
