package com.orion.utils.reflect;

import com.orion.utils.Exceptions;
import com.orion.utils.Valid;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 反射 注解工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/5/15 13:18
 */
public class Annotations {

    private Annotations() {
    }

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
    @SafeVarargs
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
    public static boolean constructorHasAnnotated(Constructor<?> constructor, Class<? extends Annotation> annotatedClass) {
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
    @SafeVarargs
    public static boolean constructorHasAnnotated(Constructor<?> constructor, Class<? extends Annotation>... annotatedClasses) {
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
    @SafeVarargs
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
    @SafeVarargs
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

    /**
     * 获取注解所有的属性
     *
     * @param annotated 注解
     * @return ignore
     */
    public static Map<String, Object> getAttributes(Annotation annotated) {
        Map<String, Object> attrs = new HashMap<>();
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
     * 获取注解对应的value
     *
     * @param annotated 注解
     * @return 属性值
     */
    public static Object getValue(Annotation annotated) {
        return getAttribute(annotated, "value");
    }

    /**
     * 获取注解对应的属性值
     *
     * @param annotated     注解
     * @param attributeName 属性名
     * @return 属性值
     */
    public static Object getAttribute(Annotation annotated, String attributeName) {
        try {
            Method method = annotated.annotationType().getDeclaredMethod(attributeName);
            method.setAccessible(true);
            return method.invoke(annotated);
        } catch (Exception e) {
            Exceptions.printStacks(e);
            return null;
        }
    }

    /**
     * 获取注解value的默认值
     *
     * @param annotated 注解
     * @return 属性默认值
     */
    public static Object getDefaultValue(Annotation annotated) {
        return getAttributeDefaultValue(annotated.annotationType(), "value");
    }

    /**
     * 获取注解属性的默认值
     *
     * @param annotated     注解
     * @param attributeName 属性名
     * @return 属性默认值
     */
    public static Object getDefaultValue(Annotation annotated, String attributeName) {
        return getAttributeDefaultValue(annotated.annotationType(), attributeName);
    }

    /**
     * 获取注解value的默认值
     *
     * @param annotatedClass 注解类
     * @return 属性默认值
     */
    public static Object getAttributeDefaultValue(Class<? extends Annotation> annotatedClass) {
        return getAttributeDefaultValue(annotatedClass, "value");
    }

    /**
     * 获取注解属性的默认值
     *
     * @param annotatedClass 注解类
     * @param attributeName  属性名
     * @return 属性默认值
     */
    public static Object getAttributeDefaultValue(Class<? extends Annotation> annotatedClass, String attributeName) {
        try {
            Method method = annotatedClass.getDeclaredMethod(attributeName);
            method.setAccessible(true);
            return method.getDefaultValue();
        } catch (Exception e) {
            Exceptions.printStacks(e);
            return null;
        }
    }

}
