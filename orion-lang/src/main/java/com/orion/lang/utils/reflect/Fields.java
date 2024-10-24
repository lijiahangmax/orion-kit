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

import com.orion.lang.constant.Const;
import com.orion.lang.define.collect.ConcurrentReferenceHashMap;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.convert.TypeStore;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toMap;

/**
 * 反射 字段工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/15 13:16
 */
@SuppressWarnings("ALL")
public class Fields {

    private static final Map<Class<?>, List<Field>> FIELD_CACHE = new ConcurrentReferenceHashMap<>(Const.CAPACITY_16, ConcurrentReferenceHashMap.ReferenceType.SOFT);

    private Fields() {
    }

    // -------------------- cache start --------------------

    public static List<Field> getFieldsByCache(Class<?> clazz) {
        List<Field> fields = FIELD_CACHE.get(clazz);
        if (fields == null) {
            FIELD_CACHE.put(clazz, fields = getFields(clazz));
        }
        return fields;
    }

    public static Field getFieldByCache(Class<?> clazz, String fieldName) {
        List<Field> fields = FIELD_CACHE.get(clazz);
        if (fields == null) {
            FIELD_CACHE.put(clazz, fields = getFields(clazz));
        }
        if (fields == null) {
            return null;
        }
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }

    // -------------------- cache end --------------------

    /**
     * 通过方法获取字段名 仅限于 getter setter
     *
     * @param method 方法
     * @return 字段名称
     */
    public static String getFieldNameByMethod(Method method) {
        if (method == null) {
            return null;
        }
        return getFieldNameByMethod(method.getName());
    }

    /**
     * 通过方法名获取字段名 仅限于 getter setter
     *
     * @param methodName 方法名称
     * @return 字段名称
     */
    public static String getFieldNameByMethod(String methodName) {
        if (Strings.isBlank(methodName)) {
            return null;
        }
        methodName = methodName.trim();
        String fieldName = null;
        int length = methodName.length();
        if (methodName.startsWith(Methods.GETTER_PREFIX) || methodName.startsWith(Methods.SETTER_PREFIX)) {
            if (length != 3) {
                fieldName = methodName.substring(3, length);
            }
        } else if (methodName.startsWith(Methods.BOOLEAN_GETTER_PREFIX)) {
            if (length != 2) {
                fieldName = methodName.substring(2, length);
            }
        }
        if (fieldName != null) {
            return Strings.firstLower(fieldName);
        }
        return null;
    }

    /**
     * 通过方法获取字段 仅限于 getter setter
     *
     * @param methodClass 方法类
     * @param method      方法
     * @return 字段
     */
    public static Field getFieldByMethod(Class<?> methodClass, Method method) {
        return getFieldByMethod(methodClass, method.getName());
    }

    /**
     * 通过方法获取字段 仅限于 getter setter
     *
     * @param methodClass 方法类
     * @param methodName  方法名称
     * @return 字段
     */
    public static Field getFieldByMethod(Class<?> methodClass, String methodName) {
        String fieldName = getFieldNameByMethod(methodName);
        if (fieldName != null) {
            return getAccessibleField(methodClass, fieldName);
        }
        return null;
    }

    /**
     * 直接读取对象属性值
     *
     * @param obj       对象
     * @param fieldName 字段名称
     * @param <E>       属性类型
     * @return 对象
     */
    public static <E> E getFieldValue(Object obj, String fieldName) {
        Valid.notNull(obj, "invoker object is null");
        Valid.notBlank(fieldName, "invoke field is null");
        Field field = getAccessibleField(obj.getClass(), fieldName);
        if (field == null) {
            throw Exceptions.invoke(Strings.format("get field value not found field: {}, class {}", fieldName, obj.getClass().getName()));
        }
        return getFieldValue(obj, field);
    }

    /**
     * 直接读取对象属性值
     *
     * @param obj   对象
     * @param field 字段
     * @param <E>   属性类型
     * @return 对象
     */
    public static <E> E getFieldValue(Object obj, Field field) {
        Valid.notNull(obj, "invoker object is null");
        Valid.notNull(field, "invoke field is null");
        try {
            setAccessible(field);
            return (E) field.get(obj);
        } catch (Exception e) {
            throw Exceptions.invoke(Strings.format("get field value error: {}, field: {}, class: {}", e.getMessage(), field.getName(), obj.getClass().getName()), e);
        }
    }

    /**
     * 直接设置对象属性值 可用于基本类型字段
     *
     * @param obj       对象
     * @param fieldName 字段名称
     * @param value     对象值
     * @param <E>       属性类型
     */
    public static <E> void setFieldValue(Object obj, String fieldName, E value) {
        Valid.notNull(obj, "invoker object is null");
        Valid.notBlank(fieldName, "invoke field is null");
        Field field = getAccessibleField(obj.getClass(), fieldName);
        if (field == null) {
            throw Exceptions.invoke(Strings.format("set field value not found field: {}, class {}", fieldName, obj.getClass().getName()));
        }
        setFieldValue(obj, field, value);
    }

    /**
     * 直接设置对象属性值 可用于基本类型字段
     *
     * @param obj   对象
     * @param field 字段
     * @param value 对象值
     * @param <E>   属性类型
     */
    public static <E> void setFieldValue(Object obj, Field field, E value) {
        Valid.notNull(obj, "invoker object is null");
        Valid.notNull(field, "invoke field is null");
        try {
            setAccessible(field);
            field.set(obj, value);
        } catch (Exception e) {
            throw Exceptions.invoke(Strings.format("set field value error: {}, field: {}, class: {}", e.getMessage(), field.getName(), obj.getClass().getName(), value), e);
        }
    }

    /**
     * 直接设置对象属性值 类型推断 可用于基本类型字段
     *
     * @param obj       对象
     * @param fieldName 字段
     * @param value     对象值
     * @param <E>       属性类型
     */
    public static <E> void setFieldValueInfer(Object obj, String fieldName, E value) {
        Valid.notNull(obj, "invoker object is null");
        Valid.notNull(fieldName, "invoke field is null");
        Field field = getAccessibleField(obj.getClass(), fieldName);
        if (field == null) {
            throw Exceptions.invoke(Strings.format("set field value not found field: {}, class {}", fieldName, obj.getClass().getName()));
        }
        setFieldValueInfer(obj, field, value);
    }

    /**
     * 直接设置对象属性值 类型推断 可用于基本类型字段
     *
     * @param obj   对象
     * @param field 字段
     * @param value 对象值
     * @param <E>   属性类型
     */
    public static <E> void setFieldValueInfer(Object obj, Field field, E value) {
        Valid.notNull(obj, "invoker object is null");
        Valid.notNull(field, "invoke field is null");
        try {
            setAccessible(field);
            if (TypeStore.canConvert(value.getClass(), field.getType())) {
                field.set(obj, TypeStore.STORE.to(value, field.getType()));
                return;
            }
            throw Exceptions.invoke(Strings.format("could infer set field value, field: {}, class: {}", field.getName(), obj.getClass().getName()));
        } catch (Exception e) {
            throw Exceptions.invoke(Strings.format("set field value error: {}, field: {}, class: {}", e.getMessage(), field.getName(), obj.getClass().getName()), e);
        }
    }

    /**
     * 获取该类的所有属性列表
     *
     * @param clazz 反射类
     */
    public static Map<String, Field> getFieldMap(Class<?> clazz) {
        List<Field> fieldList = getFields(clazz);
        return Lists.isNotEmpty(fieldList) ? fieldList.stream().collect(Collectors.toMap(Field::getName, field -> field)) : Collections.emptyMap();
    }

    /**
     * 获取该类的所有属性列表 包括父类 不包括 static transient
     *
     * @param clazz 反射类
     * @return 属性
     */
    public static List<Field> getFields(Class<?> clazz) {
        Valid.notNull(clazz, "field class is null");
        if (clazz.getSuperclass() != null) {
            List<Field> fieldList = Stream.of(clazz.getDeclaredFields())
                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
                    .filter(field -> !Modifier.isTransient(field.getModifiers()))
                    .collect(Collectors.toList());
            Class<?> superClass = clazz.getSuperclass();
            // 当前类属性
            Map<String, Field> fieldMap = fieldList.stream().collect(toMap(Field::getName, identity()));
            // 父类属性
            getFields(superClass).stream().filter(field -> !fieldMap.containsKey(field.getName())).forEach(fieldList::add);
            return fieldList;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 获取所有static的属性
     *
     * @param clazz 类
     * @return static属性
     */
    public static List<Field> getStaticFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        return Arrays.stream(fields)
                .filter(f -> Modifier.isStatic(f.getModifiers()))
                .filter(f -> !Modifier.isTransient(f.getModifiers()))
                .collect(toCollection(ArrayList::new));
    }

    /**
     * 获取对象的DeclaredField, 并强制设置为可访问
     *
     * @param clazz     class
     * @param fieldName 字段名称
     * @return 字段对象
     */
    public static Field getAccessibleField(Class<?> clazz, String fieldName) {
        Valid.notNull(clazz, "field class is null");
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                setAccessible(field);
                return field;
            } catch (NoSuchFieldException e) {
                // ignore
            }
        }
        return null;
    }

    /**
     * 设置属性可访问
     *
     * @param field 属性
     */
    public static void setAccessible(Field field) {
        Valid.notNull(field, "set accessible field class is null");
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

}
