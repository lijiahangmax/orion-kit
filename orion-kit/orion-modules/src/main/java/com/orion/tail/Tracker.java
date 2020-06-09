package com.orion.tail;

import com.orion.utils.Exceptions;
import com.orion.utils.Streams;
import com.orion.utils.Threads;
import com.orion.utils.io.Files1;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 文件追踪器
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/14 16:13
 */
public class Tracker implements Runnable {

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
    private long delayMillis = 1000;

    /**
     * 编码集
     */
    private String charset = "UTF-8";

    /**
     * 已读取的行数
     */
    private AtomicLong readLines = new AtomicLong();

    /**
     * 开始监听的起始位置
     * > 0 fileSize - tailStartPos 可能会导致第一个字符为乱码
     * 0 全部读取
     */
    private long tailStartPos;

    /**
     * 文件大小减少操作 并且当前位置大于等于文件大小
     * 0 从头开始读取
     * 1 从fileSize - tailStartPos读取
     * 2 从文件当前位置读取
     * 3 关闭
     */
    private int fileMinusMode;

    /**
     * 0 关闭
     * 1 继续监听
     * 2 等待到指定次数如果还不存在则关闭
     */
    private int fileNotFoundMode;

    /**
     * 文件找不到等待次数
     */
    private int fileNotFoundWaitTimes = 10;

    /**
     * 运行flag
     */
    private volatile boolean run = true;

    /**
     * 行处理器
     */
    private LineHandler handler;

    public Tracker(File tailFile, LineHandler handler) {
        this.tailFile = tailFile;
        this.handler = handler;
    }

    public Tracker(File tailFile, String charset, LineHandler handler) {
        this.tailFile = tailFile;
        this.charset = charset;
        this.handler = handler;
    }

    public Tracker(File tailFile, long delayMillis, LineHandler handler) {
        this.tailFile = tailFile;
        this.delayMillis = delayMillis;
        this.handler = handler;
    }

    public Tracker(File tailFile, long delayMillis, String charset, LineHandler handler) {
        this.tailFile = tailFile;
        this.delayMillis = delayMillis;
        this.charset = charset;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            boolean init = init();
            if (init) {
                setSeek();
            } else {
                return;
            }
            // 上次检查文件更改的时间
            long lastMod = 0;
            // 上次文件大小
            long lastLen = tailFile.length();
            while (run) {
                if (tailFile.lastModified() > lastMod) {
                    long length = tailFile.length();
                    if (lastLen > length && reader.getFilePointer() >= length) {
                        // 文件减少 重新设置
                        resetSeek();
                    }
                    String s = Streams.readLines(reader, charset);
                    if (s != null) {
                        if (handler != null) {
                            s = s.replaceAll("\r\n", "\n").replaceAll("\r", "\n");
                            String[] ls = s.split("\n");
                            for (String l : ls) {
                                handler.readLine(l, readLines.getAndIncrement(), this);
                            }
                        }
                    }
                }
                lastMod = tailFile.lastModified();
                lastLen = tailFile.length();
                Threads.sleep(delayMillis);
            }
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            Streams.closeQuietly(reader);
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
            if (fileNotFoundMode == 1) {
                while (run) {
                    Threads.sleep(delayMillis);
                    if (tailFile.exists() && tailFile.isFile()) {
                        reader = Files1.openRandomAccess(tailFile, "r");
                        return true;
                    }
                }
            } else if (fileNotFoundMode == 2) {
                for (int i = 0; i < fileNotFoundWaitTimes; i++) {
                    Threads.sleep(delayMillis);
                    if (run && tailFile.exists() && tailFile.isFile()) {
                        reader = Files1.openRandomAccess(tailFile, "r");
                        return true;
                    }
                }
            }
        } else {
            reader = Files1.openRandomAccess(tailFile, "r");
            return true;
        }
        run = false;
        return false;
    }

    /**
     * 设置初始化位置
     */
    private void setSeek() throws IOException {
        long length = tailFile.length();
        long s = length - tailStartPos;
        if (s > 0) {
            reader.seek(s);
        }
    }

    /**
     * 重新设置初始化位置
     */
    private void resetSeek() throws IOException {
        if (fileMinusMode == 0) {
            reader.seek(0);
        } else if (fileMinusMode == 1) {
            long length = tailFile.length();
            long s = length - tailStartPos;
            if (s > 0) {
                reader.seek(s);
            } else {
                reader.seek(0);
            }
        } else if (fileMinusMode == 2) {
            reader.seek(tailFile.length());
        } else {
            run = false;
        }
    }

    /**
     * 关闭监听
     */
    public void stop() {
        run = false;
    }

    /**
     * 设置读取间隔
     *
     * @param delayMillis 间隔 ms
     * @return this
     */
    public Tracker delayMillis(long delayMillis) {
        this.delayMillis = delayMillis;
        return this;
    }

    /**
     * 设置读取编码格式
     *
     * @param charset 编码格式
     * @return this
     */
    public Tracker charset(String charset) {
        this.charset = charset;
        return this;
    }

    /**
     * 设置读取偏移量
     *
     * @param tailStartPos 偏移量
     * @return this
     */
    public Tracker tailStartPos(long tailStartPos) {
        this.tailStartPos = tailStartPos;
        return this;
    }

    /**
     * 设置文件减少操作
     *
     * @param fileMinusMode 文件减少操作
     * @return this
     */
    public Tracker fileMinusMode(int fileMinusMode) {
        this.fileMinusMode = fileMinusMode;
        return this;
    }

    /**
     * 设置文件不存在操作
     *
     * @param fileNotFoundMode 文件不存在操作
     * @return this
     */
    public Tracker fileNotFoundMode(int fileNotFoundMode) {
        this.fileNotFoundMode = fileNotFoundMode;
        return this;
    }

    /**
     * 设置等待次数
     *
     * @param fileNotFoundWaitTimes 等待次数
     * @return this
     */
    public Tracker fileNotFoundWaitTimes(int fileNotFoundWaitTimes) {
        this.fileNotFoundWaitTimes = fileNotFoundWaitTimes;
        return this;
    }

    /**
     * 设置文件未找到 进行关闭
     *
     * @return this
     */
    public Tracker fileNotFoundClose() {
        fileNotFoundMode = 0;
        return this;
    }

    /**
     * 设置文件未找到 进行等待
     *
     * @return this
     */
    public Tracker fileNotFoundWait() {
        fileNotFoundMode = 1;
        return this;
    }

    /**
     * 设置文件未找到 进行有次数的等待
     *
     * @param times 次数
     * @return this
     */
    public Tracker fileNotFoundWait(int times) {
        fileNotFoundMode = 3;
        fileNotFoundWaitTimes = times;
        return this;
    }

    /**
     * 文件减少后从头读取
     *
     * @return this
     */
    public Tracker fileMinusReadHead() {
        fileMinusMode = 0;
        return this;
    }

    /**
     * 文件减少后从头 fileSize - tailStartPos 开始读取
     *
     * @return this
     */
    public Tracker fileMinusReadPos() {
        fileMinusMode = 1;
        return this;
    }

    /**
     * 文件减少后从当前位置读取
     *
     * @return this
     */
    public Tracker fileMinusReadCurrent() {
        fileMinusMode = 2;
        return this;
    }

    /**
     * 文件减少后关闭
     *
     * @return this
     */
    public Tracker fileMinusReadClose() {
        fileMinusMode = 3;
        return this;
    }

    public File getTailFile() {
        return tailFile;
    }

    public long getDelayMillis() {
        return delayMillis;
    }

    public String getCharset() {
        return charset;
    }

    public long getReadLines() {
        return readLines.get();
    }

    public boolean isRun() {
        return run;
    }

}
