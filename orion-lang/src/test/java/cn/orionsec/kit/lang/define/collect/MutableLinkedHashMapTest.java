package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class MutableLinkedHashMapTest {

    @Test
    public void testCreate() {
        MutableLinkedHashMap<String, Object> map = MutableLinkedHashMap.create();
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testCreateFromMap() {
        Map<String, Object> source = Map.of("k", "v");
        MutableLinkedHashMap<String, Object> map = MutableLinkedHashMap.create(source);
        assertEquals(1, map.size());
    }

    @Test
    public void testConstructors() {
        MutableLinkedHashMap<String, Object> m1 = new MutableLinkedHashMap<>(16);
        MutableLinkedHashMap<String, Object> m2 = new MutableLinkedHashMap<>(16, 0.75f);
        MutableLinkedHashMap<String, Object> m3 = new MutableLinkedHashMap<>(16, 0.75f, true);
        assertNotNull(m1);
        assertNotNull(m2);
        assertNotNull(m3);
    }

    @Test
    public void testGetString() {
        MutableLinkedHashMap<String, Object> map = MutableLinkedHashMap.create();
        map.put("k", "hello");
        assertEquals("hello", map.getString("k"));
    }

    @Test
    public void testGetInteger() {
        MutableLinkedHashMap<String, Object> map = MutableLinkedHashMap.create();
        map.put("n", 100);
        assertEquals(Integer.valueOf(100), map.getInteger("n"));
    }
}
