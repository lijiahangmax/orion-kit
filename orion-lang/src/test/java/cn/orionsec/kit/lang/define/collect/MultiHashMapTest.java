package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MultiHashMapTest {

    @Test
    public void testCreate() {
        MultiHashMap<String, String, Integer> map = MultiHashMap.create();
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testPutAndGet() {
        MultiHashMap<String, String, Integer> map = MultiHashMap.create();
        map.put("group1", "key1", 100);
        map.put("group1", "key2", 200);
        map.put("group2", "key1", 300);
        assertEquals(Integer.valueOf(100), map.get("group1", "key1"));
        assertEquals(Integer.valueOf(200), map.get("group1", "key2"));
        assertEquals(Integer.valueOf(300), map.get("group2", "key1"));
    }

    @Test
    public void testGetNonExistent() {
        MultiHashMap<String, String, Integer> map = MultiHashMap.create();
        assertNull(map.get("none", "none"));
    }

    @Test
    public void testGetOrDefault() {
        MultiHashMap<String, String, Integer> map = MultiHashMap.create();
        map.put("g", "k", 1);
        assertEquals(Integer.valueOf(1), map.getOrDefault("g", "k", 99));
        assertEquals(Integer.valueOf(99), map.getOrDefault("g", "missing", 99));
    }

    @Test
    public void testRemoveElement() {
        MultiHashMap<String, String, Integer> map = MultiHashMap.create();
        map.put("g", "k1", 1);
        map.put("g", "k2", 2);
        assertEquals(Integer.valueOf(1), map.removeElement("g", "k1"));
        assertNull(map.get("g", "k1"));
        assertEquals(Integer.valueOf(2), map.get("g", "k2"));
    }

    @Test
    public void testSize() {
        MultiHashMap<String, String, Integer> map = MultiHashMap.create();
        map.put("g", "k1", 1);
        map.put("g", "k2", 2);
        assertEquals(2, map.size("g"));
        assertEquals(0, map.size("nonexistent"));
    }

    @Test
    public void testIsEmpty() {
        MultiHashMap<String, String, Integer> map = MultiHashMap.create();
        assertTrue(map.isEmpty("g"));
        map.put("g", "k", 1);
        assertFalse(map.isEmpty("g"));
    }

    @Test
    public void testClear() {
        MultiHashMap<String, String, Integer> map = MultiHashMap.create();
        map.put("g", "k1", 1);
        map.put("g", "k2", 2);
        map.clear("g");
        assertTrue(map.isEmpty("g"));
    }

    @Test
    public void testContainsKey() {
        MultiHashMap<String, String, Integer> map = MultiHashMap.create();
        map.put("g", "k", 1);
        assertTrue(map.containsKey("g", "k"));
        assertFalse(map.containsKey("g", "missing"));
    }

    @Test
    public void testPutAll() {
        MultiHashMap<String, String, Integer> map = MultiHashMap.create();
        Map<String, Integer> sub = new HashMap<>();
        sub.put("k1", 1);
        sub.put("k2", 2);
        map.putAll("g", sub);
        assertEquals(2, map.size("g"));
    }

    @Test
    public void testValues() {
        MultiHashMap<String, String, Integer> map = MultiHashMap.create();
        map.put("g", "k1", 1);
        map.put("g", "k2", 2);
        assertEquals(2, map.values("g").size());
    }

    @Test
    public void testForEach() {
        MultiHashMap<String, String, Integer> map = MultiHashMap.create();
        map.put("g", "k1", 1);
        map.put("g", "k2", 2);
        int[] sum = {0};
        map.forEach("g", (k, v) -> sum[0] += v);
        assertEquals(3, sum[0]);
    }

    @Test
    public void testComputeSpace() {
        MultiHashMap<String, String, Integer> map = new MultiHashMap<>(8, 4);
        map.valueInitialCapacity(8);
        HashMap<String, Integer> space = map.computeSpace("g");
        assertNotNull(space);
    }
}
