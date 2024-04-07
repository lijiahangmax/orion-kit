package com.orion.lang.support.timeout;

import java.io.Closeable;
import java.util.List;

/**
 * 超时检测器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/16 10:09
 */
public interface TimeoutChecker<T extends TimeoutEndpoint> extends Runnable, Closeable {

    /**
     * 添加任务
     *
     * @param task task
     */
    void addTask(T task);

    /**
     * 获取全部任务
     *
     * @return tasks
     */
    List<T> getTasks();

    /**
     * 清空全部任务
     */
    void clear();

    /**
     * 任务是否为空
     *
     * @return isEmpty
     */
    boolean isEmpty();

    /**
     * 是否运行中
     *
     * @return isRun
     */
    boolean isRun();

}
