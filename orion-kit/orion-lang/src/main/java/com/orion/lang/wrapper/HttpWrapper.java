package com.orion.lang.wrapper;

import com.orion.able.JsonAble;
import com.orion.able.Logable;
import com.orion.able.Mapable;
import com.orion.lang.support.CloneSupport;
import com.orion.utils.Objects1;
import com.orion.utils.Strings;
import com.orion.utils.json.Jsons;

import java.util.HashMap;
import java.util.Map;

/**
 * restful结果封装
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/5/29 11:06
 */
public class HttpWrapper<T> extends CloneSupport<HttpWrapper<T>> implements Wrapper<T>, JsonAble, Logable, Mapable<String, Object> {

    private static final long serialVersionUID = 7048691672612601L;

    /**
     * 状态码
     */
    private int code;

    /**
     * 信息对象
     */
    private String msg;

    /**
     * 结果对象
     */
    private T data;

    public HttpWrapper() {
        this(HTTP_OK_CODE, HTTP_OK_MESSAGE, null);
    }

    public HttpWrapper(int code) {
        this(code, HTTP_OK_MESSAGE, null);
    }

    public HttpWrapper(int code, String msg) {
        this(code, msg, null);
    }

    public HttpWrapper(int code, T data) {
        this(code, HTTP_OK_MESSAGE, data);
    }

    public HttpWrapper(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 初始化
     */
    public static <T> HttpWrapper<T> get() {
        return new HttpWrapper<>();
    }

    /**
     * 定义
     */
    public static <T> HttpWrapper<T> wrap(int code) {
        return new HttpWrapper<>(code);
    }

    public static <T> HttpWrapper<T> wrap(int code, String msg) {
        return new HttpWrapper<>(code, msg);
    }

    public static <T> HttpWrapper<T> wrap(int code, String msg, Object... args) {
        return new HttpWrapper<>(code, Strings.format(msg, args));
    }

    public static <T> HttpWrapper<T> wrap(int code, String msg, T data) {
        return new HttpWrapper<>(code, msg, data);
    }

    public static <T> HttpWrapper<T> wrap(int code, T data) {
        return new HttpWrapper<>(code, data);
    }

    public static <T> HttpWrapper<T> wrap(T data, int code, String msg, Object... args) {
        return new HttpWrapper<>(code, Strings.format(msg, args), data);
    }

    /**
     * 成功
     */
    public static <T> HttpWrapper<T> ok() {
        return new HttpWrapper<>(HTTP_OK_CODE, HTTP_OK_MESSAGE);
    }

    public static <T> HttpWrapper<T> ok(String msg) {
        return new HttpWrapper<>(HTTP_OK_CODE, msg);
    }

    public static <T> HttpWrapper<T> ok(String msg, Object... args) {
        return new HttpWrapper<>(HTTP_OK_CODE, Strings.format(msg, args));
    }

    public static <T> HttpWrapper<T> ok(T data) {
        return new HttpWrapper<>(HTTP_OK_CODE, HTTP_OK_MESSAGE, data);
    }

    public static <T> HttpWrapper<T> ok(String msg, T data) {
        return new HttpWrapper<>(HTTP_OK_CODE, msg, data);
    }

    public static <T> HttpWrapper<T> ok(T data, String msg, Object... args) {
        return new HttpWrapper<>(HTTP_OK_CODE, Strings.format(msg, args), data);
    }

    /**
     * 失败
     */
    public static <T> HttpWrapper<T> error() {
        return new HttpWrapper<>(HTTP_ERROR_CODE, HTTP_ERROR_MESSAGE);
    }

    public static <T> HttpWrapper<T> error(String msg) {
        return new HttpWrapper<>(HTTP_ERROR_CODE, msg);
    }

    public static <T> HttpWrapper<T> error(String msg, Object... args) {
        return new HttpWrapper<>(HTTP_ERROR_CODE, Strings.format(msg, args));
    }

    public static <T> HttpWrapper<T> error(T data) {
        return new HttpWrapper<>(HTTP_ERROR_CODE, HTTP_ERROR_MESSAGE, data);
    }

    public static <T> HttpWrapper<T> error(String msg, T data) {
        return new HttpWrapper<>(HTTP_ERROR_CODE, msg, data);
    }

    public static <T> HttpWrapper<T> error(T data, String msg, Object... args) {
        return new HttpWrapper<>(HTTP_ERROR_CODE, Strings.format(msg, args), data);
    }

    public static <T> HttpWrapper<T> error(Throwable t) {
        return new HttpWrapper<>(HTTP_ERROR_CODE, t.getMessage());
    }

    public static <T> HttpWrapper<T> error(Throwable t, T data) {
        return new HttpWrapper<>(HTTP_ERROR_CODE, t.getMessage(), data);
    }

    public HttpWrapper<T> code(int code) {
        this.code = code;
        return this;
    }

    public HttpWrapper<T> msg(String msg) {
        this.msg = msg;
        return this;
    }

    public HttpWrapper<T> data(T data) {
        this.data = data;
        return this;
    }

    public int getCode() {
        return code;
    }

    public HttpWrapper<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public HttpWrapper<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public HttpWrapper<T> setData(T data) {
        this.data = data;
        return this;
    }

    /**
     * 是否为成功状态码
     *
     * @return 是否成功
     */
    public boolean isOk() {
        return this.code == HTTP_OK_CODE;
    }

    @Override
    public String toString() {
        return toLogString();
    }

    @Override
    public String toJsonString() {
        return Jsons.toJsonWriteNull(this);
    }

    @Override
    public String toLogString() {
        return "HttpWrapper:\n\tcode ==> " + code + "\n\t" +
                "msg ==> " + msg + "\n\t" +
                "data ==> " + Objects1.toString(data);
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>(6);
        map.put("code", code);
        map.put("msg", msg);
        map.put("data", data);
        return map;
    }

}
