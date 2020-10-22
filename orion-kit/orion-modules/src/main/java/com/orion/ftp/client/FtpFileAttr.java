package com.orion.ftp.client;

import com.orion.able.JsonAble;

import java.io.Serializable;
import java.util.Date;

/**
 * FTP文件属性
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/17 15:56
 */
public class FtpFileAttr implements Serializable, JsonAble {

    private static final long serialVersionUID = -83262527027527775L;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 权限
     */
    private String permission;

    public String getPath() {
        return path;
    }

    public FtpFileAttr setPath(String path) {
        this.path = path;
        return this;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public FtpFileAttr setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

    public Long getSize() {
        return size;
    }

    public FtpFileAttr setSize(Long size) {
        this.size = size;
        return this;
    }

    public String getPermission() {
        return permission;
    }

    public FtpFileAttr setPermission(String permission) {
        this.permission = permission;
        return this;
    }

    @Override
    public String toJsonString() {
        return toJSON();
    }

    @Override
    public String toString() {
        return toJSON();
    }

}
