/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.lang.define.cache.key;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.define.cache.key.struct.CacheStruct;
import cn.orionsec.kit.lang.define.cache.key.struct.RedisCacheStruct;
import cn.orionsec.kit.lang.utils.Strings;

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

    protected static String globalPrefix = null;

    protected static final CacheStruct DEFAULT_STRUCT = RedisCacheStruct.STRING;

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
        this(key, null, Strings.EMPTY, null, DEFAULT_STRUCT, DEFAULT_TIMEOUT, DEFAULT_UNIT);
    }

    public CacheKeyDefine(String key, String prefix) {
        this(key, prefix, Strings.EMPTY, null, DEFAULT_STRUCT, DEFAULT_TIMEOUT, DEFAULT_UNIT);
    }

    public CacheKeyDefine(String key, String prefix, String desc, Class<?> type, CacheStruct struct, long timeout, TimeUnit unit) {
        this.desc = desc;
        this.type = type;
        this.struct = struct;
        this.timeout = timeout;
        this.unit = unit;
        // 缓存前缀
        if (prefix == null) {
            prefix = globalPrefix;
        }
        if (prefix == null) {
            prefix = Strings.EMPTY;
        }
        this.key = prefix + key;
    }

    /**
     * 设置全局缓存前缀
     *
     * @param globalPrefix globalPrefix
     */
    public static void setGlobalPrefix(String globalPrefix) {
        CacheKeyDefine.globalPrefix = globalPrefix;
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
        return struct + Const.SPACE + key + " (" + desc + ") [" + type.getSimpleName() + "] timeout: " + timeout + Const.SPACE + unit.name();
    }
}
