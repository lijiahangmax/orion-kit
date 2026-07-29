package cn.orionsec.kit.lang.utils.collect;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Lists 工具类测试
 */
public class ListsTest {

    @Test
    public void testNewList() {
        List<String> list = Lists.newList();
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testOf() {
        List<String> list = Lists.of("a", "b", "c");
        assertEquals(3, list.size());
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
        assertEquals("c", list.get(2));
    }

    @Test
    public void testDef() {
        List<String> result = Lists.def(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());

        List<String> existing = Lists.of("x");
        assertSame(existing, Lists.def(existing));
    }

    @Test
    public void testReverse() {
        List<Integer> list = Lists.of(1, 2, 3, 4, 5);
        Lists.reverse(list);
        assertEquals(Arrays.asList(5, 4, 3, 2, 1), list);
    }

    @Test
    public void testIndexOf() {
        List<String> list = Lists.of("a", "b", "c", "b");
        assertEquals(1, Lists.indexOf(list, "b"));
        assertEquals(-1, Lists.indexOf(list, "z"));
        assertEquals(-1, Lists.indexOf(null, "a"));
    }

    @Test
    public void testLastIndexOf() {
        List<String> list = Lists.of("a", "b", "c", "b");
        assertEquals(3, Lists.lastIndexOf(list, "b"));
        assertEquals(-1, Lists.lastIndexOf(null, "a"));
    }

    @Test
    public void testEq() {
        List<Integer> list1 = Lists.of(1, 2, 3);
        List<Integer> list2 = Lists.of(1, 2, 3);
        List<Integer> list3 = Lists.of(1, 2, 4);
        assertTrue(Lists.eq(list1, list2));
        assertFalse(Lists.eq(list1, list3));
        assertTrue(Lists.eq(null, null));
        assertFalse(Lists.eq(list1, null));
    }

    @Test
    public void testMerge() {
        List<Integer> source = Lists.of(1, 2);
        List<Integer> add1 = Lists.of(3, 4);
        List<Integer> add2 = Lists.of(5);
        Lists.merge(source, add1, add2);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), source);
    }

    @Test
    public void testGetIfPresent() {
        List<String> list = Lists.of("a", "b", "c");
        assertEquals("a", Lists.getIfPresent(list, 0));
        assertEquals("c", Lists.getIfPresent(list, 2));
        assertNull(Lists.getIfPresent(list, 10));
        assertNull(Lists.getIfPresent(null, 0));
    }

    @Test
    public void testToInts() {
        List<Integer> list = Lists.of(1, 2, 3);
        int[] arr = Lists.toInts(list);
        assertArrayEquals(new int[]{1, 2, 3}, arr);
    }
}
