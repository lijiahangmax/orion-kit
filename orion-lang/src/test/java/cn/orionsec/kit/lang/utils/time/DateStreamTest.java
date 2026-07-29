package cn.orionsec.kit.lang.utils.time;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * DateStream 单元测试
 */
public class DateStreamTest {

    @Test
    public void testCreate() {
        Date d = Dates.build(2020, 6, 15, 10, 30, 0);
        DateStream stream = new DateStream(d);
        assertEquals(2020, stream.getYear());
        assertEquals(6, stream.getMonth());
        assertEquals(15, stream.getDay());
        assertEquals(10, stream.getHour());
        assertEquals(30, stream.getMinute());
        assertEquals(0, stream.getSecond());
    }

    @Test
    public void testAddYear() {
        Date d = Dates.build(2020, 6, 15);
        DateStream stream = new DateStream(d);
        stream.addYear(1);
        assertEquals(2021, stream.getYear());
    }

    @Test
    public void testSubYear() {
        Date d = Dates.build(2020, 6, 15);
        DateStream stream = new DateStream(d);
        stream.subYear(2);
        assertEquals(2018, stream.getYear());
    }

    @Test
    public void testSetYear() {
        Date d = Dates.build(2020, 6, 15);
        DateStream stream = new DateStream(d);
        stream.setYear(2025);
        assertEquals(2025, stream.getYear());
    }

    @Test
    public void testAddMonth() {
        Date d = Dates.build(2020, 6, 15);
        DateStream stream = new DateStream(d);
        stream.addMonth(3);
        assertEquals(9, stream.getMonth());
    }

    @Test
    public void testSubMonth() {
        Date d = Dates.build(2020, 6, 15);
        DateStream stream = new DateStream(d);
        stream.subMonth(2);
        assertEquals(4, stream.getMonth());
    }

    @Test
    public void testSetMonth() {
        Date d = Dates.build(2020, 6, 15);
        DateStream stream = new DateStream(d);
        stream.setMonth(12);
        assertEquals(12, stream.getMonth());
    }

    @Test
    public void testAddDay() {
        Date d = Dates.build(2020, 6, 15);
        DateStream stream = new DateStream(d);
        stream.addDay(5);
        assertEquals(20, stream.getDay());
    }

    @Test
    public void testSubDay() {
        Date d = Dates.build(2020, 6, 15);
        DateStream stream = new DateStream(d);
        stream.subDay(5);
        assertEquals(10, stream.getDay());
    }

    @Test
    public void testSetDay() {
        Date d = Dates.build(2020, 6, 15);
        DateStream stream = new DateStream(d);
        stream.setDay(28);
        assertEquals(28, stream.getDay());
    }

    @Test
    public void testAddHour() {
        Date d = Dates.build(2020, 6, 15, 10, 0, 0);
        DateStream stream = new DateStream(d);
        stream.addHour(3);
        assertEquals(13, stream.getHour());
    }

    @Test
    public void testSubHour() {
        Date d = Dates.build(2020, 6, 15, 10, 0, 0);
        DateStream stream = new DateStream(d);
        stream.subHour(2);
        assertEquals(8, stream.getHour());
    }

    @Test
    public void testSetHour() {
        Date d = Dates.build(2020, 6, 15, 10, 0, 0);
        DateStream stream = new DateStream(d);
        stream.setHour(23);
        assertEquals(23, stream.getHour());
    }

    @Test
    public void testAddMinute() {
        Date d = Dates.build(2020, 6, 15, 10, 30, 0);
        DateStream stream = new DateStream(d);
        stream.addMinute(15);
        assertEquals(45, stream.getMinute());
    }

    @Test
    public void testSubMinute() {
        Date d = Dates.build(2020, 6, 15, 10, 30, 0);
        DateStream stream = new DateStream(d);
        stream.subMinute(10);
        assertEquals(20, stream.getMinute());
    }

    @Test
    public void testAddSecond() {
        Date d = Dates.build(2020, 6, 15, 10, 30, 0);
        DateStream stream = new DateStream(d);
        stream.addSecond(45);
        assertEquals(45, stream.getSecond());
    }

    @Test
    public void testSubSecond() {
        Date d = Dates.build(2020, 6, 15, 10, 30, 30);
        DateStream stream = new DateStream(d);
        stream.subSecond(10);
        assertEquals(20, stream.getSecond());
    }

    @Test
    public void testAddMilliSecond() {
        Date d = Dates.build(2020, 6, 15, 10, 30, 0, 100);
        DateStream stream = new DateStream(d);
        stream.setMilliSecond(0);
        stream.addMilliSecond(500);
        assertEquals(500, stream.getMilliSecond());
    }

    @Test
    public void testClearHms() {
        Date d = Dates.build(2020, 6, 15, 14, 30, 45);
        DateStream stream = new DateStream(d);
        stream.clearHms();
        assertEquals(0, stream.getHour());
        assertEquals(0, stream.getMinute());
        assertEquals(0, stream.getSecond());
    }

    @Test
    public void testDayEnd() {
        Date d = Dates.build(2020, 6, 15, 10, 0, 0);
        DateStream stream = new DateStream(d);
        stream.dayEnd();
        assertEquals(23, stream.getHour());
        assertEquals(59, stream.getMinute());
        assertEquals(59, stream.getSecond());
    }

    @Test
    public void testMonthFirstDay() {
        Date d = Dates.build(2020, 6, 15);
        DateStream stream = new DateStream(d);
        stream.monthFirstDay();
        assertEquals(1, stream.getDay());
    }

    @Test
    public void testMonthFirstDayHms() {
        Date d = Dates.build(2020, 6, 15, 14, 30, 0);
        DateStream stream = new DateStream(d);
        stream.monthFirstDayHms();
        assertEquals(1, stream.getDay());
        assertEquals(0, stream.getHour());
        assertEquals(0, stream.getMinute());
    }

    @Test
    public void testMonthLastDay() {
        Date d = Dates.build(2020, 6, 15);
        DateStream stream = new DateStream(d);
        stream.monthLastDay();
        assertEquals(30, stream.getDay());
    }

    @Test
    public void testMonthLastDayHms() {
        Date d = Dates.build(2020, 6, 15);
        DateStream stream = new DateStream(d);
        stream.monthLastDayHms();
        assertEquals(30, stream.getDay());
        assertEquals(23, stream.getHour());
        assertEquals(59, stream.getMinute());
    }

    @Test
    public void testFormat() {
        Date d = Dates.build(2020, 6, 15, 10, 30, 0);
        DateStream stream = new DateStream(d);
        assertEquals("2020-06-15 10:30:00", stream.format());
    }

    @Test
    public void testFormatWithPattern() {
        Date d = Dates.build(2020, 6, 15, 10, 30, 0);
        DateStream stream = new DateStream(d);
        assertEquals("2020-06-15", stream.format("yyyy-MM-dd"));
    }

    @Test
    public void testChaining() {
        Date d = Dates.build(2020, 1, 1, 0, 0, 0);
        DateStream stream = new DateStream(d);
        String result = stream.addYear(1).addMonth(2).addDay(3).format("yyyy-MM-dd");
        assertEquals("2021-03-04", result);
    }

    @Test
    public void testInRange() {
        // DateStream.inRange(start, end) calls DateRanges.inRange(start, end, c.getTime())
        // which checks if start is in range [end, c.getTime()]
        Date start = Dates.build(2020, 6, 1);
        Date end = Dates.build(2020, 1, 1);
        Date d = Dates.build(2020, 12, 31);
        DateStream stream = new DateStream(d);
        assertTrue(stream.inRange(start, end));
    }

    @Test
    public void testNotInRange() {
        // checks if start is NOT in range [end, c.getTime()]
        Date start = Dates.build(2020, 1, 1);
        Date end = Dates.build(2020, 6, 1);
        Date d = Dates.build(2020, 3, 15);
        DateStream stream = new DateStream(d);
        assertTrue(stream.notInRange(start, end));
    }

    @Test
    public void testBefore() {
        Date d = Dates.build(2020, 1, 1);
        Date other = Dates.build(2020, 6, 1);
        DateStream stream = new DateStream(d);
        assertTrue(stream.before(other));
    }

    @Test
    public void testAfter() {
        Date d = Dates.build(2020, 6, 1);
        Date other = Dates.build(2020, 1, 1);
        DateStream stream = new DateStream(d);
        assertTrue(stream.after(other));
    }

    @Test
    public void testIsLeapYear() {
        Date d = Dates.build(2020, 6, 15);
        DateStream stream = new DateStream(d);
        assertTrue(stream.isLeapYear());

        Date d2 = Dates.build(2019, 6, 15);
        DateStream stream2 = new DateStream(d2);
        assertFalse(stream2.isLeapYear());
    }

    @Test
    public void testGetMonthLastDay() {
        Date d = Dates.build(2020, 2, 15);
        DateStream stream = new DateStream(d);
        assertEquals(29, stream.getMonthLastDay());
    }

    @Test
    public void testOf() {
        DateStream stream = DateStream.of("2020-06-15 10:30:00");
        assertEquals(2020, stream.getYear());
        assertEquals(6, stream.getMonth());
        assertEquals(15, stream.getDay());
    }

    @Test
    public void testGet() {
        Date d = Dates.build(2020, 6, 15);
        DateStream stream = new DateStream(d);
        Date result = stream.get();
        assertNotNull(result);
    }

    @Test
    public void testDate() {
        Date d = Dates.build(2020, 6, 15);
        DateStream stream = new DateStream(d);
        assertEquals(stream.get(), stream.date());
    }

    @Test
    public void testCalendar() {
        Date d = Dates.build(2020, 6, 15);
        DateStream stream = new DateStream(d);
        Calendar c = stream.calendar();
        assertNotNull(c);
        assertEquals(2020, c.get(Calendar.YEAR));
    }

    @Test
    public void testInFuture() {
        Date d = Dates.build(2099, 1, 1);
        DateStream stream = new DateStream(d);
        assertTrue(stream.inFuture());

        Date d2 = Dates.build(2000, 1, 1);
        DateStream stream2 = new DateStream(d2);
        assertFalse(stream2.inFuture());
    }
}
