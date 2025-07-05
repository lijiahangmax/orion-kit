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

import cn.orionsec.kit.lang.function.Mapper;
import cn.orionsec.kit.lang.function.Reduce;
import cn.orionsec.kit.lang.utils.convert.Converts;
import cn.orionsec.kit.lang.utils.random.Randoms;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 数组工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/9/9 10:25
 * <p>
 * 需要注意: 可变参数 Object... Object[] T... T[] 不应该传基本类型的数组, 应该传基本类型的包装类
 * 基本类型数组无法转化为包装类型的数组, 从而入参为转化Object, 导致与预期结果不相同
 */
@SuppressWarnings("ALL")
public class Arrays1 {

    private static final int BINARY = 2;

    private static final int INITIAL_HASH = 7;

    private static final int MULTIPLIER = 31;

    private static final Object[] EMPTY_OBJECT_ARR = {};

    private Arrays1() {
    }

    // -------------------- new --------------------

    /**
     * 创建数组
     *
     * @param len 数组长度
     * @return 数组
     */
    public static byte[] newBytes(int len) {
        return new byte[len];
    }

    public static short[] newShorts(int len) {
        return new short[len];
    }

    public static int[] newInts(int len) {
        return new int[len];
    }

    public static long[] newLongs(int len) {
        return new long[len];
    }

    public static float[] newFloats(int len) {
        return new float[len];
    }

    public static double[] newDoubles(int len) {
        return new double[len];
    }

    public static char[] newChars(int len) {
        return new char[len];
    }

    public static boolean[] newBooleans(int len) {
        return new boolean[len];
    }

    public static Object[] newObjects(int len) {
        return new Object[len];
    }

    /**
     * 创建数组
     *
     * @param type   类型
     * @param length 空间
     * @param <T>    ignore
     * @return 数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] newArrays(Class<T> type, int length) {
        return (T[]) Array.newInstance(type, length);
    }

    /**
     * 创建数组
     *
     * @param generator e.g. Integer[]::new
     * @param length    空间
     * @param <T>       ignore
     * @return 数组
     */
    public static <T> T[] newArrays(IntFunction<T[]> generator, int length) {
        return generator.apply(length);
    }

    // -------------------- isArray --------------------

    /**
     * 判断对象是否为数组
     *
     * @param o 对象
     * @return true 数组
     */
    public static boolean isArray(Object o) {
        if (o == null) {
            return false;
        }
        return o.getClass().isArray();
    }

    /**
     * 判断是否是基本类型的数组
     *
     * @param o 对象
     * @return true 是基本类型的数组
     */
    public static boolean isBaseArray(Object o) {
        if (o == null) {
            return false;
        }
        return !isNotBaseArray(o);
    }

    /**
     * 判断是否是包装类型的数组
     *
     * @param o 对象
     * @return true 是包装类型的数组
     */
    public static boolean isWrapArray(Object o) {
        if (o == null) {
            return false;
        }
        return !isNotBaseArray(o);
    }

    /**
     * 判断是否不是基本类型的数组
     *
     * @param o 对象
     * @return true 不是基本类型的数组
     */
    public static boolean isNotBaseArray(Object o) {
        if (o == null) {
            return false;
        } else if (o instanceof byte[]) {
            return false;
        } else if (o instanceof short[]) {
            return false;
        } else if (o instanceof int[]) {
            return false;
        } else if (o instanceof long[]) {
            return false;
        } else if (o instanceof float[]) {
            return false;
        } else if (o instanceof double[]) {
            return false;
        } else if (o instanceof char[]) {
            return false;
        } else {
            return !(o instanceof boolean[]);
        }
    }

    /**
     * 判断是否不是包装类型的数组
     *
     * @param o 对象
     * @return true 不是包装类型的数组
     */
    public static boolean isWrapBaseArray(Object o) {
        if (o == null) {
            return false;
        } else if (o instanceof Byte[]) {
            return false;
        } else if (o instanceof Short[]) {
            return false;
        } else if (o instanceof Integer[]) {
            return false;
        } else if (o instanceof Long[]) {
            return false;
        } else if (o instanceof Float[]) {
            return false;
        } else if (o instanceof Double[]) {
            return false;
        } else if (o instanceof Character[]) {
            return false;
        } else {
            return !(o instanceof Boolean[]);
        }
    }

    // -------------------- get --------------------

    /**
     * 获取数组下标对应的值
     *
     * @param arr 数组
     * @param i   下标
     * @param <T> ignore
     * @return value
     */
    @SuppressWarnings("unchecked")
    public static <T> T gets(Object arr, int i) {
        Valid.notNull(arr, "array is null");
        return (T) Array.get(arr, i);
    }

    /**
     * 获取元素 如果元素存在
     *
     * @param arr list
     * @param i   i
     * @param <T> T
     * @return T
     */
    public static <T> T getIfPresent(T[] arr, int i) {
        int len = length(arr);
        if (len == 0) {
            return null;
        }
        if (i >= len) {
            return null;
        }
        return arr[i];
    }

    public static <T> T get(T[] arr, int i) {
        Valid.notNull(arr, "array is null");
        if (arr.length <= i) {
            throw Exceptions.arrayIndex("array length: " + arr.length + " get index: " + i);
        }
        return arr[i];
    }

    public static byte get(byte[] arr, int i) {
        Valid.notNull(arr, "array is null");
        if (arr.length <= i) {
            throw Exceptions.arrayIndex("array length: " + arr.length + " get index: " + i);
        }
        return arr[i];
    }

    public static short get(short[] arr, int i) {
        Valid.notNull(arr, "array is null");
        if (arr.length <= i) {
            throw Exceptions.arrayIndex("array length: " + arr.length + " get index: " + i);
        }
        return arr[i];
    }

    public static int get(int[] arr, int i) {
        Valid.notNull(arr, "array is null");
        if (arr.length <= i) {
            throw Exceptions.arrayIndex("array length: " + arr.length + " get index: " + i);
        }
        return arr[i];
    }

    public static long get(long[] arr, int i) {
        Valid.notNull(arr, "array is null");
        if (arr.length <= i) {
            throw Exceptions.arrayIndex("array length: " + arr.length + " get index: " + i);
        }
        return arr[i];
    }

    public static float get(float[] arr, int i) {
        Valid.notNull(arr, "array is null");
        if (arr.length <= i) {
            throw Exceptions.arrayIndex("array length: " + arr.length + " get index: " + i);
        }
        return arr[i];
    }

    public static double get(double[] arr, int i) {
        Valid.notNull(arr, "array is null");
        if (arr.length <= i) {
            throw Exceptions.arrayIndex("array length: " + arr.length + " get index: " + i);
        }
        return arr[i];
    }

    public static boolean get(boolean[] arr, int i) {
        Valid.notNull(arr, "array is null");
        if (arr.length <= i) {
            throw Exceptions.arrayIndex("array length: " + arr.length + " get index: " + i);
        }
        return arr[i];
    }

    public static char get(char[] arr, int i) {
        Valid.notNull(arr, "array is null");
        if (arr.length <= i) {
            throw Exceptions.arrayIndex("array length: " + arr.length + " get index: " + i);
        }
        return arr[i];
    }

    // -------------------- set --------------------

    /**
     * 设置数组下标对应的值
     *
     * @param arr   数组
     * @param i     下标
     * @param value 值
     * @param <T>   ignore
     */
    public static <T> void sets(Object arr, int i, T value) {
        Valid.notNull(arr, "array is null");
        Array.set(arr, i, value);
    }

    public static <T> void set(T[] arr, int i, T value) {
        Valid.notNull(arr, "array is null");
        if (arr.length <= i) {
            throw Exceptions.arrayIndex("array length: " + arr.length + " get index: " + i);
        }
        arr[i] = value;
    }

    public static void set(byte[] arr, int i, byte value) {
        Valid.notNull(arr, "array is null");
        if (arr.length <= i) {
            throw Exceptions.arrayIndex("array length: " + arr.length + " get index: " + i);
        }
        arr[i] = value;
    }

    public static void set(short[] arr, int i, short value) {
        Valid.notNull(arr, "array is null");
        if (arr.length <= i) {
            throw Exceptions.arrayIndex("array length: " + arr.length + " get index: " + i);
        }
        arr[i] = value;
    }

    public static void set(int[] arr, int i, int value) {
        Valid.notNull(arr, "array is null");
        if (arr.length <= i) {
            throw Exceptions.arrayIndex("array length: " + arr.length + " get index: " + i);
        }
        arr[i] = value;
    }

    public static void set(long[] arr, int i, long value) {
        Valid.notNull(arr, "array is null");
        if (arr.length <= i) {
            throw Exceptions.arrayIndex("array length: " + arr.length + " get index: " + i);
        }
        arr[i] = value;
    }

    public static void set(float[] arr, int i, float value) {
        Valid.notNull(arr, "array is null");
        if (arr.length <= i) {
            throw Exceptions.arrayIndex("array length: " + arr.length + " get index: " + i);
        }
        arr[i] = value;
    }

    public static void set(double[] arr, int i, double value) {
        Valid.notNull(arr, "array is null");
        if (arr.length <= i) {
            throw Exceptions.arrayIndex("array length: " + arr.length + " get index: " + i);
        }
        arr[i] = value;
    }

    public static void set(boolean[] arr, int i, boolean value) {
        Valid.notNull(arr, "array is null");
        if (arr.length <= i) {
            throw Exceptions.arrayIndex("array length: " + arr.length + " get index: " + i);
        }
        arr[i] = value;
    }

    public static void set(char[] arr, int i, char value) {
        Valid.notNull(arr, "array is null");
        if (arr.length <= i) {
            throw Exceptions.arrayIndex("array length: " + arr.length + " get index: " + i);
        }
        arr[i] = value;
    }

    // -------------------- random --------------------

    public static <T> T random(T[] arr) {
        Valid.notNull(arr, "array is null");
        return arr[Randoms.randomInt(arr.length)];
    }

    public static byte random(byte[] arr) {
        Valid.notNull(arr, "array is null");
        return arr[Randoms.randomInt(arr.length)];
    }

    public static short random(short[] arr) {
        Valid.notNull(arr, "array is null");
        return arr[Randoms.randomInt(arr.length)];
    }

    public static int random(int[] arr) {
        Valid.notNull(arr, "array is null");
        return arr[Randoms.randomInt(arr.length)];
    }

    public static long random(long[] arr) {
        Valid.notNull(arr, "array is null");
        return arr[Randoms.randomInt(arr.length)];
    }

    public static float random(float[] arr) {
        Valid.notNull(arr, "array is null");
        return arr[Randoms.randomInt(arr.length)];
    }

    public static double random(double[] arr) {
        Valid.notNull(arr, "array is null");
        return arr[Randoms.randomInt(arr.length)];
    }

    public static boolean random(boolean[] arr) {
        Valid.notNull(arr, "array is null");
        return arr[Randoms.randomInt(arr.length)];
    }

    public static char random(char[] arr) {
        Valid.notNull(arr, "array is null");
        return arr[Randoms.randomInt(arr.length)];
    }

    // -------------------- mapper reducer --------------------

    /**
     * 映射
     *
     * @param arr       需要映射的对象
     * @param generator 数组创建接口 e.g. Integer[]::new
     * @param mapper    映射接口
     * @param <I>       输入类型
     * @param <O>       输出类型
     * @return 映射结果
     */
    public static <I, O> O[] mapper(I[] arr, IntFunction<? extends O[]> generator, Mapper<? super I, ? extends O> mapper) {
        int len = length(arr);
        if (len == 0) {
            return generator.apply(0);
        }
        O[] oa = generator.apply(len);
        for (int j = 0; j < len; j++) {
            O o = mapper.map(arr[j]);
            oa[j] = o;
        }
        return oa;
    }

    /**
     * 规约
     *
     * @param reduce 规约接口
     * @param arr    需要规约的对象 该对象不能为基本类型的数组, 可以为基本类型的变量
     * @param <V>    数据类型
     * @param <R>    规约类型
     * @return 规约后的数据
     */
    public static <V, R> R reduce(V[] arr, Reduce<? super V, ? extends R> reduce) {
        int len = length(arr);
        if (len == 0) {
            throw Exceptions.argument("array length is 0");
        }
        return reduce.accept(arr);
    }

    // -------------------- compact --------------------

    /**
     * 去除数组中的 null
     *
     * @param arr 数组
     * @return 处理后的数组, 如果数组没有null则直接返回, 如果数组有null
     */
    public static Object[] compact(Object[] arr) {
        int len = length(arr);
        if (len == 0) {
            return new Object[0];
        } else if (len == 1) {
            if (arr[0] == null) {
                return new Object[0];
            }
        }
        int num = compactSwap(arr, len);
        Object[] na = new Object[len - num];
        System.arraycopy(arr, 0, na, 0, len - num);
        return na;
    }

    public static <T> T[] compacts(T[] arr, IntFunction<T[]> generator) {
        int len = length(arr);
        if (len == 0) {
            return generator.apply(0);
        } else if (len == 1) {
            if (arr[0] == null) {
                return generator.apply(0);
            }
        }
        int num = compactSwap(arr, len);
        T[] na = generator.apply(len - num);
        System.arraycopy(arr, 0, na, 0, len - num);
        return na;
    }

    /**
     * 交换数组中的位置
     *
     * @param arr 数组
     * @param len 数组长度
     * @return 处理空值的次数
     */
    private static int compactSwap(Object[] arr, int len) {
        int num = 0;
        for (int i = 0; i < len; i++) {
            if (arr[i] != null) {
                continue;
            }
            if (i == len - num + 1) {
                break;
            }
            num++;
            if (i != len - num) {
                System.arraycopy(arr, i + 1, arr, i, len - i - 1);
                i--;
            }
        }
        return num;
    }

    // -------------------- exclude --------------------

    /**
     * 排除数组中指定的值
     *
     * @param arr 数组
     * @param es  排除值
     * @return 处理后的数组, 如果数组没有包含的值则直接返回
     */
    public static byte[] exclude(byte[] arr, byte... es) {
        int len = length(arr);
        if (len == 0) {
            return new byte[0];
        }
        if (isEmpty(es)) {
            return arr;
        }
        int num = excludeSwap(arr, len, i -> {
            for (byte e : es) {
                if (i == e) {
                    return true;
                }
            }
            return false;
        });
        byte[] na = new byte[len - num];
        System.arraycopy(arr, 0, na, 0, len - num);
        return na;
    }

    public static short[] exclude(short[] arr, short... es) {
        int len = length(arr);
        if (len == 0) {
            return new short[0];
        }
        if (isEmpty(es)) {
            return arr;
        }
        int num = excludeSwap(arr, len, i -> {
            for (short e : es) {
                if (i == e) {
                    return true;
                }
            }
            return false;
        });
        short[] na = new short[len - num];
        System.arraycopy(arr, 0, na, 0, len - num);
        return na;
    }

    public static int[] exclude(int[] arr, int... es) {
        int len = length(arr);
        if (len == 0) {
            return new int[0];
        }
        if (isEmpty(es)) {
            return arr;
        }
        int num = excludeSwap(arr, len, i -> {
            for (int e : es) {
                if (i == e) {
                    return true;
                }
            }
            return false;
        });
        int[] na = new int[len - num];
        System.arraycopy(arr, 0, na, 0, len - num);
        return na;
    }

    public static long[] exclude(long[] arr, long... es) {
        int len = length(arr);
        if (len == 0) {
            return new long[0];
        }
        if (isEmpty(es)) {
            return arr;
        }
        int num = excludeSwap(arr, len, i -> {
            for (long e : es) {
                if (i == e) {
                    return true;
                }
            }
            return false;
        });
        long[] na = new long[len - num];
        System.arraycopy(arr, 0, na, 0, len - num);
        return na;
    }

    public static float[] exclude(float[] arr, float... es) {
        int len = length(arr);
        if (len == 0) {
            return new float[0];
        }
        if (isEmpty(es)) {
            return arr;
        }
        int num = excludeSwap(arr, len, i -> {
            for (float e : es) {
                if (i == e) {
                    return true;
                }
            }
            return false;
        });
        float[] na = new float[len - num];
        System.arraycopy(arr, 0, na, 0, len - num);
        return na;
    }

    public static double[] exclude(double[] arr, double... es) {
        int len = length(arr);
        if (len == 0) {
            return new double[0];
        }
        if (isEmpty(es)) {
            return arr;
        }
        int num = excludeSwap(arr, len, i -> {
            for (double e : es) {
                if (i == e) {
                    return true;
                }
            }
            return false;
        });
        double[] na = new double[len - num];
        System.arraycopy(arr, 0, na, 0, len - num);
        return na;
    }

    public static boolean[] exclude(boolean[] arr, boolean... es) {
        int len = length(arr);
        if (len == 0) {
            return new boolean[0];
        }
        if (isEmpty(es)) {
            return arr;
        }
        int num = excludeSwap(arr, len, i -> {
            for (boolean e : es) {
                if (i == e) {
                    return true;
                }
            }
            return false;
        });
        boolean[] na = new boolean[len - num];
        System.arraycopy(arr, 0, na, 0, len - num);
        return na;
    }

    public static char[] exclude(char[] arr, char... es) {
        int len = length(arr);
        if (len == 0) {
            return new char[0];
        }
        if (isEmpty(es)) {
            return arr;
        }
        int num = excludeSwap(arr, len, i -> {
            for (char e : es) {
                if (i == e) {
                    return true;
                }
            }
            return false;
        });
        char[] na = new char[len - num];
        System.arraycopy(arr, 0, na, 0, len - num);
        return na;
    }

    /**
     * 排除数组中指定的值
     *
     * @param arr 数组
     * @param es  排除值 这个值可以为基本类型的元素, 但是不能是基本类型的数组
     * @return 处理后的数组, 如果数组没有包含的值则直接返回
     */
    public static Object[] exclude(Object[] arr, Object... es) {
        int len = length(arr);
        if (len == 0) {
            return new Object[0];
        }
        if (isEmpty(es)) {
            return arr;
        }
        int num = excludeSwap(arr, len, i -> {
            for (Object e : es) {
                if (Objects1.eq(i, e)) {
                    return true;
                }
            }
            return false;
        });
        Object[] na = new Object[len - num];
        System.arraycopy(arr, 0, na, 0, len - num);
        return na;
    }

    @SafeVarargs
    public static <T> T[] excludes(T[] arr, IntFunction<T[]> generator, T... es) {
        int len = length(arr);
        if (len == 0) {
            return generator.apply(0);
        }
        if (isEmpty(es)) {
            return arr;
        }
        int num = excludeSwaps(arr, len, i -> {
            for (Object e : es) {
                if (Objects1.eq(i, e)) {
                    return true;
                }
            }
            return false;
        });
        T[] na = generator.apply(len - num);
        System.arraycopy(arr, 0, na, 0, len - num);
        return na;
    }

    public static <T> T[] excludes(T[] arr, Predicate<T> p, IntFunction<T[]> generator) {
        int len = length(arr);
        if (len == 0) {
            return generator.apply(0);
        }
        int num = excludeSwaps(arr, len, p);
        T[] na = generator.apply(len - num);
        System.arraycopy(arr, 0, na, 0, len - num);
        return na;
    }

    /**
     * 交换数组中的位置
     *
     * @param arr 数组
     * @param len 数组长度
     * @return 处理空值的次数
     */
    private static int excludeSwap(byte[] arr, int len, Predicate<Byte> p) {
        int num = 0;
        for (int i = 0; i < len; i++) {
            if (!p.test(arr[i])) {
                continue;
            }
            if (i == len - num + 1) {
                break;
            }
            num++;
            if (i != len - num) {
                System.arraycopy(arr, i + 1, arr, i, len - i - 1);
                i--;
            }
        }
        return num;
    }

    private static int excludeSwap(short[] arr, int len, Predicate<Short> p) {
        int num = 0;
        for (int i = 0; i < len; i++) {
            if (!p.test(arr[i])) {
                continue;
            }
            if (i == len - num + 1) {
                break;
            }
            num++;
            if (i != len - num) {
                System.arraycopy(arr, i + 1, arr, i, len - i - 1);
                i--;
            }
        }
        return num;
    }

    private static int excludeSwap(int[] arr, int len, Predicate<Integer> p) {
        int num = 0;
        for (int i = 0; i < len; i++) {
            if (!p.test(arr[i])) {
                continue;
            }
            if (i == len - num + 1) {
                break;
            }
            num++;
            if (i != len - num) {
                System.arraycopy(arr, i + 1, arr, i, len - i - 1);
                i--;
            }
        }
        return num;
    }

    private static int excludeSwap(long[] arr, int len, Predicate<Long> p) {
        int num = 0;
        for (int i = 0; i < len; i++) {
            if (!p.test(arr[i])) {
                continue;
            }
            if (i == len - num + 1) {
                break;
            }
            num++;
            if (i != len - num) {
                System.arraycopy(arr, i + 1, arr, i, len - i - 1);
                i--;
            }
        }
        return num;
    }

    private static int excludeSwap(float[] arr, int len, Predicate<Float> p) {
        int num = 0;
        for (int i = 0; i < len; i++) {
            if (!p.test(arr[i])) {
                continue;
            }
            if (i == len - num + 1) {
                break;
            }
            num++;
            if (i != len - num) {
                System.arraycopy(arr, i + 1, arr, i, len - i - 1);
                i--;
            }
        }
        return num;
    }

    private static int excludeSwap(double[] arr, int len, Predicate<Double> p) {
        int num = 0;
        for (int i = 0; i < len; i++) {
            if (!p.test(arr[i])) {
                continue;
            }
            if (i == len - num + 1) {
                break;
            }
            num++;
            if (i != len - num) {
                System.arraycopy(arr, i + 1, arr, i, len - i - 1);
                i--;
            }
        }
        return num;
    }

    private static int excludeSwap(boolean[] arr, int len, Predicate<Boolean> p) {
        int num = 0;
        for (int i = 0; i < len; i++) {
            if (!p.test(arr[i])) {
                continue;
            }
            if (i == len - num + 1) {
                break;
            }
            num++;
            if (i != len - num) {
                System.arraycopy(arr, i + 1, arr, i, len - i - 1);
                i--;
            }
        }
        return num;
    }

    private static int excludeSwap(char[] arr, int len, Predicate<Character> p) {
        int num = 0;
        for (int i = 0; i < len; i++) {
            if (!p.test(arr[i])) {
                continue;
            }
            if (i == len - num + 1) {
                break;
            }
            num++;
            if (i != len - num) {
                System.arraycopy(arr, i + 1, arr, i, len - i - 1);
                i--;
            }
        }
        return num;
    }

    private static int excludeSwap(Object[] arr, int len, Predicate<Object> p) {
        int num = 0;
        for (int i = 0; i < len; i++) {
            if (!p.test(arr[i])) {
                continue;
            }
            if (i == len - num + 1) {
                break;
            }
            num++;
            if (i != len - num) {
                System.arraycopy(arr, i + 1, arr, i, len - i - 1);
                i--;
            }
        }
        return num;
    }

    private static <T> int excludeSwaps(T[] arr, int len, Predicate<T> p) {
        int num = 0;
        for (int i = 0; i < len; i++) {
            if (!p.test(arr[i])) {
                continue;
            }
            if (i == len - num + 1) {
                break;
            }
            num++;
            if (i != len - num) {
                System.arraycopy(arr, i + 1, arr, i, len - i - 1);
                i--;
            }
        }
        return num;
    }

    // -------------------- forEach --------------------

    /**
     * 消费对象
     *
     * @param consumer 消费函数
     * @param arr      元素
     * @param <T>      对象类型
     */
    public static <T> void forEach(Consumer<T> consumer, T[] arr) {
        for (int i = 0, len = length(arr); i < len; i++) {
            consumer.accept(arr[i]);
        }
    }

    public static void forEach(Consumer<Byte> consumer, byte[] arr) {
        for (int i = 0, len = length(arr); i < len; i++) {
            consumer.accept(arr[i]);
        }
    }

    public static void forEach(Consumer<Short> consumer, short[] arr) {
        for (int i = 0, len = length(arr); i < len; i++) {
            consumer.accept(arr[i]);
        }
    }

    public static void forEach(Consumer<Integer> consumer, int[] arr) {
        for (int i = 0, len = length(arr); i < len; i++) {
            consumer.accept(arr[i]);
        }
    }

    public static void forEach(Consumer<Long> consumer, long[] arr) {
        for (int i = 0, len = length(arr); i < len; i++) {
            consumer.accept(arr[i]);
        }
    }

    public static void forEach(Consumer<Float> consumer, float[] arr) {
        for (int i = 0, len = length(arr); i < len; i++) {
            consumer.accept(arr[i]);
        }
    }

    public static void forEach(Consumer<Double> consumer, double[] arr) {
        for (int i = 0, len = length(arr); i < len; i++) {
            consumer.accept(arr[i]);
        }
    }

    public static void forEach(Consumer<Character> consumer, char[] arr) {
        for (int i = 0, len = length(arr); i < len; i++) {
            consumer.accept(arr[i]);
        }
    }

    public static void forEach(Consumer<Boolean> consumer, boolean[] arr) {
        for (int i = 0, len = length(arr); i < len; i++) {
            consumer.accept(arr[i]);
        }
    }

    // -------------------- toArray --------------------

    public static byte[] toByteArray(String s, String split) {
        if (Strings.isBlank(s)) {
            return new byte[0];
        }
        return Converts.toBytes(s.split(split));
    }

    public static short[] toShortArray(String s, String split) {
        if (Strings.isBlank(s)) {
            return new short[0];
        }
        return Converts.toShorts(s.split(split));
    }

    public static int[] toIntArray(String s, String split) {
        if (Strings.isBlank(s)) {
            return new int[0];
        }
        return Converts.toInts(s.split(split));
    }

    public static long[] toLongArray(String s, String split) {
        if (Strings.isBlank(s)) {
            return new long[0];
        }
        return Converts.toLongs(s.split(split));
    }

    public static float[] toFloatArray(String s, String split) {
        if (Strings.isBlank(s)) {
            return new float[0];
        }
        return Converts.toFloats(s.split(split));
    }

    public static double[] toDoubleArray(String s, String split) {
        if (Strings.isBlank(s)) {
            return new double[0];
        }
        return Converts.toDoubles(s.split(split));
    }

    public static boolean[] toBooleanArray(String s, String split) {
        if (Strings.isBlank(s)) {
            return new boolean[0];
        }
        return Converts.toBooleans(s.split(split));
    }

    public static String[] toStringArray(String s, String split) {
        if (Strings.isBlank(s)) {
            return new String[0];
        }
        return s.split(split);
    }

    // -------------------- asArray --------------------

    /**
     * 将可变参数转化为数组
     *
     * @param arr 可边参数
     * @return 数组
     */
    public static byte[] of(byte... arr) {
        return arr;
    }

    public static short[] of(short... arr) {
        return arr;
    }

    public static int[] of(int... arr) {
        return arr;
    }

    public static long[] of(long... arr) {
        return arr;
    }

    public static float[] of(float... arr) {
        return arr;
    }

    public static double[] of(double... arr) {
        return arr;
    }

    public static boolean[] of(boolean... arr) {
        return arr;
    }

    public static char[] of(char... arr) {
        return arr;
    }

    /**
     * 不可接收基本类型的数组, 可以使用包装类 否则会返回 T[][]
     *
     * @param arr arr
     * @param <T> arr
     * @return array
     */
    @SafeVarargs
    public static <T> T[] of(T... arr) {
        return arr;
    }

    /**
     * 可接收基本类型的数组, 返回Object[]
     *
     * @param arr arr
     * @return array
     */
    public static Object[] ofs(Object... arr) {
        int lengths = lengths(arr);
        if (lengths == 0) {
            return EMPTY_OBJECT_ARR;
        }
        Object[] r = new Object[lengths];
        int len = arr.length;
        if (len != lengths || (len == 1 && isArray(arr[0]))) {
            for (int j = 0; j < lengths; j++) {
                r[j] = Array.get(arr[0], j);
            }
        } else {
            return arr;
        }
        return r;
    }

    // -------------------- isEmpty --------------------

    /**
     * 判断数组是否为空
     *
     * @param arr 数组
     * @return true为空
     */
    public static boolean isEmpties(Object arr) {
        return arr == null || (isArray(arr) && Array.getLength(arr) == 0);
    }

    public static <T> boolean isEmpty(T[] arr) {
        return arr == null || length(arr) == 0;
    }

    public static boolean isEmpty(byte[] arr) {
        return arr == null || length(arr) == 0;
    }

    public static boolean isEmpty(short[] arr) {
        return arr == null || length(arr) == 0;
    }

    public static boolean isEmpty(int[] arr) {
        return arr == null || length(arr) == 0;
    }

    public static boolean isEmpty(long[] arr) {
        return arr == null || length(arr) == 0;
    }

    public static boolean isEmpty(float[] arr) {
        return arr == null || length(arr) == 0;
    }

    public static boolean isEmpty(double[] arr) {
        return arr == null || length(arr) == 0;
    }

    public static boolean isEmpty(boolean[] arr) {
        return arr == null || length(arr) == 0;
    }

    public static boolean isEmpty(char[] arr) {
        return arr == null || length(arr) == 0;
    }

    // -------------------- isNotEmpty --------------------

    /**
     * 判断数组是否不为空
     *
     * @param arr 数组
     * @return true不为空
     */
    public static boolean isNotEmpties(Object arr) {
        return !isEmpties(arr);
    }

    public static <T> boolean isNotEmpty(T[] arr) {
        return !isEmpty(arr);
    }

    public static boolean isNotEmpty(byte[] arr) {
        return !isEmpty(arr);
    }

    public static boolean isNotEmpty(short[] arr) {
        return !isEmpty(arr);
    }

    public static boolean isNotEmpty(int[] arr) {
        return !isEmpty(arr);
    }

    public static boolean isNotEmpty(long[] arr) {
        return !isEmpty(arr);
    }

    public static boolean isNotEmpty(float[] arr) {
        return !isEmpty(arr);
    }

    public static boolean isNotEmpty(double[] arr) {
        return !isEmpty(arr);
    }

    public static boolean isNotEmpty(boolean[] arr) {
        return !isEmpty(arr);
    }

    public static boolean isNotEmpty(char[] arr) {
        return !isEmpty(arr);
    }

    // -------------------- def --------------------

    /**
     * 如果数组为空返回默认数组
     *
     * @param arr arr
     * @param def def
     * @param <T> T
     * @return array
     */
    public static <T> T[] def(T[] arr, T[] def) {
        return arr == null ? def : arr;
    }

    public static byte[] def(byte[] arr, byte[] def) {
        return arr == null ? def : arr;
    }

    public static short[] def(short[] arr, short[] def) {
        return arr == null ? def : arr;
    }

    public static int[] def(int[] arr, int[] def) {
        return arr == null ? def : arr;
    }

    public static long[] def(long[] arr, long[] def) {
        return arr == null ? def : arr;
    }

    public static float[] def(float[] arr, float[] def) {
        return arr == null ? def : arr;
    }

    public static double[] def(double[] arr, double[] def) {
        return arr == null ? def : arr;
    }

    public static boolean[] def(boolean[] arr, boolean[] def) {
        return arr == null ? def : arr;
    }

    public static char[] def(char[] arr, char[] def) {
        return arr == null ? def : arr;
    }

    public static <T> T[] def(T[] arr, Supplier<T[]> def) {
        return arr == null ? def.get() : arr;
    }

    public static byte[] def(byte[] arr, Supplier<byte[]> def) {
        return arr == null ? def.get() : arr;
    }

    public static short[] def(short[] arr, Supplier<short[]> def) {
        return arr == null ? def.get() : arr;
    }

    public static int[] def(int[] arr, Supplier<int[]> def) {
        return arr == null ? def.get() : arr;
    }

    public static long[] def(long[] arr, Supplier<long[]> def) {
        return arr == null ? def.get() : arr;
    }

    public static float[] def(float[] arr, Supplier<float[]> def) {
        return arr == null ? def.get() : arr;
    }

    public static double[] def(double[] arr, Supplier<double[]> def) {
        return arr == null ? def.get() : arr;
    }

    public static boolean[] def(boolean[] arr, Supplier<boolean[]> def) {
        return arr == null ? def.get() : arr;
    }

    public static char[] def(char[] arr, Supplier<char[]> def) {
        return arr == null ? def.get() : arr;
    }

    // -------------------- length --------------------

    /**
     * 获取数组长度
     *
     * @param arr 数组 如果传参是一个基本类型数组, 返回值也是基本类型数组的长度
     * @return 长度
     * <p>
     * 在默认情况下, 如果可变参数o, 传参是基本类型的数组, 使用length属性判断, o.length = 1
     */
    public static int lengths(Object... arr) {
        if (arr == null) {
            return 0;
        }
        if (arr.length == 1) {
            Object t = arr[0];
            if (t instanceof byte[]) {
                return ((byte[]) t).length;
            } else if (t instanceof short[]) {
                return ((short[]) t).length;
            } else if (t instanceof int[]) {
                return ((int[]) t).length;
            } else if (t instanceof long[]) {
                return ((long[]) t).length;
            } else if (t instanceof float[]) {
                return ((float[]) t).length;
            } else if (t instanceof double[]) {
                return ((double[]) t).length;
            } else if (t instanceof char[]) {
                return ((char[]) t).length;
            } else if (t instanceof boolean[]) {
                return ((boolean[]) t).length;
            } else if (t instanceof Object[]) {
                return ((Object[]) t).length;
            }
        }
        return arr.length;
    }

    public static int lens(Object arr) {
        if (arr == null) {
            return 0;
        }
        if (isArray(arr)) {
            return Array.getLength(arr);
        } else {
            return 0;
        }
    }

    // -------------------- resize --------------------

    /**
     * 调整数组大小
     *
     * @param arr     原数组
     * @param newSize 新长度
     * @return 新数组
     */
    public static byte[] resize(byte[] arr, int newSize) {
        if (newSize <= 0) {
            return new byte[0];
        }
        if (arr.length < newSize) {
            byte[] nbs = new byte[newSize];
            System.arraycopy(arr, 0, nbs, 0, arr.length);
            return nbs;
        } else if (arr.length > newSize) {
            byte[] nbs = new byte[newSize];
            System.arraycopy(arr, 0, nbs, 0, newSize);
            return nbs;
        }
        return arr;
    }

    public static short[] resize(short[] arr, int newSize) {
        if (newSize <= 0) {
            return new short[0];
        }
        if (arr.length < newSize) {
            short[] nbs = new short[newSize];
            System.arraycopy(arr, 0, nbs, 0, arr.length);
            return nbs;
        } else if (arr.length > newSize) {
            short[] nbs = new short[newSize];
            System.arraycopy(arr, 0, nbs, 0, newSize);
            return nbs;
        }
        return arr;
    }

    public static int[] resize(int[] arr, int newSize) {
        if (newSize <= 0) {
            return new int[0];
        }
        if (arr.length < newSize) {
            int[] nbs = new int[newSize];
            System.arraycopy(arr, 0, nbs, 0, arr.length);
            return nbs;
        } else if (arr.length > newSize) {
            int[] nbs = new int[newSize];
            System.arraycopy(arr, 0, nbs, 0, newSize);
            return nbs;
        }
        return arr;
    }

    public static long[] resize(long[] arr, int newSize) {
        if (newSize <= 0) {
            return new long[0];
        }
        if (arr.length < newSize) {
            long[] nbs = new long[newSize];
            System.arraycopy(arr, 0, nbs, 0, arr.length);
            return nbs;
        } else if (arr.length > newSize) {
            long[] nbs = new long[newSize];
            System.arraycopy(arr, 0, nbs, 0, newSize);
            return nbs;
        }
        return arr;
    }

    public static float[] resize(float[] arr, int newSize) {
        if (newSize <= 0) {
            return new float[0];
        }
        if (arr.length < newSize) {
            float[] nbs = new float[newSize];
            System.arraycopy(arr, 0, nbs, 0, arr.length);
            return nbs;
        } else if (arr.length > newSize) {
            float[] nbs = new float[newSize];
            System.arraycopy(arr, 0, nbs, 0, newSize);
            return nbs;
        }
        return arr;
    }

    public static double[] resize(double[] arr, int newSize) {
        if (newSize <= 0) {
            return new double[0];
        }
        if (arr.length < newSize) {
            double[] nbs = new double[newSize];
            System.arraycopy(arr, 0, nbs, 0, arr.length);
            return nbs;
        } else if (arr.length > newSize) {
            double[] nbs = new double[newSize];
            System.arraycopy(arr, 0, nbs, 0, newSize);
            return nbs;
        }
        return arr;
    }

    public static boolean[] resize(boolean[] arr, int newSize) {
        if (newSize <= 0) {
            return new boolean[0];
        }
        if (arr.length < newSize) {
            boolean[] nbs = new boolean[newSize];
            System.arraycopy(arr, 0, nbs, 0, arr.length);
            return nbs;
        } else if (arr.length > newSize) {
            boolean[] nbs = new boolean[newSize];
            System.arraycopy(arr, 0, nbs, 0, newSize);
            return nbs;
        }
        return arr;
    }

    public static char[] resize(char[] arr, int newSize) {
        if (newSize <= 0) {
            return new char[0];
        }
        if (arr.length < newSize) {
            char[] nbs = new char[newSize];
            System.arraycopy(arr, 0, nbs, 0, arr.length);
            return nbs;
        } else if (arr.length > newSize) {
            char[] nbs = new char[newSize];
            System.arraycopy(arr, 0, nbs, 0, newSize);
            return nbs;
        }
        return arr;
    }

    public static Object[] resize(Object[] arr, int newSize) {
        if (newSize <= 0) {
            return new Object[0];
        }
        if (arr.length < newSize) {
            Object[] nbs = new Object[newSize];
            System.arraycopy(arr, 0, nbs, 0, arr.length);
            return nbs;
        } else if (arr.length > newSize) {
            Object[] nbs = new Object[newSize];
            System.arraycopy(arr, 0, nbs, 0, newSize);
            return nbs;
        }
        return arr;
    }

    public static <T> T[] resize(T[] arr, int newSize, IntFunction<T[]> generator) {
        if (newSize <= 0) {
            return generator.apply(0);
        }
        if (arr.length < newSize) {
            T[] nbs = generator.apply(newSize);
            System.arraycopy(arr, 0, nbs, 0, arr.length);
            return nbs;
        } else if (arr.length > newSize) {
            T[] nbs = generator.apply(newSize);
            System.arraycopy(arr, 0, nbs, 0, newSize);
            return nbs;
        }
        return arr;
    }

    // -------------------- arraycopy --------------------

    /**
     * 数组拷贝 自动扩容2倍
     *
     * @param src     原数组
     * @param srcPos  原数组起始位置
     * @param dest    目标数组
     * @param destPos 目标数组起始位置
     * @param length  拷贝原数组几位到目标数组
     * @return 扩容返回新目标数组 不扩容返回原目标数组
     */
    public static byte[] arraycopy(byte[] src, int srcPos, byte[] dest, int destPos, int length) {
        int newLen = destPos + length;
        if (newLen >= dest.length) {
            int newPos = dest.length * 2;
            if (newPos <= newLen) {
                newPos = newLen * 2;
            }
            byte[] bytes = new byte[newPos];
            System.arraycopy(dest, 0, bytes, 0, dest.length - (dest.length - destPos));
            System.arraycopy(src, srcPos, bytes, destPos, length);
            return bytes;
        } else {
            System.arraycopy(src, srcPos, dest, destPos, length);
            return dest;
        }
    }

    public static short[] arraycopy(short[] src, int srcPos, short[] dest, int destPos, int length) {
        int newLen = destPos + length;
        if (newLen >= dest.length) {
            int newPos = dest.length * 2;
            if (newPos <= newLen) {
                newPos = newLen * 2;
            }
            short[] shorts = new short[newPos];
            System.arraycopy(dest, 0, shorts, 0, dest.length - (dest.length - destPos));
            System.arraycopy(src, srcPos, shorts, destPos, length);
            return shorts;
        } else {
            System.arraycopy(src, srcPos, dest, destPos, length);
            return dest;
        }
    }

    public static int[] arraycopy(int[] src, int srcPos, int[] dest, int destPos, int length) {
        int newLen = destPos + length;
        if (newLen >= dest.length) {
            int newPos = dest.length * 2;
            if (newPos <= newLen) {
                newPos = newLen * 2;
            }
            int[] ints = new int[newPos];
            System.arraycopy(dest, 0, ints, 0, dest.length - (dest.length - destPos));
            System.arraycopy(src, srcPos, ints, destPos, length);
            return ints;
        } else {
            System.arraycopy(src, srcPos, dest, destPos, length);
            return dest;
        }
    }

    public static long[] arraycopy(long[] src, int srcPos, long[] dest, int destPos, int length) {
        int newLen = destPos + length;
        if (newLen >= dest.length) {
            int newPos = dest.length * 2;
            if (newPos <= newLen) {
                newPos = newLen * 2;
            }
            long[] longs = new long[newPos];
            System.arraycopy(dest, 0, longs, 0, dest.length - (dest.length - destPos));
            System.arraycopy(src, srcPos, longs, destPos, length);
            return longs;
        } else {
            System.arraycopy(src, srcPos, dest, destPos, length);
            return dest;
        }
    }

    public static float[] arraycopy(float[] src, int srcPos, float[] dest, int destPos, int length) {
        int newLen = destPos + length;
        if (newLen >= dest.length) {
            int newPos = dest.length * 2;
            if (newPos <= newLen) {
                newPos = newLen * 2;
            }
            float[] floats = new float[newPos];
            System.arraycopy(dest, 0, floats, 0, dest.length - (dest.length - destPos));
            System.arraycopy(src, srcPos, floats, destPos, length);
            return floats;
        } else {
            System.arraycopy(src, srcPos, dest, destPos, length);
            return dest;
        }
    }

    public static double[] arraycopy(double[] src, int srcPos, double[] dest, int destPos, int length) {
        int newLen = destPos + length;
        if (newLen >= dest.length) {
            int newPos = dest.length * 2;
            if (newPos <= newLen) {
                newPos = newLen * 2;
            }
            double[] doubles = new double[newPos];
            System.arraycopy(dest, 0, doubles, 0, dest.length - (dest.length - destPos));
            System.arraycopy(src, srcPos, doubles, destPos, length);
            return doubles;
        } else {
            System.arraycopy(src, srcPos, dest, destPos, length);
            return dest;
        }
    }

    public static boolean[] arraycopy(boolean[] src, int srcPos, boolean[] dest, int destPos, int length) {
        int newLen = destPos + length;
        if (newLen >= dest.length) {
            int newPos = dest.length * 2;
            if (newPos <= newLen) {
                newPos = newLen * 2;
            }
            boolean[] booleans = new boolean[newPos];
            System.arraycopy(dest, 0, booleans, 0, dest.length - (dest.length - destPos));
            System.arraycopy(src, srcPos, booleans, destPos, length);
            return booleans;
        } else {
            System.arraycopy(src, srcPos, dest, destPos, length);
            return dest;
        }
    }

    public static char[] arraycopy(char[] src, int srcPos, char[] dest, int destPos, int length) {
        int newLen = destPos + length;
        if (newLen >= dest.length) {
            int newPos = dest.length * 2;
            if (newPos <= newLen) {
                newPos = newLen * 2;
            }
            char[] chars = new char[newPos];
            System.arraycopy(dest, 0, chars, 0, dest.length - (dest.length - destPos));
            System.arraycopy(src, srcPos, chars, destPos, length);
            return chars;
        } else {
            System.arraycopy(src, srcPos, dest, destPos, length);
            return dest;
        }
    }

    public static Object[] arraycopy(Object[] src, int srcPos, Object[] dest, int destPos, int length) {
        int newLen = destPos + length;
        if (newLen >= dest.length) {
            int newPos = dest.length * 2;
            if (newPos <= newLen) {
                newPos = newLen * 2;
            }
            Object[] objects = new Object[newPos];
            System.arraycopy(dest, 0, objects, 0, dest.length - (dest.length - destPos));
            System.arraycopy(src, srcPos, objects, destPos, length);
            return objects;
        } else {
            System.arraycopy(src, srcPos, dest, destPos, length);
            return dest;
        }
    }

    public static <T> T[] arraycopys(T[] src, int srcPos, T[] dest, int destPos, int length, IntFunction<T[]> generator) {
        int destlen = lengths(dest);
        if (destPos + length >= destlen) {
            T[] apply = generator.apply(destlen * 2);
            System.arraycopy(dest, 0, apply, 0, destlen - (destlen - destPos));
            System.arraycopy(src, srcPos, apply, destPos, length);
            return apply;
        } else {
            System.arraycopy(src, srcPos, dest, destPos, length);
            return dest;
        }
    }

    // -------------------- length --------------------

    public static <T> int length(T[] arr) {
        return arr == null ? 0 : arr.length;
    }

    public static int length(byte[] arr) {
        return arr == null ? 0 : arr.length;
    }

    public static int length(short[] arr) {
        return arr == null ? 0 : arr.length;
    }

    public static int length(int[] arr) {
        return arr == null ? 0 : arr.length;
    }

    public static int length(long[] arr) {
        return arr == null ? 0 : arr.length;
    }

    public static int length(float[] arr) {
        return arr == null ? 0 : arr.length;
    }

    public static int length(double[] arr) {
        return arr == null ? 0 : arr.length;
    }

    public static int length(char[] arr) {
        return arr == null ? 0 : arr.length;
    }

    public static int length(boolean[] arr) {
        return arr == null ? 0 : arr.length;
    }

    // -------------------- wrap --------------------

    /**
     * 将数组转化为其包装类
     *
     * @param w 数组
     * @return 包装数组
     */
    public static Byte[] wrap(byte[] w) {
        int length = length(w);
        if (length == 0) {
            return new Byte[0];
        }
        Byte[] n = new Byte[length];
        for (int i = 0; i < length; i++) {
            n[i] = w[i];
        }
        return n;
    }

    public static Short[] wrap(short[] w) {
        int length = length(w);
        if (length == 0) {
            return new Short[0];
        }
        Short[] n = new Short[length];
        for (int i = 0; i < length; i++) {
            n[i] = w[i];
        }
        return n;
    }

    public static Integer[] wrap(int[] arr) {
        int length = length(arr);
        if (length == 0) {
            return new Integer[0];
        }
        Integer[] n = new Integer[length];
        for (int i = 0; i < length; i++) {
            n[i] = arr[i];
        }
        return n;
    }

    public static Long[] wrap(long[] arr) {
        int length = length(arr);
        if (length == 0) {
            return new Long[0];
        }
        Long[] n = new Long[length];
        for (int i = 0; i < length; i++) {
            n[i] = arr[i];
        }
        return n;
    }

    public static Float[] wrap(float[] arr) {
        int length = length(arr);
        if (length == 0) {
            return new Float[0];
        }
        Float[] n = new Float[length];
        for (int i = 0; i < length; i++) {
            n[i] = arr[i];
        }
        return n;
    }

    public static Double[] wrap(double[] arr) {
        int length = length(arr);
        if (length == 0) {
            return new Double[0];
        }
        Double[] n = new Double[length];
        for (int i = 0; i < length; i++) {
            n[i] = arr[i];
        }
        return n;
    }

    public static Boolean[] wrap(boolean[] arr) {
        int length = length(arr);
        if (length == 0) {
            return new Boolean[0];
        }
        Boolean[] n = new Boolean[length];
        for (int i = 0; i < length; i++) {
            n[i] = arr[i];
        }
        return n;
    }

    public static Character[] wrap(char[] arr) {
        int length = length(arr);
        if (length == 0) {
            return new Character[0];
        }
        Character[] n = new Character[length];
        for (int i = 0; i < length; i++) {
            n[i] = arr[i];
        }
        return n;
    }

    public static Object wrap(Object arr) {
        if (arr == null) {
            return null;
        } else if (arr instanceof byte[]) {
            return wrap((byte[]) arr);
        } else if (arr instanceof short[]) {
            return wrap((short[]) arr);
        } else if (arr instanceof int[]) {
            return wrap((int[]) arr);
        } else if (arr instanceof long[]) {
            return wrap((long[]) arr);
        } else if (arr instanceof float[]) {
            return wrap((float[]) arr);
        } else if (arr instanceof double[]) {
            return wrap((double[]) arr);
        } else if (arr instanceof boolean[]) {
            return wrap((boolean[]) arr);
        } else if (arr instanceof char[]) {
            return wrap((char[]) arr);
        } else {
            return arr;
        }
    }

    // -------------------- unWrap --------------------

    /**
     * 将数组转化为其基本装类
     *
     * @param arr 数组
     * @return 基本数组
     */
    public static byte[] unWrap(Byte[] arr) {
        int length = length(arr);
        if (length == 0) {
            return new byte[0];
        }
        byte[] n = new byte[length];
        for (int i = 0; i < length; i++) {
            n[i] = arr[i];
        }
        return n;
    }

    public static short[] unWrap(Short[] arr) {
        int length = length(arr);
        if (length == 0) {
            return new short[0];
        }
        short[] n = new short[length];
        for (int i = 0; i < length; i++) {
            n[i] = arr[i];
        }
        return n;
    }

    public static int[] unWrap(Integer[] arr) {
        int length = length(arr);
        if (length == 0) {
            return new int[0];
        }
        int[] n = new int[length];
        for (int i = 0; i < length; i++) {
            n[i] = arr[i];
        }
        return n;
    }

    public static long[] unWrap(Long[] arr) {
        int length = length(arr);
        if (length == 0) {
            return new long[0];
        }
        long[] n = new long[length];
        for (int i = 0; i < length; i++) {
            n[i] = arr[i];
        }
        return n;
    }

    public static float[] unWrap(Float[] arr) {
        int length = length(arr);
        if (length == 0) {
            return new float[0];
        }
        float[] n = new float[length];
        for (int i = 0; i < length; i++) {
            n[i] = arr[i];
        }
        return n;
    }

    public static double[] unWrap(Double[] arr) {
        int length = length(arr);
        if (length == 0) {
            return new double[0];
        }
        double[] n = new double[length];
        for (int i = 0; i < length; i++) {
            n[i] = arr[i];
        }
        return n;
    }

    public static boolean[] unWrap(Boolean[] arr) {
        int length = length(arr);
        if (length == 0) {
            return new boolean[0];
        }
        boolean[] n = new boolean[length];
        for (int i = 0; i < length; i++) {
            n[i] = arr[i];
        }
        return n;
    }

    public static char[] unWrap(Character[] arr) {
        int length = length(arr);
        if (length == 0) {
            return new char[0];
        }
        char[] n = new char[length];
        for (int i = 0; i < length; i++) {
            n[i] = arr[i];
        }
        return n;
    }

    public static Object unWrap(Object arr) {
        if (arr == null) {
            return null;
        } else if (arr instanceof Byte[]) {
            return unWrap((Byte[]) arr);
        } else if (arr instanceof Short[]) {
            return unWrap((Short[]) arr);
        } else if (arr instanceof Integer[]) {
            return unWrap((Integer[]) arr);
        } else if (arr instanceof Long[]) {
            return unWrap((Long[]) arr);
        } else if (arr instanceof Float[]) {
            return unWrap((Float[]) arr);
        } else if (arr instanceof Double[]) {
            return unWrap((Double[]) arr);
        } else if (arr instanceof Boolean[]) {
            return unWrap((Boolean[]) arr);
        } else if (arr instanceof Character[]) {
            return unWrap((Character[]) arr);
        } else {
            return arr;
        }
    }

    // -------------------- merge --------------------

    /**
     * 合并数组
     *
     * @param generator e.g. Integer[]::new
     * @param arr       需要合并的数组
     * @param merge     需要合并的数组
     * @param <T>       ignore
     * @return 合并后的数组
     */
    @SafeVarargs
    public static <T> T[] merges(IntFunction<T[]> generator, T[] arr, T[]... merge) {
        if (length(merge) == 0) {
            return arr;
        }
        int len = length(arr);
        int maxLen = len;
        for (Object[] m : merge) {
            maxLen += length(m);
        }
        T[] array = generator.apply(maxLen);
        if (len != 0) {
            System.arraycopy(arr, 0, array, 0, len);
        }
        for (T[] m : merge) {
            for (int j = 0, mLen = length(m); j < mLen; j++) {
                array[len++] = m[j];
            }
        }
        return array;
    }

    public static byte[] merge(byte[] arr, byte[]... merge) {
        if (length(merge) == 0) {
            return arr;
        }
        int len = length(arr);
        int maxLen = len;
        for (byte[] m : merge) {
            maxLen += length(m);
        }
        byte[] gem = new byte[maxLen];
        if (len != 0) {
            System.arraycopy(arr, 0, gem, 0, len);
        }
        for (byte[] m : merge) {
            for (int j = 0, mLen = length(m); j < mLen; j++) {
                gem[len++] = m[j];
            }
        }
        return gem;
    }

    public static short[] merge(short[] arr, short[]... merge) {
        if (length(merge) == 0) {
            return arr;
        }
        int len = length(arr);
        int maxLen = len;
        for (short[] m : merge) {
            maxLen += length(m);
        }
        short[] array = new short[maxLen];
        if (len != 0) {
            System.arraycopy(arr, 0, array, 0, len);
        }
        for (short[] m : merge) {
            for (int j = 0, mLen = length(m); j < mLen; j++) {
                array[len++] = m[j];
            }
        }
        return array;
    }

    public static int[] merge(int[] arr, int[]... merge) {
        if (length(merge) == 0) {
            return arr;
        }
        int len = length(arr);
        int maxLen = len;
        for (int[] m : merge) {
            maxLen += length(m);
        }
        int[] array = new int[maxLen];
        if (len != 0) {
            System.arraycopy(arr, 0, array, 0, len);
        }
        for (int[] m : merge) {
            for (int j = 0, mLen = length(m); j < mLen; j++) {
                array[len++] = m[j];
            }
        }
        return array;
    }

    public static long[] merge(long[] arr, long[]... merge) {
        if (length(merge) == 0) {
            return arr;
        }
        int len = length(arr);
        int maxLen = len;
        for (long[] m : merge) {
            maxLen += length(m);
        }
        long[] array = new long[maxLen];
        if (len != 0) {
            System.arraycopy(arr, 0, array, 0, len);
        }
        for (long[] m : merge) {
            for (int j = 0, mLen = length(m); j < mLen; j++) {
                array[len++] = m[j];
            }
        }
        return array;
    }

    public static float[] merge(float[] arr, float[]... merge) {
        if (length(merge) == 0) {
            return arr;
        }
        int len = length(arr);
        int maxLen = len;
        for (float[] m : merge) {
            maxLen += length(m);
        }
        float[] array = new float[maxLen];
        if (len != 0) {
            System.arraycopy(arr, 0, array, 0, len);
        }
        for (float[] m : merge) {
            for (int j = 0, mLen = length(m); j < mLen; j++) {
                array[len++] = m[j];
            }
        }
        return array;
    }

    public static double[] merge(double[] arr, double[]... merge) {
        if (length(merge) == 0) {
            return arr;
        }
        int len = length(arr);
        int maxLen = len;
        for (double[] m : merge) {
            maxLen += length(m);
        }
        double[] array = new double[maxLen];
        if (len != 0) {
            System.arraycopy(arr, 0, array, 0, len);
        }
        for (double[] m : merge) {
            for (int j = 0, mLen = length(m); j < mLen; j++) {
                array[len++] = m[j];
            }
        }
        return array;
    }

    public static boolean[] merge(boolean[] arr, boolean[]... merge) {
        if (length(merge) == 0) {
            return arr;
        }
        int len = length(arr);
        int maxLen = len;
        for (boolean[] m : merge) {
            maxLen += length(m);
        }
        boolean[] array = new boolean[maxLen];
        if (len != 0) {
            System.arraycopy(arr, 0, array, 0, len);
        }
        for (boolean[] m : merge) {
            for (int j = 0, mLen = length(m); j < mLen; j++) {
                array[len++] = m[j];
            }
        }
        return array;
    }

    public static char[] merge(char[] arr, char[]... merge) {
        if (length(merge) == 0) {
            return arr;
        }
        int len = length(arr);
        int maxLen = len;
        for (char[] m : merge) {
            maxLen += length(m);
        }
        char[] array = new char[maxLen];
        if (len != 0) {
            System.arraycopy(arr, 0, array, 0, len);
        }
        for (char[] m : merge) {
            for (int j = 0, mLen = length(m); j < mLen; j++) {
                array[len++] = m[j];
            }
        }
        return array;
    }

    public static Object[] merge(Object[] arr, Object[]... merge) {
        if (length(merge) == 0) {
            return arr;
        }
        int len = length(arr);
        int maxLen = len;
        for (Object[] m : merge) {
            maxLen += length(m);
        }
        Object[] array = new Object[maxLen];
        if (len != 0) {
            System.arraycopy(arr, 0, array, 0, len);
        }
        for (Object[] m : merge) {
            for (int j = 0, mLen = length(m); j < mLen; j++) {
                array[len++] = m[j];
            }
        }
        return array;
    }

    // -------------------- indexOf --------------------

    /**
     * 查找第一个查询到的元素的位置
     *
     * @param arr   数组
     * @param s     元素
     * @param start 开始向后查找的下标
     * @return 位置
     */
    public static int indexOf(byte[] arr, byte s, int start) {
        return indexOfs(arr, s, start);
    }

    public static int indexOf(short[] arr, short s, int start) {
        return indexOfs(arr, s, start);
    }

    public static int indexOf(int[] arr, int s, int start) {
        return indexOfs(arr, s, start);
    }

    public static int indexOf(long[] arr, long s, int start) {
        return indexOfs(arr, s, start);
    }

    public static int indexOf(float[] arr, float s, int start) {
        return indexOfs(arr, s, start);
    }

    public static int indexOf(double[] arr, double s, int start) {
        return indexOfs(arr, s, start);
    }

    public static int indexOf(char[] arr, char s, int start) {
        return indexOfs(arr, s, start);
    }

    public static int indexOf(boolean[] arr, boolean s, int start) {
        return indexOfs(arr, s, start);
    }

    public static <T> int indexOf(T[] arr, T s, int start) {
        return indexOfs(arr, s, start);
    }

    public static int indexOf(byte[] arr, byte s) {
        return indexOfs(arr, s, 0);
    }

    public static int indexOf(short[] arr, short s) {
        return indexOfs(arr, s, 0);
    }

    public static int indexOf(int[] arr, int s) {
        return indexOfs(arr, s, 0);
    }

    public static int indexOf(long[] arr, long s) {
        return indexOfs(arr, s, 0);
    }

    public static int indexOf(float[] arr, float s) {
        return indexOfs(arr, s, 0);
    }

    public static int indexOf(double[] arr, double s) {
        return indexOfs(arr, s, 0);
    }

    public static int indexOf(char[] arr, char s) {
        return indexOfs(arr, s, 0);
    }

    public static int indexOf(boolean[] arr, boolean s) {
        return indexOfs(arr, s, 0);
    }

    public static <T> int indexOf(T[] arr, T s) {
        return indexOfs(arr, s, 0);
    }

    private static <T> int indexOfs(T arr, T s, int start) {
        Object[] array = ofs(arr);
        int length = array.length;
        if (start < 0) {
            return -1;
        } else if (start >= length) {
            return -1;
        }
        for (int i = start; i < length; i++) {
            if (Objects1.eq(array[i], s)) {
                return i;
            }
        }
        return -1;
    }

    // -------------------- lastIndexOf --------------------

    /**
     * 查找最后一个查询到的元素的位置
     *
     * @param arr   数组
     * @param s     元素
     * @param start 开始向前查找的下标
     * @return 位置
     */
    public static int lastIndexOf(byte[] arr, byte s, int start) {
        return lastIndexOfs(arr, s, start);
    }

    public static int lastIndexOf(short[] arr, short s, int start) {
        return lastIndexOfs(arr, s, start);
    }

    public static int lastIndexOf(int[] arr, int s, int start) {
        return lastIndexOfs(arr, s, start);
    }

    public static int lastIndexOf(long[] arr, long s, int start) {
        return lastIndexOfs(arr, s, start);
    }

    public static int lastIndexOf(float[] arr, float s, int start) {
        return lastIndexOfs(arr, s, start);
    }

    public static int lastIndexOf(double[] arr, double s, int start) {
        return lastIndexOfs(arr, s, start);
    }

    public static int lastIndexOf(char[] arr, char s, int start) {
        return lastIndexOfs(arr, s, start);
    }

    public static int lastIndexOf(boolean[] arr, boolean s, int start) {
        return lastIndexOfs(arr, s, start);
    }

    public static <T> int lastIndexOf(T[] arr, T s, int start) {
        return lastIndexOfs(arr, s, start);
    }

    public static int lastIndexOf(byte[] arr, byte s) {
        return lastIndexOfs(arr, s, arr.length - 1);
    }

    public static int lastIndexOf(short[] arr, short s) {
        return lastIndexOfs(arr, s, arr.length - 1);
    }

    public static int lastIndexOf(int[] arr, int s) {
        return lastIndexOfs(arr, s, arr.length - 1);
    }

    public static int lastIndexOf(long[] arr, long s) {
        return lastIndexOfs(arr, s, arr.length - 1);
    }

    public static int lastIndexOf(float[] arr, float s) {
        return lastIndexOfs(arr, s, arr.length - 1);
    }

    public static int lastIndexOf(double[] arr, double s) {
        return lastIndexOfs(arr, s, arr.length - 1);
    }

    public static int lastIndexOf(char[] arr, char s) {
        return lastIndexOfs(arr, s, arr.length - 1);
    }

    public static int lastIndexOf(boolean[] arr, boolean s) {
        return lastIndexOfs(arr, s, arr.length - 1);
    }

    public static <T> int lastIndexOf(T[] arr, T s) {
        return lastIndexOfs(arr, s, arr.length - 1);
    }

    private static <T> int lastIndexOfs(T arr, T s, int start) {
        Object[] array = ofs(arr);
        int length = array.length;
        if (start < 0) {
            return -1;
        } else if (start >= length) {
            return -1;
        }
        for (int i = start; i >= 0; i--) {
            if (Objects1.eq(array[i], s)) {
                return i;
            }
        }
        return -1;
    }

    // -------------------- contains --------------------

    /**
     * 判断数组是否包含某个元素
     *
     * @param arr 数组
     * @param s   元素
     * @return true包含
     */
    public static boolean contains(byte[] arr, byte s) {
        return indexOf(arr, s) != -1;
    }

    public static boolean contains(short[] arr, short s) {
        return indexOf(arr, s) != -1;
    }

    public static boolean contains(int[] arr, int s) {
        return indexOf(arr, s) != -1;
    }

    public static boolean contains(long[] arr, long s) {
        return indexOf(arr, s) != -1;
    }

    public static boolean contains(float[] arr, float s) {
        return indexOf(arr, s) != -1;
    }

    public static boolean contains(double[] arr, double s) {
        return indexOf(arr, s) != -1;
    }

    public static boolean contains(char[] arr, char s) {
        return indexOf(arr, s) != -1;
    }

    public static boolean contains(boolean[] arr, boolean s) {
        return indexOf(arr, s) != -1;
    }

    public static <T> boolean contains(T[] arr, T s) {
        return indexOf(arr, s) != -1;
    }

    // -------------------- count --------------------

    /**
     * 查询目标元素在数组中出现的次数
     *
     * @param target 目标元素
     * @param arr    数组 可以传基本类型的元素 但是不可以传基本类型的数组
     * @param <T>    T
     * @return 次数
     */
    @SafeVarargs
    public static <T> int count(T target, T... arr) {
        if (length(arr) == 0) {
            return 0;
        }
        int count = 0;
        for (T i : arr) {
            if (Objects1.eq(i, target)) {
                count++;
            }
        }
        return count;
    }

    public static int count(byte target, byte... arr) {
        if (length(arr) == 0) {
            return 0;
        }
        int count = 0;
        for (byte i : arr) {
            if (i == target) {
                count++;
            }
        }
        return count;
    }

    public static int count(short target, short... arr) {
        if (length(arr) == 0) {
            return 0;
        }
        int count = 0;
        for (short i : arr) {
            if (i == target) {
                count++;
            }
        }
        return count;
    }

    public static int count(int target, int... arr) {
        if (length(arr) == 0) {
            return 0;
        }
        int count = 0;
        for (int i : arr) {
            if (i == target) {
                count++;
            }
        }
        return count;
    }

    public static int count(long target, long... arr) {
        if (length(arr) == 0) {
            return 0;
        }
        int count = 0;
        for (long i : arr) {
            if (i == target) {
                count++;
            }
        }
        return count;
    }

    public static int count(float target, float... arr) {
        if (length(arr) == 0) {
            return 0;
        }
        int count = 0;
        for (float i : arr) {
            if (i == target) {
                count++;
            }
        }
        return count;
    }

    public static int count(double target, double... arr) {
        if (length(arr) == 0) {
            return 0;
        }
        int count = 0;
        for (double i : arr) {
            if (i == target) {
                count++;
            }
        }
        return count;
    }

    public static int count(boolean target, boolean... arr) {
        if (length(arr) == 0) {
            return 0;
        }
        int count = 0;
        for (boolean i : arr) {
            if (i == target) {
                count++;
            }
        }
        return count;
    }

    public static int count(char target, char... arr) {
        if (length(arr) == 0) {
            return 0;
        }
        int count = 0;
        for (char i : arr) {
            if (i == target) {
                count++;
            }
        }
        return count;
    }

    // -------------------- some --------------------

    /**
     * 查询目标元素是否出现在数组中
     *
     * @param target 目标元素
     * @param arr    数组, 可以传基本类型的元素, 但是不可以传基本类型的数组
     * @param <T>    T
     * @return true 出现
     */
    @SafeVarargs
    public static <T> boolean some(T target, T... arr) {
        if (length(arr) == 0) {
            return false;
        }
        for (T i : arr) {
            if (i == target) {
                return true;
            }
        }
        return false;
    }

    public static boolean some(byte target, byte... arr) {
        if (length(arr) == 0) {
            return false;
        }
        for (byte i : arr) {
            if (i == target) {
                return true;
            }
        }
        return false;
    }

    public static boolean some(short target, short... arr) {
        if (length(arr) == 0) {
            return false;
        }
        for (short i : arr) {
            if (i == target) {
                return true;
            }
        }
        return false;
    }

    public static boolean some(int target, int... arr) {
        if (length(arr) == 0) {
            return false;
        }
        for (int i : arr) {
            if (i == target) {
                return true;
            }
        }
        return false;
    }

    public static boolean some(long target, long... arr) {
        if (length(arr) == 0) {
            return false;
        }
        for (long i : arr) {
            if (i == target) {
                return true;
            }
        }
        return false;
    }

    public static boolean some(float target, float... arr) {
        if (length(arr) == 0) {
            return false;
        }
        for (float i : arr) {
            if (i == target) {
                return true;
            }
        }
        return false;
    }

    public static boolean some(double target, double... arr) {
        if (length(arr) == 0) {
            return false;
        }
        for (double i : arr) {
            if (i == target) {
                return true;
            }
        }
        return false;
    }

    public static boolean some(char target, char... arr) {
        if (length(arr) == 0) {
            return false;
        }
        for (char i : arr) {
            if (i == target) {
                return true;
            }
        }
        return false;
    }

    public static boolean some(boolean target, boolean... arr) {
        if (length(arr) == 0) {
            return false;
        }
        for (boolean i : arr) {
            if (i == target) {
                return true;
            }
        }
        return false;
    }

    // -------------------- reverse --------------------

    /**
     * 数组倒排
     *
     * @param arr 数组
     */
    public static void reverse(byte[] arr) {
        int length = length(arr);
        if (length == 0) {
            return;
        }
        for (int i = 0, mid = length / BINARY; i < mid; i++) {
            int len = length - i - 1;
            if (arr[i] != arr[len]) {
                swap(arr, i, len);
            }
        }
    }

    public static void reverse(short[] arr) {
        int length = length(arr);
        if (length == 0) {
            return;
        }
        for (int i = 0, mid = length / BINARY; i < mid; i++) {
            int len = length - i - 1;
            if (arr[i] != arr[len]) {
                swap(arr, i, len);
            }
        }
    }

    public static void reverse(int[] arr) {
        int length = length(arr);
        if (length == 0) {
            return;
        }
        for (int i = 0, mid = length / BINARY; i < mid; i++) {
            int len = length - i - 1;
            if (arr[i] != arr[len]) {
                swap(arr, i, len);
            }
        }
    }

    public static void reverse(long[] arr) {
        int length = length(arr);
        if (length == 0) {
            return;
        }
        for (int i = 0, mid = length / BINARY; i < mid; i++) {
            int len = length - i - 1;
            if (arr[i] != arr[len]) {
                swap(arr, i, len);
            }
        }
    }

    public static void reverse(float[] arr) {
        int length = length(arr);
        if (length == 0) {
            return;
        }
        for (int i = 0, mid = length / BINARY; i < mid; i++) {
            int len = length - i - 1;
            if (arr[i] != arr[len]) {
                swap(arr, i, len);
            }
        }
    }

    public static void reverse(double[] arr) {
        int length = length(arr);
        if (length == 0) {
            return;
        }
        for (int i = 0, mid = length / BINARY; i < mid; i++) {
            int len = length - i - 1;
            if (arr[i] != arr[len]) {
                swap(arr, i, len);
            }
        }
    }

    public static void reverse(char[] arr) {
        int length = length(arr);
        if (length == 0) {
            return;
        }
        for (int i = 0, mid = length / BINARY; i < mid; i++) {
            int len = length - i - 1;
            if (arr[i] != arr[len]) {
                swap(arr, i, len);
            }
        }
    }

    public static void reverse(boolean[] arr) {
        int length = length(arr);
        if (length == 0) {
            return;
        }
        for (int i = 0, mid = length / BINARY; i < mid; i++) {
            int len = length - i - 1;
            if (arr[i] != arr[len]) {
                swap(arr, i, len);
            }
        }
    }

    public static <T> void reverse(T[] arr) {
        int length = length(arr);
        if (length == 0) {
            return;
        }
        for (int i = 0, mid = length / BINARY; i < mid; i++) {
            int len = length - i - 1;
            if (arr[i] != arr[len]) {
                swap(arr, i, len);
            }
        }
    }

    // -------------------- first --------------------

    /**
     * 获取数组第一个元素
     *
     * @param arr array
     * @param <T> T
     * @return 第一个元素 长度为0则抛出异常
     */
    public static <T> T first(T[] arr) {
        int length = length(arr);
        if (length == 0) {
            throw Exceptions.arrayIndex("array is empty");
        }
        return arr[0];
    }

    public static byte first(byte[] arr) {
        int length = length(arr);
        if (length == 0) {
            throw Exceptions.arrayIndex("array is empty");
        }
        return arr[0];
    }

    public static short first(short[] array) {
        int length = length(array);
        if (length == 0) {
            throw Exceptions.arrayIndex("array is empty");
        }
        return array[0];
    }

    public static int first(int[] arr) {
        int length = length(arr);
        if (length == 0) {
            throw Exceptions.arrayIndex("array is empty");
        }
        return arr[0];
    }

    public static long first(long[] arr) {
        int length = length(arr);
        if (length == 0) {
            throw Exceptions.arrayIndex("array is empty");
        }
        return arr[0];
    }

    public static float first(float[] arr) {
        int length = length(arr);
        if (length == 0) {
            throw Exceptions.arrayIndex("array is empty");
        }
        return arr[0];
    }

    public static double first(double[] arr) {
        int length = length(arr);
        if (length == 0) {
            throw Exceptions.arrayIndex("array is empty");
        }
        return arr[0];
    }

    public static boolean first(boolean[] arr) {
        int length = length(arr);
        if (length == 0) {
            throw Exceptions.arrayIndex("array is empty");
        }
        return arr[0];
    }

    public static char first(char[] arr) {
        int length = length(arr);
        if (length == 0) {
            throw Exceptions.arrayIndex("array is empty");
        }
        return arr[0];
    }

    /**
     * 获取数组第一个元素
     *
     * @param arr array
     * @param def 默认值
     * @param <T> T
     * @return 第一个元素
     */
    public static <T> T first(T[] arr, T def) {
        int length = length(arr);
        if (length == 0) {
            return def;
        }
        return arr[0];
    }

    public static byte first(byte[] arr, byte def) {
        int length = length(arr);
        if (length == 0) {
            return def;
        }
        return arr[0];
    }

    public static short first(short[] arr, short def) {
        int length = length(arr);
        if (length == 0) {
            return def;
        }
        return arr[0];
    }

    public static int first(int[] arr, int def) {
        int length = length(arr);
        if (length == 0) {
            return def;
        }
        return arr[0];
    }

    public static long first(long[] arr, long def) {
        int length = length(arr);
        if (length == 0) {
            return def;
        }
        return arr[0];
    }

    public static float first(float[] arr, float def) {
        int length = length(arr);
        if (length == 0) {
            return def;
        }
        return arr[0];
    }

    public static double first(double[] arr, double def) {
        int length = length(arr);
        if (length == 0) {
            return def;
        }
        return arr[0];
    }

    public static boolean first(boolean[] arr, boolean def) {
        int length = length(arr);
        if (length == 0) {
            return def;
        }
        return arr[0];
    }

    public static char first(char[] arr, char def) {
        int length = length(arr);
        if (length == 0) {
            return def;
        }
        return arr[0];
    }

    // -------------------- last --------------------

    /**
     * 获取数组最后一个元素
     *
     * @param arr array
     * @param <T> T
     * @return 最后一个元素  长度为0则抛出异常
     */
    public static <T> T last(T[] arr) {
        int length = length(arr);
        if (length == 0) {
            throw Exceptions.arrayIndex("array is empty");
        }
        return arr[length - 1];
    }

    public static byte last(byte[] arr) {
        int length = length(arr);
        if (length == 0) {
            throw Exceptions.arrayIndex("array is empty");
        }
        return arr[length - 1];
    }

    public static short last(short[] arr) {
        int length = length(arr);
        if (length == 0) {
            throw Exceptions.arrayIndex("array is empty");
        }
        return arr[length - 1];
    }

    public static int last(int[] arr) {
        int length = length(arr);
        if (length == 0) {
            throw Exceptions.arrayIndex("array is empty");
        }
        return arr[length - 1];
    }

    public static long last(long[] arr) {
        int length = length(arr);
        if (length == 0) {
            throw Exceptions.arrayIndex("array is empty");
        }
        return arr[length - 1];
    }

    public static float last(float[] arr) {
        int length = length(arr);
        if (length == 0) {
            throw Exceptions.arrayIndex("array is empty");
        }
        return arr[length - 1];
    }

    public static double last(double[] arr) {
        int length = length(arr);
        if (length == 0) {
            throw Exceptions.arrayIndex("array is empty");
        }
        return arr[length - 1];
    }

    public static boolean last(boolean[] arr) {
        int length = length(arr);
        if (length == 0) {
            throw Exceptions.arrayIndex("array is empty");
        }
        return arr[length - 1];
    }

    public static char last(char[] arr) {
        int length = length(arr);
        if (length == 0) {
            throw Exceptions.arrayIndex("array is empty");
        }
        return arr[length - 1];
    }

    /**
     * 获取数组最后一个元素
     *
     * @param arr array
     * @param def 默认值
     * @param <T> T
     * @return 最后一个元素
     */
    public static <T> T last(T[] arr, T def) {
        int length = length(arr);
        if (length == 0) {
            return def;
        }
        return arr[length - 1];
    }

    public static byte last(byte[] arr, byte def) {
        int length = length(arr);
        if (length == 0) {
            return def;
        }
        return arr[length - 1];
    }

    public static short last(short[] arr, short def) {
        int length = length(arr);
        if (length == 0) {
            return def;
        }
        return arr[length - 1];
    }

    public static int last(int[] arr, int def) {
        int length = length(arr);
        if (length == 0) {
            return def;
        }
        return arr[length - 1];
    }

    public static long last(long[] arr, long def) {
        int length = length(arr);
        if (length == 0) {
            return def;
        }
        return arr[length - 1];
    }

    public static float last(float[] arr, float def) {
        int length = length(arr);
        if (length == 0) {
            return def;
        }
        return arr[length - 1];
    }

    public static double last(double[] arr, double def) {
        int length = length(arr);
        if (length == 0) {
            return def;
        }
        return arr[length - 1];
    }

    public static boolean last(boolean[] arr, boolean def) {
        int length = length(arr);
        if (length == 0) {
            return def;
        }
        return arr[length - 1];
    }

    public static char last(char[] arr, char def) {
        int length = length(arr);
        if (length == 0) {
            return def;
        }
        return arr[length - 1];
    }

    // -------------------- swap --------------------

    /**
     * 换位
     *
     * @param arr 数组
     * @param i   换位的元素1
     * @param j   换位的元素2
     */
    public static void unChangeSwap(byte[] arr, int i, int j) {
        arr[i] ^= arr[j];
        arr[j] ^= arr[i];
        arr[i] ^= arr[j];
    }

    public static void swap(byte[] arr, int i, int j) {
        byte temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void swap(short[] arr, int i, int j) {
        short temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void swap(long[] arr, int i, int j) {
        long temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void swap(float[] arr, int i, int j) {
        float temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void swap(double[] arr, int i, int j) {
        double temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void swap(char[] arr, int i, int j) {
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void swap(boolean[] arr, int i, int j) {
        boolean temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static <T> void swap(T[] arr, int i, int j) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // -------------------- max --------------------

    /**
     * 最大值
     *
     * @param arr 数组
     * @return 最大值
     */
    public static byte max(byte[] arr) {
        int len = length(arr);
        if (len == 0) {
            return 0;
        }
        byte max = arr[0];
        for (int i = 1; i < len; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
        }
        return max;
    }

    public static short max(short[] arr) {
        int len = length(arr);
        if (len == 0) {
            return 0;
        }
        short max = arr[0];
        for (int i = 1; i < len; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
        }
        return max;
    }

    public static int max(int[] arr) {
        int len = length(arr);
        if (len == 0) {
            return 0;
        }
        int max = arr[0];
        for (int i = 1; i < len; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
        }
        return max;
    }

    public static long max(long[] arr) {
        int len = length(arr);
        if (len == 0) {
            return 0;
        }
        long max = arr[0];
        for (int i = 1; i < len; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
        }
        return max;
    }

    public static float max(float[] arr) {
        int len = length(arr);
        if (len == 0) {
            return 0;
        }
        float max = arr[0];
        for (int i = 1; i < len; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
        }
        return max;
    }

    public static double max(double[] arr) {
        int len = length(arr);
        if (len == 0) {
            return 0;
        }
        double max = arr[0];
        for (int i = 1; i < len; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
        }
        return max;
    }

    public static char max(char[] arr) {
        int len = length(arr);
        if (len == 0) {
            return 0;
        }
        char max = arr[0];
        for (int i = 1; i < len; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
        }
        return max;
    }

    public static <T extends Comparable<T>> T max(T[] arr) {
        int len = length(arr);
        if (len == 0) {
            return null;
        }
        T max = arr[0];
        int offset = 1;
        if (max == null) {
            for (int blen = arr.length; offset < blen; offset++) {
                T bi = arr[offset];
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
            if (arr[i] != null && max.compareTo(arr[i]) < 0) {
                max = arr[i];
            }
        }
        return max;
    }

    public static <T> T max(T[] arr, Comparator<T> c) {
        int len = length(arr);
        if (len == 0) {
            return null;
        }
        T max = arr[0];
        int offset = 1;
        if (max == null) {
            for (int blen = arr.length; offset < blen; offset++) {
                T bi = arr[offset];
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
            if (arr[i] != null && c.compare(max, arr[i]) < 0) {
                max = arr[i];
            }
        }
        return max;
    }

    // -------------------- min --------------------

    /**
     * 最小值
     *
     * @param arr 数组
     * @return 最小值
     */
    public static byte min(byte[] arr) {
        int len = length(arr);
        if (len == 0) {
            return 0;
        }
        byte min = arr[0];
        for (int i = 1; i < len; i++) {
            if (min > arr[i]) {
                min = arr[i];
            }
        }
        return min;
    }

    public static short min(short[] arr) {
        int len = length(arr);
        if (len == 0) {
            return 0;
        }
        short min = arr[0];
        for (int i = 1; i < len; i++) {
            if (min > arr[i]) {
                min = arr[i];
            }
        }
        return min;
    }

    public static int min(int[] arr) {
        int len = length(arr);
        if (len == 0) {
            return 0;
        }
        int min = arr[0];
        for (int i = 1; i < len; i++) {
            if (min > arr[i]) {
                min = arr[i];
            }
        }
        return min;
    }

    public static long min(long[] arr) {
        int len = length(arr);
        if (len == 0) {
            return 0;
        }
        long min = arr[0];
        for (int i = 1; i < len; i++) {
            if (min > arr[i]) {
                min = arr[i];
            }
        }
        return min;
    }

    public static float min(float[] arr) {
        int len = length(arr);
        if (len == 0) {
            return 0;
        }
        float min = arr[0];
        for (int i = 1; i < len; i++) {
            if (min > arr[i]) {
                min = arr[i];
            }
        }
        return min;
    }

    public static double min(double[] arr) {
        int len = length(arr);
        if (len == 0) {
            return 0;
        }
        double min = arr[0];
        for (int i = 1; i < len; i++) {
            if (min > arr[i]) {
                min = arr[i];
            }
        }
        return min;
    }

    public static char min(char[] arr) {
        int len = length(arr);
        if (len == 0) {
            return 0;
        }
        char min = arr[0];
        for (int i = 1; i < len; i++) {
            if (min > arr[i]) {
                min = arr[i];
            }
        }
        return min;
    }

    public static <T extends Comparable<T>> T min(T[] arr) {
        int len = length(arr);
        if (len == 0) {
            return null;
        }
        T min = arr[0];
        int offset = 1;
        if (min == null) {
            for (int blen = arr.length; offset < blen; offset++) {
                T bi = arr[offset];
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
            if (arr[i] != null && min.compareTo(arr[i]) > 0) {
                min = arr[i];
            }
        }
        return min;
    }

    public static <T> T min(T[] arr, Comparator<T> c) {
        int len = length(arr);
        if (len == 0) {
            return null;
        }
        T min = arr[0];
        int offset = 1;
        if (min == null) {
            for (int blen = arr.length; offset < blen; offset++) {
                T bi = arr[offset];
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
            if (arr[i] != null && c.compare(min, arr[i]) > 0) {
                min = arr[i];
            }
        }
        return min;
    }

    // -------------------- hashcode --------------------

    /**
     * 获取数组的hashCode
     *
     * @param arr arr
     * @return hashCode
     */
    public static int hashCode(Object[] arr) {
        if (arr == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (Object element : arr) {
            hash = MULTIPLIER * hash + Objects1.hashCode(element);
        }
        return hash;
    }

    public static int hashCode(byte[] arr) {
        if (arr == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (byte element : arr) {
            hash = MULTIPLIER * hash + element;
        }
        return hash;
    }

    public static int hashCode(short[] arr) {
        if (arr == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (short element : arr) {
            hash = MULTIPLIER * hash + element;
        }
        return hash;
    }

    public static int hashCode(int[] arr) {
        if (arr == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (int element : arr) {
            hash = MULTIPLIER * hash + element;
        }
        return hash;
    }

    public static int hashCode(long[] arr) {
        if (arr == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (long element : arr) {
            hash = MULTIPLIER * hash + Long.hashCode(element);
        }
        return hash;
    }

    public static int hashCode(float[] arr) {
        if (arr == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (float element : arr) {
            hash = MULTIPLIER * hash + Float.hashCode(element);
        }
        return hash;
    }

    public static int hashCode(double[] arr) {
        if (arr == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (double element : arr) {
            hash = MULTIPLIER * hash + Double.hashCode(element);
        }
        return hash;
    }

    public static int hashCode(boolean[] arr) {
        if (arr == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (boolean element : arr) {
            hash = MULTIPLIER * hash + Boolean.hashCode(element);
        }
        return hash;
    }

    public static int hashCode(char[] arr) {
        if (arr == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (char element : arr) {
            hash = MULTIPLIER * hash + element;
        }
        return hash;
    }

    /**
     * 判断数组是否相等
     *
     * @param o1 o1
     * @param o2 o2
     * @return ignore
     */
    public static boolean arrayEquals(Object o1, Object o2) {
        if (o1 instanceof Object[] && o2 instanceof Object[]) {
            return Arrays.equals((Object[]) o1, (Object[]) o2);
        }
        if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
            return Arrays.equals((boolean[]) o1, (boolean[]) o2);
        }
        if (o1 instanceof byte[] && o2 instanceof byte[]) {
            return Arrays.equals((byte[]) o1, (byte[]) o2);
        }
        if (o1 instanceof char[] && o2 instanceof char[]) {
            return Arrays.equals((char[]) o1, (char[]) o2);
        }
        if (o1 instanceof double[] && o2 instanceof double[]) {
            return Arrays.equals((double[]) o1, (double[]) o2);
        }
        if (o1 instanceof float[] && o2 instanceof float[]) {
            return Arrays.equals((float[]) o1, (float[]) o2);
        }
        if (o1 instanceof int[] && o2 instanceof int[]) {
            return Arrays.equals((int[]) o1, (int[]) o2);
        }
        if (o1 instanceof long[] && o2 instanceof long[]) {
            return Arrays.equals((long[]) o1, (long[]) o2);
        }
        if (o1 instanceof short[] && o2 instanceof short[]) {
            return Arrays.equals((short[]) o1, (short[]) o2);
        }
        return false;
    }

}
