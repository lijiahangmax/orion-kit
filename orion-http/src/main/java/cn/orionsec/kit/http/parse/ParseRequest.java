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
package cn.orionsec.kit.http.parse;

import cn.orionsec.kit.http.BaseHttpRequest;
import cn.orionsec.kit.http.support.HttpCookie;
import cn.orionsec.kit.lang.able.Awaitable;
import cn.orionsec.kit.lang.constant.StandardContentType;
import cn.orionsec.kit.lang.utils.Charsets;
import cn.orionsec.kit.lang.utils.Exceptions;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * parse request
 * <p>
 * 遇到http code 200
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/6 1:19
 */
public class ParseRequest extends BaseHttpRequest implements Awaitable<ParseResponse> {

    /**
     * connection
     */
    private Connection connection;

    /**
     * request
     */
    private Connection.Request request;

    /**
     * 配置项
     */
    private ParseRequestConfig config;

    public ParseRequest() {
        this(null);
    }

    public ParseRequest(String url) {
        this.userAgent(DEFAULT_USERAGENT);
        this.url = url;
    }

    /**
     * 设置配置项
     *
     * @param config config
     * @return this
     */
    public ParseRequest config(ParseRequestConfig config) {
        this.config = config;
        return this;
    }

    @Override
    protected void buildRequest() {
        super.buildRequest();
        this.connection = Jsoup.connect(url)
                .method(Connection.Method.valueOf(method));
        this.request = connection.request();
        if (headers != null) {
            headers.forEach(connection::header);
        }
        if (cookies != null) {
            cookies.stream()
                    .map(HttpCookie::getValues)
                    .forEach(s -> s.forEach(connection::cookie));
        }
        if (ignoreHeaders != null) {
            ignoreHeaders.forEach(request::removeHeader);
        }
        connection.header(StandardContentType.CONTENT_TYPE, contentType + "; charset=" + charset);
        this.configuration();
        if (!super.isNoBodyRequest()) {
            return;
        }
        if (body != null) {
            connection.requestBody(new String(body, bodyOffset, bodyLen, Charsets.of(charset)));
        } else if (formParts != null) {
            connection.data(formParts);
        }
    }

    /**
     * 设置配置项
     */
    protected void configuration() {
        if (config == null) {
            this.config = new ParseRequestConfig();
        }
        if (config.getProxyHost() != null && config.getProxyPort() != 0) {
            request.proxy(config.getProxyHost(), config.getProxyPort());
        }
        request.sslSocketFactory();
        request.maxBodySize(config.getMaxBodySize())
                .followRedirects(config.isFollowRedirects())
                .ignoreContentType(config.isIgnoreContentType())
                .ignoreHttpErrors(config.isIgnoreError())
                .timeout(config.getTimeout())
                .postDataCharset(charset)
                .sslSocketFactory(config.getSslSocketFactory());
    }

    @Override
    public ParseResponse await() {
        this.buildRequest();
        try {
            return new ParseResponse(url, connection.execute());
        } catch (IOException e) {
            throw Exceptions.httpRequest(url, e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public Connection.Request getRequest() {
        return request;
    }

    public ParseRequestConfig getConfig() {
        return config;
    }

}
