package com.orion.net.host;

import com.jcraft.jsch.Identity;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Logger;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.Files1;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Session Holder
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/23 17:48
 */
public class SessionHolder {

    public static final int DEFAULT_SSH_PORT = 22;

    public final JSch ch;

    public SessionHolder() {
        this(new JSch());
    }

    public SessionHolder(JSch ch) {
        Valid.notNull(ch, "jsch is null");
        this.ch = ch;
    }

    static {
        // 不检查私钥
        JSch.setConfig("StrictHostKeyChecking", "no");
        // add RSA/SHA1 key support
        JSch.setConfig("server_host_key", JSch.getConfig("server_host_key") + ",ssh-rsa");
        JSch.setConfig("PubkeyAcceptedAlgorithms", JSch.getConfig("PubkeyAcceptedAlgorithms") + ",ssh-rsa");
    }

    /**
     * 创建
     *
     * @return SessionHolder
     */
    public static SessionHolder create() {
        return new SessionHolder();
    }

    /**
     * 设置日志等级
     */
    public void setLogger(SessionLogger logger) {
        int loggerLevel = logger.getLevel();
        ch.setInstanceLogger(new Logger() {
            @Override
            public boolean isEnabled(int level) {
                return loggerLevel <= level;
            }

            @Override
            public void log(int level, String message) {
                SessionLogger.log(level, message);
            }
        });
    }

    /**
     * 添加私钥认证 - 文件
     *
     * @param privateKeyPath 私钥路径
     */
    public void addIdentity(String privateKeyPath) {
        this.addIdentity(privateKeyPath, null, null);
    }

    /**
     * 添加私钥认证 - 文件
     *
     * @param privateKeyPath 私钥路径
     * @param password       私钥密码
     */
    public void addIdentity(String privateKeyPath, String password) {
        this.addIdentity(privateKeyPath, null, password);
    }

    /**
     * 添加私钥认证 - 文件
     *
     * @param privateKeyPath 私钥路径
     * @param publicKeyPath  公钥路径
     * @param password       私钥密码
     */
    public void addIdentity(String privateKeyPath, String publicKeyPath, String password) {
        Valid.notNull(privateKeyPath, "private key is null");
        try {
            ch.addIdentity(privateKeyPath,
                    publicKeyPath,
                    password == null ? null : Strings.bytes(password));
        } catch (Exception e) {
            throw Exceptions.runtime("add identity error " + e.getMessage(), e);
        }
    }

    /**
     * 添加私钥认证 - 文本
     *
     * @param keyName        名称
     * @param privateKeyPath 私钥文本
     */
    public void addIdentityValue(String keyName, String privateKeyValue) {
        this.addIdentityValue(keyName, privateKeyValue, null, null);
    }

    /**
     * 添加私钥认证 - 文本
     *
     * @param keyName        名称
     * @param privateKeyPath 私钥文本
     * @param password       私钥密码
     */
    public void addIdentityValue(String keyName, String privateKeyValue, String password) {
        this.addIdentityValue(keyName, privateKeyValue, null, password);
    }

    /**
     * 添加私钥认证 - 文本
     *
     * @param keyName        名称
     * @param privateKeyPath 私钥文本
     * @param publicKeyPath  公钥文本
     * @param password       私钥密码
     */
    public void addIdentityValue(String keyName, String privateKeyValue, String publicKeyValue, String password) {
        Valid.notNull(keyName, "key name is null");
        Valid.notNull(privateKeyValue, "private key is null");
        try {
            ch.addIdentity(keyName,
                    Strings.bytes(privateKeyValue),
                    publicKeyValue == null ? null : Strings.bytes(publicKeyValue),
                    password == null ? null : Strings.bytes(password));
        } catch (Exception e) {
            throw Exceptions.runtime("add identity error " + e.getMessage(), e);
        }
    }

    /**
     * 删除加载的秘钥
     *
     * @param key key
     */
    public void removeIdentity(String key) {
        Vector<?> identities = ch.getIdentityRepository().getIdentities();
        for (Object identity : identities) {
            if (identity instanceof Identity) {
                String keyName = ((Identity) identity).getName();
                if (keyName.equals(key) ||
                        Files1.getPath(keyName).equals(Files1.getPath(key))) {
                    try {
                        ch.removeIdentity((Identity) identity);
                    } catch (Exception e) {
                        throw Exceptions.runtime("remove identity error " + e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * 删除所有加载的秘钥
     */
    public void removeAllIdentity() {
        try {
            ch.removeAllIdentity();
        } catch (JSchException e) {
            throw Exceptions.runtime("remove all identity error " + e.getMessage());
        }
    }

    /**
     * 获取加载的秘钥
     *
     * @return keys
     */
    public List<String> getLoadKeys() {
        List<String> keys = new ArrayList<>();
        Vector<?> identities = ch.getIdentityRepository().getIdentities();
        for (Object identity : identities) {
            if (identity instanceof Identity) {
                keys.add(Files1.getPath(((Identity) identity).getName()));
            }
        }
        return keys;
    }

    /**
     * 设置已知主机
     *
     * @param filePath 文件路径
     */
    public void setKnownHosts(String filePath) {
        try {
            ch.setKnownHosts(filePath);
        } catch (Exception e) {
            throw Exceptions.runtime("set unknown hosts error " + e.getMessage());
        }
    }

    /**
     * 设置已知主机
     *
     * @param inputStream 文件流
     */
    public void setKnownHosts(InputStream inputStream) {
        try {
            ch.setKnownHosts(inputStream);
        } catch (Exception e) {
            throw Exceptions.runtime("set unknown hosts error " + e.getMessage());
        }
    }

    /**
     * 获取一个 SessionStore
     *
     * @param username 用户名
     * @param host     主机
     * @return SessionStore
     */
    public SessionStore getSession(String host, String username) {
        return this.getSession(host, DEFAULT_SSH_PORT, username);
    }

    /**
     * 获取一个 SessionStore
     *
     * @param username 用户名
     * @param host     主机
     * @param port     端口
     * @return SessionStore
     */
    public SessionStore getSession(String host, int port, String username) {
        try {
            return new SessionStore(ch.getSession(username, host, port));
        } catch (Exception e) {
            throw Exceptions.connection(e);
        }
    }

}
