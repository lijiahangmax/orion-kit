package com.orion.net.host.ssh.command;

import com.jcraft.jsch.ChannelExec;
import com.orion.lang.constant.Const;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.net.host.HostConnector;

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
public class CommandExecutor extends BaseCommandExecutor implements HostConnector {

    /**
     * channel
     */
    private final ChannelExec channel;

    /**
     * command
     */
    private final byte[] command;

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
        // 分配伪终端 当程序关闭时 命令进程一起关闭
        channel.setPty(true);
    }

    @Override
    public void pty(boolean use) {
        channel.setPty(use);
    }

    @Override
    public void sendSignal(String signal) {
        try {
            channel.sendSignal(signal);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
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
        return command == null ? Const.EMPTY : this.getCommand();
    }

}
