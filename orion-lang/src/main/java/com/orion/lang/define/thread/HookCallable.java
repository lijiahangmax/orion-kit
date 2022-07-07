package com.orion.lang.define.thread;

import java.util.concurrent.Callable;

/**
 * 具有钩子的Callable
 *
 * @author l1jh15
 * @version 1.0.0
 * @since 2020/10/17 13:47
 */
public class HookCallable<V> implements Callable<V> {

    /**
     * task
     */
    private final HookCallable<V> task;

    /**
     * 钩子任务
     */
    private final Runnable hook;

    /**
     * task 异常是否执行钩子
     */
    private boolean taskErrorRunHook = true;

    public HookCallable(HookCallable<V> task, Runnable hook) {
        this.task = task;
        this.hook = hook;
    }

    public HookCallable(HookCallable<V> task, Runnable hook, boolean taskErrorRunHook) {
        this.task = task;
        this.hook = hook;
        this.taskErrorRunHook = taskErrorRunHook;
    }

    @Override
    public V call() throws Exception {
        Exception e = null;
        V v = null;
        try {
            v = task.call();
        } catch (Exception ex) {
            e = ex;
        }
        if (e == null || taskErrorRunHook) {
            hook.run();
        }
        if (e != null) {
            throw e;
        } else {
            return v;
        }
    }

}
