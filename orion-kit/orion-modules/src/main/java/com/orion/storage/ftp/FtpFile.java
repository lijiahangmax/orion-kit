package com.orion.storage.ftp;

import java.io.Serializable;
import java.util.Calendar;

/**
 * FTP文件
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/9 21:22
 */
@SuppressWarnings("ALL")
public class FtpFile implements Serializable {

    private static final long serialVersionUID = -8231234702679455L;

    /**
     * 文件类型
     * 0: 文件类型
     * 1: 文件夹类型
     * 2: 连接文件类型
     * 3: 未知类型
     */
    private int type;

    /**
     * 文件连接数
     */
    private int hardLinkCount;

    /**
     * 文件大小
     */
    private long size;

    /**
     * 文件详情
     */
    private String rawListing;

    /**
     * 文件所属用户
     */
    private String user;

    /**
     * 文件所属组
     */
    private String group;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 连接指向的文件
     */
    private String link;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 文件最后一次更改的时间
     */
    private Calendar date;

    /**
     * 012 user: rwx
     * 345 group: rwx
     * 678 world: rwx
     */
    private boolean[] permission = new boolean[9];

    public int getType() {
        return type;
    }

    public FtpFile setType(int type) {
        this.type = type;
        return this;
    }

    public int getHardLinkCount() {
        return hardLinkCount;
    }

    public FtpFile setHardLinkCount(int hardLinkCount) {
        this.hardLinkCount = hardLinkCount;
        return this;
    }

    public long getSize() {
        return size;
    }

    public FtpFile setSize(long size) {
        this.size = size;
        return this;
    }

    public String getRawListing() {
        return rawListing;
    }

    public FtpFile setRawListing(String rawListing) {
        this.rawListing = rawListing;
        return this;
    }

    public String getUser() {
        return user;
    }

    public FtpFile setUser(String user) {
        this.user = user;
        return this;
    }

    public String getGroup() {
        return group;
    }

    public FtpFile setGroup(String group) {
        this.group = group;
        return this;
    }

    public String getName() {
        return name;
    }

    public FtpFile setName(String name) {
        this.name = name;
        return this;
    }

    public String getLink() {
        return link;
    }

    public FtpFile setLink(String link) {
        this.link = link;
        return this;
    }

    public String getPath() {
        return path;
    }

    public FtpFile setPath(String path) {
        this.path = path;
        return this;
    }

    public Calendar getDate() {
        return date;
    }

    public FtpFile setDate(Calendar date) {
        this.date = date;
        return this;
    }

    public boolean[] getPermission() {
        return permission;
    }

    public FtpFile setPermission(boolean[] permission) {
        this.permission = permission;
        return this;
    }

    @Override
    public String toString() {
        return path;
    }

}
