package cn.orionsec.kit.ext.tail;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tracker 抽象类测试
 */
public class TrackerTest {

    /**
     * 用于测试的 Tracker 具体实现
     */
    private static class TestTracker extends Tracker {

        private boolean tailCalled = false;

        @Override
        public void tail() {
            tailCalled = true;
            this.run = true;
        }

        @Override
        public void close() {
        }

        public boolean isTailCalled() {
            return tailCalled;
        }
    }

    @Test
    public void testInitialState() {
        TestTracker tracker = new TestTracker();
        assertFalse(tracker.isRun());
    }

    @Test
    public void testTail() {
        TestTracker tracker = new TestTracker();
        tracker.tail();
        assertTrue(tracker.isTailCalled());
        assertTrue(tracker.isRun());
    }

    @Test
    public void testRunCallsTail() {
        TestTracker tracker = new TestTracker();
        tracker.run();
        assertTrue(tracker.isTailCalled());
    }

    @Test
    public void testStop() {
        TestTracker tracker = new TestTracker();
        tracker.tail();
        assertTrue(tracker.isRun());
        tracker.stop();
        assertFalse(tracker.isRun());
    }

    @Test
    public void testIsRunnable() {
        TestTracker tracker = new TestTracker();
        assertTrue(tracker instanceof Runnable);
    }

    @Test
    public void testStartWithExecutor() throws Exception {
        TestTracker tracker = new TestTracker();
        CountDownLatch latch = new CountDownLatch(1);
        Executor executor = r -> {
            r.run();
            latch.countDown();
        };
        tracker.start(executor);
        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertTrue(tracker.isTailCalled());
        tracker.stop();
        assertFalse(tracker.isRun());
    }

}
