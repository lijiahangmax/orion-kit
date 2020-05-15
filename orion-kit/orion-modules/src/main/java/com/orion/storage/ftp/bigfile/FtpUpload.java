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
 * FTP 大文件上传
 * 支持断点续传, 实时速率, 平均速率
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/3/21 20:12
 */
public class FtpUpload implements Runnable {

    /**
     * 开始时间
     */
    private long startTime;

    /**
     * 结束时间
     */
    private long endTime;

    /**
     * 上传时文件大小
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

    public FtpUpload(FtpInstance instance, String remote, File local) {
        this.instance = instance;
        this.remote = remote;
        this.local = local;
        this.lock = FileLocks.getSuffixFileLock("ftpupload", local);
    }

    @Override
    public void run() {
        this.startTime = System.currentTimeMillis();
        InputStream in = null;
        OutputStream out = null;
        size = local.length();
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
            if (local.exists() && lock.checkLock()) {
                now = fileAttr.getSize();
                startSize = fileAttr.getSize();
                if (now >= size) {
                    lock.unLock();
                    return;
                }
                RandomAccessFile random = new RandomAccessFile(local, "r");
                random.seek(startSize);
                out = instance.client().appendFileStream(instance.serverCharset(instance.config().getRemoteBaseDir() + remote));
                int read;
                byte[] bs = new byte[instance.config().getBuffSize()];
                while (-1 != (read = random.read(bs))) {
                    now += read;
                    out.write(bs, 0, read);
                }
                lock.unLock();
                Streams.closeQuietly(random);
            } else {
                if (fileAttr != null) {
                    if (fileAttr.getSize() == size) {
                        return;
                    }
                }
                lock.tryLock();
                String parentPath = Files1.getParentPath(remote);
                instance.mkdirs(parentPath);
                out = instance.client().storeFileStream(instance.serverCharset(instance.config().getRemoteBaseDir() + remote));
                in = new BufferedInputStream(new FileInputStream(local));
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
            Streams.closeQuietly(in);
            Streams.closeQuietly(out);
            done = true;
            try {
                if (out != null) {
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
    public FtpUpload openNowRate() {
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
