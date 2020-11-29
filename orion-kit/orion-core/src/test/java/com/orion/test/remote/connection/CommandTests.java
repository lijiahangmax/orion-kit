package com.orion.test.remote.connection;

import ch.ethz.ssh2.log.Logger;
import com.orion.remote.connection.ConnectionStore;
import com.orion.remote.connection.ssh.CommandExecutor;

import java.io.File;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/12 18:47
 */
public class CommandTests {

    public static void main(String[] args) {
        ls();
    }

    private static void echo() {
        Logger.enabled = true;
        ConnectionStore c = new ConnectionStore("192.168.146.230")
                // .auth("root", "admin123")
                .auth("root", new File("C:\\Users\\ljh15\\Desktop\\server\\key\\230"), "123456");
        CommandExecutor root = c.getCommandExecutor("echo 1")
                .lineHandler((e, l) -> {
                    System.out.println(l);
                });
        root.exec();
    }

    private static void ls() {
        CommandExecutor root = new ConnectionStore("192.168.146.230")
                // .auth("root", "admin123")
                .auth("root", new File("C:\\Users\\ljh15\\Desktop\\server\\key\\230"), "123456")
                .getCommandExecutor("ls -la /")
                .lineHandler((e, l) -> {
                    System.out.println(l);
                });
        root.exec();
    }

}
