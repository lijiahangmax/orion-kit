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
package cn.orionsec.kit.test.watch.file;

import cn.orionsec.kit.ext.watch.file.DelayFileWatcher;
import cn.orionsec.kit.ext.watch.file.handler.DefaultEventHandler;
import cn.orionsec.kit.lang.utils.Threads;
import cn.orionsec.kit.lang.utils.io.FileAttribute;

import java.io.File;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/31 0:54
 */
public class FileWatchTests {

    public static void main(String[] args) {
        DelayFileWatcher watcher = new DelayFileWatcher(2000, new DefaultEventHandler() {
            @Override
            public void onAccess(File file, FileAttribute before, FileAttribute current) {
                System.out.println("access: " + file);
            }

            @Override
            public void onModified(File file, FileAttribute before, FileAttribute current) {
                System.out.println("modified: " + file);
            }

            @Override
            public void onCreate(File file, FileAttribute current) {
                System.out.println("create: " + file);
            }

            @Override
            public void onDelete(File file, FileAttribute before) {
                System.out.println("delete: " + file);
            }
        }).addFile("C:\\Users\\ljh15\\Desktop\\t\\s.txt", "C:\\Users\\ljh15\\Desktop\\t\\e.txt");

        Threads.start(watcher);
        Threads.sleep(21000);
        watcher.stop();
        System.out.println("stop");
    }

}
