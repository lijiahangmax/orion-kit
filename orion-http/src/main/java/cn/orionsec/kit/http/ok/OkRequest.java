/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.http.ok;

import cn.orionsec.kit.lang.able.Asyncable;
import cn.orionsec.kit.lang.able.Awaitable;
import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.Exceptions;
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
        this(null, OkRequests.getClient());
    }

    public OkRequest(OkHttpClient client) {
        this(null, client);
    }

    public OkRequest(String url) {
        this(url, OkRequests.getClient());
    }

    public OkRequest(String url, OkHttpClient client) {
        this.url = url;
        this.client = client;
        this.userAgent(DEFAULT_USERAGENT);
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
        super.buildRequest();
        this.call = client.newCall(request);
        // sync
        try (Response resp = call.execute()) {
            return new OkResponse(url, tag, resp);
        } catch (IOException e) {
            throw Exceptions.httpRequest(url, e);
        }
    }

    @Override
    public void async(Consumer<OkResponse> callback) {
        Assert.notNull(callback, "async call back is null");
        this.asyncCallback = callback;
        this.async = true;
        super.buildRequest();
        this.call = client.newCall(request);
        // async
        OkResponse response = new OkResponse(url, tag);
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
                    throw Exceptions.httpRequest(url, "async ok request on failure: " + getRequestMessage(), e);
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
