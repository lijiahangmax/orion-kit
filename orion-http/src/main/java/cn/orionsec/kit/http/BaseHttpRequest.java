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
package cn.orionsec.kit.http;

import cn.orionsec.kit.http.support.HttpContentType;
import cn.orionsec.kit.http.support.HttpCookie;
import cn.orionsec.kit.http.support.HttpMethod;
import cn.orionsec.kit.http.useragent.StandardUserAgent;
import cn.orionsec.kit.lang.config.KitConfig;
import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.constant.StandardHttpHeader;
import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.Urls;

import java.util.*;

/**
 * 公共 Http 请求 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/5 16:50
 */
public abstract class BaseHttpRequest {

    protected static final String DEFAULT_USERAGENT = KitConfig.get(KitHttpConfiguration.CONFIG.HTTP_DEFAULT_USERAGENT);

    /**
     * url
     */
    protected String url;

    /**
     * method
     */
    protected String method;

    /**
     * 请求参数
     */
    protected Map<String, Object> queryParams;

    /**
     * 请求参数
     */
    protected String queryString;

    /**
     * 请求参数是否编码
     */
    protected boolean queryStringEncode;

    /**
     * charset
     */
    protected String charset;

    /**
     * Content-Type GET HEAD DELETE OPTION TRACE 无效
     */
    protected String contentType;

    /**
     * 请求头
     */
    protected Map<String, String> headers;

    /**
     * 忽略的请求头
     */
    protected Collection<String> ignoreHeaders;

    /**
     * cookies
     */
    protected Collection<HttpCookie> cookies;

    /**
     * 表单参数
     */
    protected Map<String, String> formParts;

    /**
     * 请求体
     */
    protected byte[] body;

    /**
     * 偏移量
     */
    protected int bodyOffset;

    /**
     * 长度
     */
    protected int bodyLen;

    public BaseHttpRequest() {
        this.method = HttpMethod.GET.method();
        this.queryStringEncode = true;
        this.charset = Const.UTF_8;
        this.contentType = HttpContentType.TEXT_PLAIN.getType();
    }

    // -------------------- line --------------------

    public BaseHttpRequest url(String url) {
        this.url = url;
        return this;
    }

    /**
     * 格式化 url 的参数 {}
     *
     * @param o 参数
     * @return this
     */
    public BaseHttpRequest format(Object... o) {
        this.url = Strings.format(this.url, o);
        return this;
    }

    /**
     * 格式化 url 的参数 ${?}
     *
     * @param map 参数
     * @return this
     */
    public BaseHttpRequest format(Map<String, Object> map) {
        this.url = Strings.format(this.url, map);
        return this;
    }

    public BaseHttpRequest method(String method) {
        this.method = method;
        return this;
    }

    public BaseHttpRequest method(HttpMethod method) {
        this.method = method.method();
        return this;
    }

    public BaseHttpRequest queryParam(String key, Object value) {
        if (this.queryParams == null) {
            this.queryParams = new LinkedHashMap<>();
        }
        this.queryParams.put(key, value);
        return this;
    }

    public BaseHttpRequest queryParams(Map<String, ?> queryParams) {
        if (this.queryParams == null) {
            this.queryParams = new LinkedHashMap<>();
        }
        this.queryParams.putAll(queryParams);
        return this;
    }

    public BaseHttpRequest queryStringEncode(boolean queryStringEncode) {
        this.queryStringEncode = queryStringEncode;
        return this;
    }

    // -------------------- header --------------------

    public BaseHttpRequest charset(String charset) {
        this.charset = charset;
        return this;
    }

    public BaseHttpRequest header(String key, String value) {
        if (this.headers == null) {
            this.headers = new LinkedHashMap<>();
        }
        this.headers.put(key, value);
        return this;
    }

    public BaseHttpRequest headers(Map<String, String> headers) {
        if (this.headers == null) {
            this.headers = headers;
        } else {
            this.headers.putAll(headers);
        }
        return this;
    }

    public BaseHttpRequest userAgent(String value) {
        return this.header(StandardUserAgent.USER_AGENT, value);
    }

    public BaseHttpRequest referer(String value) {
        return this.header(StandardHttpHeader.REFERER, value);
    }

    public BaseHttpRequest contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public BaseHttpRequest contentType(HttpContentType contentType) {
        this.contentType = contentType.getType();
        return this;
    }

    public BaseHttpRequest ignoreHeader(String ignoreHeader) {
        if (ignoreHeader == null) {
            return this;
        }
        if (this.ignoreHeaders == null) {
            this.ignoreHeaders = new ArrayList<>();
        }
        this.ignoreHeaders.add(ignoreHeader);
        return this;
    }

    public BaseHttpRequest ignoreHeaders(String... ignoreHeaders) {
        if (ignoreHeaders == null) {
            return this;
        }
        if (this.ignoreHeaders == null) {
            this.ignoreHeaders = new ArrayList<>();
        }
        this.ignoreHeaders.addAll(Arrays.asList(ignoreHeaders));
        return this;
    }

    public BaseHttpRequest ignoreHeaders(Collection<String> ignoreHeaders) {
        if (this.ignoreHeaders == null) {
            this.ignoreHeaders = ignoreHeaders;
        } else {
            this.ignoreHeaders.addAll(ignoreHeaders);
        }
        return this;
    }

    public BaseHttpRequest cookie(HttpCookie cookie) {
        if (this.cookies == null) {
            this.cookies = new ArrayList<>();
        }
        this.cookies.add(cookie);
        return this;
    }

    public BaseHttpRequest cookies(Collection<HttpCookie> cookies) {
        if (this.cookies == null) {
            this.cookies = cookies;
        } else {
            this.cookies.addAll(cookies);
        }
        return this;
    }

    // -------------------- body --------------------

    public BaseHttpRequest formPart(String key, String value) {
        if (this.formParts == null) {
            this.formParts = new LinkedHashMap<>();
        }
        this.formParts.put(key, value);
        return this;
    }

    public BaseHttpRequest formParts(Map<String, String> formParts) {
        if (this.formParts == null) {
            this.formParts = formParts;
        } else {
            this.formParts.putAll(formParts);
        }
        return this;
    }

    public BaseHttpRequest body(String body) {
        if (body == null) {
            return this;
        }
        this.body = Strings.bytes(body, this.charset);
        this.bodyOffset = 0;
        this.bodyLen = this.body.length;
        return this;
    }

    public BaseHttpRequest body(byte[] body) {
        this.body = body;
        this.bodyOffset = 0;
        this.bodyLen = body.length;
        return this;
    }

    public BaseHttpRequest body(byte[] body, int offset, int len) {
        this.body = body;
        this.bodyOffset = offset;
        this.bodyLen = len;
        return this;
    }

    /**
     * 构建请求
     */
    protected void buildRequest() {
        Assert.notNull(url, "request url is null");
        this.method = method.trim().toUpperCase();
        if (this.isNoBodyRequest() && formParts != null) {
            if (queryParams == null) {
                this.queryParams = new LinkedHashMap<>();
            }
            queryParams.putAll(formParts);
        }
        if (queryParams != null) {
            this.queryString = Urls.buildQueryString(queryParams, queryStringEncode);
            this.url += ("?" + queryString);
        }
    }

    /**
     * 是否为无body的请求
     *
     * @return ignore
     */
    protected boolean isNoBodyRequest() {
        return HttpMethod.GET.method().equals(method)
                || HttpMethod.HEAD.method().equals(method)
                || HttpMethod.DELETE.method().equals(method)
                || HttpMethod.OPTIONS.method().equals(method)
                || HttpMethod.TRACE.method().equals(method);
    }

    // -------------------- getter --------------------

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, Object> getQueryParams() {
        return queryParams;
    }

    public String getQueryString() {
        return queryString;
    }

    public boolean isQueryStringEncode() {
        return queryStringEncode;
    }

    public String getCharset() {
        return charset;
    }

    public String getContentType() {
        return contentType;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Collection<String> getIgnoreHeaders() {
        return ignoreHeaders;
    }

    public Collection<HttpCookie> getCookies() {
        return cookies;
    }

    public Map<String, String> getFormParts() {
        return formParts;
    }

    public byte[] getBody() {
        return body;
    }

    public int getBodyOffset() {
        return bodyOffset;
    }

    public int getBodyLen() {
        return bodyLen;
    }

    @Override
    public String toString() {
        return method + Strings.SPACE + url;
    }

}
