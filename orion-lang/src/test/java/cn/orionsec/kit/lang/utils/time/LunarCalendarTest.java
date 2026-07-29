package cn.orionsec.kit.lang.utils.time;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * LunarCalendar 单元测试
 */
public class LunarCalendarTest {

    @Test
    public void testConstructorWithDate() {
        // 2020-01-25 是农历正月初一(庚子年)
        Date d = Dates.build(2020, 1, 25);
        LunarCalendar lunar = new LunarCalendar(d);
        assertEquals(2020, lunar.getYear());
        assertEquals(1, lunar.getMonth());
        assertEquals(1, lunar.getDay());
    }

    @Test
    public void testConstructorWithTimestamp() {
        // 使用固定时间戳测试
        Date d = Dates.build(2021, 2, 12); // 2021-02-12 辛丑年正月初一
        LunarCalendar lunar = new LunarCalendar(d.getTime());
        assertEquals(2021, lunar.getYear());
        assertEquals(1, lunar.getMonth());
        assertEquals(1, lunar.getDay());
    }

    @Test
    public void testConstructorWithYearMonthDay() {
        LunarCalendar lunar = new LunarCalendar(2020, 6, 15);
        assertEquals(2020, lunar.getYear());
        assertEquals(6, lunar.getMonth());
        assertEquals(15, lunar.getDay());
    }

    @Test
    public void testGetChineseMonth() {
        // Use non-leap year (2019) to avoid "闰" prefix from (int,int,int) constructor
        LunarCalendar lunar = new LunarCalendar(2019, 1, 1);
        assertEquals("正月", lunar.getChineseMonth());

        LunarCalendar lunar2 = new LunarCalendar(2019, 12, 1);
        assertEquals("腊月", lunar2.getChineseMonth());

        LunarCalendar lunar3 = new LunarCalendar(2019, 6, 1);
        assertEquals("六月", lunar3.getChineseMonth());

        // Leap year constructor sets leap=true, so it adds "闰" prefix
        LunarCalendar lunarLeap = new LunarCalendar(2020, 1, 1);
        assertEquals("闰正月", lunarLeap.getChineseMonth());
    }

    @Test
    public void testGetChineseDay() {
        LunarCalendar lunar1 = new LunarCalendar(2020, 1, 1);
        assertEquals("初一", lunar1.getChineseDay());

        LunarCalendar lunar2 = new LunarCalendar(2020, 1, 10);
        assertEquals("初十", lunar2.getChineseDay());

        LunarCalendar lunar3 = new LunarCalendar(2020, 1, 15);
        assertEquals("十五", lunar3.getChineseDay());

        LunarCalendar lunar4 = new LunarCalendar(2020, 1, 20);
        assertEquals("二十", lunar4.getChineseDay());

        LunarCalendar lunar5 = new LunarCalendar(2020, 1, 30);
        assertEquals("三十", lunar5.getChineseDay());
    }

    @Test
    public void testGetChineseDayOthers() {
        LunarCalendar lunar = new LunarCalendar(2020, 1, 5);
        assertEquals("初五", lunar.getChineseDay());

        LunarCalendar lunar2 = new LunarCalendar(2020, 1, 22);
        assertEquals("廿二", lunar2.getChineseDay());
    }

    @Test
    public void testGetChineseZodiac() {
        // 2020庚子年 - 鼠
        LunarCalendar lunar = new LunarCalendar(2020, 1, 1);
        assertEquals("鼠", lunar.getChineseZodiac());
    }

    @Test
    public void testGetCyclical() {
        // 2020年 - 庚子
        LunarCalendar lunar = new LunarCalendar(2020, 1, 1);
        String cyclical = lunar.getCyclical();
        assertNotNull(cyclical);
        assertEquals(2, cyclical.length());
    }

    @Test
    public void testToString() {
        LunarCalendar lunar = new LunarCalendar(2020, 6, 15);
        assertEquals("2020-06-15", lunar.toString());
    }

    @Test
    public void testToChineseString() {
        LunarCalendar lunar = new LunarCalendar(2020, 1, 1);
        String chinese = lunar.toChineseString();
        assertNotNull(chinese);
        assertTrue(chinese.contains("鼠"));
        assertTrue(chinese.contains("正月"));
        assertTrue(chinese.contains("初一"));
    }

    @Test
    public void testKnownDate1() {
        // 2023-01-22 是农历正月初一(癸卯年)
        Date d = Dates.build(2023, 1, 22);
        LunarCalendar lunar = new LunarCalendar(d);
        assertEquals(2023, lunar.getYear());
        assertEquals(1, lunar.getMonth());
        assertEquals(1, lunar.getDay());
    }

    @Test
    public void testKnownDate2() {
        // 2022-02-01 是农历正月初一(壬寅年)
        Date d = Dates.build(2022, 2, 1);
        LunarCalendar lunar = new LunarCalendar(d);
        assertEquals(2022, lunar.getYear());
        assertEquals(1, lunar.getMonth());
        assertEquals(1, lunar.getDay());
    }
}
