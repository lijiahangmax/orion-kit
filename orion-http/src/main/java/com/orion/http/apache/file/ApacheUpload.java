package com.orion.http.apache.file;

import com.orion.http.support.HttpCookie;
import com.orion.http.support.HttpUploadPart;
import com.orion.http.useragent.StandardUserAgent;
import com.orion.utils.Strings;
import com.orion.utils.Urls;
import com.orion.utils.collect.Lists;
import com.orion.utils.io.Streams;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Apache 文件上传
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/6/13 1:03
 */
public class ApacheUpload {

    /**
     * url
     */
    private String url;

    /**
     * client
     */
    private CloseableHttpClient client;

    /**
     * 是否执行完毕
     */
    private boolean done;

    /**
     * 开始时间
     */
    private long startDate;

    /**
     * 结束时间
     */
    private long endDate;

    /**
     * 请求参数
     */
    private Map<String, String> queryParams;

    /**
     * 请求参数
     */
    private String queryString;

    /**
     * 请求参数是否编码
     */
    private boolean queryStringEncode;

    /**
     * form请求参数
     */
    private Map<String, String> formParts;

    /**
     * cookies
     */
    private List<HttpCookie> cookies;

    /**
     * 请求头
     */
    private Map<String, String> headers;

    /**
     * 忽略的请求头
     */
    private List<String> ignoreHeaders;

    /**
     * request
     */
    private HttpPost request;

    /**
     * response
     */
    private CloseableHttpResponse response;

    /**
     * exception
     */
    private Exception exception;

    public ApacheUpload(String url) {
        this.url = url;
    }

    public ApacheUpload(String url, CloseableHttpClient client) {
        this.url = url;
        this.client = client;
    }

    public ApacheUpload client(CloseableHttpClient client) {
        this.client = client;
        return this;
    }

    /**
     * 格式化url的参数 {}
     *
     * @param o 参数
     * @return this
     */
    public ApacheUpload format(Object... o) {
        this.url = Strings.format(this.url, o);
        return this;
    }

    /**
     * 格式化url的参数 ${?}
     *
     * @param map 参数
     * @return this
     */
    public ApacheUpload format(Map<String, Object> map) {
        this.url = Strings.format(this.url, map);
        return this;
    }

    /**
     * queryStringEncode
     *
     * @return this
     */
    public ApacheUpload queryStringEncode() {
        this.queryStringEncode = true;
        return this;
    }

    /**
     * queryStringEncode
     *
     * @param encode ignore
     * @return this
     */
    public ApacheUpload queryStringEncode(boolean encode) {
        this.queryStringEncode = encode;
        return this;
    }

    /**
     * headers
     *
     * @param headers headers
     * @return this
     */
    public ApacheUpload headers(Map<String, String> headers) {
        if (this.headers == null) {
            this.headers = headers;
        } else {
            this.headers.putAll(headers);
        }
        return this;
    }

    /**
     * headers
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public ApacheUpload headers(String key, String value) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        this.headers.put(key, value);
        return this;
    }

    /**
     * UserAgent
     *
     * @param value value
     * @return this
     */
    public ApacheUpload userAgent(String value) {
        if (this.headers == null) {
            this.headers = new LinkedHashMap<>();
        }
        this.headers.put(StandardUserAgent.USER_AGENT, value);
        return this;
    }

    /**
     * cookie
     *
     * @param cookie cookie
     * @return this
     */
    public ApacheUpload cookie(HttpCookie cookie) {
        if (this.cookies == null) {
            this.cookies = new ArrayList<>();
        }
        this.cookies.add(cookie);
        return this;
    }

    /**
     * cookies
     *
     * @param cookies cookies
     * @return this
     */
    public ApacheUpload cookies(List<HttpCookie> cookies) {
        if (this.cookies == null) {
            this.cookies = cookies;
        } else {
            this.cookies.addAll(cookies);
        }
        return this;
    }

    /**
     * ignoreHeader
     *
     * @param ignoreHeader ignoreHeader
     * @return this
     */
    public ApacheUpload ignoreHeader(String ignoreHeader) {
        if (ignoreHeader == null) {
            return this;
        }
        if (this.ignoreHeaders == null) {
            this.ignoreHeaders = new ArrayList<>();
        }
        this.ignoreHeaders.add(ignoreHeader);
        return this;
    }

    /**
     * ignoreHeaders
     *
     * @param ignoreHeaders ignoreHeaders
     * @return this
     */
    public ApacheUpload ignoreHeaders(String... ignoreHeaders) {
        if (ignoreHeaders == null) {
            return this;
        }
        if (this.ignoreHeaders == null) {
            this.ignoreHeaders = new ArrayList<>();
        }
        this.ignoreHeaders.addAll(Arrays.asList(ignoreHeaders));
        return this;
    }

    /**
     * ignoreHeaders
     *
     * @param ignoreHeaders ignoreHeaders
     * @return this
     */
    public ApacheUpload ignoreHeaders(List<String> ignoreHeaders) {
        if (this.ignoreHeaders == null) {
            this.ignoreHeaders = ignoreHeaders;
        } else {
            this.ignoreHeaders.addAll(ignoreHeaders);
        }
        return this;
    }

    /**
     * 添加url参数
     *
     * @param queryParams 参数
     * @return this
     */
    public ApacheUpload queryParams(Map<String, String> queryParams) {
        if (this.queryParams == null) {
            this.queryParams = queryParams;
        } else {
            this.queryParams.putAll(queryParams);
        }
        return this;
    }

    /**
     * 添加url参数
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public ApacheUpload queryParam(String key, String value) {
        if (this.queryParams == null) {
            this.queryParams = new LinkedHashMap<>();
        }
        this.queryParams.put(key, value);
        return this;
    }

    /**
     * 添加表单参数
     *
     * @param formParts 参数
     * @return this
     */
    public ApacheUpload formParts(Map<String, String> formParts) {
        if (this.formParts == null) {
            this.formParts = formParts;
        } else {
            this.formParts.putAll(formParts);
        }
        return this;
    }

    /**
     * 添加表单参数
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public ApacheUpload formPart(String key, String value) {
        if (this.formParts == null) {
            this.formParts = new LinkedHashMap<>();
        }
        this.formParts.put(key, value);
        return this;
    }

    /**
     * 取消请求
     *
     * @return this
     */
    private ApacheUpload cancel() {
        this.request.abort();
        return this;
    }

    /**
     * 上传单文件
     *
     * @param part ignore
     */
    public ApacheUpload upload(HttpUploadPart part) {
        return upload(Lists.of(part));
    }

    /**
     * 上传多文件
     *
     * @param parts ignore
     */
    public ApacheUpload upload(List<HttpUploadPart> parts) {
        if (this.queryParams != null) {
            this.queryString = Urls.buildQueryString(this.queryParams, this.queryStringEncode);
            this.url += ("?" + this.queryString);
        }
        this.request = new HttpPost(url);
        if (this.headers != null) {
            this.headers.forEach(request::addHeader);
        }
        if (this.cookies != null) {
            this.cookies.forEach(c -> this.request.addHeader(new BasicHeader("Cookie", c.toString())));
        }
        if (this.ignoreHeaders != null) {
            this.ignoreHeaders.forEach(request::removeHeaders);
        }
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        if (queryParams != null) {
            formParts.forEach(builder::addTextBody);
        }
        for (HttpUploadPart part : parts) {
            String key = part.getKey();
            String contentType = part.getContentType();
            File file = part.getFile();
            String fileName = part.getFileName();
            byte[] bytes = part.getBytes();
            InputStream in = part.getIn();
            if (in != null) {
                builder.addBinaryBody(key, in, contentType == null ? ContentType.APPLICATION_OCTET_STREAM : ContentType.parse(contentType), fileName);
            } else if (file != null) {
                builder.addBinaryBody(key, file, contentType == null ? ContentType.APPLICATION_OCTET_STREAM : ContentType.parse(contentType), fileName);
            } else if (bytes != null) {
                builder.addBinaryBody(key, bytes, contentType == null ? ContentType.APPLICATION_OCTET_STREAM : ContentType.parse(contentType), fileName);
            }
        }
        this.request.setEntity(builder.build());
        try {
            this.startDate = System.currentTimeMillis();
            this.response = client.execute(this.request);
        } catch (IOException e) {
            this.exception = e;
        } finally {
            this.endDate = System.currentTimeMillis();
            this.done = true;
            this.request.releaseConnection();
            Streams.close(this.response);
        }
        return this;
    }

    public String getUrl() {
        return url;
    }

    public HttpClient getClient() {
        return client;
    }

    public boolean isDone() {
        return done;
    }

    public boolean isError() {
        return exception != null;
    }

    public boolean isSuccess() {
        if (this.response == null) {
            return false;
        }
        int code = this.response.getStatusLine().getStatusCode();
        return code >= 200 && code < 300;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public long getUseDate() {
        if (startDate == 0) {
            return 0;
        }
        if (endDate == 0) {
            return System.currentTimeMillis() - startDate;
        } else {
            return endDate - startDate;
        }
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public String getQueryString() {
        return queryString;
    }

    public boolean isQueryStringEncode() {
        return queryStringEncode;
    }

    public Map<String, String> getFormParts() {
        return formParts;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public List<String> getIgnoreHeaders() {
        return ignoreHeaders;
    }

    public HttpPost getRequest() {
        return request;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public Exception getException() {
        return exception;
    }

    public List<HttpCookie> getCookies() {
        return cookies;
    }

    @Override
    public String toString() {
        return url;
    }

}
