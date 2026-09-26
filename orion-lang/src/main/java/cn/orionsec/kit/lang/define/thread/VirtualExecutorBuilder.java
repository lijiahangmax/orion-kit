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

import cn.orionsec.kit.lang.able.Buildable;
import cn.orionsec.kit.lang.utils.Strings;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 虚拟线程池构造器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/9/26 15:00
 */
public class VirtualExecutorBuilder implements Buildable<ExecutorService> {

    /**
     * 默认线程名称前缀
     */
    public static final String DEFAULT_NAME_PREFIX = "orion-vthread-";

    /**
     * 线程工厂
     */
    private ThreadFactory threadFactory;

    private VirtualExecutorBuilder() {
    }

    /**
     * 创建 VirtualExecutorBuilder
     *
     * @return this
     */
    public static VirtualExecutorBuilder create() {
        return new VirtualExecutorBuilder();
    }

    /**
     * 设置线程工厂
     *
     * @param threadFactory ThreadFactory
     * @return this
     */
    public VirtualExecutorBuilder threadFactory(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
        return this;
    }

    /**
     * 设置线程工厂
     *
     * @param threadPrefix 线程名称前缀
     * @return this
     */
    public VirtualExecutorBuilder namedThreadFactory(String threadPrefix) {
        String prefix = Strings.isBlank(threadPrefix) ? DEFAULT_NAME_PREFIX : threadPrefix;
        this.threadFactory = Thread.ofVirtual().name(prefix, 0).factory();
        return this;
    }

    @Override
    public ExecutorService build() {
        ThreadFactory factory = (threadFactory != null) ? threadFactory : Thread.ofVirtual().name(DEFAULT_NAME_PREFIX, 0).factory();
        return Executors.newThreadPerTaskExecutor(factory);
    }

}
