package com.orion.http.ok;

import com.orion.constant.StandardHttpHeader;
import com.orion.http.BaseHttpRequest;
import com.orion.http.support.HttpMethod;
import com.orion.http.support.HttpUploadPart;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.io.Streams;
import okhttp3.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * OkHttp 请求基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/8 11:38
 */
public abstract class BaseOkRequest extends BaseHttpRequest {

    /**
     * tag
     */
    protected Object tag;

    /**
     * call
     */
    protected Call call;

    /**
     * 请求
     */
    protected Request request;

    /**
     * client
     */
    protected OkHttpClient client;

    /**
     * OkHttp Response
     */
    protected OkResponse response;

    public BaseOkRequest client(OkHttpClient client) {
        this.client = client;
        return this;
    }

    public BaseOkRequest tag(Object tag) {
        this.tag = tag;
        return this;
    }

    /**
     * ssl
     *
     * @param ssl ssl
     * @return this
     */
    public BaseOkRequest ssl(boolean ssl) {
        this.ssl = ssl;
        if (this.ssl) {
            this.client = OkClient.getSslClient();
        }
        return this;
    }

    /**
     * ssl
     *
     * @return this
     */
    public BaseOkRequest ssl() {
        this.ssl = true;
        return this;
    }

    /**
     * 取消请求
     *
     * @return this
     */
    public BaseOkRequest cancel() {
        this.call.cancel();
        return this;
    }

    @Override
    protected void buildRequest() {
        super.buildRequest();
        Request.Builder requestBuilder = new Request.Builder();
        HttpMethod.valid(method, 6);
        requestBuilder.url(url);
        if (headers != null) {
            headers.forEach(requestBuilder::addHeader);
        }
        if (cookies != null) {
            cookies.forEach(c -> requestBuilder.addHeader(StandardHttpHeader.COOKIE, c.toString()));
        }
        if (ignoreHeaders != null) {
            ignoreHeaders.forEach(requestBuilder::removeHeader);
        }
        if (tag != null) {
            requestBuilder.tag(tag);
        }
        boolean noBody = super.isNoBodyRequest();
        if (noBody) {
            requestBuilder.method(method, null);
        } else {
            this.setBody(requestBuilder);
        }
        request = requestBuilder.build();
    }

    /**
     * 设置body
     *
     * @param requestBuilder builder
     */
    protected void setBody(Request.Builder requestBuilder) {
        if (body != null) {
            requestBuilder.method(method, RequestBody.create(MediaType.parse(contentType + "; charset=" + charset), body, bodyOffset, bodyLen));
        } else if (formParts != null) {
            FormBody.Builder formBuilder = new FormBody.Builder(Charset.forName(charset));
            formParts.forEach(formBuilder::addEncoded);
            requestBuilder.method(method, formBuilder.build());
        } else {
            requestBuilder.method(method, RequestBody.create(MediaType.parse(contentType + "; charset=" + charset), Strings.EMPTY));
        }
    }

    /**
     * 设置二进制body
     *
     * @param requestBuilder builder
     * @param parts          parts
     */
    protected void setMultipartBody(Request.Builder requestBuilder, Collection<HttpUploadPart> parts) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (this.formParts != null) {
            formParts.forEach(builder::addFormDataPart);
        }
        for (HttpUploadPart part : parts) {
            if (part == null) {
                continue;
            }
            RequestBody body = null;
            if (part.getIn() != null) {
                try {
                    body = RequestBody.create(MediaType.parse(part.getContentType()), Streams.toByteArray(part.getIn()));
                } catch (IOException e) {
                    throw Exceptions.ioRuntime("set upload file error", e);
                }
            } else if (part.getFile() != null) {
                body = RequestBody.create(MediaType.parse(part.getContentType()), part.getFile());
            } else if (part.getBytes() != null) {
                body = RequestBody.create(MediaType.parse(part.getContentType()), part.getBytes(), part.getOff(), part.getLen());
            }
            if (body != null) {
                builder.addFormDataPart(part.getParam(), part.getFileName(), body);
            }
        }
        requestBuilder.method(method, builder.build());
    }

    /**
     * @return 获取请求信息
     */
    protected String getRequestMessage() {
        StringBuilder builder = new StringBuilder()
                .append(request.method())
                .append(Strings.SPACE)
                .append(request.url());
        Object tag = request.tag();
        if (tag != null) {
            builder.append(" tag: ")
                    .append(tag);
        }
        return builder.toString();
    }

    public Request getRequest() {
        return request;
    }

    public Call getCall() {
        return call;
    }

    public Object getTag() {
        return tag;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public OkResponse getResponse() {
        return response;
    }

}
