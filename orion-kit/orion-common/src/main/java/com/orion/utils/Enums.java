package com.orion.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * 枚举工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/1/3 15:43
 */
public class Enums {

    private Enums() {
    }

    /**
     * 值映射为枚举
     *
     * @param enumClass 枚举类
     * @param value     枚举值
     * @param method    取值方法
     * @param <E>       对应枚举
     * @return ignore
     */
    public static <E extends Enum<?>> E valueOf(Class<E> enumClass, Object value, Method method) {
        E[] es = enumClass.getEnumConstants();
        for (E e : es) {
            Object v;
            try {
                method.setAccessible(true);
                v = method.invoke(e);
            } catch (IllegalAccessException | InvocationTargetException e1) {
                throw new RuntimeException("NoSuchMethod");
            }
            if (value instanceof Number && v instanceof Number && new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(String.valueOf(v))) == 0) {
                return e;
            }
            if (Objects.equals(v, value)) {
                return e;
            }
        }
        return null;
    }

}
