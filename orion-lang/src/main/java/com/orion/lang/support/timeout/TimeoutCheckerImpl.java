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
package com.orion.lang.support.timeout;

import com.orion.lang.utils.Threads;

import java.util.ArrayList;
import java.util.List;

/**
 * 超时检测器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/15 19:24
 */
public class TimeoutCheckerImpl<T extends TimeoutEndpoint> implements TimeoutChecker<T> {

    private final List<T> tasks = new ArrayList<>();

    private final long delay;

    private boolean run;

    public TimeoutCheckerImpl() {
        this(TimeoutCheckers.DEFAULT_DELAY);
    }

    public TimeoutCheckerImpl(long delay) {
        this.delay = delay;
        this.run = true;
    }

    @Override
    public void addTask(T task) {
        tasks.add(task);
    }

    @Override
    public void run() {
        while (run) {
            // 完成或超时 直接移除
            tasks.removeIf(ch -> ch.isDone() || ch.checkTimeout());
            // 等待
            Threads.sleep(delay);
        }
    }

    @Override
    public void clear() {
        tasks.clear();
    }


    @Override
    public boolean isEmpty() {
        return tasks.isEmpty();
    }


    @Override
    public boolean isRun() {
        return run;
    }

    @Override
    public List<T> getTasks() {
        return tasks;
    }

    @Override
    public void close() {
        this.run = false;
    }

}
