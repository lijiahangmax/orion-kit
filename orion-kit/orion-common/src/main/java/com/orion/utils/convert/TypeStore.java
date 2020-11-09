package com.orion.utils.convert;

import com.orion.function.Conversion;
import com.orion.lang.collect.MultiConcurrentHashMap;
import com.orion.lang.support.CloneSupport;
import com.orion.lang.wrapper.Pair;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.Valid;
import com.orion.utils.collect.Lists;
import com.orion.utils.reflect.Classes;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类型转换容器
 * 自动转换基本类型为包装类型
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/11/9 14:29
 */
@SuppressWarnings("unchecked")
public class TypeStore extends CloneSupport<TypeStore> implements Serializable {

    private static final long serialVersionUID = 12038041239487192L;

    public static final TypeStore STORE = new TypeStore();

    public TypeStore() {
    }

    private static final ConcurrentHashMap<Pair<Class<?>, Class<?>>, Boolean> IMPL_MAP = new ConcurrentHashMap<>();

    static {
        new BasicTypeMapper();
    }

    private final MultiConcurrentHashMap<Class<?>, Class<?>, Conversion<?, ?>> conversionMapping = new MultiConcurrentHashMap<>();

    public <T, R> void register(Class<T> source, Class<R> target, Conversion<T, R> conversion) {
        conversionMapping.put(source, target, conversion);
    }

    public <T, R> Conversion<T, R> get(Class<T> source, Class<R> target) {
        return (Conversion<T, R>) conversionMapping.get(source, target);
    }

    public MultiConcurrentHashMap<Class<?>, Class<?>, Conversion<?, ?>> getConversionMapping() {
        return conversionMapping;
    }

    /**
     * 转换
     *
     * @param t           T
     * @param targetClass targetClass
     * @param <R>         target
     * @return R
     */
    public <T, R> R to(T t, Class<R> targetClass) {
        Valid.notNull(t, "convert target object is null");
        Class<?> sourceClass = t.getClass();
        targetClass = (Class<R>) Classes.getWrapClass(targetClass);
        if (targetClass.equals(Object.class) || targetClass.equals(sourceClass)) {
            return (R) t;
        }
        if (checkImpl(targetClass, sourceClass)) {
            return (R) t;
        }
        Conversion<T, R> conversion = (Conversion<T, R>) this.get(sourceClass, targetClass);
        if (conversion == null) {
            throw Exceptions.convert(Strings.format("Unable to convert source [{}] class to target [{}] class", sourceClass, targetClass));
        }
        return conversion.apply(t);
    }

    /**
     * 获取适配的 class
     *
     * @param sourceType 原始类型
     * @return List
     */
    public List<Class<?>> getSuitableClasses(Class<?> sourceType) {
        return Lists.as(conversionMapping.computeIfAbsent(sourceType, c -> new ConcurrentHashMap<>()).keys());
    }

    /**
     * 获取适配的 Conversion
     *
     * @return Map
     */
    public Map<Class<?>, Conversion<?, ?>> getSuitableConversion(Class<?> sourceType) {
        return conversionMapping.computeIfAbsent(sourceType, c -> new ConcurrentHashMap<>());
    }

    /**
     * 获取全局 Store
     *
     * @return TypeStore
     */
    public static TypeStore getStore() {
        return STORE;
    }

    /**
     * 检查target是否为require的实现类
     *
     * @param require ignore
     * @param target  ignore
     * @return ignore
     */
    private static boolean checkImpl(Class<?> require, Class<?> target) {
        Pair<Class<?>, Class<?>> e = new Pair<>(require, target);
        Boolean b = IMPL_MAP.get(e);
        if (b == null) {
            boolean i = Classes.isImplClass(require, target);
            IMPL_MAP.put(e, i);
            return i;
        } else {
            return b;
        }
    }

}
