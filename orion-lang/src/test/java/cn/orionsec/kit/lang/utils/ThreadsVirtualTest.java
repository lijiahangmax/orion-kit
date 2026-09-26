package cn.orionsec.kit.lang.utils;

import cn.orionsec.kit.lang.define.thread.VirtualExecutorBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

/**
 * 虚拟线程 API 单元测试
 */
public class ThreadsVirtualTest {

    @Test
    public void testVirtualExecutorNotNull() {
        assertNotNull(Threads.VIRTUAL_EXECUTOR);
        assertFalse(Threads.VIRTUAL_EXECUTOR.isShutdown());
    }

    @Test
    public void testStartVirtual() throws Exception {
        AtomicInteger counter = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            Threads.startVirtual(() -> {
                counter.incrementAndGet();
                latch.countDown();
            });
        }
        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertEquals(10, counter.get());
    }

    @Test
    public void testStartVirtualCollection() throws Exception {
        List<Runnable> tasks = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            tasks.add(() -> {
                counter.incrementAndGet();
                latch.countDown();
            });
        }
        Threads.startVirtual(tasks);
        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertEquals(5, counter.get());
    }

    @Test
    public void testCallVirtual() throws Exception {
        Future<String> future = Threads.callVirtual(() -> "virtual-result");
        assertEquals("virtual-result", future.get(5, TimeUnit.SECONDS));
    }

    @Test
    public void testCallVirtualIsVirtualThread() throws Exception {
        Future<Boolean> future = Threads.callVirtual(Threads::isVirtualThread);
        assertTrue(future.get(5, TimeUnit.SECONDS));
        assertFalse(Threads.isVirtualThread());
    }

    @Test
    public void testNewVirtualThreadPool() throws Exception {
        ExecutorService pool = Threads.newVirtualThreadPool("test-virtual-");
        try {
            AtomicReference<String> name = new AtomicReference<>();
            Future<Boolean> future = pool.submit(() -> {
                name.set(Thread.currentThread().getName());
                return Thread.currentThread().isVirtual();
            });
            assertTrue(future.get(5, TimeUnit.SECONDS));
            assertTrue(name.get().startsWith("test-virtual-"));
        } finally {
            Threads.shutdownVirtualPool(pool, 1000);
        }
        assertTrue(pool.isShutdown());
    }

    @Test
    public void testNewVirtualThreadPoolDefaultName() throws Exception {
        ExecutorService pool = Threads.newVirtualThreadPool();
        try {
            Future<String> future = pool.submit(() -> Thread.currentThread().getName());
            String name = future.get(5, TimeUnit.SECONDS);
            assertTrue(name.startsWith(VirtualExecutorBuilder.DEFAULT_NAME_PREFIX));
        } finally {
            Threads.shutdownVirtualPool(pool, 1000);
        }
    }

    @Test
    public void testVirtualThreadScaleWithBlocking() throws Exception {
        int taskCount = 500;
        ExecutorService pool = Threads.newVirtualThreadPool("test-scale-");
        try {
            CountDownLatch latch = new CountDownLatch(taskCount);
            long start = System.currentTimeMillis();
            for (int i = 0; i < taskCount; i++) {
                pool.execute(() -> {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            assertTrue("虚拟线程阻塞任务执行超时", latch.await(10, TimeUnit.SECONDS));
            long elapsed = System.currentTimeMillis() - start;
            assertTrue("elapsed=" + elapsed, elapsed < 5000);
        } finally {
            Threads.shutdownVirtualPool(pool, 1000);
        }
    }

    @Test
    public void testSleepTimeUnit() {
        long start = System.currentTimeMillis();
        Threads.sleep(30, TimeUnit.MILLISECONDS);
        assertTrue(System.currentTimeMillis() - start >= 20);
    }

}
