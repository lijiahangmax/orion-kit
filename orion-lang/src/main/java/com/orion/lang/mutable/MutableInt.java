package com.orion.lang.mutable;

import com.orion.able.Mutable;

/**
 * 可变 int
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/8 17:30
 */
public class MutableInt extends Number implements Comparable<MutableInt>, Mutable<Number> {

    private static final long serialVersionUID = -8723849675982317L;

    private int value;

    public MutableInt() {
    }

    public MutableInt(int value) {
        this.value = value;
    }

    public MutableInt(Number value) {
        this.value = value.intValue();
    }

    public MutableInt(String value) {
        this.value = Integer.parseInt(value);
    }

    @Override
    public Integer get() {
        return this.value;
    }

    @Override
    public void set(Number value) {
        this.value = value.intValue();
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void increment() {
        ++this.value;
    }

    public int getAndIncrement() {
        return this.value++;
    }

    public int incrementAndGet() {
        return ++this.value;
    }

    public void decrement() {
        --this.value;
    }

    public int getAndDecrement() {
        return this.value--;
    }

    public int decrementAndGet() {
        return --this.value;
    }

    public void add(int i) {
        this.value += i;
    }

    public void add(Number i) {
        this.value += i.intValue();
    }

    public void subtract(int i) {
        this.value -= i;
    }

    public void subtract(Number i) {
        this.value -= i.intValue();
    }

    public int addAndGet(int i) {
        this.value += i;
        return this.value;
    }

    public int addAndGet(Number i) {
        this.value += i.intValue();
        return this.value;
    }

    public int getAndAdd(int i) {
        int last = this.value;
        this.value += i;
        return last;
    }

    public int getAndAdd(Number i) {
        int last = this.value;
        this.value += i.intValue();
        return last;
    }

    public Integer toInteger() {
        return this.intValue();
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
        if (obj instanceof MutableInt) {
            return this.value == ((MutableInt) obj).intValue();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.value;
    }

    @Override
    public int compareTo(MutableInt other) {
        return Integer.compare(this.value, other.value);
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

}
