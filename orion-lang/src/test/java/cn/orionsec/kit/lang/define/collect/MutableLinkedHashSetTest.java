package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class MutableLinkedHashSetTest {

    @Test
    public void testCreate() {
        MutableLinkedHashSet<Object> set = MutableLinkedHashSet.create();
        assertNotNull(set);
        assertTrue(set.isEmpty());
    }

    @Test
    public void testCreateFromCollection() {
        MutableLinkedHashSet<Object> set = MutableLinkedHashSet.create(Arrays.asList("a", "b", "c"));
        assertEquals(3, set.size());
    }

    @Test
    public void testConstructors() {
        MutableLinkedHashSet<Object> s1 = new MutableLinkedHashSet<>(16);
        MutableLinkedHashSet<Object> s2 = new MutableLinkedHashSet<>(16, 0.75f);
        assertNotNull(s1);
        assertNotNull(s2);
    }

    @Test
    public void testGetByIndex() {
        MutableLinkedHashSet<Object> set = MutableLinkedHashSet.create();
        set.add("first");
        set.add("second");
        // LinkedHashSet preserves insertion order
        assertEquals("first", set.get(0));
        assertEquals("second", set.get(1));
    }

    @Test
    public void testGetBeyondSize() {
        MutableLinkedHashSet<Object> set = MutableLinkedHashSet.create();
        set.add("a");
        assertNull(set.get(5));
    }
}
