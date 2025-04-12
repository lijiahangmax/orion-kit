/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.net.host.ssh.command;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.support.Attempt;
import cn.orionsec.kit.lang.support.timeout.TimeoutChecker;
import cn.orionsec.kit.lang.support.timeout.TimeoutEndpoint;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.net.host.ssh.BaseHostExecutor;
import com.jcraft.jsch.ChannelExec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * 命令执行器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/6 12:51
 */
public class CommandExecutor extends BaseHostExecutor<ChannelExec> implements ICommandExecutor {

    /**
     * command
     */
    private final byte[] command;

    /**
     * 命令错误输出流
     */
    private InputStream errorStream;

    /**
     * 是否合并标准输出流和错误输出流
     */
    private boolean merge;

    /**
     * 错误输出流处理器
     */
    private Consumer<InputStream> errorStreamHandler;

    /**
     * 超时时间 ms
     */
    private long timeout;

    /**
     * 超时检测器
     */
    private TimeoutChecker<TimeoutEndpoint> checker;

    /**
     * 开始时间
     */
    private long startTime;

    /**
     * 是否已超时
     */
    private volatile boolean isTimeout;

    public CommandExecutor(ChannelExec channel, String command) {
        this(channel, Strings.bytes(command, StandardCharsets.UTF_8));
    }

    public CommandExecutor(ChannelExec channel, String command, String charset) {
        this(channel, Strings.bytes(command, charset));
    }

    public CommandExecutor(ChannelExec channel, byte[] command) {
        super(channel);
        this.command = command;
        this.channel.setCommand(command);
        // 分配伪终端 当程序关闭时 命令进程一起关闭
        channel.setPty(true);
    }

    @Override
    public void pty(boolean enable) {
        channel.setPty(enable);
    }

    @Override
    public void env(byte[] key, byte[] value) {
        channel.setEnv(key, value);
    }

    @Override
    public void env(String key, String value) {
        channel.setEnv(key, value);
    }

    @Override
    public void x11Forward(boolean enable) {
        channel.setXForwarding(enable);
    }

    @Override
    public void setAgentForwarding(boolean enable) {
        channel.setAgentForwarding(enable);
    }

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
    public void timeout(long timeout, TimeoutChecker<TimeoutEndpoint> checker) {
        this.timeout = timeout;
        this.checker = checker;
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
        this.isTimeout = true;
        Streams.close(this);
        return true;
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
            if (isTimeout) {
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
    public void exec() {
        if (streamHandler == null) {
            throw Exceptions.runtime("command std out stream handler is null");
        }
        if (!this.isConnected()) {
            throw Exceptions.runtime("channel is not connected");
        }
        try {
            if (merge) {
                // 合并流
                this.inputStream = new SequenceInputStream(channel.getInputStream(), channel.getErrStream());
            } else {
                // 标准输出
                this.inputStream = channel.getInputStream();
                // 错误输出
                if (this.errorStreamHandler != null) {
                    this.errorStream = channel.getErrStream();
                }
            }
            // 标准输入
            this.outputStream = channel.getOutputStream();
        } catch (IOException e) {
            throw Exceptions.runtime(e);
        }
        // 监听 标准输出 错误输出
        this.listenerOutput();
    }

    @Override
    public void close() {
        super.close();
        Streams.close(errorStream);
        this.disconnectChannel();
    }

    @Override
    public int getExitCode() {
        return channel.getExitStatus();
    }

    @Override
    public String getCommand() {
        return new String(command, StandardCharsets.UTF_8);
    }

    @Override
    public byte[] getCommandBytes() {
        return command;
    }

    @Override
    public boolean isTimeout() {
        return isTimeout;
    }

    @Override
    public InputStream getErrorStream() {
        return errorStream;
    }

    @Override
    public String toString() {
        return command == null ? Const.EMPTY : this.getCommand();
    }

}
