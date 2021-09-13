package com.orion.utils.time;

import com.orion.utils.Exceptions;

import java.time.*;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/11 16:50
 */
class BaseDates {

    public static final String WORLD = "EEE MMM dd HH:mm:ss z yyyy";
    public static final String YM = "yyyy-MM";
    public static final String YMD = "yyyy-MM-dd";
    public static final String YMD_HM = "yyyy-MM-dd HH:mm";
    public static final String YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    public static final String YMD_HMSS = "yyyy-MM-dd HH:mm:ss SSS";
    public static final String HMS = "HH:mm:ss";
    public static final String HMSS = "HH:mm:ss SSS";
    public static final String YM1 = "yyyy/MM";
    public static final String YMD1 = "yyyy/MM/dd";
    public static final String YMD_HM1 = "yyyy/MM/dd HH/mm";
    public static final String YMD_HMS1 = "yyyy/MM/dd HH/mm/ss";
    public static final String YMD_HMSS1 = "yyyy/MM/dd HH/mm/ss SSS";
    public static final String YM2 = "yyyyMM";
    public static final String YMD2 = "yyyyMMdd";
    public static final String YMD_HM2 = "yyyyMMddHHmm";
    public static final String YMD_HMS2 = "yyyyMMddHHmmss";
    public static final String YMD_HMSS2 = "yyyyMMddHHmmssSSS";
    public static final String[] PARSE_PATTERN_GROUP1 = {YMD_HMS, YMD, YM, YMD_HM, YMD_HMSS};
    public static final String[] PARSE_PATTERN_GROUP2 = {YMD_HMS1, YMD1, YM1, YMD_HM1, YMD_HMSS1};
    public static final String[] PARSE_PATTERN_GROUP3 = {YMD_HMS2, YMD2, YM2, YMD_HM2, YMD_HMSS2};

    public static final long WEEK_STAMP = 604800000L;
    public static final long YEAR_STAMP = 31536000000L;
    public static final long MONTH_STAMP = 2592000000L;
    public static final long DAY_STAMP = 86400000L;
    public static final long HOUR_STAMP = 3600000L;
    public static final long MINUTE_STAMP = 60000L;
    public static final long SECOND_STAMP = 1000L;

    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();
    public static final ZoneOffset DEFAULT_ZONE_OFFSET = ZoneOffset.of("+8");
    public static final ZoneOffset DEFAULT_ZERO_ZONE_OFFSET = ZoneOffset.ofHours(0);

    private static final String PAD_HMS = "%04d-%02d-%02d";
    private static final String PAD_YMD_HMS = "%04d-%02d-%02d %02d:%02d:%02d";
    private static final String PAD_YMD_HMSS = "%04d-%02d-%02d %02d:%02d:%02d %03d";

    BaseDates() {
    }

    /**
     * 判断对象是否为时间格式 不包含String
     *
     * @param c c
     * @return ignore
     */
    public static boolean isDateClass(Class<?> c) {
        return c == Long.TYPE || c == Long.class
                || c == Integer.TYPE || c == Integer.class
                || c == Date.class || c == Calendar.class
                || c == LocalDate.class || c == LocalDateTime.class || c == Instant.class;
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
     * 判断时间是否在未来
     *
     * @param date date
     * @return true 在未来
     */
    public static boolean inFuture(long date) {
        return date > System.currentTimeMillis();
    }

    public static String interval(long ms) {
        return interval(ms, false, null, null, null, null);
    }

    public static String interval(long ms, boolean full) {
        return interval(ms, full, null, null, null, null);
    }

    /**
     * 时差
     *
     * @param ms     时间戳毫秒
     * @param full   为0是否显示 0天0时0分1秒 : 1秒
     * @param day    天显示的文字
     * @param hour   时显示的文字
     * @param minute 分显示的文字
     * @param second 秒显示的文字
     * @return 时差
     */
    public static String interval(long ms, boolean full, String day, String hour, String minute, String second) {
        long[] analysis = intervalAnalysis(ms);
        if (full) {
            return analysis[0] + (day == null ? "天" : day)
                    + analysis[1] + (hour == null ? "时" : hour)
                    + analysis[2] + (minute == null ? "分" : minute)
                    + analysis[3] + (second == null ? "秒" : second);
        }
        boolean ay = true, ah = true, am = true;
        if (analysis[0] == 0) {
            ay = false;
        }
        if (!ay && analysis[1] == 0) {
            ah = false;
        }
        if (!ah && analysis[2] == 0) {
            am = false;
        }
        StringBuilder sb = new StringBuilder();
        if (ay) {
            sb.append(analysis[0]).append(day == null ? "天" : day);
        }
        if (ah) {
            sb.append(analysis[1]).append(hour == null ? "时" : hour);
        }
        if (am) {
            sb.append(analysis[2]).append(minute == null ? "分" : minute);
        }
        sb.append(analysis[3]).append(second == null ? "秒" : second);
        return sb.toString();
    }

    /**
     * 时差解构
     *
     * @param ms 时间戳毫秒
     * @return 时差 [天,时,分,秒]
     */
    public static long[] intervalAnalysis(long ms) {
        long distanceDay = ms / DAY_STAMP;
        long distanceHour = ms % DAY_STAMP / HOUR_STAMP;
        long distanceMinute = ms % DAY_STAMP % HOUR_STAMP / MINUTE_STAMP;
        long distanceSecond = ms % DAY_STAMP % HOUR_STAMP % MINUTE_STAMP / SECOND_STAMP;
        if (distanceSecond == 0 && ms != 0) {
            distanceSecond = 1;
        }
        return new long[]{distanceDay, distanceHour, distanceMinute, distanceSecond};
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
     * 获得月份的最后一天
     *
     * @param year  年份
     * @param month 月份
     * @return 最后一天
     */
    public static int getMonthLastDay(int year, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 2:
                return isLeapYear(year) ? 29 : 28;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                throw Exceptions.argument("month must >= 1 and <= 12");
        }
    }

    /**
     * 获取季度
     *
     * @param month month
     * @return 季度
     */
    public static int getQuarter(int month) {
        switch (month) {
            case 1:
            case 2:
            case 3:
                return 1;
            case 4:
            case 5:
            case 6:
                return 2;
            case 7:
            case 8:
            case 9:
                return 3;
            case 10:
            case 11:
            case 12:
                return 4;
            default:
                throw Exceptions.argument("month must >= 1 and <= 12");
        }
    }

    public static String pad(int year, int month, int day) {
        return String.format(PAD_HMS, year, month, day);
    }

    public static String pad(int year, int month, int day, int hour, int minute, int second) {
        return String.format(PAD_YMD_HMS, year, month, day, hour, minute, second);
    }

    /**
     * 填充时间
     *
     * @param year   year
     * @param month  month
     * @param day    day
     * @param hour   hour
     * @param minute minute
     * @param second second
     * @param milli  milli
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String pad(int year, int month, int day, int hour, int minute, int second, int milli) {
        return String.format(PAD_YMD_HMSS, year, month, day, hour, minute, second, milli);
    }

}
