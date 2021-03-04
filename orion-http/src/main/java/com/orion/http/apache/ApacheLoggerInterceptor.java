package com.orion.http.apache;

import com.orion.id.UUIds;
import com.orion.lang.wrapper.Args;
import org.apache.http.*;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Apache HttpClient 日志拦截器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/6/12 15:07
 */
public class ApacheLoggerInterceptor implements HttpRequestInterceptor, HttpResponseInterceptor {

    /**
     * LOG
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApacheLoggerInterceptor.class);

    private static final ThreadLocal<Args.Four<String, String, Long, String>> REQUEST_STORE = new ThreadLocal<>();

    @Override
    public void process(HttpRequest httpRequest, HttpContext httpContext) {
        RequestLine request = httpRequest.getRequestLine();
        String method = request.getMethod();
        String uri = request.getUri();
        String traceId = UUIds.random32();
        long start = System.currentTimeMillis();
        REQUEST_STORE.set(Args.of(method, uri, start, traceId));
        LOGGER.info("Apache-HTTP-Request START method: [{}], url: [{}], start: [{}], traceId: [{}]", method, uri, start, traceId);
    }

    @Override
    public void process(HttpResponse httpResponse, HttpContext httpContext) {
        Args.Four<String, String, Long, String> p = REQUEST_STORE.get();
        REQUEST_STORE.remove();
        StatusLine response = httpResponse.getStatusLine();
        long end = System.currentTimeMillis();
        int code = response.getStatusCode();
        LOGGER.info("Apache-HTTP-Request END [use: {}ms], code: {}, success: {}, method: [{}], url: [{}], traceId: [{}]", end - p.getArg3(), code, code >= 200 && code < 300, p.getArg1(), p.getArg2(), p.getArg4());
    }
}
