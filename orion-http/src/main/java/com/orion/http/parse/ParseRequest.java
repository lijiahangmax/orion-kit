package com.orion.http.parse;

import com.orion.able.Awaitable;
import com.orion.constant.StandardContentType;
import com.orion.http.BaseHttpRequest;
import com.orion.http.support.HttpCookie;
import com.orion.http.useragent.StandardUserAgent;
import com.orion.utils.Charsets;
import com.orion.utils.Exceptions;
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
     * response
     */
    private Connection.Response response;

    /**
     * 配置项
     */
    private ParseRequestConfig config;

    public ParseRequest() {
        this.userAgent(StandardUserAgent.CHROME_3);
    }

    public ParseRequest(String url) {
        this();
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
        this.response = connection.response();
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
    protected void execute() {
        this.buildRequest();
        try {
            this.response = connection.execute();
        } catch (IOException e) {
            throw Exceptions.httpRequest(e);
        }
    }

    @Override
    public ParseResponse await() {
        this.execute();
        return new ParseResponse(url, method, request, response);
    }

    public Connection getConnection() {
        return connection;
    }

    public Connection.Request getRequest() {
        return request;
    }

    public Connection.Response getResponse() {
        return response;
    }

    public ParseRequestConfig getConfig() {
        return config;
    }

}
