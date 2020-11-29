package com.orion.utils.time;

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
     * 判断时间是否有交集
     *
     * @param dateStart1 时间1开始
     * @param dateEnd1   时间1结束
     * @param dateStart2 时间2开始
     * @param dateEnd2   时间2结束
     * @return true有交集
     */
    public static boolean cross(Date dateStart1, Date dateEnd1, Date dateStart2, Date dateEnd2) {
        long l1 = dateStart1.getTime(), l2 = dateEnd1.getTime(), l3 = dateStart2.getTime(), l4 = dateEnd2.getTime();
        return !((l1 < l3 && l2 < l3) || (l1 > l4 && l2 > l4)) && !(l1 >= l3 && l1 <= l4 && l2 >= l3 && l2 <= l4);
    }

    /**
     * 判断是否不在这个时间段内
     *
     * @param d          时间
     * @param rangeStart 范围开始时间
     * @param rangeEnd   范围结束时间
     * @return true: 不包含
     */
    public static boolean exclude(Date d, Date rangeStart, Date rangeEnd) {
        long ds = d.getTime(), rs = rangeStart.getTime(), re = rangeEnd.getTime();
        return ds < rs || ds > re;
    }

    /**
     * 判断是否不在这个时间段内
     *
     * @param dateStart  时间开始
     * @param dateEnd    时间结束
     * @param rangeStart 范围开始时间
     * @param rangeEnd   范围结束时间
     * @return true: 不包含
     */
    public static boolean exclude(Date dateStart, Date dateEnd, Date rangeStart, Date rangeEnd) {
        long ds = dateStart.getTime(), de = dateEnd.getTime(), rs = rangeStart.getTime(), re = rangeEnd.getTime();
        return (ds < rs && de < rs) || (ds > re && de > re);
    }

    /**
     * 判断时间是否在这个时间段内
     *
     * @param d          时间
     * @param rangeStart 范围开始时间
     * @param rangeEnd   范围结束时间
     * @return true: 包含
     */
    public static boolean include(Date d, Date rangeStart, Date rangeEnd) {
        long ds = d.getTime(), rs = rangeStart.getTime(), re = rangeEnd.getTime();
        return ds >= rs && ds <= re;
    }

    /**
     * 判断时间段是否在这个时间段内
     *
     * @param dateStart  时间结束
     * @param dateEnd    时间结束
     * @param rangeStart 范围开始时间
     * @param rangeEnd   范围结束时间
     * @return true: 包含
     */
    public static boolean include(Date dateStart, Date dateEnd, Date rangeStart, Date rangeEnd) {
        long ds = dateStart.getTime(), de = dateEnd.getTime(), rs = rangeStart.getTime(), re = rangeEnd.getTime();
        return ds >= rs && ds <= re && de >= rs && de <= re;
    }

    /**
     * 判断 d1 是否在 d2 之前
     *
     * @param d1 d1
     * @param d2 d2
     * @return true 之前
     */
    public static boolean before(Date d1, Date d2) {
        return d1.compareTo(d2) < 0;
    }

    /**
     * 判断 d1 是否在 d2 之后
     *
     * @param d1 d1
     * @param d2 d2
     * @return true 之后
     */
    public static boolean after(Date d1, Date d2) {
        return d1.compareTo(d2) > 0;
    }

}
