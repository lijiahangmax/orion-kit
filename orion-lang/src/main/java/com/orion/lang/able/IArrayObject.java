package com.orion.lang.able;

/**
 * 数组接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/8/23 10:59
 */
public interface IArrayObject<E> {

    /**
     * 转化为数组接口
     *
     * @return 数组
     */
    E[] toArray();

}
