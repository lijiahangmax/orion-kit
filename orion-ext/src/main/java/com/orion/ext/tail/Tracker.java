/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.orion.ext.tail;

import com.orion.lang.able.SafeCloseable;
import com.orion.lang.able.Stoppable;

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
