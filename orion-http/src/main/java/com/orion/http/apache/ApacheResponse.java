package com.orion.http.apache;

import com.orion.http.BaseHttpResponse;
import com.orion.http.support.HttpCookie;
import com.orion.lang.define.collect.MutableArrayList;
import com.orion.lang.define.mutable.MutableString;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.io.Streams;
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
