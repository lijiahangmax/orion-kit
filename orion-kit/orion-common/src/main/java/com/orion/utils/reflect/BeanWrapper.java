package com.orion.utils.reflect;

import com.orion.lang.collect.MultiHashMap;
import com.orion.utils.Exceptions;
import com.orion.utils.Objects1;
import com.orion.utils.Strings;
import com.orion.utils.Valid;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean 工具类
 * 根据getter方法获取值 和 setter方法设置值
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/2 15:37
 */
@SuppressWarnings("unchecked")
public class BeanWrapper {

    private static final Map<Class<?>, Constructor> CONSTRUCTOR_CACHE = new ConcurrentHashMap<>(16);
    private static final Map<Class<?>, List<Method>> ALL_SET_METHOD_CACHE = new ConcurrentHashMap<>(16);
    private static final Map<Class<?>, List<Method>> ALL_GET_METHOD_CACHE = new ConcurrentHashMap<>(16);
    private static final Map<Class<?>, List<Class<?>>> ALL_INTERFACE_CACHE = new ConcurrentHashMap<>(16);
    private static final MultiHashMap<Class<?>, String, Method> METHOD_CACHE = new MultiHashMap<>(true);

    private BeanWrapper() {
    }

    /**
     * Bean -> Map
     *
     * @param bean bean
     * @param <T>  bean
     * @return Map
     */
    public static <T> Map<String, Object> toMap(T bean) {
        return toMap(bean, null, false);
    }

    /**
     * Bean -> Map
     *
     * @param bean    bean
     * @param putNull 为空是否添加
     * @param <T>     bean
     * @return Map
     */
    public static <T> Map<String, Object> toMap(T bean, boolean putNull) {
        return toMap(bean, null, putNull);
    }

    /**
     * Bean -> Map
     *
     * @param bean     bean
     * @param fieldMap map key: beanField  value: mapKey
     * @param <T>      bean
     * @return Map
     */
    public static <T> Map<String, Object> toMap(T bean, Map<String, String> fieldMap) {
        return toMap(bean, fieldMap, false);
    }

    /**
     * Bean -> Map
     *
     * @param bean     bean
     * @param fieldMap map key: beanField  value: mapKey
     * @param putNull  为空是否添加
     * @param <T>      bean
     * @return Map
     */
    public static <T> Map<String, Object> toMap(T bean, Map<String, String> fieldMap, boolean putNull) {
        Map<String, Object> map = new HashMap<>();
        if (bean == null) {
            return map;
        }
        List<Method> getterMethods = getAllGetterMethod(bean.getClass());
        for (Method getterMethod : getterMethods) {
            String fieldName = Fields.getFieldNameByMethodName(getterMethod.getName());
            if (fieldMap != null) {
                String s = fieldMap.get(fieldName);
                if (s != null) {
                    fieldName = s;
                }
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

    /**
     * [] -> Map
     *
     * @param values   数组
     * @param fieldMap 数组索引对应的字段
     * @return Map
     */
    public static <T> Map<String, T> toMap(T[] values, Map<Integer, String> fieldMap) {
        return toMap(values, fieldMap, false);
    }

    /**
     * [] -> Map
     *
     * @param values   数组
     * @param fieldMap 数组索引对应的字段
     * @param putNull  为空是否添加
     * @return Map
     */
    public static <T> Map<String, T> toMap(T[] values, Map<Integer, String> fieldMap, boolean putNull) {
        Map<String, T> map = new HashMap<>();
        if (values == null) {
            return map;
        }
        for (Map.Entry<Integer, String> entry : fieldMap.entrySet()) {
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

    /**
     * Map -> Bean
     *
     * @param map   map, key: Field  value: value
     * @param clazz bean的class
     * @param <T>   class
     * @return bean
     */
    public static <T> T toBean(Map<String, ?> map, Class<T> clazz) {
        return toBean(map, null, clazz);
    }

    /**
     * Map -> Bean
     *
     * @param map      map, key: Field  value: value
     * @param fieldMap map, key: map的key value: Field
     * @param clazz    bean的class
     * @param <T>      class
     * @return bean
     */
    public static <T> T toBean(Map<String, ?> map, Map<String, ?> fieldMap, Class<T> clazz) {
        T t = Constructors.newInstance(getConstructor(clazz));
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            List<Method> setterMethods = getAllSetterMethod(clazz);
            String key = entry.getKey();
            Map<String, ?> keyMap = null;
            if (fieldMap != null) {
                Object field = fieldMap.get(key);
                if (field instanceof String) {
                    key = (String) field;
                } else if (field instanceof Map) {
                    keyMap = (Map<String, ?>) field;
                }
            }
            String setterMethodName = "set" + Strings.firstUpper(key);
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
                            Methods.invokeMethod(t, setterMethod, new Object[]{value});
                        } else if (isImplClass(Map.class, valueClass) && !isImplClass(Map.class, paramClass)) {
                            // 值为map 属性为 bean
                            Methods.invokeMethod(t, setterMethod, new Object[]{toBean((Map<String, ?>) value, keyMap, paramClass)});
                        } else if (isImplClass(Map.class, paramClass) && !isImplClass(Map.class, valueClass)) {
                            // 值为bean 属性为 map
                            Methods.invokeMethod(t, setterMethod, new Object[]{toMap(value, (Map<String, String>) keyMap)});
                        } else {
                            // 推断
                            Methods.invokeMethodInfer(t, setterMethod, new Object[]{value});
                        }
                    } catch (Exception e) {
                        // ignore
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
     * @param values   数组
     * @param fieldMap 数组索引对应的字段
     * @param clazz    class
     * @param <T>      T
     * @return bean
     */
    public static <T> T toBean(Object[] values, Map<Integer, String> fieldMap, Class<T> clazz) {
        T t = Constructors.newInstance(getConstructor(clazz));
        for (Map.Entry<Integer, String> entry : fieldMap.entrySet()) {
            try {
                Integer i = entry.getKey();
                if (i < values.length) {
                    Method setterMethod = getSetterMethod(clazz, entry.getValue());
                    Object[] valueArr = new Object[1];
                    Object value = values[i];
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
     * @param source      源对象
     * @param targetClass 目标对象class
     * @param <R>         R
     * @param <T>         T
     * @return T
     */
    public static <R, T> T copyProperties(R source, Class<T> targetClass) {
        return copy(source, targetClass, null, null);
    }

    /**
     * 复制属性
     *
     * @param source      源对象
     * @param targetClass 目标对象class
     * @param ignore      忽略的属性
     * @param <R>         R
     * @param <T>         T
     * @return T
     */
    public static <R, T> T copyProperties(R source, Class<T> targetClass, String... ignore) {
        return copy(source, targetClass, null, ignore);
    }

    /**
     * 复制属性
     *
     * @param source      源对象
     * @param targetClass 目标对象class
     * @param fieldMap    key: 源对象字段 value: 目标对象字段
     * @param <R>         R
     * @param <T>         T
     * @return T
     */
    public static <R, T> T copyProperties(R source, Class<T> targetClass, Map<String, String> fieldMap) {
        return copy(source, targetClass, fieldMap, null);
    }

    /**
     * 复制属性
     *
     * @param source      源对象
     * @param targetClass 目标对象class
     * @param fieldMap    key: 源对象字段 value: 目标对象字段
     * @param <R>         R
     * @param <T>         T
     * @return T
     */
    public static <R, T> T copyProperties(R source, Class<T> targetClass, Map<String, String> fieldMap, String... ignore) {
        return copy(source, targetClass, fieldMap, ignore);
    }

    /**
     * 复制属性
     *
     * @param source 源对象
     * @param target 目标对象
     * @param <R>    R
     * @param <T>    T
     * @
     */
    public static <R, T> void copyProperties(R source, T target) {
        copy(source, target, null, null);
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
     * @param source   源对象
     * @param target   目标对象
     * @param fieldMap key: 源对象字段 value: 目标对象字段
     * @param <R>      R
     * @param <T>      T
     */
    public static <R, T> void copyProperties(R source, T target, Map<String, String> fieldMap) {
        copy(source, target, fieldMap, null);
    }

    /**
     * 复制属性
     *
     * @param source   源对象
     * @param target   目标对象
     * @param fieldMap key: 源对象字段 value: 目标对象字段
     * @param ignore   忽略的字段
     * @param <R>      R
     * @param <T>      T
     */
    public static <R, T> void copyProperties(R source, T target, Map<String, String> fieldMap, String... ignore) {
        copy(source, target, fieldMap, ignore);
    }

    private static <R, T> T copy(R source, Class<T> targetClass, Map<String, String> fieldMap, String[] ignores) {
        Valid.notNull(source, "source object is null");
        Valid.notNull(targetClass, "target class is null");
        Constructor<T> constructor = getConstructor(targetClass);
        T target = Constructors.newInstance(constructor);
        copy(source, target, fieldMap, ignores);
        return target;
    }

    private static <R, T> void copy(R source, T target, Map<String, String> fieldMap, String[] ignores) {
        Valid.notNull(source, "source object is null");
        Valid.notNull(target, "target object is null");
        List<Method> sourceGetters = getAllGetterMethod(source.getClass());
        List<Method> targetSetters = getAllSetterMethod(target.getClass());
        for (Method targetSetter : targetSetters) {
            String targetSetterName = targetSetter.getName();
            String targetFieldName = Strings.firstLower(targetSetterName.substring(3, targetSetterName.length()));
            if (isIgnore(targetFieldName, ignores)) {
                continue;
            }
            String sourceFieldName = getFieldNameAlias(targetFieldName, fieldMap);
            for (Method sourceGetter : sourceGetters) {
                if (Objects1.eq(Fields.getFieldNameByMethodName(sourceGetter.getName()), sourceFieldName)) {
                    try {
                        Object sourceValue = Methods.invokeMethod(source, sourceGetter);
                        if (sourceValue != null) {
                            Methods.invokeMethodInfer(target, targetSetter, new Object[]{sourceValue});
                        }
                        break;
                    } catch (Exception e) {
                        throw Exceptions.parse("Could not copy source property " + sourceFieldName + " to target " + targetFieldName);
                    }
                }
            }
        }
    }

    private static boolean isIgnore(String name, String[] ignores) {
        if (ignores != null) {
            for (String ignore : ignores) {
                if (ignore.equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static String getFieldNameAlias(String name, Map<String, String> fieldMap) {
        if (fieldMap != null) {
            for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
                if (entry.getValue().equals(name)) {
                    return entry.getKey();
                }
            }
        }
        return name;
    }

    /**
     * 判断argClass是否为paramClass的实现类
     *
     * @param paramClass paramClass
     * @param argClass   argClass
     * @return true 是实现类或本类
     */
    private static boolean isImplClass(Class paramClass, Class argClass) {
        if (paramClass.equals(argClass) || paramClass.equals(Object.class)) {
            return true;
        }
        List<Class<?>> list = ALL_INTERFACE_CACHE.get(argClass);
        if (list == null) {
            list = Classes.getInterfaces(argClass);
            list.addAll(Classes.getSuperClasses(argClass));
            ALL_INTERFACE_CACHE.put(argClass, list);
        }
        if (list.isEmpty()) {
            return false;
        }
        return list.contains(paramClass);
    }

    /**
     * 获取所有setter方法
     *
     * @param clazz class
     * @return method
     */
    private static List<Method> getAllSetterMethod(Class clazz) {
        List<Method> methodList = ALL_SET_METHOD_CACHE.get(clazz);
        if (methodList == null) {
            methodList = Methods.getAllSetterMethod(clazz);
            ALL_SET_METHOD_CACHE.put(clazz, methodList);
        }
        return methodList;
    }

    /**
     * 获取所有getter方法
     *
     * @param clazz class
     * @return method
     */
    private static List<Method> getAllGetterMethod(Class clazz) {
        List<Method> methodList = ALL_GET_METHOD_CACHE.get(clazz);
        if (methodList == null) {
            methodList = Methods.getAllGetterMethod(clazz);
            ALL_GET_METHOD_CACHE.put(clazz, methodList);
        }
        return methodList;
    }

    /**
     * 获取setter方法
     *
     * @param clazz     class
     * @param fieldName fieldName
     * @return method
     */
    private static Method getSetterMethod(Class clazz, String fieldName) {
        String methodName = "set" + Strings.firstUpper(fieldName);
        Method method = METHOD_CACHE.get(clazz, methodName);
        if (method == null) {
            method = Methods.getAccessibleMethod(clazz, methodName, 1);
            METHOD_CACHE.put(clazz, methodName, method);
        }
        return method;
    }

    /**
     * 获取无参方法
     *
     * @param clazz class
     * @return constructor
     */
    private static <T> Constructor<T> getConstructor(Class<T> clazz) {
        Constructor<T> constructor = CONSTRUCTOR_CACHE.get(clazz);
        if (constructor == null) {
            constructor = Constructors.getDefaultConstructor(clazz);
            CONSTRUCTOR_CACHE.put(clazz, constructor);
        }
        return constructor;
    }

}
