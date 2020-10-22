package com.orion.http.apache;

import com.orion.able.Awaitable;
import com.orion.http.common.HttpContent;
import com.orion.http.common.HttpCookie;
import com.orion.http.common.HttpMethod;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.Urls;
import com.orion.utils.Valid;
import com.orion.utils.io.Streams;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Hyper HttpClient 请求
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/6/12 14:52
 */
public class ApacheRequest implements Awaitable<ApacheResponse> {

    /**
     * url
     */
    private String url;

    /**
     * method
     */
    private String method = "GET";

    /**
     * charset
     */
    private String charset;

    /**
     * Content-Type GET HEAD 无效
     */
    private String contentType;

    /**
     * 请求参数
     */
    private Map<String, String> queryParams;

    /**
     * 表单参数
     */
    private Map<String, String> formParts;

    /**
     * cookies
     */
    private List<HttpCookie> cookies;

    /**
     * 请求参数
     */
    private String queryString;

    /**
     * 请求参数是否编码
     */
    private boolean queryStringEncode;

    /**
     * 请求体
     */
    private byte[] body;

    /**
     * 偏移量
     */
    private int bodyOffset;

    /**
     * 长度
     */
    private int bodyLen;

    /**
     * 请求头
     */
    private Map<String, String> headers;

    /**
     * 忽略的请求头
     */
    private List<String> ignoreHeaders;

    /**
     * 是否使用ssl
     */
    private boolean ssl;

    /**
     * client
     */
    private CloseableHttpClient client;

    /**
     * HttpRequest
     */
    private HttpRequestBase request;

    /**
     * response
     */
    private ApacheResponse response;

    public ApacheRequest() {
        this.client = ApacheClient.getClient();
    }

    public ApacheRequest(CloseableHttpClient client) {
        this.client = client;
    }

    public ApacheRequest(String url) {
        this.url = url;
        this.client = ApacheClient.getClient();
    }

    public ApacheRequest(String url, CloseableHttpClient client) {
        this.url = url;
        this.client = client;
    }

    /**
     * url
     *
     * @param url url
     * @return this
     */
    public ApacheRequest url(String url) {
        this.url = url;
        return this;
    }

    /**
     * 格式化url的参数 {}
     *
     * @param o 参数
     * @return this
     */
    public ApacheRequest format(Object... o) {
        this.url = Strings.format(this.url, o);
        return this;
    }

    /**
     * 格式化url的参数 ${?}
     *
     * @param map 参数
     * @return this
     */
    public ApacheRequest format(Map<String, Object> map) {
        this.url = Strings.format(this.url, map);
        return this;
    }

    /**
     * client
     *
     * @param client client
     * @return this
     */
    public ApacheRequest client(CloseableHttpClient client) {
        this.client = client;
        return this;
    }

    /**
     * method
     *
     * @param method method
     * @return this
     */
    public ApacheRequest method(String method) {
        this.method = method;
        return this;
    }

    /**
     * method
     *
     * @param method method
     * @return this
     */
    public ApacheRequest method(HttpMethod method) {
        this.method = method.getMethod();
        return this;
    }

    /**
     * 编码格式
     *
     * @param charset charset
     * @return this
     */
    public ApacheRequest charset(String charset) {
        this.charset = charset;
        return this;
    }

    /**
     * contentType
     *
     * @param contentType contentType
     * @return this
     */
    public ApacheRequest contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * contentType
     *
     * @param contentType contentType
     * @return this
     */
    public ApacheRequest contentType(HttpContent contentType) {
        this.contentType = contentType.getType();
        return this;
    }

    /**
     * queryParams
     *
     * @param queryParams queryParams
     * @return this
     */
    public ApacheRequest queryParams(Map<String, String> queryParams) {
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
    public ApacheRequest queryParam(String key, String value) {
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
    public ApacheRequest formParts(Map<String, String> formParts) {
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
    public ApacheRequest formPart(String key, String value) {
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
    public ApacheRequest queryStringEncode() {
        this.queryStringEncode = true;
        return this;
    }

    /**
     * queryStringEncode
     *
     * @param encode encode
     * @return this
     */
    public ApacheRequest queryStringEncode(boolean encode) {
        this.queryStringEncode = encode;
        return this;
    }

    /**
     * body
     *
     * @param body body
     * @return this
     */
    public ApacheRequest body(String body) {
        return body(body, false);
    }

    /**
     * body
     *
     * @param body       body
     * @param useCharset 使用charset
     * @return this
     */
    public ApacheRequest body(String body, boolean useCharset) {
        if (body == null) {
            return this;
        }
        if (useCharset) {
            try {
                this.body = body.getBytes(this.charset);
            } catch (Exception e) {
                throw Exceptions.unCoding(e);
            }
        } else {
            this.body = body.getBytes();
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
    public ApacheRequest body(byte[] body) {
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
    public ApacheRequest body(byte[] body, int offset, int len) {
        this.body = body;
        this.bodyOffset = offset;
        this.bodyLen = len;
        return this;
    }

    /**
     * headers
     *
     * @param headers headers
     * @return this
     */
    public ApacheRequest headers(Map<String, String> headers) {
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
    public ApacheRequest headers(String key, String value) {
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
    public ApacheRequest userAgent(String value) {
        if (this.headers == null) {
            this.headers = new LinkedHashMap<>();
        }
        this.headers.put("User-Agent", value);
        return this;
    }

    /**
     * cookie
     *
     * @param cookie cookie
     * @return this
     */
    public ApacheRequest cookie(HttpCookie cookie) {
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
    public ApacheRequest cookies(List<HttpCookie> cookies) {
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
    public ApacheRequest ignoreHeader(String ignoreHeader) {
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
    public ApacheRequest ignoreHeaders(String... ignoreHeaders) {
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
    public ApacheRequest ignoreHeaders(List<String> ignoreHeaders) {
        if (this.ignoreHeaders == null) {
            this.ignoreHeaders = ignoreHeaders;
        } else {
            this.ignoreHeaders.addAll(ignoreHeaders);
        }
        return this;
    }

    /**
     * ssl
     *
     * @param ssl ssl
     * @return this
     */
    public ApacheRequest ssl(boolean ssl) {
        this.ssl = ssl;
        if (ssl) {
            this.client = ApacheClient.getSslClient();
        }
        return this;
    }

    /**
     * ssl
     *
     * @return this
     */
    public ApacheRequest ssl() {
        this.ssl = true;
        this.client = ApacheClient.getSslClient();
        return this;
    }

    /**
     * 取消请求
     *
     * @return this
     */
    private ApacheRequest cancel() {
        this.request.abort();
        return this;
    }

    @Override
    public ApacheResponse await() {
        execute();
        return this.response;
    }

    private void buildRequest() {
        this.method = this.method.trim().toUpperCase();
        if (HttpMethod.GET.getMethod().equals(this.method) || HttpMethod.HEAD.getMethod().equals(this.method)) {
            if (this.formParts != null) {
                if (this.queryParams == null) {
                    this.queryParams = new LinkedHashMap<>();
                }
                this.queryParams.putAll(this.formParts);
            }
        }
        if (queryParams != null) {
            this.queryString = Urls.buildQueryString(this.queryParams, this.queryStringEncode);
            this.url += ("?" + this.queryString);
        }
        this.getRequestByMethod();
        if (this.headers != null) {
            this.headers.forEach((k, v) -> this.request.addHeader(new BasicHeader(k, v)));
        }
        if (this.cookies != null) {
            this.cookies.forEach(c -> this.request.addHeader(new BasicHeader("Cookie", c.toString())));
        }
        if (this.ignoreHeaders != null) {
            this.ignoreHeaders.forEach(ignoreHeader -> this.request.removeHeader(new BasicHeader(ignoreHeader, null)));
        }
        if (!HttpMethod.GET.getMethod().equals(this.method) && !HttpMethod.HEAD.getMethod().equals(this.method)) {
            if (this.contentType == null) {
                this.contentType = HttpContent.TEXT_PLAIN.getType();
            }
            if (this.charset == null) {
                this.request.setHeader(new BasicHeader("Content-type", this.contentType));
            } else {
                this.request.setHeader(new BasicHeader("Content-type", this.contentType + "; charset=" + this.charset));
            }
        }
    }

    private void execute() {
        Valid.notNull(this.url, "Request url is null");
        this.buildRequest();
        try {
            CloseableHttpResponse execute = this.client.execute(this.request);
            this.response = new ApacheResponse(this.request, execute)
                    .url(this.url)
                    .method(this.method);
            this.request.releaseConnection();
            Streams.close(execute);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    private void getRequestByMethod() {
        HttpMethod.validMethod(this.method, false);
        if (HttpMethod.GET.getMethod().equals(this.method)) {
            this.request = new HttpGet(this.url);
        } else if (HttpMethod.POST.getMethod().equals(this.method)) {
            HttpPost request = new HttpPost(this.url);
            request.setEntity(getEntry());
            this.request = request;
        } else if (HttpMethod.PUT.getMethod().equals(this.method)) {
            HttpPut request = new HttpPut(this.url);
            request.setEntity(getEntry());
            this.request = request;
        } else if (HttpMethod.DELETE.getMethod().equals(this.method)) {
            this.request = new HttpDelete(this.url);
        } else if (HttpMethod.PATCH.getMethod().equals(this.method)) {
            HttpPatch request = new HttpPatch(this.url);
            request.setEntity(getEntry());
            this.request = request;
        } else if (HttpMethod.HEAD.getMethod().equals(this.method)) {
            this.request = new HttpHead(this.url);
        } else if (HttpMethod.TRACE.getMethod().equals(this.method)) {
            this.request = new HttpTrace(this.url);
        } else if (HttpMethod.OPTIONS.getMethod().equals(this.method)) {
            this.request = new HttpOptions(this.url);
        }
    }

    private HttpEntity getEntry() {
        if (this.body != null) {
            return new ByteArrayEntity(this.body, this.bodyOffset, this.bodyLen);
        }
        if (!HttpMethod.GET.getMethod().equals(this.method) &&
                !HttpMethod.HEAD.getMethod().equals(this.method) &&
                this.formParts != null) {
            List<NameValuePair> pairs = new ArrayList<>();
            this.formParts.forEach((k, v) -> pairs.add(new BasicNameValuePair(k, v)));
            try {
                this.contentType = HttpContent.APPLICATION_FORM.getType();
                if (this.charset == null) {
                    return new UrlEncodedFormEntity(pairs);
                } else {
                    return new UrlEncodedFormEntity(pairs, Charset.forName(charset));
                }
            } catch (Exception e) {
                throw Exceptions.unCoding(e);
            }
        }
        return null;
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

    public CloseableHttpClient getClient() {
        return client;
    }

    public HttpUriRequest getRequest() {
        return request;
    }

    public ApacheResponse getResponse() {
        return response;
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
        return url;
    }

}
