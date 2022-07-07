package com.orion.lang.define.thread;

/**
 * 具有 tag 的 Runnable
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/13 18:00
 */
public class TagRunnable implements Runnable {

    private final Runnable r;

    private final Object tag;

    public TagRunnable(Object tag, Runnable r) {
        this.tag = tag;
        this.r = r;
    }

    @Override
    public void run() {
        r.run();
    }

    public Object getTag() {
        return tag;
    }

}
