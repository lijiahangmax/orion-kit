package com.orion.net.host.sftp;

import com.orion.lang.utils.io.FileType;
import com.orion.lang.utils.io.Files1;

import java.io.Serializable;
import java.util.Date;

/**
 * sftp 文件
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/5/18 14:17
 */
public class SftpFile implements Serializable {

    private static final long serialVersionUID = -154572740257792L;

    /**
     * 文件路径
     */
    protected String path;

    /**
     * 访问时间
     */
    protected Date accessTime;

    /**
     * 修改时间
     */
    protected Date modifyTime;

    /**
     * 文件大小
     */
    protected long size;

    /**
     * 用户id
     */
    protected int uid;

    /**
     * 组id
     */
    protected int gid;

    /**
     * 10进制表示的 8进制权限
     */
    protected int permission;

    /**
     * 权限
     */
    protected String permissionString;

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
        return FileType.DIRECTORY.isMatch(permissionString);
    }

    /**
     * 是否为连接文件
     *
     * @return true 连接文件
     */
    public boolean isLinkFile() {
        return FileType.SYMLINK.isMatch(permissionString);
    }

    /**
     * 是否为普通文件
     *
     * @return true 普通文件
     */
    public boolean isRegularFile() {
        return FileType.REGULAR_FILE.isMatch(permissionString);
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public void setPermissionString(String permissionString) {
        this.permissionString = permissionString;
    }

    @Override
    public String toString() {
        return path;
    }

}
