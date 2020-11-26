package com.orion.utils.reflect;

import com.orion.lang.collect.MutableHashMap;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 反射 注解工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/5/15 13:18
 */
@SuppressWarnings("ALL")
public class Annotations {

    private Annotations() {
    }

    /**
     * 判断类是否有所有指定注解
     *
     * @param clazz            class
     * @param annotatedClasses 注解类
     * @return ignore
     */
    @SafeVarargs
    public static boolean present(Class<?> clazz, Class<? extends Annotation>... annotatedClasses) {
        Valid.notNull(clazz, "Class is null");
        Valid.notEmpty(annotatedClasses, "AnnotatedClasses length is 0");
        if (annotatedClasses.length == 1) {
            return clazz.isAnnotationPresent(annotatedClasses[0]);
        } else {
            return Arrays.stream(annotatedClasses).allMatch(clazz::isAnnotationPresent);
        }
    }

    /**
     * 判断构造方法是否有所有指定注解
     *
     * @param constructor      构造方法
     * @param annotatedClasses 注解类
     * @return ignore
     */
    @SafeVarargs
    public static boolean present(Constructor<?> constructor, Class<? extends Annotation>... annotatedClasses) {
        Valid.notNull(constructor, "Constructor is null");
        Valid.notEmpty(annotatedClasses, "AnnotatedClasses length is 0");
        if (annotatedClasses.length == 1) {
            return constructor.isAnnotationPresent(annotatedClasses[0]);
        } else {
            return Arrays.stream(annotatedClasses).allMatch(constructor::isAnnotationPresent);
        }
    }

    /**
     * 判断方法是否有所有指定注解
     *
     * @param method           方法
     * @param annotatedClasses 注解类
     * @return ignore
     */
    @SafeVarargs
    public static boolean present(Method method, Class<? extends Annotation>... annotatedClasses) {
        Valid.notNull(method, "Method is null");
        Valid.notEmpty(annotatedClasses, "AnnotatedClasses length is 0");
        if (annotatedClasses.length == 1) {
            return method.isAnnotationPresent(annotatedClasses[0]);
        } else {
            return Arrays.stream(annotatedClasses).allMatch(method::isAnnotationPresent);
        }
    }

    /**
     * 判断字段是否有所有指定注解
     *
     * @param field            字段
     * @param annotatedClasses 注解类
     * @return ignore
     */
    @SafeVarargs
    public static boolean present(Field field, Class<? extends Annotation>... annotatedClasses) {
        Valid.notNull(field, "Field is null");
        Valid.notEmpty(annotatedClasses, "AnnotatedClasses length is 0");
        if (annotatedClasses.length == 1) {
            return field.isAnnotationPresent(annotatedClasses[0]);
        } else {
            return Arrays.stream(annotatedClasses).allMatch(field::isAnnotationPresent);
        }
    }

    /**
     * Annotation -> @
     *
     * @param a   Annotation
     * @param <A> A
     * @return @
     */
    public static <A extends Annotation> A cast(Annotation a) {
        return ((A) a);
    }

    /**
     * 获取注解对应的value
     *
     * @param annotated 注解
     * @param <A>       A
     * @param <E>       E
     * @return 属性值
     */
    public static <A extends Annotation, E> E getValue(A annotated) {
        return getAttribute(annotated, "value");
    }

    /**
     * 获取注解对应的属性值
     *
     * @param annotated     注解
     * @param attributeName 属性名
     * @param <A>           A
     * @param <E>           E
     * @return 属性值
     */
    public static <A extends Annotation, E> E getAttribute(A annotated, String attributeName) {
        try {
            Method method = annotated.annotationType().getDeclaredMethod(attributeName);
            method.setAccessible(true);
            return (E) method.invoke(annotated);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取注解所有的属性
     *
     * @param annotated 注解
     * @param <A>       A
     * @return ignore
     */
    public static <A extends Annotation> MutableHashMap<String, Object> getAttributes(A annotated) {
        MutableHashMap<String, Object> attrs = new MutableHashMap<>();
        Method[] methods = annotated.annotationType().getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            if (method.getParameterTypes().length != 0 || method.getReturnType() == Void.TYPE) {
                continue;
            }
            try {
                attrs.put(method.getName(), method.invoke(annotated));
            } catch (Exception e) {
                throw Exceptions.invoke("Could not obtain annotation attribute values", e);
            }
        }
        return attrs;
    }

    /**
     * 获取注解value的默认值
     *
     * @param annotated 注解\
     * @param <A>       A
     * @param <E>       E
     * @return 属性默认值
     */
    public static <A extends Annotation, E> E getDefaultValue(A annotated) {
        return getDefaultValue(annotated.annotationType(), "value");
    }

    /**
     * 获取注解属性的默认值
     *
     * @param annotated     注解
     * @param attributeName 属性名
     * @param <A>           A
     * @param <E>           E
     * @return 属性默认值
     */
    public static <A extends Annotation, E> E getDefaultValue(A annotated, String attributeName) {
        return getDefaultValue(annotated.annotationType(), attributeName);
    }

    /**
     * 获取注解value的默认值
     *
     * @param annotatedClass 注解类
     * @param <E>            E
     * @return 属性默认值
     */
    public static <E> E getDefaultValue(Class<? extends Annotation> annotatedClass) {
        return getDefaultValue(annotatedClass, "value");
    }

    /**
     * 获取注解属性的默认值
     *
     * @param annotatedClass 注解类
     * @param attributeName  属性名
     * @param <E>            E
     * @return 属性默认值
     */
    public static <E> E getDefaultValue(Class<? extends Annotation> annotatedClass, String attributeName) {
        try {
            Method method = annotatedClass.getDeclaredMethod(attributeName);
            method.setAccessible(true);
            return (E) method.getDefaultValue();
        } catch (Exception e) {
            return null;
        }
    }

}
