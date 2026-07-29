package cn.orionsec.kit.lang.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ComparesTest {

    @Test
    public void testCompare() {
        assertTrue(Compares.compare(1, 2) < 0);
        assertTrue(Compares.compare(2, 1) > 0);
        assertEquals(0, Compares.compare(1, 1));
    }

    @Test
    public void testCompared() {
        assertTrue(Compares.compared(1, 1));
        assertFalse(Compares.compared(1, 2));
        assertFalse(Compares.compared(null, 1));
        assertFalse(Compares.compared(1, null));
    }

    @Test
    public void testLt() {
        assertTrue(Compares.lt(1, 2));
        assertFalse(Compares.lt(2, 1));
        assertFalse(Compares.lt(1, 1));
    }

    @Test
    public void testLte() {
        assertTrue(Compares.lte(1, 2));
        assertTrue(Compares.lte(1, 1));
        assertFalse(Compares.lte(2, 1));
    }

    @Test
    public void testGt() {
        assertTrue(Compares.gt(2, 1));
        assertFalse(Compares.gt(1, 2));
        assertFalse(Compares.gt(1, 1));
    }

    @Test
    public void testGte() {
        assertTrue(Compares.gte(2, 1));
        assertTrue(Compares.gte(1, 1));
        assertFalse(Compares.gte(1, 2));
    }

    @Test
    public void testInRange() {
        assertTrue(Compares.inRange(5, 1, 10));
        assertTrue(Compares.inRange(1, 1, 10));
        assertTrue(Compares.inRange(10, 1, 10));
        assertFalse(Compares.inRange(0, 1, 10));
    }

    @Test
    public void testNotInRange() {
        assertTrue(Compares.notInRange(0, 1, 10));
        assertFalse(Compares.notInRange(5, 1, 10));
    }

    @Test
    public void testRangeInRange() {
        assertTrue(Compares.rangeInRange(1, 10, 2, 5));
        assertTrue(Compares.rangeInRange(1, 10, 1, 10));
        assertFalse(Compares.rangeInRange(1, 5, 3, 8));
    }
}
