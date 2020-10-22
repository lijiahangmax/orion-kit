package com.orion.test.remote.channel;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.orion.utils.Threads;
import com.orion.utils.io.Streams;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * @author ljh15
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
        commandOutput.write("ls -la /\n".getBytes());
        Threads.start(() -> {
            try {
                Streams.lineConsumer(resultInput, System.out::println);
            } catch (IOException e) {
                e.printStackTrace();
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
