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

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Threads;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.net.host.telnet.shell.TelnetShellExecutor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TelnetShell test
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/3/2 1:40
 */
@Ignore("需要真实 Telnet 服务器 无法在单元测试环境连接")
public class TelnetShellExecutorTests {

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
    public void shell() {
        List<String> lines = Collections.synchronizedList(new ArrayList<>());
        try {
            TelnetShellExecutor executor = s.getShellExecutor();
            // 监听输出流
            executor.streamHandler(in -> {
                try {
                    Streams.lineConsumer(in, lines::add);
                } catch (Exception e) {
                    // 关闭时会断开流
                }
            });
            // 异步监听
            Threads.start(executor);
            Threads.sleep(1000);
            // 写入命令, 用算术展开让回显和结果不同, 便于断言
            executor.writeLine("echo shell-$((6*7))-ok");
            Threads.sleep(2000);
            executor.writeLine("echo shell-$((1+1))-ok");
            Threads.sleep(2000);
            // 关闭
            executor.close();
            Threads.sleep(500);
        } finally {
            s.close();
        }
        List<String> snapshot;
        synchronized (lines) {
            snapshot = new ArrayList<>(lines);
        }
        String output = String.join(Const.LF, snapshot);
        System.out.println(output);
        Assert.assertTrue("shell output missing first result, actual: " + output, output.contains("shell-42-ok"));
        Assert.assertTrue("shell output missing second result, actual: " + output, output.contains("shell-2-ok"));
    }

}
