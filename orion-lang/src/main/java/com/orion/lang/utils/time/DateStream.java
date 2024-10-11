/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package com.orion.lang.utils.time;

import com.orion.lang.define.support.CloneSupport;
import com.orion.lang.utils.Valid;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期流
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/31 21:38
 */
public class DateStream extends CloneSupport<DateStream> implements Serializable {

    private static final long serialVersionUID = -21831208940539450L;

    private final Calendar c;

    public DateStream() {
        this.c = Dates.calendar();
    }

    public DateStream(Date date) {
        Valid.notNull(date, "date is null");
        this.c = Dates.calendar(date);
    }

    public DateStream(Date date, TimeZone timeZone) {
        Valid.notNull(date, "date is null");
        Valid.notNull(timeZone, "time zone is null");
        this.c = Calendar.getInstance(timeZone);
        this.c.setTime(date);
    }

    public DateStream(Date date, Locale locale) {
        Valid.notNull(date, "date is null");
        Valid.notNull(locale, "locale is null");
        this.c = Calendar.getInstance(locale);
        this.c.setTime(date);
    }

    public DateStream(Date date, TimeZone timeZone, Locale locale) {
        Valid.notNull(date, "date is null");
        Valid.notNull(timeZone, "time zone is null");
        Valid.notNull(locale, "locale is null");
        this.c = Calendar.getInstance(timeZone, locale);
        this.c.setTime(date);
    }

    public DateStream(Calendar c) {
        this.c = c;
    }

    public static DateStream of(Object o) {
        return new DateStream(Dates.date(o));
    }

    public static DateStream of(Object o, TimeZone timeZone) {
        return new DateStream(Dates.date(o), timeZone);
    }

    public static DateStream of(Object o, Locale locale) {
        return new DateStream(Dates.date(o), locale);
    }

    public static DateStream of(Object o, TimeZone timeZone, Locale locale) {
        return new DateStream(Dates.date(o), timeZone, locale);
    }

    // -------------------- year --------------------

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

    // -------------------- month --------------------

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

    // -------------------- week --------------------

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

    // -------------------- day --------------------

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

    // -------------------- hour --------------------

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

    // -------------------- minute --------------------

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

    // -------------------- second --------------------

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

    // -------------------- milli second --------------------

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

    public DateStream add(int type, int v) {
        c.add(type, v);
        return this;
    }

    public DateStream subtract(int type, int v) {
        c.add(type, -v);
        return this;
    }

    public DateStream set(int type, int v) {
        c.set(type, v);
        return this;
    }

    // -------------------- compute --------------------

    /**
     * 日期的 00:00:00
     * 清除时分秒
     *
     * @return this
     */
    public DateStream clearHms() {
        Dates.clearHms(c);
        return this;
    }

    /**
     * 日期的 23:59:59
     *
     * @return this
     */
    public DateStream dayEnd() {
        Dates.dayEnd(c);
        return this;
    }

    /**
     * 设置日期为1号
     *
     * @return this
     */
    public DateStream monthFirstDay() {
        Dates.monthFirstDay(c, false);
        return this;
    }

    /**
     * 设置日期为1号 时间为00:00:00
     *
     * @return this
     */
    public DateStream monthFirstDayHms() {
        Dates.monthFirstDay(c, true);
        return this;
    }

    /**
     * 设置日期为1号
     *
     * @param clearHms 是否设置时间为00:00:00
     * @return this
     */
    public DateStream monthFirstDay(boolean clearHms) {
        Dates.monthFirstDay(c, clearHms);
        return this;
    }

    /**
     * 设置日期为月份的最后一天
     *
     * @return this
     */
    public DateStream monthLastDay() {
        Dates.monthLastDay(c, false);
        return this;
    }

    /**
     * 设置日期为月份的最后一天 时间为23:59:59
     *
     * @return this
     */
    public DateStream monthLastDayHms() {
        Dates.monthLastDay(c, true);
        return this;
    }

    /**
     * 设置日期为月份的最后一天
     *
     * @param dayEnd 是否设置时间为23:59:59
     * @return this
     */
    public DateStream monthLastDay(boolean dayEnd) {
        Dates.monthLastDay(c, dayEnd);
        return this;
    }

    // -------------------- result --------------------

    public String format() {
        return Dates.format(c.getTime());
    }

    public String format(String pattern) {
        return Dates.format(c.getTime(), pattern);
    }

    public String format(Locale locale) {
        return Dates.format(c.getTime(), locale);
    }

    public String format(String pattern, Locale locale) {
        return Dates.format(c.getTime(), pattern, locale);
    }

    public String format(TimeZone timeZone) {
        return Dates.format(c.getTime(), timeZone);
    }

    public String format(String pattern, TimeZone timeZone) {
        return Dates.format(c.getTime(), pattern, timeZone);
    }

    public String format(TimeZone timeZone, Locale locale) {
        return Dates.format(c.getTime(), timeZone, locale);
    }

    public String format(String pattern, TimeZone timeZone, Locale locale) {
        return Dates.format(c.getTime(), pattern, timeZone, locale);
    }

    /**
     * 是否在时间区间内
     *
     * @param start 开始区间
     * @param end   结束区间
     * @return true包含
     */
    public boolean inRange(Date start, Date end) {
        return DateRanges.inRange(start, end, c.getTime());
    }

    /**
     * 是否不在时间区间内
     *
     * @param start 开始区间
     * @param end   结束区间
     * @return true不包含
     */
    public boolean notInRange(Date start, Date end) {
        return DateRanges.notInRange(start, end, c.getTime());
    }

    /**
     * 判断实际是否在 d 之前
     *
     * @param d d
     * @return true之前
     */
    public boolean before(Date d) {
        return DateRanges.before(c.getTime(), d);
    }

    /**
     * 判断实际是否在 d 之后
     *
     * @param d d
     * @return true之后
     */
    public boolean after(Date d) {
        return DateRanges.after(c.getTime(), d);
    }

    /**
     * 判断时间是否在未来
     *
     * @return true 在未来
     */
    public boolean inFuture() {
        return Dates.inFuture(c.getTimeInMillis());
    }

    /**
     * 是否为闰年
     *
     * @return true 闰年
     */
    public boolean isLeapYear() {
        return Dates.isLeapYear(c);
    }

    /**
     * 获得月份的最后一天
     *
     * @return 最后一天
     */
    public int getMonthLastDay() {
        return Dates.getMonthLastDay(c);
    }

    public int getYear() {
        return c.get(Calendar.YEAR);
    }

    public int getMonth() {
        return c.get(Calendar.MONTH) + 1;
    }

    public int getWeek() {
        return c.get(Calendar.WEEK_OF_MONTH);
    }

    public int getDay() {
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public int getHour() {
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute() {
        return c.get(Calendar.MINUTE);
    }

    public int getSecond() {
        return c.get(Calendar.SECOND);
    }

    public int getMilliSecond() {
        return c.get(Calendar.MILLISECOND);
    }

    public boolean isAm() {
        return Calendar.PM == c.get(Calendar.AM_PM);
    }

    public boolean isPm() {
        return Calendar.AM == c.get(Calendar.AM_PM);
    }

    public int getField(int type) {
        return c.get(type);
    }

    public Date get() {
        return c.getTime();
    }

    public Date date() {
        return c.getTime();
    }

    public long milliSecond() {
        return c.getTimeInMillis();
    }

    public Calendar calendar() {
        return c;
    }

    @Override
    public String toString() {
        return c.toString();
    }

}