package cn.orionsec.kit.lang.utils.math;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Numbers 工具类测试
 */
public class NumbersTest {

    @Test
    public void testIsZero() {
        assertTrue(Numbers.isZero(0));
        assertTrue(Numbers.isZero(0L));
        assertTrue(Numbers.isZero(0.0));
        assertFalse(Numbers.isZero(1));
        assertFalse(Numbers.isZero(-1));
    }

    @Test
    public void testIsNotZero() {
        assertTrue(Numbers.isNotZero(1));
        assertTrue(Numbers.isNotZero(-1));
        assertFalse(Numbers.isNotZero(0));
    }

    @Test
    public void testIsNegative() {
        assertTrue(Numbers.isNegative(-1));
        assertTrue(Numbers.isNegative(-100L));
        assertFalse(Numbers.isNegative(0));
        assertFalse(Numbers.isNegative(1));
    }

    @Test
    public void testIsNotNegative() {
        assertTrue(Numbers.isNotNegative(0));
        assertTrue(Numbers.isNotNegative(1));
        assertFalse(Numbers.isNotNegative(-1));
    }

    @Test
    public void testMin() {
        assertEquals(1, Numbers.min(3, 1, 4, 1, 5));
        assertEquals(-5, Numbers.min(0, -5, 3, 2));
        assertEquals(10L, Numbers.min(10L, 20L, 30L));
    }

    @Test
    public void testMax() {
        assertEquals(5, Numbers.max(3, 1, 4, 1, 5));
        assertEquals(3, Numbers.max(0, -5, 3, 2));
        assertEquals(30L, Numbers.max(10L, 20L, 30L));
    }

    @Test
    public void testSum() {
        assertEquals(15L, Numbers.sum(1, 2, 3, 4, 5));
        assertEquals(6L, Numbers.sum(1L, 2L, 3L));
    }

    @Test
    public void testAvg() {
        assertEquals(3, Numbers.avg(2, 3, 4));
        assertEquals(0, Numbers.avg());
    }

    @Test
    public void testInRange() {
        assertTrue(Numbers.inRange(5, 1, 10));
        assertTrue(Numbers.inRange(1, 1, 10));
        assertFalse(Numbers.inRange(11, 1, 10));
    }

    @Test
    public void testGetMin2Power() {
        assertEquals(8, Numbers.getMin2Power(6));
        assertEquals(16, Numbers.getMin2Power(9));
        assertEquals(16, Numbers.getMin2Power(16));
        assertEquals(32, Numbers.getMin2Power(17));
        assertEquals(1, Numbers.getMin2Power(1));
    }

    @Test
    public void testCompare() {
        assertEquals(0, Numbers.compare(5, 5));
        assertTrue(Numbers.compare(3, 5) < 0);
        assertTrue(Numbers.compare(5, 3) > 0);
    }

    @Test
    public void testIsAllZero() {
        assertTrue(Numbers.isAllZero(0, 0, 0));
        assertFalse(Numbers.isAllZero(0, 1, 0));
        assertFalse(Numbers.isAllZero());
    }

    @Test
    public void testIsDecimal() {
        assertTrue(Numbers.isDecimal(3.14));
        assertFalse(Numbers.isDecimal(3.0));
        assertTrue(Numbers.isDecimal(1.5f));
        assertFalse(Numbers.isDecimal(2.0f));
    }
}
