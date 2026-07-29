package cn.orionsec.kit.lang.utils.time;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Dates 单元测试
 */
public class DatesTest {

    @Test
    public void testDate() {
        Date d = Dates.date();
        assertNotNull(d);
    }

    @Test
    public void testDateFromSeconds() {
        // 2020-01-01 00:00:00 UTC+8 => 1577808000
        Date d = Dates.date(1577808000);
        assertNotNull(d);
        assertEquals(1577808000000L, d.getTime());
    }

    @Test
    public void testDateFromMillis() {
        Date d = Dates.date(1577808000000L);
        assertNotNull(d);
        assertEquals(1577808000000L, d.getTime());
    }

    @Test
    public void testDateFromObject() {
        // null
        assertNull(Dates.date((Object) null));
        // Date
        Date now = new Date();
        assertEquals(now, Dates.date((Object) now));
        // String
        Date parsed = Dates.date((Object) "2020-01-15 10:30:00");
        assertNotNull(parsed);
        assertEquals("2020-01-15 10:30:00", Dates.format(parsed));
    }

    @Test
    public void testBuild() {
        Date d = Dates.build(2020, 6, 15);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        assertEquals(2020, c.get(Calendar.YEAR));
        assertEquals(5, c.get(Calendar.MONTH)); // 0-based
        assertEquals(15, c.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testBuildWithHms() {
        Date d = Dates.build(2020, 3, 10, 14, 30, 45);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        assertEquals(2020, c.get(Calendar.YEAR));
        assertEquals(2, c.get(Calendar.MONTH));
        assertEquals(10, c.get(Calendar.DAY_OF_MONTH));
        assertEquals(14, c.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, c.get(Calendar.MINUTE));
        assertEquals(45, c.get(Calendar.SECOND));
    }

    @Test
    public void testFormat() {
        Date d = Dates.build(2020, 1, 15, 10, 30, 0);
        String formatted = Dates.format(d);
        assertEquals("2020-01-15 10:30:00", formatted);
    }

    @Test
    public void testFormatWithPattern() {
        Date d = Dates.build(2020, 1, 15, 10, 30, 0);
        assertEquals("2020-01-15", Dates.format(d, "yyyy-MM-dd"));
        assertEquals("20200115", Dates.format(d, "yyyyMMdd"));
    }

    @Test
    public void testFormatNull() {
        assertNull(Dates.format(null, "yyyy-MM-dd"));
    }

    @Test
    public void testParse() {
        Date d = Dates.parse("2020-01-15 10:30:00");
        assertNotNull(d);
        assertEquals("2020-01-15 10:30:00", Dates.format(d));
    }

    @Test
    public void testParseYmd() {
        Date d = Dates.parse("2020-01-15");
        assertNotNull(d);
        String formatted = Dates.format(d, "yyyy-MM-dd");
        assertEquals("2020-01-15", formatted);
    }

    @Test
    public void testParseSlash() {
        Date d = Dates.parse("2020/01/15");
        assertNotNull(d);
        assertEquals("2020-01-15", Dates.format(d, "yyyy-MM-dd"));
    }

    @Test
    public void testParseNumber() {
        Date d = Dates.parse("20200115");
        assertNotNull(d);
        assertEquals("2020-01-15", Dates.format(d, "yyyy-MM-dd"));
    }

    @Test
    public void testParseBlank() {
        assertNull(Dates.parse(""));
        assertNull(Dates.parse("  "));
    }

    @Test
    public void testParseWithPattern() {
        Date d = Dates.parse("2020-06-15", "yyyy-MM-dd");
        assertNotNull(d);
        assertEquals("2020-06-15", Dates.format(d, "yyyy-MM-dd"));
    }

    @Test
    public void testClearHms() {
        Date d = Dates.build(2020, 6, 15, 14, 30, 45);
        Date cleared = Dates.clearHms(d);
        Calendar c = Calendar.getInstance();
        c.setTime(cleared);
        assertEquals(0, c.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, c.get(Calendar.MINUTE));
        assertEquals(0, c.get(Calendar.SECOND));
        assertEquals(0, c.get(Calendar.MILLISECOND));
    }

    @Test
    public void testDayEnd() {
        Date d = Dates.build(2020, 6, 15, 10, 0, 0);
        Date end = Dates.dayEnd(d);
        Calendar c = Calendar.getInstance();
        c.setTime(end);
        assertEquals(23, c.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, c.get(Calendar.MINUTE));
        assertEquals(59, c.get(Calendar.SECOND));
    }

    @Test
    public void testMonthFirstDay() {
        Date d = Dates.build(2020, 6, 15, 14, 30, 0);
        Date first = Dates.monthFirstDay(d);
        Calendar c = Calendar.getInstance();
        c.setTime(first);
        assertEquals(1, c.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testMonthLastDay() {
        Date d = Dates.build(2020, 6, 15);
        Date last = Dates.monthLastDay(d);
        Calendar c = Calendar.getInstance();
        c.setTime(last);
        assertEquals(30, c.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testIsLeapYear() {
        assertTrue(Dates.isLeapYear(Dates.build(2020, 1, 1)));
        assertFalse(Dates.isLeapYear(Dates.build(2019, 1, 1)));
        assertTrue(Dates.isLeapYear(Dates.build(2000, 1, 1)));
        assertFalse(Dates.isLeapYear(Dates.build(1900, 1, 1)));
    }

    @Test
    public void testGetMonthLastDayStatic() {
        assertEquals(31, Dates.getMonthLastDay(2020, 1));
        assertEquals(29, Dates.getMonthLastDay(2020, 2));
        assertEquals(28, Dates.getMonthLastDay(2019, 2));
        assertEquals(30, Dates.getMonthLastDay(2020, 4));
        assertEquals(31, Dates.getMonthLastDay(2020, 12));
    }

    @Test
    public void testGetQuarter() {
        assertEquals(1, Dates.getQuarter(Dates.build(2020, 1, 1)));
        assertEquals(1, Dates.getQuarter(Dates.build(2020, 3, 1)));
        assertEquals(2, Dates.getQuarter(Dates.build(2020, 4, 1)));
        assertEquals(3, Dates.getQuarter(Dates.build(2020, 7, 1)));
        assertEquals(4, Dates.getQuarter(Dates.build(2020, 12, 1)));
    }

    @Test
    public void testIsAmPm() {
        Date morning = Dates.build(2020, 6, 15, 9, 0, 0);
        Date afternoon = Dates.build(2020, 6, 15, 14, 0, 0);
        assertTrue(Dates.isAm(morning));
        assertFalse(Dates.isPm(morning));
        assertTrue(Dates.isPm(afternoon));
        assertFalse(Dates.isAm(afternoon));
    }

    @Test
    public void testHourType() {
        assertEquals("凌晨", Dates.hourType(Dates.build(2020, 1, 1, 3, 0, 0)));
        assertEquals("上午", Dates.hourType(Dates.build(2020, 1, 1, 9, 0, 0)));
        assertEquals("中午", Dates.hourType(Dates.build(2020, 1, 1, 12, 0, 0)));
        assertEquals("下午", Dates.hourType(Dates.build(2020, 1, 1, 15, 0, 0)));
        assertEquals("傍晚", Dates.hourType(Dates.build(2020, 1, 1, 19, 0, 0)));
        assertEquals("晚上", Dates.hourType(Dates.build(2020, 1, 1, 21, 0, 0)));
        assertEquals("深夜", Dates.hourType(Dates.build(2020, 1, 1, 23, 0, 0)));
    }

    @Test
    public void testIntervalMs() {
        Date d1 = Dates.build(2020, 1, 1, 0, 0, 0);
        Date d2 = Dates.build(2020, 1, 2, 0, 0, 0);
        assertEquals(86400000L, Dates.intervalMs(d1, d2));
    }

    @Test
    public void testInterval() {
        Date d1 = Dates.build(2020, 1, 1, 0, 0, 0);
        Date d2 = Dates.build(2020, 1, 2, 2, 30, 45);
        String interval = Dates.interval(d1, d2);
        assertEquals("1天2时30分45秒", interval);
    }

    @Test
    public void testIntervalFull() {
        Date d1 = Dates.build(2020, 1, 1, 0, 0, 0);
        Date d2 = Dates.build(2020, 1, 1, 0, 0, 5);
        String interval = Dates.interval(d1, d2, true);
        assertEquals("0天0时0分5秒", interval);
    }

    @Test
    public void testIntervalAnalysis() {
        Date d1 = Dates.build(2020, 1, 1, 0, 0, 0);
        Date d2 = Dates.build(2020, 1, 2, 3, 25, 10);
        long[] analysis = Dates.intervalAnalysis(d1, d2);
        assertEquals(1, analysis[0]); // days
        assertEquals(3, analysis[1]); // hours
        assertEquals(25, analysis[2]); // minutes
        assertEquals(10, analysis[3]); // seconds
    }

    @Test
    public void testConvert() {
        String result = Dates.convert("2020-01-15 10:30:00", "yyyy-MM-dd HH:mm:ss", "yyyyMMdd");
        assertEquals("20200115", result);
    }

    @Test
    public void testAnalysis() {
        Date d = Dates.build(2020, 3, 15, 14, 30, 45);
        int[] a = Dates.analysis(d);
        assertEquals(2020, a[0]);
        assertEquals(3, a[1]);
        assertEquals(15, a[2]);
        assertEquals(14, a[3]);
        assertEquals(30, a[4]);
        assertEquals(45, a[5]);
    }

    @Test
    public void testGetIncrementDayDates() {
        Date start = Dates.build(2020, 1, 1);
        Date[] dates = Dates.getIncrementDayDates(start, 1, 3);
        assertEquals(3, dates.length);
        assertEquals("2020-01-01", Dates.format(dates[0], "yyyy-MM-dd"));
        assertEquals("2020-01-02", Dates.format(dates[1], "yyyy-MM-dd"));
        assertEquals("2020-01-03", Dates.format(dates[2], "yyyy-MM-dd"));
    }

    @Test
    public void testIsExpired() {
        Date past = Dates.build(2000, 1, 1);
        Date future = Dates.build(2099, 1, 1);
        assertTrue(Dates.isExpired(past));
        assertFalse(Dates.isExpired(future));
    }

    @Test
    public void testInFuture() {
        Date past = Dates.build(2000, 1, 1);
        Date future = Dates.build(2099, 1, 1);
        assertFalse(Dates.inFuture(past));
        assertTrue(Dates.inFuture(future));
    }

    @Test
    public void testCalendar() {
        Calendar c = Dates.calendar();
        assertNotNull(c);
    }

    @Test
    public void testCalendarFromDate() {
        Date d = Dates.build(2020, 6, 15);
        Calendar c = Dates.calendar(d);
        assertEquals(2020, c.get(Calendar.YEAR));
        assertEquals(5, c.get(Calendar.MONTH));
        assertEquals(15, c.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testIsDateClass() {
        assertTrue(BaseDates.isDateClass(Date.class));
        assertTrue(BaseDates.isDateClass(Calendar.class));
        assertTrue(BaseDates.isDateClass(Long.class));
        assertTrue(BaseDates.isDateClass(Integer.class));
        assertFalse(BaseDates.isDateClass(String.class));
    }

    @Test
    public void testIsMilli() {
        assertTrue(BaseDates.isMilli(1577808000000L));
        assertFalse(BaseDates.isMilli(1577808000L));
    }

    @Test
    public void testPad() {
        assertEquals("2020-01-15", BaseDates.pad(2020, 1, 15));
        assertEquals("2020-01-15 10:30:00", BaseDates.pad(2020, 1, 15, 10, 30, 0));
        assertEquals("2020-01-15 10:30:00 123", BaseDates.pad(2020, 1, 15, 10, 30, 0, 123));
    }

    @Test
    public void testMonthFirstDayHms() {
        Date d = Dates.build(2020, 6, 15, 14, 30, 45);
        Date first = Dates.monthFirstDayHms(d);
        Calendar c = Calendar.getInstance();
        c.setTime(first);
        assertEquals(1, c.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, c.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, c.get(Calendar.MINUTE));
        assertEquals(0, c.get(Calendar.SECOND));
    }

    @Test
    public void testMonthLastDayHms() {
        Date d = Dates.build(2020, 6, 15);
        Date last = Dates.monthLastDayHms(d);
        Calendar c = Calendar.getInstance();
        c.setTime(last);
        assertEquals(30, c.get(Calendar.DAY_OF_MONTH));
        assertEquals(23, c.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, c.get(Calendar.MINUTE));
        assertEquals(59, c.get(Calendar.SECOND));
    }

    @Test
    public void testStream() {
        DateStream stream = Dates.stream();
        assertNotNull(stream);
        assertNotNull(stream.date());
    }

    @Test
    public void testStreamWithDate() {
        Date d = Dates.build(2020, 1, 1);
        DateStream stream = Dates.stream(d);
        assertNotNull(stream);
        assertEquals(2020, stream.getYear());
    }
}
