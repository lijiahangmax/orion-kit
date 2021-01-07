package com.orion.utils.reflect;

import com.orion.lang.collect.MutableHashMap;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.collect.Lists;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    // ------------------ class ------------------

    /**
     * 通过类型 获取class上的注解
     *
     * @param clazz      class
     * @param annotation 注解class
     * @param <A>        A
     * @return 注解
     */
    public static <A extends Annotation> A getAnnotation(Class<?> clazz, Class<A> annotation) {
        return clazz.getDeclaredAnnotation(annotation);
    }

    /**
     * 通过类型 获取class上的所有注解
     *
     * @param clazz      class
     * @param annotation 注解class
     * @param <A>        A
     * @return 注解list
     * @see java.lang.annotation.Repeatable
     */
    public static <A extends Annotation> List<A> getAnnotations(Class<?> clazz, Class<A> annotation) {
        return Lists.of(clazz.getDeclaredAnnotationsByType(annotation));
    }

    /**
     * 通过类型 获取class上的注解
     *
     * @param clazz      class
     * @param annotation 注解class
     * @param index      注解索引
     * @param <A>        A
     * @return 注解
     * @see java.lang.annotation.Repeatable
     */
    public static <A extends Annotation> A getAnnotations(Class<?> clazz, Class<A> annotation, int index) {
        List<A> list = Lists.of(clazz.getDeclaredAnnotationsByType(annotation));
        if (list.size() <= index) {
            return null;
        }
        return list.get(index);
    }

    /**
     * 获取class上的注解
     *
     * @param clazz class
     * @return 注解list
     */
    public static List<Annotation> getAnnotations(Class<?> clazz) {
        return Lists.of(clazz.getDeclaredAnnotations());
    }

    /**
     * 获取class上的注解
     *
     * @param clazz class
     * @param index 注解索引
     * @param <A>   A
     * @return 注解
     */
    public static <A extends Annotation> A getAnnotations(Class<?> clazz, int index) {
        List<Annotation> list = Lists.of(clazz.getDeclaredAnnotations());
        if (list.size() <= index) {
            return null;
        }
        return ((A) list.get(index));
    }

    // ------------------ constructor ------------------

    /**
     * 通过类型 获取constructor上的注解
     *
     * @param constructor constructor
     * @param annotation  注解class
     * @param <A>         A
     * @return 注解
     */
    public static <A extends Annotation> A getAnnotation(Constructor<?> constructor, Class<A> annotation) {
        return constructor.getDeclaredAnnotation(annotation);
    }

    /**
     * 获取constructor上的注解
     *
     * @param constructor constructor
     * @return 注解list
     */
    public static List<Annotation> getAnnotations(Constructor<?> constructor) {
        return Lists.of(constructor.getDeclaredAnnotations());
    }

    /**
     * 获取constructor上的注解
     *
     * @param constructor constructor
     * @param index       注解索引
     * @param <A>         A
     * @return 注解
     */
    public static <A extends Annotation> A getAnnotations(Constructor<?> constructor, int index) {
        List<Annotation> list = Lists.of(constructor.getDeclaredAnnotations());
        if (list.size() <= index) {
            return null;
        }
        return ((A) list.get(index));
    }

    // ------------------ field ------------------

    /**
     * 通过类型 获取field上的注解
     *
     * @param field      field
     * @param annotation 注解class
     * @param <A>        A
     * @return 注解
     */
    public static <A extends Annotation> A getAnnotation(Field field, Class<A> annotation) {
        return field.getDeclaredAnnotation(annotation);
    }

    /**
     * 获取field上的注解
     *
     * @param field field
     * @return 注解list
     */
    public static List<Annotation> getAnnotations(Field field) {
        return Lists.of(field.getDeclaredAnnotations());
    }

    /**
     * 获取field上的注解
     *
     * @param field field
     * @param index 注解索引
     * @param <A>   A
     * @return 注解
     */
    public static <A extends Annotation> A getAnnotations(Field field, int index) {
        List<Annotation> list = Lists.of(field.getDeclaredAnnotations());
        if (list.size() <= index) {
            return null;
        }
        return ((A) list.get(index));
    }

    // ------------------ method ------------------

    /**
     * 通过类型 获取method上的注解
     *
     * @param method     method
     * @param annotation 注解class
     * @param <A>        A
     * @return 注解
     */
    public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotation) {
        return method.getDeclaredAnnotation(annotation);
    }

    /**
     * 获取method上的注解
     *
     * @param method method
     * @return 注解list
     */
    public static List<Annotation> getAnnotations(Method method) {
        return Lists.of(method.getDeclaredAnnotations());
    }

    /**
     * 获取method上的注解
     *
     * @param method method
     * @param index  注解索引
     * @param <A>    A
     * @return 注解
     */
    public static <A extends Annotation> A getAnnotations(Method method, int index) {
        List<Annotation> list = Lists.of(method.getDeclaredAnnotations());
        if (list.size() <= index) {
            return null;
        }
        return ((A) list.get(index));
    }

    // ------------------ constructor parameter ------------------

    /**
     * 获取 constructor 参数上的注解
     *
     * @param constructor constructor
     * @param paramIndex  参数索引
     * @param annotation  注解class
     * @param <A>         A
     * @return 注解
     */
    public static <A extends Annotation> A getParameterAnnotation(Constructor<?> constructor, int paramIndex, Class<A> annotation) {
        Annotation[][] annotations = constructor.getParameterAnnotations();
        if (annotations.length <= paramIndex) {
            return null;
        }
        for (Annotation a : annotations[paramIndex]) {
            if (a.annotationType().equals(annotation)) {
                return (A) a;
            }
        }
        return null;
    }

    /**
     * 获取 constructor 参数上的注解
     *
     * @param constructor constructor
     * @param paramIndex  参数索引
     * @param index       注解索引
     * @param <A>         A
     * @return 注解
     */
    public static <A extends Annotation> A getParameterAnnotation(Constructor<?> constructor, int paramIndex, int index) {
        Annotation[][] annotations = constructor.getParameterAnnotations();
        if (annotations.length <= paramIndex) {
            return null;
        }
        Annotation[] a = annotations[paramIndex];
        if (a.length <= index) {
            return null;
        }
        return (A) a[index];
    }

    /**
     * 获取 constructor 参数上的注解
     *
     * @param constructor constructor
     * @param paramIndex  参数索引
     * @return 注解list
     */
    public static List<Annotation> getParameterAnnotation(Constructor<?> constructor, int paramIndex) {
        Annotation[][] annotations = constructor.getParameterAnnotations();
        if (annotations.length <= paramIndex) {
            return new ArrayList<>();
        }
        return Lists.of(annotations[paramIndex]);
    }

    /**
     * 获取 constructor 参数上的注解
     *
     * @param constructor constructor
     * @return 注解list
     */
    public static List<List<Annotation>> getParameterAnnotation(Constructor<?> constructor) {
        Annotation[][] annotations = constructor.getParameterAnnotations();
        List<List<Annotation>> list = new ArrayList<>();
        for (Annotation[] annotation : annotations) {
            list.add(Lists.of(annotation));
        }
        return list;
    }

    // ------------------ method parameter ------------------

    /**
     * 获取 method 参数上的注解
     *
     * @param method     method
     * @param paramIndex 参数索引
     * @param annotation 注解class
     * @param <A>        A
     * @return 注解
     */
    public static <A extends Annotation> A getParameterAnnotation(Method method, int paramIndex, Class<A> annotation) {
        Annotation[][] annotations = method.getParameterAnnotations();
        if (annotations.length <= paramIndex) {
            return null;
        }
        for (Annotation a : annotations[paramIndex]) {
            if (a.annotationType().equals(annotation)) {
                return (A) a;
            }
        }
        return null;
    }

    /**
     * 获取 method 参数上的注解
     *
     * @param method     method
     * @param paramIndex 参数索引
     * @param index      注解索引
     * @param <A>        A
     * @return 注解
     */
    public static <A extends Annotation> A getParameterAnnotation(Method method, int paramIndex, int index) {
        Annotation[][] annotations = method.getParameterAnnotations();
        if (annotations.length <= paramIndex) {
            return null;
        }
        Annotation[] a = annotations[paramIndex];
        if (a.length <= index) {
            return null;
        }
        return (A) a[index];
    }

    /**
     * 获取 method 参数上的注解
     *
     * @param method     method
     * @param paramIndex 参数索引
     * @return 注解list
     */
    public static List<Annotation> getParameterAnnotation(Method method, int paramIndex) {
        Annotation[][] annotations = method.getParameterAnnotations();
        if (annotations.length <= paramIndex) {
            return new ArrayList<>();
        }
        return Lists.of(annotations[paramIndex]);
    }

    /**
     * 获取 method 参数上的注解
     *
     * @param method method
     * @return 注解list
     */
    public static List<List<Annotation>> getParameterAnnotation(Method method) {
        Annotation[][] annotations = method.getParameterAnnotations();
        List<List<Annotation>> list = new ArrayList<>();
        for (Annotation[] annotation : annotations) {
            list.add(Lists.of(annotation));
        }
        return list;
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
