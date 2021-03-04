package com.orion.remote.channel;

import com.orion.function.impl.ReaderLineBiConsumer;
import com.orion.remote.channel.ssh.ShellExecutor;
import com.orion.utils.Threads;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/7 20:36
 */
public class ShellExecutorTests {

    public static void main(String[] args) {
        SessionHolder.setLogger(SessionLogger.INFO);
        ShellExecutor e = SessionHolder.getSession("root", "192.168.146.230")
                .setPassword("admin123")
                .setTimeout(20000)
                .connect(20000)
                .getShellExecutor();
        e.streamHandler(ReaderLineBiConsumer.getDefaultPrint2());
        e.callback(exe -> System.out.println("end...."));
        e.connect(20000).exec();
        e.write("ps -ef | grep java")
                .write("ps -ef | grep ssh")
                .write("ping www.baidu.com")
                // block
                .write("ping www.jd.com");
        Threads.sleep(4000);
        System.out.println("--------------------------");
        System.out.println(e.isClosed());
        System.out.println(e.isConnected());
        System.out.println("--------------------------");
        e.close();
        System.out.println(e.isClosed());
        System.out.println(e.isConnected());
        System.out.println("--------------------------");
    }

}
