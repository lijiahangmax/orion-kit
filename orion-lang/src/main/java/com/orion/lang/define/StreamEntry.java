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
package com.orion.lang.define;

import java.io.Serializable;

/**
 * IO 写入字节实体
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/9 15:47
 */
public class StreamEntry implements Serializable {

    private static final long serialVersionUID = -8954322539057831L;

    private byte[] bytes;

    private int off;

    private int len;

    public StreamEntry(byte[] bytes) {
        this.bytes = bytes;
        this.len = bytes.length;
    }

    public StreamEntry(byte[] bytes, int off, int len) {
        this.bytes = bytes;
        this.off = off;
        this.len = len;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public void setOff(int off) {
        this.off = off;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getOff() {
        return off;
    }

    public int getLen() {
        return len;
    }

    @Override
    public String toString() {
        return "off: " + off + ", len: " + len;
    }

}
