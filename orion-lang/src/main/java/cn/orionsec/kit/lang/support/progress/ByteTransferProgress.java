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
package cn.orionsec.kit.lang.support.progress;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 数据传输进度条
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/12 21:51
 */
public class ByteTransferProgress implements Progress {

    /**
     * 开始 offset
     */
    protected long start;

    /**
     * 当前 current
     */
    protected volatile AtomicLong current;

    /**
     * 总长 end
     */
    protected long end;

    /**
     * 开始时间
     */
    protected long startTime;

    /**
     * 结束时间
     */
    protected long endTime;

    /**
     * 是否失败
     */
    protected volatile boolean error;

    /**
     * 是否完成
     */
    protected volatile boolean done;

    /**
     * 传输完成回调
     */
    protected Runnable callback;

    public ByteTransferProgress(long end) {
        this(0, end);
    }

    public ByteTransferProgress(long start, long end) {
        this.current = new AtomicLong();
        this.start = start;
        this.end = end;
    }

    /**
     * 完成回调
     *
     * @param callback 回调器
     * @return this
     */
    public ByteTransferProgress callback(Runnable callback) {
        this.callback = callback;
        return this;
    }

    /**
     * 设置开始
     *
     * @param start start
     */
    public void setStart(long start) {
        this.start = start;
    }

    /**
     * 设置结束
     *
     * @param end 结束
     */
    public void setEnd(long end) {
        this.end = end;
    }

    /**
     * 设置当前值
     *
     * @param current current
     */
    public void setCurrent(long current) {
        this.current = new AtomicLong(current);
    }

    @Override
    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void reset() {
        this.start = 0;
        this.startTime = 0L;
    }

    /**
     * 增加进度
     *
     * @param read byte
     */
    public void accept(long read) {
        current.addAndGet(read);
    }

    /**
     * 设置开始时间
     *
     * @param startTime 开始时间
     */
    public void startTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * 设置结束时间
     *
     * @param endTime 结束时间
     */
    public void endTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    public void finish() {
        this.finish(false);
    }

    @Override
    public void finish(boolean error) {
        if (done) {
            return;
        }
        this.endTime = System.currentTimeMillis();
        this.done = true;
        this.error = error;
        if (callback != null) {
            callback.run();
        }
    }

    @Override
    public double getProgress() {
        if (done && !error) {
            return 1;
        }
        if (end == 0) {
            return 0;
        }
        return (double) current.get() / (double) end;
    }

    /**
     * 使用的时间
     *
     * @return ms
     */
    public long usedTime() {
        return endTime - startTime;
    }

    public long getStart() {
        return start;
    }

    public long getCurrent() {
        return current.get();
    }

    public long getEnd() {
        return end;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public boolean isDone() {
        return done;
    }

    public boolean isError() {
        return error;
    }

}
