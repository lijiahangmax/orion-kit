package com.orion.utils.math;

import com.orion.utils.Arrays1;
import com.orion.utils.Strings;
import com.orion.utils.Valid;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数字工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/8/13 23:29
 */
public class Numbers {

    /**
     * 无小数点
     */
    private static final DecimalFormat NO_DECIMAL = new DecimalFormat("#");

    /**
     * 格式化
     */
    private static final Map<Integer, DecimalFormat> DECIMAL_FORMAT_MAP = new ConcurrentHashMap<>();

    private Numbers() {
    }

    static {
        DECIMAL_FORMAT_MAP.put(0, NO_DECIMAL);
        DECIMAL_FORMAT_MAP.put(1, new DecimalFormat("#.#"));
        DECIMAL_FORMAT_MAP.put(2, new DecimalFormat("#.##"));
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

    // -------------------- isZero --------------------

    /**
     * 是否为0
     *
     * @param x ignore
     * @return true 0
     */
    public static boolean isZero(Byte x) {
        return x != null && x == 0;
    }

    public static boolean isZero(Short x) {
        return x != null && x == 0;
    }

    public static boolean isZero(Integer x) {
        return x != null && x == 0;
    }

    public static boolean isZero(Long x) {
        return x != null && x == 0;
    }

    public static boolean isZero(Float x) {
        return x != null && x == 0;
    }

    public static boolean isZero(Double x) {
        return x != null && x == 0;
    }

    /**
     * 是否不为0
     *
     * @param x ignore
     * @return true 非0
     */
    public static boolean isNotZero(Byte x) {
        return x == null || x != 0;
    }

    public static boolean isNotZero(Short x) {
        return x == null || x != 0;
    }

    public static boolean isNotZero(Integer x) {
        return x == null || x != 0;
    }

    public static boolean isNotZero(Long x) {
        return x == null || x != 0;
    }

    public static boolean isNotZero(Float x) {
        return x == null || x != 0;
    }

    public static boolean isNotZero(Double x) {
        return x == null || x != 0;
    }

    /**
     * 是否全部为0
     *
     * @param array array
     * @return true 全为0
     */
    public static boolean isAllZero(byte... array) {
        if (array == null) {
            return false;
        } else if (array.length == 0) {
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
        if (array == null) {
            return false;
        } else if (array.length == 0) {
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
        if (array == null) {
            return false;
        } else if (array.length == 0) {
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
        if (array == null) {
            return false;
        } else if (array.length == 0) {
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
        if (array == null) {
            return false;
        } else if (array.length == 0) {
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
        if (array == null) {
            return false;
        } else if (array.length == 0) {
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
        if (array == null) {
            return true;
        } else if (array.length == 0) {
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
        if (array == null) {
            return true;
        } else if (array.length == 0) {
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
        if (array == null) {
            return true;
        } else if (array.length == 0) {
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
        if (array == null) {
            return true;
        } else if (array.length == 0) {
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
        if (array == null) {
            return true;
        } else if (array.length == 0) {
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
        if (array == null) {
            return true;
        } else if (array.length == 0) {
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
    public static boolean isNegative(Byte x) {
        return x != null && x < 0;
    }

    public static boolean isNegative(Short x) {
        return x != null && x < 0;
    }

    public static boolean isNegative(Integer x) {
        return x != null && x < 0;
    }

    public static boolean isNegative(Long x) {
        return x != null && x < 0;
    }

    public static boolean isNegative(Float x) {
        return x != null && x < 0;
    }

    public static boolean isNegative(Double x) {
        return x != null && x < 0;
    }

    /**
     * 是否不为负数
     *
     * @param x ignore
     * @return true 非负数
     */
    public static boolean isNotNegative(Byte x) {
        return x == null || x >= 0;
    }

    public static boolean isNotNegative(Short x) {
        return x == null || x >= 0;
    }

    public static boolean isNotNegative(Integer x) {
        return x == null || x >= 0;
    }

    public static boolean isNotNegative(Long x) {
        return x == null || x >= 0;
    }

    public static boolean isNotNegative(Float x) {
        return x == null || x >= 0;
    }

    public static boolean isNotNegative(Double x) {
        return x == null || x >= 0;
    }

    /**
     * 是否全为负数
     *
     * @param array array
     * @return true 全为负数
     */
    public static boolean isAllNegative(byte... array) {
        if (array == null) {
            return false;
        } else if (array.length == 0) {
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
        if (array == null) {
            return false;
        } else if (array.length == 0) {
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
        if (array == null) {
            return false;
        } else if (array.length == 0) {
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
        if (array == null) {
            return false;
        } else if (array.length == 0) {
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
        if (array == null) {
            return false;
        } else if (array.length == 0) {
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
        if (array == null) {
            return false;
        } else if (array.length == 0) {
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
        if (array == null) {
            return true;
        } else if (array.length == 0) {
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
        if (array == null) {
            return true;
        } else if (array.length == 0) {
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
        if (array == null) {
            return true;
        } else if (array.length == 0) {
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
        if (array == null) {
            return true;
        } else if (array.length == 0) {
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
        if (array == null) {
            return true;
        } else if (array.length == 0) {
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
        if (array == null) {
            return true;
        } else if (array.length == 0) {
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
        return x != null && x.isNaN();
    }

    public static boolean isNaN(Double x) {
        return x != null && x.isNaN();
    }

    /**
     * 是否不为NaN
     *
     * @param x ignore
     * @return true 不为NaN
     */
    public static boolean isNotNaN(Float x) {
        return x == null || !x.isNaN();
    }

    public static boolean isNotNaN(Double x) {
        return x == null || !x.isNaN();
    }

    /**
     * 是否全为NaN
     *
     * @param array array
     * @return true 全为NaN
     */
    public static boolean isAllNaN(float... array) {
        if (array == null) {
            return false;
        } else if (array.length == 0) {
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
        if (array == null) {
            return false;
        } else if (array.length == 0) {
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
        if (array == null) {
            return true;
        } else if (array.length == 0) {
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
        if (array == null) {
            return true;
        } else if (array.length == 0) {
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

    // -------------------- scale --------------------

    /**
     * 清空小数
     *
     * @param f float
     * @return int
     */
    public static int cleanDecimal(float f) {
        return Integer.parseInt(NO_DECIMAL.format(f));
    }

    /**
     * 清空小数
     *
     * @param d double
     * @return string
     */
    public static long cleanDecimal(double d) {
        return Long.parseLong(NO_DECIMAL.format(d));
    }

    /**
     * 清空小数
     *
     * @param f float
     * @return string
     */
    public static String cleanFloatDecimal(float f) {
        return NO_DECIMAL.format(f);
    }

    /**
     * 清空小数
     *
     * @param d double
     * @return string
     */
    public static String cleanDoubleDecimal(double d) {
        return NO_DECIMAL.format(d);
    }

    /**
     * 设置小数位
     *
     * @param d          double
     * @param decimalLen 小数位
     * @return string
     */
    public static String setScale(double d, int decimalLen) {
        DecimalFormat format = DECIMAL_FORMAT_MAP.get(decimalLen);
        if (format == null) {
            format = new DecimalFormat("#." + Strings.repeat('#', decimalLen));
            DECIMAL_FORMAT_MAP.put(decimalLen, format);
        }
        return format.format(d);
    }

    /**
     * 设置小数位
     *
     * @param f          float
     * @param decimalLen 小数位
     * @return string
     */
    public static String setScale(float f, int decimalLen) {
        DecimalFormat format = DECIMAL_FORMAT_MAP.get(decimalLen);
        if (format == null) {
            format = new DecimalFormat("#." + Strings.repeat('#', decimalLen));
            DECIMAL_FORMAT_MAP.put(decimalLen, format);
        }
        return format.format(f);
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

}
