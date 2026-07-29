package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class MutableHashSetTest {

    @Test
    public void testCreate() {
        MutableHashSet<Object> set = MutableHashSet.create();
        assertNotNull(set);
        assertTrue(set.isEmpty());
    }

    @Test
    public void testCreateFromCollection() {
        MutableHashSet<Object> set = MutableHashSet.create(Arrays.asList(1, "two", 3.0));
        assertEquals(3, set.size());
    }

    @Test
    public void testConstructors() {
        MutableHashSet<Object> s1 = new MutableHashSet<>(16);
        MutableHashSet<Object> s2 = new MutableHashSet<>(16, 0.75f);
        assertNotNull(s1);
        assertNotNull(s2);
    }

    @Test
    public void testGetByIndex() {
        MutableHashSet<Object> set = MutableHashSet.create();
        set.add("a");
        set.add("b");
        // get(0) should return first element
        assertNotNull(set.get(0));
    }

    @Test
    public void testGetBeyondSize() {
        MutableHashSet<Object> set = MutableHashSet.create();
        set.add("a");
        assertNull(set.get(5));
    }
}
