package com.orion.net.host.ssh.command;

import com.orion.lang.define.thread.HookRunnable;
import com.orion.lang.support.Attempt;
import com.orion.lang.utils.Threads;
import com.orion.lang.utils.io.Streams;
import com.orion.net.host.ssh.BaseHostExecutor;

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
public abstract class BaseCommandExecutor extends BaseHostExecutor implements ICommandExecutor {

    /**
     * 命令错误输出流
     */
    protected InputStream errorStream;

    /**
     * 命令合并输出流
     */
    protected InputStream mergeStream;

    /**
     * 是否合并标准输出流和错误输出流
     */
    protected boolean merge;

    /**
     * 是否同步获取输出
     */
    protected boolean sync;

    /**
     * 错误输出流处理器
     */
    protected Consumer<InputStream> errorStreamHandler;

    @Override
    public void merge() {
        this.merge = true;
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
            if (merge) {
                streamHandler.accept(mergeStream);
            } else {
                streamHandler.accept(inputStream);
            }
            if (errorStreamHandler != null && !merge) {
                errorStreamHandler.accept(errorStream);
            }
        }, () -> {
            this.done = true;
            if (callback != null) {
                callback.run();
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
        Streams.close(mergeStream);
    }

    @Override
    public boolean isMerge() {
        return merge;
    }

    @Override
    public InputStream getMergeStream() {
        return mergeStream;
    }

    @Override
    public InputStream getErrorStream() {
        return errorStream;
    }

}
