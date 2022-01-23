package com.orion.test.mutable;

import com.orion.lang.collect.MutableArrayList;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/1/23 11:18
 */
public class MutableListTests {

    private MutableArrayList<Integer> list;

    private int key;

    @Before
    public void init() {
        this.list = new MutableArrayList<>();
        this.key = 0;
        list.add(64);
    }

    @Test
    public void byteTests() {
        System.out.println(list.getByte(key));
        System.out.println(list.getByte(key, (byte) 0));
        System.out.println(list.getByte(key, () -> (byte) 0));
        System.out.println(list.getByteValue(key));
        System.out.println(list.getByteValue(key, (byte) 0));
        System.out.println(list.getByteValue(key, () -> (byte) 0));
    }

    @Test
    public void shortTests() {
        System.out.println(list.getShort(key));
        System.out.println(list.getShort(key, (short) 0));
        System.out.println(list.getShort(key, () -> (short) 0));
        System.out.println(list.getShortValue(key));
        System.out.println(list.getShortValue(key, (short) 0));
        System.out.println(list.getShortValue(key, () -> (short) 0));
    }

    @Test
    public void intTests() {
        System.out.println(list.getInteger(key));
        System.out.println(list.getInteger(key, 0));
        System.out.println(list.getInteger(key, () -> 0));
        System.out.println(list.getIntValue(key));
        System.out.println(list.getIntValue(key, 0));
        System.out.println(list.getIntValue(key, () -> 0));
    }

    @Test
    public void longTests() {
        System.out.println(list.getLong(key));
        System.out.println(list.getLong(key, 0L));
        System.out.println(list.getLong(key, () -> 0L));
        System.out.println(list.getLongValue(key));
        System.out.println(list.getLongValue(key, 0L));
        System.out.println(list.getLongValue(key, () -> 0L));
    }

    @Test
    public void floatTests() {
        System.out.println(list.getFloat(key));
        System.out.println(list.getFloat(key, 0F));
        System.out.println(list.getFloat(key, () -> 0F));
        System.out.println(list.getFloatValue(key));
        System.out.println(list.getFloatValue(key, 0F));
        System.out.println(list.getFloatValue(key, () -> 0F));
    }

    @Test
    public void doubleTests() {
        System.out.println(list.getDouble(key));
        System.out.println(list.getDouble(key, 0D));
        System.out.println(list.getDouble(key, () -> 0D));
        System.out.println(list.getDoubleValue(key));
        System.out.println(list.getDoubleValue(key, 0D));
        System.out.println(list.getDoubleValue(key, () -> 0D));
    }

    @Test
    public void booleanTests() {
        System.out.println(list.getBoolean(key));
        System.out.println(list.getBoolean(key, false));
        System.out.println(list.getBoolean(key, () -> false));
        System.out.println(list.getBooleanValue(key));
        System.out.println(list.getBooleanValue(key, false));
        System.out.println(list.getBooleanValue(key, () -> false));
    }

    @Test
    public void charTests() {
        System.out.println(list.getCharacter(key));
        System.out.println(list.getCharacter(key, '0'));
        System.out.println(list.getCharacter(key, () -> '0'));
        System.out.println(list.getCharValue(key));
        System.out.println(list.getCharValue(key, '0'));
        System.out.println(list.getCharValue(key, () -> '0'));
    }

    @Test
    public void stringTests() {
        System.out.println(list.getString(key));
        System.out.println(list.getString(key, "d"));
        System.out.println(list.getString(key, () -> "d"));
    }

    @Test
    public void dateTests() {
        System.out.println(list.getDate(key));
        System.out.println(list.getDate(key, new Date()));
        System.out.println(list.getDate(key, Date::new));
        System.out.println(list.getLocalDate(key));
        System.out.println(list.getLocalDate(key, LocalDate.now()));
        System.out.println(list.getLocalDate(key, LocalDate::now));
        System.out.println(list.getLocalDateTime(key));
        System.out.println(list.getLocalDateTime(key, LocalDateTime.now()));
        System.out.println(list.getLocalDateTime(key, LocalDateTime::now));
    }

    @Test
    public void numberTests() {
        System.out.println(list.getBigDecimal(key));
        System.out.println(list.getBigDecimal(key, BigDecimal.ONE));
        System.out.println(list.getBigDecimal(key, () -> BigDecimal.valueOf(1L)));
        System.out.println(list.getBigInteger(key));
        System.out.println(list.getBigInteger(key, BigInteger.ONE));
        System.out.println(list.getBigInteger(key, () -> BigInteger.valueOf(1L)));
    }

    @Test
    public void getTests() {
        System.out.println((Object) list.getObject(key));
        System.out.println(list.getObject(key, BigDecimal.ONE));
        System.out.println(list.getObject(key, () -> BigDecimal.valueOf(1L)));
        System.out.println(list.get(key, 123));
        System.out.println(list.get(key, () -> 123));
    }

}
