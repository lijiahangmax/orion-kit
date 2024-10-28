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
package cn.orionsec.kit.lang.utils.math;

import cn.orionsec.kit.lang.utils.Arrays1;
import cn.orionsec.kit.lang.utils.Compares;
import cn.orionsec.kit.lang.utils.Objects1;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * BigDecimal 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/9/4 11:37
 */
public class BigDecimals {

    private BigDecimals() {
    }

    /**
     * 格式化
     *
     * @param format 要格式化成的格式 #.00, #.#
     * @return 格式化的字符串
     */
    public static String format(Object o, String format) {
        try {
            DecimalFormat df = new DecimalFormat(format);
            return df.format(objectToDecimal(o));
        } catch (Exception e) {
            return String.valueOf(o);
        }
    }

    /**
     * Object -> BigDecimal
     *
     * @param o Object
     * @return BigDecimal
     */
    public static BigDecimal toBigDecimal(Object o) {
        return objectToDecimal(o);
    }

    /**
     * Object -> BigDecimal
     *
     * @param o        Object
     * @param defaultV 默认值
     * @return BigDecimal
     */
    public static BigDecimal toBigDecimal(Object o, BigDecimal defaultV) {
        return Objects1.def(objectToDecimal(o), defaultV);
    }

    /**
     * Object[] -> BigDecimals[]
     *
     * @param o Object[]
     * @return BigDecimals[]
     */
    public static BigDecimal[] toBigDecimals(Object... o) {
        int length = Arrays1.length(o);
        if (length == 0) {
            return new BigDecimal[0];
        }
        BigDecimal[] r = new BigDecimal[length];
        for (int i = 0; i < length; i++) {
            r[i] = objectToDecimal(o[i]);
        }
        return r;
    }

    /**
     * Object -> BigDecimal
     *
     * @param o Object
     * @return BigDecimal
     */
    private static BigDecimal objectToDecimal(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof BigDecimal) {
            return (BigDecimal) o;
        } else if (o instanceof String) {
            return new BigDecimal((String) o);
        } else if (o instanceof Byte) {
            return new BigDecimal((Byte) o);
        } else if (o instanceof Short) {
            return new BigDecimal((Short) o);
        } else if (o instanceof Integer) {
            return new BigDecimal((Integer) o);
        } else if (o instanceof Long) {
            return BigDecimal.valueOf((Long) o);
        } else if (o instanceof Float) {
            return BigDecimal.valueOf((Float) o);
        } else if (o instanceof Double) {
            return BigDecimal.valueOf((Double) o);
        } else if (o instanceof Boolean) {
            if ((Boolean) o) {
                return BigDecimal.ONE;
            } else {
                return BigDecimal.ZERO;
            }
        } else if (o instanceof Character) {
            return new BigDecimal((o.toString()));
        } else if (o instanceof char[]) {
            return new BigDecimal((char[]) o);
        } else if (o instanceof BigInteger) {
            return new BigDecimal((BigInteger) o);
        }
        return null;
    }

    /**
     * BigDecimal -> String
     *
     * @param b BigDecimal
     * @return String
     */
    public static String toStr(BigDecimal b) {
        return toStr(b, null, 2, RoundingMode.DOWN);
    }

    /**
     * BigDecimal -> String
     *
     * @param b        BigDecimal
     * @param defaultV 默认值
     * @return String
     */
    public static String toStr(BigDecimal b, String defaultV) {
        return toStr(b, defaultV, 2, RoundingMode.DOWN);
    }

    /**
     * BigDecimal -> String
     *
     * @param b        BigDecimal
     * @param defaultV 默认值
     * @param length   小数点长度
     * @return String
     */
    public static String toStr(BigDecimal b, String defaultV, int length) {
        return toStr(b, defaultV, length, RoundingMode.DOWN);
    }

    /**
     * BigDecimal -> String
     *
     * @param b        BigDecimal
     * @param defaultV 默认值
     * @param length   小数点长度
     * @param mode     舍入模式
     * @return String
     */
    public static String toStr(BigDecimal b, String defaultV, int length, RoundingMode mode) {
        if (b == null) {
            return defaultV;
        }
        if (mode == null) {
            return b.setScale(length).toPlainString();
        } else {
            return b.setScale(length, mode).toPlainString();
        }
    }

    /**
     * BigDecimal -> Long
     *
     * @param b BigDecimal
     * @return Long
     */
    public static Long toLong(BigDecimal b) {
        return toLong(b, 0L);
    }

    /**
     * BigDecimal -> Long
     *
     * @param b        BigDecimal
     * @param defaultV 默认值
     * @return Long
     */
    public static Long toLong(BigDecimal b, Long defaultV) {
        if (b == null) {
            return defaultV;
        }
        return b.longValue();
    }

    /**
     * BigDecimal -> Integer
     *
     * @param b BigDecimal
     * @return Integer
     */
    public static Integer toInteger(BigDecimal b) {
        return toInteger(b, 0);
    }

    /**
     * BigDecimal -> Integer
     *
     * @param b        BigDecimal
     * @param defaultV 默认值
     * @return Integer
     */
    public static Integer toInteger(BigDecimal b, Integer defaultV) {
        if (b == null) {
            return defaultV;
        }
        return b.intValue();
    }

    /**
     * BigDecimal -> Double
     *
     * @param b BigDecimal
     * @return Double
     */
    public static Double toDouble(BigDecimal b) {
        return toDouble(b, 0.00, 2, RoundingMode.DOWN);
    }

    /**
     * BigDecimal -> Double
     *
     * @param b        BigDecimal
     * @param defaultV 默认值
     * @return Double
     */
    public static Double toDouble(BigDecimal b, Double defaultV) {
        return toDouble(b, defaultV, 2, RoundingMode.DOWN);
    }

    /**
     * BigDecimal -> Double
     *
     * @param b        BigDecimal
     * @param defaultV 默认值
     * @param length   小数点长度
     * @return Double
     */
    public static Double toDouble(BigDecimal b, Double defaultV, int length) {
        return toDouble(b, defaultV, length, RoundingMode.DOWN);
    }

    /**
     * BigDecimal -> Double
     *
     * @param b        BigDecimal
     * @param defaultV 默认值
     * @param length   小数点长度
     * @param mode     舍入模式
     * @return Double
     */
    public static Double toDouble(BigDecimal b, Double defaultV, int length, RoundingMode mode) {
        if (b == null) {
            return defaultV;
        }
        if (mode == null) {
            return b.setScale(length).doubleValue();
        } else {
            return b.setScale(length, mode).doubleValue();
        }
    }

    /**
     * 相加
     *
     * @param s 加数
     * @param b 加数
     * @return 和
     */
    public static BigDecimal add(BigDecimal s, BigDecimal... b) {
        return add(false, s, b);
    }

    /**
     * 相加(跳过负数)
     *
     * @param s 加数
     * @param b 加数
     * @return 和
     */
    public static BigDecimal addSkipNegative(BigDecimal s, BigDecimal... b) {
        return add(true, s, b);
    }

    /**
     * 相加
     *
     * @param skipNegative 跳过负数
     * @param s            加数
     * @param b            加数
     * @return 和
     */
    private static BigDecimal add(boolean skipNegative, BigDecimal s, BigDecimal... b) {
        if (s == null) {
            s = BigDecimal.ZERO;
        }
        if (Arrays1.length(b) == 0) {
            return s;
        }
        for (BigDecimal c : b) {
            if (c != null) {
                if (skipNegative) {
                    if (c.compareTo(BigDecimal.ZERO) > 0) {
                        s = s.add(c);
                    }
                } else {
                    s = s.add(c);
                }
            }
        }
        return s;
    }

    /**
     * 相减
     *
     * @param s 被减数
     * @param b 减数
     * @return 差
     */
    public static BigDecimal subtract(BigDecimal s, BigDecimal... b) {
        return subtract(false, s, b);
    }

    /**
     * 相减(跳过负数)
     *
     * @param s 被减数
     * @param b 减数
     * @return 差
     */
    public static BigDecimal subtractSkipNegative(BigDecimal s, BigDecimal... b) {
        return subtract(true, s, b);
    }

    /**
     * 相减
     *
     * @param skipNegative 跳过负数
     * @param s            被减数
     * @param b            减数
     * @return 差
     */
    private static BigDecimal subtract(boolean skipNegative, BigDecimal s, BigDecimal... b) {
        if (s == null) {
            s = BigDecimal.ZERO;
        }
        if (Arrays1.length(b) == 0) {
            return s;
        }
        for (BigDecimal c : b) {
            if (c != null) {
                if (skipNegative) {
                    if (c.compareTo(BigDecimal.ZERO) > 0) {
                        s = s.subtract(c);
                    }
                } else {
                    s = s.subtract(c);
                }
            }
        }
        return s;
    }

    /**
     * 相乘
     *
     * @param s 乘数
     * @param b 乘数
     * @return 积
     */
    public static BigDecimal multiply(BigDecimal s, BigDecimal... b) {
        return multiply(false, false, s, b);
    }

    /**
     * 相乘 (跳过负数, 0)
     *
     * @param s 乘数
     * @param b 乘数
     * @return 积
     */
    public static BigDecimal multiplySkip(BigDecimal s, BigDecimal... b) {
        return multiply(true, true, s, b);
    }

    /**
     * 相乘(跳过负数)
     *
     * @param s 乘数
     * @param b 乘数
     * @return 积
     */
    public static BigDecimal multiplySkipNegative(BigDecimal s, BigDecimal... b) {
        return multiply(true, false, s, b);
    }

    /**
     * 相乘(跳过0)
     *
     * @param s 乘数
     * @param b 乘数
     * @return 积
     */
    public static BigDecimal multiplySkipZero(BigDecimal s, BigDecimal... b) {
        return multiply(false, true, s, b);
    }

    /**
     * 相乘
     *
     * @param skipNegative 跳过负数
     * @param skipZero     跳过0
     * @param s            乘数
     * @param b            乘数
     * @return 差
     */
    private static BigDecimal multiply(boolean skipNegative, boolean skipZero, BigDecimal s, BigDecimal... b) {
        if (s == null) {
            s = BigDecimal.ZERO;
        }
        if (Arrays1.length(b) == 0) {
            return s;
        }
        for (BigDecimal c : b) {
            if (c != null) {
                if (skipNegative || skipZero) {
                    int compare = c.compareTo(BigDecimal.ZERO);
                    if (compare > 0) {
                        s = s.multiply(c);
                    } else if (compare == 0) {
                        if (!skipZero) {
                            s = s.multiply(c);
                        }
                    } else {
                        if (!skipNegative) {
                            s = s.multiply(c);
                        }
                    }
                } else {
                    s = s.multiply(c);
                }
            }
        }
        return s;
    }

    /**
     * 相除
     *
     * @param s 被除数
     * @param b 除数
     * @return 商
     */
    public static BigDecimal divide(BigDecimal s, BigDecimal... b) {
        return divide(false, false, s, b);
    }

    /**
     * 相除 (跳过负数, 0)
     *
     * @param s 被除数
     * @param b 除数
     * @return 商
     */
    public static BigDecimal divideSkip(BigDecimal s, BigDecimal... b) {
        return divide(true, true, s, b);
    }

    /**
     * 相除(跳过负数)
     *
     * @param s 被除数
     * @param b 除数
     * @return 商
     */
    public static BigDecimal divideSkipNegative(BigDecimal s, BigDecimal... b) {
        return divide(true, false, s, b);
    }

    /**
     * 相除(跳过0)
     *
     * @param s 被除数
     * @param b 除数
     * @return 商
     */
    public static BigDecimal divideSkipZero(BigDecimal s, BigDecimal... b) {
        return divide(false, true, s, b);
    }

    /**
     * 相除
     *
     * @param skipNegative 跳过负数
     * @param skipZero     跳过0, 不报错
     * @param s            被除数
     * @param b            除数
     * @return 商
     */
    private static BigDecimal divide(boolean skipNegative, boolean skipZero, BigDecimal s, BigDecimal... b) {
        if (s == null) {
            return BigDecimal.ZERO;
        }
        if (Arrays1.length(b) == 0) {
            return s;
        }
        for (BigDecimal c : b) {
            if (c != null) {
                if (skipNegative || skipZero) {
                    int compare = c.compareTo(BigDecimal.ZERO);
                    if (compare > 0) {
                        s = s.divide(c);
                    } else if (compare == 0) {
                        if (!skipZero) {
                            s = s.divide(c);
                        }
                    } else {
                        if (!skipNegative) {
                            s = s.divide(c);
                        }
                    }
                } else {
                    s = s.divide(c);
                }
            }
        }
        return s;
    }

    /**
     * 最大值
     *
     * @param b ignore
     * @return 最大值
     */
    public static BigDecimal max(BigDecimal... b) {
        int len = Arrays1.length(b);
        if (len == 0) {
            return null;
        }
        BigDecimal max = b[0];
        int offset = 1;
        if (max == null) {
            for (; offset < len; offset++) {
                BigDecimal bi = b[offset];
                if (bi != null) {
                    max = bi;
                    offset++;
                    break;
                }
            }
            if (max == null) {
                return null;
            }
        }
        for (int i = offset; i < len; i++) {
            if (b[i] != null && max.compareTo(b[i]) < 0) {
                max = b[i];
            }
        }
        return max;
    }

    /**
     * 最小值
     *
     * @param b ignore
     * @return 最大值
     */
    public static BigDecimal min(BigDecimal... b) {
        int len = Arrays1.length(b);
        if (len == 0) {
            return null;
        }
        BigDecimal min = b[0];
        int offset = 1;
        if (min == null) {
            for (; offset < len; offset++) {
                BigDecimal bi = b[offset];
                if (bi != null) {
                    min = bi;
                    offset++;
                    break;
                }
            }
            if (min == null) {
                return null;
            }
        }
        for (int i = offset; i < len; i++) {
            if (b[i] != null && min.compareTo(b[i]) > 0) {
                min = b[i];
            }
        }
        return min;
    }

    /**
     * 总和
     *
     * @param b ignore
     * @return 总和
     */
    public static BigDecimal sum(BigDecimal... b) {
        return sum(false, b);
    }

    /**
     * 总和 跳过负数
     *
     * @param b ignore
     * @return 总和
     */
    public static BigDecimal sumSkipNegative(BigDecimal... b) {
        return sum(true, b);
    }

    /**
     * 总和
     *
     * @param skipNegative 跳过负数
     * @param b            ignore
     * @return 总和
     */
    private static BigDecimal sum(boolean skipNegative, BigDecimal... b) {
        BigDecimal sum = BigDecimal.ZERO;
        int len = Arrays1.length(b);
        for (int i = 0; i < len; i++) {
            BigDecimal d = b[i];
            if (d.compareTo(BigDecimal.ZERO) < 0) {
                if (!skipNegative) {
                    sum = sum.add(d);
                }
            } else {
                sum = sum.add(d);
            }
        }
        return sum;
    }

    /**
     * 平均值
     *
     * @param b ignore
     * @return 平均值
     */
    public static BigDecimal avg(BigDecimal... b) {
        return avg(false, false, b);
    }

    /**
     * 平均值 跳过0
     *
     * @param b ignore
     * @return 平均值
     */
    public static BigDecimal avgSkipZero(BigDecimal... b) {
        return avg(true, false, b);
    }

    /**
     * 平均值 跳过负数
     *
     * @param b ignore
     * @return 平均值
     */
    public static BigDecimal avgSkipNegative(BigDecimal... b) {
        return avg(false, true, b);
    }

    /**
     * 平均值 跳过0和负数
     *
     * @param b ignore
     * @return 平均值
     */
    public static BigDecimal avgSkip(BigDecimal... b) {
        return avg(true, true, b);
    }

    /**
     * 平均值
     *
     * @param skipZero     跳过0
     * @param skipNegative 跳过负数
     * @param b            ignore
     * @return 平均值
     */
    private static BigDecimal avg(boolean skipZero, boolean skipNegative, BigDecimal... b) {
        BigDecimal c;
        int len = Arrays1.length(b);
        int skip = 0;
        if (len == 0) {
            return null;
        }
        if (skipZero && skipNegative) {
            for (BigDecimal d : b) {
                if (d.compareTo(BigDecimal.ZERO) <= 0) {
                    skip++;
                }
            }
        } else if (skipZero) {
            for (BigDecimal d : b) {
                if (d.compareTo(BigDecimal.ZERO) == 0) {
                    skip++;
                }
            }
        } else if (skipNegative) {
            for (BigDecimal d : b) {
                if (d.compareTo(BigDecimal.ZERO) < 0) {
                    skip++;
                }
            }
        }

        if (skipNegative) {
            c = add(true, null, b);
        } else {
            c = add(false, null, b);
        }
        if (c.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        return c.divide(new BigDecimal(len - skip), 10, RoundingMode.DOWN);
    }

    /**
     * 等于
     *
     * @param d1 ignore
     * @param d2 ignore
     * @return 等于true
     */
    public static boolean eq(BigDecimal d1, BigDecimal d2) {
        return Objects1.eq(d1, d2);
    }

    /**
     * 等于
     *
     * @param d1 ignore
     * @param d2 ignore
     * @return 等于true
     */
    public static boolean compared(BigDecimal d1, BigDecimal d2) {
        return Compares.compared(d1, d2);
    }

    /**
     * 是否有小数位
     *
     * @param d BigDecimal
     * @return true有
     */
    public static boolean isDecimal(BigDecimal d) {
        return Double.compare(d.doubleValue(), ((double) d.longValue())) != 0;
    }

    /**
     * 是否为负数
     *
     * @param d ignore
     * @return true负数
     */
    public static boolean isNegative(BigDecimal d) {
        if (d == null) {
            return false;
        }
        return d.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * 返回绝对值
     *
     * @param d ignore
     * @return 绝对值
     */
    public static BigDecimal abs(BigDecimal d) {
        if (d == null) {
            return null;
        }
        return d.abs();
    }

    /**
     * 是否为0
     *
     * @param d ignore
     * @return 是否为0
     */
    public static boolean isZero(BigDecimal d) {
        if (d == null) {
            return false;
        }
        return BigDecimal.ZERO.equals(d);
    }

    /**
     * 是否小于0
     *
     * @param d d
     * @return true 小于0
     */
    public static boolean ltZero(BigDecimal d) {
        if (d == null) {
            return false;
        }
        return d.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * 是否小于等于0
     *
     * @param d d
     * @return true 小于等于0
     */
    public static boolean lteZero(BigDecimal d) {
        if (d == null) {
            return false;
        }
        return d.compareTo(BigDecimal.ZERO) <= 0;
    }

    /**
     * 是否大于0
     *
     * @param d d
     * @return true 大于0
     */
    public static boolean gtZero(BigDecimal d) {
        if (d == null) {
            return false;
        }
        return d.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * 是否大于等于0
     *
     * @param d d
     * @return true 大于等于0
     */
    public static boolean gteZero(BigDecimal d) {
        if (d == null) {
            return false;
        }
        return d.compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * 格式化解析
     *
     * @param value  value
     * @param format 格式
     * @return value
     */
    public static BigDecimal parse(String value, String format) {
        return parse(value, format, null);
    }

    /**
     * 格式化解析
     *
     * @param value    value
     * @param format   格式
     * @param defaultV 默认值
     * @return value
     */
    public static BigDecimal parse(String value, String format, BigDecimal defaultV) {
        if (value == null) {
            return defaultV;
        }
        try {
            return objectToDecimal(new DecimalFormat(format).parse(value));
        } catch (Exception e) {
            return defaultV;
        }
    }

}
