package com.orion.support.upload;

import com.orion.constant.Const;
import com.orion.support.progress.ByteTransferRateProgress;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.io.FileLocks;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import java.io.*;

/**
 * 大文件上传 基类 支持断点续传, 实时速率
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/14 00:07
 */
public abstract class BaseFileUpload implements Runnable {

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
    protected ByteTransferRateProgress progress;

    /**
     * 缓冲区大小
     */
    protected int bufferSize;

    protected BaseFileUpload(String remote, File local, String lockSuffix, int bufferSize) {
        Valid.notEmpty(remote, "remote file is empty");
        Valid.notNull(local, "upload file is null");
        if (!local.exists() || !local.isFile()) {
            throw Exceptions.notFound("not found upload local file");
        }
        this.remote = remote;
        this.local = local;
        this.bufferSize = bufferSize;
        this.lock = FileLocks.getSuffixFileLock(lockSuffix, local);
        this.progress = new ByteTransferRateProgress(local.length());
    }

    /**
     * 开始上传
     *
     * @throws IOException IOException
     */
    protected void startUpload() throws IOException {
        boolean error = false;
        try {
            long remoteSize = this.getFileSize();
            if (remoteSize == -1) {
                // 远程文件为空 直接上传
                this.upload();
            } else {
                if (remoteSize == local.length()) {
                    lock.unLock();
                    progress.startTime(System.currentTimeMillis());
                    this.transferFinish();
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
        } catch (Exception e) {
            error = true;
            throw e;
        } finally {
            progress.finish(error);
        }
    }

    /**
     * 直接上传
     *
     * @throws IOException IOException
     */
    protected void upload() throws IOException {
        this.initUpload(false, 0);
        progress.start();
        lock.tryLock();
        InputStream in = null;
        try {
            in = new BufferedInputStream(Files1.openInputStreamFastSafe(local), bufferSize);
            int read;
            byte[] bs = new byte[bufferSize];
            while ((read = in.read(bs)) != -1) {
                progress.accept(read);
                this.write(bs, read);
            }
            lock.unLock();
        } finally {
            Streams.close(in);
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
        this.initUpload(true, skip);
        progress.setStart(skip);
        progress.setCurrent(skip);
        progress.start();
        RandomAccessFile access = null;
        try {
            access = Files1.openRandomAccessSafe(local, Const.ACCESS_R);
            access.seek(skip);
            int read;
            byte[] bs = new byte[bufferSize];
            while ((read = access.read(bs)) != -1) {
                progress.accept(read);
                this.write(bs, read);
            }
            lock.unLock();
        } finally {
            Streams.close(access);
            this.transferFinish();
        }
    }

    /**
     * 获取远程文件大小
     *
     * @return fileSize 文件不存在则返回 -1
     * @throws IOException IOException
     */
    protected abstract long getFileSize() throws IOException;

    /**
     * 准开始上传
     *
     * @param breakPoint 是否为断点续传
     * @param skip       skip
     * @throws IOException IOException
     */
    protected abstract void initUpload(boolean breakPoint, long skip) throws IOException;

    /**
     * 写入
     *
     * @param bs  bs
     * @param len 长度
     * @throws IOException IOException
     */
    protected abstract void write(byte[] bs, int len) throws IOException;

    /**
     * 传输完成回调
     *
     * @throws IOException IOException
     */
    protected abstract void transferFinish() throws IOException;

    public ByteTransferRateProgress getProgress() {
        return progress;
    }

}
