package com.orion.remote.channel.executor;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;
import com.orion.able.Executable;
import com.orion.able.SafeCloseable;
import com.orion.utils.Exceptions;

/**
 * BaseExecutor
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/5 23:37
 */
public abstract class BaseExecutor implements Executable, SafeCloseable {

    /**
     * 正常退出嘛
     */
    private static final Integer NORMAL_EXIT_CODE = 0;

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
        getSession().disconnect();
        return this;
    }

    /**
     * 关闭连接
     *
     * @return this
     */
    public BaseExecutor disconnect() {
        channel.disconnect();
        getSession().disconnect();
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

    /**
     * 获取执行码
     *
     * @return 0正常结束
     */
    public int getExitCode() {
        return channel.getExitStatus();
    }

    /**
     * 是否正常退出
     *
     * @return ignore
     */
    public boolean isNormalExit() {
        return NORMAL_EXIT_CODE.equals(channel.getExitStatus());
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
