package cn.orionsec.kit.lang.utils.time;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Zodiacs 单元测试
 */
public class ZodiacsTest {

    @Test
    public void testGetChineseZodiacByYear() {
        assertEquals("鼠", Zodiacs.getChineseZodiac(1900));
        assertEquals("牛", Zodiacs.getChineseZodiac(1901));
        assertEquals("虎", Zodiacs.getChineseZodiac(1902));
        assertEquals("兔", Zodiacs.getChineseZodiac(1903));
        assertEquals("龙", Zodiacs.getChineseZodiac(1904));
        assertEquals("蛇", Zodiacs.getChineseZodiac(1905));
        assertEquals("马", Zodiacs.getChineseZodiac(1906));
        assertEquals("羊", Zodiacs.getChineseZodiac(1907));
        assertEquals("猴", Zodiacs.getChineseZodiac(1908));
        assertEquals("鸡", Zodiacs.getChineseZodiac(1909));
        assertEquals("狗", Zodiacs.getChineseZodiac(1910));
        assertEquals("猪", Zodiacs.getChineseZodiac(1911));
        // 循环
        assertEquals("鼠", Zodiacs.getChineseZodiac(1912));
        assertEquals("鼠", Zodiacs.getChineseZodiac(2020));
        assertEquals("牛", Zodiacs.getChineseZodiac(2021));
    }

    @Test
    public void testGetChineseZodiacInvalid() {
        assertNull(Zodiacs.getChineseZodiac(1899));
        assertNull(Zodiacs.getChineseZodiac(1800));
    }

    @Test
    public void testGetChineseZodiacByDate() {
        Date d = Dates.build(2020, 6, 15);
        String zodiac = Zodiacs.getChineseZodiac(d);
        assertEquals("鼠", zodiac);
    }

    @Test
    public void testGetChineseZodiacByCalendar() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2021);
        String zodiac = Zodiacs.getChineseZodiac(c);
        assertEquals("牛", zodiac);
    }

    @Test
    public void testGetChineseZodiacNullCalendar() {
        assertNull(Zodiacs.getChineseZodiac((Calendar) null));
    }

    @Test
    public void testGetConstellationByMonthDay() {
        // month 是 0-based
        assertEquals("摩羯座", Zodiacs.getConstellation(0, 10));  // 1月10日
        assertEquals("水瓶座", Zodiacs.getConstellation(0, 25));  // 1月25日
        assertEquals("水瓶座", Zodiacs.getConstellation(1, 10));  // 2月10日
        assertEquals("双鱼座", Zodiacs.getConstellation(1, 25));  // 2月25日
        assertEquals("双鱼座", Zodiacs.getConstellation(2, 10));  // 3月10日
        assertEquals("白羊座", Zodiacs.getConstellation(2, 25));  // 3月25日
        assertEquals("白羊座", Zodiacs.getConstellation(3, 10));  // 4月10日
        assertEquals("金牛座", Zodiacs.getConstellation(3, 25));  // 4月25日
        assertEquals("金牛座", Zodiacs.getConstellation(4, 10));  // 5月10日
        assertEquals("双子座", Zodiacs.getConstellation(4, 25));  // 5月25日
        assertEquals("双子座", Zodiacs.getConstellation(5, 10));  // 6月10日
        assertEquals("巨蟹座", Zodiacs.getConstellation(5, 25));  // 6月25日
        assertEquals("巨蟹座", Zodiacs.getConstellation(6, 10));  // 7月10日
        assertEquals("狮子座", Zodiacs.getConstellation(6, 25));  // 7月25日
        assertEquals("狮子座", Zodiacs.getConstellation(7, 10));  // 8月10日
        assertEquals("处女座", Zodiacs.getConstellation(7, 25));  // 8月25日
        assertEquals("处女座", Zodiacs.getConstellation(8, 10));  // 9月10日
        assertEquals("天秤座", Zodiacs.getConstellation(8, 25));  // 9月25日
        assertEquals("天秤座", Zodiacs.getConstellation(9, 10));  // 10月10日
        assertEquals("天蝎座", Zodiacs.getConstellation(9, 25));  // 10月25日
        assertEquals("天蝎座", Zodiacs.getConstellation(10, 10)); // 11月10日
        assertEquals("射手座", Zodiacs.getConstellation(10, 25)); // 11月25日
        assertEquals("射手座", Zodiacs.getConstellation(11, 10)); // 12月10日
        assertEquals("摩羯座", Zodiacs.getConstellation(11, 25)); // 12月25日
    }

    @Test
    public void testGetConstellationByDate() {
        // 6月15日 -> 双子座 (month in calendar is 5, 0-based)
        Date d = Dates.build(2020, 6, 15);
        String constellation = Zodiacs.getConstellation(d);
        assertEquals("双子座", constellation);
    }

    @Test
    public void testGetConstellationByCalendar() {
        Calendar c = Calendar.getInstance();
        c.set(2020, Calendar.MARCH, 25);
        String constellation = Zodiacs.getConstellation(c);
        assertEquals("白羊座", constellation);
    }

    @Test
    public void testGetConstellationNullCalendar() {
        assertNull(Zodiacs.getConstellation((Calendar) null));
    }
}
