package com.orion.remote.channel.ssh;

import com.jcraft.jsch.ChannelShell;
import com.orion.utils.Exceptions;

import java.io.IOException;

/**
 * Shell执行器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/7 19:50
 */
public class ShellExecutor extends BaseRemoteExecutor {

    /**
     * channel
     */
    private ChannelShell channel;

    public ShellExecutor(ChannelShell channel) {
        super(channel);
        this.channel = channel;
        try {
            inputStream = this.channel.getInputStream();
            outputStream = this.channel.getOutputStream();
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 读取输出
     */
    @Override
    public void exec() {
        super.exec();
        super.listenerInput();
    }

    @Override
    public ChannelShell getChannel() {
        return channel;
    }

}
