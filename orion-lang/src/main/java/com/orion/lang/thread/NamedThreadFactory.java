package com.orion.lang.thread;

import com.orion.utils.Strings;
import com.orion.utils.Valid;

import java.lang.Thread.UncaughtExceptionHandler;
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
    private int priority = 5;

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
        Valid.gte(priority, 1, "priority must greater than or eq 1");
        Valid.lte(priority, 10, "priority must less than or eq 10");
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
