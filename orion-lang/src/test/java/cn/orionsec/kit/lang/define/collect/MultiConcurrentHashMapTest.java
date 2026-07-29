package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class MultiConcurrentHashMapTest {

    @Test
    public void testCreate() {
        MultiConcurrentHashMap<String, String, Integer> map = MultiConcurrentHashMap.create();
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testPutAndGet() {
        MultiConcurrentHashMap<String, String, Integer> map = MultiConcurrentHashMap.create();
        map.put("g1", "k1", 100);
        map.put("g1", "k2", 200);
        assertEquals(Integer.valueOf(100), map.get("g1", "k1"));
        assertEquals(Integer.valueOf(200), map.get("g1", "k2"));
    }

    @Test
    public void testConcurrentAccess() throws InterruptedException {
        MultiConcurrentHashMap<String, String, Integer> map = MultiConcurrentHashMap.create();
        int threads = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);
        for (int t = 0; t < threads; t++) {
            final int threadId = t;
            executor.submit(() -> {
                for (int i = 0; i < 50; i++) {
                    map.put("group" + threadId, "key" + i, i);
                }
                latch.countDown();
            });
        }
        latch.await();
        executor.shutdown();
        for (int t = 0; t < threads; t++) {
            assertEquals(50, map.size("group" + t));
        }
    }

    @Test
    public void testValueInitialCapacity() {
        MultiConcurrentHashMap<String, String, Integer> map = new MultiConcurrentHashMap<>(8, 4);
        map.valueInitialCapacity(16);
        map.put("g", "k", 1);
        assertEquals(Integer.valueOf(1), map.get("g", "k"));
    }
}
