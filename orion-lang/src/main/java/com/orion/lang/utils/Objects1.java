package com.orion.lang.utils;

import com.orion.lang.constant.Const;
import com.orion.lang.define.Null;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.collect.Maps;
import com.orion.lang.utils.hash.Hashes;
import com.orion.lang.utils.reflect.Methods;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
    public static <T> T def(T o, T def) {
        if (isNull(o)) {
            return def;
        }
        return o;
    }

    /**
     * 默认值
     *
     * @param o        对象
     * @param supplier 默认值提供者
     * @return 如果对象为空返回默认值
     */
    public static <T> T def(T o, Supplier<T> supplier) {
        if (isNull(o)) {
            return supplier.get();
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
    public static <T, R> R map(T o, Function<T, R> f) {
        if (!isNull(o) && !isNull(f)) {
            return f.apply(o);
        }
        return null;
    }

    /**
     * 如果对象不为null调用consumer
     *
     * @param o 对象
     * @param c consumer
     */
    public static <T> void accept(T o, Consumer<T> c) {
        if (!isNull(o) && !isNull(c)) {
            c.accept(o);
        }
    }

    /**
     * 判断对象是否相等
     *
     * @param o1 对象1
     * @param o2 对象2
     * @return true相等
     */
    public static boolean eq(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        if (o1.equals(o2)) {
            return true;
        }
        if (o1.getClass().isArray() && o2.getClass().isArray()) {
            return Arrays1.arrayEquals(o1, o2);
        }
        return false;
    }

    /**
     * 获取第一个非空对象
     *
     * @param values values
     * @param <T>    T
     * @return 非空对象 没有返回null
     */
    @SafeVarargs
    public static <T> T firstNotNull(T... values) {
        if (values != null) {
            for (T val : values) {
                if (val != null) {
                    return val;
                }
            }
        }
        return null;
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
        if (o.getClass().isArray()) {
            return Array.getLength(o) == 0;
        } else if (o instanceof Collection) {
            return Lists.isEmpty((Collection<?>) o);
        } else if (o instanceof Map) {
            return Maps.isEmpty((Map<?, ?>) o);
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
    public static boolean isVoids(Class<?> c) {
        return Void.TYPE.equals(c) || Void.class.equals(c) || c == com.orion.lang.define.Void.class;
    }

    /**
     * 判断是否不为void
     *
     * @param c class
     * @return true不为void
     */
    public static boolean isNotVoids(Class<?> c) {
        return !isVoids(c);
    }

    /**
     * 如果实现了cloneable 会调用clone方法
     * 如果没实现cloneable 则会深拷贝对象 对象必须实现序列化接口
     *
     * @param o   对象
     * @param <T> T
     * @return clone T
     */
    public static <T> T clone(T o) {
        if (o == null) {
            return null;
        }
        if (Arrays1.isArray(o)) {
            return deserialize(serialize(((Serializable) o)));
        }
        if (o.getClass() != Object.class && o instanceof Cloneable) {
            return Methods.invokeMethod(o, "clone");
        } else if (o instanceof Serializable) {
            return deserialize(serialize(((Serializable) o)));
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
        try (ByteArrayOutputStream bs = new ByteArrayOutputStream(Const.BUFFER_KB_1);
             ObjectOutputStream oos = new ObjectOutputStream(bs)) {
            oos.writeObject(o);
            oos.flush();
            return bs.toByteArray();
        } catch (IOException e) {
            throw Exceptions.argument("failed to serialize object of type: " + o.getClass(), e);
        }
    }

    /**
     * 反序列化
     *
     * @param bytes ignore
     * @return ignore
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (T) ois.readObject();
        } catch (Exception e) {
            throw Exceptions.argument("failed to deserialize object", e);
        }
    }

    /**
     * 转为string
     *
     * @param o ignore
     * @return ignore
     */
    public static String toString(Object o) {
        if (o == null) {
            return Strings.EMPTY;
        } else if (o instanceof String) {
            return (String) o;
        } else if (o instanceof Object[]) {
            return Arrays.toString((Object[]) o);
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
        } else {
            return def(o.toString(), Strings.EMPTY);
        }
    }

    /**
     * 获取对象标识 包含 class
     *
     * @param obj 对象
     * @return 对象标识
     */
    public static String getObjectIdentity(Object obj) {
        if (obj == null) {
            return Strings.EMPTY;
        }
        return obj.getClass().getName() + "@" + getIdentity(obj);
    }

    /**
     * 获取对象标识
     *
     * @param obj 对象
     * @return 对象标识
     */
    public static String getIdentity(Object obj) {
        return Integer.toHexString(System.identityHashCode(obj));
    }

    /**
     * 获取对象的hash值
     *
     * @param obj obj
     * @return hashCode
     */
    public static int hashCode(Object obj) {
        return Hashes.hashCode(obj);
    }

}
