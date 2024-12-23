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
package cn.orionsec.kit.lang.utils.check;

import java.util.zip.Checksum;

/**
 * CRC8 实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/3 22:01
 */
public class CRC8 implements Checksum {

    private final short init;

    private final short[] crcTable;

    private short value;

    public CRC8(int polynomial) {
        this(polynomial, (short) 0);
    }

    public CRC8(int polynomial, short init) {
        this.value = this.init = init;
        this.crcTable = new short[256];
        for (int dividend = 0; dividend < 256; dividend++) {
            int remainder = dividend;// << 8;
            for (int bit = 0; bit < 8; ++bit) {
                if ((remainder & 0x01) != 0) {
                    remainder = (remainder >>> 1) ^ polynomial;
                } else {
                    remainder >>>= 1;
                }
            }
            crcTable[dividend] = (short) remainder;
        }
    }

    @Override
    public void update(byte[] b, int offset, int len) {
        for (int i = 0; i < len; i++) {
            int data = b[offset + i] ^ value;
            this.value = (short) (crcTable[data & 0xFF] ^ (value << 8));
        }
    }

    public void update(byte[] b) {
        this.update(b, 0, b.length);
    }

    @Override
    public void update(int b) {
        this.update(new byte[]{(byte) b}, 0, 1);
    }

    @Override
    public long getValue() {
        return value & 0xFF;
    }

    @Override
    public void reset() {
        this.value = init;
    }

}
