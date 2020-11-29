package com.orion.test.remote.channel;

import com.orion.remote.channel.SessionFactory;
import com.orion.remote.channel.SessionLogger;
import com.orion.remote.channel.executor.CommandExecutor;
import com.orion.utils.Threads;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/6 23:39
 */
public class CommandExecutorTests {

    public static void main(String[] args) {
        SessionFactory.setLogger(SessionLogger.INFO);
        CommandExecutor executeChannel = SessionFactory.getSession("root", "192.168.146.230")
                .setPassword("admin123")
                .setTimeout(20000)
                .connect(20000)
                .getCommandExecutor("ls -la /")
                .inherit()
                .lineHandler((exec, l) -> {
                    System.out.println(l);
                });
        // .streamHandler((exec, in) -> {
        //     try {
        //         Streams.copy(in, Files1.openOutputStream("C:\\Users\\ljh15\\Desktop\\key\\1"));
        //     } catch (IOException e) {
        //         // ignore
        //     }
        // });
        executeChannel.connect(20000).exec();
        Threads.sleep(2000);
        System.out.println("--------------------------");
        System.out.println(executeChannel.isDone());
        System.out.println(executeChannel.isClosed());
        System.out.println(executeChannel.isConnected());
        System.out.println(executeChannel.getExitCode());
        System.out.println(executeChannel.isNormalExit());
        System.out.println("--------------------------");
        executeChannel.disconnect().close();
    }

}
