package com.orion.net.remote.channel.ssh;

import com.jcraft.jsch.ChannelExec;
import com.orion.net.base.ssh.BaseCommandExecutor;
import com.orion.net.remote.channel.ChannelConnector;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;

import java.io.IOException;
import java.io.SequenceInputStream;
import java.nio.charset.StandardCharsets;

/**
 * 命令执行器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/6 12:51
 */
public class CommandExecutor extends BaseCommandExecutor implements ChannelConnector {

    /**
     * channel
     */
    private ChannelExec channel;

    /**
     * command
     */
    private byte[] command;

    public CommandExecutor(ChannelExec channel, String command) {
        this(channel, Strings.bytes(command, StandardCharsets.UTF_8));
    }

    public CommandExecutor(ChannelExec channel, String command, String charset) {
        this(channel, Strings.bytes(command, charset));
    }

    public CommandExecutor(ChannelExec channel, byte[] command) {
        this.channel = channel;
        this.command = command;
        this.channel.setCommand(command);
        try {
            this.inputStream = channel.getInputStream();
            this.errorStream = channel.getErrStream();
            this.outputStream = channel.getOutputStream();
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
        // 分配伪终端 当程序关闭时 命令进程一起关闭
        channel.setPty(true);
    }

    @Override
    public void pty(boolean use) {
        channel.setPty(use);
    }

    /**
     * 发送信号量
     *
     * @param signal 信号
     */
    public void sendSignal(String signal) {
        try {
            channel.sendSignal(signal);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 设置环境变量
     *
     * @param key   key
     * @param value value
     */
    public void env(byte[] key, byte[] value) {
        channel.setEnv(key, value);
    }

    /**
     * 设置环境变量
     *
     * @param key   key
     * @param value value
     */
    public void env(String key, String value) {
        channel.setEnv(key, value);
    }

    @Override
    public void exec() {
        if (streamHandler == null) {
            throw Exceptions.runtime("command std out stream handler is null");
        }
        if (!this.isConnected()) {
            throw Exceptions.runtime("channel is not connected");
        }
        if (run) {
            throw Exceptions.runtime("this executor can only be executed once");
        }
        this.run = true;
        if (inherit) {
            this.inheritStream = new SequenceInputStream(inputStream, errorStream);
        }
        // read standard input & error stream
        this.listenerStdoutAndError();
    }

    @Override
    public void close() {
        super.close();
        this.disconnectChannel();
    }

    @Override
    public int getExitCode() {
        return channel.getExitStatus();
    }

    @Override
    public ChannelExec getChannel() {
        return channel;
    }

    @Override
    public String getCommand() {
        return new String(command, StandardCharsets.UTF_8);
    }

    public byte[] getCommandBytes() {
        return command;
    }

    @Override
    public String toString() {
        return command == null ? "[]" : "[" + this.getCommand() + "]";
    }
}
