package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

public class MutableConcurrentHashMapTest {

    @Test
    public void testCreate() {
        MutableConcurrentHashMap<String, Object> map = MutableConcurrentHashMap.create();
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testCreateFromMap() {
        Map<String, Object> source = new ConcurrentHashMap<>();
        source.put("k", "v");
        MutableConcurrentHashMap<String, Object> map = MutableConcurrentHashMap.create(source);
        assertEquals(1, map.size());
    }

    @Test
    public void testConstructors() {
        MutableConcurrentHashMap<String, Object> m1 = new MutableConcurrentHashMap<>(16);
        MutableConcurrentHashMap<String, Object> m2 = new MutableConcurrentHashMap<>(16, 0.75f);
        assertNotNull(m1);
        assertNotNull(m2);
    }

    @Test
    public void testGetInteger() {
        MutableConcurrentHashMap<String, Object> map = MutableConcurrentHashMap.create();
        map.put("num", 42);
        assertEquals(Integer.valueOf(42), map.getInteger("num"));
    }

    @Test
    public void testGetString() {
        MutableConcurrentHashMap<String, Object> map = MutableConcurrentHashMap.create();
        map.put("str", "hello");
        assertEquals("hello", map.getString("str"));
    }
}
