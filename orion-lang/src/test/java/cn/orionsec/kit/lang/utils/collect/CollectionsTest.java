package cn.orionsec.kit.lang.utils.collect;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Collections 工具类测试
 */
public class CollectionsTest {

    @Test
    public void testSize() {
        assertEquals(0, Collections.size(null));
        assertEquals(3, Collections.size(Arrays.asList(1, 2, 3)));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(Collections.isEmpty(null));
        assertTrue(Collections.isEmpty(new ArrayList<>()));
        assertFalse(Collections.isEmpty(Arrays.asList(1)));
    }

    @Test
    public void testIsNotEmpty() {
        assertFalse(Collections.isNotEmpty(null));
        assertTrue(Collections.isNotEmpty(Arrays.asList(1)));
    }

    @Test
    public void testIsAllEmpty() {
        assertTrue(Collections.isAllEmpty(null, new ArrayList<>()));
        assertFalse(Collections.isAllEmpty(Arrays.asList(1), new ArrayList<>()));
    }

    @Test
    public void testIsNoneEmpty() {
        assertFalse(Collections.isNoneEmpty(null));
        assertTrue(Collections.isNoneEmpty(Arrays.asList(1), Arrays.asList(2)));
        assertFalse(Collections.isNoneEmpty(Arrays.asList(1), new ArrayList<>()));
    }

    @Test
    public void testJoin() {
        List<String> list = Arrays.asList("a", "b", "c");
        assertEquals("a,b,c", Collections.join(list));
        assertEquals("a-b-c", Collections.join(list, "-"));
        assertEquals("[a|b|c]", Collections.join(list, "|", "[", "]"));
    }

    @Test
    public void testFirst() {
        List<Integer> list = Arrays.asList(10, 20, 30);
        assertEquals(Integer.valueOf(10), Collections.first(list));
        assertNull(Collections.first(null));
        assertNull(Collections.first(new ArrayList<>()));
    }

    @Test
    public void testLast() {
        List<Integer> list = Arrays.asList(10, 20, 30);
        assertEquals(Integer.valueOf(30), Collections.last(list));
        assertNull(Collections.last(null));
    }

    @Test
    public void testInter() {
        List<Integer> c1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> c2 = Arrays.asList(3, 4, 5, 6);
        Set<Integer> inter = Collections.inter(c1, c2);
        assertEquals(2, inter.size());
        assertTrue(inter.contains(3));
        assertTrue(inter.contains(4));
    }

    @Test
    public void testUnion() {
        List<Integer> c1 = Arrays.asList(1, 2, 3);
        List<Integer> c2 = Arrays.asList(3, 4, 5);
        Set<Integer> union = Collections.union(c1, c2);
        assertEquals(5, union.size());
    }

    @Test
    public void testDiff() {
        List<Integer> c1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> c2 = Arrays.asList(3, 4, 5, 6);
        Set<Integer> diff = Collections.diff(c1, c2);
        assertEquals(2, diff.size());
        assertTrue(diff.contains(1));
        assertTrue(diff.contains(2));
    }

    @Test
    public void testCompact() {
        List<String> list = new ArrayList<>(Arrays.asList("a", null, "b", null, "c"));
        Collections.compact(list);
        assertEquals(3, list.size());
        assertFalse(list.contains(null));
    }

    @Test
    public void testGetFirstDuplicateItem() {
        List<Integer> list = Arrays.asList(1, 2, 3, 2, 4);
        assertEquals(Integer.valueOf(2), Collections.getFirstDuplicateItem(list));

        List<Integer> noDup = Arrays.asList(1, 2, 3);
        assertNull(Collections.getFirstDuplicateItem(noDup));
    }
}
