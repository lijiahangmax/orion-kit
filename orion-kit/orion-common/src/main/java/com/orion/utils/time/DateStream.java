package com.orion.utils.time;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期流
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/31 21:38
 */
public class DateStream {

    private Calendar c;

    public DateStream(Date date) {
        this.c = Calendar.getInstance();
        this.c.setTime(date);
    }

    public DateStream(Date date, TimeZone timeZone) {
        this.c = Calendar.getInstance(timeZone);
        this.c.setTime(date);
    }

    public DateStream(Date date, Locale locale) {
        this.c = Calendar.getInstance(locale);
        this.c.setTime(date);
    }

    public DateStream(Date date, TimeZone timeZone, Locale locale) {
        this.c = Calendar.getInstance(timeZone, locale);
        this.c.setTime(date);
    }

    public DateStream(Calendar c) {
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

    @Override
    public String toString() {
        return this.c.toString();
    }

}