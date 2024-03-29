package com.orion.lang.define.cache.key;

import com.orion.lang.able.Buildable;
import com.orion.lang.define.cache.key.struct.CacheStruct;

import java.util.concurrent.TimeUnit;

/**
 * 缓存key构造器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/10 11:17
 */
public class CacheKeyBuilder implements Buildable<CacheKeyDefine> {

    /**
     * 缓存key
     */
    private String key;

    /**
     * 缓存描述
     */
    private String desc;

    /**
     * 数据类型
     */
    private Class<?> type;

    /**
     * 数据结构
     */
    private CacheStruct struct;

    /**
     * 超时时间
     */
    private long timeout;

    /**
     * 超时时间单位
     */
    private TimeUnit unit;

    public CacheKeyBuilder() {
        this.struct = CacheKeyDefine.DEFAULT_STRUCT;
        this.timeout = CacheKeyDefine.DEFAULT_TIMEOUT;
        this.unit = CacheKeyDefine.DEFAULT_UNIT;
    }

    public static CacheKeyBuilder create() {
        return new CacheKeyBuilder();
    }

    /**
     * 设置 key
     *
     * @param key key
     * @return this
     */
    public CacheKeyBuilder key(String key) {
        this.key = key;
        return this;
    }

    /**
     * 设置描述
     *
     * @param desc desc
     * @return this
     */
    public CacheKeyBuilder desc(String desc) {
        this.desc = desc;
        return this;
    }

    /**
     * 设置数据类型
     *
     * @param type type
     * @return this
     */
    public CacheKeyBuilder type(Class<?> type) {
        this.type = type;
        return this;
    }

    /**
     * 设置数据结构
     *
     * @param struct struct
     * @return this
     */
    public CacheKeyBuilder struct(CacheStruct struct) {
        this.struct = struct;
        return this;
    }

    /**
     * 设置超时时间
     *
     * @param timeout timeout
     * @return this
     */
    public CacheKeyBuilder timeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * 设置超时时间
     *
     * @param timeout timeout
     * @param unit    unit
     * @return this
     */
    public CacheKeyBuilder timeout(long timeout, TimeUnit unit) {
        this.timeout = timeout;
        this.unit = unit;
        return this;
    }

    /**
     * 设置超时时间单位
     *
     * @param unit unit
     * @return this
     */
    public CacheKeyBuilder unit(TimeUnit unit) {
        this.unit = unit;
        return this;
    }

    @Override
    public CacheKeyDefine build() {
        return new CacheKeyDefine(key, desc, type, struct, timeout, unit);
    }

}
