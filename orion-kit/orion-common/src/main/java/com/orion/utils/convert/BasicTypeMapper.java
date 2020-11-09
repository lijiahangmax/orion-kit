package com.orion.utils.convert;

import com.orion.function.Conversion;
import com.orion.utils.math.BigDecimals;
import com.orion.utils.math.BigIntegers;
import com.orion.utils.time.Dates;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 基本类型转换映射
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/11/9 15:49
 */
@SuppressWarnings("ALL")
public class BasicTypeMapper implements Serializable {

    private static final long serialVersionUID = -938129593771195L;

    private final TypeStore store;

    public BasicTypeMapper() {
        this(TypeStore.STORE);
    }

    public BasicTypeMapper(TypeStore store) {
        this.store = store;
        this.loadByte();
        this.loadShort();
        this.loadInt();
        this.loadLong();
        this.loadFloat();
        this.loadDouble();
        this.loadBoolean();
        this.loadChar();
        this.loadString();
    }

    private final static Conversion TO_BYTE = Converts::toByte;
    private final static Conversion TO_SHORT = Converts::toShort;
    private final static Conversion TO_INT = Converts::toInt;
    private final static Conversion TO_LONG = Converts::toLong;
    private final static Conversion TO_FLOAT = Converts::toFloat;
    private final static Conversion TO_DOUBLE = Converts::toDouble;
    private final static Conversion TO_BOOLEAN = Converts::toBoolean;
    private final static Conversion TO_CHAR = Converts::toChar;
    private final static Conversion TO_BIG_DECIMAL = BigDecimals::toBigDecimal;
    private final static Conversion TO_BIG_INTEGER = BigIntegers::toBigInteger;
    private final static Conversion TO_STRING = Converts::toString;
    private final static Conversion TO_DATE = Converts::toDate;
    private final static Conversion TO_LOCAL_DATE_TIME = Dates::localDateTime;
    private final static Conversion TO_LOCAL_DATE = Dates::localDate;

    private void loadByte() {
        store.register(Byte.class, Short.class, TO_SHORT);
        store.register(Byte.class, Integer.class, TO_INT);
        store.register(Byte.class, Long.class, TO_LONG);
        store.register(Byte.class, Float.class, TO_FLOAT);
        store.register(Byte.class, Double.class, TO_DOUBLE);
        store.register(Byte.class, Boolean.class, TO_BOOLEAN);
        store.register(Byte.class, Character.class, TO_CHAR);
        store.register(Byte.class, BigDecimal.class, TO_BIG_DECIMAL);
        store.register(Byte.class, BigInteger.class, TO_BIG_INTEGER);
        store.register(Byte.class, String.class, TO_STRING);
    }

    private void loadShort() {
        store.register(Short.class, Byte.class, TO_BYTE);
        store.register(Short.class, Integer.class, TO_INT);
        store.register(Short.class, Long.class, TO_LONG);
        store.register(Short.class, Float.class, TO_FLOAT);
        store.register(Short.class, Double.class, TO_DOUBLE);
        store.register(Short.class, Boolean.class, TO_BOOLEAN);
        store.register(Short.class, Character.class, TO_CHAR);
        store.register(Short.class, BigDecimal.class, TO_BIG_DECIMAL);
        store.register(Short.class, BigInteger.class, TO_BIG_INTEGER);
        store.register(Short.class, String.class, TO_STRING);
    }

    private void loadInt() {
        store.register(Integer.class, Byte.class, TO_BYTE);
        store.register(Integer.class, Short.class, TO_SHORT);
        store.register(Integer.class, Long.class, TO_LONG);
        store.register(Integer.class, Float.class, TO_FLOAT);
        store.register(Integer.class, Double.class, TO_DOUBLE);
        store.register(Integer.class, Boolean.class, TO_BOOLEAN);
        store.register(Integer.class, Character.class, TO_CHAR);
        store.register(Integer.class, BigDecimal.class, TO_BIG_DECIMAL);
        store.register(Integer.class, BigInteger.class, TO_BIG_INTEGER);
        store.register(Integer.class, String.class, TO_STRING);
    }

    private void loadLong() {
        store.register(Long.class, Byte.class, TO_BYTE);
        store.register(Long.class, Short.class, TO_SHORT);
        store.register(Long.class, Integer.class, TO_INT);
        store.register(Long.class, Float.class, TO_FLOAT);
        store.register(Long.class, Double.class, TO_DOUBLE);
        store.register(Long.class, Boolean.class, TO_BOOLEAN);
        store.register(Long.class, Character.class, TO_CHAR);
        store.register(Long.class, BigDecimal.class, TO_BIG_DECIMAL);
        store.register(Long.class, BigInteger.class, TO_BIG_INTEGER);
        store.register(Long.class, String.class, TO_STRING);
        store.register(Long.class, Date.class, TO_DATE);
        store.register(Long.class, LocalDateTime.class, TO_LOCAL_DATE_TIME);
        store.register(Long.class, LocalDate.class, TO_LOCAL_DATE);
    }

    private void loadFloat() {
        store.register(Float.class, Byte.class, TO_BYTE);
        store.register(Float.class, Short.class, TO_SHORT);
        store.register(Float.class, Integer.class, TO_INT);
        store.register(Float.class, Long.class, TO_LONG);
        store.register(Float.class, Double.class, TO_DOUBLE);
        store.register(Float.class, Boolean.class, TO_BOOLEAN);
        store.register(Float.class, Character.class, TO_CHAR);
        store.register(Float.class, BigDecimal.class, TO_BIG_DECIMAL);
        store.register(Float.class, BigInteger.class, TO_BIG_INTEGER);
        store.register(Float.class, String.class, TO_STRING);
    }

    private void loadDouble() {
        store.register(Double.class, Byte.class, TO_BYTE);
        store.register(Double.class, Short.class, TO_SHORT);
        store.register(Double.class, Integer.class, TO_INT);
        store.register(Double.class, Long.class, TO_LONG);
        store.register(Double.class, Float.class, TO_FLOAT);
        store.register(Double.class, Boolean.class, TO_BOOLEAN);
        store.register(Double.class, Character.class, TO_CHAR);
        store.register(Double.class, BigDecimal.class, TO_BIG_DECIMAL);
        store.register(Double.class, BigInteger.class, TO_BIG_INTEGER);
        store.register(Double.class, String.class, TO_STRING);
    }

    private void loadBoolean() {
        store.register(Boolean.class, Byte.class, TO_BYTE);
        store.register(Boolean.class, Short.class, TO_SHORT);
        store.register(Boolean.class, Integer.class, TO_INT);
        store.register(Boolean.class, Long.class, TO_LONG);
        store.register(Boolean.class, Float.class, TO_FLOAT);
        store.register(Boolean.class, Double.class, TO_DOUBLE);
        store.register(Boolean.class, Character.class, TO_CHAR);
        store.register(Boolean.class, BigDecimal.class, TO_BIG_DECIMAL);
        store.register(Boolean.class, BigInteger.class, TO_BIG_INTEGER);
        store.register(Boolean.class, String.class, TO_STRING);
    }

    private void loadChar() {
        store.register(Character.class, Byte.class, TO_BYTE);
        store.register(Character.class, Short.class, TO_SHORT);
        store.register(Character.class, Integer.class, TO_INT);
        store.register(Character.class, Long.class, TO_LONG);
        store.register(Character.class, Float.class, TO_FLOAT);
        store.register(Character.class, Double.class, TO_DOUBLE);
        store.register(Character.class, Boolean.class, TO_BOOLEAN);
        store.register(Character.class, BigDecimal.class, TO_BIG_DECIMAL);
        store.register(Character.class, BigInteger.class, TO_BIG_INTEGER);
        store.register(Character.class, String.class, TO_STRING);
    }

    private void loadString() {
        store.register(String.class, Byte.class, TO_BYTE);
        store.register(String.class, Short.class, TO_SHORT);
        store.register(String.class, Integer.class, TO_INT);
        store.register(String.class, Long.class, TO_LONG);
        store.register(String.class, Float.class, TO_FLOAT);
        store.register(String.class, Double.class, TO_DOUBLE);
        store.register(String.class, Boolean.class, TO_BOOLEAN);
        store.register(String.class, Character.class, TO_CHAR);
        store.register(String.class, BigDecimal.class, TO_BIG_DECIMAL);
        store.register(String.class, BigInteger.class, TO_BIG_INTEGER);
        store.register(String.class, Date.class, TO_DATE);
        store.register(String.class, LocalDateTime.class, TO_LOCAL_DATE_TIME);
        store.register(String.class, LocalDate.class, TO_LOCAL_DATE);
    }

}
