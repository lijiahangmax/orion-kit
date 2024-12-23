/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.test.process;

import cn.orionsec.kit.ext.process.ProcessAwaitExecutor;
import cn.orionsec.kit.ext.process.Processes;
import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.function.impl.ReaderLineConsumer;
import cn.orionsec.kit.lang.utils.Threads;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/12 17:40
 */
public class ProcessAwaitTests {

    public static void main(String[] args) {
        ping();
    }

    public static void echo() {
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor("echo %JAVA_HOME%");
        executor.streamHandler(ReaderLineConsumer.printer())
                .callback(() -> {
                    System.out.println("end");
                    executor.close();
                })
                .terminal()
                .exec();
    }

    public static void sql() {
        ProcessAwaitExecutor exec = new ProcessAwaitExecutor("mysql -u root -padmin123");
        exec.streamHandler(ReaderLineConsumer.printer(Const.GBK));
        exec.callback(() -> {
            System.out.println("end");
            exec.close();
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
        ProcessAwaitExecutor exec = new ProcessAwaitExecutor("C:\\Users\\lijiahang\\Desktop\\1.bat");
        exec.streamHandler(ReaderLineConsumer.printer())
                .callback(() -> {
                    System.out.println("end");
                    exec.close();
                })
                .exec();
    }

    public static void ping() {
        ProcessAwaitExecutor e = new ProcessAwaitExecutor("ping www.baidu.com -n 100");
        e.streamHandler(ReaderLineConsumer.printer(Const.GBK));
        e.callback(() -> {
            System.out.println("end");
        });
        e.terminal();
        e.exec();
        Threads.sleep(5000);
        e.close();
    }

    public static void gcWrong() {
        // 关闭的是cmd.exe 而不是 jstat, 则jstat子进程还是在运行中
        ProcessAwaitExecutor e = new ProcessAwaitExecutor("jstat -gc 13484 500")
                .streamHandler(ReaderLineConsumer.printer(Const.GBK));
        e.callback(() -> {
            System.out.println("end");
        });
        e.terminal();
        e.exec();
        Threads.sleep(5000);
        e.close();
    }

    public static void gcRight() {
        // 在文件夹运行 关闭的是jstat
        ProcessAwaitExecutor e = new ProcessAwaitExecutor(new String[]{"jstat", "-gc", "13484", "500"})
                .streamHandler(ReaderLineConsumer.printer(Const.GBK));
        e.callback(() -> {
            System.out.println("end");
        });
        e.dir("D:\\JDK1.8\\bin");
        e.redirectError();
        e.exec();
        Threads.sleep(5000);
        e.close();
    }

    @Test
    public void testResult1() {
        System.out.println(Processes.getOutputResultString("echo %JAVA_HOME%"));
    }

    @Test
    public void testResult2() throws UnsupportedEncodingException {
        System.out.println(new String(Processes.getOutputResult(true, "echo111 %JAVA_HOME%"), Const.GBK));
    }

    @Test
    public void testResult3() throws UnsupportedEncodingException {
        System.out.println(new String(Processes.getOutputResultWithDir(true, "D:\\JDK1.8\\bin", "jps", "-lv"), Const.GBK));
    }

    @Test
    public void testResult4() throws UnsupportedEncodingException {
        System.out.println(new String(Processes.getOutputResult(true, "jps", "-lv"), Const.GBK));
    }

    @Test
    public void testResult5() throws UnsupportedEncodingException {
        System.out.println(new String(Processes.getOutputResultWithDir(true, "A:\\Work\\jdk1.8\\bin1111\\", "jps", "-lv"), Const.GBK));
    }

}
