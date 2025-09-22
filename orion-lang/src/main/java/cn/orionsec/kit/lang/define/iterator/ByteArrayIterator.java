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
package cn.orionsec.kit.lang.define.iterator;

import cn.orionsec.kit.lang.able.SafeCloseable;
import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.io.Streams;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Iterator;

/**
 * 流迭代器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/26 13:24
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class ByteArrayIterator implements Iterator<Integer>, Iterable<Integer>, SafeCloseable, Serializable {

    private final InputStream in;

    private final byte[] buffer;

    private int read;

    private boolean isRead;

    private boolean autoClose;

    public ByteArrayIterator(InputStream in, byte[] buffer) {
        Assert.notNull(in, "input stream is null");
        Assert.notNull(buffer, "buffer is null");
        this.in = in;
        this.buffer = buffer;
    }

    /**
     * 设置流自动关闭
     *
     * @param autoClose 是否自动关闭
     * @return this
     */
    public ByteArrayIterator autoClose(boolean autoClose) {
        this.autoClose = autoClose;
        return this;
    }

    /**
     * 设置偏移量
     *
     * @param skip 偏移量
     * @return this
     */
    public ByteArrayIterator skip(long skip) {
        try {
            in.skip(skip);
            return this;
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    @Override
    public boolean hasNext() {
        if (read == -1) {
            return false;
        } else if (isRead) {
            return true;
        }
        try {
            this.read = in.read(buffer);
            this.isRead = true;
            return read != -1;
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    @Override
    public Integer next() {
        if (!this.hasNext()) {
            throw Exceptions.noSuchElement("no more data");
        }
        this.isRead = false;
        return read;
    }

    @Override
    public void close() {
        this.read = -1;
        this.isRead = false;
        if (autoClose) {
            Streams.close(in);
        }
    }

    @Override
    public Iterator<Integer> iterator() {
        return this;
    }

}
