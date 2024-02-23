package com.orion.lang.define.cache.key.model;

import java.io.Serializable;

/**
 * 包含 id 的缓存模型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/15 0:45
 */
public interface CacheIdModel<T> extends Serializable {

    /**
     * 获取 id
     *
     * @return id
     */
    T getId();

    /**
     * 设置 id
     *
     * @param id id
     */
    void setId(T id);

}
