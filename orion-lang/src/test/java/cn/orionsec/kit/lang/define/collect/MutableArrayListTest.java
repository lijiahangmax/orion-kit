package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class MutableArrayListTest {

    @Test
    public void testCreate() {
        MutableArrayList<Object> list = MutableArrayList.create();
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testCreateFromCollection() {
        MutableArrayList<Object> list = MutableArrayList.create(Arrays.asList(1, "hello", 3.14));
        assertEquals(3, list.size());
    }

    @Test
    public void testGetInteger() {
        MutableArrayList<Object> list = MutableArrayList.create();
        list.add(42);
        list.add("hello");
        list.add(null);
        assertEquals(Integer.valueOf(42), list.getInteger(0));
        assertNull(list.getInteger(2));
        assertEquals(Integer.valueOf(99), list.getInteger(2, 99));
    }

    @Test
    public void testGetString() {
        MutableArrayList<Object> list = MutableArrayList.create();
        list.add("test");
        list.add(null);
        assertEquals("test", list.getString(0));
        assertNull(list.getString(1));
        assertEquals("def", list.getString(1, "def"));
    }

    @Test
    public void testGetBoolean() {
        MutableArrayList<Object> list = MutableArrayList.create();
        list.add(true);
        list.add(null);
        assertEquals(Boolean.TRUE, list.getBoolean(0));
        assertFalse(list.getBooleanValue(1));
    }

    @Test
    public void testGetObject() {
        MutableArrayList<Object> list = MutableArrayList.create();
        list.add("value");
        String val = list.getObject(0);
        assertEquals("value", val);
    }

    @Test
    public void testGetWithDefault() {
        MutableArrayList<Object> list = MutableArrayList.create();
        list.add("x");
        list.add(null);
        assertEquals("x", list.get(0, "def"));
        assertEquals("def", list.get(1, "def"));
    }
}
