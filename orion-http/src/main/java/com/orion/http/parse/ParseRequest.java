package com.orion.http.parse;

import com.orion.http.BaseHttpRequest;
import com.orion.http.support.HttpCookie;
import com.orion.lang.able.Awaitable;
import com.orion.lang.constant.StandardContentType;
import com.orion.lang.utils.Charsets;
import com.orion.lang.utils.Exceptions;
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
