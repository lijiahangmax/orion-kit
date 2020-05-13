package com.orion.utils.math;

import com.orion.utils.Arrays1;
import com.orion.utils.Objects1;

import java.math.BigInteger;

/**
 * BigInteger工具类
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/9/9 11:47
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
        if (o == null) {
            return defaultV;
        }
        return objectToBigInteger(o);
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
     * @param length   进制
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
        return toLong(b, 0L, false);
    }

    /**
     * BigInteger -> Long
     *
     * @param b        BigInteger
     * @param defaultV 默认值
     * @return Long
     */
    public static Long toLong(BigInteger b, Long defaultV) {
        return toLong(b, defaultV, false);
    }

    /**
     * BigInteger -> Long
     *
     * @param b        BigInteger
     * @param defaultV 默认值
     * @param exact    精确模式
     * @return Long
     */
    public static Long toLong(BigInteger b, Long defaultV, boolean exact) {
        if (b == null) {
            return defaultV;
        }
        if (exact) {
            return b.longValueExact();
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
        return toInteger(b, 0, false);
    }

    /**
     * BigInteger -> Integer
     *
     * @param b        BigInteger
     * @param defaultV 默认值
     * @return Integer
     */
    public static Integer toInteger(BigInteger b, Integer defaultV) {
        return toInteger(b, defaultV, false);
    }

    /**
     * BigInteger -> Integer
     *
     * @param b        BigInteger
     * @param defaultV 默认值
     * @param exact    精确模式
     * @return Integer
     */
    public static Integer toInteger(BigInteger b, Integer defaultV, boolean exact) {
        if (b == null) {
            return defaultV;
        }
        if (exact) {
            return b.intValueExact();
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
    public static BigInteger addSkipMinus(BigInteger s, BigInteger... b) {
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
    public static BigInteger subtractSkipMinus(BigInteger s, BigInteger... b) {
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
    public static BigInteger multiplySkipMinus(BigInteger s, BigInteger... b) {
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
    public static BigInteger divideSkipMinus(BigInteger s, BigInteger... b) {
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
        BigInteger max = null;
        int len = Arrays1.length(b);
        if (len == 0) {
            return null;
        }
        max = b[0];
        int offset = 1;
        if (max == null) {
            for (int blen = b.length; offset < blen; offset++) {
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
        BigInteger min = null;
        int len = Arrays1.length(b);
        if (len == 0) {
            return null;
        }
        min = b[0];
        int offset = 1;
        if (min == null) {
            for (int blen = b.length; offset < blen; offset++) {
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
     * 平均值
     *
     * @param b ignore
     * @return 平均值
     */
    public static BigInteger avg(BigInteger... b) {
        return avg(false, false, b);
    }

    /**
     * 平均值
     *
     * @param skipZero     跳过0
     * @param skipNegative 跳过负数
     * @param b            ignore
     * @return 平均值
     */
    public static BigInteger avg(boolean skipZero, boolean skipNegative, BigInteger... b) {
        BigInteger c = null;
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
        return c.divide(new BigInteger(len - skip + ""));
    }

    /**
     * 小于
     *
     * @param d1 ignore
     * @param d2 ignore
     * @return 小于true
     */
    public static boolean lt(BigInteger d1, BigInteger d2) {
        if (d1 == null || d2 == null) {
            return false;
        }
        return d1.compareTo(d2) < 0;
    }

    /**
     * 小于等于
     *
     * @param d1 ignore
     * @param d2 ignore
     * @return 小于等于true
     */
    public static boolean lte(BigInteger d1, BigInteger d2) {
        if (d1 == null && d2 == null) {
            return true;
        }
        if (d1 != null && d2 != null) {
            return d1.compareTo(d2) <= 0;
        }
        return false;
    }

    /**
     * 大于
     *
     * @param d1 ignore
     * @param d2 ignore
     * @return 大于true
     */
    public static boolean gt(BigInteger d1, BigInteger d2) {
        if (d1 == null || d2 == null) {
            return false;
        }
        return d1.compareTo(d2) > 0;
    }

    /**
     * 大于等于
     *
     * @param d1 ignore
     * @param d2 ignore
     * @return 大于等于true
     */
    public static boolean gte(BigInteger d1, BigInteger d2) {
        if (d1 == null && d2 == null) {
            return true;
        }
        if (d1 != null && d2 != null) {
            return d1.compareTo(d2) >= 0;
        }
        return false;
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

}
