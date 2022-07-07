package com.orion.ext.watch.folder;

import com.orion.ext.watch.folder.handler.WatchHandler;

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
