package com.orion.remote.channel;

import com.orion.function.impl.ReaderLineConsumer;
import com.orion.remote.channel.ssh.ShellExecutor;
import com.orion.utils.Threads;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/7 20:36
 */
public class ShellExecutorTests {

    public static void main(String[] args) {
        SessionHolder.setLogger(SessionLogger.INFO);
        ShellExecutor e = SessionHolder.getSession("192.168.146.230", "root")
                .setPassword("admin123")
                .setTimeout(20000)
                .connect(20000)
                .getShellExecutor();
        e.streamHandler(ReaderLineConsumer.getDefaultPrint());
        e.callback(exe -> System.out.println("end...."));
        e.connect(20000).exec();
        e.write("ps -ef | grep java\n")
                .write("ps -ef | grep ssh\n")
                .write("ping www.baidu.com\n")
                // block
                .write("ping www.jd.com\n");
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
    }

}
