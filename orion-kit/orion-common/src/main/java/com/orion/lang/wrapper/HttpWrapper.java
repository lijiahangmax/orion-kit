package com.orion.lang.wrapper;

import com.orion.able.Jsonable;
import com.orion.able.Logable;
import com.orion.able.Mapable;
import com.orion.utils.json.Jsons;
import com.orion.utils.Objects1;
import com.orion.utils.Strings;

import java.util.HashMap;
import java.util.Map;

/**
 * restful结果封装
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/5/29 11:06
 */
public class HttpWrapper<T> implements Wrapper<T>, Jsonable, Logable, Mapable {

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

    private HttpWrapper() {
    }

    private HttpWrapper(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private HttpWrapper(int code, String msg, T data) {
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
        return new HttpWrapper<>(code, null);
    }

    public static <T> HttpWrapper<T> wrap(int code, String msg) {
        return new HttpWrapper<>(code, msg);
    }

    public static <T> HttpWrapper<T> wrap(int code, String tpl, Object... args) {
        return new HttpWrapper<>(code, Strings.format(tpl, args));
    }

    public static <T> HttpWrapper<T> wrap(int code, String msg, T data) {
        return new HttpWrapper<>(code, msg, data);
    }

    public static <T> HttpWrapper<T> wrap(T data, int code, String tpl, Object... args) {
        return new HttpWrapper<>(code, Strings.format(tpl, args), data);
    }

    public static <T> HttpWrapper<T> wrap(HttpStatus status) {
        return new HttpWrapper<>(status.getCode(), status.getMsg());
    }

    public static <T> HttpWrapper<T> wrap(HttpStatus status, T data) {
        return new HttpWrapper<>(status.getCode(), status.getMsg(), data);
    }

    public static <T> HttpWrapper<T> wrap(HttpStatus status, Object... args) {
        return new HttpWrapper<>(status.getCode(), Strings.format(status.getMsg(), args));
    }

    public static <T> HttpWrapper<T> wrap(T data, HttpStatus status, Object... args) {
        return new HttpWrapper<>(status.getCode(), Strings.format(status.getMsg(), args), data);
    }

    /**
     * 成功
     */
    public static <T> HttpWrapper<T> ok() {
        return new HttpWrapper<>(HttpStatus.OK.getCode(), HttpStatus.OK.getMsg());
    }

    public static <T> HttpWrapper<T> ok(String msg) {
        return new HttpWrapper<>(HttpStatus.OK.getCode(), msg);
    }

    public static <T> HttpWrapper<T> ok(String tpl, Object... args) {
        return new HttpWrapper<>(HttpStatus.OK.getCode(), Strings.format(tpl, args));
    }

    public static <T> HttpWrapper<T> ok(T data) {
        return new HttpWrapper<>(HttpStatus.OK.getCode(), HttpStatus.OK.getMsg(), data);
    }

    public static <T> HttpWrapper<T> ok(String msg, T data) {
        return new HttpWrapper<>(HttpStatus.OK.getCode(), msg, data);
    }

    public static <T> HttpWrapper<T> ok(T data, String tpl, Object... args) {
        return new HttpWrapper<>(HttpStatus.OK.getCode(), Strings.format(tpl, args), data);
    }

    /**
     * 失败
     */
    public static <T> HttpWrapper<T> error() {
        return new HttpWrapper<>(HttpStatus.ERROR.getCode(), HttpStatus.ERROR.getMsg());
    }

    public static <T> HttpWrapper<T> error(String msg) {
        return new HttpWrapper<>(HttpStatus.ERROR.getCode(), msg);
    }

    public static <T> HttpWrapper<T> error(String tpl, Object... args) {
        return new HttpWrapper<>(HttpStatus.ERROR.getCode(), Strings.format(tpl, args));
    }

    public static <T> HttpWrapper<T> error(T data) {
        return new HttpWrapper<>(HttpStatus.ERROR.getCode(), HttpStatus.ERROR.getMsg(), data);
    }

    public static <T> HttpWrapper<T> error(String msg, T data) {
        return new HttpWrapper<>(HttpStatus.ERROR.getCode(), msg, data);
    }

    public static <T> HttpWrapper<T> error(T data, String tpl, Object... args) {
        return new HttpWrapper<>(HttpStatus.ERROR.getCode(), Strings.format(tpl, args), data);
    }

    /**
     * url
     */
    public static <T> HttpWrapper<UrlWrapper<T>> url() {
        return new HttpWrapper<UrlWrapper<T>>().setData(UrlWrapper.get());
    }

    public static <T> HttpWrapper<UrlWrapper<T>> url(int code) {
        return new HttpWrapper<UrlWrapper<T>>().setCode(code).setData(UrlWrapper.get());
    }

    public static <T> HttpWrapper<UrlWrapper<T>> url(int code, String msg) {
        return new HttpWrapper<>(code, msg, UrlWrapper.get());
    }

    public static <T> HttpWrapper<UrlWrapper<T>> url(int code, String tpl, Object... args) {
        return new HttpWrapper<>(code, Strings.format(tpl, args), UrlWrapper.get());
    }

    public static <T> HttpWrapper<UrlWrapper<T>> url(int code, String msg, T data) {
        return new HttpWrapper<>(code, msg, UrlWrapper.get(data));
    }

    public static <T> HttpWrapper<UrlWrapper<T>> url(int code, T data, String tpl, Object... args) {
        return new HttpWrapper<>(code, Strings.format(tpl, args), UrlWrapper.get(data));
    }

    /**
     * refresh
     */
    public static <T> HttpWrapper<UrlWrapper<T>> refresh() {
        return new HttpWrapper<UrlWrapper<T>>().setData(UrlWrapper.refresh());
    }

    public static <T> HttpWrapper<UrlWrapper<T>> refresh(int code) {
        return new HttpWrapper<UrlWrapper<T>>().setCode(code).setData(UrlWrapper.refresh());
    }

    public static <T> HttpWrapper<UrlWrapper<T>> refresh(int code, String msg) {
        return new HttpWrapper<>(code, msg, UrlWrapper.refresh());
    }

    public static <T> HttpWrapper<UrlWrapper<T>> refresh(int code, String tpl, Object... args) {
        return new HttpWrapper<>(code, Strings.format(tpl, args), UrlWrapper.refresh());
    }

    public static <T> HttpWrapper<UrlWrapper<T>> refresh(int code, String msg, T data) {
        return new HttpWrapper<>(code, msg, UrlWrapper.refresh(data));
    }

    public static <T> HttpWrapper<UrlWrapper<T>> refresh(int code, T data, String tpl, Object... args) {
        return new HttpWrapper<>(code, Strings.format(tpl, args), UrlWrapper.refresh(data));
    }

    /**
     * redirect
     */
    public static <T> HttpWrapper<UrlWrapper<T>> redirect() {
        return new HttpWrapper<UrlWrapper<T>>().setData(UrlWrapper.redirect());
    }

    public static <T> HttpWrapper<UrlWrapper<T>> redirect(String url) {
        return new HttpWrapper<UrlWrapper<T>>().setData(UrlWrapper.redirect(url));
    }

    public static <T> HttpWrapper<UrlWrapper<T>> redirect(String url, int code) {
        return new HttpWrapper<UrlWrapper<T>>().setCode(code).setData(UrlWrapper.redirect(url));
    }

    public static <T> HttpWrapper<UrlWrapper<T>> redirect(String url, int code, String msg) {
        return new HttpWrapper<>(code, msg, UrlWrapper.redirect(url));
    }

    public static <T> HttpWrapper<UrlWrapper<T>> redirect(String url, int code, String tpl, Object... args) {
        return new HttpWrapper<>(code, Strings.format(tpl, args), UrlWrapper.redirect(url));
    }

    public static <T> HttpWrapper<UrlWrapper<T>> redirect(String url, int code, String msg, T data) {
        return new HttpWrapper<>(code, msg, UrlWrapper.redirect(url, data));
    }

    public static <T> HttpWrapper<UrlWrapper<T>> redirect(String url, int code, T data, String tpl, Object... args) {
        return new HttpWrapper<>(code, Strings.format(tpl, args), UrlWrapper.redirect(url, data));
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

    @Override
    public String toString() {
        return "HttpWrapper{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + Objects1.toString(data) +
                '}';
    }

    @Override
    public String toJsonString() {
        return Jsons.toJSONWriteNull(this);
    }

    @Override
    public String toLogString() {
        return "HttpWrapper:\n\tcode ==> " + code + "\n\t" +
                "msg ==> " + msg + "\n\t" +
                "data ==> " + data;
    }

    @Override
    public Map toMap() {
        Map<String, Object> map = new HashMap<>(6);
        map.put("code", code);
        map.put("msg", msg);
        map.put("data", data);
        return map;
    }

}
