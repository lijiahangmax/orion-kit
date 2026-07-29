package cn.orionsec.kit.ext.watch.folder;

import cn.orionsec.kit.ext.watch.folder.handler.DefaultWatchHandler;
import cn.orionsec.kit.ext.watch.folder.handler.WatchHandler;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

/**
 * BlockFolderWatcher 单元测试
 */
public class BlockFolderWatcherTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private BlockFolderWatcher watcher;
    private Thread watchThread;

    @After
    public void tearDown() {
        if (watcher != null) {
            watcher.close();
        }
        if (watchThread != null) {
            watchThread.interrupt();
            try {
                watchThread.join(2000);
            } catch (InterruptedException ignored) {
            }
        }
    }

    @Test
    public void testCreateEvent() throws Exception {
        File watchDir = tempFolder.newFolder("watch-dir");

        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean created = new AtomicBoolean(false);

        WatchHandler handler = new DefaultWatchHandler() {
            @Override
            public void onCreate(WatchEvent<?> event, Path path) {
                created.set(true);
                latch.countDown();
            }
        };

        watcher = new BlockFolderWatcher(handler, WatchEventKind.CREATE);
        watcher.registerPath(watchDir);

        watchThread = new Thread(watcher);
        watchThread.setDaemon(true);
        watchThread.start();

        Thread.sleep(300);

        // 在监听目录中创建文件
        File newFile = new File(watchDir, "created.txt");
        try (FileWriter fw = new FileWriter(newFile)) {
            fw.write("new content");
        }

        assertTrue("创建事件未触发", latch.await(5, TimeUnit.SECONDS));
        assertTrue(created.get());
    }

    @Test
    public void testModifyEvent() throws Exception {
        File watchDir = tempFolder.newFolder("watch-dir");
        File existingFile = new File(watchDir, "existing.txt");
        try (FileWriter fw = new FileWriter(existingFile)) {
            fw.write("initial");
        }

        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean modified = new AtomicBoolean(false);

        WatchHandler handler = new DefaultWatchHandler() {
            @Override
            public void onModify(WatchEvent<?> event, Path path) {
                modified.set(true);
                latch.countDown();
            }
        };

        watcher = new BlockFolderWatcher(handler, WatchEventKind.MODIFY);
        watcher.registerPath(watchDir);

        watchThread = new Thread(watcher);
        watchThread.setDaemon(true);
        watchThread.start();

        Thread.sleep(300);

        // 修改文件
        try (FileWriter fw = new FileWriter(existingFile)) {
            fw.write("modified content");
        }

        assertTrue("修改事件未触发", latch.await(5, TimeUnit.SECONDS));
        assertTrue(modified.get());
    }

    @Test
    public void testDeleteEvent() throws Exception {
        File watchDir = tempFolder.newFolder("watch-dir");
        File fileToDelete = new File(watchDir, "delete-me.txt");
        try (FileWriter fw = new FileWriter(fileToDelete)) {
            fw.write("to be deleted");
        }

        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean deleted = new AtomicBoolean(false);

        WatchHandler handler = new DefaultWatchHandler() {
            @Override
            public void onDelete(WatchEvent<?> event, Path path) {
                deleted.set(true);
                latch.countDown();
            }
        };

        watcher = new BlockFolderWatcher(handler, WatchEventKind.DELETE);
        watcher.registerPath(watchDir);

        watchThread = new Thread(watcher);
        watchThread.setDaemon(true);
        watchThread.start();

        Thread.sleep(300);

        // 删除文件
        assertTrue(fileToDelete.delete());

        assertTrue("删除事件未触发", latch.await(5, TimeUnit.SECONDS));
        assertTrue(deleted.get());
    }

    @Test
    public void testStopAndClose() throws Exception {
        File watchDir = tempFolder.newFolder("watch-dir");

        watcher = new BlockFolderWatcher(new DefaultWatchHandler(), WatchEventKind.CREATE);
        watcher.registerPath(watchDir);

        watchThread = new Thread(watcher);
        watchThread.setDaemon(true);
        watchThread.start();

        Thread.sleep(300);

        // close 应当停止并关闭 watchService
        watcher.close();
        watchThread.join(2000);
        assertFalse(watchThread.isAlive());
    }

    @Test
    public void testRegisterPathString() throws Exception {
        File watchDir = tempFolder.newFolder("watch-dir");

        watcher = new BlockFolderWatcher(new DefaultWatchHandler(), WatchEventKind.CREATE);
        watcher.registerPath(watchDir.getAbsolutePath());

        Collection<Path> paths = watcher.getWatchPaths();
        assertNotNull(paths);
        assertEquals(1, paths.size());
    }

    @Test
    public void testRegisterPathFile() throws Exception {
        File watchDir = tempFolder.newFolder("watch-dir");

        watcher = new BlockFolderWatcher(new DefaultWatchHandler(), WatchEventKind.CREATE);
        watcher.registerPath(watchDir);

        Collection<Path> paths = watcher.getWatchPaths();
        assertNotNull(paths);
        assertEquals(1, paths.size());
    }

    @Test
    public void testRegisterPathWithDepth() throws Exception {
        File watchDir = tempFolder.newFolder("watch-dir");
        File subDir = new File(watchDir, "sub");
        subDir.mkdirs();

        watcher = new BlockFolderWatcher(new DefaultWatchHandler(), WatchEventKind.CREATE);
        watcher.registerPath(watchDir.toPath(), 2);

        Collection<Path> paths = watcher.getWatchPaths();
        assertNotNull(paths);
        assertTrue(paths.size() >= 2);
    }

    @Test
    public void testRegisterNonExistentPath() throws Exception {
        File watchDir = new File(tempFolder.getRoot(), "non-existent-dir");
        assertFalse(watchDir.exists());

        watcher = new BlockFolderWatcher(new DefaultWatchHandler(), WatchEventKind.CREATE);
        watcher.registerPath(watchDir);

        // 目录应被自动创建
        assertTrue(watchDir.exists());
    }

    @Test
    public void testConstructorWithWatchEventKindArray() throws Exception {
        File watchDir = tempFolder.newFolder("watch-dir");

        watcher = new BlockFolderWatcher(new DefaultWatchHandler(), WatchEventKind.CREATE, WatchEventKind.MODIFY, WatchEventKind.DELETE);
        watcher.registerPath(watchDir);

        assertNotNull(watcher.getWatchPaths());
    }

    @Test
    public void testConstructorWithStandardKinds() throws Exception {
        File watchDir = tempFolder.newFolder("watch-dir");

        watcher = new BlockFolderWatcher(new DefaultWatchHandler(),
                java.nio.file.StandardWatchEventKinds.ENTRY_CREATE,
                java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY);
        watcher.registerPath(watchDir);

        assertNotNull(watcher.getWatchPaths());
    }

}
