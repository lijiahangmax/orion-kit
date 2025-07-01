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

import cn.orionsec.kit.lang.able.Buildable;
import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.define.cache.key.struct.CacheStruct;

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
     * 缓存 key
     */
    private String key;

    /**
     * 缓存前缀
     */
    private String prefix;

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
     * 设置前缀
     *
     * @param prefix prefix
     * @return this
     */
    public CacheKeyBuilder prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    /**
     * 设置无前缀
     *
     * @return this
     */
    public CacheKeyBuilder noPrefix() {
        this.prefix = Const.EMPTY;
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
        return new CacheKeyDefine(key, prefix, desc, type, struct, timeout, unit);
    }

}
