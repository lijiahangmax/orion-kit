package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class MutableVectorTest {

    @Test
    public void testCreate() {
        MutableVector<Object> vec = MutableVector.create();
        assertNotNull(vec);
        assertTrue(vec.isEmpty());
    }

    @Test
    public void testCreateFromCollection() {
        MutableVector<Object> vec = MutableVector.create(Arrays.asList(1, "two", 3.0));
        assertEquals(3, vec.size());
    }

    @Test
    public void testConstructors() {
        MutableVector<Object> v1 = new MutableVector<>(10);
        MutableVector<Object> v2 = new MutableVector<>(10, 5);
        assertNotNull(v1);
        assertNotNull(v2);
    }

    @Test
    public void testGetInteger() {
        MutableVector<Object> vec = MutableVector.create();
        vec.add(42);
        vec.add(null);
        assertEquals(Integer.valueOf(42), vec.getInteger(0));
        assertNull(vec.getInteger(1));
    }

    @Test
    public void testGetString() {
        MutableVector<Object> vec = MutableVector.create();
        vec.add("hello");
        assertEquals("hello", vec.getString(0));
    }

    @Test
    public void testGetWithDefault() {
        MutableVector<Object> vec = MutableVector.create();
        vec.add("val");
        vec.add(null);
        assertEquals("val", vec.get(0, "def"));
        assertEquals("def", vec.get(1, "def"));
    }
}
