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
package cn.orionsec.kit.net.ftp.client;

import cn.orionsec.kit.lang.utils.Strings;
import org.apache.commons.net.ftp.FTPFile;

import java.io.Serializable;
import java.util.Date;

/**
 * FTP 文件
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/9 21:22
 */
public class FtpFile implements Serializable {

    private static final long serialVersionUID = -8231234702679455L;

    private final FTPFile ftpFile;

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
    private boolean[] permission;

    /**
     * 权限信息
     */
    private String permissionString;

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
        this.permissionString = rawListing.split(Strings.SPACE)[0];
        this.permission = new boolean[9];
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHardLinkCount() {
        return hardLinkCount;
    }

    public void setHardLinkCount(int hardLinkCount) {
        this.hardLinkCount = hardLinkCount;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getRawListing() {
        return rawListing;
    }

    public void setRawListing(String rawListing) {
        this.rawListing = rawListing;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public boolean[] getPermission() {
        return permission;
    }

    public void setPermission(boolean[] permission) {
        this.permission = permission;
    }

    public void setPermissionString(String permissionString) {
        this.permissionString = permissionString;
    }

    public String getPermissionString() {
        return permissionString;
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
