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

/**
 * BigInteger 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/9/9 11:47
 */
public class BigIntegers {

    private BigIntegers() {
    }

    /**
     * Object -> BigInteger
     *
     * @param o Object
     * @return BigInteger
     */
    public static BigInteger toBigInteger(Object o) {
        return objectToBigInteger(o);
    }

    /**
     * Object -> BigInteger
     *
     * @param o        Object
     * @param defaultV 默认值
     * @return BigInteger
     */
    public static BigInteger toBigInteger(Object o, BigInteger defaultV) {
        return Objects1.def(objectToBigInteger(o), defaultV);
    }

    /**
     * Object[] -> BigIntegers[]
     *
     * @param o Object[]
     * @return BigIntegers[]
     */
    public static BigInteger[] toBigIntegers(Object... o) {
        if (Arrays1.length(o) == 0) {
            return new BigInteger[]{};
        }
        BigInteger[] r = new BigInteger[o.length];
        for (int i = 0, len = o.length; i < len; i++) {
            r[i] = objectToBigInteger(o[i]);
        }
        return r;
    }

    /**
     * Object -> BigInteger
     *
     * @param o Object
     * @return BigInteger
     */
    private static BigInteger objectToBigInteger(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof BigInteger) {
            return (BigInteger) o;
        } else if (o instanceof BigDecimal) {
            return BigInteger.valueOf(((BigDecimal) o).longValue());
        } else if (o instanceof String) {
            return new BigInteger((String) o);
        } else if (o instanceof Boolean) {
            if ((Boolean) o) {
                return BigInteger.ONE;
            } else {
                return BigInteger.ZERO;
            }
        } else if (o instanceof Long) {
            return BigInteger.valueOf((Long) o);
        }
        return new BigInteger(o.toString());
    }

    /**
     * BigInteger -> String
     *
     * @param b BigInteger
     * @return String
     */
    public static String toStr(BigInteger b) {
        return toStr(b, null, 10);
    }

    /**
     * BigInteger -> String
     *
     * @param b        BigInteger
     * @param defaultV 默认值
     * @return String
     */
    public static String toStr(BigInteger b, String defaultV) {
        return toStr(b, defaultV, 10);
    }

    /**
     * BigInteger -> String
     *
     * @param b        BigInteger
     * @param defaultV 默认值
     * @param radix    进制
     * @return String
     */
    public static String toStr(BigInteger b, String defaultV, int radix) {
        if (b == null) {
            return defaultV;
        }
        return b.toString(radix);
    }

    /**
     * BigInteger -> Long
     *
     * @param b BigInteger
     * @return Long
     */
    public static Long toLong(BigInteger b) {
        return toLong(b, 0L);
    }

    /**
     * BigInteger -> Long
     *
     * @param b        BigInteger
     * @param defaultV 默认值
     * @return Long
     */
    public static Long toLong(BigInteger b, Long defaultV) {
        if (b == null) {
            return defaultV;
        }
        return b.longValue();
    }

    /**
     * BigInteger -> Integer
     *
     * @param b BigInteger
     * @return Integer
     */
    public static Integer toInteger(BigInteger b) {
        return toInteger(b, 0);
    }

    /**
     * BigInteger -> Integer
     *
     * @param b        BigInteger
     * @param defaultV 默认值
     * @return Integer
     */
    public static Integer toInteger(BigInteger b, Integer defaultV) {
        if (b == null) {
            return defaultV;
        }
        return b.intValue();
    }

    /**
     * BigInteger -> Double
     *
     * @param b BigInteger
     * @return Double
     */
    public static Double toDouble(BigInteger b) {
        return toDouble(b, 0.00);
    }

    /**
     * BigInteger -> Double
     *
     * @param b        BigInteger
     * @param defaultV 默认值
     * @return Double
     */
    public static Double toDouble(BigInteger b, Double defaultV) {
        if (b == null) {
            return defaultV;
        }
        return b.doubleValue();
    }

    /**
     * 相加
     *
     * @param s 加数
     * @param b 加数
     * @return 和
     */
    public static BigInteger add(BigInteger s, BigInteger... b) {
        return add(false, s, b);
    }

    /**
     * 相加(跳过负数)
     *
     * @param s 加数
     * @param b 加数
     * @return 和
     */
    public static BigInteger addSkipNegative(BigInteger s, BigInteger... b) {
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
    private static BigInteger add(boolean skipNegative, BigInteger s, BigInteger... b) {
        if (s == null) {
            s = BigInteger.ZERO;
        }
        if (Arrays1.length(b) == 0) {
            return s;
        }
        for (BigInteger c : b) {
            if (c != null) {
                if (skipNegative) {
                    if (c.compareTo(BigInteger.ZERO) > 0) {
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
    public static BigInteger subtract(BigInteger s, BigInteger... b) {
        return subtract(false, s, b);
    }

    /**
     * 相减(跳过负数)
     *
     * @param s 被减数
     * @param b 减数
     * @return 差
     */
    public static BigInteger subtractSkipNegative(BigInteger s, BigInteger... b) {
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
    private static BigInteger subtract(boolean skipNegative, BigInteger s, BigInteger... b) {
        if (s == null) {
            s = BigInteger.ZERO;
        }
        if (Arrays1.length(b) == 0) {
            return s;
        }
        for (BigInteger c : b) {
            if (c != null) {
                if (skipNegative) {
                    if (c.compareTo(BigInteger.ZERO) > 0) {
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
     * @param s 被减数
     * @param b 减数
     * @return 差
     */
    public static BigInteger multiply(BigInteger s, BigInteger... b) {
        return multiply(false, false, s, b);
    }

    /**
     * 相乘 (跳过负数, 0)
     *
     * @param s 乘数
     * @param b 乘数
     * @return 积
     */
    public static BigInteger multiplySkip(BigInteger s, BigInteger... b) {
        return multiply(true, true, s, b);
    }

    /**
     * 相乘(跳过负数)
     *
     * @param s 乘数
     * @param b 乘数
     * @return 积
     */
    public static BigInteger multiplySkipNegative(BigInteger s, BigInteger... b) {
        return multiply(true, false, s, b);
    }

    /**
     * 相乘(跳过0)
     *
     * @param s 乘数
     * @param b 乘数
     * @return 积
     */
    public static BigInteger multiplySkipZero(BigInteger s, BigInteger... b) {
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
    private static BigInteger multiply(boolean skipNegative, boolean skipZero, BigInteger s, BigInteger... b) {
        if (s == null) {
            s = BigInteger.ZERO;
        }
        if (Arrays1.length(b) == 0) {
            return s;
        }
        for (BigInteger c : b) {
            if (c != null) {
                if (skipNegative || skipZero) {
                    int compare = c.compareTo(BigInteger.ZERO);
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
    public static BigInteger divide(BigInteger s, BigInteger... b) {
        return divide(false, false, s, b);
    }

    /**
     * 相除 (跳过负数, 0)
     *
     * @param s 被除数
     * @param b 除数
     * @return 商
     */
    public static BigInteger divideSkip(BigInteger s, BigInteger... b) {
        return divide(true, true, s, b);
    }

    /**
     * 相除(跳过负数)
     *
     * @param s 被除数
     * @param b 除数
     * @return 商
     */
    public static BigInteger divideSkipNegative(BigInteger s, BigInteger... b) {
        return divide(true, false, s, b);
    }

    /**
     * 相除(跳过0)
     *
     * @param s 被除数
     * @param b 除数
     * @return 商
     */
    public static BigInteger divideSkipZero(BigInteger s, BigInteger... b) {
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
    private static BigInteger divide(boolean skipNegative, boolean skipZero, BigInteger s, BigInteger... b) {
        if (s == null) {
            return BigInteger.ZERO;
        }
        if (Arrays1.length(b) == 0) {
            return s;
        }
        for (BigInteger c : b) {
            if (c != null) {
                if (skipNegative || skipZero) {
                    int compare = c.compareTo(BigInteger.ZERO);
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
    public static BigInteger max(BigInteger... b) {
        int len = Arrays1.length(b);
        if (len == 0) {
            return null;
        }
        BigInteger max = b[0];
        int offset = 1;
        if (max == null) {
            for (; offset < len; offset++) {
                BigInteger bi = b[offset];
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
    public static BigInteger min(BigInteger... b) {
        int len = Arrays1.length(b);
        if (len == 0) {
            return null;
        }
        BigInteger min = b[0];
        int offset = 1;
        if (min == null) {
            for (; offset < len; offset++) {
                BigInteger bi = b[offset];
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
    public static BigInteger sum(BigInteger... b) {
        return sum(false, b);
    }

    /**
     * 总和 跳过负数
     *
     * @param b ignore
     * @return 总和
     */
    public static BigInteger sumSkipNegative(BigInteger... b) {
        return sum(true, b);
    }

    /**
     * 总和
     *
     * @param skipNegative 跳过负数
     * @param b            ignore
     * @return 总和
     */
    private static BigInteger sum(boolean skipNegative, BigInteger... b) {
        BigInteger sum = BigInteger.ZERO;
        int len = Arrays1.length(b);
        for (int i = 0; i < len; i++) {
            BigInteger d = b[i];
            if (d.compareTo(BigInteger.ZERO) < 0) {
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
    public static BigInteger avg(BigInteger... b) {
        return avg(false, false, b);
    }

    /**
     * 平均值 跳过0
     *
     * @param b ignore
     * @return 平均值
     */
    public static BigInteger avgSkipZero(BigInteger... b) {
        return avg(true, false, b);
    }

    /**
     * 平均值 跳过负数
     *
     * @param b ignore
     * @return 平均值
     */
    public static BigInteger avgSkipNegative(BigInteger... b) {
        return avg(false, true, b);
    }

    /**
     * 平均值 跳过0和负数
     *
     * @param b ignore
     * @return 平均值
     */
    public static BigInteger avgSkip(BigInteger... b) {
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
    private static BigInteger avg(boolean skipZero, boolean skipNegative, BigInteger... b) {
        BigInteger c;
        int len = Arrays1.length(b);
        int skip = 0;
        if (len == 0) {
            return null;
        }
        if (skipZero && skipNegative) {
            for (BigInteger d : b) {
                if (d.compareTo(BigInteger.ZERO) <= 0) {
                    skip++;
                }
            }
        } else if (skipZero) {
            for (BigInteger d : b) {
                if (d.compareTo(BigInteger.ZERO) == 0) {
                    skip++;
                }
            }
        } else if (skipNegative) {
            for (BigInteger d : b) {
                if (d.compareTo(BigInteger.ZERO) < 0) {
                    skip++;
                }
            }
        }

        if (skipNegative) {
            c = add(true, null, b);
        } else {
            c = add(false, null, b);
        }
        return c.divide(BigInteger.valueOf(len - skip));
    }

    /**
     * 等于
     *
     * @param d1 ignore
     * @param d2 ignore
     * @return 等于true
     */
    public static boolean eq(BigInteger d1, BigInteger d2) {
        return Objects1.eq(d1, d2);
    }

    /**
     * 等于
     *
     * @param d1 ignore
     * @param d2 ignore
     * @return 等于true
     */
    public static boolean compared(BigInteger d1, BigInteger d2) {
        return Compares.compared(d1, d2);
    }

    /**
     * 是否为负数
     *
     * @param d ignore
     * @return true负数
     */
    public static boolean isNegative(BigInteger d) {
        if (d == null) {
            return false;
        }
        return d.compareTo(BigInteger.ZERO) < 0;
    }

    /**
     * 返回绝对值
     *
     * @param d ignore
     * @return 绝对值
     */
    public static BigInteger abs(BigInteger d) {
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
    public static boolean isZero(BigInteger d) {
        if (d == null) {
            return false;
        }
        return BigInteger.ZERO.equals(d);
    }

    /**
     * 是否小于0
     *
     * @param d d
     * @return true 小于0
     */
    public static boolean ltZero(BigInteger d) {
        if (d == null) {
            return false;
        }
        return d.compareTo(BigInteger.ZERO) < 0;
    }

    /**
     * 是否小于等于0
     *
     * @param d d
     * @return true 小于等于0
     */
    public static boolean lteZero(BigInteger d) {
        if (d == null) {
            return false;
        }
        return d.compareTo(BigInteger.ZERO) <= 0;
    }

    /**
     * 是否大于0
     *
     * @param d d
     * @return true 大于0
     */
    public static boolean gtZero(BigInteger d) {
        if (d == null) {
            return false;
        }
        return d.compareTo(BigInteger.ZERO) > 0;
    }

    /**
     * 是否大于等于0
     *
     * @param d d
     * @return true 大于等于0
     */
    public static boolean gteZero(BigInteger d) {
        if (d == null) {
            return false;
        }
        return d.compareTo(BigInteger.ZERO) >= 0;
    }

}
