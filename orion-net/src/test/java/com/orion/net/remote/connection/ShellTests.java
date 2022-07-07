package com.orion.net.remote.connection;

import com.orion.lang.function.impl.ReaderLineConsumer;
import com.orion.lang.utils.Threads;
import com.orion.net.remote.connection.ssh.ShellExecutor;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/20 13:38
 */
public class ShellTests {

    public static void main(String[] args) {
        ConnectionStore.enableLogger();
        ConnectionStore store = new ConnectionStore("192.168.146.230")
                .auth("root", "admin123");
        ShellExecutor e = store.getShellExecutor();
        e.streamHandler(ReaderLineConsumer.getDefaultPrint());
        e.exec();
        e.write("ps -ef | grep java\n");
        e.write("ps -ef | grep ssh\n");
        e.write("ping www.baidu.com\n");
        // block
        e.write("ping www.jd.com\n");
        Threads.sleep(2000);
        System.out.println("--------------------------");
        System.out.println(e.isDone());
        e.interrupt();
        e.close();
        System.out.println("--------------------------");
        System.out.println(e.isDone());
        store.close();
        System.exit(0);
    }

}
