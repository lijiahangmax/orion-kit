package com.orion.lang.define.cache.key;

import com.orion.lang.constant.Const;
import com.orion.lang.define.cache.key.struct.CacheStruct;
import com.orion.lang.define.cache.key.struct.RedisCacheStruct;
import com.orion.lang.utils.Strings;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 缓存 key 定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/10 11:12
 */
public class CacheKeyDefine implements Serializable {

    protected static final long DEFAULT_TIMEOUT = 0L;

    protected static final TimeUnit DEFAULT_UNIT = TimeUnit.MILLISECONDS;

    /**
     * 缓存 key
     */
    private final String key;

    /**
     * 缓存描述
     */
    private final String desc;

    /**
     * 数据类型
     */
    private final Class<?> type;
    /**
     * 数据结构
     */
    private final CacheStruct struct;

    /**
     * 超时时间
     */
    private long timeout;

    /**
     * 超时时间单位
     */
    private TimeUnit unit;

    public CacheKeyDefine(String key) {
        this(key, Strings.EMPTY, null, RedisCacheStruct.STRING, DEFAULT_TIMEOUT, DEFAULT_UNIT);
    }

    public CacheKeyDefine(String key, String desc, Class<?> type, CacheStruct struct, long timeout, TimeUnit unit) {
        this.key = key;
        this.desc = desc;
        this.type = type;
        this.struct = struct;
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

    public Class<?> getType() {
        return type;
    }

    public CacheStruct getStruct() {
        return struct;
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
        return struct + Const.EMPTY + key + " (" + desc + ") [" + type.getSimpleName() + "] timeout: " + timeout + Const.SPACE + unit.name();
    }
}
