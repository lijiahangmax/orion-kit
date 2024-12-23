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
package cn.orionsec.kit.ext.location.region.block;

import cn.orionsec.kit.ext.location.region.core.RegionSupport;

/**
 * @author Jiahang Li
 */
public class IndexBlock {

    private static final int LENGTH = 12;

    /**
     * start ip address
     */
    private long startIp;

    /**
     * end ip address
     */
    private long endIp;

    /**
     * data ptr and data length
     */
    private int dataPtr;

    /**
     * data length
     */
    private int dataLen;

    public IndexBlock(long startIp, long endIp, int dataPtr, int dataLen) {
        this.startIp = startIp;
        this.endIp = endIp;
        this.dataPtr = dataPtr;
        this.dataLen = dataLen;
    }

    public void setStartIp(long startIp) {
        this.startIp = startIp;
    }

    public void setEndIp(long endIp) {
        this.endIp = endIp;
    }

    public void setDataPtr(int dataPtr) {
        this.dataPtr = dataPtr;
    }

    public void setDataLen(int dataLen) {
        this.dataLen = dataLen;
    }

    public long getStartIp() {
        return startIp;
    }

    public long getEndIp() {
        return endIp;
    }

    public int getDataPtr() {
        return dataPtr;
    }

    public int getDataLen() {
        return dataLen;
    }

    public static int getIndexBlockLength() {
        return LENGTH;
    }

    /**
     * get the bytes for storage
     *
     * @return byte[]
     */
    public byte[] getBytes() {
        /*
         * +------------+-----------+-----------+
         * | 4bytes     | 4bytes    | 4bytes    |
         * +------------+-----------+-----------+
         *  start ip      end ip      data ptr + len
         */
        byte[] b = new byte[12];
        // start ip
        RegionSupport.writeIntLong(b, 0, startIp);
        // end ip
        RegionSupport.writeIntLong(b, 4, endIp);
        // write the data ptr and the length
        long mix = dataPtr | ((dataLen << 24) & 0xFF000000L);
        RegionSupport.writeIntLong(b, 8, mix);
        return b;
    }

}
