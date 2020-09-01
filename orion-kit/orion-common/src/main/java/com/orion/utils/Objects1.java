package com.orion.utils;

import com.orion.lang.Null;
import com.orion.utils.collect.Lists;
import com.orion.utils.collect.Maps;
import com.orion.utils.reflect.Methods;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;

/**
 * Object工具类
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/9/20 17:10
 */
public class Objects1 {

    private Objects1() {
    }

    /**
     * 默认值
     *
     * @param o   对象
     * @param def 默认值
     * @return 如果对象为空返回默认值
     */
    public static Object def(Object o, Object def) {
        if (isNull(o)) {
            return def;
        }
        return o;
    }

    /**
     * 默认值
     *
     * @param o   对象
     * @param def 默认值
     * @return 如果对象为空返回默认值
     */
    public static <T> T det(T o, T def) {
        if (isNull(o)) {
            return def;
        }
        return o;
    }

    /**
     * 如果对象不为null调用function
     *
     * @param o 对象
     * @param f function
     * @return 如果对象不为空调用function
     */
    public static <T, R> R of(T o, Function<T, R> f) {
        if (!isNull(o) && !isNull(f)) {
            return f.apply(o);
        }
        return null;
    }

    /**
     * 判断对象是否相等
     *
     * @param o1 对象1
     * @param o2 对象2
     * @return true相等
     */
    public static boolean eq(Object o1, Object o2) {
        return (o1 == o2) || (o1 != null && o1.equals(o2));
    }

    /**
     * 判断对象是否为null
     *
     * @param o 对象
     * @return true为空
     */
    public static boolean isNull(Object o) {
        return o == null;
    }

    /**
     * 判断对象是否不为null
     *
     * @param o 对象
     * @return true不为空
     */
    public static boolean isNotNull(Object o) {
        return o != null;
    }

    /**
     * 判断对象是否为null 或者为 Null
     *
     * @param o 对象
     * @return true为空
     */
    public static boolean isNulls(Object o) {
        return o == null || o == Null.VALUE;
    }

    /**
     * 判断对象是否不为null 并且不为 Null
     *
     * @param o 对象
     * @return true不为空
     */
    public static boolean isNotNulls(Object o) {
        return o != null && o != Null.VALUE;
    }

    /**
     * 判断对象是否全为null
     *
     * @param o 对象
     * @return true全为null
     */
    public static boolean isAllNull(Object... o) {
        if (o == null) {
            return true;
        }
        for (Object o1 : o) {
            if (o1 != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断对象是否全不为null
     *
     * @param o 对象
     * @return true全不为null, 参数为null返回false
     */
    public static boolean isNoneNull(Object... o) {
        if (o == null) {
            return false;
        }
        for (Object o1 : o) {
            if (o1 == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断对象是否为空
     *
     * @param o 对象
     * @return true空
     */
    public static boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        }
        if (o instanceof Object[]) {
            return Arrays1.isEmpty((Object[]) o);
        } else if (o instanceof Collection) {
            return Lists.isEmpty((Collection) o);
        } else if (o instanceof Map) {
            return Maps.isEmpty((Map) o);
        } else if (o instanceof String) {
            return Strings.isBlank((String) o);
        }
        return false;
    }

    /**
     * 判断对象是否不为空
     *
     * @param o 对象
     * @return true非空
     */
    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    /**
     * 判断对象是否全部为空
     *
     * @param o 对象
     * @return true全为空
     */
    public static boolean isAllEmpty(Object... o) {
        if (o == null) {
            return true;
        }
        for (Object o1 : o) {
            if (isNotEmpty(o1)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断对象是否全部不为空
     *
     * @param o 对象
     * @return true全不为空
     */
    public static boolean isNoneEmpty(Object... o) {
        if (o == null) {
            return false;
        }
        for (Object o1 : o) {
            if (isEmpty(o1)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否为void
     *
     * @param c class
     * @return true为void
     */
    public static boolean isVoids(Class c) {
        return "void".equals(c.toString()) || c == com.orion.lang.Void.class || c == Void.class;
    }

    /**
     * 判断是否不为void
     *
     * @param c class
     * @return true不为void
     */
    public static boolean isNotVoids(Class c) {
        return !isVoids(c);
    }

    @SuppressWarnings("unchecked")
    public static <T> T clone(T o) {
        if (o == null) {
            return null;
        }
        if (Arrays1.isArray(o)) {
            return (T) deserialize(serialize(((Serializable) o)));
        }
        if (o.getClass() != Object.class && o instanceof Cloneable) {
            return Methods.invokeMethod(o, "clone");
        } else if (o instanceof Serializable) {
            return (T) deserialize(serialize(((Serializable) o)));
        } else {
            throw Exceptions.argument("Failed to serialize object, not implements Cloneable and not implements Serializable");
        }
    }

    /**
     * 序列化对象
     *
     * @param o 对象
     * @return ignore
     */
    public static <T extends Serializable> byte[] serialize(T o) {
        if (o == null) {
            return null;
        }
        ByteArrayOutputStream bs = new ByteArrayOutputStream(1024);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bs);
            oos.writeObject(o);
            oos.flush();
        } catch (IOException e) {
            throw Exceptions.argument("Failed to serialize object of type: " + o.getClass(), e);
        }
        return bs.toByteArray();
    }

    /**
     * 反序列化
     *
     * @param bytes ignore
     * @return ignore
     */
    public static Object deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return ois.readObject();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to deserialize object", e);
        }
    }

    /**
     * 比较接口
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
     * 比较接口
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
     * 转为string
     *
     * @param o ignore
     * @return ignore
     */
    public static String toString(Object o) {
        if (o == null) {
            return "";
        } else if (o instanceof byte[]) {
            return Arrays.toString((byte[]) o);
        } else if (o instanceof short[]) {
            return Arrays.toString((short[]) o);
        } else if (o instanceof int[]) {
            return Arrays.toString((int[]) o);
        } else if (o instanceof long[]) {
            return Arrays.toString((long[]) o);
        } else if (o instanceof float[]) {
            return Arrays.toString((float[]) o);
        } else if (o instanceof double[]) {
            return Arrays.toString((double[]) o);
        } else if (o instanceof char[]) {
            return Arrays.toString((char[]) o);
        } else if (o instanceof boolean[]) {
            return Arrays.toString((boolean[]) o);
        } else if (o instanceof Object[]) {
            return Arrays.toString((Object[]) o);
        } else {
            return o.toString();
        }
    }

}
