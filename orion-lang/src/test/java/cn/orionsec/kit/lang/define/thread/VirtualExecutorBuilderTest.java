package cn.orionsec.kit.lang.define.thread;

import cn.orionsec.kit.lang.utils.Threads;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * VirtualExecutorBuilder 单元测试
 */
public class VirtualExecutorBuilderTest {

    @Test
    public void testBuildWithDefaultPrefix() throws Exception {
        ExecutorService pool = VirtualExecutorBuilder.create().build();
        try {
            Future<Boolean> virtual = pool.submit(() -> Thread.currentThread().isVirtual());
            Future<String> name = pool.submit(() -> Thread.currentThread().getName());
            assertTrue(virtual.get(5, TimeUnit.SECONDS));
            assertTrue(name.get(5, TimeUnit.SECONDS).startsWith(VirtualExecutorBuilder.DEFAULT_NAME_PREFIX));
        } finally {
            Threads.shutdownVirtualPool(pool, 1000);
        }
        assertTrue(pool.isShutdown());
    }

    @Test
    public void testBuildWithNamedThreadFactory() throws Exception {
        ExecutorService pool = VirtualExecutorBuilder.create()
                .namedThreadFactory("test-builder-")
                .build();
        try {
            Future<String> future = pool.submit(() -> Thread.currentThread().getName());
            String name = future.get(5, TimeUnit.SECONDS);
            assertTrue(name.startsWith("test-builder-"));
            assertTrue(future.isDone());
        } finally {
            Threads.shutdownVirtualPool(pool, 1000);
        }
    }

    @Test
    public void testBuildWithBlankPrefix() throws Exception {
        ExecutorService pool = VirtualExecutorBuilder.create()
                .namedThreadFactory("  ")
                .build();
        try {
            Future<String> future = pool.submit(() -> Thread.currentThread().getName());
            assertTrue(future.get(5, TimeUnit.SECONDS).startsWith(VirtualExecutorBuilder.DEFAULT_NAME_PREFIX));
        } finally {
            Threads.shutdownVirtualPool(pool, 1000);
        }
    }

    @Test
    public void testBuildWithCustomThreadFactory() throws Exception {
        ThreadFactory factory = Thread.ofVirtual().name("custom-vt-", 0).factory();
        ExecutorService pool = VirtualExecutorBuilder.create()
                .threadFactory(factory)
                .build();
        try {
            Future<Boolean> virtual = pool.submit(() -> Thread.currentThread().isVirtual());
            Future<String> name = pool.submit(() -> Thread.currentThread().getName());
            assertTrue(virtual.get(5, TimeUnit.SECONDS));
            assertTrue(name.get(5, TimeUnit.SECONDS).startsWith("custom-vt-"));
        } finally {
            Threads.shutdownVirtualPool(pool, 1000);
        }
    }

    @Test
    public void testExecutorBuilderVirtualChain() throws Exception {
        ExecutorService pool = ExecutorBuilder.create()
                .virtual()
                .namedThreadFactory("chain-vt-")
                .build();
        try {
            Future<Boolean> virtual = pool.submit(() -> Thread.currentThread().isVirtual());
            Future<String> name = pool.submit(() -> Thread.currentThread().getName());
            assertTrue(virtual.get(5, TimeUnit.SECONDS));
            assertTrue(name.get(5, TimeUnit.SECONDS).startsWith("chain-vt-"));
        } finally {
            Threads.shutdownVirtualPool(pool, 1000);
        }
    }

    @Test
    public void testThreadsFactoryConsistentWithBuilder() throws Exception {
        ExecutorService pool = Threads.newVirtualThreadPool("threads-vt-");
        try {
            Future<Boolean> virtual = pool.submit(() -> Thread.currentThread().isVirtual());
            Future<String> name = pool.submit(() -> Thread.currentThread().getName());
            assertTrue(virtual.get(5, TimeUnit.SECONDS));
            assertTrue(name.get(5, TimeUnit.SECONDS).startsWith("threads-vt-"));
        } finally {
            Threads.shutdownVirtualPool(pool, 1000);
        }
    }

    @Test
    public void testConcurrentExecuting() throws Exception {
        int taskCount = 200;
        ExecutorService pool = VirtualExecutorBuilder.create()
                .namedThreadFactory("vt-builder-scale-")
                .build();
        try {
            List<Future<Integer>> futures = new ArrayList<>();
            for (int i = 0; i < taskCount; i++) {
                final int index = i;
                futures.add(pool.submit(() -> {
                    Thread.sleep(10);
                    return index;
                }));
            }
            int sum = 0;
            for (Future<Integer> future : futures) {
                sum += future.get(10, TimeUnit.SECONDS);
            }
            assertEquals(taskCount * (taskCount - 1) / 2, sum);
        } finally {
            Threads.shutdownVirtualPool(pool, 1000);
        }
    }

}
