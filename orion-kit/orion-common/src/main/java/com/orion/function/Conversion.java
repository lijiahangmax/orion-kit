package com.orion.function;

/**
 * 转化接口
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/23 10:35
 */
@FunctionalInterface
public interface Conversion<T, R> {

    R apply(T t);

}
