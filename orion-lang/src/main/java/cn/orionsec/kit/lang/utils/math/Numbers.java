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
package cn.orionsec.kit.lang.utils.math;

import cn.orionsec.kit.lang.utils.Arrays1;
import cn.orionsec.kit.lang.utils.Compares;
import cn.orionsec.kit.lang.utils.Valid;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 数字工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/8/13 23:29
 */
public class Numbers {

    private Numbers() {
    }

    /**
     * 将给定的数除余为指定区间中的数
     *
     * @param num        num
     * @param startRange 区间开始
     * @param endRange   区间结束
     * @return %
     */
    public static int getRangeNum(int num, int startRange, int endRange) {
        Valid.gte(startRange, 0, "start range must greater than or equal 0");
        Valid.gt(endRange, startRange, "end range must greater than start range");
        return startRange + (num % (endRange - startRange));
    }

    // -------------------- zero --------------------

    /**
     * 是否为0
     *
     * @param x ignore
     * @return true 0
     */
    public static boolean isZero(byte x) {
        return x == 0;
    }

    public static boolean isZero(short x) {
        return x == 0;
    }

    public static boolean isZero(int x) {
        return x == 0;
    }

    public static boolean isZero(long x) {
        return x == 0;
    }

    public static boolean isZero(float x) {
        return x == 0;
    }

    public static boolean isZero(double x) {
        return x == 0;
    }

    /**
     * 是否不为0
     *
     * @param x ignore
     * @return true 非0
     */
    public static boolean isNotZero(byte x) {
        return x != 0;
    }

    public static boolean isNotZero(short x) {
        return x != 0;
    }

    public static boolean isNotZero(int x) {
        return x != 0;
    }

    public static boolean isNotZero(long x) {
        return x != 0;
    }

    public static boolean isNotZero(float x) {
        return x != 0;
    }

    public static boolean isNotZero(double x) {
        return x != 0;
    }

    /**
     * 是否小于0
     *
     * @param x x
     * @return true 小于0
     */
    public static boolean ltZero(byte x) {
        return x < 0;
    }

    public static boolean ltZero(short x) {
        return x < 0;
    }

    public static boolean ltZero(int x) {
        return x < 0;
    }

    public static boolean ltZero(long x) {
        return x < 0;
    }

    public static boolean ltZero(float x) {
        return x < 0;
    }

    public static boolean ltZero(double x) {
        return x < 0;
    }

    /**
     * 是否小于等于0
     *
     * @param x x
     * @return true 小于等于0
     */
    public static boolean lteZero(byte x) {
        return x <= 0;
    }

    public static boolean lteZero(short x) {
        return x <= 0;
    }

    public static boolean lteZero(int x) {
        return x <= 0;
    }

    public static boolean lteZero(long x) {
        return x <= 0;
    }

    public static boolean lteZero(float x) {
        return x <= 0;
    }

    public static boolean lteZero(double x) {
        return x <= 0;
    }

    /**
     * 是否大于0
     *
     * @param x x
     * @return true 大于0
     */
    public static boolean gtZero(byte x) {
        return x > 0;
    }

    public static boolean gtZero(short x) {
        return x > 0;
    }

    public static boolean gtZero(int x) {
        return x > 0;
    }

    public static boolean gtZero(long x) {
        return x > 0;
    }

    public static boolean gtZero(float x) {
        return x > 0;
    }

    public static boolean gtZero(double x) {
        return x > 0;
    }

    /**
     * 是否大于等于0
     *
     * @param x x
     * @return true 大于等于0
     */
    public static boolean gteZero(byte x) {
        return x >= 0;
    }

    public static boolean gteZero(short x) {
        return x >= 0;
    }

    public static boolean gteZero(int x) {
        return x >= 0;
    }

    public static boolean gteZero(long x) {
        return x >= 0;
    }

    public static boolean gteZero(float x) {
        return x >= 0;
    }

    public static boolean gteZero(double x) {
        return x >= 0;
    }

    /**
     * 是否全部为0
     *
     * @param array array
     * @return true 全为0
     */
    public static boolean isAllZero(byte... array) {
        if (Arrays1.isEmpty(array)) {
            return false;
        }
        for (byte b : array) {
            if (b != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllZero(short... array) {
        if (Arrays1.isEmpty(array)) {
            return false;
        }
        for (short b : array) {
            if (b != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllZero(int... array) {
        if (Arrays1.isEmpty(array)) {
            return false;
        }
        for (int b : array) {
            if (b != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllZero(long... array) {
        if (Arrays1.isEmpty(array)) {
            return false;
        }
        for (long b : array) {
            if (b != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllZero(float... array) {
        if (Arrays1.isEmpty(array)) {
            return false;
        }
        for (float b : array) {
            if (b != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllZero(double... array) {
        if (Arrays1.isEmpty(array)) {
            return false;
        }
        for (double b : array) {
            if (b != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否全部不为0
     *
     * @param array array
     * @return true 全不为0
     */
    public static boolean isNoneZero(byte... array) {
        if (Arrays1.isEmpty(array)) {
            return true;
        }
        for (byte b : array) {
            if (b == 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNoneZero(short... array) {
        if (Arrays1.isEmpty(array)) {
            return true;
        }
        for (short b : array) {
            if (b == 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNoneZero(int... array) {
        if (Arrays1.isEmpty(array)) {
            return true;
        }
        for (int b : array) {
            if (b == 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNoneZero(long... array) {
        if (Arrays1.isEmpty(array)) {
            return true;
        }
        for (long b : array) {
            if (b == 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNoneZero(float... array) {
        if (Arrays1.isEmpty(array)) {
            return true;
        }
        for (float b : array) {
            if (b == 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNoneZero(double... array) {
        if (Arrays1.isEmpty(array)) {
            return true;
        }
        for (double b : array) {
            if (b == 0) {
                return false;
            }
        }
        return true;
    }

    // -------------------- isNegative --------------------

    /**
     * 是否为负数
     *
     * @param x ignore
     * @return true 负数
     */
    public static boolean isNegative(byte x) {
        return x < 0;
    }

    public static boolean isNegative(short x) {
        return x < 0;
    }

    public static boolean isNegative(int x) {
        return x < 0;
    }

    public static boolean isNegative(long x) {
        return x < 0;
    }

    public static boolean isNegative(float x) {
        return x < 0;
    }

    public static boolean isNegative(double x) {
        return x < 0;
    }

    /**
     * 是否不为负数
     *
     * @param x ignore
     * @return true 非负数
     */
    public static boolean isNotNegative(byte x) {
        return x >= 0;
    }

    public static boolean isNotNegative(short x) {
        return x >= 0;
    }

    public static boolean isNotNegative(int x) {
        return x >= 0;
    }

    public static boolean isNotNegative(long x) {
        return x >= 0;
    }

    public static boolean isNotNegative(float x) {
        return x >= 0;
    }

    public static boolean isNotNegative(double x) {
        return x >= 0;
    }

    /**
     * 是否全为负数
     *
     * @param array array
     * @return true 全为负数
     */
    public static boolean isAllNegative(byte... array) {
        if (Arrays1.isEmpty(array)) {
            return false;
        }
        for (byte b : array) {
            if (b >= 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllNegative(short... array) {
        if (Arrays1.isEmpty(array)) {
            return false;
        }
        for (short b : array) {
            if (b >= 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllNegative(int... array) {
        if (Arrays1.isEmpty(array)) {
            return false;
        }
        for (int b : array) {
            if (b >= 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllNegative(long... array) {
        if (Arrays1.isEmpty(array)) {
            return false;
        }
        for (long b : array) {
            if (b >= 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllNegative(float... array) {
        if (Arrays1.isEmpty(array)) {
            return false;
        }
        for (float b : array) {
            if (b >= 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllNegative(double... array) {
        if (Arrays1.isEmpty(array)) {
            return false;
        }
        for (double b : array) {
            if (b >= 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否全不为负数
     *
     * @param array array
     * @return true 全不为负数
     */
    public static boolean isNoneNegative(byte... array) {
        if (Arrays1.isEmpty(array)) {
            return true;
        }
        for (byte b : array) {
            if (b < 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNoneNegative(short... array) {
        if (Arrays1.isEmpty(array)) {
            return true;
        }
        for (short b : array) {
            if (b < 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNoneNegative(int... array) {
        if (Arrays1.isEmpty(array)) {
            return true;
        }
        for (int b : array) {
            if (b < 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNoneNegative(long... array) {
        if (Arrays1.isEmpty(array)) {
            return true;
        }
        for (long b : array) {
            if (b < 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNoneNegative(float... array) {
        if (Arrays1.isEmpty(array)) {
            return true;
        }
        for (float b : array) {
            if (b < 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNoneNegative(double... array) {
        if (Arrays1.isEmpty(array)) {
            return true;
        }
        for (double b : array) {
            if (b < 0) {
                return false;
            }
        }
        return true;
    }

    // -------------------- isNaN --------------------

    /**
     * 是否为NaN
     *
     * @param x ignore
     * @return true 为NaN
     */
    public static boolean isNaN(Float x) {
        return x.isNaN();
    }

    public static boolean isNaN(Double x) {
        return x.isNaN();
    }

    /**
     * 是否不为NaN
     *
     * @param x ignore
     * @return true 不为NaN
     */
    public static boolean isNotNaN(Float x) {
        return !x.isNaN();
    }

    public static boolean isNotNaN(Double x) {
        return !x.isNaN();
    }

    /**
     * 是否全为NaN
     *
     * @param array array
     * @return true 全为NaN
     */
    public static boolean isAllNaN(float... array) {
        if (Arrays1.isEmpty(array)) {
            return false;
        }
        for (float b : array) {
            if (!Float.isNaN(b)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllNaN(double... array) {
        if (Arrays1.isEmpty(array)) {
            return false;
        }
        for (double b : array) {
            if (!Double.isNaN(b)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否全不为NaN
     *
     * @param array array
     * @return true 全不为NaN
     */
    public static boolean isNoneNaN(float... array) {
        if (Arrays1.isEmpty(array)) {
            return true;
        }
        for (float b : array) {
            if (Float.isNaN(b)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNoneNaN(double... array) {
        if (Arrays1.isEmpty(array)) {
            return true;
        }
        for (double b : array) {
            if (Double.isNaN(b)) {
                return false;
            }
        }
        return true;
    }

    // -------------------- compare --------------------

    /**
     * 比较接口
     *
     * @param x x
     * @param y y
     * @return -1 0 1
     */
    public static int compare(byte x, byte y) {
        return Byte.compare(x, y);
    }

    public static int compare(short x, short y) {
        return Short.compare(x, y);
    }

    public static int compare(int x, int y) {
        return Integer.compare(x, y);
    }

    public static int compare(long x, long y) {
        return Long.compare(x, y);
    }

    public static int compare(float x, float y) {
        return Float.compare(x, y);
    }

    public static int compare(double x, double y) {
        return Double.compare(x, y);
    }

    // -------------------- min --------------------

    /**
     * 最小值
     *
     * @param array array
     * @return 最小值
     */
    public static byte min(byte... array) {
        byte min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static short min(short... array) {
        short min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static int min(int... array) {
        int min = array[0];
        for (int j = 1; j < array.length; j++) {
            if (array[j] < min) {
                min = array[j];
            }
        }
        return min;
    }

    public static long min(long... array) {
        long min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static float min(float... array) {
        float min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (Float.isNaN(array[i])) {
                return Float.NaN;
            }
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static double min(double... array) {
        double min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (Double.isNaN(array[i])) {
                return Double.NaN;
            }
            if (array[i] < min) {
                min = array[i];
            }
        }

        return min;
    }

    // -------------------- max --------------------

    /**
     * 最大值
     *
     * @param array array
     * @return 最大值
     */
    public static byte max(byte... array) {
        byte max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static short max(short... array) {
        short max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static int max(int... array) {
        int max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (array[j] > max) {
                max = array[j];
            }
        }
        return max;
    }

    public static long max(long... array) {
        long max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (array[j] > max) {
                max = array[j];
            }
        }
        return max;
    }

    public static float max(float... array) {
        float max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (Float.isNaN(array[j])) {
                return Float.NaN;
            }
            if (array[j] > max) {
                max = array[j];
            }
        }
        return max;
    }

    public static double max(double... array) {
        double max = array[0];
        for (int j = 1; j < array.length; j++) {
            if (Double.isNaN(array[j])) {
                return Double.NaN;
            }
            if (array[j] > max) {
                max = array[j];
            }
        }
        return max;
    }

    // -------------------- sum --------------------

    public static long sum(byte... array) {
        long max = 0;
        for (int j = 0; j < array.length; j++) {
            max += array[j];
        }
        return max;
    }

    public static long sum(short... array) {
        long max = 0;
        for (int j = 0; j < array.length; j++) {
            max += array[j];
        }
        return max;
    }

    public static long sum(int... array) {
        long max = 0;
        for (int j = 0; j < array.length; j++) {
            max += array[j];
        }
        return max;
    }

    public static long sum(long... array) {
        long max = 0;
        for (int j = 0; j < array.length; j++) {
            max += array[j];
        }
        return max;
    }

    public static double sum(float... array) {
        double max = 0;
        for (int j = 0; j < array.length; j++) {
            if (!Float.isNaN(array[j])) {
                max += array[j];
            }
        }
        return max;
    }

    public static double sum(double... array) {
        double max = 0;
        for (int j = 0; j < array.length; j++) {
            if (!Double.isNaN(array[j])) {
                max += array[j];
            }
        }
        return max;
    }

    // -------------------- avg --------------------

    public static byte avg(byte... array) {
        int len = Arrays1.length(array);
        if (len == 0) {
            return 0;
        }
        return (byte) (sum(array) / len);
    }

    public static short avg(short... array) {
        int len = Arrays1.length(array);
        if (len == 0) {
            return 0;
        }
        return (short) (sum(array) / len);
    }

    public static int avg(int... array) {
        int len = Arrays1.length(array);
        if (len == 0) {
            return 0;
        }
        return (int) (sum(array) / len);
    }

    public static long avg(long... array) {
        int len = Arrays1.length(array);
        if (len == 0) {
            return 0;
        }
        return sum(array) / len;
    }

    public static float avg(float... array) {
        int len = Arrays1.length(array);
        if (len == 0) {
            return 0;
        }
        return (float) (sum(array) / len);
    }

    public static double avg(double... array) {
        int len = Arrays1.length(array);
        if (len == 0) {
            return 0;
        }
        return sum(array) / len;
    }

    // -------------------- range --------------------

    /**
     * 判断是否在区间内 开区间
     *
     * @param num   测试的数字
     * @param start 区间开始
     * @param end   区间结束
     * @return true 在区间内
     */
    public static boolean inRange(byte num, byte start, byte end) {
        return Compares.inRange(num, start, end);
    }

    public static boolean inRange(short num, short start, short end) {
        return Compares.inRange(num, start, end);
    }

    public static boolean inRange(int num, int start, int end) {
        return Compares.inRange(num, start, end);
    }

    public static boolean inRange(long num, long start, long end) {
        return Compares.inRange(num, start, end);
    }

    public static boolean inRange(float num, float start, float end) {
        return Compares.inRange(num, start, end);
    }

    public static boolean inRange(double num, double start, double end) {
        return Compares.inRange(num, start, end);
    }

    /**
     * 判断是否不在区间内 开区间
     *
     * @param num   测试的数字
     * @param start 区间开始
     * @param end   区间结束
     *              [1, 1] 1 true
     *              [1, 2] 1 true
     * @return true 不在区间内
     */
    public static boolean notInRange(byte num, byte start, byte end) {
        return !inRange(num, start, end);
    }

    public static boolean notInRange(short num, short start, short end) {
        return !inRange(num, start, end);
    }

    public static boolean notInRange(int num, int start, int end) {
        return !inRange(num, start, end);
    }

    public static boolean notInRange(long num, long start, long end) {
        return !inRange(num, start, end);
    }

    public static boolean notInRange(float num, float start, float end) {
        return !inRange(num, start, end);
    }

    public static boolean notInRange(double num, double start, double end) {
        return !inRange(num, start, end);
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
     * @return true 在区间内
     */
    public static boolean rangeInRange(byte rangeStart, byte rangeEnd, byte testRangeStart, byte testRangeEnd) {
        return Compares.rangeInRange(rangeStart, rangeEnd, testRangeStart, testRangeEnd);
    }

    public static boolean rangeInRange(short rangeStart, short rangeEnd, short testRangeStart, short testRangeEnd) {
        return Compares.rangeInRange(rangeStart, rangeEnd, testRangeStart, testRangeEnd);
    }

    public static boolean rangeInRange(int rangeStart, int rangeEnd, int testRangeStart, int testRangeEnd) {
        return Compares.rangeInRange(rangeStart, rangeEnd, testRangeStart, testRangeEnd);
    }

    public static boolean rangeInRange(long rangeStart, long rangeEnd, long testRangeStart, long testRangeEnd) {
        return Compares.rangeInRange(rangeStart, rangeEnd, testRangeStart, testRangeEnd);
    }

    public static boolean rangeInRange(float rangeStart, float rangeEnd, float testRangeStart, float testRangeEnd) {
        return Compares.rangeInRange(rangeStart, rangeEnd, testRangeStart, testRangeEnd);
    }

    public static boolean rangeInRange(double rangeStart, double rangeEnd, double testRangeStart, double testRangeEnd) {
        return Compares.rangeInRange(rangeStart, rangeEnd, testRangeStart, testRangeEnd);
    }

    /**
     * 判断一个区间是否不在另一个区间内 开区间
     *
     * @param rangeStart     区间开始
     * @param rangeEnd       区间结束
     * @param testRangeStart 测试的区间开始
     * @param testRangeEnd   测试的区间结束
     * @return true 不在区间内
     */
    public static boolean rangeNotInRange(byte rangeStart, byte rangeEnd, byte testRangeStart, byte testRangeEnd) {
        return !rangeInRange(rangeStart, rangeEnd, testRangeStart, testRangeEnd);
    }

    public static boolean rangeNotInRange(short rangeStart, short rangeEnd, short testRangeStart, short testRangeEnd) {
        return !rangeInRange(rangeStart, rangeEnd, testRangeStart, testRangeEnd);
    }

    public static boolean rangeNotInRange(int rangeStart, int rangeEnd, int testRangeStart, int testRangeEnd) {
        return !rangeInRange(rangeStart, rangeEnd, testRangeStart, testRangeEnd);
    }

    public static boolean rangeNotInRange(long rangeStart, long rangeEnd, long testRangeStart, long testRangeEnd) {
        return !rangeInRange(rangeStart, rangeEnd, testRangeStart, testRangeEnd);
    }

    public static boolean rangeNotInRange(float rangeStart, float rangeEnd, float testRangeStart, float testRangeEnd) {
        return !rangeInRange(rangeStart, rangeEnd, testRangeStart, testRangeEnd);
    }

    public static boolean rangeNotInRange(double rangeStart, double rangeEnd, double testRangeStart, double testRangeEnd) {
        return !rangeInRange(rangeStart, rangeEnd, testRangeStart, testRangeEnd);
    }

    // -------------------- cross --------------------

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
     * @return 是否有交差
     */
    public static boolean cross(byte start1, byte end1, byte start2, byte end2) {
        return Compares.cross(start1, end1, start2, end2);
    }

    public static boolean cross(short start1, short end1, short start2, short end2) {
        return Compares.cross(start1, end1, start2, end2);
    }

    public static boolean cross(int start1, int end1, int start2, int end2) {
        return Compares.cross(start1, end1, start2, end2);
    }

    public static boolean cross(long start1, long end1, long start2, long end2) {
        return Compares.cross(start1, end1, start2, end2);
    }

    public static boolean cross(float start1, float end1, float start2, float end2) {
        return Compares.cross(start1, end1, start2, end2);
    }

    public static boolean cross(double start1, double end1, double start2, double end2) {
        return Compares.cross(start1, end1, start2, end2);
    }

    /**
     * 不交叉
     *
     * @param start1 开始区间1
     * @param end1   结束区间1
     * @param start2 开始区间2
     * @param end2   结束区间2
     * @return 是否不交叉
     */
    public static boolean uncross(byte start1, byte end1, byte start2, byte end2) {
        return !cross(start1, end1, start2, end2);
    }

    public static boolean uncross(short start1, short end1, short start2, short end2) {
        return !cross(start1, end1, start2, end2);
    }

    public static boolean uncross(int start1, int end1, int start2, int end2) {
        return !cross(start1, end1, start2, end2);
    }

    public static boolean uncross(long start1, long end1, long start2, long end2) {
        return !cross(start1, end1, start2, end2);
    }

    public static boolean uncross(float start1, float end1, float start2, float end2) {
        return !cross(start1, end1, start2, end2);
    }

    public static boolean uncross(double start1, double end1, double start2, double end2) {
        return !cross(start1, end1, start2, end2);
    }

    // -------------------- scale --------------------

    /**
     * 格式化
     *
     * @param value  value
     * @param format format
     * @return formatter
     */
    public static String format(float value, String format) {
        return new DecimalFormat(format).format(value);
    }

    /**
     * 格式化
     *
     * @param value  value
     * @param format format
     * @return formatter
     */
    public static String format(double value, String format) {
        return new DecimalFormat(format).format(value);
    }

    /**
     * 清空小数
     *
     * @param f float
     * @return int
     */
    public static int cleanDecimal(float f) {
        return Integer.parseInt(new DecimalFormat("#").format(f));
    }

    /**
     * 清空小数
     *
     * @param d double
     * @return string
     */
    public static long cleanDecimal(double d) {
        return Long.parseLong(new DecimalFormat("#").format(d));
    }

    /**
     * 清空小数
     *
     * @param f float
     * @return string
     */
    public static String cleanFloatDecimal(float f) {
        return new DecimalFormat("#").format(f);
    }

    /**
     * 清空小数
     *
     * @param d double
     * @return string
     */
    public static String cleanDoubleDecimal(double d) {
        return new DecimalFormat("#").format(d);
    }

    /**
     * 设置小数位
     *
     * @param d                  double
     * @param precision          小数位
     * @param forceTrailingZeros 是否强制填充 0
     * @return string
     */
    public static String formatPrecision(double d, int precision, boolean forceTrailingZeros) {
        return getFormatter(precision, forceTrailingZeros).format(d);
    }

    /**
     * 设置小数位
     *
     * @param f                  float
     * @param precision          小数位
     * @param forceTrailingZeros 是否强制填充 0
     * @return string
     */
    public static String formatPrecision(float f, int precision, boolean forceTrailingZeros) {
        return getFormatter(precision, forceTrailingZeros).format(f);
    }

    /**
     * 设置小数位
     *
     * @param b                  float
     * @param precision          小数位
     * @param forceTrailingZeros 是否强制填充 0
     * @return string
     */
    public static String formatPrecision(BigDecimal b, int precision, boolean forceTrailingZeros) {
        return getFormatter(precision, forceTrailingZeros).format(b);
    }

    /**
     * 获取小数格式
     *
     * @param precision          precision
     * @param forceTrailingZeros 是否强制填充 0
     * @return formatter
     */
    public static DecimalFormat getFormatter(int precision, boolean forceTrailingZeros) {
        StringBuilder pattern = new StringBuilder("0");
        if (precision > 0) {
            pattern.append(".");
            for (int i = 0; i < precision; i++) {
                pattern.append(forceTrailingZeros ? "0" : "#");
            }
        }
        return new DecimalFormat(pattern.toString());
    }

    /**
     * 是否有小数位
     *
     * @param f float
     * @return true有
     */
    public static boolean isDecimal(float f) {
        return Float.compare(f, ((float) (int) f)) != 0;
    }

    /**
     * 是否有小数位
     *
     * @param d double
     * @return true有
     */
    public static boolean isDecimal(double d) {
        return Double.compare(d, ((double) (long) d)) != 0;
    }

    /**
     * 获取最小二次幂
     *
     * @param c num
     * @return 最小二次幂 6 -> 8
     */
    public static int getMin2Power(int c) {
        int n = c - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return n + 1;
    }

}
