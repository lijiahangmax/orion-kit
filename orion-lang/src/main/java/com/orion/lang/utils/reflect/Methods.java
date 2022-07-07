package com.orion.lang.utils.reflect;

import com.orion.lang.constant.Const;
import com.orion.lang.define.collect.ConcurrentReferenceHashMap;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.collect.Lists;

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
 * 反射 方法工具类
 * <p>
 * 如果需要调用基本类型入参的方法 可以先获取Method
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/15 13:15
 */
@SuppressWarnings("ALL")
public class Methods {

    /**
     * set方法前缀
     */
    protected static final String SETTER_PREFIX = "set";

    /**
     * get方法前缀
     */
    protected static final String GETTER_PREFIX = "get";

    /**
     * boolean get方法前缀
     */
    protected static final String BOOLEAN_GETTER_PREFIX = "is";

    private static final Map<Class<?>, List<Method>> CLASS_SET_METHOD_CACHE = new ConcurrentReferenceHashMap<>(Const.CAPACITY_16, ConcurrentReferenceHashMap.ReferenceType.SOFT);

    private static final Map<Class<?>, List<Method>> CLASS_GET_METHOD_CACHE = new ConcurrentReferenceHashMap<>(Const.CAPACITY_16, ConcurrentReferenceHashMap.ReferenceType.SOFT);

    private Methods() {
    }

    // -------------------- cache start --------------------

    /**
     * 获取getter方法
     *
     * @param clazz class
     * @param field field
     * @return getter
     */
    public static Method getGetterMethodByCache(Class<?> clazz, String field) {
        List<Method> methods = getGetterMethodsByCache(clazz);
        if (methods == null) {
            return null;
        }
        String methodName1 = GETTER_PREFIX + Strings.firstUpper(field);
        for (Method method : methods) {
            if (method.getParameterCount() == 0 && method.getName().equals(methodName1)) {
                return method;
            }
        }
        String methodName2 = BOOLEAN_GETTER_PREFIX + Strings.firstUpper(field);
        for (Method method : methods) {
            if (method.getParameterCount() == 0 && method.getName().equals(methodName2)) {
                return method;
            }
        }
        return null;
    }

    /**
     * 获取setter方法
     *
     * @param clazz class
     * @param field field
     * @return method
     */
    public static Method getSetterMethodByCache(Class<?> clazz, String field) {
        List<Method> methods = getSetterMethodsByCache(clazz);
        if (methods == null) {
            return null;
        }
        String methodName = SETTER_PREFIX + Strings.firstUpper(field);
        for (Method method : methods) {
            if (method.getParameterCount() == 1 && method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }

    /**
     * 获取所有getter方法
     *
     * @param clazz class
     * @return method
     */
    public static List<Method> getGetterMethodsByCache(Class<?> clazz) {
        List<Method> methodList = CLASS_GET_METHOD_CACHE.get(clazz);
        if (methodList == null) {
            CLASS_GET_METHOD_CACHE.put(clazz, methodList = getGetterMethods(clazz));
        }
        return methodList;
    }

    /**
     * 获取所有setter方法
     *
     * @param clazz class
     * @return method
     */
    public static List<Method> getSetterMethodsByCache(Class<?> clazz) {
        List<Method> methodList = CLASS_SET_METHOD_CACHE.get(clazz);
        if (methodList == null) {
            CLASS_SET_METHOD_CACHE.put(clazz, methodList = getSetterMethods(clazz));
        }
        return methodList;
    }

    // -------------------- cache end --------------------

    /**
     * 通过字段获取getter方法
     *
     * @param field field
     * @return getter方法
     */
    public static String getGetterMethodNameByField(Field field) {
        return getGetterMethodNameByFieldName(field.getName(), field.getType().equals(Boolean.TYPE));
    }

    /**
     * 通过字段名称获取getter方法
     *
     * @param fieldName fieldName
     * @return getter方法
     */
    public static String getGetterMethodNameByFieldName(String fieldName) {
        if (Strings.isBlank(fieldName)) {
            return null;
        }
        return GETTER_PREFIX + Strings.firstUpper(fieldName.trim());
    }

    /**
     * 通过字段名称获取getter方法
     *
     * @param fieldName      fieldName
     * @param isBooleanClass 是否为 Boolean.TYPE
     * @return getter方法
     */
    public static String getGetterMethodNameByFieldName(String fieldName, boolean isBooleanClass) {
        if (Strings.isBlank(fieldName)) {
            return null;
        }
        return (isBooleanClass ? BOOLEAN_GETTER_PREFIX : GETTER_PREFIX) + Strings.firstUpper(fieldName.trim());
    }

    /**
     * 通过字段获取setter方法
     *
     * @param field field
     * @return setter方法
     */
    public static String getSetterMethodNameByField(Field field) {
        return SETTER_PREFIX + Strings.firstUpper(field.getName());
    }

    /**
     * 通过字段名称获取setter方法
     *
     * @param fieldName fieldName
     * @return setter方法
     */
    public static String getSetterMethodNameByFieldName(String fieldName) {
        if (Strings.isBlank(fieldName)) {
            return null;
        }
        return SETTER_PREFIX + Strings.firstUpper(fieldName.trim());
    }

    /**
     * 通过方法获取所有的getter方法
     *
     * @param clazz class
     * @return getter方法
     */
    public static List<Method> getGetterMethods(Class<?> clazz) {
        List<Method> list = new ArrayList<>();
        // get super class methods
        for (Method method : clazz.getMethods()) {
            if (!"getClass".equals(method.getName()) && !Modifier.isStatic(method.getModifiers()) && method.getParameters().length == 0) {
                String name = method.getName();
                if (name.startsWith(GETTER_PREFIX) && name.length() != 3 && !method.getReturnType().equals(Void.TYPE)) {
                    setAccessible(method);
                    list.add(method);
                } else if (method.getName().startsWith(BOOLEAN_GETTER_PREFIX) && name.length() != 2 && method.getReturnType().equals(Boolean.TYPE)) {
                    setAccessible(method);
                    list.add(method);
                }
            }
        }
        return list;
    }

    /**
     * 通过方法获取所有的setter方法
     *
     * @param clazz class
     * @return setter方法
     */
    public static List<Method> getSetterMethods(Class<?> clazz) {
        List<Method> list = new ArrayList<>();
        // get super class methods
        for (Method method : clazz.getMethods()) {
            String name = method.getName();
            if (name.startsWith(SETTER_PREFIX) && name.length() != 3 && !Modifier.isStatic(method.getModifiers()) && method.getParameters().length == 1) {
                setAccessible(method);
                list.add(method);
            }
        }
        return list;
    }

    /**
     * 通过字段获取所有的getter方法
     *
     * @param clazz class
     * @return getter方法
     */
    public static List<Method> getGetterMethodsByField(Class<?> clazz) {
        List<Method> list = new ArrayList<>();
        List<Field> fields = Fields.getFields(clazz);
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                Method method;
                if (field.getType().equals(Boolean.TYPE)) {
                    String fieldName = Strings.firstUpper(field.getName());
                    method = getAccessibleMethod(clazz, BOOLEAN_GETTER_PREFIX + fieldName, 0);
                    if (method == null) {
                        method = getAccessibleMethod(clazz, GETTER_PREFIX + fieldName, 0);
                    }
                } else {
                    method = getAccessibleMethod(clazz, GETTER_PREFIX + Strings.firstUpper(field.getName()), 0);
                }
                if (method != null) {
                    list.add(method);
                }
            }
        }
        return list;
    }

    /**
     * 通过字段获取所有的setter方法
     *
     * @param clazz class
     * @return setter方法
     */
    public static List<Method> getSetterMethodsByField(Class<?> clazz) {
        List<Method> list = new ArrayList<>();
        List<Field> fields = Fields.getFields(clazz);
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                String methodName = SETTER_PREFIX + Strings.firstUpper(field.getName());
                Method method = getAccessibleMethod(clazz, methodName, field.getType());
                if (method != null) {
                    list.add(method);
                }
            }
        }
        return list;
    }

    /**
     * 通过字段获取getter方法
     *
     * @param clazz class
     * @param field field
     * @return get方法
     */
    public static Method getGetterMethodByField(Class<?> clazz, Field field) {
        Method method;
        if (field.getType().equals(Boolean.TYPE)) {
            String fieldName = Strings.firstUpper(field.getName());
            method = getAccessibleMethod(clazz, BOOLEAN_GETTER_PREFIX + fieldName, 0);
            if (method == null) {
                method = getAccessibleMethod(clazz, GETTER_PREFIX + fieldName, 0);
            }
        } else {
            String methodName = GETTER_PREFIX + Strings.firstUpper(field.getName());
            method = getAccessibleMethod(clazz, methodName, 0);
        }
        return method;
    }

    /**
     * 通过字段名称获取getter方法
     *
     * @param clazz     class
     * @param fieldName fieldName
     * @return getter方法
     */
    public static Method getGetterMethodByFieldName(Class<?> clazz, String fieldName) {
        if (Strings.isBlank(fieldName)) {
            return null;
        }
        fieldName = Strings.firstUpper(fieldName.trim());
        Method method = getAccessibleMethod(clazz, GETTER_PREFIX + fieldName, 0);
        if (method == null) {
            method = getAccessibleMethod(clazz, BOOLEAN_GETTER_PREFIX + fieldName, 0);
        }
        return method;
    }

    /**
     * 通过字段名称获取setter方法
     *
     * @param clazz class
     * @param field field
     * @return setter方法
     */
    public static Method getSetterMethodByField(Class<?> clazz, Field field) {
        String methodName = SETTER_PREFIX + Strings.firstUpper(field.getName());
        return getAccessibleMethod(clazz, methodName, field.getType());
    }

    /**
     * 通过字段获取setter方法
     *
     * @param clazz     class
     * @param fieldName field
     * @return setter方法
     */
    public static Method getSetterMethodByFieldName(Class<?> clazz, String fieldName) {
        if (Strings.isBlank(fieldName)) {
            return null;
        }
        fieldName = fieldName.trim();
        String methodName = SETTER_PREFIX + Strings.firstUpper(fieldName);
        return getAccessibleMethod(clazz, methodName, 1);
    }

    /**
     * 获取对象匹配方法名和参数类型的DeclaredMethod, 并强制设置为可访问, 可以获取基本类型的方法
     *
     * @param clazz          class
     * @param methodName     方法名
     * @param parameterTypes 参数类型
     * @return 方法对象
     */
    public static Method getAccessibleMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        Valid.notNull(clazz, "method class is null");
        for (Class<?> searchType = clazz; searchType != Object.class; searchType = searchType.getSuperclass()) {
            try {
                Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
                setAccessible(method);
                return method;
            } catch (Exception e) {
                // ignore
            }
        }
        return null;
    }

    /**
     * 获取对象匹配方法名和参数长度的第一个DeclaredMethod, 并强制设置为可访问, 可以获取基本类型的方法
     *
     * @param clazz      class
     * @param methodName 方法名称
     * @param argsNum    参数数量
     * @return 方法对象
     */
    public static Method getAccessibleMethod(Class<?> clazz, String methodName, int argsNum) {
        Valid.notNull(clazz, "method class is null");
        for (Class<?> searchType = clazz; searchType != Object.class; searchType = searchType.getSuperclass()) {
            Method[] methods = searchType.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName) && method.getParameterTypes().length == argsNum) {
                    setAccessible(method);
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * 获取对象匹配方法名的第一个DeclaredMethod, 并强制设置为可访问, 可以获取基本类型的方法
     *
     * @param clazz      class
     * @param methodName 方法名称
     * @return 方法对象
     */
    public static Method getAccessibleMethod(Class<?> clazz, String methodName) {
        for (Class<?> searchType = clazz; searchType != Object.class; searchType = searchType.getSuperclass()) {
            Method[] methods = searchType.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    setAccessible(method);
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * 获取对象匹配方法名和参数长度的DeclaredMethod, 并强制设置为可访问, 可以获取基本类型的方法
     *
     * @param clazz      class
     * @param methodName 方法名称
     * @param argsNum    参数数量
     * @return 方法对象
     */
    public static List<Method> getAccessibleMethods(Class<?> clazz, String methodName, int argsNum) {
        Valid.notNull(clazz, "method class is null");
        List<Method> methods = new ArrayList<>();
        for (Class<?> searchType = clazz; searchType != Object.class; searchType = searchType.getSuperclass()) {
            Method[] searchMethods = searchType.getDeclaredMethods();
            for (Method method : searchMethods) {
                if (method.getName().equals(methodName) && method.getParameterTypes().length == argsNum) {
                    setAccessible(method);
                    methods.add(method);
                }
            }
        }
        return methods;
    }

    /**
     * 获取对象匹配方法名的DeclaredMethod, 并强制设置为可访问
     *
     * @param clazz      class
     * @param methodName 方法名称
     * @return 方法对象
     */
    public static List<Method> getAccessibleMethods(Class<?> clazz, String methodName) {
        Valid.notNull(clazz, "method class is null");
        List<Method> methods = new ArrayList<>();
        for (Class<?> searchType = clazz; searchType != Object.class; searchType = searchType.getSuperclass()) {
            Method[] searchMethods = searchType.getDeclaredMethods();
            for (Method method : searchMethods) {
                if (method.getName().equals(methodName)) {
                    setAccessible(method);
                    methods.add(method);
                }
            }
        }
        return methods;
    }

    /**
     * 获取该类的所有方法
     *
     * @param clazz 反射类
     * @return 方法
     */
    public static List<Method> getAccessibleMethods(Class<?> clazz) {
        Valid.notNull(clazz, "method class is null");
        if (clazz.getSuperclass() != null) {
            List<Method> methodList = Stream.of(clazz.getDeclaredMethods())
                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
                    .peek(Methods::setAccessible)
                    .collect(toCollection(ArrayList::new));
            Class<?> superClass = clazz.getSuperclass();
            // 当前类方法
            Map<String, Method> methodMap = methodList.stream().collect(toMap(Method::getName, identity()));
            // 父类方法
            getAccessibleMethods(superClass).stream().filter(m -> !methodMap.containsKey(m.getName())).forEach(methodList::add);
            return methodList;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 获取该类的所有方法列表
     *
     * @param clazz 反射类
     * @return 方法
     */
    public static Map<String, Method> getAccessibleMethodMap(Class<?> clazz) {
        List<Method> methodList = getAccessibleMethods(clazz);
        return Lists.isNotEmpty(methodList) ? methodList.stream().collect(Collectors.toMap(Method::getName, identity())) : new HashMap<>();
    }

    /**
     * 设置方法可访问
     */
    public static void setAccessible(Method method) {
        Valid.notNull(method, "set accessible method class is null");
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    /**
     * 获取所有static的方法
     *
     * @param clazz 类
     * @return static方法
     */
    public static List<Method> getStaticMethods(Class<?> clazz) {
        Valid.notNull(clazz, "class is null");
        Method[] methods = clazz.getDeclaredMethods();
        return Arrays.stream(methods)
                .filter(m -> Modifier.isStatic(m.getModifiers()))
                .collect(Collectors.toList());
    }

    /**
     * 调用field的getter方法
     *
     * @param obj       对象
     * @param fieldName 字段名称
     * @param <E>       属性类型
     * @return ignore
     */
    public static <E> E invokeGetter(Object obj, String fieldName) {
        Valid.notNull(obj, "invoke object is null");
        Valid.notBlank(fieldName, "invoke getter field is null");
        try {
            Field field = Fields.getAccessibleField(obj.getClass(), fieldName);
            if (field == null || !field.getType().equals(Boolean.TYPE)) {
                return invokeMethod(obj, GETTER_PREFIX + Strings.firstUpper(fieldName), null, (Object[]) null);
            } else {
                return invokeMethod(obj, BOOLEAN_GETTER_PREFIX + Strings.firstUpper(fieldName), null, (Object[]) null);
            }
        } catch (Exception e) {
            throw Exceptions.invoke(Strings.format("invoke field: {} setter method error {}", fieldName, e.getMessage()), e);
        }
    }

    /**
     * 调用setter方法 不支持基本类型
     *
     * @param obj       对象
     * @param fieldName 字段名称
     * @param value     ignore
     * @param <E>       属性类型
     */
    public static <E, R> R invokeSetter(Object obj, String fieldName, E value) {
        return (R) invokeMethod(obj, SETTER_PREFIX + Strings.firstUpper(fieldName), new Class[]{value.getClass()}, new Object[]{value});
    }

    /**
     * 调用setter方法, 多级调用需要手动拼接set 不支持基本类型
     *
     * @param obj                   对象
     * @param fieldSetterMethodName 字段名称
     * @param values                ignore
     * @param <E>                   属性类型
     */
    @SafeVarargs
    public static <E, R> R invokeSetter(Object obj, String fieldSetterMethodName, E... values) {
        Valid.notNull(obj, "invoke object is null");
        Valid.notBlank(fieldSetterMethodName, "invoke Setter Method is null");
        String[] names = fieldSetterMethodName.split("\\.");
        if (names.length != Arrays1.length(values)) {
            throw Exceptions.argument("setting method and parameter length are inconsistent");
        }
        if (names.length == 1) {
            return (R) invokeMethod(obj, SETTER_PREFIX + Strings.firstUpper(names[0]), new Class[]{values[0].getClass()}, new Object[]{values[0]});
        } else {
            for (int i = 0; i < names.length - 1; i++) {
                E value = values[i];
                invokeMethod(obj, names[i], value == null ? null : new Class[]{value.getClass()}, value == null ? null : new Object[]{values[i]});
            }
            int end = names.length - 1;
            return (R) invokeMethod(obj, names[end], values[end] == null ? null : new Class[]{values[end].getClass()}, values[end] == null ? null : new Object[]{values[end]});
        }
    }

    /**
     * 调用setter方法 类型推断调用 支持基本数据类型
     *
     * @param obj       对象
     * @param fieldName 字段名称
     * @param value     ignore
     * @param <E>       属性类型
     */
    public static <E, R> R invokeSetterInfer(Object obj, String fieldName, E value) {
        return invokeMethodInfer(obj, SETTER_PREFIX + Strings.firstUpper(fieldName), new Object[]{value});
    }

    /**
     * 调用setter方法 类型推断调用 支持基本数据类型
     *
     * @param obj    对象
     * @param method method
     * @param value  ignore
     * @param <E>    属性类型
     */
    public static <E, R> R invokeSetterInfer(Object obj, Method method, E value) {
        return invokeMethodInfer(obj, method, new Object[]{value});
    }

    /**
     * 直接调用对象方法 不支持基本类型
     *
     * @param obj            对象
     * @param methodName     方法名称
     * @param parameterTypes 参数列表类型
     * @param args           参数列表
     * @param <E>            返回值类型
     * @return ignore
     */
    public static <E> E invokeMethod(Object obj, String methodName, Class<?>[] parameterTypes, Object... args) {
        Valid.notNull(obj, "invoker object is null");
        Valid.notBlank(methodName, "invoke method is null");
        Method method = Methods.getAccessibleMethod(obj.getClass(), methodName, parameterTypes);
        if (method == null) {
            throw Exceptions.invoke(Strings.format("method {} not found in class {}", methodName, obj.getClass()));
        }
        try {
            return (E) method.invoke(obj, args);
        } catch (Exception e) {
            throw Exceptions.invoke(Strings.format("invoke method error: {}, class: {}, args: {}", methodName, obj.getClass(), Arrays.toString(args)), e);
        }
    }

    /**
     * 直接调用对象方法
     *
     * @param obj        对象
     * @param methodName 方法名称
     * @param <E>        返回值类型
     * @return 对象
     */
    public static <E> E invokeMethod(Object obj, String methodName) {
        return invokeMethod(obj, methodName, (Object[]) null);
    }

    /**
     * 直接调用对象方法
     *
     * @param obj    对象
     * @param method 方法
     * @param <E>    返回值类型
     * @return 对象
     */
    public static <E> E invokeMethod(Object obj, Method method) {
        return invokeMethod(obj, method, (Object[]) null);
    }

    /**
     * 直接调用对象方法 不支持基本数据类型
     *
     * @param obj        对象
     * @param methodName 方法名称
     * @param args       参数列表
     * @param <E>        返回值类型
     * @return 对象
     */
    public static <E> E invokeMethod(Object obj, String methodName, Object... args) {
        Valid.notNull(obj, "invoker object is null");
        Valid.notBlank(methodName, "invoke method is null");
        Method method = Methods.getAccessibleMethod(obj.getClass(), methodName, Arrays1.length(args));
        if (method == null) {
            throw Exceptions.invoke(Strings.format("invoke method error: {} not found in class {}", methodName, obj.getClass().getName()));
        }
        try {
            return (E) method.invoke(obj, args);
        } catch (Exception e) {
            throw Exceptions.invoke(Strings.format("invoke method error: {}, class: {}, args: {}", methodName, obj.getClass().getName(), Arrays.toString(args)), e);
        }
    }

    /**
     * 直接调用对象方法 不支持基本数据类型
     *
     * @param obj    对象
     * @param method 方法
     * @param args   参数列表
     * @param <E>    返回值类型
     * @return 对象
     */
    public static <E> E invokeMethod(Object obj, Method method, Object... args) {
        Valid.notNull(obj, "invoke object is null");
        Valid.notNull(method, "invoke method is null");
        try {
            Methods.setAccessible(method);
            return (E) method.invoke(obj, args);
        } catch (Exception e) {
            throw Exceptions.invoke(Strings.format("invoke method error: {}, class: {}, args: {}", method.getName(), obj.getClass().getName(), Arrays.toString(args)), e);
        }
    }

    /**
     * 直接调用对象方法, 会进行参数推断, 支持基本数据类型
     *
     * @param obj    对象
     * @param method 方法
     * @param args   参数列表
     * @param <E>    返回值类型
     * @return 对象
     */
    public static <E> E invokeMethodInfer(Object obj, Method method, Object... args) {
        Valid.notNull(obj, "invoke object is null");
        Valid.notNull(method, "invoke method is null");
        if (Arrays1.isEmpty(args)) {
            return invokeMethod(obj, method, (Object[]) null);
        }
        return TypeInfer.invokeInfer(obj, Lists.singleton(method), args);
    }

    /**
     * 直接调用对象方法, 会进行参数推断, 支持基本数据类型
     *
     * @param obj        对象
     * @param methodName 方法名称
     * @param args       参数列表
     * @param <E>        返回值类型
     * @return 对象
     */
    public static <E> E invokeMethodInfer(Object obj, String methodName, Object... args) {
        Valid.notNull(obj, "invoke object is null");
        Valid.notBlank(methodName, "invoke method is null");
        if (Arrays1.isEmpty(args)) {
            return invokeMethod(obj, methodName, (Object[]) null);
        }
        int len = Arrays1.length(args);
        List<Method> methods = Methods.getAccessibleMethods(obj.getClass(), methodName, len);
        return TypeInfer.invokeInfer(obj, methods, args);
    }

}
