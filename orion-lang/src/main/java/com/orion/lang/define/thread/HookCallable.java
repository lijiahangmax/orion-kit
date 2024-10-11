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
