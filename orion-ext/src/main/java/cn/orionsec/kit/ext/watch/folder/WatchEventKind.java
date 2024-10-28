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
package cn.orionsec.kit.ext.watch.folder;

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
