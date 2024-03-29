package com.orion.http.apache;

import com.orion.http.BaseHttpRequest;
import com.orion.http.support.HttpContentType;
import com.orion.http.support.HttpMethod;
import com.orion.lang.able.Awaitable;
import com.orion.lang.constant.StandardContentType;
import com.orion.lang.constant.StandardHttpHeader;
import com.orion.lang.utils.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * ApacheHttp 请求 基类
 *
 * @author Jiahang Li
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
            cookies.forEach(c -> request.addHeader(new BasicHeader(StandardHttpHeader.COOKIE, c.toString())));
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
            this.request = new HttpPost(url);
        } else if (HttpMethod.PATCH.method().equals(method)) {
            this.request = new HttpPatch(url);
        } else if (HttpMethod.PUT.method().equals(method)) {
            this.request = new HttpPut(url);
        } else if (HttpMethod.GET.method().equals(method)) {
            this.request = new HttpGet(url);
        } else if (HttpMethod.DELETE.method().equals(method)) {
            this.request = new HttpDelete(url);
        } else if (HttpMethod.HEAD.method().equals(method)) {
            this.request = new HttpHead(url);
        } else if (HttpMethod.TRACE.method().equals(method)) {
            this.request = new HttpTrace(url);
        } else if (HttpMethod.OPTIONS.method().equals(method)) {
            this.request = new HttpOptions(url);
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
            this.contentType = HttpContentType.APPLICATION_FORM.getType();
            return new UrlEncodedFormEntity(pairs, Charsets.of(charset));
        }
        return null;
    }

    public CloseableHttpClient getClient() {
        return client;
    }

}
