package com.orion.test.remote.channel;

import com.orion.remote.channel.SessionFactory;
import com.orion.remote.channel.SessionLogger;
import com.orion.remote.channel.executor.ShellExecutor;
import com.orion.utils.Threads;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/7 20:36
 */
public class ShellExecutorTests {

    public static void main(String[] args) {
        SessionFactory.setLogger(SessionLogger.INFO);
        ShellExecutor e = SessionFactory.getSession("root", "192.168.146.230")
                .setPassword("admin123")
                .setTimeout(20000)
                .connect(20000)
                .getShellExecutor()
                .lineHandler((exec, s) -> {
                    System.out.println(s);
                });
        e.connect(20000).exec();
        e.write("ps -ef | grep java")
                .write("ps -ef | grep ssh")
                .write("ping www.baidu.com");
        Threads.sleep(2000);
        System.out.println("--------------------------");
        System.out.println(e.isClosed());
        System.out.println(e.isConnected());
        System.out.println(e.getExitCode());
        System.out.println(e.isNormalExit());
        System.out.println("--------------------------");
        e.disconnect().close();
        System.out.println(e.isClosed());
        System.out.println(e.isConnected());
        System.out.println(e.getExitCode());
        System.out.println(e.isNormalExit());
        System.out.println("--------------------------");
    }

}
