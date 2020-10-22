package com.orion.http.ok;

import com.orion.able.Asyncable;
import com.orion.able.Awaitable;
import com.orion.http.common.HttpContent;
import com.orion.http.common.HttpCookie;
import com.orion.http.common.HttpMethod;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.Urls;
import com.orion.utils.Valid;
import okhttp3.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Consumer;

/**
 * Mock 请求
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/7 23:49
 */
@SuppressWarnings("ALL")
public class OkRequest implements Awaitable<OkResponse>, Asyncable<Consumer<OkResponse>> {

    /**
     * url
     */
    private String url;

    /**
     * method
     */
    private String method = "GET";

    /**
     * 编码格式 GET HEAD 无效
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
     * cookies
     */
    private List<HttpCookie> cookies;

    /**
     * tag
     */
    private Object tag;

    /**
     * 是否使用ssl
     */
    private boolean ssl;

    /**
     * 是否为异步
     */
    private boolean async;

    /**
     * 异步接口
     */
    private Consumer<OkResponse> consumer;

    /**
     * 请求
     */
    private Request request;

    /**
     * call
     */
    private Call call;

    /**
     * client
     */
    private OkHttpClient client;

    /**
     * MockResponse
     */
    private OkResponse response;

    public OkRequest() {
        this.client = OkClient.getClient();
    }

    public OkRequest(OkHttpClient client) {
        this.client = client;
    }

    public OkRequest(String url) {
        this.url = url;
        this.client = OkClient.getClient();
    }

    public OkRequest(String url, OkHttpClient client) {
        this.url = url;
        this.client = client;
    }

    /**
     * url
     *
     * @param url url
     * @return this
     */
    public OkRequest url(String url) {
        this.url = url;
        return this;
    }

    /**
     * 格式化url的参数 {}
     *
     * @param o 参数
     * @return this
     */
    public OkRequest format(Object... o) {
        this.url = Strings.format(this.url, o);
        return this;
    }

    /**
     * 格式化url的参数 ${?}
     *
     * @param map 参数
     * @return this
     */
    public OkRequest format(Map<String, Object> map) {
        this.url = Strings.format(this.url, map);
        return this;
    }

    /**
     * 设置Client
     *
     * @param client client
     * @return this
     */
    public OkRequest client(OkHttpClient client) {
        this.client = client;
        return this;
    }

    /**
     * method
     *
     * @param method method
     * @return this
     */
    public OkRequest method(String method) {
        this.method = method;
        return this;
    }

    /**
     * method
     *
     * @param method method
     * @return this
     */
    public OkRequest method(HttpMethod method) {
        this.method = method.getMethod();
        return this;
    }

    /**
     * charset
     *
     * @param charset charset
     * @return this
     */
    public OkRequest charset(String charset) {
        this.charset = charset;
        return this;
    }

    /**
     * contentType
     *
     * @param contentType contentType
     * @return this
     */
    public OkRequest contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * contentType
     *
     * @param contentType contentType
     * @return this
     */
    public OkRequest contentType(HttpContent contentType) {
        this.contentType = contentType.getType();
        return this;
    }

    /**
     * queryParams
     *
     * @param queryParams queryParams
     * @return this
     */
    public OkRequest queryParams(Map<String, String> queryParams) {
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
    public OkRequest queryParam(String key, String value) {
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
    public OkRequest formParts(Map<String, String> formParts) {
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
    public OkRequest formPart(String key, String value) {
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
    public OkRequest queryStringEncode() {
        this.queryStringEncode = true;
        return this;
    }

    /**
     * queryStringEncode
     *
     * @param encode ignore
     * @return this
     */
    public OkRequest queryStringEncode(boolean encode) {
        this.queryStringEncode = encode;
        return this;
    }

    /**
     * body
     *
     * @param body body
     * @return this
     */
    public OkRequest body(String body) {
        return body(body, false);
    }

    /**
     * body
     *
     * @param body       body
     * @param useCharset 使用charset
     * @return this
     */
    public OkRequest body(String body, boolean useCharset) {
        if (body == null) {
            return this;
        }
        if (!useCharset) {
            this.body = body.getBytes();
        } else {
            try {
                this.body = body.getBytes(this.charset);
            } catch (Exception e) {
                throw Exceptions.unCoding(e);
            }
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
    public OkRequest body(byte[] body) {
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
    public OkRequest body(byte[] body, int offset, int len) {
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
    public OkRequest headers(Map<String, String> headers) {
        if (this.headers == null) {
            this.headers = headers;
        } else {
            this.headers.putAll(headers);
        }
        return this;
    }

    /**
     * header
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public OkRequest header(String key, String value) {
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
    public OkRequest userAgent(String value) {
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
    public OkRequest cookie(HttpCookie cookie) {
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
    public OkRequest cookies(List<HttpCookie> cookies) {
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
    public OkRequest ignoreHeader(String ignoreHeader) {
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
    public OkRequest ignoreHeaders(String... ignoreHeaders) {
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
     * @param ignoreHeaders ignoreHeaders
     * @return this
     */
    public OkRequest ignoreHeaders(List<String> ignoreHeaders) {
        if (this.ignoreHeaders == null) {
            this.ignoreHeaders = ignoreHeaders;
        } else {
            this.ignoreHeaders.addAll(ignoreHeaders);
        }
        return this;
    }

    /**
     * tag
     *
     * @param tag tag
     * @return this
     */
    public OkRequest tag(Object tag) {
        this.tag = tag;
        return this;
    }

    /**
     * ssl
     *
     * @param ssl ssl
     * @return this
     */
    public OkRequest ssl(boolean ssl) {
        this.ssl = ssl;
        if (this.ssl) {
            this.client = OkClient.getSslClient();
        }
        return this;
    }

    /**
     * ssl
     *
     * @return this
     */
    public OkRequest ssl() {
        this.ssl = true;
        return this;
    }

    /**
     * 取消请求
     *
     * @return this
     */
    public OkRequest cancel() {
        this.call.cancel();
        return this;
    }

    /**
     * 同步调用
     *
     * @return MockResult
     */
    @Override
    public OkResponse await() {
        execute();
        return this.response;
    }

    /**
     * 异步调用
     *
     * @param consumer consumer
     */
    @Override
    public void async(Consumer<OkResponse> consumer) {
        Valid.notNull(consumer, "Async call back is null");
        this.consumer = consumer;
        this.async = true;
        execute();
    }

    /**
     * 构建request
     */
    private void buildRequest() {
        Request.Builder requestBuilder = new Request.Builder();
        this.method = this.method.toUpperCase();
        HttpMethod.validMethod(this.method, true);
        if (this.headers != null) {
            this.headers.forEach(requestBuilder::addHeader);
        }
        if (this.cookies != null) {
            this.cookies.forEach(c -> requestBuilder.addHeader("Cookie", c.toString()));
        }
        if (this.ignoreHeaders != null) {
            this.ignoreHeaders.forEach(requestBuilder::removeHeader);
        }
        boolean show = false;
        if (HttpMethod.GET.getMethod().equals(this.method) || HttpMethod.HEAD.getMethod().equals(this.method)) {
            show = true;
            if (this.formParts != null) {
                if (this.queryParams == null) {
                    this.queryParams = new LinkedHashMap<>();
                }
                this.queryParams.putAll(this.formParts);
            }
        }
        if (this.queryParams != null) {
            this.queryString = Urls.buildQueryString(this.queryParams, this.queryStringEncode);
            this.url += ("?" + this.queryString);
        }
        requestBuilder.url(this.url);
        if (HttpMethod.GET.getMethod().equals(this.method)) {
            requestBuilder.get();
        } else if (HttpMethod.HEAD.getMethod().equals(this.method)) {
            requestBuilder.head();
        } else {
            if (this.contentType == null) {
                this.contentType = HttpContent.TEXT_PLAIN.getType();
            }
            if (this.charset == null) {
                if (this.body != null) {
                    requestBuilder.method(this.method, RequestBody.create(MediaType.parse(this.contentType), this.body, this.bodyOffset, this.bodyLen));
                } else if (this.formParts != null && !show) {
                    FormBody.Builder formBuilder = new FormBody.Builder();
                    this.formParts.forEach(formBuilder::add);
                    requestBuilder.method(this.method, formBuilder.build());
                } else {
                    requestBuilder.method(this.method, RequestBody.create(MediaType.parse(this.contentType), ""));
                }
            } else {
                if (this.body != null) {
                    requestBuilder.method(this.method, RequestBody.create(MediaType.parse(this.contentType + "; charset=" + this.charset), this.body, this.bodyOffset, this.bodyLen));
                } else if (this.formParts != null && !show) {
                    FormBody.Builder formBuilder = new FormBody.Builder(Charset.forName(this.charset));
                    this.formParts.forEach(formBuilder::addEncoded);
                    requestBuilder.method(this.method, formBuilder.build());
                } else {
                    requestBuilder.method(this.method, RequestBody.create(MediaType.parse(this.contentType + "; charset=" + this.charset), ""));
                }
            }
        }
        if (this.tag != null) {
            requestBuilder.tag(this.tag);
        }
        this.request = requestBuilder.build();
    }

    /**
     * 执行
     */
    private void execute() {
        Valid.notNull(this.url, "Request url is null");
        this.buildRequest();
        this.call = this.client.newCall(this.request);
        if (this.async) {
            this.response = new OkResponse().request(this.request).mockRequest(this);
            this.call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response res) {
                    try {
                        consumer.accept(response.call(call).response(res).done());
                    } catch (Exception e) {
                        consumer.accept(response.call(call).response(res).exception(e).done());
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    consumer.accept(response.call(call).exception(e).done());
                }
            });
        } else {
            try {
                this.response = new OkResponse(this.request, this.call.execute()).call(this.call).mockRequest(this);
            } catch (IOException e) {
                this.response = new OkResponse(this.request, e).call(this.call).mockRequest(this);
            }
        }
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public String getCharset() {
        return charset;
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

    public Map<String, String> getFormParts() {
        return formParts;
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

    public Object getTag() {
        return tag;
    }

    public boolean isSsl() {
        return ssl;
    }

    public boolean isAsync() {
        return async;
    }

    public Consumer<OkResponse> getConsumer() {
        return consumer;
    }

    public Request getRequest() {
        return request;
    }

    public Call getCall() {
        return call;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public OkResponse getResponse() {
        return response;
    }

    public List<HttpCookie> getCookies() {
        return cookies;
    }

    @Override
    public String toString() {
        return url;
    }

}
