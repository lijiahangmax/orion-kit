package com.orion.http.apache;

import com.orion.utils.Exceptions;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

/**
 * Apache HttpClient 请求
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/6/12 14:52
 */
public class ApacheRequest extends BaseApacheRequest {

    public ApacheRequest() {
        this.client = ApacheClient.getClient();
    }

    public ApacheRequest(CloseableHttpClient client) {
        this.client = client;
    }

    public ApacheRequest(String url) {
        this.url = url;
        this.client = ApacheClient.getClient();
    }

    public ApacheRequest(String url, CloseableHttpClient client) {
        this.url = url;
        this.client = client;
    }

    @Override
    protected void execute() {
        this.buildRequest();
        try (CloseableHttpResponse resp = client.execute(request)) {
            this.response = new ApacheResponse(url, method, request, resp);
        } catch (IOException e) {
            throw Exceptions.httpRequest(e);
        } finally {
            request.releaseConnection();
        }
    }

    @Override
    public ApacheResponse await() {
        this.execute();
        return this.response;
    }

}
