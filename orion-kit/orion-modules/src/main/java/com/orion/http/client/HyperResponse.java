package com.orion.http.client;

import com.orion.http.common.HttpCookie;
import com.orion.lang.collect.ConvertArrayList;
import com.orion.utils.Arrays1;
import com.orion.utils.ext.StringExt;
import com.orion.utils.io.Streams;
import org.apache.http.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Hyper HttpClient 响应
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/6/12 15:08
 */
public class HyperResponse implements Serializable {

    /**
     * 请求
     */
    private HttpRequest request;

    /**
     * 响应
     */
    private HttpResponse response;

    /**
     * 状态码
     */
    private int code;

    /**
     * message
     */
    private String message;

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
     * protocol
     */
    private String protocol;

    /**
     * headers
     */
    private Map<String, ConvertArrayList<String>> headers = new LinkedHashMap<>();

    public HyperResponse(HttpRequest request, HttpResponse response) throws IOException {
        this.request = request;
        this.response = response;
        StatusLine statusLine = response.getStatusLine();
        this.code = statusLine.getStatusCode();
        this.message = statusLine.getReasonPhrase();
        this.protocol = statusLine.getProtocolVersion().getProtocol();
        for (Header header : response.getAllHeaders()) {
            ConvertArrayList<String> list = headers.computeIfAbsent(header.getName(), k -> new ConvertArrayList<>());
            list.add(header.getValue());
        }
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            this.body = Streams.toByteArray(entity.getContent());
        }
    }

    public HyperResponse url(String url) {
        this.url = url;
        return this;
    }

    public HyperResponse method(String method) {
        this.method = method;
        return this;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
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
        return protocol;
    }

    public Map<String, ConvertArrayList<String>> getHeaders() {
        return headers;
    }

    public ConvertArrayList<String> getHeaders(String key) {
        ConvertArrayList<String> list = new ConvertArrayList<>();
        for (Header header : response.getHeaders(key)) {
            list.add(header.getValue());
        }
        return list;
    }

    public StringExt getHeader(String key) {
        Header header = response.getFirstHeader(key);
        if (header != null) {
            return new StringExt(header.getValue());
        }
        return new StringExt(null);
    }

    public StringExt getHeader(String key, String def) {
        Header header = response.getFirstHeader(key);
        if (header != null) {
            return new StringExt(header.getValue(), def);
        }
        return new StringExt(def);
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
        return "HyperResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", bodyLength=" + Arrays1.length(body) +
                ", url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", protocol='" + protocol + '\'' +
                ", headers=" + headers +
                '}';
    }

}
