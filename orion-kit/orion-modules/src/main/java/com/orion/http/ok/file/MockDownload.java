package com.orion.http.ok.file;

import com.orion.http.ok.MockClient;
import com.orion.utils.Streams;
import com.orion.utils.Strings;
import com.orion.utils.Threads;
import com.orion.utils.io.FileLocks;
import com.orion.utils.io.Files1;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Mock 下载文件
 * 支持断点下载和进度 前提是服务器支持
 * 支持实时下载速率以及平均下载速率
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/9 14:13
 */
public class MockDownload {

    /**
     * url
     */
    private String url;

    /**
     * client
     */
    private OkHttpClient client = MockClient.getClient();

    /**
     * 缓冲区大小
     */
    private int buffSize = 1024 * 4;

    /**
     * 下载状态 0未开始 1下载中 2下载成功 3下载失败
     */
    private int downloadStatus;

    /**
     * 开始时间
     */
    private long startDate;

    /**
     * 结束时间
     */
    private long endDate;

    /**
     * body长度
     */
    private long bodyLength;

    /**
     * 当前下载量
     */
    private long nowLength;

    /**
     * 起始位置
     */
    private long startLength;

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
     * callback
     */
    private Callback callback;

    /**
     * call
     */
    private Call call;

    /**
     * request
     */
    private Request request;

    /**
     * response
     */
    private Response response;

    /**
     * exception
     */
    private Exception exception;

    /**
     * 文件锁
     */
    private FileLocks.SuffixFileLock lock;

    /**
     * 跳过的字节数
     */
    private long skipBytes;

    /**
     * 下载文件总大小的头
     */
    private String allSizeHeader = "Attachment-Size";

    /**
     * 断点下载跳过的头
     */
    private String skipSizeHeader = "Attachment-Skip";

    public MockDownload(String url) {
        this.url = url;
    }

    /**
     * 设置缓冲区大小
     *
     * @param buffSize 缓冲区大小
     * @return this
     */
    public MockDownload buffSize(int buffSize) {
        this.buffSize = buffSize;
        return this;
    }

    /**
     * 设置服务器返回文件大小的头信息
     *
     * @param allSizeHeader 头
     * @return this
     */
    public MockDownload allSizeHeader(String allSizeHeader) {
        this.allSizeHeader = allSizeHeader;
        return this;
    }

    /**
     * 设置断点下载跳过的头
     *
     * @param skipSizeHeader 头
     * @return this
     */
    public MockDownload skipSizeHeader(String skipSizeHeader) {
        this.skipSizeHeader = skipSizeHeader;
        return this;
    }

    /**
     * 开启计算实时速率
     *
     * @return this
     */
    public MockDownload openNowRate() {
        this.openNowRate = true;
        return this;
    }

    /**
     * 下载文件
     *
     * @param file 文件存放路径
     */
    public void download(File file) throws IOException {
        Files1.touch(file);
        this.request = new Request.Builder().url(this.url).build();
        this.download(Files1.openOutputStream(file), true);
    }

    /**
     * 下载文件
     *
     * @param file       文件存放路径
     * @param breakpoint 是否断点续传 前提是服务器支持
     */
    public void download(File file, boolean breakpoint) throws IOException {
        Files1.touch(file);
        if (breakpoint) {
            lock = FileLocks.getSuffixFileLock("httpdownload", file);
            if (lock.checkLock()) {
                long size = Files1.getSize(file);
                this.request = new Request.Builder().url(this.url).addHeader(skipSizeHeader, size + "").build();
                this.skipBytes = size;
                this.download(Files1.openOutputStream(file, true), true);
            } else {
                lock.tryLock();
                this.request = new Request.Builder().url(this.url).build();
                this.download(Files1.openOutputStream(file), true);
            }
        } else {
            this.request = new Request.Builder().url(this.url).build();
            this.download(Files1.openOutputStream(file), true);
        }
    }

    /**
     * 下载文件
     *
     * @param out 输出流
     */
    public void download(OutputStream out) {
        this.request = new Request.Builder().url(this.url).build();
        this.download(out, false);
    }

    /**
     * 下载文件
     *
     * @param out       输出流
     * @param skipBytes 跳过字节数, 前提是服务器支持
     */
    public void download(OutputStream out, long skipBytes) {
        if (skipBytes <= 0) {
            this.request = new Request.Builder().url(this.url).build();
        } else {
            this.skipBytes = skipBytes;
            this.request = new Request.Builder().url(this.url).addHeader(this.skipSizeHeader, this.skipSizeHeader + "").build();
        }
        this.download(out, false);
    }

    /**
     * 下载文件 会抛出异常
     *
     * @param out out
     */
    private void download(OutputStream out, boolean autoClose) {
        this.callback = new Callback() {

            @Override
            public void onFailure(Call c, IOException e) {
                call = c;
                exception = e;
                downloadStatus = 3;
            }

            @Override
            public void onResponse(Call c, Response r) throws IOException {
                call = c;
                response = r;
                downloadStatus = 1;
                String allSize = r.header(allSizeHeader);
                if (allSize != null && Strings.isNumber(allSize)) {
                    bodyLength = Long.valueOf(allSize);
                }
                InputStream in = null;
                byte[] buf = new byte[buffSize];
                try {
                    ResponseBody body = response.body();
                    if (body == null) {
                        return;
                    }
                    startDate = System.currentTimeMillis();
                    if (openNowRate) {
                        nowRateThread = new Thread(() -> {
                            while (getProgress() != 1) {
                                long size = nowLength;
                                Threads.sleep(1050);
                                nowRate = nowLength - size;
                            }
                        });
                        nowRateThread.setDaemon(true);
                        nowRateThread.start();
                    }
                    in = body.byteStream();
                    int len;
                    nowLength = skipBytes;
                    startLength = skipBytes;
                    while ((len = in.read(buf)) != -1) {
                        out.write(buf, 0, len);
                        nowLength += len;
                    }
                    downloadStatus = 2;
                    if (lock != null) {
                        lock.unLock();
                    }
                } catch (Exception e) {
                    exception = e;
                    downloadStatus = 3;
                    throw e;
                } finally {
                    endDate = System.currentTimeMillis();
                    Streams.closeQuietly(in);
                    if (autoClose) {
                        Streams.closeQuietly(out);
                    }
                }
            }

        };
        client.newCall(this.request).enqueue(this.callback);
    }

    public String getUrl() {
        return url;
    }

    public int getBuffSize() {
        return buffSize;
    }

    /**
     * 获取当前下载进度
     *
     * @return 当前进度 0 ~ 1
     */
    public double getProgress() {
        if (nowLength == 0 || bodyLength == 0) {
            return 0;
        }
        return (double) nowLength / (double) bodyLength;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public Callback getCallback() {
        return callback;
    }

    public Call getCall() {
        return call;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }

    public Exception getException() {
        return exception;
    }

    /**
     * 获取平均速度
     *
     * @return 平均速度
     */
    public double getAvgRate() {
        if (nowLength - startLength == 0) {
            return 0;
        }
        long useDate = endDate;
        if (endDate == 0) {
            useDate = System.currentTimeMillis();
        }
        double used = useDate - startDate;
        return ((nowLength - startLength) / used) * 1000;
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
     * 是否下载完毕
     *
     * @return true完毕
     */
    public boolean isDone() {
        return this.downloadStatus == 2;
    }

    /**
     * 是否下载失败
     *
     * @return true失败
     */
    public boolean isError() {
        return this.downloadStatus == 3;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public long getBodyLength() {
        return bodyLength;
    }

    public long getNowLength() {
        return nowLength;
    }

    public long getStartLength() {
        return startLength;
    }

}
