package com.orion.utils;

import com.orion.function.Conversion;
import com.orion.lang.MapEntry;
import com.orion.lang.collect.MultiHashMap;
import com.orion.utils.math.BigIntegers;
import com.orion.utils.math.BigDecimals;
import com.orion.utils.reflect.Classes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * 转化对象类型
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2019/12/31 10:09
 */
@SuppressWarnings("ALL")
public class Converts {

    private Converts() {
    }

    private static final MultiHashMap<Class<?>, Class<?>, Conversion> CONVERT_MULTI_MAP = MultiHashMap.newMultiHashMap(true);

    private static final ConcurrentHashMap<MapEntry<Class<?>, Class<?>>, Boolean> IMPL_MAP = new ConcurrentHashMap<>();

    private static boolean loadByte, loadShort, loadInt, loadLong, loadFloat, loadDouble, loadBoolean, loadChar, loadString;

    private static final Conversion TO_BYTE = Converts::toByte, TO_SHORT = Converts::toShort, TO_INT = Converts::toInt, TO_LONG = Converts::toLong,
            TO_FLOAT = Converts::toFloat, TO_DOUBLE = Converts::toDouble, TO_BOOLEAN = Converts::toBoolean, TO_CHAR = Converts::toChar,
            TO_BIG_DECIMAL = BigDecimals::toBigDecimal, TO_BIG_INTEGER = BigIntegers::toBigInteger,
            TO_DATE = Dates::date, TO_LOCAL_DATE_TIME = Dates::localDateTime, TO_LOCAL_DATE = Dates::localDate, TO_STRING = Converts::toString;

    // -------------------- convert --------------------

    /**
     * 将原对象转为目标对象
     * 默认只提供基本类型转化
     *
     * @param t   原对象
     * @param rc  目标对象class
     * @param <T> ignore
     * @param <R> ignore
     * @return R
     */
    public static <T, R> R convert(T t, Class<R> rc) {
        if (t == null || rc == null) {
            return null;
        }
        Class<?> tc = t.getClass();
        if (rc.equals(Object.class) || rc.equals(tc)) {
            return (R) t;
        }
        boolean check = false;
        if (Classes.isBaseClass(rc)) {
            check = true;
            rc = Classes.getWrapClass(rc);
        }
        if (Classes.isBaseClass(tc)) {
            check = true;
            tc = Classes.getWrapClass(tc);
        }
        if (check && rc.equals(tc)) {
            return (R) t;
        }
        if (checkImpl(rc, tc)) {
            return (R) t;
        }
        Map<Class<?>, Conversion> conversionMap = getConversion(tc);
        if (conversionMap == null) {
            throw Exceptions.convert(Strings.format("Unable to convert source [{}] class to target [{}] class", tc, rc));
        }
        Conversion conversion = conversionMap.get(rc);
        if (conversion == null) {
            throw Exceptions.convert(Strings.format("Unable to convert source [{}] class to target [{}] class", tc, rc));
        }
        Object apply = conversion.apply(t);
        return apply == null ? null : (R) apply;
    }

    /**
     * 检查target是否为require的实现类
     *
     * @param require ignore
     * @param target  ignore
     * @return ignore
     */
    private static boolean checkImpl(Class<?> require, Class<?> target) {
        MapEntry<Class<?>, Class<?>> e = new MapEntry<>(require, target);
        Boolean b = IMPL_MAP.get(e);
        if (b == null) {
            boolean i = Classes.isImplClass(require, target);
            IMPL_MAP.put(e, i);
            return i;
        } else {
            return b;
        }
    }

    /**
     * 转化
     *
     * @param i   原始值
     * @param f   函数
     * @param <I> ignore
     * @param <O> ignore
     * @return 新值
     */
    public static <I, O> O convert(I i, Function<I, O> f) {
        return f.apply(i);
    }

    /**
     * 转化
     *
     * @param i   原始值
     * @param f   函数
     * @param <I> ignore
     * @param <O> ignore
     * @return 新值
     */
    public static <I, O> O[] converts(I[] i, Function<I[], O[]> f) {
        return f.apply(i);
    }

    /**
     * 转化
     *
     * @param f         函数
     * @param generator e.g. Integer[]::new
     * @param is        原始值
     * @param <I>       ignore
     * @param <O>       ignore
     * @return 新值
     */
    public static <I, O> O[] converts(Function<I, O> f, IntFunction<O[]> generator, I[] is) {
        int len = Arrays1.length(is);
        O[] os = generator.apply(len);
        for (int i = 0; i < len; i++) {
            os[i] = f.apply(is[i]);
        }
        return os;
    }

    // -------------------- Conversion lazy load --------------------

    /**
     * 添加一个类型转换器
     *
     * @param tc  原对象 class
     * @param rc  目标对象 class
     * @param c   Conversion
     * @param <T> ignore
     * @param <R> ignore
     */
    private static <T, R> void addConversion(Class<T> tc, Class<R> rc, Conversion<T, R> c) {
        CONVERT_MULTI_MAP.put(tc, rc, c);
    }

    private static Map<Class<?>, Conversion> getConversion(Class<?> tc) {
        if (tc.equals(Byte.class) && !loadByte) {
            lazyLoadByte();
        } else if (tc.equals(Short.class) && !loadShort) {
            lazyLoadShort();
        } else if (tc.equals(Integer.class) && !loadInt) {
            lazyLoadInt();
        } else if (tc.equals(Long.class) && !loadLong) {
            lazyLoadLong();
        } else if (tc.equals(Float.class) && !loadFloat) {
            lazyLoadFloat();
        } else if (tc.equals(Double.class) && !loadDouble) {
            lazyLoadDouble();
        } else if (tc.equals(Boolean.class) && !loadBoolean) {
            lazyLoadBoolean();
        } else if (tc.equals(Character.class) && !loadChar) {
            lazyLoadChar();
        } else if (tc.equals(String.class) && !loadString) {
            lazyLoadString();
        }
        return CONVERT_MULTI_MAP.get(tc);
    }

    private static void lazyLoadByte() {
        loadByte = true;
        CONVERT_MULTI_MAP.put(Byte.class, Short.class, TO_SHORT);
        CONVERT_MULTI_MAP.put(Byte.class, Integer.class, TO_INT);
        CONVERT_MULTI_MAP.put(Byte.class, Long.class, TO_LONG);
        CONVERT_MULTI_MAP.put(Byte.class, Float.class, TO_FLOAT);
        CONVERT_MULTI_MAP.put(Byte.class, Double.class, TO_DOUBLE);
        CONVERT_MULTI_MAP.put(Byte.class, Boolean.class, TO_BOOLEAN);
        CONVERT_MULTI_MAP.put(Byte.class, Character.class, TO_CHAR);
        CONVERT_MULTI_MAP.put(Byte.class, BigDecimal.class, TO_BIG_DECIMAL);
        CONVERT_MULTI_MAP.put(Byte.class, BigInteger.class, TO_BIG_INTEGER);
        CONVERT_MULTI_MAP.put(Byte.class, String.class, TO_STRING);
    }

    private static void lazyLoadShort() {
        loadShort = true;
        CONVERT_MULTI_MAP.put(Short.class, Byte.class, TO_BYTE);
        CONVERT_MULTI_MAP.put(Short.class, Integer.class, TO_INT);
        CONVERT_MULTI_MAP.put(Short.class, Long.class, TO_LONG);
        CONVERT_MULTI_MAP.put(Short.class, Float.class, TO_FLOAT);
        CONVERT_MULTI_MAP.put(Short.class, Double.class, TO_DOUBLE);
        CONVERT_MULTI_MAP.put(Short.class, Boolean.class, TO_BOOLEAN);
        CONVERT_MULTI_MAP.put(Short.class, Character.class, TO_CHAR);
        CONVERT_MULTI_MAP.put(Short.class, BigDecimal.class, TO_BIG_DECIMAL);
        CONVERT_MULTI_MAP.put(Short.class, BigInteger.class, TO_BIG_INTEGER);
        CONVERT_MULTI_MAP.put(Short.class, String.class, TO_STRING);
    }

    private static void lazyLoadInt() {
        loadInt = true;
        CONVERT_MULTI_MAP.put(Integer.class, Byte.class, TO_BYTE);
        CONVERT_MULTI_MAP.put(Integer.class, Short.class, TO_SHORT);
        CONVERT_MULTI_MAP.put(Integer.class, Long.class, TO_LONG);
        CONVERT_MULTI_MAP.put(Integer.class, Float.class, TO_FLOAT);
        CONVERT_MULTI_MAP.put(Integer.class, Double.class, TO_DOUBLE);
        CONVERT_MULTI_MAP.put(Integer.class, Boolean.class, TO_BOOLEAN);
        CONVERT_MULTI_MAP.put(Integer.class, Character.class, TO_CHAR);
        CONVERT_MULTI_MAP.put(Integer.class, BigDecimal.class, TO_BIG_DECIMAL);
        CONVERT_MULTI_MAP.put(Integer.class, BigInteger.class, TO_BIG_INTEGER);
        CONVERT_MULTI_MAP.put(Integer.class, String.class, TO_STRING);
    }

    private static void lazyLoadLong() {
        loadLong = true;
        CONVERT_MULTI_MAP.put(Long.class, Byte.class, TO_BYTE);
        CONVERT_MULTI_MAP.put(Long.class, Short.class, TO_SHORT);
        CONVERT_MULTI_MAP.put(Long.class, Integer.class, TO_INT);
        CONVERT_MULTI_MAP.put(Long.class, Float.class, TO_FLOAT);
        CONVERT_MULTI_MAP.put(Long.class, Double.class, TO_DOUBLE);
        CONVERT_MULTI_MAP.put(Long.class, Boolean.class, TO_BOOLEAN);
        CONVERT_MULTI_MAP.put(Long.class, Character.class, TO_CHAR);
        CONVERT_MULTI_MAP.put(Long.class, BigDecimal.class, TO_BIG_DECIMAL);
        CONVERT_MULTI_MAP.put(Long.class, BigInteger.class, TO_BIG_INTEGER);
        CONVERT_MULTI_MAP.put(Long.class, String.class, TO_STRING);
        CONVERT_MULTI_MAP.put(Long.class, Date.class, TO_DATE);
        CONVERT_MULTI_MAP.put(Long.class, LocalDateTime.class, TO_LOCAL_DATE_TIME);
        CONVERT_MULTI_MAP.put(Long.class, LocalDate.class, TO_LOCAL_DATE);
    }

    private static void lazyLoadFloat() {
        loadFloat = true;
        CONVERT_MULTI_MAP.put(Float.class, Byte.class, TO_BYTE);
        CONVERT_MULTI_MAP.put(Float.class, Short.class, TO_SHORT);
        CONVERT_MULTI_MAP.put(Float.class, Integer.class, TO_INT);
        CONVERT_MULTI_MAP.put(Float.class, Long.class, TO_LONG);
        CONVERT_MULTI_MAP.put(Float.class, Double.class, TO_DOUBLE);
        CONVERT_MULTI_MAP.put(Float.class, Boolean.class, TO_BOOLEAN);
        CONVERT_MULTI_MAP.put(Float.class, Character.class, TO_CHAR);
        CONVERT_MULTI_MAP.put(Float.class, BigDecimal.class, TO_BIG_DECIMAL);
        CONVERT_MULTI_MAP.put(Float.class, BigInteger.class, TO_BIG_INTEGER);
        CONVERT_MULTI_MAP.put(Float.class, String.class, TO_STRING);
    }

    private static void lazyLoadDouble() {
        loadDouble = true;
        CONVERT_MULTI_MAP.put(Double.class, Byte.class, TO_BYTE);
        CONVERT_MULTI_MAP.put(Double.class, Short.class, TO_SHORT);
        CONVERT_MULTI_MAP.put(Double.class, Integer.class, TO_INT);
        CONVERT_MULTI_MAP.put(Double.class, Long.class, TO_LONG);
        CONVERT_MULTI_MAP.put(Double.class, Float.class, TO_FLOAT);
        CONVERT_MULTI_MAP.put(Double.class, Boolean.class, TO_BOOLEAN);
        CONVERT_MULTI_MAP.put(Double.class, Character.class, TO_CHAR);
        CONVERT_MULTI_MAP.put(Double.class, BigDecimal.class, TO_BIG_DECIMAL);
        CONVERT_MULTI_MAP.put(Double.class, BigInteger.class, TO_BIG_INTEGER);
        CONVERT_MULTI_MAP.put(Double.class, String.class, TO_STRING);
    }

    private static void lazyLoadBoolean() {
        loadBoolean = true;
        CONVERT_MULTI_MAP.put(Boolean.class, Byte.class, TO_BYTE);
        CONVERT_MULTI_MAP.put(Boolean.class, Short.class, TO_SHORT);
        CONVERT_MULTI_MAP.put(Boolean.class, Integer.class, TO_INT);
        CONVERT_MULTI_MAP.put(Boolean.class, Long.class, TO_LONG);
        CONVERT_MULTI_MAP.put(Boolean.class, Float.class, TO_FLOAT);
        CONVERT_MULTI_MAP.put(Boolean.class, Double.class, TO_DOUBLE);
        CONVERT_MULTI_MAP.put(Boolean.class, Character.class, TO_CHAR);
        CONVERT_MULTI_MAP.put(Boolean.class, BigDecimal.class, TO_BIG_DECIMAL);
        CONVERT_MULTI_MAP.put(Boolean.class, BigInteger.class, TO_BIG_INTEGER);
        CONVERT_MULTI_MAP.put(Boolean.class, String.class, TO_STRING);
    }

    private static void lazyLoadChar() {
        loadChar = true;
        CONVERT_MULTI_MAP.put(Character.class, Byte.class, TO_BYTE);
        CONVERT_MULTI_MAP.put(Character.class, Short.class, TO_SHORT);
        CONVERT_MULTI_MAP.put(Character.class, Integer.class, TO_INT);
        CONVERT_MULTI_MAP.put(Character.class, Long.class, TO_LONG);
        CONVERT_MULTI_MAP.put(Character.class, Float.class, TO_FLOAT);
        CONVERT_MULTI_MAP.put(Character.class, Double.class, TO_DOUBLE);
        CONVERT_MULTI_MAP.put(Character.class, Boolean.class, TO_BOOLEAN);
        CONVERT_MULTI_MAP.put(Character.class, BigDecimal.class, TO_BIG_DECIMAL);
        CONVERT_MULTI_MAP.put(Character.class, BigInteger.class, TO_BIG_INTEGER);
        CONVERT_MULTI_MAP.put(Character.class, String.class, TO_STRING);
    }

    private static void lazyLoadString() {
        loadString = true;
        CONVERT_MULTI_MAP.put(String.class, Byte.class, TO_BYTE);
        CONVERT_MULTI_MAP.put(String.class, Short.class, TO_SHORT);
        CONVERT_MULTI_MAP.put(String.class, Integer.class, TO_INT);
        CONVERT_MULTI_MAP.put(String.class, Long.class, TO_LONG);
        CONVERT_MULTI_MAP.put(String.class, Float.class, TO_FLOAT);
        CONVERT_MULTI_MAP.put(String.class, Double.class, TO_DOUBLE);
        CONVERT_MULTI_MAP.put(String.class, Boolean.class, TO_BOOLEAN);
        CONVERT_MULTI_MAP.put(String.class, Character.class, TO_CHAR);
        CONVERT_MULTI_MAP.put(String.class, BigDecimal.class, TO_BIG_DECIMAL);
        CONVERT_MULTI_MAP.put(String.class, BigInteger.class, TO_BIG_INTEGER);
        CONVERT_MULTI_MAP.put(String.class, Date.class, TO_DATE);
        CONVERT_MULTI_MAP.put(String.class, LocalDateTime.class, TO_LOCAL_DATE_TIME);
        CONVERT_MULTI_MAP.put(String.class, LocalDate.class, TO_LOCAL_DATE);
    }

    // -------------------- toString --------------------

    public static String toString(Object o) {
        return Objects1.toString(o);
    }

    // -------------------- toDate --------------------

    public static Date toDate(Object o) {
        return Dates.date(o);
    }

    public static LocalDate toLocalDate(Object o) {
        return Dates.localDate(o);
    }

    public static LocalDateTime toLocalDateTime(Object o) {
        return Dates.localDateTime(o);
    }

    // -------------------- toByte --------------------

    public static byte toByte(Byte b) {
        return b;
    }

    public static byte toByte(short b) {
        return (byte) b;
    }

    public static byte toByte(int b) {
        return (byte) b;
    }

    public static byte toByte(long b) {
        return (byte) b;
    }

    public static byte toByte(float b) {
        return (byte) b;
    }

    public static byte toByte(double b) {
        return (byte) b;
    }

    public static byte toByte(boolean b) {
        return (byte) (b ? 1 : 0);
    }

    public static byte toByte(char b) {
        return (byte) b;
    }

    public static byte toByte(String b) {
        return Byte.valueOf(b);
    }

    public static byte toByte(BigDecimal b) {
        return (byte) b.doubleValue();
    }

    public static byte toByte(BigInteger b) {
        return (byte) b.longValue();
    }

    public static byte toByte(Object o) {
        Valid.notNull(o);
        if (o instanceof Byte) {
            return (byte) o;
        } else if (o instanceof Short) {
            return (byte) ((short) o);
        } else if (o instanceof Integer) {
            return (byte) ((int) o);
        } else if (o instanceof Long) {
            return (byte) ((long) o);
        } else if (o instanceof Float) {
            return (byte) ((float) o);
        } else if (o instanceof Double) {
            return (byte) ((double) o);
        } else if (o instanceof Boolean) {
            return (byte) ((boolean) o ? 1 : 0);
        } else if (o instanceof Character) {
            return (byte) ((char) o);
        } else if (o instanceof String) {
            return Byte.valueOf((String) o);
        } else if (o instanceof BigDecimal) {
            return (byte) ((BigDecimal) o).doubleValue();
        } else if (o instanceof BigInteger) {
            return (byte) ((BigInteger) o).longValue();
        }
        throw Exceptions.argument();
    }

    // -------------------- toShort --------------------

    public static short toShort(byte[] b) {
        if (b.length == 2) {
            short s0 = (short) (b[0] & 0xff);
            short s1 = (short) (b[1] & 0xff);
            s1 <<= 8;
            return (short) (s0 | s1);
        } else if (b.length == 4) {
            return (short) toInt(b);
        } else if (b.length == 8) {
            return (short) toLong(b);
        }
        throw Exceptions.argument("array length must 2 or 4 or 8");

    }

    public static short toShort(byte b) {
        return (short) b;
    }

    public static short toShort(Short b) {
        return b;
    }

    public static short toShort(int b) {
        return (short) b;
    }

    public static short toShort(long b) {
        return (short) b;
    }

    public static short toShort(float b) {
        return (short) b;
    }

    public static short toShort(double b) {
        return (short) b;
    }

    public static short toShort(boolean b) {
        return (short) (b ? 1 : 0);
    }

    public static short toShort(char b) {
        return (short) b;
    }

    public static short toShort(String b) {
        return Short.valueOf(b);
    }

    public static short toShort(BigDecimal b) {
        return (short) b.doubleValue();
    }

    public static short toShort(BigInteger b) {
        return (short) b.longValue();
    }

    public static short toShort(Object o) {
        Valid.notNull(o);
        if (o instanceof Byte) {
            return (short) ((byte) o);
        } else if (o instanceof byte[]) {
            return toShort((byte[]) o);
        } else if (o instanceof Byte[]) {
            return toShort(Arrays1.drap((Byte[]) o));
        } else if (o instanceof Short) {
            return (short) o;
        } else if (o instanceof Integer) {
            return (short) ((int) o);
        } else if (o instanceof Long) {
            return (short) ((long) o);
        } else if (o instanceof Float) {
            return (short) ((float) o);
        } else if (o instanceof Double) {
            return (short) ((double) o);
        } else if (o instanceof Boolean) {
            return (short) ((boolean) o ? 1 : 0);
        } else if (o instanceof Character) {
            return (short) ((char) o);
        } else if (o instanceof String) {
            return Short.valueOf((String) o);
        } else if (o instanceof BigDecimal) {
            return (short) ((BigDecimal) o).doubleValue();
        } else if (o instanceof BigInteger) {
            return (short) ((BigInteger) o).longValue();
        }
        return 0;
    }

    // -------------------- toInt --------------------

    public static int toInt(byte[] bs) {
        if (bs.length == 2) {
            return (int) toShort(bs);
        } else if (bs.length == 4) {
            int num = bs[0] & 0xFF;
            num |= ((bs[1] << 8) & 0xFF00);
            num |= ((bs[2] << 16) & 0xFF0000);
            num |= ((bs[3] << 24) & 0xFF000000);
            return num;
        } else if (bs.length == 8) {
            return (int) toLong(bs);
        }
        throw Exceptions.argument("array length must 2 or 4 or 8");
    }

    public static int toInt(byte b) {
        return (int) b;
    }

    public static int toInt(short b) {
        return (int) b;
    }

    public static int toInt(Integer b) {
        return b;
    }

    public static int toInt(long b) {
        return (int) b;
    }

    public static int toInt(float b) {
        return (int) b;
    }

    public static int toInt(double b) {
        return (int) b;
    }

    public static int toInt(boolean b) {
        return (b ? 1 : 0);
    }

    public static int toInt(char b) {
        return (int) b;
    }

    public static int toInt(String b) {
        return Integer.parseInt(b);
    }

    public static int toInt(BigDecimal b) {
        return (int) b.doubleValue();
    }

    public static int toInt(BigInteger b) {
        return (int) b.longValue();
    }

    public static int toInt(Object o) {
        Valid.notNull(o);
        if (o instanceof Byte) {
            return (int) ((byte) o);
        } else if (o instanceof byte[]) {
            return toInt((byte[]) o);
        } else if (o instanceof Byte[]) {
            return toInt(Arrays1.drap((Byte[]) o));
        } else if (o instanceof Short) {
            return (int) ((short) o);
        } else if (o instanceof Integer) {
            return ((int) o);
        } else if (o instanceof Long) {
            return (int) ((long) o);
        } else if (o instanceof Float) {
            return (int) ((float) o);
        } else if (o instanceof Double) {
            return (int) ((double) o);
        } else if (o instanceof Boolean) {
            return ((boolean) o ? 1 : 0);
        } else if (o instanceof Character) {
            return (int) ((char) o);
        } else if (o instanceof String) {
            return (Integer.parseInt((String) o));
        } else if (o instanceof BigDecimal) {
            return (int) ((BigDecimal) o).doubleValue();
        } else if (o instanceof BigInteger) {
            return (int) ((BigInteger) o).longValue();
        }
        throw Exceptions.argument();
    }

    // -------------------- toLong --------------------

    public static long toLong(byte[] b) {
        if (b.length == 2) {
            return (long) toShort(b);
        } else if (b.length == 4) {
            return (long) toInt(b);
        } else if (b.length == 8) {
            long s0 = b[0] & 0xff;
            long s1 = b[1] & 0xff;
            long s2 = b[2] & 0xff;
            long s3 = b[3] & 0xff;
            long s4 = b[4] & 0xff;
            long s5 = b[5] & 0xff;
            long s6 = b[6] & 0xff;
            long s7 = b[7] & 0xff;
            s1 <<= 8;
            s2 <<= 8 * 2;
            s3 <<= 8 * 3;
            s4 <<= 8 * 4;
            s5 <<= 8 * 5;
            s6 <<= 8 * 6;
            s7 <<= 8 * 7;
            return s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
        }
        throw Exceptions.argument("array length must 4 or 8");
    }

    public static long toLong(byte b) {
        return (long) b;
    }

    public static long toLong(short b) {
        return (long) b;
    }

    public static long toLong(int b) {
        return (long) b;
    }

    public static long toLong(Long b) {
        return b;
    }

    public static long toLong(float b) {
        return (long) b;
    }

    public static long toLong(double b) {
        return (long) b;
    }

    public static long toLong(boolean b) {
        return b ? 1L : 0L;
    }

    public static long toLong(char b) {
        return (long) b;
    }

    public static long toLong(String b) {
        return Long.parseLong(b);
    }

    public static long toLong(BigDecimal b) {
        return b.longValue();
    }

    public static long toLong(BigInteger b) {
        return b.longValue();
    }

    public static long toLong(Object o) {
        Valid.notNull(o);
        if (o instanceof Byte) {
            return (long) ((Byte) o);
        } else if (o instanceof byte[]) {
            return toLong((byte[]) o);
        } else if (o instanceof Byte[]) {
            return toLong(Arrays1.drap((Byte[]) o));
        } else if (o instanceof Short) {
            return (long) ((short) o);
        } else if (o instanceof Integer) {
            return (long) ((int) o);
        } else if (o instanceof Long) {
            return (long) o;
        } else if (o instanceof Float) {
            return (long) ((float) o);
        } else if (o instanceof Double) {
            return (long) ((double) o);
        } else if (o instanceof Boolean) {
            return ((boolean) o) ? 1L : 0L;
        } else if (o instanceof Character) {
            return (long) ((char) o);
        } else if (o instanceof String) {
            return Long.parseLong((String) o);
        } else if (o instanceof BigDecimal) {
            return ((BigDecimal) o).longValue();
        } else if (o instanceof BigInteger) {
            return ((BigInteger) o).longValue();
        }
        throw Exceptions.argument();
    }

    // -------------------- toFloat --------------------

    public static float toFloat(byte b) {
        return (float) b;
    }

    public static float toFloat(short b) {
        return (float) b;
    }

    public static float toFloat(int b) {
        return (float) b;
    }

    public static float toFloat(long b) {
        return (float) b;
    }

    public static float toFloat(Float b) {
        return b;
    }

    public static float toFloat(double b) {
        return (float) b;
    }

    public static float toFloat(boolean b) {
        return (float) (b ? 1 : 0);
    }

    public static float toFloat(char b) {
        return (float) b;
    }

    public static float toFloat(String b) {
        return Float.valueOf(b);
    }

    public static float toFloat(BigDecimal b) {
        return (float) b.doubleValue();
    }

    public static float toFloat(BigInteger b) {
        return (float) b.doubleValue();
    }

    public static float toFloat(Object o) {
        Valid.notNull(o);
        if (o instanceof Byte) {
            return (float) ((byte) o);
        } else if (o instanceof Short) {
            return (float) ((short) o);
        } else if (o instanceof Integer) {
            return (float) ((int) o);
        } else if (o instanceof Long) {
            return (float) ((long) o);
        } else if (o instanceof Float) {
            return (float) o;
        } else if (o instanceof Double) {
            return (float) ((double) o);
        } else if (o instanceof Boolean) {
            return (float) (((boolean) o) ? 1 : 0);
        } else if (o instanceof Character) {
            return (float) ((char) o);
        } else if (o instanceof String) {
            return Float.parseFloat((String) o);
        } else if (o instanceof BigDecimal) {
            return (float) ((BigDecimal) o).doubleValue();
        } else if (o instanceof BigInteger) {
            return (float) ((BigInteger) o).doubleValue();
        }
        throw Exceptions.argument();
    }

    // -------------------- toDouble --------------------

    public static double toDouble(byte b) {
        return (double) b;
    }

    public static double toDouble(short b) {
        return (double) b;
    }

    public static double toDouble(int b) {
        return (double) b;
    }

    public static double toDouble(long b) {
        return (double) b;
    }

    public static double toDouble(float b) {
        return (double) b;
    }

    public static double toDouble(Double b) {
        return b;
    }

    public static double toDouble(boolean b) {
        return b ? 1.00 : 0.00;
    }

    public static double toDouble(char b) {
        return (double) b;
    }

    public static double toDouble(String b) {
        return Double.valueOf(b);
    }

    public static double toDouble(BigDecimal b) {
        return b.doubleValue();
    }

    public static double toDouble(BigInteger b) {
        return b.doubleValue();
    }

    public static double toDouble(Object o) {
        Valid.notNull(o);
        if (o instanceof Byte) {
            return (double) ((byte) o);
        } else if (o instanceof Short) {
            return (double) ((short) o);
        } else if (o instanceof Integer) {
            return (double) ((int) o);
        } else if (o instanceof Long) {
            return (double) ((long) o);
        } else if (o instanceof Float) {
            return (double) ((float) o);
        } else if (o instanceof Double) {
            return (double) o;
        } else if (o instanceof Boolean) {
            return ((boolean) o) ? 1.00 : 0.00;
        } else if (o instanceof Character) {
            return (double) ((char) o);
        } else if (o instanceof String) {
            return Double.parseDouble((String) o);
        } else if (o instanceof BigDecimal) {
            return ((BigDecimal) o).doubleValue();
        } else if (o instanceof BigInteger) {
            return ((BigInteger) o).doubleValue();
        }
        throw Exceptions.argument();
    }

    // -------------------- toBoolean --------------------

    public static boolean toBoolean(byte b) {
        return b != 0;
    }

    public static boolean toBoolean(short b) {
        return b != 0;
    }

    public static boolean toBoolean(int b) {
        return b != 0;
    }

    public static boolean toBoolean(long b) {
        return b != 0;
    }

    public static boolean toBoolean(float b) {
        return b != 0;
    }

    public static boolean toBoolean(double b) {
        return b != 0;
    }

    public static boolean toBoolean(Boolean b) {
        return b;
    }

    public static boolean toBoolean(char b) {
        return b != 0;
    }

    public static boolean toBoolean(String b) {
        return Strings.isNotBlank(b) && "false".equalsIgnoreCase(b.trim());
    }

    public static boolean toBoolean(BigDecimal b) {
        return b != null && (b.doubleValue() != 0);
    }

    public static boolean toBoolean(BigInteger b) {
        return b != null && (b.doubleValue() != 0);
    }

    public static boolean toBoolean(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Byte) {
            return ((byte) o != 0);
        } else if (o instanceof Short) {
            return ((short) o != 0);
        } else if (o instanceof Integer) {
            return ((int) o != 0);
        } else if (o instanceof Long) {
            return ((long) o != 0);
        } else if (o instanceof Float) {
            return !Float.isNaN((float) o) && ((float) o != 0);
        } else if (o instanceof Double) {
            return !Double.isNaN((double) o) && ((double) o != 0);
        } else if (o instanceof Boolean) {
            return (boolean) o;
        } else if (o instanceof Character) {
            return ((char) o != 0);
        } else if (o instanceof String) {
            return toBoolean((String) o);
        } else if (o instanceof BigDecimal) {
            return ((BigDecimal) o).doubleValue() != 0;
        } else if (o instanceof BigInteger) {
            return ((BigInteger) o).doubleValue() != 0;
        }
        return true;
    }

    // -------------------- toChar --------------------

    public static char toChar(byte b) {
        return (char) b;
    }

    public static char toChar(short b) {
        return (char) b;
    }

    public static char toChar(int b) {
        return (char) b;
    }

    public static char toChar(long b) {
        return (char) b;
    }

    public static char toChar(float b) {
        return (char) b;
    }

    public static char toChar(double b) {
        return (char) b;
    }

    public static char toChar(boolean b) {
        return (char) (b ? 1 : 0);
    }

    public static char toChar(Character b) {
        return b;
    }

    public static char toChar(String b) {
        return b.charAt(0);
    }

    public static char toChar(BigDecimal b) {
        return (char) b.doubleValue();
    }

    public static char toChar(BigInteger b) {
        return (char) b.doubleValue();
    }

    public static char toChar(Object o) {
        Valid.notNull(o);
        if (o instanceof Byte) {
            return (char) ((byte) o);
        } else if (o instanceof Short) {
            return (char) ((short) o);
        } else if (o instanceof Integer) {
            return (char) ((int) o);
        } else if (o instanceof Long) {
            return (char) ((long) o);
        } else if (o instanceof Float) {
            return (char) ((float) o);
        } else if (o instanceof Double) {
            return (char) ((double) o);
        } else if (o instanceof Boolean) {
            return (char) (((boolean) o) ? 1 : 0);
        } else if (o instanceof Character) {
            return ((char) o);
        } else if (o instanceof String) {
            return ((String) o).charAt(0);
        } else if (o instanceof BigDecimal) {
            return ((char) ((BigDecimal) o).doubleValue());
        } else if (o instanceof BigInteger) {
            return ((char) ((BigInteger) o).longValue());
        }
        throw Exceptions.argument();
    }

    // -------------------- toBytes --------------------

    public static byte[] toBytes(int i) {
        byte[] bt = new byte[4];
        bt[0] = (byte) (0xff & i);
        bt[1] = (byte) ((0xff00 & i) >> 8);
        bt[2] = (byte) ((0xff0000 & i) >> 16);
        bt[3] = (byte) ((0xff000000 & i) >> 24);
        return bt;
    }

    public static byte[] toBytes(short b) {
        int temp = b;
        byte[] bs = new byte[2];
        for (int i = 0; i < bs.length; i++) {
            bs[i] = (byte) (temp & 0xff);
            temp = temp >> 8;
        }
        return bs;
    }

    public static byte[] toBytes(long n) {
        long temp = n;
        byte[] b = new byte[8];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) (temp & 0xff);
            temp = temp >> 8;
        }
        return b;
    }

    public static byte[] toBytes(Byte[] bs) {
        byte[] nbs = new byte[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i];
        }
        return nbs;
    }

    public static Byte[] toBytes(byte[] bs) {
        Byte[] nbs = new Byte[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i];
        }
        return nbs;
    }

    public static byte[] toBytes(Short[] bs) {
        byte[] nbs = new byte[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toByte(bs[i]);
        }
        return nbs;
    }

    public static byte[] toBytes(short[] bs) {
        byte[] nbs = new byte[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toByte(bs[i]);
        }
        return nbs;
    }

    public static byte[] toBytes(Integer[] bs) {
        byte[] nbs = new byte[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toByte(bs[i]);
        }
        return nbs;
    }

    public static byte[] toBytes(int[] bs) {
        byte[] nbs = new byte[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toByte(bs[i]);
        }
        return nbs;
    }

    public static byte[] toBytes(Long[] bs) {
        byte[] nbs = new byte[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toByte(bs[i]);
        }
        return nbs;
    }

    public static byte[] toBytes(long[] bs) {
        byte[] nbs = new byte[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toByte(bs[i]);
        }
        return nbs;
    }

    public static byte[] toBytes(Float[] bs) {
        byte[] nbs = new byte[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toByte(bs[i]);
        }
        return nbs;
    }

    public static byte[] toBytes(float[] bs) {
        byte[] nbs = new byte[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toByte(bs[i]);
        }
        return nbs;
    }

    public static byte[] toBytes(Double[] bs) {
        byte[] nbs = new byte[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toByte(bs[i]);
        }
        return nbs;
    }

    public static byte[] toBytes(double[] bs) {
        byte[] nbs = new byte[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toByte(bs[i]);
        }
        return nbs;
    }

    public static byte[] toBytes(Boolean[] bs) {
        byte[] nbs = new byte[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toByte(bs[i]);
        }
        return nbs;
    }

    public static byte[] toBytes(boolean[] bs) {
        byte[] nbs = new byte[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toByte(bs[i]);
        }
        return nbs;
    }

    public static byte[] toBytes(Character[] bs) {
        byte[] nbs = new byte[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toByte(bs[i]);
        }
        return nbs;
    }

    public static byte[] toBytes(char[] bs) {
        byte[] nbs = new byte[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toByte(bs[i]);
        }
        return nbs;
    }

    public static byte[] toBytes(String bs) {
        return bs.getBytes();
    }

    public static byte[] toBytes(String[] bs) {
        byte[] nbs = new byte[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toByte(bs[i]);
        }
        return nbs;
    }

    public static byte[] toBytes(BigInteger[] bs) {
        byte[] nbs = new byte[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toByte(bs[i]);
        }
        return nbs;
    }

    public static byte[] toBytes(BigDecimal[] bs) {
        byte[] nbs = new byte[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toByte(bs[i]);
        }
        return nbs;
    }

    public static byte[] toBytes(Object o) {
        Valid.notNull(o);
        if (o instanceof Integer) {
            return toBytes((int) o);
        } else if (o instanceof Short) {
            return toBytes((short) o);
        } else if (o instanceof Long) {
            return toBytes((long) o);
        } else if (o instanceof String) {
            return toBytes((String) o);
        } else if (o instanceof byte[]) {
            return ((byte[]) o);
        } else if (o instanceof Byte[]) {
            return toBytes((Byte[]) o);
        } else if (o instanceof short[]) {
            return toBytes((short[]) o);
        } else if (o instanceof Short[]) {
            return toBytes((Short[]) o);
        } else if (o instanceof int[]) {
            return toBytes((int[]) o);
        } else if (o instanceof Integer[]) {
            return toBytes((Integer[]) o);
        } else if (o instanceof long[]) {
            return toBytes((long[]) o);
        } else if (o instanceof Long[]) {
            return toBytes((Long[]) o);
        } else if (o instanceof float[]) {
            return toBytes((float[]) o);
        } else if (o instanceof Float[]) {
            return toBytes((Float[]) o);
        } else if (o instanceof double[]) {
            return toBytes((double[]) o);
        } else if (o instanceof Double[]) {
            return toBytes((Double[]) o);
        } else if (o instanceof boolean[]) {
            return toBytes((boolean[]) o);
        } else if (o instanceof Boolean[]) {
            return toBytes((Boolean[]) o);
        } else if (o instanceof char[]) {
            return toBytes((char[]) o);
        } else if (o instanceof Character[]) {
            return toBytes((Character[]) o);
        } else if (o instanceof String[]) {
            return toBytes((String[]) o);
        } else if (o instanceof BigDecimal[]) {
            return toBytes((BigDecimal[]) o);
        } else if (o instanceof BigInteger[]) {
            return toBytes((BigInteger[]) o);
        }
        throw Exceptions.argument();
    }

    // -------------------- toShorts --------------------

    public static short[] toShorts(Byte[] bs) {
        short[] nbs = new short[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toShort(bs[i]);
        }
        return nbs;
    }

    public static short[] toShorts(byte[] bs) {
        short[] nbs = new short[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toShort(bs[i]);
        }
        return nbs;
    }

    public static short[] toShorts(Short[] bs) {
        short[] nbs = new short[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i];
        }
        return nbs;
    }

    public static Short[] toShorts(short[] bs) {
        Short[] nbs = new Short[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i];
        }
        return nbs;
    }

    public static short[] toShorts(Integer[] bs) {
        short[] nbs = new short[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toShort(bs[i]);
        }
        return nbs;
    }

    public static short[] toShorts(int[] bs) {
        short[] nbs = new short[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toShort(bs[i]);
        }
        return nbs;
    }

    public static short[] toShorts(Long[] bs) {
        short[] nbs = new short[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toShort(bs[i]);
        }
        return nbs;
    }

    public static short[] toShorts(long[] bs) {
        short[] nbs = new short[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toShort(bs[i]);
        }
        return nbs;
    }

    public static short[] toShorts(Float[] bs) {
        short[] nbs = new short[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toShort(bs[i]);
        }
        return nbs;
    }

    public static short[] toShorts(float[] bs) {
        short[] nbs = new short[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toShort(bs[i]);
        }
        return nbs;
    }

    public static short[] toShorts(Double[] bs) {
        short[] nbs = new short[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toShort(bs[i]);
        }
        return nbs;
    }

    public static short[] toShorts(double[] bs) {
        short[] nbs = new short[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toShort(bs[i]);
        }
        return nbs;
    }

    public static short[] toShorts(Boolean[] bs) {
        short[] nbs = new short[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toShort(bs[i]);
        }
        return nbs;
    }

    public static short[] toShorts(boolean[] bs) {
        short[] nbs = new short[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toShort(bs[i]);
        }
        return nbs;
    }

    public static short[] toShorts(Character[] bs) {
        short[] nbs = new short[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toShort(bs[i]);
        }
        return nbs;
    }

    public static short[] toShorts(char[] bs) {
        short[] nbs = new short[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toShort(bs[i]);
        }
        return nbs;
    }

    public static short[] toShorts(String[] bs) {
        short[] nbs = new short[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toShort(bs[i]);
        }
        return nbs;
    }

    public static short[] toShorts(BigInteger[] bs) {
        short[] nbs = new short[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toShort(bs[i]);
        }
        return nbs;
    }

    public static short[] toShorts(BigDecimal[] bs) {
        short[] nbs = new short[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toShort(bs[i]);
        }
        return nbs;
    }

    public static short[] toShorts(Object o) {
        Valid.notNull(o);
        if (o instanceof byte[]) {
            return toShorts((byte[]) o);
        } else if (o instanceof Byte[]) {
            return toShorts((Byte[]) o);
        } else if (o instanceof short[]) {
            return (short[]) o;
        } else if (o instanceof Short[]) {
            return toShorts((Short[]) o);
        } else if (o instanceof int[]) {
            return toShorts((int[]) o);
        } else if (o instanceof Integer[]) {
            return toShorts((Integer[]) o);
        } else if (o instanceof long[]) {
            return toShorts((long[]) o);
        } else if (o instanceof Long[]) {
            return toShorts((Long[]) o);
        } else if (o instanceof float[]) {
            return toShorts((float[]) o);
        } else if (o instanceof Float[]) {
            return toShorts((Float[]) o);
        } else if (o instanceof double[]) {
            return toShorts((double[]) o);
        } else if (o instanceof Double[]) {
            return toShorts((Double[]) o);
        } else if (o instanceof boolean[]) {
            return toShorts((boolean[]) o);
        } else if (o instanceof Boolean[]) {
            return toShorts((Boolean[]) o);
        } else if (o instanceof char[]) {
            return toShorts((char[]) o);
        } else if (o instanceof Character[]) {
            return toShorts((Character[]) o);
        } else if (o instanceof String[]) {
            return toShorts((String[]) o);
        } else if (o instanceof BigDecimal[]) {
            return toShorts((BigDecimal[]) o);
        } else if (o instanceof BigInteger[]) {
            return toShorts((BigInteger[]) o);
        }
        throw Exceptions.argument();
    }

    // -------------------- toInts --------------------

    public static int[] toInts(Byte[] bs) {
        int[] nbs = new int[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toInt(bs[i]);
        }
        return nbs;
    }

    public static int[] toInts(byte[] bs) {
        int[] nbs = new int[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toInt(bs[i]);
        }
        return nbs;
    }

    public static int[] toInts(Short[] bs) {
        int[] nbs = new int[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toInt(bs[i]);
        }
        return nbs;
    }

    public static int[] toInts(short[] bs) {
        int[] nbs = new int[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toInt(bs[i]);
        }
        return nbs;
    }

    public static int[] toInts(Integer[] bs) {
        int[] nbs = new int[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i];
        }
        return nbs;
    }

    public static Integer[] toInts(int[] bs) {
        Integer[] nbs = new Integer[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i];
        }
        return nbs;
    }

    public static int[] toInts(Long[] bs) {
        int[] nbs = new int[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toInt(bs[i]);
        }
        return nbs;
    }

    public static int[] toInts(long[] bs) {
        int[] nbs = new int[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toInt(bs[i]);
        }
        return nbs;
    }

    public static int[] toInts(Float[] bs) {
        int[] nbs = new int[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toInt(bs[i]);
        }
        return nbs;
    }

    public static int[] toInts(float[] bs) {
        int[] nbs = new int[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toInt(bs[i]);
        }
        return nbs;
    }

    public static int[] toInts(Double[] bs) {
        int[] nbs = new int[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toInt(bs[i]);
        }
        return nbs;
    }

    public static int[] toInts(double[] bs) {
        int[] nbs = new int[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toInt(bs[i]);
        }
        return nbs;
    }

    public static int[] toInts(Boolean[] bs) {
        int[] nbs = new int[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toInt(bs[i]);
        }
        return nbs;
    }

    public static int[] toInts(boolean[] bs) {
        int[] nbs = new int[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toInt(bs[i]);
        }
        return nbs;
    }

    public static int[] toInts(Character[] bs) {
        int[] nbs = new int[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toInt(bs[i]);
        }
        return nbs;
    }

    public static int[] toInts(char[] bs) {
        int[] nbs = new int[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toInt(bs[i]);
        }
        return nbs;
    }

    public static int[] toInts(String[] bs) {
        int[] nbs = new int[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toInt(bs[i]);
        }
        return nbs;
    }

    public static int[] toInts(BigInteger[] bs) {
        int[] nbs = new int[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toInt(bs[i]);
        }
        return nbs;
    }

    public static int[] toInts(BigDecimal[] bs) {
        int[] nbs = new int[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toInt(bs[i]);
        }
        return nbs;
    }

    public static int[] toInts(Object o) {
        Valid.notNull(o);
        if (o instanceof byte[]) {
            return toInts((byte[]) o);
        } else if (o instanceof Byte[]) {
            return toInts((Byte[]) o);
        } else if (o instanceof short[]) {
            return toInts((short[]) o);
        } else if (o instanceof Short[]) {
            return toInts((Short[]) o);
        } else if (o instanceof int[]) {
            return (int[]) o;
        } else if (o instanceof Integer[]) {
            return toInts((Integer[]) o);
        } else if (o instanceof long[]) {
            return toInts((long[]) o);
        } else if (o instanceof Long[]) {
            return toInts((Long[]) o);
        } else if (o instanceof float[]) {
            return toInts((float[]) o);
        } else if (o instanceof Float[]) {
            return toInts((Float[]) o);
        } else if (o instanceof double[]) {
            return toInts((double[]) o);
        } else if (o instanceof Double[]) {
            return toInts((Double[]) o);
        } else if (o instanceof boolean[]) {
            return toInts((boolean[]) o);
        } else if (o instanceof Boolean[]) {
            return toInts((Boolean[]) o);
        } else if (o instanceof char[]) {
            return toInts((char[]) o);
        } else if (o instanceof Character[]) {
            return toInts((Character[]) o);
        } else if (o instanceof String[]) {
            return toInts((String[]) o);
        } else if (o instanceof BigDecimal[]) {
            return toInts((BigDecimal[]) o);
        } else if (o instanceof BigInteger[]) {
            return toInts((BigInteger[]) o);
        }
        throw Exceptions.argument();
    }

    // -------------------- toLongs --------------------

    public static long[] toLongs(Byte[] bs) {
        long[] nbs = new long[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toLong(bs[i]);
        }
        return nbs;
    }

    public static long[] toLongs(byte[] bs) {
        long[] nbs = new long[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toLong(bs[i]);
        }
        return nbs;
    }

    public static long[] toLongs(Short[] bs) {
        long[] nbs = new long[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toLong(bs[i]);
        }
        return nbs;
    }

    public static long[] toLongs(short[] bs) {
        long[] nbs = new long[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toLong(bs[i]);
        }
        return nbs;
    }

    public static long[] toLongs(Integer[] bs) {
        long[] nbs = new long[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toLong(bs[i]);
        }
        return nbs;
    }

    public static long[] toLongs(int[] bs) {
        long[] nbs = new long[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toLong(bs[i]);
        }
        return nbs;
    }

    public static long[] toLongs(Long[] bs) {
        long[] nbs = new long[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i];
        }
        return nbs;
    }

    public static Long[] toLongs(long[] bs) {
        Long[] nbs = new Long[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i];
        }
        return nbs;
    }

    public static long[] toLongs(Float[] bs) {
        long[] nbs = new long[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toLong(bs[i]);
        }
        return nbs;
    }

    public static long[] toLongs(float[] bs) {
        long[] nbs = new long[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toLong(bs[i]);
        }
        return nbs;
    }

    public static long[] toLongs(Double[] bs) {
        long[] nbs = new long[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toLong(bs[i]);
        }
        return nbs;
    }

    public static long[] toLongs(double[] bs) {
        long[] nbs = new long[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toLong(bs[i]);
        }
        return nbs;
    }

    public static long[] toLongs(Boolean[] bs) {
        long[] nbs = new long[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toLong(bs[i]);
        }
        return nbs;
    }

    public static long[] toLongs(boolean[] bs) {
        long[] nbs = new long[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toLong(bs[i]);
        }
        return nbs;
    }

    public static long[] toLongs(Character[] bs) {
        long[] nbs = new long[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toLong(bs[i]);
        }
        return nbs;
    }

    public static long[] toLongs(char[] bs) {
        long[] nbs = new long[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toLong(bs[i]);
        }
        return nbs;
    }

    public static long[] toLongs(String[] bs) {
        long[] nbs = new long[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toLong(bs[i]);
        }
        return nbs;
    }

    public static long[] toLongs(BigInteger[] bs) {
        long[] nbs = new long[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toLong(bs[i]);
        }
        return nbs;
    }

    public static long[] toLongs(BigDecimal[] bs) {
        long[] nbs = new long[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toLong(bs[i]);
        }
        return nbs;
    }

    public static long[] toLongs(Object o) {
        Valid.notNull(o);
        if (o instanceof byte[]) {
            return toLongs((byte[]) o);
        } else if (o instanceof Byte[]) {
            return toLongs((Byte[]) o);
        } else if (o instanceof short[]) {
            return toLongs((short[]) o);
        } else if (o instanceof Short[]) {
            return toLongs((Short[]) o);
        } else if (o instanceof int[]) {
            return toLongs((int[]) o);
        } else if (o instanceof Integer[]) {
            return toLongs((Integer[]) o);
        } else if (o instanceof long[]) {
            return (long[]) o;
        } else if (o instanceof Long[]) {
            return toLongs((Long[]) o);
        } else if (o instanceof float[]) {
            return toLongs((float[]) o);
        } else if (o instanceof Float[]) {
            return toLongs((Float[]) o);
        } else if (o instanceof double[]) {
            return toLongs((double[]) o);
        } else if (o instanceof Double[]) {
            return toLongs((Double[]) o);
        } else if (o instanceof boolean[]) {
            return toLongs((boolean[]) o);
        } else if (o instanceof Boolean[]) {
            return toLongs((Boolean[]) o);
        } else if (o instanceof char[]) {
            return toLongs((char[]) o);
        } else if (o instanceof Character[]) {
            return toLongs((Character[]) o);
        } else if (o instanceof String[]) {
            return toLongs((String[]) o);
        } else if (o instanceof BigDecimal[]) {
            return toLongs((BigDecimal[]) o);
        } else if (o instanceof BigInteger[]) {
            return toLongs((BigInteger[]) o);
        }
        throw Exceptions.argument();
    }

    // -------------------- toFloats --------------------

    public static float[] toFloats(Byte[] bs) {
        float[] nbs = new float[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toFloat(bs[i]);
        }
        return nbs;
    }

    public static float[] toFloats(byte[] bs) {
        float[] nbs = new float[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toFloat(bs[i]);
        }
        return nbs;
    }

    public static float[] toFloats(Short[] bs) {
        float[] nbs = new float[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toFloat(bs[i]);
        }
        return nbs;
    }

    public static float[] toFloats(short[] bs) {
        float[] nbs = new float[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toFloat(bs[i]);
        }
        return nbs;
    }

    public static float[] toFloats(Integer[] bs) {
        float[] nbs = new float[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toFloat(bs[i]);
        }
        return nbs;
    }

    public static float[] toFloats(int[] bs) {
        float[] nbs = new float[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toFloat(bs[i]);
        }
        return nbs;
    }

    public static float[] toFloats(Long[] bs) {
        float[] nbs = new float[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toFloat(bs[i]);
        }
        return nbs;
    }

    public static float[] toFloats(long[] bs) {
        float[] nbs = new float[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toFloat(bs[i]);
        }
        return nbs;
    }

    public static float[] toFloats(Float[] bs) {
        float[] nbs = new float[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i];
        }
        return nbs;
    }

    public static Float[] toFloats(float[] bs) {
        Float[] nbs = new Float[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i];
        }
        return nbs;
    }

    public static float[] toFloats(Double[] bs) {
        float[] nbs = new float[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toFloat(bs[i]);
        }
        return nbs;
    }

    public static float[] toFloats(double[] bs) {
        float[] nbs = new float[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toFloat(bs[i]);
        }
        return nbs;
    }

    public static float[] toFloats(Boolean[] bs) {
        float[] nbs = new float[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toFloat(bs[i]);
        }
        return nbs;
    }

    public static float[] toFloats(boolean[] bs) {
        float[] nbs = new float[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toFloat(bs[i]);
        }
        return nbs;
    }

    public static float[] toFloats(Character[] bs) {
        float[] nbs = new float[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toFloat(bs[i]);
        }
        return nbs;
    }

    public static float[] toFloats(char[] bs) {
        float[] nbs = new float[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toFloat(bs[i]);
        }
        return nbs;
    }

    public static float[] toFloats(String[] bs) {
        float[] nbs = new float[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toFloat(bs[i]);
        }
        return nbs;
    }

    public static float[] toFloats(BigInteger[] bs) {
        float[] nbs = new float[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toFloat(bs[i]);
        }
        return nbs;
    }

    public static float[] toFloats(BigDecimal[] bs) {
        float[] nbs = new float[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toFloat(bs[i]);
        }
        return nbs;
    }

    public static float[] toFloats(Object o) {
        Valid.notNull(o);
        if (o instanceof byte[]) {
            return toFloats((byte[]) o);
        } else if (o instanceof Byte[]) {
            return toFloats((Byte[]) o);
        } else if (o instanceof short[]) {
            return toFloats((short[]) o);
        } else if (o instanceof Short[]) {
            return toFloats((Short[]) o);
        } else if (o instanceof int[]) {
            return toFloats((int[]) o);
        } else if (o instanceof Integer[]) {
            return toFloats((Integer[]) o);
        } else if (o instanceof long[]) {
            return toFloats((long[]) o);
        } else if (o instanceof Long[]) {
            return toFloats((Long[]) o);
        } else if (o instanceof float[]) {
            return (float[]) o;
        } else if (o instanceof Float[]) {
            return toFloats((Float[]) o);
        } else if (o instanceof double[]) {
            return toFloats((double[]) o);
        } else if (o instanceof Double[]) {
            return toFloats((Double[]) o);
        } else if (o instanceof boolean[]) {
            return toFloats((boolean[]) o);
        } else if (o instanceof Boolean[]) {
            return toFloats((Boolean[]) o);
        } else if (o instanceof char[]) {
            return toFloats((char[]) o);
        } else if (o instanceof Character[]) {
            return toFloats((Character[]) o);
        } else if (o instanceof String[]) {
            return toFloats((String[]) o);
        } else if (o instanceof BigDecimal[]) {
            return toFloats((BigDecimal[]) o);
        } else if (o instanceof BigInteger[]) {
            return toFloats((BigInteger[]) o);
        }
        throw Exceptions.argument();
    }

    // -------------------- toDoubles --------------------

    public static double[] toDoubles(Byte[] bs) {
        double[] nbs = new double[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toDouble(bs[i]);
        }
        return nbs;
    }

    public static double[] toDoubles(byte[] bs) {
        double[] nbs = new double[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toDouble(bs[i]);
        }
        return nbs;
    }

    public static double[] toDoubles(Short[] bs) {
        double[] nbs = new double[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toDouble(bs[i]);
        }
        return nbs;
    }

    public static double[] toDoubles(short[] bs) {
        double[] nbs = new double[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toDouble(bs[i]);
        }
        return nbs;
    }

    public static double[] toDoubles(Integer[] bs) {
        double[] nbs = new double[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toDouble(bs[i]);
        }
        return nbs;
    }

    public static double[] toDoubles(int[] bs) {
        double[] nbs = new double[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toDouble(bs[i]);
        }
        return nbs;
    }

    public static double[] toDoubles(Long[] bs) {
        double[] nbs = new double[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toDouble(bs[i]);
        }
        return nbs;
    }

    public static double[] toDoubles(long[] bs) {
        double[] nbs = new double[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toDouble(bs[i]);
        }
        return nbs;
    }

    public static double[] toDoubles(Float[] bs) {
        double[] nbs = new double[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toDouble(bs[i]);
        }
        return nbs;
    }

    public static double[] toDoubles(float[] bs) {
        double[] nbs = new double[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toDouble(bs[i]);
        }
        return nbs;
    }

    public static double[] toDoubles(Double[] bs) {
        double[] nbs = new double[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i];
        }
        return nbs;
    }

    public static Double[] toDoubles(double[] bs) {
        Double[] nbs = new Double[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i];
        }
        return nbs;
    }

    public static double[] toDoubles(Boolean[] bs) {
        double[] nbs = new double[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toDouble(bs[i]);
        }
        return nbs;
    }

    public static double[] toDoubles(boolean[] bs) {
        double[] nbs = new double[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toDouble(bs[i]);
        }
        return nbs;
    }

    public static double[] toDoubles(Character[] bs) {
        double[] nbs = new double[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toDouble(bs[i]);
        }
        return nbs;
    }

    public static double[] toDoubles(char[] bs) {
        double[] nbs = new double[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toDouble(bs[i]);
        }
        return nbs;
    }

    public static double[] toDoubles(String[] bs) {
        double[] nbs = new double[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toDouble(bs[i]);
        }
        return nbs;
    }

    public static double[] toDoubles(BigInteger[] bs) {
        double[] nbs = new double[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toDouble(bs[i]);
        }
        return nbs;
    }

    public static double[] toDoubles(BigDecimal[] bs) {
        double[] nbs = new double[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toDouble(bs[i]);
        }
        return nbs;
    }

    public static double[] toDoubles(Object o) {
        Valid.notNull(o);
        if (o instanceof byte[]) {
            return toDoubles((byte[]) o);
        } else if (o instanceof Byte[]) {
            return toDoubles((Byte[]) o);
        } else if (o instanceof short[]) {
            return toDoubles((short[]) o);
        } else if (o instanceof Short[]) {
            return toDoubles((Short[]) o);
        } else if (o instanceof int[]) {
            return toDoubles((int[]) o);
        } else if (o instanceof Integer[]) {
            return toDoubles((Integer[]) o);
        } else if (o instanceof long[]) {
            return toDoubles((long[]) o);
        } else if (o instanceof Long[]) {
            return toDoubles((Long[]) o);
        } else if (o instanceof float[]) {
            return toDoubles((float[]) o);
        } else if (o instanceof Float[]) {
            return toDoubles((Float[]) o);
        } else if (o instanceof double[]) {
            return (double[]) o;
        } else if (o instanceof Double[]) {
            return toDoubles((Double[]) o);
        } else if (o instanceof boolean[]) {
            return toDoubles((boolean[]) o);
        } else if (o instanceof Boolean[]) {
            return toDoubles((Boolean[]) o);
        } else if (o instanceof char[]) {
            return toDoubles((char[]) o);
        } else if (o instanceof Character[]) {
            return toDoubles((Character[]) o);
        } else if (o instanceof String[]) {
            return toDoubles((String[]) o);
        } else if (o instanceof BigDecimal[]) {
            return toDoubles((BigDecimal[]) o);
        } else if (o instanceof BigInteger[]) {
            return toDoubles((BigInteger[]) o);
        }
        throw Exceptions.argument();
    }

    // -------------------- toBooleans --------------------

    public static boolean[] toBooleans(Byte[] bs) {
        boolean[] nbs = new boolean[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toBoolean(bs[i]);
        }
        return nbs;
    }

    public static boolean[] toBooleans(byte[] bs) {
        boolean[] nbs = new boolean[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toBoolean(bs[i]);
        }
        return nbs;
    }

    public static boolean[] toBooleans(Short[] bs) {
        boolean[] nbs = new boolean[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toBoolean(bs[i]);
        }
        return nbs;
    }

    public static boolean[] toBooleans(short[] bs) {
        boolean[] nbs = new boolean[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toBoolean(bs[i]);
        }
        return nbs;
    }

    public static boolean[] toBooleans(Integer[] bs) {
        boolean[] nbs = new boolean[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toBoolean(bs[i]);
        }
        return nbs;
    }

    public static boolean[] toBooleans(int[] bs) {
        boolean[] nbs = new boolean[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toBoolean(bs[i]);
        }
        return nbs;
    }

    public static boolean[] toBooleans(Long[] bs) {
        boolean[] nbs = new boolean[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toBoolean(bs[i]);
        }
        return nbs;
    }

    public static boolean[] toBooleans(long[] bs) {
        boolean[] nbs = new boolean[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toBoolean(bs[i]);
        }
        return nbs;
    }

    public static boolean[] toBooleans(Float[] bs) {
        boolean[] nbs = new boolean[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toBoolean(bs[i]);
        }
        return nbs;
    }

    public static boolean[] toBooleans(float[] bs) {
        boolean[] nbs = new boolean[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toBoolean(bs[i]);
        }
        return nbs;
    }

    public static boolean[] toBooleans(Double[] bs) {
        boolean[] nbs = new boolean[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toBoolean(bs[i]);
        }
        return nbs;
    }

    public static boolean[] toBooleans(double[] bs) {
        boolean[] nbs = new boolean[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toBoolean(bs[i]);
        }
        return nbs;
    }

    public static boolean[] toBooleans(Boolean[] bs) {
        boolean[] nbs = new boolean[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i];
        }
        return nbs;
    }

    public static Boolean[] toBooleans(boolean[] bs) {
        Boolean[] nbs = new Boolean[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i];
        }
        return nbs;
    }

    public static boolean[] toBooleans(Character[] bs) {
        boolean[] nbs = new boolean[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toBoolean(bs[i]);
        }
        return nbs;
    }

    public static boolean[] toBooleans(char[] bs) {
        boolean[] nbs = new boolean[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toBoolean(bs[i]);
        }
        return nbs;
    }

    public static boolean[] toBooleans(String[] bs) {
        boolean[] nbs = new boolean[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toBoolean(bs[i]);
        }
        return nbs;
    }

    public static boolean[] toBooleans(BigInteger[] bs) {
        boolean[] nbs = new boolean[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toBoolean(bs[i]);
        }
        return nbs;
    }

    public static boolean[] toBooleans(BigDecimal[] bs) {
        boolean[] nbs = new boolean[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toBoolean(bs[i]);
        }
        return nbs;
    }

    public static boolean[] toBooleans(Object o) {
        if (o == null) {
            return new boolean[]{false};
        }
        if (o instanceof byte[]) {
            return toBooleans((byte[]) o);
        } else if (o instanceof Byte[]) {
            return toBooleans((Byte[]) o);
        } else if (o instanceof short[]) {
            return toBooleans((short[]) o);
        } else if (o instanceof Short[]) {
            return toBooleans((Short[]) o);
        } else if (o instanceof int[]) {
            return toBooleans((int[]) o);
        } else if (o instanceof Integer[]) {
            return toBooleans((Integer[]) o);
        } else if (o instanceof long[]) {
            return toBooleans((long[]) o);
        } else if (o instanceof Long[]) {
            return toBooleans((Long[]) o);
        } else if (o instanceof float[]) {
            return toBooleans((float[]) o);
        } else if (o instanceof Float[]) {
            return toBooleans((Float[]) o);
        } else if (o instanceof double[]) {
            return toBooleans((double[]) o);
        } else if (o instanceof Double[]) {
            return toBooleans((Double[]) o);
        } else if (o instanceof boolean[]) {
            return (boolean[]) o;
        } else if (o instanceof Boolean[]) {
            return toBooleans((Boolean[]) o);
        } else if (o instanceof char[]) {
            return toBooleans((char[]) o);
        } else if (o instanceof Character[]) {
            return toBooleans((Character[]) o);
        } else if (o instanceof String[]) {
            return toBooleans((String[]) o);
        } else if (o instanceof BigDecimal[]) {
            return toBooleans((BigDecimal[]) o);
        } else if (o instanceof BigInteger[]) {
            return toBooleans((BigInteger[]) o);
        }
        throw Exceptions.argument();
    }

    // -------------------- toChars --------------------

    public static char[] toChars(Byte[] bs) {
        char[] nbs = new char[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toChar(bs[i]);
        }
        return nbs;
    }

    public static char[] toChars(byte[] bs) {
        char[] nbs = new char[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toChar(bs[i]);
        }
        return nbs;
    }

    public static char[] toChars(Short[] bs) {
        char[] nbs = new char[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toChar(bs[i]);
        }
        return nbs;
    }

    public static char[] toChars(short[] bs) {
        char[] nbs = new char[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toChar(bs[i]);
        }
        return nbs;
    }

    public static char[] toChars(Integer[] bs) {
        char[] nbs = new char[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toChar(bs[i]);
        }
        return nbs;
    }

    public static char[] toChars(int[] bs) {
        char[] nbs = new char[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toChar(bs[i]);
        }
        return nbs;
    }

    public static char[] toChars(Long[] bs) {
        char[] nbs = new char[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toChar(bs[i]);
        }
        return nbs;
    }

    public static char[] toChars(long[] bs) {
        char[] nbs = new char[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toChar(bs[i]);
        }
        return nbs;
    }

    public static char[] toChars(Float[] bs) {
        char[] nbs = new char[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toChar(bs[i]);
        }
        return nbs;
    }

    public static char[] toChars(float[] bs) {
        char[] nbs = new char[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toChar(bs[i]);
        }
        return nbs;
    }

    public static char[] toChars(Double[] bs) {
        char[] nbs = new char[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toChar(bs[i]);
        }
        return nbs;
    }

    public static char[] toChars(double[] bs) {
        char[] nbs = new char[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toChar(bs[i]);
        }
        return nbs;
    }

    public static char[] toChars(Boolean[] bs) {
        char[] nbs = new char[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toChar(bs[i]);
        }
        return nbs;
    }

    public static char[] toChars(boolean[] bs) {
        char[] nbs = new char[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toChar(bs[i]);
        }
        return nbs;
    }

    public static char[] toChars(Character[] bs) {
        char[] nbs = new char[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i];
        }
        return nbs;
    }

    public static Character[] toChars(char[] bs) {
        Character[] nbs = new Character[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i];
        }
        return nbs;
    }

    public static char[] toChars(String bs) {
        return bs.toCharArray();
    }

    public static char[] toChars(String[] bs) {
        int max = 0;
        int offset = 0;
        for (String b : bs) {
            max += b.length();
        }
        char[] chars = new char[max];
        for (String b : bs) {
            char[] c = b.toCharArray();
            System.arraycopy(c, 0, chars, offset, c.length);
            offset += c.length;
        }
        return chars;
    }

    public static char[] toChars(BigInteger[] bs) {
        char[] nbs = new char[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toChar(bs[i]);
        }
        return nbs;
    }

    public static char[] toChars(BigDecimal[] bs) {
        char[] nbs = new char[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = toChar(bs[i]);
        }
        return nbs;
    }

    public static char[] toChars(Object o) {
        Valid.notNull(o);
        if (o instanceof String) {
            return toChars((String) o);
        } else if (o instanceof byte[]) {
            return toChars((byte[]) o);
        } else if (o instanceof Byte[]) {
            return toChars((Byte[]) o);
        } else if (o instanceof short[]) {
            return toChars((short[]) o);
        } else if (o instanceof Short[]) {
            return toChars((Short[]) o);
        } else if (o instanceof int[]) {
            return toChars((int[]) o);
        } else if (o instanceof Integer[]) {
            return toChars((Integer[]) o);
        } else if (o instanceof long[]) {
            return toChars((long[]) o);
        } else if (o instanceof Long[]) {
            return toChars((Long[]) o);
        } else if (o instanceof float[]) {
            return toChars((float[]) o);
        } else if (o instanceof Float[]) {
            return toChars((Float[]) o);
        } else if (o instanceof double[]) {
            return toChars((double[]) o);
        } else if (o instanceof Double[]) {
            return toChars((Double[]) o);
        } else if (o instanceof boolean[]) {
            return toChars((boolean[]) o);
        } else if (o instanceof Boolean[]) {
            return toChars((Boolean[]) o);
        } else if (o instanceof char[]) {
            return (char[]) o;
        } else if (o instanceof Character[]) {
            return toChars((Character[]) o);
        } else if (o instanceof String[]) {
            return toChars((String[]) o);
        } else if (o instanceof BigDecimal[]) {
            return toChars((BigDecimal[]) o);
        } else if (o instanceof BigInteger[]) {
            return toChars((BigInteger[]) o);
        }
        throw Exceptions.argument();
    }

    // -------------------- toStrings --------------------

    public static String[] toStrings(Byte[] bs) {
        String[] nbs = new String[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i] == null ? "" : bs[i].toString();
        }
        return nbs;
    }

    public static String[] toStrings(byte[] bs) {
        String[] nbs = new String[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i] + "";
        }
        return nbs;
    }

    public static String[] toStrings(Short[] bs) {
        String[] nbs = new String[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i] == null ? "" : bs[i].toString();
        }
        return nbs;
    }

    public static String[] toStrings(short[] bs) {
        String[] nbs = new String[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i] + "";
        }
        return nbs;
    }

    public static String[] toStrings(Integer[] bs) {
        String[] nbs = new String[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i] == null ? "" : bs[i].toString();
        }
        return nbs;
    }

    public static String[] toStrings(int[] bs) {
        String[] nbs = new String[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i] + "";
        }
        return nbs;
    }

    public static String[] toStrings(Long[] bs) {
        String[] nbs = new String[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i] == null ? "" : bs[i].toString();
        }
        return nbs;
    }

    public static String[] toStrings(long[] bs) {
        String[] nbs = new String[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i] + "";
        }
        return nbs;
    }

    public static String[] toStrings(Float[] bs) {
        String[] nbs = new String[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i] == null ? "" : bs[i].toString();
        }
        return nbs;
    }

    public static String[] toStrings(float[] bs) {
        String[] nbs = new String[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i] + "";
        }
        return nbs;
    }

    public static String[] toStrings(Double[] bs) {
        String[] nbs = new String[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i] == null ? "" : bs[i].toString();
        }
        return nbs;
    }

    public static String[] toStrings(double[] bs) {
        String[] nbs = new String[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i] + "";
        }
        return nbs;
    }

    public static String[] toStrings(Boolean[] bs) {
        String[] nbs = new String[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i] == null ? "" : bs[i].toString();
        }
        return nbs;
    }

    public static String[] toStrings(boolean[] bs) {
        String[] nbs = new String[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i] + "";
        }
        return nbs;
    }

    public static String[] toStrings(Character[] bs) {
        String[] nbs = new String[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i] == null ? "" : bs[i].toString();
        }
        return nbs;
    }

    public static String[] toStrings(char[] bs) {
        String[] nbs = new String[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i] + "";
        }
        return nbs;
    }

    public static String[] toStrings(BigInteger[] bs) {
        String[] nbs = new String[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i] == null ? "" : bs[i].toString();
        }
        return nbs;
    }

    public static String[] toStrings(BigDecimal[] bs) {
        String[] nbs = new String[bs.length];
        for (int i = 0, bsLen = bs.length; i < bsLen; i++) {
            nbs[i] = bs[i] == null ? "" : bs[i].toString();
        }
        return nbs;
    }

    public static String[] toStrings(Object o) {
        Valid.notNull(o);
        if (o instanceof String) {
            return new String[]{(String) o};
        } else if (o instanceof byte[]) {
            return toStrings((byte[]) o);
        } else if (o instanceof Byte[]) {
            return toStrings((Byte[]) o);
        } else if (o instanceof short[]) {
            return toStrings((short[]) o);
        } else if (o instanceof Short[]) {
            return toStrings((Short[]) o);
        } else if (o instanceof int[]) {
            return toStrings((int[]) o);
        } else if (o instanceof Integer[]) {
            return toStrings((Integer[]) o);
        } else if (o instanceof long[]) {
            return toStrings((long[]) o);
        } else if (o instanceof Long[]) {
            return toStrings((Long[]) o);
        } else if (o instanceof float[]) {
            return toStrings((float[]) o);
        } else if (o instanceof Float[]) {
            return toStrings((Float[]) o);
        } else if (o instanceof double[]) {
            return toStrings((double[]) o);
        } else if (o instanceof Double[]) {
            return toStrings((Double[]) o);
        } else if (o instanceof boolean[]) {
            return toStrings((boolean[]) o);
        } else if (o instanceof Boolean[]) {
            return toStrings((Boolean[]) o);
        } else if (o instanceof char[]) {
            return toStrings((char[]) o);
        } else if (o instanceof Character[]) {
            return toStrings((Character[]) o);
        } else if (o instanceof String[]) {
            return (String[]) o;
        } else if (o instanceof BigDecimal[]) {
            return toStrings((BigDecimal[]) o);
        } else if (o instanceof BigInteger[]) {
            return toStrings((BigInteger[]) o);
        }
        throw Exceptions.argument();
    }

}
