package com.orion.remote.channel.executor.sftp.bigfile;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.orion.able.SafeCloseable;
import com.orion.remote.channel.executor.sftp.SftpExecutor;
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
 * @since 2020/10/13 18:43
 */
public class SftpUpload implements Runnable, SafeCloseable {

    /**
     * channel
     */
    private ChannelSftp channel;

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
     * 缓冲区大小
     */
    private static final int BUFFER_SIZE = 8 * 1024;

    public SftpUpload(ChannelSftp channel, String remote, String local) {
        this(channel, remote, new File(local));
    }

    public SftpUpload(ChannelSftp channel, String remote, File local) {
        this.channel = channel;
        this.remote = remote;
        this.local = local;
        this.lock = FileLocks.getSuffixFileLock("sftpupload", local);
    }

    /**
     * 打开连接
     *
     * @return this
     */
    public SftpUpload connect() {
        try {
            channel.connect();
            return this;
        } catch (Exception e) {
            throw Exceptions.connection(e);
        }
    }

    /**
     * 打开连接
     *
     * @param timeout 超时时间 ms
     * @return this
     */
    public SftpUpload connect(int timeout) {
        try {
            channel.connect(timeout);
            return this;
        } catch (Exception e) {
            throw Exceptions.connection(e);
        }
    }

    @Override
    public void run() {
        this.startTime = System.currentTimeMillis();
        InputStream in = null;
        OutputStream out = null;
        RandomAccessFile random = null;
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
            SftpATTRS fileAttr = null;
            try {
                fileAttr = channel.stat(remote);
            } catch (SftpException e) {
                // 文件不存在
                if (!new SftpExecutor(channel).touch(remote)) {
                    throw Exceptions.ioRuntime("Cannot create remote file");
                }
            }
            if (local.exists() && lock.checkLock()) {
                if (fileAttr == null) {
                    now = 0;
                    startSize = 0;
                    out = channel.put(remote);
                } else {
                    now = fileAttr.getSize();
                    startSize = fileAttr.getSize();
                    out = channel.put(remote, 2);
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
                    out.write(bs, 0, read);
                    now += read;
                }
                lock.unLock();
                Streams.close(random);
            } else {
                if (fileAttr != null) {
                    if (fileAttr.getSize() == size) {
                        return;
                    }
                }
                out = channel.put(remote);
                lock.tryLock();
                String parentPath = Files1.getParentPath(remote);
                if (!new SftpExecutor(channel).mkdirs(parentPath)) {
                    throw Exceptions.ioRuntime("Cannot create remote folder");
                }
                in = new BufferedInputStream(new FileInputStream(local));
                int read;
                byte[] bs = new byte[BUFFER_SIZE];
                while (-1 != (read = in.read(bs))) {
                    out.write(bs, 0, read);
                    now += read;
                }
                lock.unLock();
            }
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        } catch (SftpException e) {
            throw Exceptions.sftp(e);
        } finally {
            this.endTime = System.currentTimeMillis();
            this.done = true;
            Streams.close(in);
            Streams.close(out);
            Streams.close(random);
        }
    }

    @Override
    public void close() {
        channel.disconnect();
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
