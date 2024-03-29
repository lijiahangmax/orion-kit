package com.orion.lang.able;

/**
 * 键值对接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/11/18 18:18
 */
public interface IEntryObject<K, V> {

    /**
     * 获取key
     *
     * @return key
     */
    K getKey();

    /**
     * 获取value
     *
     * @return value
     */
    V getValue();

}
