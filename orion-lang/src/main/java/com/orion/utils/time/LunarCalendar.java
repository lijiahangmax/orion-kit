package com.orion.utils.time;

import com.orion.utils.Strings;

import java.util.Calendar;
import java.util.Date;

/**
 * 农历
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/3 23:41
 */
public class LunarCalendar {

    /**
     * 1900-01-31
     */
    private static final long BASE_DATE = -2206425943000L;

    private static final String[] MONTH = {"正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "腊"};
    private static final String[] DAY = {"一", "二", "三", "四", "五", "六", "七", "八", "九"};
    private static final String[] GAN = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
    private static final String[] ZHI = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};

    private static final long[] LUNAR = new long[]{
            0x04BD8, 0x04AE0, 0x0A570, 0x054D5, 0x0D260, 0x0D950, 0x16554, 0x056A0, 0x09AD0, 0x055D2,
            0x04AE0, 0x0A5B6, 0x0A4D0, 0x0D250, 0x1D255, 0x0B540, 0x0D6A0, 0x0ADA2, 0x095B0, 0x14977,
            0x04970, 0x0A4B0, 0x0B4B5, 0x06A50, 0x06D40, 0x1AB54, 0x02B60, 0x09570, 0x052F2, 0x04970,
            0x06566, 0x0D4A0, 0x0EA50, 0x16A95, 0x05AD0, 0x02B60, 0x186E3, 0x092E0, 0x1C8D7, 0x0C950,
            0x0D4A0, 0x1D8A6, 0x0B550, 0x056A0, 0x1A5B4, 0x025D0, 0x092D0, 0x0D2B2, 0x0A950, 0x0B557,
            0x06CA0, 0x0B550, 0x15355, 0x04DA0, 0x0A5B0, 0x14573, 0x052B0, 0x0A9A8, 0x0E950, 0x06AA0,
            0x0AEA6, 0x0AB50, 0x04B60, 0x0AAE4, 0x0A570, 0x05260, 0x0F263, 0x0D950, 0x05B57, 0x056A0,
            0x096D0, 0x04DD5, 0x04AD0, 0x0A4D0, 0x0D4D4, 0x0D250, 0x0D558, 0x0B540, 0x0B6A0, 0x195A6,
            0x095B0, 0x049B0, 0x0A974, 0x0A4B0, 0x0B27A, 0x06A50, 0x06D40, 0x0AF46, 0x0AB60, 0x09570,
            0x04AF5, 0x04970, 0x064B0, 0x074A3, 0x0EA50, 0x06B58, 0x05AC0, 0x0AB60, 0x096D5, 0x092E0,
            0x0C960, 0x0D954, 0x0D4A0, 0x0DA50, 0x07552, 0x056A0, 0x0ABB7, 0x025D0, 0x092D0, 0x0CAB5,
            0x0A950, 0x0B4A0, 0x0BAA4, 0x0AD50, 0x055D9, 0x04BA0, 0x0A5B0, 0x15176, 0x052B0, 0x0A930,
            0x07954, 0x06AA0, 0x0AD50, 0x05B52, 0x04B60, 0x0A6E6, 0x0A4E0, 0x0D260, 0x0EA65, 0x0D530,
            0x05AA0, 0x076A3, 0x096D0, 0x04AFB, 0x04AD0, 0x0A4D0, 0x1D0B6, 0x0D250, 0x0D520, 0x0DD45,
            0x0B5A0, 0x056D0, 0x055B2, 0x049B0, 0x0A577, 0x0A4B0, 0x0AA50, 0x1B255, 0x06D20, 0x0ADA0,
            0x14B63, 0x09370, 0x049F8, 0x04970, 0x064B0, 0x168A6, 0x0EA50, 0x06B20, 0x1A6C4, 0x0AAE0,
            0x092E0, 0x0D2E3, 0x0C960, 0x0D557, 0x0D4A0, 0x0DA50, 0x05D55, 0x056A0, 0x0A6D0, 0x055D4,
            0x052D0, 0x0A9B8, 0x0A950, 0x0B4A0, 0x0B6A6, 0x0AD50, 0x055A0, 0x0ABA4, 0x0A5B0, 0x052B0,
            0x0B273, 0x06930, 0x07337, 0x06AA0, 0x0AD50, 0x14B55, 0x04B60, 0x0A570, 0x054E4, 0x0D160,
            0x0E968, 0x0D520, 0x0DAA0, 0x16AA6, 0x056D0, 0x04AE0, 0x0A9D4, 0x0A2D0, 0x0D150, 0x0F252,
    };

    /**
     * 农历年
     */
    private final int year;

    /**
     * 农历月
     */
    private final int month;

    /**
     * 农历日
     */
    private final int day;

    /**
     * 是否闰年
     */
    private boolean leap;

    public LunarCalendar() {
        this(System.currentTimeMillis());
    }

    public LunarCalendar(Date date) {
        this(date.getTime());
    }

    public LunarCalendar(Calendar c) {
        this(c.getTime());
    }

    public LunarCalendar(long time) {
        // 求出和1900年1月31日相差的天数
        int offset = (int) ((time - BASE_DATE) / Dates.DAY_STAMP);
        // 用offset减去每农历年的天数 计算当天是农历第几天 offset是当年的第几天
        int daysOfYear;
        int iYear = 1900;
        final int maxYear = iYear + LUNAR.length - 1;
        for (; iYear <= maxYear; iYear++) {
            daysOfYear = yearDays(iYear);
            if (offset < daysOfYear) {
                break;
            }
            offset -= daysOfYear;
        }
        this.year = iYear;

        // 计算农历月份 闰哪个月 1-12
        int leapMonth = leapMonth(iYear);
        // 用当年的天数offset,逐个减去每月（农历）的天数 求出当天是本月的第几天
        int iMonth;
        int daysOfMonth = 0;
        for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
            // 闰月
            if (leapMonth > 0 && iMonth == (leapMonth + 1) && !leap) {
                --iMonth;
                this.leap = true;
                daysOfMonth = leapDays(year);
            } else {
                daysOfMonth = monthDays(year, iMonth);
            }
            offset -= daysOfMonth;
            // 解除闰月
            if (leap && iMonth == (leapMonth + 1)) {
                this.leap = false;
            }
        }

        // offset为0时 并且刚才计算的月份是闰月 要校正
        if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
            if (leap) {
                this.leap = false;
            } else {
                this.leap = true;
                --iMonth;
            }
        }

        // offset小于0时 也要校正
        if (offset < 0) {
            offset += daysOfMonth;
            --iMonth;
        }
        this.month = iMonth;
        this.day = offset + 1;
    }

    public LunarCalendar(int year, int month, int day) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.leap = Dates.isLeapYear(year);
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    /**
     * 获得农历月称呼
     *
     * @return 返回农历月份称呼
     */
    public String getChineseMonth() {
        return (leap ? "闰" : Strings.EMPTY) + MONTH[month - 1] + "月";
    }

    /**
     * 获得农历日
     *
     * @return 获得农历日
     */
    public String getChineseDay() {
        String[] chineseTen = {"初", "十", "廿", "卅"};
        int n = (day % 10 == 0) ? 9 : (day % 10 - 1);
        if (day > 30) {
            return Strings.EMPTY;
        }
        switch (day) {
            case 10:
                return "初十";
            case 20:
                return "二十";
            case 30:
                return "三十";
            default:
                return chineseTen[day / 10] + DAY[n];
        }
    }

    /**
     * 获得年份生肖
     *
     * @return 获得年份生肖
     */
    public String getChineseZodiac() {
        return Zodiacs.getChineseZodiac(year);
    }

    /**
     * 获得天干地支
     *
     * @return 获得天干地支
     */
    public String getCyclical() {
        return cycle(year - 1900 + 36);
    }

    /**
     * 获取农历时间
     *
     * @return 农历时间
     */
    public String toChineseString() {
        return String.format("%s%s年 %s%s", getCyclical(), getChineseZodiac(), getChineseMonth(), getChineseDay());
    }

    @Override
    public String toString() {
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    /**
     * 获取干支
     *
     * @param num 月日的offset
     * @return 干支
     */
    private static String cycle(int num) {
        return GAN[num % 10] + ZHI[num % 12];
    }

    /**
     * 获取年的总天数
     *
     * @param y 年
     * @return 总天数
     */
    private static int yearDays(int y) {
        int i, sum = 348;
        for (i = 0x8000; i > 0x8; i >>= 1) {
            if ((LUNAR[y - 1900] & i) != 0) {
                sum += 1;
            }
        }
        return (sum + leapDays(y));
    }

    /**
     * 获取闰月的天数
     *
     * @param y 年
     * @return 闰月的天数 不是闰月返回0
     */
    private static int leapDays(int y) {
        if (leapMonth(y) != 0) {
            return (LUNAR[y - 1900] & 0x10000) != 0 ? 30 : 29;
        }
        return 0;
    }

    /**
     * 获取月份的总天数
     *
     * @param y 年
     * @param m 月
     * @return 天
     */
    private static int monthDays(int y, int m) {
        return (LUNAR[y - 1900] & (0x10000 >> m)) == 0 ? 29 : 30;
    }

    /**
     * 获取闰月份
     *
     * @param y 年
     * @return 闰月 没有则为0
     */
    private static int leapMonth(int y) {
        return (int) (LUNAR[y - 1900] & 0xf);
    }

}
