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

    @Override
    public byte[] getBody() {
        return body;
    }

    @Override
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
