package com.orion.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日期时间工具类
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/10/9 10:38
 */
public class Dates {

    public static final String WORLD = "EEE MMM dd HH:mm:ss z yyyy";
    public static final String YMD = "yyyy-MM-dd";
    public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String YMDHMSS = "yyyy-MM-dd HH:mm:ss SSS";
    public static final String YMD1 = "yyyy/MM/dd";
    public static final String YMDHMS1 = "yyyy/MM/dd HH/mm/ss";
    public static final String YMDHMSS1 = "yyyy/MM/dd HH/mm/ss SSS";
    private static final String[] PARSE_PATTERN = {YMDHMS, YMD, YMDHMSS};
    private static final String[] PARSE_PATTERN1 = {YMDHMS1, YMD1, YMDHMSS1};

    private static final long WEEK_STAMP = 604800000L;
    private static final long YEAR_STAMP = 31536000000L;
    private static final long MONTH_STAMP = 2592000000L;
    private static final long DAY_STAMP = 86400000L;
    private static final long HOUR_STAMP = 3600000L;
    private static final long MINUTE_STAMP = 60000L;
    private static final long MILLI = 1000L;

    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();
    private static final ZoneOffset DEFAULT_ZONE_OFFSET = ZoneOffset.of("+8");
    private static final ZoneOffset DEFAULT_ZERO_ZONE_OFFSET = ZoneOffset.ofHours(0);

    private static final Map<String, DateTimeFormatter> DATE_TIME_FORMAT_CONTAINER = new ConcurrentHashMap<>();

    private Dates() {
    }

    static {
        DATE_TIME_FORMAT_CONTAINER.put(YMD, DateTimeFormatter.ofPattern(YMD));
        DATE_TIME_FORMAT_CONTAINER.put(YMDHMS, DateTimeFormatter.ofPattern(YMDHMS));
    }

    /**
     * 将对象转换为时间
     *
     * @param o 对象
     * @return 时间
     */
    public static Date date(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Long) {
            long l = (long) o;
            if (isMilli(l)) {
                return date(l);
            } else {
                return date(l * MILLI);
            }
        } else if (o instanceof byte[] || o instanceof Byte[] || o instanceof short[] || o instanceof Short[] ||
                o instanceof int[] || o instanceof Integer[] || o instanceof long[] || o instanceof Long[] ||
                o instanceof float[] || o instanceof Float[] || o instanceof double[] || o instanceof Double[] ||
                o instanceof char[] || o instanceof Character[] || o instanceof String[]) {
            try {
                int len = Arrays1.lengths(o);
                int[] analyse = new int[7];
                for (int i = 0; i < len; i++) {
                    analyse[i] = Converts.toInt(((Object) Arrays1.gets(o, i)));
                }
                return build(analyse[0], analyse[1], analyse[2], analyse[3], analyse[4], analyse[5], analyse[6]);
            } catch (Exception e) {
                return null;
            }
        } else if (o instanceof Date) {
            return (Date) o;
        } else if (o instanceof Calendar) {
            return ((Calendar) o).getTime();
        } else if (o instanceof String) {
            return parse((String) o);
        } else if (o instanceof LocalDate) {
            return date((LocalDate) o);
        } else if (o instanceof LocalDateTime) {
            return date((LocalDateTime) o);
        } else if (o instanceof Instant) {
            return date((Instant) o);
        }
        return null;
    }

    /**
     * 是否是时间戳毫秒
     *
     * @param l ignore
     * @return true 是
     */
    public static boolean isMilli(long l) {
        return l > 1000000000000L;
    }

    /**
     * 解构时间
     *
     * @param d 时间
     * @return [y, M, d, H, m, s, S]
     */
    public static int[] analyse(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return new int[]{
                c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE), c.get(Calendar.SECOND), c.get(Calendar.MILLISECOND)
        };
    }

    /**
     * 构建时间
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 时间
     */
    public static Date build(int year, int month, int day) {
        return build(year, month, day, 0, 0, 0, 0);
    }

    /**
     * 构建时间
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @param h     时
     * @param m     分
     * @param s     秒
     * @return 时间
     */
    public static Date build(int year, int month, int day, int h, int m, int s) {
        return build(year, month, day, h, m, s, 0);
    }

    /**
     * 构建时间
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @param h     时
     * @param m     分
     * @param s     秒
     * @param ms    毫秒
     * @return 时间
     */
    public static Date build(int year, int month, int day, int h, int m, int s, int ms) {
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day, h, m, s);
        if (ms != 0) {
            c.set(Calendar.MILLISECOND, ms);
        }
        return c.getTime();
    }

    /**
     * 清除时分秒
     *
     * @return 清除后的时间
     */
    public static Date clearHMS() {
        return clearHMS(new Date());
    }

    /**
     * 清除时分秒
     *
     * @param d 时间
     * @return 清除后的时间
     */
    public static Date clearHMS(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 清除当前时间为当月1号
     *
     * @return 时间
     */
    public static Date clearDay() {
        return clearDay(new Date(), false);
    }

    /**
     * 清除当前时间为当月1号, 清除HMS
     *
     * @return 时间
     */
    public static Date clearDayHMS() {
        return clearDay(new Date(), true);
    }

    /**
     * 清除时间为1号
     *
     * @param d 时间
     * @return 时间
     */
    public static Date clearDay(Date d) {
        return clearDay(d, false);
    }

    /**
     * 清除时间为1号, 清除HMS
     *
     * @param d 时间
     * @return 时间
     */
    public static Date clearDayHMS(Date d) {
        return clearDay(d, true);
    }

    /**
     * 清除时间为当月1号
     *
     * @param d        时间
     * @param clearHMS 是否清除HMS
     * @return 时间
     */
    private static Date clearDay(Date d, boolean clearHMS) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.DAY_OF_MONTH, 1);
        if (clearHMS) {
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
        }
        return c.getTime();
    }

    /**
     * 今天的 23:59:59
     *
     * @return 23:59:59
     */
    public static Date dayEnd() {
        return dayEnd(new Date());
    }

    /**
     * 日期的  23:59:59
     *
     * @param d 时间
     * @return 23:59:59
     */
    public static Date dayEnd(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    /**
     * 当前月份的最后一天
     *
     * @return 时间
     */
    public static Date monthEnd() {
        return monthDayEnd(new Date(), false);
    }

    /**
     * 月份的最后一天
     *
     * @param d 时间
     * @return 时间
     */
    public static Date monthEnd(Date d) {
        return monthDayEnd(d, false);
    }

    /**
     * 当前月份的最后一天 时间的最后一秒
     *
     * @return 时间
     */
    public static Date monthDayEnd() {
        return monthDayEnd(new Date(), true);
    }

    /**
     * 月份的最后一天 时间的最后一秒
     *
     * @param d 时间
     * @return 时间
     */
    public static Date monthDayEnd(Date d) {
        return monthDayEnd(d, true);
    }

    /**
     * 月份的最后一天
     *
     * @param d      时间
     * @param dayEnd 是否将时间 转化为 23:59:59
     * @return 时间
     */
    private static Date monthDayEnd(Date d, boolean dayEnd) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.DAY_OF_MONTH, getMonthLastDay(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1));
        if (dayEnd) {
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            c.set(Calendar.MILLISECOND, 999);
        }
        return c.getTime();
    }

    /**
     * 对秒进行偏移
     *
     * @param off 偏移量
     * @return 偏移量
     */
    public static Date offsetSecond(int off) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, off);
        return c.getTime();
    }

    /**
     * 对秒进行偏移
     *
     * @param d   时间
     * @param off 偏移量
     * @return 偏移量
     */
    public static Date offsetSecond(Date d, int off) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.SECOND, off);
        return c.getTime();
    }

    /**
     * 对分进行偏移
     *
     * @param off 偏移量
     * @return 偏移量
     */
    public static Date offsetMinute(int off) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, off);
        return c.getTime();
    }

    /**
     * 对分进行偏移
     *
     * @param d   时间
     * @param off 偏移量
     * @return 偏移量
     */
    public static Date offsetMinute(Date d, int off) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.MINUTE, off);
        return c.getTime();
    }

    /**
     * 对时进行偏移
     *
     * @param off 偏移量
     * @return 偏移量
     */
    public static Date offsetHour(int off) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, off);
        return c.getTime();
    }

    /**
     * 对时进行偏移
     *
     * @param d   时间
     * @param off 偏移量
     * @return 偏移量
     */
    public static Date offsetHour(Date d, int off) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.HOUR_OF_DAY, off);
        return c.getTime();
    }

    /**
     * 对天进行偏移
     *
     * @param off 偏移量
     * @return 偏移量
     */
    public static Date offsetDay(int off) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, off);
        return c.getTime();
    }

    /**
     * 对天进行偏移
     *
     * @param d   时间
     * @param off 偏移量
     * @return 偏移量
     */
    public static Date offsetDay(Date d, int off) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DAY_OF_MONTH, off);
        return c.getTime();
    }

    /**
     * 对周进行偏移
     *
     * @param off 偏移量
     * @return 偏移量
     */
    public static Date offsetWeek(int off) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.WEEK_OF_MONTH, off);
        return c.getTime();
    }

    /**
     * 对周进行偏移
     *
     * @param d   时间
     * @param off 偏移量
     * @return 偏移量
     */
    public static Date offsetWeek(Date d, int off) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.WEEK_OF_MONTH, off);
        return c.getTime();
    }

    /**
     * 对月进行偏移
     *
     * @param off 偏移量
     * @return 偏移量
     */
    public static Date offsetMonth(int off) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, off);
        return c.getTime();
    }

    /**
     * 对月进行偏移
     *
     * @param d   时间
     * @param off 偏移量
     * @return 偏移量
     */
    public static Date offsetMonth(Date d, int off) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.MONTH, off);
        return c.getTime();
    }

    /**
     * 对年进行偏移
     *
     * @param off 偏移量
     * @return 偏移量
     */
    public static Date offsetYear(int off) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, off);
        return c.getTime();
    }

    /**
     * 对年进行偏移
     *
     * @param d   时间
     * @param off 偏移量
     * @return 偏移量
     */
    public static Date offsetYear(Date d, int off) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.YEAR, off);
        return c.getTime();
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间
     */
    public static String current() {
        return DateFormatUtils.format(new Date(), YMDHMS);
    }

    /**
     * 获取当前时间
     *
     * @param pattern 格式
     * @return 当前时间
     */
    public static String current(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 判断时间是否有交集
     *
     * @param d1 时间1开始
     * @param d2 时间1结束
     * @param d3 时间2开始
     * @param d4 时间2结束
     * @return true有交集
     */
    public static boolean cross(Date d1, Date d2, Date d3, Date d4) {
        long l1 = d1.getTime(), l2 = d2.getTime(), l3 = d3.getTime(), l4 = d4.getTime();
        if (l1 >= l3 && l2 >= l4) {
            return false;
        } else {
            return !(l1 <= l3 && l2 <= l4);
        }
    }

    /**
     * 格式化
     *
     * @param d 时间
     * @return ignore
     */
    public static String format(Date d) {
        return FastDateFormat.getInstance(YMDHMS).format(d);
    }

    /**
     * 格式化
     *
     * @param d       时间
     * @param pattern 格式
     * @return ignore
     */
    public static String format(Date d, String pattern) {
        return FastDateFormat.getInstance(pattern).format(d);
    }

    /**
     * 日期转化
     *
     * @param d 日期
     * @return 日期
     */
    public static Date parse(String d) {
        if (Strings.isBlank(d)) {
            return null;
        }
        if (d.contains("-")) {
            String pattern = null;
            for (String s : PARSE_PATTERN) {
                if (d.length() == s.length()) {
                    pattern = s;
                    break;
                }
            }
            if (pattern != null) {
                return parse(d, pattern);
            }
        } else if (d.contains("/")) {
            String pattern = null;
            for (String s : PARSE_PATTERN1) {
                if (d.length() == s.length()) {
                    pattern = s;
                    break;
                }
            }
            if (pattern != null) {
                return parse(d, pattern);
            }
        } else if (d.split(" ").length == 6) {
            return world(d);
        }
        return null;
    }

    /**
     * 日期转化
     *
     * @param d       日期
     * @param pattern 格式
     * @return 日期
     */
    public static Date parse(String d, String pattern) {
        try {
            return FastDateFormat.getInstance(pattern).parse(d);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 日期转化
     *
     * @param d        日期
     * @param patterns 格式
     * @return 日期
     */
    public static Date parse(String d, String... patterns) {
        for (String pattern : patterns) {
            Date parse = parse(d, pattern);
            if (parse != null) {
                return parse;
            }
        }
        return null;
    }

    /**
     * 将英国时间转为date
     *
     * @param s 英国时间
     * @return date
     */
    public static Date world(String s) {
        return world(s, WORLD, Locale.ENGLISH);
    }

    /**
     * 将世界时间转为date
     *
     * @param s       时间
     * @param pattern 格式
     * @param locale  时区
     * @return date
     */
    public static Date world(String s, String pattern, Locale locale) {
        try {
            SimpleDateFormat p = new SimpleDateFormat(pattern, locale);
            return p.parse(s);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 时间段
     *
     * @param hour 小时
     * @return 时间段
     */
    public static String hourType(int hour) {
        if (hour >= 0 && hour < 6) {
            return "凌晨";
        } else if (hour >= 6 && hour < 11) {
            return "上午";
        } else if (hour >= 11 && hour < 13) {
            return "中午";
        } else if (hour >= 13 && hour < 18) {
            return "下午";
        } else if (hour >= 18 && hour < 20) {
            return "傍晚";
        } else if (hour >= 20 && hour < 22) {
            return "晚上";
        } else if (hour >= 22 && hour <= 23) {
            return "深夜";
        }
        return "未知";
    }

    /**
     * 获得与当前时间的前后
     *
     * @param date 对比的时间
     * @return 结果
     */
    public static String ago(Date date) {
        return ago(new Date(), date, false, false);
    }

    /**
     * 获得时间的前后
     *
     * @param source 源时间
     * @param date   对比的时间
     * @return 结果
     */
    public static String ago(Date source, Date date) {
        return ago(source, date, false, false);
    }

    /**
     * 获得时间的前后
     *
     * @param source  源时间
     * @param date    对比的时间
     * @param vague   是否使用模糊时间 如: 昨天/23小时前
     * @param useWeek 是否使用周 如: 1周前/8天前
     * @return 结果
     */
    public static String ago(Date source, Date date, boolean vague, boolean useWeek) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(source);
        c2.setTime(date);
        long now = c1.getTime().getTime(), d = date.getTime();
        int nowYear = c1.get(Calendar.YEAR),
                dYear = c2.get(Calendar.YEAR),
                nowDay = c1.get(Calendar.DAY_OF_MONTH),
                dDay = c2.get(Calendar.DAY_OF_MONTH);
        if (now - d > 0) {
            long before = now - d;
            if (nowYear - dYear > 10 && vague) {
                return "很久以前";
            }
            if (nowYear - dYear == 1 && vague) {
                return "去年";
            }
            if (before > (DAY_STAMP * 365)) {
                return (before / (DAY_STAMP * 365)) + "年前";
            }
            if (before > (MONTH_STAMP)) {
                return (before / (MONTH_STAMP)) != 12 ? (before / (MONTH_STAMP)) + "月前" : "1年前";
            }
            if (before > WEEK_STAMP && useWeek) {
                return (before / WEEK_STAMP) + "周前";
            }
            if (nowDay - dDay == 1 && vague) {
                return "昨天";
            }
            if (nowDay - dDay == 2 && vague) {
                return "前天";
            }
            if (before > DAY_STAMP) {
                return (before / DAY_STAMP) + "天前";
            }
            if (before > HOUR_STAMP) {
                return (before / HOUR_STAMP) + "小时前";
            }
            if (before > MINUTE_STAMP) {
                return (before / MINUTE_STAMP) + "分钟前";
            }
        } else if (d - now > 0) {
            long after = d - now;
            if (dYear - nowYear > 10 && vague) {
                return "很久以后";
            }
            if (dYear - nowYear == 1 && vague) {
                return "明年";
            }
            if (after > (DAY_STAMP * 365)) {
                return (after / (DAY_STAMP * 365)) + "年后";
            }
            if (after > (MONTH_STAMP)) {
                return (after / (MONTH_STAMP)) != 12 ? (after / (MONTH_STAMP)) + "月后" : "1年后";
            }
            if (after > WEEK_STAMP && useWeek) {
                return (after / WEEK_STAMP) + "周后";
            }
            if (dDay - nowDay == 1 && vague) {
                return "明天";
            }
            if (dDay - nowDay == 2 && vague) {
                return "后天";
            }
            if (after > DAY_STAMP) {
                return (after / DAY_STAMP) + "天后";
            }
            if (after > HOUR_STAMP) {
                return (after / HOUR_STAMP) + "小时后";
            }
            if (after > MINUTE_STAMP) {
                return (after / MINUTE_STAMP) + "分钟后";
            }
        }
        return "刚刚";
    }

    /**
     * 获得与当前时间的前后
     *
     * @param d 对比的时间
     * @return 结果
     */
    @Deprecated
    public static String dateAgo(Date d) {
        return dateAgo(new Date(), d, true);
    }

    /**
     * 获得与当前时间的前后
     *
     * @param source 源时间
     * @param d      对比的时间
     * @return 结果
     */
    @Deprecated
    public static String dateAgo(Date source, Date d) {
        return dateAgo(source, d, true);
    }

    /**
     * 获得时间的前后
     *
     * @param source 源时间
     * @param d      对比的时间
     * @param strict 是否使用精确时间 如: 23小时前/昨天
     * @return 结果
     */
    @Deprecated
    public static String dateAgo(Date source, Date d, boolean strict) {
        DateStream ss = stream(source);
        // 目标时间
        int sYear = ss.getYear(),
                sMonth = ss.getMonth(),
                sDay = ss.getDay(),
                sHour = ss.getHour(),
                sMinute = ss.getMinute();
        // 比较时间
        DateStream ds = stream(d);
        int dYear = ds.getYear(),
                dMonth = ds.getMonth(),
                dDay = ds.getDay(),
                dHour = ds.getHour(),
                dMinute = ds.getMinute();
        if (d.before(source)) {
            if (sYear > dYear) {
                if ((sYear - dYear) > 10) {
                    return "很久以前";
                } else if (sYear - dYear == 1) {
                    if (strict) {
                        if (sMonth > dMonth) {
                            return "1年前";
                        } else if (sMonth == dMonth) {
                            if (sDay > dDay) {
                                return "1年前";
                            } else {
                                return "11月前";
                            }
                        } else {
                            return 12 - (dMonth - sMonth) + "月前";
                        }
                    } else {
                        return "去年";
                    }
                } else {
                    return sYear - dYear + "年前";
                }
            }
            if (sMonth > dMonth) {
                if (sMonth - dMonth == 1 && strict) {
                    if (sDay >= dDay) {
                        return "1月前";
                    } else {
                        return getMonthLastDay(sYear, sMonth) - (dDay - sDay) + "天前";
                    }
                } else {
                    return sMonth - dMonth + "月前";
                }
            }
            if (sDay > dDay) {
                if (strict) {
                    if (sDay - dDay == 1) {
                        if (sHour >= dHour) {
                            return "昨天";
                        } else {
                            return 24 - (dHour - sHour) + "小时前";
                        }
                    } else if (sDay - dDay == 2) {
                        return "前天";
                    } else {
                        return sDay - dDay + "天前";
                    }
                } else {
                    if (sDay - dDay == 1) {
                        return "昨天";
                    } else if (sDay - dDay == 2) {
                        return "前天";
                    } else {
                        return sDay - dDay + "天前";
                    }
                }
            }
            if (sHour > dHour) {
                if (sHour - dHour == 1 && strict) {
                    if (sMinute >= dMinute) {
                        return "1小时前";
                    } else {
                        return 60 - (dMinute - sMinute) + "分钟前";
                    }
                } else {
                    return sHour - dHour + "小时前";
                }
            }
            if (sMinute > dMinute) {
                return sMinute - dMinute + "分钟前";
            }
        } else if (d.after(source)) {
            if (sYear < dYear) {
                if ((dYear - sYear) > 10) {
                    return "很久以后";
                } else if (dYear - sYear == 1) {
                    if (strict) {
                        if (sMonth > dMonth) {
                            return 12 + dMonth - sMonth + "月后";
                        } else if (sMonth == dMonth) {
                            if (dDay > sDay) {
                                return "1年后";
                            } else {
                                return "11月后";
                            }
                        } else {
                            return "1年后";
                        }
                    } else {
                        return "明年";
                    }
                } else {
                    return dYear - sYear + "年后";
                }
            }
            if (sMonth < dMonth) {
                if (dMonth - sMonth == 1 && strict) {
                    if (sDay >= dDay) {
                        return getMonthLastDay(sYear, sMonth) + (dDay - sDay) + "天后";
                    } else {
                        return "1月后";
                    }
                } else {
                    return dMonth - sMonth + "月后";
                }
            }
            if (sDay < dDay) {
                if (dDay - sDay == 1 && strict) {
                    if (dHour < sHour) {
                        return 24 - (sHour - dHour) + "小时后";
                    } else {
                        return "明天";
                    }
                } else if (dDay - sDay == 1) {
                    return "后天";
                } else if (dDay - sDay == 2) {
                    return "后天";
                } else {
                    return dDay - sDay + "天后";
                }
            }
            if (sHour < dHour) {
                if (dHour - sHour == 1 && strict) {
                    if (dMinute < sMinute) {
                        return 60 - (sMinute - dMinute) + "分钟后";
                    } else {
                        return "1小时后";
                    }
                } else {
                    return dHour - sHour + "小时后";
                }
            }
            if (sMinute < dMinute) {
                return dMinute - sMinute + "分钟后";
            }
        }
        return "刚刚";
    }

    /**
     * 时差
     *
     * @param date1 时间1
     * @param date2 时间2
     * @return 时差
     */
    public static long intervalMS(Date date1, Date date2) {
        return date1.getTime() - date2.getTime();
    }

    /**
     * 时差解构
     *
     * @param date1 时间1
     * @param date2 时间2
     * @return 时差 [天,时,分,秒]
     */
    public static long[] intervalAnalyse(Date date1, Date date2) {
        long d1 = date1.getTime(), d2 = date2.getTime();
        return intervalAnalyse((d1 - d2) > 0 ? d1 - d2 : d2 - d1);
    }

    /**
     * 时差
     *
     * @param date1 时间1
     * @param date2 时间2
     * @return 时差 如0天0时0分3秒
     */
    public static String interval(Date date1, Date date2) {
        return interval(date1, date2, null, null, null, null);
    }

    /**
     * 时差
     *
     * @param date1  时间1
     * @param date2  时间2
     * @param day    天显示的文字
     * @param hour   时显示的文字
     * @param minute 分显示的文字
     * @param second 秒显示的文字
     * @return 时差
     */
    public static String interval(Date date1, Date date2, String day, String hour, String minute, String second) {
        long d1 = date1.getTime(), d2 = date2.getTime();
        return interval((d1 - d2) > 0 ? d1 - d2 : d2 - d1, day, hour, minute, second);
    }

    /**
     * 时差解构
     *
     * @param ms 时间戳毫秒
     * @return 时差 [天,时,分,秒]
     */
    public static long[] intervalAnalyse(long ms) {
        long distanceDay = ms / DAY_STAMP;
        long distanceHour = ms % DAY_STAMP / HOUR_STAMP;
        long distanceMinute = ms % DAY_STAMP % HOUR_STAMP / MINUTE_STAMP;
        long distanceSecond = ms % DAY_STAMP % HOUR_STAMP % MINUTE_STAMP / MILLI;
        return new long[]{distanceDay, distanceHour, distanceMinute, distanceSecond};
    }

    /**
     * 时差
     *
     * @param ms 时间戳毫秒
     * @return 时差 如0天0时0分3秒
     */
    public static String interval(long ms) {
        return interval(ms, null, null, null, null);
    }

    /**
     * 时差
     *
     * @param ms     时间戳毫秒
     * @param day    天显示的文字
     * @param hour   时显示的文字
     * @param minute 分显示的文字
     * @param second 秒显示的文字
     * @return 时差
     */
    public static String interval(long ms, String day, String hour, String minute, String second) {
        long distanceDay = ms / DAY_STAMP;
        long distanceHour = ms % DAY_STAMP / HOUR_STAMP;
        long distanceMinute = ms % DAY_STAMP % HOUR_STAMP / MINUTE_STAMP;
        long distanceSecond = ms % DAY_STAMP % HOUR_STAMP % MINUTE_STAMP / MILLI;
        return distanceDay + (day == null ? "天" : day) + distanceHour + (hour == null ? "时" : hour) + distanceMinute + (minute == null ? "分" : minute) + distanceSecond + (second == null ? "秒" : second);
    }

    /**
     * 转换格式
     *
     * @param d 时间
     * @param o 原格式
     * @param n 新格式
     * @return 日期
     */
    public static String convert(String d, String o, String n) {
        return format(parse(d, o), n);
    }

    /**
     * 转换格式
     *
     * @param d 时间
     * @param n 新格式
     * @return 日期
     */
    public static String convert(String d, String n) {
        Date parse = parse(d);
        if (parse == null) {
            return "";
        }
        return format(parse, n);
    }

    /**
     * 判断是否在这个时间段内
     *
     * @param d 时间
     * @param s 开始时间
     * @param e 结束时间
     * @return true: 包含
     */
    public static boolean include(Date d, Date s, Date e) {
        long ds = d.getTime(), ss = s.getTime(), es = e.getTime();
        return ds >= ss && ds <= es;
    }

    /**
     * 判断是否不在这个时间段内
     *
     * @param d 时间
     * @param s 开始时间
     * @param e 结束时间
     * @return true: 不包含
     */
    public static boolean exclude(Date d, Date s, Date e) {
        long ds = d.getTime(), ss = s.getTime(), es = e.getTime();
        return ds < ss || ds > es;
    }

    /**
     * 今年是否为闰年
     *
     * @return true闰年
     */
    public static boolean isLeapYear() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }

    /**
     * 是否为闰年
     *
     * @param d 时间
     * @return true闰年
     */
    public static boolean isLeapYear(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int year = c.get(Calendar.YEAR);
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }

    /**
     * 是否为闰年
     *
     * @param year 年
     * @return true闰年
     */
    public static boolean isLeapYear(int year) {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }

    /**
     * 获得本月的最后一天
     *
     * @return 最后一天
     */
    public static int getMonthLastDay() {
        Calendar c = Calendar.getInstance();
        return getMonthLastDay(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
    }

    /**
     * 获得月份的最后一天
     *
     * @param date 时间
     * @return 最后一天
     */
    public static int getMonthLastDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return getMonthLastDay(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
    }

    /**
     * 获得月份的最后一天
     *
     * @param y 年份
     * @param m 月份
     * @return 最后一天
     */
    public static int getMonthLastDay(int y, int m) {
        if (m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10 || m == 12) {
            return 31;
        } else if (m == 2) {
            if (isLeapYear(y)) {
                return 29;
            } else {
                return 28;
            }
        } else {
            return 30;
        }
    }

    // ---------- JDK8 ----------

    public static Instant instant() {
        return Instant.now();
    }

    public static Instant instant(Date date) {
        return Instant.ofEpochMilli(date.getTime());
    }

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

    public static Date date() {
        return new Date();
    }

    public static Date date(long ms) {
        return new Date(ms);
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

    public static LocalDateTime clearHMS(LocalDateTime d) {
        return LocalDateTime.of(d.toLocalDate(), LocalTime.of(0, 0, 0, 0));
    }

    public static LocalDateTime clearDay(LocalDateTime d) {
        return d.withDayOfMonth(1);
    }

    public static LocalDateTime clearDayHMS(LocalDateTime d) {
        return LocalDateTime.of(d.toLocalDate().withDayOfMonth(1), LocalTime.of(0, 0, 0, 0));
    }

    /**
     * 格式化
     *
     * @param d ignore
     * @return ignore
     */
    public static String format(TemporalAccessor d) {
        return format(d, YMDHMS);
    }

    /**
     * 格式化
     *
     * @param d       ignore
     * @param pattern 格式
     * @return ignore
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

    /**
     * 日期转化
     *
     * @param d ignore
     * @return ignore
     */
    public static TemporalAccessor parser(String d) {
        TemporalAccessor r = null;
        if (d.contains("-")) {
            String pattern = null;
            for (String s : PARSE_PATTERN) {
                if (d.length() == s.length()) {
                    pattern = s;
                    break;
                }
            }
            if (pattern != null) {
                return parser(d, pattern);
            }
        } else if (d.contains("/")) {
            String pattern = null;
            for (String s : PARSE_PATTERN1) {
                if (d.length() == s.length()) {
                    pattern = s;
                    break;
                }
            }
            if (pattern != null) {
                return parser(d, pattern);
            }
        }
        return r;
    }

    public static TemporalAccessor parser(String d, String pattern) {
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

    // ---------- Stream ----------

    /**
     * 日期流
     *
     * @return 流
     */
    public static DateStream stream() {
        return new DateStream(new Date());
    }

    /**
     * 日期流
     *
     * @param date 时间
     * @return 流
     */
    public static DateStream stream(Date date) {
        Valid.notNull(date);
        return new DateStream(date);
    }

    /**
     * 日期流
     *
     * @param c 日历
     * @return 流
     */
    public static DateStream stream(Calendar c) {
        Valid.notNull(c);
        return new DateStream(c);
    }

    /**
     * 日期流
     */
    public static class DateStream {

        private Calendar c;

        private DateStream(Date date) {
            this.c = Calendar.getInstance();
            this.c.setTime(date);
        }

        private DateStream(Calendar c) {
            this.c = c;
        }

        // ---------- Year ----------

        public DateStream addYear(int y) {
            c.add(Calendar.YEAR, y);
            return this;
        }

        public DateStream subYear(int y) {
            c.add(Calendar.YEAR, -y);
            return this;
        }

        public DateStream setYear(int y) {
            c.set(Calendar.YEAR, y);
            return this;
        }

        public int getYear() {
            return c.get(Calendar.YEAR);
        }

        // ---------- Month ----------

        public DateStream addMonth(int m) {
            c.add(Calendar.MONTH, m);
            return this;
        }

        public DateStream subMonth(int m) {
            c.add(Calendar.MONTH, -m);
            return this;
        }

        public DateStream setMonth(int m) {
            c.set(Calendar.MONTH, m - 1);
            return this;
        }

        public int getMonth() {
            return c.get(Calendar.MONTH) + 1;
        }

        // ---------- Week ----------

        public DateStream addWeek(int w) {
            c.add(Calendar.WEEK_OF_MONTH, w);
            return this;
        }

        public DateStream subWeek(int w) {
            c.add(Calendar.WEEK_OF_MONTH, -w);
            return this;
        }

        public DateStream setWeek(int w) {
            c.set(Calendar.WEEK_OF_MONTH, w);
            return this;
        }

        public int getWeek() {
            return c.get(Calendar.WEEK_OF_MONTH);
        }

        // ---------- Day ----------

        public DateStream addDay(int d) {
            c.add(Calendar.DAY_OF_MONTH, d);
            return this;
        }

        public DateStream subDay(int d) {
            c.add(Calendar.DAY_OF_MONTH, -d);
            return this;
        }

        public DateStream setDay(int d) {
            c.set(Calendar.DAY_OF_MONTH, d);
            return this;
        }

        public int getDay() {
            return c.get(Calendar.DAY_OF_MONTH);
        }

        // ---------- Hour ----------

        public DateStream addHour(int h) {
            c.add(Calendar.HOUR_OF_DAY, h);
            return this;
        }

        public DateStream subHour(int h) {
            c.add(Calendar.HOUR_OF_DAY, -h);
            return this;
        }

        public DateStream setHour(int h) {
            c.set(Calendar.HOUR_OF_DAY, h);
            return this;
        }

        public int getHour() {
            return c.get(Calendar.HOUR_OF_DAY);
        }

        // ---------- Minute ----------

        public DateStream addMinute(int m) {
            c.add(Calendar.MINUTE, m);
            return this;
        }

        public DateStream subMinute(int m) {
            c.add(Calendar.MINUTE, -m);
            return this;
        }

        public DateStream setMinute(int m) {
            c.set(Calendar.MINUTE, m);
            return this;
        }

        public int getMinute() {
            return c.get(Calendar.MINUTE);
        }

        // ---------- Second ----------

        public DateStream addSecond(int s) {
            c.add(Calendar.SECOND, s);
            return this;
        }

        public DateStream subSecond(int s) {
            c.add(Calendar.SECOND, -s);
            return this;
        }

        public DateStream setSecond(int s) {
            c.set(Calendar.SECOND, s);
            return this;
        }

        public int getSecond() {
            return c.get(Calendar.SECOND);
        }

        // ---------- MilliSecond ----------

        public DateStream addMilliSecond(int ms) {
            c.add(Calendar.MILLISECOND, ms);
            return this;
        }

        public DateStream subMilliSecond(int ms) {
            c.add(Calendar.MILLISECOND, -ms);
            return this;
        }

        public DateStream setMilliSecond(int ms) {
            c.set(Calendar.MILLISECOND, ms);
            return this;
        }

        public int getMilliSecond() {
            return c.get(Calendar.MILLISECOND);
        }

        // ---------- global ----------

        public DateStream add(int type, int v) {
            c.add(type, v);
            return this;
        }

        public DateStream sub(int type, int v) {
            c.add(type, -v);
            return this;
        }

        public DateStream set(int type, int v) {
            c.set(type, v);
            return this;
        }

        // ---------- Result ----------

        public int get(int type) {
            return c.get(type);
        }

        public String format() {
            return Dates.format(this.c.getTime());
        }

        public String format(String pattern) {
            return Dates.format(this.c.getTime(), pattern);
        }

        public Date date() {
            return this.c.getTime();
        }

        public Calendar calendar() {
            return this.c;
        }

    }

}
