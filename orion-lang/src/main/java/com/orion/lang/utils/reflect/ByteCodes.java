package com.orion.lang.utils.reflect;

import java.lang.reflect.Method;

/**
 * 反射 字节码工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/3/10 12:45
 */
public class ByteCodes {

    private static final String VM_NAME = "java.vm.name";

    private ByteCodes() {
    }

    /**
     * 当前环境是否为 android 环境
     *
     * @return is android
     */
    public static boolean isAndroid() {
        return isAndroid(System.getProperty(VM_NAME));
    }

    /**
     * 检查环境是否为 android 环境
     *
     * @param vm vm
     * @return is android
     */
    public static boolean isAndroid(String vm) {
        vm = vm.toLowerCase();
        return vm.contains("dalvik") || vm.contains("lemur");
    }

    /**
     * 获取 method 签名
     *
     * @param method method
     * @return 签名
     */
    public static String getMethodSignature(Method method) {
        StringBuilder buf = new StringBuilder();
        buf.append("(");
        Class<?>[] types = method.getParameterTypes();
        for (Class<?> type : types) {
            buf.append(getClassSignature(type));
        }
        buf.append(")");
        buf.append(getClassSignature(method.getReturnType()));
        return buf.toString();
    }

    /**
     * 获取 class 签名
     *
     * @param clazz class
     * @return 签名
     */
    public static String getClassSignature(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return getPrimitiveLetter(clazz);
        }
        if (clazz.isArray()) {
            return "[" + getClassSignature(clazz.getComponentType());
        }
        return "L" + getClassTypeName(clazz) + ";";
    }

    /**
     * 获取 class 类型名称
     *
     * @param clazz class
     * @return 签名
     */
    public static String getClassTypeName(Class<?> clazz) {
        if (clazz.isArray()) {
            return "[" + getClassSignature(clazz.getComponentType());
        }
        if (!clazz.isPrimitive()) {
            return clazz.getName().replaceAll("\\.", "/");
        }
        return getPrimitiveLetter(clazz);
    }

    /**
     * 获取基本类型字母
     *
     * @param type type
     * @return letter
     */
    public static String getPrimitiveLetter(Class<?> type) {
        if (Integer.TYPE.equals(type)) {
            return "I";
        } else if (Void.TYPE.equals(type)) {
            return "V";
        } else if (Boolean.TYPE.equals(type)) {
            return "Z";
        } else if (Character.TYPE.equals(type)) {
            return "C";
        } else if (Byte.TYPE.equals(type)) {
            return "B";
        } else if (Short.TYPE.equals(type)) {
            return "S";
        } else if (Float.TYPE.equals(type)) {
            return "F";
        } else if (Long.TYPE.equals(type)) {
            return "J";
        } else if (Double.TYPE.equals(type)) {
            return "D";
        }
        throw new IllegalStateException("type: " + type.getCanonicalName() + " is not a primitive type");
    }

}
