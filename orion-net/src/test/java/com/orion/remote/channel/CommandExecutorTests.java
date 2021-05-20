package com.orion.remote.channel;

import com.orion.function.impl.ReaderLineBiConsumer;
import com.orion.remote.channel.ssh.CommandExecutor;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/6 23:39
 */
public class CommandExecutorTests {

    public static void main(String[] args) {
        ls();
        System.out.println("/---------------------------/");
        echo();
    }

    private static void ls() {
        SessionHolder.setLogger(SessionLogger.ERROR);
        CommandExecutor e = SessionHolder.getSession("192.168.146.230", "root")
                .setPassword("admin123")
                .setTimeout(20000)
                .connect(20000)
                .getCommandExecutor("ls -la /a/b/c");
        e.callback(exe -> {
            System.out.println("end....");
            System.out.println(e.getExitCode());
            e.close();
        });
        e.streamHandler(ReaderLineBiConsumer.getDefaultPrint2());
        e.errorStreamHandler(ReaderLineBiConsumer.getDefaultPrint2());
        e.connect().exec();
    }

    private static void echo() {
        SessionHolder.setLogger(SessionLogger.INFO);
        CommandExecutor e = SessionHolder.getSession("192.168.146.230", "root")
                .setPassword("admin123")
                .setTimeout(20000)
                .connect(20000)
                .getCommandExecutor("echo $PATH");
        e.inherit().streamHandler(ReaderLineBiConsumer.getDefaultPrint2());
        e.callback(exe -> {
            System.out.println("结束....");
            System.out.println(e.getExitCode());
            e.close();
        });
        e.connect().exec();
    }

    @Test
    public void test1() {
        SessionHolder.addIdentity("C:\\Users\\ljh15\\Desktop\\env", "admin123");

        SessionStore store = SessionHolder.getSession("192.168.146.230", "root")
                .connect();

        CommandExecutor executor = store.getCommandExecutor("echo $JAVA_HOME");

        executor.inherit().connect();

        System.out.println(SessionStore.getCommandOutputResultString(executor));

        executor.close();
        store.close();
    }

    @Test
    public void test2() {
        SessionStore store = SessionHolder.getSession("192.168.146.230", "root")
                .setPassword("admin123")
                .connect();

        CommandExecutor executor = store.getCommandExecutor("/bin/bash -c 'echo $JAVA_HOME'");

        executor.inherit().connect();

        System.out.println(SessionStore.getCommandOutputResultString(executor));

        executor.close();
        store.close();
    }

    @Test
    public void test3() {
        SessionStore store = SessionHolder.getSession("192.168.146.230", "root")
                .setPassword("admin123")
                .connect();

        CommandExecutor executor = store.getCommandExecutor("ec111ho 1");

        executor.inherit().connect();

        System.out.println(SessionStore.getCommandOutputResultString(executor));

        executor.close();
        store.close();
    }

}
