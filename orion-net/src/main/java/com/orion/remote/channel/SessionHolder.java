package com.orion.remote.channel;

import com.jcraft.jsch.Identity;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Logger;
import com.orion.utils.Exceptions;
import com.orion.utils.io.Files1;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Session Holder
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/23 17:48
 */
public class SessionHolder {

    private SessionHolder() {
    }

    public static final JSch CH = new JSch();

    static {
        JSch.setConfig("StrictHostKeyChecking", "no");
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
     * 添加秘钥登陆文件
     *
     * @param keyPath  文件路径
     * @param password 文件密码
     */
    public static void addIdentity(String keyPath, String password) {
        try {
            CH.addIdentity(keyPath, password);
        } catch (Exception e) {
            Exceptions.printStacks(e);
            throw Exceptions.runtime("add identity error " + e.getMessage());
        }
    }

    /**
     * 添加秘钥登陆文件
     *
     * @param keyPath  文件路径
     * @param password 文件密码
     */
    public static void addIdentity(String keyPath, byte[] password) {
        try {
            CH.addIdentity(keyPath, password);
        } catch (Exception e) {
            Exceptions.printStacks(e);
            throw Exceptions.runtime("add identity error " + e.getMessage());
        }
    }

    /**
     * 添加秘钥登陆文件
     *
     * @param keyPath 文件路径
     */
    public static void addIdentity(String keyPath) {
        try {
            CH.addIdentity(keyPath);
        } catch (Exception e) {
            Exceptions.printStacks(e);
            throw Exceptions.runtime("add identity error " + e.getMessage());
        }
    }

    /**
     * 删除加载的key
     *
     * @param keyPath keyPath
     */
    public static void removeIdentity(String keyPath) {
        Vector<?> identities = CH.getIdentityRepository().getIdentities();
        for (Object identity : identities) {
            if (identity instanceof Identity) {
                String key = ((Identity) identity).getName();
                if (Files1.getPath(key).equals(Files1.getPath(keyPath))) {
                    try {
                        CH.removeIdentity((Identity) identity);
                    } catch (Exception e) {
                        Exceptions.printStacks(e);
                        throw Exceptions.runtime("remove identity error " + e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * 删除所有加载的key
     */
    public static void removeAllIdentity() {
        try {
            CH.removeAllIdentity();
        } catch (JSchException e) {
            Exceptions.printStacks(e);
            throw Exceptions.runtime("remove all identity error " + e.getMessage());
        }
    }

    /**
     * 获取加载的key
     *
     * @return keys
     */
    public static List<String> getLoadKeys() {
        List<String> keys = new ArrayList<>();
        Vector<?> identities = CH.getIdentityRepository().getIdentities();
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
    public static void setKnownHosts(String filePath) {
        try {
            CH.setKnownHosts(filePath);
        } catch (Exception e) {
            Exceptions.printStacks(e);
            throw Exceptions.runtime("set unknown hosts error " + e.getMessage());
        }
    }

    /**
     * 设置已知主机
     *
     * @param inputStream 文件流
     */
    public static void setKnownHosts(InputStream inputStream) {
        try {
            CH.setKnownHosts(inputStream);
        } catch (Exception e) {
            Exceptions.printStacks(e);
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
    public static SessionStore getSession(String host, String username) {
        return new SessionStore(host, username);
    }

    /**
     * 获取一个 SessionStore
     *
     * @param username 用户名
     * @param host     主机
     * @param port     端口
     * @return SessionStore
     */
    public static SessionStore getSession(String host, int port, String username) {
        return new SessionStore(host, port, username);
    }

}
