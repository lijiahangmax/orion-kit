package com.orion.able;

/**
 * 键值对接口
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/11/18 18:18
 */
public interface EntryAble<K, V> {

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
