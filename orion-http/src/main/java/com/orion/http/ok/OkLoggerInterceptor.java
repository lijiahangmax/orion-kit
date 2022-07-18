package com.orion.http.ok;

import com.orion.lang.id.UUIds;
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
