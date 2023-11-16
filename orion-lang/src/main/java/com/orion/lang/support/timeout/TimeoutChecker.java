package com.orion.lang.support.timeout;

import java.io.Closeable;

/**
 * 超时检测器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/16 10:09
 */
public interface TimeoutChecker extends Runnable, Closeable {

    /**
     * 500 ms
     */
    long DEFAULT_DELAY = 500;

    /**
     * 添加任务
     *
     * @param task task
     * @param <T>  T
     */
    <T extends TimeoutEndpoint> void addTask(T task);

    /**
     * 创建检测器
     *
     * @return checker
     */
    static TimeoutChecker create() {
        return new TimeoutCheckerImpl(DEFAULT_DELAY);
    }

    /**
     * 创建检测器
     *
     * @param delay 检测时间
     * @return checker
     */
    static TimeoutChecker create(long delay) {
        return new TimeoutCheckerImpl(delay);
    }

}
