package com.orion.lang.mutable;

import com.orion.able.Mutable;

import java.io.Serializable;

/**
 * 可变 boolean
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/8 18:22
 */
public class MutableBoolean implements Mutable<Boolean>, Serializable, Comparable<MutableBoolean> {

    private static final long serialVersionUID = -18927387182378954L;

    private boolean value;

    public MutableBoolean() {
    }

    public MutableBoolean(boolean value) {
        this.value = value;
    }

    public MutableBoolean(Boolean value) {
        this.value = value;
    }

    @Override
    public Boolean get() {
        return this.value;
    }

    @Override
    public void set(Boolean value) {
        this.value = value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public void setFalse() {
        this.value = false;
    }

    public void setTrue() {
        this.value = true;
    }

    public boolean isTrue() {
        return this.value;
    }

    public boolean isFalse() {
        return !this.value;
    }

    public Boolean toBoolean() {
        return this.booleanValue();
    }

    public boolean booleanValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MutableBoolean) {
            return this.value == ((MutableBoolean) obj).booleanValue();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.value ? Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode();
    }

    @Override
    public int compareTo(MutableBoolean other) {
        return Boolean.compare(this.value, other.value);
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

}
