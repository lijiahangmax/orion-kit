package com.orion.net.host;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;
import com.orion.lang.utils.Exceptions;

/**
 * 主机连接器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/5/18 10:28
 */
public interface HostConnector {

    /**
     * 建立连接
     */
    default void connect() {
        try {
            this.getChannel().connect();
        } catch (Exception e) {
            throw Exceptions.connection(e);
        }
    }

    /**
     * 建立连接
     * 使用此方法可能会导致连接不上 但是还不会抛出异常! 即 isConnected() 返回 false
     * 因为设置了超时时间 会将 {@link Channel#sendChannelOpen} 的 retry 设置为 1
     *
     * @param timeout 超时时间 ms
     */
    @SuppressWarnings("ALL")
    default void connect(int timeout) {
        try {
            this.getChannel().connect(timeout);
        } catch (Exception e) {
            throw Exceptions.connection(e);
        }
    }

    /**
     * 关闭 channel
     */
    default void disconnectChannel() {
        this.getChannel().disconnect();
    }

    /**
     * 关闭 session
     */
    default void disconnectSession() {
        this.getSession().disconnect();
    }

    /**
     * 关闭 channel 和 session
     */
    default void disconnect() {
        this.getChannel().disconnect();
        this.getSession().disconnect();
    }

    /**
     * 检查 channel 是否已连接
     *
     * @return 是否已连接
     */
    default boolean isConnected() {
        return this.getChannel().isConnected();
    }

    /**
     * 检查 channel 是否已关闭
     *
     * @return 是否已关闭
     */
    default boolean isClosed() {
        return this.getChannel().isClosed();
    }

    /**
     * 检查 channel 是否已结束
     *
     * @return 是否已结束
     */
    default boolean isEof() {
        return this.getChannel().isEOF();
    }

    /**
     * 获取 session
     *
     * @return session
     */
    default Session getSession() {
        try {
            return this.getChannel().getSession();
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }

    /**
     * 获取 channel
     *
     * @return channel
     */
    Channel getChannel();

}
