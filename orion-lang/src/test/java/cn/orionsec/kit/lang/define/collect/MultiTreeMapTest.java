package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

public class MultiTreeMapTest {

    @Test
    public void testCreate() {
        MultiTreeMap<String, String, Integer> map = MultiTreeMap.create();
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testCreateWithComparator() {
        MultiTreeMap<String, String, Integer> map = MultiTreeMap.create(Comparator.naturalOrder());
        map.put("b", "k1", 1);
        map.put("a", "k2", 2);
        assertEquals("a", map.firstKey());
    }

    @Test
    public void testCreateWithBothComparators() {
        MultiTreeMap<String, String, Integer> map = MultiTreeMap.create(
                Comparator.naturalOrder(), Comparator.naturalOrder());
        map.put("g", "b", 1);
        map.put("g", "a", 2);
        assertEquals("a", map.get("g").firstKey());
    }

    @Test
    public void testPutAndGet() {
        MultiTreeMap<String, String, Integer> map = MultiTreeMap.create();
        map.put("g", "k1", 10);
        map.put("g", "k2", 20);
        assertEquals(Integer.valueOf(10), map.get("g", "k1"));
    }

    @Test
    public void testValueComparator() {
        MultiTreeMap<String, String, Integer> map = MultiTreeMap.create();
        map.valueComparator(Comparator.reverseOrder());
        map.put("g", "a", 1);
        map.put("g", "b", 2);
        // after setting value comparator, new spaces will use it
        map.put("g2", "a", 1);
        map.put("g2", "b", 2);
        assertEquals("b", map.computeSpace("g2").firstKey());
    }
}
