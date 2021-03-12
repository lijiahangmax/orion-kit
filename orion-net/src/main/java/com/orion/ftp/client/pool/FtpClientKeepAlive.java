package com.orion.ftp.client.pool;

import com.orion.able.SafeCloseable;
import com.orion.utils.Threads;
import org.apache.commons.net.ftp.FTPClient;

import java.util.concurrent.BlockingQueue;

/**
 * FTP监听长连接心跳
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/18 12:54
 */
public class FtpClientKeepAlive implements SafeCloseable {

    /**
     * 线程池
     */
    private FtpClientPool pool;

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
