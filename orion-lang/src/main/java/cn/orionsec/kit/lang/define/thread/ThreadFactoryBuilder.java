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
package cn.orionsec.kit.lang.define.thread;

import cn.orionsec.kit.lang.able.Buildable;
import cn.orionsec.kit.lang.utils.Valid;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 线程器构建器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/17 14:35
 */
public class ThreadFactoryBuilder implements Buildable<ThreadFactory> {

    /**
     * 前缀
     */
    private String prefix;

    /**
     * 类加载器
     */
    private ClassLoader classLoader;

    /**
     * 是否守护线程
     */
    private boolean daemon;

    /**
     * 线程优先级
     */
    private int priority;

    /**
     * 未捕获异常处理器
     */
    private UncaughtExceptionHandler uncaughtExceptionHandler;

    /**
     * 线程组
     */
    private ThreadGroup group;

    public ThreadFactoryBuilder() {
        this.priority = 5;
    }

    /**
     * 创建 ThreadFactoryBuilder
     *
     * @return ThreadFactoryBuilder
     */
    public static ThreadFactoryBuilder create() {
        return new ThreadFactoryBuilder();
    }

    public ThreadFactoryBuilder setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public ThreadFactoryBuilder setDaemon(boolean daemon) {
        this.daemon = daemon;
        return this;
    }

    public ThreadFactoryBuilder setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }

    public ThreadFactoryBuilder setPriority(int priority) {
        Valid.gte(priority, 1, "priority must greater than or eq 1");
        Valid.lte(priority, 10, "priority must less than or eq 10");
        this.priority = priority;
        return this;
    }

    public ThreadFactoryBuilder setUncaughtExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
        return this;
    }

    public ThreadFactoryBuilder setGroup(String groupName) {
        this.group = new ThreadGroup(groupName);
        return this;
    }

    public ThreadFactoryBuilder setGroup(ThreadGroup group) {
        this.group = group;
        return this;
    }

    /**
     * 构建 ThreadFactory
     *
     * @return ThreadFactory
     */
    @Override
    public ThreadFactory build() {
        return build(this);
    }

    /**
     * 构建
     *
     * @param builder ThreadFactoryBuilder
     * @return ThreadFactory
     */
    private static ThreadFactory build(ThreadFactoryBuilder builder) {
        String namePrefix = builder.prefix;
        UncaughtExceptionHandler handler = builder.uncaughtExceptionHandler;
        AtomicLong count = namePrefix == null ? null : new AtomicLong();
        return r -> {
            Thread thread;
            if (builder.group == null) {
                thread = new Thread(r);
            } else {
                thread = new Thread(builder.group, r);
            }
            if (namePrefix != null) {
                thread.setName(namePrefix + count.getAndIncrement());
            }
            if (builder.classLoader != null) {
                thread.setContextClassLoader(builder.classLoader);
            }
            thread.setDaemon(builder.daemon);
            thread.setPriority(builder.priority);
            if (handler != null) {
                thread.setUncaughtExceptionHandler(handler);
            }
            return thread;
        };
    }

}
