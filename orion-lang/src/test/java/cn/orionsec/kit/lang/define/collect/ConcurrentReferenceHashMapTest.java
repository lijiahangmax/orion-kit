package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class ConcurrentReferenceHashMapTest {

    @Test
    public void testCreateDefault() {
        ConcurrentReferenceHashMap<String, String> map = new ConcurrentReferenceHashMap<>();
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testCreateWithCapacity() {
        ConcurrentReferenceHashMap<String, String> map = new ConcurrentReferenceHashMap<>(32);
        assertNotNull(map);
    }

    @Test
    public void testPutAndGet() {
        ConcurrentReferenceHashMap<String, Integer> map = new ConcurrentReferenceHashMap<>();
        map.put("key1", 1);
        map.put("key2", 2);
        assertEquals(Integer.valueOf(1), map.get("key1"));
        assertEquals(Integer.valueOf(2), map.get("key2"));
    }

    @Test
    public void testPutNullKey() {
        ConcurrentReferenceHashMap<String, Integer> map = new ConcurrentReferenceHashMap<>();
        map.put(null, 42);
        assertEquals(Integer.valueOf(42), map.get(null));
    }

    @Test
    public void testPutNullValue() {
        ConcurrentReferenceHashMap<String, Integer> map = new ConcurrentReferenceHashMap<>();
        map.put("key", null);
        assertNull(map.get("key"));
        assertTrue(map.containsKey("key"));
    }

    @Test
    public void testSize() {
        ConcurrentReferenceHashMap<String, Integer> map = new ConcurrentReferenceHashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        assertEquals(3, map.size());
    }

    @Test
    public void testRemove() {
        ConcurrentReferenceHashMap<String, Integer> map = new ConcurrentReferenceHashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        assertEquals(Integer.valueOf(1), map.remove("a"));
        assertNull(map.get("a"));
        assertEquals(1, map.size());
    }

    @Test
    public void testContainsKey() {
        ConcurrentReferenceHashMap<String, Integer> map = new ConcurrentReferenceHashMap<>();
        map.put("key", 1);
        assertTrue(map.containsKey("key"));
        assertFalse(map.containsKey("missing"));
    }

    @Test
    public void testClear() {
        ConcurrentReferenceHashMap<String, Integer> map = new ConcurrentReferenceHashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.clear();
        assertTrue(map.isEmpty());
    }

    @Test
    public void testPutIfAbsent() {
        ConcurrentReferenceHashMap<String, Integer> map = new ConcurrentReferenceHashMap<>();
        assertNull(map.putIfAbsent("key", 1));
        assertEquals(Integer.valueOf(1), map.putIfAbsent("key", 2));
        assertEquals(Integer.valueOf(1), map.get("key"));
    }

    @Test
    public void testReplace() {
        ConcurrentReferenceHashMap<String, Integer> map = new ConcurrentReferenceHashMap<>();
        map.put("key", 1);
        assertEquals(Integer.valueOf(1), map.replace("key", 2));
        assertEquals(Integer.valueOf(2), map.get("key"));
        assertNull(map.replace("missing", 3));
    }

    @Test
    public void testReplaceWithOldValue() {
        ConcurrentReferenceHashMap<String, Integer> map = new ConcurrentReferenceHashMap<>();
        map.put("key", 1);
        assertTrue(map.replace("key", 1, 2));
        assertFalse(map.replace("key", 1, 3));
        assertEquals(Integer.valueOf(2), map.get("key"));
    }

    @Test
    public void testRemoveWithValue() {
        ConcurrentReferenceHashMap<String, Integer> map = new ConcurrentReferenceHashMap<>();
        map.put("key", 1);
        assertFalse(map.remove("key", 2));
        assertTrue(map.remove("key", 1));
        assertNull(map.get("key"));
    }

    @Test
    public void testConcurrentAccess() throws InterruptedException {
        ConcurrentReferenceHashMap<Integer, Integer> map = new ConcurrentReferenceHashMap<>();
        int threads = 5;
        int perThread = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);
        for (int t = 0; t < threads; t++) {
            final int offset = t * perThread;
            executor.submit(() -> {
                for (int i = 0; i < perThread; i++) {
                    map.put(offset + i, offset + i);
                }
                latch.countDown();
            });
        }
        latch.await();
        executor.shutdown();
        assertEquals(threads * perThread, map.size());
    }

    @Test
    public void testEntrySet() {
        ConcurrentReferenceHashMap<String, Integer> map = new ConcurrentReferenceHashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        assertEquals(2, map.entrySet().size());
    }
}
