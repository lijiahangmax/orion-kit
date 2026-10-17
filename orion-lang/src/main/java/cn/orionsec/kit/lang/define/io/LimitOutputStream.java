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
package cn.orionsec.kit.lang.define.io;

import cn.orionsec.kit.lang.utils.Assert;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * 限长输出流
 * <p>
 * 写入的数据超过上限后直接丢弃 不会抛出异常
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/10/17 00:00
 */
public class LimitOutputStream extends OutputStream {

    private final ByteArrayOutputStream buffer;

    private final int limit;

    public LimitOutputStream(int limit) {
        Assert.gte(limit, 0, "limit must greater than or equal 0");
        this.limit = limit;
        this.buffer = new ByteArrayOutputStream();
    }

    @Override
    public void write(int b) {
        if (buffer.size() < limit) {
            buffer.write(b);
        }
    }

    @Override
    public void write(byte[] bs) {
        this.write(bs, 0, bs.length);
    }

    @Override
    public void write(byte[] bs, int off, int len) {
        int remain = limit - buffer.size();
        if (remain <= 0) {
            return;
        }
        buffer.write(bs, off, Math.min(len, remain));
    }

    /**
     * 获取已写入的数据
     *
     * @return 数据
     */
    public byte[] toByteArray() {
        return buffer.toByteArray();
    }

}
