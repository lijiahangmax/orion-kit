package com.orion.lang.mutable;

import com.orion.able.Mutable;

/**
 * 可变 long
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/8 18:11
 */
public class MutableLong extends Number implements Comparable<MutableLong>, Mutable<Number> {

    private static final long serialVersionUID = 108275912873945L;

    private long value;

    public MutableLong() {
    }

    public MutableLong(long value) {
        this.value = value;
    }

    public MutableLong(Number value) {
        this.value = value.longValue();
    }

    public MutableLong(String value) {
        this.value = Long.parseLong(value);
    }

    @Override
    public Long get() {
        return this.value;
    }

    @Override
    public void set(Number value) {
        this.value = value.longValue();
    }

    public void setValue(long value) {
        this.value = value;
    }

    public void increment() {
        ++this.value;
    }

    public long getAndIncrement() {
        return this.value++;
    }

    public long incrementAndGet() {
        return ++this.value;
    }

    public void decrement() {
        --this.value;
    }

    public long getAndDecrement() {
        return this.value--;
    }

    public long decrementAndGet() {
        return --this.value;
    }

    public void add(long i) {
        this.value += i;
    }

    public void add(Number i) {
        this.value += i.longValue();
    }

    public void subtract(long i) {
        this.value -= i;
    }

    public void subtract(Number i) {
        this.value -= i.longValue();
    }

    public long addAndGet(long i) {
        this.value += i;
        return this.value;
    }

    public long addAndGet(Number i) {
        this.value += i.longValue();
        return this.value;
    }

    public long getAndAdd(long i) {
        long last = this.value;
        this.value += i;
        return last;
    }

    public long getAndAdd(Number i) {
        long last = this.value;
        this.value += i.longValue();
        return last;
    }

    public Long toLong() {
        return this.longValue();
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
        return this.value;
    }

    @Override
    public float floatValue() {
        return (float) this.value;
    }

    @Override
    public double doubleValue() {
        return (double) this.value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MutableLong) {
            return this.value == ((MutableLong) obj).longValue();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (int) (this.value ^ this.value >>> 32);
    }

    @Override
    public int compareTo(MutableLong other) {
        return Long.compare(this.value, other.value);
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

}
