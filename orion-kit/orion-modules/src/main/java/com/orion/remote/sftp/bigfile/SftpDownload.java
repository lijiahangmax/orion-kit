package com.orion.remote.sftp.bigfile;

import ch.ethz.ssh2.SFTPv3Client;
import ch.ethz.ssh2.SFTPv3FileAttributes;
import ch.ethz.ssh2.SFTPv3FileHandle;
import com.orion.utils.Exceptions;
import com.orion.utils.Streams;
import com.orion.utils.Threads;
import com.orion.utils.file.FileLocks;
import com.orion.utils.file.Files1;

import java.io.*;

/**
 * SFTP 大文件下载
 * 支持断点续传, 实时速率, 平均速率
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/14 14:34
 */
public class SftpDownload implements Runnable {

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
    private SFTPv3Client client;

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

    /**
     * 一次 写入/读取 最大长度
     */
    private final int WRITE_MAX_SIZE = 32768;

    public SftpDownload(SFTPv3Client client, String remote, File local) {
        this.client = client;
        this.remote = remote;
        this.local = local;
        this.lock = FileLocks.getSuffixFileLock("sftpdownload", local);
    }

    @Override
    public void run() {
        this.startTime = System.currentTimeMillis();
        OutputStream out = null;
        SFTPv3FileHandle readHandler = null;
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
            SFTPv3FileAttributes fileAttr;
            try {
                fileAttr = client.stat(remote);
            } catch (Exception e) {
                throw Exceptions.notFound("Not found remote file: " + remote);
            }
            size = fileAttr.size;
            readHandler = client.openFileRO(remote);
            if (local.exists() && lock.checkLock()) {
                now = local.length();
                startSize = now;
                if (startSize >= size) {
                    lock.unLock();
                    return;
                }
                out = new BufferedOutputStream(new FileOutputStream(local, true));
                int read;
                byte[] bs = new byte[WRITE_MAX_SIZE];
                while (-1 != (read = client.read(readHandler, now, bs, 0, WRITE_MAX_SIZE))) {
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
                out = new BufferedOutputStream(new FileOutputStream(local));
                int read;
                byte[] bs = new byte[WRITE_MAX_SIZE];
                while (-1 != (read = client.read(readHandler, now, bs, 0, WRITE_MAX_SIZE))) {
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
            Streams.closeQuietly(out);
            try {
                if (readHandler != null) {
                    readHandler.getClient().closeFile(readHandler);
                }
            } catch (IOException e) {
                // ignore
            }
        }
    }

    /**
     * 开启计算实时速率
     *
     * @return this
     */
    public SftpDownload openNowRate() {
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
