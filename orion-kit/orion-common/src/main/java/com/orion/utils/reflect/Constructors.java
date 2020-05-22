package com.orion.utils.reflect;

import com.orion.utils.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 反射 构造方法工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/15 13:17
 */
@SuppressWarnings("ALL")
public class Constructors {

    private Constructors() {
    }

    /**
     * 获取默认构造方法
     *
     * @param clazz class
     * @return 构造方法
     */
    public static <T> Constructor<T> getDefaultConstructor(Class<T> clazz) {
        Valid.notNull(clazz, "Class is null");
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
    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class[] parameterTypes) {
        Valid.notNull(clazz, "Class is null");
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
        Valid.notNull(clazz, "Class is null");
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
        Valid.notNull(clazz, "Class is null");
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
     * 实例化对象
     *
     * @param constructor constructor
     * @param <T>         类实例型
     * @return 实例
     */
    public static <T> T newInstance(Constructor<T> constructor) {
        Valid.notNull(constructor, "Constructor is null");
        try {
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw Exceptions.invoke("Cannot initialize class", e);
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
    public static <T> T newInstance(Constructor<T> constructor, Object[] values) {
        Valid.notNull(constructor, "Constructor is null");
        try {
            constructor.setAccessible(true);
            return constructor.newInstance(values);
        } catch (Exception e) {
            throw Exceptions.invoke("Cannot initialize class", e);
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
        Valid.notNull(clazz, "Class is null");
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw Exceptions.invoke(Strings.format("Cannot initialize class: {}", clazz.getName()), e);
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
    public static <T> T newInstance(Class<T> clazz, Class[] parameterTypes, Object[] values) {
        Valid.notNull(clazz, "Class is null");
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor.newInstance(values);
        } catch (Exception e) {
            throw Exceptions.invoke(Strings.format("Cannot initialize class: {}, parameterTypes: {}, values: {}", clazz.getName(), Arrays.toString(parameterTypes), Arrays.toString(values)), e);
        }
    }

    /**
     * 实例化对象 参数类型推断, 不推荐使用到多个构造方法参数列表长度相同的类
     *
     * @param clazz 需要实例化的对象
     * @param <T>   类实例型
     * @return 实例
     */
    public static <T> T newInstanceInfer(Class<T> clazz, Object[] args) {
        Valid.notNull(clazz, "Class is null");
        if (args == null || args.length == 0) {
            return newInstance(clazz);
        }
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            try {
                return (T) newInstanceInfers(constructor, Objects1.clone(args));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw Exceptions.invoke(Strings.format("Cannot initialize class: {}, values: {}", clazz.getName(), Arrays.toString(args)), e);
            } catch (Exception e) {
                // ignore
            }
        }
        throw Exceptions.invoke(Strings.format("Cannot initialize class not found constructor: {}, values: {}", clazz.getName(), Arrays.toString(args)));
    }

    /**
     * 实例化对象 参数类型推断, 不推荐使用到多个构造方法参数列表长度相同的类
     *
     * @param constructor constructor
     * @param <T>         类实例型
     * @return 实例
     */
    public static <T> T newInstanceInfer(Constructor<T> constructor, Object[] args) {
        try {
            return newInstanceInfers(constructor, args);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw Exceptions.invoke(Strings.format("Cannot initialize class values: {}", Arrays.toString(args)));
        }
    }

    /**
     * 实例化对象 参数类型推断, 不推荐使用到多个构造方法参数列表长度相同的类
     *
     * @param constructor constructor
     * @param <T>         类实例型
     * @return 实例
     */
    private static <T> T newInstanceInfers(Constructor<T> constructor, Object[] args) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?>[] params = constructor.getParameterTypes();
        int len = Arrays1.length(args);
        if (params.length != len) {
            throw Exceptions.invoke(Strings.format("Constructor parameters len is {}, but args length is {}", params.length, len));
        }
        for (int i = 0; i < len; i++) {
            if (args[i] != null) {
                if (!params[i].equals(Object.class) && params[i] != args[i].getClass() && !Classes.getInterfaces(args[i].getClass()).contains(params[i])) {
                    args[i] = TypeInfer.convert(args[i], params[i]);
                }
            }
        }
        constructor.setAccessible(true);
        return constructor.newInstance(args);
    }

}
