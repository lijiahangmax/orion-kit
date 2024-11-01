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
package cn.orionsec.kit.test.watch.folder;

import cn.orionsec.kit.ext.watch.folder.BlockFolderWatcher;
import cn.orionsec.kit.ext.watch.folder.WatchEventKind;
import cn.orionsec.kit.ext.watch.folder.handler.DefaultWatchHandler;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/31 15:15
 */
public class FolderWatchTests {

    public static void main(String[] args) {
        BlockFolderWatcher watcher = new BlockFolderWatcher(new DefaultWatchHandler() {
            @Override
            public void onCreate(WatchEvent<?> event, Path path) {
                System.out.println("create " + path + " " + event.kind() + " " + event.count() + " " + event.context());
            }

            @Override
            public void onModify(WatchEvent<?> event, Path path) {
                System.out.println("modify " + path + " " + event.kind() + " " + event.count() + " " + event.context());
            }

            @Override
            public void onDelete(WatchEvent<?> event, Path path) {
                System.out.println("delete " + path + " " + event.kind() + " " + event.count() + " " + event.context());
            }

            @Override
            public void onOverflow(WatchEvent<?> event, Path path) {
                System.out.println("overflow " + path + " " + event.kind() + " " + event.count() + " " + event.context());
            }
        }, WatchEventKind.ALL);
        watcher.registerPath("C:\\Users\\ljh15\\Desktop\\t\\k1");
        watcher.run();

    }

}
