package com.orion.lang.utils.time.format;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * copy with apache
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/25 19:29
 */
public interface DatePrinter {

    /**
     * 格式化毫秒
     *
     * @param millis 毫秒
     * @return ignore
     */
    String format(long millis);

    /**
     * 格式化时间
     *
     * @param date 时间
     * @return ignore
     */
    String format(Date date);

    /**
     * 格式化时间
     *
     * @param calendar calendar
     * @return ignore
     */
    String format(Calendar calendar);

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
