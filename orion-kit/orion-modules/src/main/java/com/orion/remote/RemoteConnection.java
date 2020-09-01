package com.orion.remote;

import ch.ethz.ssh2.*;
import com.orion.exception.AuthenticationException;
import com.orion.utils.Exceptions;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 远程会话连接
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/15 21:23
 */
public class RemoteConnection {

    private static final Map<String, Connection> STORE = new ConcurrentHashMap<>();

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

    public RemoteConnection(String host) {
        this.host = host;
        this.connection = new Connection(host);
        try {
            this.info = this.connection.connect();
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    private RemoteConnection(Connection c) {
        this.auth = true;
        this.connection = c;
        try {
            this.info = c.getConnectionInfo();
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 从store获取链接
     *
     * @param host host
     * @return connection
     */
    public static RemoteConnection getConnection(String host) {
        Connection connection = STORE.get(host);
        if (connection != null) {
            return new RemoteConnection(connection);
        }
        return new RemoteConnection(host);
    }

    /**
     * 认证
     *
     * @param username username
     * @param password password
     * @return this
     * @throws AuthenticationException If authentication fails
     */
    public RemoteConnection auth(String username, String password) throws AuthenticationException {
        return auth(username, password, null, null);
    }

    /**
     * 认证
     *
     * @param username username
     * @param password password
     * @param keyFile  公钥路径
     * @return this
     * @throws AuthenticationException If authentication fails
     */
    public RemoteConnection auth(String username, String password, File keyFile) throws AuthenticationException {
        return auth(username, password, keyFile, null);
    }

    /**
     * 认证
     *
     * @param username username
     * @param password password
     * @param key      公钥
     * @return this
     * @throws AuthenticationException If authentication fails
     */
    public RemoteConnection auth(String username, String password, char[] key) throws AuthenticationException {
        return auth(username, password, null, key);
    }

    /**
     * 认证
     *
     * @param username username
     * @param password password
     * @param keyFile  公钥路径
     * @return this
     * @throws AuthenticationException If authentication fails
     */
    private RemoteConnection auth(String username, String password, File keyFile, char[] key) throws AuthenticationException {
        try {
            boolean authenticate;
            if (password == null) {
                authenticate = this.connection.authenticateWithNone(username);
            } else if (keyFile != null) {
                authenticate = this.connection.authenticateWithPublicKey(username, keyFile, password);
            } else if (key != null) {
                authenticate = this.connection.authenticateWithPublicKey(username, key, password);
            } else {
                authenticate = this.connection.authenticateWithPassword(username, password);
            }
            if (!authenticate) {
                throw Exceptions.authentication("authentication failure username: " + username);
            } else {
                STORE.put(this.connection.getHostname(), this.connection);
                this.auth = true;
            }
        } catch (Exception e) {
            throw Exceptions.authentication("authenticate command is error", e);
        }
        return this;
    }

    /**
     * 获取SSHSession
     *
     * @return Session
     */
    public Session getSession() {
        if (this.auth) {
            try {
                return this.connection.openSession();
            } catch (IOException e) {
                throw Exceptions.ioRuntime(e);
            }
        } else {
            throw Exceptions.runtime("unauthorized");
        }
    }

    /**
     * 获取SCPClient
     *
     * @return SCPClient
     */
    public SCPClient getSCPClient() {
        if (this.auth) {
            try {
                return this.connection.createSCPClient();
            } catch (IOException e) {
                throw Exceptions.ioRuntime(e);
            }
        } else {
            throw Exceptions.runtime("unauthorized");
        }
    }

    /**
     * 获取SCPClient
     *
     * @return SCPClient
     */
    public SFTPv3Client getSFTPClient() {
        if (this.auth) {
            try {
                return new SFTPv3Client(this.connection);
            } catch (IOException e) {
                throw Exceptions.ioRuntime(e);
            }
        } else {
            throw Exceptions.runtime("unauthorized");
        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        STORE.remove(this.host);
        this.connection.close();
    }

    public boolean isAuth() {
        return auth;
    }

    public ConnectionInfo getInfo() {
        return info;
    }

}
