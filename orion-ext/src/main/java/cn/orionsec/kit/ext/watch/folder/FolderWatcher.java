/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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

import cn.orionsec.kit.ext.watch.folder.handler.WatchHandler;
import cn.orionsec.kit.lang.able.SafeCloseable;
import cn.orionsec.kit.lang.able.Stoppable;
import cn.orionsec.kit.lang.able.Watchable;
import cn.orionsec.kit.lang.utils.Arrays1;
import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.io.Streams;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 文件夹监听器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/31 11:16
 */
public abstract class FolderWatcher implements Runnable, Watchable, Stoppable, SafeCloseable {

    protected WatchHandler handler;

    protected WatchEvent.Kind<?>[] kinds;

    protected WatchEvent.Modifier[] modifiers;

    protected volatile boolean run;

    protected WatchService watchService;

    protected Map<WatchKey, Path> watchKeys = new LinkedHashMap<>();

    protected FolderWatcher(WatchHandler handler, WatchEventKind... kinds) {
        this(handler, null, Arrays1.mapper(kinds, WatchEvent.Kind<?>[]::new, WatchEventKind::getValue));
    }

    protected FolderWatcher(WatchHandler handler, WatchEvent.Kind<?>... kinds) {
        this(handler, null, kinds);
    }

    protected FolderWatcher(WatchHandler handler, WatchEvent.Modifier[] modifiers, WatchEventKind... kinds) {
        this(handler, modifiers, Arrays1.mapper(kinds, WatchEvent.Kind<?>[]::new, WatchEventKind::getValue));
    }

    protected FolderWatcher(WatchHandler handler, WatchEvent.Modifier[] modifiers, WatchEvent.Kind<?>... kinds) {
        Assert.notNull(handler, "watch handler is null");
        this.handler = handler;
        this.modifiers = modifiers;
        this.kinds = Arrays1.def(kinds, WatchEventKind.ALL);
        this.init();
    }

    /**
     * 初始化
     */
    protected void init() {
        try {
            this.watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            throw Exceptions.watch(e);
        }
    }

    public void registerPath(String path) {
        this.registerPath(Paths.get(path), 1);
    }

    public void registerPath(File path) {
        this.registerPath(Paths.get(path.getAbsolutePath()), 1);
    }

    public void registerPath(URI path) {
        this.registerPath(Paths.get(path), 1);
    }

    public void registerPath(Path path) {
        this.registerPath(path, 1);
    }

    public void registerPath(String path, int maxDepth) {
        this.registerPath(Paths.get(path), maxDepth);
    }

    public void registerPath(File path, int maxDepth) {
        this.registerPath(Paths.get(path.getAbsolutePath()), maxDepth);
    }

    public void registerPath(URI path, int maxDepth) {
        this.registerPath(Paths.get(path), maxDepth);
    }

    /**
     * 注册监听的文件夹到 service
     * <p>
     * 如果文件夹不存在则创建
     * <p>
     * 可以监听到新的文件 但是监听不到新的文件夹
     *
     * @param path     path
     * @param maxDepth 递归子目录深度 =1不递归 >1递归 x - 1
     */
    public void registerPath(Path path, int maxDepth) {
        boolean exists = Files.exists(path, LinkOption.NOFOLLOW_LINKS);
        boolean isDir = Files.isDirectory(path);
        if (!exists || !isDir) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw Exceptions.ioRuntime(e);
            }
        }
        try {
            WatchKey key;
            if (Arrays1.isEmpty(modifiers)) {
                key = path.register(watchService, kinds);
            } else {
                key = path.register(watchService, kinds, modifiers);
            }
            watchKeys.put(key, path);
            // 递归注册
            if (maxDepth > 1) {
                // 遍历子目录并加入监听
                Files.walkFileTree(path, EnumSet.noneOf(FileVisitOption.class), maxDepth, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        registerPath(dir, 0);
                        return super.postVisitDirectory(dir, exc);
                    }
                });
            }
        } catch (AccessDeniedException e) {
            // ignore 禁止访问异常
        } catch (IOException e) {
            throw Exceptions.watch(e);
        }
    }

    @Override
    public void run() {
        this.run = true;
        this.watch();
    }

    @Override
    public void stop() {
        this.run = false;
    }

    @Override
    public void close() {
        this.stop();
        Streams.close(watchService);
    }

    public Collection<Path> getWatchPaths() {
        return watchKeys.values();
    }

}
