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

import java.io.IOException;
import java.io.InputStream;

/**
 * 限长输入流
 * <p>
 * 最多读取指定长度的数据 读取完后返回 -1
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/10/17 00:00
 */
public class LimitInputStream extends InputStream {

    private final InputStream wrapper;

    private final int limit;

    private int count;

    public LimitInputStream(InputStream wrapper, int limit) {
        this.wrapper = Assert.notNull(wrapper, "wrapper input stream is null");
        Assert.gte(limit, 0, "limit must greater than or equal 0");
        this.limit = limit;
    }

    @Override
    public int read() throws IOException {
        if (count >= limit) {
            return -1;
        }
        int read = wrapper.read();
        if (read != -1) {
            count++;
        }
        return read;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (count >= limit) {
            return -1;
        }
        int read = wrapper.read(b, off, Math.min(len, limit - count));
        if (read != -1) {
            count += read;
        }
        return read;
    }

    @Override
    public long skip(long n) throws IOException {
        long skip = wrapper.skip(Math.min(n, limit - count));
        count += (int) skip;
        return skip;
    }

    @Override
    public int available() throws IOException {
        return Math.min(wrapper.available(), limit - count);
    }

    @Override
    public void close() throws IOException {
        wrapper.close();
    }

}
