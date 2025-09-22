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
package cn.orionsec.kit.ext.tail.delay;

import cn.orionsec.kit.ext.tail.Tracker;
import cn.orionsec.kit.ext.tail.mode.FileMinusMode;
import cn.orionsec.kit.ext.tail.mode.FileNotFoundMode;
import cn.orionsec.kit.ext.tail.mode.FileOffsetMode;
import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.Threads;
import cn.orionsec.kit.lang.utils.io.FileReaders;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;

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
public abstract class AbstractDelayTracker extends Tracker {

    /**
     * 追踪的文件
     */
    protected File tailFile;

    /**
     * RandomAccessFile
     */
    protected RandomAccessFile reader;

    /**
     * 编码集
     */
    protected String charset;

    /**
     * 延迟时间
     */
    protected int delayMillis;

    /**
     * 启动时文件偏移 处理模式
     */
    protected FileOffsetMode fileOffsetMode;

    /**
     * 开始监听的文件偏移量
     * <p>
     * = -1 从文件当前大小开始
     * = 0 从0开始
     * > 0 BYTE 从length - offset 开始
     * > 0 LINE 倒数第几行开始
     */
    protected long offset;

    /**
     * 启动时未找到文件 处理模式
     */
    protected FileNotFoundMode notFoundMode;

    /**
     * 启动时未找到文件 处理次数
     */
    protected int notFountTimes;

    /**
     * 运行时文件大小减少 处理模式
     */
    protected FileMinusMode minusMode;

    public AbstractDelayTracker(String tailFile) {
        this(new File(tailFile));
    }

    public AbstractDelayTracker(File tailFile) {
        Assert.notNull(tailFile, "tail file is null");
        this.tailFile = tailFile;
        this.charset = Const.UTF_8;
        this.offset = -1L;
        this.delayMillis = Const.MS_S_1;
        this.fileOffsetMode = FileOffsetMode.BYTE;
        this.notFoundMode = FileNotFoundMode.CLOSE;
        this.minusMode = FileMinusMode.CURRENT;
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
                    // 读取
                    this.read();
                }
                lastMod = tailFile.lastModified();
                lastLen = tailFile.length();
                Threads.sleep(delayMillis);
            }
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        } finally {
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
    protected boolean init() throws IOException {
        if (Files1.isFile(tailFile)) {
            // 是文件则直接初始化
            this.reader = Files1.openRandomAccess(tailFile, Const.ACCESS_R);
            return true;
        }
        // 文件未找到则需要根据规则等待
        switch (notFoundMode) {
            case WAIT:
                // 等待
                this.run = true;
                while (run) {
                    Threads.sleep(delayMillis);
                    if (Files1.isFile(tailFile)) {
                        this.reader = Files1.openRandomAccess(tailFile, Const.ACCESS_R);
                        return true;
                    }
                }
                return this.run = false;
            case WAIT_TIMES:
                // 等待时间
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
                    if (run && Files1.isFile(tailFile)) {
                        this.reader = Files1.openRandomAccess(tailFile, Const.ACCESS_R);
                        return true;
                    }
                }
                return false;
            case WAIT_COUNT:
                // 等待次数
                for (int i = 0; i < notFountTimes; i++) {
                    Threads.sleep(delayMillis);
                    if (run && Files1.isFile(tailFile)) {
                        this.reader = Files1.openRandomAccess(tailFile, Const.ACCESS_R);
                        return true;
                    }
                }
                return false;
            case THROWS:
                // 抛出异常
                throw Exceptions.notFound(Strings.format("tail file {} not found", tailFile.getAbsolutePath()));
            case CLOSE:
            default:
                // 关闭
                return false;
        }
    }

    /**
     * 设置初始化位置
     */
    protected void setSeek() throws IOException {
        long length = tailFile.length();
        if (offset == -1) {
            reader.seek(length);
        } else if (offset == 0) {
            reader.seek(0);
        } else if (offset > 0) {
            long pos = 0;
            if (fileOffsetMode == FileOffsetMode.BYTE) {
                pos = length - offset;
            } else if (fileOffsetMode == FileOffsetMode.LINE) {
                pos = FileReaders.readTailLinesSeek(reader, (int) offset);
            }
            if (pos >= 0) {
                reader.seek(pos);
            }
        }
    }

    /**
     * 重新设置初始化位置
     */
    protected void resetSeek() throws IOException {
        switch (minusMode) {
            case CLOSE:
                // 关闭
                this.run = false;
                return;
            case CURRENT:
                // 恢复到当前位置
                reader.seek(tailFile.length());
                return;
            case OFFSET:
                // 恢复到偏移量
                this.setSeek();
                return;
            case RESUME:
                // 恢复到0
                reader.seek(0);
                return;
            case THROWS:
                // 抛异常
                throw Exceptions.state(Strings.format("tail file {} minus", tailFile.getAbsolutePath()));
            default:
        }
    }

    /**
     * 读取
     *
     * @throws IOException IOException
     */
    protected abstract void read() throws IOException;

    /**
     * 设置文件最后更新时间
     */
    public boolean setFileLastModifyTime() {
        return tailFile.setLastModified(System.currentTimeMillis());
    }

    public AbstractDelayTracker charset(String charset) {
        this.charset = charset;
        return this;
    }

    public AbstractDelayTracker offset(long offset) {
        this.offset = offset;
        return this;
    }

    public AbstractDelayTracker offset(FileOffsetMode fileOffsetMode, long offset) {
        this.fileOffsetMode = fileOffsetMode;
        this.offset = offset;
        return this;
    }

    public AbstractDelayTracker delayMillis(int delayMillis) {
        this.delayMillis = delayMillis;
        return this;
    }

    public AbstractDelayTracker notFoundMode(FileNotFoundMode notFoundMode) {
        return notFoundMode(notFoundMode, 0);
    }

    public AbstractDelayTracker notFoundMode(FileNotFoundMode notFoundMode, int notFountTimes) {
        this.notFoundMode = notFoundMode;
        this.notFountTimes = notFountTimes;
        if (notFoundMode == FileNotFoundMode.WAIT_TIMES || notFoundMode == FileNotFoundMode.WAIT_COUNT) {
            Assert.gte(notFountTimes, 0, "not fount times has to be greater than or equal to 0");
        }
        return this;
    }

    public AbstractDelayTracker minusMode(FileMinusMode minusMode) {
        this.minusMode = minusMode;
        return this;
    }

    @Override
    public void close() {
        Streams.close(reader);
    }

    @Override
    public String toString() {
        return tailFile.toString();
    }

}
