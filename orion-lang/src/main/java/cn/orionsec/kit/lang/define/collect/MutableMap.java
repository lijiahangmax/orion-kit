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
package cn.orionsec.kit.lang.define.collect;

import cn.orionsec.kit.lang.function.*;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.convert.Converts;
import cn.orionsec.kit.lang.utils.math.BigDecimals;
import cn.orionsec.kit.lang.utils.math.BigIntegers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.function.*;

/**
 * 可转换的 map
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/5 17:20
 */
public interface MutableMap<K, V> extends Map<K, V> {

    default Byte getByte(K k) {
        return this.getByte(k, Suppliers.nullSupplier());
    }

    default Byte getByte(K k, Byte def) {
        return this.getByte(k, () -> def);
    }

    default Byte getByte(K k, Supplier<Byte> supplier) {
        Valid.notNull(supplier);
        V v = this.get(k);
        if (v == null) {
            return supplier.get();
        }
        return Converts.toByte(v);
    }

    default byte getByteValue(K k) {
        return this.getByteValue(k, Suppliers.BYTE_SUPPLIER);
    }

    default byte getByteValue(K k, byte def) {
        return this.getByteValue(k, () -> def);
    }

    default byte getByteValue(K k, ByteSupplier supplier) {
        Valid.notNull(supplier);
        V v = this.get(k);
        if (v == null) {
            return supplier.getAsByte();
        }
        return Converts.toByte(v);
    }

    default Short getShort(K k) {
        return this.getShort(k, Suppliers.nullSupplier());
    }

    default Short getShort(K k, Short def) {
        return this.getShort(k, () -> def);
    }

    default Short getShort(K k, Supplier<Short> supplier) {
        Valid.notNull(supplier);
        V v = this.get(k);
        if (v == null) {
            return supplier.get();
        }
        return Converts.toShort(k);
    }

    default short getShortValue(K k) {
        return this.getShortValue(k, Suppliers.SHORT_SUPPLIER);
    }

    default short getShortValue(K k, short def) {
        return this.getShortValue(k, () -> def);
    }

    default short getShortValue(K k, ShortSupplier supplier) {
        Valid.notNull(supplier);
        V v = this.get(k);
        if (v == null) {
            return supplier.getAsShort();
        }
        return Converts.toShort(k);
    }

    default Integer getInteger(K k) {
        return this.getInteger(k, Suppliers.nullSupplier());
    }

    default Integer getInteger(K k, Integer def) {
        return this.getInteger(k, () -> def);
    }

    default Integer getInteger(K k, Supplier<Integer> supplier) {
        Valid.notNull(supplier);
        V v = this.get(k);
        if (v == null) {
            return supplier.get();
        }
        return Converts.toInt(v);
    }

    default int getIntValue(K k) {
        return this.getIntValue(k, Suppliers.INT_SUPPLIER);
    }

    default int getIntValue(K k, int def) {
        return this.getIntValue(k, () -> def);
    }

    default int getIntValue(K k, IntSupplier supplier) {
        Valid.notNull(supplier);
        V v = this.get(k);
        if (v == null) {
            return supplier.getAsInt();
        }
        return Converts.toInt(v);
    }

    default Long getLong(K k) {
        return this.getLong(k, Suppliers.nullSupplier());
    }

    default Long getLong(K k, Long def) {
        return this.getLong(k, () -> def);
    }

    default Long getLong(K k, Supplier<Long> supplier) {
        Valid.notNull(supplier);
        V v = this.get(k);
        if (v == null) {
            return supplier.get();
        }
        return Converts.toLong(v);
    }

    default long getLongValue(K k) {
        return this.getLongValue(k, Suppliers.LONG_SUPPLIER);
    }

    default long getLongValue(K k, long def) {
        return this.getLongValue(k, () -> def);
    }

    default long getLongValue(K k, LongSupplier supplier) {
        Valid.notNull(supplier);
        V v = this.get(k);
        if (v == null) {
            return supplier.getAsLong();
        }
        return Converts.toLong(v);
    }

    default Float getFloat(K k) {
        return this.getFloat(k, Suppliers.nullSupplier());
    }

    default Float getFloat(K k, Float def) {
        return this.getFloat(k, () -> def);
    }

    default Float getFloat(K k, Supplier<Float> supplier) {
        Valid.notNull(supplier);
        V v = this.get(k);
        if (v == null) {
            return supplier.get();
        }
        return Converts.toFloat(v);
    }

    default float getFloatValue(K k) {
        return this.getFloatValue(k, Suppliers.FLOAT_SUPPLIER);
    }

    default float getFloatValue(K k, float def) {
        return this.getFloatValue(k, () -> def);
    }

    default float getFloatValue(K k, FloatSupplier supplier) {
        Valid.notNull(supplier);
        V v = this.get(k);
        if (v == null) {
            return supplier.getAsFloat();
        }
        return Converts.toFloat(v);
    }

    default Double getDouble(K k) {
        return this.getDouble(k, Suppliers.nullSupplier());
    }

    default Double getDouble(K k, Double def) {
        return this.getDouble(k, () -> def);
    }

    default Double getDouble(K k, Supplier<Double> supplier) {
        Valid.notNull(supplier);
        V v = this.get(k);
        if (v == null) {
            return supplier.get();
        }
        return Converts.toDouble(v);
    }

    default double getDoubleValue(K k) {
        return this.getDoubleValue(k, Suppliers.DOUBLE_SUPPLIER);
    }

    default double getDoubleValue(K k, double def) {
        return this.getDoubleValue(k, () -> def);
    }

    default double getDoubleValue(K k, DoubleSupplier supplier) {
        Valid.notNull(supplier);
        V v = this.get(k);
        if (v == null) {
            return supplier.getAsDouble();
        }
        return Converts.toDouble(v);
    }

    default Boolean getBoolean(K k) {
        return this.getBoolean(k, Suppliers.nullSupplier());
    }

    default Boolean getBoolean(K k, Boolean def) {
        return this.getBoolean(k, () -> def);
    }

    default Boolean getBoolean(K k, Supplier<Boolean> supplier) {
        Valid.notNull(supplier);
        V v = this.get(k);
        if (v == null) {
            return supplier.get();
        }
        return Converts.toBoolean(v);
    }

    default boolean getBooleanValue(K k) {
        return this.getBooleanValue(k, Suppliers.BOOLEAN_SUPPLIER);
    }

    default boolean getBooleanValue(K k, boolean def) {
        return this.getBooleanValue(k, () -> def);
    }

    default boolean getBooleanValue(K k, BooleanSupplier supplier) {
        Valid.notNull(supplier);
        V v = this.get(k);
        if (v == null) {
            return supplier.getAsBoolean();
        }
        return Converts.toBoolean(v);
    }

    default Character getCharacter(K k) {
        return this.getCharacter(k, Suppliers.nullSupplier());
    }

    default Character getCharacter(K k, Character def) {
        return this.getCharacter(k, () -> def);
    }

    default Character getCharacter(K k, Supplier<Character> supplier) {
        Valid.notNull(supplier);
        V v = this.get(k);
        if (v == null) {
            return supplier.get();
        }
        return Converts.toChar(v);
    }

    default char getCharValue(K k) {
        return this.getCharValue(k, Suppliers.CHAR_SUPPLIER);
    }

    default char getCharValue(K k, char def) {
        return this.getCharValue(k, () -> def);
    }

    default char getCharValue(K k, CharSupplier supplier) {
        Valid.notNull(supplier);
        V v = this.get(k);
        if (v == null) {
            return supplier.getAsChar();
        }
        return Converts.toChar(v);
    }

    default String getString(K k) {
        return this.getString(k, Suppliers.nullSupplier());
    }

    default String getString(K k, String def) {
        return this.getString(k, () -> def);
    }

    default String getString(K k, Supplier<String> supplier) {
        Valid.notNull(supplier);
        V v = this.get(k);
        if (v == null) {
            return supplier.get();
        }
        return Converts.toString(v);
    }

    default Date getDate(K k) {
        return this.getDate(k, Suppliers.nullSupplier());
    }

    default Date getDate(K k, Date def) {
        return this.getDate(k, () -> def);
    }

    default Date getDate(K k, Supplier<Date> supplier) {
        Valid.notNull(supplier);
        V v = this.get(k);
        if (v == null) {
            return supplier.get();
        }
        return Converts.toDate(v);
    }

    default LocalDateTime getLocalDateTime(K k) {
        return this.getLocalDateTime(k, Suppliers.nullSupplier());
    }

    default LocalDateTime getLocalDateTime(K k, LocalDateTime def) {
        return this.getLocalDateTime(k, () -> def);
    }

    default LocalDateTime getLocalDateTime(K k, Supplier<LocalDateTime> supplier) {
        Valid.notNull(supplier);
        V v = this.get(k);
        if (v == null) {
            return supplier.get();
        }
        return Converts.toLocalDateTime(v);
    }

    default LocalDate getLocalDate(K k) {
        return this.getLocalDate(k, Suppliers.nullSupplier());
    }

    default LocalDate getLocalDate(K k, LocalDate def) {
        return this.getLocalDate(k, () -> def);
    }

    default LocalDate getLocalDate(K k, Supplier<LocalDate> supplier) {
        Valid.notNull(supplier);
        V v = this.get(k);
        if (v == null) {
            return supplier.get();
        }
        return Converts.toLocalDate(v);
    }

    default BigDecimal getBigDecimal(K k) {
        return this.getBigDecimal(k, Suppliers.nullSupplier());
    }

    default BigDecimal getBigDecimal(K k, BigDecimal def) {
        return this.getBigDecimal(k, () -> def);
    }

    default BigDecimal getBigDecimal(K k, Supplier<BigDecimal> supplier) {
        Valid.notNull(supplier);
        V v = this.get(k);
        if (v == null) {
            return supplier.get();
        }
        return BigDecimals.toBigDecimal(v);
    }

    default BigInteger getBigInteger(K k) {
        return this.getBigInteger(k, Suppliers.nullSupplier());
    }

    default BigInteger getBigInteger(K k, BigInteger def) {
        return this.getBigInteger(k, () -> def);
    }

    default BigInteger getBigInteger(K k, Supplier<BigInteger> supplier) {
        Valid.notNull(supplier);
        V v = this.get(k);
        if (v == null) {
            return supplier.get();
        }
        return BigIntegers.toBigInteger(v);
    }

    default <E> E getObject(K k) {
        return this.getObject(k, Suppliers.nullSupplier());
    }

    default <E> E getObject(K k, E def) {
        return this.getObject(k, () -> def);
    }

    @SuppressWarnings("unchecked")
    default <E> E getObject(K k, Supplier<E> supplier) {
        Valid.notNull(supplier);
        V v = this.get(k);
        if (v == null) {
            return supplier.get();
        }
        return (E) v;
    }

    default V get(K k, V def) {
        return this.get(k, () -> def);
    }

    default V get(K k, Supplier<V> supplier) {
        Valid.notNull(supplier);
        V v = this.get(k);
        if (v == null) {
            return supplier.get();
        }
        return v;
    }

}
