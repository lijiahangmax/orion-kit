package com.orion.net.host.ssh.shell;

import com.jcraft.jsch.ChannelShell;
import com.orion.lang.utils.Exceptions;
import com.orion.net.host.HostConnector;

import java.io.IOException;

/**
 * shell 执行器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/7 19:50
 */
public class ShellExecutor extends BaseShellExecutor implements HostConnector {

    /**
     * channel
     */
    private final ChannelShell channel;

    public ShellExecutor(ChannelShell channel) {
        this.channel = channel;
        try {
            this.inputStream = channel.getInputStream();
            this.outputStream = channel.getOutputStream();
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    @Override
    public void x11Forward(boolean enable) {
        channel.setXForwarding(enable);
    }

    @Override
    public void resize() {
        try {
            channel.setPtySize(cols, rows, width, height);
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    @Override
    public void connect() {
        channel.setPtyType(terminalType, cols, rows, width, height);
        try {
            channel.connect();
        } catch (Exception e) {
            throw Exceptions.connection(e);
        }
    }

    @Override
    public void connect(int timeout) {
        channel.setPtyType(terminalType, cols, rows, width, height);
        try {
            channel.connect(timeout);
        } catch (Exception e) {
            throw Exceptions.connection(e);
        }
    }

    @Override
    public void env(byte[] key, byte[] value) {
        this.getChannel().setEnv(key, value);
    }

    @Override
    public void env(String key, String value) {
        channel.setEnv(key, value);
    }

    @Override
    public void exec() {
        if (streamHandler == null) {
            throw Exceptions.runtime("shell std output stream handler is null");
        }
        if (!this.isConnected()) {
            throw Exceptions.runtime("channel is not connected");
        }
        // 监听输出流
        super.listenerOutput();
    }

    @Override
    public void close() {
        super.close();
        this.disconnectChannel();
    }

    @Override
    public ChannelShell getChannel() {
        return channel;
    }

}
