package com.orion.net.ftp.client;

import com.orion.net.ftp.client.config.FtpConfig;
import com.orion.net.ftp.client.instance.IFtpInstance;
import com.orion.net.ftp.client.pool.FtpClientFactory;
import com.orion.net.ftp.client.pool.FtpClientPool;
import org.apache.commons.net.ftp.FTPClient;

/**
 * FTP工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/17 16:10
 */
public class Ftps {

    private Ftps() {
    }

    /**
     * 销毁连接实例
     *
     * @param client client
     */
    public static void destroy(FTPClient client) {
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

    /**
     * 判断连接是否存活
     *
     * @param client client
     * @return 是否存活
     */
    public static boolean isActive(FTPClient client) {
        if (client != null && client.isConnected()) {
            try {
                return client.sendNoOp();
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * 创建 FTP 实例
     *
     * @param ftpConfig config
     * @return FTP 实例
     */
    public static IFtpInstance createInstance(FtpConfig ftpConfig) {
        return new FtpClientFactory(ftpConfig).createInstance();
    }

    /**
     * 创建 FTP 连接池
     *
     * @param ftpConfig config
     * @return pool
     */
    public static FtpClientPool createClientPool(FtpConfig ftpConfig) {
        return new FtpClientPool(new FtpClientFactory(ftpConfig));
    }

    /**
     * 创建 FTP 连接池
     *
     * @param ftpConfig config
     * @param size      size
     * @return pool
     */
    public static FtpClientPool createClientPool(FtpConfig ftpConfig, int size) {
        return new FtpClientPool(new FtpClientFactory(ftpConfig), size);
    }

}
