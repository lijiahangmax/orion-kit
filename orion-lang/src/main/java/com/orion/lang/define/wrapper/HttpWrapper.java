package com.orion.lang.define.wrapper;

import com.alibaba.fastjson.annotation.JSONField;
import com.orion.lang.able.ILogObject;
import com.orion.lang.able.IMapObject;
import com.orion.lang.constant.Const;
import com.orion.lang.define.support.CloneSupport;
import com.orion.lang.utils.Objects1;
import com.orion.lang.utils.Strings;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * restful 结果封装
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/5/29 11:06
 */
public class HttpWrapper<T> extends CloneSupport<HttpWrapper<T>> implements Wrapper<T>, ILogObject, IMapObject<String, Object> {

    private static final long serialVersionUID = 7048691672612601L;

    /**
     * 状态码
     */
    @JSONField(ordinal = 0)
    private int code;

    /**
     * 信息对象
     */
    @JSONField(ordinal = 1)
    private String msg;

    /**
     * 结果对象
     */
    @JSONField(ordinal = 2)
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

    public HttpWrapper(CodeInfo info) {
        this(info.code(), info.message(), null);
    }

    public HttpWrapper(CodeInfo info, T data) {
        this(info.code(), info.message(), data);
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
    public static <T> HttpWrapper<T> of(CodeInfo info) {
        return new HttpWrapper<>(info.code(), info.message());
    }

    public static <T> HttpWrapper<T> of(CodeInfo info, T data) {
        return new HttpWrapper<>(info.code(), info.message(), data);
    }

    public static <T> HttpWrapper<T> of(int code, String msg) {
        return new HttpWrapper<>(code, msg);
    }

    public static <T> HttpWrapper<T> of(int code, String msg, Object... args) {
        return new HttpWrapper<>(code, Strings.format(msg, args));
    }

    public static <T> HttpWrapper<T> of(int code, String msg, T data) {
        return new HttpWrapper<>(code, msg, data);
    }

    public static <T> HttpWrapper<T> of(int code, T data) {
        return new HttpWrapper<>(code, data);
    }

    public static <T> HttpWrapper<T> of(T data, int code, String msg, Object... args) {
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

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    /**
     * 是否为成功状态码
     *
     * @return 是否成功
     */
    @JSONField(serialize = false)
    public boolean isOk() {
        return HTTP_OK_CODE.equals(code);
    }

    @Override
    public String toString() {
        return this.toLogString();
    }

    @Override
    public String toLogString() {
        return "HttpWrapper:" +
                "\n  code ==> " + code +
                "\n   msg ==> " + msg +
                "\n  data ==> " + Objects1.toString(data);
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>(Const.CAPACITY_8);
        map.put("code", code);
        map.put("msg", msg);
        map.put("data", data);
        return map;
    }

    /**
     * @return {@link RpcWrapper}
     */
    public RpcWrapper<T> toRpcWrapper() {
        return new RpcWrapper<>(code, msg, data);
    }

    /**
     * @return result 的 Optional
     */
    public Optional<T> optional() {
        return Optional.of(this)
                .filter(HttpWrapper::isOk)
                .map(HttpWrapper::getData);
    }

}
