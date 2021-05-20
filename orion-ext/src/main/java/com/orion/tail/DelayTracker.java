package com.orion.tail;

import com.orion.constant.Const;
import com.orion.tail.handler.LineHandler;
import com.orion.tail.mode.FileMinusMode;
import com.orion.tail.mode.FileNotFoundMode;
import com.orion.utils.*;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 延时文件追踪器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/14 16:13
 */
public class DelayTracker extends Tracker {

    /**
     * 追踪的文件
     */
    private File tailFile;

    /**
     * RandomAccessFile
     */
    private RandomAccessFile reader;

    /**
     * 延迟时间
     */
    private int delayMillis;

    /**
     * 编码集
     */
    private String charset;

    /**
     * 读取的行
     */
    private int accessCount;

    /**
     * 开始监听的文件偏移量
     * <p>
     * =-1 从文件当前大小开始
     * =0 从0开始
     * >0 从length-offset 开始
     */
    private long offset;

    /**
     * 运行flag
     */
    private volatile boolean run;

    /**
     * 行处理器
     */
    private LineHandler handler;

    /**
     * 启动时未找到文件 处理模式
     */
    private FileNotFoundMode notFoundMode;

    /**
     * 启动时未找到文件 处理次数
     */
    private int notFountTimes;

    /**
     * 运行时文件大小减少 处理模式
     */
    private FileMinusMode minusMode;

    public DelayTracker(String tailFile, LineHandler handler) {
        this(new File(tailFile), handler);
    }

    public DelayTracker(File tailFile, LineHandler handler) {
        Valid.notNull(handler, "line handler is null");
        this.tailFile = tailFile;
        this.handler = handler;
        this.delayMillis = Const.MS_S_1;
        this.charset = Const.UTF_8;
        this.notFoundMode = FileNotFoundMode.CLOSE;
        this.minusMode = FileMinusMode.CLOSE;
    }

    @Override
    public void tail() {
        try {
            if (!this.init()) {
                return;
            }
            this.run = true;
            this.setSeek();
            // 上次检查文件更改的时间
            long lastMod = 0;
            // 上次文件大小
            long lastLen = tailFile.length();
            while (run) {
                if (tailFile.lastModified() > lastMod) {
                    long length = tailFile.length();
                    if (lastLen > length && reader.getFilePointer() >= length) {
                        // 文件减少 重新设置
                        this.resetSeek();
                    }
                    String read = Streams.readLines(reader, charset);
                    if (read == null || read.isEmpty()) {
                        continue;
                    }
                    String[] lines = read.replaceAll(Const.CR_LF, Const.LF)
                            .replaceAll(Const.CR, Const.LF)
                            .split(Const.LF);
                    for (String line : lines) {
                        if (accessCount == 0) {
                            line = cleanMissCode(line);
                        }
                        handler.readLine(line, accessCount++, this);
                    }
                }
                lastMod = tailFile.lastModified();
                lastLen = tailFile.length();
                Threads.sleep(delayMillis);
            }
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            this.accessCount = 0;
            Streams.close(reader);
            this.reader = null;
        }
    }

    /**
     * 初始化
     *
     * @return 是否初始化成功
     * @throws IOException IOException
     */
    private boolean init() throws IOException {
        if (!tailFile.exists() || tailFile.isDirectory()) {
            switch (notFoundMode) {
                case WAIT:
                    this.run = true;
                    while (run) {
                        Threads.sleep(delayMillis);
                        if (tailFile.exists() && tailFile.isFile()) {
                            this.reader = Files1.openRandomAccess(tailFile, "r");
                            return true;
                        }
                    }
                    return run = false;
                case WAIT_TIMES:
                    int fi = 1, fd = notFountTimes, last = notFountTimes;
                    if (notFountTimes > delayMillis) {
                        long s = notFountTimes / delayMillis;
                        long m = notFountTimes % delayMillis;
                        fd = delayMillis;
                        if (m == 0) {
                            last = delayMillis;
                            fi = (int) s;
                        } else {
                            last = (int) m;
                            fi += (int) s;
                        }
                    }
                    for (int i = 0; i < fi; i++) {
                        if (i == fi - 1) {
                            Threads.sleep(last);
                        } else {
                            Threads.sleep(fd);
                        }
                        if (run && tailFile.exists() && tailFile.isFile()) {
                            this.reader = Files1.openRandomAccess(tailFile, "r");
                            return true;
                        }
                    }
                    return false;
                case WAIT_COUNT:
                    for (int i = 0; i < notFountTimes; i++) {
                        Threads.sleep(delayMillis);
                        if (run && tailFile.exists() && tailFile.isFile()) {
                            this.reader = Files1.openRandomAccess(tailFile, "r");
                            return true;
                        }
                    }
                    return false;
                case THROWS:
                    throw Exceptions.notFound(Strings.format("tail file {} not found", tailFile.getAbsolutePath()));
                case CLOSE:
                default:
                    return false;
            }
        } else {
            this.reader = Files1.openRandomAccess(tailFile, "r");
            return true;
        }
    }

    /**
     * 设置初始化位置
     */
    private void setSeek() throws IOException {
        long length = tailFile.length();
        if (offset == -1) {
            reader.seek(length);
        } else if (offset > 0) {
            long s = length - offset;
            if (s > 0) {
                reader.seek(s);
            }
        }
    }

    /**
     * 重新设置初始化位置
     */
    private void resetSeek() throws IOException {
        switch (minusMode) {
            case CLOSE:
                this.run = false;
                return;
            case CURRENT:
                reader.seek(tailFile.length());
                return;
            case OFFSET:
                long length = tailFile.length();
                long s = length - offset;
                if (s > 0) {
                    reader.seek(s);
                } else {
                    reader.seek(0);
                }
                return;
            case RESUME:
                reader.seek(0);
                return;
            case THROWS:
                throw Exceptions.state(Strings.format("tail file {} minus", tailFile.getAbsolutePath()));
            default:
        }
    }

    @Override
    public void stop() {
        this.run = false;
    }

    public DelayTracker offset(long offset) {
        this.offset = offset;
        return this;
    }

    public DelayTracker delayMillis(int delayMillis) {
        this.delayMillis = delayMillis;
        return this;
    }

    public DelayTracker charset(String charset) {
        this.charset = charset;
        return this;
    }

    public DelayTracker notFoundMode(FileNotFoundMode notFoundMode) {
        return notFoundMode(notFoundMode, 0);
    }

    public DelayTracker notFoundMode(FileNotFoundMode notFoundMode, int notFountTimes) {
        this.notFoundMode = notFoundMode;
        this.notFountTimes = notFountTimes;
        if (notFoundMode == FileNotFoundMode.WAIT_TIMES || notFoundMode == FileNotFoundMode.WAIT_COUNT) {
            Valid.gte(notFountTimes, 0, "not fount times has to be greater than or equal to 0");
        }
        return this;
    }

    public DelayTracker minusMode(FileMinusMode minusMode) {
        this.minusMode = minusMode;
        return this;
    }

    public boolean isRun() {
        return run;
    }

    /**
     * 去除乱码
     *
     * @param str str
     * @return str
     */
    private static String cleanMissCode(String str) {
        int testCount = Math.min(str.length(), 2);
        for (int i = testCount; i > 0; i--) {
            if (Spells.isMessyCode(String.valueOf(str.charAt(i - 1)))) {
                str = str.substring(i);
                break;
            }
        }
        return str;
    }

    @Override
    public String toString() {
        return tailFile.toString();
    }

}
