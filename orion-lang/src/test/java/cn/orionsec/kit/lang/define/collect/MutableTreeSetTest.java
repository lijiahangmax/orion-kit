package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class MutableTreeSetTest {

    @Test
    public void testCreate() {
        MutableTreeSet<Object> set = MutableTreeSet.create();
        assertNotNull(set);
        assertTrue(set.isEmpty());
    }

    @Test
    public void testCreateFromCollection() {
        MutableTreeSet<String> set = MutableTreeSet.create(Arrays.asList("b", "a", "c"));
        assertEquals(3, set.size());
        // TreeSet sorts elements
        assertEquals("a", set.get(0));
    }

    @Test
    public void testCreateWithComparator() {
        MutableTreeSet<String> set = MutableTreeSet.create(String.CASE_INSENSITIVE_ORDER);
        set.add("B");
        set.add("a");
        set.add("C");
        assertEquals(3, set.size());
    }

    @Test
    public void testGetByIndex() {
        MutableTreeSet<Integer> set = new MutableTreeSet<>();
        set.add(3);
        set.add(1);
        set.add(2);
        // sorted: 1, 2, 3
        assertEquals(Integer.valueOf(1), set.get(0));
        assertEquals(Integer.valueOf(2), set.get(1));
        assertEquals(Integer.valueOf(3), set.get(2));
    }

    @Test
    public void testGetBeyondSize() {
        MutableTreeSet<String> set = new MutableTreeSet<>();
        set.add("a");
        assertNull(set.get(5));
    }
}
