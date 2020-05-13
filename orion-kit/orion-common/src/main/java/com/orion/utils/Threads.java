package com.orion.utils;

import com.orion.lang.ConcurrentCallable;
import com.orion.lang.ConcurrentRunnable;
import com.orion.utils.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 线程工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/2/24 18:13
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
     * @param concurrent true并发执行
     */
    public static void start(List<Runnable> rs, boolean concurrent) {
        if (concurrent) {
            concurrentRunnable(rs);
        } else {
            int size = Lists.size(rs);
            if (size == 1) {
                Runnable r = rs.get(0);
                if (r != null) {
                    start(r);
                }
            } else if (size > 1) {
                Lists.compact(rs);
                int size1 = Lists.size(rs);
                if (size1 == 1) {
                    start(rs.get(0));
                } else if (size1 > 1) {
                    ExecutorService ser = Executors.newFixedThreadPool(size1);
                    for (int i = 0; i < size1; i++) {
                        ser.execute(rs.get(i));
                    }
                    ser.shutdown();
                }
            }
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
        if (pool == null) {
            start(rs, concurrent);
            return;
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
        ExecutorService exc = Executors.newFixedThreadPool(1);
        Future<V> future = exc.submit(c);
        exc.shutdown();
        return future;
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
     * @param concurrent true并发
     * @return Future
     */
    public static <V> List<Future<V>> call(List<Callable<V>> cs, boolean concurrent) {
        if (concurrent) {
            return concurrentCallable(cs);
        } else {
            List<Future<V>> list = new ArrayList<>();
            int size = Lists.size(cs);
            if (size >= 1) {
                Lists.compact(cs);
                int size1 = Lists.size(cs);
                ExecutorService pool = Executors.newFixedThreadPool(size1);
                if (size1 >= 1) {
                    for (int i = 0; i < size1; i++) {
                        list.add(pool.submit(cs.get(i)));
                    }
                }
                pool.shutdown();
            }
            return list;
        }
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
            return call(cs, concurrent);
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
            Thread.sleep(unit.toMillis(duration));
        } catch (InterruptedException e) {
            // ignore
        }
    }

    /**
     * 并发多次单个线程
     *
     * @param count 并发次数
     * @param r     runnable
     */
    public static void concurrent(int count, Runnable r) {
        ExecutorService ser = Executors.newFixedThreadPool(count);
        CyclicBarrier cb = new CyclicBarrier(count);
        ConcurrentRunnable cr = new ConcurrentRunnable(r, cb);
        for (int i = 0; i < count; i++) {
            ser.execute(cr);
        }
        ser.shutdown();
    }

    /**
     * 并发多次单个线程
     *
     * @param count 并发次数
     * @param r     runnable
     * @param pool  线程池
     */
    public static void concurrent(int count, Runnable r, ExecutorService pool) {
        if (pool == null) {
            concurrent(count, r);
            return;
        }
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
     * @param c     callable
     * @return Future
     */
    public static <V> List<Future<V>> concurrent(int count, Callable<V> c) {
        List<Future<V>> list = new ArrayList<>(count);
        ExecutorService ser = Executors.newFixedThreadPool(count);
        CyclicBarrier cb = new CyclicBarrier(count);
        ConcurrentCallable<V> cc = new ConcurrentCallable<>(c, cb);
        for (int i = 0; i < count; i++) {
            list.add(ser.submit(cc));
        }
        ser.shutdown();
        return list;
    }

    /**
     * 并发多次单个线程
     *
     * @param count 并发次数
     * @param c     callable
     * @param pool  线程池
     * @return Future
     */
    public static <V> List<Future<V>> concurrent(int count, Callable<V> c, ExecutorService pool) {
        if (pool == null) {
            return concurrent(count, c);
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
     *
     * @param rs 线程
     */
    public static void concurrentRunnable(List<Runnable> rs) {
        int size = Lists.size(rs);
        if (size == 1) {
            Runnable r = rs.get(0);
            if (r != null) {
                start(r);
            }
        } else if (size > 1) {
            Lists.compact(rs);
            int size1 = Lists.size(rs);
            if (size1 == 1) {
                start(rs.get(0));
            } else if (size1 > 1) {
                ExecutorService ser = Executors.newFixedThreadPool(size1);
                CyclicBarrier cb = new CyclicBarrier(size1);
                for (int i = 0; i < size1; i++) {
                    ConcurrentRunnable cr = new ConcurrentRunnable(rs.get(i), cb);
                    ser.execute(cr);
                }
                ser.shutdown();
            }
        }
    }

    /**
     * 并发多个线程
     *
     * @param pool 线程池
     * @param rs   线程
     */
    public static void concurrentRunnable(List<Runnable> rs, ExecutorService pool) {
        if (pool == null) {
            concurrentRunnable(rs);
            return;
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
     *
     * @param cs 线程
     * @return Future
     */
    public static <V> List<Future<V>> concurrentCallable(List<Callable<V>> cs) {
        List<Future<V>> list = new ArrayList<>();
        int size = Lists.size(cs);
        if (size >= 1) {
            Lists.compact(cs);
            int size1 = Lists.size(cs);
            if (size1 >= 1) {
                ExecutorService ser = Executors.newFixedThreadPool(size1);
                CyclicBarrier cb = new CyclicBarrier(size1);
                for (int i = 0; i < size1; i++) {
                    ConcurrentCallable<V> cr = new ConcurrentCallable<>(cs.get(i), cb);
                    list.add(ser.submit(cr));
                }
                ser.shutdown();
            }
        }
        return list;
    }

    /**
     * 并发多个线程
     *
     * @param cs   线程
     * @param pool 线程池
     * @return Future
     */
    public static <V> List<Future<V>> concurrentCallable(List<Callable<V>> cs, ExecutorService pool) {
        if (pool == null) {
            return concurrentCallable(cs);
        }
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
     * 获取当前处理器核心数的线程池
     *
     * @return 线程池
     */
    public static ExecutorService getCoreFixedPool() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
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

}
