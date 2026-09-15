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

import cn.orionsec.kit.lang.function.impl.ReaderLineConsumer;
import cn.orionsec.kit.net.host.telnet.command.TelnetCommandExecutor;
import cn.orionsec.kit.net.host.telnet.command.TelnetExecutors;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

/**
 * TelnetCommand test
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/3/2 1:40
 */
@Ignore("需要真实 Telnet 服务器 无法在单元测试环境连接")
public class TelnetCommandExecutorTests {

    private static final String HOST = "127.0.0.1";

    private static final int PORT = 23;

    private static final String USERNAME = "root";

    private static final String PASSWORD = "admin123";

    private static final String PROMPT = "#";

    private TelnetTunnel s;

    @Before
    public void init() {
        this.s = TelnetTunnel.create(HOST, PORT)
                .username(USERNAME)
                .password(PASSWORD)
                .prompt(PROMPT)
                .connectTimeout(10000)
                .readTimeout(10000)
                .connect();
    }

    @Test
    public void ls() {
        TelnetCommandExecutor e = s.getCommandExecutor("ls -la /");
        e.streamHandler(ReaderLineConsumer.printer());
        e.callback(() -> System.out.println("end...."));
        e.exec();
    }

    @Test
    public void echo() {
        TelnetCommandExecutor e = s.getCommandExecutor("echo $PATH");
        e.keepEcho(true);
        e.streamHandler(ReaderLineConsumer.printer());
        e.callback(() -> System.out.println("end...."));
        e.exec();
    }

    @Test
    public void test1() throws IOException {
        TelnetCommandExecutor executor = s.getCommandExecutor("echo $LC_MEASUREMENT 123");
        executor.keepEcho(true);
        System.out.println(TelnetExecutors.getCommandOutputResultString(executor));
        executor.close();
    }

}
