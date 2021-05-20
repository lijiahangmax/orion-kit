package com.orion.lang.mutable;

import com.orion.able.Mutable;

import java.io.Serializable;

/**
 * 可变对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/8 13:50
 */
public class MutableObject<T> implements Mutable<T>, Serializable {

    private static final long serialVersionUID = 298530495803846909L;

    private T value;

    public MutableObject() {
    }

    public MutableObject(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return this.value;
    }

    @Override
    public void set(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (this.getClass() == obj.getClass()) {
            MutableObject<?> that = (MutableObject<?>) obj;
            return this.value.equals(that.value);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.value == null ? 0 : this.value.hashCode();
    }

    @Override
    public String toString() {
        return this.value == null ? "null" : this.value.toString();
    }

}
