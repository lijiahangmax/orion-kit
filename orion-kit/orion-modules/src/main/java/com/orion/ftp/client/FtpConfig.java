package com.orion.ftp.client;

import com.orion.able.Jsonable;

import java.io.Serializable;

/**
 * FTP连接配置类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/17 16:00
 */
public class FtpConfig implements Serializable, Jsonable {

    /**
     * host
     */
    private String host;

    /**
     * post
     */
    private int port = 21;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 远程文件夹
     */
    private String remoteBaseDir;

    /**
     * 远程文件编码
     */
    private String remoteContentCharset = "UTF-8";

    /**
     * 本地文件编码
     */
    private String localContentCharset = "UTF-8";

    /**
     * 远程文件名称编码
     */
    private String remoteFileNameCharset = "UTF-8";

    /**
     * 本地文件名称编码
     */
    private String localFileNameCharset = "UTF-8";

    /**
     * 是否显示隐藏文件
     */
    private Boolean showHidden = false;

    /**
     * 缓冲区大小 2M
     */
    public int buffSize = 2 * 1024 * 1024;

    /**
     * 被动模式
     */
    private Boolean passiveMode;

    /**
     * 数据超时时间
     */
    private int dateTimeout = 60 * 1000;

    /**
     * 连接超时时间
     */
    private int connTimeout = 60 * 1000;

    public FtpConfig(String host) {
        this.host = host;
    }

    public FtpConfig(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 认证
     *
     * @param username 用户名
     * @param password 密码
     * @return this
     */
    public FtpConfig auth(String username, String password) {
        this.username = username;
        this.password = password;
        this.remoteBaseDir = "/home/" + username + "/";
        return this;
    }

    /**
     * 设置远程根目录
     *
     * @param base 需要前后都加 /
     * @return this
     */
    public FtpConfig baseDir(String base) {
        this.remoteBaseDir = base;
        return this;
    }

    public String getHost() {
        return host;
    }

    public FtpConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public FtpConfig setPort(int port) {
        this.port = port;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public FtpConfig setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public FtpConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getRemoteBaseDir() {
        return remoteBaseDir;
    }

    public FtpConfig setRemoteBaseDir(String remoteBaseDir) {
        this.remoteBaseDir = remoteBaseDir;
        return this;
    }

    public String getRemoteContentCharset() {
        return remoteContentCharset;
    }

    public FtpConfig setRemoteContentCharset(String remoteContentCharset) {
        this.remoteContentCharset = remoteContentCharset;
        return this;
    }

    public String getLocalContentCharset() {
        return localContentCharset;
    }

    public FtpConfig setLocalContentCharset(String localContentCharset) {
        this.localContentCharset = localContentCharset;
        return this;
    }

    public String getRemoteFileNameCharset() {
        return remoteFileNameCharset;
    }

    public FtpConfig setRemoteFileNameCharset(String remoteFileNameCharset) {
        this.remoteFileNameCharset = remoteFileNameCharset;
        return this;
    }

    public String getLocalFileNameCharset() {
        return localFileNameCharset;
    }

    public FtpConfig setLocalFileNameCharset(String localFileNameCharset) {
        this.localFileNameCharset = localFileNameCharset;
        return this;
    }

    public Boolean getShowHidden() {
        return showHidden;
    }

    public FtpConfig setShowHidden(Boolean showHidden) {
        this.showHidden = showHidden;
        return this;
    }

    public int getBuffSize() {
        return buffSize;
    }

    public FtpConfig setBuffSize(int buffSize) {
        this.buffSize = buffSize;
        return this;
    }

    public Boolean getPassiveMode() {
        return passiveMode;
    }

    public FtpConfig setPassiveMode(Boolean passiveMode) {
        this.passiveMode = passiveMode;
        return this;
    }

    public int getDateTimeout() {
        return dateTimeout;
    }

    public FtpConfig setDateTimeout(int dateTimeout) {
        this.dateTimeout = dateTimeout;
        return this;
    }

    public int getConnTimeout() {
        return connTimeout;
    }

    public FtpConfig setConnTimeout(int connTimeout) {
        this.connTimeout = connTimeout;
        return this;
    }

    @Override
    public String toJsonString() {
        return toJSON();
    }

    @Override
    public String toString() {
        return toJSON();
    }

}
