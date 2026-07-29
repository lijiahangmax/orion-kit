package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class MutableLinkedListTest {

    @Test
    public void testCreate() {
        MutableLinkedList<Object> list = MutableLinkedList.create();
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testCreateFromCollection() {
        MutableLinkedList<Object> list = MutableLinkedList.create(Arrays.asList(1, "two", 3.0));
        assertEquals(3, list.size());
    }

    @Test
    public void testGetInteger() {
        MutableLinkedList<Object> list = MutableLinkedList.create();
        list.add(100);
        list.add(null);
        assertEquals(Integer.valueOf(100), list.getInteger(0));
        assertNull(list.getInteger(1));
    }

    @Test
    public void testGetString() {
        MutableLinkedList<Object> list = MutableLinkedList.create();
        list.add("hello");
        assertEquals("hello", list.getString(0));
    }

    @Test
    public void testGetWithDefault() {
        MutableLinkedList<Object> list = MutableLinkedList.create();
        list.add("val");
        list.add(null);
        assertEquals("val", list.get(0, "def"));
        assertEquals("def", list.get(1, "def"));
    }
}
