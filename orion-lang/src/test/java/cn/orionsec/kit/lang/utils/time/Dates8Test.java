package cn.orionsec.kit.lang.utils.time;

import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Dates8 单元测试
 */
public class Dates8Test {

    @Test
    public void testLocalDateTime() {
        LocalDateTime ldt = Dates8.localDateTime(2020, 6, 15, 10, 30, 0);
        assertEquals(2020, ldt.getYear());
        assertEquals(6, ldt.getMonthValue());
        assertEquals(15, ldt.getDayOfMonth());
        assertEquals(10, ldt.getHour());
        assertEquals(30, ldt.getMinute());
        assertEquals(0, ldt.getSecond());
    }

    @Test
    public void testLocalDate() {
        LocalDate ld = Dates8.localDate(2020, 6, 15);
        assertEquals(2020, ld.getYear());
        assertEquals(6, ld.getMonthValue());
        assertEquals(15, ld.getDayOfMonth());
    }

    @Test
    public void testLocalTime() {
        LocalTime lt = Dates8.localTime(14, 30, 45);
        assertEquals(14, lt.getHour());
        assertEquals(30, lt.getMinute());
        assertEquals(45, lt.getSecond());
    }

    @Test
    public void testLocalTimeWithNano() {
        LocalTime lt = Dates8.localTime(14, 30, 45, 123456789);
        assertEquals(14, lt.getHour());
        assertEquals(30, lt.getMinute());
        assertEquals(45, lt.getSecond());
        assertEquals(123456789, lt.getNano());
    }

    @Test
    public void testFormat() {
        LocalDateTime ldt = Dates8.localDateTime(2020, 6, 15, 10, 30, 0);
        String formatted = Dates8.format(ldt);
        assertEquals("2020-06-15 10:30:00", formatted);
    }

    @Test
    public void testFormatWithPattern() {
        LocalDateTime ldt = Dates8.localDateTime(2020, 6, 15, 10, 30, 0);
        assertEquals("2020-06-15", Dates8.format(ldt, "yyyy-MM-dd"));
        assertEquals("20200615", Dates8.format(ldt, "yyyyMMdd"));
    }

    @Test
    public void testFormatLocalDate() {
        LocalDate ld = Dates8.localDate(2020, 6, 15);
        assertEquals("2020-06-15", Dates8.format(ld, "yyyy-MM-dd"));
    }

    @Test
    public void testParse() {
        TemporalAccessor ta = Dates8.parse("2020-06-15 10:30:00");
        assertNotNull(ta);
    }

    @Test
    public void testParseWithPattern() {
        TemporalAccessor ta = Dates8.parse("2020-06-15", "yyyy-MM-dd");
        assertNotNull(ta);
    }

    @Test
    public void testParseSlash() {
        TemporalAccessor ta = Dates8.parse("2020/06/15");
        assertNotNull(ta);
    }

    @Test
    public void testParseNumber() {
        TemporalAccessor ta = Dates8.parse("20200615");
        assertNotNull(ta);
    }

    @Test
    public void testDateFromLocalDateTime() {
        LocalDateTime ldt = Dates8.localDateTime(2020, 6, 15, 10, 30, 0);
        Date d = Dates8.date(ldt);
        assertNotNull(d);
        String formatted = Dates.format(d, "yyyy-MM-dd HH:mm:ss");
        assertEquals("2020-06-15 10:30:00", formatted);
    }

    @Test
    public void testDateFromLocalDate() {
        LocalDate ld = Dates8.localDate(2020, 6, 15);
        Date d = Dates8.date(ld);
        assertNotNull(d);
    }

    @Test
    public void testDateFromInstant() {
        Instant instant = Instant.ofEpochMilli(1592188200000L);
        Date d = Dates8.date(instant);
        assertNotNull(d);
        assertEquals(1592188200000L, d.getTime());
    }

    @Test
    public void testLocalDateTimeFromDate() {
        Date d = Dates.build(2020, 6, 15, 10, 30, 0);
        LocalDateTime ldt = Dates8.localDateTime(d);
        assertEquals(2020, ldt.getYear());
        assertEquals(6, ldt.getMonthValue());
        assertEquals(15, ldt.getDayOfMonth());
        assertEquals(10, ldt.getHour());
        assertEquals(30, ldt.getMinute());
    }

    @Test
    public void testLocalDateFromDate() {
        Date d = Dates.build(2020, 6, 15);
        LocalDate ld = Dates8.localDate(d);
        assertEquals(2020, ld.getYear());
        assertEquals(6, ld.getMonthValue());
        assertEquals(15, ld.getDayOfMonth());
    }

    @Test
    public void testLocalDateFromLocalDateTime() {
        LocalDateTime ldt = Dates8.localDateTime(2020, 6, 15, 10, 30, 0);
        LocalDate ld = Dates8.localDate(ldt);
        assertEquals(2020, ld.getYear());
        assertEquals(6, ld.getMonthValue());
        assertEquals(15, ld.getDayOfMonth());
    }

    @Test
    public void testLocalTimeFromLocalDateTime() {
        LocalDateTime ldt = Dates8.localDateTime(2020, 6, 15, 10, 30, 45);
        LocalTime lt = Dates8.localTime(ldt);
        assertEquals(10, lt.getHour());
        assertEquals(30, lt.getMinute());
        assertEquals(45, lt.getSecond());
    }

    @Test
    public void testInstant() {
        Instant i = Dates8.instant();
        assertNotNull(i);
    }

    @Test
    public void testInstantFromDate() {
        Date d = new Date(1592188200000L);
        Instant i = Dates8.instant(d);
        assertEquals(1592188200000L, i.toEpochMilli());
    }

    @Test
    public void testInstantFromMs() {
        Instant i = Dates8.instant(1592188200000L);
        assertEquals(1592188200000L, i.toEpochMilli());
    }

    @Test
    public void testTimestamp() {
        LocalDateTime ldt = Dates8.localDateTime(2020, 6, 15, 10, 30, 0);
        long ts = Dates8.timestamp(ldt);
        assertTrue(ts > 0);
    }

    @Test
    public void testClearHms() {
        LocalDateTime ldt = Dates8.localDateTime(2020, 6, 15, 10, 30, 45);
        LocalDateTime cleared = Dates8.clearHms(ldt);
        assertEquals(0, cleared.getHour());
        assertEquals(0, cleared.getMinute());
        assertEquals(0, cleared.getSecond());
        assertEquals(15, cleared.getDayOfMonth());
    }

    @Test
    public void testClearDay() {
        LocalDateTime ldt = Dates8.localDateTime(2020, 6, 15, 10, 30, 45);
        LocalDateTime cleared = Dates8.clearDay(ldt);
        assertEquals(1, cleared.getDayOfMonth());
        assertEquals(10, cleared.getHour());
    }

    @Test
    public void testClearDayHms() {
        LocalDateTime ldt = Dates8.localDateTime(2020, 6, 15, 10, 30, 45);
        LocalDateTime cleared = Dates8.clearDayHms(ldt);
        assertEquals(1, cleared.getDayOfMonth());
        assertEquals(0, cleared.getHour());
        assertEquals(0, cleared.getMinute());
        assertEquals(0, cleared.getSecond());
    }

    @Test
    public void testLocalDateTimeFromObject() {
        LocalDateTime ldt = Dates8.localDateTime((Object) "2020-06-15 10:30:00");
        assertNotNull(ldt);
        assertEquals(2020, ldt.getYear());
        assertEquals(6, ldt.getMonthValue());
        assertEquals(15, ldt.getDayOfMonth());
    }

    @Test
    public void testLocalDateTimeFromObjectNull() {
        assertNull(Dates8.localDateTime((Object) null));
    }

    @Test
    public void testLocalDateFromObject() {
        LocalDate ld = Dates8.localDate((Object) "2020-06-15");
        assertNotNull(ld);
        assertEquals(2020, ld.getYear());
        assertEquals(6, ld.getMonthValue());
        assertEquals(15, ld.getDayOfMonth());
    }

    @Test
    public void testLocalDateFromObjectNull() {
        assertNull(Dates8.localDate((Object) null));
    }

    @Test
    public void testIsExpired() {
        LocalDateTime past = Dates8.localDateTime(2000, 1, 1, 0, 0, 0);
        LocalDateTime future = Dates8.localDateTime(2099, 1, 1, 0, 0, 0);
        assertTrue(Dates8.isExpired(past));
        assertFalse(Dates8.isExpired(future));
    }

    @Test
    public void testLocalDateTimeNow() {
        LocalDateTime now = Dates8.localDateTime();
        assertNotNull(now);
    }

    @Test
    public void testLocalDateNow() {
        LocalDate now = Dates8.localDate();
        assertNotNull(now);
    }

    @Test
    public void testLocalTimeNow() {
        LocalTime now = Dates8.localTime();
        assertNotNull(now);
    }

    @Test
    public void testLocalDateTimeFromLocalDate() {
        LocalDate ld = Dates8.localDate(2020, 6, 15);
        LocalDateTime ldt = Dates8.localDateTime(ld);
        assertEquals(2020, ldt.getYear());
        assertEquals(6, ldt.getMonthValue());
        assertEquals(15, ldt.getDayOfMonth());
        assertEquals(0, ldt.getHour());
    }

    @Test
    public void testLocalDateTimeFromLocalDateAndTime() {
        LocalDate ld = Dates8.localDate(2020, 6, 15);
        LocalTime lt = Dates8.localTime(10, 30, 0);
        LocalDateTime ldt = Dates8.localDateTime(ld, lt);
        assertEquals(2020, ldt.getYear());
        assertEquals(10, ldt.getHour());
        assertEquals(30, ldt.getMinute());
    }
}
