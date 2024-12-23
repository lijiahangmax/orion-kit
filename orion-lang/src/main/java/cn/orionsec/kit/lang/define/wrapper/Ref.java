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
package cn.orionsec.kit.lang.define.wrapper;

import cn.orionsec.kit.lang.able.IJsonObject;
import cn.orionsec.kit.lang.define.support.CloneSupport;
import cn.orionsec.kit.lang.utils.Objects1;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * 对象 包装类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/8/30 23:53
 */
public class Ref<T> extends CloneSupport<Ref<T>> implements Serializable, IJsonObject {

    private static final long serialVersionUID = -885690364340775L;

    private T value;

    public Ref() {
    }

    public Ref(T value) {
        this.value = value;
    }

    public static <T> Ref<T> of(T t) {
        return new Ref<>(t);
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    /**
     * 映射
     *
     * @param mapping mapping
     * @param <E>     E
     * @return mapped
     */
    public <E> Ref<E> map(Function<T, E> mapping) {
        return new Ref<>(Objects1.map(this.value, mapping));
    }

    /**
     * @return Optional
     */
    public Optional<T> optional() {
        return Optional.ofNullable(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return value == null;
        }
        if (this.getClass() != o.getClass()) {
            // unref
            return Objects.equals(value, o);
        } else {
            // ref
            Ref<?> ref = (Ref<?>) o;
            return Objects.equals(value, ref.value);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value == null ? null : value.toString();
    }

}
