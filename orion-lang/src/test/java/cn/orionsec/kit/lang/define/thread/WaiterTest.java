package cn.orionsec.kit.lang.define.thread;

import cn.orionsec.kit.lang.utils.Threads;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;

/**
 * Waiter 单元测试
 */
public class WaiterTest {

    @Test
    public void testAwaitTimeout() {
        Waiter waiter = new Waiter();
        long start = System.currentTimeMillis();
        boolean signaled = waiter.await(100);
        long elapsed = System.currentTimeMillis() - start;
        assertFalse(signaled);
        assertTrue("elapsed=" + elapsed, elapsed >= 80);
    }

    @Test
    public void testAwaitZeroOrNegative() {
        Waiter waiter = new Waiter();
        assertFalse(waiter.await(0));
        assertFalse(waiter.await(-1));
    }

    @Test
    public void testSignalWakeup() throws Exception {
        Waiter waiter = new Waiter();
        CountDownLatch started = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(1);
        AtomicBoolean result = new AtomicBoolean(false);
        AtomicLong elapsed = new AtomicLong();
        Threads.startVirtual(() -> {
            try {
                started.countDown();
                long start = System.currentTimeMillis();
                result.set(waiter.await(10_000));
                elapsed.set(System.currentTimeMillis() - start);
            } finally {
                done.countDown();
            }
        });
        assertTrue(started.await(5, TimeUnit.SECONDS));
        Thread.sleep(100);
        waiter.signal();
        assertTrue("等待线程未在 signal 后结束", done.await(5, TimeUnit.SECONDS));
        assertTrue("应被 signal 唤醒", result.get());
        assertTrue("唤醒延迟应远小于等待时间, elapsed=" + elapsed.get(), elapsed.get() < 5000);
    }

    @Test
    public void testSignalBeforeAwait() {
        Waiter waiter = new Waiter();
        waiter.signal();
        long start = System.currentTimeMillis();
        boolean result = waiter.await(10_000);
        long elapsed = System.currentTimeMillis() - start;
        assertTrue(result);
        assertTrue("先唤醒后等待应立即返回, elapsed=" + elapsed, elapsed < 1000);
    }

    @Test
    public void testInterruptPreserved() throws Exception {
        Waiter waiter = new Waiter();
        AtomicBoolean interrupted = new AtomicBoolean(false);
        Thread thread = new Thread(() -> {
            waiter.await(10_000);
            interrupted.set(Thread.currentThread().isInterrupted());
        });
        thread.start();
        Thread.sleep(100);
        thread.interrupt();
        thread.join(3000);
        assertFalse("等待线程未被中断唤醒", thread.isAlive());
        assertTrue("等待线程应保留中断状态", interrupted.get());
    }

    @Test
    public void testMultipleWaiters() throws Exception {
        Waiter waiter = new Waiter();
        int count = 4;
        CountDownLatch started = new CountDownLatch(count);
        CountDownLatch done = new CountDownLatch(count);
        AtomicInteger signaled = new AtomicInteger();
        for (int i = 0; i < count; i++) {
            Threads.startVirtual(() -> {
                started.countDown();
                if (waiter.await(10_000)) {
                    signaled.incrementAndGet();
                }
                done.countDown();
            });
        }
        assertTrue(started.await(5, TimeUnit.SECONDS));
        Thread.sleep(100);
        waiter.signal();
        assertTrue("所有等待者都应被唤醒", done.await(5, TimeUnit.SECONDS));
        assertEquals(count, signaled.get());
    }

    @Test
    public void testSignalConsumedOnce() {
        Waiter waiter = new Waiter();
        waiter.signal();
        waiter.signal();
        assertTrue(waiter.await(1000));
        long start = System.currentTimeMillis();
        assertFalse(waiter.await(200));
        assertTrue("第二次等待应超时, elapsed=" + (System.currentTimeMillis() - start), System.currentTimeMillis() - start >= 150);
    }

}
