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
package cn.orionsec.kit.http.apache.file;

import cn.orionsec.kit.http.BaseHttpRequest;
import cn.orionsec.kit.http.apache.ApacheRequests;
import cn.orionsec.kit.http.apache.ApacheResponse;
import cn.orionsec.kit.http.apache.BaseApacheRequest;
import cn.orionsec.kit.http.support.HttpContentType;
import cn.orionsec.kit.http.support.HttpMethod;
import cn.orionsec.kit.http.support.HttpUploadPart;
import cn.orionsec.kit.lang.constant.StandardContentType;
import cn.orionsec.kit.lang.utils.Charsets;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.collect.Lists;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * Apache Http 文件上传
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/6/13 1:03
 */
public class ApacheUpload extends BaseApacheRequest {

    /**
     * 文件体
     */
    private Collection<HttpUploadPart> parts;

    /**
     * 是否执行完毕
     */
    private volatile boolean done;

    public ApacheUpload(String url) {
        this(url, ApacheRequests.getClient());
    }

    public ApacheUpload(String url, CloseableHttpClient client) {
        this.url = url;
        this.client = client;
        this.method = HttpMethod.POST.method();
    }

    @Override
    public ApacheUpload method(HttpMethod method) {
        return this.method(method.method());
    }

    @Override
    public ApacheUpload method(String method) {
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
     */
    public ApacheUpload part(HttpUploadPart part) {
        this.parts = Lists.singleton(part);
        return this;
    }

    /**
     * 上传多文件
     *
     * @param parts ignore
     */
    public ApacheUpload parts(Collection<HttpUploadPart> parts) {
        this.parts = parts;
        return this;
    }

    @Override
    protected HttpEntity getEntry() {
        Valid.notEmpty(parts, "upload part is empty");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        if (formParts != null) {
            formParts.forEach((k, v) -> {
                builder.addTextBody(k, v, ContentType.create(StandardContentType.TEXT_PLAIN, Charsets.of(charset)));
            });
        }
        for (HttpUploadPart part : parts) {
            String key = part.getParam();
            String contentType = part.getContentType();
            File file = part.getFile();
            String fileName = part.getFileName();
            byte[] bytes = part.getBytes();
            InputStream in = part.getIn();
            if (in != null) {
                builder.addBinaryBody(key, in, ContentType.parse(contentType), fileName);
            } else if (file != null) {
                builder.addBinaryBody(key, file, ContentType.parse(contentType), fileName);
            } else if (bytes != null) {
                builder.addBinaryBody(key, bytes, ContentType.parse(contentType), fileName);
            }
        }
        return builder.build();
    }

    @Override
    public ApacheResponse await() {
        super.buildRequest();
        try (CloseableHttpResponse resp = client.execute(request)) {
            return new ApacheResponse(url, resp);
        } catch (IOException e) {
            throw Exceptions.httpRequest(url, e);
        } finally {
            this.done = true;
            this.request.releaseConnection();
        }
    }

    public boolean isDone() {
        return done;
    }

}
