package cn.orionsec.kit.ext.tail;

import cn.orionsec.kit.ext.tail.delay.AbstractDelayTracker;
import cn.orionsec.kit.ext.tail.delay.DelayTracker;
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
 * AbstractDelayTracker 测试
 */
public class AbstractDelayTrackerTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private volatile AbstractDelayTracker tracker;

    @After
    public void tearDown() {
        if (tracker != null) {
            tracker.stop();
            tracker.close();
        }
    }

    @Test
    public void testFluentApi() throws IOException {
        File file = tempFolder.newFile("test.txt");
        tracker = new DelayTracker(file, (read, line, t) -> {
        });

        // 测试链式调用
        AbstractDelayTracker result = tracker.charset("UTF-8")
                .offset(10)
                .delayMillis(200)
                .notFoundMode(FileNotFoundMode.CLOSE)
                .minusMode(FileMinusMode.CURRENT);

        assertNotNull(result);
        assertSame(tracker, result);
    }

    @Test
    public void testOffsetWithMode() throws IOException {
        File file = tempFolder.newFile("test.txt");
        tracker = new DelayTracker(file, (read, line, t) -> {
        });

        AbstractDelayTracker result = tracker.offset(FileOffsetMode.LINE, 5);
        assertNotNull(result);
        assertSame(tracker, result);
    }

    @Test
    public void testNotFoundModeWithTimes() throws IOException {
        File file = tempFolder.newFile("test.txt");
        tracker = new DelayTracker(file, (read, line, t) -> {
        });

        AbstractDelayTracker result = tracker.notFoundMode(FileNotFoundMode.WAIT_TIMES, 500);
        assertNotNull(result);
    }

    @Test
    public void testNotFoundModeWaitCount() throws IOException {
        File file = tempFolder.newFile("test.txt");
        tracker = new DelayTracker(file, (read, line, t) -> {
        });

        AbstractDelayTracker result = tracker.notFoundMode(FileNotFoundMode.WAIT_COUNT, 3);
        assertNotNull(result);
    }

    @Test
    public void testSetFileLastModifyTime() throws IOException {
        File file = tempFolder.newFile("test.txt");
        tracker = new DelayTracker(file, (read, line, t) -> {
        });

        boolean result = tracker.setFileLastModifyTime();
        assertTrue(result);
    }

    @Test
    public void testOffsetLineMode() throws Exception {
        File file = tempFolder.newFile("tail.txt");
        // 写入多行
        writeToFile(file, "line1\nline2\nline3\nline4\nline5\n");

        List<String> lines = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch latch = new CountDownLatch(2);

        tracker = new DelayTracker(file, (read, line, t) -> {
            lines.add(read);
            latch.countDown();
        });
        tracker.delayMillis(50);
        // 从倒数第2行开始读取
        tracker.offset(FileOffsetMode.LINE, 2);

        Thread thread = new Thread(tracker);
        thread.setDaemon(true);
        thread.start();

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        tracker.stop();
        thread.join(1000);

        assertTrue(lines.size() >= 2);
    }

    @Test
    public void testOffsetZero() throws Exception {
        File file = tempFolder.newFile("tail.txt");
        writeToFile(file, "first\nsecond\n");

        List<String> lines = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch latch = new CountDownLatch(2);

        tracker = new DelayTracker(file, (read, line, t) -> {
            lines.add(read);
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

        assertTrue(lines.size() >= 2);
        assertEquals("first", lines.get(0));
        assertEquals("second", lines.get(1));
    }

    @Test
    public void testClose() throws IOException {
        File file = tempFolder.newFile("test.txt");
        tracker = new DelayTracker(file, (read, line, t) -> {
        });
        // close 不应该抛异常（reader 为 null 时）
        tracker.close();
    }

    private void writeToFile(File file, String content) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file, true)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
            fos.flush();
        }
        file.setLastModified(System.currentTimeMillis());
    }

}
