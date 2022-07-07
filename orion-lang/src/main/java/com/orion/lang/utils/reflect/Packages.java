package com.orion.lang.utils.reflect;

import com.orion.lang.utils.Strings;

/**
 * 反射 包工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/15 13:19
 */
public class Packages {

    private Packages() {
    }

    /**
     * 获取包名
     *
     * @param o ignore
     * @return ignore
     */
    public static String getPackageName(Object o) {
        return o == null ? Strings.EMPTY : getPackageName(o.getClass().getName());
    }

    /**
     * 获取包名
     *
     * @param clazz ignore
     * @return ignore
     */
    public static String getPackageName(Class<?> clazz) {
        return getPackageName(clazz.getName());
    }

    /**
     * 获取包名
     *
     * @param className ignore
     * @return ignore
     */
    public static String getPackageName(String className) {
        int lastIndex = className.lastIndexOf(".");
        return (lastIndex != -1 ? className.substring(0, lastIndex) : Strings.EMPTY);
    }

    /**
     * 获取包
     *
     * @param o ignore
     * @return ignore
     */
    public static Package getPackage(Object o) {
        return o == null ? null : getPackage(getPackageName(o.getClass().getName()));
    }

    /**
     * 获取包
     *
     * @param clazz ignore
     * @return ignore
     */
    public static Package getPackage(Class<?> clazz) {
        return getPackage(getPackageName(clazz.getName()));
    }

    /**
     * 获取包
     *
     * @param packageName ignore
     * @return ignore
     */
    public static Package getPackage(String packageName) {
        return Package.getPackage(packageName);
    }

}
