package com.orion.support.progress;

import com.orion.constant.Const;
import com.orion.utils.Threads;

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
    protected long current;

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
     * 是否计算实时速率
     */
    protected boolean computeRate;

    /**
     * 当前速度 byte
     */
    protected long nowRate;

    /**
     * 计算间隔
     */
    protected int interval;

    /**
     * 是否失败
     */
    protected volatile boolean error;

    /**
     * 是否完成
     */
    protected volatile boolean done;

    public ByteTransferProgress(long end) {
        this(0, end);
    }

    public ByteTransferProgress(long start, long end) {
        this.current = 0;
        this.start = start;
        this.end = end;
    }

    public void computeRate() {
        this.computeRate(Const.MS_S_1);
    }

    /**
     * 开启计算实时速率
     *
     * @param interval 间隔
     */
    public void computeRate(int interval) {
        this.computeRate = true;
        this.interval = interval;
    }

    /**
     * 开始
     *
     * @param start 开始大小
     */
    public void start(long start) {
        this.start = start;
        this.start();
    }

    /**
     * 开始
     */
    public void start() {
        this.startTime = System.currentTimeMillis();
        if (computeRate) {
            Threads.CACHE_EXECUTOR.execute(() -> {
                while (!done) {
                    long size = current;
                    Threads.sleep(interval);
                    nowRate = current - size;
                }
            });
        }
    }

    /**
     * 增加进度
     *
     * @param read byte
     */
    public void accept(long read) {
        this.current += read;
    }

    /**
     * 设置当前值
     *
     * @param current current
     */
    public void current(long current) {
        this.current = current;
    }

    /**
     * 设置结束
     *
     * @param end 结束
     */
    public void end(long end) {
        this.end = end;
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

    /**
     * 结束
     */
    public void finish() {
        this.finish(false);
    }

    /**
     * 结束
     */
    public void finish(boolean error) {
        if (done) {
            return;
        }
        this.endTime = System.currentTimeMillis();
        this.done = true;
        this.error = error;
    }

    @Override
    public double getProgress() {
        if (done && !error) {
            return 1;
        }
        if (end == 0) {
            return 0;
        }
        return (double) current / (double) end;
    }

    /**
     * 使用的时间
     *
     * @return ms
     */
    public long usedTime() {
        return endTime - startTime;
    }

    public long getNowRate() {
        return nowRate;
    }

    public long getStart() {
        return start;
    }

    public long getCurrent() {
        return current;
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
