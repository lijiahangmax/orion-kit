package com.orion.net.remote.connection.sftp;

import ch.ethz.ssh2.SFTPv3FileHandle;
import com.orion.lang.constant.Const;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.Streams;
import com.orion.net.base.sftp.SftpFile;
import com.orion.net.base.sftp.transfer.BaseFileDownloader;

import java.io.File;
import java.io.IOException;

/**
 * SFTP 大文件下载 支持断点续传, 实时速率
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/14 14:34
 */
public class SftpDownloader extends BaseFileDownloader {

    private static final String LOCK_SUFFIX = "osd";

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

    public SftpDownloader(SftpExecutor executor, String remote, String local) {
        this(executor, remote, new File(local));
    }

    public SftpDownloader(SftpExecutor executor, String remote, File local) {
        super(remote, local, LOCK_SUFFIX, Const.BUFFER_KB_32);
        Valid.notNull(executor, "sftp executor is null");
        this.executor = executor;
    }

    @Override
    public void run() {
        try {
            synchronized (executor) {
                super.startDownload();
            }
        } catch (IOException e) {
            throw Exceptions.sftp("sftp download exception remote file: " + remote + " -> local file: " + local.getAbsolutePath(), e);
        }
    }

    @Override
    protected long getRemoteFileSize() {
        SftpFile remoteFile = executor.getFile(remote);
        if (remoteFile == null) {
            throw Exceptions.notFound("not found download remote file");
        }
        return remoteFile.getSize();
    }

    @Override
    protected void initDownload(boolean breakPoint, long skip) {
        this.current += skip;
        this.handle = executor.openFileHandler(remote, 1);
        if (handle == null) {
            throw Exceptions.sftp("unopened remote file stream");
        }
    }

    @Override
    protected int read(byte[] bs) throws IOException {
        int r = executor.read(handle, current, bs);
        if (r != -1) {
            this.current += r;
        }
        return r;
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
