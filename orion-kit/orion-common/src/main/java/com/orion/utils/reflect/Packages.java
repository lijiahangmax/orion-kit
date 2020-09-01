package com.orion.utils.reflect;

/**
 * 反射 包工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/5/15 13:19
 */
@SuppressWarnings("ALL")
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
        return o == null ? "" : getPackageName(o.getClass().getName());
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
        return (lastIndex != -1 ? className.substring(0, lastIndex) : "");
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
