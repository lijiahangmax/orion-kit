package cn.orionsec.kit.ext.tail;

import cn.orionsec.kit.ext.tail.delay.DelayTrackerListener;
import cn.orionsec.kit.ext.tail.handler.DataHandler;
import cn.orionsec.kit.ext.tail.mode.FileNotFoundMode;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * DelayTrackerListener 测试
 */
public class DelayTrackerListenerTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private volatile DelayTrackerListener tracker;

    @After
    public void tearDown() {
        if (tracker != null) {
            tracker.stop();
            tracker.close();
        }
    }

    @Test
    public void testConstructorWithFile() throws IOException {
        File file = tempFolder.newFile("test.txt");
        DataHandler handler = (bytes, len, t) -> {
        };
        tracker = new DelayTrackerListener(file, handler);
        assertNotNull(tracker);
    }

    @Test
    public void testConstructorWithString() throws IOException {
        File file = tempFolder.newFile("test.txt");
        DataHandler handler = (bytes, len, t) -> {
        };
        tracker = new DelayTrackerListener(file.getAbsolutePath(), handler);
        assertNotNull(tracker);
    }

    @Test
    public void testReadDataFromFile() throws Exception {
        File file = tempFolder.newFile("tail.txt");
        writeToFile(file, "hello world");

        AtomicInteger totalBytes = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(1);

        tracker = new DelayTrackerListener(file, (bytes, len, t) -> {
            totalBytes.addAndGet(len);
            latch.countDown();
        });
        tracker.delayMillis(50);
        tracker.offset(0);

        Thread thread = new Thread(tracker);
        thread.setDaemon(true);
        thread.start();

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        tracker.stop();
        thread.join(1000);

        assertTrue(totalBytes.get() > 0);
    }

    @Test
    public void testAppendAndRead() throws Exception {
        File file = tempFolder.newFile("tail.txt");

        AtomicInteger totalBytes = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(1);

        tracker = new DelayTrackerListener(file, (bytes, len, t) -> {
            totalBytes.addAndGet(len);
            latch.countDown();
        });
        tracker.delayMillis(50);
        tracker.offset(-1);

        Thread thread = new Thread(tracker);
        thread.setDaemon(true);
        thread.start();

        // 等待 tracker 启动
        Thread.sleep(150);

        // 追加数据
        writeToFile(file, "new data here");

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        tracker.stop();
        thread.join(1000);

        assertTrue(totalBytes.get() > 0);
    }

    @Test
    public void testFileNotFoundClose() throws Exception {
        File file = new File(tempFolder.getRoot(), "nonexistent.txt");

        tracker = new DelayTrackerListener(file, (bytes, len, t) -> {
        });
        tracker.notFoundMode(FileNotFoundMode.CLOSE);
        tracker.delayMillis(50);

        tracker.tail();
        assertFalse(tracker.isRun());
    }

    @Test(expected = Exception.class)
    public void testFileNotFoundThrows() throws Exception {
        File file = new File(tempFolder.getRoot(), "nonexistent.txt");

        tracker = new DelayTrackerListener(file, (bytes, len, t) -> {
        });
        tracker.notFoundMode(FileNotFoundMode.THROWS);
        tracker.delayMillis(50);

        tracker.tail();
    }

    @Test
    public void testStopDuringRun() throws Exception {
        File file = tempFolder.newFile("tail.txt");
        writeToFile(file, "data");

        tracker = new DelayTrackerListener(file, (bytes, len, t) -> {
        });
        tracker.delayMillis(50);
        tracker.offset(0);

        Thread thread = new Thread(tracker);
        thread.setDaemon(true);
        thread.start();

        Thread.sleep(150);
        assertTrue(tracker.isRun());

        tracker.stop();
        thread.join(1000);
        assertFalse(tracker.isRun());
    }

    @Test
    public void testToString() throws IOException {
        File file = tempFolder.newFile("test.txt");
        tracker = new DelayTrackerListener(file, (bytes, len, t) -> {
        });
        assertEquals(file.toString(), tracker.toString());
    }

    private void writeToFile(File file, String content) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file, true)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
            fos.flush();
        }
        file.setLastModified(System.currentTimeMillis());
    }

}
