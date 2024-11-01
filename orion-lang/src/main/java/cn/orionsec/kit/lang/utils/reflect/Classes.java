/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package cn.orionsec.kit.lang.utils.reflect;

import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.collect.Lists;

import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 反射 类工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/15 13:21
 */
@SuppressWarnings("ALL")
public class Classes {

    /**
     * 代理 class 的名称
     */
    private static final List<String> PROXY_CLASS_NAMES = new ArrayList<>();

    /**
     * 当前类加载器
     */
    private static final ClassLoader CURRENT_CLASS_LOADER;

    /**
     * cglib动态代理类
     */
    public static final String CGLIB_CLASS_SEPARATOR = "$$";

    /**
     * 基本类型的class
     */
    private static final Class<?>[] BASE_CLASS = new Class<?>[]{Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Boolean.TYPE, Character.TYPE};

    /**
     * 包装类型的class
     */
    private static final Class<?>[] WRAP_CLASS = new Class<?>[]{Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class, Character.class};

    /**
     * 基本类型数组的class
     */
    private static final Class<?>[] BASE_ARRAY_CLASS = new Class<?>[]{byte[].class, short[].class, int[].class, long[].class, float[].class, double[].class, boolean[].class, char[].class};

    /**
     * 包装类型数组的class
     */
    private static final Class<?>[] WRAP_ARRAY_CLASS = new Class<?>[]{Byte[].class, Short[].class, Integer[].class, Long[].class, Float[].class, Double[].class, Boolean[].class, Character[].class};

    static {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader != null) {
            CURRENT_CLASS_LOADER = contextClassLoader;
        } else {
            CURRENT_CLASS_LOADER = Classes.class.getClassLoader();
        }
        PROXY_CLASS_NAMES.add("net.sf.cglib.proxy.Factory");
        PROXY_CLASS_NAMES.add("org.springframework.cglib.proxy.Factory");
        PROXY_CLASS_NAMES.add("javassist.util.proxy.ProxyObject");
        PROXY_CLASS_NAMES.add("org.apache.ibatis.javassist.util.proxy.ProxyObject");
    }

    private Classes() {
    }

    /**
     * 获取类加载器
     *
     * @return 类加载器
     */
    public static ClassLoader getCurrentClassLoader() {
        return CURRENT_CLASS_LOADER;
    }

    /**
     * 加载类
     *
     * @param className 类名
     * @return 类
     */
    public static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 加载类
     *
     * @param className 类名
     * @param init      是否初始化类
     * @return 类
     */
    public static Class<?> loadClass(String className, boolean init) {
        try {
            return Class.forName(className, init, CURRENT_CLASS_LOADER);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 加载类
     *
     * @param className   类名
     * @param init        是否初始化类
     * @param classLoader classLoader
     * @return 类
     */
    public static Class<?> loadClass(String className, boolean init, ClassLoader classLoader) {
        try {
            return Class.forName(className, init, classLoader);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断是否为代理对象
     *
     * @param clazz 传入 class 对象
     * @return 如果对象class是代理 class, 返回 true
     */
    public static boolean isProxy(Class<?> clazz) {
        if (clazz != null) {
            if (clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
                return true;
            }
            for (Class<?> cls : clazz.getInterfaces()) {
                if (PROXY_CLASS_NAMES.contains(cls.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否是jdk动态代理对象
     * 不会判断是否为 SpringProxy
     *
     * @param o ignore
     * @return true 是
     */
    public static boolean isJdkProxy(Object o) {
        return o != null && Proxy.isProxyClass(o.getClass());
    }

    /**
     * 是否是cglib动态代理对象
     *
     * @param o ignore
     * @return true 是
     */
    public static boolean isCglibProxy(Object o) {
        return o != null && o.getClass().toString().contains(CGLIB_CLASS_SEPARATOR);
    }

    /**
     * 获得对象的父类
     *
     * @param clazz class
     * @return 父类对象
     */
    public static Class<?> getSuperClass(Class<?> clazz) {
        Valid.notNull(clazz, "class is null");
        Class<?> superClass = clazz.getSuperclass();
        if (!clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            return superClass;
        } else {
            return Object.class;
        }
    }

    /**
     * 获得对象的所有父类, 不包括Object
     *
     * @param clazz class
     * @return 父类对象
     */
    public static List<Class<?>> getSuperClasses(Class<?> clazz) {
        Valid.notNull(clazz, "class is null");
        if (!clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            List<Class<?>> list = new ArrayList<>();
            for (Class<?> superClass = clazz.getSuperclass(); superClass != null && superClass != Object.class; superClass = superClass.getSuperclass()) {
                list.add(superClass);
            }
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 获取所有接口, 如果是接口, 则返回自己
     *
     * @param clazz 类
     * @return 接口
     */
    public static List<Class<?>> getInterfaces(Class<?> clazz) {
        Valid.notNull(clazz, "class is null");
        if (clazz.isInterface()) {
            return Lists.of(clazz);
        }
        Set<Class<?>> interfaces = new HashSet<>();
        while (clazz != null) {
            Class<?>[] ins = clazz.getInterfaces();
            for (Class<?> in : ins) {
                interfaces.addAll(getInterfaces(in));
            }
            clazz = clazz.getSuperclass();
        }
        return new ArrayList<>(interfaces);
    }

    /**
     * 判断是否是接口
     *
     * @param clazz class
     * @return true接口
     */
    public static boolean isInterface(Class<?> clazz) {
        Valid.notNull(clazz, "class is null");
        return Modifier.isInterface(clazz.getModifiers());
    }

    /**
     * 判断argClass是否为requireClass的实现类
     *
     * @param requireClass requireClass 父
     * @param argClass     argClass 子
     * @return true 是实现类或本类
     */
    public static boolean isImplClass(Class<?> requireClass, Class<?> argClass) {
        Valid.notNull(requireClass, "require class is null");
        Valid.notNull(argClass, "arg class is null");
        if (requireClass.equals(argClass) || requireClass.equals(Object.class)) {
            return true;
        }
        // List<Class<?>> list = getInterfaces(argClass);
        // if (list.isEmpty() || !list.contains(requireClass)) {
        //     list = getSuperClasses(argClass);
        // }
        // return list.contains(requireClass);
        return requireClass.isAssignableFrom(argClass);
    }

    /**
     * 判断对象是否是数组
     *
     * @param clazz class
     * @return true数组
     */
    public static boolean isArray(Class<?> clazz) {
        Valid.notNull(clazz, "class is null");
        return clazz.isArray();
    }

    /**
     * 判断是否为基本类型
     *
     * @param clazz class
     * @return 基本类型true
     */
    public static boolean isBaseClass(Class<?> clazz) {
        Valid.notNull(clazz, "class is null");
        for (Class<?> baseClass : BASE_CLASS) {
            if (clazz.equals(baseClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为包装类型
     *
     * @param clazz class
     * @return 包装类型true
     */
    public static boolean isWrapClass(Class<?> clazz) {
        Valid.notNull(clazz, "class is null");
        for (Class<?> wrapClass : WRAP_CLASS) {
            if (clazz.equals(wrapClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回基本类型的包装类型
     *
     * @param clazz class
     * @return class
     */
    public static Class<?> getWrapClass(Class<?> clazz) {
        Valid.notNull(clazz, "class is null");
        for (int i = 0; i < BASE_CLASS.length; i++) {
            if (clazz.equals(BASE_CLASS[i])) {
                return WRAP_CLASS[i];
            }
        }
        return clazz;
    }

    /**
     * 返回包装类型的基本类型
     *
     * @param clazz class
     * @return class
     */
    public static Class<?> getBaseClass(Class<?> clazz) {
        Valid.notNull(clazz, "class is null");
        for (int i = 0; i < WRAP_CLASS.length; i++) {
            if (clazz.equals(WRAP_CLASS[i])) {
                return BASE_CLASS[i];
            }
        }
        return clazz;
    }

    /**
     * 判断是否为基本数组类型
     *
     * @param clazz class
     * @return 基本数组类型true
     */
    public static boolean isBaseArrayClass(Class<?> clazz) {
        Valid.notNull(clazz, "class is null");
        for (Class<?> baseClass : BASE_ARRAY_CLASS) {
            if (clazz.equals(baseClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为包装数组类型
     *
     * @param clazz class
     * @return 包装数组类型true
     */
    public static boolean isWrapArrayClass(Class<?> clazz) {
        Valid.notNull(clazz, "class is null");
        for (Class<?> wrapClass : WRAP_ARRAY_CLASS) {
            if (clazz.equals(wrapClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回基本数组类型的包装数组类型
     *
     * @param clazz class
     * @return class
     */
    public static Class<?> getWrapArrayClass(Class<?> clazz) {
        Valid.notNull(clazz, "class is null");
        for (int i = 0; i < BASE_ARRAY_CLASS.length; i++) {
            if (clazz.equals(BASE_ARRAY_CLASS[i])) {
                return WRAP_ARRAY_CLASS[i];
            }
        }
        return clazz;
    }

    /**
     * 返回包装数组类型的基本数组类型
     *
     * @param clazz class
     * @return class
     */
    public static Class<?> getBaseArrayClass(Class<?> clazz) {
        Valid.notNull(clazz, "class is null");
        for (int i = 0; i < WRAP_ARRAY_CLASS.length; i++) {
            if (clazz.equals(WRAP_ARRAY_CLASS[i])) {
                return BASE_ARRAY_CLASS[i];
            }
        }
        return clazz;
    }

    /**
     * 判断class是否是数字类型
     *
     * @param clazz class
     * @return true number
     */
    public static boolean isNumberClass(Class<?> clazz) {
        Valid.notNull(clazz, "class is null");
        boolean direct = clazz == Byte.TYPE || clazz == Short.TYPE
                || clazz == Integer.TYPE || clazz == Long.TYPE
                || clazz == Float.TYPE || clazz == Double.TYPE
                || clazz == Number.class;
        if (direct) {
            return direct;
        }
        for (Class<?> superClass = clazz.getSuperclass(); superClass != null && superClass != Object.class; superClass = superClass.getSuperclass()) {
            if (superClass == Number.class) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断对象是否是数字类型
     *
     * @param o object
     * @return true number
     */
    public static boolean isNumberClass(Object o) {
        Valid.notNull(o, "object is null");
        return o instanceof Number;
    }

}
