package com.orion.http.ok;

import com.orion.able.SafeCloseable;
import com.orion.http.support.HttpCookie;
import com.orion.lang.mutable.MutableString;
import com.orion.utils.Arrays1;
import com.orion.utils.Exceptions;
import com.orion.utils.io.Streams;
import okhttp3.Call;
import okhttp3.Request;
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
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/7 23:52
 */
public class OkResponse implements Serializable, SafeCloseable {

    /**
     * 状态码
     */
    private int code;

    /**
     * message
     */
    private String message;

    /**
     * 响应体
     */
    private byte[] body;

    /**
     * url
     */
    private String url;

    /**
     * method
     */
    private String method;

    /**
     * tag
     */
    private Object tag;

    /**
     * protocol
     */
    private String protocol;

    /**
     * headers
     */
    private Map<String, List<String>> headers;

    /**
     * call
     */
    private Call call;

    /**
     * response
     */
    private Request request;

    /**
     * response
     */
    private Response response;

    /**
     * exception
     */
    private Exception exception;

    /**
     * OkHttp Request
     */
    private OkRequest okRequest;

    /**
     * 是否已经执行完毕
     */
    private boolean done = true;

    /**
     * 用于异步
     */
    OkResponse() {
        done = false;
    }

    public OkResponse(Request request, Response response) {
        this(request, response, null);
    }

    public OkResponse(Request request, Exception exception) {
        this(request, null, exception);
    }

    public OkResponse(Request request, Response response, Exception exception) {
        this.request = request;
        this.response = response;
        this.exception = exception;
        if (this.exception == null) {
            if (this.request != null) {
                this.url = this.request.url().toString();
                this.tag = this.request.tag();
                this.method = this.request.method();
            }
            if (this.response != null) {
                this.code = this.response.code();
                this.message = this.response.message();
                this.headers = this.response.headers().toMultimap();
                this.protocol = this.response.protocol().toString();
                try {
                    ResponseBody b;
                    if ((b = this.response.body()) != null) {
                        this.body = b.bytes();
                    }
                } catch (IOException e) {
                    Exceptions.printStacks(e);
                }
            }
        }
    }

    /**
     * 异步请求是否执行完毕
     *
     * @return true完毕
     */
    public boolean isDone() {
        return this.done;
    }

    /**
     * 是否执行失败
     *
     * @return true失败
     */
    public boolean isError() {
        return this.exception != null;
    }

    /**
     * 请求是否成功
     *
     * @return true成功
     */
    public boolean isOk() {
        return this.code >= 200 && this.code < 400;
    }

    /**
     * call
     *
     * @param call call
     * @return this
     */
    public OkResponse call(Call call) {
        this.call = call;
        return this;
    }

    /**
     * done
     *
     * @return this
     */
    protected OkResponse done() {
        this.done = true;
        if (this.exception == null) {
            if (this.request != null) {
                this.url = this.request.url().toString();
                this.tag = this.request.tag();
                this.method = this.request.method();
            }
            if (this.response != null) {
                this.code = this.response.code();
                this.message = this.response.message();
                this.headers = this.response.headers().toMultimap();
                this.protocol = this.response.protocol().toString();
                try {
                    ResponseBody b;
                    if ((b = this.response.body()) != null) {
                        this.body = b.bytes();
                    }
                } catch (IOException e) {
                    Exceptions.printStacks(e);
                }
            }
        }
        return this;
    }

    /**
     * request
     *
     * @param request request
     * @return this
     */
    public OkResponse request(Request request) {
        this.request = request;
        return this;
    }

    /**
     * response
     *
     * @param response response
     * @return this
     */
    public OkResponse response(Response response) {
        this.response = response;
        return this;
    }

    /**
     * exception
     *
     * @param exception exception
     * @return this
     */
    public OkResponse exception(Exception exception) {
        this.exception = exception;
        return this;
    }

    /**
     * OkHttp Request
     *
     * @param okRequest OkHttp Request
     * @return this
     */
    public OkResponse okRequest(OkRequest okRequest) {
        this.okRequest = okRequest;
        return this;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public byte[] getBody() {
        return body;
    }

    public String getBodyString() {
        if (body != null) {
            return new String(body);
        }
        return null;
    }

    public Call getCall() {
        return call;
    }

    public Request getRequest() {
        return request;
    }

    public OkRequest getOkRequest() {
        return okRequest;
    }

    public Response getResponse() {
        return response;
    }

    public Exception getException() {
        return exception;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public Object getTag() {
        return tag;
    }

    public String getProtocol() {
        return protocol;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public List<String> getHeaders(String key) {
        return response.headers(key);
    }

    public MutableString getHeader(String key) {
        return new MutableString(response.header(key));
    }

    public MutableString getHeader(String key, String def) {
        return new MutableString(response.header(key, def));
    }

    public List<HttpCookie> getCookies() {
        List<HttpCookie> list = new ArrayList<>();
        for (String value : response.headers().values(HttpCookie.SET_COOKIE)) {
            list.add(new HttpCookie(value));
        }
        return list;
    }

    @Override
    public void close() {
        Streams.close(this.response);
    }

    @Override
    public String toString() {
        return "OkHttpResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", bodyLength=" + Arrays1.length(body) +
                ", url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", tag=" + tag +
                ", protocol='" + protocol + '\'' +
                ", headers=" + headers +
                ", exception=" + exception +
                ", done=" + done +
                '}';
    }
}
