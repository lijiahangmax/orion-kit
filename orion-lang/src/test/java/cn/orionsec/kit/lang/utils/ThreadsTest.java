package cn.orionsec.kit.lang.utils;

import cn.orionsec.kit.lang.utils.random.Randoms;
import org.junit.Test;

import java.util.List;
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


    @Test
    public void testAwait() {
        List<Future<Integer>> fs = Threads.concurrent(() -> {
            int i = 1000 + Randoms.randomInt(1000, 3000);
            Threads.sleep(i);
            System.out.println(Thread.currentThread().getName() + " awake " + i);
            return i;
        }, 3, Threads.CACHE_EXECUTOR);
        Threads.await(fs);
        System.out.println("await collection end");

        Threads.await(fs.get(0), fs.get(1), fs.get(2));
        System.out.println("await varargs end");
    }

    @Test
    public void testAwaitGet() {
        List<Future<Integer>> fs = Threads.concurrent(() -> {
            int i = 1000 + Randoms.randomInt(1000, 3000);
            Threads.sleep(i);
            return i;
        }, 3, Threads.CACHE_EXECUTOR);
        System.out.println(Threads.awaitGet(fs));
        System.out.println(Threads.awaitGet(fs.get(0), fs.get(1), fs.get(2)));
    }

}
