package cn.orionsec.kit.lang.utils.unit;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * LengthUnit 单元测试
 */
public class LengthUnitTest {

    @Test
    public void testMmToMm() {
        BigDecimal result = LengthUnit.MM.toMillimetre(1000L);
        assertEquals(0, new BigDecimal("1000").compareTo(result));
    }

    @Test
    public void testMmToCm() {
        BigDecimal result = LengthUnit.MM.toCentimeter(100L);
        assertEquals(0, new BigDecimal("10.00").compareTo(result.stripTrailingZeros()) | 0);
        assertTrue(result.doubleValue() == 10.0);
    }

    @Test
    public void testMmToM() {
        BigDecimal result = LengthUnit.MM.toMetre(1000L);
        assertEquals(1.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testMmToKm() {
        BigDecimal result = LengthUnit.MM.toKilometre(1000000L);
        assertEquals(1.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testCmToMm() {
        BigDecimal result = LengthUnit.CM.toMillimetre(10L);
        assertEquals(100.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testCmToM() {
        BigDecimal result = LengthUnit.CM.toMetre(100L);
        assertEquals(1.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testDmToM() {
        BigDecimal result = LengthUnit.DM.toMetre(10L);
        assertEquals(1.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testMToCm() {
        BigDecimal result = LengthUnit.M.toCentimeter(1L);
        assertEquals(100.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testMToKm() {
        BigDecimal result = LengthUnit.M.toKilometre(1000L);
        assertEquals(1.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testKmToM() {
        BigDecimal result = LengthUnit.KM.toMetre(1L);
        assertEquals(1000.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testKmToMm() {
        BigDecimal result = LengthUnit.KM.toMillimetre(1L);
        assertEquals(1000000.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testKmToCm() {
        BigDecimal result = LengthUnit.KM.toCentimeter(1L);
        assertEquals(100000.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testWithScaleAndRoundingMode() {
        BigDecimal result = LengthUnit.MM.toMetre(BigDecimal.valueOf(1500), 4, RoundingMode.HALF_UP);
        assertEquals(1.5, result.doubleValue(), 0.001);
    }

    @Test
    public void testIdentity() {
        BigDecimal result = LengthUnit.M.toMetre(5L);
        assertEquals(0, BigDecimal.valueOf(5).compareTo(result));
    }
}
