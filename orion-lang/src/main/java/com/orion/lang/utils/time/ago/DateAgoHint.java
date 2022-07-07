package com.orion.lang.utils.time.ago;

import java.io.Serializable;

/**
 * 配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/27 18:38
 */
public class DateAgoHint implements Serializable {

    private static final long serialVersionUID = 41684659873123848L;

    /**
     * 很久之前
     */
    protected String longAgo;

    /**
     * 很久之后
     */
    protected String longFuture;

    /**
     * 去年
     */
    protected String lastYear;

    /**
     * 明年
     */
    protected String nextYear;

    /**
     * 昨天
     */
    protected String yesterday;

    /**
     * 明天
     */
    protected String tomorrow;

    /**
     * 前天
     */
    protected String beforeYesterday;

    /**
     * 后天
     */
    protected String afterTomorrow;

    /**
     * 刚刚
     */
    protected String justNow;

    /**
     * 现在
     */
    protected String now;

    /**
     * 片刻
     */
    protected String moment;

    /**
     * 几年前
     */
    protected String yearAgo;

    /**
     * 几年后
     */
    protected String yearFuture;

    /**
     * 几月前
     */
    protected String monthAgo;

    /**
     * 几月后
     */
    protected String monthFuture;

    /**
     * 几周前
     */
    protected String weekAgo;

    /**
     * 几周后
     */
    protected String weekFuture;

    /**
     * 几天前
     */
    protected String dayAgo;

    /**
     * 几天后
     */
    protected String dayFuture;

    /**
     * 小时前
     */
    protected String hourAgo;

    /**
     * 小时后
     */
    protected String hourFuture;

    /**
     * 几分钟前
     */
    protected String minuteAgo;

    /**
     * 几分钟后
     */
    protected String minuteFuture;

    /**
     * 几秒前
     */
    protected String secondAgo;

    /**
     * 几秒后
     */
    protected String secondFuture;

    public DateAgoHint() {
        this.longAgo = "很久以前";
        this.longFuture = "很久以后";
        this.lastYear = "去年";
        this.nextYear = "明年";
        this.yesterday = "昨天";
        this.tomorrow = "明天";
        this.beforeYesterday = "前天";
        this.afterTomorrow = "后天";
        this.justNow = "刚刚";
        this.now = "现在";
        this.moment = "片刻之后";
        this.yearAgo = "年前";
        this.yearFuture = "年后";
        this.monthAgo = "月前";
        this.monthFuture = "月后";
        this.weekAgo = "周前";
        this.weekFuture = "周后";
        this.dayAgo = "天前";
        this.dayFuture = "天后";
        this.hourAgo = "小时前";
        this.hourFuture = "小时后";
        this.minuteAgo = "分钟前";
        this.minuteFuture = "分钟后";
        this.secondAgo = "秒前";
        this.secondFuture = "秒后";
    }

    public String getLongAgo() {
        return longAgo;
    }

    public void setLongAgo(String longAgo) {
        this.longAgo = longAgo;
    }

    public String getLongFuture() {
        return longFuture;
    }

    public void setLongFuture(String longFuture) {
        this.longFuture = longFuture;
    }

    public String getLastYear() {
        return lastYear;
    }

    public void setLastYear(String lastYear) {
        this.lastYear = lastYear;
    }

    public String getNextYear() {
        return nextYear;
    }

    public void setNextYear(String nextYear) {
        this.nextYear = nextYear;
    }

    public String getYesterday() {
        return yesterday;
    }

    public void setYesterday(String yesterday) {
        this.yesterday = yesterday;
    }

    public String getTomorrow() {
        return tomorrow;
    }

    public void setTomorrow(String tomorrow) {
        this.tomorrow = tomorrow;
    }

    public String getBeforeYesterday() {
        return beforeYesterday;
    }

    public void setBeforeYesterday(String beforeYesterday) {
        this.beforeYesterday = beforeYesterday;
    }

    public String getAfterTomorrow() {
        return afterTomorrow;
    }

    public void setAfterTomorrow(String afterTomorrow) {
        this.afterTomorrow = afterTomorrow;
    }

    public String getJustNow() {
        return justNow;
    }

    public void setJustNow(String justNow) {
        this.justNow = justNow;
    }

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

    public String getMoment() {
        return moment;
    }

    public void setMoment(String moment) {
        this.moment = moment;
    }

    public String getYearAgo() {
        return yearAgo;
    }

    public void setYearAgo(String yearAgo) {
        this.yearAgo = yearAgo;
    }

    public String getYearFuture() {
        return yearFuture;
    }

    public void setYearFuture(String yearFuture) {
        this.yearFuture = yearFuture;
    }

    public String getMonthAgo() {
        return monthAgo;
    }

    public void setMonthAgo(String monthAgo) {
        this.monthAgo = monthAgo;
    }

    public String getMonthFuture() {
        return monthFuture;
    }

    public void setMonthFuture(String monthFuture) {
        this.monthFuture = monthFuture;
    }

    public String getWeekAgo() {
        return weekAgo;
    }

    public void setWeekAgo(String weekAgo) {
        this.weekAgo = weekAgo;
    }

    public String getWeekFuture() {
        return weekFuture;
    }

    public void setWeekFuture(String weekFuture) {
        this.weekFuture = weekFuture;
    }

    public String getDayAgo() {
        return dayAgo;
    }

    public void setDayAgo(String dayAgo) {
        this.dayAgo = dayAgo;
    }

    public String getDayFuture() {
        return dayFuture;
    }

    public void setDayFuture(String dayFuture) {
        this.dayFuture = dayFuture;
    }

    public String getHourAgo() {
        return hourAgo;
    }

    public void setHourAgo(String hourAgo) {
        this.hourAgo = hourAgo;
    }

    public String getHourFuture() {
        return hourFuture;
    }

    public void setHourFuture(String hourFuture) {
        this.hourFuture = hourFuture;
    }

    public String getMinuteAgo() {
        return minuteAgo;
    }

    public void setMinuteAgo(String minuteAgo) {
        this.minuteAgo = minuteAgo;
    }

    public String getMinuteFuture() {
        return minuteFuture;
    }

    public void setMinuteFuture(String minuteFuture) {
        this.minuteFuture = minuteFuture;
    }

    public String getSecondAgo() {
        return secondAgo;
    }

    public void setSecondAgo(String secondAgo) {
        this.secondAgo = secondAgo;
    }

    public String getSecondFuture() {
        return secondFuture;
    }

    public void setSecondFuture(String secondFuture) {
        this.secondFuture = secondFuture;
    }

}
