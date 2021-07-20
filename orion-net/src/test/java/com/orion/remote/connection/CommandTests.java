package com.orion.remote.connection;

import com.orion.function.impl.ReaderLineConsumer;
import com.orion.remote.connection.ssh.CommandExecutor;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/12 18:47
 */
public class CommandTests {

    public static void main(String[] args) {
        ConnectionStore.enableLogger();
        ls();
        ConnectionStore.disableLogger();
        System.out.println("//////////////////////////");
        echo();
    }

    private static void ls() {
        CommandExecutor executor = new ConnectionStore("192.168.146.230")
                .auth("root", "admin123")
                // .auth("root", new File("C:\\Users\\ljh15\\Desktop\\server\\key\\230"), "123456")
                .getCommandExecutor("ls -la /as");
        executor.streamHandler(ReaderLineConsumer.getDefaultPrint());
        executor.errorStreamHandler(ReaderLineConsumer.getDefaultPrint());
        // executor.inherit();
        executor.callback(s -> System.out.println("end"));
        executor.exec();
    }

    private static void echo() {
        ConnectionStore c = new ConnectionStore("192.168.146.230")
                .auth("root", "admin123");
        // .auth("root", new File("C:\\Users\\ljh15\\Desktop\\server\\key\\230"), "123456");
        CommandExecutor executor = c.getCommandExecutor("echo 1");
        executor.streamHandler(ReaderLineConsumer.getDefaultPrint());
        executor.callback(s -> {
            System.out.println("执行结束");
            s.close();
            c.close();
        });
        executor.exec();
    }

    @Test
    public void test1() throws IOException {
        ConnectionStore c = new ConnectionStore("192.168.146.230")
                .auth("root", "admin123");

        CommandExecutor executor = c.getCommandExecutor("echo $JAVA_HOME")
                .inherit();

        System.out.println(ConnectionStore.getCommandOutputResultString(executor));

        executor.close();
        c.close();
    }

    @Test
    public void test2() throws IOException {
        ConnectionStore c = new ConnectionStore("192.168.146.230")
                .auth("root", "admin123");

        CommandExecutor executor = c.getCommandExecutor("/bin/bash -c 'echo $JAVA_HOME'")
                .inherit();

        System.out.println(ConnectionStore.getCommandOutputResultString(executor));

        executor.close();
        c.close();
    }

    @Test
    public void test3() throws IOException {
        ConnectionStore c = new ConnectionStore("192.168.146.230")
                .auth("root", "admin123");

        CommandExecutor executor = c.getCommandExecutor("ec111ho 1")
                .inherit();

        System.out.println(ConnectionStore.getCommandOutputResultString(executor));

        executor.close();
        c.close();
    }

}
