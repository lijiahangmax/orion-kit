package com.orion.utils;

import com.orion.function.Mapper;
import com.orion.function.Reduce;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * 数组工具类
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/9/9 10:25
 * <p>
 * 需要注意: 可变参数 Object... T... 不应该传基本类型的数组, 应该传基本类型的包装类,
 * 基本类型数组无法转化为包装类型的数组, 从而入参为转化Object, 导致与预期结果不相同
 */
@SuppressWarnings("ALL")
public class Arrays1 {

    private Arrays1() {
    }

    private static final int BINARY = 2;
    private static final int INITIAL_HASH = 7;
    private static final int MULTIPLIER = 31;

    // ------------------ new ------------------

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

    // ------------------ isArray ------------------

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

    // ------------------ get ------------------

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
        if (arr == null) {
            throw new RuntimeException("array is undefined");
        }
        return (T) Array.get(arr, i);
    }

    public static <T> T get(T[] arr, int i) {
        if (arr == null) {
            throw new RuntimeException("array is undefined");
        }
        if (arr.length <= i) {
            throw new ArrayIndexOutOfBoundsException("array length: " + arr.length + " get index: " + i);
        }
        return arr[i];
    }

    public static byte get(byte[] arr, int i) {
        if (arr == null) {
            throw new RuntimeException("array is undefined");
        }
        if (arr.length <= i) {
            throw new ArrayIndexOutOfBoundsException("array length: " + arr.length + " get index: " + i);
        }
        return arr[i];
    }

    public static short get(short[] arr, int i) {
        if (arr == null) {
            throw new RuntimeException("array is undefined");
        }
        if (arr.length <= i) {
            throw new ArrayIndexOutOfBoundsException("array length: " + arr.length + " get index: " + i);
        }
        return arr[i];
    }

    public static int get(int[] arr, int i) {
        if (arr == null) {
            throw new RuntimeException("array is undefined");
        }
        if (arr.length <= i) {
            throw new ArrayIndexOutOfBoundsException("array length: " + arr.length + " get index: " + i);
        }
        return arr[i];
    }

    public static long get(long[] arr, int i) {
        if (arr == null) {
            throw new RuntimeException("array is undefined");
        }
        if (arr.length <= i) {
            throw new ArrayIndexOutOfBoundsException("array length: " + arr.length + " get index: " + i);
        }
        return arr[i];
    }

    public static float get(float[] arr, int i) {
        if (arr == null) {
            throw new RuntimeException("array is undefined");
        }
        if (arr.length <= i) {
            throw new ArrayIndexOutOfBoundsException("array length: " + arr.length + " get index: " + i);
        }
        return arr[i];
    }

    public static double get(double[] arr, int i) {
        if (arr == null) {
            throw new RuntimeException("array is undefined");
        }
        if (arr.length <= i) {
            throw new ArrayIndexOutOfBoundsException("array length: " + arr.length + " get index: " + i);
        }
        return arr[i];
    }

    public static boolean get(boolean[] arr, int i) {
        if (arr == null) {
            throw new RuntimeException("array is undefined");
        }
        if (arr.length <= i) {
            throw new ArrayIndexOutOfBoundsException("array length: " + arr.length + " get index: " + i);
        }
        return arr[i];
    }

    public static char get(char[] arr, int i) {
        if (arr == null) {
            throw new RuntimeException("array is undefined");
        }
        if (arr.length <= i) {
            throw new ArrayIndexOutOfBoundsException("array length: " + arr.length + " get index: " + i);
        }
        return arr[i];
    }

    // ------------------ set ------------------

    /**
     * 设置数组下标对应的值
     *
     * @param arr   数组
     * @param i     下标
     * @param value 值
     * @param <T>   ignore
     */
    public static <T> void sets(Object arr, int i, T value) {
        if (arr == null) {
            throw new RuntimeException("array is undefined");
        }
        Array.set(arr, i, value);
    }

    public static <T> void set(T[] arr, int i, T value) {
        if (arr == null) {
            throw new RuntimeException("array is undefined");
        }
        if (arr.length <= i) {
            throw new ArrayIndexOutOfBoundsException("array length: " + arr.length + " get index: " + i);
        }
        arr[i] = value;
    }

    public static void set(byte[] arr, int i, byte value) {
        if (arr == null) {
            throw new RuntimeException("array is undefined");
        }
        if (arr.length <= i) {
            throw new ArrayIndexOutOfBoundsException("array length: " + arr.length + " get index: " + i);
        }
        arr[i] = value;
    }

    public static void set(short[] arr, int i, short value) {
        if (arr == null) {
            throw new RuntimeException("array is undefined");
        }
        if (arr.length <= i) {
            throw new ArrayIndexOutOfBoundsException("array length: " + arr.length + " get index: " + i);
        }
        arr[i] = value;
    }

    public static void set(int[] arr, int i, int value) {
        if (arr == null) {
            throw new RuntimeException("array is undefined");
        }
        if (arr.length <= i) {
            throw new ArrayIndexOutOfBoundsException("array length: " + arr.length + " get index: " + i);
        }
        arr[i] = value;
    }

    public static void set(long[] arr, int i, long value) {
        if (arr == null) {
            throw new RuntimeException("array is undefined");
        }
        if (arr.length <= i) {
            throw new ArrayIndexOutOfBoundsException("array length: " + arr.length + " get index: " + i);
        }
        arr[i] = value;
    }

    public static void set(float[] arr, int i, float value) {
        if (arr == null) {
            throw new RuntimeException("array is undefined");
        }
        if (arr.length <= i) {
            throw new ArrayIndexOutOfBoundsException("array length: " + arr.length + " get index: " + i);
        }
        arr[i] = value;
    }

    public static void set(double[] arr, int i, double value) {
        if (arr == null) {
            throw new RuntimeException("array is undefined");
        }
        if (arr.length <= i) {
            throw new ArrayIndexOutOfBoundsException("array length: " + arr.length + " get index: " + i);
        }
        arr[i] = value;
    }

    public static void set(boolean[] arr, int i, boolean value) {
        if (arr == null) {
            throw new RuntimeException("array is undefined");
        }
        if (arr.length <= i) {
            throw new ArrayIndexOutOfBoundsException("array length: " + arr.length + " get index: " + i);
        }
        arr[i] = value;
    }

    public static void set(char[] arr, int i, char value) {
        if (arr == null) {
            throw new RuntimeException("array is undefined");
        }
        if (arr.length <= i) {
            throw new ArrayIndexOutOfBoundsException("array length: " + arr.length + " get index: " + i);
        }
        arr[i] = value;
    }

    // ------------------ Mapper Reducer ------------------

    /**
     * 映射
     *
     * @param mapper    映射接口
     * @param generator 数组创建接口 e.g. Integer[]::new
     * @param i         需要映射的对象 该对象不能为基本类型的数组, 可以为基本类型的变量
     * @param <I>       输入类型
     * @param <O>       输出类型
     * @return 映射结果
     */
    public static <I, O> O[] mapper(Mapper<? super I, ? extends O> mapper, IntFunction<? extends O[]> generator, I... i) {
        int len = length(i);
        if (len == 0) {
            throw new IllegalArgumentException("i... length is 0");
        }
        O[] oa = generator.apply(len);
        for (int j = 0; j < len; j++) {
            O o = mapper.map(i[j]);
            oa[j] = o;
        }
        return oa;
    }

    /**
     * 规约
     *
     * @param reduce 规约接口
     * @param v      需要规约的对象 该对象不能为基本类型的数组, 可以为基本类型的变量
     * @param <V>    数据类型
     * @param <R>    规约类型
     * @return 规约后的数据
     */
    public static <V, R> R reduce(Reduce<? super V, ? extends R> reduce, V... v) {
        int len = length(v);
        if (len == 0) {
            throw new IllegalArgumentException("v... length is 0");
        }
        return reduce.accept(v);
    }

    // ------------------ compact ------------------

    /**
     * 去除数组中的 null
     *
     * @param o 数组
     * @return 处理后的数组, 如果数组没有null则直接返回, 如果数组有null, 去除null后的数组顺序可能不一样
     */
    public static Object[] compact(Object[] o) {
        int len = length(o);
        if (len == 0) {
            return new Object[0];
        } else if (len == 1) {
            if (o[0] == null) {
                return new Object[0];
            }
        }
        int e = 0;
        for (; ; ) {
            int i = compactSwap(o, len, e);
            if (i == 0) {
                break;
            } else {
                e += i;
            }
        }
        Object[] na = new Object[len - e];
        System.arraycopy(o, 0, na, 0, len - e);
        return na;
    }

    public static <T> T[] compacts(T[] o, IntFunction<T[]> generator) {
        int len = length(o);
        if (len == 0) {
            return generator.apply(0);
        } else if (len == 1) {
            if (o[0] == null) {
                return generator.apply(0);
            }
        }
        int e = 0;
        for (; ; ) {
            int i = compactSwap(o, len, e);
            if (i == 0) {
                break;
            } else {
                e += i;
            }
        }
        T[] na = generator.apply(len - e);
        System.arraycopy(o, 0, na, 0, len - e);
        return na;
    }

    /**
     * 交换数组中的位置
     *
     * @param arr 数组
     * @param len 数组长度
     * @param end 数组结尾处
     * @return 处理空值的次数
     */
    private static int compactSwap(Object[] arr, int len, int end) {
        int ne = 0;
        for (int i = 0; i < len - end; i++) {
            if (arr[i] == null) {
                end++;
                ne++;
                swap(arr, i, len - end);
            }
        }
        return ne;
    }

    // ------------------ exclude ------------------

    /**
     * 排除数组中指定的值
     *
     * @param a  数组
     * @param es 排除值
     * @return 处理后的数组, 如果数组没有包含的值则直接返回, 如果数组有, 去除后的数组顺序可能不一样
     */
    public static byte[] exclude(byte[] a, byte... es) {
        int len = length(a);
        if (len == 0) {
            return new byte[0];
        }
        int e = 0;
        for (; ; ) {
            int i = excludeSwap(a, len, e, es);
            if (i == 0) {
                break;
            } else {
                e += i;
            }
        }
        byte[] na = new byte[len - e];
        System.arraycopy(a, 0, na, 0, len - e);
        return na;
    }

    public static short[] exclude(short[] a, short... es) {
        int len = length(a);
        if (len == 0) {
            return new short[0];
        }
        int e = 0;
        for (; ; ) {
            int i = excludeSwap(a, len, e, es);
            if (i == 0) {
                break;
            } else {
                e += i;
            }
        }
        short[] na = new short[len - e];
        System.arraycopy(a, 0, na, 0, len - e);
        return na;
    }

    public static int[] exclude(int[] a, int... es) {
        int len = length(a);
        if (len == 0) {
            return new int[0];
        }
        int e = 0;
        for (; ; ) {
            int i = excludeSwap(a, len, e, es);
            if (i == 0) {
                break;
            } else {
                e += i;
            }
        }
        int[] na = new int[len - e];
        System.arraycopy(a, 0, na, 0, len - e);
        return na;
    }

    public static long[] exclude(long[] a, long... es) {
        int len = length(a);
        if (len == 0) {
            return new long[0];
        }
        int e = 0;
        for (; ; ) {
            int i = excludeSwap(a, len, e, es);
            if (i == 0) {
                break;
            } else {
                e += i;
            }
        }
        long[] na = new long[len - e];
        System.arraycopy(a, 0, na, 0, len - e);
        return na;
    }

    public static float[] exclude(float[] a, float... es) {
        int len = length(a);
        if (len == 0) {
            return new float[0];
        }
        int e = 0;
        for (; ; ) {
            int i = excludeSwap(a, len, e, es);
            if (i == 0) {
                break;
            } else {
                e += i;
            }
        }
        float[] na = new float[len - e];
        System.arraycopy(a, 0, na, 0, len - e);
        return na;
    }

    public static double[] exclude(double[] a, double... es) {
        int len = length(a);
        if (len == 0) {
            return new double[0];
        }
        int e = 0;
        for (; ; ) {
            int i = excludeSwap(a, len, e, es);
            if (i == 0) {
                break;
            } else {
                e += i;
            }
        }
        double[] na = new double[len - e];
        System.arraycopy(a, 0, na, 0, len - e);
        return na;
    }

    public static char[] exclude(char[] a, char... es) {
        int len = length(a);
        if (len == 0) {
            return new char[0];
        }
        int e = 0;
        for (; ; ) {
            int i = excludeSwap(a, len, e, es);
            if (i == 0) {
                break;
            } else {
                e += i;
            }
        }
        char[] na = new char[len - e];
        System.arraycopy(a, 0, na, 0, len - e);
        return na;
    }

    public static boolean[] exclude(boolean[] a, boolean es) {
        int len = length(a);
        if (len == 0) {
            return new boolean[0];
        }
        int e = 0;
        for (; ; ) {
            int i = excludeSwap(a, len, e, es);
            if (i == 0) {
                break;
            } else {
                e += i;
            }
        }
        boolean[] na = new boolean[len - e];
        System.arraycopy(a, 0, na, 0, len - e);
        return na;
    }

    /**
     * 排除数组中指定的值
     *
     * @param a  数组
     * @param es 排除值 这个值可以为基本类型的元素, 但是不能是基本类型的数组
     * @return 处理后的数组, 如果数组没有包含的值则直接返回, 如果数组有, 去除后的数组顺序可能不一样
     */
    public static Object[] exclude(Object[] a, Object... es) {
        int len = length(a);
        if (len == 0) {
            return new Object[0];
        }
        int e = 0;
        for (; ; ) {
            int i = excludeSwap(a, len, e, es);
            if (i == 0) {
                break;
            } else {
                e += i;
            }
        }
        Object[] na = new Object[len - e];
        System.arraycopy(a, 0, na, 0, len - e);
        return na;
    }

    public static <T> T[] excludes(T[] a, IntFunction<T[]> generator, T... es) {
        int len = length(a);
        if (len == 0) {
            return generator.apply(0);
        }
        int e = 0;
        for (; ; ) {
            int i = excludeSwap(a, len, e, es);
            if (i == 0) {
                break;
            } else {
                e += i;
            }
        }
        T[] na = generator.apply(len - e);
        System.arraycopy(a, 0, na, 0, len - e);
        return na;
    }

    private static int excludeSwap(byte[] arr, int len, int end, byte... es) {
        int ne = 0;
        for (int i = 0; i < len - end; i++) {
            for (byte e : es) {
                if (Objects1.eq(arr[i], e)) {
                    end++;
                    ne++;
                    swap(arr, i, len - end);
                    break;
                }
            }
        }
        return ne;
    }

    private static int excludeSwap(short[] arr, int len, int end, short... es) {
        int ne = 0;
        for (int i = 0; i < len - end; i++) {
            for (short e : es) {
                if (Objects1.eq(arr[i], e)) {
                    end++;
                    ne++;
                    swap(arr, i, len - end);
                    break;
                }
            }
        }
        return ne;
    }

    private static int excludeSwap(int[] arr, int len, int end, int... es) {
        int ne = 0;
        for (int i = 0; i < len - end; i++) {
            for (int e : es) {
                if (Objects1.eq(arr[i], e)) {
                    end++;
                    ne++;
                    swap(arr, i, len - end);
                    break;
                }
            }
        }
        return ne;
    }

    private static int excludeSwap(long[] arr, int len, int end, long... es) {
        int ne = 0;
        for (int i = 0; i < len - end; i++) {
            for (long e : es) {
                if (Objects1.eq(arr[i], e)) {
                    end++;
                    ne++;
                    swap(arr, i, len - end);
                    break;
                }
            }
        }
        return ne;
    }

    private static int excludeSwap(float[] arr, int len, int end, float... es) {
        int ne = 0;
        for (int i = 0; i < len - end; i++) {
            for (float e : es) {
                if (Objects1.eq(arr[i], e)) {
                    end++;
                    ne++;
                    swap(arr, i, len - end);
                    break;
                }
            }
        }
        return ne;
    }

    private static int excludeSwap(double[] arr, int len, int end, double... es) {
        int ne = 0;
        for (int i = 0; i < len - end; i++) {
            for (double e : es) {
                if (Objects1.eq(arr[i], e)) {
                    end++;
                    ne++;
                    swap(arr, i, len - end);
                    break;
                }
            }
        }
        return ne;
    }

    private static int excludeSwap(char[] arr, int len, int end, char... es) {
        int ne = 0;
        for (int i = 0; i < len - end; i++) {
            for (char e : es) {
                if (Objects1.eq(arr[i], e)) {
                    end++;
                    ne++;
                    swap(arr, i, len - end);
                    break;
                }
            }
        }
        return ne;
    }

    private static int excludeSwap(boolean[] arr, int len, int end, boolean es) {
        int ne = 0;
        for (int i = 0; i < len - end; i++) {
            if (Objects1.eq(arr[i], es)) {
                end++;
                ne++;
                swap(arr, i, len - end);
                break;
            }
        }
        return ne;
    }

    private static int excludeSwap(Object[] arr, int len, int end, Object... es) {
        int ne = 0;
        for (int i = 0; i < len - end; i++) {
            for (Object e : es) {
                if (Objects1.eq(arr[i], e)) {
                    end++;
                    ne++;
                    swap(arr, i, len - end);
                    break;
                }
            }
        }
        return ne;
    }

    // ------------------ forEach ------------------

    /**
     * 消费对象
     *
     * @param consumer 消费函数
     * @param e        元素, 不可以是基本类型的数组
     * @param <T>      对象类型
     */
    public static <T> void forEachs(Consumer<? super T> consumer, T... e) {
        for (int i = 0, len = length(e); i < len; i++) {
            consumer.accept(e[i]);
        }
    }

    public static void forEach(Consumer<Object> consumer, Object[] arr) {
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

    // ------------------ toArray ------------------

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

    // ------------------ asArray ------------------

    /**
     * 将可变参数转化为数组
     *
     * @param i 可边参数
     * @return 数组
     */
    public static byte[] of(byte... i) {
        return i;
    }

    public static short[] of(short... i) {
        return i;
    }

    public static int[] of(int... i) {
        return i;
    }

    public static long[] of(long... i) {
        return i;
    }

    public static float[] of(float... i) {
        return i;
    }

    public static double[] of(double... i) {
        return i;
    }

    public static boolean[] of(boolean... i) {
        return i;
    }

    public static char[] of(char... i) {
        return i;
    }

    public static String[] of(String... i) {
        return i;
    }

    /**
     * 不可接收基本类型的数组, 可以使用包装类 否则会返回 T[][]
     *
     * @param t   t
     * @param <T> t
     * @return array
     */
    public static <T> T[] oft(T... t) {
        return t;
    }

    /**
     * 可接收基本类型的数组, 返回Object[]
     *
     * @param t   t
     * @param <T> t
     * @return array
     */
    public static Object[] ofs(Object... i) {
        int lengths = lengths(i);
        if (lengths == 0) {
            return new Object[]{};
        }
        Object[] r = new Object[lengths];
        int len = i.length;
        if (len != lengths) {
            for (int j = 0; j < lengths; j++) {
                r[j] = Array.get(i[0], j);
            }
        } else {
            return i;
        }
        return r;
    }

    // ------------------ isEmpty ------------------

    /**
     * 判断数组是否为空
     *
     * @param o 数组
     * @return true为空
     */
    public static boolean isEmpties(Object o) {
        return o == null || (isArray(o) && Array.getLength(o) == 0);
    }

    public static boolean isEmpty(Object[] o) {
        return o == null || length(o) == 0;
    }

    public static boolean isEmpty(byte[] o) {
        return o == null || length(o) == 0;
    }

    public static boolean isEmpty(short[] o) {
        return o == null || length(o) == 0;
    }

    public static boolean isEmpty(int[] o) {
        return o == null || length(o) == 0;
    }

    public static boolean isEmpty(long[] o) {
        return o == null || length(o) == 0;
    }

    public static boolean isEmpty(float[] o) {
        return o == null || length(o) == 0;
    }

    public static boolean isEmpty(double[] o) {
        return o == null || length(o) == 0;
    }

    public static boolean isEmpty(boolean[] o) {
        return o == null || length(o) == 0;
    }

    public static boolean isEmpty(char[] o) {
        return o == null || length(o) == 0;
    }

    // ------------------ isNotEmpty ------------------

    /**
     * 判断数组是否不为空
     *
     * @param o 数组
     * @return true不为空
     */
    public static boolean isNotEmpties(Object o) {
        return !isEmpties(o);
    }

    public static boolean isNotEmpty(Object[] o) {
        return !isEmpty(o);
    }

    public static boolean isNotEmpty(byte[] o) {
        return !isEmpty(o);
    }

    public static boolean isNotEmpty(short[] o) {
        return !isEmpty(o);
    }

    public static boolean isNotEmpty(int[] o) {
        return !isEmpty(o);
    }

    public static boolean isNotEmpty(long[] o) {
        return !isEmpty(o);
    }

    public static boolean isNotEmpty(float[] o) {
        return !isEmpty(o);
    }

    public static boolean isNotEmpty(double[] o) {
        return !isEmpty(o);
    }

    public static boolean isNotEmpty(boolean[] o) {
        return !isEmpty(o);
    }

    public static boolean isNotEmpty(char[] o) {
        return !isEmpty(o);
    }

    // ------------------ length ------------------

    /**
     * 获取数组长度
     *
     * @param arr 数组 如果传参是一个基本类型数组, 返回值也是基本类型数组的长度
     * @return 长度
     * <p>
     * 在默认情况下, 如果可变参数o, 传参是基本类型的数组, 使用length属性判断, o.length = 1
     */
    @SuppressWarnings("all")
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

    // ------------------ resize ------------------

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

    public static String[] resize(String[] arr, int newSize) {
        if (newSize <= 0) {
            return new String[0];
        }
        if (arr.length < newSize) {
            String[] nbs = new String[newSize];
            System.arraycopy(arr, 0, nbs, 0, arr.length);
            return nbs;
        } else if (arr.length > newSize) {
            String[] nbs = new String[newSize];
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

    // ------------------ arraycopy ------------------

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

    // ------------------ length ------------------

    public static int length(Object[] arr) {
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

    // ------------------ wrap ------------------

    /**
     * 将一种数组转化为另一种数组
     *
     * @param w            数组
     * @param wrapFunction 适配器
     * @param generator    e.g. Integer[]::new
     * @param <T>          ignore
     * @param <R>          ignore
     * @return 适配后的数组
     */
    public static <T, R> R[] wraps(T[] w, Function<T, R> wrapFunction, IntFunction<R[]> generator) {
        int length = length(w);
        if (length == 0) {
            return generator.apply(0);
        }
        R[] n = generator.apply(length);
        for (int i = 0; i < length; i++) {
            n[i] = wrapFunction.apply(w[i]);
        }
        return n;
    }

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

    public static Integer[] wrap(int[] w) {
        int length = length(w);
        if (length == 0) {
            return new Integer[0];
        }
        Integer[] n = new Integer[length];
        for (int i = 0; i < length; i++) {
            n[i] = w[i];
        }
        return n;
    }

    public static Long[] wrap(long[] w) {
        int length = length(w);
        if (length == 0) {
            return new Long[0];
        }
        Long[] n = new Long[length];
        for (int i = 0; i < length; i++) {
            n[i] = w[i];
        }
        return n;
    }

    public static Float[] wrap(float[] w) {
        int length = length(w);
        if (length == 0) {
            return new Float[0];
        }
        Float[] n = new Float[length];
        for (int i = 0; i < length; i++) {
            n[i] = w[i];
        }
        return n;
    }

    public static Double[] wrap(double[] w) {
        int length = length(w);
        if (length == 0) {
            return new Double[0];
        }
        Double[] n = new Double[length];
        for (int i = 0; i < length; i++) {
            n[i] = w[i];
        }
        return n;
    }

    public static Character[] wrap(char[] w) {
        int length = length(w);
        if (length == 0) {
            return new Character[0];
        }
        Character[] n = new Character[length];
        for (int i = 0; i < length; i++) {
            n[i] = w[i];
        }
        return n;
    }

    public static Boolean[] wrap(boolean[] w) {
        int length = length(w);
        if (length == 0) {
            return new Boolean[0];
        }
        Boolean[] n = new Boolean[length];
        for (int i = 0; i < length; i++) {
            n[i] = w[i];
        }
        return n;
    }

    // ------------------ drap ------------------

    /**
     * 将数组转化为其基本装类
     *
     * @param w 数组
     * @return 基本数组
     */
    public static byte[] drap(Byte[] w) {
        int length = length(w);
        if (length == 0) {
            return new byte[0];
        }
        byte[] n = new byte[length];
        for (int i = 0; i < length; i++) {
            n[i] = w[i];
        }
        return n;
    }

    public static short[] drap(Short[] w) {
        int length = length(w);
        if (length == 0) {
            return new short[0];
        }
        short[] n = new short[length];
        for (int i = 0; i < length; i++) {
            n[i] = w[i];
        }
        return n;
    }

    public static int[] drap(Integer[] w) {
        int length = length(w);
        if (length == 0) {
            return new int[0];
        }
        int[] n = new int[length];
        for (int i = 0; i < length; i++) {
            n[i] = w[i];
        }
        return n;
    }

    public static long[] drap(Long[] w) {
        int length = length(w);
        if (length == 0) {
            return new long[0];
        }
        long[] n = new long[length];
        for (int i = 0; i < length; i++) {
            n[i] = w[i];
        }
        return n;
    }

    public static float[] drap(Float[] w) {
        int length = length(w);
        if (length == 0) {
            return new float[0];
        }
        float[] n = new float[length];
        for (int i = 0; i < length; i++) {
            n[i] = w[i];
        }
        return n;
    }

    public static double[] drap(Double[] w) {
        int length = length(w);
        if (length == 0) {
            return new double[0];
        }
        double[] n = new double[length];
        for (int i = 0; i < length; i++) {
            n[i] = w[i];
        }
        return n;
    }

    public static char[] drap(Character[] w) {
        int length = length(w);
        if (length == 0) {
            return new char[0];
        }
        char[] n = new char[length];
        for (int i = 0; i < length; i++) {
            n[i] = w[i];
        }
        return n;
    }

    public static boolean[] drap(Boolean[] w) {
        int length = length(w);
        if (length == 0) {
            return new boolean[0];
        }
        boolean[] n = new boolean[length];
        for (int i = 0; i < length; i++) {
            n[i] = w[i];
        }
        return n;
    }

    // ------------------ merge ------------------

    /**
     * 合并数组
     *
     * @param generator e.g. Integer[]::new
     * @param a         需要合并的数组
     * @param ms        需要合并的数组
     * @param <T>       ignore
     * @return 合并后的数组
     */
    public static <T> T[] merges(IntFunction<T[]> generator, T[] a, T[]... ms) {
        if (length(ms) == 0) {
            return a;
        }
        int len = length(a);
        int maxLen = len;
        for (Object[] m : ms) {
            maxLen += length(m);
        }
        T[] arr = generator.apply(maxLen);
        if (len != 0) {
            System.arraycopy(a, 0, arr, 0, len);
        }
        for (T[] m : ms) {
            for (int j = 0, mLen = length(m); j < mLen; j++) {
                arr[len++] = m[j];
            }
        }
        return arr;
    }

    public static byte[] merge(byte[] a, byte[]... ms) {
        if (length(ms) == 0) {
            return a;
        }
        int len = length(a);
        int maxLen = len;
        for (byte[] m : ms) {
            maxLen += length(m);
        }
        byte[] arr = new byte[maxLen];
        if (len != 0) {
            System.arraycopy(a, 0, arr, 0, len);
        }
        for (byte[] m : ms) {
            for (int j = 0, mLen = length(m); j < mLen; j++) {
                arr[len++] = m[j];
            }
        }
        return arr;
    }

    public static short[] merge(short[] a, short[]... ms) {
        if (length(ms) == 0) {
            return a;
        }
        int len = length(a);
        int maxLen = len;
        for (short[] m : ms) {
            maxLen += length(m);
        }
        short[] arr = new short[maxLen];
        if (len != 0) {
            System.arraycopy(a, 0, arr, 0, len);
        }
        for (short[] m : ms) {
            for (int j = 0, mLen = length(m); j < mLen; j++) {
                arr[len++] = m[j];
            }
        }
        return arr;
    }

    public static int[] merge(int[] a, int[]... ms) {
        if (length(ms) == 0) {
            return a;
        }
        int len = length(a);
        int maxLen = len;
        for (int[] m : ms) {
            maxLen += length(m);
        }
        int[] arr = new int[maxLen];
        if (len != 0) {
            System.arraycopy(a, 0, arr, 0, len);
        }
        for (int[] m : ms) {
            for (int j = 0, mLen = length(m); j < mLen; j++) {
                arr[len++] = m[j];
            }
        }
        return arr;
    }

    public static long[] merge(long[] a, long[]... ms) {
        if (length(ms) == 0) {
            return a;
        }
        int len = length(a);
        int maxLen = len;
        for (long[] m : ms) {
            maxLen += length(m);
        }
        long[] arr = new long[maxLen];
        if (len != 0) {
            System.arraycopy(a, 0, arr, 0, len);
        }
        for (long[] m : ms) {
            for (int j = 0, mLen = length(m); j < mLen; j++) {
                arr[len++] = m[j];
            }
        }
        return arr;
    }

    public static float[] merge(float[] a, float[]... ms) {
        if (length(ms) == 0) {
            return a;
        }
        int len = length(a);
        int maxLen = len;
        for (float[] m : ms) {
            maxLen += length(m);
        }
        float[] arr = new float[maxLen];
        if (len != 0) {
            System.arraycopy(a, 0, arr, 0, len);
        }
        for (float[] m : ms) {
            for (int j = 0, mLen = length(m); j < mLen; j++) {
                arr[len++] = m[j];
            }
        }
        return arr;
    }

    public static double[] merge(double[] a, double[]... ms) {
        if (length(ms) == 0) {
            return a;
        }
        int len = length(a);
        int maxLen = len;
        for (double[] m : ms) {
            maxLen += length(m);
        }
        double[] arr = new double[maxLen];
        if (len != 0) {
            System.arraycopy(a, 0, arr, 0, len);
        }
        for (double[] m : ms) {
            for (int j = 0, mLen = length(m); j < mLen; j++) {
                arr[len++] = m[j];
            }
        }
        return arr;
    }

    public static char[] merge(char[] a, char[]... ms) {
        if (length(ms) == 0) {
            return a;
        }
        int len = length(a);
        int maxLen = len;
        for (char[] m : ms) {
            maxLen += length(m);
        }
        char[] arr = new char[maxLen];
        if (len != 0) {
            System.arraycopy(a, 0, arr, 0, len);
        }
        for (char[] m : ms) {
            for (int j = 0, mLen = length(m); j < mLen; j++) {
                arr[len++] = m[j];
            }
        }
        return arr;
    }

    public static boolean[] merge(boolean[] a, boolean[]... ms) {
        if (length(ms) == 0) {
            return a;
        }
        int len = length(a);
        int maxLen = len;
        for (boolean[] m : ms) {
            maxLen += length(m);
        }
        boolean[] arr = new boolean[maxLen];
        if (len != 0) {
            System.arraycopy(a, 0, arr, 0, len);
        }
        for (boolean[] m : ms) {
            for (int j = 0, mLen = length(m); j < mLen; j++) {
                arr[len++] = m[j];
            }
        }
        return arr;
    }

    public static Object[] merge(Object[] a, Object[]... ms) {
        if (length(ms) == 0) {
            return a;
        }
        int len = length(a);
        int maxLen = len;
        for (Object[] m : ms) {
            maxLen += length(m);
        }
        Object[] arr = new Object[maxLen];
        if (len != 0) {
            System.arraycopy(a, 0, arr, 0, len);
        }
        for (Object[] m : ms) {
            for (int j = 0, mLen = length(m); j < mLen; j++) {
                arr[len++] = m[j];
            }
        }
        return arr;
    }

    // ------------------ indexOf ------------------

    /**
     * 查找第一个查询到的元素的位置
     *
     * @param a     数组
     * @param s     元素
     * @param start 开始向后查找的下标
     * @return 位置
     */
    public static int indexOf(byte[] a, byte s, int start) {
        return indexOfs(a, s, start);
    }

    public static int indexOf(short[] a, short s, int start) {
        return indexOfs(a, s, start);
    }

    public static int indexOf(int[] a, int s, int start) {
        return indexOfs(a, s, start);
    }

    public static int indexOf(long[] a, long s, int start) {
        return indexOfs(a, s, start);
    }

    public static int indexOf(float[] a, float s, int start) {
        return indexOfs(a, s, start);
    }

    public static int indexOf(double[] a, double s, int start) {
        return indexOfs(a, s, start);
    }

    public static int indexOf(char[] a, char s, int start) {
        return indexOfs(a, s, start);
    }

    public static int indexOf(boolean[] a, boolean s, int start) {
        return indexOfs(a, s, start);
    }

    public static int indexOf(Object[] a, Object s, int start) {
        return indexOfs(a, s, start);
    }

    public static int indexOf(byte[] a, byte s) {
        return indexOfs(a, s, 0);
    }

    public static int indexOf(short[] a, short s) {
        return indexOfs(a, s, 0);
    }

    public static int indexOf(int[] a, int s) {
        return indexOfs(a, s, 0);
    }

    public static int indexOf(long[] a, long s) {
        return indexOfs(a, s, 0);
    }

    public static int indexOf(float[] a, float s) {
        return indexOfs(a, s, 0);
    }

    public static int indexOf(double[] a, double s) {
        return indexOfs(a, s, 0);
    }

    public static int indexOf(char[] a, char s) {
        return indexOfs(a, s, 0);
    }

    public static int indexOf(boolean[] a, boolean s) {
        return indexOfs(a, s, 0);
    }

    public static int indexOf(Object[] a, Object s) {
        return indexOfs(a, s, 0);
    }

    private static int indexOfs(Object a, Object s, int start) {
        Object[] arr = ofs(a);
        int length = arr.length;
        if (start < 0) {
            return -1;
        } else if (start >= length) {
            return -1;
        }
        for (int i = start; i < length; i++) {
            if (Objects1.eq(arr[i], s)) {
                return i;
            }
        }
        return -1;
    }

    // ------------------ lastIndexOf ------------------

    /**
     * 查找最后一个查询到的元素的位置
     *
     * @param a     数组
     * @param s     元素
     * @param start 开始向前查找的下标
     * @return 位置
     */
    public static int lastIndexOf(byte[] a, byte s, int start) {
        return lastIndexOfs(a, s, start);
    }

    public static int lastIndexOf(short[] a, short s, int start) {
        return lastIndexOfs(a, s, start);
    }

    public static int lastIndexOf(int[] a, int s, int start) {
        return lastIndexOfs(a, s, start);
    }

    public static int lastIndexOf(long[] a, long s, int start) {
        return lastIndexOfs(a, s, start);
    }

    public static int lastIndexOf(float[] a, float s, int start) {
        return lastIndexOfs(a, s, start);
    }

    public static int lastIndexOf(double[] a, double s, int start) {
        return lastIndexOfs(a, s, start);
    }

    public static int lastIndexOf(char[] a, char s, int start) {
        return lastIndexOfs(a, s, start);
    }

    public static int lastIndexOf(boolean[] a, boolean s, int start) {
        return lastIndexOfs(a, s, start);
    }

    public static int lastIndexOf(Object[] a, Object s, int start) {
        return lastIndexOfs(a, s, start);
    }

    public static int lastIndexOf(byte[] a, byte s) {
        return lastIndexOfs(a, s, a.length - 1);
    }

    public static int lastIndexOf(short[] a, short s) {
        return lastIndexOfs(a, s, a.length - 1);
    }

    public static int lastIndexOf(int[] a, int s) {
        return lastIndexOfs(a, s, a.length - 1);
    }

    public static int lastIndexOf(long[] a, long s) {
        return lastIndexOfs(a, s, a.length - 1);
    }

    public static int lastIndexOf(float[] a, float s) {
        return lastIndexOfs(a, s, a.length - 1);
    }

    public static int lastIndexOf(double[] a, double s) {
        return lastIndexOfs(a, s, a.length - 1);
    }

    public static int lastIndexOf(char[] a, char s) {
        return lastIndexOfs(a, s, a.length - 1);
    }

    public static int lastIndexOf(boolean[] a, boolean s) {
        return lastIndexOfs(a, s, a.length - 1);
    }

    public static int lastIndexOf(Object[] a, Object s) {
        return lastIndexOfs(a, s, a.length - 1);
    }

    private static int lastIndexOfs(Object a, Object s, int start) {
        Object[] arr = ofs(a);
        int length = arr.length;
        if (start <= 0) {
            return -1;
        } else if (start >= length) {
            return -1;
        }
        for (int i = start; i >= 0; i--) {
            if (Objects1.eq(arr[i], s)) {
                return i;
            }
        }
        return -1;
    }

    // ------------------ contains ------------------

    /**
     * 判断数组是否包含某个元素
     *
     * @param a 数组
     * @param s 元素
     * @return true包含
     */
    public static boolean contains(byte[] a, byte s) {
        return indexOf(a, s) != -1;
    }

    public static boolean contains(short[] a, short s) {
        return indexOf(a, s) != -1;
    }

    public static boolean contains(int[] a, int s) {
        return indexOf(a, s) != -1;
    }

    public static boolean contains(long[] a, long s) {
        return indexOf(a, s) != -1;
    }

    public static boolean contains(float[] a, float s) {
        return indexOf(a, s) != -1;
    }

    public static boolean contains(double[] a, double s) {
        return indexOf(a, s) != -1;
    }

    public static boolean contains(char[] a, char s) {
        return indexOf(a, s) != -1;
    }

    public static boolean contains(boolean[] a, boolean s) {
        return indexOf(a, s) != -1;
    }

    public static boolean contains(Object[] a, Object s) {
        return indexOf(a, s) != -1;
    }

    // ------------------ count ------------------

    /**
     * 查询目标元素在数组中出现的次数
     *
     * @param target 目标元素
     * @param store  数组, 可以传基本类型的元素, 但是不可以传基本类型的数组
     * @return 次数
     */
    public static <T> int count(T target, T... store) {
        if (length(store) == 0) {
            return 0;
        }
        int count = 0;
        for (T i : store) {
            if (Objects1.eq(i, target)) {
                count++;
            }
        }
        return count;
    }

    public static int count(byte target, byte... store) {
        if (length(store) == 0) {
            return 0;
        }
        int count = 0;
        for (byte i : store) {
            if (i == target) {
                count++;
            }
        }
        return count;
    }

    public static int count(short target, short... store) {
        if (length(store) == 0) {
            return 0;
        }
        int count = 0;
        for (short i : store) {
            if (i == target) {
                count++;
            }
        }
        return count;
    }

    public static int count(int target, int... store) {
        if (length(store) == 0) {
            return 0;
        }
        int count = 0;
        for (int i : store) {
            if (i == target) {
                count++;
            }
        }
        return count;
    }

    public static int count(long target, long... store) {
        if (length(store) == 0) {
            return 0;
        }
        int count = 0;
        for (long i : store) {
            if (i == target) {
                count++;
            }
        }
        return count;
    }

    public static int count(float target, float... store) {
        if (length(store) == 0) {
            return 0;
        }
        int count = 0;
        for (float i : store) {
            if (i == target) {
                count++;
            }
        }
        return count;
    }

    public static int count(double target, double... store) {
        if (length(store) == 0) {
            return 0;
        }
        int count = 0;
        for (double i : store) {
            if (i == target) {
                count++;
            }
        }
        return count;
    }

    public static int count(char target, char... store) {
        if (length(store) == 0) {
            return 0;
        }
        int count = 0;
        for (char i : store) {
            if (i == target) {
                count++;
            }
        }
        return count;
    }

    public static int count(boolean target, boolean... store) {
        if (length(store) == 0) {
            return 0;
        }
        int count = 0;
        for (boolean i : store) {
            if (i == target) {
                count++;
            }
        }
        return count;
    }

    // ------------------ some ------------------

    /**
     * 查询目标元素是否出现在数组中
     *
     * @param target 目标元素
     * @param store  数组, 可以传基本类型的元素, 但是不可以传基本类型的数组
     * @return true 出现
     */
    public static <T> boolean some(T target, T... store) {
        if (length(store) == 0) {
            return false;
        }
        for (T i : store) {
            if (i == target) {
                return true;
            }
        }
        return false;
    }

    public static boolean some(byte target, byte... store) {
        if (length(store) == 0) {
            return false;
        }
        for (byte i : store) {
            if (i == target) {
                return true;
            }
        }
        return false;
    }

    public static boolean some(short target, short... store) {
        if (length(store) == 0) {
            return false;
        }
        for (short i : store) {
            if (i == target) {
                return true;
            }
        }
        return false;
    }

    public static boolean some(int target, int... store) {
        if (length(store) == 0) {
            return false;
        }
        for (int i : store) {
            if (i == target) {
                return true;
            }
        }
        return false;
    }

    public static boolean some(long target, long... store) {
        if (length(store) == 0) {
            return false;
        }
        for (long i : store) {
            if (i == target) {
                return true;
            }
        }
        return false;
    }

    public static boolean some(float target, float... store) {
        if (length(store) == 0) {
            return false;
        }
        for (float i : store) {
            if (i == target) {
                return true;
            }
        }
        return false;
    }

    public static boolean some(double target, double... store) {
        if (length(store) == 0) {
            return false;
        }
        for (double i : store) {
            if (i == target) {
                return true;
            }
        }
        return false;
    }

    public static boolean some(char target, char... store) {
        if (length(store) == 0) {
            return false;
        }
        for (char i : store) {
            if (i == target) {
                return true;
            }
        }
        return false;
    }

    public static boolean some(boolean target, boolean... store) {
        if (length(store) == 0) {
            return false;
        }
        for (boolean i : store) {
            if (i == target) {
                return true;
            }
        }
        return false;
    }

    // ------------------ reverse ------------------

    /**
     * 数组倒排
     *
     * @param a 数组
     */
    public static void reverse(byte[] a) {
        int length = length(a);
        if (length == 0) {
            return;
        }
        for (int i = 0, mid = length / BINARY; i < mid; i++) {
            int len = length - i - 1;
            if (a[i] != a[len]) {
                swap(a, i, len);
            }
        }
    }

    public static void reverse(short[] a) {
        int length = length(a);
        if (length == 0) {
            return;
        }
        for (int i = 0, mid = length / BINARY; i < mid; i++) {
            int len = length - i - 1;
            if (a[i] != a[len]) {
                swap(a, i, len);
            }
        }
    }

    public static void reverse(int[] a) {
        int length = length(a);
        if (length == 0) {
            return;
        }
        for (int i = 0, mid = length / BINARY; i < mid; i++) {
            int len = length - i - 1;
            if (a[i] != a[len]) {
                swap(a, i, len);
            }
        }
    }

    public static void reverse(long[] a) {
        int length = length(a);
        if (length == 0) {
            return;
        }
        for (int i = 0, mid = length / BINARY; i < mid; i++) {
            int len = length - i - 1;
            if (a[i] != a[len]) {
                swap(a, i, len);
            }
        }
    }

    public static void reverse(float[] a) {
        int length = length(a);
        if (length == 0) {
            return;
        }
        for (int i = 0, mid = length / BINARY; i < mid; i++) {
            int len = length - i - 1;
            if (a[i] != a[len]) {
                swap(a, i, len);
            }
        }
    }

    public static void reverse(double[] a) {
        int length = length(a);
        if (length == 0) {
            return;
        }
        for (int i = 0, mid = length / BINARY; i < mid; i++) {
            int len = length - i - 1;
            if (a[i] != a[len]) {
                swap(a, i, len);
            }
        }
    }

    public static void reverse(char[] a) {
        int length = length(a);
        if (length == 0) {
            return;
        }
        for (int i = 0, mid = length / BINARY; i < mid; i++) {
            int len = length - i - 1;
            if (a[i] != a[len]) {
                swap(a, i, len);
            }
        }
    }

    public static void reverse(boolean[] a) {
        int length = length(a);
        if (length == 0) {
            return;
        }
        for (int i = 0, mid = length / BINARY; i < mid; i++) {
            int len = length - i - 1;
            if (a[i] != a[len]) {
                swap(a, i, len);
            }
        }
    }

    public static <T> void reverse(T[] a) {
        int length = length(a);
        if (length == 0) {
            return;
        }
        for (int i = 0, mid = length / BINARY; i < mid; i++) {
            int len = length - i - 1;
            if (a[i] != a[len]) {
                swap(a, i, len);
            }
        }
    }

    // ------------------ first ------------------

    /**
     * 获取数组第一个元素
     *
     * @param array array
     * @param def   默认值
     * @return 第一个元素
     */
    public static <T> T first(T[] array, T def) {
        int length = length(array);
        if (length == 0) {
            return def;
        }
        return array[0];
    }

    public static byte first(byte[] array, byte def) {
        int length = length(array);
        if (length == 0) {
            return def;
        }
        return array[0];
    }

    public static short first(short[] array, short def) {
        int length = length(array);
        if (length == 0) {
            return def;
        }
        return array[0];
    }

    public static int first(int[] array, int def) {
        int length = length(array);
        if (length == 0) {
            return def;
        }
        return array[0];
    }

    public static long first(long[] array, long def) {
        int length = length(array);
        if (length == 0) {
            return def;
        }
        return array[0];
    }

    public static float first(float[] array, float def) {
        int length = length(array);
        if (length == 0) {
            return def;
        }
        return array[0];
    }

    public static double first(double[] array, double def) {
        int length = length(array);
        if (length == 0) {
            return def;
        }
        return array[0];
    }

    public static boolean first(boolean[] array, boolean def) {
        int length = length(array);
        if (length == 0) {
            return def;
        }
        return array[0];
    }

    public static char first(char[] array, char def) {
        int length = length(array);
        if (length == 0) {
            return def;
        }
        return array[0];
    }

    // ------------------ last ------------------

    /**
     * 获取数组最后一个元素
     *
     * @param array array
     * @param def   默认值
     * @return 第一个元素
     */
    public static <T> T last(T[] array, T def) {
        int length = length(array);
        if (length == 0) {
            return def;
        }
        return array[length - 1];
    }

    public static byte last(byte[] array, byte def) {
        int length = length(array);
        if (length == 0) {
            return def;
        }
        return array[length - 1];
    }

    public static short last(short[] array, short def) {
        int length = length(array);
        if (length == 0) {
            return def;
        }
        return array[length - 1];
    }

    public static int last(int[] array, int def) {
        int length = length(array);
        if (length == 0) {
            return def;
        }
        return array[length - 1];
    }

    public static long last(long[] array, long def) {
        int length = length(array);
        if (length == 0) {
            return def;
        }
        return array[length - 1];
    }

    public static float last(float[] array, float def) {
        int length = length(array);
        if (length == 0) {
            return def;
        }
        return array[length - 1];
    }

    public static double last(double[] array, double def) {
        int length = length(array);
        if (length == 0) {
            return def;
        }
        return array[length - 1];
    }

    public static boolean last(boolean[] array, boolean def) {
        int length = length(array);
        if (length == 0) {
            return def;
        }
        return array[length - 1];
    }

    public static char last(char[] array, char def) {
        int length = length(array);
        if (length == 0) {
            return def;
        }
        return array[length - 1];
    }

    // ------------------ swap ------------------

    /**
     * 换位
     *
     * @param a 数组
     * @param i 换位的元素1
     * @param j 换位的元素2
     */
    public static void onChangeSwap(byte[] a, int i, int j) {
        a[i] ^= a[j];
        a[j] ^= a[i];
        a[i] ^= a[j];
    }

    public static void swap(byte[] a, int i, int j) {
        byte temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static void swap(short[] a, int i, int j) {
        short temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static void swap(long[] a, int i, int j) {
        long temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static void swap(float[] a, int i, int j) {
        float temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static void swap(double[] a, int i, int j) {
        double temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static void swap(char[] a, int i, int j) {
        char temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static void swap(boolean[] a, int i, int j) {
        boolean temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static void swap(Object[] a, int i, int j) {
        Object temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // ------------------ max ------------------

    /**
     * 最大值
     *
     * @param bs 数组
     * @return 最大值
     */
    public static byte max(byte[] bs) {
        int len = length(bs);
        if (len == 0) {
            return 0;
        }
        byte max = bs[0];
        for (int i = 1; i < len; i++) {
            if (max < bs[i]) {
                max = bs[i];
            }
        }
        return max;
    }

    public static short max(short[] bs) {
        int len = length(bs);
        if (len == 0) {
            return 0;
        }
        short max = bs[0];
        for (int i = 1; i < len; i++) {
            if (max < bs[i]) {
                max = bs[i];
            }
        }
        return max;
    }

    public static int max(int[] bs) {
        int len = length(bs);
        if (len == 0) {
            return 0;
        }
        int max = bs[0];
        for (int i = 1; i < len; i++) {
            if (max < bs[i]) {
                max = bs[i];
            }
        }
        return max;
    }

    public static long max(long[] bs) {
        int len = length(bs);
        if (len == 0) {
            return 0;
        }
        long max = bs[0];
        for (int i = 1; i < len; i++) {
            if (max < bs[i]) {
                max = bs[i];
            }
        }
        return max;
    }

    public static float max(float[] bs) {
        int len = length(bs);
        if (len == 0) {
            return 0;
        }
        float max = bs[0];
        for (int i = 1; i < len; i++) {
            if (max < bs[i]) {
                max = bs[i];
            }
        }
        return max;
    }

    public static double max(double[] bs) {
        int len = length(bs);
        if (len == 0) {
            return 0;
        }
        double max = bs[0];
        for (int i = 1; i < len; i++) {
            if (max < bs[i]) {
                max = bs[i];
            }
        }
        return max;
    }

    public static char max(char[] bs) {
        int len = length(bs);
        if (len == 0) {
            return 0;
        }
        char max = bs[0];
        for (int i = 1; i < len; i++) {
            if (max < bs[i]) {
                max = bs[i];
            }
        }
        return max;
    }

    public static <T extends Comparable> T max(T[] bs) {
        int len = length(bs);
        if (len == 0) {
            return null;
        }
        T max = bs[0];
        int offset = 1;
        if (max == null) {
            for (int blen = bs.length; offset < blen; offset++) {
                T bi = bs[offset];
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
            if (bs[i] != null && max.compareTo(bs[i]) < 0) {
                max = bs[i];
            }
        }
        return max;
    }

    public static <T> T max(T[] bs, Comparator c) {
        int len = length(bs);
        if (len == 0) {
            return null;
        }
        T max = bs[0];
        int offset = 1;
        if (max == null) {
            for (int blen = bs.length; offset < blen; offset++) {
                T bi = bs[offset];
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
            if (bs[i] != null && c.compare(max, bs[i]) < 0) {
                max = bs[i];
            }
        }
        return max;
    }

    // ------------------ min ------------------

    /**
     * 最小值
     *
     * @param bs 数组
     * @return 最小值
     */
    public static byte min(byte[] bs) {
        int len = length(bs);
        if (len == 0) {
            return 0;
        }
        byte min = bs[0];
        for (int i = 1; i < len; i++) {
            if (min > bs[i]) {
                min = bs[i];
            }
        }
        return min;
    }

    public static short min(short[] bs) {
        int len = length(bs);
        if (len == 0) {
            return 0;
        }
        short min = bs[0];
        for (int i = 1; i < len; i++) {
            if (min > bs[i]) {
                min = bs[i];
            }
        }
        return min;
    }

    public static int min(int[] bs) {
        int len = length(bs);
        if (len == 0) {
            return 0;
        }
        int min = bs[0];
        for (int i = 1; i < len; i++) {
            if (min > bs[i]) {
                min = bs[i];
            }
        }
        return min;
    }

    public static long min(long[] bs) {
        int len = length(bs);
        if (len == 0) {
            return 0;
        }
        long min = bs[0];
        for (int i = 1; i < len; i++) {
            if (min > bs[i]) {
                min = bs[i];
            }
        }
        return min;
    }

    public static float min(float[] bs) {
        int len = length(bs);
        if (len == 0) {
            return 0;
        }
        float min = bs[0];
        for (int i = 1; i < len; i++) {
            if (min > bs[i]) {
                min = bs[i];
            }
        }
        return min;
    }

    public static double min(double[] bs) {
        int len = length(bs);
        if (len == 0) {
            return 0;
        }
        double min = bs[0];
        for (int i = 1; i < len; i++) {
            if (min > bs[i]) {
                min = bs[i];
            }
        }
        return min;
    }

    public static char min(char[] bs) {
        int len = length(bs);
        if (len == 0) {
            return 0;
        }
        char min = bs[0];
        for (int i = 1; i < len; i++) {
            if (min > bs[i]) {
                min = bs[i];
            }
        }
        return min;
    }

    public static <T extends Comparable> T min(T[] bs) {
        int len = length(bs);
        if (len == 0) {
            return null;
        }
        T min = bs[0];
        int offset = 1;
        if (min == null) {
            for (int blen = bs.length; offset < blen; offset++) {
                T bi = bs[offset];
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
            if (bs[i] != null && min.compareTo(bs[i]) > 0) {
                min = bs[i];
            }
        }
        return min;
    }

    public static <T> T min(T[] bs, Comparator c) {
        int len = length(bs);
        if (len == 0) {
            return null;
        }
        T min = bs[0];
        int offset = 1;
        if (min == null) {
            for (int blen = bs.length; offset < blen; offset++) {
                T bi = bs[offset];
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
            if (bs[i] != null && c.compare(min, bs[i]) > 0) {
                min = bs[i];
            }
        }
        return min;
    }

    // ------------------ hashcode ------------------

    /**
     * 获取数组的hashCode
     *
     * @param array array
     * @return hashCode
     */
    public static int hashCode(Object[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (Object element : array) {
            hash = MULTIPLIER * hash + Objects1.hashCode(element);
        }
        return hash;
    }

    public static int hashCode(byte[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (byte element : array) {
            hash = MULTIPLIER * hash + element;
        }
        return hash;
    }

    public static int hashCode(short[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (short element : array) {
            hash = MULTIPLIER * hash + element;
        }
        return hash;
    }

    public static int hashCode(int[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (int element : array) {
            hash = MULTIPLIER * hash + element;
        }
        return hash;
    }

    public static int hashCode(long[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (long element : array) {
            hash = MULTIPLIER * hash + Long.hashCode(element);
        }
        return hash;
    }

    public static int hashCode(float[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (float element : array) {
            hash = MULTIPLIER * hash + Float.hashCode(element);
        }
        return hash;
    }

    public static int hashCode(double[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (double element : array) {
            hash = MULTIPLIER * hash + Double.hashCode(element);
        }
        return hash;
    }

    public static int hashCode(boolean[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (boolean element : array) {
            hash = MULTIPLIER * hash + Boolean.hashCode(element);
        }
        return hash;
    }

    public static int hashCode(char[] array) {
        if (array == null) {
            return 0;
        }
        int hash = INITIAL_HASH;
        for (char element : array) {
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
