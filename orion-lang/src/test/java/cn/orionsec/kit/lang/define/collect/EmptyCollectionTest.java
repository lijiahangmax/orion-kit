package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.*;

public class EmptyCollectionTest {

    // --- EmptyList tests ---

    @Test
    public void testEmptyListSize() {
        EmptyList<String> list = new EmptyList<>();
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    public void testEmptyListContains() {
        EmptyList<String> list = new EmptyList<>();
        assertFalse(list.contains("anything"));
    }

    @Test
    public void testEmptyListContainsAll() {
        EmptyList<String> list = new EmptyList<>();
        assertTrue(list.containsAll(Collections.emptyList()));
        assertFalse(list.containsAll(Arrays.asList("a")));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testEmptyListGet() {
        EmptyList<String> list = new EmptyList<>();
        list.get(0);
    }

    @Test
    public void testEmptyListIterator() {
        EmptyList<String> list = new EmptyList<>();
        assertFalse(list.iterator().hasNext());
    }

    @Test
    public void testEmptyListToArray() {
        EmptyList<String> list = new EmptyList<>();
        assertEquals(0, list.toArray().length);
    }

    @Test
    public void testEmptyListEquals() {
        EmptyList<String> list = new EmptyList<>();
        assertTrue(list.equals(new ArrayList<>()));
    }

    @Test
    public void testEmptyListHashCode() {
        EmptyList<String> list = new EmptyList<>();
        assertEquals(1, list.hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEmptyListRemoveIf() {
        EmptyList<String> list = new EmptyList<>();
        list.removeIf(s -> true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEmptyListReplaceAll() {
        EmptyList<String> list = new EmptyList<>();
        list.replaceAll(s -> s);
    }

    // --- EmptySet tests ---

    @Test
    public void testEmptySetSize() {
        EmptySet<String> set = new EmptySet<>();
        assertEquals(0, set.size());
        assertTrue(set.isEmpty());
    }

    @Test
    public void testEmptySetContains() {
        EmptySet<String> set = new EmptySet<>();
        assertFalse(set.contains("x"));
    }

    @Test
    public void testEmptySetIterator() {
        EmptySet<String> set = new EmptySet<>();
        assertFalse(set.iterator().hasNext());
    }

    @Test
    public void testEmptySetToArray() {
        EmptySet<String> set = new EmptySet<>();
        assertEquals(0, set.toArray().length);
    }

    @Test
    public void testEmptySetRemoveIf() {
        EmptySet<String> set = new EmptySet<>();
        assertFalse(set.removeIf(s -> true));
    }

    // --- EmptyMap tests ---

    @Test
    public void testEmptyMapSize() {
        EmptyMap<String, Integer> map = new EmptyMap<>();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void testEmptyMapContains() {
        EmptyMap<String, Integer> map = new EmptyMap<>();
        assertFalse(map.containsKey("k"));
        assertFalse(map.containsValue(1));
    }

    @Test
    public void testEmptyMapGet() {
        EmptyMap<String, Integer> map = new EmptyMap<>();
        assertNull(map.get("k"));
        assertEquals(Integer.valueOf(42), map.getOrDefault("k", 42));
    }

    @Test
    public void testEmptyMapEquals() {
        EmptyMap<String, Integer> map = new EmptyMap<>();
        assertTrue(map.equals(new HashMap<>()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEmptyMapPutIfAbsent() {
        EmptyMap<String, Integer> map = new EmptyMap<>();
        map.putIfAbsent("k", 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEmptyMapRemove() {
        EmptyMap<String, Integer> map = new EmptyMap<>();
        map.remove("k", 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEmptyMapReplace() {
        EmptyMap<String, Integer> map = new EmptyMap<>();
        map.replace("k", 1);
    }
}
