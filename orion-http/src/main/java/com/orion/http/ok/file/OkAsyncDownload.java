package com.orion.http.ok.file;

import com.orion.http.ok.BaseOkRequest;
import com.orion.http.ok.OkRequests;
import com.orion.http.ok.OkResponse;
import com.orion.lang.able.Asyncable;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.Streams;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;

/**
 * OkHttp 异步下载文件
 *
 * @author Jiahang Li
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

    public OkAsyncDownload(String url) {
        this(url, OkRequests.getClient());
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
        super.buildRequest();
        this.call = client.newCall(request);
        OkResponse response = new OkResponse(url, tag);
        this.call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response res) throws IOException {
                response.asyncSetResponse(res, false);
                if (response.isOk()) {
                    ResponseBody body = res.body();
                    if (body != null) {
                        try {
                            Streams.transfer(body.byteStream(), out);
                        } finally {
                            if (autoClose) {
                                Streams.close(out);
                            }
                        }
                    }
                }
                callback.accept(response);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                response.error(e);
                if (autoClose) {
                    Streams.close(out);
                }
                if (asyncFailThrows) {
                    throw Exceptions.httpRequest(url, "async ok download file on failure: " + OkAsyncDownload.super.getRequestMessage(), e);
                }
                callback.accept(response);
            }
        });
    }

}
