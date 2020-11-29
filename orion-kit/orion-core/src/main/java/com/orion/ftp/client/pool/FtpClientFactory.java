package com.orion.ftp.client.pool;

import com.orion.ftp.client.FtpConfig;
import com.orion.utils.Exceptions;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;

/**
 * FTPClient工厂
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/18 11:25
 */
public class FtpClientFactory {

    /**
     * FTP配置
     */
    private FtpConfig config;

    public FtpClientFactory(FtpConfig config) {
        this.config = config;
    }

    /**
     * 获取一个连接
     *
     * @return 连接
     */
    public FTPClient getClient() {
        FTPClient client = new FTPClient();
        int reply;
        try {
            client.setDataTimeout(config.getDateTimeout());
            client.setConnectTimeout(config.getConnTimeout());
            client.setListHiddenFiles(config.getShowHidden());
            client.setControlEncoding(config.getRemoteContentCharset());
            client.setBufferSize(config.getBuffSize());
            client.connect(config.getHost(), config.getPort());
            client.login(config.getUsername(), config.getPassword());
            reply = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                client.disconnect();
                throw Exceptions.ftp("connection FTP client error because not ready");
            } else {
                client.setFileType(FTPClient.BINARY_FILE_TYPE);
                if (config.getPassiveMode() != null && config.getPassiveMode()) {
                    client.enterLocalPassiveMode();
                    client.enterRemotePassiveMode();
                }
                client.cwd(config.getRemoteBaseDir());
            }
            return client;
        } catch (IOException e) {
            throw Exceptions.ftp("connection FTP client error", e);
        }
    }

    /**
     * 销毁
     */
    public void destroy(FTPClient client) {
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
     * 判断是否存活
     */
    boolean isActive(FTPClient client) {
        if (client != null) {
            try {
                return client.sendNoOp();
            } catch (Exception e) {
                Exceptions.printStacks(e);
                return false;
            }
        }
        return false;
    }

    /**
     * 获取配置项
     */
    FtpConfig getConfig() {
        return config;
    }

}
