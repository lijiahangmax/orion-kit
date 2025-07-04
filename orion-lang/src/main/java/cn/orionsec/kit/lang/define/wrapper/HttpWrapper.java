/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.lang.define.wrapper;

import cn.orionsec.kit.lang.KitLangConfiguration;
import cn.orionsec.kit.lang.able.ILogObject;
import cn.orionsec.kit.lang.able.IMapObject;
import cn.orionsec.kit.lang.config.KitConfig;
import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.define.support.CloneSupport;
import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.Strings;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * restful 结果封装
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/5/29 11:06
 */
public class HttpWrapper<T> extends CloneSupport<HttpWrapper<T>> implements Wrapper<T>, ILogObject, IMapObject<String, Object> {

    private static final long serialVersionUID = 7048691672612601L;

    // -------------------- HTTP --------------------

    public static final Integer HTTP_OK_CODE = KitConfig.get(KitLangConfiguration.CONFIG.HTTP_OK_CODE);

    public static final String HTTP_OK_MESSAGE = KitConfig.get(KitLangConfiguration.CONFIG.HTTP_OK_MESSAGE);

    public static final Integer HTTP_ERROR_CODE = KitConfig.get(KitLangConfiguration.CONFIG.HTTP_ERROR_CODE);

    public static final String HTTP_ERROR_MESSAGE = KitConfig.get(KitLangConfiguration.CONFIG.HTTP_ERROR_MESSAGE);

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
     * 获取空 HttpWrapper
     *
     * @param <T> T
     * @return HttpWrapper
     */
    public static <T> HttpWrapper<T> get() {
        return new HttpWrapper<>();
    }

    /**
     * 定义 HttpWrapper
     *
     * @param <T>  T
     * @param info info
     * @return HttpWrapper
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

    public static <T> HttpWrapper<T> of(int code, String msg, T data) {
        return new HttpWrapper<>(code, msg, data);
    }

    /**
     * 成功
     *
     * @param <T> T
     * @return HttpWrapper
     */
    public static <T> HttpWrapper<T> ok() {
        return new HttpWrapper<>(HTTP_OK_CODE, HTTP_OK_MESSAGE);
    }

    public static <T> HttpWrapper<T> ok(T data) {
        return new HttpWrapper<>(HTTP_OK_CODE, HTTP_OK_MESSAGE, data);
    }

    /**
     * 失败
     *
     * @param <T> T
     * @return HttpWrapper
     */
    public static <T> HttpWrapper<T> error() {
        return new HttpWrapper<>(HTTP_ERROR_CODE, HTTP_ERROR_MESSAGE);
    }

    public static <T> HttpWrapper<T> error(String msg) {
        return new HttpWrapper<>(HTTP_ERROR_CODE, msg);
    }

    public static <T> HttpWrapper<T> error(String msg, Object... params) {
        return new HttpWrapper<>(HTTP_ERROR_CODE, Strings.format(msg, params));
    }

    public static <T> HttpWrapper<T> error(Throwable t) {
        if (t == null) {
            return new HttpWrapper<>(HTTP_ERROR_CODE, HTTP_ERROR_MESSAGE);
        } else {
            return new HttpWrapper<>(HTTP_ERROR_CODE, t.getMessage());
        }
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
    @JsonIgnore
    public boolean isOk() {
        return HTTP_OK_CODE.equals(code);
    }

    @Override
    public String toString() {
        return this.toJsonString();
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
     * 映射
     *
     * @param mapping mapping
     * @param <E>     E
     * @return mapped
     */
    public <E> HttpWrapper<E> map(Function<T, E> mapping) {
        HttpWrapper<E> result = new HttpWrapper<>();
        result.code = this.code;
        result.msg = this.msg;
        result.data = Objects1.map(this.data, mapping);
        return result;
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
