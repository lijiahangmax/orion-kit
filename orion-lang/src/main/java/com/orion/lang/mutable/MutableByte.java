package com.orion.lang.mutable;

import com.orion.able.Mutable;

/**
 * 可变 byte
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/1/8 13:58
 */
public class MutableByte extends Number implements Comparable<MutableByte>, Mutable<Number> {

    private static final long serialVersionUID = -9071284079851923804L;

    private byte value;

    public MutableByte() {
    }

    public MutableByte(byte value) {
        this.value = value;
    }

    public MutableByte(Number value) {
        this.value = value.byteValue();
    }

    public MutableByte(String value) {
        this.value = Byte.parseByte(value);
    }

    @Override
    public Byte get() {
        return this.value;
    }

    @Override
    public void set(Number value) {
        this.value = value.byteValue();
    }

    public void setValue(byte value) {
        this.value = value;
    }

    public void increment() {
        ++this.value;
    }

    public byte getAndIncrement() {
        return this.value++;
    }

    public byte incrementAndGet() {
        return ++this.value;
    }

    public void decrement() {
        --this.value;
    }

    public byte getAndDecrement() {
        return this.value--;
    }

    public byte decrementAndGet() {
        return --this.value;
    }

    public void add(byte b) {
        this.value += b;
    }

    public void add(Number b) {
        this.value += b.byteValue();
    }

    public void subtract(byte b) {
        this.value -= b;
    }

    public void subtract(Number b) {
        this.value -= b.byteValue();
    }

    public byte addAndGet(byte b) {
        this.value += b;
        return this.value;
    }

    public byte addAndGet(Number b) {
        this.value += b.byteValue();
        return this.value;
    }

    public byte getAndAdd(byte b) {
        byte last = this.value;
        this.value += b;
        return last;
    }

    public byte getAndAdd(Number b) {
        byte last = this.value;
        this.value += b.byteValue();
        return last;
    }

    public Byte toByte() {
        return this.value;
    }

    @Override
    public byte byteValue() {
        return this.value;
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
        if (obj instanceof MutableByte) {
            return this.value == ((MutableByte) obj).byteValue();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.value;
    }

    @Override
    public int compareTo(MutableByte other) {
        return Byte.compare(this.value, other.value);
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

}
