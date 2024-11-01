/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package cn.orionsec.kit.http.apache;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

/**
 * Apache 重试策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/5 11:27
 */
public enum ApacheClientRetryPolicy {

    /**
     * 不重试
     */
    NO_RETRY((exception, executionCount, context) -> false),

    /**
     * 自动重试
     */
    AUTO_RETRY((e, i, httpContext) -> {
        if (i > 3) {
            // 重试超过3次, 放弃请求
            return false;
        }
        if (e instanceof NoHttpResponseException) {
            // 服务器没有响应, 可能是服务器断开了连接, 应该重试
            return true;
        }
        if (e instanceof SSLHandshakeException) {
            // SSL握手异常
            return false;
        }
        if (e instanceof InterruptedIOException) {
            // 超时
            return false;
        }
        if (e instanceof UnknownHostException) {
            // 服务器不可达
            return false;
        }
        if (e instanceof SSLException) {
            return false;
        }
        HttpClientContext context = HttpClientContext.adapt(httpContext);
        HttpRequest request = context.getRequest();
        // 如果请求不是关闭连接的请求
        return !(request instanceof HttpEntityEnclosingRequest);
    });

    private final HttpRequestRetryHandler handler;

    ApacheClientRetryPolicy(HttpRequestRetryHandler handler) {
        this.handler = handler;
    }

    public HttpRequestRetryHandler getHandler() {
        return handler;
    }

}
