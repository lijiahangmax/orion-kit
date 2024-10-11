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
package com.orion.http.apache;

import com.orion.lang.utils.Exceptions;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

/**
 * Apache HttpClient 请求
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/6/12 14:52
 */
public class ApacheRequest extends BaseApacheRequest {

    public ApacheRequest() {
        this(null, ApacheRequests.getClient());
    }

    public ApacheRequest(CloseableHttpClient client) {
        this(null, client);
    }

    public ApacheRequest(String url) {
        this(url, ApacheRequests.getClient());
    }

    public ApacheRequest(String url, CloseableHttpClient client) {
        this.url = url;
        this.client = client;
    }

    @Override
    public ApacheResponse await() {
        this.buildRequest();
        try (CloseableHttpResponse resp = client.execute(request)) {
            return new ApacheResponse(url, resp);
        } catch (IOException e) {
            throw Exceptions.httpRequest(url, e);
        } finally {
            request.releaseConnection();
        }
    }

}
