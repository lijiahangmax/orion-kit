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

/**
 * 可变 short
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/8 14:38
 */
public class MutableShort extends Number implements Comparable<MutableShort>, Mutable<Number> {

    private static final long serialVersionUID = -718264587912374L;

    private short value;

    public MutableShort() {
    }

    public MutableShort(short value) {
        this.value = value;
    }

    public MutableShort(Number value) {
        this.value = value.shortValue();
    }

    public MutableShort(String value) {
        this.value = Short.parseShort(value);
    }

    public static MutableShort create() {
        return new MutableShort();
    }

    public static MutableShort of(short value) {
        return new MutableShort(value);
    }

    @Override
    public Short get() {
        return this.value;
    }

    @Override
    public void set(Number value) {
        this.value = value.shortValue();
    }

    public void setValue(short value) {
        this.value = value;
    }

    public void increment() {
        ++this.value;
    }

    public short getAndIncrement() {
        return this.value++;
    }

    public short incrementAndGet() {
        return ++this.value;
    }

    public void decrement() {
        --this.value;
    }

    public short getAndDecrement() {
        return this.value--;
    }

    public short decrementAndGet() {
        return --this.value;
    }

    public void add(short b) {
        this.value += b;
    }

    public void add(Number b) {
        this.value += b.shortValue();
    }

    public void subtract(short b) {
        this.value -= b;
    }

    public void subtract(Number b) {
        this.value -= b.shortValue();
    }

    public short addAndGet(short b) {
        this.value += b;
        return this.value;
    }

    public short addAndGet(Number b) {
        this.value += b.shortValue();
        return this.value;
    }

    public short getAndAdd(short b) {
        short last = this.value;
        this.value += b;
        return last;
    }

    public short getAndAdd(Number b) {
        short last = this.value;
        this.value += b.shortValue();
        return last;
    }

    public Short toShort() {
        return this.value;
    }

    @Override
    public byte byteValue() {
        return (byte) this.value;
    }

    @Override
    public short shortValue() {
        return this.value;
    }

    @Override
    public int intValue() {
        return this.value;
    }

    @Override
    public long longValue() {
        return this.value;
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
        if (obj instanceof MutableShort) {
            return this.value == ((MutableShort) obj).shortValue();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.value;
    }

    @Override
    public int compareTo(MutableShort other) {
        return Short.compare(this.value, other.value);
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

}
