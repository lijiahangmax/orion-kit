package com.orion.lang.thread;

/**
 * 具有tag的runnable
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/13 18:00
 */
public class TagRunnable implements Runnable {

    private Runnable r;
    private Object tag;

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
