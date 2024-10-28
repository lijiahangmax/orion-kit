/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package cn.orionsec.kit.lang.define.mutable;

import cn.orionsec.kit.lang.able.Mutable;

import java.io.Serializable;

/**
 * 可变 boolean
 *
 * @author Jiahang Li
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

    public static MutableBoolean create() {
        return new MutableBoolean();
    }

    public static MutableBoolean of(boolean value) {
        return new MutableBoolean(value);
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
