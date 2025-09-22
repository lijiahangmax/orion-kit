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
package cn.orionsec.kit.http.apache;

import cn.orionsec.kit.http.BaseHttpRequest;
import cn.orionsec.kit.http.support.HttpContentType;
import cn.orionsec.kit.http.support.HttpMethod;
import cn.orionsec.kit.lang.able.Awaitable;
import cn.orionsec.kit.lang.constant.StandardContentType;
import cn.orionsec.kit.lang.constant.StandardHttpHeader;
import cn.orionsec.kit.lang.utils.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * ApacheHttp 请求 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/7 17:20
 */
public abstract class BaseApacheRequest extends BaseHttpRequest implements Awaitable<ApacheResponse> {

    /**
     * client
     */
    protected CloseableHttpClient client;

    /**
     * HttpRequest
     */
    protected HttpRequestBase request;

    /**
     * client
     *
     * @param client client
     * @return this
     */
    public BaseApacheRequest client(CloseableHttpClient client) {
        this.client = client;
        return this;
    }

    /**
     * 取消请求
     *
     * @return this
     */
    public BaseApacheRequest cancel() {
        this.request.abort();
        return this;
    }

    @Override
    protected void buildRequest() {
        // 构建请求路径
        super.buildRequest();
        // 获取请求方法
        this.getRequestByMethod();
        // 设置 header
        if (headers != null) {
            headers.forEach((k, v) -> request.addHeader(new BasicHeader(k, v)));
        }
        // 设置 cookie
        if (cookies != null) {
            cookies.forEach(c -> request.addHeader(new BasicHeader(StandardHttpHeader.COOKIE, c.toString())));
        }
        // 忽略的请求头
        if (ignoreHeaders != null) {
            ignoreHeaders.forEach(ignoreHeader -> request.removeHeader(new BasicHeader(ignoreHeader, null)));
        }
        // 设置 contentType
        if (!super.isNoBodyRequest()) {
            request.setHeader(new BasicHeader(StandardContentType.CONTENT_TYPE, contentType));
        }
    }

    /**
     * 获取 method
     */
    protected void getRequestByMethod() {
        HttpMethod.valid(method);
        if (HttpMethod.POST.method().equals(method)) {
            this.request = new HttpPost(url);
        } else if (HttpMethod.PATCH.method().equals(method)) {
            this.request = new HttpPatch(url);
        } else if (HttpMethod.PUT.method().equals(method)) {
            this.request = new HttpPut(url);
        } else if (HttpMethod.GET.method().equals(method)) {
            this.request = new HttpGet(url);
        } else if (HttpMethod.DELETE.method().equals(method)) {
            this.request = new HttpDelete(url);
        } else if (HttpMethod.HEAD.method().equals(method)) {
            this.request = new HttpHead(url);
        } else if (HttpMethod.TRACE.method().equals(method)) {
            this.request = new HttpTrace(url);
        } else if (HttpMethod.OPTIONS.method().equals(method)) {
            this.request = new HttpOptions(url);
        }
        if (request instanceof HttpEntityEnclosingRequestBase) {
            ((HttpEntityEnclosingRequestBase) request).setEntity(this.getEntry());
        }
    }

    /**
     * 获取 entity
     *
     * @return entity
     */
    protected HttpEntity getEntry() {
        if (body != null) {
            return new ByteArrayEntity(body, bodyOffset, bodyLen);
        } else if (formParts != null) {
            List<NameValuePair> pairs = new ArrayList<>();
            formParts.forEach((k, v) -> pairs.add(new BasicNameValuePair(k, v)));
            this.contentType = HttpContentType.APPLICATION_FORM.getType();
            return new UrlEncodedFormEntity(pairs, Charsets.of(charset));
        }
        return null;
    }

    public CloseableHttpClient getClient() {
        return client;
    }

}
