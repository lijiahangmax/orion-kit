package com.orion.net.remote.channel;

import com.orion.lang.function.impl.ReaderLineConsumer;
import com.orion.lang.utils.Threads;
import com.orion.net.remote.channel.ssh.ShellExecutor;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/7 20:36
 */
public class ShellExecutorTests {

    public static void main(String[] args) {
        SessionHolder h = SessionHolder.create();
        h.setLogger(SessionLogger.INFO);
        ShellExecutor e = h.getSession("192.168.146.230", "root")
                .password("admin123")
                // ShellExecutor e = SessionHolder.getSession("192.168.146.230", "root")
                //         .setPassword("admin123")
                .timeout(20000)
                .connect(20000)
                .getShellExecutor();
        e.streamHandler(ReaderLineConsumer.printer());
        e.callback(() -> System.out.println("end...."));
        e.connect(20000);
        e.exec();
        e.write("ps -ef | grep java\n");
        e.write("ps -ef | grep ssh\n");
        e.write("ping www.baidu.com\n");
        // block
        e.write("ping www.jd.com\n");
        Threads.sleep(2000);
        System.out.println("--------------------------");
        System.out.println(e.isClosed());
        System.out.println(e.isConnected());
        System.out.println("--------------------------");
        e.interrupt();
        e.close();
        System.out.println(e.isClosed());
        System.out.println(e.isConnected());
        System.out.println("--------------------------");
        System.exit(0);
    }

}
