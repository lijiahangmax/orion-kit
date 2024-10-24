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
package com.orion.lang.define.number;

import java.io.Serializable;

/**
 * 128位数字表示 分高位和低位
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/31 16:00
 */
public class Number128 implements Serializable {

    private static final long serialVersionUID = -709672812391203L;

    private long lowValue;

    private long highValue;

    /**
     * @param lowValue  低位
     * @param highValue 高位
     */
    public Number128(long lowValue, long highValue) {
        this.lowValue = lowValue;
        this.highValue = highValue;
    }

    public long getLowValue() {
        return lowValue;
    }

    public long getHighValue() {
        return highValue;
    }

    public void setLowValue(long lowValue) {
        this.lowValue = lowValue;
    }

    public void setHighValue(long hiValue) {
        this.highValue = hiValue;
    }

    public long[] getLongArray() {
        return new long[]{lowValue, highValue};
    }

}
