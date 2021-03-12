package com.orion.remote.connection.sftp;

import ch.ethz.ssh2.SFTPv3FileAttributes;
import com.orion.constant.Const;
import com.orion.utils.io.Files1;
import com.orion.utils.time.Dates;

import java.io.Serializable;
import java.util.Date;

/**
 * SFTP文件属性
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/30 10:06
 */
public class FileAttribute implements Serializable {

    private static final long serialVersionUID = 2356232780252635L;

    protected FileAttribute(String path, SFTPv3FileAttributes attrs) {
        this(path, null, attrs);
    }

    protected FileAttribute(String path, String longEntry, SFTPv3FileAttributes attrs) {
        this.path = path;
        this.longEntry = longEntry;
        this.attrs = attrs;
        this.size = attrs.size;
        this.uid = attrs.uid;
        this.gid = attrs.gid;
        this.permission = attrs.permissions;
        this.accessTime = Dates.date(attrs.atime);
        this.modifyTime = Dates.date(attrs.mtime);
    }

    /**
     * 文件路径
     */
    private String path;

    /**
     * 访问时间
     */
    private Date accessTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 文件大小
     */
    private long size;

    /**
     * 用户id
     */
    private int uid;

    /**
     * 组id
     */
    private int gid;

    /**
     * 8进制权限
     */
    private int permission;

    /**
     * 文件信息行
     */
    private String longEntry;

    private SFTPv3FileAttributes attrs;

    public String getPath() {
        return path;
    }

    public Date getAccessTime() {
        return accessTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public long getSize() {
        return size;
    }

    public int getUid() {
        return uid;
    }

    public int getGid() {
        return gid;
    }

    public int getPermission() {
        return permission;
    }

    public String getLongEntry() {
        return longEntry;
    }

    protected SFTPv3FileAttributes getAttrs() {
        return attrs;
    }

    /**
     * 获取文件名称
     *
     * @return 文件名称
     */
    public String getName() {
        return Files1.getFileName(path);
    }

    /**
     * 是否为文件夹
     *
     * @return true 文件夹
     */
    public boolean isDirectory() {
        return attrs.isDirectory();
    }

    /**
     * 是否为连接文件
     *
     * @return true 连接文件
     */
    public boolean isLinkFile() {
        return attrs.isSymlink();
    }

    /**
     * 是否为普通文件
     *
     * @return true 普通文件
     */
    public boolean isRegularFile() {
        return attrs.isRegularFile();
    }

    public FileAttribute setSize(long size) {
        this.size = size;
        this.attrs.size = size;
        return this;
    }

    public FileAttribute setUid(int uid) {
        this.uid = uid;
        this.attrs.uid = uid;
        return this;
    }

    public FileAttribute setGid(int gid) {
        this.gid = gid;
        this.attrs.gid = gid;
        return this;
    }

    public FileAttribute setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        this.attrs.mtime = ((int) (modifyTime.getTime() / Const.MS_S_1));
        return this;
    }

    public FileAttribute setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
        this.attrs.atime = ((int) (accessTime.getTime() / Const.MS_S_1));
        return this;
    }

    public FileAttribute setPermission(int permission) {
        this.permission = permission;
        this.attrs.permissions = permission;
        return this;
    }

    @Override
    public String toString() {
        return path;
    }
}
