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
package com.orion.ext.watch.file;

import com.orion.ext.watch.file.handler.EventHandler;
import com.orion.lang.constant.Const;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Threads;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.FileAttribute;
import com.orion.lang.utils.io.Files1;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件事件监听器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/27 14:26
 */
public class DelayFileWatcher extends FileWatcher {

    /**
     * 延迟时间
     */
    private final int delayMillis;

    /**
     * watchKeys
     */
    private final Map<File, FileAttribute> watchKeys;

    /**
     * event
     */
    private final FileWatchEvent[] events;

    /**
     * 事件处理器
     */
    private final EventHandler eventHandler;

    private final boolean inCreateEvent;

    private volatile boolean run;

    public DelayFileWatcher(EventHandler handler, FileWatchEvent... events) {
        this(Const.MS_S_5, handler, events);
    }

    public DelayFileWatcher(int delayMillis, EventHandler handler, FileWatchEvent... events) {
        Valid.notNull(handler, "event handler is null");
        this.delayMillis = delayMillis;
        this.eventHandler = handler;
        this.events = Arrays1.def(events, FileWatchEvent.ALL);
        this.inCreateEvent = Arrays1.contains(this.events, FileWatchEvent.CREATE);
        this.watchKeys = new LinkedHashMap<>();
    }

    /**
     * 添加观察的文件
     *
     * @param files files
     * @return this
     */
    public DelayFileWatcher addFile(String... files) {
        for (String file : files) {
            this.add(new File(file));
        }
        return this;
    }

    /**
     * 添加观察的文件
     *
     * @param files files
     * @return this
     */
    public DelayFileWatcher addFile(File... files) {
        for (File file : files) {
            this.add(file);
        }
        return this;
    }

    /**
     * 添加观察的文件
     *
     * @param file 文件夹内的文件
     * @return this
     */
    public DelayFileWatcher addFiles(String file) {
        return addFiles(new File(file), false);
    }

    /**
     * 添加观察的文件
     *
     * @param file 文件夹内的文件
     * @return this
     */
    public DelayFileWatcher addFiles(File file) {
        return addFiles(file, false);
    }

    /**
     * 添加观察的文件
     *
     * @param file  文件夹内的文件
     * @param child 是否递归子文件夹
     * @return this
     */
    public DelayFileWatcher addFiles(String file, boolean child) {
        return addFiles(new File(file), child);
    }

    /**
     * 添加观察的文件
     *
     * @param file  文件夹内的文件
     * @param child 是否递归子文件夹
     * @return this
     */
    public DelayFileWatcher addFiles(File file, boolean child) {
        List<File> files = Files1.listFiles(file, child);
        for (File f : files) {
            add(f);
        }
        return this;
    }

    private void add(File file) {
        boolean exists = file.exists();
        if (exists && file.isDirectory()) {
            return;
        }
        if (!exists && !inCreateEvent) {
            return;
        }
        watchKeys.put(file, Files1.getAttribute(file));
    }

    @Override
    public void watch() {
        this.run = true;
        while (run) {
            for (File file : watchKeys.keySet()) {
                FileAttribute currAttr = Files1.getAttribute(file);
                FileAttribute beforeAttr = watchKeys.put(file, currAttr);
                for (FileWatchEvent event : events) {
                    switch (event) {
                        case ACCESS:
                            if (beforeAttr != null && currAttr != null && beforeAttr.getAccessTime() != currAttr.getAccessTime()) {
                                eventHandler.onAccess(file, beforeAttr, currAttr);
                            }
                            break;
                        case MODIFIED:
                            if (beforeAttr != null && currAttr != null && beforeAttr.getModifiedTime() != currAttr.getModifiedTime()) {
                                eventHandler.onModified(file, beforeAttr, currAttr);
                            }
                            break;
                        case CREATE:
                            if (beforeAttr == null && currAttr != null) {
                                eventHandler.onCreate(file, currAttr);
                            }
                            break;
                        case DELETE:
                            if (beforeAttr != null && currAttr == null) {
                                eventHandler.onDelete(file, beforeAttr);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            Threads.sleep(delayMillis);
        }
    }

    @Override
    public void stop() {
        this.run = false;
    }

    @Override
    public boolean isRun() {
        return run;
    }

}

