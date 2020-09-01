package com.orion.lang.thread;

import com.orion.utils.Strings;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义命名的线程工厂
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/6/2 12:47
 */
public class NamedThreadFactory implements ThreadFactory {

    /**
     * 计数器
     */
    private AtomicInteger counter = new AtomicInteger();

    /**
     * 前缀
     */
    private String prefix;

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
    private Integer priority;

    /**
     * 异常处理器
     */
    private Thread.UncaughtExceptionHandler handler;

    public NamedThreadFactory(String prefix) {
        this.prefix = Strings.def(prefix);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(prefix + counter.getAndIncrement());
        if (classLoader != null) {
            thread.setContextClassLoader(classLoader);
        }
        thread.setDaemon(daemon);
        if (priority != null) {
            thread.setPriority(priority);
        }
        if (handler != null) {
            thread.setUncaughtExceptionHandler(handler);
        }
        return thread;
    }

    public NamedThreadFactory setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }

    public NamedThreadFactory setDaemon(boolean daemon) {
        this.daemon = daemon;
        return this;
    }

    public NamedThreadFactory setPriority(Integer priority) {
        this.priority = priority;
        return this;
    }

    public NamedThreadFactory setHandler(Thread.UncaughtExceptionHandler handler) {
        this.handler = handler;
        return this;
    }

}
