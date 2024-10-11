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
package com.orion.lang.define.io;

import java.io.OutputStream;

/**
 * 输出流到 /dev/null
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/31 15:48
 */
public class IgnoreOutputStream extends OutputStream {

    public static final IgnoreOutputStream OUT = new IgnoreOutputStream();

    /**
     * write to /dev/null
     *
     * @param bs  bs
     * @param off offset
     * @param len length
     */
    @Override
    public void write(byte[] bs, int off, int len) {
    }

    /**
     * write to /dev/null
     *
     * @param b b
     */
    @Override
    public void write(int b) {
    }

    /**
     * write to /dev/null
     *
     * @param bs bs
     */
    @Override
    public void write(byte[] bs) {
    }

}
