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
package cn.orionsec.kit.lang.support.progress;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.Threads;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * 数据传输进度条 (实时速率计算)
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/11/6 23:07
 */
public class ByteTransferRateProgress extends ByteTransferProgress {

    /**
     * 是否计算实时速率
     */
    protected boolean computeRate;

    /**
     * 计算间隔 ms
     */
    protected int interval;

    /**
     * 当前速度 byte
     */
    protected volatile long nowRate;

    /**
     * 进度调度器
     */
    private ExecutorService rateExecutor;

    /**
     * 进度处理器
     */
    private Consumer<? super ByteTransferRateProgress> rateAcceptor;

    public ByteTransferRateProgress(long end) {
        this(0, end);
    }

    public ByteTransferRateProgress(long start, long end) {
        super(start, end);
    }

    public ByteTransferRateProgress computeRate() {
        return this.computeRate(Const.MS_S_1);
    }

    /**
     * 开启计算实时速率
     *
     * @param interval 间隔
     * @return this
     */
    public ByteTransferRateProgress computeRate(int interval) {
        this.computeRate = true;
        this.interval = interval;
        return this;
    }

    /**
     * 进度调度器
     *
     * @param rateExecutor 线程池
     * @return this
     */
    public ByteTransferRateProgress rateExecutor(ExecutorService rateExecutor) {
        this.rateExecutor = rateExecutor;
        return this;
    }

    /**
     * 进度回调
     *
     * @param rateAcceptor acceptor
     * @return this
     */
    public ByteTransferRateProgress rateAcceptor(Consumer<? super ByteTransferRateProgress> rateAcceptor) {
        this.rateAcceptor = rateAcceptor;
        return this;
    }

    @Override
    public void start() {
        this.startTime = System.currentTimeMillis();
        if (computeRate) {
            Threads.start(() -> {
                while (!done) {
                    long size = current.get();
                    Threads.sleep(interval);
                    this.nowRate = current.get() - size;
                    if (rateAcceptor != null) {
                        rateAcceptor.accept(this);
                    }
                }
            }, Objects1.def(rateExecutor, Threads.CACHE_EXECUTOR));
        }
    }

    public long getNowRate() {
        return nowRate;
    }

}
