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
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.Threads;

import java.util.Scanner;

/**
 * Telnet demo
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/3/2 1:40
 */
public class TelnetDemo {

    /**
     * @param args args
     */
    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "127.0.0.1";
        int port = TelnetSession.DEFAULT_TELNET_PORT;
        if (args.length > 1 && Strings.isNotBlank(args[1])) {
            port = Integer.parseInt(args[1]);
        }
        String username = args.length > 2 ? args[2] : null;
        String password = args.length > 3 ? args[3] : null;
        String prompt = args.length > 4 ? args[4] : "$";
        TelnetSession session = TelnetSession.create(host, port)
                .username(username)
                .password(password)
                .prompt(prompt)
                .timeout(10000)
                .readTimeout(10000)
                .connect();
        // 获取执行器
        TelnetExecutor executor = session.getShellExecutor();
        // 设置输出处理器
        executor.streamHandler(ReaderLineConsumer.printer());
        // 设置回调
        executor.callback(() -> System.out.println("end...."));
        executor.connect();
        // 启动执行
        Threads.start(executor);
        // 启动手动输入
        Threads.start(() -> {
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (Strings.isBlank(line)) {
                    continue;
                }
                executor.writeLine(line);
                if ("exit".equalsIgnoreCase(line)) {
                    break;
                }
            }
            executor.close();
            session.close();
        });
    }

}
