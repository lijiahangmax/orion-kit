package com.orion.tail;

import com.orion.utils.Threads;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

/**
 * 文件追踪执行器
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/14 23:51
 */
public class TrackExecutor {

    private TrackExecutor() {
    }

    /**
     * tail 线程池
     */
    private static final ExecutorService TAIL_POOL = Threads.newThreadPool(0, 10, 10000, new LinkedBlockingQueue<>(), "TAIL-RUNNER-");

    /**
     * 追踪文件
     *
     * @param file    文件
     * @param handler handler
     * @return Tracker
     */
    public static Tracker tail(File file, LineHandler handler) {
        Tracker tracker = new Tracker(file, handler);
        TAIL_POOL.execute(tracker);
        return tracker;
    }

    /**
     * 追踪文件
     *
     * @param file    文件
     * @param handler handler
     * @return Tracker
     */
    public static Tracker tail(File file, Consumer<String> handler) {
        Tracker tracker = new Tracker(file, (s, l, t) -> handler.accept(s));
        TAIL_POOL.execute(tracker);
        return tracker;
    }

    /**
     * 追踪文件
     *
     * @param file    文件
     * @param charset 编码格式
     * @param handler handler
     * @return Tracker
     */
    public static Tracker tail(File file, String charset, LineHandler handler) {
        Tracker tracker = new Tracker(file, charset, handler);
        TAIL_POOL.execute(tracker);
        return tracker;
    }

    /**
     * 追踪文件
     *
     * @param file    文件
     * @param charset 编码格式
     * @param handler handler
     * @return Tracker
     */
    public static Tracker tail(File file, String charset, Consumer<String> handler) {
        Tracker tracker = new Tracker(file, charset, (s, l, t) -> handler.accept(s));
        TAIL_POOL.execute(tracker);
        return tracker;
    }

    /**
     * 追踪文件 只读取新的偏移数据
     *
     * @param file    文件
     * @param handler handler
     * @return Tracker
     */
    public static Tracker tailLast(File file, LineHandler handler) {
        Tracker tracker = new Tracker(file, handler).tailStartPos(0).fileMinusReadCurrent();
        TAIL_POOL.execute(tracker);
        return tracker;
    }

    /**
     * 追踪文件 只读取新的偏移数据
     *
     * @param file    文件
     * @param handler handler
     * @return Tracker
     */
    public static Tracker tailLast(File file, Consumer<String> handler) {
        Tracker tracker = new Tracker(file, (s, l, t) -> handler.accept(s)).tailStartPos(0).fileMinusReadCurrent();
        TAIL_POOL.execute(tracker);
        return tracker;
    }

    /**
     * 追踪文件 只读取新的偏移数据
     *
     * @param file    文件
     * @param charset 编码格式
     * @param handler handler
     * @return Tracker
     */
    public static Tracker tailLast(File file, String charset, LineHandler handler) {
        Tracker tracker = new Tracker(file, charset, handler).tailStartPos(0).fileMinusReadCurrent();
        TAIL_POOL.execute(tracker);
        return tracker;
    }

    /**
     * 追踪文件 只读取新的偏移数据
     *
     * @param file    文件
     * @param charset 编码格式
     * @param handler handler
     * @return Tracker
     */
    public static Tracker tailLast(File file, String charset, Consumer<String> handler) {
        Tracker tracker = new Tracker(file, charset, (s, l, t) -> handler.accept(s)).tailStartPos(0).fileMinusReadCurrent();
        TAIL_POOL.execute(tracker);
        return tracker;
    }

    /**
     * 追踪文件 如果文件不存在则等待
     *
     * @param file    文件
     * @param handler handler
     * @return Tracker
     */
    public static Tracker tailForce(File file, LineHandler handler) {
        Tracker tracker = new Tracker(file, handler).fileNotFoundWait();
        TAIL_POOL.execute(tracker);
        return tracker;
    }

    /**
     * 追踪文件 如果文件不存在则等待
     *
     * @param file    文件
     * @param handler handler
     * @return Tracker
     */
    public static Tracker tailForce(File file, Consumer<String> handler) {
        Tracker tracker = new Tracker(file, (s, l, t) -> handler.accept(s)).fileNotFoundWait();
        TAIL_POOL.execute(tracker);
        return tracker;
    }

    /**
     * 追踪文件 如果文件不存在则等待
     *
     * @param file    文件
     * @param charset 编码格式
     * @param handler handler
     * @return Tracker
     */
    public static Tracker tailForce(File file, String charset, LineHandler handler) {
        Tracker tracker = new Tracker(file, charset, handler).fileNotFoundWait();
        TAIL_POOL.execute(tracker);
        return tracker;
    }

    /**
     * 追踪文件 如果文件不存在则等待
     *
     * @param file    文件
     * @param charset 编码格式
     * @param handler handler
     * @return Tracker
     */
    public static Tracker tailForce(File file, String charset, Consumer<String> handler) {
        Tracker tracker = new Tracker(file, charset, (s, l, t) -> handler.accept(s)).fileNotFoundWait();
        TAIL_POOL.execute(tracker);
        return tracker;
    }

}
