package com.orion.utils.time;

import com.orion.lang.collect.ConcurrentReferenceHashMap;
import com.orion.utils.Strings;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Map;

/**
 * jdk8 时间工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/11 16:51
 */
public class Dates8 extends BaseDates {

    private static final Map<String, DateTimeFormatter> DATE_TIME_FORMAT_CONTAINER = new ConcurrentReferenceHashMap<>(16, ConcurrentReferenceHashMap.ReferenceType.SOFT);

    private Dates8() {
    }

    static {
        DATE_TIME_FORMAT_CONTAINER.put(YMD, DateTimeFormatter.ofPattern(YMD));
        DATE_TIME_FORMAT_CONTAINER.put(YMDHMS, DateTimeFormatter.ofPattern(YMDHMS));
    }

    /**
     * 将对象转化为 LocalDateTime
     *
     * @param o 对象
     * @return LocalDateTime
     */
    public static LocalDateTime localDateTime(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof LocalDateTime) {
            return (LocalDateTime) o;
        }
        Date date = Dates.date(o);
        if (date == null) {
            return null;
        }
        return localDateTime(date);
    }

    /**
     * 将对象转化为 LocalDate
     *
     * @param o 对象
     * @return LocalDate
     */
    public static LocalDate localDate(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof LocalDate) {
            return (LocalDate) o;
        }
        Date date = Dates.date(o);
        if (date == null) {
            return null;
        }
        return localDate(date);
    }

    public static Instant instant() {
        return Instant.now();
    }

    public static Instant instant(Date date) {
        return Instant.ofEpochMilli(date.getTime());
    }

    /**
     * 获取 Instant
     *
     * @param ms 时间戳毫秒
     * @return Instant
     */
    public static Instant instant(long ms) {
        // ms是时间戳毫秒
        return Instant.ofEpochMilli(ms);
    }

    public static Instant instant(LocalDate d) {
        return d.atStartOfDay().toInstant(DEFAULT_ZERO_ZONE_OFFSET);
    }

    public static Instant instant(LocalDate d, ZoneOffset offset) {
        return d.atStartOfDay().toInstant(offset);
    }

    public static Instant instant(LocalDateTime dt) {
        return dt.toInstant(DEFAULT_ZONE_OFFSET);
    }

    public static Instant instant(LocalDateTime dt, ZoneOffset offset) {
        return dt.toInstant(offset);
    }

    public static long timestamp() {
        return System.currentTimeMillis() / MILLI;
    }

    public static long timestamp(Date date) {
        return date.getTime() / MILLI;
    }

    public static long timestamp(Instant instant) {
        return instant.getEpochSecond();
    }

    public static long timestamp(LocalDate d) {
        return d.atStartOfDay().toInstant(DEFAULT_ZERO_ZONE_OFFSET).getEpochSecond();
    }

    public static long timestamp(LocalDate d, ZoneOffset offset) {
        return d.atStartOfDay().toInstant(offset).toEpochMilli() / MILLI;
    }

    public static long timestamp(LocalDateTime d) {
        return d.toEpochSecond(DEFAULT_ZONE_OFFSET);
    }

    public static long timestamp(LocalDateTime d, ZoneOffset offset) {
        return d.toEpochSecond(offset);
    }

    public static Date date(Instant instant) {
        return Date.from(instant);
    }

    public static Date date(LocalDate d) {
        return Date.from(d.atStartOfDay().toInstant(DEFAULT_ZONE_OFFSET));
    }

    public static Date date(LocalDate d, ZoneOffset offset) {
        return Date.from(d.atStartOfDay().toInstant(offset));
    }

    public static Date date(LocalDateTime d) {
        return Date.from(d.atZone(DEFAULT_ZONE_OFFSET).toInstant());
    }

    public static Date date(LocalDateTime d, ZoneOffset offset) {
        return Date.from(d.atZone(offset).toInstant());
    }

    public static LocalDate localDate() {
        return LocalDate.now();
    }

    public static LocalDate localDate(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }

    public static LocalDate localDate(long ms) {
        return LocalDateTime.ofEpochSecond(ms / MILLI, 0, DEFAULT_ZONE_OFFSET).toLocalDate();
    }

    public static LocalDate localDate(long ms, ZoneOffset offset) {
        return LocalDateTime.ofEpochSecond(ms / MILLI, 0, offset).toLocalDate();
    }

    public static LocalDate localDate(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), DEFAULT_ZONE_ID).toLocalDate();
    }

    public static LocalDate localDate(Date date, ZoneId zoneId) {
        return LocalDateTime.ofInstant(date.toInstant(), zoneId).toLocalDate();
    }

    public static LocalDate localDate(Instant instant) {
        return LocalDateTime.ofInstant(instant, DEFAULT_ZONE_ID).toLocalDate();
    }

    public static LocalDate localDate(Instant instant, ZoneId zoneId) {
        return LocalDateTime.ofInstant(instant, zoneId).toLocalDate();
    }

    public static LocalDate localDate(LocalDateTime d) {
        return d.toLocalDate();
    }

    public static LocalTime localTime() {
        return LocalTime.now();
    }

    public static LocalTime localTime(int h, int m, int s) {
        return LocalTime.of(h, m, s);
    }

    public static LocalTime localTime(int h, int m, int s, int nano) {
        return LocalTime.of(h, m, s, nano);
    }

    public static LocalTime localTime(long ms, int nano) {
        return LocalDateTime.ofEpochSecond(ms / MILLI, nano, DEFAULT_ZONE_OFFSET).toLocalTime();
    }

    public static LocalTime localTime(long ms, int nano, ZoneOffset offset) {
        return LocalDateTime.ofEpochSecond(ms / MILLI, nano, offset).toLocalTime();
    }

    public static LocalTime localTime(long ms) {
        return LocalDateTime.ofEpochSecond(ms / MILLI, 0, DEFAULT_ZONE_OFFSET).toLocalTime();
    }

    public static LocalTime localTime(long ms, ZoneOffset offset) {
        return LocalDateTime.ofEpochSecond(ms / MILLI, 0, offset).toLocalTime();
    }

    public static LocalTime localTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), DEFAULT_ZONE_ID).toLocalTime();
    }

    public static LocalTime localTime(Date date, ZoneId zoneId) {
        return LocalDateTime.ofInstant(date.toInstant(), zoneId).toLocalTime();
    }

    public static LocalTime localTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, DEFAULT_ZONE_ID).toLocalTime();
    }

    public static LocalTime localTime(Instant instant, ZoneId zoneId) {
        return LocalDateTime.ofInstant(instant, zoneId).toLocalTime();
    }

    public static LocalTime localTime(LocalDateTime d) {
        return d.toLocalTime();
    }

    public static LocalDateTime localDateTime() {
        return LocalDateTime.now();
    }

    public static LocalDateTime localDateTime(int year, int month, int day) {
        return LocalDateTime.of(year, month, day, 0, 0, 0);
    }

    public static LocalDateTime localDateTime(int year, int month, int day, int h, int m, int s) {
        return LocalDateTime.of(year, month, day, h, m, s);
    }

    public static LocalDateTime localDateTime(int year, int month, int day, int h, int m, int s, int ms) {
        return LocalDateTime.of(year, month, day, h, m, s, ms);
    }

    public static LocalDateTime localDateTime(long ms) {
        return LocalDateTime.ofEpochSecond(ms / MILLI, 0, DEFAULT_ZONE_OFFSET);
    }

    public static LocalDateTime localDateTime(long ms, ZoneOffset offset) {
        return LocalDateTime.ofEpochSecond(ms / MILLI, 0, offset);
    }

    public static LocalDateTime localDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), DEFAULT_ZONE_ID);
    }

    public static LocalDateTime localDateTime(Date date, ZoneId zoneId) {
        return LocalDateTime.ofInstant(date.toInstant(), zoneId);
    }

    public static LocalDateTime localDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, DEFAULT_ZONE_ID);
    }

    public static LocalDateTime localDateTime(Instant instant, ZoneId zoneId) {
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    public static LocalDateTime localDateTime(LocalDate d) {
        return d.atStartOfDay();
    }

    public static LocalDateTime localDateTime(LocalDate d, LocalTime t) {
        return LocalDateTime.of(d, t);
    }

    public static LocalDateTime clearHms(LocalDateTime d) {
        return LocalDateTime.of(d.toLocalDate(), LocalTime.of(0, 0, 0, 0));
    }

    public static LocalDateTime clearDay(LocalDateTime d) {
        return d.withDayOfMonth(1);
    }

    public static LocalDateTime clearDayHms(LocalDateTime d) {
        return LocalDateTime.of(d.toLocalDate().withDayOfMonth(1), LocalTime.of(0, 0, 0, 0));
    }

    public static String format(TemporalAccessor d) {
        return format(d, YMDHMS);
    }

    /**
     * 格式化时间
     *
     * @param d       时间
     * @param pattern pattern
     * @return 格式化
     */
    public static String format(TemporalAccessor d, String pattern) {
        try {
            DateTimeFormatter formatter = DATE_TIME_FORMAT_CONTAINER.get(pattern);
            if (formatter == null) {
                formatter = DateTimeFormatter.ofPattern(pattern);
                DATE_TIME_FORMAT_CONTAINER.put(pattern, formatter);
            }
            return formatter.format(d);
        } catch (Exception e) {
            return null;
        }
    }

    public static TemporalAccessor parse(String d) {
        TemporalAccessor r = null;
        String pattern = null;
        if (d.contains("-")) {
            for (String s : PARSE_PATTERN_GROUP1) {
                if (d.length() == s.length()) {
                    pattern = s;
                    break;
                }
            }
            if (pattern != null) {
                return parse(d, pattern);
            }
        } else if (d.contains("/")) {
            for (String s : PARSE_PATTERN_GROUP2) {
                if (d.length() == s.length()) {
                    pattern = s;
                    break;
                }
            }
            if (pattern != null) {
                return parse(d, pattern);
            }
        } else if (Strings.isNumber(d)) {
            for (String s : PARSE_PATTERN_GROUP3) {
                if (d.length() == s.length()) {
                    pattern = s;
                    break;
                }
            }
            if (pattern != null) {
                return parse(d, pattern);
            }
        }
        return r;
    }

    /**
     * 日期转化
     *
     * @param d       ignore
     * @param pattern pattern
     * @return ignore
     */
    public static TemporalAccessor parse(String d, String pattern) {
        try {
            DateTimeFormatter formatter = DATE_TIME_FORMAT_CONTAINER.get(pattern);
            if (formatter == null) {
                formatter = DateTimeFormatter.ofPattern(pattern);
                DATE_TIME_FORMAT_CONTAINER.put(pattern, formatter);
            }
            return formatter.parse(d);
        } catch (Exception e) {
            return null;
        }
    }

}
