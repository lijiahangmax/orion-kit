package com.orion.net.remote.connection.sftp;

import ch.ethz.ssh2.SFTPv3FileAttributes;
import com.orion.lang.constant.Const;
import com.orion.lang.utils.io.FileType;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.time.Dates;
import com.orion.net.base.file.sftp.SftpFile;

import java.util.Date;

/**
 * sftp 文件包装
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/5/18 16:16
 */
public class SftpFileWrapper extends SftpFile {

    /**
     * 文件属性
     */
    private final SFTPv3FileAttributes attrs;

    protected SftpFileWrapper(String path, SFTPv3FileAttributes attrs) {
        this.path = path;
        this.attrs = attrs;
        this.accessTime = Dates.date(attrs.atime);
        this.modifyTime = Dates.date(attrs.mtime);
        this.size = attrs.size;
        this.uid = attrs.uid;
        this.gid = attrs.gid;
        this.permission = Files1.permission8to10(attrs.permissions & 0xFFF);
        String permissionString = Files1.permission10toString(permission);
        if (attrs.isDirectory()) {
            permissionString = FileType.DIRECTORY.getSymbol() + permissionString;
        } else if (attrs.isSymlink()) {
            permissionString = FileType.SYMLINK.getSymbol() + permissionString;
        } else {
            permissionString = FileType.REGULAR_FILE.getSymbol() + permissionString;
        }
        this.permissionString = permissionString;
    }

    public SFTPv3FileAttributes getAttrs() {
        return attrs;
    }

    public void setSize(long size) {
        this.size = size;
        this.attrs.size = size;
    }

    public void setUid(int uid) {
        this.uid = uid;
        this.attrs.uid = uid;
    }

    public void setGid(int gid) {
        this.gid = gid;
        this.attrs.gid = gid;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        this.attrs.mtime = ((int) (modifyTime.getTime() / Const.MS_S_1));
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
        this.attrs.atime = ((int) (accessTime.getTime() / Const.MS_S_1));
    }

    public void setPermission(int permission) {
        this.permission = permission;
        this.attrs.permissions = Files1.permission10to8(permission);
    }

}
