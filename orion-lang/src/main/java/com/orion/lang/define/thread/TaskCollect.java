package com.orion.lang.define.thread;

import com.orion.lang.define.wrapper.Tuple;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Valid;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * callable 结果收集器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/22 15:18
 */
public class TaskCollect {

    /**
     * 调度器
     */
    private ExecutorService dispatch;

    /**
     * 结果
     */
    private Future<?>[] futures;

    public TaskCollect(ExecutorService dispatch) {
        Valid.notNull(dispatch, "task dispatch is null");
        this.dispatch = dispatch;
    }

    /**
     * 执行任务
     *
     * @param tasks task
     * @return this
     */
    public TaskCollect tasks(Callable<?>... tasks) {
        this.futures = new Future<?>[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            Callable<?> task = tasks[i];
            try {
                futures[i] = dispatch.submit(task);
            } catch (Exception e) {
                throw Exceptions.task("an exception occurred while the task was running", e);
            }
        }
        return this;
    }

    /**
     * 收集结果
     *
     * @return result
     */
    public Tuple collect() {
        Object[] result = new Object[futures.length];
        for (int i = 0; i < futures.length; i++) {
            Future<?> future = futures[i];
            try {
                result[i] = future.get();
            } catch (InterruptedException e) {
                throw Exceptions.task("collect result timeout", e);
            } catch (Exception e) {
                throw Exceptions.task("collect result error", e);
            }
        }
        return Tuple.of(result);
    }

}
