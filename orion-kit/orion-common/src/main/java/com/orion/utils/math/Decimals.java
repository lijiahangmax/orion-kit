package com.orion.utils.math;

import com.orion.utils.Arrays1;
import com.orion.utils.Objects1;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * BigDecimal工具类
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/9/4 11:37
 */
public class Decimals {

    private Decimals() {
    }

    /**
     * 格式化double
     *
     * @param format 要格式化成的格式 #.00, #.#
     * @return 格式化的字符串
     */
    public static String format(Object o, String format) {
        try {
            DecimalFormat df = new DecimalFormat(format);
            return df.format(o);
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
    public static BigDecimal toDecimal(Object o) {
        return objectToDecimal(o);
    }

    /**
     * Object -> BigDecimal
     *
     * @param o        Object
     * @param defaultV 默认值
     * @return BigDecimal
     */
    public static BigDecimal toDecimal(Object o, BigDecimal defaultV) {
        if (o == null) {
            return defaultV;
        }
        return objectToDecimal(o);
    }

    /**
     * Object[] -> BigDecimals[]
     *
     * @param o Object[]
     * @return BigDecimals[]
     */
    public static BigDecimal[] toDecimals(Object... o) {
        if (Arrays1.length(o) == 0) {
            return new BigDecimal[]{};
        }
        BigDecimal[] r = new BigDecimal[o.length];
        for (int i = 0, len = o.length; i < len; i++) {
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
        if (o instanceof String) {
            return new BigDecimal((String) o);
        } else if (o instanceof Byte) {
            return new BigDecimal((Byte) o);
        } else if (o instanceof Short) {
            return new BigDecimal((Short) o);
        } else if (o instanceof Integer) {
            return new BigDecimal((Integer) o);
        } else if (o instanceof Long) {
            return new BigDecimal((Long) o);
        } else if (o instanceof Float) {
            return new BigDecimal((Float) o);
        } else if (o instanceof Double) {
            return new BigDecimal((Double) o);
        } else if (o instanceof Boolean) {
            if ((Boolean) o) {
                return BigDecimal.ONE;
            } else {
                return BigDecimal.ZERO;
            }
        } else if (o instanceof Character) {
            return new BigDecimal((Character) o);
        } else if (o instanceof char[]) {
            return new BigDecimal((char[]) o);
        } else if (o instanceof BigInteger) {
            return new BigDecimal((BigInteger) o);
        } else if (o instanceof BigDecimal) {
            return (BigDecimal) o;
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
            mode = RoundingMode.DOWN;
        }
        return b.setScale(length, mode).toPlainString();
    }

    /**
     * BigDecimal -> Long
     *
     * @param b BigDecimal
     * @return Long
     */
    public static Long toLong(BigDecimal b) {
        return toLong(b, 0L, false);
    }

    /**
     * BigDecimal -> Long
     *
     * @param b        BigDecimal
     * @param defaultV 默认值
     * @return Long
     */
    public static Long toLong(BigDecimal b, Long defaultV) {
        return toLong(b, defaultV, false);
    }

    /**
     * BigDecimal -> Long
     *
     * @param b        BigDecimal
     * @param defaultV 默认值
     * @param exact    精确模式
     * @return Long
     */
    public static Long toLong(BigDecimal b, Long defaultV, boolean exact) {
        if (b == null) {
            return defaultV;
        }
        if (exact) {
            return b.longValueExact();
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
        return toInteger(b, 0, false);
    }

    /**
     * BigDecimal -> Integer
     *
     * @param b        BigDecimal
     * @param defaultV 默认值
     * @return Integer
     */
    public static Integer toInteger(BigDecimal b, Integer defaultV) {
        return toInteger(b, defaultV, false);
    }

    /**
     * BigDecimal -> Integer
     *
     * @param b        BigDecimal
     * @param defaultV 默认值
     * @param exact    精确模式
     * @return Integer
     */
    public static Integer toInteger(BigDecimal b, Integer defaultV, boolean exact) {
        if (b == null) {
            return defaultV;
        }
        if (exact) {
            return b.intValueExact();
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
            mode = RoundingMode.DOWN;
        }
        return b.setScale(length, mode).doubleValue();
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
    public static BigDecimal addskipNegative(BigDecimal s, BigDecimal... b) {
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
    public static BigDecimal subtractskipNegative(BigDecimal s, BigDecimal... b) {
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
    public static BigDecimal multiplyskipNegative(BigDecimal s, BigDecimal... b) {
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
    public static BigDecimal divideskipNegative(BigDecimal s, BigDecimal... b) {
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
        BigDecimal max = null;
        int len = Arrays1.length(b);
        if (len == 0) {
            return null;
        }
        max = b[0];
        int offset = 1;
        if (max == null) {
            for (int blen = b.length; offset < blen; offset++) {
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
        BigDecimal min = null;
        int len = Arrays1.length(b);
        if (len == 0) {
            return null;
        }
        min = b[0];
        int offset = 1;
        if (min == null) {
            for (int blen = b.length; offset < blen; offset++) {
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
     * 平均值
     *
     * @param b ignore
     * @return 平均值
     */
    public static BigDecimal avg(BigDecimal... b) {
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
    public static BigDecimal avg(boolean skipZero, boolean skipNegative, BigDecimal... b) {
        BigDecimal c = null;
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
     * 小于
     *
     * @param d1 ignore
     * @param d2 ignore
     * @return 小于true
     */
    public static boolean lt(BigDecimal d1, BigDecimal d2) {
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
    public static boolean lte(BigDecimal d1, BigDecimal d2) {
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
    public static boolean gt(BigDecimal d1, BigDecimal d2) {
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
    public static boolean gte(BigDecimal d1, BigDecimal d2) {
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
    public static boolean eq(BigDecimal d1, BigDecimal d2) {
        return Objects1.eq(d1, d2);
    }

}
