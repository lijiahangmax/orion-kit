/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package com.orion.net.host.sftp;

import com.jcraft.jsch.SftpATTRS;
import com.orion.lang.constant.Const;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.time.Dates;

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
    private final SftpATTRS attrs;

    protected SftpFileWrapper(String path, SftpATTRS attrs) {
        this.path = path;
        this.attrs = attrs;
        this.accessTime = Dates.date(attrs.getATime());
        this.modifyTime = Dates.date(attrs.getMTime());
        this.size = attrs.getSize();
        this.uid = attrs.getUId();
        this.gid = attrs.getGId();
        this.permission = Files1.permission8to10(attrs.getPermissions() & 0xFFF);
        this.permissionString = attrs.getPermissionsString();
    }

    public SftpATTRS getAttrs() {
        return attrs;
    }

    @Override
    public void setSize(long size) {
        this.size = size;
        this.attrs.setSIZE(size);
    }

    @Override
    public void setUid(int uid) {
        this.uid = uid;
        this.attrs.setUIDGID(uid, gid);
    }

    @Override
    public void setGid(int gid) {
        this.gid = gid;
        this.attrs.setUIDGID(uid, gid);
    }

    @Override
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
        this.attrs.setACMODTIME((int) (accessTime.getTime() / Const.MS_S_1), (int) (modifyTime.getTime() / Const.MS_S_1));
    }

    @Override
    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
        this.attrs.setACMODTIME((int) (accessTime.getTime() / Const.MS_S_1), (int) (modifyTime.getTime() / Const.MS_S_1));
    }

    @Override
    public void setPermission(int permission) {
        this.permission = permission;
        this.attrs.setPERMISSIONS(Files1.permission10to8(permission));
    }

}
