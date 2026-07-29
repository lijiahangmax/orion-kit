package cn.orionsec.kit.lang.utils.math;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * BigIntegers 工具类测试
 */
public class BigIntegersTest {

    @Test
    public void testToBigInteger() {
        assertEquals(BigInteger.valueOf(10), BigIntegers.toBigInteger(10L));
        assertEquals(new BigInteger("100"), BigIntegers.toBigInteger("100"));
        assertEquals(BigInteger.ONE, BigIntegers.toBigInteger(true));
        assertEquals(BigInteger.ZERO, BigIntegers.toBigInteger(false));
        assertNull(BigIntegers.toBigInteger(null));
    }

    @Test
    public void testToBigIntegerWithDefault() {
        BigInteger def = BigInteger.TEN;
        assertEquals(def, BigIntegers.toBigInteger(null, def));
        assertEquals(new BigInteger("5"), BigIntegers.toBigInteger("5", def));
    }

    @Test
    public void testToBigIntegers() {
        BigInteger[] result = BigIntegers.toBigIntegers(1, 2L, "3");
        assertEquals(3, result.length);
    }

    @Test
    public void testToStr() {
        BigInteger b = new BigInteger("255");
        assertEquals("255", BigIntegers.toStr(b));
        assertNull(BigIntegers.toStr(null));
    }

    @Test
    public void testToStrWithRadix() {
        BigInteger b = new BigInteger("255");
        assertEquals("ff", BigIntegers.toStr(b, null, 16));
    }

    @Test
    public void testToLong() {
        BigInteger b = BigInteger.valueOf(999L);
        assertEquals(Long.valueOf(999L), BigIntegers.toLong(b));
        assertEquals(Long.valueOf(0L), BigIntegers.toLong(null));
    }

    @Test
    public void testToInteger() {
        BigInteger b = BigInteger.valueOf(42);
        assertEquals(Integer.valueOf(42), BigIntegers.toInteger(b));
        assertEquals(Integer.valueOf(0), BigIntegers.toInteger(null));
    }

    @Test
    public void testToDouble() {
        BigInteger b = BigInteger.valueOf(100);
        assertEquals(Double.valueOf(100.0), BigIntegers.toDouble(b));
        assertEquals(Double.valueOf(0.0), BigIntegers.toDouble(null));
    }

    @Test
    public void testAdd() {
        BigInteger a = BigInteger.valueOf(10);
        BigInteger b = BigInteger.valueOf(20);
        BigInteger c = BigInteger.valueOf(30);
        BigInteger result = BigIntegers.add(a, b, c);
        assertEquals(BigInteger.valueOf(60), result);
    }

    @Test
    public void testToBigIntegersEmpty() {
        BigInteger[] result = BigIntegers.toBigIntegers();
        assertEquals(0, result.length);
    }
}
