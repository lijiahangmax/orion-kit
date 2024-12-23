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
package cn.orionsec.kit.test.tail;

import cn.orionsec.kit.ext.tail.delay.DelayTracker;
import cn.orionsec.kit.ext.tail.delay.DelayTrackerListener;
import cn.orionsec.kit.ext.tail.mode.FileMinusMode;
import cn.orionsec.kit.ext.tail.mode.FileNotFoundMode;
import cn.orionsec.kit.ext.tail.mode.FileOffsetMode;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.Threads;
import cn.orionsec.kit.lang.utils.io.Files1;
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
