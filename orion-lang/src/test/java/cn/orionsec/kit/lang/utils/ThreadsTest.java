package cn.orionsec.kit.lang.utils;

import org.junit.Test;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class ThreadsTest {

    @Test
    public void testSleep() {
        long start = System.currentTimeMillis();
        Threads.sleep(50);
        long elapsed = System.currentTimeMillis() - start;
        assertTrue(elapsed >= 40);
    }

    @Test
    public void testStart() throws Exception {
        AtomicInteger counter = new AtomicInteger(0);
        Threads.start(counter::incrementAndGet);
        Thread.sleep(200);
        assertEquals(1, counter.get());
    }

    @Test
    public void testCall() throws Exception {
        Future<String> future = Threads.call(() -> "result");
        assertEquals("result", future.get());
    }

    @Test
    public void testGlobalExecutorNotNull() {
        assertNotNull(Threads.GLOBAL_EXECUTOR);
        assertFalse(Threads.GLOBAL_EXECUTOR.isShutdown());
    }

    @Test
    public void testCacheExecutorNotNull() {
        assertNotNull(Threads.CACHE_EXECUTOR);
        assertFalse(Threads.CACHE_EXECUTOR.isShutdown());
    }
}
