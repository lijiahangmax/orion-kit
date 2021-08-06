package com.orion.lang.thread;

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
