package com.orion.net.ftp.client.bigfile;

import com.orion.net.base.file.transfer.BaseFileDownloader;
import com.orion.net.ftp.client.FtpFile;
import com.orion.net.ftp.client.instance.IFtpInstance;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.io.Streams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * FTP 大文件下载 支持断点续传, 实时速率
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/13 00:51
 */
public class FtpDownloader extends BaseFileDownloader {

    private static final String LOCK_SUFFIX = "orion.ftp.download";

    /**
     * 实例
     */
    private final IFtpInstance instance;

    /**
     * 输入流
     */
    private InputStream in;

    public FtpDownloader(IFtpInstance instance, String remote, String local) {
        this(instance, remote, new File(local));
    }

    public FtpDownloader(IFtpInstance instance, String remote, File local) {
        super(remote, local, LOCK_SUFFIX, instance.getConfig().getBuffSize());
        Valid.notNull(instance, "ftp instance is null");
        this.instance = instance;
    }

    @Override
    public void run() {
        try {
            synchronized (instance) {
                super.startDownload();
            }
        } catch (IOException e) {
            throw Exceptions.ftp("ftp download exception remote file: " + remote + " -> local file: " + local.getAbsolutePath(), e);
        }
    }

    @Override
    protected long getFileSize() {
        FtpFile remoteFile = instance.getFile(remote);
        if (remoteFile == null) {
            throw Exceptions.notFound("not found download remote file");
        }
        return remoteFile.getSize();
    }

    @Override
    protected void initDownload(boolean breakPoint, long skip) throws IOException {
        if (breakPoint) {
            this.in = instance.openInputStream(remote, skip);
        } else {
            this.in = instance.openInputStream(remote);
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
        try {
            Streams.close(in);
            if (in != null) {
                instance.pending();
            }
        } catch (Exception e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    @Override
    public void abort() {
        try {
            in.close();
        } catch (IOException e) {
            throw Exceptions.ftp(e);
        }
    }

    public IFtpInstance getInstance() {
        return instance;
    }

}