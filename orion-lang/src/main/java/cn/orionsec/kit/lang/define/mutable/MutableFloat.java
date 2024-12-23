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

/**
 * 可变 float
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/8 18:15
 */
public class MutableFloat extends Number implements Comparable<MutableFloat>, Mutable<Number> {

    private static final long serialVersionUID = -8176512397589472L;

    private float value;

    public MutableFloat() {
    }

    public MutableFloat(float value) {
        this.value = value;
    }

    public MutableFloat(Number value) {
        this.value = value.floatValue();
    }

    public MutableFloat(String value) {
        this.value = Float.parseFloat(value);
    }

    public static MutableFloat create() {
        return new MutableFloat();
    }

    public static MutableFloat of(float value) {
        return new MutableFloat(value);
    }

    @Override
    public Float get() {
        return this.value;
    }

    @Override
    public void set(Number value) {
        this.value = value.floatValue();
    }

    public void setValue(float value) {
        this.value = value;
    }

    public boolean isNaN() {
        return Float.isNaN(this.value);
    }

    public boolean isInfinite() {
        return Float.isInfinite(this.value);
    }

    public void increment() {
        ++this.value;
    }

    public float getAndIncrement() {
        return (this.value++);
    }

    public float incrementAndGet() {
        return ++this.value;
    }

    public void decrement() {
        --this.value;
    }

    public float getAndDecrement() {
        return this.value--;
    }

    public float decrementAndGet() {
        return --this.value;
    }

    public void add(float i) {
        this.value += i;
    }

    public void add(Number i) {
        this.value += i.floatValue();
    }

    public void subtract(float i) {
        this.value -= i;
    }

    public void subtract(Number i) {
        this.value -= i.floatValue();
    }

    public float addAndGet(float i) {
        this.value += i;
        return this.value;
    }

    public float addAndGet(Number i) {
        this.value += i.floatValue();
        return this.value;
    }

    public float getAndAdd(float i) {
        float last = this.value;
        this.value += i;
        return last;
    }

    public float getAndAdd(Number i) {
        float last = this.value;
        this.value += i.floatValue();
        return last;
    }

    public Float toFloat() {
        return this.floatValue();
    }

    @Override
    public byte byteValue() {
        return (byte) this.value;
    }

    @Override
    public short shortValue() {
        return (short) this.value;
    }

    @Override
    public int intValue() {
        return (int) this.value;
    }

    @Override
    public long longValue() {
        return (long) this.value;
    }

    @Override
    public float floatValue() {
        return this.value;
    }

    @Override
    public double doubleValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MutableFloat && Float.floatToIntBits(((MutableFloat) obj).value) == Float.floatToIntBits(this.value);
    }

    @Override
    public int hashCode() {
        return Float.floatToIntBits(this.value);
    }

    @Override
    public int compareTo(MutableFloat other) {
        return Float.compare(this.value, other.value);
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

}

