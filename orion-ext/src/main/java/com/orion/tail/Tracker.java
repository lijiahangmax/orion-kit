package com.orion.tail;

import com.orion.able.Stoppable;
import com.orion.constant.Const;

/**
 * 文件追踪器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/15 13:53
 */
public abstract class Tracker implements Runnable, Stoppable {

    /**
     * 运行flag
     */
    protected volatile boolean run;

    /**
     * 编码集
     */
    protected String charset;

    public Tracker() {
        this.charset = Const.UTF_8;
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

    public Tracker charset(String charset) {
        this.charset = charset;
        return this;
    }

    public boolean isRun() {
        return run;
    }

    public String getCharset() {
        return charset;
    }

}
