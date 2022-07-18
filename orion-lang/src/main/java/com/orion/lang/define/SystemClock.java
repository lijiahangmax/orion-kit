package com.orion.lang.define;

import java.sql.Timestamp;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 系统时钟
 * <p>
 * 高并发场景下 System.currentTimeMillis() 的性能问题的优化
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/07/23 17:25
 */
public class SystemClock {

    private final long period;

    private final AtomicLong now;

    private SystemClock(long period) {
        this.period = period;
        this.now = new AtomicLong(System.currentTimeMillis());
        this.scheduleClockUpdating();
    }

    /**
     * 获取当前毫秒
     *
     * @return 当前毫秒数
     */
    public static long now() {
        return InstanceHolder.INSTANCE.currentTimeMillis();
    }

    /**
     * 获取当前毫秒数
     *
     * @return 当前毫秒数
     */
    public static String nowDate() {
        return new Timestamp(InstanceHolder.INSTANCE.currentTimeMillis()).toString();
    }

    @SuppressWarnings("ALL")
    private void scheduleClockUpdating() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "System Clock");
            thread.setDaemon(true);
            return thread;
        });
        scheduler.scheduleAtFixedRate(() -> now.set(System.currentTimeMillis()), period, period, TimeUnit.MILLISECONDS);
    }

    private long currentTimeMillis() {
        return now.get();
    }

    /**
     * 单例
     */
    private static class InstanceHolder {
        private static final SystemClock INSTANCE = new SystemClock(1);
    }

}
