package com.orion.lang.thread;

import com.orion.able.BuilderAble;
import com.orion.utils.Valid;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 线程器构建器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/17 14:35
 */
public class ThreadFactoryBuilder implements BuilderAble<ThreadFactory> {

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
    private int priority = 5;

    /**
     * 未捕获异常处理器
     */
    private UncaughtExceptionHandler uncaughtExceptionHandler;

    /**
     * 线程组
     */
    private ThreadGroup group;

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
        AtomicLong count = (null == namePrefix) ? null : new AtomicLong();
        return r -> {
            Thread thread;
            if (builder.group == null) {
                thread = new Thread(r);
            } else {
                thread = new Thread(builder.group, r);
            }
            if (null != namePrefix) {
                thread.setName(namePrefix + count.getAndIncrement());
            }
            if (builder.classLoader != null) {
                thread.setContextClassLoader(builder.classLoader);
            }
            thread.setDaemon(builder.daemon);
            thread.setPriority(builder.priority);
            if (null != handler) {
                thread.setUncaughtExceptionHandler(handler);
            }
            return thread;
        };
    }

}
