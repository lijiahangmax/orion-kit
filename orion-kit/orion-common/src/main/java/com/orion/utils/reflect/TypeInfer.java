package com.orion.utils.reflect;

import com.orion.utils.Converts;
import com.orion.utils.math.BigIntegers;
import com.orion.utils.math.Decimals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * 类型推断
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/22 10:41
 */
class TypeInfer {

    /**
     * 根据class转换object
     *
     * @param o     o
     * @param clazz class
     * @return o
     */
    static Object convert(Object o, Class<?> clazz) {
        if (clazz.equals(byte.class)) {
            return Converts.toByte(o);
        } else if (clazz.equals(Byte.class)) {
            return Converts.toByte(o);
        } else if (clazz.equals(short.class)) {
            return Converts.toShort(o);
        } else if (clazz.equals(Short.class)) {
            return Converts.toShort(o);
        } else if (clazz.equals(int.class)) {
            return Converts.toInt(o);
        } else if (clazz.equals(Integer.class)) {
            return Converts.toInt(o);
        } else if (clazz.equals(long.class)) {
            return Converts.toLong(o);
        } else if (clazz.equals(Long.class)) {
            return Converts.toLong(o);
        } else if (clazz.equals(float.class)) {
            return Converts.toFloat(o);
        } else if (clazz.equals(Float.class)) {
            return Converts.toFloat(o);
        } else if (clazz.equals(double.class)) {
            return Converts.toDouble(o);
        } else if (clazz.equals(Double.class)) {
            return Converts.toDouble(o);
        } else if (clazz.equals(boolean.class)) {
            return Converts.toBoolean(o);
        } else if (clazz.equals(Boolean.class)) {
            return Converts.toBoolean(o);
        } else if (clazz.equals(char.class)) {
            return Converts.toChar(o);
        } else if (clazz.equals(Character.class)) {
            return Converts.toChar(o);
        } else if (clazz.equals(String.class)) {
            return Converts.toString(o);
        } else if (clazz.equals(Date.class)) {
            return Converts.toDate(o);
        } else if (clazz.equals(BigDecimal.class)) {
            return Decimals.toDecimal(o);
        } else if (clazz.equals(BigInteger.class)) {
            return BigIntegers.toBigInteger(o);
        }
        return o;
    }

}
