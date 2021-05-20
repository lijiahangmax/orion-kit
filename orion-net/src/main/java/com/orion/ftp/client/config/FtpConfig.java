package com.orion.ftp.client.config;

import com.orion.able.JsonAble;
import com.orion.constant.Const;
import com.orion.utils.Charsets;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * FTP连接配置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/17 16:00
 */
public class FtpConfig implements Serializable, JsonAble {

    /**
     * host
     */
    private String host;

    /**
     * port
     */
    private int port;

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
    private String remoteRootDir;

    /**
     * 远程文件编码
     */
    private String remoteContentCharset;

    /**
     * 本地文件编码
     */
    private String localContentCharset;

    /**
     * 远程文件名称编码
     */
    private Charset remoteFileNameCharset;

    /**
     * 本地文件名称编码
     */
    private Charset localFileNameCharset;

    /**
     * 是否显示隐藏文件
     */
    private boolean showHidden;

    /**
     * 缓冲区大小
     */
    public int buffSize;

    /**
     * 被动模式
     */
    private boolean passiveMode;

    /**
     * 数据超时时间
     */
    private int dateTimeout;

    /**
     * 连接超时时间
     */
    private int connTimeout;

    public FtpConfig(String host) {
        this(host, 21);
    }

    public FtpConfig(String host, int port) {
        this.host = host;
        this.port = port;
        this.remoteRootDir = Const.SLASH;
        this.remoteContentCharset = Const.UTF_8;
        this.localContentCharset = Const.UTF_8;
        this.remoteFileNameCharset = Charsets.UTF_8;
        this.localFileNameCharset = Charsets.UTF_8;
        this.showHidden = false;
        this.buffSize = Const.BUFFER_KB_8;
        this.passiveMode = false;
        this.dateTimeout = Const.MS_S_60;
        this.connTimeout = Const.MS_S_5;
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
        return this;
    }

    /**
     * 设置远程根目录
     *
     * @param base 需要前后都加 /
     * @return this
     */
    public FtpConfig rootDir(String base) {
        this.remoteRootDir = base;
        return this;
    }

    public FtpConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public FtpConfig setPort(int port) {
        this.port = port;
        return this;
    }

    public FtpConfig setRemoteRootDir(String remoteRootDir) {
        this.remoteRootDir = remoteRootDir;
        return this;
    }

    public FtpConfig setRemoteContentCharset(String remoteContentCharset) {
        this.remoteContentCharset = remoteContentCharset;
        return this;
    }

    public FtpConfig setLocalContentCharset(String localContentCharset) {
        this.localContentCharset = localContentCharset;
        return this;
    }

    public FtpConfig setRemoteFileNameCharset(String remoteFileNameCharset) {
        this.remoteFileNameCharset = Charsets.of(remoteFileNameCharset);
        return this;
    }

    public FtpConfig setLocalFileNameCharset(String localFileNameCharset) {
        this.localFileNameCharset = Charsets.of(localFileNameCharset);
        return this;
    }

    public FtpConfig setRemoteFileNameCharset(Charset remoteFileNameCharset) {
        this.remoteFileNameCharset = remoteFileNameCharset;
        return this;
    }

    public FtpConfig setLocalFileNameCharset(Charset localFileNameCharset) {
        this.localFileNameCharset = localFileNameCharset;
        return this;
    }

    public FtpConfig setShowHidden(boolean showHidden) {
        this.showHidden = showHidden;
        return this;
    }

    public FtpConfig setBuffSize(int buffSize) {
        this.buffSize = buffSize;
        return this;
    }

    public FtpConfig setPassiveMode(boolean passiveMode) {
        this.passiveMode = passiveMode;
        return this;
    }

    public FtpConfig setDateTimeout(int dateTimeout) {
        this.dateTimeout = dateTimeout;
        return this;
    }

    public FtpConfig setConnTimeout(int connTimeout) {
        this.connTimeout = connTimeout;
        return this;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRemoteRootDir() {
        return remoteRootDir;
    }

    public String getRemoteContentCharset() {
        return remoteContentCharset;
    }

    public String getLocalContentCharset() {
        return localContentCharset;
    }

    public Charset getRemoteFileNameCharset() {
        return remoteFileNameCharset;
    }

    public Charset getLocalFileNameCharset() {
        return localFileNameCharset;
    }

    public boolean isShowHidden() {
        return showHidden;
    }

    public int getBuffSize() {
        return buffSize;
    }

    public boolean isPassiveMode() {
        return passiveMode;
    }

    public int getDateTimeout() {
        return dateTimeout;
    }

    public int getConnTimeout() {
        return connTimeout;
    }

    @Override
    public String toJsonString() {
        return toJson();
    }

    @Override
    public String toString() {
        return toJson();
    }

}
