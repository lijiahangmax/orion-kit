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

import cn.orionsec.kit.lang.able.IdGenerator;
import cn.orionsec.kit.lang.utils.random.Randoms;
import cn.orionsec.kit.lang.utils.time.Dates;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * MongoDB 线程安全 id 生成机
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/7/11 16:14
 */
public class ObjectIdWorker implements IdGenerator<String> {

    /**
     * 自增序列
     */
    private final AtomicInteger seq;

    /**
     * 机器码
     */
    private final int machineCode;

    public ObjectIdWorker(int machineCode) {
        this.machineCode = machineCode;
        this.seq = new AtomicInteger(Randoms.randomInt());
    }

    /**
     * 获取一个 objectId
     *
     * @return objectId
     */
    public byte[] nextBytes() {
        ByteBuffer bb = ByteBuffer.wrap(new byte[12]);
        bb.putInt(((int) (System.currentTimeMillis() / Dates.SECOND_STAMP)));
        bb.putInt(machineCode);
        bb.putInt(seq.getAndIncrement());
        return bb.array();
    }

    /**
     * 获取一个 objectId (无下划线)
     *
     * @return objectId
     */
    @Override
    public String nextId() {
        return this.nextId(false);
    }

    /**
     * 获取一个 objectId
     *
     * @param symbol 是否包含分隔符
     * @return objectId
     */
    public String nextId(boolean symbol) {
        byte[] array = nextBytes();
        StringBuilder buf = new StringBuilder(symbol ? 26 : 24);
        int t;
        for (int i = 0; i < array.length; i++) {
            if (symbol && i % 4 == 0 && i != 0) {
                buf.append("-");
            }
            t = array[i] & 0xFF;
            if (t < 16) {
                buf.append('0');
            }
            buf.append(Integer.toHexString(t));

        }
        return buf.toString();
    }

    public int getMachineCode() {
        return machineCode;
    }

}
