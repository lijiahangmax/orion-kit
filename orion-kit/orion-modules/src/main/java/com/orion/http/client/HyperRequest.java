package com.orion.http.client;

import com.orion.able.Awaitable;
import com.orion.http.common.HttpContent;
import com.orion.http.common.HttpMethod;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.Urls;
import com.orion.utils.Valid;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Hyper HttpClient 请求
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/6/12 14:52
 */
public class HyperRequest implements Awaitable<HyperResponse> {

    /**
     * url
     */
    private String url;

    /**
     * method
     */
    private String method = "GET";

    /**
     * Content-Type GET HEAD 无效
     */
    private String contentType;

    /**
     * 请求参数
     */
    private Map<String, String> queryParams;

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
    private String[] ignoreHeaders;

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
    private HttpUriRequest request;

    /**
     * response
     */
    private HyperResponse response;

    public HyperRequest() {
    }

    public HyperRequest(String url) {
        this.url = url;
    }

    /**
     * url
     *
     * @param url url
     * @return this
     */
    public HyperRequest url(String url) {
        this.url = url;
        return this;
    }

    /**
     * 格式化url的参数 {}
     *
     * @param o 参数
     * @return this
     */
    public HyperRequest format(Object... o) {
        this.url = Strings.format(this.url, o);
        return this;
    }

    /**
     * 格式化url的参数 ${?}
     *
     * @param map 参数
     * @return this
     */
    public HyperRequest format(Map<String, Object> map) {
        this.url = Strings.format(this.url, map);
        return this;
    }

    /**
     * method
     *
     * @param method method
     * @return this
     */
    public HyperRequest method(String method) {
        this.method = method;
        return this;
    }

    /**
     * method
     *
     * @param method method
     * @return this
     */
    public HyperRequest method(HttpMethod method) {
        this.method = method.getMethod();
        return this;
    }

    /**
     * contentType
     *
     * @param contentType contentType
     * @return this
     */
    public HyperRequest contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * contentType
     *
     * @param contentType contentType
     * @return this
     */
    public HyperRequest contentType(HttpContent contentType) {
        this.contentType = contentType.getType();
        return this;
    }

    /**
     * queryParams
     *
     * @param queryParams queryParams
     * @return this
     */
    public HyperRequest queryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
        return this;
    }

    /**
     * queryParams
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public HyperRequest queryParam(String key, String value) {
        if (this.queryParams == null) {
            this.queryParams = new HashMap<>();
        }
        this.queryParams.put(key, value);
        return this;
    }

    /**
     * queryParams
     *
     * @param query key, value
     * @return this
     */
    public HyperRequest queryParam(Map.Entry<String, String> query) {
        if (this.queryParams == null) {
            this.queryParams = new HashMap<>();
        }
        this.queryParams.put(query.getKey(), query.getValue());
        return this;
    }

    /**
     * queryStringEncode
     *
     * @return this
     */
    public HyperRequest queryStringEncode() {
        this.queryStringEncode = true;
        return this;
    }

    /**
     * body
     *
     * @param body body
     * @return this
     */
    public HyperRequest body(String body) {
        if (body == null) {
            return this;
        }
        this.body = body.getBytes();
        this.bodyLen = this.body.length;
        this.bodyOffset = 0;
        return this;
    }

    /**
     * body
     *
     * @param body body
     * @return this
     */
    public HyperRequest body(byte[] body) {
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
    public HyperRequest body(byte[] body, int offset, int len) {
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
    public HyperRequest headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    /**
     * headers
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public HyperRequest headers(String key, String value) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        this.headers.put(key, value);
        return this;
    }

    /**
     * headers
     *
     * @param header key, value
     * @return this
     */
    public HyperRequest headers(Map.Entry<String, String> header) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        this.headers.put(header.getKey(), header.getValue());
        return this;
    }

    /**
     * ignoreHeaders
     *
     * @return this
     */
    public HyperRequest ignoreHeaders(String... ignoreHeaders) {
        this.ignoreHeaders = ignoreHeaders;
        return this;
    }

    /**
     * ssl
     *
     * @param ssl ssl
     * @return this
     */
    public HyperRequest ssl(boolean ssl) {
        this.ssl = ssl;
        return this;
    }

    /**
     * ssl
     *
     * @return this
     */
    public HyperRequest ssl() {
        this.ssl = true;
        return this;
    }

    /**
     * 取消请求
     *
     * @return this
     */
    private HyperRequest cancel() {
        this.request.abort();
        return this;
    }

    @Override
    public HyperResponse await() {
        execute();
        return this.response;
    }

    private void buildRequest() {
        if (queryParams != null) {
            this.queryString = Urls.buildUrl(this.queryParams);
            if (this.queryStringEncode) {
                this.queryString = Urls.encode(this.queryString);
            }
            this.url += ("?" + this.queryString);
        }
        this.method = this.method.trim().toUpperCase();
        this.getRequestByMethod();
        if (this.headers != null) {
            this.headers.forEach((k, v) -> this.request.addHeader(new BasicHeader(k, v)));
        }
        if (this.ignoreHeaders != null) {
            for (String ignoreHeader : this.ignoreHeaders) {
                this.request.removeHeader(new BasicHeader(ignoreHeader, null));
            }
        }
        if (!HttpMethod.GET.getMethod().equals(this.method) && !HttpMethod.HEAD.getMethod().equals(this.method)) {
            if (this.contentType == null) {
                this.contentType = HttpContent.TEXT_PLAIN.getType();
            }
            this.request.setHeader(new BasicHeader("Content-type", this.contentType));
        }
    }

    private void execute() {
        Valid.notNull(this.url, "Request url is null");
        this.buildRequest();
        if (this.client == null) {
            if (this.ssl) {
                this.client = HyperClient.getSslClient();
            } else {
                this.client = HyperClient.getClient();
            }
        }
        try {
            CloseableHttpResponse execute = this.client.execute(this.request);
            this.response = new HyperResponse(this.request, execute)
                    .url(this.url)
                    .method(this.method);
            execute.close();
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    private void getRequestByMethod() {
        HttpMethod.validHethod(this.method, false);
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
        }
    }

    private HttpEntity getEntry() {
        if (this.body == null) {
            return null;
        }
        return new ByteArrayEntity(this.body, this.bodyOffset, this.bodyLen);
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

    public String[] getIgnoreHeaders() {
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

    public HyperResponse getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "HyperRequest{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", contentType='" + contentType + '\'' +
                ", queryString='" + queryString + '\'' +
                ", queryStringEncode=" + queryStringEncode +
                ", bodyLen=" + bodyLen +
                ", headers=" + headers +
                ", ignoreHeaders=" + Arrays.toString(ignoreHeaders) +
                ", ssl=" + ssl +
                '}';
    }

}
