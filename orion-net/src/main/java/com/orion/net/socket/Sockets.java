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
package com.orion.net.socket;

import com.orion.lang.utils.Valid;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Socket 工具
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/16 11:23
 */
public class Sockets {

    private Sockets() {
    }

    /**
     * 获取一个指定端口中可用端口的 ServerSocket
     *
     * @param ports 端口
     * @return ServerSocket 未找到返回null
     */
    public static ServerSocket create(int[] ports) {
        for (int port : ports) {
            try {
                return new ServerSocket(port);
            } catch (IOException ex) {
                // try next port
            }
        }
        return null;
    }

    /**
     * 获取一个可用端口的 ServerSocket (5000 - 65535]
     *
     * @return ServerSocket 未找到返回null
     */
    public static ServerSocket create() {
        return create(5001, 65535);
    }

    /**
     * 获取一个指定端口范围中可用端口的 ServerSocket
     *
     * @param start 端口开始 > 1000
     * @param end   端口结束 <= 65535
     * @return ServerSocket 未找到返回null
     */
    public static ServerSocket create(int start, int end) {
        Valid.gt(start, 1000, "start port must greater than 1000");
        Valid.gt(end, 1000, "end port must greater than 1000");
        Valid.lte(end, 65535, "end port must less than 65536");
        for (int i = start; i <= end; i++) {
            try {
                return new ServerSocket(i);
            } catch (IOException ex) {
                // try next port
            }
        }
        return null;
    }

}
