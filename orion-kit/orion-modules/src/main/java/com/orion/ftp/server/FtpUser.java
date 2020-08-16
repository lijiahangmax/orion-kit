package com.orion.ftp.server;

import java.io.Serializable;

/**
 * FTP user
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/8/14 12:03
 */
public class FtpUser implements Serializable {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码 为空为匿名用户
     */
    private String password;

    /**
     * 根目录
     */
    private String homePath;

    /**
     * 空闲时间 秒
     */
    private int maxIdleTime;

    /**
     * 是否有写权限
     */
    private boolean writePermission = true;

    /**
     * 最大上传速度
     */
    private int maxUploadRate;

    /**
     * 最大下载速度
     */
    private int maxDownloadRate;

    public FtpUser() {
    }

    public FtpUser(String username, String password, String homePath) {
        this.username = username;
        this.password = password;
        this.homePath = homePath;
    }

    public FtpUser username(String username) {
        this.username = username;
        return this;
    }

    public FtpUser password(String password) {
        this.password = password;
        return this;
    }

    public FtpUser homePath(String homePath) {
        this.homePath = homePath;
        return this;
    }

    public FtpUser maxIdleTime(int maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
        return this;
    }

    public FtpUser writePermission(boolean writePermission) {
        this.writePermission = writePermission;
        return this;
    }

    public FtpUser maxUploadRate(int maxUploadRate) {
        this.maxUploadRate = maxUploadRate;
        return this;
    }

    public FtpUser maxDownloadRate(int maxDownloadRate) {
        this.maxDownloadRate = maxDownloadRate;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHomePath() {
        return homePath;
    }

    public int getMaxIdleTime() {
        return maxIdleTime;
    }

    public boolean isWritePermission() {
        return writePermission;
    }

    public int getMaxUploadRate() {
        return maxUploadRate;
    }

    public int getMaxDownloadRate() {
        return maxDownloadRate;
    }

    @Override
    public String toString() {
        return username;
    }

}
