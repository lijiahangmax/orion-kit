package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MutableHashMapTest {

    @Test
    public void testCreate() {
        MutableHashMap<String, Object> map = MutableHashMap.create();
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testCreateFromMap() {
        Map<String, Object> source = new HashMap<>();
        source.put("int", 123);
        source.put("str", "hello");
        MutableHashMap<String, Object> map = MutableHashMap.create(source);
        assertEquals(2, map.size());
    }

    @Test
    public void testGetInteger() {
        MutableHashMap<String, Object> map = MutableHashMap.create();
        map.put("num", 42);
        assertEquals(Integer.valueOf(42), map.getInteger("num"));
        assertNull(map.getInteger("missing"));
        assertEquals(Integer.valueOf(99), map.getInteger("missing", 99));
    }

    @Test
    public void testGetIntValue() {
        MutableHashMap<String, Object> map = MutableHashMap.create();
        map.put("num", 42);
        assertEquals(42, map.getIntValue("num"));
        assertEquals(0, map.getIntValue("missing"));
        assertEquals(10, map.getIntValue("missing", 10));
    }

    @Test
    public void testGetLong() {
        MutableHashMap<String, Object> map = MutableHashMap.create();
        map.put("val", 100L);
        assertEquals(Long.valueOf(100L), map.getLong("val"));
    }

    @Test
    public void testGetString() {
        MutableHashMap<String, Object> map = MutableHashMap.create();
        map.put("name", "test");
        assertEquals("test", map.getString("name"));
        assertNull(map.getString("missing"));
        assertEquals("def", map.getString("missing", "def"));
    }

    @Test
    public void testGetBoolean() {
        MutableHashMap<String, Object> map = MutableHashMap.create();
        map.put("flag", true);
        assertEquals(Boolean.TRUE, map.getBoolean("flag"));
        assertFalse(map.getBooleanValue("missing"));
    }

    @Test
    public void testGetDouble() {
        MutableHashMap<String, Object> map = MutableHashMap.create();
        map.put("d", 3.14);
        assertEquals(Double.valueOf(3.14), map.getDouble("d"));
    }

    @Test
    public void testGetObject() {
        MutableHashMap<String, Object> map = MutableHashMap.create();
        map.put("obj", "hello");
        String val = map.getObject("obj");
        assertEquals("hello", val);
        String def = map.getObject("missing", "default");
        assertEquals("default", def);
    }

    @Test
    public void testGetWithDefault() {
        MutableHashMap<String, Object> map = MutableHashMap.create();
        map.put("k", "v");
        assertEquals("v", map.get("k", "def"));
        assertEquals("def", map.get("missing", "def"));
    }
}
