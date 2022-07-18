package com.orion.lang.define.thread;

/**
 * 具有 tag 的 Runnable
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/13 18:00
 */
public class TagRunnable implements Runnable {

    private final Runnable task;

    private final Object tag;

    public TagRunnable(Object tag, Runnable runnable) {
        this.tag = tag;
        this.task = runnable;
    }

    @Override
    public void run() {
        task.run();
    }

    public Object getTag() {
        return tag;
    }

}
