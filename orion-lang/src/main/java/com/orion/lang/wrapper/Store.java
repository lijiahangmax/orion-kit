package com.orion.lang.wrapper;

import com.orion.lang.support.CloneSupport;
import com.orion.utils.Strings;

import java.io.Serializable;
import java.util.Optional;

/**
 * 对象 包装类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/8/30 23:53
 */
public class Store<T> extends CloneSupport<Store<T>> implements Serializable {

    private static final long serialVersionUID = -885690364340775L;

    private T value;

    public Store() {
    }

    public Store(T value) {
        this.value = value;
    }

    public static <T> Store<T> of(T t) {
        return new Store<>(t);
    }

    public T get() {
        return value;
    }

    public Store<T> set(T value) {
        this.value = value;
        return this;
    }

    /**
     * @return Optional
     */
    public Optional<T> optional() {
        return Optional.ofNullable(value);
    }

    @Override
    public String toString() {
        return value == null ? Strings.EMPTY : value.toString();
    }

}
