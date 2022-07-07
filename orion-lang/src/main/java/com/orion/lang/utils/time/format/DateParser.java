package com.orion.lang.utils.time.format;

import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * copy with apache
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/25 12:30
 */
public interface DateParser {

    /**
     * 格式化时间
     *
     * @param source 时间
     * @return date
     */
    Date parse(String source);

    /**
     * 格式化时间
     *
     * @param source 时间
     * @param pos    位置
     * @return date
     */
    Date parse(String source, ParsePosition pos);

    /**
     * 格式化时间 解析到日历
     *
     * @param source   时间
     * @param pos      位置
     * @param calendar 日历
     * @return 是否解析成功
     */
    boolean parse(String source, ParsePosition pos, Calendar calendar);

    /**
     * 获取格式
     *
     * @return 格式
     */
    String getPattern();

    /**
     * 获取时区
     *
     * @return 时区
     */
    TimeZone getTimeZone();

    /**
     * 获取地区
     *
     * @return 地区
     */
    Locale getLocale();

}
