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
package cn.orionsec.kit.lang.define.lock;

import cn.orionsec.kit.lang.able.Buildable;

import java.util.concurrent.TimeUnit;

/**
 * 锁 key 构造器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/10/13 10:00
 */
public class LockKeyBuilder implements Buildable<LockKeyDefine> {

    /**
     * 锁 key
     */
    private String key;

    /**
     * 锁描述
     */
    private String desc;

    /**
     * 获取锁等待时间
     */
    private long timeout;

    /**
     * 等待时间单位
     */
    private TimeUnit unit;

    public LockKeyBuilder() {
        this.timeout = LockKeyDefine.DEFAULT_TIMEOUT;
        this.unit = LockKeyDefine.DEFAULT_UNIT;
    }

    public static LockKeyBuilder create() {
        return new LockKeyBuilder();
    }

    /**
     * 设置 key
     *
     * @param key key
     * @return this
     */
    public LockKeyBuilder key(String key) {
        this.key = key;
        return this;
    }

    /**
     * 设置描述
     *
     * @param desc desc
     * @return this
     */
    public LockKeyBuilder desc(String desc) {
        this.desc = desc;
        return this;
    }

    /**
     * 设置获取锁等待时间
     *
     * @param timeout timeout
     * @return this
     */
    public LockKeyBuilder timeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * 设置获取锁等待时间
     *
     * @param timeout timeout
     * @param unit    unit
     * @return this
     */
    public LockKeyBuilder timeout(long timeout, TimeUnit unit) {
        this.timeout = timeout;
        this.unit = unit;
        return this;
    }

    /**
     * 设置等待时间单位
     *
     * @param unit unit
     * @return this
     */
    public LockKeyBuilder unit(TimeUnit unit) {
        this.unit = unit;
        return this;
    }

    @Override
    public LockKeyDefine build() {
        return new LockKeyDefine(key, desc, timeout, unit);
    }

}
