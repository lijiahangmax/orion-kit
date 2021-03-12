package com.orion.ftp.client.bigfile;

import com.orion.ftp.client.FtpFile;
import com.orion.ftp.client.FtpInstance;
import com.orion.support.progress.ByteTransferProgress;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.io.FileLocks;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import java.io.*;

/**
 * FTP 大文件上传 支持断点续传 实时速率
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/3/12 23:42
 */
public class FtpUpload implements Runnable {

    /**
     * 远程文件
     */
    private String remote;

    /**
     * 本地文件
     */
    private File local;

    /**
     * 文件锁
     */
    private FileLocks.NamedFileLock lock;

    /**
     * 实例
     */
    private FtpInstance instance;

    /**
     * 进度条
     */
    private ByteTransferProgress progress;

    /**
     * 计算实时速率
     */
    private boolean computeRate;

    public FtpUpload(FtpInstance instance, String remote, String local) {
        this(instance, remote, new File(local));
    }

    public FtpUpload(FtpInstance instance, String remote, File local) {
        Valid.notNull(instance, "ftp instance is null");
        Valid.notEmpty(remote, "remote file is empty");
        Valid.notNull(local, "upload file is null");
        if (!local.exists() || !local.isFile()) {
            throw Exceptions.notFound("not found upload local file");
        }
        this.instance = instance;
        this.remote = remote;
        this.local = local;
        this.lock = FileLocks.getSuffixFileLock("orion.ftp.upload", local);
        this.progress = new ByteTransferProgress(local.length());
    }

    /**
     * 开启计算实时速率
     *
     * @param computeRate rate
     * @return this
     */
    public FtpUpload computeRate(boolean computeRate) {
        this.computeRate = computeRate;
        return this;
    }

    @Override
    public void run() {
        if (computeRate) {
            progress.computeRate();
        }
        FtpFile remoteFile = instance.getFile(remote);
        try {
            if (remoteFile == null) {
                // 远程文件为空 直接上传
                this.upload();
            } else {
                long remoteSize = remoteFile.getSize();
                if (remoteSize == local.length()) {
                    lock.unLock();
                    progress.startTime(System.currentTimeMillis());
                    progress.finish();
                    return;
                }
                if (lock.isLocked()) {
                    // 被锁定 继续上传
                    this.breakPointResume(remoteSize);
                } else {
                    // 没被锁定 重新上传
                    this.upload();
                }
            }
        } catch (IOException e) {
            throw Exceptions.ftp("ftp upload exception local file: " + local.getAbsolutePath() + " -> remote file: " + remote, e);
        }
    }

    /**
     * 直接上传
     */
    private void upload() throws IOException {
        progress.start();
        lock.tryLock();
        InputStream in = null;
        OutputStream out = null;
        try {
            out = instance.getOutputStreamWriter(remote);
            in = new BufferedInputStream(Files1.openInputStreamFastSafe(local), instance.config().getBuffSize());
            int read;
            byte[] bs = new byte[instance.config().getBuffSize()];
            while (-1 != (read = in.read(bs))) {
                progress.accept(read);
                out.write(bs, 0, read);
            }
        } finally {
            progress.finish();
            lock.unLock();
            Streams.close(in);
            Streams.close(out);
            if (out != null) {
                instance.pending();
            }
        }
    }

    /**
     * 断点续传
     *
     * @param skip 跳过的长度
     */
    private void breakPointResume(long skip) throws IOException {
        progress.current(skip);
        progress.start(skip);
        RandomAccessFile access = null;
        OutputStream out = null;
        try {
            out = instance.getOutputStreamAppend(remote);
            access = Files1.openRandomAccessSafe(local, "r");
            access.seek(skip);
            int read;
            byte[] bs = new byte[instance.config().getBuffSize()];
            while (-1 != (read = access.read(bs))) {
                progress.accept(read);
                out.write(bs, 0, read);
            }
        } finally {
            progress.finish();
            lock.unLock();
            Streams.close(access);
            Streams.close(out);
            if (out != null) {
                instance.pending();
            }
        }
    }

    public ByteTransferProgress getProgress() {
        return progress;
    }

}
