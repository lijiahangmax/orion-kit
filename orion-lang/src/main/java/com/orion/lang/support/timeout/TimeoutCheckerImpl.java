package com.orion.lang.support.timeout;

import com.orion.lang.utils.Threads;

import java.util.ArrayList;
import java.util.List;

/**
 * 超时检测器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/15 19:24
 */
public class TimeoutCheckerImpl<T extends TimeoutEndpoint> implements TimeoutChecker<T> {

    private final List<T> tasks = new ArrayList<>();

    private final long delay;

    private boolean run;

    public TimeoutCheckerImpl() {
        this(TimeoutCheckers.DEFAULT_DELAY);
    }

    public TimeoutCheckerImpl(long delay) {
        this.delay = delay;
        this.run = true;
    }

    @Override
    public void addTask(T task) {
        tasks.add(task);
    }

    @Override
    public void run() {
        while (run) {
            // 完成或超时 直接移除
            tasks.removeIf(ch -> ch.isDone() || ch.checkTimeout());
            // 等待
            Threads.sleep(delay);
        }
    }

    @Override
    public void clear() {
        tasks.clear();
    }


    @Override
    public boolean isEmpty() {
        return tasks.isEmpty();
    }


    @Override
    public boolean isRun() {
        return run;
    }

    @Override
    public List<T> getTasks() {
        return tasks;
    }

    @Override
    public void close() {
        this.run = false;
    }

}
