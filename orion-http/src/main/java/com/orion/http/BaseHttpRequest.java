package com.orion.http;

import com.orion.constant.Const;
import com.orion.http.support.HttpContentType;
import com.orion.http.support.HttpCookie;
import com.orion.http.support.HttpMethod;
import com.orion.http.useragent.StandardUserAgent;
import com.orion.utils.Strings;
import com.orion.utils.Urls;
import com.orion.utils.Valid;

import java.util.*;

/**
 * 公共Http请求 基类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/3/5 16:50
 */
public abstract class BaseHttpRequest {

    /**
     * url
     */
    protected String url;

    /**
     * method
     */
    protected String method = HttpMethod.GET.method();

    /**
     * 请求参数
     */
    protected Map<String, String> queryParams;

    /**
     * 请求参数
     */
    protected String queryString;

    /**
     * 请求参数是否编码
     */
    protected boolean queryStringEncode = true;

    /**
     * charset
     */
    protected String charset = Const.UTF_8;

    /**
     * Content-Type GET HEAD DELETE OPTION TRACE 无效
     */
    protected String contentType = HttpContentType.TEXT_PLAIN.getType();

    /**
     * 请求头
     */
    protected Map<String, String> headers;

    /**
     * 忽略的请求头
     */
    protected List<String> ignoreHeaders;

    /**
     * cookies
     */
    protected List<HttpCookie> cookies;

    /**
     * 表单参数
     */
    protected Map<String, String> formParts;

    /**
     * 请求体
     */
    protected byte[] body;

    /**
     * 偏移量
     */
    protected int bodyOffset;

    /**
     * 长度
     */
    protected int bodyLen;

    /**
     * 是否使用ssl
     */
    protected boolean ssl;

    // ------------------ line ------------------

    public BaseHttpRequest url(String url) {
        this.url = url;
        return this;
    }

    /**
     * 格式化url的参数 {}
     *
     * @param o 参数
     * @return this
     */
    public BaseHttpRequest format(Object... o) {
        this.url = Strings.format(this.url, o);
        return this;
    }

    /**
     * 格式化url的参数 ${?}
     *
     * @param map 参数
     * @return this
     */
    public BaseHttpRequest format(Map<String, Object> map) {
        this.url = Strings.format(this.url, map);
        return this;
    }

    public BaseHttpRequest method(String method) {
        this.method = method;
        return this;
    }

    public BaseHttpRequest method(HttpMethod method) {
        this.method = method.method();
        return this;
    }

    public BaseHttpRequest queryParam(String key, String value) {
        if (this.queryParams == null) {
            this.queryParams = new LinkedHashMap<>();
        }
        this.queryParams.put(key, value);
        return this;
    }

    public BaseHttpRequest queryParams(Map<String, String> queryParams) {
        if (this.queryParams == null) {
            this.queryParams = queryParams;
        } else {
            this.queryParams.putAll(queryParams);
        }
        return this;
    }

    public BaseHttpRequest queryStringEncode(boolean queryStringEncode) {
        this.queryStringEncode = queryStringEncode;
        return this;
    }

    // ------------------ header ------------------

    public BaseHttpRequest charset(String charset) {
        this.charset = charset;
        return this;
    }

    public BaseHttpRequest header(String key, String value) {
        if (this.headers == null) {
            this.headers = new LinkedHashMap<>();
        }
        this.headers.put(key, value);
        return this;
    }

    public BaseHttpRequest headers(Map<String, String> headers) {
        if (this.headers == null) {
            this.headers = headers;
        } else {
            this.headers.putAll(headers);
        }
        return this;
    }

    public BaseHttpRequest userAgent(String value) {
        return header(StandardUserAgent.USER_AGENT, value);
    }

    public BaseHttpRequest referrer(String value) {
        return header("Referrer", value);
    }

    public BaseHttpRequest contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public BaseHttpRequest contentType(HttpContentType contentType) {
        this.contentType = contentType.getType();
        return this;
    }

    public BaseHttpRequest ignoreHeader(String ignoreHeader) {
        if (ignoreHeader == null) {
            return this;
        }
        if (this.ignoreHeaders == null) {
            this.ignoreHeaders = new ArrayList<>();
        }
        this.ignoreHeaders.add(ignoreHeader);
        return this;
    }

    public BaseHttpRequest ignoreHeaders(String... ignoreHeaders) {
        if (ignoreHeaders == null) {
            return this;
        }
        if (this.ignoreHeaders == null) {
            this.ignoreHeaders = new ArrayList<>();
        }
        this.ignoreHeaders.addAll(Arrays.asList(ignoreHeaders));
        return this;
    }

    public BaseHttpRequest ignoreHeaders(List<String> ignoreHeaders) {
        if (this.ignoreHeaders == null) {
            this.ignoreHeaders = ignoreHeaders;
        } else {
            this.ignoreHeaders.addAll(ignoreHeaders);
        }
        return this;
    }

    public BaseHttpRequest cookie(HttpCookie cookie) {
        if (this.cookies == null) {
            this.cookies = new ArrayList<>();
        }
        this.cookies.add(cookie);
        return this;
    }

    public BaseHttpRequest cookies(List<HttpCookie> cookies) {
        if (this.cookies == null) {
            this.cookies = cookies;
        } else {
            this.cookies.addAll(cookies);
        }
        return this;
    }

    // ------------------ body ------------------

    public BaseHttpRequest formPart(String key, String value) {
        if (this.formParts == null) {
            this.formParts = new LinkedHashMap<>();
        }
        this.formParts.put(key, value);
        return this;
    }

    public BaseHttpRequest formParts(Map<String, String> formParts) {
        if (this.formParts == null) {
            this.formParts = formParts;
        } else {
            this.formParts.putAll(formParts);
        }
        return this;
    }

    public BaseHttpRequest body(String body) {
        if (body == null) {
            return this;
        }
        this.body = Strings.bytes(body, this.charset);
        this.bodyOffset = 0;
        this.bodyLen = this.body.length;
        return this;
    }

    public BaseHttpRequest body(byte[] body) {
        this.body = body;
        this.bodyOffset = 0;
        this.bodyLen = body.length;
        return this;
    }

    public BaseHttpRequest body(byte[] body, int offset, int len) {
        this.body = body;
        this.bodyOffset = offset;
        this.bodyLen = len;
        return this;
    }

    /**
     * 构建请求
     */
    protected void buildRequest() {
        Valid.notNull(this.url, "request url is null");
        this.method = this.method.trim().toUpperCase();
        if (this.isNoBodyRequest() && this.formParts != null) {
            if (this.queryParams == null) {
                this.queryParams = new LinkedHashMap<>();
            }
            this.queryParams.putAll(this.formParts);
        }
        if (queryParams != null) {
            this.queryString = Urls.buildQueryString(this.queryParams, this.queryStringEncode);
            this.url += ("?" + this.queryString);
        }
    }

    /**
     * 是否为无body的请求
     *
     * @return ignore
     */
    protected boolean isNoBodyRequest() {
        return HttpMethod.GET.method().equals(this.method) ||
                HttpMethod.HEAD.method().equals(this.method) ||
                HttpMethod.DELETE.method().equals(this.method) ||
                HttpMethod.OPTIONS.method().equals(this.method) ||
                HttpMethod.TRACE.method().equals(this.method);
    }

    /**
     * 执行请求
     */
    protected abstract void execute();

    // ------------------ getter ------------------

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public String getQueryString() {
        return queryString;
    }

    public boolean isQueryStringEncode() {
        return queryStringEncode;
    }

    public String getCharset() {
        return charset;
    }

    public String getContentType() {
        return contentType;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public List<String> getIgnoreHeaders() {
        return ignoreHeaders;
    }

    public List<HttpCookie> getCookies() {
        return cookies;
    }

    public Map<String, String> getFormParts() {
        return formParts;
    }

    public byte[] getBody() {
        return body;
    }

    public int getBodyOffset() {
        return bodyOffset;
    }

    public int getBodyLen() {
        return bodyLen;
    }

    public boolean isSsl() {
        return ssl;
    }

    @Override
    public String toString() {
        return method + Strings.SPACE + url;
    }

}
