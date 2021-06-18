package com.orion.tail;

import com.orion.able.SafeCloseable;
import com.orion.able.Stoppable;

/**
 * 文件追踪器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/15 13:53
 */
public abstract class Tracker implements Runnable, Stoppable, SafeCloseable {

    /**
     * 运行flag
     */
    protected volatile boolean run;

    public Tracker() {
    }

    /**
     * 开启tail
     */
    public abstract void tail();

    @Override
    public void run() {
        this.tail();
    }

    @Override
    public void stop() {
        this.run = false;
    }

    public boolean isRun() {
        return run;
    }

}
