package cn.orionsec.kit.lang.support.progress;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

/**
 * ByteTransferProgress 单元测试
 */
public class ByteTransferProgressTest {

    @Test
    public void testConstructorWithEnd() {
        ByteTransferProgress progress = new ByteTransferProgress(100);
        assertEquals(0, progress.getStart());
        assertEquals(100, progress.getEnd());
    }

    @Test
    public void testConstructorWithStartAndEnd() {
        ByteTransferProgress progress = new ByteTransferProgress(10, 100);
        assertEquals(10, progress.getStart());
        assertEquals(100, progress.getEnd());
    }

    @Test
    public void testStart() {
        ByteTransferProgress progress = new ByteTransferProgress(100);
        progress.start();
        assertTrue(progress.getStartTime() > 0);
    }

    @Test
    public void testAccept() {
        ByteTransferProgress progress = new ByteTransferProgress(100);
        progress.accept(50);
        assertEquals(50, progress.getCurrent());
        progress.accept(30);
        assertEquals(80, progress.getCurrent());
    }

    @Test
    public void testGetProgress() {
        ByteTransferProgress progress = new ByteTransferProgress(100);
        progress.accept(50);
        assertEquals(0.5, progress.getProgress(), 0.001);
    }

    @Test
    public void testGetProgressZeroEnd() {
        ByteTransferProgress progress = new ByteTransferProgress(0);
        assertEquals(0.0, progress.getProgress(), 0.001);
    }

    @Test
    public void testFinish() {
        ByteTransferProgress progress = new ByteTransferProgress(100);
        progress.start();
        progress.accept(100);
        progress.finish();
        assertTrue(progress.isDone());
        assertFalse(progress.isError());
        assertEquals(1.0, progress.getProgress(), 0.001);
    }

    @Test
    public void testFinishWithError() {
        ByteTransferProgress progress = new ByteTransferProgress(100);
        progress.start();
        progress.finish(true);
        assertTrue(progress.isDone());
        assertTrue(progress.isError());
    }

    @Test
    public void testFinishIdempotent() {
        ByteTransferProgress progress = new ByteTransferProgress(100);
        progress.start();
        progress.finish();
        long endTime1 = progress.getEndTime();
        progress.finish();
        // 第二次调用不会改变结束时间
        assertEquals(endTime1, progress.getEndTime());
    }

    @Test
    public void testCallback() {
        AtomicBoolean called = new AtomicBoolean(false);
        ByteTransferProgress progress = new ByteTransferProgress(100);
        progress.callback(() -> called.set(true));
        progress.finish();
        assertTrue(called.get());
    }

    @Test
    public void testReset() {
        ByteTransferProgress progress = new ByteTransferProgress(10, 100);
        progress.start();
        progress.reset();
        assertEquals(0, progress.getStart());
        assertEquals(0, progress.getStartTime());
    }

    @Test
    public void testSetCurrent() {
        ByteTransferProgress progress = new ByteTransferProgress(100);
        progress.setCurrent(75);
        assertEquals(75, progress.getCurrent());
    }

    @Test
    public void testUsedTime() {
        ByteTransferProgress progress = new ByteTransferProgress(100);
        progress.startTime(1000);
        progress.endTime(2000);
        assertEquals(1000, progress.usedTime());
    }

    @Test
    public void testSetEnd() {
        ByteTransferProgress progress = new ByteTransferProgress(100);
        progress.setEnd(200);
        assertEquals(200, progress.getEnd());
    }

    @Test
    public void testSetStart() {
        ByteTransferProgress progress = new ByteTransferProgress(100);
        progress.setStart(5);
        assertEquals(5, progress.getStart());
    }

}
