package com.orion.http.ok.file;

import com.orion.http.common.HttpCookie;
import com.orion.http.common.HttpUploadPart;
import com.orion.http.ok.OkResponse;
import com.orion.utils.Strings;
import com.orion.utils.Urls;
import com.orion.utils.collect.Lists;
import okhttp3.*;

import java.io.IOException;
import java.util.*;

/**
 * OkHttp 上传文件
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/9 14:09
 */
public class OkUpload {

    /**
     * url
     */
    private String url;

    /**
     * client
     */
    private OkHttpClient client;

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
     * url请求参数
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
     * 请求头
     */
    private Map<String, String> headers;

    /**
     * cookies
     */
    private List<HttpCookie> cookies;

    /**
     * 忽略的请求头
     */
    private List<String> ignoreHeaders;

    /**
     * tag
     */
    private Object tag;

    private OkResponse response;

    private Request request;

    private Call call;

    private Exception exception;

    public OkUpload(String url) {
        this.url = url;
    }

    public OkUpload(String url, OkHttpClient client) {
        this.url = url;
        this.client = client;
    }

    public OkUpload client(OkHttpClient client) {
        this.client = client;
        return this;
    }

    /**
     * 格式化url的参数 {}
     *
     * @param o 参数
     * @return this
     */
    public OkUpload format(Object... o) {
        this.url = Strings.format(this.url, o);
        return this;
    }

    /**
     * 格式化url的参数 ${?}
     *
     * @param map 参数
     * @return this
     */
    public OkUpload format(Map<String, Object> map) {
        this.url = Strings.format(this.url, map);
        return this;
    }

    /**
     * 添加url参数
     *
     * @param queryParams 参数
     * @return this
     */
    public OkUpload queryParams(Map<String, String> queryParams) {
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
    public OkUpload queryParam(String key, String value) {
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
    public OkUpload formParts(Map<String, String> formParts) {
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
    public OkUpload formPart(String key, String value) {
        if (this.formParts == null) {
            this.formParts = new LinkedHashMap<>();
        }
        this.formParts.put(key, value);
        return this;
    }

    /**
     * queryStringEncode
     *
     * @return this
     */
    public OkUpload queryStringEncode() {
        this.queryStringEncode = true;
        return this;
    }

    /**
     * queryStringEncode
     *
     * @param encode ignore
     * @return this
     */
    public OkUpload queryStringEncode(boolean encode) {
        this.queryStringEncode = encode;
        return this;
    }

    /**
     * headers
     *
     * @param headers headers
     * @return this
     */
    public OkUpload headers(Map<String, String> headers) {
        if (this.headers == null) {
            this.headers = headers;
        } else {
            this.headers.putAll(headers);
        }
        return this;
    }

    /**
     * header
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public OkUpload header(String key, String value) {
        if (this.headers == null) {
            this.headers = new LinkedHashMap<>();
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
    public OkUpload userAgent(String value) {
        if (this.headers == null) {
            this.headers = new LinkedHashMap<>();
        }
        this.headers.put("User-Agent", value);
        return this;
    }

    /**
     * cookie
     *
     * @param cookie cookie
     * @return this
     */
    public OkUpload cookie(HttpCookie cookie) {
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
    public OkUpload cookies(List<HttpCookie> cookies) {
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
    public OkUpload ignoreHeader(String ignoreHeader) {
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
    public OkUpload ignoreHeaders(String... ignoreHeaders) {
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
    public OkUpload ignoreHeaders(List<String> ignoreHeaders) {
        if (this.ignoreHeaders == null) {
            this.ignoreHeaders = ignoreHeaders;
        } else {
            this.ignoreHeaders.addAll(ignoreHeaders);
        }
        return this;
    }

    /**
     * tag
     *
     * @param tag tag
     * @return this
     */
    public OkUpload tag(Object tag) {
        this.tag = tag;
        return this;
    }

    /**
     * 取消请求
     *
     * @return this
     */
    public OkUpload cancel() {
        this.call.cancel();
        return this;
    }

    /**
     * 上传单文件
     *
     * @param part ignore
     * @return this
     */
    public OkUpload upload(HttpUploadPart part) {
        return upload(Lists.of(part));
    }

    /**
     * 上传多文件
     *
     * @param parts ignore
     * @return this
     */
    public OkUpload upload(List<HttpUploadPart> parts) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if (this.formParts != null) {
            formParts.forEach(builder::addFormDataPart);
        }
        if (this.queryParams != null) {
            this.queryString = Urls.buildQueryString(this.queryParams, this.queryStringEncode);
            this.url += ("?" + this.queryString);
        }
        for (HttpUploadPart part : parts) {
            if (part == null) {
                continue;
            }
            RequestBody body = null;
            if (part.getFile() != null) {
                body = RequestBody.create(part.getContentType() == null ? null : MediaType.parse(part.getContentType()), part.getFile());
            } else if (part.getBytes() != null) {
                body = RequestBody.create(part.getContentType() == null ? null : MediaType.parse(part.getContentType()), part.getBytes(), part.getOff(), part.getLen());
            }
            if (body != null) {
                builder.addFormDataPart(part.getKey(), part.getFileName(), body);
            }
        }
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(builder.build());
        if (this.tag != null) {
            requestBuilder.tag(this.tag);
        }
        if (this.headers != null) {
            this.headers.forEach(requestBuilder::addHeader);
        }
        if (this.cookies != null) {
            this.cookies.forEach(c -> requestBuilder.addHeader("Cookie", c.toString()));
        }
        if (this.ignoreHeaders != null) {
            this.ignoreHeaders.forEach(requestBuilder::removeHeader);
        }
        this.request = requestBuilder.build();
        try {
            this.startDate = System.currentTimeMillis();
            this.call = this.client.newCall(this.request);
            this.response = new OkResponse(this.request, this.call.execute()).call(this.call);
        } catch (IOException e) {
            this.exception = e;
            this.response = new OkResponse(this.request, e).call(this.call);
        } finally {
            this.endDate = System.currentTimeMillis();
            this.done = true;
        }
        return this;
    }

    public String getUrl() {
        return url;
    }

    public OkHttpClient getClient() {
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
        return this.response.isOk();
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

    public Map<String, String> getHeaders() {
        return headers;
    }

    public List<String> getIgnoreHeaders() {
        return ignoreHeaders;
    }

    public Object getTag() {
        return tag;
    }

    public Request getRequest() {
        return request;
    }

    public Call getCall() {
        return call;
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

    public OkResponse getResponse() {
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
