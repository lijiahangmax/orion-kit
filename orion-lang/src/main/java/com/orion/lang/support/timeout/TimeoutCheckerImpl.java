package com.orion.lang.support.timeout;

import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Threads;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 超时检测器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/15 19:24
 */
public class TimeoutCheckerImpl implements TimeoutChecker {

    private final List<TimeoutEndpoint> tasks = new ArrayList<>();

    private final long delay;

    private boolean run;

    public TimeoutCheckerImpl() {
        this(DEFAULT_DELAY);
    }

    public TimeoutCheckerImpl(long delay) {
        this.delay = delay;
        this.run = true;
    }

    @Override
    public <T extends TimeoutEndpoint> void addTask(T task) {
        synchronized (tasks) {
            tasks.add(task);
            // 唤醒等待
            tasks.notify();
        }
    }

    @Override
    public void run() {
        synchronized (tasks) {
            while (run) {
                // 完成或超时 直接移除
                tasks.removeIf(ch -> ch.isDone() || ch.checkTimeout());
                // 等待
                try {
                    if (tasks.isEmpty()) {
                        // 为空则等待 直到有新的数据
                        tasks.wait();
                    } else {
                        // 不为空则休眠
                        Threads.sleep(delay);
                    }
                } catch (InterruptedException e) {
                    throw Exceptions.interruptedRuntime(e);
                }
            }
        }
    }

    @Override
    public void close() throws IOException {
        synchronized (tasks) {
            this.run = false;
            // 唤醒等待
            tasks.notify();
        }
    }

}
