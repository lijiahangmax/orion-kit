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
package com.orion.net.ftp.client.pool;

import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Valid;
import com.orion.net.ftp.client.config.FtpConfig;
import com.orion.net.ftp.client.config.FtpsConfig;
import com.orion.net.ftp.client.instance.FtpInstance;
import com.orion.net.ftp.client.instance.IFtpInstance;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;

/**
 * FTPClient 工厂
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/18 11:25
 */
public class FtpClientFactory {

    /**
     * FTP配置
     */
    private final FtpConfig config;

    public FtpClientFactory(FtpConfig config) {
        Valid.notNull(config, "config is null");
        this.config = config;
    }

    /**
     * 创建一个连接
     *
     * @return 连接
     */
    public FTPClient createClient() {
        FTPClient client;
        if (config instanceof FtpsConfig) {
            client = new FTPSClient(((FtpsConfig) config).getProtocol(), ((FtpsConfig) config).isImplicit());
        } else {
            client = new FTPClient();
        }
        int reply;
        try {
            client.setDataTimeout(config.getDateTimeout());
            client.setConnectTimeout(config.getConnTimeout());
            client.setListHiddenFiles(config.isShowHidden());
            client.setControlEncoding(config.getRemoteContentCharset());
            client.setBufferSize(config.getBuffSize());
            client.connect(config.getHost(), config.getPort());
            if (config instanceof FtpsConfig && client instanceof FTPSClient) {
                ((FTPSClient) client).execPROT(((FtpsConfig) config).getProtect());
                SSLSocketFactory socketFactory = ((FtpsConfig) config).getSocketFactory();
                if (socketFactory != null) {
                    client.setSocketFactory(socketFactory);
                }
            }

            client.login(config.getUsername(), config.getPassword());
            reply = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                client.disconnect();
                throw Exceptions.ftp("connection FTP client error because not ready");
            } else {
                client.setFileType(FTPClient.BINARY_FILE_TYPE);
                if (config.isPassiveMode()) {
                    client.enterLocalPassiveMode();
                    client.enterRemotePassiveMode();
                }
                client.cwd(config.getRemoteRootDir());
            }
            return client;
        } catch (IOException e) {
            throw Exceptions.ftp("connection FTP client error", e);
        }
    }

    /**
     * 创建一个实例
     *
     * @return this
     */
    public IFtpInstance createInstance() {
        return new FtpInstance(this.createClient(), config);
    }

    /**
     * 获取配置项
     */
    public FtpConfig getConfig() {
        return config;
    }

}
