package com.orion.tail;

import com.orion.able.StopAble;

/**
 * 文件追踪器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/15 13:53
 */
public abstract class Tracker implements Runnable, StopAble {

    /**
     * 开启tail
     */
    public abstract void tail();

    @Override
    public void run() {
        tail();
    }

}
