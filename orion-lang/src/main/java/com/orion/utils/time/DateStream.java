package com.orion.utils.time;

import com.orion.lang.support.CloneSupport;
import com.orion.utils.Valid;

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

    private Calendar c;

    public DateStream() {
        this.c = Calendar.getInstance();
    }

    public DateStream(Date date) {
        Valid.notNull(date, "date is null");
        this.c = Calendar.getInstance();
        this.c.setTime(date);
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

    public int getYear() {
        return c.get(Calendar.YEAR);
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

    public int getMonth() {
        return c.get(Calendar.MONTH) + 1;
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

    public int getWeek() {
        return c.get(Calendar.WEEK_OF_MONTH);
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

    public int getDay() {
        return c.get(Calendar.DAY_OF_MONTH);
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

    public int getHour() {
        return c.get(Calendar.HOUR_OF_DAY);
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

    public int getMinute() {
        return c.get(Calendar.MINUTE);
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

    public int getSecond() {
        return c.get(Calendar.SECOND);
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

    public int getMilliSecond() {
        return c.get(Calendar.MILLISECOND);
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

    // -------------------- result --------------------

    public String format() {
        return Dates.format(this.c.getTime());
    }

    public String format(String pattern) {
        return Dates.format(this.c.getTime(), pattern);
    }

    public String format(Locale locale) {
        return Dates.format(this.c.getTime(), locale);
    }

    public String format(String pattern, Locale locale) {
        return Dates.format(this.c.getTime(), pattern, locale);
    }

    public String format(TimeZone timeZone) {
        return Dates.format(this.c.getTime(), timeZone);
    }

    public String format(String pattern, TimeZone timeZone) {
        return Dates.format(this.c.getTime(), pattern, timeZone);
    }

    public String format(TimeZone timeZone, Locale locale) {
        return Dates.format(this.c.getTime(), timeZone, locale);
    }

    public String format(String pattern, TimeZone timeZone, Locale locale) {
        return Dates.format(this.c.getTime(), pattern, timeZone, locale);
    }

    /**
     * 是否在时间区间内
     *
     * @param start 开始区间
     * @param end   结束区间
     * @return true包含
     */
    public boolean inRange(Date start, Date end) {
        return DateRanges.inRange(start, end, this.c.getTime());
    }

    /**
     * 是否不在时间区间内
     *
     * @param start 开始区间
     * @param end   结束区间
     * @return true不包含
     */
    public boolean notInRange(Date start, Date end) {
        return DateRanges.notInRange(start, end, this.c.getTime());
    }

    /**
     * 判断实际是否在 d 之前
     *
     * @param d d
     * @return true之前
     */
    public boolean before(Date d) {
        return DateRanges.before(this.c.getTime(), d);
    }

    /**
     * 判断实际是否在 d 之后
     *
     * @param d d
     * @return true之后
     */
    public boolean after(Date d) {
        return DateRanges.after(this.c.getTime(), d);
    }

    public int get(int type) {
        return c.get(type);
    }

    public Date date() {
        return this.c.getTime();
    }

    public Calendar calendar() {
        return this.c;
    }

    @Override
    public String toString() {
        return this.c.toString();
    }

}