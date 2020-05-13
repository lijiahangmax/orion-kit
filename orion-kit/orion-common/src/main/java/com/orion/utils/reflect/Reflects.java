package com.orion.utils.reflect;

import com.orion.utils.*;
import com.orion.utils.collect.Lists;
import com.orion.utils.file.Files1;
import com.orion.utils.math.BigIntegers;
import com.orion.utils.math.Decimals;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.Collections;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toMap;

/**
 * 反射工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/1/7 10:03
 */
@SuppressWarnings("ALL")
public class Reflects {

    /**
     * set方法前缀
     */
    private static final String SETTER_PREFIX = "set";

    /**
     * get方法前缀
     */
    private static final String GETTER_PREFIX = "get";

    /**
     * boolean get方法前缀
     */
    private static final String BOOLEAN_GETTER_PREFIX = "is";

    /**
     * cglib动态代理类
     */
    private static final String CGLIB_CLASS_SEPARATOR = "$$";

    /**
     * 代理 class 的名称
     */
    private static final List<String> PROXY_CLASS_NAMES = new ArrayList<>();

    /**
     * 当前类加载器
     */
    private static final ClassLoader CURRENT_CLASS_LOADER;

    /**
     * 基本类型的class
     */
    private static final Class[] BASE_CLASS = new Class[]{byte.class, short.class, int.class, long.class, float.class, double.class, boolean.class, char.class};

    /**
     * 包装类型的class
     */
    private static final Class[] WRAP_CLASS = new Class[]{Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class, Character.class};

    static {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader != null) {
            CURRENT_CLASS_LOADER = contextClassLoader;
        } else {
            CURRENT_CLASS_LOADER = Reflects.class.getClassLoader();
        }
        PROXY_CLASS_NAMES.add("net.sf.cglib.proxy.Factory");
        PROXY_CLASS_NAMES.add("org.springframework.cglib.proxy.Factory");
        PROXY_CLASS_NAMES.add("javassist.util.proxy.ProxyObject");
        PROXY_CLASS_NAMES.add("org.apache.ibatis.javassist.util.proxy.ProxyObject");
    }

    private Reflects() {
    }

    /**
     * 获取类加载器
     *
     * @return 类加载器
     */
    public static ClassLoader getCurrentClassLoader() {
        return CURRENT_CLASS_LOADER;
    }

    /**
     * 加载类
     *
     * @param className 类名
     * @return 类
     */
    public static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 加载类
     *
     * @param className 类名
     * @param init      是否初始化类
     * @return 类
     */
    public static Class<?> loadClass(String className, boolean init) {
        try {
            return Class.forName(className, init, CURRENT_CLASS_LOADER);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 加载类
     *
     * @param className   类名
     * @param init        是否初始化类
     * @param classLoader classLoader
     * @return 类
     */
    public static Class<?> loadClass(String className, boolean init, ClassLoader classLoader) {
        try {
            return Class.forName(className, init, classLoader);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断是否为代理对象
     *
     * @param clazz 传入 class 对象
     * @return 如果对象class是代理 class, 返回 true
     */
    public static boolean isProxy(Class<?> clazz) {
        if (clazz != null) {
            if (clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
                return true;
            }
            for (Class<?> cls : clazz.getInterfaces()) {
                if (PROXY_CLASS_NAMES.contains(cls.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否是jdk动态代理对象
     * 不会判断是否为 SpringProxy
     *
     * @param o ignore
     * @return true 是
     */
    public static boolean isJDKProxy(Object o) {
        return Proxy.isProxyClass(o.getClass());
    }

    /**
     * 是否是cglib动态代理对象
     *
     * @param o ignore
     * @return true 是
     */
    public static boolean isCglibProxy(Object o) {
        return o != null && o.getClass().toString().contains(CGLIB_CLASS_SEPARATOR);
    }

    // -------------------- invoke --------------------

    /**
     * 调用getter方法
     *
     * @param obj       对象
     * @param fieldName 字段名称
     * @param <E>       属性类型
     * @return ignore
     */
    public static <E> E invokeGetter(Object obj, String fieldName) {
        Valid.notNull(obj, "Invoke object is null");
        Valid.notBlank(fieldName, "Invoke Getter Field is null");
        try {
            Field field = getAccessibleField(obj.getClass(), fieldName);
            if (field == null) {
                throw Exceptions.notFound(Strings.format("Field: {} not found", fieldName));
            }
            if (field.getType().equals(boolean.class)) {
                return invokeMethod(obj, BOOLEAN_GETTER_PREFIX + Strings.firstUpper(fieldName), null, null);
            } else {
                return invokeMethod(obj, GETTER_PREFIX + Strings.firstUpper(fieldName), null, null);
            }
        } catch (Exception e) {
            throw Exceptions.invoke(Strings.format("Invoke Field: {} Setter Method error {}", fieldName, e.getMessage()), e);
        }
    }

    /**
     * 调用setter方法
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
     * 调用setter方法, 多级调用需要手动拼接set
     *
     * @param obj                   对象
     * @param fieldSetterMethodName 字段名称
     * @param values                ignore
     * @param <E>                   属性类型
     */
    public static <E, R> R invokeSetter(Object obj, String fieldSetterMethodName, E[] values) {
        Valid.notNull(obj, "Invoke object is null");
        Valid.notBlank(fieldSetterMethodName, "Invoke Setter Method is null");
        String[] names = fieldSetterMethodName.split("\\.");
        if (names.length != Arrays1.length(values)) {
            throw Exceptions.argument("Setting method and parameter length are inconsistent");
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
     * 调用setter方法 类型推断调用
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
     * 直接调用对象方法
     *
     * @param obj            对象
     * @param methodName     方法名称
     * @param parameterTypes 参数列表类型
     * @param args           参数列表
     * @param <E>            返回值类型
     * @return ignore
     */
    public static <E> E invokeMethod(Object obj, String methodName, Class<?>[] parameterTypes, Object[] args) {
        Valid.notNull(obj, "Invoker object is null");
        Valid.notBlank(methodName, "Invoke Method is null");
        Method method = getAccessibleMethod(obj.getClass(), methodName, parameterTypes);
        if (method == null) {
            throw Exceptions.invoke(Strings.format("Method {} not found in class {}", methodName, obj.getClass()));
        }
        try {
            return (E) method.invoke(obj, args);
        } catch (Exception e) {
            throw Exceptions.invoke(Strings.format("Invoke Method error: {}, class: {}, args: {}", methodName, obj.getClass(), Arrays.toString(args)), e);
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
        return invokeMethod(obj, methodName, null);
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
        return invokeMethod(obj, method, null);
    }

    /**
     * 直接调用对象方法
     *
     * @param obj        对象
     * @param methodName 方法名称
     * @param args       参数列表
     * @param <E>        返回值类型
     * @return 对象
     */
    public static <E> E invokeMethod(Object obj, String methodName, Object[] args) {
        Valid.notNull(obj, "Invoker object is null");
        Valid.notBlank(methodName, "Invoke Method is null");
        Method method = getAccessibleMethod(obj.getClass(), methodName, Arrays1.length(args));
        if (method == null) {
            throw Exceptions.invoke(Strings.format("Invoke Method error: {} not found in class {}", methodName, obj.getClass().getName()));
        }
        try {
            return (E) method.invoke(obj, args);
        } catch (Exception e) {
            throw Exceptions.invoke(Strings.format("Invoke Method error: {}, class: {}, args: {}", methodName, obj.getClass().getName(), Arrays.toString(args)), e);
        }
    }

    /**
     * 直接调用对象方法
     *
     * @param obj    对象
     * @param method 方法
     * @param args   参数列表
     * @param <E>    返回值类型
     * @return 对象
     */
    public static <E> E invokeMethod(Object obj, Method method, Object[] args) {
        Valid.notNull(obj, "Invoker object is null");
        Valid.notNull(method, "Invoke Method is null");
        try {
            setAccessible(method);
            return (E) method.invoke(obj, args);
        } catch (Exception e) {
            throw Exceptions.invoke(Strings.format("Invoke Method error: {}, class: {}, args: {}", method.getName(), obj.getClass().getName(), Arrays.toString(args)), e);
        }
    }

    /**
     * 直接调用对象方法, 会进行参数推断, 不推荐使用到多个重载方法且参数列表长度相同的方法
     *
     * @param obj    对象
     * @param method 方法
     * @param args   参数列表
     * @param <E>    返回值类型
     * @return 对象
     */
    public static <E> E invokeMethodInfer(Object obj, Method method, Object[] args) {
        Valid.notNull(obj, "Invoker object is null");
        Valid.notNull(method, "Invoke Method is null");
        if (args == null || args.length == 0) {
            return invokeMethod(obj, method, null);
        }
        try {
            return invokeMethodInfers(obj, method, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw Exceptions.invoke(Strings.format("Invoke Method error: {}, class: {}, args: {}", method.getName(), obj.getClass().getName(), Arrays.toString(args)), e);
        } catch (Exception e) {
            // ignore
        }
        throw Exceptions.invoke(Strings.format("Invoke Method error: {}, class: {}, args: {}", method.getName(), obj.getClass().getName(), Arrays.toString(args)));
    }

    /**
     * 直接调用对象方法, 会进行参数推断, 不推荐使用到多个重载方法且参数列表长度相同的方法
     *
     * @param obj        对象
     * @param methodName 方法名称
     * @param args       参数列表
     * @param <E>        返回值类型
     * @return 对象
     */
    public static <E> E invokeMethodInfer(Object obj, String methodName, Object[] args) {
        Valid.notNull(obj, "Invoker object is null");
        Valid.notBlank(methodName, "Invoke Method is null");
        if (args == null || args.length == 0) {
            return invokeMethod(obj, methodName, null);
        }
        int len = Arrays1.length(args);
        Method[] methods = getAccessibleMethods(obj.getClass(), methodName, len);
        for (Method method : methods) {
            try {
                return invokeMethodInfers(obj, method, Objects1.clone(args));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw Exceptions.invoke(Strings.format("Invoke Method error: {}, class: {}, args: {}", methodName, obj.getClass().getName(), Arrays.toString(args)), e);
            } catch (Exception e) {
                // ignore
            }
        }
        throw Exceptions.invoke(Strings.format("Invoke Method not found : {}, class: {}, args: {}", methodName, obj.getClass().getName(), Arrays.toString(args)));
    }

    /**
     * 直接调用对象方法, 会进行参数推断, 不推荐使用到多个重载方法且参数列表长度相同的方法
     *
     * @param obj    对象
     * @param method 方法名称
     * @param args   参数列表
     * @param <E>    返回值类型
     * @return 对象
     */
    private static <E> E invokeMethodInfers(Object obj, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Class<?>[] params = method.getParameterTypes();
        int len = Arrays1.length(args);
        if (params.length != len) {
            throw Exceptions.invoke(Strings.format("Method {} parameters len is {}, but args length is {}", method.getName(), params.length, len));
        }
        for (int i = 0; i < len; i++) {
            if (args[i] != null) {
                if (!params[i].equals(Object.class) && params[i] != args[i].getClass() && !getInterfaces(args[i].getClass()).contains(params[i])) {
                    args[i] = convert(args[i], params[i]);
                }
            }
        }
        setAccessible(method);
        return (E) method.invoke(obj, args);
    }

    /**
     * 根据class转换object
     *
     * @param o     o
     * @param clazz class
     * @return o
     */
    private static Object convert(Object o, Class<?> clazz) {
        if (clazz.equals(byte.class)) {
            return Converts.toByte(o);
        } else if (clazz.equals(Byte.class)) {
            return Converts.toByte(o);
        } else if (clazz.equals(short.class)) {
            return Converts.toShort(o);
        } else if (clazz.equals(Short.class)) {
            return Converts.toShort(o);
        } else if (clazz.equals(int.class)) {
            return Converts.toInt(o);
        } else if (clazz.equals(Integer.class)) {
            return Converts.toInt(o);
        } else if (clazz.equals(long.class)) {
            return Converts.toLong(o);
        } else if (clazz.equals(Long.class)) {
            return Converts.toLong(o);
        } else if (clazz.equals(float.class)) {
            return Converts.toFloat(o);
        } else if (clazz.equals(Float.class)) {
            return Converts.toFloat(o);
        } else if (clazz.equals(double.class)) {
            return Converts.toDouble(o);
        } else if (clazz.equals(Double.class)) {
            return Converts.toDouble(o);
        } else if (clazz.equals(boolean.class)) {
            return Converts.toBoolean(o);
        } else if (clazz.equals(Boolean.class)) {
            return Converts.toBoolean(o);
        } else if (clazz.equals(char.class)) {
            return Converts.toChar(o);
        } else if (clazz.equals(Character.class)) {
            return Converts.toChar(o);
        } else if (clazz.equals(String.class)) {
            return Converts.toString(o);
        } else if (clazz.equals(Date.class)) {
            return Converts.toDate(o);
        } else if (clazz.equals(BigDecimal.class)) {
            return Decimals.toDecimal(o);
        } else if (clazz.equals(BigInteger.class)) {
            return BigIntegers.toBigInteger(o);
        }
        return o;
    }

    // -------------------- method --------------------

    /**
     * 通过方法获取所有的getter方法
     *
     * @param clazz class
     * @return get方法
     */
    public static List<Method> getAllGetterMethod(Class<?> clazz) {
        List<Method> list = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            if (!"getClass".equals(method.getName()) && !Modifier.isStatic(method.getModifiers()) && method.getParameters().length == 0) {
                String name = method.getName();
                if (name.startsWith("get") && name.length() != 3 && !"void".equals(method.getReturnType().getName())) {
                    setAccessible(method);
                    list.add(method);
                } else if (method.getName().startsWith("is") && method.getReturnType().equals(boolean.class)) {
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
     * @return set方法
     */
    public static List<Method> getAllSetterMethod(Class<?> clazz) {
        List<Method> list = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            String name = method.getName();
            if (name.startsWith("set") && name.length() != 3 && !Modifier.isStatic(method.getModifiers()) && method.getParameters().length == 1) {
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
     * @return get方法
     */
    public static List<Method> getAllGetterMethodByField(Class<?> clazz) {
        List<Method> list = new ArrayList<>();
        List<Field> fields = getFieldList(clazz);
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                String methodName;
                if (field.getType().equals(boolean.class)) {
                    methodName = "is" + Strings.firstUpper(field.getName());
                } else {
                    methodName = "get" + Strings.firstUpper(field.getName());
                }
                Method method = getAccessibleMethod(clazz, methodName, 0);
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
     * @return set方法
     */
    public static List<Method> getAllSetterMethodByField(Class<?> clazz) {
        List<Method> list = new ArrayList<>();
        List<Field> fields = getFieldList(clazz);
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                String methodName = "set" + Strings.firstUpper(field.getName());
                try {
                    list.add(getAccessibleMethod(clazz, methodName, field.getType()));
                } catch (Exception e) {
                    // ignore
                }
            }
        }
        return list;
    }

    /**
     * 获取对象的DeclaredMethod, 并强制设置为可访问
     *
     * @param clazz          class
     * @param methodName     方法名
     * @param parameterTypes 参数类型
     * @return 方法对象
     */
    public static Method getAccessibleMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        Valid.notNull(clazz, "Method class is null");
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
     * 获取对象匹配参数长度的第一个DeclaredMethod, 并强制设置为可访问
     *
     * @param clazz      class
     * @param methodName 方法名称
     * @param argsNum    参数数量
     * @return 方法对象
     */
    public static Method getAccessibleMethod(Class<?> clazz, String methodName, int argsNum) {
        Valid.notNull(clazz, "Method class is null");
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
     * 获取对象匹配参数长度的DeclaredMethod, 并强制设置为可访问
     *
     * @param clazz      class
     * @param methodName 方法名称
     * @param argsNum    参数数量
     * @return 方法对象
     */
    public static Method[] getAccessibleMethods(Class<?> clazz, String methodName, int argsNum) {
        Valid.notNull(clazz, "Method class is null");
        int i = 0;
        Method[] methods = new Method[0];
        for (Class<?> searchType = clazz; searchType != Object.class; searchType = searchType.getSuperclass()) {
            Method[] searchMethods = searchType.getDeclaredMethods();
            for (Method method : searchMethods) {
                if (method.getName().equals(methodName) && method.getParameterTypes().length == argsNum) {
                    int len = methods.length;
                    if (i + 1 >= len) {
                        methods = Arrays1.resize(methods, len + 2, Method[]::new);
                    }
                    setAccessible(method);
                    methods[i++] = method;
                }
            }
        }
        if (i != methods.length) {
            methods = Arrays1.resize(methods, i, Method[]::new);
        }
        return methods;
    }

    /**
     * 获取该类的所有方法列表
     *
     * @param clazz 反射类
     * @return 方法
     */
    public static Map<String, Method> getMethodMap(Class<?> clazz) {
        List<Method> methodList = getMethodList(clazz);
        return Lists.isNotEmpty(methodList) ? methodList.stream().collect(Collectors.toMap(Method::getName, identity())) : new HashMap<>();
    }

    /**
     * 获取该类的所有方法
     *
     * @param clazz 反射类
     * @return 方法
     */
    public static List<Method> getMethodList(Class<?> clazz) {
        Valid.notNull(clazz, "Method class is null");
        if (clazz.getSuperclass() != null) {
            List<Method> methodList = Stream.of(clazz.getDeclaredMethods())
                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
                    .collect(toCollection(ArrayList::new));
            Class<?> superClass = clazz.getSuperclass();
            // 当前类方法
            Map<String, Method> methodMap = methodList.stream().collect(toMap(Method::getName, identity()));
            // 父类方法
            getMethodList(superClass).stream().filter(m -> !methodMap.containsKey(m.getName())).forEach(methodList::add);
            return methodList;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 设置方法可访问
     */
    public static void setAccessible(Method method) {
        Valid.notNull(method, "Set Accessible Method class is null");
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    // -------------------- field --------------------

    /**
     * 直接读取对象属性值
     *
     * @param obj       对象
     * @param fieldName 字段名称
     * @param <E>       属性类型
     * @return 对象
     */
    public static <E> E getFieldValue(Object obj, String fieldName) {
        Valid.notNull(obj, "Invoker object is null");
        Valid.notBlank(fieldName, "Invoke Field is null");
        Field field = getAccessibleField(obj.getClass(), fieldName);
        if (field == null) {
            throw Exceptions.invoke(Strings.format("Get Field Value not found field: {}, class {}", fieldName, obj.getClass().getName()));
        }
        try {
            return (E) field.get(obj);
        } catch (Exception e) {
            throw Exceptions.invoke(Strings.format("Get Field Value error: {}, field: {}, class: {}", e.getMessage(), fieldName, obj.getClass().getName()), e);
        }
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
        Valid.notNull(obj, "Invoker object is null");
        Valid.notNull(field, "Invoke Field is null");
        try {
            setAccessible(field);
            return (E) field.get(obj);
        } catch (Exception e) {
            throw Exceptions.invoke(Strings.format("Get Field Value error: {}, field: {}, class: {}", e.getMessage(), field.getName(), obj.getClass().getName()), e);
        }
    }

    /**
     * 直接设置对象属性值
     *
     * @param obj       对象
     * @param fieldName 字段名称
     * @param value     对象值
     * @param <E>       属性类型
     */
    public static <E> void setFieldValue(Object obj, String fieldName, E value) {
        Valid.notNull(obj, "Invoker object is null");
        Valid.notBlank(fieldName, "Invoke Field is null");
        Field field = getAccessibleField(obj.getClass(), fieldName);
        if (field == null) {
            throw Exceptions.invoke(Strings.format("Set Field Value not found field: {}, class {}", fieldName, obj.getClass().getName()));
        }
        try {
            field.set(obj, value);
        } catch (Exception e) {
            throw Exceptions.invoke(Strings.format("Set Field Value error: {}, field: {}, class: {}", e.getMessage(), fieldName, obj.getClass().getName(), value), e);
        }
    }

    /**
     * 直接设置对象属性值
     *
     * @param obj   对象
     * @param field 字段
     * @param value 对象值
     * @param <E>   属性类型
     */
    public static <E> void setFieldValue(Object obj, Field field, E value) {
        Valid.notNull(obj, "Invoker object is null");
        Valid.notNull(field, "Invoke Field is null");
        try {
            setAccessible(field);
            field.set(obj, value);
        } catch (Exception e) {
            throw Exceptions.invoke(Strings.format("Set Field Value error: {}, field: {}, class: {}", e.getMessage(), field.getName(), obj.getClass().getName(), value), e);
        }
    }

    /**
     * 获取该类的所有属性列表
     *
     * @param clazz 反射类
     */
    public static Map<String, Field> getFieldMap(Class<?> clazz) {
        List<Field> fieldList = getFieldList(clazz);
        return Lists.isNotEmpty(fieldList) ? fieldList.stream().collect(Collectors.toMap(Field::getName, field -> field)) : Collections.emptyMap();
    }

    /**
     * 获取该类的所有属性列表
     *
     * @param clazz 反射类
     * @return 属性
     */
    public static List<Field> getFieldList(Class<?> clazz) {
        Valid.notNull(clazz, "Field class is null");
        if (clazz.getSuperclass() != null) {
            List<Field> fieldList = Stream.of(clazz.getDeclaredFields())
                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
                    .filter(field -> !Modifier.isTransient(field.getModifiers()))
                    .collect(toCollection(ArrayList::new));
            Class<?> superClass = clazz.getSuperclass();
            // 当前类属性
            Map<String, Field> fieldMap = fieldList.stream().collect(toMap(Field::getName, identity()));
            // 父类属性
            getFieldList(superClass).stream().filter(field -> !fieldMap.containsKey(field.getName())).forEach(fieldList::add);
            return fieldList;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 获取对象的DeclaredField, 并强制设置为可访问
     *
     * @param clazz     class
     * @param fieldName 字段名称
     * @return 字段对象
     */
    public static Field getAccessibleField(Class<?> clazz, String fieldName) {
        Valid.notNull(clazz, "Field class is null");
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
        Valid.notNull(field, "Set Accessible Field class is null");
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    // -------------------- constructor --------------------

    /**
     * 获取默认构造方法
     *
     * @param clazz class
     * @return 构造方法
     */
    public static <T> Constructor<T> getDefaultConstructor(Class<T> clazz) {
        Valid.notNull(clazz, "Class is null");
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取构造方法
     *
     * @param clazz          class
     * @param parameterTypes 参数类型
     * @param <T>            ignore
     * @return 构造方法
     */
    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class[] parameterTypes) {
        Valid.notNull(clazz, "Class is null");
        if (Arrays1.length(parameterTypes) == 0) {
            return getDefaultConstructor(clazz);
        }
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取构造方法
     *
     * @param clazz class
     * @param len   参数长度
     * @param <T>   ignore
     * @return 构造方法
     */
    public static <T> Constructor<T> getConstructor(Class<T> clazz, int len) {
        Valid.notNull(clazz, "Class is null");
        if (len == 0) {
            return getDefaultConstructor(clazz);
        }
        try {
            Constructor<?>[] constructors = clazz.getConstructors();
            for (Constructor<?> constructor : constructors) {
                if (constructor.getParameterTypes().length == len) {
                    constructor.setAccessible(true);
                    return (Constructor<T>) constructor;
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    /**
     * 获取构造方法
     *
     * @param clazz class
     * @param len   参数长度
     * @param <T>   ignore
     * @return 构造方法
     */
    public static <T> List<Constructor<T>> getConstructors(Class<T> clazz, int len) {
        Valid.notNull(clazz, "Class is null");
        List<Constructor<T>> list = new ArrayList<>();
        if (len == 0) {
            list.add(getDefaultConstructor(clazz));
            return list;
        }
        try {
            Constructor<?>[] constructors = clazz.getConstructors();
            for (Constructor<?> constructor : constructors) {
                if (constructor.getParameterTypes().length == len) {
                    constructor.setAccessible(true);
                    list.add((Constructor<T>) constructor);
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return list;
    }

    /**
     * 实例化对象
     *
     * @param constructor constructor
     * @param <T>         类实例型
     * @return 实例
     */
    public static <T> T newInstance(Constructor<T> constructor) {
        Valid.notNull(constructor, "Constructor is null");
        try {
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw Exceptions.invoke("Cannot initialize class", e);
        }
    }

    /**
     * 实例化对象
     *
     * @param constructor constructor
     * @param values      参数
     * @param <T>         类实例型
     * @return 实例
     */
    public static <T> T newInstance(Constructor<T> constructor, Object[] values) {
        Valid.notNull(constructor, "Constructor is null");
        try {
            constructor.setAccessible(true);
            return constructor.newInstance(values);
        } catch (Exception e) {
            throw Exceptions.invoke("Cannot initialize class", e);
        }
    }

    /**
     * 实例化对象
     *
     * @param clazz 需要实例化的对象
     * @param <T>   类实例型
     * @return 实例
     */
    public static <T> T newInstance(Class<T> clazz) {
        Valid.notNull(clazz, "Class is null");
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw Exceptions.invoke(Strings.format("Cannot initialize class: {}", clazz.getName()), e);
        }
    }

    /**
     * 实例化对象
     *
     * @param clazz          需要实例化的对象
     * @param parameterTypes 参数类型
     * @param values         参数
     * @param <T>            类实例型
     * @return 实例
     */
    public static <T> T newInstance(Class<T> clazz, Class[] parameterTypes, Object[] values) {
        Valid.notNull(clazz, "Class is null");
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor.newInstance(values);
        } catch (Exception e) {
            throw Exceptions.invoke(Strings.format("Cannot initialize class: {}, parameterTypes: {}, values: {}", clazz.getName(), Arrays.toString(parameterTypes), Arrays.toString(values)), e);
        }
    }

    /**
     * 实例化对象 参数类型推断, 不推荐使用到多个构造方法参数列表长度相同的类
     *
     * @param clazz 需要实例化的对象
     * @param <T>   类实例型
     * @return 实例
     */
    public static <T> T newInstanceInfer(Class<T> clazz, Object[] args) {
        Valid.notNull(clazz, "Class is null");
        if (args == null || args.length == 0) {
            return newInstance(clazz);
        }
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            try {
                return (T) newInstanceInfers(constructor, Objects1.clone(args));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw Exceptions.invoke(Strings.format("Cannot initialize class: {}, values: {}", clazz.getName(), Arrays.toString(args)), e);
            } catch (Exception e) {
                // ignore
            }
        }
        throw Exceptions.invoke(Strings.format("Cannot initialize class not found constructor: {}, values: {}", clazz.getName(), Arrays.toString(args)));
    }

    /**
     * 实例化对象 参数类型推断, 不推荐使用到多个构造方法参数列表长度相同的类
     *
     * @param constructor constructor
     * @param <T>         类实例型
     * @return 实例
     */
    public static <T> T newInstanceInfer(Constructor<T> constructor, Object[] args) {
        try {
            return newInstanceInfers(constructor, args);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw Exceptions.invoke(Strings.format("Cannot initialize class values: {}", Arrays.toString(args)));
        }
    }

    /**
     * 实例化对象 参数类型推断, 不推荐使用到多个构造方法参数列表长度相同的类
     *
     * @param constructor constructor
     * @param <T>         类实例型
     * @return 实例
     */
    private static <T> T newInstanceInfers(Constructor<T> constructor, Object[] args) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?>[] params = constructor.getParameterTypes();
        int len = Arrays1.length(args);
        if (params.length != len) {
            throw Exceptions.invoke(Strings.format("Constructor parameters len is {}, but args length is {}", params.length, len));
        }
        for (int i = 0; i < len; i++) {
            if (args[i] != null) {
                if (!params[i].equals(Object.class) && params[i] != args[i].getClass() && !getInterfaces(args[i].getClass()).contains(params[i])) {
                    args[i] = convert(args[i], params[i]);
                }
            }
        }
        constructor.setAccessible(true);
        return constructor.newInstance(args);
    }

    // -------------------- annotation --------------------

    /**
     * 判断类是否有指定注解
     *
     * @param clazz          class
     * @param annotatedClass 注解类
     * @return ignore
     */
    public static boolean classHasAnnotated(Class<?> clazz, Class<? extends Annotation> annotatedClass) {
        Valid.notNull(clazz, "Class is null");
        Valid.notNull(annotatedClass, "AnnotatedClass is null");
        return clazz.getDeclaredAnnotation(annotatedClass) != null;
    }

    /**
     * 判断类是否有所有指定注解
     *
     * @param clazz            class
     * @param annotatedClasses 注解类
     * @return ignore
     */
    public static boolean classHasAnnotated(Class<?> clazz, Class<? extends Annotation>... annotatedClasses) {
        Valid.notNull(clazz, "Class is null");
        Valid.notEmpty(annotatedClasses, "AnnotatedClasses length is 0");
        if (annotatedClasses.length == 1) {
            return clazz.getDeclaredAnnotation(annotatedClasses[0]) != null;
        } else {
            return checkHasAnnotated(clazz.getDeclaredAnnotations(), annotatedClasses);
        }
    }

    /**
     * 判断构造方法是否有指定注解
     *
     * @param constructor    构造方法
     * @param annotatedClass 注解类
     * @return ignore
     */
    public static boolean constructorHasAnnotated(Constructor constructor, Class<? extends Annotation> annotatedClass) {
        Valid.notNull(constructor, "Constructor is null");
        Valid.notNull(annotatedClass, "AnnotatedClass is null");
        return constructor.getDeclaredAnnotation(annotatedClass) != null;
    }

    /**
     * 判断构造方法是否有所有指定注解
     *
     * @param constructor      构造方法
     * @param annotatedClasses 注解类
     * @return ignore
     */
    public static boolean constructorHasAnnotated(Constructor constructor, Class<? extends Annotation>... annotatedClasses) {
        Valid.notNull(constructor, "Constructor is null");
        Valid.notEmpty(annotatedClasses, "AnnotatedClasses length is 0");
        if (annotatedClasses.length == 1) {
            return constructor.getDeclaredAnnotation(annotatedClasses[0]) != null;
        } else {
            return checkHasAnnotated(constructor.getDeclaredAnnotations(), annotatedClasses);
        }
    }

    /**
     * 判断方法是否有指定注解
     *
     * @param method         方法
     * @param annotatedClass 注解类
     * @return ignore
     */
    public static boolean methodHasAnnotated(Method method, Class<? extends Annotation> annotatedClass) {
        Valid.notNull(method, "Method is null");
        Valid.notNull(annotatedClass, "AnnotatedClass is null");
        return method.getDeclaredAnnotation(annotatedClass) != null;
    }

    /**
     * 判断方法是否有所有指定注解
     *
     * @param method           方法
     * @param annotatedClasses 注解类
     * @return ignore
     */
    public static boolean methodHasAnnotated(Method method, Class<? extends Annotation>... annotatedClasses) {
        Valid.notNull(method, "Method is null");
        Valid.notEmpty(annotatedClasses, "AnnotatedClasses length is 0");
        if (annotatedClasses.length == 1) {
            return method.getDeclaredAnnotation(annotatedClasses[0]) != null;
        } else {
            return checkHasAnnotated(method.getDeclaredAnnotations(), annotatedClasses);
        }
    }

    /**
     * 判断字段是否有指定注解
     *
     * @param field          字段
     * @param annotatedClass 注解类
     * @return ignore
     */
    public static boolean fieldHasAnnotated(Field field, Class<? extends Annotation> annotatedClass) {
        Valid.notNull(field, "Field is null");
        Valid.notNull(annotatedClass, "AnnotatedClass is null");
        return field.getDeclaredAnnotation(annotatedClass) != null;
    }

    /**
     * 判断字段是否有所有指定注解
     *
     * @param field            字段
     * @param annotatedClasses 注解类
     * @return ignore
     */
    public static boolean fieldHasAnnotated(Field field, Class<? extends Annotation>... annotatedClasses) {
        Valid.notNull(field, "Field is null");
        Valid.notEmpty(annotatedClasses, "AnnotatedClasses length is 0");
        if (annotatedClasses.length == 1) {
            return field.getDeclaredAnnotation(annotatedClasses[0]) != null;
        } else {
            return checkHasAnnotated(field.getDeclaredAnnotations(), annotatedClasses);
        }
    }

    /**
     * 判断注解集是否包含指定注解集
     *
     * @param annotations      注解集
     * @param annotatedClasses 指定注解集
     * @return true包含
     */
    private static boolean checkHasAnnotated(Annotation[] annotations, Class<? extends Annotation>[] annotatedClasses) {
        for (Class<? extends Annotation> annotatedClass : annotatedClasses) {
            boolean has = false;
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(annotatedClass)) {
                    has = true;
                    break;
                }
            }
            if (!has) {
                return false;
            }
        }
        return true;
    }

    // -------------------- class --------------------

    /**
     * 获得对象的父类
     *
     * @param clazz class
     * @return 父类对象
     */
    public static Class<?> getSuperClass(Class<?> clazz) {
        Valid.notNull(clazz, "Class is null");
        Class<?> superClass = clazz.getSuperclass();
        if (!clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            return superClass;
        } else {
            return Object.class;
        }
    }

    /**
     * 获得对象的所有父类, 不包括Object
     *
     * @param clazz class
     * @return 父类对象
     */
    public static List<Class<?>> getSuperClasses(Class<?> clazz) {
        Valid.notNull(clazz, "Class is null");
        if (!clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            List<Class<?>> list = new ArrayList<>();
            for (Class<?> superClass = clazz.getSuperclass(); superClass != null && superClass != Object.class; superClass = superClass.getSuperclass()) {
                list.add(superClass);
            }
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 获取所有接口, 如果是接口, 则返回自己
     *
     * @param clazz 类
     * @return 接口
     */
    public static List<Class<?>> getInterfaces(Class<?> clazz) {
        Valid.notNull(clazz, "Class is null");
        if (clazz.isInterface()) {
            return Lists.of(clazz);
        }
        Set<Class<?>> interfaces = new HashSet<>();
        while (clazz != null) {
            Class<?>[] ins = clazz.getInterfaces();
            for (Class<?> in : ins) {
                interfaces.addAll(getInterfaces(in));
            }
            clazz = clazz.getSuperclass();
        }
        return new ArrayList<>(interfaces);
    }

    /**
     * 判断是否是接口
     *
     * @param clazz class
     * @return true接口
     */
    public static boolean isInterface(Class<?> clazz) {
        Valid.notNull(clazz, "Class is null");
        return Modifier.isInterface(clazz.getModifiers());
    }

    /**
     * 判断argClass是否为requireClass的实现类
     *
     * @param requireClass paramClass
     * @param argClass     argClass
     * @return true 是实现类或本类
     */
    public static boolean isImplClass(Class requireClass, Class argClass) {
        Valid.notNull(requireClass, "RequireClass is null");
        Valid.notNull(argClass, "ArgClass is null");
        if (requireClass.equals(argClass) || requireClass.equals(Object.class)) {
            return true;
        }
        List<Class<?>> list = getInterfaces(argClass);
        if (list.isEmpty() || !list.contains(requireClass)) {
            list = getSuperClasses(argClass);
        }
        return list.contains(requireClass);
    }

    /**
     * 判断对象是否是数组
     *
     * @param clazz class
     * @return true数组
     */
    public static boolean isArray(Class clazz) {
        Valid.notNull(clazz, "Class is null");
        return clazz.isArray();
    }

    /**
     * 判断是否为基本类型
     *
     * @param clazz class
     * @return 基本类型true
     */
    public static boolean isBaseClass(Class clazz) {
        Valid.notNull(clazz, "Class is null");
        for (Class baseClass : BASE_CLASS) {
            if (clazz.equals(baseClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为包装类型
     *
     * @param clazz class
     * @return 包装类型true
     */
    public static boolean isWrapClass(Class clazz) {
        Valid.notNull(clazz, "Class is null");
        for (Class wrapClass : WRAP_CLASS) {
            if (clazz.equals(wrapClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回基本类型的包装类型
     *
     * @param clazz class
     * @return class
     */
    public static Class getWrapClass(Class clazz) {
        Valid.notNull(clazz, "Class is null");
        for (int i = 0; i < BASE_CLASS.length; i++) {
            if (clazz.equals(BASE_CLASS[i])) {
                return WRAP_CLASS[i];
            }
        }
        return clazz;
    }

    // -------------------- static --------------------

    /**
     * 获取所有static的方法
     *
     * @param clazz 类
     * @return static方法
     */
    public static List<Method> getStaticMethods(Class<?> clazz) {
        Valid.notNull(clazz, "Class is null");
        Method[] methods = clazz.getDeclaredMethods();
        return Arrays.stream(methods)
                .filter(m -> Modifier.isStatic(m.getModifiers()))
                .collect(toCollection(ArrayList::new));
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

    // -------------------- jar --------------------

    /**
     * 获取target目录URL
     *
     * @return targetUrl / thisJar
     */
    public static URL getTargetURL() {
        URL resource = Reflects.class.getClassLoader().getResource("");
        if (resource == null) {
            resource = Reflects.class.getResource("");
        }
        return resource;
    }

    /**
     * 获取当前jar文件, 如果不是jar环境返回null
     *
     * @return 当前 jarFile
     */
    public static JarFile getJarFile() {
        URL targetURL = getTargetURL();
        if (targetURL != null && "jar".equals(targetURL.getProtocol())) {
            try {
                return ((JarURLConnection) targetURL.openConnection()).getJarFile();
            } catch (Exception e) {
                // ignore
            }
        }
        return null;
    }

    /**
     * 获取jar文件
     *
     * @param file jar文件
     * @return jarFile
     */
    public static JarFile getJarFile(File file) {
        try {
            return new JarFile(file);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取jar文件
     *
     * @param url url 支持jar file
     * @return jarFile
     */
    public static JarFile getJarFile(URL url) {
        String protocol = url.getProtocol();
        try {
            switch (protocol) {
                case "jar":
                    return ((JarURLConnection) url.openConnection()).getJarFile();
                case "file":
                    return new JarFile(url.getFile());
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从jar获取所有类
     *
     * @param jarPath jar
     * @return 类名
     */
    public static List<String> getClassByJar(String jarPath) {
        Valid.notBlank(jarPath, "Jar Path is null");
        List<String> classes = new ArrayList<>();
        try (JarFile jarFile = new JarFile(jarPath)) {
            return getClassByJar(jarFile);
        } catch (IOException e) {
            // ignore
        }
        return classes;
    }

    /**
     * 从jar获取所有类
     *
     * @param jarFile jar
     * @return 类名
     */
    public static List<String> getClassByJar(JarFile jarFile) {
        Valid.notNull(jarFile, "Jar File is null");
        List<String> classes = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            String className = entries.nextElement().getName();
            if (className.endsWith(".class")) {
                classes.add(className.replace("/", ".").substring(0, className.lastIndexOf(".")));
            }
        }
        return classes;
    }

    /**
     * 从jar中获取除class的所有文件
     *
     * @param jarPath jar
     * @return 资源名称
     */
    public static List<String> getSourceByJar(String jarPath) {
        Valid.notBlank(jarPath, "Jar Path is null");
        List<String> sources = new ArrayList<>();
        try (JarFile jarFile = new JarFile(jarPath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                String fileName = entries.nextElement().getName();
                if (!fileName.endsWith(".class") && !fileName.endsWith("/")) {
                    sources.add(Files1.getPath(jarPath + "!/" + fileName));
                }
            }
        } catch (IOException e) {
            // ignore
        }
        return sources;
    }

    /**
     * 从jar中获取除class的所有文件
     *
     * @param jarFile jar
     * @return 资源名称
     */
    public static List<String> getSourceByJar(JarFile jarFile) {
        Valid.notNull(jarFile, "jar File is null");
        List<String> sources = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            String fileName = entries.nextElement().getName();
            if (!fileName.endsWith(".class") && !fileName.endsWith("/")) {
                sources.add("/" + fileName);
            }
        }
        return sources;
    }

    /**
     * 从jar中获取文件
     *
     * @param jarPath jar
     * @param suffix  后缀
     * @return 资源名称
     */
    public static List<String> getSourceByJar(String jarPath, String suffix) {
        Valid.notBlank(jarPath, "Jar Path is null");
        List<String> sources = new ArrayList<>();
        try (JarFile jarFile = new JarFile(jarPath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                String fileName = entries.nextElement().getName();
                if (suffix != null && fileName.endsWith(suffix) && !fileName.endsWith("/")) {
                    sources.add(Files1.getPath(jarPath + "!/" + fileName));
                }
            }
        } catch (IOException e) {
            // ignore
        }
        return sources;
    }

    /**
     * 从jar中获取文件
     *
     * @param jarFile jar
     * @param suffix  后缀
     * @return 资源名称
     */
    public static List<String> getSourceByJar(JarFile jarFile, String suffix) {
        Valid.notNull(jarFile, "Jar File is null");
        List<String> sources = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            String fileName = entries.nextElement().getName();
            if (suffix != null && fileName.endsWith(suffix) && !fileName.endsWith("/")) {
                sources.add("/" + fileName);
            }
        }
        return sources;
    }

    // -------------------- package --------------------

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
