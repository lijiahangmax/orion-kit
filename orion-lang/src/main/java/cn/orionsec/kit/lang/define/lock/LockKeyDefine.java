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

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Strings;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 锁 key 定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/10/13 10:00
 */
public class LockKeyDefine implements Serializable {

    protected static final long DEFAULT_TIMEOUT = 0L;

    protected static final TimeUnit DEFAULT_UNIT = TimeUnit.MILLISECONDS;

    /**
     * 锁 key
     */
    private final String key;

    /**
     * 锁描述
     */
    private final String desc;

    /**
     * 获取锁等待时间
     */
    private final long timeout;

    /**
     * 等待时间单位
     */
    private final TimeUnit unit;

    public LockKeyDefine(String key) {
        this(key, Strings.EMPTY, DEFAULT_TIMEOUT, DEFAULT_UNIT);
    }

    public LockKeyDefine(String key, String desc) {
        this(key, desc, DEFAULT_TIMEOUT, DEFAULT_UNIT);
    }

    public LockKeyDefine(String key, String desc, long timeout, TimeUnit unit) {
        this.key = key;
        this.desc = desc;
        this.timeout = timeout;
        this.unit = unit;
    }

    /**
     * 复制
     *
     * @return define
     */
    public LockKeyDefine copy() {
        return new LockKeyDefine(key, desc, timeout, unit);
    }

    /**
     * 格式化 key 占位符 {}
     *
     * @param param param
     * @return define
     */
    public LockKeyDefine format(Object... param) {
        return new LockKeyDefine(Strings.format(this.key, param), desc, timeout, unit);
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

    public TimeUnit getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return this.getKey() + Const.SPACE + "[" + desc + "] timeout: " + timeout + Const.SPACE + unit.name();
    }

}
