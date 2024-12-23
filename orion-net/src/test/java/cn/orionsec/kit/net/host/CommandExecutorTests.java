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
import cn.orionsec.kit.lang.support.timeout.TimeoutChecker;
import cn.orionsec.kit.lang.support.timeout.TimeoutCheckers;
import cn.orionsec.kit.lang.support.timeout.TimeoutEndpoint;
import cn.orionsec.kit.lang.utils.Threads;
import cn.orionsec.kit.net.host.ssh.command.CommandExecutor;
import cn.orionsec.kit.net.host.ssh.command.CommandExecutors;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/6 23:39
 */
public class CommandExecutorTests {

    private SessionHolder h;

    private SessionStore s;

    @Before
    public void init() {
        this.h = SessionHolder.create();
        this.s = h.getSession("192.168.146.230", "root")
                .password("admin123")
                .timeout(20000)
                .connect(20000);
    }

    @Test
    public void ls() {
        h.setLogger(SessionLogger.ERROR);
        CommandExecutor e = s.getCommandExecutor("ls -la /root");
        e.callback(() -> {
            System.out.println("end....");
            System.out.println(e.getExitCode());
            System.out.println(e.isSuccessExit());
            e.close();
        });
        e.streamHandler(ReaderLineConsumer.printer());
        e.errorStreamHandler(ReaderLineConsumer.printer());
        e.connect();
        e.exec();
    }

    @Test
    public void echo() {
        h.setLogger(SessionLogger.INFO);
        CommandExecutor e = s.getCommandExecutor("echo $PATH");
        e.merge();
        e.streamHandler(ReaderLineConsumer.printer());
        e.callback(() -> {
            System.out.println("结束....");
            System.out.println(e.getExitCode());
            e.close();
        });
        e.connect();
        e.exec();
    }

    @Test
    public void test1() throws IOException {
        CommandExecutor executor = s.getCommandExecutor("echo $LC_MEASUREMENT 123 $JAVA_HOME");
        executor.env("LC_MEASUREMENT", "xxx");
        executor.merge();
        executor.connect();

        System.out.println(CommandExecutors.getCommandOutputResultString(executor));
        System.out.println(executor.getExitCode());
        executor.close();
    }

    @Test
    public void test2() {
        CommandExecutor executor = s.getCommandExecutor("echo input\n"
                + "read -p \"enter number:\" no\n"
                + "read -p \"enter name:\" name\n"
                + "echo \"you have entered $no, $name\"");
        // 执行
        Threads.start(() -> {
            try {
                CommandExecutors.execCommand(executor, System.out);
                System.out.println(executor.getExitCode());
                executor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // 等待连接
        Threads.sleep(2000);
        executor.write("1\n");
        Threads.sleep(200);
        executor.write("2\n");

        // 防止关闭
        Threads.sleep(10000);
    }

    @Test
    public void test3() throws IOException {
        CommandExecutor executor = s.getCommandExecutor("ec111ho 1");

        executor.merge();
        executor.connect();

        System.out.println(CommandExecutors.getCommandOutputResultString(executor));

        System.out.println(executor.getExitCode());
        executor.close();
    }

    @Test
    public void timeout() throws IOException {
        // 启动超时检测
        TimeoutChecker<TimeoutEndpoint> checker = TimeoutCheckers.create();
        Threads.start(checker);

        CommandExecutor executor = s.getCommandExecutor("top");
        executor.timeout(3000, checker);
        // 执行
        CommandExecutors.execCommand(executor, System.out);

        System.out.println();
        System.out.println("----------------------------------");
        System.out.println(executor.isDone());
        System.out.println(executor.isTimeout());
        System.out.println("----------------------------------");
        executor.close();

        // 防止关闭
        Threads.sleep(10000);
    }

}
