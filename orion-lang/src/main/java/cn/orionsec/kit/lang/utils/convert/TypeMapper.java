/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.lang.utils.convert;

import cn.orionsec.kit.lang.function.Conversion;
import cn.orionsec.kit.lang.utils.Valid;

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
