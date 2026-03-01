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
 * Telnet 执行器工具
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/3/2 1:40
 */
public class TelnetExecutors {

    private TelnetExecutors() {
    }

    /**
     * @param host     host
     * @param username username
     * @param password password
     * @param command  command
     * @return result
     * @throws IOException IOException
     */
    public static String getCommandOutputResult(String host, String username, String password, String command) throws IOException {
        return getCommandOutputResult(host, TelnetSession.DEFAULT_TELNET_PORT, username, password, command);
    }

    /**
     * @param host     host
     * @param port     port
     * @param username username
     * @param password password
     * @param command  command
     * @return result
     * @throws IOException IOException
     */
    public static String getCommandOutputResult(String host, int port, String username, String password, String command) throws IOException {
        // 建立会话并连接
        try (TelnetSession session = TelnetSession.create(host, port)
                .username(username)
                .password(password)
                .connect();
             TelnetExecutor executor = session.getExecutor()) {
            // 执行命令
            return executor.execCommand(command);
        }
    }

}
