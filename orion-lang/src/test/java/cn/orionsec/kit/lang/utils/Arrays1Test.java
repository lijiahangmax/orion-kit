package cn.orionsec.kit.lang.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class Arrays1Test {

    @Test
    public void testNewBytes() {
        byte[] arr = Arrays1.newBytes(5);
        assertNotNull(arr);
        assertEquals(5, arr.length);
    }

    @Test
    public void testNewInts() {
        int[] arr = Arrays1.newInts(3);
        assertNotNull(arr);
        assertEquals(3, arr.length);
    }

    @Test
    public void testNewArrays() {
        String[] arr = Arrays1.newArrays(String.class, 4);
        assertNotNull(arr);
        assertEquals(4, arr.length);
    }

    @Test
    public void testIsArray() {
        assertTrue(Arrays1.isArray(new int[]{1, 2}));
        assertTrue(Arrays1.isArray(new String[]{"a"}));
        assertFalse(Arrays1.isArray("not array"));
        assertFalse(Arrays1.isArray(null));
    }

    @Test
    public void testIsBaseArray() {
        assertTrue(Arrays1.isBaseArray(new int[]{1}));
        assertTrue(Arrays1.isBaseArray(new byte[]{1}));
        assertFalse(Arrays1.isBaseArray(null));
    }

    @Test
    public void testGetAndSet() {
        int[] arr = {1, 2, 3};
        assertEquals(1, Arrays1.get(arr, 0));
        assertEquals(3, Arrays1.get(arr, 2));
        Arrays1.set(arr, 0, 10);
        assertEquals(10, Arrays1.get(arr, 0));
    }

    @Test
    public void testGetGeneric() {
        String[] arr = {"a", "b", "c"};
        assertEquals("a", Arrays1.get(arr, 0));
        assertEquals("c", Arrays1.get(arr, 2));
    }

    @Test
    public void testGetIfPresent() {
        String[] arr = {"a", "b"};
        assertEquals("a", Arrays1.getIfPresent(arr, 0));
        assertNull(Arrays1.getIfPresent(arr, 10));
        assertNull(Arrays1.getIfPresent(null, 0));
    }

    @Test
    public void testSets() {
        int[] arr = new int[3];
        Arrays1.sets(arr, 0, 99);
        assertEquals(99, arr[0]);
    }

    @Test(expected = Exception.class)
    public void testGetIndexOutOfBounds() {
        int[] arr = {1, 2};
        Arrays1.get(arr, 5);
    }
}
