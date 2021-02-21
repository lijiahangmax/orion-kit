package com.orion.test.remote.connection;

import com.orion.function.impl.ReaderLineBiConsumer;
import com.orion.remote.connection.ConnectionStore;
import com.orion.remote.connection.ssh.CommandExecutor;

/**
 * @author ljh15
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
        executor.streamHandler(ReaderLineBiConsumer.getDefaultPrint2());
        executor.errorStreamHandler(ReaderLineBiConsumer.getDefaultPrint2());
        // executor.inherit();
        executor.callback(s -> System.out.println("end"));
        executor.exec();
    }

    private static void echo() {
        ConnectionStore c = new ConnectionStore("192.168.146.230")
                .auth("root", "admin123");
        // .auth("root", new File("C:\\Users\\ljh15\\Desktop\\server\\key\\230"), "123456");
        CommandExecutor executor = c.getCommandExecutor("echo 1");
        executor.streamHandler(ReaderLineBiConsumer.getDefaultPrint2());
        executor.callback(s -> {
            System.out.println("执行结束");
            s.close();
            c.close();
        });
        executor.exec();
    }

}
