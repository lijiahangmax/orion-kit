package com.orion.watch;

import com.orion.watch.folder.WatchEventKind;

import java.io.IOException;
import java.nio.file.*;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/27 14:03
 */
public class WatchTests {

    public static void main(String[] args) throws IOException, InterruptedException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path f1 = Paths.get("C:\\Users\\ljh15\\Desktop\\t\\1");
        Path f2 = Paths.get("C:\\Users\\ljh15\\Desktop\\t\\2");
        f1.register(watchService, WatchEventKind.ALL);
        f2.register(watchService, WatchEventKind.ALL);
        for (; ; ) {
            System.out.println("s");
            WatchKey key = watchService.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    System.out.println(1);
                } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    System.out.println(2);
                } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                    System.out.println(3);
                } else if (kind == StandardWatchEventKinds.OVERFLOW) {
                    System.out.println(4);
                }
            }
            key.reset();
            System.out.println("e");
        }
    }

}
