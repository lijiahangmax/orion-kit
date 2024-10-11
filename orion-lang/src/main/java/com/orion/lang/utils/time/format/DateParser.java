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
