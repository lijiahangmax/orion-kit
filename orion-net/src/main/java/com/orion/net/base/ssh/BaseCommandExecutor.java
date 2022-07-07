package com.orion.net.base.ssh;

import com.orion.lang.define.thread.HookRunnable;
import com.orion.lang.support.Attempt;
import com.orion.lang.utils.Threads;
import com.orion.lang.utils.io.Streams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;

/**
 * 命令执行器 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/5/17 15:26
 */
public abstract class BaseCommandExecutor extends BaseRemoteExecutor implements ICommandExecutor {

    /**
     * 命令错误输出流
     */
    protected InputStream errorStream;

    /**
     * 命令合并输出流
     */
    protected InputStream inheritStream;

    /**
     * 是否合并标准输出流和错误输出流
     */
    protected boolean inherit;

    /**
     * 是否同步获取输出
     */
    protected boolean sync;

    /**
     * 错误输出流处理器
     */
    protected Consumer<InputStream> errorStreamHandler;

    @Override
    public void inherit() {
        this.inherit = true;
    }

    @Override
    public void sync() {
        this.sync = true;
    }

    @Override
    public void errorStreamHandler(Consumer<InputStream> errorStreamHandler) {
        this.errorStreamHandler = errorStreamHandler;
    }

    @Override
    public void transferError(OutputStream out) throws IOException {
        this.errorStreamHandler = Attempt.rethrows(i -> {
            Streams.transfer(i, out);
        });
    }

    /**
     * 监听标准输出流和错误输出流
     */
    protected void listenerStdoutAndError() {
        // 监听读取
        Runnable runnable = new HookRunnable(() -> {
            if (inherit) {
                streamHandler.accept(inheritStream);
            } else {
                streamHandler.accept(inputStream);
            }
            if (errorStreamHandler != null && !inherit) {
                errorStreamHandler.accept(errorStream);
            }
        }, () -> {
            this.done = true;
            if (callback != null) {
                callback.accept(this);
            }
        }, true);
        if (sync) {
            // 同步执行
            runnable.run();
        } else {
            // 异步执行
            Threads.start(runnable, scheduler);
        }
    }

    @Override
    public void close() {
        super.close();
        Streams.close(errorStream);
        Streams.close(inheritStream);
    }

    @Override
    public boolean isInherit() {
        return inherit;
    }

    @Override
    public InputStream getInheritStream() {
        return inheritStream;
    }

    @Override
    public InputStream getErrorStream() {
        return errorStream;
    }

}
