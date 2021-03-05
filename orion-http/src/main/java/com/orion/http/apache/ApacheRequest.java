package com.orion.http.apache;

import com.orion.able.Awaitable;
import com.orion.http.BaseRequest;
import com.orion.http.support.HttpContentType;
import com.orion.http.support.HttpMethod;
import com.orion.utils.Exceptions;
import com.orion.utils.Urls;
import com.orion.utils.Valid;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Apache HttpClient 请求
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/6/12 14:52
 */
public class ApacheRequest extends BaseRequest implements Awaitable<ApacheResponse> {

    /**
     * client
     */
    private CloseableHttpClient client;

    /**
     * HttpRequest
     */
    private HttpRequestBase request;

    /**
     * response
     */
    private ApacheResponse response;

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

    /**
     * client
     *
     * @param client client
     * @return this
     */
    public ApacheRequest client(CloseableHttpClient client) {
        this.client = client;
        return this;
    }

    /**
     * ssl
     *
     * @param ssl ssl
     * @return this
     */
    public ApacheRequest ssl(boolean ssl) {
        this.ssl = ssl;
        if (ssl) {
            this.client = ApacheClient.getSslClient();
        }
        return this;
    }

    /**
     * ssl
     *
     * @return this
     */
    public ApacheRequest ssl() {
        this.ssl = true;
        this.client = ApacheClient.getSslClient();
        return this;
    }

    /**
     * 取消请求
     *
     * @return this
     */
    private ApacheRequest cancel() {
        this.request.abort();
        return this;
    }

    @Override
    public ApacheResponse await() {
        execute();
        return this.response;
    }

    private void buildRequest() {
        this.method = this.method.trim().toUpperCase();
        if (HttpMethod.GET.getMethod().equals(this.method) || HttpMethod.HEAD.getMethod().equals(this.method)) {
            if (this.formParts != null) {
                if (this.queryParams == null) {
                    this.queryParams = new LinkedHashMap<>();
                }
                this.queryParams.putAll(this.formParts);
            }
        }
        if (queryParams != null) {
            this.queryString = Urls.buildQueryString(this.queryParams, this.queryStringEncode);
            this.url += ("?" + this.queryString);
        }
        this.getRequestByMethod();
        if (this.headers != null) {
            this.headers.forEach((k, v) -> this.request.addHeader(new BasicHeader(k, v)));
        }
        if (this.cookies != null) {
            this.cookies.forEach(c -> this.request.addHeader(new BasicHeader("Cookie", c.toString())));
        }
        if (this.ignoreHeaders != null) {
            this.ignoreHeaders.forEach(ignoreHeader -> this.request.removeHeader(new BasicHeader(ignoreHeader, null)));
        }
        if (!HttpMethod.GET.getMethod().equals(this.method) && !HttpMethod.HEAD.getMethod().equals(this.method)) {
            if (this.contentType == null) {
                this.contentType = HttpContentType.TEXT_PLAIN.getType();
            }
            if (this.charset == null) {
                this.request.setHeader(new BasicHeader("Content-type", this.contentType));
            } else {
                this.request.setHeader(new BasicHeader("Content-type", this.contentType + "; charset=" + this.charset));
            }
        }
    }

    private void execute() {
        Valid.notNull(this.url, "Request url is null");
        this.buildRequest();
        try (CloseableHttpResponse response = this.client.execute(this.request)) {
            this.response = new ApacheResponse(this.request, response)
                    .url(this.url)
                    .method(this.method);
            this.request.releaseConnection();
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    private void getRequestByMethod() {
        HttpMethod.validMethod(this.method, false);
        if (HttpMethod.GET.getMethod().equals(this.method)) {
            this.request = new HttpGet(this.url);
        } else if (HttpMethod.POST.getMethod().equals(this.method)) {
            HttpPost request = new HttpPost(this.url);
            request.setEntity(getEntry());
            this.request = request;
        } else if (HttpMethod.PUT.getMethod().equals(this.method)) {
            HttpPut request = new HttpPut(this.url);
            request.setEntity(getEntry());
            this.request = request;
        } else if (HttpMethod.DELETE.getMethod().equals(this.method)) {
            this.request = new HttpDelete(this.url);
        } else if (HttpMethod.PATCH.getMethod().equals(this.method)) {
            HttpPatch request = new HttpPatch(this.url);
            request.setEntity(getEntry());
            this.request = request;
        } else if (HttpMethod.HEAD.getMethod().equals(this.method)) {
            this.request = new HttpHead(this.url);
        } else if (HttpMethod.TRACE.getMethod().equals(this.method)) {
            this.request = new HttpTrace(this.url);
        } else if (HttpMethod.OPTIONS.getMethod().equals(this.method)) {
            this.request = new HttpOptions(this.url);
        }
    }

    private HttpEntity getEntry() {
        if (this.body != null) {
            return new ByteArrayEntity(this.body, this.bodyOffset, this.bodyLen);
        }
        if (!HttpMethod.GET.getMethod().equals(this.method) &&
                !HttpMethod.HEAD.getMethod().equals(this.method) &&
                this.formParts != null) {
            List<NameValuePair> pairs = new ArrayList<>();
            this.formParts.forEach((k, v) -> pairs.add(new BasicNameValuePair(k, v)));
            try {
                this.contentType = HttpContentType.APPLICATION_FORM.getType();
                if (this.charset == null) {
                    return new UrlEncodedFormEntity(pairs);
                } else {
                    return new UrlEncodedFormEntity(pairs, Charset.forName(charset));
                }
            } catch (Exception e) {
                throw Exceptions.unCoding(e);
            }
        }
        return null;
    }

    public CloseableHttpClient getClient() {
        return client;
    }

    public HttpUriRequest getRequest() {
        return request;
    }

    public ApacheResponse getResponse() {
        return response;
    }

}
