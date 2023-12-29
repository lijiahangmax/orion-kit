package com.orion.lang.able;

/**
 * 可获取接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 11:26
 */
public interface Gettable<T> {

    /**
     * 获取
     *
     * @return return
     */
    T get();

}
