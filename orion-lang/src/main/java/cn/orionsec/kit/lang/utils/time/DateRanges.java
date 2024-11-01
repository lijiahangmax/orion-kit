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
package cn.orionsec.kit.lang.utils.time;

import cn.orionsec.kit.lang.utils.Compares;
import cn.orionsec.kit.lang.utils.Valid;

import java.util.Date;

/**
 * 时间区间工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/8/13 18:34
 */
public class DateRanges {

    private DateRanges() {
    }

    /**
     * 判断时间是否有交差
     *
     * @param rangeStart1 时间1开始
     * @param rangeEnd1   时间1结束
     * @param rangeStart2 时间2开始
     * @param rangeEnd2   时间2结束
     * @return true有交差
     */
    public static boolean cross(Date rangeStart1, Date rangeEnd1, Date rangeStart2, Date rangeEnd2) {
        return Compares.cross(rangeStart1, rangeEnd1, rangeStart2, rangeEnd2);
    }

    /**
     * 判断时间是否没有交差
     *
     * @param rangeStart1 时间1开始
     * @param rangeEnd1   时间1结束
     * @param rangeStart2 时间2开始
     * @param rangeEnd2   时间2结束
     * @return true没有交差
     */
    public static boolean uncross(Date rangeStart1, Date rangeEnd1, Date rangeStart2, Date rangeEnd2) {
        return !cross(rangeStart1, rangeEnd1, rangeStart2, rangeEnd2);
    }

    /**
     * 判断时间是否在这个时间段内
     *
     * @param date       时间
     * @param rangeStart 范围开始时间
     * @param rangeEnd   范围结束时间
     * @return true 包含
     */
    public static boolean inRange(Date date, Date rangeStart, Date rangeEnd) {
        return Compares.inRange(date, rangeStart, rangeEnd);
    }

    /**
     * 判断是否不在这个时间段内
     *
     * @param rangeStart 范围开始时间
     * @param rangeEnd   范围结束时间
     * @param date       时间
     * @return true 不包含
     */
    public static boolean notInRange(Date date, Date rangeStart, Date rangeEnd) {
        return !inRange(date, rangeStart, rangeEnd);
    }

    /**
     * 判断时间段是否在这个时间段内
     *
     * @param rangeStart     范围开始时间
     * @param rangeEnd       范围结束时间
     * @param testRangeStart 测试范围开始时间
     * @param testRangeEnd   测试范围结束时间
     * @return true 包含
     */
    public static boolean rangeInRange(Date rangeStart, Date rangeEnd, Date testRangeStart, Date testRangeEnd) {
        return Compares.rangeInRange(rangeStart, rangeEnd, testRangeStart, testRangeEnd);
    }

    /**
     * 判断时间段是否在不这个时间段内
     *
     * @param rangeStart     范围开始时间
     * @param rangeEnd       范围结束时间
     * @param testRangeStart 测试范围开始时间
     * @param testRangeEnd   测试范围结束时间
     * @return true 不包含
     */
    public static boolean rangeNotInRange(Date rangeStart, Date rangeEnd, Date testRangeStart, Date testRangeEnd) {
        return !rangeInRange(rangeStart, rangeEnd, testRangeStart, testRangeEnd);
    }

    /**
     * 判断 date1 是否在 date2 之前
     *
     * @param date1 date1
     * @param date2 date2
     * @return true 之前
     */
    public static boolean before(Date date1, Date date2) {
        Valid.notNull(date1, "date1 is null");
        Valid.notNull(date2, "date2 is null");
        return date1.compareTo(date2) < 0;
    }

    /**
     * 判断 date1 是否在 date2 之后
     *
     * @param date1 date1
     * @param date2 date2
     * @return true 之后
     */
    public static boolean after(Date date1, Date date2) {
        Valid.notNull(date1, "date1 is null");
        Valid.notNull(date2, "date2 is null");
        return date1.compareTo(date2) > 0;
    }

}
