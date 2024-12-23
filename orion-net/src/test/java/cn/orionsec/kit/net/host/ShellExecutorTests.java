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
package cn.orionsec.kit.net.host;

import cn.orionsec.kit.lang.function.impl.ReaderLineConsumer;
import cn.orionsec.kit.lang.utils.Threads;
import cn.orionsec.kit.net.host.ssh.shell.ShellExecutor;

import java.util.Scanner;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/7 20:36
 */
public class ShellExecutorTests {

    public static void main(String[] args) {
        SessionHolder h = SessionHolder.create();
        h.setLogger(SessionLogger.INFO);
        ShellExecutor e = h.getSession("192.168.146.230", "root")
                .password("admin123")
                .timeout(20000)
                .connect(20000)
                .getShellExecutor();
        e.streamHandler(ReaderLineConsumer.printer());
        e.callback(() -> System.out.println("end...."));
        e.connect(20000);

        // 连接
        Threads.start(e);

        // 手动键入
        Threads.start(() -> {
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                e.writeLine(scanner.nextLine());
            }
        });

        // 键入
        Threads.sleep(1000);
        e.write("ps -ef | grep java\n");
        e.write("ps -ef | grep ssh\n");
        e.write("ping www.baidu.com\n");
        // block
        e.write("ping www.jd.com\n");
        Threads.sleep(2000);
        System.out.println("--------------------------");
        System.out.println(e.isClosed());
        System.out.println(e.isConnected());
        System.out.println("--------------------------");
        e.interrupt();

        // 测试键入
        Threads.sleep(2000);
        System.out.println("------------- 测试键入 20s -------------");
        Threads.sleep(20000);

        // 关闭
        System.out.println("--------------------------");
        e.close();
        System.out.println(e.isClosed());
        System.out.println(e.isConnected());
        System.out.println("--------------------------");
        System.exit(0);
    }

}
