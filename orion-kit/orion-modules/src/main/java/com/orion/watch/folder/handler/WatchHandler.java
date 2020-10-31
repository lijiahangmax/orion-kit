package com.orion.watch.folder.handler;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * 事件处理器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/27 12:11
 */
public interface WatchHandler {

    /**
     * 事件丢失或出错时执行的方法
     *
     * @param event 事件
     * @param path  事件发生的当前Path路径
     */
    void onOverflow(WatchEvent<?> event, Path path);

    /**
     * 文件修改时执行的方法
     *
     * @param event 事件
     * @param path  事件发生的当前Path路径
     */
    void onModify(WatchEvent<?> event, Path path);

    /**
     * 文件创建时执行的方法
     *
     * @param event 事件
     * @param path  事件发生的当前Path路径
     */
    void onCreate(WatchEvent<?> event, Path path);

    /**
     * 文件删除时执行的方法
     *
     * @param event 事件
     * @param path  事件发生的当前Path路径
     */
    void onDelete(WatchEvent<?> event, Path path);

}
