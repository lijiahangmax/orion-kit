package com.orion.test.mutable;

import com.orion.lang.define.collect.MutableHashMap;
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
 * @since 2022/1/23 14:36
 */
public class MutableMapTests {

    private MutableHashMap<Integer, Integer> map;

    private Integer key;

    @Before
    public void init() {
        this.map = new MutableHashMap<>();
        this.key = 0;
        // map.put(key, 1);
        map.put(key, null);
    }

    @Test
    public void byteTests() {
        System.out.println(map.getByte(key));
        System.out.println(map.getByte(key, (byte) 0));
        System.out.println(map.getByte(key, () -> (byte) 0));
        System.out.println(map.getByteValue(key));
        System.out.println(map.getByteValue(key, (byte) 0));
        System.out.println(map.getByteValue(key, () -> (byte) 0));
    }

    @Test
    public void shortTests() {
        System.out.println(map.getShort(key));
        System.out.println(map.getShort(key, (short) 0));
        System.out.println(map.getShort(key, () -> (short) 0));
        System.out.println(map.getShortValue(key));
        System.out.println(map.getShortValue(key, (short) 0));
        System.out.println(map.getShortValue(key, () -> (short) 0));
    }

    @Test
    public void intTests() {
        System.out.println(map.getInteger(key));
        System.out.println(map.getInteger(key, 0));
        System.out.println(map.getInteger(key, () -> 0));
        System.out.println(map.getIntValue(key));
        System.out.println(map.getIntValue(key, 0));
        System.out.println(map.getIntValue(key, () -> 0));
    }

    @Test
    public void longTests() {
        System.out.println(map.getLong(key));
        System.out.println(map.getLong(key, 0L));
        System.out.println(map.getLong(key, () -> 0L));
        System.out.println(map.getLongValue(key));
        System.out.println(map.getLongValue(key, 0L));
        System.out.println(map.getLongValue(key, () -> 0L));
    }

    @Test
    public void floatTests() {
        System.out.println(map.getFloat(key));
        System.out.println(map.getFloat(key, 0F));
        System.out.println(map.getFloat(key, () -> 0F));
        System.out.println(map.getFloatValue(key));
        System.out.println(map.getFloatValue(key, 0F));
        System.out.println(map.getFloatValue(key, () -> 0F));
    }

    @Test
    public void doubleTests() {
        System.out.println(map.getDouble(key));
        System.out.println(map.getDouble(key, 0D));
        System.out.println(map.getDouble(key, () -> 0D));
        System.out.println(map.getDoubleValue(key));
        System.out.println(map.getDoubleValue(key, 0D));
        System.out.println(map.getDoubleValue(key, () -> 0D));
    }

    @Test
    public void booleanTests() {
        System.out.println(map.getBoolean(key));
        System.out.println(map.getBoolean(key, false));
        System.out.println(map.getBoolean(key, () -> false));
        System.out.println(map.getBooleanValue(key));
        System.out.println(map.getBooleanValue(key, false));
        System.out.println(map.getBooleanValue(key, () -> false));
    }

    @Test
    public void charTests() {
        System.out.println(map.getCharacter(key));
        System.out.println(map.getCharacter(key, '0'));
        System.out.println(map.getCharacter(key, () -> '0'));
        System.out.println(map.getCharValue(key));
        System.out.println(map.getCharValue(key, '0'));
        System.out.println(map.getCharValue(key, () -> '0'));
    }

    @Test
    public void stringTests() {
        System.out.println(map.getString(key));
        System.out.println(map.getString(key, "d"));
        System.out.println(map.getString(key, () -> "d"));
    }

    @Test
    public void dateTests() {
        System.out.println(map.getDate(key));
        System.out.println(map.getDate(key, new Date()));
        System.out.println(map.getDate(key, Date::new));
        System.out.println(map.getLocalDate(key));
        System.out.println(map.getLocalDate(key, LocalDate.now()));
        System.out.println(map.getLocalDate(key, LocalDate::now));
        System.out.println(map.getLocalDateTime(key));
        System.out.println(map.getLocalDateTime(key, LocalDateTime.now()));
        System.out.println(map.getLocalDateTime(key, LocalDateTime::now));
    }

    @Test
    public void numberTests() {
        System.out.println(map.getBigDecimal(key));
        System.out.println(map.getBigDecimal(key, BigDecimal.ONE));
        System.out.println(map.getBigDecimal(key, () -> BigDecimal.valueOf(1L)));
        System.out.println(map.getBigInteger(key));
        System.out.println(map.getBigInteger(key, BigInteger.ONE));
        System.out.println(map.getBigInteger(key, () -> BigInteger.valueOf(1L)));
    }

    @Test
    public void getTests() {
        System.out.println((Object) map.getObject(key));
        System.out.println(map.getObject(key, BigDecimal.ONE));
        System.out.println(map.getObject(key, () -> BigDecimal.valueOf(1L)));
        System.out.println(map.get(key, 123));
        System.out.println(map.get(key, () -> 123));
    }

}
