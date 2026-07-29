package cn.orionsec.kit.lang.define.number;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * number 包单元测试
 */
public class NumberPackageTest {

    @Test
    public void testNumber128Create() {
        Number128 n = new Number128(100L, 200L);
        assertEquals(100L, n.getLowValue());
        assertEquals(200L, n.getHighValue());
    }

    @Test
    public void testNumber128Setters() {
        Number128 n = new Number128(0L, 0L);
        n.setLowValue(50L);
        n.setHighValue(60L);
        assertEquals(50L, n.getLowValue());
        assertEquals(60L, n.getHighValue());
    }

    @Test
    public void testNumber128GetLongArray() {
        Number128 n = new Number128(10L, 20L);
        long[] arr = n.getLongArray();
        assertEquals(2, arr.length);
        assertEquals(10L, arr[0]);
        assertEquals(20L, arr[1]);
    }

    @Test
    public void testNumber128MaxValues() {
        Number128 n = new Number128(Long.MAX_VALUE, Long.MIN_VALUE);
        assertEquals(Long.MAX_VALUE, n.getLowValue());
        assertEquals(Long.MIN_VALUE, n.getHighValue());
    }
}
