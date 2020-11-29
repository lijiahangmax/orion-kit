package com.orion.remote.connection;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.ConnectionInfo;
import ch.ethz.ssh2.HTTPProxyData;
import ch.ethz.ssh2.SFTPv3Client;
import com.orion.remote.connection.scp.ScpExecutor;
import com.orion.remote.connection.sftp.SftpExecutor;
import com.orion.remote.connection.sftp.bigfile.SftpDownload;
import com.orion.remote.connection.sftp.bigfile.SftpUpload;
import com.orion.remote.connection.ssh.CommandExecutor;
import com.orion.remote.connection.ssh.ShellExecutor;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;

import java.io.File;
import java.io.IOException;

/**
 * 远程会话连接
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/15 21:23
 */
public class ConnectionStore {

    /**
     * 连接对象
     */
    private Connection connection;

    /**
     * host
     */
    private String host;

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
        this.connection = new Connection(host, port, proxy);
        try {
            this.info = this.connection.connect();
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public ConnectionStore auth(String username) {
        return auth(username, null, null, null);
    }

    public ConnectionStore auth(String username, String password) {
        return auth(username, password, null, null);
    }

    public ConnectionStore auth(String username, File pemKeyFile, String password) {
        return auth(username, password, pemKeyFile, null);
    }

    public ConnectionStore auth(String username, char[] pemKey, String password) {
        return auth(username, password, null, pemKey);
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
                authenticate = this.connection.authenticateWithNone(username);
            } else if (pemKeyFile != null) {
                authenticate = this.connection.authenticateWithPublicKey(username, pemKeyFile, password);
            } else if (pemKey != null) {
                authenticate = this.connection.authenticateWithPublicKey(username, pemKey, password);
            } else {
                authenticate = this.connection.authenticateWithPassword(username, password);
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

    /**
     * 获取 CommandExecutor
     *
     * @param command 命令
     * @return CommandExecutor
     */
    public CommandExecutor getCommandExecutor(String command) {
        Valid.isTrue(this.auth, "unauthorized");
        try {
            return new CommandExecutor(connection.openSession(), command);
        } catch (IOException e) {
            throw Exceptions.connection("could open session", e);
        }
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
            throw Exceptions.connection("could open session", e);
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
            throw Exceptions.connection("could open session", e);
        }
    }

    /**
     * 获取 ShellExecutor
     *
     * @param shellType shell类型
     * @return ShellExecutor
     */
    public ShellExecutor getShellExecutor(String shellType) {
        Valid.isTrue(this.auth, "unauthorized");
        try {
            return new ShellExecutor(connection.openSession(), shellType);
        } catch (IOException e) {
            throw Exceptions.connection("could open session", e);
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
            throw Exceptions.connection("could open scp client", e);
        }
    }

    /**
     * 获取 SftpExecutor
     *
     * @return SftpExecutor
     */
    public SftpExecutor getSftpExecutor() {
        Valid.isTrue(this.auth, "unauthorized");
        try {
            return new SftpExecutor(new SFTPv3Client(this.connection));
        } catch (IOException e) {
            throw Exceptions.connection("could open sftp client", e);
        }
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
            throw Exceptions.connection("could open sftp client", e);
        }
    }

    /**
     * 获取 SftpUpload
     *
     * @param remote 远程文件
     * @param local  本地文件
     * @return SftpUpload
     */
    public SftpUpload getSftpUpload(String remote, String local) {
        Valid.isTrue(this.auth, "unauthorized");
        try {
            return new SftpUpload(new SFTPv3Client(this.connection), remote, local);
        } catch (IOException e) {
            throw Exceptions.connection("could open sftp client", e);
        }
    }

    /**
     * 获取 SftpUpload
     *
     * @param remote 远程文件
     * @param local  本地文件
     * @return SftpUpload
     */
    public SftpUpload getSftpUpload(String remote, File local) {
        Valid.isTrue(this.auth, "unauthorized");
        try {
            return new SftpUpload(new SFTPv3Client(this.connection), remote, local);
        } catch (IOException e) {
            throw Exceptions.connection("could open sftp client", e);
        }
    }

    /**
     * 获取 SftpDownload
     *
     * @param remote 远程文件
     * @param local  本地文件
     * @return SftpDownload
     */
    public SftpDownload getSftpDownload(String remote, String local) {
        Valid.isTrue(this.auth, "unauthorized");
        try {
            return new SftpDownload(new SFTPv3Client(this.connection), remote, local);
        } catch (IOException e) {
            throw Exceptions.connection("could open sftp client", e);
        }
    }

    /**
     * 获取 SftpDownload
     *
     * @param remote 远程文件
     * @param local  本地文件
     * @return SftpDownload
     */
    public SftpDownload getSftpDownload(String remote, File local) {
        Valid.isTrue(this.auth, "unauthorized");
        try {
            return new SftpDownload(new SFTPv3Client(this.connection), remote, local);
        } catch (IOException e) {
            throw Exceptions.connection("could open sftp client", e);
        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        this.connection.close();
    }

    public String getHost() {
        return host;
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
