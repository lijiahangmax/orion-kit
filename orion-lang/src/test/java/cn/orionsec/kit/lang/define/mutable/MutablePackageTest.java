package cn.orionsec.kit.lang.define.mutable;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * mutable 包单元测试
 */
public class MutablePackageTest {

    // ==================== MutableInt ====================

    @Test
    public void testMutableIntBasic() {
        MutableInt mi = MutableInt.of(10);
        assertEquals(10, mi.intValue());
        mi.setValue(20);
        assertEquals(20, mi.intValue());
    }

    @Test
    public void testMutableIntIncrement() {
        MutableInt mi = new MutableInt(5);
        mi.increment();
        assertEquals(6, mi.intValue());
        assertEquals(6, mi.getAndIncrement());
        assertEquals(7, mi.intValue());
        assertEquals(8, mi.incrementAndGet());
    }

    @Test
    public void testMutableIntDecrement() {
        MutableInt mi = new MutableInt(10);
        mi.decrement();
        assertEquals(9, mi.intValue());
        assertEquals(9, mi.getAndDecrement());
        assertEquals(8, mi.intValue());
        assertEquals(7, mi.decrementAndGet());
    }

    @Test
    public void testMutableIntAdd() {
        MutableInt mi = new MutableInt(0);
        mi.add(5);
        assertEquals(5, mi.intValue());
        assertEquals(8, mi.addAndGet(3));
        assertEquals(8, mi.getAndAdd(2));
        assertEquals(10, mi.intValue());
    }

    @Test
    public void testMutableIntSubtract() {
        MutableInt mi = new MutableInt(10);
        mi.subtract(3);
        assertEquals(7, mi.intValue());
    }

    @Test
    public void testMutableIntEquals() {
        MutableInt a = MutableInt.of(42);
        MutableInt b = MutableInt.of(42);
        MutableInt c = MutableInt.of(99);
        assertEquals(a, b);
        assertNotEquals(a, c);
    }

    @Test
    public void testMutableIntCompareTo() {
        MutableInt a = MutableInt.of(5);
        MutableInt b = MutableInt.of(10);
        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
        assertEquals(0, a.compareTo(MutableInt.of(5)));
    }

    @Test
    public void testMutableIntNumberConversions() {
        MutableInt mi = new MutableInt(100);
        assertEquals(100L, mi.longValue());
        assertEquals(100.0f, mi.floatValue(), 0.001);
        assertEquals(100.0, mi.doubleValue(), 0.001);
        assertEquals((short) 100, mi.shortValue());
        assertEquals((byte) 100, mi.byteValue());
    }

    // ==================== MutableBoolean ====================

    @Test
    public void testMutableBooleanBasic() {
        MutableBoolean mb = MutableBoolean.of(false);
        assertFalse(mb.get());
        mb.setTrue();
        assertTrue(mb.isTrue());
        assertFalse(mb.isFalse());
    }

    @Test
    public void testMutableBooleanSetFalse() {
        MutableBoolean mb = new MutableBoolean(true);
        mb.setFalse();
        assertFalse(mb.booleanValue());
    }

    @Test
    public void testMutableBooleanEquals() {
        MutableBoolean a = MutableBoolean.of(true);
        MutableBoolean b = MutableBoolean.of(true);
        MutableBoolean c = MutableBoolean.of(false);
        assertEquals(a, b);
        assertNotEquals(a, c);
    }

    @Test
    public void testMutableBooleanCompareTo() {
        MutableBoolean t = MutableBoolean.of(true);
        MutableBoolean f = MutableBoolean.of(false);
        assertTrue(t.compareTo(f) > 0);
        assertEquals(0, t.compareTo(MutableBoolean.of(true)));
    }

    // ==================== MutableObject ====================

    @Test
    public void testMutableObjectBasic() {
        MutableObject<String> mo = MutableObject.of("hello");
        assertEquals("hello", mo.get());
        mo.set("world");
        assertEquals("world", mo.get());
    }

    @Test
    public void testMutableObjectCreate() {
        MutableObject<Integer> mo = MutableObject.create();
        assertNull(mo.get());
        mo.set(42);
        assertEquals(Integer.valueOf(42), mo.get());
    }

    @Test
    public void testMutableObjectEquals() {
        MutableObject<String> a = MutableObject.of("test");
        MutableObject<String> b = MutableObject.of("test");
        MutableObject<String> c = MutableObject.of("other");
        assertEquals(a, b);
        assertNotEquals(a, c);
    }

    @Test
    public void testMutableObjectToString() {
        MutableObject<Integer> mo = MutableObject.of(123);
        assertEquals("123", mo.toString());
        MutableObject<Object> mn = MutableObject.create();
        assertEquals("null", mn.toString());
    }

    // ==================== MutableString ====================

    @Test
    public void testMutableStringCreate() {
        MutableString ms = MutableString.create();
        assertNotNull(ms);
        assertEquals(0, ms.length());
    }

    @Test
    public void testMutableStringOf() {
        MutableString ms = MutableString.of("hello");
        assertEquals("hello", ms.get());
        assertEquals(5, ms.length());
    }

    @Test
    public void testMutableStringSet() {
        MutableString ms = new MutableString("old");
        ms.set("new");
        assertEquals("new", ms.get());
    }

    @Test
    public void testMutableStringCharAt() {
        MutableString ms = new MutableString("abc");
        assertEquals('a', ms.charAt(0));
        assertEquals('b', ms.charAt(1));
        assertEquals('c', ms.charAt(2));
    }

    @Test
    public void testMutableStringSubSequence() {
        MutableString ms = new MutableString("hello world");
        assertEquals("hello", ms.subSequence(0, 5).toString());
    }

    // ==================== MutableLong (via MutableInt pattern) ====================

    @Test
    public void testMutableLongBasic() {
        MutableLong ml = MutableLong.of(100L);
        assertEquals(100L, ml.longValue());
        ml.increment();
        assertEquals(101L, ml.longValue());
        ml.decrement();
        assertEquals(100L, ml.longValue());
    }

    @Test
    public void testMutableLongAdd() {
        MutableLong ml = new MutableLong(50L);
        ml.add(10L);
        assertEquals(60L, ml.longValue());
        ml.subtract(5L);
        assertEquals(55L, ml.longValue());
    }

    @Test
    public void testMutableLongEquals() {
        MutableLong a = MutableLong.of(42L);
        MutableLong b = MutableLong.of(42L);
        assertEquals(a, b);
    }

    // ==================== MutableDouble ====================

    @Test
    public void testMutableDoubleBasic() {
        MutableDouble md = MutableDouble.of(3.14);
        assertEquals(3.14, md.doubleValue(), 0.001);
        md.setValue(2.71);
        assertEquals(2.71, md.doubleValue(), 0.001);
    }

    @Test
    public void testMutableDoubleAdd() {
        MutableDouble md = new MutableDouble(1.0);
        md.add(0.5);
        assertEquals(1.5, md.doubleValue(), 0.001);
    }

    @Test
    public void testMutableDoubleEquals() {
        MutableDouble a = MutableDouble.of(1.0);
        MutableDouble b = MutableDouble.of(1.0);
        assertEquals(a, b);
    }

    // ==================== MutableFloat ====================

    @Test
    public void testMutableFloatBasic() {
        MutableFloat mf = MutableFloat.of(1.5f);
        assertEquals(1.5f, mf.floatValue(), 0.001);
        mf.setValue(2.5f);
        assertEquals(2.5f, mf.floatValue(), 0.001);
    }

    @Test
    public void testMutableFloatAdd() {
        MutableFloat mf = new MutableFloat(1.0f);
        mf.add(0.5f);
        assertEquals(1.5f, mf.floatValue(), 0.001);
    }

    // ==================== MutableByte ====================

    @Test
    public void testMutableByteBasic() {
        MutableByte mb = MutableByte.of((byte) 10);
        assertEquals(10, mb.byteValue());
        mb.increment();
        assertEquals(11, mb.byteValue());
    }

    // ==================== MutableShort ====================

    @Test
    public void testMutableShortBasic() {
        MutableShort ms = MutableShort.of((short) 100);
        assertEquals(100, ms.shortValue());
        ms.increment();
        assertEquals(101, ms.shortValue());
    }
}
