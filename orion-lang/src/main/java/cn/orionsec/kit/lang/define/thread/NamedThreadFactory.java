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

import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.Strings;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义命名的线程工厂
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/6/2 12:47
 */
public class NamedThreadFactory implements ThreadFactory {

    /**
     * 计数器
     */
    private final AtomicInteger counter;

    /**
     * 前缀
     */
    private final String prefix;

    /**
     * 类加载器
     */
    private ClassLoader classLoader;

    /**
     * 守护线程
     */
    private boolean daemon;

    /**
     * 优先级
     */
    private int priority;

    /**
     * 线程组
     */
    private ThreadGroup group;

    /**
     * 异常处理器
     */
    private UncaughtExceptionHandler handler;

    public NamedThreadFactory(String prefix) {
        this.prefix = Strings.def(prefix);
        this.counter = new AtomicInteger();
        this.priority = 5;
    }

    public NamedThreadFactory setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }

    public NamedThreadFactory setDaemon(boolean daemon) {
        this.daemon = daemon;
        return this;
    }

    public NamedThreadFactory setPriority(int priority) {
        Assert.gte(priority, 1, "priority must greater than or eq 1");
        Assert.lte(priority, 10, "priority must less than or eq 10");
        this.priority = priority;
        return this;
    }

    public NamedThreadFactory setHandler(UncaughtExceptionHandler handler) {
        this.handler = handler;
        return this;
    }

    public NamedThreadFactory setGroup(String groupName) {
        this.group = new ThreadGroup(groupName);
        return this;
    }

    public NamedThreadFactory setGroup(ThreadGroup group) {
        this.group = group;
        return this;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread;
        if (group == null) {
            thread = new Thread(r);
        } else {
            thread = new Thread(group, r);
        }
        thread.setName(prefix + counter.getAndIncrement());
        if (classLoader != null) {
            thread.setContextClassLoader(classLoader);
        }
        thread.setDaemon(daemon);
        thread.setPriority(priority);
        if (handler != null) {
            thread.setUncaughtExceptionHandler(handler);
        }
        return thread;
    }

}
