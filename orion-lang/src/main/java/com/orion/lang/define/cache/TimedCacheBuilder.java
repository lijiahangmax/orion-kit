package com.orion.lang.define.cache;

import com.orion.lang.able.Buildable;

import java.util.function.BiConsumer;

/**
 * 过期缓存
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/13 13:42
 */
public class TimedCacheBuilder implements Buildable<TimedCache> {

    private static final int CHECK_DELAY = 5000;

    /**
     * 过期延迟 ms
     */
    private int expiredDelay;

    /**
     * 检测延迟 ms
     */
    private int checkDelay;

    /**
     * 过期监听器
     */
    private BiConsumer<String, Object> expiredListener;

    private TimedCacheBuilder() {
        this.checkDelay = CHECK_DELAY;
    }

    /**
     * 创建过期缓存
     *
     * @return cache
     */
    public static TimedCacheBuilder create() {
        return new TimedCacheBuilder();
    }

    /**
     * 创建过期缓存
     *
     * @param expired 过期时间
     * @return cache
     */
    public static TimedCache create(int expired) {
        return new TimedCache(expired, CHECK_DELAY, null);
    }

    /**
     * 创建过期缓存
     *
     * @param expired    过期时间
     * @param checkDelay 检测时间
     * @return cache
     */
    public static TimedCache create(int expired, int checkDelay) {
        return new TimedCache(expired, checkDelay, null);
    }

    /**
     * 设置检测延迟 ms
     *
     * @param expiredDelay expiredDelay
     * @return this
     */
    public TimedCacheBuilder expiredDelay(int expiredDelay) {
        this.expiredDelay = expiredDelay;
        return this;
    }

    /**
     * 检测延迟 ms
     *
     * @param checkDelay checkDelay
     * @return this
     */
    public TimedCacheBuilder checkDelay(int checkDelay) {
        this.checkDelay = checkDelay;
        return this;
    }

    /**
     * 过期监听器
     *
     * @param expiredListener expiredListener
     * @return this
     */
    public TimedCacheBuilder expiredListener(BiConsumer<String, Object> expiredListener) {
        this.expiredListener = expiredListener;
        return this;
    }

    @Override
    public TimedCache build() {
        return new TimedCache(expiredDelay, checkDelay, expiredListener);
    }

}
