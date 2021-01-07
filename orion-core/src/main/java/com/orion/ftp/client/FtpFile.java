package com.orion.ftp.client;

import org.apache.commons.net.ftp.FTPFile;

import java.io.Serializable;
import java.util.Date;

/**
 * FTP文件
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/5/9 21:22
 */
public class FtpFile implements Serializable {

    private static final long serialVersionUID = -8231234702679455L;

    public FtpFile(String path, FTPFile ftpFile) {
        this.ftpFile = ftpFile;
        this.path = path;
        this.name = ftpFile.getName();
        this.size = ftpFile.getSize();
        this.type = ftpFile.getType();
        this.user = ftpFile.getUser();
        this.group = ftpFile.getGroup();
        this.link = ftpFile.getLink();
        this.hardLinkCount = ftpFile.getHardLinkCount();
        this.modifyTime = ftpFile.getTimestamp().getTime();
        this.rawListing = ftpFile.getRawListing();
        this.permission[0] = ftpFile.hasPermission(0, 0);
        this.permission[1] = ftpFile.hasPermission(0, 1);
        this.permission[2] = ftpFile.hasPermission(0, 2);
        this.permission[3] = ftpFile.hasPermission(1, 0);
        this.permission[4] = ftpFile.hasPermission(1, 1);
        this.permission[5] = ftpFile.hasPermission(1, 2);
        this.permission[6] = ftpFile.hasPermission(2, 0);
        this.permission[7] = ftpFile.hasPermission(2, 1);
        this.permission[8] = ftpFile.hasPermission(2, 2);
    }

    private FTPFile ftpFile;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件大小
     */
    private long size;

    /**
     * 文件类型
     * 0: 文件类型
     * 1: 文件夹类型
     * 2: 连接文件类型
     * 3: 未知类型
     */
    private int type;

    /**
     * 文件所属用户
     */
    private String user;

    /**
     * 文件所属组
     */
    private String group;

    /**
     * 连接指向的文件
     */
    private String link;

    /**
     * 文件连接数
     */
    private int hardLinkCount;

    /**
     * 文件最后一次更改的时间
     */
    private Date modifyTime;

    /**
     * 文件详情
     */
    private String rawListing;

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

    public Date getModifyTime() {
        return modifyTime;
    }

    public FtpFile setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

    public boolean[] getPermission() {
        return permission;
    }

    public FtpFile setPermission(boolean[] permission) {
        this.permission = permission;
        return this;
    }

    public FTPFile getFtpFile() {
        return ftpFile;
    }

    /**
     * @return 是否为普通文件
     */
    public boolean isFile() {
        return this.type == 0;
    }

    /**
     * @return 是否为文件夹
     */
    public boolean isDirectory() {
        return this.type == 1;
    }

    /**
     * @return 是否为连接文件
     */
    public boolean isSymbolicLink() {
        return this.type == 2;
    }

    /**
     * @return 是否为未知文件
     */
    public boolean isUnknown() {
        return this.type == 3;
    }

    @Override
    public String toString() {
        return path;
    }

}
