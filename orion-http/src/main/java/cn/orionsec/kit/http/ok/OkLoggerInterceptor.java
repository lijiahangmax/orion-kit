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

import cn.orionsec.kit.lang.id.UUIds;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * OkHttp 日志拦截器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/8 23:44
 */
public class OkLoggerInterceptor implements Interceptor {

    /**
     * LOG
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OkLoggerInterceptor.class);

    private static final String DEF = "OK-Request ";

    private String suffix;

    public OkLoggerInterceptor() {
        this(DEF);
    }

    public OkLoggerInterceptor(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        long start = System.currentTimeMillis();
        String traceId = UUIds.random32();
        LOGGER.info(suffix + "START method: [{}], url: [{}], tag: [{}], traceId: [{}]", request.method(), request.url(), request.tag(), traceId);
        try {
            Response response = chain.proceed(request);
            long end = System.currentTimeMillis();
            LOGGER.info(suffix + "END [use: {}ms], code: {}, success: {}, method: [{}], url: [{}], tag: [{}], traceId: [{}]", end - start, response.code(), response.isSuccessful(), request.method(), request.url(), request.tag(), traceId);
            return response;
        } catch (IOException e) {
            long end = System.currentTimeMillis();
            LOGGER.error(suffix + "ERROR [use: {}ms], method: [{}], url: [{}], tag: [{}], traceId: [{}], errorMessage: [{}]", end - start, request.method(), request.url(), request.tag(), traceId, e.getMessage());
            throw e;
        }
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

}
