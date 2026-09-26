package cn.orionsec.kit.lang.id;

import cn.orionsec.kit.lang.utils.Threads;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * {@link SequenceIdWorker} 单元测试
 */
public class SequenceIdWorkerTest {

    @Test
    public void testNextId() {
        SequenceIdWorker worker = new SequenceIdWorker(1, 1);
        Long id = worker.nextId();
        assertNotNull(id);
        assertTrue(id > 0);
    }

    @Test
    public void testNextIdUnique() {
        SequenceIdWorker worker = new SequenceIdWorker(1, 1);
        Set<Long> ids = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            assertTrue("生成重复 id", ids.add(worker.nextId()));
        }
    }

    @Test
    public void testNextIdRandomSequence() {
        SequenceIdWorker worker = new SequenceIdWorker(1, 1, false, true);
        for (int i = 0; i < 1000; i++) {
            assertNotNull(worker.nextId());
        }
    }

    @Test
    public void testNextIdClockCache() {
        SequenceIdWorker worker = new SequenceIdWorker(1, 1, true, false);
        Set<Long> ids = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            assertTrue("生成重复 id", ids.add(worker.nextId()));
        }
    }

    @Test
    public void testNextIdConcurrentUnique() throws Exception {
        SequenceIdWorker worker = new SequenceIdWorker(1, 1);
        int threads = 16;
        int perThread = 2000;
        Set<Long> ids = ConcurrentHashMap.newKeySet();
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);
        for (int i = 0; i < threads; i++) {
            Threads.startVirtual(() -> {
                try {
                    start.await();
                    for (int j = 0; j < perThread; j++) {
                        ids.add(worker.nextId());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }
        start.countDown();
        assertTrue(done.await(20, TimeUnit.SECONDS));
        assertEquals(threads * perThread, ids.size());
    }

    @Test(expected = RuntimeException.class)
    public void testDataCenterIdOutOfRange() {
        new SequenceIdWorker(4, 0);
    }

    @Test(expected = RuntimeException.class)
    public void testWorkerIdOutOfRange() {
        new SequenceIdWorker(0, 256);
    }

}
