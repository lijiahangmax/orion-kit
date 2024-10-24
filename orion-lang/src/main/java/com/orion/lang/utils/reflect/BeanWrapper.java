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

import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Objects1;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.orion.lang.utils.reflect.Classes.isImplClass;

/**
 * Bean 工具类
 * 根据getter方法获取值和setter方法设置值
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/2 15:37
 */
@SuppressWarnings("unchecked")
public class BeanWrapper {

    /**
     * LOG
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BeanWrapper.class);

    private BeanWrapper() {
    }

    public static <T> Map<String, Object> toMap(T bean, String... ignoreFields) {
        return toMap(bean, null, false, ignoreFields);
    }

    public static <T> Map<String, Object> toMap(T bean, boolean putNull, String... ignoreFields) {
        return toMap(bean, null, putNull, ignoreFields);
    }

    public static <T> Map<String, Object> toMap(T bean, Map<String, String> fieldMapper, String... ignoreFields) {
        return toMap(bean, fieldMapper, false, ignoreFields);
    }

    /**
     * Bean -> Map
     *
     * @param bean         bean
     * @param fieldMapper  map key: beanField  value: mapKey
     * @param putNull      为空是否添加
     * @param ignoreFields 跳过的字段
     * @param <T>          bean
     * @return Map
     */
    public static <T> Map<String, Object> toMap(T bean, Map<String, String> fieldMapper, boolean putNull, String... ignoreFields) {
        Map<String, Object> map = new HashMap<>();
        if (bean == null) {
            return map;
        }
        List<Method> getterMethods = Methods.getGetterMethodsByCache(bean.getClass());
        for (Method getterMethod : getterMethods) {
            String fieldName = Fields.getFieldNameByMethod(getterMethod.getName());
            if (fieldMapper != null) {
                String s = fieldMapper.get(fieldName);
                if (s != null) {
                    fieldName = s;
                }
            }
            if (isIgnore(fieldName, ignoreFields)) {
                continue;
            }
            Object o = Methods.invokeMethod(bean, getterMethod);
            if (o == null) {
                if (putNull) {
                    map.put(fieldName, null);
                }
                continue;
            }
            map.put(fieldName, o);
        }
        return map;
    }

    public static <T> Map<String, T> toMap(T[] values, Map<Integer, String> indexMapper) {
        return toMap(values, indexMapper, false);
    }

    /**
     * [] -> Map
     *
     * @param values      数组
     * @param indexMapper 数组索引对应的名称
     * @param putNull     为空是否添加
     * @return Map
     */
    public static <T> Map<String, T> toMap(T[] values, Map<Integer, String> indexMapper, boolean putNull) {
        Map<String, T> map = new HashMap<>();
        if (values == null) {
            return map;
        }
        for (Map.Entry<Integer, String> entry : indexMapper.entrySet()) {
            Integer i = entry.getKey();
            if (i < values.length) {
                T value = values[i];
                if (value == null) {
                    if (putNull) {
                        map.put(entry.getValue(), null);
                    }
                } else {
                    map.put(entry.getValue(), value);
                }
            } else if (putNull) {
                map.put(entry.getValue(), null);
            }
        }
        return map;
    }

    public static <T> T toBean(Map<?, ?> map, Class<T> clazz) {
        return toBean(map, null, clazz);
    }

    /**
     * Map -> Bean
     *
     * @param map         map, key: Field  value: value
     * @param fieldMapper map, key: map的key value: field value可以多级映射
     * @param clazz       bean的class
     * @param <T>         class
     * @return bean
     */
    public static <T> T toBean(Map<?, ?> map, Map<String, ?> fieldMapper, Class<T> clazz) {
        T t = Constructors.newInstance(Constructors.getDefaultConstructorByCache(clazz));
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            List<Method> setterMethods = Methods.getSetterMethodsByCache(clazz);
            String key = Objects1.toString(entry.getKey());
            Map<String, ?> keyMap = null;
            if (fieldMapper != null) {
                Object field = fieldMapper.get(key);
                if (field instanceof String) {
                    key = (String) field;
                } else if (field instanceof Map) {
                    keyMap = (Map<String, ?>) field;
                }
            }
            String setterMethodName = Methods.getSetterMethodNameByField(key);
            for (Method setterMethod : setterMethods) {
                Object value = entry.getValue();
                if (setterMethod.getName().equals(setterMethodName)) {
                    if (value == null) {
                        break;
                    }
                    Class<?> paramClass = setterMethod.getParameterTypes()[0];
                    Class<?> valueClass = value.getClass();
                    try {
                        if (isImplClass(paramClass, valueClass)) {
                            // 赋值
                            Methods.invokeMethod(t, setterMethod, value);
                        } else if (isImplClass(Map.class, valueClass) && !isImplClass(Map.class, paramClass)) {
                            // 值为map 属性为 bean
                            Methods.invokeMethod(t, setterMethod, toBean((Map<?, ?>) value, keyMap, paramClass));
                        } else if (isImplClass(Map.class, paramClass) && !isImplClass(Map.class, valueClass)) {
                            // 值为bean 属性为 map
                            Methods.invokeMethod(t, setterMethod, toMap(value, (Map<String, String>) keyMap));
                        } else {
                            // 推断
                            Methods.invokeMethodInfer(t, setterMethod, value);
                        }
                    } catch (Exception e) {
                        LOGGER.error("BeanWrapper.toBean callSetterMethod error {}", setterMethodName, e);
                    }
                    break;
                }
            }
        }
        return t;
    }

    /**
     * [] -> Bean
     *
     * @param values      数组
     * @param indexMapper 数组索引对应的字段
     * @param clazz       class
     * @param <T>         T
     * @return bean
     */
    public static <T> T toBean(Object[] values, Map<Integer, String> indexMapper, Class<T> clazz) {
        T t = Constructors.newInstance(Constructors.getDefaultConstructorByCache(clazz));
        for (Map.Entry<Integer, String> entry : indexMapper.entrySet()) {
            try {
                Integer index = entry.getKey();
                if (index < values.length) {
                    Method setterMethod = Methods.getSetterMethodByCache(clazz, entry.getValue());
                    if (setterMethod == null) {
                        continue;
                    }
                    Object[] valueArr = new Object[1];
                    Object value = values[index];
                    Class<?> paramClass = setterMethod.getParameterTypes()[0];
                    Class<?> valueClass = value.getClass();
                    if (isImplClass(paramClass, valueClass)) {
                        // 赋值
                        valueArr[0] = value;
                        Methods.invokeMethod(t, setterMethod, valueArr);
                    } else if (isImplClass(Map.class, valueClass) && !isImplClass(Map.class, paramClass)) {
                        // 值为map 属性为 bean
                        valueArr[0] = toBean((Map<String, ?>) value, null, paramClass);
                        Methods.invokeMethod(t, setterMethod, valueArr);
                    } else if (isImplClass(Map.class, paramClass) && !isImplClass(Map.class, valueClass)) {
                        // 值为bean 属性为 map
                        valueArr[0] = toMap(value);
                        Methods.invokeMethod(t, setterMethod, valueArr);
                    } else {
                        // 推断
                        valueArr[0] = value;
                        Methods.invokeMethodInfer(t, setterMethod, valueArr);
                    }
                }
            } catch (Exception e) {
                // ignore
            }
        }
        return t;
    }

    /**
     * 复制属性
     *
     * @param source       源对象
     * @param targetClass  目标对象class
     * @param ignoreFields 忽略的属性
     * @param <R>          R
     * @param <T>          T
     * @return T
     */
    public static <R, T> T copyProperties(R source, Class<T> targetClass, String... ignoreFields) {
        return copy(source, targetClass, null, ignoreFields);
    }

    /**
     * 复制属性
     *
     * @param source      源对象
     * @param targetClass 目标对象class
     * @param fieldMapper key: 源对象字段 value: 目标对象字段
     * @param <R>         R
     * @param <T>         T
     * @return T
     */
    public static <R, T> T copyProperties(R source, Class<T> targetClass, Map<String, String> fieldMapper, String... ignoreFields) {
        return copy(source, targetClass, fieldMapper, ignoreFields);
    }

    /**
     * 复制属性
     *
     * @param source 源对象
     * @param target 目标对象
     * @param ignore 忽略的属性
     * @param <R>    R
     * @param <T>    T
     * @
     */
    public static <R, T> void copyProperties(R source, T target, String... ignore) {
        copy(source, target, null, ignore);
    }

    /**
     * 复制属性
     *
     * @param source       源对象
     * @param target       目标对象
     * @param fieldMapper  key: 源对象字段 value: 目标对象字段
     * @param ignoreFields 忽略的字段
     * @param <R>          R
     * @param <T>          T
     */
    public static <R, T> void copyProperties(R source, T target, Map<String, String> fieldMapper, String... ignoreFields) {
        copy(source, target, fieldMapper, ignoreFields);
    }

    private static <R, T> T copy(R source, Class<T> targetClass, Map<String, String> fieldMapper, String[] ignoreFields) {
        Valid.notNull(source, "source object is null");
        Valid.notNull(targetClass, "target class is null");
        Constructor<T> constructor = Constructors.getDefaultConstructorByCache(targetClass);
        T target = Constructors.newInstance(constructor);
        copy(source, target, fieldMapper, ignoreFields);
        return target;
    }

    private static <R, T> void copy(R source, T target, Map<String, String> fieldMapper, String[] ignoreFields) {
        Valid.notNull(source, "source object is null");
        Valid.notNull(target, "target object is null");
        List<Method> sourceGetters = Methods.getGetterMethodsByCache(source.getClass());
        List<Method> targetSetters = Methods.getSetterMethodsByCache(target.getClass());
        for (Method targetSetter : targetSetters) {
            String targetSetterName = targetSetter.getName();
            String targetFieldName = Strings.firstLower(targetSetterName.substring(3));
            if (isIgnore(targetFieldName, ignoreFields)) {
                continue;
            }
            String sourceFieldName = getFieldNameAlias(targetFieldName, fieldMapper);
            for (Method sourceGetter : sourceGetters) {
                if (Objects1.eq(Fields.getFieldNameByMethod(sourceGetter.getName()), sourceFieldName)) {
                    try {
                        Object sourceValue = Methods.invokeMethod(source, sourceGetter);
                        if (sourceValue != null) {
                            Methods.invokeMethodInfer(target, targetSetter, sourceValue);
                        }
                        break;
                    } catch (Exception e) {
                        throw Exceptions.parse("could not copy source property " + sourceFieldName + " to target " + targetFieldName);
                    }
                }
            }
        }
    }

    /**
     * 是否被忽略
     *
     * @param name         字段
     * @param ignoreFields 忽略的字段
     * @return true 被忽略
     */
    private static boolean isIgnore(String name, String[] ignoreFields) {
        if (ignoreFields != null) {
            for (String ignore : ignoreFields) {
                if (ignore.equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取字段别名
     *
     * @param name        字段名
     * @param fieldMapper 字段映射
     * @return 别名
     */
    private static String getFieldNameAlias(String name, Map<String, String> fieldMapper) {
        if (fieldMapper != null) {
            for (Map.Entry<String, String> entry : fieldMapper.entrySet()) {
                if (entry.getValue().equals(name)) {
                    return entry.getKey();
                }
            }
        }
        return name;
    }

}
