/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.http.ok.file;

import cn.orionsec.kit.http.BaseHttpRequest;
import cn.orionsec.kit.http.ok.BaseOkRequest;
import cn.orionsec.kit.http.ok.OkRequests;
import cn.orionsec.kit.http.ok.OkResponse;
import cn.orionsec.kit.http.support.HttpContentType;
import cn.orionsec.kit.http.support.HttpMethod;
import cn.orionsec.kit.http.support.HttpUploadPart;
import cn.orionsec.kit.lang.able.Awaitable;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.collect.Lists;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Collection;

/**
 * OkHttp 上传文件
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/9 14:09
 */
public class OkUpload extends BaseOkRequest implements Awaitable<OkResponse> {

    /**
     * 文件体
     */
    private Collection<HttpUploadPart> parts;

    /**
     * 是否执行完毕
     */
    private volatile boolean done;

    public OkUpload(String url) {
        this(url, OkRequests.getClient());
    }

    public OkUpload(String url, OkHttpClient client) {
        this.url = url;
        this.client = client;
        this.method = HttpMethod.POST.method();
    }

    @Override
    public OkUpload method(HttpMethod method) {
        return this.method(method.method());
    }

    @Override
    public OkUpload method(String method) {
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
    public OkUpload part(HttpUploadPart part) {
        this.parts = Lists.singleton(part);
        return this;
    }

    /**
     * 上传多文件
     *
     * @param parts ignore
     * @return this
     */
    public OkUpload parts(Collection<HttpUploadPart> parts) {
        this.parts = parts;
        return this;
    }

    @Override
    protected void setBody(Request.Builder requestBuilder) {
        super.setMultipartBody(requestBuilder, parts);
    }

    @Override
    public OkResponse await() {
        super.buildRequest();
        this.call = client.newCall(request);
        try (Response resp = call.execute()) {
            return new OkResponse(url, tag, resp);
        } catch (IOException e) {
            throw Exceptions.httpRequest(url, "ok request upload file on failure: " + super.getRequestMessage(), e);
        } finally {
            this.done = true;
        }
    }

    public boolean isDone() {
        return done;
    }

}
