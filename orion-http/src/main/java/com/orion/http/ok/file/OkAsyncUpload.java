package com.orion.http.ok.file;

import com.orion.able.Asyncable;
import com.orion.http.BaseHttpRequest;
import com.orion.http.ok.BaseOkRequest;
import com.orion.http.ok.OkClient;
import com.orion.http.ok.OkResponse;
import com.orion.http.support.HttpContentType;
import com.orion.http.support.HttpMethod;
import com.orion.http.support.HttpUploadPart;
import com.orion.utils.Exceptions;
import com.orion.utils.Valid;
import com.orion.utils.collect.Lists;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

/**
 * OkHttp 异步上传文件
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/3/8 15:35
 */
public class OkAsyncUpload extends BaseOkRequest implements Asyncable<Consumer<OkResponse>> {

    /**
     * 文件体
     */
    private List<HttpUploadPart> parts;

    /**
     * 异步执行失败是否抛出异常
     */
    private boolean asyncFailThrows;

    /**
     * 异步传输回调接口
     */
    private Consumer<OkResponse> callback;

    public OkAsyncUpload(String url) {
        this.url = url;
        this.client = OkClient.getClient();
    }

    public OkAsyncUpload(String url, OkHttpClient client) {
        this.url = url;
        this.client = client;
    }

    public OkAsyncUpload asyncFailThrows(boolean asyncFailThrows) {
        this.asyncFailThrows = asyncFailThrows;
        return this;
    }

    public OkAsyncUpload asyncFailThrows() {
        this.asyncFailThrows = true;
        return this;
    }

    @Override
    public OkAsyncUpload method(HttpMethod method) {
        return this.method(method.method());
    }

    @Override
    public OkAsyncUpload method(String method) {
        this.method = method;
        if (super.isNoBodyRequest()) {
            throw Exceptions.unsupported("unsupported method " + method);
        }
        return this;
    }

    @Override
    public BaseHttpRequest body(String body) {
        throw Exceptions.unsupported("upload file unsupported set body");
    }

    @Override
    public BaseHttpRequest body(byte[] body) {
        throw Exceptions.unsupported("upload file unsupported set body");
    }

    @Override
    public BaseHttpRequest body(byte[] body, int offset, int len) {
        throw Exceptions.unsupported("upload file unsupported set body");
    }

    @Override
    public BaseHttpRequest contentType(String contentType) {
        throw Exceptions.unsupported("upload file unsupported set contentType");
    }

    @Override
    public BaseHttpRequest contentType(HttpContentType contentType) {
        throw Exceptions.unsupported("upload file unsupported set contentType");
    }

    /**
     * 上传单文件
     *
     * @param part ignore
     * @return this
     */
    public OkAsyncUpload part(HttpUploadPart part) {
        this.parts = Lists.singleton(part);
        return this;
    }

    /**
     * 上传多文件
     *
     * @param parts ignore
     * @return this
     */
    public OkAsyncUpload parts(List<HttpUploadPart> parts) {
        this.parts = parts;
        return this;
    }

    @Override
    protected void setBody(Request.Builder requestBuilder) {
        super.setMultipartBody(requestBuilder, parts);
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
            public void onResponse(Call call, Response res) {
                response.response(res);
                callback.accept(response);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                response.error(e);
                if (asyncFailThrows) {
                    throw Exceptions.httpRequest("async ok upload file on failure: " + OkAsyncUpload.super.getRequestMessage(), e);
                }
                callback.accept(response);
            }
        });
    }

}
