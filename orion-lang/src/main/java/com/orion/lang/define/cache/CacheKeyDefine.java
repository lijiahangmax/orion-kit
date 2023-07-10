package com.orion.lang.define.cache;

import com.orion.lang.utils.Strings;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 缓存 key 定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/10 11:12
 */
public class CacheKeyDefine {

    protected static final long DEFAULT_TIMEOUT = 0L;

    protected static final TimeUnit DEFAULT_UNIT = TimeUnit.MILLISECONDS;

    /**
     * 缓存key
     */
    private final String key;

    /**
     * 缓存描述
     */
    private final String desc;

    /**
     * 超时时间
     */
    private long timeout;

    /**
     * 超时时间单位
     */
    private TimeUnit unit;

    public CacheKeyDefine(String key, String desc) {
        this(key, desc, DEFAULT_TIMEOUT, DEFAULT_UNIT);
    }

    public CacheKeyDefine(String key, String desc, long timeout) {
        this(key, desc, timeout, DEFAULT_UNIT);
    }

    public CacheKeyDefine(String key, String desc, long timeout, TimeUnit unit) {
        this.key = key;
        this.desc = desc;
        this.timeout = timeout;
        this.unit = unit;
    }

    /**
     * 格式化 key 占位符 {}
     *
     * @param param param
     * @return key
     */
    public String format(Object... param) {
        return Strings.format(key, param);
    }

    /**
     * 格式化 key 占位符 ${xxx}
     *
     * @param map map
     * @return key
     */
    public String format(Map<?, ?> map) {
        return Strings.format(key, map);
    }

    public String getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return key + " (" + desc + ") timeout: " + timeout + unit.name();
    }
}
