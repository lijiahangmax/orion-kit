package com.orion.able;

/**
 * 具有主键性质的接口
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/11/18 18:21
 */
public interface Keyable<T> {

    /**
     * 获取 key
     *
     * @return key
     */
    T getKey();

}
