package com.orion.http.ok;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Mock log拦截器
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
        LOGGER.info("OK-Request START method: [{}], url: [{}], tag: [{}]", request.method(), request.url(), request.tag());
        try {
            Response response = chain.proceed(request);
            long end = System.currentTimeMillis();
            LOGGER.info("OK-Request END [use: {}ms], code: {}, success: {}, method: [{}], url: [{}], tag: [{}]", end - start, response.code(), response.isSuccessful(), request.method(), request.url(), request.tag());
            return response;
        } catch (IOException e) {
            long end = System.currentTimeMillis();
            LOGGER.error("OK-Request ERROR [use: {}ms], method: [{}], url: [{}], tag: [{}], errorMessage: [{}]", end - start, request.method(), request.url(), request.tag(), e.getMessage());
            throw e;
        }
    }

}
