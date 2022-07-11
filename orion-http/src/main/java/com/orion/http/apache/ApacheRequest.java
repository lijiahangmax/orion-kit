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
