package com.orion.remote.channel.ssh;

import com.jcraft.jsch.Channel;
import com.orion.lang.thread.HookRunnable;
import com.orion.remote.channel.BaseExecutor;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.Threads;
import com.orion.utils.io.Streams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * ssh Executor 基类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/2/21 19:05
 */
public abstract class BaseRemoteExecutor extends BaseExecutor {

    /**
     * 标准输入流
     */
    protected InputStream inputStream;

    /**
     * 标准输出流
     */
    protected OutputStream outputStream;

    /**
     * 标准输出流处理器
     */
    protected BiConsumer<? super BaseRemoteExecutor, InputStream> streamHandler;

    /**
     * 执行完毕回调
     */
    protected Consumer<? super BaseRemoteExecutor> callback;

    /**
     * 是否已运行
     */
    protected boolean run;

    /**
     * 是否关闭
     */
    protected volatile boolean close;

    /**
     * 是否执行完毕
     */
    protected volatile boolean done;

    /**
     * pool
     */
    protected ExecutorService scheduler;

    protected BaseRemoteExecutor(Channel channel) {
        super(channel);
    }

    /**
     * 设置读取线程池
     *
     * @param scheduler pool
     * @return this
     */
    public BaseRemoteExecutor scheduler(ExecutorService scheduler) {
        this.scheduler = scheduler;
        return this;
    }

    /**
     * 回调
     *
     * @param callback 回调方法
     * @return this
     */
    public BaseRemoteExecutor callback(Consumer<? super BaseRemoteExecutor> callback) {
        this.callback = callback;
        return this;
    }

    /**
     * 设置标准输出流处理器
     *
     * @param streamHandler 标准输出流处理器
     * @return this
     */
    public BaseRemoteExecutor streamHandler(BiConsumer<? super BaseRemoteExecutor, InputStream> streamHandler) {
        this.streamHandler = streamHandler;
        return this;
    }

    /**
     * 写入命令
     *
     * @param command command
     * @return this
     */
    public BaseRemoteExecutor write(String command) {
        return this.write(Strings.bytes(command));
    }

    /**
     * 写入命令
     *
     * @param command command
     * @param charset 编码格式
     * @return this
     */
    public BaseRemoteExecutor write(String command, String charset) {
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
    public BaseRemoteExecutor write(byte[] command) {
        try {
            outputStream.write(command);
            outputStream.write('\n');
            outputStream.flush();
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
        return this;
    }

    /**
     * 退出
     *
     * @return this
     */
    public BaseRemoteExecutor exit() {
        return this.write(Strings.bytes("exit 0"));
    }

    @Override
    public void exec() {
        if (streamHandler == null) {
            throw Exceptions.runtime("streamHandler is null");
        }
        if (!this.isConnected()) {
            throw Exceptions.runtime("channel is not connected");
        }
        if (run) {
            throw Exceptions.runtime("this executor can only be executed once");
        }
        run = true;
    }

    /**
     * 监听标准输出流
     */
    protected void listenerInput() {
        Runnable runnable = new HookRunnable(() -> {
            streamHandler.accept(this, inputStream);
        }, () -> {
            done = true;
            if (this.callback != null) {
                this.callback.accept(this);
            }
        }, true);
        Threads.start(runnable, scheduler);
    }

    /**
     * 关闭流
     */
    @Override
    public void close() {
        close = true;
        Streams.close(inputStream);
        Streams.close(outputStream);
        super.disconnect();
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public boolean isRun() {
        return run;
    }

    public boolean isClose() {
        return close;
    }

    public boolean isDone() {
        return done;
    }

}
