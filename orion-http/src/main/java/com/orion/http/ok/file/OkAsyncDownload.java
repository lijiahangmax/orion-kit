package com.orion.http.ok.file;

import com.orion.able.Asyncable;
import com.orion.http.ok.BaseOkRequest;
import com.orion.http.ok.OkClient;
import com.orion.http.ok.OkResponse;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;

/**
 * OkHttp 异步下载文件
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/9 14:13
 */
public class OkAsyncDownload extends BaseOkRequest implements Asyncable<Consumer<OkResponse>> {

    /**
     * 下载的流
     */
    private OutputStream out;

    /**
     * 是否自动关闭
     */
    private boolean autoClose;

    /**
     * 异步执行失败是否抛出异常
     */
    private boolean asyncFailThrows;

    /**
     * 异步传输回调接口
     */
    private Consumer<OkResponse> callback;

    /**
     * 开始时间
     */
    private long startTime;

    /**
     * 结束时间
     */
    private long endTime;

    public OkAsyncDownload(String url) {
        this.url = url;
        this.client = OkClient.getClient();
    }

    public OkAsyncDownload(String url, OkHttpClient client) {
        this.url = url;
        this.client = client;
    }

    public OkAsyncDownload asyncFailThrows(boolean asyncFailThrows) {
        this.asyncFailThrows = asyncFailThrows;
        return this;
    }

    public OkAsyncDownload asyncFailThrows() {
        this.asyncFailThrows = true;
        return this;
    }

    /**
     * 下载文件
     *
     * @param file 文件存放路径
     * @return this
     */
    public OkAsyncDownload download(String file) {
        Files1.touch(file);
        this.out = Files1.openOutputStreamSafe(file);
        this.autoClose = true;
        return this;
    }

    /**
     * 下载文件
     *
     * @param file 文件存放路径
     * @return this
     */
    public OkAsyncDownload download(File file) {
        Files1.touch(file);
        this.out = Files1.openOutputStreamSafe(file);
        this.autoClose = true;
        return this;
    }

    /**
     * 下载文件
     *
     * @param out 输出流
     * @return this
     */
    public OkAsyncDownload download(OutputStream out) {
        this.out = out;
        this.autoClose = false;
        return this;
    }

    /**
     * 下载文件
     *
     * @param out       输出流
     * @param autoClose 是否自动关闭
     * @return this
     */
    public OkAsyncDownload download(OutputStream out, boolean autoClose) {
        this.out = out;
        this.autoClose = autoClose;
        return this;
    }

    @Override
    public void async(Consumer<OkResponse> callback) {
        Valid.notNull(callback, "async call back is null");
        this.callback = callback;
        this.execute();
    }

    @Override
    protected void execute() {
        super.buildRequest();
        call = client.newCall(request);
        response = new OkResponse(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response res) throws IOException {
                response.responseDownload(res);
                if (response.isOk()) {
                    Streams.transfer(res.body().byteStream(), out);
                }
                callback.accept(response);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                response.error(e);
                if (asyncFailThrows) {
                    throw Exceptions.httpRequest("async ok download file on failure: " + OkAsyncDownload.super.getRequestMessage(), e);
                }
                callback.accept(response);
            }
        });
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

}
