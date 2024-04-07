package com.orion.lang.support.timeout;

/**
 * 超时检测器 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/3 11:07
 */
public class TimeoutCheckers {

    /**
     * 默认延迟时间 500 ms
     */
    public static final long DEFAULT_DELAY = 500;

    private TimeoutCheckers() {
    }

    /**
     * 创建超时检测器
     *
     * @param <T> T
     * @return checker
     */
    public static <T extends TimeoutEndpoint> TimeoutChecker<T> create() {
        return new TimeoutCheckerImpl<T>(DEFAULT_DELAY);
    }

    /**
     * 创建超时检测器
     *
     * @param delay delay
     * @param <T>   T
     * @return checker
     */
    public static <T extends TimeoutEndpoint> TimeoutChecker<T> create(long delay) {
        return new TimeoutCheckerImpl<T>(delay);
    }

}
