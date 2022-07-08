package com.orion.http.parse;

import com.orion.http.BaseHttpResponse;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
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
public class ParseResponse extends BaseHttpResponse implements Serializable {

    /**
     * response
     */
    private final Connection.Response response;

    /**
     * 响应体
     */
    private final byte[] body;

    /**
     * url
     */
    private final String url;

    /**
     * document
     */
    private Document document;

    public ParseResponse(String url, Connection.Response response) {
        this.url = url;
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

    @Override
    public String getUrl() {
        return url;
    }

    @Override
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
