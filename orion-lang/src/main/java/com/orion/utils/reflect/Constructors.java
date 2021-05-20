package com.orion.utils.reflect;

import com.orion.lang.collect.ConcurrentReferenceHashMap;
import com.orion.utils.Arrays1;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.Valid;
import com.orion.utils.collect.Lists;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 反射 构造方法工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/15 13:17
 */
@SuppressWarnings("ALL")
public class Constructors {

    private static final Map<Class<?>, Constructor<?>> CLASS_DEFAULT_CONSTRUCTOR_CACHE = new ConcurrentReferenceHashMap<>(16, ConcurrentReferenceHashMap.ReferenceType.SOFT);

    private Constructors() {
    }

    // -------------------- cache start --------------------

    /**
     * 获取无参构造方法
     *
     * @param clazz class
     * @return constructor
     */
    public static <T> Constructor<T> getDefaultConstructorByCache(Class<T> clazz) {
        Constructor<?> constructor = CLASS_DEFAULT_CONSTRUCTOR_CACHE.get(clazz);
        if (constructor == null) {
            CLASS_DEFAULT_CONSTRUCTOR_CACHE.put(clazz, constructor = getDefaultConstructor(clazz));
        }
        return (Constructor<T>) constructor;
    }

    // -------------------- cache end --------------------

    /**
     * 获取默认构造方法
     *
     * @param clazz class
     * @return 构造方法
     */
    public static <T> Constructor<T> getDefaultConstructor(Class<T> clazz) {
        Valid.notNull(clazz, "class is null");
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取构造方法
     *
     * @param clazz          class
     * @param parameterTypes 参数类型
     * @param <T>            ignore
     * @return 构造方法
     */
    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) {
        Valid.notNull(clazz, "class is null");
        if (Arrays1.length(parameterTypes) == 0) {
            return getDefaultConstructor(clazz);
        }
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取构造方法
     *
     * @param clazz class
     * @param len   参数长度
     * @param <T>   ignore
     * @return 构造方法
     */
    public static <T> Constructor<T> getConstructor(Class<T> clazz, int len) {
        Valid.notNull(clazz, "class is null");
        if (len == 0) {
            return getDefaultConstructor(clazz);
        }
        try {
            Constructor<?>[] constructors = clazz.getConstructors();
            for (Constructor<?> constructor : constructors) {
                if (constructor.getParameterTypes().length == len) {
                    constructor.setAccessible(true);
                    return (Constructor<T>) constructor;
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    /**
     * 获取构造方法
     *
     * @param clazz class
     * @param len   参数长度
     * @param <T>   ignore
     * @return 构造方法
     */
    public static <T> List<Constructor<T>> getConstructors(Class<T> clazz, int len) {
        Valid.notNull(clazz, "class is null");
        List<Constructor<T>> list = new ArrayList<>();
        if (len == 0) {
            list.add(getDefaultConstructor(clazz));
            return list;
        }
        try {
            Constructor<?>[] constructors = clazz.getConstructors();
            for (Constructor<?> constructor : constructors) {
                if (constructor.getParameterTypes().length == len) {
                    constructor.setAccessible(true);
                    list.add((Constructor<T>) constructor);
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return list;
    }

    /**
     * 获取构造方法
     *
     * @param clazz class
     * @param <T>   ignore
     * @return 构造方法
     */
    public static <T> List<Constructor<T>> getConstructors(Class<T> clazz) {
        Valid.notNull(clazz, "class is null");
        List<Constructor<T>> list = new ArrayList<>();
        try {
            Constructor<?>[] constructors = clazz.getConstructors();
            for (Constructor<?> constructor : constructors) {
                constructor.setAccessible(true);
                list.add((Constructor<T>) constructor);
            }
        } catch (Exception e) {
            // ignore
        }
        return list;
    }

    /**
     * 设置构造方法可访问
     */
    public static void setAccessible(Constructor<?> constructor) {
        Valid.notNull(constructor, "set accessible constructor class is null");
        if ((!Modifier.isPublic(constructor.getModifiers()) || !Modifier.isPublic(constructor.getDeclaringClass().getModifiers())) && !constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
    }

    /**
     * 实例化对象
     *
     * @param constructor constructor
     * @param <T>         类实例型
     * @return 实例
     */
    public static <T> T newInstance(Constructor<T> constructor) {
        Valid.notNull(constructor, "constructor is null");
        try {
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw Exceptions.invoke("cannot initialize class", e);
        }
    }

    /**
     * 实例化对象
     *
     * @param constructor constructor
     * @param values      参数
     * @param <T>         类实例型
     * @return 实例
     */
    public static <T> T newInstance(Constructor<T> constructor, Object... values) {
        Valid.notNull(constructor, "constructor is null");
        try {
            constructor.setAccessible(true);
            return constructor.newInstance(values);
        } catch (Exception e) {
            throw Exceptions.invoke("cannot initialize class", e);
        }
    }

    /**
     * 实例化对象
     *
     * @param clazz 需要实例化的对象
     * @param <T>   类实例型
     * @return 实例
     */
    public static <T> T newInstance(Class<T> clazz) {
        Valid.notNull(clazz, "class is null");
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw Exceptions.invoke(Strings.format("cannot initialize class: {}", clazz.getName()), e);
        }
    }

    /**
     * 实例化对象
     *
     * @param clazz          需要实例化的对象
     * @param parameterTypes 参数类型
     * @param values         参数
     * @param <T>            类实例型
     * @return 实例
     */
    public static <T> T newInstance(Class<T> clazz, Class<?>[] parameterTypes, Object... values) {
        Valid.notNull(clazz, "class is null");
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor.newInstance(values);
        } catch (Exception e) {
            throw Exceptions.invoke(Strings.format("cannot initialize class: {}, parameterTypes: {}, values: {}", clazz.getName(), Arrays.toString(parameterTypes), Arrays.toString(values)), e);
        }
    }

    /**
     * 实例化对象 参数类型推断
     *
     * @param constructor constructor
     * @param <T>         类实例型
     * @return 实例
     */
    public static <T> T newInstanceInfer(Constructor<T> constructor, Object... args) {
        Valid.notNull(constructor, "constructor is null");
        if (Arrays1.isEmpty(args)) {
            return newInstance(constructor);
        }
        return TypeInfer.newInstanceInfer(Lists.singleton(constructor), args);
    }

    /**
     * 实例化对象 参数类型推断
     *
     * @param clazz 需要实例化的对象
     * @param <T>   类实例型
     * @return 实例
     */
    public static <T> T newInstanceInfer(Class<T> clazz, Object... args) {
        Valid.notNull(clazz, "class is null");
        if (Arrays1.isEmpty(args)) {
            return newInstance(clazz);
        }
        List<Constructor<T>> constructors = getConstructors(clazz, args.length);
        return TypeInfer.newInstanceInfer(constructors, args);
    }

}
