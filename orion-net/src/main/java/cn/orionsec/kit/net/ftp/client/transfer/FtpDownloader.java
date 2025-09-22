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
package cn.orionsec.kit.net.ftp.client.transfer;

import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.net.ftp.client.FtpFile;
import cn.orionsec.kit.net.ftp.client.instance.IFtpInstance;
import cn.orionsec.kit.net.specification.transfer.BaseFileDownloader;

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

    private static final String LOCK_SUFFIX = "ofd";

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
        Assert.notNull(instance, "ftp instance is null");
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
    protected long getRemoteFileSize() {
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