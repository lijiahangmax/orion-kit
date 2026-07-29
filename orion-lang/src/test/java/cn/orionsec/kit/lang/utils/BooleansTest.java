package cn.orionsec.kit.lang.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class BooleansTest {

    @Test
    public void testNegate() {
        assertEquals(Boolean.FALSE, Booleans.negate(Boolean.TRUE));
        assertEquals(Boolean.TRUE, Booleans.negate(Boolean.FALSE));
        assertNull(Booleans.negate(null));
    }

    @Test
    public void testIsTrue() {
        assertTrue(Booleans.isTrue(Boolean.TRUE));
        assertFalse(Booleans.isTrue(Boolean.FALSE));
        assertFalse(Booleans.isTrue(null));
    }

    @Test
    public void testIsNotTrue() {
        assertFalse(Booleans.isNotTrue(Boolean.TRUE));
        assertTrue(Booleans.isNotTrue(Boolean.FALSE));
        assertTrue(Booleans.isNotTrue(null));
    }

    @Test
    public void testIsFalse() {
        assertTrue(Booleans.isFalse(Boolean.FALSE));
        assertFalse(Booleans.isFalse(Boolean.TRUE));
        assertFalse(Booleans.isFalse(null));
    }

    @Test
    public void testIsNotFalse() {
        assertFalse(Booleans.isNotFalse(Boolean.FALSE));
        assertTrue(Booleans.isNotFalse(Boolean.TRUE));
        assertTrue(Booleans.isNotFalse(null));
    }

    @Test
    public void testToBoolean() {
        assertTrue(Booleans.toBoolean(Boolean.TRUE));
        assertFalse(Booleans.toBoolean(Boolean.FALSE));
        assertFalse(Booleans.toBoolean((Boolean) null));
        assertTrue(Booleans.toBoolean(1));
        assertFalse(Booleans.toBoolean(0));
        assertTrue(Booleans.toBoolean(-1));
    }

    @Test
    public void testToString() {
        assertEquals("yes", Booleans.toString(true, "yes", "no"));
        assertEquals("no", Booleans.toString(false, "yes", "no"));
    }

    @Test
    public void testAnd() {
        assertTrue(Booleans.and(new boolean[]{true, true, true}));
        assertFalse(Booleans.and(new boolean[]{true, false, true}));
        assertFalse(Booleans.and((boolean[]) null));
        assertFalse(Booleans.and(new boolean[0]));
    }

    @Test
    public void testOr() {
        assertTrue(Booleans.or(new boolean[]{false, true, false}));
        assertFalse(Booleans.or(new boolean[]{false, false, false}));
        assertFalse(Booleans.or((boolean[]) null));
        assertFalse(Booleans.or(new boolean[0]));
    }

    @Test
    public void testCompare() {
        assertEquals(0, Booleans.compare(true, true));
        assertEquals(0, Booleans.compare(false, false));
        assertEquals(1, Booleans.compare(true, false));
        assertEquals(-1, Booleans.compare(false, true));
    }
}
