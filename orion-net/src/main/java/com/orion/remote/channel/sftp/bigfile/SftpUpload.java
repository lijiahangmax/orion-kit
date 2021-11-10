package com.orion.remote.channel.sftp.bigfile;

import com.orion.constant.Const;
import com.orion.remote.channel.sftp.SftpExecutor;
import com.orion.remote.channel.sftp.SftpFile;
import com.orion.support.upload.BaseFileUpload;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.io.Streams;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * SFTP 大文件上传 支持断点续传, 实时速率
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/13 18:43
 */
public class SftpUpload extends BaseFileUpload {

    private static final String LOCK_SUFFIX = "orion.sftp.upload";

    /**
     * sftp执行器
     */
    private SftpExecutor executor;

    /**
     * 输出流
     */
    private OutputStream out;

    public SftpUpload(SftpExecutor executor, String remote, String local) {
        this(executor, remote, new File(local));
    }

    public SftpUpload(SftpExecutor executor, String remote, File local) {
        super(remote, local, LOCK_SUFFIX, Const.BUFFER_KB_8);
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
        SftpFile remoteFile = executor.getFile(remote);
        if (remoteFile == null) {
            try {
                executor.touch(remote);
                return -1;
            } catch (Exception e) {
                throw Exceptions.sftp("touch remote file error > " + remote);
            }
        }
        return remoteFile.getSize();
    }

    @Override
    protected void initUpload(boolean breakPoint, long skip) throws IOException {
        if (breakPoint) {
            out = executor.getOutputStreamAppend(remote);
        } else {
            out = executor.getOutputStreamWriter(remote);
        }
    }

    @Override
    protected void write(byte[] bs, int len) throws IOException {
        out.write(bs, 0, len);
    }

    @Override
    protected void transferFinish() {
        Streams.close(out);
    }

    public SftpExecutor getExecutor() {
        return executor;
    }

}
