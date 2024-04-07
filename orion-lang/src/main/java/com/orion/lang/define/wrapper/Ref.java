package com.orion.lang.define.wrapper;

import com.orion.lang.able.IJsonObject;
import com.orion.lang.define.support.CloneSupport;
import com.orion.lang.utils.Objects1;

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
