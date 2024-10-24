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
package com.orion.ext.location.region.block;

import com.orion.ext.location.region.core.RegionSupport;

/**
 * @author Jiahang Li
 */
public class HeaderBlock {

    /**
     * index block start ip address
     */
    private long indexStartIp;

    /**
     * ip address
     */
    private int indexPtr;

    public HeaderBlock(long indexStartIp, int indexPtr) {
        this.indexStartIp = indexStartIp;
        this.indexPtr = indexPtr;
    }

    public void setIndexStartIp(long indexStartIp) {
        this.indexStartIp = indexStartIp;
    }

    public void setIndexPtr(int indexPtr) {
        this.indexPtr = indexPtr;
    }

    public long getIndexStartIp() {
        return indexStartIp;
    }

    public int getIndexPtr() {
        return indexPtr;
    }

    /**
     * get the bytes for db storage
     *
     * @return byte[]
     */
    public byte[] getBytes() {
        /*
         * +------------+-----------+
         * | 4bytes     | 4bytes    |
         * +------------+-----------+
         *  start ip      index ptr
         */
        byte[] b = new byte[8];
        RegionSupport.writeIntLong(b, 0, indexStartIp);
        RegionSupport.writeIntLong(b, 4, indexPtr);
        return b;
    }
}
