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
package cn.orionsec.kit.lang.id;

import cn.orionsec.kit.lang.utils.Exceptions;

import java.net.InetAddress;

/**
 * 分布式高效有序 id 生产黑科技 (sequence)
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/07/23 19:15
 */
public class Sequences {

    private static byte LAST_IP;

    private static final SequenceIdWorker ID_WORKER;

    static {
        ID_WORKER = new SequenceIdWorker(getLastAddress(), 0x000000FF & getLastAddress());
    }

    /**
     * 生成id
     */
    public static Long nextId() {
        return ID_WORKER.nextId();
    }

    /**
     * 用 IP 地址最后几个字节标识
     * <p>
     * e.g: 192.168.1.30 -> 30
     *
     * @return last IP
     */
    private static byte getLastAddress() {
        if (LAST_IP != 0) {
            return LAST_IP;
        }
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            byte[] addressByte = inetAddress.getAddress();
            LAST_IP = addressByte[addressByte.length - 1];
        } catch (Exception e) {
            throw Exceptions.runtime("unknown host exception", e);
        }
        return (byte) Math.abs(LAST_IP % 3);
    }

}
