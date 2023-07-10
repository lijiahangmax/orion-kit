package com.orion.lang.define.cache;

import com.orion.lang.able.Buildable;

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
     * 超时时间
     */
    private long timeout;

    /**
     * 超时时间单位
     */
    private TimeUnit unit;

    public CacheKeyBuilder() {
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
        return new CacheKeyDefine(key, desc, timeout, unit);
    }

}
