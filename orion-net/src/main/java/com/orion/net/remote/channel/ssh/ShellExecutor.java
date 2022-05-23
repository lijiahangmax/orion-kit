package com.orion.net.remote.channel.ssh;

import com.jcraft.jsch.ChannelShell;
import com.orion.net.base.ssh.BaseShellExecutor;
import com.orion.net.remote.channel.ChannelConnector;
import com.orion.utils.Exceptions;

import java.io.IOException;

/**
 * shell 执行器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/7 19:50
 */
public class ShellExecutor extends BaseShellExecutor implements ChannelConnector {

    /**
     * channel
     */
    private ChannelShell channel;

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
    public void exec() {
        if (streamHandler == null) {
            throw Exceptions.runtime("shell std output stream handler is null");
        }
        if (!this.isConnected()) {
            throw Exceptions.runtime("channel is not connected");
        }
        if (run) {
            throw Exceptions.runtime("shell executor can only be executed once");
        }
        this.run = true;
        // read standard output
        super.listenerStdout();
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
