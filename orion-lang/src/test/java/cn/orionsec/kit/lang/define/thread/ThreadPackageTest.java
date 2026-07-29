package cn.orionsec.kit.lang.define.thread;

import cn.orionsec.kit.lang.define.wrapper.Tuple;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * thread 包单元测试
 */
public class ThreadPackageTest {

    // ==================== ConcurrentRunnable ====================

    @Test
    public void testConcurrentRunnableWithCyclicBarrier() throws Exception {
        int threadCount = 3;
        CyclicBarrier barrier = new CyclicBarrier(threadCount);
        AtomicInteger counter = new AtomicInteger(0);
        CountDownLatch done = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            new Thread(new ConcurrentRunnable(() -> {
                counter.incrementAndGet();
                done.countDown();
            }, barrier)).start();
        }
        assertTrue(done.await(5, TimeUnit.SECONDS));
        assertEquals(threadCount, counter.get());
    }

    @Test
    public void testConcurrentRunnableWithCountDownLatch() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean executed = new AtomicBoolean(false);
        CountDownLatch done = new CountDownLatch(1);

        new Thread(new ConcurrentRunnable(() -> {
            executed.set(true);
            done.countDown();
        }, latch)).start();

        Thread.sleep(100);
        assertFalse(executed.get());
        latch.countDown();
        assertTrue(done.await(5, TimeUnit.SECONDS));
        assertTrue(executed.get());
    }

    // ==================== ConcurrentCallable ====================

    @Test
    public void testConcurrentCallableWithCyclicBarrier() throws Exception {
        int threadCount = 3;
        CyclicBarrier barrier = new CyclicBarrier(threadCount);
        ExecutorService exec = Executors.newFixedThreadPool(threadCount);
        CountDownLatch done = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int val = i;
            exec.submit(new ConcurrentCallable<>(() -> {
                done.countDown();
                return val;
            }, barrier));
        }
        assertTrue(done.await(5, TimeUnit.SECONDS));
        exec.shutdown();
    }

    @Test
    public void testConcurrentCallableWithCountDownLatch() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        ExecutorService exec = Executors.newSingleThreadExecutor();

        Future<String> future = exec.submit(new ConcurrentCallable<>(() -> "hello", latch));
        latch.countDown();
        assertEquals("hello", future.get(5, TimeUnit.SECONDS));
        exec.shutdown();
    }

    // ==================== HookRunnable ====================

    @Test
    public void testHookRunnableSuccess() {
        AtomicBoolean taskRun = new AtomicBoolean(false);
        AtomicBoolean hookRun = new AtomicBoolean(false);

        HookRunnable hr = new HookRunnable(
                () -> taskRun.set(true),
                () -> hookRun.set(true)
        );
        hr.run();
        assertTrue(taskRun.get());
        assertTrue(hookRun.get());
    }

    @Test
    public void testHookRunnableErrorWithHook() {
        AtomicBoolean hookRun = new AtomicBoolean(false);

        HookRunnable hr = new HookRunnable(
                () -> {
                    throw new RuntimeException("err");
                },
                () -> hookRun.set(true),
                true
        );
        try {
            hr.run();
            fail();
        } catch (RuntimeException e) {
            assertEquals("err", e.getMessage());
        }
        assertTrue(hookRun.get());
    }

    @Test
    public void testHookRunnableErrorWithoutHook() {
        AtomicBoolean hookRun = new AtomicBoolean(false);

        HookRunnable hr = new HookRunnable(
                () -> {
                    throw new RuntimeException("err");
                },
                () -> hookRun.set(true),
                false
        );
        try {
            hr.run();
            fail();
        } catch (RuntimeException e) {
            assertEquals("err", e.getMessage());
        }
        assertFalse(hookRun.get());
    }

    // ==================== NamedThreadFactory ====================

    @Test
    public void testNamedThreadFactoryPrefix() {
        NamedThreadFactory factory = new NamedThreadFactory("test-worker-");
        Thread t1 = factory.newThread(() -> {
        });
        Thread t2 = factory.newThread(() -> {
        });
        assertEquals("test-worker-0", t1.getName());
        assertEquals("test-worker-1", t2.getName());
    }

    @Test
    public void testNamedThreadFactoryDaemon() {
        NamedThreadFactory factory = new NamedThreadFactory("daemon-")
                .setDaemon(true);
        Thread t = factory.newThread(() -> {
        });
        assertTrue(t.isDaemon());
    }

    @Test
    public void testNamedThreadFactoryPriority() {
        NamedThreadFactory factory = new NamedThreadFactory("prio-")
                .setPriority(8);
        Thread t = factory.newThread(() -> {
        });
        assertEquals(8, t.getPriority());
    }

    @Test
    public void testNamedThreadFactoryGroup() {
        NamedThreadFactory factory = new NamedThreadFactory("grp-")
                .setGroup("myGroup");
        Thread t = factory.newThread(() -> {
        });
        assertEquals("myGroup", t.getThreadGroup().getName());
    }

    // ==================== NamedThreadLocal ====================

    @Test
    public void testNamedThreadLocalName() {
        NamedThreadLocal<String> tl = new NamedThreadLocal<>("myLocal");
        assertEquals("myLocal", tl.toString());
    }

    @Test
    public void testNamedThreadLocalWithInitial() {
        NamedThreadLocal<Integer> tl = NamedThreadLocal.withInitial("counter", () -> 42);
        assertEquals(Integer.valueOf(42), tl.get());
        assertEquals("counter", tl.toString());
    }

    @Test
    public void testNamedThreadLocalSetGet() {
        NamedThreadLocal<String> tl = new NamedThreadLocal<>("test");
        assertNull(tl.get());
        tl.set("value");
        assertEquals("value", tl.get());
        tl.remove();
        assertNull(tl.get());
    }

    // ==================== RejectPolicy ====================

    @Test
    public void testRejectPolicyValues() {
        assertNotNull(RejectPolicy.ABORT.getHandler());
        assertNotNull(RejectPolicy.DISCARD.getHandler());
        assertNotNull(RejectPolicy.DISCARD_OLDEST.getHandler());
        assertNotNull(RejectPolicy.CALLER_RUNS.getHandler());
        assertTrue(RejectPolicy.ABORT.getHandler() instanceof ThreadPoolExecutor.AbortPolicy);
        assertTrue(RejectPolicy.DISCARD.getHandler() instanceof ThreadPoolExecutor.DiscardPolicy);
        assertTrue(RejectPolicy.CALLER_RUNS.getHandler() instanceof ThreadPoolExecutor.CallerRunsPolicy);
    }

    // ==================== TagRunnable ====================

    @Test
    public void testTagRunnableGetTag() {
        TagRunnable tr = new TagRunnable("myTag", () -> {
        });
        assertEquals("myTag", tr.getTag());
    }

    @Test
    public void testTagRunnableRun() {
        AtomicBoolean executed = new AtomicBoolean(false);
        TagRunnable tr = new TagRunnable("tag", () -> executed.set(true));
        tr.run();
        assertTrue(executed.get());
    }

    // ==================== ExecutorBuilder ====================

    @Test
    public void testExecutorBuilderBasic() {
        ThreadPoolExecutor executor = ExecutorBuilder.create()
                .corePoolSize(2)
                .maxPoolSize(4)
                .useLinkedBlockingQueue(100)
                .namedThreadFactory("test-exec-")
                .build();
        assertNotNull(executor);
        assertEquals(2, executor.getCorePoolSize());
        assertEquals(4, executor.getMaximumPoolSize());
        executor.shutdown();
    }

    @Test
    public void testExecutorBuilderWithSynchronousQueue() {
        ThreadPoolExecutor executor = ExecutorBuilder.create()
                .corePoolSize(0)
                .maxPoolSize(4)
                .useSynchronousQueue()
                .build();
        assertNotNull(executor);
        assertTrue(executor.getQueue() instanceof SynchronousQueue);
        executor.shutdown();
    }

    @Test
    public void testExecutorBuilderWithArrayBlockingQueue() {
        ThreadPoolExecutor executor = ExecutorBuilder.create()
                .corePoolSize(1)
                .maxPoolSize(2)
                .useArrayBlockingQueue(50)
                .build();
        assertNotNull(executor);
        assertTrue(executor.getQueue() instanceof ArrayBlockingQueue);
        executor.shutdown();
    }

    @Test
    public void testExecutorBuilderRejectHandler() {
        ThreadPoolExecutor executor = ExecutorBuilder.create()
                .corePoolSize(1)
                .maxPoolSize(1)
                .useLinkedBlockingQueue(1)
                .rejectHandler(RejectPolicy.CALLER_RUNS.getHandler())
                .build();
        assertNotNull(executor);
        assertTrue(executor.getRejectedExecutionHandler() instanceof ThreadPoolExecutor.CallerRunsPolicy);
        executor.shutdown();
    }

    // ==================== ThreadFactoryBuilder ====================

    @Test
    public void testThreadFactoryBuilderPrefix() {
        ThreadFactory factory = ThreadFactoryBuilder.create()
                .setPrefix("builder-")
                .build();
        Thread t = factory.newThread(() -> {
        });
        assertTrue(t.getName().startsWith("builder-"));
    }

    @Test
    public void testThreadFactoryBuilderDaemonAndPriority() {
        ThreadFactory factory = ThreadFactoryBuilder.create()
                .setPrefix("cfg-")
                .setDaemon(true)
                .setPriority(3)
                .build();
        Thread t = factory.newThread(() -> {
        });
        assertTrue(t.isDaemon());
        assertEquals(3, t.getPriority());
    }

    @Test
    public void testThreadFactoryBuilderGroup() {
        ThreadFactory factory = ThreadFactoryBuilder.create()
                .setPrefix("grp-")
                .setGroup("testGroup")
                .build();
        Thread t = factory.newThread(() -> {
        });
        assertEquals("testGroup", t.getThreadGroup().getName());
    }

    // ==================== TaskCollector ====================

    @Test
    public void testTaskCollectorCollect() {
        ExecutorService exec = Executors.newFixedThreadPool(3);
        TaskCollector collector = new TaskCollector(exec);
        Tuple result = collector.tasks(
                () -> "a",
                () -> "b",
                () -> 42
        ).collect();

        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        assertEquals(42, (int) result.get(2));
        exec.shutdown();
    }

    @Test
    public void testTaskCollectorSingleTask() {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        TaskCollector collector = new TaskCollector(exec);
        Tuple result = collector.tasks(() -> 100).collect();
        assertEquals(100, (int) result.get(0));
        exec.shutdown();
    }
}
