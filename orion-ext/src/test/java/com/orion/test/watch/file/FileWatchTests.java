package com.orion.test.watch.file;

import com.orion.ext.watch.file.DelayFileWatcher;
import com.orion.ext.watch.file.handler.DefaultEventHandler;
import com.orion.lang.utils.Threads;
import com.orion.lang.utils.io.FileAttribute;

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
