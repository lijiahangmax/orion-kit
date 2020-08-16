package com.orion.ftp.client;

import com.orion.ftp.client.pool.FtpClientFactory;
import com.orion.ftp.client.pool.FtpClientPool;
import org.apache.commons.net.ftp.FTPClient;

/**
 * FTP工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/3/17 16:10
 */
public class Ftps {

    private Ftps() {
    }

    /**
     * FTP 配置
     */
    private static FtpConfig config;

    /**
     * FTP 工厂
     */
    private static FtpClientFactory factory;

    /**
     * 连接池对象
     */
    private static FtpClientPool pool;

    /**
     * 重新加载配置
     *
     * @param host     主机
     * @param username 用户名
     * @param password 密码
     * @throws InterruptedException 释放连接超时
     */
    public static void init(String host, String username, String password) throws InterruptedException {
        init(host, 21, username, password, null);
    }

    public static void init(String host, int port, String username, String password) throws InterruptedException {
        init(host, port, username, password, null);
    }

    /**
     * 重新加载配置
     *
     * @param host     主机
     * @param username 用户名
     * @param password 密码
     * @param homePath homePath
     * @throws InterruptedException 释放连接超时
     */
    public static void init(String host, String username, String password, String homePath) throws InterruptedException {
        init(host, 21, username, password, homePath);
    }

    /**
     * 重新加载配置
     *
     * @param host     主机
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @param homePath homePath
     * @throws InterruptedException 释放连接超时
     */
    public static void init(String host, int port, String username, String password, String homePath) throws InterruptedException {
        if (Ftps.pool != null) {
            Ftps.pool.close();
        }
        Ftps.config = new FtpConfig(host, port).auth(username, password);
        if (homePath != null) {
            Ftps.config.baseDir(homePath);
        }
        Ftps.factory = new FtpClientFactory(config);
        Ftps.pool = new FtpClientPool(factory);
    }

    /**
     * 重新加载配置
     *
     * @param config 配置项
     * @throws InterruptedException 释放连接超时
     */
    public static void init(FtpConfig config) throws InterruptedException {
        if (Ftps.pool != null) {
            Ftps.pool.close();
        }
        Ftps.config = config;
        Ftps.factory = new FtpClientFactory(config);
        Ftps.pool = new FtpClientPool(factory);
    }

    /**
     * 归还连接
     *
     * @param client client
     */
    private static void returnClient(FTPClient client) {
        if (pool != null) {
            pool.returnClient(client);
        } else {
            destroy(client);
        }
    }

    /**
     * 归还连接
     *
     * @param instance ftp实例
     */
    private static void returnClient(FtpInstance instance) {
        if (pool != null) {
            pool.returnClient(instance.client());
        } else {
            destroy(instance.client());
        }
    }

    /**
     * 从连接池中获取连接
     * 需要调用 returnClient 归还连接
     *
     * @return FTP 连接
     */
    private static FTPClient getClient() {
        if (pool == null) {
            if (factory == null) {
                return null;
            }
            return factory.getClient();
        }
        return pool.getClient();
    }

    /**
     * 从连接池中获取连接
     *
     * @param byPool 是否从连接池中获取
     * @return FTP 连接
     */
    private static FTPClient getClient(boolean byPool) {
        if (byPool) {
            if (pool == null) {
                return null;
            }
            return pool.getClient();
        } else {
            if (factory == null) {
                return null;
            }
            return factory.getClient();
        }
    }

    /**
     * 获取链接工厂
     *
     * @return 工厂
     */
    private static FtpClientFactory getFactory() {
        return factory;
    }

    /**
     * 获取FTP实例
     * 需要调用 returnClient 归还连接
     *
     * @return 实例
     */
    public static FtpInstance getInstance() {
        return getInstance(true, null);
    }

    /**
     * 获取FTP实例
     * 需要调用 returnClient 归还连接
     *
     * @param byPool 是否从连接池中获取
     * @return 实例
     */
    public static FtpInstance getInstance(boolean byPool) {
        return getInstance(byPool, null);
    }

    /**
     * 获取FTP实例
     * 需要调用 returnClient 归还连接
     *
     * @param config 配置信息
     * @return 实例
     */
    public FtpInstance getInstance(FtpConfig config) {
        return getInstance(true, config);
    }

    /**
     * 获取FTP实例
     * 需要调用 returnClient 归还连接
     *
     * @param byPool 是否从连接池中获取
     * @return 实例
     */
    public static FtpInstance getInstance(boolean byPool, FtpConfig config) {
        if (byPool) {
            if (pool == null) {
                return null;
            }
            return new FtpInstance(config != null ? config : Ftps.config, pool.getClient());
        }
        if (factory == null) {
            return null;
        }
        return new FtpInstance(config != null ? config : Ftps.config, pool.getClient());
    }

    /**
     * 销毁
     */
    private static void destroy(FTPClient client) {
        if (client != null && client.isConnected()) {
            try {
                client.logout();
            } catch (Exception e) {
                // ignore
            } finally {
                try {
                    client.disconnect();
                } catch (Exception e) {
                    // ignore
                }
            }
        }
    }

}
