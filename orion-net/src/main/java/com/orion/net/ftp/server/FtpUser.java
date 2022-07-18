package com.orion.net.ftp.server;

import java.io.Serializable;

/**
 * FTP 用户信息
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/8/14 12:03
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
    private boolean writePermission;

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

    public FtpUser(String username, String password) {
        this(username, password, "/home/" + username);
    }

    public FtpUser(String username, String password, String homePath) {
        this.username = username;
        this.password = password;
        this.homePath = homePath;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHomePath(String homePath) {
        this.homePath = homePath;
    }

    public void setMaxIdleTime(int maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    public void setWritePermission(boolean writePermission) {
        this.writePermission = writePermission;
    }

    public void setMaxUploadRate(int maxUploadRate) {
        this.maxUploadRate = maxUploadRate;
    }

    public void setMaxDownloadRate(int maxDownloadRate) {
        this.maxDownloadRate = maxDownloadRate;
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
