package com.orion.net.remote.connection;

import com.orion.lang.function.impl.ReaderLineConsumer;
import com.orion.lang.utils.Threads;
import com.orion.net.remote.CommandExecutors;
import com.orion.net.remote.channel.SessionHolder;
import com.orion.net.remote.channel.SessionLogger;
import com.orion.net.remote.connection.ssh.CommandExecutor;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/12 18:47
 */
public class CommandTests {

    private ConnectionStore c;

    @Before
    public void before() {
        this.c = new ConnectionStore("192.168.146.230")
                .auth("root", "admin123");
        ConnectionStore.enableLogger();
    }

    @Test
    public void ls() {
        SessionHolder.HOLDER.setLogger(SessionLogger.ERROR);
        CommandExecutor e = c.getCommandExecutor("ls -la /root");
        e.callback(exe -> {
            System.out.println("end....");
            System.out.println(e.getExitCode());
            System.out.println(e.isSuccessExit());
            e.close();
        });
        e.streamHandler(ReaderLineConsumer.getDefaultPrint());
        e.errorStreamHandler(ReaderLineConsumer.getDefaultPrint());
        e.exec();
        Threads.sleep(3000);
    }

    @Test
    public void echo() {
        SessionHolder.HOLDER.setLogger(SessionLogger.INFO);
        CommandExecutor e = c.getCommandExecutor("echo $PATH");
        e.inherit();
        e.streamHandler(ReaderLineConsumer.getDefaultPrint());
        e.callback(exe -> {
            System.out.println("结束....");
            System.out.println(e.getExitCode());
            e.close();
        });
        e.exec();
        Threads.sleep(3000);
    }

    @Test
    public void test1() throws IOException {
        CommandExecutor executor = c.getCommandExecutor("echo $JAVA_HOME");

        executor.inherit();

        System.out.println(CommandExecutors.getCommandOutputResultString(executor));

        System.out.println(executor.getExitCode());
        executor.close();
    }

    @Test
    public void test2() throws IOException {
        CommandExecutor executor = c.getCommandExecutor("/bin/bash -c 'echo $JAVA_HOME'");

        executor.inherit();

        System.out.println(CommandExecutors.getCommandOutputResultString(executor));

        System.out.println(executor.getExitCode());
        executor.close();
    }

    @Test
    public void test3() throws IOException {
        CommandExecutor executor = c.getCommandExecutor("ec111ho 1");

        executor.inherit();

        System.out.println(CommandExecutors.getCommandOutputResultString(executor));

        System.out.println(executor.getExitCode());
        executor.close();
    }

}
