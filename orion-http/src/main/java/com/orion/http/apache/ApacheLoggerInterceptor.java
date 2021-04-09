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

    private static final ThreadLocal<Args.Four<String, String, Long, String>> HOLDER = new ThreadLocal<>();

    private static final String DEF = "Apache-HTTP-Request ";

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
        HOLDER.set(Args.of(method, uri, start, traceId));
        LOGGER.info(suffix + "START method: [{}], url: [{}], start: [{}], traceId: [{}]", method, uri, start, traceId);
    }

    @Override
    public void process(HttpResponse httpResponse, HttpContext httpContext) {
        long end = System.currentTimeMillis();
        Args.Four<String, String, Long, String> p = HOLDER.get();
        HOLDER.remove();
        StatusLine response = httpResponse.getStatusLine();
        int code = response.getStatusCode();
        LOGGER.info(suffix + "END [use: {}ms], code: {}, success: {}, method: [{}], url: [{}], traceId: [{}]", end - p.getArg3(), code, code >= 200 && code < 400, p.getArg1(), p.getArg2(), p.getArg4());
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

}
