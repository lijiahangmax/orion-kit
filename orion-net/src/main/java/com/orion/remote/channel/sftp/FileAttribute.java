package com.orion.remote.channel.sftp;

import com.jcraft.jsch.SftpATTRS;
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
 * @since 2020/10/10 17:07
 */
public class FileAttribute implements Serializable {

    private static final long serialVersionUID = -154572740257792L;

    protected FileAttribute(String path, SftpATTRS attrs) {
        this(path, null, attrs);
    }

    protected FileAttribute(String path, String longEntry, SftpATTRS attrs) {
        this.path = path;
        this.longEntry = longEntry;
        this.attrs = attrs;
        this.size = attrs.getSize();
        this.uid = attrs.getUId();
        this.gid = attrs.getGId();
        this.permission = attrs.getPermissions();
        this.permissionString = attrs.getPermissionsString();
        this.accessTime = Dates.date(attrs.getATime());
        this.modifyTime = Dates.date(attrs.getMTime());
    }

    /**
     * 文件绝对路径
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
     * 权限
     */
    private String permissionString;

    /**
     * 文件信息行 只有列表有
     */
    private String longEntry;

    /**
     * attr
     */
    private SftpATTRS attrs;

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

    public String getPermissionString() {
        return permissionString;
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
        return 'd' == permissionString.charAt(0);
    }

    /**
     * 是否为连接文件
     *
     * @return true 连接文件
     */
    public boolean isLinkFile() {
        return 'l' == permissionString.charAt(0);
    }

    /**
     * 是否为普通文件
     *
     * @return true 普通文件
     */
    public boolean isRegularFile() {
        return '-' == permissionString.charAt(0);
    }

    protected SftpATTRS getAttrs() {
        return attrs;
    }

    public FileAttribute setSize(long size) {
        this.size = size;
        this.attrs.setSIZE(size);
        return this;
    }

    public FileAttribute setUid(int uid) {
        this.uid = uid;
        this.attrs.setUIDGID(uid, gid);
        return this;
    }

    public FileAttribute setGid(int gid) {
        this.gid = gid;
        this.attrs.setUIDGID(uid, gid);
        return this;
    }

    public FileAttribute setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        this.attrs.setACMODTIME((int) (accessTime.getTime() / Const.MS_S_1), (int) (modifyTime.getTime() / Const.MS_S_1));
        return this;
    }

    public FileAttribute setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
        this.attrs.setACMODTIME((int) (accessTime.getTime() / Const.MS_S_1), (int) (modifyTime.getTime() / Const.MS_S_1));
        return this;
    }

    public FileAttribute setPermission(int permission) {
        this.permission = permission;
        this.attrs.setPERMISSIONS(permission);
        return this;
    }

    @Override
    public String toString() {
        return path;
    }

}
