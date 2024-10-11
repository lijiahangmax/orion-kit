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
package com.orion.lang.define.thread;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池拒绝策略枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/19 13:04
 */
public enum RejectPolicy {

    /**
     * 任务遭到拒绝将抛出异常
     */
    ABORT(new ThreadPoolExecutor.AbortPolicy()),

    /**
     * 放弃当前任务 静默抛出
     */
    DISCARD(new ThreadPoolExecutor.DiscardPolicy()),

    /**
     * 删除工作队列头部任务进行重试, 如果失败则重复操作
     */
    DISCARD_OLDEST(new ThreadPoolExecutor.DiscardOldestPolicy()),

    /**
     * 由调用线程执行任务
     */
    CALLER_RUNS(new ThreadPoolExecutor.CallerRunsPolicy());

    private final RejectedExecutionHandler handler;

    RejectPolicy(RejectedExecutionHandler handler) {
        this.handler = handler;
    }

    public RejectedExecutionHandler getHandler() {
        return this.handler;
    }

}
