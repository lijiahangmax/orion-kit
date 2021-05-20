package com.orion.test.tail;

import com.orion.tail.DelayTracker;
import com.orion.tail.mode.FileMinusMode;
import com.orion.tail.mode.FileNotFoundMode;
import com.orion.utils.Strings;
import com.orion.utils.Threads;
import com.orion.utils.io.Files1;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/14 16:07
 */
public class TailTests {

    public static void main(String[] args) throws IOException {
        DelayTracker d = new DelayTracker("C:\\Users\\ljh15\\Desktop\\tail.txt", (s, l, t) -> {
            System.out.println(l + ": " + s);
        }).offset(20)
                .notFoundMode(FileNotFoundMode.WAIT)
                .minusMode(FileMinusMode.CLOSE);
        new Thread(d).start();
    }

    @Test
    public void testAdd() throws IOException {
        FileOutputStream outputStream = Files1.openOutputStream(new File("C:\\Users\\ljh15\\Desktop\\tail.txt"), true);
        while (true) {
            outputStream.write(Strings.bytes(Strings.randomChars(10)));
            outputStream.write(13);
            Threads.sleep(1000);
        }
    }

}
