package com.orion.test.process;

import com.orion.constant.Const;
import com.orion.function.FunctionConst;
import com.orion.function.impl.ReaderLineBiConsumer;
import com.orion.process.ProcessAwaitExecutor;
import com.orion.utils.Threads;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/12 17:40
 */
public class ProcessAwaitTests {

    public static void main(String[] args) {
        sql();
    }

    public static void echo() {
        new ProcessAwaitExecutor("echo %JAVA_HOME%")
                .streamHandler(ReaderLineBiConsumer.getDefaultPrint2())
                .callback(e -> {
                    System.out.println("end");
                    e.close();
                })
                .terminal()
                .exec();
    }

    public static void sql() {
        ProcessAwaitExecutor exec = new ProcessAwaitExecutor("mysql -u root -padmin123")
                .streamHandler(ReaderLineBiConsumer.getDefaultPrint2())
                .callback(e -> {
                    System.out.println("end");
                    e.close();
                });
        exec.terminal()
                .redirectError()
                .exec();
        exec.write("use test;\n");
        exec.write("show tables;\n");
        exec.write("select * from user limit 2;\n");
        exec.write("quit;\n");
    }

    public static void bat() {
        new ProcessAwaitExecutor("C:\\Users\\ljh15\\Desktop\\1.bat")
                .streamHandler(ReaderLineBiConsumer.getDefaultPrint2())
                .exec();
    }

    public static void ping() {
        ProcessAwaitExecutor e = new ProcessAwaitExecutor("ping www.baidu.com -n 100")
                .streamHandler(new ReaderLineBiConsumer().charset(Const.GBK).lineConsumer(FunctionConst.PRINT_2_BI_CONSUMER));
        e.callback(ex -> {
            System.out.println("end");
            ex.close();
        });
        e.terminal();
        e.exec();
        Threads.sleep(2000);
        e.close();
    }

}
