package com.orion.process;

import com.orion.process.handler.ErrorHandler;
import com.orion.process.handler.StreamHandler;
import com.orion.utils.Exceptions;
import com.orion.utils.Threads;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

/**
 * 进程执行器
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/17 13:17
 */
public class Processes {

    private Processes() {
    }

    /**
     * shell执行线程池
     */
    private static final ExecutorService POOL = Threads.newThreadPool(0, 15, 30000, new LinkedBlockingQueue<>(), "PROCESS-RUNNER-");

    /**
     * 同步执行命令
     *
     * @param command  command
     * @param consumer 行处理器
     * @return ProcessAwaitExecutor
     */
    public static ProcessAwaitExecutor awaitLine(String command, Consumer<String> consumer) {
        return awaitLine(command, null, null, null, consumer, null);
    }

    /**
     * 同步执行命令
     *
     * @param command  command
     * @param charset  编码格式
     * @param consumer 行处理器
     * @return ProcessAwaitExecutor
     */
    public static ProcessAwaitExecutor awaitLine(String command, String charset, Consumer<String> consumer) {
        return awaitLine(command, null, null, charset, consumer, null);
    }

    /**
     * 同步执行命令
     *
     * @param command  command
     * @param dir      命令路径
     * @param charset  编码格式
     * @param consumer 行处理器
     * @return ProcessAwaitExecutor
     */
    public static ProcessAwaitExecutor awaitLine(String command, String dir, String charset, Consumer<String> consumer) {
        return awaitLine(command, null, dir, charset, consumer, null);
    }

    /**
     * 同步执行命令
     *
     * @param command  command
     * @param consumer 行处理器
     * @return ProcessAwaitExecutor
     */
    public static ProcessAwaitExecutor awaitLine(String[] command, Consumer<String> consumer) {
        return awaitLine(null, command, null, null, consumer, null);
    }

    /**
     * 同步执行命令
     *
     * @param command  command
     * @param charset  编码格式
     * @param consumer 行处理器
     * @return ProcessAwaitExecutor
     */
    public static ProcessAwaitExecutor awaitLine(String[] command, String charset, Consumer<String> consumer) {
        return awaitLine(null, command, null, charset, consumer, null);
    }

    /**
     * 同步执行命令
     *
     * @param command  command
     * @param dir      命令路径
     * @param charset  编码格式
     * @param consumer 行处理器
     * @return ProcessAwaitExecutor
     */
    public static ProcessAwaitExecutor awaitLine(String[] command, String dir, String charset, Consumer<String> consumer) {
        return awaitLine(null, command, dir, charset, consumer, null);
    }

    /**
     * 同步执行命令
     *
     * @param command  command
     * @param charset  编码格式
     * @param consumer 行处理器
     * @param handler  异常处理器
     * @return ProcessAwaitExecutor
     */
    public static ProcessAwaitExecutor awaitLine(String command, String charset, Consumer<String> consumer, ErrorHandler<ProcessAwaitExecutor> handler) {
        return awaitLine(command, null, null, charset, consumer, handler);
    }

    /**
     * 同步执行命令
     *
     * @param command  command
     * @param dir      命令路径
     * @param charset  编码格式
     * @param consumer 行处理器
     * @param handler  异常处理器
     * @return ProcessAwaitExecutor
     */
    public static ProcessAwaitExecutor awaitLine(String command, String dir, String charset, Consumer<String> consumer, ErrorHandler<ProcessAwaitExecutor> handler) {
        return awaitLine(command, null, dir, charset, consumer, handler);
    }

    /**
     * 同步执行命令
     *
     * @param command  command
     * @param charset  编码格式
     * @param consumer 行处理器
     * @param handler  异常处理器
     * @return ProcessAwaitExecutor
     */
    public static ProcessAwaitExecutor awaitLine(String[] command, String charset, Consumer<String> consumer, ErrorHandler<ProcessAwaitExecutor> handler) {
        return awaitLine(null, command, null, charset, consumer, handler);
    }

    /**
     * 同步执行命令
     *
     * @param command  command
     * @param dir      命令路径
     * @param charset  编码格式
     * @param consumer 行处理器
     * @param handler  异常处理器
     * @return ProcessAwaitExecutor
     */
    public static ProcessAwaitExecutor awaitLine(String[] command, String dir, String charset, Consumer<String> consumer, ErrorHandler<ProcessAwaitExecutor> handler) {
        return awaitLine(null, command, dir, charset, consumer, handler);
    }

    private static ProcessAwaitExecutor awaitLine(String c, String[] cs, String dir, String charset, Consumer<String> consumer, ErrorHandler<ProcessAwaitExecutor> handler) {
        ProcessAwaitExecutor executor;
        if (c != null) {
            executor = new ProcessAwaitExecutor(c, dir);
        } else {
            executor = new ProcessAwaitExecutor(cs, dir);
        }
        executor.redirectErr().errorHandler(handler).streamHandler((p, i, e, o) -> {
            try {
                BufferedReader reader;
                if (charset == null) {
                    reader = new BufferedReader(new InputStreamReader(i));
                } else {
                    reader = new BufferedReader(new InputStreamReader(i, charset));
                }
                String line = reader.readLine();
                while (line != null) {
                    consumer.accept(line);
                    line = reader.readLine();
                }
            } catch (Exception e1) {
                throw Exceptions.ioRuntime(e1);
            }
        });
        POOL.execute(executor::await);
        return executor;
    }

    /**
     * 同步执行命令
     *
     * @param command       command
     * @param streamHandler 流处理器
     * @return ProcessAwaitExecutor
     */
    public static ProcessAwaitExecutor await(String command, StreamHandler streamHandler) {
        return await(command, null, null, streamHandler, null);
    }

    /**
     * 同步执行命令
     *
     * @param command       command
     * @param dir           命令路径
     * @param streamHandler 流处理器
     * @return ProcessAwaitExecutor
     */
    public static ProcessAwaitExecutor await(String command, String dir, StreamHandler streamHandler) {
        return await(command, null, dir, streamHandler, null);
    }

    /**
     * 同步执行命令
     *
     * @param command       command
     * @param streamHandler 流处理器
     * @return ProcessAwaitExecutor
     */
    public static ProcessAwaitExecutor await(String[] command, StreamHandler streamHandler) {
        return await(null, command, null, streamHandler, null);
    }

    /**
     * 同步执行命令
     *
     * @param command       command
     * @param dir           命令路径
     * @param streamHandler 流处理器
     * @return ProcessAwaitExecutor
     */
    public static ProcessAwaitExecutor await(String[] command, String dir, StreamHandler streamHandler) {
        return await(null, command, dir, streamHandler, null);
    }

    /**
     * 同步执行命令
     *
     * @param command       command
     * @param streamHandler 流处理器
     * @param handler       异常处理器
     * @return ProcessAwaitExecutor
     */
    public static ProcessAwaitExecutor await(String command, StreamHandler streamHandler, ErrorHandler<ProcessAwaitExecutor> handler) {
        return await(command, null, null, streamHandler, handler);
    }

    /**
     * 同步执行命令
     *
     * @param command       command
     * @param dir           命令路径
     * @param streamHandler 流处理器
     * @param handler       异常处理器
     * @return ProcessAwaitExecutor
     */
    public static ProcessAwaitExecutor await(String command, String dir, StreamHandler streamHandler, ErrorHandler<ProcessAwaitExecutor> handler) {
        return await(command, null, dir, streamHandler, handler);
    }

    /**
     * 同步执行命令
     *
     * @param command       command
     * @param streamHandler 流处理器
     * @param handler       异常处理器
     * @return ProcessAwaitExecutor
     */
    public static ProcessAwaitExecutor await(String[] command, StreamHandler streamHandler, ErrorHandler<ProcessAwaitExecutor> handler) {
        return await(null, command, null, streamHandler, handler);
    }

    /**
     * 同步执行命令
     *
     * @param command       command
     * @param dir           命令路径
     * @param streamHandler 流处理器
     * @param handler       异常处理器
     * @return ProcessAwaitExecutor
     */
    public static ProcessAwaitExecutor await(String[] command, String dir, StreamHandler streamHandler, ErrorHandler<ProcessAwaitExecutor> handler) {
        return await(null, command, dir, streamHandler, handler);
    }

    private static ProcessAwaitExecutor await(String c, String[] cs, String dir, StreamHandler streamHandler, ErrorHandler<ProcessAwaitExecutor> handler) {
        ProcessAwaitExecutor executor;
        if (c != null) {
            executor = new ProcessAwaitExecutor(c, dir);
        } else {
            executor = new ProcessAwaitExecutor(cs, dir);
        }
        POOL.execute(executor.redirectErr().errorHandler(handler).streamHandler(streamHandler)::await);
        return executor;
    }

    /**
     * 异步执行命令
     *
     * @param command 命令
     * @param out     异步输出文件夹
     * @return ProcessAsyncExecutor
     */
    public static ProcessAsyncExecutor async(String command, File out) {
        return async(command, null, null, out, null);
    }

    /**
     * 异步执行命令
     *
     * @param command 命令
     * @param dir     执行目录
     * @param out     异步输出文件夹
     * @return ProcessAsyncExecutor
     */
    public static ProcessAsyncExecutor async(String command, String dir, File out) {
        return async(command, null, dir, out, null);
    }

    /**
     * 异步执行命令
     *
     * @param command 命令
     * @param out     异步输出文件夹
     * @return ProcessAsyncExecutor
     */
    public static ProcessAsyncExecutor async(String[] command, File out) {
        return async(null, command, null, out, null);
    }

    /**
     * 异步执行命令
     *
     * @param command 命令
     * @param dir     执行目录
     * @param out     异步输出文件夹
     * @return ProcessAsyncExecutor
     */
    public static ProcessAsyncExecutor async(String[] command, String dir, File out) {
        return async(null, command, dir, out, null);
    }

    /**
     * 异步执行命令
     *
     * @param command 命令
     * @param out     异步输出文件夹
     * @param handler 异常处理器
     * @return ProcessAsyncExecutor
     */
    public static ProcessAsyncExecutor async(String command, File out, ErrorHandler<ProcessAsyncExecutor> handler) {
        return async(command, null, null, out, handler);
    }

    /**
     * 异步执行命令
     *
     * @param command 命令
     * @param dir     执行目录
     * @param out     异步输出文件夹
     * @param handler 异常处理器
     * @return ProcessAsyncExecutor
     */
    public static ProcessAsyncExecutor async(String command, String dir, File out, ErrorHandler<ProcessAsyncExecutor> handler) {
        return async(command, null, dir, out, handler);
    }

    /**
     * 异步执行命令
     *
     * @param command 命令
     * @param out     异步输出文件夹
     * @param handler 异常处理器
     * @return ProcessAsyncExecutor
     */
    public static ProcessAsyncExecutor async(String[] command, File out, ErrorHandler<ProcessAsyncExecutor> handler) {
        return async(null, command, null, out, handler);
    }

    /**
     * 异步执行命令
     *
     * @param command 命令
     * @param dir     执行目录
     * @param out     异步输出文件夹
     * @param handler 异常处理器
     * @return ProcessAsyncExecutor
     */
    public static ProcessAsyncExecutor async(String[] command, String dir, File out, ErrorHandler<ProcessAsyncExecutor> handler) {
        return async(null, command, dir, out, handler);
    }

    private static ProcessAsyncExecutor async(String c, String[] cs, String dir, File out, ErrorHandler<ProcessAsyncExecutor> handler) {
        ProcessAsyncExecutor executor;
        if (c != null) {
            executor = new ProcessAsyncExecutor(c, dir);
        } else {
            executor = new ProcessAsyncExecutor(cs, dir);
        }
        POOL.execute(executor.errorHandler(handler).redirectErr().outputFile(out)::async);
        return executor;
    }

}
