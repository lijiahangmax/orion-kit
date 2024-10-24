/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.orion.net.ftp.client;

import com.orion.net.ftp.client.config.FtpConfig;
import com.orion.net.ftp.client.instance.IFtpInstance;
import com.orion.net.ftp.client.pool.FtpClientFactory;
import com.orion.net.ftp.client.pool.FtpClientPool;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FTP 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/17 16:10
 */
public class Ftps {

    /**
     * LOG
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Ftps.class);

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
                LOGGER.error("Ftps.destroy logout error", e);
            } finally {
                try {
                    client.disconnect();
                } catch (Exception e) {
                    LOGGER.error("Ftps.destroy disconnect error", e);
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
