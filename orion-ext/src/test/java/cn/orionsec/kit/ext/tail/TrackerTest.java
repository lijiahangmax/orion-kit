package cn.orionsec.kit.ext.tail;

import org.junit.Test;

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

}
