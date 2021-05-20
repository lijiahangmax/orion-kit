package com.orion.ftp.server;

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

    // 生成命令 keytool -genkey -alias jwt -keyalg RSA -keysize 1024 -keystore ftps.jks -validity 365

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

    public FtpServerSslConfig setSslProtocol(String sslProtocol) {
        this.sslProtocol = sslProtocol;
        return this;
    }

    public FtpServerSslConfig setKeyStoreFile(File keyStoreFile) {
        this.keyStoreFile = keyStoreFile;
        return this;
    }

    public FtpServerSslConfig setKeyStoreFile(String keyStoreFile) {
        this.keyStoreFile = new File(keyStoreFile);
        return this;
    }

    public FtpServerSslConfig setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
        return this;
    }

    public FtpServerSslConfig setKeyStoreAlgorithm(String keyStoreAlgorithm) {
        this.keyStoreAlgorithm = keyStoreAlgorithm;
        return this;
    }

    public FtpServerSslConfig setKeyAlias(String keyAlias) {
        this.keyAlias = keyAlias;
        return this;
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
