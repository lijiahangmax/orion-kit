package com.orion.utils;

import com.orion.lang.thread.ConcurrentCallable;
import com.orion.lang.thread.ConcurrentRunnable;
import com.orion.lang.thread.NamedThreadFactory;
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

    private Threads() {
    }

    /**
     * 执行线程
     *
     * @param r ignore
     * @return 线程
     */
    public static Thread start(Runnable r) {
        Thread thread = new Thread(r);
        thread.start();
        return thread;
    }

    /**
     * 执行线程
     *
     * @param r    ignore
     * @param pool 线程池
     */
    public static void start(Runnable r, ExecutorService pool) {
        if (pool == null) {
            start(r);
        } else {
            pool.execute(r);
        }
    }

    /**
     * 执行线程
     *
     * @param rs         线程
     * @param pool       线程池
     * @param concurrent true并发执行
     */
    public static void start(List<Runnable> rs, ExecutorService pool, boolean concurrent) {
        Valid.notNull(pool, "ThreadPool is null");
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
        FutureTask<V> futureTask = new FutureTask<>(c);
        futureTask.run();
        return futureTask;
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
            return call(c);
        }
        return pool.submit(c);
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
        Valid.notNull(pool, "ThreadPool is null");
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
            // ignore
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
            // ignore
        }
    }

    /**
     * 并发多次单个线程
     *
     * @param count 并发次数
     * @param pool  线程池
     * @param r     runnable
     */
    public static void concurrent(int count, ExecutorService pool, Runnable r) {
        Valid.notNull(pool, "ThreadPool is null");
        CyclicBarrier cb = new CyclicBarrier(count);
        ConcurrentRunnable cr = new ConcurrentRunnable(r, cb);
        for (int i = 0; i < count; i++) {
            pool.execute(cr);
        }
    }

    /**
     * 并发多次单个线程
     *
     * @param count 并发次数
     * @param pool  线程池
     * @param c     callable
     * @return Future
     */
    public static <V> List<Future<V>> concurrent(int count, ExecutorService pool, Callable<V> c) {
        Valid.notNull(pool, "ThreadPool is null");
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
     *
     * @param pool 线程池
     * @param rs   线程
     */
    public static void concurrentRunnable(List<Runnable> rs, ExecutorService pool) {
        Valid.notNull(pool, "ThreadPool is null");
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
     *
     * @param cs   线程
     * @param pool 线程池
     * @return Future
     */
    public static <V> List<Future<V>> concurrentCallable(List<Callable<V>> cs, ExecutorService pool) {
        Valid.notNull(pool, "ThreadPool is null");
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
                    System.err.println("Pool did not terminated");
                }
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 关闭线程池
     *
     * @param pool    线程池
     * @param timeout 取消队列中的Pending的任务, 中断阻塞的时间毫秒
     */
    public static void shutdownPool(ExecutorService pool, int timeout) {
        try {
            pool.shutdownNow();
            if (!pool.awaitTermination(timeout, TimeUnit.MILLISECONDS)) {
                System.err.println("Pool did not terminated");
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 关闭线程池
     *
     * @param pool     线程池
     * @param timeout  取消队列中的Pending的任务, 中断阻塞的时间
     * @param timeUnit 时间单位
     */
    public static void shutdownPool(ExecutorService pool, int timeout, TimeUnit timeUnit) {
        try {
            pool.shutdownNow();
            if (!pool.awaitTermination(timeout, timeUnit)) {
                System.err.println("Pool did not terminated");
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
