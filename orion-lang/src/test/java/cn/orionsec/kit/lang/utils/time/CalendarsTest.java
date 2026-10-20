package cn.orionsec.kit.lang.utils.time;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Calendars 单元测试
 */
public class CalendarsTest {

    @Test
    public void testAddYear() {
        Date d = build(2026, 10, 20, 10, 30, 45, 123);
        assertDate(Calendars.addYear(d, 1), 2027, 10, 20, 10, 30, 45, 123);
        assertDate(Calendars.addYear(d, 1, null), 2027, 10, 20, 10, 30, 45, 123);
        assertDate(Calendars.addYear(d, -1), 2025, 10, 20, 10, 30, 45, 123);
        // 不修改原时间
        assertDate(d, 2026, 10, 20, 10, 30, 45, 123);
    }

    @Test
    public void testAddYearStart() {
        Date d = build(2026, 10, 20, 10, 30, 45, 123);
        assertDate(Calendars.addYear(d, 1, Calendars.START), 2027, 1, 1, 0, 0, 0, 0);
    }

    @Test
    public void testAddYearEnd() {
        Date d = build(2026, 10, 20, 10, 30, 45, 123);
        assertDate(Calendars.addYear(d, 1, Calendars.END), 2027, 12, 31, 23, 59, 59, 999);
    }

    @Test
    public void testAddYearCurrent() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        Calendar c = Calendar.getInstance();
        c.setTime(Calendars.addYear(1));
        assertEquals(year + 1, c.get(Calendar.YEAR));
        c.setTime(Calendars.addYear(1, Calendars.START));
        assertEquals(year + 1, c.get(Calendar.YEAR));
        assertEquals(Calendar.JANUARY, c.get(Calendar.MONTH));
        assertEquals(1, c.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, c.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, c.get(Calendar.MINUTE));
        assertEquals(0, c.get(Calendar.SECOND));
        assertEquals(0, c.get(Calendar.MILLISECOND));
        c.setTime(Calendars.addYear(1, Calendars.END));
        assertEquals(year + 1, c.get(Calendar.YEAR));
        assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
        assertEquals(31, c.get(Calendar.DAY_OF_MONTH));
        assertEquals(23, c.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, c.get(Calendar.MINUTE));
        assertEquals(59, c.get(Calendar.SECOND));
        assertEquals(999, c.get(Calendar.MILLISECOND));
    }

    @Test
    public void testAddMonthStart() {
        Date d = build(2026, 10, 20, 10, 30, 45, 123);
        assertDate(Calendars.addMonth(d, 1, Calendars.START), 2026, 11, 1, 0, 0, 0, 0);
        assertDate(Calendars.addMonth(d, -10, Calendars.START), 2025, 12, 1, 0, 0, 0, 0);
    }

    @Test
    public void testAddMonthEnd() {
        Date d = build(2026, 10, 20, 10, 30, 45, 123);
        assertDate(Calendars.addMonth(d, 1, Calendars.END), 2026, 11, 30, 23, 59, 59, 999);
        // 闰年 2 月
        assertDate(Calendars.addMonth(build(2028, 1, 31, 10, 30, 45, 123), 1, Calendars.END), 2028, 2, 29, 23, 59, 59, 999);
    }

    @Test
    public void testAddDayStart() {
        Date d = build(2026, 10, 31, 10, 30, 45, 123);
        assertDate(Calendars.addDay(d, 1, Calendars.START), 2026, 11, 1, 0, 0, 0, 0);
    }

    @Test
    public void testAddDayEnd() {
        Date d = build(2026, 10, 20, 10, 30, 45, 123);
        assertDate(Calendars.addDay(d, 1, Calendars.END), 2026, 10, 21, 23, 59, 59, 999);
        assertDate(Calendars.addDay(d, -20, Calendars.END), 2026, 9, 30, 23, 59, 59, 999);
    }

    @Test
    public void testAddHourStart() {
        Date d = build(2026, 10, 20, 10, 30, 45, 123);
        assertDate(Calendars.addHour(d, 2, Calendars.START), 2026, 10, 20, 12, 0, 0, 0);
        assertDate(Calendars.addHour(d, -11, Calendars.START), 2026, 10, 19, 23, 0, 0, 0);
    }

    @Test
    public void testAddHourEnd() {
        Date d = build(2026, 10, 20, 10, 30, 45, 123);
        assertDate(Calendars.addHour(d, 2, Calendars.END), 2026, 10, 20, 12, 59, 59, 999);
    }

    @Test
    public void testAddMinuteStart() {
        Date d = build(2026, 10, 20, 10, 30, 45, 123);
        assertDate(Calendars.addMinute(d, 20, Calendars.START), 2026, 10, 20, 10, 50, 0, 0);
    }

    @Test
    public void testAddMinuteEnd() {
        Date d = build(2026, 10, 20, 10, 30, 45, 123);
        assertDate(Calendars.addMinute(d, 20, Calendars.END), 2026, 10, 20, 10, 50, 59, 999);
    }

    @Test
    public void testAddSecondStart() {
        Date d = build(2026, 10, 20, 10, 30, 45, 123);
        assertDate(Calendars.addSecond(d, 30, Calendars.START), 2026, 10, 20, 10, 31, 15, 0);
    }

    @Test
    public void testAddSecondEnd() {
        Date d = build(2026, 10, 20, 10, 30, 45, 123);
        assertDate(Calendars.addSecond(d, 30, Calendars.END), 2026, 10, 20, 10, 31, 15, 999);
    }

    @Test
    public void testAddMilliSecond() {
        Date d = build(2026, 10, 20, 10, 30, 45, 123);
        assertDate(Calendars.addMilliSecond(d, 500), 2026, 10, 20, 10, 30, 45, 623);
        assertDate(Calendars.addMilliSecond(d, 1000), 2026, 10, 20, 10, 30, 46, 123);
        assertDate(Calendars.addMilliSecond(d, -200), 2026, 10, 20, 10, 30, 44, 923);
        // 不修改原时间
        assertDate(d, 2026, 10, 20, 10, 30, 45, 123);
    }

    @Test
    public void testAddMilliSecondCurrent() {
        long before = System.currentTimeMillis();
        long result = Calendars.addMilliSecond(1000).getTime();
        long after = System.currentTimeMillis();
        assertTrue(result >= before + 1000 && result <= after + 1000);
    }

    @Test
    public void testNullDate() {
        assertNull(Calendars.addYear(null, 1));
        assertNull(Calendars.addYear((Date) null, 1, Calendars.START));
        assertNull(Calendars.addMonth(null, 1, Calendars.END));
        assertNull(Calendars.addDay(null, 1));
        assertNull(Calendars.addHour(null, 1, Calendars.START));
        assertNull(Calendars.addMinute(null, 1));
        assertNull(Calendars.addSecond(null, 1, Calendars.END));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFlag() {
        Calendars.addYear(new Date(), 1, 3);
    }

    private static Date build(int year, int month, int day, int hour, int minute, int second, int milli) {
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day, hour, minute, second);
        c.set(Calendar.MILLISECOND, milli);
        return c.getTime();
    }

    private static void assertDate(Date d, int year, int month, int day, int hour, int minute, int second, int milli) {
        assertNotNull(d);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        assertEquals(year, c.get(Calendar.YEAR));
        assertEquals(month - 1, c.get(Calendar.MONTH));
        assertEquals(day, c.get(Calendar.DAY_OF_MONTH));
        assertEquals(hour, c.get(Calendar.HOUR_OF_DAY));
        assertEquals(minute, c.get(Calendar.MINUTE));
        assertEquals(second, c.get(Calendar.SECOND));
        assertEquals(milli, c.get(Calendar.MILLISECOND));
    }

}
