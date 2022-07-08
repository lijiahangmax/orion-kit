package com.orion.http.ok;

import com.orion.http.useragent.StandardUserAgent;
import com.orion.lang.able.Asyncable;
import com.orion.lang.able.Awaitable;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Valid;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * OkHttp 请求
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/7 23:49
 */
public class OkRequest extends BaseOkRequest implements Awaitable<OkResponse>, Asyncable<Consumer<OkResponse>> {

    /**
     * 是否为异步
     */
    private boolean async;

    /**
     * 异步执行失败是否抛出异常
     */
    private boolean asyncFailThrows;

    /**
     * 异步接口
     */
    private Consumer<OkResponse> asyncCallback;

    public OkRequest() {
        this(null, OkClient.getClient());
    }

    public OkRequest(OkHttpClient client) {
        this(null, client);
    }

    public OkRequest(String url) {
        this(url, OkClient.getClient());
    }

    public OkRequest(String url, OkHttpClient client) {
        this.url = url;
        this.client = client;
        this.userAgent(StandardUserAgent.CHROME_3);
    }

    public OkRequest asyncFailThrows(boolean asyncFailThrows) {
        this.asyncFailThrows = asyncFailThrows;
        return this;
    }

    public OkRequest asyncFailThrows() {
        this.asyncFailThrows = true;
        return this;
    }

    @Override
    public OkResponse await() {
        this.execute();
        return this.response;
    }

    @Override
    public void async(Consumer<OkResponse> callback) {
        Valid.notNull(callback, "async call back is null");
        this.asyncCallback = callback;
        this.async = true;
        this.execute();
    }

    @Override
    protected void execute() {
        super.buildRequest();
        this.call = client.newCall(request);
        if (!async) {
            // sync
            try (Response resp = call.execute()) {
                response = new OkResponse(url, tag, resp);
                return;
            } catch (IOException e) {
                throw Exceptions.httpRequest(url, e);
            }
        }
        // async
        this.response = new OkResponse(url, tag);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response res) {
                response.asyncSetResponse(res);
                asyncCallback.accept(response);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                response.error(e);
                if (asyncFailThrows) {
                    throw Exceptions.httpRequest(url, "async ok request on failure: " + OkRequest.super.getRequestMessage(), e);
                }
                asyncCallback.accept(response);
            }
        });
    }

    public boolean isAsync() {
        return async;
    }

    public Consumer<OkResponse> getAsyncCallback() {
        return asyncCallback;
    }

}
