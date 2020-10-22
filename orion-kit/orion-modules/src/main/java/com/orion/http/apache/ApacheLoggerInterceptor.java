package com.orion.http.apache;

import com.orion.lang.wrapper.Args;
import org.apache.http.*;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hyper HttpClient 日志拦截器
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

    private static final ThreadLocal<Args.Three<String, String, Long>> REQUEST_STORE = new ThreadLocal<>();

    @Override
    public void process(HttpRequest httpRequest, HttpContext httpContext) {
        RequestLine request = httpRequest.getRequestLine();
        String method = request.getMethod();
        String uri = request.getUri();
        long start = System.currentTimeMillis();
        REQUEST_STORE.set(Args.of(method, uri, start));
        LOGGER.info("Hyper-Request START method: [{}], url: [{}], start: [{}]", method, uri, start);
    }

    @Override
    public void process(HttpResponse httpResponse, HttpContext httpContext) {
        Args.Three<String, String, Long> p = REQUEST_STORE.get();
        REQUEST_STORE.remove();
        StatusLine response = httpResponse.getStatusLine();
        long end = System.currentTimeMillis();
        int code = response.getStatusCode();
        LOGGER.info("OK-Request END [use: {}ms], code: {}, success: {}, method: [{}], url: [{}]", end - p.getArg3(), code, code >= 200 && code < 300, p.getArg1(), p.getArg2());
    }
}
