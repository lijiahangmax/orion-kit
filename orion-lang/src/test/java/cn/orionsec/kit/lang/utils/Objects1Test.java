package cn.orionsec.kit.lang.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class Objects1Test {

    @Test
    public void testDef() {
        assertEquals("hello", Objects1.def("hello", "default"));
        assertEquals("default", Objects1.def(null, "default"));
    }

    @Test
    public void testDefWithSupplier() {
        assertEquals("hello", Objects1.def("hello", () -> "default"));
        assertEquals("default", Objects1.def(null, () -> "default"));
    }

    @Test
    public void testEq() {
        assertTrue(Objects1.eq("abc", "abc"));
        assertTrue(Objects1.eq(null, null));
        assertFalse(Objects1.eq("abc", null));
        assertFalse(Objects1.eq(null, "abc"));
        assertFalse(Objects1.eq("abc", "def"));
    }

    @Test
    public void testFirstNotNull() {
        assertEquals("a", Objects1.firstNotNull(null, "a", "b"));
        assertEquals("a", Objects1.firstNotNull("a", "b"));
        assertNull(Objects1.firstNotNull((Object[]) null));
        assertNull(Objects1.firstNotNull(null, null));
    }

    @Test
    public void testIsNull() {
        assertTrue(Objects1.isNull(null));
        assertFalse(Objects1.isNull("abc"));
    }

    @Test
    public void testIsNotNull() {
        assertFalse(Objects1.isNotNull(null));
        assertTrue(Objects1.isNotNull("abc"));
    }

    @Test
    public void testIsAllNull() {
        assertTrue(Objects1.isAllNull(null, null));
        assertTrue(Objects1.isAllNull((Object[]) null));
        assertFalse(Objects1.isAllNull(null, "a"));
    }

    @Test
    public void testIsNoneNull() {
        assertTrue(Objects1.isNoneNull("a", "b"));
        assertFalse(Objects1.isNoneNull("a", null));
        assertFalse(Objects1.isNoneNull((Object[]) null));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(Objects1.isEmpty(null));
        assertTrue(Objects1.isEmpty(""));
        assertTrue(Objects1.isEmpty(new int[0]));
        assertTrue(Objects1.isEmpty(new ArrayList<>()));
        assertTrue(Objects1.isEmpty(new HashMap<>()));
        assertFalse(Objects1.isEmpty("abc"));
    }

    @Test
    public void testIsNotEmpty() {
        assertTrue(Objects1.isNotEmpty("abc"));
        assertFalse(Objects1.isNotEmpty(null));
    }

    @Test
    public void testToString() {
        assertEquals("", Objects1.toString(null));
        assertEquals("hello", Objects1.toString("hello"));
        assertEquals("[1, 2, 3]", Objects1.toString(new int[]{1, 2, 3}));
    }

    @Test
    public void testIsVoids() {
        assertTrue(Objects1.isVoids(Void.TYPE));
        assertTrue(Objects1.isVoids(Void.class));
        assertFalse(Objects1.isVoids(String.class));
    }

    @Test
    public void testSerializeDeserialize() {
        String original = "test string";
        byte[] bytes = Objects1.serialize(original);
        assertNotNull(bytes);
        String result = Objects1.deserialize(bytes);
        assertEquals(original, result);
    }

    @Test
    public void testSerializeNull() {
        assertNull(Objects1.serialize(null));
        assertNull(Objects1.deserialize(null));
    }
}
