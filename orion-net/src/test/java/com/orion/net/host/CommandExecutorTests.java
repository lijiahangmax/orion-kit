package com.orion.net.host;

import com.orion.lang.function.impl.ReaderLineConsumer;
import com.orion.lang.support.timeout.TimeoutChecker;
import com.orion.lang.utils.Threads;
import com.orion.net.host.ssh.command.CommandExecutor;
import com.orion.net.host.ssh.command.CommandExecutors;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/6 23:39
 */
public class CommandExecutorTests {

    private SessionHolder h;

    private SessionStore s;

    @Before
    public void init() {
        this.h = SessionHolder.create();
        this.s = h.getSession("192.168.146.230", "root")
                .password("admin123")
                .timeout(20000)
                .connect(20000);
    }

    @Test
    public void ls() {
        h.setLogger(SessionLogger.ERROR);
        CommandExecutor e = s.getCommandExecutor("ls -la /root");
        e.callback(() -> {
            System.out.println("end....");
            System.out.println(e.getExitCode());
            System.out.println(e.isSuccessExit());
            e.close();
        });
        e.streamHandler(ReaderLineConsumer.printer());
        e.errorStreamHandler(ReaderLineConsumer.printer());
        e.connect();
        e.exec();
    }

    @Test
    public void echo() {
        h.setLogger(SessionLogger.INFO);
        CommandExecutor e = s.getCommandExecutor("echo $PATH");
        e.merge();
        e.streamHandler(ReaderLineConsumer.printer());
        e.callback(() -> {
            System.out.println("结束....");
            System.out.println(e.getExitCode());
            e.close();
        });
        e.connect();
        e.exec();
    }

    @Test
    public void test1() throws IOException {
        CommandExecutor executor = s.getCommandExecutor("echo $LC_MEASUREMENT 123 $JAVA_HOME");
        executor.env("LC_MEASUREMENT", "xxx");
        executor.merge();
        executor.connect();

        System.out.println(CommandExecutors.getCommandOutputResultString(executor));
        System.out.println(executor.getExitCode());
        executor.close();
    }

    @Test
    public void test2() throws IOException {
        CommandExecutor executor = s.getCommandExecutor("echo input\n"
                + "read -p \"enter number:\" no\n"
                + "read -p \"enter name:\" name\n"
                + "echo \"you have entered $no, $name\"");
        // 执行
        Threads.start(() -> {
            try {
                CommandExecutors.execCommand(executor, System.out);
                System.out.println(executor.getExitCode());
                executor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // 等待连接
        Threads.sleep(2000);
        executor.write("1\n");
        Threads.sleep(200);
        executor.write("2\n");

        // 防止关闭
        Threads.sleep(10000);
    }

    @Test
    public void test3() throws IOException {
        CommandExecutor executor = s.getCommandExecutor("ec111ho 1");

        executor.merge();
        executor.connect();

        System.out.println(CommandExecutors.getCommandOutputResultString(executor));

        System.out.println(executor.getExitCode());
        executor.close();
    }

    @Test
    public void timeout() throws IOException {
        // 启动超时检测
        TimeoutChecker checker = TimeoutChecker.create();
        Threads.start(checker);

        CommandExecutor executor = s.getCommandExecutor("top");
        executor.timeout(3000, checker);
        // 执行
        CommandExecutors.execCommand(executor, System.out);

        System.out.println();
        System.out.println("----------------------------------");
        System.out.println(executor.isDone());
        System.out.println(executor.isTimeout());
        System.out.println("----------------------------------");
        executor.close();

        // 防止关闭
        Threads.sleep(10000);
    }

}
