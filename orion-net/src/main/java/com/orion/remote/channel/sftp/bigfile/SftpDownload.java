package com.orion.remote.channel.sftp.bigfile;

import com.orion.constant.Const;
import com.orion.remote.channel.sftp.SftpExecutor;
import com.orion.remote.channel.sftp.SftpFile;
import com.orion.support.download.BaseFileDownload;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.io.Streams;

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
public class SftpDownload extends BaseFileDownload {

    private static final String LOCK_SUFFIX = "orion.sftp.download";

    /**
     * sftp执行器
     */
    private SftpExecutor executor;

    /**
     * 输入流
     */
    private InputStream in;

    public SftpDownload(SftpExecutor executor, String remote, String local) {
        this(executor, remote, new File(local));
    }

    public SftpDownload(SftpExecutor executor, String remote, File local) {
        super(remote, local, LOCK_SUFFIX, Const.BUFFER_KB_8);
        Valid.notNull(executor, "sftp executor is null");
        this.executor = executor;
    }

    @Override
    public void run() {
        try {
            super.startDownload();
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
            in = executor.getInputStream(remote, skip);
        } else {
            in = executor.getInputStream(remote);
        }
    }

    @Override
    protected int read(byte[] bs) throws IOException {
        return in.read(bs);
    }

    @Override
    protected void transferFinish() {
        Streams.close(in);
    }

    public SftpExecutor getExecutor() {
        return executor;
    }

}
