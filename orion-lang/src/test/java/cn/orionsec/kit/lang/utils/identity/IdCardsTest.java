package cn.orionsec.kit.lang.utils.identity;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * IdCards 单元测试
 */
public class IdCardsTest {

    // 使用一个合法的测试身份证号(虚构但校验位正确)
    // 110101199003074135 - 北京 1990-03-07 男
    private static final String VALID_ID = "110101199003074135";

    @Test
    public void testIsValidCard() {
        assertTrue(IdCards.isValidCard(VALID_ID));
    }

    @Test
    public void testIsValidCardInvalid() {
        assertFalse(IdCards.isValidCard("110101199003074130"));
        assertFalse(IdCards.isValidCard("12345"));
        assertFalse(IdCards.isValidCard("000000199003074133"));
    }

    @Test
    public void testGetBirth() {
        String birth = IdCards.getBirth(VALID_ID);
        assertEquals("19900307", birth);
    }

    @Test
    public void testGetYear() {
        assertEquals(1990, IdCards.getYear(VALID_ID));
    }

    @Test
    public void testGetMonth() {
        assertEquals(3, IdCards.getMonth(VALID_ID));
    }

    @Test
    public void testGetDay() {
        assertEquals(7, IdCards.getDay(VALID_ID));
    }

    @Test
    public void testGetGender() {
        // 第17位是3(第17个字符下标16),奇数=男
        assertTrue(IdCards.getGender(VALID_ID));
    }

    @Test
    public void testGetProvince() {
        String province = IdCards.getProvince(VALID_ID);
        assertEquals("北京", province);
    }

    @Test
    public void testGetProvinceCode() {
        Integer code = IdCards.getProvinceCode(VALID_ID);
        assertEquals(Integer.valueOf(11), code);
    }

    @Test
    public void testGetCityCode() {
        Integer code = IdCards.getCityCode(VALID_ID);
        assertEquals(Integer.valueOf(1101), code);
    }

    @Test
    public void testGetCountryCode() {
        Integer code = IdCards.getCountryCode(VALID_ID);
        assertEquals(Integer.valueOf(110101), code);
    }

    @Test
    public void testGetCheckCode18() {
        // 110101199003074135 的校验位是 '5'
        char checkCode = IdCards.getCheckCode18("11010119900307413");
        assertEquals('5', checkCode);
    }

    @Test
    public void testGetPowerSum() {
        char[] arr = "11010119900307413".toCharArray();
        int sum = IdCards.getPowerSum(arr);
        assertTrue(sum > 0);
    }

    @Test
    public void testGetAge() {
        int age = IdCards.getAge(VALID_ID);
        assertTrue(age > 30);
    }
}
