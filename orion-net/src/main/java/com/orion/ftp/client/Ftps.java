package com.orion.ftp.client;

import com.orion.ftp.client.config.FtpConfig;
import com.orion.ftp.client.pool.FtpClientFactory;
import com.orion.utils.Exceptions;
import org.apache.commons.net.ftp.FTPClient;

/**
 * FTP工具类
 *
 * @author ljh15
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
                Exceptions.printStacks(e);
            }
        }
        return false;
    }

    /**
     * 创建FTP实例
     *
     * @param ftpConfig config
     * @return FTP实例
     */
    public static FtpInstance createInstance(FtpConfig ftpConfig) {
        return new FtpClientFactory(ftpConfig).createInstance();
    }

}
