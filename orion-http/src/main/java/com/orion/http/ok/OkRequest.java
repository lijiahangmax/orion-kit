package com.orion.http.ok;

import com.orion.able.Asyncable;
import com.orion.able.Awaitable;
import com.orion.http.BaseRequest;
import com.orion.http.support.HttpContentType;
import com.orion.http.support.HttpMethod;
import com.orion.utils.Strings;
import com.orion.utils.Urls;
import com.orion.utils.Valid;
import okhttp3.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.function.Consumer;

/**
 * OkHttp 请求
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/7 23:49
 */
public class OkRequest extends BaseRequest implements Awaitable<OkResponse>, Asyncable<Consumer<OkResponse>> {

    /**
     * tag
     */
    private Object tag;

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
     * OkHttp Response
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
     * @return OkHttp Response
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
        Valid.notNull(consumer, "async call back is null");
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
                this.contentType = HttpContentType.TEXT_PLAIN.getType();
            }
            if (this.charset == null) {
                if (this.body != null) {
                    requestBuilder.method(this.method, RequestBody.create(MediaType.parse(this.contentType), this.body, this.bodyOffset, this.bodyLen));
                } else if (this.formParts != null && !show) {
                    FormBody.Builder formBuilder = new FormBody.Builder();
                    this.formParts.forEach(formBuilder::add);
                    requestBuilder.method(this.method, formBuilder.build());
                } else {
                    requestBuilder.method(this.method, RequestBody.create(MediaType.parse(this.contentType), Strings.EMPTY));
                }
            } else {
                if (this.body != null) {
                    requestBuilder.method(this.method, RequestBody.create(MediaType.parse(this.contentType + "; charset=" + this.charset), this.body, this.bodyOffset, this.bodyLen));
                } else if (this.formParts != null && !show) {
                    FormBody.Builder formBuilder = new FormBody.Builder(Charset.forName(this.charset));
                    this.formParts.forEach(formBuilder::addEncoded);
                    requestBuilder.method(this.method, formBuilder.build());
                } else {
                    requestBuilder.method(this.method, RequestBody.create(MediaType.parse(this.contentType + "; charset=" + this.charset), Strings.EMPTY));
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
        Valid.notNull(this.url, "request url is null");
        this.buildRequest();
        this.call = this.client.newCall(this.request);
        if (this.async) {
            this.response = new OkResponse().request(this.request).okRequest(this);
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
                this.response = new OkResponse(this.request, this.call.execute()).call(this.call).okRequest(this);
            } catch (IOException e) {
                this.response = new OkResponse(this.request, e).call(this.call).okRequest(this);
            }
        }
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

}
