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
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/10 17:07
 */
public class SftpFile implements Serializable {

    private static final long serialVersionUID = -154572740257792L;

    protected SftpFile(String path, SftpATTRS attrs) {
        this(path, null, attrs);
    }

    protected SftpFile(String path, String longEntry, SftpATTRS attrs) {
        this.path = path;
        this.longEntry = longEntry;
        this.attrs = attrs;
        this.size = attrs.getSize();
        this.uid = attrs.getUId();
        this.gid = attrs.getGId();
        this.permission = Files1.permission8to10(attrs.getPermissions() & 0XFFF);
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
     * 10进制表示的 8进制权限
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

    public SftpFile setSize(long size) {
        this.size = size;
        this.attrs.setSIZE(size);
        return this;
    }

    public SftpFile setUid(int uid) {
        this.uid = uid;
        this.attrs.setUIDGID(uid, gid);
        return this;
    }

    public SftpFile setGid(int gid) {
        this.gid = gid;
        this.attrs.setUIDGID(uid, gid);
        return this;
    }

    public SftpFile setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        this.attrs.setACMODTIME((int) (accessTime.getTime() / Const.MS_S_1), (int) (modifyTime.getTime() / Const.MS_S_1));
        return this;
    }

    public SftpFile setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
        this.attrs.setACMODTIME((int) (accessTime.getTime() / Const.MS_S_1), (int) (modifyTime.getTime() / Const.MS_S_1));
        return this;
    }

    public SftpFile setPermission(int permission) {
        this.permission = permission;
        this.attrs.setPERMISSIONS(Files1.permission10to8(permission));
        return this;
    }

    @Override
    public String toString() {
        return path;
    }

}
