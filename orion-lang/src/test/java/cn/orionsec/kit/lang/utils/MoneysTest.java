package cn.orionsec.kit.lang.utils;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MoneysTest {

    @Test
    public void testToCurrencyZero() {
        assertEquals("零元整", Moneys.toCurrency(BigDecimal.ZERO));
    }

    @Test
    public void testToCurrencyPositive() {
        String result = Moneys.toCurrency(new BigDecimal("100.00"));
        assertEquals("壹佰元整", result);
    }

    @Test
    public void testToCurrencyWithCents() {
        String result = Moneys.toCurrency(new BigDecimal("1.23"));
        assertEquals("壹元贰角叁分", result);
    }

    @Test
    public void testToCurrencyNegative() {
        String result = Moneys.toCurrency(new BigDecimal("-100.00"));
        assertTrue(result.startsWith("负"));
    }

    @Test
    public void testToDecimalZero() {
        BigDecimal result = Moneys.toDecimal("零元整");
        assertEquals(new BigDecimal("0.00"), result);
    }

    @Test
    public void testToDecimalPositive() {
        BigDecimal result = Moneys.toDecimal("壹佰元整");
        assertEquals(new BigDecimal("100.00"), result);
    }

    @Test
    public void testRoundTrip() {
        BigDecimal original = new BigDecimal("12345.67");
        String currency = Moneys.toCurrency(original);
        BigDecimal back = Moneys.toDecimal(currency);
        assertEquals(original, back);
    }

    @Test
    public void testRoundTripLargeAmount() {
        BigDecimal original = new BigDecimal("9999999.99");
        String currency = Moneys.toCurrency(original);
        BigDecimal back = Moneys.toDecimal(currency);
        assertEquals(original, back);
    }
}
