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
package cn.orionsec.kit.net.telnet;

import cn.orionsec.kit.net.specification.executor.IRemoteExecutor;

import java.io.IOException;

/**
 * Telnet 执行器 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/3/2 1:40
 */
public interface ITelnetExecutor extends IRemoteExecutor {

    /**
     * 设置提示符
     *
     * @param prompt prompt
     */
    void prompt(String prompt);

    /**
     * 设置编码
     *
     * @param charset charset
     */
    void charset(String charset);

    /**
     * 设置最大读取缓冲区字节数
     *
     * @param maxReadBuffer maxReadBuffer
     */
    void maxReadBuffer(int maxReadBuffer);

    /**
     * 读取直到命中指定内容
     *
     * @param pattern pattern
     * @return 读取内容
     * @throws IOException IOException
     */
    String readUntil(String pattern) throws IOException;

    /**
     * 读取直到命中指定内容
     *
     * @param pattern pattern
     * @param timeout timeout
     * @return 读取内容
     * @throws IOException IOException
     */
    String readUntil(String pattern, int timeout) throws IOException;

    /**
     * 读取直到命中提示符
     *
     * @return 读取内容
     * @throws IOException IOException
     */
    String readUntilPrompt() throws IOException;

    /**
     * 断开连接
     */
    void disconnect();

    /**
     * 是否已连接
     *
     * @return 是否已连接
     */
    boolean isConnected();

    /**
     * @return 提示符
     */
    String getPrompt();

    /**
     * @return 编码
     */
    String getCharset();

    /**
     * @return 读取超时时间 ms
     */
    int getReadTimeout();

    /**
     * @return 最大读取缓冲区字节数
     */
    int getMaxReadBuffer();

    /**
     * @return client
     */
    org.apache.commons.net.telnet.TelnetClient getClient();

}
