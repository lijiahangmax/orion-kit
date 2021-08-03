package com.orion.watch.folder;

import java.nio.file.StandardWatchEventKinds;

/**
 * 文件监听类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/27 12:11
 */
public enum WatchEventKind {

    /**
     * 事件丢失
     */
    OVERFLOW(StandardWatchEventKinds.OVERFLOW),

    /**
     * 修改事件
     */
    MODIFY(StandardWatchEventKinds.ENTRY_MODIFY),

    /**
     * 创建事件
     */
    CREATE(StandardWatchEventKinds.ENTRY_CREATE),

    /**
     * 删除事件
     */
    DELETE(StandardWatchEventKinds.ENTRY_DELETE);

    /**
     * 全部事件
     */
    public static final java.nio.file.WatchEvent.Kind<?>[] ALL = {
            OVERFLOW.getValue(),
            MODIFY.getValue(),
            CREATE.getValue(),
            DELETE.getValue()
    };

    private final java.nio.file.WatchEvent.Kind<?> value;

    WatchEventKind(java.nio.file.WatchEvent.Kind<?> value) {
        this.value = value;
    }

    public java.nio.file.WatchEvent.Kind<?> getValue() {
        return this.value;
    }

}
