package com.orion.remote.channel.ssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.orion.constant.Letters;
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
 * @author Jiahang Li
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
     * 设置环境变量
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public BaseRemoteExecutor env(byte[] key, byte[] value) {
        if (channel instanceof ChannelExec) {
            ((ChannelExec) channel).setEnv(key, value);
        } else if (channel instanceof ChannelShell) {
            ((ChannelShell) channel).setEnv(key, value);
        }
        return this;
    }

    /**
     * 设置环境变量
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public BaseRemoteExecutor env(String key, String value) {
        if (channel instanceof ChannelExec) {
            ((ChannelExec) channel).setEnv(key, value);
        } else if (channel instanceof ChannelShell) {
            ((ChannelShell) channel).setEnv(key, value);
        }
        return this;
    }

    /**
     * 写入命令
     *
     * @param command command
     * @return this
     */
    public BaseRemoteExecutor write(String command) {
        return this.write(Strings.bytes(command), false);
    }

    /**
     * 写入命令
     *
     * @param command command
     * @return this
     */
    public BaseRemoteExecutor writeLine(String command) {
        return this.write(Strings.bytes(command), true);
    }

    /**
     * 写入命令
     *
     * @param command command
     * @param charset 编码格式
     * @return this
     */
    public BaseRemoteExecutor write(String command, String charset) {
        return this.write(Strings.bytes(command, charset), false);
    }

    /**
     * 写入命令
     *
     * @param command command
     * @param charset 编码格式
     * @return this
     */
    public BaseRemoteExecutor writeLine(String command, String charset) {
        return this.write(Strings.bytes(command, charset), true);
    }

    /**
     * 写入命令
     *
     * @param command command
     * @return this
     */
    public BaseRemoteExecutor write(byte[] command) {
        return this.write(command, false);
    }

    /**
     * 写入命令
     *
     * @param command command
     * @param lf      是否键入 \n
     * @return this
     */
    public BaseRemoteExecutor write(byte[] command, boolean lf) {
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
    public BaseRemoteExecutor interrupt() {
        return this.write(new byte[]{3}, true);
    }

    /**
     * 挂起 键入 ctrl+x
     *
     * @return this
     */
    public BaseRemoteExecutor hangUp() {
        return this.write(new byte[]{24}, true);
    }

    /**
     * 退出 键入 exit 0
     *
     * @return this
     */
    public BaseRemoteExecutor exit() {
        return this.write(Strings.bytes("exit 0"), true);
    }

    /**
     * 发送信号量
     *
     * @param signal 信号
     * @return this
     */
    public BaseRemoteExecutor sendSignal(String signal) {
        try {
            channel.sendSignal(signal);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
        return this;
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
        this.run = true;
    }

    /**
     * 监听标准输出流
     */
    protected void listenerInput() {
        Runnable runnable = new HookRunnable(() -> {
            streamHandler.accept(this, inputStream);
        }, () -> {
            this.done = true;
            if (callback != null) {
                callback.accept(this);
            }
        }, true);
        Threads.start(runnable, scheduler);
    }

    /**
     * 关闭流
     */
    @Override
    public void close() {
        Streams.close(inputStream);
        Streams.close(outputStream);
        super.disconnectChannel();
    }

    /**
     * 是否结束
     *
     * @return 是否结束
     */
    public boolean isEof() {
        return channel.isEOF();
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

    public boolean isDone() {
        return done;
    }

}
