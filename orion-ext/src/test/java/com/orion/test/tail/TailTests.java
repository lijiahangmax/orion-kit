package com.orion.test.tail;

import com.orion.ext.tail.delay.DelayTracker;
import com.orion.ext.tail.delay.DelayTrackerListener;
import com.orion.ext.tail.mode.FileMinusMode;
import com.orion.ext.tail.mode.FileNotFoundMode;
import com.orion.ext.tail.mode.FileOffsetMode;
import com.orion.utils.Strings;
import com.orion.utils.Threads;
import com.orion.utils.io.Files1;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/14 16:07
 */
public class TailTests {

    @Test
    public void read1() {
        DelayTracker tracker = new DelayTracker("C:\\Users\\ljh15\\Desktop\\tail.txt", (s, l, t) -> {
            System.out.println(l + ": " + s);
        });
        tracker.offset(FileOffsetMode.LINE, 5)
                .notFoundMode(FileNotFoundMode.WAIT)
                .minusMode(FileMinusMode.CLOSE)
                .tail();
    }

    @Test
    public void read2() {
        OutputStream out = Files1.openOutputStreamSafe("C:\\Users\\ljh15\\Desktop\\merge.txt");
        DelayTrackerListener tracker = new DelayTrackerListener("C:\\Users\\ljh15\\Desktop\\tail.txt", (s, l, t) -> {
            try {
                if (l != -1) {
                    out.write(s, 0, l);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        tracker.offset(5).notFoundMode(FileNotFoundMode.WAIT)
                .minusMode(FileMinusMode.CLOSE)
                .tail();
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
