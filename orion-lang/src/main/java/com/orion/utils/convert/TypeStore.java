package com.orion.utils.convert;

import com.orion.function.Conversion;
import com.orion.lang.collect.MultiConcurrentHashMap;
import com.orion.lang.iterator.ClassIterator;
import com.orion.lang.support.CloneSupport;
import com.orion.utils.Arrays1;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.Valid;
import com.orion.utils.collect.Sets;
import com.orion.utils.reflect.Classes;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类型转换容器
 * 自动转换基本类型为包装类型
 * 支持子父类转换 但是不支持接口转换
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/9 14:29
 */
@SuppressWarnings({"unchecked"})
public class TypeStore extends CloneSupport<TypeStore> implements Serializable {

    private static final long serialVersionUID = 12038041239487192L;

    public static final TypeStore STORE = new TypeStore();

    private final MultiConcurrentHashMap<Class<?>, Class<?>, Conversion<?, ?>> conversionMapping;

    public TypeStore() {
        this.conversionMapping = new MultiConcurrentHashMap<>();
    }

    static {
        // 装载基础Mapper
        new BasicTypeStoreProvider(STORE);
    }

    /**
     * 注册转换器
     *
     * @param source     源class
     * @param target     目标class
     * @param conversion 转换器
     * @param <T>        T
     * @param <R>        R
     */
    public <T, R> void register(Class<T> source, Class<R> target, Conversion<T, R> conversion) {
        conversionMapping.put(source, target, conversion);
    }

    /**
     * 获取对象转换器
     *
     * @param source 源class
     * @param target 目标class
     * @param <T>    T
     * @param <R>    R
     * @return 转换器
     */
    public <T, R> Conversion<T, R> get(Class<T> source, Class<R> target) {
        return (Conversion<T, R>) conversionMapping.get(source, target);
    }

    /**
     * 获取映射转换器
     *
     * @return 转换器列表
     */
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
        // 检查是否可以直接转换
        if (canDirectConvert(sourceClass, targetClass, false)) {
            return (R) t;
        }
        // 获取类转换器
        Conversion<T, R> conversion = (Conversion<T, R>) this.get(sourceClass, targetClass);
        if (conversion != null) {
            return conversion.apply(t);
        }
        // 获取父类转换器
        for (Class<?> sourceParentClass : new ClassIterator<>(sourceClass)) {
            conversion = (Conversion<T, R>) this.get(sourceParentClass, targetClass);
            if (conversion != null) {
                return conversion.apply(t);
            }
        }
        // 检查是否是数组
        if (!Classes.isArray(targetClass)) {
            throw Exceptions.convert(Strings.format("unable to convert source [{}] class to target [{}] class", sourceClass, targetClass));
        }
        // 如果不是基本类型的数组则无法转换
        Class<?> baseArrayClass = Classes.getBaseArrayClass(targetClass);
        if (baseArrayClass.equals(targetClass)) {
            throw Exceptions.convert(Strings.format("unable to convert source [{}] class to target [{}] class", sourceClass, targetClass));
        }
        // 如果 targetClass 是 sourceClass 的包装类数组则直接包装
        if (sourceClass.equals(baseArrayClass)) {
            return (R) Arrays1.wrap(t);
        }
        // 尝试使用 targetClass 的基本类型数组获取
        Conversion<T, ?> baseConvert = (Conversion<T, ?>) this.get(sourceClass, baseArrayClass);
        if (baseConvert == null) {
            throw Exceptions.convert(Strings.format("unable to convert source [{}] class to target [{}] class", sourceClass, targetClass));
        }
        // 如果能获取到则将转换结果包装
        Object apply = baseConvert.apply(t);
        if (apply != null) {
            return (R) Arrays1.wrap(apply);
        }
        return null;
    }

    /**
     * 获取适配的 class 不适配父类
     *
     * @param sourceType 原始类型
     * @return set
     */
    public Set<Class<?>> getSuitableClasses(Class<?> sourceType) {
        return this.getSuitableClasses(sourceType, false);
    }

    /**
     * 获取所有适配的 class 适配父类
     *
     * @param sourceType 原始类型
     * @return set
     */
    public Set<Class<?>> getAllSuitableClasses(Class<?> sourceType) {
        return this.getSuitableClasses(sourceType, true);
    }

    protected Set<Class<?>> getSuitableClasses(Class<?> sourceType, boolean all) {
        Set<Class<?>> classes = Sets.as(conversionMapping.computeIfAbsent(sourceType, c -> new ConcurrentHashMap<>(8)).keys());
        if (all) {
            for (Class<?> parentType : new ClassIterator<>(sourceType)) {
                Map<Class<?>, Conversion<?, ?>> map = conversionMapping.get(parentType);
                if (map != null) {
                    classes.addAll(map.keySet());
                }
            }
        }
        return classes;
    }

    /**
     * 获取适配的 Conversion 不适配父类
     *
     * @param sourceType 原始类型
     * @return map
     */
    public Map<Class<?>, Conversion<?, ?>> getSuitableConversion(Class<?> sourceType) {
        return this.getSuitableConversion(sourceType, false);
    }

    /**
     * 获取所有适配的 Conversion 适配父类
     *
     * @param sourceType 原始类型
     * @return map
     */
    public Map<Class<?>, Conversion<?, ?>> getAllSuitableConversion(Class<?> sourceType) {
        return this.getSuitableConversion(sourceType, true);
    }

    protected Map<Class<?>, Conversion<?, ?>> getSuitableConversion(Class<?> sourceType, boolean all) {
        Map<Class<?>, Conversion<?, ?>> mapping = conversionMapping.computeIfAbsent(sourceType, c -> new ConcurrentHashMap<>(8));
        if (all) {
            for (Class<?> parentType : new ClassIterator<>(sourceType)) {
                Map<Class<?>, Conversion<?, ?>> parentMapping = conversionMapping.get(parentType);
                if (parentMapping == null) {
                    continue;
                }
                parentMapping.forEach(mapping::putIfAbsent);
            }
        }
        return mapping;
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
     * 判断类型是否可以转换 sourceClass -> targetClass
     *
     * @param sourceClass 源class
     * @param targetClass 目标class
     * @return true可以直接转换
     */
    public static boolean canConvert(Class<?> sourceClass, Class<?> targetClass) {
        return canConvert(sourceClass, targetClass, STORE);
    }

    /**
     * 判断类型是否可以转换 sourceClass -> targetClass
     *
     * @param sourceClass 源class
     * @param targetClass 目标class
     * @param store       store
     * @return true可以直接转换
     */
    public static boolean canConvert(Class<?> sourceClass, Class<?> targetClass, TypeStore store) {
        Valid.notNull(sourceClass, "source class is null");
        Valid.notNull(targetClass, "target class is null");
        sourceClass = Classes.getWrapClass(sourceClass);
        targetClass = Classes.getWrapClass(targetClass);
        if (canDirectConvert(sourceClass, targetClass, false)) {
            return true;
        }
        if (store.get(sourceClass, targetClass) != null) {
            return true;
        }
        for (Class<?> sourceParentClass : new ClassIterator<>(sourceClass)) {
            if (store.get(sourceParentClass, targetClass) != null) {
                return true;
            }
        }
        if (!Classes.isArray(targetClass)) {
            return false;
        }
        Class<?> baseArrayClass = Classes.getBaseArrayClass(targetClass);
        if (baseArrayClass.equals(targetClass)) {
            return false;
        }
        if (sourceClass.equals(baseArrayClass)) {
            return true;
        }
        return store.get(sourceClass, baseArrayClass) != null;
    }

    /**
     * 判断类型是否可以直接转换 sourceClass -> targetClass
     *
     * @param sourceClass 源class
     * @param targetClass 目标class
     * @return true可以直接转换
     */
    public static boolean canDirectConvert(Class<?> sourceClass, Class<?> targetClass) {
        return canDirectConvert(sourceClass, targetClass, true);
    }

    /**
     * 判断类型是否可以直接转换 sourceClass -> targetClass
     *
     * @param sourceClass 源class
     * @param targetClass 目标class
     * @param wrap        是否包装基本类型
     * @return true可以直接转换
     */
    private static boolean canDirectConvert(Class<?> sourceClass, Class<?> targetClass, boolean wrap) {
        Valid.notNull(sourceClass, "source class is null");
        Valid.notNull(targetClass, "target class is null");
        if (wrap) {
            sourceClass = Classes.getWrapClass(sourceClass);
            targetClass = Classes.getWrapClass(targetClass);
        }
        // check unable
        if (targetClass.equals(Object.class) || targetClass.equals(sourceClass)) {
            return true;
        }
        // check impl
        return Classes.isImplClass(targetClass, sourceClass);
    }

}
