package com.orion.net.remote.channel;

import com.orion.function.impl.ReaderLineConsumer;
import com.orion.net.remote.CommandExecutors;
import com.orion.net.remote.channel.ssh.CommandExecutor;
import com.orion.utils.Threads;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/6 23:39
 */
public class CommandExecutorTests {

    private SessionStore s;

    @Before
    public void init() {
        this.s = SessionHolder.getSession("192.168.146.230", "root")
                .password("admin123")
                .timeout(20000)
                .connect(20000);
    }

    @Test
    public void ls() {
        SessionHolder.setLogger(SessionLogger.ERROR);
        CommandExecutor e = s.getCommandExecutor("ls -la /root");
        e.callback(exe -> {
            System.out.println("end....");
            System.out.println(e.getExitCode());
            System.out.println(e.isSuccessExit());
            e.close();
        });
        e.streamHandler(ReaderLineConsumer.getDefaultPrint());
        e.errorStreamHandler(ReaderLineConsumer.getDefaultPrint());
        e.connect();
        e.exec();
        Threads.sleep(3000);
    }

    @Test
    public void echo() {
        SessionHolder.setLogger(SessionLogger.INFO);
        CommandExecutor e = s.getCommandExecutor("echo $PATH");
        e.inherit();
        e.streamHandler(ReaderLineConsumer.getDefaultPrint());
        e.callback(exe -> {
            System.out.println("结束....");
            System.out.println(e.getExitCode());
            e.close();
        });
        e.connect();
        e.exec();
        Threads.sleep(3000);
    }

    @Test
    public void test1() throws IOException {
        CommandExecutor executor = s.getCommandExecutor("echo $JAVA_HOME");

        executor.inherit();
        executor.connect();

        System.out.println(CommandExecutors.getCommandOutputResultString(executor));

        System.out.println(executor.getExitCode());
        executor.close();
    }

    @Test
    public void test2() throws IOException {
        CommandExecutor executor = s.getCommandExecutor("/bin/bash -c 'echo $JAVA_HOME'");

        executor.inherit();
        executor.connect();

        System.out.println(CommandExecutors.getCommandOutputResultString(executor));

        System.out.println(executor.getExitCode());
        executor.close();
    }

    @Test
    public void test3() throws IOException {
        CommandExecutor executor = s.getCommandExecutor("ec111ho 1");

        executor.inherit();
        executor.connect();

        System.out.println(CommandExecutors.getCommandOutputResultString(executor));

        System.out.println(executor.getExitCode());
        executor.close();
    }

}
