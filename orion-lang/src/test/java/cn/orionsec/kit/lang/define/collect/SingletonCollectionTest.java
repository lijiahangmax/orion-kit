package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SingletonCollectionTest {

    // --- SingletonList tests ---

    @Test
    public void testSingletonListSize() {
        SingletonList<String> list = SingletonList.create("hello");
        assertEquals(1, list.size());
    }

    @Test
    public void testSingletonListContains() {
        SingletonList<String> list = SingletonList.create("hello");
        assertTrue(list.contains("hello"));
        assertFalse(list.contains("world"));
    }

    @Test
    public void testSingletonListGet() {
        SingletonList<Integer> list = SingletonList.create(42);
        assertEquals(Integer.valueOf(42), list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSingletonListGetInvalid() {
        SingletonList<Integer> list = SingletonList.create(42);
        list.get(1);
    }

    @Test
    public void testSingletonListIterator() {
        SingletonList<String> list = SingletonList.create("test");
        Iterator<String> it = list.iterator();
        assertTrue(it.hasNext());
        assertEquals("test", it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void testSingletonListForEach() {
        SingletonList<String> list = SingletonList.create("x");
        List<String> collected = new ArrayList<>();
        list.forEach(collected::add);
        assertEquals(1, collected.size());
        assertEquals("x", collected.get(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSingletonListRemoveIf() {
        SingletonList<String> list = SingletonList.create("x");
        list.removeIf(s -> true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSingletonListReplaceAll() {
        SingletonList<String> list = SingletonList.create("x");
        list.replaceAll(String::toUpperCase);
    }

    // --- SingletonSet tests ---

    @Test
    public void testSingletonSetSize() {
        SingletonSet<String> set = SingletonSet.create("hello");
        assertEquals(1, set.size());
    }

    @Test
    public void testSingletonSetContains() {
        SingletonSet<String> set = SingletonSet.create("hello");
        assertTrue(set.contains("hello"));
        assertFalse(set.contains("world"));
    }

    @Test
    public void testSingletonSetGet() {
        SingletonSet<Integer> set = SingletonSet.create(42);
        assertEquals(Integer.valueOf(42), set.get());
    }

    @Test
    public void testSingletonSetIterator() {
        SingletonSet<String> set = SingletonSet.create("test");
        Iterator<String> it = set.iterator();
        assertTrue(it.hasNext());
        assertEquals("test", it.next());
        assertFalse(it.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSingletonSetRemoveIf() {
        SingletonSet<String> set = SingletonSet.create("x");
        set.removeIf(s -> true);
    }

    // --- SingletonMap tests ---

    @Test
    public void testSingletonMapSize() {
        SingletonMap<String, Integer> map = SingletonMap.create("k", 1);
        assertEquals(1, map.size());
        assertFalse(map.isEmpty());
    }

    @Test
    public void testSingletonMapContains() {
        SingletonMap<String, Integer> map = SingletonMap.create("k", 1);
        assertTrue(map.containsKey("k"));
        assertFalse(map.containsKey("other"));
        assertTrue(map.containsValue(1));
        assertFalse(map.containsValue(2));
    }

    @Test
    public void testSingletonMapGet() {
        SingletonMap<String, Integer> map = SingletonMap.create("k", 42);
        assertEquals(Integer.valueOf(42), map.get("k"));
        assertNull(map.get("other"));
    }

    @Test
    public void testSingletonMapGetOrDefault() {
        SingletonMap<String, Integer> map = SingletonMap.create("k", 42);
        assertEquals(Integer.valueOf(42), map.getOrDefault("k", 0));
        assertEquals(Integer.valueOf(0), map.getOrDefault("other", 0));
    }

    @Test
    public void testSingletonMapForEach() {
        SingletonMap<String, Integer> map = SingletonMap.create("k", 1);
        Map<String, Integer> collected = new HashMap<>();
        map.forEach(collected::put);
        assertEquals(1, collected.size());
        assertEquals(Integer.valueOf(1), collected.get("k"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSingletonMapPutIfAbsent() {
        SingletonMap<String, Integer> map = SingletonMap.create("k", 1);
        map.putIfAbsent("k2", 2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSingletonMapRemove() {
        SingletonMap<String, Integer> map = SingletonMap.create("k", 1);
        map.remove("k", 1);
    }

    @Test
    public void testSingletonMapKeySet() {
        SingletonMap<String, Integer> map = SingletonMap.create("k", 1);
        assertNotNull(map.keySet());
        assertEquals(1, map.keySet().size());
    }

    @Test
    public void testSingletonMapValues() {
        SingletonMap<String, Integer> map = SingletonMap.create("k", 1);
        assertNotNull(map.values());
        assertEquals(1, map.values().size());
    }

    @Test
    public void testSingletonMapEntrySet() {
        SingletonMap<String, Integer> map = SingletonMap.create("k", 1);
        assertNotNull(map.entrySet());
        assertEquals(1, map.entrySet().size());
    }
}
