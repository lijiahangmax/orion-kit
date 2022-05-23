package com.orion.net.base.ssh;

import com.orion.support.Attempt;
import com.orion.utils.Exceptions;
import com.orion.utils.io.Streams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * 远程执行器 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/5/17 14:32
 */
public abstract class BaseRemoteExecutor implements IRemoteExecutor {

    /**
     * 标准输出流
     */
    protected InputStream inputStream;

    /**
     * 标准输入流
     */
    protected OutputStream outputStream;

    /**
     * 标准输出流处理器
     */
    protected Consumer<InputStream> streamHandler;

    /**
     * 执行完毕回调
     */
    protected Consumer<? super IRemoteExecutor> callback;

    /**
     * 是否已运行
     */
    protected volatile boolean run;

    /**
     * 是否执行完毕
     */
    protected volatile boolean done;

    /**
     * 调度线程池
     */
    protected ExecutorService scheduler;

    @Override
    public void scheduler(ExecutorService scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void callback(Consumer<? super IRemoteExecutor> callback) {
        this.callback = callback;
    }

    @Override
    public void transfer(OutputStream out) throws IOException {
        this.streamHandler = Attempt.rethrows(i -> {
            Streams.transfer(i, out);
        });
    }

    @Override
    public void streamHandler(Consumer<InputStream> streamHandler) {
        this.streamHandler = streamHandler;
    }

    @Override
    public void write(byte[] command) {
        try {
            outputStream.write(command);
            outputStream.flush();
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    @Override
    public void close() {
        Streams.close(inputStream);
        Streams.close(outputStream);
    }

    @Override
    public boolean isRun() {
        return run;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }

}
