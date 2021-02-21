package com.orion.process;

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
 * @author ljh15
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
        return this.write(Strings.bytes(command));
    }

    /**
     * 写入命令
     *
     * @param command command
     * @param charset 编码格式
     * @return this
     */
    public ProcessAwaitExecutor write(String command, String charset) {
        if (charset == null) {
            return this.write(Strings.bytes(command));
        } else {
            return this.write(Strings.bytes(command, charset));
        }
    }

    /**
     * 写入命令
     *
     * @param command command
     * @return this
     */
    public ProcessAwaitExecutor write(byte[] command) {
        try {
            this.outputStream.write(command);
            this.outputStream.write('\n');
            this.outputStream.flush();
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
        return this;
    }

    @Override
    public void exec() {
        if (streamHandler == null) {
            throw Exceptions.runtime("streamHandler is null");
        }
        try {
            this.pb = new ProcessBuilder(command);
            this.env = this.pb.environment();
            if (this.addEnv != null) {
                this.env.putAll(this.addEnv);
            }
            if (this.removeEnv != null) {
                for (String key : this.removeEnv) {
                    this.env.remove(key);
                }
            }
            this.pb.directory(this.dir == null ? null : new File(this.dir));
            if (this.inherit) {
                this.pb.inheritIO();
            }
            // 是否将错误流合并到输出流
            this.process = this.pb.redirectErrorStream(this.redirectError).start();
            this.outputStream = this.process.getOutputStream();
            this.inputStream = this.process.getInputStream();
            this.errorStream = this.process.getErrorStream();

            // 如果流不处理可能会阻塞
            this.listenerInputAndError();

            if (this.waitFor != -1) {
                if (this.waitFor != 0) {
                    this.process.waitFor(this.waitFor, TimeUnit.MILLISECONDS);
                } else {
                    this.process.waitFor();
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
        close(false);
    }

    /**
     * 关闭进程
     *
     * @param exit 是否键入exit
     */
    public void close(boolean exit) {
        this.close = true;
        if (exit && this.outputStream != null) {
            try {
                this.write(Strings.bytes("exit 0"));
            } catch (Exception e1) {
                Exceptions.printStacks(e1);
            }
        }
        if (this.process != null) {
            this.process.destroy();
        }
        Streams.close(this.inputStream);
        Streams.close(this.outputStream);
        Streams.close(this.errorStream);
    }

    /**
     * 是否正在执行
     *
     * @return true执行中
     */
    @Override
    public boolean isAlive() {
        return this.process.isAlive();
    }

    /**
     * 退出
     *
     * @return this
     */
    public ProcessAwaitExecutor exit() {
        return this.write(Strings.bytes("exit 0"));
    }

    /**
     * 获取exit code 会阻塞
     *
     * @return -1 未执行完毕  0 成功  1 失败
     */
    @Override
    public int getExitCode() {
        if (!this.process.isAlive()) {
            return this.process.exitValue();
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
        Runnable runnable = new HookRunnable(() -> {
            streamHandler.accept(this, inputStream);
            if (errorStreamHandler != null && !redirectError) {
                errorStreamHandler.accept(this, errorStream);
            }
        }, () -> {
            done = true;
            if (this.callback != null) {
                this.callback.accept(this);
            }
        }, true);
        Threads.start(runnable, scheduler);
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
