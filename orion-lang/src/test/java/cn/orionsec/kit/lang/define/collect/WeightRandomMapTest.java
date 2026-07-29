package cn.orionsec.kit.lang.define.collect;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class WeightRandomMapTest {

    @Test
    public void testCreate() {
        WeightRandomMap<String> map = WeightRandomMap.create();
        assertNotNull(map);
    }

    @Test
    public void testPutAndNext() {
        WeightRandomMap<String> map = WeightRandomMap.create();
        map.put("a", 1.0);
        map.put("b", 2.0);
        map.put("c", 3.0);
        // should return one of the elements
        String result = map.next();
        assertNotNull(result);
        assertTrue(result.equals("a") || result.equals("b") || result.equals("c"));
    }

    @Test
    public void testNextSingleElement() {
        WeightRandomMap<String> map = WeightRandomMap.create();
        map.put("only", 1.0);
        assertEquals("only", map.next());
    }

    @Test
    public void testNextEmpty() {
        WeightRandomMap<String> map = WeightRandomMap.create();
        assertNull(map.next());
    }

    @Test
    public void testDistribution() {
        WeightRandomMap<String> map = WeightRandomMap.create();
        map.put("high", 100.0);
        map.put("low", 1.0);
        // run multiple times to check that high weight is selected more
        int highCount = 0;
        int total = 1000;
        for (int i = 0; i < total; i++) {
            if ("high".equals(map.next())) {
                highCount++;
            }
        }
        // "high" should appear significantly more than "low"
        assertTrue(highCount > total / 2);
    }

    @Test
    public void testZeroWeight() {
        WeightRandomMap<String> map = WeightRandomMap.create();
        map.put("zero", 0.0);
        map.put("positive", 5.0);
        // zero weight should not be added, so only "positive" is available
        for (int i = 0; i < 100; i++) {
            assertEquals("positive", map.next());
        }
    }

    @Test
    public void testWeightObject() {
        WeightRandomMap.WeightObject<String> w1 = new WeightRandomMap.WeightObject<>("a", 1.0);
        WeightRandomMap.WeightObject<String> w2 = new WeightRandomMap.WeightObject<>("a", 1.0);
        assertEquals(w1, w2);
        assertEquals(w1.hashCode(), w2.hashCode());
        assertEquals("a", w1.getObject());
        assertEquals(1.0, w1.getWeight(), 0.001);
        w1.setObject("b");
        assertEquals("b", w1.getObject());
    }

    @Test
    public void testConstructFromWeightObjects() {
        Set<WeightRandomMap.WeightObject<String>> weights = new HashSet<>();
        weights.add(new WeightRandomMap.WeightObject<>("x", 2.0));
        weights.add(new WeightRandomMap.WeightObject<>("y", 3.0));
        WeightRandomMap<String> map = new WeightRandomMap<>(weights);
        assertNotNull(map.next());
    }
}
