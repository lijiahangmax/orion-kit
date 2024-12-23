/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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

import cn.orionsec.kit.lang.utils.Exceptions;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

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
     * <p>
     * V	    void
     * Z	    boolean
     * B	    byte
     * C	    char
     * S	    short
     * I	    int
     * J	    long
     * F	    float
     * D	    double
     * [	    以[开头 配合其他的特殊字符表示对应数据类型的数组 几个[表示几维数组
     * L全类名;	引用类型	以L开头 ;结尾 中间是引用类型的全类名用 /替换.
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
        throw Exceptions.argument("type: " + type.getCanonicalName() + " is not a primitive type");
    }

    /**
     * 检查基本类型泛型数组
     *
     * @param genericArrayType 泛型数组类型
     * @return type
     */
    public static Type checkPrimitiveArray(GenericArrayType genericArrayType) {
        Type clazz = genericArrayType;
        Type genericComponentType = genericArrayType.getGenericComponentType();
        String prefix = "[";
        while (genericComponentType instanceof GenericArrayType) {
            genericComponentType = ((GenericArrayType) genericComponentType).getGenericComponentType();
            prefix += prefix;
        }
        if (genericComponentType instanceof Class<?>) {
            Class<?> ck = (Class<?>) genericComponentType;
            if (ck.isPrimitive()) {
                try {
                    if (ck == boolean.class) {
                        clazz = Class.forName(prefix + "Z");
                    } else if (ck == char.class) {
                        clazz = Class.forName(prefix + "C");
                    } else if (ck == byte.class) {
                        clazz = Class.forName(prefix + "B");
                    } else if (ck == short.class) {
                        clazz = Class.forName(prefix + "S");
                    } else if (ck == int.class) {
                        clazz = Class.forName(prefix + "I");
                    } else if (ck == long.class) {
                        clazz = Class.forName(prefix + "J");
                    } else if (ck == float.class) {
                        clazz = Class.forName(prefix + "F");
                    } else if (ck == double.class) {
                        clazz = Class.forName(prefix + "D");
                    }
                } catch (ClassNotFoundException e) {
                    // ignore
                }
            }
        }
        return clazz;
    }

}
