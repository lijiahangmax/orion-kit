package com.orion.utils.convert;

import com.orion.function.Conversion;
import com.orion.utils.Valid;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * 对象类型转换映射的自定义实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/9 14:20
 */
public abstract class TypeMapper<T> implements Serializable {

    private static final long serialVersionUID = -123903459090989178L;

    private final TypeStore store;

    private final Class<T> sourceType;

    public TypeMapper(Class<T> sourceType) {
        this(sourceType, TypeStore.STORE);
    }

    public TypeMapper(Class<T> sourceType, TypeStore store) {
        Valid.notNull(sourceType, "sourceType is null");
        Valid.notNull(store, "typeStore is null");
        this.sourceType = sourceType;
        this.store = store;
    }

    /**
     * 注册转换器
     *
     * @param target     target class
     * @param conversion 转换器
     * @param <R>        R
     */
    protected <R> void register(Class<R> target, Conversion<T, R> conversion) {
        store.register(sourceType, target, conversion);
    }

    /**
     * 获取转换器
     *
     * @param target target class
     * @param <R>    target
     * @return Conversion
     */
    public <R> Conversion<T, R> get(Class<R> target) {
        return store.get(sourceType, target);
    }

    /**
     * 转换
     *
     * @param t      T
     * @param target targetClass
     * @param <R>    target
     * @return R
     */
    public <R> R to(T t, Class<R> target) {
        return store.to(t, target);
    }

    /**
     * 获取适配的 class
     *
     * @return set
     */
    public Set<Class<?>> getSuitableClasses() {
        return store.getSuitableClasses(sourceType);
    }

    /**
     * 获取所有适配的 class
     *
     * @return set
     */
    public Set<Class<?>> getAllSuitableClasses() {
        return store.getSuitableClasses(sourceType, true);
    }

    /**
     * 获取适配的 Conversion
     *
     * @return map
     */
    public Map<Class<?>, Conversion<?, ?>> getSuitableConversion() {
        return store.getSuitableConversion(sourceType);
    }

    /**
     * 获取所有适配的 Conversion
     *
     * @return map
     */
    public Map<Class<?>, Conversion<?, ?>> getAllSuitableConversion() {
        return store.getSuitableConversion(sourceType, true);
    }

}
