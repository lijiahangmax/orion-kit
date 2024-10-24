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
package com.orion.net.ftp.server;

import java.io.File;
import java.io.Serializable;

/**
 * FTP ssl 配置信息
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/11 17:51
 */
public class FtpServerSslConfig implements Serializable {

    /**
     * ssl 协议
     */
    private String sslProtocol;

    /**
     * key 文件
     */
    private File keyStoreFile;

    /**
     * 密码
     */
    private String keyStorePassword;

    /**
     * 加密方式
     */
    private String keyStoreAlgorithm;

    /**
     * 别名
     */
    private String keyAlias;

    public FtpServerSslConfig(String keyStoreFile, String keyStorePassword) {
        this(new File(keyStoreFile), keyStorePassword);
    }

    public FtpServerSslConfig(File keyStoreFile, String keyStorePassword) {
        this.keyStoreFile = keyStoreFile;
        this.keyStorePassword = keyStorePassword;
    }

    public void setSslProtocol(String sslProtocol) {
        this.sslProtocol = sslProtocol;
    }

    public void setKeyStoreFile(File keyStoreFile) {
        this.keyStoreFile = keyStoreFile;
    }

    public void setKeyStoreFile(String keyStoreFile) {
        this.keyStoreFile = new File(keyStoreFile);
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public void setKeyStoreAlgorithm(String keyStoreAlgorithm) {
        this.keyStoreAlgorithm = keyStoreAlgorithm;
    }

    public void setKeyAlias(String keyAlias) {
        this.keyAlias = keyAlias;
    }

    public String getSslProtocol() {
        return sslProtocol;
    }

    public File getKeyStoreFile() {
        return keyStoreFile;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public String getKeyStoreAlgorithm() {
        return keyStoreAlgorithm;
    }

    public String getKeyAlias() {
        return keyAlias;
    }

}
