/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package com.orion.http.parse;

import com.orion.lang.constant.Const;

import javax.net.ssl.SSLSocketFactory;
import java.io.Serializable;

/**
 * 请求配置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/6 12:23
 */
public class ParseRequestConfig implements Serializable {

    private static final long serialVersionUID = 492034989586956230L;

    /**
     * 代理主机
     */
    private String proxyHost;

    /**
     * 代理端口
     */
    private int proxyPort;

    /**
     * 连接超时时间
     * <p>
     * 底层读取超时时间是 连接超时时间 / 2
     */
    private int timeout;

    /**
     * body 大小 默认0 (不限制)
     */
    private int maxBodySize;

    /**
     * 是否忽略 contentType
     * <p>
     * 否则返回的 contentType 必须为 text/* application/xml application/*+xml
     */
    private boolean ignoreContentType;

    /**
     * 是否忽略错误code ( status < 200 || status >= 400)
     */
    private boolean ignoreError;

    /**
     * 是否遵循重定向
     */
    private boolean followRedirects;

    /**
     * ssl 工厂
     */
    private SSLSocketFactory sslSocketFactory;

    public ParseRequestConfig() {
        this.timeout = Const.MS_S_10;
        this.followRedirects = true;
        this.ignoreContentType = true;
        this.ignoreError = true;
    }

    public ParseRequestConfig proxy(String host, int port) {
        this.proxyHost = host;
        this.proxyPort = port;
        return this;
    }

    public ParseRequestConfig timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public ParseRequestConfig maxBodySize(int maxBodySize) {
        this.maxBodySize = maxBodySize;
        return this;
    }

    public ParseRequestConfig followRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
        return this;
    }

    public ParseRequestConfig ignoreContentType(boolean ignoreContentType) {
        this.ignoreContentType = ignoreContentType;
        return this;
    }

    public ParseRequestConfig ignoreError(boolean ignoreError) {
        this.ignoreError = ignoreError;
        return this;
    }

    public ParseRequestConfig sslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
        return this;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getMaxBodySize() {
        return maxBodySize;
    }

    public boolean isFollowRedirects() {
        return followRedirects;
    }

    public boolean isIgnoreContentType() {
        return ignoreContentType;
    }

    public boolean isIgnoreError() {
        return ignoreError;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }
}
