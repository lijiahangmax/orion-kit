package com.orion.able;

/**
 * 同步执行接口
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/17 13:40
 */
public interface Awaitable<T> {

    /**
     * 同步执行
     */
    T await();

}
