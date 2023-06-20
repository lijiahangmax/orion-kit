package com.orion.lang.utils.reflect;

import com.orion.lang.function.IGetter;
import com.orion.lang.function.ISetter;
import com.orion.lang.utils.Exceptions;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 反射 lambda 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/3/10 11:04
 */
public class Lambdas {

    private static final String WRITE_REPLACE = "writeReplace";

    private Lambdas() {
    }

    /**
     * 获取序列化 lambda 对象
     *
     * @param serial serial
     * @return SerializedLambda
     */
    public static SerializedLambda getSerializedLambda(Serializable serial) {
        try {
            Method method = serial.getClass().getDeclaredMethod(WRITE_REPLACE);
            method.setAccessible(true);
            return (SerializedLambda) method.invoke(serial);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取 lambda 调用类名称
     *
     * @param lambda lambda
     * @return className
     */
    public static String getImplClassName(SerializedLambda lambda) {
        return lambda.getImplClass().replaceAll("/", ".");
    }

    /**
     * 获取 lambda 调用类
     *
     * @param lambda lambda
     * @param <T>    T
     * @return class
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getImplClass(SerializedLambda lambda) {
        return (Class<T>) Classes.loadClass(getImplClassName(lambda));
    }

    /**
     * 获取对象 getter 方法引用的字段名称
     *
     * @param getter getter
     * @param <T>    T
     * @param <R>    R
     * @return field
     */
    public static <T, R> String getGetterFieldName(IGetter<T, R> getter) {
        return getFieldName(getSerializedLambda(getter));
    }

    /**
     * 获取对象 setter 方法引用的字段名称
     *
     * @param setter getter
     * @param <T>    T
     * @param <U>    U
     * @return field
     */
    public static <T, U> String getSetterFieldName(ISetter<T, U> setter) {
        return getFieldName(getSerializedLambda(setter));
    }

    /**
     * 获取调用方法字段名称
     *
     * @param lambda lambda
     * @return fieldName
     */
    public static String getFieldName(SerializedLambda lambda) {
        return Fields.getFieldNameByMethodName(lambda.getImplMethodName());
    }

    /**
     * 获取字段
     *
     * @param lambda lambda
     * @return field
     */
    public static Field getField(SerializedLambda lambda) {
        // 获取 class
        Class<?> clazz = getImplClass(lambda);
        // 字段名称
        String fieldName = Fields.getFieldNameByMethodName(lambda.getImplMethodName());
        // 获取字段
        return Fields.getAccessibleField(clazz, fieldName);
    }

    /**
     * 获取调用方法名称
     *
     * @param lambda lambda
     * @return methodName
     */
    public static String getMethodName(SerializedLambda lambda) {
        return lambda.getImplMethodName();
    }

    /**
     * 获取调用方法
     *
     * @param lambda lambda
     * @return method
     */
    public static Method getMethod(SerializedLambda lambda) {
        // 获取 class
        Class<Object> clazz = getImplClass(lambda);
        String signature = lambda.getImplMethodSignature();
        // 获取方法
        List<Method> methods = Methods.getAccessibleMethods(clazz, lambda.getImplMethodName());
        for (Method method : methods) {
            if (signature.equals(ByteCodes.getMethodSignature(method))) {
                return method;
            }
        }
        throw Exceptions.runtime("no such method in class: " + clazz + " method signature: " + signature);
    }

}
