package com.orion.http.apache;

import com.orion.lang.define.wrapper.Tuple;
import com.orion.lang.id.UUIds;
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
