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
package cn.orionsec.kit.net.ssh;

import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.net.ssh.command.SshCommandExecutor;
import cn.orionsec.kit.net.ssh.sftp.SftpExecutor;
import cn.orionsec.kit.net.ssh.shell.SshShellExecutor;
import com.jcraft.jsch.Session;

import java.io.File;
import java.io.InputStream;

/**
 * SSH 客户端委托
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/9/15 11:05
 */
public abstract class SshClientDelegate implements ISshClient {

    protected final ISshClient delegate;

    protected SshClientDelegate(ISshClient delegate) {
        Assert.notNull(delegate, "delegate is null");
        this.delegate = delegate;
    }

    @Override
    public ISshClient password(byte[] password) {
        delegate.password(password);
        return this;
    }

    @Override
    public ISshClient password(String password) {
        delegate.password(password);
        return this;
    }

    @Override
    public ISshClient identity(File privateKey) {
        delegate.identity(privateKey);
        return this;
    }

    @Override
    public ISshClient identity(File privateKey, String passphrase) {
        delegate.identity(privateKey, passphrase);
        return this;
    }

    @Override
    public ISshClient identity(File privateKey, File publicKey, String passphrase) {
        delegate.identity(privateKey, publicKey, passphrase);
        return this;
    }

    @Override
    public ISshClient identity(String publicKeyValue, String privateKeyValue) {
        delegate.identity(publicKeyValue, privateKeyValue);
        return this;
    }

    @Override
    public ISshClient identity(String publicKey, String privateKey, String passphrase) {
        delegate.identity(publicKey, privateKey, passphrase);
        return this;
    }

    @Override
    public ISshClient knownHosts(File file) {
        delegate.knownHosts(file);
        return this;
    }

    @Override
    public ISshClient knownHosts(String value) {
        delegate.knownHosts(value);
        return this;
    }

    @Override
    public ISshClient knownHosts(InputStream in) {
        delegate.knownHosts(in);
        return this;
    }

    @Override
    public ISshClient timeout(int timeout) {
        delegate.timeout(timeout);
        return this;
    }

    @Override
    public ISshClient config(String key, String value) {
        delegate.config(key, value);
        return this;
    }

    @Override
    public ISshClient logger(SessionLogger logger) {
        delegate.logger(logger);
        return this;
    }

    @Override
    public ISshClient httpProxy(String host, int port) {
        delegate.httpProxy(host, port);
        return this;
    }

    @Override
    public ISshClient httpProxy(String host, int port, String username, String password) {
        delegate.httpProxy(host, port, username, password);
        return this;
    }

    @Override
    public ISshClient socks4Proxy(String host, int port) {
        delegate.socks4Proxy(host, port);
        return this;
    }

    @Override
    public ISshClient socks4Proxy(String host, int port, String username, String password) {
        delegate.socks4Proxy(host, port, username, password);
        return this;
    }

    @Override
    public ISshClient socks5Proxy(String host, int port) {
        delegate.socks5Proxy(host, port);
        return this;
    }

    @Override
    public ISshClient socks5Proxy(String host, int port, String username, String password) {
        delegate.socks5Proxy(host, port, username, password);
        return this;
    }

    @Override
    public ISshClient proxy(SessionProxyType type, String host, int port) {
        delegate.proxy(type, host, port);
        return this;
    }

    @Override
    public ISshClient proxy(SessionProxyType type, String host, int port, String username, String password) {
        delegate.proxy(type, host, port, username, password);
        return this;
    }

    @Override
    public ISshClient clientVersion(String version) {
        delegate.clientVersion(version);
        return this;
    }

    @Override
    public ISshClient daemonThread(boolean daemon) {
        delegate.daemonThread(daemon);
        return this;
    }

    @Override
    public int setPortForwardingL(String host, int rport) {
        return delegate.setPortForwardingL(host, rport);
    }

    @Override
    public int setPortForwardingL(int lport, String host, int rport) {
        return delegate.setPortForwardingL(lport, host, rport);
    }

    @Override
    public ISshClient connect() {
        delegate.connect();
        return this;
    }

    @Override
    public ISshClient connect(int timeout) {
        delegate.connect(timeout);
        return this;
    }

    @Override
    public SshCommandExecutor getCommandExecutor(String command) {
        return delegate.getCommandExecutor(command);
    }

    @Override
    public SshCommandExecutor getCommandExecutor(String command, String charset) {
        return delegate.getCommandExecutor(command, charset);
    }

    @Override
    public SshCommandExecutor getCommandExecutor(byte[] command) {
        return delegate.getCommandExecutor(command);
    }

    @Override
    public SshShellExecutor getShellExecutor() {
        return delegate.getShellExecutor();
    }

    @Override
    public SftpExecutor getSftpExecutor() {
        return delegate.getSftpExecutor();
    }

    @Override
    public SftpExecutor getSftpExecutor(String fileNameCharset) {
        return delegate.getSftpExecutor(fileNameCharset);
    }

    @Override
    public void disconnect() {
        delegate.disconnect();
    }

    @Override
    public boolean isConnected() {
        return delegate.isConnected();
    }

    @Override
    public Session getSession() {
        return delegate.getSession();
    }

    @Override
    public String getHost() {
        return delegate.getHost();
    }

    @Override
    public int getPort() {
        return delegate.getPort();
    }

    @Override
    public String getUsername() {
        return delegate.getUsername();
    }

}
