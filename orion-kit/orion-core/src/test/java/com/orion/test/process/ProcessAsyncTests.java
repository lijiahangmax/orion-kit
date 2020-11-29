package com.orion.test.process;

import com.orion.process.ProcessAsyncExecutor;
import org.junit.Test;

import java.io.File;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/12 17:40
 */
public class ProcessAsyncTests {

    @Test
    public void echo() {
        new ProcessAsyncExecutor("echo %JAVA_HOME%")
                .outputFile(new File("C:\\Users\\ljh15\\Desktop\\r1.txt"))
                .terminal()
                .exec();
    }

    @Test
    public void sql() {
        ProcessAsyncExecutor exec = new ProcessAsyncExecutor("mysql -u root -padmin123")
                .outputFile(new File("C:\\Users\\ljh15\\Desktop\\r1.txt"))
                .inputFile(new File("C:\\Users\\ljh15\\Desktop\\r2.txt"));
        exec.terminal()
                .redirectError()
                .exec();

    }

    @Test
    public void bat() {
        new ProcessAsyncExecutor("C:\\Users\\ljh15\\Desktop\\1.bat")
                .outputFile(new File("C:\\Users\\ljh15\\Desktop\\r1.txt"))
                .exec();
    }

}
