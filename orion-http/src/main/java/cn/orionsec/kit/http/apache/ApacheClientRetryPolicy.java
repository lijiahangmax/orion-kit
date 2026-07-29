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
package cn.orionsec.kit.http.apache;

import org.apache.hc.client5.http.HttpRequestRetryStrategy;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.NoHttpResponseException;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.util.TimeValue;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
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
    NO_RETRY(new HttpRequestRetryStrategy() {
        @Override
        public boolean retryRequest(HttpRequest request, IOException exception, int execCount, HttpContext context) {
            return false;
        }

        @Override
        public boolean retryRequest(HttpResponse response, int execCount, HttpContext context) {
            return false;
        }

        @Override
        public TimeValue getRetryInterval(HttpResponse response, int execCount, HttpContext context) {
            return TimeValue.ofMilliseconds(0);
        }
    }),

    /**
     * 自动重试
     */
    AUTO_RETRY(new HttpRequestRetryStrategy() {
        @Override
        public boolean retryRequest(HttpRequest request, IOException e, int execCount, HttpContext context) {
            if (execCount > 3) {
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
            return true;
        }

        @Override
        public boolean retryRequest(HttpResponse response, int execCount, HttpContext context) {
            return false;
        }

        @Override
        public TimeValue getRetryInterval(HttpResponse response, int execCount, HttpContext context) {
            return TimeValue.ofMilliseconds(0);
        }
    });

    private final HttpRequestRetryStrategy strategy;

    ApacheClientRetryPolicy(HttpRequestRetryStrategy strategy) {
        this.strategy = strategy;
    }

    public HttpRequestRetryStrategy getStrategy() {
        return strategy;
    }

}
