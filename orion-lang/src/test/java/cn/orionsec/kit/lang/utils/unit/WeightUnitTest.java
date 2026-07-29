package cn.orionsec.kit.lang.utils.unit;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.assertEquals;

/**
 * WeightUnit 单元测试
 */
public class WeightUnitTest {

    @Test
    public void testMgToMg() {
        BigDecimal result = WeightUnit.MG.toMilligram(1000L);
        assertEquals(0, BigDecimal.valueOf(1000).compareTo(result));
    }

    @Test
    public void testMgToG() {
        BigDecimal result = WeightUnit.MG.toGram(1000L);
        assertEquals(1.0, result.doubleValue(), 0.001);
    }

    @Test
    public void testMgToKg() {
        BigDecimal result = WeightUnit.MG.toKilogram(1000000L);
        assertEquals(1.0, result.doubleValue(), 0.001);
    }

    @Test
    public void testGToMg() {
        BigDecimal result = WeightUnit.G.toMilligram(1L);
        assertEquals(1000.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testGToKg() {
        BigDecimal result = WeightUnit.G.toKilogram(1000L);
        assertEquals(1.0, result.doubleValue(), 0.001);
    }

    @Test
    public void testGToT() {
        BigDecimal result = WeightUnit.G.toTon(1000000L);
        assertEquals(1.0, result.doubleValue(), 0.001);
    }

    @Test
    public void testKgToG() {
        BigDecimal result = WeightUnit.KG.toGram(1L);
        assertEquals(1000.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testKgToT() {
        BigDecimal result = WeightUnit.KG.toTon(1000L);
        assertEquals(1.0, result.doubleValue(), 0.001);
    }

    @Test
    public void testTToKg() {
        BigDecimal result = WeightUnit.T.toKilogram(1L);
        assertEquals(1000.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testTToG() {
        BigDecimal result = WeightUnit.T.toGram(1L);
        assertEquals(1000000.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testTToMg() {
        BigDecimal result = WeightUnit.T.toMilligram(1L);
        assertEquals(1000000000.0, result.doubleValue(), 0.01);
    }

    @Test
    public void testIdentity() {
        assertEquals(0, BigDecimal.valueOf(5).compareTo(WeightUnit.KG.toKilogram(5L)));
        assertEquals(0, BigDecimal.valueOf(3).compareTo(WeightUnit.G.toGram(3L)));
    }

    @Test
    public void testWithScaleAndRoundingMode() {
        BigDecimal result = WeightUnit.MG.toGram(BigDecimal.valueOf(1500), 6, RoundingMode.HALF_UP);
        assertEquals(1.5, result.doubleValue(), 0.001);
    }
}
