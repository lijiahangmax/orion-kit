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
package com.orion.lang.utils.reflect;

import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Maps;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Map;

/**
 * 反射 类型工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/12/7 18:12
 */
public class Types {

    private Types() {
    }

    /**
     * 获取类型的泛型类型
     * <p>
     * 如果有返回数组
     * 如果没有返回 null
     *
     * @param type 类型
     * @return 泛型数组
     */
    public static Class<?>[] getTypeParameterizedTypes(Type type) {
        if (type instanceof ParameterizedType) {
            Type[] arguments = ((ParameterizedType) type).getActualTypeArguments();
            int argumentsLength = arguments.length;
            Class<?>[] classes = new Class[argumentsLength];
            for (int i = 0; i < argumentsLength; i++) {
                Type argument = arguments[i];
                if (argument instanceof ParameterizedType) {
                    classes[i] = ((Class<?>) ((ParameterizedType) argument).getRawType());
                } else if (argument instanceof Class) {
                    classes[i] = (Class<?>) argument;
                } else {
                    classes[i] = Object.class;
                }
            }
            return classes;
        } else {
            return null;
        }
    }

    /**
     * 类型装换为泛型类型
     *
     * @param type type
     * @return ParameterizedType
     */
    public static ParameterizedType toParameterizedType(Type type) {
        if (type instanceof ParameterizedType) {
            return (ParameterizedType) type;
        } else if (type instanceof Class) {
            Class<?> clazz = (Class<?>) type;
            Type genericSuper = clazz.getGenericSuperclass();
            if (genericSuper == null || Object.class.equals(genericSuper)) {
                final Type[] genericInterfaces = clazz.getGenericInterfaces();
                if (Arrays1.isNotEmpty(genericInterfaces)) {
                    genericSuper = genericInterfaces[0];
                }
            }
            return toParameterizedType(genericSuper);
        }
        return null;
    }

    /**
     * 是否未知类型
     *
     * @param type type
     * @return 是否未知类型
     */
    public static boolean isUnknown(Type type) {
        return type == null || type instanceof TypeVariable;
    }

    /**
     * 获取 type 对应的原始 class
     *
     * @param type type
     * @return 原始 class
     */
    public static Class<?> getTypeRawClass(Type type) {
        if (type != null) {
            if (type instanceof Class) {
                return (Class<?>) type;
            } else if (type instanceof ParameterizedType) {
                return (Class<?>) ((ParameterizedType) type).getRawType();
            } else if (type instanceof TypeVariable) {
                return (Class<?>) ((TypeVariable<?>) type).getBounds()[0];
            } else if (type instanceof WildcardType) {
                final Type[] upperBounds = ((WildcardType) type).getUpperBounds();
                if (upperBounds.length == 1) {
                    return getTypeRawClass(upperBounds[0]);
                }
            }
        }
        return null;
    }

    public static Type getTypeArgument(Type type) {
        return getTypeArgument(type, 0);
    }

    public static Type getTypeArgument(Type type, int index) {
        Type[] typeArguments = getTypeArguments(type);
        if (typeArguments != null && typeArguments.length > index) {
            return typeArguments[index];
        }
        return null;
    }

    /**
     * 获取类型参数
     *
     * @param type type
     * @return type
     */
    public static Type[] getTypeArguments(Type type) {
        // 获取参数类型
        ParameterizedType pt = toParameterizedType(type);
        if (pt == null) {
            return null;
        }
        return pt.getActualTypeArguments();
    }

    public static Type getActualType(Type actualType, Class<?> typeDefineClass, Type typeVariable) {
        Type[] types = getActualTypes(actualType, typeDefineClass, typeVariable);
        if (Arrays1.isNotEmpty(types)) {
            return types[0];
        }
        return null;
    }

    /**
     * 获取实际泛型类型
     *
     * @param actualType      实际类型 ArrayList
     * @param typeDefineClass 父类类型 List
     * @param typeVariables   参数类型 String
     * @return 泛型类型
     */
    public static Type[] getActualTypes(Type actualType, Class<?> typeDefineClass, Type... typeVariables) {
        if (!typeDefineClass.isAssignableFrom(getTypeRawClass(actualType))) {
            throw Exceptions.argument(Strings.format("parameter {} must be assignable from {}", typeDefineClass, actualType));
        }
        TypeVariable<?>[] typeVars = typeDefineClass.getTypeParameters();
        if (Arrays1.isEmpty(typeVars)) {
            return null;
        }
        Type[] actualTypeArguments = getTypeArguments(actualType);
        if (Arrays1.isEmpty(actualTypeArguments)) {
            return null;
        }
        int size = Math.min(typeVars.length, actualTypeArguments.length);
        Map<Type, Type> tableMap = Maps.of(typeVars, actualTypeArguments);
        Type[] result = new Type[size];
        for (int i = 0; i < typeVariables.length; i++) {
            result[i] = typeVariables[i] instanceof TypeVariable ? tableMap.get(typeVariables[i]) : typeVariables[i];
        }
        return result;
    }

}
