package cn.orionsec.kit.lang.support.timeout;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * TimeoutChecker 单元测试
 */
public class TimeoutCheckerTest {

    @Test
    public void testTimeoutCheckersCreate() {
        TimeoutChecker<TimeoutEndpoint> checker = TimeoutCheckers.create();
        assertNotNull(checker);
        assertTrue(checker.isEmpty());
        assertTrue(checker.isRun());
    }

    @Test
    public void testTimeoutCheckersCreateWithDelay() {
        TimeoutChecker<TimeoutEndpoint> checker = TimeoutCheckers.create(1000);
        assertNotNull(checker);
        assertTrue(checker.isEmpty());
    }

    @Test
    public void testDefaultDelay() {
        assertEquals(500L, TimeoutCheckers.DEFAULT_DELAY);
    }

    @Test
    public void testAddTask() {
        TimeoutChecker<TimeoutEndpoint> checker = TimeoutCheckers.create();
        TimeoutEndpoint endpoint = new SimpleEndpoint(false, false);
        checker.addTask(endpoint);
        assertFalse(checker.isEmpty());
    }

    @Test
    public void testGetTasks() {
        TimeoutChecker<TimeoutEndpoint> checker = TimeoutCheckers.create();
        TimeoutEndpoint endpoint = new SimpleEndpoint(false, false);
        checker.addTask(endpoint);
        List<TimeoutEndpoint> tasks = checker.getTasks();
        assertEquals(1, tasks.size());
        assertSame(endpoint, tasks.get(0));
    }

    @Test
    public void testClear() {
        TimeoutChecker<TimeoutEndpoint> checker = TimeoutCheckers.create();
        checker.addTask(new SimpleEndpoint(false, false));
        checker.addTask(new SimpleEndpoint(false, false));
        assertFalse(checker.isEmpty());
        checker.clear();
        assertTrue(checker.isEmpty());
    }

    @Test
    public void testClose() throws Exception {
        TimeoutChecker<TimeoutEndpoint> checker = TimeoutCheckers.create();
        assertTrue(checker.isRun());
        checker.close();
        assertFalse(checker.isRun());
    }

    @Test
    public void testTimeoutCheckerImpl() {
        TimeoutCheckerImpl<TimeoutEndpoint> impl = new TimeoutCheckerImpl<>(100);
        assertTrue(impl.isRun());
        assertTrue(impl.isEmpty());
        impl.close();
        assertFalse(impl.isRun());
    }

    /**
     * 简单的 TimeoutEndpoint 实现用于测试
     */
    private static class SimpleEndpoint implements TimeoutEndpoint {
        private final boolean done;
        private final boolean timeout;

        SimpleEndpoint(boolean done, boolean timeout) {
            this.done = done;
            this.timeout = timeout;
        }

        @Override
        public boolean isDone() {
            return done;
        }

        @Override
        public boolean checkTimeout() {
            return timeout;
        }
    }

}
