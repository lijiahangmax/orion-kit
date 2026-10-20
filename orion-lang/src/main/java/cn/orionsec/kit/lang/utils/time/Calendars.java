/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.lang.utils.time;

import cn.orionsec.kit.lang.utils.Exceptions;

import java.util.Calendar;
import java.util.Date;

/**
 * 日历工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/10/20 10:00
 */
public class Calendars {

    /**
     * 起始时间 其余字段重置为最小值
     */
    public static final int START = 1;

    /**
     * 结束时间 其余字段重置为最大值
     */
    public static final int END = 2;

    private Calendars() {
    }

    // -------------------- year --------------------

    /**
     * 增加年
     *
     * @param date  时间
     * @param value 年数
     * @param flag  {@link #START} {@link #END} null 不处理
     * @return 处理后的时间
     */
    public static Date addYear(Date date, int value, Integer flag) {
        return add(date, Calendar.YEAR, value, flag);
    }

    public static Date addYear(Date date, int value) {
        return addYear(date, value, null);
    }

    public static Date addYear(int value, Integer flag) {
        return addYear(new Date(), value, flag);
    }

    public static Date addYear(int value) {
        return addYear(new Date(), value, null);
    }

    // -------------------- month --------------------

    /**
     * 增加月
     *
     * @param date  时间
     * @param value 月数
     * @param flag  {@link #START} {@link #END} null 不处理
     * @return 处理后的时间
     */
    public static Date addMonth(Date date, int value, Integer flag) {
        return add(date, Calendar.MONTH, value, flag);
    }

    public static Date addMonth(Date date, int value) {
        return addMonth(date, value, null);
    }

    public static Date addMonth(int value, Integer flag) {
        return addMonth(new Date(), value, flag);
    }

    public static Date addMonth(int value) {
        return addMonth(new Date(), value, null);
    }

    // -------------------- day --------------------

    /**
     * 增加日
     *
     * @param date  时间
     * @param value 天数
     * @param flag  {@link #START} {@link #END} null 不处理
     * @return 处理后的时间
     */
    public static Date addDay(Date date, int value, Integer flag) {
        return add(date, Calendar.DAY_OF_MONTH, value, flag);
    }

    public static Date addDay(Date date, int value) {
        return addDay(date, value, null);
    }

    public static Date addDay(int value, Integer flag) {
        return addDay(new Date(), value, flag);
    }

    public static Date addDay(int value) {
        return addDay(new Date(), value, null);
    }

    // -------------------- hour --------------------

    /**
     * 增加时
     *
     * @param date  时间
     * @param value 小时数
     * @param flag  {@link #START} {@link #END} null 不处理
     * @return 处理后的时间
     */
    public static Date addHour(Date date, int value, Integer flag) {
        return add(date, Calendar.HOUR_OF_DAY, value, flag);
    }

    public static Date addHour(Date date, int value) {
        return addHour(date, value, null);
    }

    public static Date addHour(int value, Integer flag) {
        return addHour(new Date(), value, flag);
    }

    public static Date addHour(int value) {
        return addHour(new Date(), value, null);
    }

    // -------------------- minute --------------------

    /**
     * 增加分
     *
     * @param date  时间
     * @param value 分钟数
     * @param flag  {@link #START} {@link #END} null 不处理
     * @return 处理后的时间
     */
    public static Date addMinute(Date date, int value, Integer flag) {
        return add(date, Calendar.MINUTE, value, flag);
    }

    public static Date addMinute(Date date, int value) {
        return addMinute(date, value, null);
    }

    public static Date addMinute(int value, Integer flag) {
        return addMinute(new Date(), value, flag);
    }

    public static Date addMinute(int value) {
        return addMinute(new Date(), value, null);
    }

    // -------------------- second --------------------

    /**
     * 增加秒
     *
     * @param date  时间
     * @param value 秒数
     * @param flag  {@link #START} {@link #END} null 不处理
     * @return 处理后的时间
     */
    public static Date addSecond(Date date, int value, Integer flag) {
        return add(date, Calendar.SECOND, value, flag);
    }

    public static Date addSecond(Date date, int value) {
        return addSecond(date, value, null);
    }

    public static Date addSecond(int value, Integer flag) {
        return addSecond(new Date(), value, flag);
    }

    public static Date addSecond(int value) {
        return addSecond(new Date(), value, null);
    }

    // -------------------- milli second --------------------

    /**
     * 增加毫秒
     *
     * @param date  时间
     * @param value 毫秒数
     * @return 处理后的时间
     */
    public static Date addMilliSecond(Date date, int value) {
        return add(date, Calendar.MILLISECOND, value, null);
    }

    public static Date addMilliSecond(int value) {
        return addMilliSecond(new Date(), value);
    }

    // -------------------- support --------------------

    private static Date add(Date date, int field, int value, Integer flag) {
        if (date == null) {
            return null;
        }
        checkFlag(flag);
        Calendar c = Dates.calendar(date);
        c.add(field, value);
        if (flag != null) {
            reset(c, field, flag);
        }
        return c.getTime();
    }

    /**
     * 校验标记
     *
     * @param flag {@link #START} {@link #END} null
     */
    private static void checkFlag(Integer flag) {
        if (flag != null && flag != START && flag != END) {
            throw Exceptions.argument("flag must be START or END");
        }
    }

    /**
     * 重置小于操作单位的字段
     *
     * @param c     时间
     * @param field 操作单位
     * @param flag  {@link #START} {@link #END}
     */
    private static void reset(Calendar c, int field, int flag) {
        boolean start = flag == START;
        switch (field) {
            case Calendar.YEAR:
                c.set(Calendar.MONTH, start ? 0 : 11);
            case Calendar.MONTH:
                if (start) {
                    c.set(Calendar.DAY_OF_MONTH, 1);
                } else {
                    c.set(Calendar.DAY_OF_MONTH, Dates.getMonthLastDay(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1));
                }
            case Calendar.DAY_OF_MONTH:
                c.set(Calendar.HOUR_OF_DAY, start ? 0 : 23);
            case Calendar.HOUR_OF_DAY:
                c.set(Calendar.MINUTE, start ? 0 : 59);
            case Calendar.MINUTE:
                c.set(Calendar.SECOND, start ? 0 : 59);
            case Calendar.SECOND:
                c.set(Calendar.MILLISECOND, start ? 0 : 999);
                break;
            default:
                break;
        }
    }

}
