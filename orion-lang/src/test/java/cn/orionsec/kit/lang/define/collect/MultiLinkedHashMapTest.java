package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import static org.junit.Assert.*;

public class MultiLinkedHashMapTest {

    @Test
    public void testCreate() {
        MultiLinkedHashMap<String, String, Integer> map = MultiLinkedHashMap.create();
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testCreateWithParams() {
        MultiLinkedHashMap<String, String, Integer> map = MultiLinkedHashMap.create(16, 0.75f, false);
        assertNotNull(map);
    }

    @Test
    public void testPutAndGet() {
        MultiLinkedHashMap<String, String, Integer> map = MultiLinkedHashMap.create();
        map.put("g1", "k1", 10);
        map.put("g1", "k2", 20);
        assertEquals(Integer.valueOf(10), map.get("g1", "k1"));
        assertEquals(Integer.valueOf(20), map.get("g1", "k2"));
    }

    @Test
    public void testValueCapacity() {
        MultiLinkedHashMap<String, String, Integer> map = MultiLinkedHashMap.create();
        map.valueCapacity(32);
        map.valueCapacity(32, 0.5f);
        map.valueCapacity(32, 0.5f, true);
        map.put("g", "k", 1);
        assertEquals(Integer.valueOf(1), map.get("g", "k"));
    }

    @Test
    public void testRemoveElement() {
        MultiLinkedHashMap<String, String, Integer> map = MultiLinkedHashMap.create();
        map.put("g", "k1", 1);
        map.put("g", "k2", 2);
        map.removeElement("g", "k1");
        assertNull(map.get("g", "k1"));
    }

    @Test
    public void testComputeSpace() {
        MultiLinkedHashMap<String, String, Integer> map = new MultiLinkedHashMap<>(8);
        assertNotNull(map.computeSpace("g"));
    }
}
