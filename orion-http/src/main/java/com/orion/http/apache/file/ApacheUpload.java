package com.orion.http.apache.file;

import com.orion.http.BaseHttpRequest;
import com.orion.http.apache.ApacheClient;
import com.orion.http.apache.ApacheResponse;
import com.orion.http.apache.BaseApacheRequest;
import com.orion.http.support.HttpContentType;
import com.orion.http.support.HttpMethod;
import com.orion.http.support.HttpUploadPart;
import com.orion.lang.constant.StandardContentType;
import com.orion.lang.utils.Charsets;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.collect.Lists;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * Apache Http 文件上传
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/6/13 1:03
 */
public class ApacheUpload extends BaseApacheRequest {

    /**
     * 文件体
     */
    private Collection<HttpUploadPart> parts;

    /**
     * 开始时间
     */
    private long startDate;

    /**
     * 结束时间
     */
    private long endDate;

    /**
     * 是否执行完毕
     */
    private volatile boolean done;

    public ApacheUpload(String url) {
        this(url, ApacheClient.getClient());
    }

    public ApacheUpload(String url, CloseableHttpClient client) {
        this.url = url;
        this.client = client;
        this.method = HttpMethod.POST.method();
    }

    @Override
    public ApacheUpload method(HttpMethod method) {
        return this.method(method.method());
    }

    @Override
    public ApacheUpload method(String method) {
        this.method = method;
        if (super.isNoBodyRequest()) {
            throw Exceptions.unsupported("unsupported method " + method);
        }
        return this;
    }

    @Override
    public BaseHttpRequest body(String body) {
        throw Exceptions.unsupported("upload file unsupported set body");
    }

    @Override
    public BaseHttpRequest body(byte[] body) {
        throw Exceptions.unsupported("upload file unsupported set body");
    }

    @Override
    public BaseHttpRequest body(byte[] body, int offset, int len) {
        throw Exceptions.unsupported("upload file unsupported set body");
    }

    @Override
    public BaseHttpRequest contentType(String contentType) {
        throw Exceptions.unsupported("upload file unsupported set contentType");
    }

    @Override
    public BaseHttpRequest contentType(HttpContentType contentType) {
        throw Exceptions.unsupported("upload file unsupported set contentType");
    }

    /**
     * 上传单文件
     *
     * @param part ignore
     */
    public ApacheUpload part(HttpUploadPart part) {
        this.parts = Lists.singleton(part);
        return this;
    }

    /**
     * 上传多文件
     *
     * @param parts ignore
     */
    public ApacheUpload parts(Collection<HttpUploadPart> parts) {
        this.parts = parts;
        return this;
    }

    @Override
    protected HttpEntity getEntry() {
        Valid.notEmpty(parts, "upload part is empty");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        if (formParts != null) {
            formParts.forEach((k, v) -> {
                builder.addTextBody(k, v, ContentType.create(StandardContentType.TEXT_PLAIN, Charsets.of(charset)));
            });
        }
        for (HttpUploadPart part : parts) {
            String key = part.getParam();
            String contentType = part.getContentType();
            File file = part.getFile();
            String fileName = part.getFileName();
            byte[] bytes = part.getBytes();
            InputStream in = part.getIn();
            if (in != null) {
                builder.addBinaryBody(key, in, ContentType.parse(contentType), fileName);
            } else if (file != null) {
                builder.addBinaryBody(key, file, ContentType.parse(contentType), fileName);
            } else if (bytes != null) {
                builder.addBinaryBody(key, bytes, ContentType.parse(contentType), fileName);
            }
        }
        return builder.build();
    }

    @Override
    protected void execute() {
        super.buildRequest();
        this.startDate = System.currentTimeMillis();
        try (CloseableHttpResponse resp = client.execute(request)) {
            this.response = new ApacheResponse(url, resp);
        } catch (IOException e) {
            throw Exceptions.httpRequest(e);
        } finally {
            this.endDate = System.currentTimeMillis();
            this.done = true;
            this.request.releaseConnection();
        }
    }

    @Override
    public ApacheResponse await() {
        this.execute();
        return response;
    }

    public boolean isDone() {
        return done;
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

}
