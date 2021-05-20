package com.orion.http.apache;

import com.orion.http.support.HttpCookie;
import com.orion.lang.collect.MutableArrayList;
import com.orion.lang.mutable.MutableString;
import com.orion.utils.Strings;
import com.orion.utils.io.Streams;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
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
public class ApacheResponse implements Serializable {

    /**
     * 请求
     */
    private HttpRequest request;

    /**
     * 响应
     */
    private HttpResponse response;

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

    public ApacheResponse(String url, String method, HttpRequest request, HttpResponse response) throws IOException {
        this.url = url;
        this.method = method;
        this.request = request;
        this.response = response;
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            this.body = Streams.toByteArray(entity.getContent());
        }
    }

    public HttpRequest getRequest() {
        return request;
    }

    public HttpResponse getResponse() {
        return response;
    }

    /**
     * 请求是否成功
     *
     * @return true成功
     */
    public boolean isOk() {
        return this.getCode() >= 200 && this.getCode() < 300;
    }

    public int getCode() {
        return response.getStatusLine().getStatusCode();
    }

    public String getMessage() {
        return response.getStatusLine().getReasonPhrase();
    }

    public byte[] getBody() {
        return body;
    }

    public String getBodyString() {
        if (body != null) {
            return new String(body);
        }
        return null;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public String getProtocol() {
        return response.getStatusLine().getProtocolVersion().getProtocol();
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
        return new MutableString(null);
    }

    public MutableString getHeader(String key, String def) {
        Header header = response.getFirstHeader(key);
        if (header != null) {
            return new MutableString(header.getValue(), def);
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
