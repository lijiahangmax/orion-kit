package com.orion.lang.define.thread;

/**
 * 具有钩子的Runnable
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/17 13:40
 */
public class HookRunnable implements Runnable {

    /**
     * task
     */
    private final Runnable task;

    /**
     * hook
     */
    private final Runnable hook;

    /**
     * task 异常是否执行hook
     */
    private final boolean taskErrorRunHook;

    public HookRunnable(Runnable task, Runnable hook) {
        this(task, hook, true);
    }

    public HookRunnable(Runnable task, Runnable hook, boolean taskErrorRunHook) {
        this.task = task;
        this.hook = hook;
        this.taskErrorRunHook = taskErrorRunHook;
    }

    @Override
    public void run() {
        RuntimeException e = null;
        try {
            task.run();
        } catch (RuntimeException ex) {
            e = ex;
        }
        if (hook != null && (e == null || taskErrorRunHook)) {
            hook.run();
        }
        if (e != null) {
            throw e;
        }
    }

}
