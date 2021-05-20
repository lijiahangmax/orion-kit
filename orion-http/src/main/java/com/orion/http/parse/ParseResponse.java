package com.orion.http.parse;

import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.Serializable;

/**
 * response
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/6 16:35
 */
public class ParseResponse implements Serializable {

    /**
     * request
     */
    private Connection.Request request;

    /**
     * response
     */
    private Connection.Response response;

    /**
     * 响应体
     */
    private byte[] body;

    /**
     * url
     */
    private String url;

    /**
     * method
     */
    private String method;

    /**
     * document
     */
    private Document document;

    public ParseResponse(String url, String method, Connection.Request request, Connection.Response response) {
        this.url = url;
        this.method = method;
        this.request = request;
        this.response = response;
        this.body = response.bodyAsBytes();
    }

    /**
     * 解析
     *
     * @return this
     */
    public ParseResponse parse() {
        try {
            this.document = response.parse();
            return this;
        } catch (IOException e) {
            throw Exceptions.parse(e);
        }
    }

    public String getContentType() {
        return response.contentType();
    }

    public String getCharset() {
        return response.charset();
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public int getCode() {
        return response.statusCode();
    }

    public String getMessage() {
        return response.statusMessage();
    }

    public byte[] getBody() {
        return body;
    }

    public String getBodyString() {
        return new String(body);
    }

    public Connection.Request getRequest() {
        return request;
    }

    public Connection.Response getResponse() {
        return response;
    }

    public Document getDocument() {
        return document;
    }

    @Override
    public String toString() {
        return this.getCode() + Strings.SPACE + Strings.def(this.getMessage());
    }

}
