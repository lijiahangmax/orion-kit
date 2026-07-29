package cn.orionsec.kit.lang.utils.collect;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Sets 工具类测试
 */
public class SetsTest {

    @Test
    public void testNewSet() {
        Set<String> set = Sets.newSet();
        assertNotNull(set);
        assertTrue(set.isEmpty());
    }

    @Test
    public void testOf() {
        Set<String> set = Sets.of("a", "b", "c", "a");
        assertEquals(3, set.size());
        assertTrue(set.contains("a"));
        assertTrue(set.contains("b"));
        assertTrue(set.contains("c"));
    }

    @Test
    public void testDef() {
        Set<String> result = Sets.def(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());

        Set<String> existing = Sets.of("x");
        assertSame(existing, Sets.def(existing));
    }

    @Test
    public void testMerge() {
        Set<Integer> source = Sets.of(1, 2);
        Set<Integer> add = Sets.of(3, 4);
        Sets.merge(source, add);
        assertEquals(4, source.size());
        assertTrue(source.contains(3));
        assertTrue(source.contains(4));
    }

    @Test
    public void testGet() {
        Set<String> set = new LinkedHashSet<>(Arrays.asList("a", "b", "c"));
        assertEquals("a", Sets.get(set, 0));
        assertEquals("b", Sets.get(set, 1));
        assertEquals("c", Sets.get(set, 2));
        assertNull(Sets.get(set, 10));
    }

    @Test
    public void testSingleton() {
        Set<String> set = Sets.singleton("only");
        assertNotNull(set);
        assertEquals(1, set.size());
        assertTrue(set.contains("only"));
    }

    @Test
    public void testEmpty() {
        Set<String> set = Sets.empty();
        assertNotNull(set);
        assertTrue(set.isEmpty());
    }

    @Test
    public void testAs() {
        List<String> list = Arrays.asList("a", "b", "c");
        Set<String> set = Sets.as(list.iterator());
        assertEquals(3, set.size());
        assertTrue(set.contains("a"));
    }

    @Test
    public void testNewTreeSet() {
        TreeSet<Integer> set = Sets.newTreeSet();
        set.add(3);
        set.add(1);
        set.add(2);
        assertEquals(Integer.valueOf(1), set.first());
        assertEquals(Integer.valueOf(3), set.last());
    }

    @Test
    public void testRemoveToSize() {
        Set<String> set = new LinkedHashSet<>(Arrays.asList("a", "b", "c", "d", "e"));
        Sets.removeToSize(set, 3);
        assertEquals(3, set.size());
    }
}
