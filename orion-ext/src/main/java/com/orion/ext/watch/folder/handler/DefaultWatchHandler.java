package com.orion.ext.watch.folder.handler;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * 空实现 事件处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/31 11:19
 */
public class DefaultWatchHandler implements WatchHandler {

    @Override
    public void onOverflow(WatchEvent<?> event, Path path) {

    }

    @Override
    public void onModify(WatchEvent<?> event, Path path) {

    }

    @Override
    public void onCreate(WatchEvent<?> event, Path path) {

    }

    @Override
    public void onDelete(WatchEvent<?> event, Path path) {

    }

}
