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
package cn.orionsec.kit.net.host;

import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.Threads;
import cn.orionsec.kit.lang.utils.io.Streams;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/22 17:06
 */
public class ShellTests {

    private static final String IP1 = "192.168.146.230";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "admin123";

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        JSch c = new JSch();
        Session session = c.getSession(USERNAME, IP1, 22);
        session.setPassword(PASSWORD);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(2000);
        ChannelShell channelShell = (ChannelShell) session.openChannel("shell");

        // 命令输入流
        PipedInputStream commandInput = new PipedInputStream();
        PipedOutputStream commandOutput = new PipedOutputStream(commandInput);

        // 结果输出流
        PipedOutputStream resultOutput = new PipedOutputStream();
        PipedInputStream resultInput = new PipedInputStream(resultOutput);

        channelShell.setInputStream(commandInput);
        channelShell.setOutputStream(resultOutput);
        channelShell.connect(2000);
        commandOutput.write(Strings.bytes("ls -la /\n"));
        Threads.start(() -> {
            try {
                Streams.lineConsumer(resultInput, System.out::println);
            } catch (IOException e) {
                Exceptions.printStacks(e);
            }
        });
        System.out.println(1);
        Thread.sleep(2000);
        commandInput.close();
        commandOutput.close();
        channelShell.disconnect();
        session.disconnect();
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

}
