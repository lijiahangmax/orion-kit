package com.orion.net.remote.channel;

import com.jcraft.jsch.Identity;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Logger;
import com.orion.lang.utils.Exceptions;
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

    public static final SessionHolder HOLDER;

    public static final int DEFAULT_SSH_PORT = 22;

    public final JSch ch;

    public SessionHolder(JSch ch) {
        Valid.notNull(ch, "jsch is null");
        this.ch = ch;
    }

    static {
        HOLDER = new SessionHolder(new JSch());
        JSch.setConfig("StrictHostKeyChecking", "no");
    }

    public static SessionHolder getHolder() {
        return HOLDER;
    }

    /**
     * 设置日志等级
     */
    public static void setLogger(SessionLogger logger) {
        int loggerLevel = logger.getLevel();
        JSch.setLogger(new Logger() {
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
     * 获取配置信息
     *
     * @param key key
     * @return value
     */
    public static String getConfig(String key) {
        return JSch.getConfig(key);
    }

    /**
     * 设置配置信息
     *
     * @param key   key
     * @param value value
     */
    public static void setConfig(String key, String value) {
        JSch.setConfig(key, value);
    }

    /**
     * 添加公钥登陆文件
     *
     * @param keyPath  公钥路径
     * @param password 公钥密码
     */
    public void addIdentity(String keyPath, String password) {
        Valid.notNull(password, "public key password is null");
        try {
            ch.addIdentity(keyPath, password);
        } catch (Exception e) {
            throw Exceptions.runtime("add identity error " + e.getMessage());
        }
    }

    /**
     * 添加公钥登陆文件
     *
     * @param keyPath  公钥路径
     * @param password 公钥密码
     */
    public void addIdentity(String keyPath, byte[] password) {
        Valid.notNull(password, "public key password is null");
        try {
            ch.addIdentity(keyPath, password);
        } catch (Exception e) {
            throw Exceptions.runtime("add identity error " + e.getMessage());
        }
    }

    /**
     * 添加公钥登陆文件
     *
     * @param keyPath 公钥路径
     */
    public void addIdentity(String keyPath) {
        try {
            ch.addIdentity(keyPath);
        } catch (Exception e) {
            throw Exceptions.runtime("add identity error " + e.getMessage());
        }
    }

    /**
     * 删除加载的公钥
     *
     * @param keyPath keyPath
     */
    public void removeIdentity(String keyPath) {
        Vector<?> identities = ch.getIdentityRepository().getIdentities();
        for (Object identity : identities) {
            if (identity instanceof Identity) {
                String key = ((Identity) identity).getName();
                if (Files1.getPath(key).equals(Files1.getPath(keyPath))) {
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
     * 删除所有加载的公钥
     */
    public void removeAllIdentity() {
        try {
            ch.removeAllIdentity();
        } catch (JSchException e) {
            throw Exceptions.runtime("remove all identity error " + e.getMessage());
        }
    }

    /**
     * 获取加载的公钥
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
