package com.orion.net.host.ssh.command;

import com.orion.lang.support.Attempt;
import com.orion.lang.support.timeout.TimeoutChecker;
import com.orion.lang.utils.Exceptions;
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
     * 是否合并标准输出流和错误输出流
     */
    protected boolean merge;

    /**
     * 错误输出流处理器
     */
    protected Consumer<InputStream> errorStreamHandler;

    /**
     * 超时时间 ms
     */
    protected long timeout;

    /**
     * 超时检测器
     */
    protected TimeoutChecker checker;

    /**
     * 开始时间
     */
    protected long startTime;

    /**
     * 是否已超时
     */
    protected volatile boolean expired;

    @Override
    public void merge() {
        this.merge = true;
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

    @Override
    public void timeout(long timeout, TimeoutChecker checker) {
        this.timeout = timeout;
        this.checker = checker;
    }

    @Override
    protected void listenerOutput() {
        this.startTime = System.currentTimeMillis();
        // 检测超时
        if (timeout > 0 && checker != null) {
            checker.addTask(this);
        }
        try {
            // 监听读取
            streamHandler.accept(inputStream);
            // 监听错误输出
            if (errorStreamHandler != null) {
                errorStreamHandler.accept(errorStream);
            }
        } catch (Exception e) {
            // 超时异常
            if (expired) {
                throw Exceptions.timeout(e);
            }
            throw e;
        } finally {
            this.done = true;
            if (callback != null) {
                callback.run();
            }
        }
    }

    @Override
    public boolean checkTimeout() {
        if (timeout == 0) {
            return false;
        }
        // 未超时
        if (System.currentTimeMillis() - startTime < timeout) {
            return false;
        }
        // 超时 直接断开连接
        this.expired = true;
        Streams.close(this);
        return false;
    }

    @Override
    public void close() {
        super.close();
        Streams.close(errorStream);
    }

    @Override
    public boolean isTimeout() {
        return expired;
    }

    @Override
    public InputStream getErrorStream() {
        return errorStream;
    }

}
