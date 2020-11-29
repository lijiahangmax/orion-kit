package com.orion.able;

/**
 * 数组接口
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/8/23 10:59
 */
public interface Arrayable<E> {

    /**
     * 转化为数组接口
     *
     * @return 数组
     */
    E[] toArray();

}
