package com.orion.remote.channel;

import com.jcraft.jsch.*;
import com.orion.able.SafeCloseable;
import com.orion.constant.Const;
import com.orion.remote.channel.sftp.SftpExecutor;
import com.orion.remote.channel.ssh.CommandExecutor;
import com.orion.remote.channel.ssh.ShellExecutor;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.Valid;
import com.orion.utils.io.Streams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Session Store
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/5 23:08
 */
public class SessionStore implements SafeCloseable {

    /**
     * session
     */
    private Session session;

    /**
     * host
     */
    private String host;

    /**
     * port
     */
    private int port;

    /**
     * username
     */
    private String username;

    public SessionStore(String host, String username) {
        this(host, 22, username);
    }

    public SessionStore(String host, int port, String username) {
        this.host = host;
        this.port = port;
        this.username = username;
        try {
            this.session = SessionHolder.CH.getSession(username, host, port);
        } catch (Exception e) {
            throw Exceptions.connection(e);
        }
    }

    /**
     * 设置属性
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public SessionStore setConfig(String key, String value) {
        session.setConfig(key, value);
        return this;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     * @return this
     */
    public SessionStore setPassword(String password) {
        session.setPassword(password);
        return this;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     * @return this
     */
    public SessionStore setPassword(byte[] password) {
        session.setPassword(password);
        return this;
    }

    /**
     * 设置超时时间
     *
     * @param timeout 超时时间
     * @return this
     */
    public SessionStore setTimeout(int timeout) {
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

    public SessionStore setHttpProxy(String host, int port) {
        session.setProxy(new ProxyHTTP(host, port));
        return this;
    }

    /**
     * 设置http代理
     *
     * @param host     主机
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return this
     */
    public SessionStore setHttpProxy(String host, int port, String username, String password) {
        ProxyHTTP proxy = new ProxyHTTP(host, port);
        if (!Strings.isBlank(username)) {
            proxy.setUserPasswd(username, password);
        }
        session.setProxy(proxy);
        return this;
    }

    public SessionStore setSocket4Proxy(String host, int port) {
        session.setProxy(new ProxySOCKS4(host, port));
        return this;
    }

    /**
     * 设置socket4代理
     *
     * @param host     主机
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return this
     */
    public SessionStore setSocket4Proxy(String host, int port, String username, String password) {
        ProxySOCKS4 proxy = new ProxySOCKS4(host, port);
        if (!Strings.isBlank(username)) {
            proxy.setUserPasswd(username, password);
        }
        session.setProxy(proxy);
        return this;
    }

    public SessionStore setSocket5Proxy(String host, int port) {
        session.setProxy(new ProxySOCKS5(host, port));
        return this;
    }

    /**
     * 设置socket5代理
     *
     * @param host     主机
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return this
     */
    public SessionStore setSocket5Proxy(String host, int port, String username, String password) {
        ProxySOCKS5 proxy = new ProxySOCKS5(host, port);
        if (!Strings.isBlank(username)) {
            proxy.setUserPasswd(username, password);
        }
        session.setProxy(proxy);
        return this;
    }

    /**
     * 设置客户端版本
     *
     * @param version 版本
     * @return this
     */
    public SessionStore setClientVersion(String version) {
        session.setClientVersion(version);
        return this;
    }

    /**
     * 设置是否守护进程
     *
     * @param daemon true守护进程
     * @return this
     */
    public SessionStore setDaemonThread(boolean daemon) {
        session.setDaemonThread(daemon);
        return this;
    }

    /**
     * 建立连接
     *
     * @return this
     */
    public SessionStore connect() {
        try {
            session.connect();
        } catch (Exception e) {
            throw Exceptions.connection(e);
        }
        return this;
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
            boolean authFail = Strings.def(e.getMessage()).contains("auth fail");
            if (authFail) {
                throw Exceptions.authentication(e);
            } else {
                throw Exceptions.connection(e);
            }
        }
        return this;
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
            return new CommandExecutor((ChannelExec) session.openChannel("exec"), command);
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
            return new ShellExecutor((ChannelShell) session.openChannel("shell"));
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
            return new SftpExecutor((ChannelSftp) session.openChannel("sftp"), fileNameCharset);
        } catch (JSchException e) {
            throw Exceptions.state("could not open channel", e);
        }
    }

    public static String getCommandOutputResultString(CommandExecutor executor) throws IOException {
        return new String(getCommandOutputResult(executor));
    }

    /**
     * 同步获取数据
     *
     * @param executor executor
     * @return result
     * @throws IOException IOException
     */
    public static byte[] getCommandOutputResult(CommandExecutor executor) throws IOException {
        Valid.notNull(executor, "command executor is null");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            executor.sync()
                    .transfer(out)
                    .exec();
            return out.toByteArray();
        } finally {
            Streams.close(out);
        }
    }

    /**
     * 获取session
     *
     * @return Session
     */
    public Session getSession() {
        return session;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void close() {
        if (this.isConnected()) {
            this.disconnect();
        }
    }

}
