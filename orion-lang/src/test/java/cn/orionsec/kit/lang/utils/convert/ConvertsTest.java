package cn.orionsec.kit.lang.utils.convert;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConvertsTest {

    @Test
    public void testToByteFromInt() {
        byte result = Converts.toByte(10);
        assertEquals(10, result);
    }

    @Test
    public void testToByteFromBoolean() {
        assertEquals(1, Converts.toByte(true));
        assertEquals(0, Converts.toByte(false));
    }

    @Test
    public void testToByteFromString() {
        assertEquals(42, Converts.toByte("42"));
    }

    @Test
    public void testToByteFromChar() {
        assertEquals(65, Converts.toByte('A'));
    }

    @Test
    public void testToShortFromByte() {
        assertEquals(10, Converts.toShort((byte) 10));
    }

    @Test
    public void testToShortFromInt() {
        assertEquals(1000, Converts.toShort(1000));
    }

    @Test
    public void testToShortFromString() {
        assertEquals(123, Converts.toShort("123"));
    }

    @Test
    public void testToShortFromBoolean() {
        assertEquals(1, Converts.toShort(true));
        assertEquals(0, Converts.toShort(false));
    }

    @Test
    public void testToIntFromByte() {
        assertEquals(5, Converts.toInt((byte) 5));
    }

    @Test
    public void testToIntFromShort() {
        assertEquals(300, Converts.toInt((short) 300));
    }

    @Test
    public void testToIntFromLong() {
        assertEquals(12345, Converts.toInt(12345L));
    }

    @Test
    public void testToIntFromFloat() {
        assertEquals(3, Converts.toInt(3.7f));
    }

    @Test
    public void testToIntFromDouble() {
        assertEquals(7, Converts.toInt(7.9));
    }

    @Test
    public void testToIntFromBoolean() {
        assertEquals(1, Converts.toInt(true));
        assertEquals(0, Converts.toInt(false));
    }

    @Test
    public void testToIntFromChar() {
        assertEquals(65, Converts.toInt('A'));
    }

    @Test
    public void testToIntFromString() {
        assertEquals(999, Converts.toInt("999"));
    }

    @Test
    public void testToLongFromInt() {
        assertEquals(100L, Converts.toLong(100));
    }

    @Test
    public void testToLongFromString() {
        assertEquals(123456789L, Converts.toLong("123456789"));
    }

    @Test
    public void testToFloatFromInt() {
        assertEquals(10.0f, Converts.toFloat(10), 0.001f);
    }

    @Test
    public void testToFloatFromString() {
        assertEquals(3.14f, Converts.toFloat("3.14"), 0.001f);
    }

    @Test
    public void testToDoubleFromInt() {
        assertEquals(10.0, Converts.toDouble(10), 0.001);
    }

    @Test
    public void testToDoubleFromString() {
        assertEquals(3.14159, Converts.toDouble("3.14159"), 0.00001);
    }

    @Test
    public void testToBooleanFromInt() {
        assertTrue(Converts.toBoolean(1));
        assertFalse(Converts.toBoolean(0));
    }

    @Test
    public void testToBooleanFromString() {
        assertTrue(Converts.toBoolean("true"));
        assertFalse(Converts.toBoolean("false"));
    }

    @Test
    public void testToCharFromInt() {
        assertEquals('A', Converts.toChar(65));
    }

    @Test
    public void testToCharFromString() {
        assertEquals('H', Converts.toChar("H"));
    }

    @Test
    public void testToString() {
        assertEquals("123", Converts.toString(123));
        assertEquals("true", Converts.toString(true));
    }
}
