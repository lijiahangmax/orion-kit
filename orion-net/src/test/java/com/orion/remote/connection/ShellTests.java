package com.orion.remote.connection;

import com.orion.constant.Const;
import com.orion.function.impl.ReaderLineBiConsumer;
import com.orion.lang.thread.ExecutorBuilder;
import com.orion.remote.connection.ssh.ShellExecutor;
import com.orion.utils.Threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/20 13:38
 */
public class ShellTests {

    private static final ExecutorService EXECUTOR = ExecutorBuilder.create()
            .setNamedThreadFactory("orion-remote-shell-thread-")
            .setCorePoolSize(8)
            .setMaxPoolSize(16)
            .setKeepAliveTime(Const.MS_S_3)
            .setWorkQueue(new LinkedBlockingQueue<>())
            .setAllowCoreThreadTimeOut(true)
            .build();

    public static void main(String[] args) {
        ConnectionStore.enableLogger();
        ConnectionStore store = new ConnectionStore("192.168.146.230")
                .auth("root", "admin123");
        ShellExecutor e = store.getShellExecutor();
        e.streamHandler(ReaderLineBiConsumer.getDefaultPrint2());
        e.scheduler(EXECUTOR);
        e.exec();
        e.write("ps -ef | grep java")
                .write("ps -ef | grep ssh")
                .write("ping www.baidu.com")
                // block
                .write("ping www.jd.com");
        Threads.sleep(5000);
        System.out.println("--------------------------");
        System.out.println(e.isDone());
        System.out.println(e.isClose());
        e.close();
        System.out.println("--------------------------");
        System.out.println(e.isDone());
        System.out.println(e.isClose());
        store.close();
    }

}
