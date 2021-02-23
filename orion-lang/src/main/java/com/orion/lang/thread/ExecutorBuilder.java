package com.orion.lang.thread;

import com.orion.able.Buildable;
import com.orion.constant.Const;
import com.orion.utils.Objects1;
import com.orion.utils.Systems;

import java.util.concurrent.*;

/**
 * 线程池构造器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/17 13:58
 */
public class ExecutorBuilder implements Buildable<ThreadPoolExecutor> {

    /**
     * 默认的等待队列容量
     */
    public static final int DEFAULT_QUEUE_CAPACITY = 1024;

    /**
     * 初始池大小
     */
    private int corePoolSize;

    /**
     * 最大池大小（允许同时执行的最大线程数） 默认: 核心数 * 2
     */
    private int maxPoolSize = Systems.VM_PROCESS_NUM * 2;

    /**
     * 线程存活时间, 即当池中线程多于初始大小时, 多出的线程保留的时长
     */
    private long keepAliveTime = Const.MS_S_60;

    /**
     * 队列, 用于存在未执行的线程
     */
    private BlockingQueue<Runnable> workQueue;

    /**
     * 线程工厂, 用于自定义线程创建
     */
    private ThreadFactory threadFactory;

    /**
     * 当线程阻塞（block）时的异常处理器, 所谓线程阻塞即线程池和等待队列已满, 无法处理线程时采取的策略
     */
    private RejectedExecutionHandler handler;

    /**
     * 线程执行超时后是否回收线程
     */
    private boolean allowCoreThreadTimeOut;

    /**
     * 预开启所有的核心线程
     */
    private boolean preStartAllCoreThreads;

    /**
     * 设置初始池大小, 默认0
     *
     * @param corePoolSize 初始池大小
     * @return this
     */
    public ExecutorBuilder setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
        return this;
    }

    /**
     * 设置最大池大小（允许同时执行的最大线程数）
     *
     * @param maxPoolSize 最大池大小（允许同时执行的最大线程数）
     * @return this
     */
    public ExecutorBuilder setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
        return this;
    }

    /**
     * 设置线程存活时间, 即当池中线程多于初始大小时, 多出的线程保留的时长
     *
     * @param keepAliveTime 线程存活时间
     * @param unit          单位
     * @return this
     */
    public ExecutorBuilder setKeepAliveTime(long keepAliveTime, TimeUnit unit) {
        return setKeepAliveTime(unit.toNanos(keepAliveTime));
    }

    /**
     * 设置线程存活时间, 即当池中线程多于初始大小时, 多出的线程保留的时长, 单位ms
     *
     * @param keepAliveTime 线程存活时间, 单位ms
     * @return this
     */
    public ExecutorBuilder setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
        return this;
    }

    /**
     * 设置队列, 用于存在未执行的线程
     * <p>
     * SynchronousQueue    它将任务直接提交给线程而不保持它们, 当运行线程小于maxPoolSize时会创建新线程, 否则触发异常策略
     * <p>
     * LinkedBlockingQueue 默认无界队列, 当运行线程大于corePoolSize时始终放入此队列, 此时maximumPoolSize无效
     * 当构造LinkedBlockingQueue对象时传入参数, 变为有界队列, 队列满时, 运行线程小于maxPoolSize时会创建新线程, 否则触发异常策略
     * <p>
     * ArrayBlockingQueue  有界队列, 相对无界队列有利于控制队列大小, 队列满时, 运行线程小于maxPoolSize时会创建新线程, 否则触发异常策略
     *
     * @param workQueue 队列
     * @return this
     */
    public ExecutorBuilder setWorkQueue(BlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
        return this;
    }

    /**
     * 使用 ArrayBlockingQueue 做为等待队列
     * 有界队列, 相对无界队列有利于控制队列大小, 队列满时, 运行线程小于maxPoolSize时会创建新线程, 否则触发异常策略
     *
     * @param capacity 队列容量
     * @return this
     */
    public ExecutorBuilder useArrayBlockingQueue(int capacity) {
        return setWorkQueue(new ArrayBlockingQueue<>(capacity));
    }

    /**
     * 使用 SynchronousQueue 做为等待队列 （非公平策略）
     * 它将任务直接提交给线程而不保持它们当运行线程小于maxPoolSize时会创建新线程, 否则触发异常策略
     *
     * @return this
     */
    public ExecutorBuilder useSynchronousQueue() {
        return setWorkQueue(new SynchronousQueue<>(false));
    }

    /**
     * 使用 SynchronousQueue 做为等待队列
     * 它将任务直接提交给线程而不保持它们当运行线程小于maxPoolSize时会创建新线程, 否则触发异常策略
     *
     * @param fair 是否使用公平访问策略
     * @return this
     */
    public ExecutorBuilder useSynchronousQueue(boolean fair) {
        return setWorkQueue(new SynchronousQueue<>(fair));
    }

    /**
     * 设置线程工厂
     *
     * @param threadFactory ThreadFactory
     * @return this
     */
    public ExecutorBuilder setThreadFactory(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
        return this;
    }

    /**
     * 设置线程工厂
     *
     * @param threadPrefix 线程名称前缀
     * @return this
     */
    public ExecutorBuilder setNamedThreadFactory(String threadPrefix) {
        this.threadFactory = new NamedThreadFactory(threadPrefix);
        return this;
    }

    /**
     * 设置当线程阻塞（block）时的异常处理器, 所谓线程阻塞即线程池和等待队列已满, 无法处理线程时采取的策略
     *
     * @param handler RejectedExecutionHandler
     * @return this
     */
    public ExecutorBuilder setHandler(RejectedExecutionHandler handler) {
        this.handler = handler;
        return this;
    }

    /**
     * 设置线程执行超时后是否回收线程
     *
     * @param allowCoreThreadTimeOut 线程执行超时后是否回收线程
     * @return this
     */
    public ExecutorBuilder setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
        return this;
    }

    /**
     * 预开启所有的核心线程
     *
     * @param preStartAllCoreThreads 是否预开启所有的核心线程
     * @return this
     */
    public ExecutorBuilder setPreStartAllCoreThreads(boolean preStartAllCoreThreads) {
        this.preStartAllCoreThreads = preStartAllCoreThreads;
        return this;
    }

    /**
     * 创建ExecutorBuilder
     *
     * @return this
     */
    public static ExecutorBuilder create() {
        return new ExecutorBuilder();
    }

    /**
     * 构建ThreadPoolExecutor
     */
    @Override
    public ThreadPoolExecutor build() {
        return build(this);
    }

    /**
     * 构建ThreadPoolExecutor
     *
     * @param builder ExecutorBuilder
     * @return ThreadPoolExecutor
     */
    private static ThreadPoolExecutor build(ExecutorBuilder builder) {
        int corePoolSize = builder.corePoolSize;
        int maxPoolSize = builder.maxPoolSize;
        long keepAliveTime = builder.keepAliveTime;
        BlockingQueue<Runnable> workQueue;
        if (null != builder.workQueue) {
            workQueue = builder.workQueue;
        } else {
            // corePoolSize为0则要使用SynchronousQueue避免无限阻塞
            workQueue = (corePoolSize <= 0) ? new SynchronousQueue<>() : new LinkedBlockingQueue<>(DEFAULT_QUEUE_CAPACITY);
        }
        ThreadFactory threadFactory = (null != builder.threadFactory) ? builder.threadFactory : Executors.defaultThreadFactory();
        RejectedExecutionHandler handler = Objects1.def(builder.handler, ThreadPoolExecutor.AbortPolicy::new);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                keepAliveTime, TimeUnit.MILLISECONDS,
                workQueue, threadFactory, handler);
        threadPoolExecutor.allowCoreThreadTimeOut(builder.allowCoreThreadTimeOut);
        if (builder.preStartAllCoreThreads) {
            threadPoolExecutor.prestartAllCoreThreads();
        }
        return threadPoolExecutor;
    }

}
