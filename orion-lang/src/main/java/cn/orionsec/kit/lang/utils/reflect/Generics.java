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

import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.reflect.type.TypeReference;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 反射 泛型工具类
 * <p>
 * 非泛型类型返回 null 而非 Object
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/8/31 11:58
 */
public class Generics {

    private Generics() {
    }

    // -------------------- field --------------------

    /**
     * 获取字段泛型
     *
     * @param field        field
     * @param genericIndex generic type genericIndex
     * @return 泛型类型 没有则返回 null
     */
    public static Class<?> getFieldGenericType(Field field, int genericIndex) {
        Class<?>[] types = Types.getTypeParameterizedTypes(field.getGenericType());
        if (types == null) {
            return null;
        }
        return Valid.validIndex(types, genericIndex);
    }

    /**
     * 获取字段泛型列表
     *
     * @param field field
     * @return 泛型类型列表 没有则返回 null
     */
    public static Class<?>[] getFieldGenericTypes(Field field) {
        return Types.getTypeParameterizedTypes(field.getGenericType());
    }

    // -------------------- method parameter --------------------

    /**
     * 获取方法参数泛型
     *
     * @param method         method
     * @param parameterIndex parameterIndex
     * @param genericIndex   genericIndex
     * @return 泛型类型 没有则返回 null
     */
    public static Class<?> getMethodParameterGenericType(Method method, int parameterIndex, int genericIndex) {
        Class<?>[] types = Types.getTypeParameterizedTypes(method.getGenericParameterTypes()[parameterIndex]);
        if (types == null) {
            return null;
        }
        return Valid.validIndex(types, genericIndex);
    }

    /**
     * 获取方法参数泛型列表
     *
     * @param method         method
     * @param parameterIndex index
     * @return 泛型列表 没有则返回 null
     */
    public static Class<?>[] getMethodParameterGenericTypes(Method method, int parameterIndex) {
        return Types.getTypeParameterizedTypes(method.getGenericParameterTypes()[parameterIndex]);
    }

    /**
     * 获取方法参数泛型列表
     *
     * @param method method
     * @return 泛型列表 没有则返回 null
     */
    public static Class<?>[][] getMethodParameterGenericTypes(Method method) {
        return Arrays.stream(method.getGenericParameterTypes())
                .map(Types::getTypeParameterizedTypes)
                .toArray(Class<?>[][]::new);
    }

    // -------------------- method return --------------------

    /**
     * 获取字段泛型
     *
     * @param method       method
     * @param genericIndex genericIndex
     * @return 泛型类型 没有则返回 null
     */
    public static Class<?> getMethodReturnGenericType(Method method, int genericIndex) {
        Class<?>[] types = Types.getTypeParameterizedTypes(method.getGenericReturnType());
        if (types == null) {
            return null;
        }
        return Valid.validIndex(types, genericIndex);
    }

    /**
     * 获取字段泛型列表
     *
     * @param method method
     * @return 泛型类型列表 没有则返回 null
     */
    public static Class<?>[] getMethodReturnGenericTypes(Method method) {
        return Types.getTypeParameterizedTypes(method.getGenericReturnType());
    }

    // -------------------- class --------------------

    /**
     * 获取类类型泛型
     *
     * @param ref          ref
     * @param genericIndex genericIndex
     * @return 泛型类型 没有则返回 null
     */
    public static Class<?> getClassGenericType(TypeReference<?> ref, int genericIndex) {
        Class<?>[] types = Types.getTypeParameterizedTypes(ref.getType());
        if (types == null) {
            return null;
        }
        return Valid.validIndex(types, genericIndex);
    }

    /**
     * 获取类类型泛型列表
     *
     * @param ref ref
     * @return 泛型类型列表 没有则返回 null
     */
    public static Class<?>[] getClassGenericTypes(TypeReference<?> ref) {
        return Types.getTypeParameterizedTypes(ref.getType());
    }

    // -------------------- super class --------------------

    /**
     * 获取父类泛型
     *
     * @param clazz        clazz
     * @param genericIndex genericIndex
     * @return 泛型类型 没有则返回 null
     */
    public static Class<?> getSuperClassGenericType(Class<?> clazz, int genericIndex) {
        Class<?>[] types = Types.getTypeParameterizedTypes(clazz.getGenericSuperclass());
        if (types == null) {
            return null;
        }
        return Valid.validIndex(types, genericIndex);
    }

    /**
     * 获取父类泛型列表
     *
     * @param clazz clazz
     * @return 泛型类型列表 没有则返回 null
     */
    public static Class<?>[] getSuperClassGenericTypes(Class<?> clazz) {
        return Types.getTypeParameterizedTypes(clazz.getGenericSuperclass());
    }

    // -------------------- interface --------------------

    /**
     * 获取接口泛型
     *
     * @param clazz          clazz
     * @param interfaceClass 接口类型
     * @param genericIndex   genericIndex
     * @return 泛型类型 没有则返回 null
     */
    public static Class<?> getInterfaceGenericType(Class<?> clazz, Class<?> interfaceClass, int genericIndex) {
        Class<?>[] types = getInterfaceGenericTypes(clazz, interfaceClass);
        if (types == null) {
            return null;
        }
        return Valid.validIndex(types, genericIndex);
    }

    /**
     * 获取接口泛型列表
     *
     * @param clazz          clazz
     * @param interfaceClass 接口类型
     * @return 泛型类型列表 没有则返回 null
     */
    public static Class<?>[] getInterfaceGenericTypes(Class<?> clazz, Class<?> interfaceClass) {
        for (Type type : clazz.getGenericInterfaces()) {
            if (interfaceClass.equals(type)) {
                // 相等则代表非泛型类型
                break;
            }
            if (type instanceof ParameterizedType) {
                // 实际类型相等
                if (interfaceClass.equals(((ParameterizedType) type).getRawType())) {
                    return Types.getTypeParameterizedTypes(type);
                }
            }
        }
        return null;
    }

    /**
     * 获取接口泛型列表
     *
     * @param clazz clazz
     * @return 泛型类型列表 没有则 value 为 null
     */
    public static Map<Class<?>, Class<?>[]> getInterfaceGenericTypes(Class<?> clazz) {
        Map<Class<?>, Class<?>[]> map = new LinkedHashMap<>();
        for (Type type : clazz.getGenericInterfaces()) {
            if (type instanceof ParameterizedType) {
                Type rawClass = ((ParameterizedType) type).getRawType();
                map.put((Class<?>) rawClass, Types.getTypeParameterizedTypes(type));
            }
        }
        return map;
    }

}
