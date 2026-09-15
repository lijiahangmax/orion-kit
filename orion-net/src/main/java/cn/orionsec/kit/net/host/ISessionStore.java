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
import cn.orionsec.kit.net.host.sftp.SftpExecutor;
import cn.orionsec.kit.net.host.ssh.command.CommandExecutor;
import cn.orionsec.kit.net.host.ssh.shell.ShellExecutor;
import com.jcraft.jsch.Session;

import java.io.File;
import java.io.InputStream;

/**
 * SSH 会话 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/9/15 10:32
 */
public interface ISessionStore extends SafeCloseable {

    int DEFAULT_SSH_PORT = 22;

    /**
     * 设置密码
     *
     * @param password 密码
     * @return this
     */
    ISessionStore password(byte[] password);

    /**
     * 设置密码
     *
     * @param password 密码
     * @return this
     */
    ISessionStore password(String password);

    /**
     * 添加私钥认证
     *
     * @param privateKey 私钥文件
     * @return this
     */
    ISessionStore identity(File privateKey);

    /**
     * 添加私钥认证
     *
     * @param privateKey 私钥文件
     * @param passphrase 私钥口令
     * @return this
     */
    ISessionStore identity(File privateKey, String passphrase);

    /**
     * 添加私钥认证
     *
     * @param privateKey 私钥文件
     * @param publicKey  公钥文件
     * @param passphrase 私钥口令
     * @return this
     */
    ISessionStore identity(File privateKey, File publicKey, String passphrase);

    /**
     * 添加私钥认证
     *
     * @param publicKeyValue  公钥文本
     * @param privateKeyValue 私钥文本
     * @return this
     */
    ISessionStore identity(String publicKeyValue, String privateKeyValue);

    /**
     * 添加私钥认证
     *
     * @param publicKey  公钥文本
     * @param privateKey 私钥文本
     * @param passphrase 私钥口令
     * @return this
     */
    ISessionStore identity(String publicKey, String privateKey, String passphrase);

    /**
     * 设置已知主机
     *
     * @param file 文件
     * @return this
     */
    ISessionStore knownHosts(File file);

    /**
     * 设置已知主机
     *
     * @param value 文本
     * @return this
     */
    ISessionStore knownHosts(String value);

    /**
     * 设置已知主机
     *
     * @param in 文件流
     * @return this
     */
    ISessionStore knownHosts(InputStream in);

    /**
     * 设置超时时间
     *
     * @param timeout 超时时间 ms
     * @return this
     */
    ISessionStore timeout(int timeout);

    /**
     * 设置属性
     *
     * @param key   key
     * @param value value
     * @return this
     */
    ISessionStore config(String key, String value);

    /**
     * 设置日志等级
     *
     * @param logger 日志等级
     * @return this
     */
    ISessionStore logger(SessionLogger logger);

    /**
     * 设置 HTTP 代理
     *
     * @param host 代理地址
     * @param port 代理端口
     * @return this
     */
    ISessionStore httpProxy(String host, int port);

    /**
     * 设置 HTTP 代理
     *
     * @param host     代理地址
     * @param port     代理端口
     * @param username 代理用户名
     * @param password 代理密码
     * @return this
     */
    ISessionStore httpProxy(String host, int port, String username, String password);

    /**
     * 设置 SOCKS4 代理
     *
     * @param host 代理地址
     * @param port 代理端口
     * @return this
     */
    ISessionStore socks4Proxy(String host, int port);

    /**
     * 设置 SOCKS4 代理
     *
     * @param host     代理地址
     * @param port     代理端口
     * @param username 代理用户名
     * @param password 代理密码
     * @return this
     */
    ISessionStore socks4Proxy(String host, int port, String username, String password);

    /**
     * 设置 SOCKS5 代理
     *
     * @param host 代理地址
     * @param port 代理端口
     * @return this
     */
    ISessionStore socks5Proxy(String host, int port);

    /**
     * 设置 SOCKS5 代理
     *
     * @param host     代理地址
     * @param port     代理端口
     * @param username 代理用户名
     * @param password 代理密码
     * @return this
     */
    ISessionStore socks5Proxy(String host, int port, String username, String password);

    /**
     * 设置代理
     *
     * @param type 代理类型
     * @param host 代理地址
     * @param port 代理端口
     * @return this
     */
    ISessionStore proxy(SessionProxyType type, String host, int port);

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
    ISessionStore proxy(SessionProxyType type, String host, int port, String username, String password);

    /**
     * 设置客户端版本
     *
     * @param version 版本
     * @return this
     */
    ISessionStore clientVersion(String version);

    /**
     * 设置是否守护进程
     *
     * @param daemon true守护进程
     * @return this
     */
    ISessionStore daemonThread(boolean daemon);

    /**
     * 创建本地端口转发
     *
     * @param host  远程主机
     * @param rport 远程端口
     * @return 本地绑定的端口
     */
    int setPortForwardingL(String host, int rport);

    /**
     * 创建本地端口转发
     *
     * @param lport 本地端口 0为随机端口
     * @param host  远程主机
     * @param rport 远程端口
     * @return 本地绑定的端口
     */
    int setPortForwardingL(int lport, String host, int rport);

    /**
     * 建立连接
     *
     * @return this
     */
    ISessionStore connect();

    /**
     * 建立连接
     *
     * @param timeout 超时时间 ms
     * @return this
     */
    ISessionStore connect(int timeout);

    /**
     * 获取 CommandExecutor
     *
     * @param command 命令
     * @return CommandExecutor
     */
    CommandExecutor getCommandExecutor(String command);

    /**
     * 获取 CommandExecutor
     *
     * @param command 命令
     * @param charset 编码
     * @return CommandExecutor
     */
    CommandExecutor getCommandExecutor(String command, String charset);

    /**
     * 获取 CommandExecutor
     *
     * @param command 命令
     * @return CommandExecutor
     */
    CommandExecutor getCommandExecutor(byte[] command);

    /**
     * 获取 ShellExecutor
     *
     * @return ShellExecutor
     */
    ShellExecutor getShellExecutor();

    /**
     * 获取 SftpExecutor
     *
     * @return SftpExecutor
     */
    SftpExecutor getSftpExecutor();

    /**
     * 获取 SftpExecutor
     *
     * @param fileNameCharset 文件名称编码
     * @return SftpExecutor
     */
    SftpExecutor getSftpExecutor(String fileNameCharset);

    /**
     * 断开连接
     */
    void disconnect();

    /**
     * @return 是否已建立连接
     */
    boolean isConnected();

    /**
     * 获取 session
     *
     * @return Session
     */
    Session getSession();

    /**
     * @return 主机
     */
    String getHost();

    /**
     * @return 端口
     */
    int getPort();

    /**
     * @return 用户名
     */
    String getUsername();

}
