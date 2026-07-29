package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class ConcurrentHashSetTest {

    @Test
    public void testCreateEmpty() {
        ConcurrentHashSet<String> set = new ConcurrentHashSet<>();
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
    }

    @Test
    public void testCreateWithCapacity() {
        ConcurrentHashSet<String> set = new ConcurrentHashSet<>(32);
        assertTrue(set.isEmpty());
    }

    @Test
    public void testCreateFromCollection() {
        List<String> list = Arrays.asList("a", "b", "c");
        ConcurrentHashSet<String> set = ConcurrentHashSet.create(list);
        assertEquals(3, set.size());
        assertTrue(set.contains("a"));
        assertTrue(set.contains("b"));
        assertTrue(set.contains("c"));
    }

    @Test
    public void testCreateFromMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("x", 1);
        map.put("y", 2);
        ConcurrentHashSet<String> set = ConcurrentHashSet.create(map);
        assertEquals(2, set.size());
        assertTrue(set.contains("x"));
        assertTrue(set.contains("y"));
    }

    @Test
    public void testAdd() {
        ConcurrentHashSet<String> set = ConcurrentHashSet.create();
        assertTrue(set.add("hello"));
        assertFalse(set.add("hello"));
        assertEquals(1, set.size());
    }

    @Test
    public void testRemove() {
        ConcurrentHashSet<String> set = ConcurrentHashSet.create();
        set.add("a");
        set.add("b");
        set.remove("a");
        assertEquals(1, set.size());
        assertFalse(set.contains("a"));
    }

    @Test
    public void testClear() {
        ConcurrentHashSet<String> set = ConcurrentHashSet.create();
        set.add("a");
        set.add("b");
        set.clear();
        assertTrue(set.isEmpty());
    }

    @Test
    public void testIterator() {
        ConcurrentHashSet<Integer> set = ConcurrentHashSet.create();
        set.add(1);
        set.add(2);
        set.add(3);
        Set<Integer> collected = new HashSet<>();
        for (Integer i : set) {
            collected.add(i);
        }
        assertEquals(3, collected.size());
    }

    @Test
    public void testForEach() {
        ConcurrentHashSet<String> set = ConcurrentHashSet.create();
        set.add("a");
        set.add("b");
        List<String> result = new ArrayList<>();
        set.forEach(result::add);
        assertEquals(2, result.size());
    }

    @Test
    public void testConcurrentAccess() throws InterruptedException {
        ConcurrentHashSet<Integer> set = ConcurrentHashSet.create();
        int threads = 10;
        int perThread = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);
        for (int t = 0; t < threads; t++) {
            final int offset = t * perThread;
            executor.submit(() -> {
                for (int i = 0; i < perThread; i++) {
                    set.add(offset + i);
                }
                latch.countDown();
            });
        }
        latch.await();
        executor.shutdown();
        assertEquals(threads * perThread, set.size());
    }

    @Test
    public void testToString() {
        ConcurrentHashSet<String> set = ConcurrentHashSet.create();
        set.add("test");
        assertNotNull(set.toString());
    }

    @Test
    public void testGetStore() {
        ConcurrentHashSet<String> set = ConcurrentHashSet.create();
        set.add("a");
        assertNotNull(set.getStore());
        assertTrue(set.getStore().containsKey("a"));
    }
}
