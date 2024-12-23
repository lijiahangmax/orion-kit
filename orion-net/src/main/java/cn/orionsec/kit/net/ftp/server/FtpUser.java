/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.net.ftp.server;

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
