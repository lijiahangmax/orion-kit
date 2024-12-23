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
package cn.orionsec.kit.office.csv.reader;

import cn.orionsec.kit.lang.able.SafeCloseable;
import cn.orionsec.kit.lang.utils.Exceptions;

import java.util.Iterator;

/**
 * csv 迭代器
 * <p>
 * 不会存储也不会消费
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/2 18:31
 */
public class CsvReaderIterator<T> implements SafeCloseable, Iterator<T>, Iterable<T> {

    private final BaseCsvReader<T> reader;

    /**
     * 是否为第一次
     */
    private boolean first;

    /**
     * 是否还有下一个元素
     */
    private boolean hasNext;

    /**
     * 下一个元素
     */
    private T next;

    public CsvReaderIterator(BaseCsvReader<T> reader) {
        this.reader = reader;
        this.first = true;
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        if (first) {
            this.first = false;
            this.hasNext = this.getNext();
        }
        return hasNext;
    }

    @Override
    public T next() {
        if (!this.hasNext()) {
            this.throwNoSuch();
        }
        // 获取下一个元素并且返回上一个元素
        T next = this.next;
        this.hasNext = this.getNext();
        return next;
    }

    /**
     * 获取下一个元素
     *
     * @return element
     */
    private boolean getNext() {
        if (reader.end) {
            return false;
        }
        this.next = reader.nextRow();
        return !reader.end;
    }

    /**
     * 抛出异常
     */
    private void throwNoSuch() {
        throw Exceptions.noSuchElement("there are no more elements");
    }

    @Override
    public void close() {
        reader.close();
    }

}
