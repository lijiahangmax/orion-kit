package cn.orionsec.kit.lang.utils.collect;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Maps 工具类测试
 */
public class MapsTest {

    @Test
    public void testNewMap() {
        Map<String, Integer> map = Maps.newMap();
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testOfKeyValues() {
        Map<String, Integer> map = Maps.of("a", 1, "b", 2);
        assertEquals(2, map.size());
        assertEquals(Integer.valueOf(1), map.get("a"));
        assertEquals(Integer.valueOf(2), map.get("b"));
    }

    @Test
    public void testSizeAndEmpty() {
        assertEquals(0, Maps.size(null));
        assertTrue(Maps.isEmpty(null));
        Map<String, String> map = Maps.newMap();
        map.put("k", "v");
        assertEquals(1, Maps.size(map));
        assertTrue(Maps.isNotEmpty(map));
    }

    @Test
    public void testDef() {
        Map<String, String> result = Maps.def(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());

        Map<String, String> existing = Maps.newMap();
        existing.put("x", "y");
        assertSame(existing, Maps.def(existing));
    }

    @Test
    public void testMerge() {
        Map<String, Integer> source = Maps.newMap();
        source.put("a", 1);
        Map<String, Integer> add = Maps.newMap();
        add.put("b", 2);
        Maps.merge(source, add);
        assertEquals(Integer.valueOf(1), source.get("a"));
        assertEquals(Integer.valueOf(2), source.get("b"));
    }

    @Test
    public void testIsAllEmpty() {
        assertTrue(Maps.isAllEmpty(null, Maps.newMap()));
        Map<String, String> m = Maps.newMap();
        m.put("k", "v");
        assertFalse(Maps.isAllEmpty(m));
    }

    @Test
    public void testIsNoneEmpty() {
        assertFalse(Maps.isNoneEmpty(null));
        Map<String, String> m = Maps.newMap();
        m.put("k", "v");
        assertTrue(Maps.isNoneEmpty(m));
    }

    @Test
    public void testGetAndSet() {
        Map<String, Integer> map = Maps.newMap();
        map.put("a", 1);
        assertEquals(Integer.valueOf(1), Maps.get(map, "a"));
        assertNull(Maps.get(map, "b"));
        assertNull(Maps.get(null, "a"));
    }

    @Test
    public void testSingleton() {
        Map<String, Integer> map = Maps.singleton("key", 100);
        assertNotNull(map);
        assertEquals(1, map.size());
        assertEquals(Integer.valueOf(100), map.get("key"));
    }

    @Test
    public void testFill() {
        Map<String, Integer> map = Maps.newMap();
        map.put("a", 1);
        map.put("b", 2);
        Maps.fill(map, 0);
        assertEquals(Integer.valueOf(0), map.get("a"));
        assertEquals(Integer.valueOf(0), map.get("b"));
    }
}
