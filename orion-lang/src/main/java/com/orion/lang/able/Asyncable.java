package com.orion.lang.able;

/**
 * 异步执行接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/17 13:41
 */
public interface Asyncable<T> {

    /**
     * 异步执行
     */
    void async(T handler);

}
