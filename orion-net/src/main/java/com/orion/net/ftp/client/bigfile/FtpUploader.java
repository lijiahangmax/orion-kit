package com.orion.net.ftp.client.bigfile;

import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.Streams;
import com.orion.net.base.file.transfer.BaseFileUploader;
import com.orion.net.ftp.client.FtpFile;
import com.orion.net.ftp.client.instance.FtpInstance;
import com.orion.net.ftp.client.instance.IFtpInstance;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * FTP 大文件上传 支持断点续传 实时速率
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/12 23:42
 */
public class FtpUploader extends BaseFileUploader {

    private static final String LOCK_SUFFIX = "ofu";

    /**
     * 实例
     */
    private final IFtpInstance instance;

    /**
     * 输出流
     */
    private OutputStream out;

    public FtpUploader(FtpInstance instance, String remote, String local) {
        this(instance, remote, new File(local));
    }

    public FtpUploader(FtpInstance instance, String remote, File local) {
        super(remote, local, LOCK_SUFFIX, instance.getConfig().getBuffSize());
        Valid.notNull(instance, "ftp instance is null");
        this.instance = instance;
    }

    @Override
    public void run() {
        try {
            synchronized (instance) {
                super.startUpload();
            }
        } catch (IOException e) {
            throw Exceptions.ftp("ftp upload exception local file: " + local.getAbsolutePath() + " -> remote file: " + remote, e);
        }
    }

    @Override
    protected long getRemoteFileSize() {
        FtpFile remoteFile = instance.getFile(remote);
        if (remoteFile == null) {
            return -1;
        }
        return remoteFile.getSize();
    }

    @Override
    protected void initUpload(boolean breakPoint, long skip) throws IOException {
        this.out = instance.openOutputStream(remote, breakPoint);
    }

    @Override
    protected void write(byte[] bs, int len) throws IOException {
        out.write(bs, 0, len);
    }

    @Override
    protected void transferFinish() {
        this.close();
    }

    @Override
    public void close() {
        try {
            Streams.close(out);
            if (out != null) {
                instance.pending();
            }
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    @Override
    public void abort() {
        try {
            out.close();
        } catch (IOException e) {
            throw Exceptions.ftp(e);
        }
    }

    public IFtpInstance getInstance() {
        return instance;
    }

}
