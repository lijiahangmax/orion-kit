package com.orion.remote.connection.sftp.bigfile;

import ch.ethz.ssh2.SFTPv3FileHandle;
import com.orion.constant.Const;
import com.orion.remote.connection.sftp.SftpExecutor;
import com.orion.remote.connection.sftp.SftpFile;
import com.orion.support.upload.BaseFileUpload;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;

import java.io.File;
import java.io.IOException;

/**
 * SFTP 大文件上传 支持断点续传, 实时速率
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/14 15:07
 */
public class SftpUpload extends BaseFileUpload {

    private static final String LOCK_SUFFIX = "orion.sftp.upload";

    /**
     * 实例
     */
    private SftpExecutor executor;

    /**
     * 文件处理器
     */
    private SFTPv3FileHandle handle;

    /**
     * 当前位置
     */
    private long current;

    public SftpUpload(SftpExecutor executor, String remote, String local) {
        this(executor, remote, new File(local));
    }

    public SftpUpload(SftpExecutor executor, String remote, File local) {
        super(remote, local, LOCK_SUFFIX, Const.BUFFER_KB_32);
        Valid.notNull(executor, "sftp executor is null");
        this.executor = executor;
    }

    @Override
    public void run() {
        try {
            super.startUpload();
        } catch (IOException e) {
            throw Exceptions.sftp("sftp upload exception local file: " + local.getAbsolutePath() + " -> remote file: " + remote, e);
        }
    }

    @Override
    protected long getFileSize() {
        SftpFile file = executor.getFile(remote);
        if (file == null) {
            executor.touchTruncate(remote);
            return -1;
        }
        return file.getSize();
    }

    @Override
    protected void initUpload(boolean breakPoint, long skip) {
        if (breakPoint) {
            current += skip;
            handle = executor.openFileHandler(remote, 3);
        } else {
            handle = executor.openFileHandler(remote, 2);
        }
    }

    @Override
    protected void write(byte[] bs, int len) throws IOException {
        executor.write(handle, current, bs, 0, len);
        current += len;
    }

    @Override
    protected void transferFinish() {
        if (handle != null) {
            executor.closeFile(handle);
        }
    }

    public SftpExecutor getExecutor() {
        return executor;
    }

}
