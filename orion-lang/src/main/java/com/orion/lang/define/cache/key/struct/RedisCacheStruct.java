package com.orion.lang.define.cache.key.struct;

/**
 * redis 缓存结构
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/15 0:13
 */
public enum RedisCacheStruct implements CacheStruct {

    /**
     * string
     */
    STRING,

    /**
     * list
     */
    LIST,

    /**
     * hash
     */
    HASH,

    /**
     * set
     */
    SET,

    /**
     * z_set
     */
    Z_SET,

    /**
     * bit
     */
    BIT,

    /**
     * geo
     */
    GEO,

    /**
     * hyper_log_log
     */
    HYPER_LOG_LOG,

    ;

    @Override
    public String getStruct() {
        return this.name();
    }

}
