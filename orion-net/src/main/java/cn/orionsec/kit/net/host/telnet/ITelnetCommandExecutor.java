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
package cn.orionsec.kit.net.host.telnet;

import java.io.IOException;

/**
 * Telnet 命令执行器 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/29
 */
public interface ITelnetCommandExecutor extends ITelnetExecutor {

    /**
     * 执行命令
     *
     * @param command command
     * @return 输出内容
     * @throws IOException IOException
     */
    String execCommand(String command) throws IOException;

    /**
     * 执行命令
     *
     * @param command command
     * @param timeout timeout
     * @return 输出内容
     * @throws IOException IOException
     */
    String execCommand(String command, int timeout) throws IOException;

    /**
     * 是否保留命令回显和提示符
     *
     * @param keepEcho keepEcho
     */
    void keepEcho(boolean keepEcho);

    /**
     * 设置最大读取缓冲区字节数
     *
     * @param maxReadBuffer maxReadBuffer
     */
    void maxReadBuffer(int maxReadBuffer);

    /**
     * @return 执行的命令
     */
    String getCommand();

}
