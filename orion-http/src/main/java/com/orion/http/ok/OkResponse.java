package com.orion.http.ok;

import com.orion.http.BaseHttpResponse;
import com.orion.http.support.HttpCookie;
import com.orion.lang.define.mutable.MutableString;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.Valid;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * OkHttp Response
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/7 23:52
 */
public class OkResponse extends BaseHttpResponse implements Serializable {

    /**
     * response
     */
    private Response response;

    /**
     * url
     */
    private final String url;

    /**
     * tag
     */
    private final Object tag;

    /**
     * 响应体
     * <p>
     * 如果是 {@link com.orion.http.ok.file.OkAsyncDownload} 则不能获取body
     */
    private byte[] body;

    /**
     * 用于异步 exception
     */
    private Exception exception;

    /**
     * 用于异步 是否已经执行完毕
     */
    private volatile boolean done;

    public OkResponse(String url, Object tag) {
        this.done = false;
        this.url = url;
        this.tag = tag;
    }

    public OkResponse(String url, Object tag, Response response) {
        this.done = true;
        this.url = url;
        this.tag = tag;
        this.response = response;
        this.setBody();
    }

    public void asyncSetResponse(Response response) {
        this.asyncSetResponse(response, true);
    }

    public void asyncSetResponse(Response response, boolean setBody) {
        this.done = true;
        this.response = response;
        if (setBody) {
            this.setBody();
        }
    }

    public void error(Exception exception) {
        this.done = true;
        this.exception = exception;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public int getCode() {
        this.validDone();
        return response.code();
    }

    public String getMessage() {
        this.validDone();
        return response.message();
    }

    @Override
    public byte[] getBody() {
        this.validDone();
        return body;
    }

    @Override
    public String getBodyString() {
        this.validDone();
        return new String(body);
    }

    public Response getResponse() {
        return response;
    }

    public Exception getException() {
        return exception;
    }

    public Object getTag() {
        return tag;
    }

    public Map<String, List<String>> getHeaders() {
        this.validDone();
        return this.response.headers().toMultimap();
    }

    public List<String> getHeaders(String key) {
        this.validDone();
        return response.headers(key);
    }

    public MutableString getHeader(String key) {
        this.validDone();
        return new MutableString(response.header(key));
    }

    public MutableString getHeader(String key, String def) {
        this.validDone();
        return new MutableString(response.header(key, def));
    }

    public List<HttpCookie> getCookies() {
        List<HttpCookie> list = new ArrayList<>();
        for (String value : response.headers().values(HttpCookie.SET_COOKIE)) {
            list.add(new HttpCookie(value));
        }
        return list;
    }

    /**
     * @return 是否执行完毕
     */
    public boolean isDone() {
        return this.done;
    }

    /**
     * @return 是否执行失败
     */
    public boolean isError() {
        return this.exception != null;
    }

    @Override
    public boolean isOk() {
        this.validDone();
        return super.isOk();
    }

    /**
     * 检查请求是否完毕
     */
    private void validDone() {
        Valid.isTrue(done, "ok request is not done");
    }

    /**
     * 设置body
     */
    private void setBody() {
        try {
            ResponseBody b;
            if ((b = this.response.body()) != null) {
                this.body = b.bytes();
            }
        } catch (IOException e) {
            throw Exceptions.ioRuntime("ok response get body error", e);
        }
    }

    @Override
    public String toString() {
        return this.getCode() + Strings.SPACE + Strings.def(this.getMessage());
    }

}
