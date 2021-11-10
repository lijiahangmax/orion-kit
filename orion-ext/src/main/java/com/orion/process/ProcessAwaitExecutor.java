package com.orion.process;

import com.orion.constant.Letters;
import com.orion.lang.thread.HookRunnable;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.Threads;
import com.orion.utils.io.Streams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 同步进程执行器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/17 13:44
 */
public class ProcessAwaitExecutor extends BaseProcessExecutor {

    /**
     * ProcessBuilder
     */
    private ProcessBuilder pb;

    /**
     * Process
     */
    private Process process;

    /**
     * 输入流
     */
    private InputStream inputStream;

    /**
     * 错误流
     */
    private InputStream errorStream;

    /**
     * 输出流
     */
    private OutputStream outputStream;

    /**
     * 是否合并流到系统流
     */
    private boolean inherit;

    /**
     * 是否等待命令执行
     */
    private long waitFor = -1L;

    /**
     * 流处理器
     */
    private BiConsumer<ProcessAwaitExecutor, InputStream> streamHandler;

    /**
     * 流处理器
     */
    private BiConsumer<ProcessAwaitExecutor, InputStream> errorStreamHandler;

    /**
     * 执行完毕回调
     */
    protected Consumer<? super ProcessAwaitExecutor> callback;

    /**
     * pool
     */
    protected ExecutorService scheduler;

    /**
     * 是否执行命令同步获取结果
     */
    private boolean sync;

    /**
     * 是否已完成
     */
    private volatile boolean done;

    /**
     * 是否已关闭
     */
    private volatile boolean close;

    public ProcessAwaitExecutor(String command) {
        this(new String[]{command}, null);
    }

    public ProcessAwaitExecutor(String[] command) {
        this(command, null);
    }

    public ProcessAwaitExecutor(String command, String dir) {
        this(new String[]{command}, dir);
    }

    public ProcessAwaitExecutor(String[] command, String dir) {
        super(command, dir);
        this.streamHandler = streamHandler;
    }

    /**
     * 合并子流到系统流
     *
     * @return this
     */
    public ProcessAwaitExecutor inherit() {
        this.inherit = true;
        return this;
    }

    /**
     * 等待命令执行完毕
     *
     * @return this
     */
    public ProcessAwaitExecutor waitFor() {
        this.waitFor = 0L;
        return this;
    }

    /**
     * 是否等待命令执行完毕
     *
     * @param timeout 超时时间
     * @return this
     */
    public ProcessAwaitExecutor waitFor(long timeout) {
        this.waitFor = timeout;
        return this;
    }

    /**
     * 执行命令同步获取结果
     *
     * @return this
     */
    public ProcessAwaitExecutor sync() {
        this.sync = true;
        this.waitFor = 0L;
        return this;
    }

    /**
     * 设置标准输出流处理器
     *
     * @param streamHandler 标准输出流处理器
     * @return this
     */
    public ProcessAwaitExecutor streamHandler(BiConsumer<ProcessAwaitExecutor, InputStream> streamHandler) {
        this.streamHandler = streamHandler;
        return this;
    }

    /**
     * 设置标错误出流处理器
     *
     * @param errorStreamHandler 错误输出流处理器
     * @return this
     */
    public ProcessAwaitExecutor errorStreamHandler(BiConsumer<ProcessAwaitExecutor, InputStream> errorStreamHandler) {
        this.errorStreamHandler = errorStreamHandler;
        return this;
    }

    /**
     * 设置读取线程池
     *
     * @param scheduler pool
     * @return this
     */
    public ProcessAwaitExecutor scheduler(ExecutorService scheduler) {
        this.scheduler = scheduler;
        return this;
    }

    /**
     * 回调
     *
     * @param callback 回调方法
     * @return this
     */
    public ProcessAwaitExecutor callback(Consumer<ProcessAwaitExecutor> callback) {
        this.callback = callback;
        return this;
    }

    /**
     * 写入命令
     *
     * @param command command
     * @return this
     */
    public ProcessAwaitExecutor write(String command) {
        return this.write(Strings.bytes(command), false);
    }

    /**
     * 写入命令
     *
     * @param command command
     * @return this
     */
    public ProcessAwaitExecutor writeLine(String command) {
        return this.write(Strings.bytes(command), true);
    }

    /**
     * 写入命令
     *
     * @param command command
     * @param charset 编码格式
     * @return this
     */
    public ProcessAwaitExecutor write(String command, String charset) {
        return this.write(Strings.bytes(command, charset), false);
    }

    /**
     * 写入命令
     *
     * @param command command
     * @param charset 编码格式
     * @return this
     */
    public ProcessAwaitExecutor writeLine(String command, String charset) {
        return this.write(Strings.bytes(command, charset), true);
    }

    /**
     * 写入命令
     *
     * @param command command
     * @return this
     */
    public ProcessAwaitExecutor write(byte[] command) {
        return this.write(command, false);
    }

    /**
     * 写入命令
     *
     * @param command command
     * @param lf      是否键入 \n
     * @return this
     */
    public ProcessAwaitExecutor write(byte[] command, boolean lf) {
        try {
            outputStream.write(command);
            if (lf) {
                outputStream.write(Letters.LF);
            }
            outputStream.flush();
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
        return this;
    }

    /**
     * 中断 键入 ctrl+c
     *
     * @return this
     */
    public ProcessAwaitExecutor interrupt() {
        return this.write(new byte[]{3}, true);
    }

    /**
     * 挂起 键入 ctrl+x
     *
     * @return this
     */
    public ProcessAwaitExecutor hangUp() {
        return this.write(new byte[]{24}, true);
    }

    /**
     * 退出
     *
     * @return this
     */
    public ProcessAwaitExecutor exit() {
        return this.write(Strings.bytes("exit 0"), true);
    }

    @Override
    public void exec() {
        if (streamHandler == null) {
            throw Exceptions.runtime("streamHandler is null");
        }
        try {
            this.pb = new ProcessBuilder(command);
            this.env = pb.environment();
            if (removeEnv != null) {
                for (String key : removeEnv) {
                    env.remove(key);
                }
            }
            if (addEnv != null) {
                env.putAll(addEnv);
            }
            pb.directory(dir == null ? null : new File(dir));
            if (inherit) {
                pb.inheritIO();
            }
            // 是否将错误流合并到输出流
            this.process = pb.redirectErrorStream(redirectError).start();
            this.outputStream = process.getOutputStream();
            this.inputStream = process.getInputStream();
            this.errorStream = process.getErrorStream();

            // 如果流不处理可能会阻塞
            this.listenerInputAndError();

            if (waitFor != -1) {
                if (waitFor != 0) {
                    process.waitFor(waitFor, TimeUnit.MILLISECONDS);
                } else {
                    process.waitFor();
                }
            }
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }

    /**
     * 关闭进程
     */
    @Override
    public void close() {
        this.close(false);
    }

    /**
     * 关闭进程
     *
     * @param exit 是否键入exit
     */
    public void close(boolean exit) {
        this.close = true;
        if (exit && outputStream != null) {
            try {
                this.write(Strings.bytes("exit 0"));
            } catch (Exception e1) {
                Exceptions.printStacks(e1);
            }
        }
        if (process != null) {
            process.destroy();
        }
        Streams.close(inputStream);
        Streams.close(outputStream);
        Streams.close(errorStream);
    }

    /**
     * 是否正在执行
     *
     * @return true执行中
     */
    @Override
    public boolean isAlive() {
        return process.isAlive();
    }

    /**
     * 获取exit code 会阻塞
     *
     * @return -1 未执行完毕  0 成功  1 失败
     */
    @Override
    public int getExitCode() {
        if (!process.isAlive()) {
            return process.exitValue();
        }
        return -1;
    }

    @Override
    public Process getProcess() {
        return process;
    }

    @Override
    public ProcessBuilder getProcessBuilder() {
        return pb;
    }

    /**
     * 监听标准输出流和错误流
     */
    private void listenerInputAndError() {
        if (!sync) {
            Runnable runnable = new HookRunnable(() -> {
                streamHandler.accept(this, inputStream);
                if (errorStreamHandler != null && !redirectError) {
                    errorStreamHandler.accept(this, errorStream);
                }
            }, () -> {
                this.done = true;
                if (callback != null) {
                    callback.accept(this);
                }
            }, true);
            Threads.start(runnable, scheduler);
        } else {
            try {
                streamHandler.accept(this, inputStream);
            } finally {
                this.done = true;
                if (callback != null) {
                    callback.accept(this);
                }
            }
        }
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public InputStream getErrorStream() {
        return errorStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public boolean isInherit() {
        return inherit;
    }

    public long getWaitFor() {
        return waitFor;
    }

    public boolean isDone() {
        return done;
    }

    public boolean isClose() {
        return close;
    }

}
