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
package cn.orionsec.kit.lang.define.collect;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Valid;

import java.io.Serializable;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 固长队列
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/6 11:30
 */
public class FixedDeque<E> extends ConcurrentLinkedDeque<E> implements Serializable {

    private static final long serialVersionUID = 923412312354068942L;

    private final int maxSize;

    public FixedDeque(int maxSize) {
        this.maxSize = Valid.gt(maxSize, Const.N_0);
    }

    public static <E> FixedDeque<E> create(int maxSize) {
        return new FixedDeque<>(maxSize);
    }

    @Override
    public boolean offerFirst(E s) {
        if (this.isFull()) {
            // 尾位出
            this.pollLast();
        }
        return super.offerFirst(s);
    }

    @Override
    public boolean offerLast(E s) {
        if (this.isFull()) {
            // 首位出
            this.pollFirst();
        }
        return super.offerLast(s);
    }

    /**
     * 队列是否已满
     *
     * @return 是否已满
     */
    public boolean isFull() {
        return this.size() >= maxSize;
    }

}
