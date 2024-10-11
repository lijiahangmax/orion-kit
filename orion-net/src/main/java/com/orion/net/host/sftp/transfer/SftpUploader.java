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
package com.orion.net.host.sftp.transfer;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.Streams;
import com.orion.net.host.sftp.SftpExecutor;
import com.orion.net.host.sftp.SftpFile;
import com.orion.net.specification.transfer.BaseFileUploader;

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
public class SftpUploader extends BaseFileUploader {

    private static final String LOCK_SUFFIX = "osu";

    /**
     * sftp 执行器
     */
    private final SftpExecutor executor;

    /**
     * 输出流
     */
    private OutputStream out;

    public SftpUploader(SftpExecutor executor, String remote, String local) {
        this(executor, remote, new File(local));
    }

    public SftpUploader(SftpExecutor executor, String remote, File local) {
        super(remote, local, LOCK_SUFFIX, Const.BUFFER_KB_8);
        Valid.notNull(executor, "sftp executor is null");
        this.executor = executor;
    }

    @Override
    public void run() {
        try {
            synchronized (executor) {
                super.startUpload();
            }
        } catch (IOException e) {
            throw Exceptions.sftp("sftp upload exception local file: " + local.getAbsolutePath() + " -> remote file: " + remote, e);
        }
    }

    @Override
    protected long getRemoteFileSize() {
        SftpFile remoteFile = executor.getFile(remote);
        if (remoteFile == null) {
            try {
                // 打开流时会自动创建 这里无需创建文件
                // executor.touchTruncate(remote);
                return -1;
            } catch (Exception e) {
                throw Exceptions.sftp("touch remote file error > " + remote, e);
            }
        }
        return remoteFile.getSize();
    }

    @Override
    protected void initUpload(boolean breakPoint, long skip) throws IOException {
        if (breakPoint) {
            this.out = executor.openOutputStream(remote, true);
        } else {
            this.out = executor.openOutputStream(remote);
        }
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
        Streams.close(out);
    }

    @Override
    public void abort() {
        Streams.close(executor);
    }

    public SftpExecutor getExecutor() {
        return executor;
    }

}
