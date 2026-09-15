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

import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.net.host.sftp.SftpExecutor;
import cn.orionsec.kit.net.host.ssh.command.CommandExecutor;
import cn.orionsec.kit.net.host.ssh.shell.ShellExecutor;
import com.jcraft.jsch.Session;

import java.io.File;
import java.io.InputStream;

/**
 * 会话委托
 * <p>
 * 默认将所有操作委托给目标会话, 子类只需要重写需要扩展的方法
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/9/15 11:05
 */
public abstract class SessionStoreDelegate implements ISessionStore {

    protected final ISessionStore sessionStore;

    protected SessionStoreDelegate(ISessionStore sessionStore) {
        Assert.notNull(sessionStore, "sessionStore is null");
        this.sessionStore = sessionStore;
    }

    @Override
    public ISessionStore password(byte[] password) {
        sessionStore.password(password);
        return this;
    }

    @Override
    public ISessionStore password(String password) {
        sessionStore.password(password);
        return this;
    }

    @Override
    public ISessionStore identity(File privateKey) {
        sessionStore.identity(privateKey);
        return this;
    }

    @Override
    public ISessionStore identity(File privateKey, String passphrase) {
        sessionStore.identity(privateKey, passphrase);
        return this;
    }

    @Override
    public ISessionStore identity(File privateKey, File publicKey, String passphrase) {
        sessionStore.identity(privateKey, publicKey, passphrase);
        return this;
    }

    @Override
    public ISessionStore identity(String publicKeyValue, String privateKeyValue) {
        sessionStore.identity(publicKeyValue, privateKeyValue);
        return this;
    }

    @Override
    public ISessionStore identity(String publicKey, String privateKey, String passphrase) {
        sessionStore.identity(publicKey, privateKey, passphrase);
        return this;
    }

    @Override
    public ISessionStore knownHosts(File file) {
        sessionStore.knownHosts(file);
        return this;
    }

    @Override
    public ISessionStore knownHosts(String value) {
        sessionStore.knownHosts(value);
        return this;
    }

    @Override
    public ISessionStore knownHosts(InputStream in) {
        sessionStore.knownHosts(in);
        return this;
    }

    @Override
    public ISessionStore timeout(int timeout) {
        sessionStore.timeout(timeout);
        return this;
    }

    @Override
    public ISessionStore config(String key, String value) {
        sessionStore.config(key, value);
        return this;
    }

    @Override
    public ISessionStore logger(SessionLogger logger) {
        sessionStore.logger(logger);
        return this;
    }

    @Override
    public ISessionStore httpProxy(String host, int port) {
        sessionStore.httpProxy(host, port);
        return this;
    }

    @Override
    public ISessionStore httpProxy(String host, int port, String username, String password) {
        sessionStore.httpProxy(host, port, username, password);
        return this;
    }

    @Override
    public ISessionStore socks4Proxy(String host, int port) {
        sessionStore.socks4Proxy(host, port);
        return this;
    }

    @Override
    public ISessionStore socks4Proxy(String host, int port, String username, String password) {
        sessionStore.socks4Proxy(host, port, username, password);
        return this;
    }

    @Override
    public ISessionStore socks5Proxy(String host, int port) {
        sessionStore.socks5Proxy(host, port);
        return this;
    }

    @Override
    public ISessionStore socks5Proxy(String host, int port, String username, String password) {
        sessionStore.socks5Proxy(host, port, username, password);
        return this;
    }

    @Override
    public ISessionStore proxy(SessionProxyType type, String host, int port) {
        sessionStore.proxy(type, host, port);
        return this;
    }

    @Override
    public ISessionStore proxy(SessionProxyType type, String host, int port, String username, String password) {
        sessionStore.proxy(type, host, port, username, password);
        return this;
    }

    @Override
    public ISessionStore clientVersion(String version) {
        sessionStore.clientVersion(version);
        return this;
    }

    @Override
    public ISessionStore daemonThread(boolean daemon) {
        sessionStore.daemonThread(daemon);
        return this;
    }

    @Override
    public int setPortForwardingL(String host, int rport) {
        return sessionStore.setPortForwardingL(host, rport);
    }

    @Override
    public int setPortForwardingL(int lport, String host, int rport) {
        return sessionStore.setPortForwardingL(lport, host, rport);
    }

    @Override
    public ISessionStore connect() {
        sessionStore.connect();
        return this;
    }

    @Override
    public ISessionStore connect(int timeout) {
        sessionStore.connect(timeout);
        return this;
    }

    @Override
    public CommandExecutor getCommandExecutor(String command) {
        return sessionStore.getCommandExecutor(command);
    }

    @Override
    public CommandExecutor getCommandExecutor(String command, String charset) {
        return sessionStore.getCommandExecutor(command, charset);
    }

    @Override
    public CommandExecutor getCommandExecutor(byte[] command) {
        return sessionStore.getCommandExecutor(command);
    }

    @Override
    public ShellExecutor getShellExecutor() {
        return sessionStore.getShellExecutor();
    }

    @Override
    public SftpExecutor getSftpExecutor() {
        return sessionStore.getSftpExecutor();
    }

    @Override
    public SftpExecutor getSftpExecutor(String fileNameCharset) {
        return sessionStore.getSftpExecutor(fileNameCharset);
    }

    @Override
    public void disconnect() {
        sessionStore.disconnect();
    }

    @Override
    public boolean isConnected() {
        return sessionStore.isConnected();
    }

    @Override
    public Session getSession() {
        return sessionStore.getSession();
    }

    @Override
    public String getHost() {
        return sessionStore.getHost();
    }

    @Override
    public int getPort() {
        return sessionStore.getPort();
    }

    @Override
    public String getUsername() {
        return sessionStore.getUsername();
    }

    @Override
    public void close() {
        this.disconnect();
    }

}
