package com.orion.http.apache;

import com.orion.able.Awaitable;
import com.orion.constant.StandardContentType;
import com.orion.http.BaseHttpRequest;
import com.orion.http.support.HttpContentType;
import com.orion.http.support.HttpMethod;
import com.orion.utils.Exceptions;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * ApacheHttp请求 基类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2021/3/7 17:20
 */
public abstract class BaseApacheRequest extends BaseHttpRequest implements Awaitable<ApacheResponse> {

    /**
     * client
     */
    protected CloseableHttpClient client;

    /**
     * HttpRequest
     */
    protected HttpRequestBase request;

    /**
     * response
     */
    protected ApacheResponse response;

    /**
     * client
     *
     * @param client client
     * @return this
     */
    public BaseApacheRequest client(CloseableHttpClient client) {
        this.client = client;
        return this;
    }

    /**
     * ssl
     *
     * @param ssl ssl
     * @return this
     */
    public BaseApacheRequest ssl(boolean ssl) {
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
    public BaseApacheRequest ssl() {
        this.ssl = true;
        this.client = ApacheClient.getSslClient();
        return this;
    }

    /**
     * 取消请求
     *
     * @return this
     */
    public BaseApacheRequest cancel() {
        this.request.abort();
        return this;
    }

    @Override
    protected void buildRequest() {
        super.buildRequest();
        this.getRequestByMethod();
        if (headers != null) {
            headers.forEach((k, v) -> request.addHeader(new BasicHeader(k, v)));
        }
        if (cookies != null) {
            cookies.forEach(c -> request.addHeader(new BasicHeader("Cookie", c.toString())));
        }
        if (ignoreHeaders != null) {
            ignoreHeaders.forEach(ignoreHeader -> request.removeHeader(new BasicHeader(ignoreHeader, null)));
        }
        if (!super.isNoBodyRequest()) {
            return;
        }
        request.setHeader(new BasicHeader(StandardContentType.CONTENT_TYPE, contentType + "; charset=" + charset));
    }

    /**
     * 获取 method
     */
    protected void getRequestByMethod() {
        HttpMethod.valid(method);
        if (HttpMethod.POST.method().equals(method)) {
            request = new HttpPost(url);
        } else if (HttpMethod.PATCH.method().equals(method)) {
            request = new HttpPatch(url);
        } else if (HttpMethod.PUT.method().equals(method)) {
            request = new HttpPut(url);
        } else if (HttpMethod.GET.method().equals(method)) {
            request = new HttpGet(url);
        } else if (HttpMethod.DELETE.method().equals(method)) {
            request = new HttpDelete(url);
        } else if (HttpMethod.HEAD.method().equals(method)) {
            request = new HttpHead(url);
        } else if (HttpMethod.TRACE.method().equals(method)) {
            request = new HttpTrace(url);
        } else if (HttpMethod.OPTIONS.method().equals(method)) {
            request = new HttpOptions(url);
        }
        if (request instanceof HttpEntityEnclosingRequestBase) {
            ((HttpEntityEnclosingRequestBase) request).setEntity(this.getEntry());
        }
    }

    /**
     * 获取 entity
     *
     * @return entity
     */
    protected HttpEntity getEntry() {
        if (body != null) {
            return new ByteArrayEntity(body, bodyOffset, bodyLen);
        } else if (formParts != null) {
            List<NameValuePair> pairs = new ArrayList<>();
            formParts.forEach((k, v) -> pairs.add(new BasicNameValuePair(k, v)));
            try {
                contentType = HttpContentType.APPLICATION_FORM.getType();
                return new UrlEncodedFormEntity(pairs, Charset.forName(charset));
            } catch (Exception e) {
                throw Exceptions.unsupportedEncoding(e);
            }
        }
        return null;
    }

    public CloseableHttpClient getClient() {
        return client;
    }

    public ApacheResponse getResponse() {
        return response;
    }

}
