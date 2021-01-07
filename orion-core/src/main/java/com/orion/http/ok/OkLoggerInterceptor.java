package com.orion.http.ok;

import com.orion.id.UUIds;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * OkHttp log拦截器
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/8 23:44
 */
public class OkLoggerInterceptor implements Interceptor {

    /**
     * LOG
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OkLoggerInterceptor.class);

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        long start = System.currentTimeMillis();
        String traceId = UUIds.random32();
        LOGGER.info("OK-Request START method: [{}], url: [{}], tag: [{}], traceId: [{}]", request.method(), request.url(), request.tag(), traceId);
        try {
            Response response = chain.proceed(request);
            long end = System.currentTimeMillis();
            LOGGER.info("OK-Http-Request END [use: {}ms], code: {}, success: {}, method: [{}], url: [{}], tag: [{}], traceId: [{}]", end - start, response.code(), response.isSuccessful(), request.method(), request.url(), request.tag(), traceId);
            return response;
        } catch (IOException e) {
            long end = System.currentTimeMillis();
            LOGGER.error("OK-Http-Request ERROR [use: {}ms], method: [{}], url: [{}], tag: [{}], traceId: [{}], errorMessage: [{}]", end - start, request.method(), request.url(), request.tag(), traceId, e.getMessage());
            throw e;
        }
    }

}
