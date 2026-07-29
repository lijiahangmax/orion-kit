package cn.orionsec.kit.ext.watch.file;

import cn.orionsec.kit.ext.watch.file.handler.DefaultEventHandler;
import cn.orionsec.kit.ext.watch.file.handler.EventHandler;
import cn.orionsec.kit.lang.utils.io.FileAttribute;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

/**
 * DelayFileWatcher 单元测试
 */
public class DelayFileWatcherTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private DelayFileWatcher watcher;
    private Thread watchThread;

    @After
    public void tearDown() {
        if (watcher != null) {
            watcher.stop();
        }
        if (watchThread != null) {
            try {
                watchThread.join(2000);
            } catch (InterruptedException ignored) {
            }
        }
    }

    @Test
    public void testModifiedEvent() throws Exception {
        File testFile = tempFolder.newFile("test.txt");
        try (FileWriter fw = new FileWriter(testFile)) {
            fw.write("initial");
        }

        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean modified = new AtomicBoolean(false);

        EventHandler handler = new DefaultEventHandler() {
            @Override
            public void onModified(File file, FileAttribute before, FileAttribute current) {
                modified.set(true);
                latch.countDown();
            }
        };

        watcher = new DelayFileWatcher(200, handler, FileWatchEvent.MODIFIED);
        watcher.addFile(testFile);

        watchThread = new Thread(watcher);
        watchThread.setDaemon(true);
        watchThread.start();

        // 等待 watcher 启动
        Thread.sleep(300);

        // 修改文件
        try (FileWriter fw = new FileWriter(testFile)) {
            fw.write("modified content");
        }

        assertTrue("修改事件未触发", latch.await(3, TimeUnit.SECONDS));
        assertTrue(modified.get());
    }

    @Test
    public void testDeleteEvent() throws Exception {
        File testFile = tempFolder.newFile("delete-me.txt");
        try (FileWriter fw = new FileWriter(testFile)) {
            fw.write("to be deleted");
        }

        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean deleted = new AtomicBoolean(false);

        EventHandler handler = new DefaultEventHandler() {
            @Override
            public void onDelete(File file, FileAttribute before) {
                deleted.set(true);
                latch.countDown();
            }
        };

        watcher = new DelayFileWatcher(200, handler, FileWatchEvent.DELETE);
        watcher.addFile(testFile);

        watchThread = new Thread(watcher);
        watchThread.setDaemon(true);
        watchThread.start();

        Thread.sleep(300);

        // 删除文件
        assertTrue(testFile.delete());

        assertTrue("删除事件未触发", latch.await(3, TimeUnit.SECONDS));
        assertTrue(deleted.get());
    }

    @Test
    public void testCreateEvent() throws Exception {
        File testFile = new File(tempFolder.getRoot(), "will-create.txt");
        // 文件还不存在

        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean created = new AtomicBoolean(false);

        EventHandler handler = new DefaultEventHandler() {
            @Override
            public void onCreate(File file, FileAttribute current) {
                created.set(true);
                latch.countDown();
            }
        };

        watcher = new DelayFileWatcher(200, handler, FileWatchEvent.CREATE);
        watcher.addFile(testFile);

        watchThread = new Thread(watcher);
        watchThread.setDaemon(true);
        watchThread.start();

        Thread.sleep(300);

        // 创建文件
        try (FileWriter fw = new FileWriter(testFile)) {
            fw.write("new file");
        }

        assertTrue("创建事件未触发", latch.await(3, TimeUnit.SECONDS));
        assertTrue(created.get());
    }

    @Test
    public void testStopWatcher() throws Exception {
        File testFile = tempFolder.newFile("stop-test.txt");

        watcher = new DelayFileWatcher(100, new DefaultEventHandler(), FileWatchEvent.MODIFIED);
        watcher.addFile(testFile);

        assertFalse(watcher.isRun());

        watchThread = new Thread(watcher);
        watchThread.setDaemon(true);
        watchThread.start();

        Thread.sleep(200);
        assertTrue(watcher.isRun());

        watcher.stop();
        Thread.sleep(300);
        assertFalse(watcher.isRun());
    }

    @Test
    public void testIsRunState() throws Exception {
        File testFile = tempFolder.newFile("state-test.txt");

        watcher = new DelayFileWatcher(100, new DefaultEventHandler(), FileWatchEvent.MODIFIED);
        watcher.addFile(testFile);

        assertFalse(watcher.isRun());

        watchThread = new Thread(watcher);
        watchThread.setDaemon(true);
        watchThread.start();

        Thread.sleep(200);
        assertTrue(watcher.isRun());

        watcher.stop();
        Thread.sleep(200);
        assertFalse(watcher.isRun());
    }

    @Test
    public void testAddMultipleFiles() throws Exception {
        File file1 = tempFolder.newFile("file1.txt");
        File file2 = tempFolder.newFile("file2.txt");

        CountDownLatch latch = new CountDownLatch(2);
        AtomicReference<Integer> count = new AtomicReference<>(0);

        EventHandler handler = new DefaultEventHandler() {
            @Override
            public void onModified(File file, FileAttribute before, FileAttribute current) {
                count.updateAndGet(v -> v + 1);
                latch.countDown();
            }
        };

        watcher = new DelayFileWatcher(200, handler, FileWatchEvent.MODIFIED);
        watcher.addFile(file1, file2);

        watchThread = new Thread(watcher);
        watchThread.setDaemon(true);
        watchThread.start();

        Thread.sleep(300);

        // 修改两个文件
        try (FileWriter fw = new FileWriter(file1)) {
            fw.write("modified1");
        }
        try (FileWriter fw = new FileWriter(file2)) {
            fw.write("modified2");
        }

        assertTrue("事件未全部触发", latch.await(3, TimeUnit.SECONDS));
        assertEquals(Integer.valueOf(2), count.get());
    }

    @Test
    public void testAddFilesByString() throws Exception {
        File testFile = tempFolder.newFile("string-path.txt");
        try (FileWriter fw = new FileWriter(testFile)) {
            fw.write("content");
        }

        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean triggered = new AtomicBoolean(false);

        EventHandler handler = new DefaultEventHandler() {
            @Override
            public void onModified(File file, FileAttribute before, FileAttribute current) {
                triggered.set(true);
                latch.countDown();
            }
        };

        watcher = new DelayFileWatcher(200, handler, FileWatchEvent.MODIFIED);
        watcher.addFile(testFile.getAbsolutePath());

        watchThread = new Thread(watcher);
        watchThread.setDaemon(true);
        watchThread.start();

        Thread.sleep(300);

        try (FileWriter fw = new FileWriter(testFile)) {
            fw.write("updated");
        }

        assertTrue("事件未触发", latch.await(3, TimeUnit.SECONDS));
        assertTrue(triggered.get());
    }

    @Test
    public void testDefaultDelayConstructor() throws Exception {
        File testFile = tempFolder.newFile("default-delay.txt");

        watcher = new DelayFileWatcher(new DefaultEventHandler(), FileWatchEvent.MODIFIED);
        watcher.addFile(testFile);
        assertNotNull(watcher);
    }

    @Test
    public void testDirectoryIsIgnored() throws Exception {
        File dir = tempFolder.newFolder("sub-dir");

        watcher = new DelayFileWatcher(200, new DefaultEventHandler(), FileWatchEvent.MODIFIED);
        watcher.addFile(dir);
        // 目录应被忽略，不会出错
        assertNotNull(watcher);
    }

    @Test
    public void testRunMethod() throws Exception {
        File testFile = tempFolder.newFile("run-test.txt");

        watcher = new DelayFileWatcher(100, new DefaultEventHandler(), FileWatchEvent.MODIFIED);
        watcher.addFile(testFile);

        // run() 应该调用 watch()
        watchThread = new Thread(watcher);
        watchThread.setDaemon(true);
        watchThread.start();

        Thread.sleep(200);
        assertTrue(watcher.isRun());

        watcher.stop();
    }

}
