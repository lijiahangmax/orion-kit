package cn.orionsec.kit.ext.tail;

import cn.orionsec.kit.ext.tail.delay.DelayTracker;
import cn.orionsec.kit.ext.tail.handler.LineHandler;
import cn.orionsec.kit.ext.tail.mode.FileMinusMode;
import cn.orionsec.kit.ext.tail.mode.FileNotFoundMode;
import cn.orionsec.kit.ext.tail.mode.FileOffsetMode;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * DelayTracker 测试
 */
public class DelayTrackerTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private volatile DelayTracker tracker;

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
        LineHandler handler = (read, line, t) -> {
        };
        tracker = new DelayTracker(file, handler);
        assertNotNull(tracker);
    }

    @Test
    public void testConstructorWithString() throws IOException {
        File file = tempFolder.newFile("test.txt");
        LineHandler handler = (read, line, t) -> {
        };
        tracker = new DelayTracker(file.getAbsolutePath(), handler);
        assertNotNull(tracker);
    }

    @Test
    public void testReadLinesFromFile() throws Exception {
        File file = tempFolder.newFile("tail.txt");
        // 先写入初始内容
        writeToFile(file, "initial\n");

        List<String> lines = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch latch = new CountDownLatch(1);

        tracker = new DelayTracker(file, (read, line, t) -> {
            lines.add(read);
            if (lines.size() >= 1) {
                latch.countDown();
            }
        });
        tracker.delayMillis(50);
        tracker.offset(0);

        // 在后台线程运行
        Thread thread = new Thread(tracker);
        thread.setDaemon(true);
        thread.start();

        // 等待读取
        assertTrue(latch.await(2, TimeUnit.SECONDS));
        tracker.stop();
        thread.join(1000);

        assertFalse(lines.isEmpty());
        assertEquals("initial", lines.get(0));
    }

    @Test
    public void testAppendAndRead() throws Exception {
        File file = tempFolder.newFile("tail.txt");

        List<String> lines = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch latch = new CountDownLatch(2);

        tracker = new DelayTracker(file, (read, line, t) -> {
            lines.add(read);
            latch.countDown();
        });
        tracker.delayMillis(50);
        // offset -1 表示从文件末尾开始
        tracker.offset(-1);

        Thread thread = new Thread(tracker);
        thread.setDaemon(true);
        thread.start();

        // 等待 tracker 启动
        Thread.sleep(150);

        // 追加内容
        writeToFile(file, "line1\nline2\n");

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        tracker.stop();
        thread.join(1000);

        assertTrue(lines.size() >= 2);
        assertEquals("line1", lines.get(0));
        assertEquals("line2", lines.get(1));
    }

    @Test
    public void testOffsetByte() throws Exception {
        File file = tempFolder.newFile("tail.txt");
        // 写入内容 "abcdefghij\n" (11字节)
        writeToFile(file, "abcdefghij\n");

        List<String> lines = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch latch = new CountDownLatch(1);

        tracker = new DelayTracker(file, (read, line, t) -> {
            lines.add(read);
            latch.countDown();
        });
        tracker.delayMillis(50);
        // offset 5 bytes from end
        tracker.offset(FileOffsetMode.BYTE, 5);

        Thread thread = new Thread(tracker);
        thread.setDaemon(true);
        thread.start();

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        tracker.stop();
        thread.join(1000);

        assertFalse(lines.isEmpty());
    }

    @Test
    public void testFileNotFoundClose() throws Exception {
        File file = new File(tempFolder.getRoot(), "nonexistent.txt");

        tracker = new DelayTracker(file, (read, line, t) -> {
        });
        tracker.notFoundMode(FileNotFoundMode.CLOSE);
        tracker.delayMillis(50);

        // tail 应该直接返回，不会进入循环
        tracker.tail();
        assertFalse(tracker.isRun());
    }

    @Test(expected = Exception.class)
    public void testFileNotFoundThrows() throws Exception {
        File file = new File(tempFolder.getRoot(), "nonexistent.txt");

        tracker = new DelayTracker(file, (read, line, t) -> {
        });
        tracker.notFoundMode(FileNotFoundMode.THROWS);
        tracker.delayMillis(50);

        tracker.tail();
    }

    @Test
    public void testFileNotFoundWaitCount() throws Exception {
        File file = new File(tempFolder.getRoot(), "nonexistent.txt");

        tracker = new DelayTracker(file, (read, line, t) -> {
        });
        tracker.notFoundMode(FileNotFoundMode.WAIT_COUNT, 2);
        tracker.delayMillis(50);

        // 文件不存在，等2次后退出
        tracker.tail();
        assertFalse(tracker.isRun());
    }

    @Test
    public void testFileNotFoundWaitTimes() throws Exception {
        File file = new File(tempFolder.getRoot(), "nonexistent.txt");

        tracker = new DelayTracker(file, (read, line, t) -> {
        });
        tracker.notFoundMode(FileNotFoundMode.WAIT_TIMES, 100);
        tracker.delayMillis(50);

        tracker.tail();
        assertFalse(tracker.isRun());
    }

    @Test
    public void testMinusModeCurrent() throws Exception {
        File file = tempFolder.newFile("tail.txt");
        writeToFile(file, "initial content\n");

        List<String> lines = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch latch = new CountDownLatch(1);

        tracker = new DelayTracker(file, (read, line, t) -> {
            lines.add(read);
            latch.countDown();
        });
        tracker.delayMillis(50);
        tracker.offset(0);
        tracker.minusMode(FileMinusMode.CURRENT);

        Thread thread = new Thread(tracker);
        thread.setDaemon(true);
        thread.start();

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        tracker.stop();
        thread.join(1000);

        assertFalse(lines.isEmpty());
    }

    @Test
    public void testCharsetSetting() throws IOException {
        File file = tempFolder.newFile("test.txt");
        tracker = new DelayTracker(file, (read, line, t) -> {
        });
        tracker.charset("GBK");
        assertNotNull(tracker);
    }

    @Test
    public void testToString() throws IOException {
        File file = tempFolder.newFile("test.txt");
        tracker = new DelayTracker(file, (read, line, t) -> {
        });
        assertEquals(file.toString(), tracker.toString());
    }

    @Test
    public void testCleanMissCode() {
        String result = DelayTracker.cleanMissCode("hello");
        assertNotNull(result);
    }

    @Test
    public void testStopDuringRun() throws Exception {
        File file = tempFolder.newFile("tail.txt");
        writeToFile(file, "data\n");

        tracker = new DelayTracker(file, (read, line, t) -> {
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

    private void writeToFile(File file, String content) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file, true)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
            fos.flush();
        }
        // 确保 lastModified 改变
        file.setLastModified(System.currentTimeMillis());
    }

}
