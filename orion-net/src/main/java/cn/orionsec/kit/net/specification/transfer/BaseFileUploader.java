/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.net.specification.transfer;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.support.progress.ByteTransferRateProgress;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.io.FileLocks;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;

import java.io.*;

/**
 * 大文件上传 基类 支持断点续传, 实时速率
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/14 00:07
 */
public abstract class BaseFileUploader implements IFileUploader {

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
     * 是否强制覆盖上传 (不检测文件锁定/不检测文件大小/不走断点续传)
     */
    protected boolean forceOverride;

    /**
     * 文件大小相同则覆盖上传
     */
    protected boolean fileSizeEqualOverride;

    /**
     * 远程文件大小 缓存
     */
    protected Long remoteFileLength;

    protected BaseFileUploader(String remote, File local, String lockSuffix, int bufferSize) {
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
     * 开始上传
     *
     * @throws IOException IOException
     */
    protected void startUpload() throws IOException {
        boolean error = false;
        try {
            // 强制覆盖上传
            if (forceOverride) {
                this.upload();
                return;
            }
            // 检测文件大小
            long remoteSize = this.getRemoteFileLength();
            if (remoteSize == -1) {
                // 远程文件为空 直接上传
                this.upload();
            } else {
                if (remoteSize == local.length()) {
                    // 文件大小一样 检测是否覆盖上传
                    if (fileSizeEqualOverride) {
                        // 如果设置文件大小一致覆盖 则重新上传
                        this.upload();
                    } else {
                        // 认为是文件相同(大文件节约性能) 则跳过
                        lock.unLock();
                        progress.startTime(System.currentTimeMillis());
                        this.transferFinish();
                    }
                } else {
                    // 文件大小不一样 检测是否断点续传
                    if (lock.isLocked()) {
                        // 被锁定 继续上传
                        this.breakPointResume(remoteSize);
                    } else {
                        // 没被锁定 重新上传
                        this.upload();
                    }
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
    protected abstract long getRemoteFileSize() throws IOException;

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

    @Override
    public ByteTransferRateProgress getProgress() {
        return progress;
    }

}
