package com.orion.support.download;

import com.orion.support.progress.ByteTransferProgress;
import com.orion.utils.Valid;
import com.orion.utils.io.FileLocks;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 大文件下载 基类 支持断点续传, 实时速率
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/14 13:30
 */
public abstract class BaseFileDownload implements Runnable {

    /**
     * 远程文件
     */
    protected String remote;

    /**
     * 本地文件
     */
    protected File local;

    /**
     * 文件锁
     */
    protected FileLocks.NamedFileLock lock;

    /**
     * 进度条
     */
    protected ByteTransferProgress progress;

    /**
     * 缓冲区大小
     */
    protected int bufferSize;

    public BaseFileDownload(String remote, File local, String lockSuffix, int bufferSize) {
        Valid.notEmpty(remote, "download remote file is empty");
        Valid.notNull(local, "local file is null");
        this.remote = remote;
        this.local = local;
        this.bufferSize = bufferSize;
        this.lock = FileLocks.getSuffixFileLock(lockSuffix, local);
        this.progress = new ByteTransferProgress(0);
    }

    /**
     * 开始下载
     *
     * @throws IOException IOException
     */
    protected void startDownload() throws IOException {
        boolean error = false;
        try {
            long remoteSize = this.getFileSize();
            progress.end(remoteSize);
            if (Files1.isFile(local)) {
                long localSize = local.length();
                if (localSize == remoteSize) {
                    // 跳过
                    lock.unLock();
                    progress.startTime(System.currentTimeMillis());
                    this.transferFinish();
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
        } catch (Exception e) {
            error = true;
            throw e;
        } finally {
            progress.finish(error);
        }
    }

    /**
     * 直接下载
     *
     * @throws IOException IOException
     */
    protected void download() throws IOException {
        this.initDownload(false, 0);
        progress.start();
        lock.tryLock();
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(Files1.openOutputStreamFastSafe(local), bufferSize);
            int read;
            byte[] bs = new byte[bufferSize];
            while ((read = this.read(bs)) != -1) {
                progress.accept(read);
                out.write(bs, 0, read);
            }
        } finally {
            lock.unLock();
            Streams.close(out);
            this.transferFinish();
        }
    }

    /**
     * 断点续传
     *
     * @param skip 跳过的长度
     * @throws IOException IOException
     */
    protected void breakPointResume(long skip) throws IOException {
        this.initDownload(true, skip);
        progress.current(skip);
        progress.start(skip);
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(Files1.openOutputStreamFastSafe(local, true), bufferSize);
            int read;
            byte[] bs = new byte[bufferSize];
            while ((read = this.read(bs)) != -1) {
                progress.accept(read);
                out.write(bs, 0, read);
            }
        } finally {
            lock.unLock();
            Streams.close(out);
            this.transferFinish();
        }
    }

    /**
     * 获取文件大小
     *
     * @return fileSize 文件不存在则返回-1
     * @throws IOException IOException
     */
    protected abstract long getFileSize() throws IOException;

    /**
     * 准开始下载
     *
     * @param breakPoint 是否为断点续传
     * @param skip       跳过的长度
     * @throws IOException IOException
     */
    protected abstract void initDownload(boolean breakPoint, long skip) throws IOException;

    /**
     * 读取数据
     *
     * @param bs bs
     * @return 长度
     * @throws IOException IOException
     */
    protected abstract int read(byte[] bs) throws IOException;

    /**
     * 传输完成回调
     *
     * @throws IOException IOException
     */
    protected abstract void transferFinish() throws IOException;

    public ByteTransferProgress getProgress() {
        return progress;
    }

}