/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.lang.define.thread;

import cn.orionsec.kit.lang.define.wrapper.Tuple;
import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.Exceptions;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * callable 结果收集器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/22 15:18
 */
public class TaskCollector {

    /**
     * 调度器
     */
    private final ExecutorService dispatch;

    /**
     * 结果
     */
    private Future<?>[] futures;

    public TaskCollector(ExecutorService dispatch) {
        Assert.notNull(dispatch, "task dispatch is null");
        this.dispatch = dispatch;
    }

    /**
     * 执行任务
     *
     * @param tasks task
     * @return this
     */
    public TaskCollector tasks(Callable<?>... tasks) {
        this.futures = new Future<?>[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            Callable<?> task = tasks[i];
            try {
                futures[i] = dispatch.submit(task);
            } catch (Exception e) {
                throw Exceptions.task("an exception occurred while the task was running", e);
            }
        }
        return this;
    }

    /**
     * 收集结果
     *
     * @return result
     */
    public Tuple collect() {
        Object[] result = new Object[futures.length];
        for (int i = 0; i < futures.length; i++) {
            Future<?> future = futures[i];
            try {
                result[i] = future.get();
            } catch (InterruptedException e) {
                throw Exceptions.task("collect result timeout", e);
            } catch (Exception e) {
                throw Exceptions.task("collect result error", e);
            }
        }
        return Tuple.of(result);
    }

}
