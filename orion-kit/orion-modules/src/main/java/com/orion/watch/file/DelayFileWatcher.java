package com.orion.watch.file;

import com.orion.utils.Arrays1;
import com.orion.utils.Threads;
import com.orion.utils.Valid;
import com.orion.utils.io.FileAttribute;
import com.orion.utils.io.Files1;
import com.orion.watch.file.handler.EventHandler;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件事件监听器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/27 14:26
 */
public class DelayFileWatcher extends FileWatcher {

    /**
     * 延迟时间
     */
    private int delayMillis;

    /**
     * watchKeys
     */
    private Map<File, FileAttribute> watchKeys = new LinkedHashMap<>();

    /**
     * event
     */
    private FileWatchEvent[] events;

    /**
     * 时间处理器
     */
    private EventHandler eventHandler;

    private boolean inCreateEvent;

    private volatile boolean run;

    public DelayFileWatcher(EventHandler handler, FileWatchEvent... events) {
        this(5000, handler, events);
    }

    public DelayFileWatcher(int delayMillis, EventHandler handler, FileWatchEvent... events) {
        Valid.notNull(handler, "Event handler is null");
        this.delayMillis = delayMillis;
        this.eventHandler = handler;
        this.events = Arrays1.def(events, FileWatchEvent.ALL);
        this.inCreateEvent = Arrays1.contains(this.events, FileWatchEvent.CREATE);
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
        run = true;
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
        run = false;
    }

    @Override
    public boolean isRun() {
        return run;
    }

}

