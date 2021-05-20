package com.orion.remote.channel;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;
import com.orion.able.Executable;
import com.orion.able.SafeCloseable;
import com.orion.utils.Exceptions;

/**
 * Executor 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/5 23:37
 */
public abstract class BaseExecutor implements Executable, SafeCloseable {

    protected Channel channel;

    protected BaseExecutor(Channel channel) {
        this.channel = channel;
    }

    /**
     * 打开连接
     *
     * @return this
     */
    public BaseExecutor connect() {
        try {
            channel.connect();
            return this;
        } catch (Exception e) {
            throw Exceptions.connection(e);
        }
    }

    /**
     * 打开连接
     * 使用此方法可能会导致连接不上 但是还不会抛出异常! 即 isConnected() 返回false
     * 因为设置了超时时间 会将 Channel#sendChannelOpen 的 retry 设置为1
     *
     * @param timeout 超时时间 ms
     * @return this
     */
    public BaseExecutor connect(int timeout) {
        try {
            channel.connect(timeout);
            return this;
        } catch (Exception e) {
            throw Exceptions.connection(e);
        }
    }

    /**
     * 关闭连接
     *
     * @return this
     */
    public BaseExecutor disconnectChannel() {
        channel.disconnect();
        return this;
    }

    /**
     * 关闭连接
     *
     * @return this
     */
    public BaseExecutor disconnectSession() {
        this.getSession().disconnect();
        return this;
    }

    /**
     * 关闭连接
     *
     * @return this
     */
    public BaseExecutor disconnect() {
        channel.disconnect();
        this.getSession().disconnect();
        return this;
    }

    /**
     * @return 是否已连接
     */
    public boolean isConnected() {
        return channel.isConnected();
    }

    /**
     * @return 是否已关闭
     */
    public boolean isClosed() {
        return channel.isClosed();
    }

    public Session getSession() {
        try {
            return channel.getSession();
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }

    public Channel getChannel() {
        return channel;
    }

}
