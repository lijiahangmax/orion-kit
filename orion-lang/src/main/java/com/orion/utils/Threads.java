package com.orion.utils;

import com.orion.constant.Const;
import com.orion.lang.Console;
import com.orion.lang.thread.*;
import com.orion.lang.wrapper.Tuple;
import com.orion.utils.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 线程工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/2/24 18:13
 */
public class Threads {

    /**
     * Threads 线程池
     */
    private static final ExecutorService GLOBAL_EXECUTOR = ExecutorBuilder.create()
            .setNamedThreadFactory("orion-global-thread-")
            .setCorePoolSize(2)
            .setMaxPoolSize(32)
            .setKeepAliveTime(Const.MS_S_60)
            .setWorkQueue(new LinkedBlockingQueue<>())
            .setAllowCoreThreadTimeOut(true)
            .build();

    /**
     * 全局线程池
     */
    public static final ExecutorService CACHE_EXECUTOR = ExecutorBuilder.create()
            .setNamedThreadFactory("orion-cache-thread-")
            .setCorePoolSize(2)
            .setMaxPoolSize(Integer.MAX_VALUE)
            .setKeepAliveTime(Const.MS_S_60)
            .setWorkQueue(new SynchronousQueue<>())
            .setAllowCoreThreadTimeOut(true)
            .build();

    private Threads() {
    }

    static {
        Systems.addShutdownHook(() -> {
            shutdownPoolNow(GLOBAL_EXECUTOR, Const.MS_S_3);
            shutdownPoolNow(CACHE_EXECUTOR, Const.MS_S_3);
        });
    }

    /**
     * 执行线程
     *
     * @param r ignore
     */
    public static void start(Runnable r) {
        GLOBAL_EXECUTOR.execute(r);
    }

    /**
     * 执行线程
     *
     * @param r    ignore
     * @param pool 线程池
     */
    public static void start(Runnable r, ExecutorService pool) {
        if (pool == null) {
            pool = GLOBAL_EXECUTOR;
        }
        pool.execute(r);
    }

    /**
     * 执行线程
     *
     * @param rs         线程
     * @param concurrent true并发执行
     */
    public static void start(List<Runnable> rs, boolean concurrent) {
        start(rs, GLOBAL_EXECUTOR, concurrent);
    }

    /**
     * 执行线程
     *
     * @param rs         线程
     * @param pool       线程池
     * @param concurrent true并发执行
     */
    public static void start(List<Runnable> rs, ExecutorService pool, boolean concurrent) {
        if (pool == null) {
            pool = GLOBAL_EXECUTOR;
        }
        if (concurrent) {
            concurrentRunnable(rs, pool);
        } else {
            int size = Lists.size(rs);
            if (size >= 1) {
                Lists.compact(rs);
                int size1 = Lists.size(rs);
                if (size1 >= 1) {
                    for (int i = 0; i < size1; i++) {
                        pool.execute(rs.get(i));
                    }
                }
            }
        }
    }

    /**
     * 执行线程
     *
     * @param c ignore
     * @return Future
     */
    public static <V> Future<V> call(Callable<V> c) {
        return GLOBAL_EXECUTOR.submit(c);
    }

    /**
     * 执行线程
     *
     * @param c    ignore
     * @param pool 线程池
     * @return Future
     */
    public static <V> Future<V> call(Callable<V> c, ExecutorService pool) {
        if (pool == null) {
            pool = GLOBAL_EXECUTOR;
        }
        return pool.submit(c);
    }

    /**
     * 执行线程
     *
     * @param cs         ignore
     * @param concurrent true并发
     * @return Future
     */
    public static <V> List<Future<V>> call(List<Callable<V>> cs, boolean concurrent) {
        return call(cs, GLOBAL_EXECUTOR, concurrent);
    }

    /**
     * 执行线程
     *
     * @param cs         ignore
     * @param pool       线程池
     * @param concurrent true并发
     * @return Future
     */
    public static <V> List<Future<V>> call(List<Callable<V>> cs, ExecutorService pool, boolean concurrent) {
        if (pool == null) {
            pool = GLOBAL_EXECUTOR;
        }
        if (concurrent) {
            return concurrentCallable(cs, pool);
        } else {
            List<Future<V>> list = new ArrayList<>();
            int size = Lists.size(cs);
            if (size >= 1) {
                Lists.compact(cs);
                int size1 = Lists.size(cs);
                if (size1 >= 1) {
                    for (int i = 0; i < size1; i++) {
                        list.add(pool.submit(cs.get(i)));
                    }
                }
            }
            return list;
        }
    }

    /**
     * 线程休眠
     *
     * @param millis 毫秒
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Exceptions.printStacks(e);
        }
    }

    /**
     * 线程休眠
     *
     * @param duration 时间
     * @param unit     时间单位
     */
    public static void sleep(long duration, TimeUnit unit) {
        try {
            unit.sleep(duration);
        } catch (InterruptedException e) {
            Exceptions.printStacks(e);
        }
    }

    /**
     * 多线程执行任务并且收集结果
     *
     * @param tasks 任务
     * @return 元组
     */
    public static Tuple collect(Callable<?>... tasks) {
        return new TaskCollect(GLOBAL_EXECUTOR)
                .tasks(tasks)
                .collect();
    }

    /**
     * 并发多次单个线程
     * <p>
     * 线程池的核心线程数必须大于等于并发次数
     *
     * @param count 并发次数
     * @param pool  线程池
     * @param r     runnable
     */
    public static void concurrent(int count, ExecutorService pool, Runnable r) {
        if (pool == null) {
            pool = GLOBAL_EXECUTOR;
        }
        CyclicBarrier cb = new CyclicBarrier(count);
        ConcurrentRunnable cr = new ConcurrentRunnable(r, cb);
        for (int i = 0; i < count; i++) {
            pool.execute(cr);
        }
    }

    /**
     * 并发多次单个线程
     * <p>
     * 线程池的核心线程数必须大于等于并发次数
     *
     * @param count 并发次数
     * @param pool  线程池
     * @param c     callable
     * @return Future
     */
    public static <V> List<Future<V>> concurrent(int count, ExecutorService pool, Callable<V> c) {
        if (pool == null) {
            pool = GLOBAL_EXECUTOR;
        }
        List<Future<V>> list = new ArrayList<>(count);
        CyclicBarrier cb = new CyclicBarrier(count);
        ConcurrentCallable<V> cc = new ConcurrentCallable<>(c, cb);
        for (int i = 0; i < count; i++) {
            list.add(pool.submit(cc));
        }
        return list;
    }

    /**
     * 并发多个线程
     * <p>
     * 线程池的核心线程数必须大于等于并发线程的长度
     *
     * @param rs   线程
     * @param pool 线程池
     */
    public static void concurrentRunnable(List<Runnable> rs, ExecutorService pool) {
        if (pool == null) {
            pool = GLOBAL_EXECUTOR;
        }
        int size = Lists.size(rs);
        if (size >= 1) {
            Lists.compact(rs);
            int size1 = Lists.size(rs);
            if (size1 >= 1) {
                CyclicBarrier cb = new CyclicBarrier(size1);
                for (int i = 0; i < size1; i++) {
                    ConcurrentRunnable cr = new ConcurrentRunnable(rs.get(i), cb);
                    pool.execute(cr);
                }
            }
        }
    }

    /**
     * 并发多个线程
     * <p>
     * 线程池的核心线程数必须大于等于并发线程的长度
     *
     * @param cs   线程
     * @param pool 线程池
     * @return Future
     */
    public static <V> List<Future<V>> concurrentCallable(List<Callable<V>> cs, ExecutorService pool) {
        List<Future<V>> list = new ArrayList<>();
        int size = Lists.size(cs);
        if (size >= 1) {
            Lists.compact(cs);
            int size1 = Lists.size(cs);
            if (size1 >= 1) {
                CyclicBarrier cb = new CyclicBarrier(size1);
                for (int i = 0; i < size1; i++) {
                    ConcurrentCallable<V> cr = new ConcurrentCallable<>(cs.get(i), cb);
                    list.add(pool.submit(cr));
                }
                pool.shutdown();
            }
        }
        return list;
    }

    /**
     * 关闭线程池
     *
     * @param pool            线程池
     * @param shutdownTimeout 终止时间
     */
    public static void shutdownPool(ExecutorService pool, int shutdownTimeout) {
        shutdownPool(pool, shutdownTimeout, shutdownTimeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 关闭线程池
     *
     * @param pool            线程池
     * @param shutdownTimeout 终止时间
     * @param timeUnit        时间单位
     */
    public static void shutdownPool(ExecutorService pool, int shutdownTimeout, TimeUnit timeUnit) {
        shutdownPool(pool, shutdownTimeout, shutdownTimeout, timeUnit);
    }

    /**
     * 关闭线程池
     *
     * @param pool               线程池
     * @param shutdownTimeout    终止时间
     * @param shutdownNowTimeout 如果调用终止超时, 取消队列中的Pending的任务, 中断阻塞的时间
     */
    public static void shutdownPool(ExecutorService pool, int shutdownTimeout, int shutdownNowTimeout) {
        shutdownPool(pool, shutdownTimeout, shutdownNowTimeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 关闭线程池
     *
     * @param pool               线程池
     * @param shutdownTimeout    终止时间
     * @param shutdownNowTimeout 如果调用终止超时, 取消队列中的Pending的任务, 中断阻塞的时间
     * @param timeUnit           时间单位
     */
    public static void shutdownPool(ExecutorService pool, int shutdownTimeout, int shutdownNowTimeout, TimeUnit timeUnit) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(shutdownTimeout, timeUnit)) {
                pool.shutdownNow();
                if (!pool.awaitTermination(shutdownNowTimeout, timeUnit)) {
                    Console.error("Pool did not terminated");
                }
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 立即关闭线程池
     *
     * @param pool    线程池
     * @param timeout 取消队列中的Pending的任务, 中断阻塞的时间毫秒
     */
    public static void shutdownPoolNow(ExecutorService pool, int timeout) {
        try {
            pool.shutdownNow();
            if (!pool.awaitTermination(timeout, TimeUnit.MILLISECONDS)) {
                Console.error("Pool did not terminated");
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 立即关闭线程池
     *
     * @param pool     线程池
     * @param timeout  取消队列中的Pending的任务, 中断阻塞的时间
     * @param timeUnit 时间单位
     */
    public static void shutdownPoolNow(ExecutorService pool, int timeout, TimeUnit timeUnit) {
        try {
            pool.shutdownNow();
            if (!pool.awaitTermination(timeout, timeUnit)) {
                Console.error("Pool did not terminated");
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 创建线程池
     *
     * @param core      核心线程数量
     * @param max       最大线程数
     * @param keepMilli 空闲线程等待时间
     * @param workQueue 任务队列
     * @param prefix    线程前缀
     * @return 线程池
     */
    public static ExecutorService newThreadPool(int core, int max, int keepMilli, BlockingQueue<Runnable> workQueue, String prefix) {
        return new ThreadPoolExecutor(core, max, keepMilli, TimeUnit.MILLISECONDS, workQueue, new NamedThreadFactory(prefix));
    }

    /**
     * 创建线程池
     *
     * @param core      核心线程数量
     * @param max       最大线程数
     * @param keepMilli 空闲线程等待时间
     * @param workQueue 任务队列
     * @param prefix    线程前缀
     * @param handler   任务队列已满时的拒绝策略
     * @return 线程池
     */
    public static ExecutorService newThreadPool(int core, int max, int keepMilli, BlockingQueue<Runnable> workQueue, String prefix, RejectedExecutionHandler handler) {
        return new ThreadPoolExecutor(core, max, keepMilli, TimeUnit.MILLISECONDS, workQueue, new NamedThreadFactory(prefix), handler);
    }

}
