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
