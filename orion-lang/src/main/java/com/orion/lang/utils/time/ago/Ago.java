/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package com.orion.lang.utils.time.ago;

import java.util.Date;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/29 11:30
 */
public abstract class Ago {

    /**
     * 原时间
     */
    protected Date source;

    /**
     * 对比的时间
     */
    protected Date target;

    /**
     * 配置
     */
    protected DateAgoHint hint;

    public Ago(Date target) {
        this(new Date(), target, null);
    }

    public Ago(Date source, Date target) {
        this(source, target, null);
    }

    public Ago(Date target, DateAgoHint hint) {
        this(new Date(), target, hint);
    }

    public Ago(Date source, Date target, DateAgoHint hint) {
        this.source = source;
        this.target = target;
        this.hint = hint;
    }

    /**
     * 设置配置
     *
     * @param hint hint
     * @return this
     */
    public Ago hint(DateAgoHint hint) {
        this.hint = hint;
        return this;
    }

    /**
     * 获取时间前后
     *
     * @return 时间前后
     */
    public abstract String ago();

    @Override
    public String toString() {
        return ago();
    }
}
