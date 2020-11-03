package com.orion.remote.channel;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Logger;
import com.orion.utils.Exceptions;

import java.io.InputStream;

/**
 * Session Factory
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/23 17:48
 */
public class SessionFactory {

    private SessionFactory() {
    }

    protected static final JSch CH = new JSch();

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
                return level <= loggerLevel;
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
        }
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
        }
    }

    /**
     * 获取一个 SessionStore
     *
     * @param username 用户名
     * @param host     主机
     * @return SessionStore
     */
    public static SessionStore getSession(String username, String host) {
        return new SessionStore(username, host);
    }

    /**
     * 获取一个 SessionStore
     *
     * @param username 用户名
     * @param host     主机
     * @param port     端口
     * @return SessionStore
     */
    public static SessionStore getSession(String username, String host, int port) {
        return new SessionStore(username, host, port);
    }

    /**
     * 关闭session
     *
     * @param store SessionStore
     */
    public static void disconnectSession(SessionStore store) {
        store.disconnect();
    }

}
