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
package cn.orionsec.kit.ext.location.region.config;

import cn.orionsec.kit.lang.constant.Const;

import java.io.Serializable;

/**
 * @author Jiahang Li
 */
public class DbConfig implements Serializable {

    /**
     * total header data block size
     */
    private int totalHeaderSize;

    /**
     * max index data block size
     * u should always choice the fastest read block size
     */
    private int indexBlockSize;

    public DbConfig(int totalHeaderSize) {
        if ((totalHeaderSize % 8) != 0) {
            totalHeaderSize = Const.BUFFER_KB_16;
        }
        this.totalHeaderSize = totalHeaderSize;
        this.indexBlockSize = Const.BUFFER_KB_8;
    }

    public DbConfig() {
        this(Const.BUFFER_KB_16);
    }

    public void setTotalHeaderSize(int totalHeaderSize) {
        this.totalHeaderSize = totalHeaderSize;
    }

    public void setIndexBlockSize(int dataBlockSize) {
        this.indexBlockSize = dataBlockSize;
    }

    public int getTotalHeaderSize() {
        return totalHeaderSize;
    }

    public int getIndexBlockSize() {
        return indexBlockSize;
    }

}
