package com.orion.ftp.client.pool;

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
public class FtpClientKeepAlive {

    /**
     * 线程池
     */
    private FtpClientPool pool;

    /**
     * 心跳检测间隔
     */
    private int heatTime;

    /**
     * 心跳检测线程
     */
    private Thread thread;

    /**
     * 运行状态
     */
    private boolean flag = true;

    FtpClientKeepAlive(FtpClientPool pool) {
        this.pool = pool;
        this.thread = new Thread(new FtpClientKeepAliveListener());
        this.thread.setDaemon(true);
    }

    /**
     * 监听心跳
     */
    void listener(int heatTime) {
        this.heatTime = heatTime;
        flag = true;
        if (thread.getState() == Thread.State.TERMINATED) {
            thread = new Thread(new FtpClientKeepAliveListener());
            thread.setDaemon(true);
            thread.start();
        } else if (thread.getState() == Thread.State.NEW) {
            thread.start();
        }
    }

    /**
     * 结束监听
     */
    void stop() {
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
                                pool.invalidateClient(ftpClient);
                                pool.addClient();
                            }
                        } catch (Exception e) {
                            pool.invalidateClient(ftpClient);
                            pool.addClient();
                        }
                    }
                }
                Threads.sleep(heatTime);
            }
        }
    }

}
