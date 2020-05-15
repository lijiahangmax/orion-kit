package com.orion.storage.ftp.bigfile;

import com.orion.storage.ftp.FtpFileAttr;
import com.orion.storage.ftp.FtpInstance;
import com.orion.utils.Exceptions;
import com.orion.utils.Streams;
import com.orion.utils.Threads;
import com.orion.utils.file.FileLocks;
import com.orion.utils.file.Files1;

import java.io.*;

/**
 * FTP 大文件下载
 * 支持断点续传, 实时速率, 平均速率
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/3/22 14:32
 */
public class FtpDownload implements Runnable {

    /**
     * 开始时间
     */
    private long startTime;

    /**
     * 结束时间
     */
    private long endTime;

    /**
     * 下载时文件大小
     */
    private long startSize;

    /**
     * 文件总大小
     */
    private long size;

    /**
     * 当前传输大小
     */
    private long now;

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
    private FileLocks.SuffixFileLock lock;

    /**
     * 实例
     */
    private FtpInstance instance;

    /**
     * 实时速率
     */
    private long nowRate;

    /**
     * 开启实时速率
     */
    private boolean openNowRate = false;

    /**
     * 实时速率线程
     */
    private Thread nowRateThread;

    /**
     * 是否已完成
     */
    private boolean done;

    public FtpDownload(FtpInstance instance, String remote, File local) {
        this.instance = instance;
        this.remote = remote;
        this.local = local;
        this.lock = FileLocks.getSuffixFileLock("ftpdownload", local);
    }

    @Override
    public void run() {
        this.startTime = System.currentTimeMillis();
        InputStream in = null;
        OutputStream out = null;
        try {
            if (openNowRate) {
                nowRateThread = new Thread(() -> {
                    while (getProgress() != 1) {
                        long size = now;
                        Threads.sleep(950);
                        nowRate = now - size;
                    }
                });
                nowRateThread.setDaemon(true);
                nowRateThread.start();
            }
            FtpFileAttr fileAttr = instance.getFileAttr(remote);
            size = fileAttr.getSize();
            if (local.exists() && lock.checkLock()) {
                now = local.length();
                startSize = now;
                if (startSize >= size) {
                    lock.unLock();
                    return;
                }
                instance.client().setRestartOffset(startSize);
                out = new BufferedOutputStream(new FileOutputStream(local, true));
                in = new BufferedInputStream(instance.client().retrieveFileStream(instance.serverCharset(instance.config().getRemoteBaseDir() + remote)));
                int read;
                byte[] bs = new byte[instance.config().getBuffSize()];
                while (-1 != (read = in.read(bs))) {
                    now += read;
                    out.write(bs, 0, read);
                }
                lock.unLock();
            } else {
                if (local.exists() && size == local.length()) {
                    return;
                }
                Files1.touch(local);
                lock.tryLock();
                in = new BufferedInputStream(instance.client().retrieveFileStream(instance.serverCharset(instance.config().getRemoteBaseDir() + remote)));
                out = new BufferedOutputStream(new FileOutputStream(local));
                int read;
                byte[] bs = new byte[instance.config().getBuffSize()];
                while (-1 != (read = in.read(bs))) {
                    now += read;
                    out.write(bs, 0, read);
                }
                lock.unLock();
            }
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            this.endTime = System.currentTimeMillis();
            this.done = true;
            Streams.closeQuietly(in);
            Streams.closeQuietly(out);
            instance.client().setRestartOffset(0);
            try {
                if (in != null) {
                    instance.client().completePendingCommand();
                }
            } catch (IOException e) {
                throw Exceptions.ioRuntime(e);
            }
        }
    }

    /**
     * 开启计算实时速率
     *
     * @return this
     */
    public FtpDownload openNowRate() {
        this.openNowRate = true;
        return this;
    }

    /**
     * 获取平均速度
     *
     * @return 平均速度
     */
    public double getAvgRate() {
        long useDate = endTime;
        if (endTime == 0) {
            useDate = System.currentTimeMillis();
        }
        double used = useDate - startTime;
        double uploadBytes = now - startSize;
        return (uploadBytes / used) * 1000;
    }

    /**
     * 获取当前速度
     *
     * @return 当前速度
     */
    public long getNowRate() {
        return nowRate;
    }

    /**
     * 获取当前进度
     *
     * @return 进度 0 ~ 1
     */
    public double getProgress() {
        return size == 0 ? 0 : (double) now / (double) size;
    }

    /**
     * 是否传输完成
     *
     * @return true完成
     */
    public boolean isDone() {
        return done;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getStartSize() {
        return startSize;
    }

    public long getSize() {
        return size;
    }

    public long getNow() {
        return now;
    }

}