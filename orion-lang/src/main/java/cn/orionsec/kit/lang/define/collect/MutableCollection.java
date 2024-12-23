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
import java.util.Collection;
import java.util.Date;
import java.util.function.*;

/**
 * 可转换的 collection
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/1/23 8:41
 */
public interface MutableCollection<E> extends Collection<E> {

    /**
     * get
     *
     * @param index index
     * @return value
     */
    E get(int index);

    default Byte getByte(int i) {
        return this.getByte(i, Suppliers.nullSupplier());
    }

    default Byte getByte(int i, Byte def) {
        return this.getByte(i, () -> def);
    }

    default Byte getByte(int i, Supplier<Byte> supplier) {
        Valid.notNull(supplier);
        E e = this.get(i);
        if (e == null) {
            return supplier.get();
        }
        return Converts.toByte(e);
    }

    default byte getByteValue(int i) {
        return this.getByteValue(i, Suppliers.BYTE_SUPPLIER);
    }

    default byte getByteValue(int i, byte def) {
        return this.getByteValue(i, () -> def);
    }

    default byte getByteValue(int i, ByteSupplier supplier) {
        Valid.notNull(supplier);
        E e = this.get(i);
        if (e == null) {
            return supplier.getAsByte();
        }
        return Converts.toByte(e);
    }

    default Short getShort(int i) {
        return this.getShort(i, Suppliers.nullSupplier());
    }

    default Short getShort(int i, Short def) {
        return this.getShort(i, () -> def);
    }

    default Short getShort(int i, Supplier<Short> supplier) {
        Valid.notNull(supplier);
        E e = this.get(i);
        if (e == null) {
            return supplier.get();
        }
        return Converts.toShort(e);
    }

    default short getShortValue(int i) {
        return this.getShortValue(i, Suppliers.SHORT_SUPPLIER);
    }

    default short getShortValue(int i, short def) {
        return this.getShortValue(i, () -> def);
    }

    default short getShortValue(int i, ShortSupplier supplier) {
        Valid.notNull(supplier);
        E e = this.get(i);
        if (e == null) {
            return supplier.getAsShort();
        }
        return Converts.toShort(e);
    }

    default Integer getInteger(int i) {
        return this.getInteger(i, Suppliers.nullSupplier());
    }

    default Integer getInteger(int i, Integer def) {
        return this.getInteger(i, () -> def);
    }

    default Integer getInteger(int i, Supplier<Integer> supplier) {
        Valid.notNull(supplier);
        E e = this.get(i);
        if (e == null) {
            return supplier.get();
        }
        return Converts.toInt(e);
    }

    default int getIntValue(int i) {
        return this.getIntValue(i, Suppliers.INT_SUPPLIER);
    }

    default int getIntValue(int i, int def) {
        return this.getIntValue(i, () -> def);
    }

    default int getIntValue(int i, IntSupplier supplier) {
        Valid.notNull(supplier);
        E e = this.get(i);
        if (e == null) {
            return supplier.getAsInt();
        }
        return Converts.toInt(e);
    }

    default Long getLong(int i) {
        return this.getLong(i, Suppliers.nullSupplier());
    }

    default Long getLong(int i, Long def) {
        return this.getLong(i, () -> def);
    }

    default Long getLong(int i, Supplier<Long> supplier) {
        Valid.notNull(supplier);
        E e = this.get(i);
        if (e == null) {
            return supplier.get();
        }
        return Converts.toLong(e);
    }

    default long getLongValue(int i) {
        return this.getLongValue(i, Suppliers.LONG_SUPPLIER);
    }

    default long getLongValue(int i, long def) {
        return this.getLongValue(i, () -> def);
    }

    default long getLongValue(int i, LongSupplier supplier) {
        Valid.notNull(supplier);
        E e = this.get(i);
        if (e == null) {
            return supplier.getAsLong();
        }
        return Converts.toLong(e);
    }

    default Float getFloat(int i) {
        return this.getFloat(i, Suppliers.nullSupplier());
    }

    default Float getFloat(int i, Float def) {
        return this.getFloat(i, () -> def);
    }

    default Float getFloat(int i, Supplier<Float> supplier) {
        Valid.notNull(supplier);
        E e = this.get(i);
        if (e == null) {
            return supplier.get();
        }
        return Converts.toFloat(e);
    }

    default float getFloatValue(int i) {
        return this.getFloatValue(i, Suppliers.FLOAT_SUPPLIER);
    }

    default float getFloatValue(int i, float def) {
        return this.getFloatValue(i, () -> def);
    }

    default float getFloatValue(int i, FloatSupplier supplier) {
        Valid.notNull(supplier);
        E e = this.get(i);
        if (e == null) {
            return supplier.getAsFloat();
        }
        return Converts.toFloat(e);
    }

    default Double getDouble(int i) {
        return this.getDouble(i, Suppliers.nullSupplier());
    }

    default Double getDouble(int i, Double def) {
        return this.getDouble(i, () -> def);
    }

    default Double getDouble(int i, Supplier<Double> supplier) {
        Valid.notNull(supplier);
        E e = this.get(i);
        if (e == null) {
            return supplier.get();
        }
        return Converts.toDouble(e);
    }

    default double getDoubleValue(int i) {
        return this.getDoubleValue(i, Suppliers.DOUBLE_SUPPLIER);
    }

    default double getDoubleValue(int i, double def) {
        return this.getDoubleValue(i, () -> def);
    }

    default double getDoubleValue(int i, DoubleSupplier supplier) {
        Valid.notNull(supplier);
        E e = this.get(i);
        if (e == null) {
            return supplier.getAsDouble();
        }
        return Converts.toDouble(e);
    }

    default Boolean getBoolean(int i) {
        return this.getBoolean(i, Suppliers.nullSupplier());
    }

    default Boolean getBoolean(int i, Boolean def) {
        return this.getBoolean(i, () -> def);
    }

    default Boolean getBoolean(int i, Supplier<Boolean> supplier) {
        Valid.notNull(supplier);
        E e = this.get(i);
        if (e == null) {
            return supplier.get();
        }
        return Converts.toBoolean(e);
    }

    default boolean getBooleanValue(int i) {
        return this.getBooleanValue(i, Suppliers.BOOLEAN_SUPPLIER);
    }

    default boolean getBooleanValue(int i, boolean def) {
        return this.getBooleanValue(i, () -> def);
    }

    default boolean getBooleanValue(int i, BooleanSupplier supplier) {
        Valid.notNull(supplier);
        E e = this.get(i);
        if (e == null) {
            return supplier.getAsBoolean();
        }
        return Converts.toBoolean(e);
    }

    default Character getCharacter(int i) {
        return this.getCharacter(i, Suppliers.nullSupplier());
    }

    default Character getCharacter(int i, Character def) {
        return this.getCharacter(i, () -> def);
    }

    default Character getCharacter(int i, Supplier<Character> supplier) {
        Valid.notNull(supplier);
        E e = this.get(i);
        if (e == null) {
            return supplier.get();
        }
        return Converts.toChar(e);
    }

    default char getCharValue(int i) {
        return this.getCharValue(i, Suppliers.CHAR_SUPPLIER);
    }

    default char getCharValue(int i, char def) {
        return this.getCharValue(i, () -> def);
    }

    default char getCharValue(int i, CharSupplier supplier) {
        Valid.notNull(supplier);
        E e = this.get(i);
        if (e == null) {
            return supplier.getAsChar();
        }
        return Converts.toChar(e);
    }

    default String getString(int i) {
        return this.getString(i, Suppliers.nullSupplier());
    }

    default String getString(int i, String def) {
        return this.getString(i, () -> def);
    }

    default String getString(int i, Supplier<String> supplier) {
        Valid.notNull(supplier);
        E e = this.get(i);
        if (e == null) {
            return supplier.get();
        }
        return Converts.toString(e);
    }

    default Date getDate(int i) {
        return this.getDate(i, Suppliers.nullSupplier());
    }

    default Date getDate(int i, Date def) {
        return this.getDate(i, () -> def);
    }

    default Date getDate(int i, Supplier<Date> supplier) {
        Valid.notNull(supplier);
        E e = this.get(i);
        if (e == null) {
            return supplier.get();
        }
        return Converts.toDate(e);
    }

    default LocalDateTime getLocalDateTime(int i) {
        return this.getLocalDateTime(i, Suppliers.nullSupplier());
    }

    default LocalDateTime getLocalDateTime(int i, LocalDateTime def) {
        return this.getLocalDateTime(i, () -> def);
    }

    default LocalDateTime getLocalDateTime(int i, Supplier<LocalDateTime> supplier) {
        Valid.notNull(supplier);
        E e = this.get(i);
        if (e == null) {
            return supplier.get();
        }
        return Converts.toLocalDateTime(e);
    }

    default LocalDate getLocalDate(int i) {
        return this.getLocalDate(i, Suppliers.nullSupplier());
    }

    default LocalDate getLocalDate(int i, LocalDate def) {
        return this.getLocalDate(i, () -> def);
    }

    default LocalDate getLocalDate(int i, Supplier<LocalDate> supplier) {
        Valid.notNull(supplier);
        E e = this.get(i);
        if (e == null) {
            return supplier.get();
        }
        return Converts.toLocalDate(e);
    }

    default BigDecimal getBigDecimal(int i) {
        return this.getBigDecimal(i, Suppliers.nullSupplier());
    }

    default BigDecimal getBigDecimal(int i, BigDecimal def) {
        return this.getBigDecimal(i, () -> def);
    }

    default BigDecimal getBigDecimal(int i, Supplier<BigDecimal> supplier) {
        Valid.notNull(supplier);
        E e = this.get(i);
        if (e == null) {
            return supplier.get();
        }
        return BigDecimals.toBigDecimal(e);
    }

    default BigInteger getBigInteger(int i) {
        return this.getBigInteger(i, Suppliers.nullSupplier());
    }

    default BigInteger getBigInteger(int i, BigInteger def) {
        return this.getBigInteger(i, () -> def);
    }

    default BigInteger getBigInteger(int i, Supplier<BigInteger> supplier) {
        Valid.notNull(supplier);
        E e = this.get(i);
        if (e == null) {
            return supplier.get();
        }
        return BigIntegers.toBigInteger(e);
    }

    default <V> V getObject(int i) {
        return this.getObject(i, Suppliers.nullSupplier());
    }

    default <V> V getObject(int i, V def) {
        return this.getObject(i, () -> def);
    }

    @SuppressWarnings("unchecked")
    default <V> V getObject(int i, Supplier<V> supplier) {
        Valid.notNull(supplier);
        E e = this.get(i);
        if (e == null) {
            return supplier.get();
        }
        return (V) e;
    }

    default E get(int i, E def) {
        return this.get(i, () -> def);
    }

    default E get(int i, Supplier<E> supplier) {
        Valid.notNull(supplier);
        E e = this.get(i);
        if (e == null) {
            return supplier.get();
        }
        return e;
    }

}
