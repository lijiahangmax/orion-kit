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
package cn.orionsec.kit.net.host;

import cn.orionsec.kit.lang.able.SafeCloseable;
import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.net.host.sftp.SftpExecutor;
import cn.orionsec.kit.net.host.ssh.command.CommandExecutor;
import cn.orionsec.kit.net.host.ssh.shell.ShellExecutor;
import com.jcraft.jsch.*;

/**
 * Session Store
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/5 23:08
 */
public class SessionStore implements SafeCloseable {

    private static final String COMMAND_TYPE = "exec";

    private static final String SHELL_TYPE = "shell";

    private static final String SFTP_TYPE = "sftp";

    private static final String AUTH_FAIL_MESSAGE = "auth fail";

    /**
     * session
     */
    protected final Session session;

    public SessionStore(Session session) {
        this.session = session;
    }

    /**
     * 设置属性
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public SessionStore config(String key, String value) {
        session.setConfig(key, value);
        return this;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     * @return this
     */
    public SessionStore password(String password) {
        session.setPassword(password);
        return this;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     * @return this
     */
    public SessionStore password(byte[] password) {
        session.setPassword(password);
        return this;
    }

    public SessionStore httpProxy(String host, int port) {
        return this.proxy(SessionProxyType.HTTP, host, port, null, null);
    }

    public SessionStore httpProxy(String host, int port, String username, String password) {
        return this.proxy(SessionProxyType.HTTP, host, port, username, password);
    }

    public SessionStore socks4Proxy(String host, int port) {
        return this.proxy(SessionProxyType.SOCKS4, host, port, null, null);
    }

    public SessionStore socks4Proxy(String host, int port, String username, String password) {
        return this.proxy(SessionProxyType.SOCKS4, host, port, username, password);
    }

    public SessionStore socks5Proxy(String host, int port) {
        return this.proxy(SessionProxyType.SOCKS5, host, port, null, null);
    }

    public SessionStore socks5Proxy(String host, int port, String username, String password) {
        return this.proxy(SessionProxyType.SOCKS5, host, port, username, password);
    }

    public SessionStore proxy(SessionProxyType type, String host, int port) {
        return this.proxy(type, host, port, null, null);
    }

    /**
     * 设置代理
     *
     * @param type     代理类型
     * @param host     代理地址
     * @param port     代理端口
     * @param username 代理用户名
     * @param password 代理密码
     * @return this
     */
    public SessionStore proxy(SessionProxyType type,
                              String host, int port,
                              String username, String password) {
        Proxy proxy = null;
        if (SessionProxyType.HTTP.equals(type)) {
            proxy = new ProxyHTTP(host, port);
            if (!Strings.isBlank(username)) {
                ((ProxyHTTP) proxy).setUserPasswd(username, password);
            }
        } else if (SessionProxyType.SOCKS4.equals(type)) {
            proxy = new ProxySOCKS4(host, port);
            if (!Strings.isBlank(username)) {
                ((ProxySOCKS4) proxy).setUserPasswd(username, password);
            }
        } else if (SessionProxyType.SOCKS5.equals(type)) {
            proxy = new ProxySOCKS5(host, port);
            if (!Strings.isBlank(username)) {
                ((ProxySOCKS5) proxy).setUserPasswd(username, password);
            }
        }
        // 设置代理
        if (proxy != null) {
            session.setProxy(proxy);
        }
        return this;
    }

    /**
     * 设置客户端版本
     *
     * @param version 版本
     * @return this
     */
    public SessionStore clientVersion(String version) {
        session.setClientVersion(version);
        return this;
    }

    /**
     * 设置是否守护进程
     *
     * @param daemon true守护进程
     * @return this
     */
    public SessionStore daemonThread(boolean daemon) {
        session.setDaemonThread(daemon);
        return this;
    }

    /**
     * 获取配置信息
     *
     * @param key key
     * @return value
     */
    public String getConfig(String key) {
        return session.getConfig(key);
    }

    /**
     * 设置配置信息
     *
     * @param key   key
     * @param value value
     */
    public void setConfig(String key, String value) {
        session.setConfig(key, value);
    }

    /**
     * 设置连接超时时间
     *
     * @param timeout 超时时间
     * @return this
     */
    public SessionStore timeout(int timeout) {
        Valid.gte(timeout, 0, "the time must greater than or equal 0");
        try {
            session.setServerAliveInterval(timeout);
            session.setServerAliveCountMax(2);
            session.setTimeout(timeout);
        } catch (Exception e) {
            // impossible
            throw Exceptions.runtime(e);
        }
        return this;
    }

    /**
     * 建立连接
     *
     * @return this
     */
    public SessionStore connect() {
        return this.connect(session.getTimeout());
    }

    /**
     * 建立连接
     *
     * @param timeout 超时时间
     * @return this
     */
    public SessionStore connect(int timeout) {
        try {
            session.connect(timeout);
        } catch (Exception e) {
            if (Strings.def(e.getMessage()).contains(AUTH_FAIL_MESSAGE)) {
                // 认证失败
                throw Exceptions.authentication(e);
            } else {
                throw Exceptions.connection(e);
            }
        }
        return this;
    }

    public CommandExecutor getCommandExecutor(String command) {
        return this.getCommandExecutor(Strings.bytes(command, Const.UTF_8));
    }

    public CommandExecutor getCommandExecutor(String command, String charset) {
        return this.getCommandExecutor(Strings.bytes(command, charset));
    }

    /**
     * 获取 CommandExecutor
     *
     * @param command 命令
     * @return CommandExecutor
     */
    public CommandExecutor getCommandExecutor(byte[] command) {
        try {
            return new CommandExecutor((ChannelExec) session.openChannel(COMMAND_TYPE), command);
        } catch (JSchException e) {
            throw Exceptions.state("could not open channel", e);
        }
    }

    /**
     * 获取 ShellExecutor
     *
     * @return ShellExecutor
     */
    public ShellExecutor getShellExecutor() {
        try {
            return new ShellExecutor((ChannelShell) session.openChannel(SHELL_TYPE));
        } catch (JSchException e) {
            throw Exceptions.state("could not open channel", e);
        }
    }

    public SftpExecutor getSftpExecutor() {
        return this.getSftpExecutor(Const.UTF_8);
    }

    /**
     * 获取 SftpExecutor
     *
     * @param fileNameCharset 文件名称编码
     * @return SftpExecutor
     */
    public SftpExecutor getSftpExecutor(String fileNameCharset) {
        try {
            return new SftpExecutor((ChannelSftp) session.openChannel(SFTP_TYPE), fileNameCharset);
        } catch (JSchException e) {
            throw Exceptions.state("could not open channel", e);
        }
    }

    /**
     * 关闭连接
     */
    public void disconnect() {
        session.disconnect();
    }

    /**
     * @return 是否已建立连接
     */
    public boolean isConnected() {
        return session.isConnected();
    }

    /**
     * 获取 session
     *
     * @return Session
     */
    public Session getSession() {
        return session;
    }

    public String getHost() {
        return session.getHost();
    }

    public void setHost(String host) {
        session.setHost(host);
    }

    public int getPort() {
        return session.getPort();
    }

    public void setPort(int port) {
        session.setPort(port);
    }

    public String getUsername() {
        return session.getUserName();
    }

    @Override
    public void close() {
        if (this.isConnected()) {
            this.disconnect();
        }
    }

}
