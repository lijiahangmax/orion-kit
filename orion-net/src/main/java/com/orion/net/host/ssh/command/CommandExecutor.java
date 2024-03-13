package com.orion.net.host.ssh.command;

import com.jcraft.jsch.ChannelExec;
import com.orion.lang.constant.Const;
import com.orion.lang.support.Attempt;
import com.orion.lang.support.timeout.TimeoutChecker;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.io.Streams;
import com.orion.net.host.ssh.BaseHostExecutor;

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
    private TimeoutChecker checker;

    /**
     * 开始时间
     */
    private long startTime;

    /**
     * 是否已超时
     */
    private volatile boolean expired;

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
    public void timeout(long timeout, TimeoutChecker checker) {
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
        this.expired = true;
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
            throw new RuntimeException(e);
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
        return expired;
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
