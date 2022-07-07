package com.orion.net.remote.connection.sftp;

import ch.ethz.ssh2.SFTPv3FileHandle;
import com.orion.lang.constant.Const;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.Streams;
import com.orion.net.base.file.sftp.SftpFile;
import com.orion.net.base.file.transfer.BaseFileUploader;

import java.io.File;
import java.io.IOException;

/**
 * SFTP 大文件上传 支持断点续传, 实时速率
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/14 15:07
 */
public class SftpUploader extends BaseFileUploader {

    private static final String LOCK_SUFFIX = "orion.sftp.upload";

    /**
     * 实例
     */
    private final SftpExecutor executor;

    /**
     * 文件处理器
     */
    private SFTPv3FileHandle handle;

    /**
     * 当前位置
     */
    private long current;

    public SftpUploader(SftpExecutor executor, String remote, String local) {
        this(executor, remote, new File(local));
    }

    public SftpUploader(SftpExecutor executor, String remote, File local) {
        super(remote, local, LOCK_SUFFIX, Const.BUFFER_KB_32);
        Valid.notNull(executor, "sftp executor is null");
        this.executor = executor;
    }

    @Override
    public void run() {
        try {
            synchronized (executor) {
                super.startUpload();
            }
        } catch (IOException e) {
            throw Exceptions.sftp("sftp upload exception local file: " + local.getAbsolutePath() + " -> remote file: " + remote, e);
        }
    }

    @Override
    protected long getFileSize() {
        SftpFile remoteFile = executor.getFile(remote);
        if (remoteFile == null) {
            try {
                executor.touchTruncate(remote);
                return -1;
            } catch (Exception e) {
                throw Exceptions.sftp("touch remote file error > " + remote, e);
            }
        }
        return remoteFile.getSize();
    }

    @Override
    protected void initUpload(boolean breakPoint, long skip) {
        if (breakPoint) {
            this.current += skip;
            this.handle = executor.openFileHandler(remote, 3);
        } else {
            this.handle = executor.openFileHandler(remote, 2);
        }
    }

    @Override
    protected void write(byte[] bs, int len) throws IOException {
        this.executor.write(handle, current, bs, 0, len);
        this.current += len;
    }

    @Override
    protected void transferFinish() {
        this.close();
    }

    @Override
    public void close() {
        if (handle != null) {
            executor.closeFile(handle);
        }
    }

    @Override
    public void abort() {
        Streams.close(executor);
    }

    public SftpExecutor getExecutor() {
        return executor;
    }

}
