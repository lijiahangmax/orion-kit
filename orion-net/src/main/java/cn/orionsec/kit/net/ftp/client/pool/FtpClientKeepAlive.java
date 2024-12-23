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
package cn.orionsec.kit.net.ftp.client.pool;

import cn.orionsec.kit.lang.able.SafeCloseable;
import cn.orionsec.kit.lang.utils.Threads;
import org.apache.commons.net.ftp.FTPClient;

import java.util.concurrent.BlockingQueue;

/**
 * FTP 监听长连接心跳
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/18 12:54
 */
public class FtpClientKeepAlive implements SafeCloseable {

    /**
     * 线程池
     */
    private final FtpClientPool pool;

    /**
     * 心跳检测间隔
     */
    private int heartCheckTime;

    /**
     * 运行状态
     */
    private volatile boolean flag;

    protected FtpClientKeepAlive(FtpClientPool pool) {
        this.pool = pool;
    }

    /**
     * 监听心跳
     */
    protected void listener(int heartCheckTime) {
        this.heartCheckTime = heartCheckTime;
        this.flag = true;
        Threads.CACHE_EXECUTOR.execute(new FtpClientKeepAliveListener());
    }

    @Override
    public void close() {
        flag = false;
    }

    private class FtpClientKeepAliveListener implements Runnable {

        @Override
        public void run() {
            FTPClient ftpClient;
            while (flag) {
                BlockingQueue<FTPClient> queue = pool.getPool();
                if (queue != null && queue.size() > 0) {
                    for (FTPClient q : queue) {
                        ftpClient = q;
                        try {
                            if (!ftpClient.sendNoOp()) {
                                pool.invalidClient(ftpClient);
                                pool.addClient();
                            }
                        } catch (Exception e) {
                            pool.invalidClient(ftpClient);
                            pool.addClient();
                        }
                    }
                }
                Threads.sleep(heartCheckTime);
            }
        }
    }

}
