package com.orion.remote.sftp;

import com.orion.able.Jsonable;

import java.io.Serializable;

/**
 * SFTP文件属性
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/30 10:06
 */
public class FileAttribute implements Serializable, Jsonable {

    private static final long serialVersionUID = 2356232780252635L;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 访问时间
     */
    private Integer accessTime;

    /**
     * 修改时间
     */
    private Integer modifyTime;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 组id
     */
    private Integer gid;

    /**
     * 8进制权限
     * <p>
     * S_IFMT     0170000   bitmask for the file type bit fields
     * S_IFSOCK   0140000   socket
     * S_IFLNK    0120000   symbolic link
     * S_IFREG    0100000   regular file
     * S_IFBLK    0060000   block device
     * S_IFDIR    0040000   directory
     * S_IFCHR    0020000   character device
     * S_IFIFO    0010000   fifo
     * S_ISUID    0004000   set UID bit
     * S_ISGID    0002000   set GID bit
     * S_ISVTX    0001000   sticky bit
     * S_IRWXU    00700     mask for file owner permissions
     * S_IRUSR    00400     owner has read permission
     * S_IWUSR    00200     owner has write permission
     * S_IXUSR    00100     owner has execute permission
     * S_IRWXG    00070     mask for group permissions
     * S_IRGRP    00040     group has read permission
     * S_IWGRP    00020     group has write permission
     * S_IXGRP    00010     group has execute permission
     * S_IRWXO    00007     mask for permissions for others (not in group)
     * S_IROTH    00004     others have read permission
     * S_IWOTH    00002     others have write permisson
     * S_IXOTH    00001     others have execute permission
     */
    private Integer permission;

    /**
     * 是否为文件夹
     */
    private boolean directory;

    /**
     * 是否为连接文件
     */
    private boolean linkFile;

    /**
     * 是否为普通文件
     */
    private boolean regularFile;

    /**
     * 文件信息
     */
    private String longEntry;

    public String getPath() {
        return path;
    }

    public FileAttribute setPath(String path) {
        this.path = path;
        return this;
    }

    public Integer getAccessTime() {
        return accessTime;
    }

    public FileAttribute setAccessTime(Integer accessTime) {
        this.accessTime = accessTime;
        return this;
    }

    public Integer getModifyTime() {
        return modifyTime;
    }

    public FileAttribute setModifyTime(Integer modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

    public Long getSize() {
        return size;
    }

    public FileAttribute setSize(Long size) {
        this.size = size;
        return this;
    }

    public Integer getUid() {
        return uid;
    }

    public FileAttribute setUid(Integer uid) {
        this.uid = uid;
        return this;
    }

    public Integer getGid() {
        return gid;
    }

    public FileAttribute setGid(Integer gid) {
        this.gid = gid;
        return this;
    }

    public Integer getPermission() {
        return permission;
    }

    public FileAttribute setPermission(Integer permission) {
        this.permission = permission;
        return this;
    }

    public boolean isDirectory() {
        return directory;
    }

    public FileAttribute setDirectory(boolean directory) {
        this.directory = directory;
        return this;
    }

    public boolean isLinkFile() {
        return linkFile;
    }

    public FileAttribute setLinkFile(boolean linkFile) {
        this.linkFile = linkFile;
        return this;
    }

    public boolean isRegularFile() {
        return regularFile;
    }

    public FileAttribute setRegularFile(boolean regularFile) {
        this.regularFile = regularFile;
        return this;
    }

    public String getLongEntry() {
        return longEntry;
    }

    public FileAttribute setLongEntry(String longEntry) {
        this.longEntry = longEntry;
        return this;
    }

    @Override
    public String toJsonString() {
        return toJSON();
    }

    @Override
    public String toString() {
        return "FileAttribute{" +
                "path='" + path + '\'' +
                ", accessTime=" + accessTime +
                ", modifyTime=" + modifyTime +
                ", size=" + size +
                ", uid=" + uid +
                ", gid=" + gid +
                ", permission='" + permission + '\'' +
                ", directory=" + directory +
                ", linkFile=" + linkFile +
                ", regularFile=" + regularFile +
                ", longEntry='" + longEntry + '\'' +
                '}';
    }

}
