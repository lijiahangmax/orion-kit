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

import cn.orionsec.kit.ext.watch.folder.handler.WatchHandler;

import java.nio.file.*;

/**
 * 阻塞文件夹监听器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/31 11:39
 */
public class BlockFolderWatcher extends FolderWatcher {

    public BlockFolderWatcher(WatchHandler handler, WatchEventKind... kinds) {
        super(handler, kinds);
    }

    public BlockFolderWatcher(WatchHandler handler, WatchEvent.Kind<?>... kinds) {
        super(handler, kinds);
    }

    public BlockFolderWatcher(WatchHandler handler, WatchEvent.Modifier[] modifiers, WatchEventKind... kinds) {
        super(handler, modifiers, kinds);
    }

    public BlockFolderWatcher(WatchHandler handler, WatchEvent.Modifier[] modifiers, WatchEvent.Kind<?>... kinds) {
        super(handler, modifiers, kinds);
    }

    @Override
    public void watch() {
        while (run) {
            WatchKey wk;
            try {
                wk = watchService.take();
            } catch (InterruptedException | ClosedWatchServiceException e) {
                // 用户中断
                return;
            }
            Path currentPath = watchKeys.get(wk);
            WatchEvent.Kind<?> kind;
            for (WatchEvent<?> event : wk.pollEvents()) {
                kind = event.kind();
                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    handler.onOverflow(event, currentPath);
                } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    handler.onModify(event, currentPath);
                } else if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    handler.onCreate(event, currentPath);
                } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                    handler.onDelete(event, currentPath);
                }
            }
            wk.reset();
        }
    }

}
