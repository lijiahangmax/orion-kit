package com.orion.test.process;

import com.orion.constant.Const;
import com.orion.lang.Console;
import com.orion.process.ProcessAwaitExecutor;
import com.orion.utils.Threads;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/12 17:40
 */
public class ProcessAwaitTests {

    public static void main(String[] args) {
        ping();
    }

    public static void echo() {
        new ProcessAwaitExecutor("echo %JAVA_HOME%")
                .lineHandler((e, l) -> {
                    System.out.println(l);
                })
                .terminal()
                .exec();
    }

    public static void sql() {
        ProcessAwaitExecutor exec = new ProcessAwaitExecutor("mysql -u root -padmin123")
                .lineHandler((e, l) -> {
                    System.out.println(l);
                });
        exec.terminal()
                .redirectError()
                .exec();
        exec.write("use test;");
        exec.write("show tables;");
        exec.write("select * from user limit 2;");
        exec.write("quit;");
    }

    public static void bat() {
        new ProcessAwaitExecutor("C:\\Users\\ljh15\\Desktop\\1.bat")
                .lineHandler((e, l) -> {
                    System.out.println(l);
                })
                .exec();
    }

    public static void ping() {
        ProcessAwaitExecutor e = new ProcessAwaitExecutor("ping www.baidu.com -n 100")
                .lineHandler(Console::trace, Const.GBK);
        e.terminal();
        e.exec();
        Threads.sleep(2000);
        e.close();
    }

}
