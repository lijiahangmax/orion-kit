package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class MutableTreeMapTest {

    @Test
    public void testCreate() {
        MutableTreeMap<String, Object> map = MutableTreeMap.create();
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testCreateFromMap() {
        Map<String, Object> source = new TreeMap<>();
        source.put("a", 1);
        source.put("b", 2);
        MutableTreeMap<String, Object> map = MutableTreeMap.create(source);
        assertEquals(2, map.size());
    }

    @Test
    public void testGetInteger() {
        MutableTreeMap<String, Object> map = MutableTreeMap.create();
        map.put("num", 42);
        assertEquals(Integer.valueOf(42), map.getInteger("num"));
    }

    @Test
    public void testGetString() {
        MutableTreeMap<String, Object> map = MutableTreeMap.create();
        map.put("k", "hello");
        assertEquals("hello", map.getString("k"));
    }

    @Test
    public void testOrdering() {
        MutableTreeMap<String, Object> map = MutableTreeMap.create();
        map.put("c", 3);
        map.put("a", 1);
        map.put("b", 2);
        assertEquals("a", map.firstKey());
        assertEquals("c", map.lastKey());
    }
}
