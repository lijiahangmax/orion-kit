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
package cn.orionsec.kit.lang.exception.argument;

import cn.orionsec.kit.lang.define.wrapper.Wrapper;
import cn.orionsec.kit.lang.utils.Valid;

/**
 * Wrapper 的 exception
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @see Wrapper
 * @since 2021/11/20 0:20
 */
public class WrapperException extends InvalidArgumentException {

    private final Wrapper<?> wrapper;

    public WrapperException(Wrapper<?> wrapper) {
        Valid.notNull(wrapper, "wrapper is null");
        this.wrapper = wrapper;
    }

    public WrapperException(Wrapper<?> wrapper, Throwable cause) {
        super(cause);
        Valid.notNull(wrapper, "wrapper is null");
        this.wrapper = wrapper;
    }

    @SuppressWarnings("unchecked")
    public <T extends Wrapper<?>> T getWrapper() {
        return (T) wrapper;
    }

}
