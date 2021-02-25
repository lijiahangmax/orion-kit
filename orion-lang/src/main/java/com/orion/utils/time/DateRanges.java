package com.orion.utils.time;

import com.orion.utils.Valid;
import com.orion.utils.math.Numbers;

import java.util.Date;

/**
 * 时间区间工具类
 *
 * @author ljh15
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
        Valid.notNull(rangeStart1, "rangeStart1 is null");
        Valid.notNull(rangeEnd1, "rangeEnd1 is null");
        Valid.notNull(rangeStart2, "rangeStart2 is null");
        Valid.notNull(rangeEnd2, "rangeEnd2 is null");
        long start1 = rangeStart1.getTime(), end1 = rangeEnd1.getTime(),
                start2 = rangeStart2.getTime(), end2 = rangeEnd2.getTime();
        return Numbers.cross(start1, end1, start2, end2);
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
     * @param rangeStart 范围开始时间
     * @param rangeEnd   范围结束时间
     * @param date       时间
     * @return true 包含
     */
    public static boolean inRange(Date rangeStart, Date rangeEnd, Date date) {
        Valid.notNull(rangeStart, "rangeStart is null");
        Valid.notNull(rangeEnd, "rangeEnd is null");
        Valid.notNull(date, "date is null");
        return Numbers.inRange(rangeStart.getTime(), rangeEnd.getTime(), date.getTime());
    }

    /**
     * 判断是否不在这个时间段内
     *
     * @param rangeStart 范围开始时间
     * @param rangeEnd   范围结束时间
     * @param date       时间
     * @return true 不包含
     */
    public static boolean notInRange(Date rangeStart, Date rangeEnd, Date date) {
        return !inRange(rangeStart, rangeEnd, date);
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
        Valid.notNull(rangeStart, "rangeStart is null");
        Valid.notNull(rangeEnd, "rangeEnd is null");
        Valid.notNull(testRangeStart, "testRangeStart is null");
        Valid.notNull(testRangeEnd, "testRangeEnd is null");
        return Numbers.rangeInRange(rangeStart.getTime(), rangeEnd.getTime(), testRangeStart.getTime(), testRangeEnd.getTime());
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
