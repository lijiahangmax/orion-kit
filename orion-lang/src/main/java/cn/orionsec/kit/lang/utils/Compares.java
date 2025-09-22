/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.lang.utils;

import java.util.Comparator;

/**
 * 比较工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/5 14:12
 */
public class Compares {

    private Compares() {
    }

    /**
     * 比较
     *
     * @param o1  对象1
     * @param o2  对象2
     * @param <T> ignore
     * @return ignore
     */
    public static <T extends Comparable<T>> int compare(T o1, T o2) {
        return o1.compareTo(o2);
    }

    /**
     * 比较
     *
     * @param o1  对象1
     * @param o2  对象2
     * @param c   比较接口
     * @param <T> ignore
     * @return ignore
     */
    public static <T> int compare(T o1, T o2, Comparator<T> c) {
        return c.compare(o1, o2);
    }

    /**
     * 比较接口判断相等
     *
     * @param o1  对象1
     * @param o2  对象2
     * @param <T> ignore
     * @return ignore
     */
    public static <T extends Comparable<T>> boolean compared(T o1, T o2) {
        if (o1 == null || o2 == null) {
            return false;
        }
        return o1.compareTo(o2) == 0;
    }

    /**
     * 比较接口判断相等
     *
     * @param o1  对象1
     * @param o2  对象2
     * @param c   比较接口
     * @param <T> ignore
     * @return ignore
     */
    public static <T> boolean compared(T o1, T o2, Comparator<T> c) {
        if (o1 == null || o2 == null) {
            return false;
        }
        return c.compare(o1, o2) == 0;
    }

    /**
     * test value < refer
     *
     * @param value value
     * @param refer refer
     * @param <T>   T
     * @return true  value < refer
     */
    public static <T extends Comparable<T>> boolean lt(T value, T refer) {
        Assert.notNull(value, "value is null");
        Assert.notNull(refer, "refer is null");
        return value.compareTo(refer) < 0;
    }

    /**
     * test value <= refer
     *
     * @param value value
     * @param refer refer
     * @param <T>   T
     * @return true  value <= refer
     */
    public static <T extends Comparable<T>> boolean lte(T value, T refer) {
        Assert.notNull(value, "value is null");
        Assert.notNull(refer, "refer is null");
        return value.compareTo(refer) <= 0;
    }

    /**
     * test value > refer
     *
     * @param value value
     * @param refer refer
     * @param <T>   T
     * @return true  value > refer
     */
    public static <T extends Comparable<T>> boolean gt(T value, T refer) {
        Assert.notNull(value, "value is null");
        Assert.notNull(refer, "refer is null");
        return value.compareTo(refer) > 0;
    }

    /**
     * test value >= refer
     *
     * @param value value
     * @param refer refer
     * @param <T>   T
     * @return true  value >= refer
     */
    public static <T extends Comparable<T>> boolean gte(T value, T refer) {
        Assert.notNull(value, "value is null");
        Assert.notNull(refer, "refer is null");
        return value.compareTo(refer) >= 0;
    }

    /**
     * 判断是否在区间内 开区间
     *
     * @param value 测试的数字
     * @param start 区间开始
     * @param end   区间结束
     * @param <T>   T
     * @return true 在区间内
     */
    public static <T extends Comparable<T>> boolean inRange(T value, T start, T end) {
        Assert.notNull(value, "value is null");
        Assert.notNull(start, "start range is null");
        Assert.notNull(value, "end range is null");
        return start.compareTo(value) <= 0 && value.compareTo(end) <= 0;
    }

    /**
     * 判断是否不在区间内 开区间
     *
     * @param value 测试的数字
     * @param start 区间开始
     * @param end   区间结束
     *              [1, 1] 1 true
     *              [1, 2] 1 true
     * @param <T>   T
     * @return true 不在区间内
     */
    public static <T extends Comparable<T>> boolean notInRange(T value, T start, T end) {
        return !inRange(value, start, end);
    }

    /**
     * 判断一个区间是否在另一个区间内 开区间
     *
     * @param rangeStart     区间开始
     * @param rangeEnd       区间结束
     * @param testRangeStart 测试的区间开始
     * @param testRangeEnd   测试的区间结束
     *                       [1, 1], [1, 1] true
     *                       [1, 2], [1, 1] true
     *                       [1, 2], [1, 2] true
     * @param <T>            T
     * @return true 在区间内
     */
    public static <T extends Comparable<T>> boolean rangeInRange(T rangeStart, T rangeEnd, T testRangeStart, T testRangeEnd) {
        Assert.notNull(rangeStart, "start range is null");
        Assert.notNull(rangeEnd, "end range is null");
        Assert.notNull(testRangeStart, "test start range is null");
        Assert.notNull(testRangeEnd, "test end range is null");
        return (rangeStart.compareTo(testRangeStart) <= 0 && rangeStart.compareTo(testRangeEnd) <= 0)
                && (testRangeStart.compareTo(rangeEnd) <= 0 && testRangeEnd.compareTo(rangeEnd) <= 0);
    }

    /**
     * 判断一个区间是否不在另一个区间内 开区间
     *
     * @param rangeStart     区间开始
     * @param rangeEnd       区间结束
     * @param testRangeStart 测试的区间开始
     * @param testRangeEnd   测试的区间结束
     * @param <T>            T
     * @return true 不在区间内
     */
    public static <T extends Comparable<T>> boolean rangeNotInRange(T rangeStart, T rangeEnd, T testRangeStart, T testRangeEnd) {
        return !rangeInRange(rangeStart, rangeEnd, testRangeStart, testRangeEnd);
    }

    /**
     * 交差
     *
     * @param start1 开始区间1
     * @param end1   结束区间1
     * @param start2 开始区间2
     * @param end2   结束区间2
     *               [1, 1] [1, 1] false
     *               [1, 1] [1, 2] false
     *               [1, 2] [1, 1] false
     *               [1, 2] [2, 3] false
     *               [1, 2] [1, 2] true
     * @param <T>    T
     * @return 是否有交差
     */
    public static <T extends Comparable<T>> boolean cross(T start1, T end1, T start2, T end2) {
        Assert.notNull(start1, "start range1 is null");
        Assert.notNull(end1, "end range1 is null");
        Assert.notNull(start2, "start range2 is null");
        Assert.notNull(end2, "end range2 is null");
        if (end1.compareTo(start2) <= 0 || end2.compareTo(start1) <= 0) {
            return false;
        }
        return (start1 == start2 && end1 == end2)
                || Compares.rangeInRange(start1, end1, start2, end2)
                || Compares.inRange(start2, start1, end1)
                || Compares.rangeInRange(start2, end2, start1, end1)
                || Compares.inRange(start1, start2, end2);
    }

    /**
     * 不交叉
     *
     * @param start1 开始区间1
     * @param end1   结束区间1
     * @param start2 开始区间2
     * @param end2   结束区间2
     * @param <T>    T
     * @return 是否不交叉
     */
    public static <T extends Comparable<T>> boolean uncross(T start1, T end1, T start2, T end2) {
        return !cross(start1, end1, start2, end2);
    }

}
