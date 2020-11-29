package com.orion.test.remote.connection;

import com.orion.remote.connection.ConnectionStore;
import com.orion.remote.connection.ssh.ShellExecutor;
import com.orion.utils.Threads;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/20 13:38
 */
public class ShellTests {

    public static void main(String[] args) {
        ConnectionStore store = new ConnectionStore("192.168.146.230")
                .auth("root", "admin123");
        ShellExecutor e = store.getShellExecutor()
                .lineHandler((ex, s) -> {
                    System.out.println(s);
                });
        e.exec();
        e.write("ps -ef | grep java")
                .write("ps -ef | grep ssh")
                .write("ping www.baidu.com");
        Threads.sleep(2000);
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
