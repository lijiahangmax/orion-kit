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
package cn.orionsec.kit.lang.define.mutable;

import cn.orionsec.kit.lang.able.Mutable;

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

    public static <T> MutableObject<T> create() {
        return new MutableObject<>();
    }

    public static <T> MutableObject<T> of(T value) {
        return new MutableObject<>(value);
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
