/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.orion.net.specification.transfer;

import com.orion.lang.support.progress.ByteTransferRateProgress;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.FileLocks;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.Streams;

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
public abstract class BaseFileDownloader implements IFileDownloader {

    /**
     * 远程文件
     */
    protected final String remote;

    /**
     * 本地文件
     */
    protected final File local;

    /**
     * 文件锁
     */
    protected final FileLocks.NamedFileLock lock;

    /**
     * 进度条
     */
    protected final ByteTransferRateProgress progress;

    /**
     * 缓冲区大小
     */
    protected int bufferSize;

    /**
     * 是否强制覆盖下载 (不检测文件锁定/不检测文件大小/不走断点续传)
     */
    protected boolean forceOverride;

    /**
     * 文件大小相同则覆盖下载
     */
    protected boolean fileSizeEqualOverride;

    /**
     * 远程文件大小 缓存
     */
    protected Long remoteFileLength;

    public BaseFileDownloader(String remote, File local, String lockSuffix, int bufferSize) {
        Valid.notEmpty(remote, "download remote file is empty");
        Valid.notNull(local, "local file is null");
        this.remote = remote;
        this.local = local;
        this.bufferSize = bufferSize;
        this.lock = FileLocks.getSuffixFileLock(lockSuffix, local);
        this.progress = new ByteTransferRateProgress(0);
    }

    @Override
    public void forceOverride(boolean forceOverride) {
        this.forceOverride = forceOverride;
    }

    @Override
    public void fileSizeEqualOverride(boolean fileSizeEqualOverride) {
        this.fileSizeEqualOverride = fileSizeEqualOverride;
    }

    @Override
    public long getRemoteFileLength() throws IOException {
        if (remoteFileLength != null) {
            return remoteFileLength;
        }
        // 获取远程文件大小
        return this.remoteFileLength = this.getRemoteFileSize();
    }

    @Override
    public boolean checkRemoteFilePresentSizeEqual() throws IOException {
        long remoteLength = this.getRemoteFileLength();
        return remoteLength == -1 || remoteLength == local.length();
    }

    /**
     * 开始下载
     *
     * @throws IOException IOException
     */
    protected void startDownload() throws IOException {
        boolean error = false;
        try {
            // 获取远程文件大小
            long remoteSize = this.getRemoteFileLength();
            // 设置进度条终点
            progress.setEnd(remoteSize);
            // 强制覆盖下载
            if (forceOverride) {
                Files1.touch(local);
                this.download();
                return;
            }
            if (Files1.isFile(local)) {
                long localSize = local.length();
                if (localSize == remoteSize) {
                    // 文件大小一样 检测是否覆盖下载
                    if (fileSizeEqualOverride) {
                        // 如果设置文件大小一致覆盖 则重新下载
                        this.download();
                    } else {
                        // 认为是文件相同(大文件节约性能) 则跳过
                        lock.unLock();
                        progress.startTime(System.currentTimeMillis());
                        this.transferFinish();
                    }
                } else {
                    // 文件大小不一样 检测是否断点续传
                    if (lock.isLocked()) {
                        // 被锁定 继续下载
                        this.breakPointResume(localSize);
                    } else {
                        // 没被锁定 重新下载
                        this.download();
                    }
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
            lock.unLock();
        } finally {
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
        progress.setStart(skip);
        progress.setCurrent(skip);
        progress.start();
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(Files1.openOutputStreamFastSafe(local, true), bufferSize);
            int read;
            byte[] bs = new byte[bufferSize];
            while ((read = this.read(bs)) != -1) {
                progress.accept(read);
                out.write(bs, 0, read);
            }
            lock.unLock();
        } finally {
            Streams.close(out);
            this.transferFinish();
        }
    }

    /**
     * 获取远程文件大小
     *
     * @return fileSize 文件不存在则返回-1
     * @throws IOException IOException
     */
    protected abstract long getRemoteFileSize() throws IOException;

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

    @Override
    public ByteTransferRateProgress getProgress() {
        return progress;
    }

}