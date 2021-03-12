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
 * FTP 大文件下载 支持断点续传, 实时速率
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/3/13 00:51
 */
public class FtpDownload implements Runnable {

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

    public FtpDownload(FtpInstance instance, String remote, String local) {
        this(instance, remote, new File(local));
    }

    public FtpDownload(FtpInstance instance, String remote, File local) {
        Valid.notNull(instance, "ftp instance is null");
        Valid.notEmpty(remote, "download remote file is empty");
        Valid.notNull(local, "local file is null");
        this.instance = instance;
        this.remote = remote;
        this.local = local;
        this.lock = FileLocks.getSuffixFileLock("orion.ftp.download", local);
        this.progress = new ByteTransferProgress(0);
    }


    /**
     * 开启计算实时速率
     *
     * @param computeRate rate
     * @return this
     */
    public FtpDownload computeRate(boolean computeRate) {
        this.computeRate = computeRate;
        return this;
    }

    @Override
    public void run() {
        if (computeRate) {
            progress.computeRate();
        }
        FtpFile remoteFile = instance.getFile(remote);
        if (remoteFile == null) {
            throw Exceptions.notFound("not found download remote file");
        }
        long remoteSize = remoteFile.getSize();
        progress.end(remoteSize);
        try {
            if (local.exists() && local.isFile()) {
                long localSize = local.length();
                if (localSize == remoteFile.getSize()) {
                    // 跳过
                    lock.unLock();
                    progress.startTime(System.currentTimeMillis());
                    progress.finish();
                    return;
                }
                if (lock.isLocked()) {
                    // 被锁定 继续下载
                    this.breakPointResume(localSize);
                } else {
                    // 没被锁定 重新下载
                    this.download();
                }
            } else {
                // 直接下载
                Files1.touch(local);
                this.download();
            }
        } catch (IOException e) {
            throw Exceptions.ftp("ftp download exception remote file: " + remote + " -> local file: " + local.getAbsolutePath(), e);
        }
    }

    /**
     * 直接下载
     */
    private void download() throws IOException {
        progress.start();
        lock.tryLock();
        InputStream in = null;
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(Files1.openOutputStreamFastSafe(local), instance.config().getBuffSize());
            in = instance.getInputStream(remote);
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
            if (in != null) {
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
        InputStream in = null;
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(Files1.openOutputStreamFastSafe(local, true), instance.config().getBuffSize());
            in = instance.getInputStream(remote, skip);
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
            if (in != null) {
                instance.pending();
            }
        }
    }

    public ByteTransferProgress getProgress() {
        return progress;
    }

}