/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package com.orion.ext.tail.delay;

import com.orion.ext.tail.handler.LineHandler;
import com.orion.lang.constant.Const;
import com.orion.lang.utils.Spells;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.FileReaders;

import java.io.File;
import java.io.IOException;

/**
 * 延时文件追踪器 行
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/14 16:13
 */
public class DelayTracker extends AbstractDelayTracker {

    /**
     * 读取的行
     */
    private int accessCount;

    /**
     * 行处理器
     */
    private final LineHandler handler;

    public DelayTracker(String tailFile, LineHandler handler) {
        this(new File(tailFile), handler);
    }

    public DelayTracker(File tailFile, LineHandler handler) {
        super(tailFile);
        this.handler = Valid.notNull(handler, "line handler is null");
    }

    @Override
    protected void read() throws IOException {
        String read = FileReaders.readAllLines(reader, charset);
        if (read == null || read.isEmpty()) {
            return;
        }

        String[] lines = Strings.replaceCRLF(read).split(Const.LF);
        for (String line : lines) {
            // if (accessCount == 0 && fileOffsetMode.equals(FileOffsetMode.BYTE) && offset > 0) {
            //     line = cleanMissCode(line);
            // }
            handler.readLine(line, accessCount++, this);
        }
    }

    /**
     * 去除乱码
     *
     * @param str str
     * @return str
     */
    public static String cleanMissCode(String str) {
        int testCount = Math.min(str.length(), 2);
        for (int i = testCount; i > 0; i--) {
            if (Spells.isMessyCode(String.valueOf(str.charAt(i - 1)))) {
                str = str.substring(i);
                break;
            }
        }
        return str;
    }

}
