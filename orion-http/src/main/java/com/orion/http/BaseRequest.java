package com.orion.http;

import com.orion.http.support.HttpContentType;
import com.orion.http.support.HttpCookie;
import com.orion.http.support.HttpMethod;
import com.orion.http.useragent.StandardUserAgent;
import com.orion.utils.Strings;

import java.util.*;

/**
 * 公共请求
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/3/5 16:50
 */
public class BaseRequest {

    /**
     * url
     */
    protected String url;

    /**
     * method
     */
    protected String method = "GET";

    /**
     * charset
     */
    protected String charset;

    /**
     * Content-Type GET HEAD 无效
     */
    protected String contentType;

    /**
     * 请求参数
     */
    protected Map<String, String> queryParams;

    /**
     * 表单参数
     */
    protected Map<String, String> formParts;

    /**
     * cookies
     */
    protected List<HttpCookie> cookies;

    /**
     * 请求参数
     */
    protected String queryString;

    /**
     * 请求参数是否编码
     */
    protected boolean queryStringEncode;

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
     * 请求头
     */
    protected Map<String, String> headers;

    /**
     * 忽略的请求头
     */
    protected List<String> ignoreHeaders;

    /**
     * 是否使用ssl
     */
    protected boolean ssl;

    /**
     * url
     *
     * @param url url
     * @return this
     */
    public BaseRequest url(String url) {
        this.url = url;
        return this;
    }

    /**
     * 格式化url的参数 {}
     *
     * @param o 参数
     * @return this
     */
    public BaseRequest format(Object... o) {
        this.url = Strings.format(this.url, o);
        return this;
    }

    /**
     * 格式化url的参数 ${?}
     *
     * @param map 参数
     * @return this
     */
    public BaseRequest format(Map<String, Object> map) {
        this.url = Strings.format(this.url, map);
        return this;
    }

    /**
     * method
     *
     * @param method method
     * @return this
     */
    public BaseRequest method(String method) {
        this.method = method;
        return this;
    }

    /**
     * method
     *
     * @param method method
     * @return this
     */
    public BaseRequest method(HttpMethod method) {
        this.method = method.getMethod();
        return this;
    }

    /**
     * 编码格式
     *
     * @param charset charset
     * @return this
     */
    public BaseRequest charset(String charset) {
        this.charset = charset;
        return this;
    }

    /**
     * contentType
     *
     * @param contentType contentType
     * @return this
     */
    public BaseRequest contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * contentType
     *
     * @param contentType contentType
     * @return this
     */
    public BaseRequest contentType(HttpContentType contentType) {
        this.contentType = contentType.getType();
        return this;
    }

    /**
     * queryParams
     *
     * @param queryParams queryParams
     * @return this
     */
    public BaseRequest queryParams(Map<String, String> queryParams) {
        if (this.queryParams == null) {
            this.queryParams = queryParams;
        } else {
            this.queryParams.putAll(queryParams);
        }
        return this;
    }

    /**
     * queryParams
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public BaseRequest queryParam(String key, String value) {
        if (this.queryParams == null) {
            this.queryParams = new LinkedHashMap<>();
        }
        this.queryParams.put(key, value);
        return this;
    }

    /**
     * formParts
     *
     * @param formParts formParts
     * @return this
     */
    public BaseRequest formParts(Map<String, String> formParts) {
        if (this.formParts == null) {
            this.formParts = formParts;
        } else {
            this.formParts.putAll(formParts);
        }
        return this;
    }

    /**
     * formParts
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public BaseRequest formPart(String key, String value) {
        if (this.formParts == null) {
            this.formParts = new LinkedHashMap<>();
        }
        this.formParts.put(key, value);
        return this;
    }

    /**
     * queryStringEncode
     *
     * @return this
     */
    public BaseRequest queryStringEncode() {
        this.queryStringEncode = true;
        return this;
    }

    /**
     * queryStringEncode
     *
     * @param encode encode
     * @return this
     */
    public BaseRequest queryStringEncode(boolean encode) {
        this.queryStringEncode = encode;
        return this;
    }

    /**
     * body
     *
     * @param body body
     * @return this
     */
    public BaseRequest body(String body) {
        return body(body, false);
    }

    /**
     * body
     *
     * @param body       body
     * @param useCharset 使用charset
     * @return this
     */
    public BaseRequest body(String body, boolean useCharset) {
        if (body == null) {
            return this;
        }
        if (useCharset) {
            this.body = Strings.bytes(body, this.charset);
        } else {
            this.body = Strings.bytes(body);
        }
        this.bodyOffset = 0;
        this.bodyLen = this.body.length;
        return this;
    }

    /**
     * body
     *
     * @param body body
     * @return this
     */
    public BaseRequest body(byte[] body) {
        this.body = body;
        this.bodyOffset = 0;
        this.bodyLen = body.length;
        return this;
    }

    /**
     * body
     *
     * @param body   body
     * @param offset offset
     * @param len    len
     * @return this
     */
    public BaseRequest body(byte[] body, int offset, int len) {
        this.body = body;
        this.bodyOffset = offset;
        this.bodyLen = len;
        return this;
    }

    /**
     * header
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public BaseRequest header(String key, String value) {
        if (this.headers == null) {
            this.headers = new LinkedHashMap<>();
        }
        this.headers.put(key, value);
        return this;
    }

    /**
     * headers
     *
     * @param headers headers
     * @return this
     */
    public BaseRequest headers(Map<String, String> headers) {
        if (this.headers == null) {
            this.headers = headers;
        } else {
            this.headers.putAll(headers);
        }
        return this;
    }

    /**
     * headers
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public BaseRequest headers(String key, String value) {
        if (this.headers == null) {
            this.headers = new LinkedHashMap<>();
        }
        this.headers.put(key, value);
        return this;
    }

    /**
     * UserAgent
     *
     * @param value value
     * @return this
     */
    public BaseRequest userAgent(String value) {
        if (this.headers == null) {
            this.headers = new LinkedHashMap<>();
        }
        this.headers.put(StandardUserAgent.USER_AGENT, value);
        return this;
    }

    /**
     * cookie
     *
     * @param cookie cookie
     * @return this
     */
    public BaseRequest cookie(HttpCookie cookie) {
        if (this.cookies == null) {
            this.cookies = new ArrayList<>();
        }
        this.cookies.add(cookie);
        return this;
    }

    /**
     * cookies
     *
     * @param cookies cookies
     * @return this
     */
    public BaseRequest cookies(List<HttpCookie> cookies) {
        if (this.cookies == null) {
            this.cookies = cookies;
        } else {
            this.cookies.addAll(cookies);
        }
        return this;
    }

    /**
     * ignoreHeader
     *
     * @param ignoreHeader ignoreHeader
     * @return this
     */
    public BaseRequest ignoreHeader(String ignoreHeader) {
        if (ignoreHeader == null) {
            return this;
        }
        if (this.ignoreHeaders == null) {
            this.ignoreHeaders = new ArrayList<>();
        }
        this.ignoreHeaders.add(ignoreHeader);
        return this;
    }

    /**
     * ignoreHeaders
     *
     * @param ignoreHeaders ignoreHeaders
     * @return this
     */
    public BaseRequest ignoreHeaders(String... ignoreHeaders) {
        if (ignoreHeaders == null) {
            return this;
        }
        if (this.ignoreHeaders == null) {
            this.ignoreHeaders = new ArrayList<>();
        }
        this.ignoreHeaders.addAll(Arrays.asList(ignoreHeaders));
        return this;
    }

    /**
     * ignoreHeaders
     *
     * @return this
     */
    public BaseRequest ignoreHeaders(List<String> ignoreHeaders) {
        if (this.ignoreHeaders == null) {
            this.ignoreHeaders = ignoreHeaders;
        } else {
            this.ignoreHeaders.addAll(ignoreHeaders);
        }
        return this;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public String getContentType() {
        return contentType;
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

    public byte[] getBody() {
        return body;
    }

    public int getBodyOffset() {
        return bodyOffset;
    }

    public int getBodyLen() {
        return bodyLen;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public List<String> getIgnoreHeaders() {
        return ignoreHeaders;
    }

    public boolean isSsl() {
        return ssl;
    }

    public String getCharset() {
        return charset;
    }

    public Map<String, String> getFormParts() {
        return formParts;
    }

    public List<HttpCookie> getCookies() {
        return cookies;
    }

    @Override
    public String toString() {
        return method + " " + url;
    }

}
