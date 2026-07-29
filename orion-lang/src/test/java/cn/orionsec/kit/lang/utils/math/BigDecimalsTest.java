package cn.orionsec.kit.lang.utils.math;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.*;

/**
 * BigDecimals 工具类测试
 */
public class BigDecimalsTest {

    @Test
    public void testToBigDecimal() {
        assertEquals(new BigDecimal("10"), BigDecimals.toBigDecimal("10"));
        assertEquals(BigDecimal.valueOf(10L), BigDecimals.toBigDecimal(10L));
        assertEquals(BigDecimal.ONE, BigDecimals.toBigDecimal(true));
        assertEquals(BigDecimal.ZERO, BigDecimals.toBigDecimal(false));
        assertNull(BigDecimals.toBigDecimal(null));
    }

    @Test
    public void testToBigDecimalWithDefault() {
        BigDecimal def = BigDecimal.TEN;
        assertEquals(def, BigDecimals.toBigDecimal(null, def));
        assertEquals(new BigDecimal("5"), BigDecimals.toBigDecimal("5", def));
    }

    @Test
    public void testToBigDecimals() {
        BigDecimal[] result = BigDecimals.toBigDecimals(1, 2L, "3");
        assertEquals(3, result.length);
        assertEquals(new BigDecimal(1), result[0]);
    }

    @Test
    public void testFormat() {
        String result = BigDecimals.format(3.14159, "#.##");
        assertEquals("3.14", result);
    }

    @Test
    public void testToStr() {
        BigDecimal b = new BigDecimal("3.14159");
        String result = BigDecimals.toStr(b);
        assertEquals("3.14", result);
    }

    @Test
    public void testToStrWithLength() {
        BigDecimal b = new BigDecimal("3.14159");
        String result = BigDecimals.toStr(b, "0", 3, RoundingMode.DOWN);
        assertEquals("3.141", result);
    }

    @Test
    public void testToStrNull() {
        assertEquals("default", BigDecimals.toStr(null, "default"));
    }

    @Test
    public void testToLong() {
        BigDecimal b = new BigDecimal("100.99");
        assertEquals(Long.valueOf(100L), BigDecimals.toLong(b));
        assertEquals(Long.valueOf(0L), BigDecimals.toLong(null));
    }

    @Test
    public void testToInteger() {
        BigDecimal b = new BigDecimal("42.5");
        assertEquals(Integer.valueOf(42), BigDecimals.toInteger(b));
        assertEquals(Integer.valueOf(0), BigDecimals.toInteger(null));
    }

    @Test
    public void testToBigDecimalFromDifferentTypes() {
        assertNotNull(BigDecimals.toBigDecimal((byte) 1));
        assertNotNull(BigDecimals.toBigDecimal((short) 2));
        assertNotNull(BigDecimals.toBigDecimal(3));
        assertNotNull(BigDecimals.toBigDecimal(4L));
        assertNotNull(BigDecimals.toBigDecimal(5.0f));
        assertNotNull(BigDecimals.toBigDecimal(6.0d));
    }
}
