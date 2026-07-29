package cn.orionsec.kit.lang.utils.unit;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * DistStorageUnit 单元测试
 */
public class DistStorageUnitTest {

    @Test
    public void testBitToByte() {
        BigDecimal result = DistStorageUnit.BIT.toByte(8L);
        assertEquals(1.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testBitToKb() {
        BigDecimal result = DistStorageUnit.BIT.toKilobyte(8192L);
        assertEquals(1.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testByteToBit() {
        BigDecimal result = DistStorageUnit.B.toBit(1L);
        assertEquals(8.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testByteToKb() {
        BigDecimal result = DistStorageUnit.B.toKilobyte(1024L);
        assertEquals(1.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testKbToMb() {
        BigDecimal result = DistStorageUnit.KB.toMegabyte(1024L);
        assertEquals(1.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testKbToByte() {
        BigDecimal result = DistStorageUnit.KB.toByte(1L);
        assertEquals(1024.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testMbToKb() {
        BigDecimal result = DistStorageUnit.MB.toKilobyte(1L);
        assertEquals(1024.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testMbToGb() {
        BigDecimal result = DistStorageUnit.MB.toGigabyte(1024L);
        assertEquals(1.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testGbToMb() {
        BigDecimal result = DistStorageUnit.GB.toMegabyte(1L);
        assertEquals(1024.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testGbToTb() {
        BigDecimal result = DistStorageUnit.GB.toTerabyte(1024L);
        assertEquals(1.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testTbToGb() {
        BigDecimal result = DistStorageUnit.TB.toGigabyte(1L);
        assertEquals(1024.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testIdentity() {
        assertEquals(0, BigDecimal.valueOf(5).compareTo(DistStorageUnit.KB.toKilobyte(5L)));
        assertEquals(0, BigDecimal.valueOf(1).compareTo(DistStorageUnit.MB.toMegabyte(1L)));
    }
}
