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
import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.Systems;

import java.util.concurrent.*;

/**
 * 线程池构造器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/17 13:58
 */
public class ExecutorBuilder implements Buildable<ThreadPoolExecutor> {

    /**
     * 默认的工作队列容量
     */
    public static final int DEFAULT_QUEUE_CAPACITY = 1024;

    /**
     * 核心池大小
     */
    private int corePoolSize;

    /**
     * 最大池大小 (允许同时执行的最大线程数) 默认: 核心数 * 2
     */
    private int maxPoolSize;

    /**
     * 线程存活时间
     * 当池中线程多于核心大小时 多出的线程保留的时长
     */
    private long keepAliveTime;

    /**
     * 工作队列 用于存在未执行的线程
     */
    private BlockingQueue<Runnable> workQueue;

    /**
     * 线程工厂 用于自定义线程创建
     */
    private ThreadFactory threadFactory;

    /**
     * 当线程阻塞 (block) 时的异常处理器
     */
    private RejectedExecutionHandler handler;

    /**
     * 线程执行超时后是否回收线程
     */
    private boolean allowCoreThreadTimeout;

    /**
     * 预开启所有的核心线程
     */
    private boolean preStartAllCoreThreads;

    public ExecutorBuilder() {
        this.keepAliveTime = Const.MS_S_60;
        this.maxPoolSize = Systems.PROCESS_NUM * 2;
    }

    /**
     * 创建 ExecutorBuilder
     *
     * @return this
     */
    public static ExecutorBuilder create() {
        return new ExecutorBuilder();
    }

    /**
     * 设置核心池大小 默认0
     *
     * @param corePoolSize 核心池大小
     * @return this
     */
    public ExecutorBuilder corePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
        return this;
    }

    /**
     * 设置最大池大小 (允许同时执行的最大线程数)
     *
     * @param maxPoolSize 最大池大小 (允许同时执行的最大线程数)
     * @return this
     */
    public ExecutorBuilder maxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
        return this;
    }

    /**
     * 设置线程存活时间
     * <p>
     * 即当池中线程多于核心大小时 多出的线程保留的时长
     *
     * @param keepAliveTime 线程存活时间
     * @param unit          单位
     * @return this
     */
    public ExecutorBuilder keepAliveTime(long keepAliveTime, TimeUnit unit) {
        return this.keepAliveTime(unit.toNanos(keepAliveTime));
    }

    /**
     * 设置线程存活时间 (ms)
     * <p>
     * 当池中线程多于核心大小时 多出的线程保留的时长
     *
     * @param keepAliveTime 线程存活时间
     * @return this
     */
    public ExecutorBuilder keepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
        return this;
    }

    /**
     * 设置工作队列
     * <p>
     * SynchronousQueue    它将任务直接提交给线程而不保持它们, 当运行线程小于 maxPoolSize 时会创建新线程, 否则触发异常策略
     * <p>
     * LinkedBlockingQueue 默认无界队列, 当运行线程大于 corePoolSize 时始终放入此队列, 此时 maximumPoolSize 无效
     * 当构造LinkedBlockingQueue对象时传入参数, 变为有界队列, 队列满时, 运行线程小于 maxPoolSize 时会创建新线程, 否则触发异常策略
     * <p>
     * ArrayBlockingQueue  有界队列, 相对无界队列有利于控制队列大小, 队列满时, 运行线程小于maxPoolSize时会创建新线程, 否则触发异常策略
     *
     * @param workQueue 队列
     * @return this
     */
    public ExecutorBuilder workQueue(BlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
        return this;
    }

    /**
     * 使用 LinkedBlockingQueue 做为工作队列
     *
     * @return this
     */
    public ExecutorBuilder useLinkedBlockingQueue() {
        return this.workQueue(new LinkedBlockingQueue<>());
    }

    /**
     * 使用 LinkedBlockingQueue 做为工作队列
     *
     * @param capacity 队列容量
     * @return this
     */
    public ExecutorBuilder useLinkedBlockingQueue(int capacity) {
        return this.workQueue(new LinkedBlockingQueue<>(capacity));
    }

    /**
     * 使用 ArrayBlockingQueue 做为工作队列
     *
     * @param capacity 队列容量
     * @return this
     */
    public ExecutorBuilder useArrayBlockingQueue(int capacity) {
        return this.workQueue(new ArrayBlockingQueue<>(capacity));
    }

    /**
     * 使用 SynchronousQueue 做为工作队列 (非公平策略)
     *
     * @return this
     */
    public ExecutorBuilder useSynchronousQueue() {
        return this.workQueue(new SynchronousQueue<>(false));
    }

    /**
     * 使用 SynchronousQueue 做为工作队列
     *
     * @param fair 是否使用公平访问策略
     * @return this
     */
    public ExecutorBuilder useSynchronousQueue(boolean fair) {
        return this.workQueue(new SynchronousQueue<>(fair));
    }

    /**
     * 设置线程工厂
     *
     * @param threadFactory ThreadFactory
     * @return this
     */
    public ExecutorBuilder threadFactory(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
        return this;
    }

    /**
     * 设置线程工厂
     *
     * @param threadPrefix 线程名称前缀
     * @return this
     */
    public ExecutorBuilder namedThreadFactory(String threadPrefix) {
        this.threadFactory = new NamedThreadFactory(threadPrefix);
        return this;
    }

    /**
     * 设置当线程阻塞 (block) 异常处理器
     * - 线程池和工作队列已满
     *
     * @param rejectHandler RejectedExecutionHandler
     * @return this
     */
    public ExecutorBuilder rejectHandler(RejectedExecutionHandler rejectHandler) {
        this.handler = rejectHandler;
        return this;
    }

    /**
     * 设置线程执行超时后回收线程
     *
     * @return this
     */
    public ExecutorBuilder allowCoreThreadTimeout() {
        this.allowCoreThreadTimeout = true;
        return this;
    }

    /**
     * 设置线程执行超时后是否回收线程
     *
     * @param allowCoreThreadTimeout 线程执行超时后是否回收线程
     * @return this
     */
    public ExecutorBuilder allowCoreThreadTimeout(boolean allowCoreThreadTimeout) {
        this.allowCoreThreadTimeout = allowCoreThreadTimeout;
        return this;
    }

    /**
     * 预开启所有的核心线程
     *
     * @return this
     */
    public ExecutorBuilder preStartAllCoreThreads() {
        this.preStartAllCoreThreads = true;
        return this;
    }

    /**
     * 设置是否预开启所有的核心线程
     *
     * @param preStartAllCoreThreads 是否预开启所有的核心线程
     * @return this
     */
    public ExecutorBuilder preStartAllCoreThreads(boolean preStartAllCoreThreads) {
        this.preStartAllCoreThreads = preStartAllCoreThreads;
        return this;
    }

    /**
     * 构建 ThreadPoolExecutor
     */
    @Override
    public ThreadPoolExecutor build() {
        return build(this);
    }

    /**
     * 构建 ThreadPoolExecutor
     *
     * @param builder ExecutorBuilder
     * @return ThreadPoolExecutor
     */
    private static ThreadPoolExecutor build(ExecutorBuilder builder) {
        int corePoolSize = builder.corePoolSize;
        int maxPoolSize = builder.maxPoolSize;
        long keepAliveTime = builder.keepAliveTime;
        BlockingQueue<Runnable> workQueue;
        if (builder.workQueue != null) {
            workQueue = builder.workQueue;
        } else {
            // corePoolSize 为 0 则要使用 SynchronousQueue 避免无限阻塞
            workQueue = (corePoolSize <= 0) ? new SynchronousQueue<>() : new LinkedBlockingQueue<>(DEFAULT_QUEUE_CAPACITY);
        }
        ThreadFactory threadFactory = (builder.threadFactory != null) ? builder.threadFactory : Executors.defaultThreadFactory();
        RejectedExecutionHandler handler = Objects1.def(builder.handler, ThreadPoolExecutor.AbortPolicy::new);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                keepAliveTime, TimeUnit.MILLISECONDS,
                workQueue, threadFactory, handler);
        threadPoolExecutor.allowCoreThreadTimeOut(builder.allowCoreThreadTimeout);
        if (builder.preStartAllCoreThreads) {
            threadPoolExecutor.prestartAllCoreThreads();
        }
        return threadPoolExecutor;
    }

}
