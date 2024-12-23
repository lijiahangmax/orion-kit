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

import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.Systems;
import cn.orionsec.kit.lang.utils.random.Randoms;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * 雪花算法 id 生成工具
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/5/29 14:07
 */
public class SnowFlakes {

    private static final SnowFlakeIdWorker ID_WORKER;

    static {
        ID_WORKER = new SnowFlakeIdWorker(getWorkId(), getDataCenterId());
    }

    /**
     * 获取id
     *
     * @return id
     */
    public static Long nextId() {
        return ID_WORKER.nextId();
    }

    /**
     * 获取工作机器id
     *
     * @return 工作机器id
     */
    private static Long getWorkId() {
        try {
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            int[] code = Strings.getCodePoints(hostAddress);
            int sums = 0;
            for (int b : code) {
                sums += b;
            }
            return (long) (sums % 32);
        } catch (UnknownHostException e) {
            // 如果获取失败, 则使用随机数备用
            return (long) Randoms.randomInt(0, 31);
        }
    }

    /**
     * 通过 hostName 获取机器id
     *
     * @return 机器id
     */
    private static Long getDataCenterId() {
        int[] cps = Strings.getCodePoints(Systems.HOST_NAME);
        int sums = 0;
        for (int i : cps) {
            sums += i;
        }
        return (long) (sums % 32);
    }

}

