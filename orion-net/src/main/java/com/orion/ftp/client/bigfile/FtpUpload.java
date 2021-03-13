package com.orion.ftp.client.bigfile;

import com.orion.ftp.client.FtpFile;
import com.orion.ftp.client.FtpInstance;
import com.orion.support.upload.BaseFileUpload;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.io.Streams;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * FTP 大文件上传 支持断点续传 实时速率
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/3/12 23:42
 */
public class FtpUpload extends BaseFileUpload {

    private static final String LOCK_SUFFIX = "orion.ftp.upload";

    /**
     * 实例
     */
    private FtpInstance instance;

    /**
     * 输出流
     */
    private OutputStream out;

    public FtpUpload(FtpInstance instance, String remote, String local) {
        this(instance, remote, new File(local));
    }

    public FtpUpload(FtpInstance instance, String remote, File local) {
        super(remote, local, LOCK_SUFFIX, instance.config().getBuffSize());
        Valid.notNull(instance, "ftp instance is null");
        this.instance = instance;
    }

    @Override
    public void run() {
        try {
            super.startUpload();
        } catch (IOException e) {
            throw Exceptions.ftp("ftp upload exception local file: " + local.getAbsolutePath() + " -> remote file: " + remote, e);
        }
    }

    @Override
    protected long getFileSize() {
        FtpFile remoteFile = instance.getFile(remote);
        if (remoteFile == null) {
            return -1;
        }
        return remoteFile.getSize();
    }

    @Override
    protected void initUpload(boolean breakPoint) {
        try {
            if (breakPoint) {
                out = instance.getOutputStreamAppend(remote);
            } else {
                out = instance.getOutputStreamWriter(remote);
            }
        } catch (IOException e) {
            throw Exceptions.ioRuntime("ftp upload get remote stream error", e);
        }
    }

    @Override
    protected void write(byte[] bs, int len) throws IOException {
        out.write(bs, 0, len);
    }

    @Override
    protected void append(byte[] bs, int len) throws IOException {
        out.write(bs, 0, len);
    }

    @Override
    protected void transferFinish() throws IOException {
        Streams.close(out);
        if (out != null) {
            instance.pending();
        }
    }

}
