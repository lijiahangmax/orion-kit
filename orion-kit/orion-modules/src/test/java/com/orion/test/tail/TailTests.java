package com.orion.test.tail;

import com.orion.tail.Tracker;
import com.orion.utils.Spells;
import com.orion.utils.Streams;
import com.orion.utils.Strings;
import com.orion.utils.Threads;
import com.orion.utils.file.Files1;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/14 16:07
 */
public class TailTests {

    public static void main(String[] args) throws IOException {
        String r = Streams.readLines(new RandomAccessFile(new File("C:\\Users\\ljh15\\Desktop\\tmp\\tail.txt"), "r"));
        System.out.println(r);
        Tracker tracker = new Tracker(new File("C:\\Users\\ljh15\\Desktop\\tmp\\tail.txt"), (s, l, t) -> {
            System.out.println(l + ": " + s + " " + Spells.isMessyCode(s));
        }).tailStartPos(121);
        tracker.run();
    }


    @Test
    public void testAdd() throws IOException {
        FileOutputStream outputStream = Files1.openOutputStream(new File("C:\\Users\\ljh15\\Desktop\\tmp\\tail.txt"));
        while (true) {
            outputStream.write(Strings.randomChars(10).getBytes());
            outputStream.write(13);
            Threads.sleep(1230);
        }
    }

}
