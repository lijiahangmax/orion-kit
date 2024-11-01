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
package cn.orionsec.kit.http.apache;

import cn.orionsec.kit.http.BaseHttpResponse;
import cn.orionsec.kit.http.support.HttpCookie;
import cn.orionsec.kit.lang.define.collect.MutableArrayList;
import cn.orionsec.kit.lang.define.mutable.MutableString;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.io.Streams;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Apache HttpClient 响应
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/6/12 15:08
 */
public class ApacheResponse extends BaseHttpResponse implements Serializable {

    /**
     * 响应
     */
    private final HttpResponse response;

    /**
     * url
     */
    private final String url;

    /**
     * 响应体
     */
    private byte[] body;

    public ApacheResponse(String url, HttpResponse response) throws IOException {
        this.url = url;
        this.response = response;
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            this.body = Streams.toByteArray(entity.getContent());
        }
    }

    public HttpResponse getResponse() {
        return response;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public int getCode() {
        return response.getStatusLine().getStatusCode();
    }

    public String getMessage() {
        return response.getStatusLine().getReasonPhrase();
    }

    @Override
    public byte[] getBody() {
        return body;
    }

    @Override
    public String getBodyString() {
        if (body != null) {
            return new String(body);
        }
        return null;
    }

    public Map<String, MutableArrayList<String>> getHeaders() {
        Map<String, MutableArrayList<String>> headers = new LinkedHashMap<>();
        for (Header header : response.getAllHeaders()) {
            MutableArrayList<String> list = headers.computeIfAbsent(header.getName(), k -> new MutableArrayList<>());
            list.add(header.getValue());
        }
        return headers;
    }

    public MutableArrayList<String> getHeaders(String key) {
        MutableArrayList<String> list = new MutableArrayList<>();
        for (Header header : response.getHeaders(key)) {
            list.add(header.getValue());
        }
        return list;
    }

    public MutableString getHeader(String key) {
        Header header = response.getFirstHeader(key);
        if (header != null) {
            return new MutableString(header.getValue());
        }
        return new MutableString();
    }

    public MutableString getHeader(String key, String def) {
        Header header = response.getFirstHeader(key);
        if (header != null) {
            return new MutableString(header.getValue());
        }
        return new MutableString(def);
    }

    public List<HttpCookie> getCookies() {
        List<HttpCookie> list = new ArrayList<>();
        for (Header header : response.getHeaders(HttpCookie.SET_COOKIE)) {
            list.add(new HttpCookie(header.getValue()));
        }
        return list;
    }

    @Override
    public String toString() {
        return this.getCode() + Strings.SPACE + Strings.def(this.getMessage());
    }

}
