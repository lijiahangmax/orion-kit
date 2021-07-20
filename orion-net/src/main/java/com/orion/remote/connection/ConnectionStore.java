package com.orion.remote.connection;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.ConnectionInfo;
import ch.ethz.ssh2.HTTPProxyData;
import ch.ethz.ssh2.SFTPv3Client;
import ch.ethz.ssh2.log.Logger;
import com.orion.constant.Const;
import com.orion.remote.connection.scp.ScpExecutor;
import com.orion.remote.connection.sftp.SftpExecutor;
import com.orion.remote.connection.ssh.CommandExecutor;
import com.orion.remote.connection.ssh.ShellExecutor;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.io.Streams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * 远程会话连接
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/15 21:23
 */
public class ConnectionStore implements AutoCloseable {

    /**
     * 连接对象
     */
    private Connection connection;

    /**
     * host
     */
    private String host;

    /**
     * port
     */
    private int port;

    /**
     * 是否认证
     */
    private boolean auth;

    /**
     * 连接属性
     */
    private ConnectionInfo info;

    public ConnectionStore(String host) {
        this(host, 22, null);
    }

    public ConnectionStore(String host, int port) {
        this(host, port, null);
    }

    public ConnectionStore(String host, int port, String proxyHost, int proxyPort) {
        this(host, port, new HTTPProxyData(proxyHost, proxyPort));
    }

    public ConnectionStore(String host, int port, String proxyHost, int proxyPort, String proxyUser, String proxyPassword) {
        this(host, port, new HTTPProxyData(proxyHost, proxyPort, proxyUser, proxyPassword));
    }

    private ConnectionStore(String host, int port, HTTPProxyData proxy) {
        this.host = host;
        this.port = port;
        this.connection = new Connection(host, port, proxy);
        try {
            this.info = this.connection.connect();
        } catch (IOException e) {
            throw Exceptions.connection(e);
        }
    }

    /**
     * 开启日志
     */
    public static void enableLogger() {
        Logger.enabled = true;
    }

    /**
     * 关闭日志
     */
    public static void disableLogger() {
        Logger.enabled = false;
    }

    public ConnectionStore auth(String username) {
        return this.auth(username, null, null, null);
    }

    public ConnectionStore auth(String username, String password) {
        return this.auth(username, password, null, null);
    }

    public ConnectionStore auth(String username, File pemKeyFile, String password) {
        return this.auth(username, password, pemKeyFile, null);
    }

    public ConnectionStore auth(String username, char[] pemKey, String password) {
        return this.auth(username, password, null, pemKey);
    }

    /**
     * 认证
     *
     * @param username   username
     * @param password   用户名密码或私钥密码
     * @param pemKeyFile 私钥路径
     * @param pemKey     私钥
     * @return this
     */
    private ConnectionStore auth(String username, String password, File pemKeyFile, char[] pemKey) {
        try {
            boolean authenticate;
            if (password == null) {
                authenticate = connection.authenticateWithNone(username);
            } else if (pemKeyFile != null) {
                authenticate = connection.authenticateWithPublicKey(username, pemKeyFile, password);
            } else if (pemKey != null) {
                authenticate = connection.authenticateWithPublicKey(username, pemKey, password);
            } else {
                authenticate = connection.authenticateWithPassword(username, password);
            }
            if (!authenticate) {
                throw Exceptions.authentication("authentication failure username: " + username);
            } else {
                this.auth = true;
            }
        } catch (Exception e) {
            throw Exceptions.authentication("authenticate command is error", e);
        }
        return this;
    }

    public CommandExecutor getCommandExecutor(String command) {
        return this.getCommandExecutor(command, Const.UTF_8);
    }

    /**
     * 获取 CommandExecutor
     *
     * @param command        命令
     * @param commandCharset 命令编码格式
     * @return CommandExecutor
     */
    public CommandExecutor getCommandExecutor(String command, String commandCharset) {
        Valid.isTrue(this.auth, "unauthorized");
        try {
            return new CommandExecutor(connection.openSession(), command, commandCharset);
        } catch (IOException e) {
            throw Exceptions.connection("could not be open session", e);
        }
    }

    /**
     * 获取 ShellExecutor
     *
     * @return ShellExecutor
     */
    public ShellExecutor getShellExecutor() {
        Valid.isTrue(this.auth, "unauthorized");
        try {
            return new ShellExecutor(connection.openSession());
        } catch (IOException e) {
            throw Exceptions.connection("could not be open session", e);
        }
    }

    public SftpExecutor getSftpExecutor() {
        return this.getSftpExecutor(Const.UTF_8);
    }

    /**
     * 获取 SftpExecutor
     *
     * @param charset 文件名称编码
     * @return SftpExecutor
     */
    public SftpExecutor getSftpExecutor(String charset) {
        Valid.isTrue(this.auth, "unauthorized");
        try {
            return new SftpExecutor(new SFTPv3Client(this.connection), charset);
        } catch (IOException e) {
            throw Exceptions.connection("could not be open sftp client", e);
        }
    }

    /**
     * 获取 ScpExecutor
     *
     * @return ScpExecutor
     */
    public ScpExecutor getScpExecutor() {
        Valid.isTrue(this.auth, "unauthorized");
        try {
            return new ScpExecutor(this.connection.createSCPClient());
        } catch (IOException e) {
            throw Exceptions.connection("could not be open scp client", e);
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

    @Override
    public void close() {
        connection.close();
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean isAuth() {
        return auth;
    }

    public Connection getConnection() {
        return connection;
    }

    public ConnectionInfo getInfo() {
        return info;
    }

}
