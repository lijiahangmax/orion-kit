package com.orion.able;

/**
 * 执行接口
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/11/18 18:29
 */
public interface Processable<T, R> {

    R execute(T t);

}
