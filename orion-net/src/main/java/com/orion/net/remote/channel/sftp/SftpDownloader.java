package com.orion.net.remote.channel.sftp;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.Streams;
import com.orion.net.base.file.sftp.SftpFile;
import com.orion.net.base.file.transfer.BaseFileDownloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * SFTP 大文件下载 支持断点续传, 实时速率
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/13 18:26
 */
public class SftpDownloader extends BaseFileDownloader {

    private static final String LOCK_SUFFIX = "osd";

    /**
     * sftp 执行器
     */
    private final SftpExecutor executor;

    /**
     * 输入流
     */
    private InputStream in;

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
    protected long getFileSize() {
        SftpFile remoteFile = executor.getFile(remote);
        if (remoteFile == null) {
            throw Exceptions.notFound("not found download remote file");
        }
        return remoteFile.getSize();
    }

    @Override
    protected void initDownload(boolean breakPoint, long skip) throws IOException {
        if (breakPoint) {
            this.in = executor.openInputStream(remote, skip);
        } else {
            this.in = executor.openInputStream(remote);
        }
    }

    @Override
    protected int read(byte[] bs) throws IOException {
        return in.read(bs);
    }

    @Override
    protected void transferFinish() {
        this.close();
    }

    @Override
    public void close() {
        Streams.close(in);
    }

    @Override
    public void abort() {
        Streams.close(executor);
    }

    public SftpExecutor getExecutor() {
        return executor;
    }

}
