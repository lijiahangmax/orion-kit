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
package cn.orionsec.kit.http.ok;

import cn.orionsec.kit.http.BaseHttpRequest;
import cn.orionsec.kit.http.support.HttpMethod;
import cn.orionsec.kit.http.support.HttpUploadPart;
import cn.orionsec.kit.lang.constant.StandardHttpHeader;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.io.Streams;
import okhttp3.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * OkHttp 请求基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/8 11:38
 */
public abstract class BaseOkRequest extends BaseHttpRequest {

    /**
     * tag
     */
    protected Object tag;

    /**
     * call
     */
    protected Call call;

    /**
     * 请求
     */
    protected Request request;

    /**
     * client
     */
    protected OkHttpClient client;

    public BaseOkRequest client(OkHttpClient client) {
        this.client = client;
        return this;
    }

    public BaseOkRequest tag(Object tag) {
        this.tag = tag;
        return this;
    }

    /**
     * 取消请求
     *
     * @return this
     */
    public BaseOkRequest cancel() {
        Valid.notNull(this.call, "request not call");
        this.call.cancel();
        return this;
    }

    @Override
    protected void buildRequest() {
        super.buildRequest();
        Request.Builder requestBuilder = new Request.Builder();
        HttpMethod.valid(method, 6);
        requestBuilder.url(url);
        // 设置 header
        if (headers != null) {
            headers.forEach(requestBuilder::addHeader);
        }
        // 设置 cookie
        if (cookies != null) {
            cookies.forEach(c -> requestBuilder.addHeader(StandardHttpHeader.COOKIE, c.toString()));
        }
        // 忽略的请求头
        if (ignoreHeaders != null) {
            ignoreHeaders.forEach(requestBuilder::removeHeader);
        }
        if (tag != null) {
            requestBuilder.tag(tag);
        }
        // 设置 body
        boolean noBody = super.isNoBodyRequest();
        if (noBody) {
            requestBuilder.method(method, null);
        } else {
            this.setBody(requestBuilder);
        }
        this.request = requestBuilder.build();
    }

    /**
     * 设置body
     *
     * @param requestBuilder builder
     */
    protected void setBody(Request.Builder requestBuilder) {
        if (body != null) {
            requestBuilder.method(method, RequestBody.create(MediaType.parse(contentType), body, bodyOffset, bodyLen));
        } else if (formParts != null) {
            FormBody.Builder formBuilder = new FormBody.Builder(Charset.forName(charset));
            formParts.forEach(formBuilder::addEncoded);
            requestBuilder.method(method, formBuilder.build());
        } else {
            requestBuilder.method(method, RequestBody.create(MediaType.parse(contentType), Strings.EMPTY));
        }
    }

    /**
     * 设置二进制body
     *
     * @param requestBuilder builder
     * @param parts          parts
     */
    protected void setMultipartBody(Request.Builder requestBuilder, Collection<HttpUploadPart> parts) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (this.formParts != null) {
            formParts.forEach(builder::addFormDataPart);
        }
        for (HttpUploadPart part : parts) {
            if (part == null) {
                continue;
            }
            RequestBody body = null;
            if (part.getIn() != null) {
                try {
                    body = RequestBody.create(MediaType.parse(part.getContentType()), Streams.toByteArray(part.getIn()));
                } catch (IOException e) {
                    throw Exceptions.ioRuntime("set upload file error", e);
                }
            } else if (part.getFile() != null) {
                body = RequestBody.create(MediaType.parse(part.getContentType()), part.getFile());
            } else if (part.getBytes() != null) {
                body = RequestBody.create(MediaType.parse(part.getContentType()), part.getBytes(), part.getOff(), part.getLen());
            }
            if (body != null) {
                builder.addFormDataPart(part.getParam(), part.getFileName(), body);
            }
        }
        requestBuilder.method(method, builder.build());
    }

    /**
     * @return 获取请求信息
     */
    protected String getRequestMessage() {
        StringBuilder builder = new StringBuilder()
                .append(request.method())
                .append(Strings.SPACE)
                .append(request.url());
        Object tag = request.tag();
        if (tag != null) {
            builder.append(" tag: ")
                    .append(tag);
        }
        return builder.toString();
    }

    public Request getRequest() {
        return request;
    }

    public Call getCall() {
        return call;
    }

    public Object getTag() {
        return tag;
    }

    public OkHttpClient getClient() {
        return client;
    }

}
