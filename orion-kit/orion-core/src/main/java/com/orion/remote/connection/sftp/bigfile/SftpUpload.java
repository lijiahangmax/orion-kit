package com.orion.remote.connection.sftp.bigfile;

import ch.ethz.ssh2.SFTPv3Client;
import ch.ethz.ssh2.SFTPv3FileAttributes;
import ch.ethz.ssh2.SFTPv3FileHandle;
import com.orion.able.SafeCloseable;
import com.orion.remote.connection.sftp.SftpExecutor;
import com.orion.utils.Exceptions;
import com.orion.utils.Threads;
import com.orion.utils.io.FileLocks;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import java.io.*;

/**
 * SFTP 大文件上传
 * 支持断点续传, 实时速率, 平均速率
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/5/14 15:07
 */
public class SftpUpload implements Runnable, SafeCloseable {

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
    private FileLocks.NamedFileLock lock;

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
     * 是否已完成
     */
    private volatile boolean done;

    /**
     * 是否已关闭
     */
    private boolean close;

    /**
     * 一次 写入 最大长度
     */
    private static final int BUFFER_SIZE = 32 * 1024;

    public SftpUpload(SFTPv3Client client, String remote, String local) {
        this(client, remote, new File(local));
    }

    public SftpUpload(SFTPv3Client client, String remote, File local) {
        this.client = client;
        this.remote = remote;
        this.local = local;
        this.lock = FileLocks.getSuffixFileLock("sftpupload", local);
    }

    @Override
    public void run() {
        this.startTime = System.currentTimeMillis();
        InputStream in = null;
        RandomAccessFile random = null;
        SFTPv3FileHandle writeHandler = null;
        size = local.length();
        try {
            if (openNowRate) {
                Threads.start(() -> {
                    while (!done) {
                        long size = now;
                        Threads.sleep(1000);
                        nowRate = now - size;
                    }
                });
            }
            SFTPv3FileAttributes fileAttr = null;

            try {
                fileAttr = client.stat(remote);
                writeHandler = client.openFileRW(remote);
            } catch (Exception e) {
                // 文件不存在
                writeHandler = client.createFile(remote);
            }
            if (local.exists() && lock.checkLock()) {
                if (fileAttr == null) {
                    now = 0;
                    startSize = 0;
                } else {
                    now = fileAttr.size;
                    startSize = fileAttr.size;
                }
                if (now >= size) {
                    lock.unLock();
                    return;
                }
                random = new RandomAccessFile(local, "r");
                random.seek(startSize);
                int read;
                byte[] bs = new byte[BUFFER_SIZE];
                while (-1 != (read = random.read(bs))) {
                    client.write(writeHandler, now, bs, 0, read);
                    now += read;
                }
                lock.unLock();
                Streams.close(random);
            } else {
                if (fileAttr != null) {
                    if (fileAttr.size == size) {
                        return;
                    }
                }
                lock.tryLock();
                String parentPath = Files1.getParentPath(remote);
                boolean mkdirs = new SftpExecutor(client).mkdirs(parentPath);
                if (!mkdirs) {
                    throw Exceptions.ioRuntime("Cannot create remote folder");
                }
                in = new BufferedInputStream(new FileInputStream(local));
                int read;
                byte[] bs = new byte[BUFFER_SIZE];
                while (-1 != (read = in.read(bs))) {
                    client.write(writeHandler, now, bs, 0, read);
                    now += read;
                }
                lock.unLock();
            }
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            this.endTime = System.currentTimeMillis();
            this.done = true;
            Streams.close(in);
            Streams.close(random);
            if (writeHandler != null) {
                try {
                    writeHandler.getClient().closeFile(writeHandler);
                } catch (IOException e) {
                    Exceptions.printStacks(e);
                }
            }
        }
    }

    @Override
    public void close() {
        close = true;
        client.close();
    }

    /**
     * 开启计算实时速率
     *
     * @return this
     */
    public SftpUpload openNowRate() {
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

    public boolean isClose() {
        return close;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getUseTime() {
        return endTime - startTime;
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
