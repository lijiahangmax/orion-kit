package com.orion.mock;

import com.orion.utils.Arrays1;
import com.orion.utils.Urls;
import com.orion.utils.Valid;
import okhttp3.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Mock 请求
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/7 23:49
 */
public class MockRequest {

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
    private Consumer<MockResult> consumer;

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
     * MockResult
     */
    private MockResult result;

    public MockRequest() {
    }

    public MockRequest(String url) {
        this.url = url;
    }

    public MockRequest(MockApi mockApi) {
        this.url = mockApi.url;
        this.method = mockApi.method.getMethod();
        this.tag = mockApi.tag;
        this.ssl = mockApi.ssl;
        if (mockApi.contentType != null) {
            this.contentType = mockApi.contentType.getType();
        }
    }

    /**
     * url
     *
     * @param url url
     * @return this
     */
    public MockRequest url(String url) {
        this.url = url;
        return this;
    }

    /**
     * method
     *
     * @param method method
     * @return this
     */
    public MockRequest method(String method) {
        this.method = method;
        return this;
    }

    /**
     * method
     *
     * @param method method
     * @return this
     */
    public MockRequest method(MockMethod method) {
        this.method = method.getMethod();
        return this;
    }

    /**
     * charset
     *
     * @param charset charset
     * @return this
     */
    public MockRequest charset(String charset) {
        this.charset = charset;
        return this;
    }

    /**
     * contentType
     *
     * @param contentType contentType
     * @return this
     */
    public MockRequest contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * contentType
     *
     * @param contentType contentType
     * @return this
     */
    public MockRequest contentType(MockContent contentType) {
        this.contentType = contentType.getType();
        return this;
    }

    /**
     * queryParams
     *
     * @param queryParams queryParams
     * @return this
     */
    public MockRequest queryParams(Map<String, String> queryParams) {
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
    public MockRequest queryParam(String key, String value) {
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
    public MockRequest queryParam(Map.Entry<String, String> query) {
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
    public MockRequest queryStringEncode() {
        this.queryStringEncode = true;
        return this;
    }

    /**
     * body
     *
     * @param body body
     * @return this
     */
    public MockRequest body(String body) {
        if (body == null) {
            return this;
        }
        this.body = body.getBytes();
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
    public MockRequest body(byte[] body) {
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
    public MockRequest body(byte[] body, int offset, int len) {
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
    public MockRequest headers(Map<String, String> headers) {
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
    public MockRequest headers(String key, String value) {
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
    public MockRequest headers(Map.Entry<String, String> header) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        this.headers.put(header.getKey(), header.getValue());
        return this;
    }

    /**
     * tag
     *
     * @param tag tag
     * @return this
     */
    public MockRequest tag(Object tag) {
        this.tag = tag;
        return this;
    }

    /**
     * ignoreHeaders
     *
     * @return this
     */
    public MockRequest ignoreHeaders(String... ignoreHeaders) {
        this.ignoreHeaders = ignoreHeaders;
        return this;
    }

    /**
     * ssl
     *
     * @param ssl ssl
     * @return this
     */
    public MockRequest ssl(boolean ssl) {
        this.ssl = ssl;
        return this;
    }

    /**
     * ssl
     *
     * @return this
     */
    public MockRequest ssl() {
        this.ssl = true;
        return this;
    }

    /**
     * 取消请求
     *
     * @return this
     */
    private MockRequest cancel() {
        this.call.cancel();
        return this;
    }

    /**
     * 同步调用
     *
     * @return MockResult
     */
    public MockResult await() {
        execute();
        return this.result;
    }

    /**
     * 异步调用
     *
     * @param consumer consumer
     */
    public void async(Consumer<MockResult> consumer) {
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
        if (this.headers != null) {
            requestBuilder.headers(Headers.of(this.headers));
        }
        if (this.ignoreHeaders != null) {
            for (String ignoreHeader : this.ignoreHeaders) {
                requestBuilder.removeHeader(ignoreHeader);
            }
        }
        if (this.queryParams != null) {
            this.queryString = Urls.buildUrl(this.queryParams);
            if (this.queryStringEncode) {
                this.queryString = Urls.encode(this.queryString);
            }
            this.url += ("?" + this.queryString);
        }
        requestBuilder.url(this.url);
        this.method = this.method.toUpperCase();
        if (MockMethod.GET.getMethod().equals(this.method)) {
            requestBuilder.get();
        } else if (MockMethod.HEAD.getMethod().equals(this.method)) {
            requestBuilder.head();
        } else {
            if (this.contentType == null) {
                this.contentType = MockContent.TEXT_PLAIN.getType();
            }
            if (this.charset == null) {
                if (this.body == null) {
                    requestBuilder.method(this.method, RequestBody.create(MediaType.parse(this.contentType), ""));
                } else {
                    if (this.bodyLen == 0) {
                        this.bodyLen = this.body.length - this.bodyOffset;
                    }
                    requestBuilder.method(this.method, RequestBody.create(MediaType.parse(this.contentType), this.body, this.bodyOffset, this.bodyLen));
                }
            } else {
                if (this.body == null) {
                    requestBuilder.method(this.method, RequestBody.create(MediaType.parse(this.contentType + "; charset=" + this.charset), ""));
                } else {
                    if (this.bodyLen == 0) {
                        this.bodyLen = this.body.length - this.bodyOffset;
                    }
                    requestBuilder.method(this.method, RequestBody.create(MediaType.parse(this.contentType + "; charset=" + this.charset), this.body, this.bodyOffset, this.bodyLen));
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
        if (this.client == null) {
            if (this.ssl) {
                this.client = MockClient.getSslClient();
            } else {
                this.client = MockClient.getClient();
            }
        }
        this.call = this.client.newCall(this.request);
        if (this.async) {
            this.result = new MockResult().request(this.request).mockRequest(this);
            this.call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        consumer.accept(result.call(call).response(response).done());
                    } catch (Exception e) {
                        consumer.accept(result.call(call).response(response).exception(e).done());
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    consumer.accept(result.call(call).exception(e).done());
                }
            });
        } else {
            try {
                this.result = new MockResult(this.request, this.call.execute()).call(this.call).mockRequest(this);
            } catch (IOException e) {
                this.result = new MockResult(this.request, e).call(this.call).mockRequest(this);
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

    public Object getTag() {
        return tag;
    }

    public boolean isSsl() {
        return ssl;
    }

    public boolean isAsync() {
        return async;
    }

    public Consumer<MockResult> getConsumer() {
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

    public MockResult getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "MockRequest{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", charset='" + charset + '\'' +
                ", contentType='" + contentType + '\'' +
                ", queryParams=" + queryParams +
                ", queryString='" + queryString + '\'' +
                ", queryStringEncode=" + queryStringEncode +
                ", bodyLength=" + Arrays1.length(body) +
                ", bodyOffset=" + bodyOffset +
                ", bodyLen=" + bodyLen +
                ", headers=" + headers +
                ", ignoreHeaders=" + Arrays.toString(ignoreHeaders) +
                ", tag=" + tag +
                ", ssl=" + ssl +
                ", async=" + async +
                ", consumer=" + consumer +
                ", request=" + request +
                ", call=" + call +
                ", client=" + client +
                ", result=" + result +
                '}';
    }

}
