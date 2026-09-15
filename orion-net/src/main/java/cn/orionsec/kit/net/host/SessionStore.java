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

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.net.host.sftp.SftpExecutor;
import cn.orionsec.kit.net.host.ssh.command.CommandExecutor;
import cn.orionsec.kit.net.host.ssh.shell.ShellExecutor;
import com.jcraft.jsch.*;

import java.io.File;
import java.io.InputStream;

/**
 * SSH 会话
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/5 23:08
 */
public class SessionStore implements ISessionStore {

    private static final String COMMAND_TYPE = "exec";
    private static final String SHELL_TYPE = "shell";
    private static final String SFTP_TYPE = "sftp";

    private static final String AUTH_FAIL_MESSAGE = "auth fail";

    private final JSch ch;
    private final Session session;

    static {
        // 不检查私钥
        JSch.setConfig("StrictHostKeyChecking", "no");
        // add RSA/SHA1 key support
        JSch.setConfig("server_host_key", JSch.getConfig("server_host_key") + ",ssh-rsa");
        JSch.setConfig("PubkeyAcceptedAlgorithms", JSch.getConfig("PubkeyAcceptedAlgorithms") + ",ssh-rsa");
    }

    private SessionStore(String host, int port, String username) {
        Assert.notBlank(host, "host is blank");
        Assert.notBlank(username, "username is blank");
        this.ch = new JSch();
        try {
            this.session = ch.getSession(username, host, port);
        } catch (Exception e) {
            throw Exceptions.connection(e);
        }
    }

    public static SessionStore create(String host, String username) {
        return create(host, DEFAULT_SSH_PORT, username);
    }

    /**
     * 创建会话
     *
     * @param host     主机
     * @param port     端口
     * @param username 用户名
     * @return SessionStore
     */
    public static SessionStore create(String host, int port, String username) {
        return new SessionStore(host, port, username);
    }

    @Override
    public SessionStore password(byte[] password) {
        session.setPassword(password);
        return this;
    }

    @Override
    public SessionStore password(String password) {
        session.setPassword(password);
        return this;
    }

    @Override
    public SessionStore identity(File privateKey) {
        return this.identity(privateKey, null, null);
    }

    @Override
    public SessionStore identity(File privateKey, String passphrase) {
        return this.identity(privateKey, null, passphrase);
    }

    @Override
    public SessionStore identity(File privateKey, File publicKey, String passphrase) {
        Assert.notNull(privateKey, "private key is null");
        try {
            ch.addIdentity(privateKey.getAbsolutePath(),
                    publicKey == null ? null : publicKey.getAbsolutePath(),
                    passphrase == null ? null : Strings.bytes(passphrase));
        } catch (Exception e) {
            throw Exceptions.runtime("add identity error " + e.getMessage(), e);
        }
        return this;
    }

    @Override
    public SessionStore identity(String publicKeyValue, String privateKeyValue) {
        return this.identity(publicKeyValue, privateKeyValue, null);
    }

    @Override
    public SessionStore identity(String publicKey, String privateKey, String passphrase) {
        Assert.notNull(privateKey, "private key is null");
        try {
            ch.addIdentity(session.getHost(),
                    Strings.bytes(privateKey),
                    publicKey == null ? null : Strings.bytes(publicKey),
                    passphrase == null ? null : Strings.bytes(passphrase));
        } catch (Exception e) {
            throw Exceptions.runtime("add identity error " + e.getMessage(), e);
        }
        return this;
    }

    @Override
    public SessionStore knownHosts(File file) {
        Assert.notNull(file, "known hosts file is null");
        return this.knownHosts(Files1.openInputStreamSafe(file));
    }

    @Override
    public SessionStore knownHosts(String value) {
        Assert.notNull(value, "known hosts is null");
        return this.knownHosts(Streams.toInputStream(value));
    }

    @Override
    public SessionStore knownHosts(InputStream in) {
        try {
            ch.setKnownHosts(in);
        } catch (Exception e) {
            throw Exceptions.runtime("set unknown hosts error " + e.getMessage());
        }
        return this;
    }

    @Override
    public SessionStore timeout(int timeout) {
        Assert.gte(timeout, 0, "the time must greater than or equal 0");
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

    @Override
    public SessionStore config(String key, String value) {
        session.setConfig(key, value);
        return this;
    }

    @Override
    public SessionStore logger(SessionLogger logger) {
        int loggerLevel = logger.getLevel();
        ch.setInstanceLogger(new com.jcraft.jsch.Logger() {
            @Override
            public boolean isEnabled(int level) {
                return loggerLevel <= level;
            }

            @Override
            public void log(int level, String message) {
                SessionLogger.log(level, message);
            }
        });
        return this;
    }

    @Override
    public SessionStore httpProxy(String host, int port) {
        return this.proxy(SessionProxyType.HTTP, host, port, null, null);
    }

    @Override
    public SessionStore httpProxy(String host, int port, String username, String password) {
        return this.proxy(SessionProxyType.HTTP, host, port, username, password);
    }

    @Override
    public SessionStore socks4Proxy(String host, int port) {
        return this.proxy(SessionProxyType.SOCKS4, host, port, null, null);
    }

    @Override
    public SessionStore socks4Proxy(String host, int port, String username, String password) {
        return this.proxy(SessionProxyType.SOCKS4, host, port, username, password);
    }

    @Override
    public SessionStore socks5Proxy(String host, int port) {
        return this.proxy(SessionProxyType.SOCKS5, host, port, null, null);
    }

    @Override
    public SessionStore socks5Proxy(String host, int port, String username, String password) {
        return this.proxy(SessionProxyType.SOCKS5, host, port, username, password);
    }

    @Override
    public SessionStore proxy(SessionProxyType type, String host, int port) {
        return this.proxy(type, host, port, null, null);
    }

    @Override
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

    @Override
    public SessionStore clientVersion(String version) {
        session.setClientVersion(version);
        return this;
    }

    @Override
    public SessionStore daemonThread(boolean daemon) {
        session.setDaemonThread(daemon);
        return this;
    }

    @Override
    public int setPortForwardingL(String host, int rport) {
        return this.setPortForwardingL(0, host, rport);
    }

    @Override
    public int setPortForwardingL(int lport, String host, int rport) {
        this.checkConnected();
        try {
            return session.setPortForwardingL(lport, host, rport);
        } catch (JSchException e) {
            throw Exceptions.state("could not set port forwarding", e);
        }
    }

    @Override
    public SessionStore connect() {
        return this.connect(session.getTimeout());
    }

    @Override
    public SessionStore connect(int timeout) {
        Assert.gte(timeout, 0, "the time must greater than or equal 0");
        try {
            session.connect(timeout);
        } catch (Exception e) {
            if (Strings.def(e.getMessage()).toLowerCase().contains(AUTH_FAIL_MESSAGE)) {
                // 认证失败
                throw Exceptions.authentication(e);
            } else {
                throw Exceptions.connection(e);
            }
        }
        return this;
    }

    @Override
    public CommandExecutor getCommandExecutor(String command) {
        return this.getCommandExecutor(Strings.bytes(command, Const.UTF_8));
    }

    @Override
    public CommandExecutor getCommandExecutor(String command, String charset) {
        return this.getCommandExecutor(Strings.bytes(command, charset));
    }

    @Override
    public CommandExecutor getCommandExecutor(byte[] command) {
        this.checkConnected();
        try {
            return new CommandExecutor((ChannelExec) session.openChannel(COMMAND_TYPE), command);
        } catch (JSchException e) {
            throw Exceptions.state("could not open channel", e);
        }
    }

    @Override
    public ShellExecutor getShellExecutor() {
        this.checkConnected();
        try {
            return new ShellExecutor((ChannelShell) session.openChannel(SHELL_TYPE));
        } catch (JSchException e) {
            throw Exceptions.state("could not open channel", e);
        }
    }

    @Override
    public SftpExecutor getSftpExecutor() {
        return this.getSftpExecutor(Const.UTF_8);
    }

    @Override
    public SftpExecutor getSftpExecutor(String fileNameCharset) {
        this.checkConnected();
        try {
            return new SftpExecutor((ChannelSftp) session.openChannel(SFTP_TYPE), fileNameCharset);
        } catch (JSchException e) {
            throw Exceptions.state("could not open channel", e);
        }
    }

    /**
     * 检查是否已连接
     */
    private void checkConnected() {
        if (!this.isConnected()) {
            throw Exceptions.connection("session is not connected");
        }
    }

    @Override
    public void disconnect() {
        session.disconnect();
    }

    @Override
    public boolean isConnected() {
        return session.isConnected();
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public String getHost() {
        return session.getHost();
    }

    @Override
    public int getPort() {
        return session.getPort();
    }

    @Override
    public String getUsername() {
        return session.getUserName();
    }

    @Override
    public void close() {
        this.disconnect();
    }

}
