package cn.orionsec.kit.lang.utils.identity;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * CreditCodes 单元测试
 */
public class CreditCodesTest {

    @Test
    public void testValidCreditCode() {
        // 生成一个随机信用代码并验证
        String code = CreditCodes.random();
        assertTrue(CreditCodes.validCreditCode(code));
    }

    @Test
    public void testInvalidCreditCode() {
        assertFalse(CreditCodes.validCreditCode("12345"));
        assertFalse(CreditCodes.validCreditCode("AAAAAAAAAAAAAAAAAA"));
    }

    @Test
    public void testRandom() {
        String code = CreditCodes.random();
        assertNotNull(code);
        assertEquals(18, code.length());
    }

    @Test
    public void testRandomMultiple() {
        // 生成多个验证全部合法
        for (int i = 0; i < 10; i++) {
            String code = CreditCodes.random();
            assertTrue("Generated code should be valid: " + code, CreditCodes.validCreditCode(code));
        }
    }

    @Test
    public void testGetParityBit() {
        String code = CreditCodes.random();
        int parity = CreditCodes.getParityBit(code.substring(0, 17));
        assertTrue(parity >= 0 && parity < 31);
    }

    @Test
    public void testGetParityBitInvalidChar() {
        // 包含非法字符应返回-1
        int result = CreditCodes.getParityBit("IIIIIIIIIIIIIIIII");
        assertEquals(-1, result);
    }
}
