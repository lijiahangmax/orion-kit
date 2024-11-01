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

import cn.orionsec.kit.lang.define.wrapper.Tuple;
import cn.orionsec.kit.lang.id.UUIds;
import org.apache.http.*;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Apache HttpClient 日志拦截器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/6/12 15:07
 */
public class ApacheLoggerInterceptor implements HttpRequestInterceptor, HttpResponseInterceptor {

    /**
     * LOG
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApacheLoggerInterceptor.class);

    private static final ThreadLocal<Tuple> HOLDER = new ThreadLocal<>();

    private static final String DEF = "HTTP-Request ";

    private String suffix;

    public ApacheLoggerInterceptor() {
        this(DEF);
    }

    public ApacheLoggerInterceptor(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public void process(HttpRequest httpRequest, HttpContext httpContext) {
        long start = System.currentTimeMillis();
        RequestLine request = httpRequest.getRequestLine();
        String method = request.getMethod();
        String uri = request.getUri();
        String traceId = UUIds.random32();
        HOLDER.set(Tuple.of(method, uri, start, traceId));
        LOGGER.info(suffix + "START method: [{}], url: [{}], start: [{}], traceId: [{}]", method, uri, start, traceId);
    }

    @Override
    public void process(HttpResponse httpResponse, HttpContext httpContext) {
        long end = System.currentTimeMillis();
        Tuple tuple = HOLDER.get();
        HOLDER.remove();
        StatusLine response = httpResponse.getStatusLine();
        int code = response.getStatusCode();
        LOGGER.info(suffix + "END [use: {}ms], code: {}, method: [{}], url: [{}], traceId: [{}]",
                end - (Long) tuple.get(2), code, tuple.get(0), tuple.get(1), tuple.get(3));
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

}
