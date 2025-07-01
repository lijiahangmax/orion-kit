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
import cn.orionsec.kit.lang.id.UUIds;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.json.Jsons;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * rpc 远程调用结果封装
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/5/29 11:27
 */
public class RpcWrapper<T> extends CloneSupport<RpcWrapper<T>> implements Wrapper<T>, ILogObject, IMapObject<String, Object> {

    private static final long serialVersionUID = 7940497300629314L;

    public static final Integer RPC_SUCCESS_CODE = KitConfig.get(KitLangConfiguration.CONFIG.RPC_SUCCESS_CODE);

    public static final String RPC_SUCCESS_MESSAGE = KitConfig.get(KitLangConfiguration.CONFIG.RPC_SUCCESS_MESSAGE);

    public static final Integer RPC_ERROR_CODE = KitConfig.get(KitLangConfiguration.CONFIG.RPC_ERROR_CODE);

    public static final String RPC_ERROR_MESSAGE = KitConfig.get(KitLangConfiguration.CONFIG.RPC_ERROR_MESSAGE);

    public static final String PRC_TRACE_PREFIX = KitConfig.get(KitLangConfiguration.CONFIG.PRC_TRACE_PREFIX);

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
    @JSONField(ordinal = 4)
    private T data;

    /**
     * 错误信息
     */
    @JSONField(ordinal = 3)
    private List<String> errorMessages;

    /**
     * 会话追踪标识
     */
    @JSONField(ordinal = 2)
    private String traceId;

    public RpcWrapper() {
        this(RPC_SUCCESS_CODE, RPC_SUCCESS_MESSAGE, null);
    }

    public RpcWrapper(int code) {
        this(code, RPC_SUCCESS_MESSAGE, null);
    }

    public RpcWrapper(int code, String msg) {
        this(code, msg, null);
    }

    public RpcWrapper(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.traceId = this.createTrace();
    }

    public RpcWrapper(CodeInfo info) {
        this(info.code(), info.message(), null);
    }

    public RpcWrapper(CodeInfo info, T data) {
        this(info.code(), info.message(), data);
    }

    /**
     * 获取空 RpcWrapper
     *
     * @param <T> T
     * @return RpcWrapper
     */
    public static <T> RpcWrapper<T> get() {
        return new RpcWrapper<>();
    }

    /**
     * 定义 RpcWrapper
     *
     * @param <T>  T
     * @param info info
     * @return RpcWrapper
     */
    public static <T> RpcWrapper<T> of(CodeInfo info) {
        return new RpcWrapper<>(info.code(), info.message());
    }

    public static <T> RpcWrapper<T> of(CodeInfo info, T data) {
        return new RpcWrapper<>(info.code(), info.message(), data);
    }

    public static <T> RpcWrapper<T> of(int code) {
        return new RpcWrapper<>(code);
    }

    public static <T> RpcWrapper<T> of(int code, String msg) {
        return new RpcWrapper<>(code, msg);
    }

    public static <T> RpcWrapper<T> of(int code, String msg, T data) {
        return new RpcWrapper<>(code, msg, data);
    }

    /**
     * 成功
     */
    public static <T> RpcWrapper<T> success() {
        return new RpcWrapper<>(RPC_SUCCESS_CODE, RPC_SUCCESS_MESSAGE);
    }

    public static <T> RpcWrapper<T> success(T data) {
        return new RpcWrapper<>(RPC_SUCCESS_CODE, RPC_SUCCESS_MESSAGE, data);
    }

    /**
     * 失败
     */
    public static <T> RpcWrapper<T> error() {
        return new RpcWrapper<>(RPC_ERROR_CODE, RPC_ERROR_MESSAGE);
    }

    public static <T> RpcWrapper<T> error(String msg) {
        return new RpcWrapper<>(RPC_ERROR_CODE, msg);
    }

    public static <T> RpcWrapper<T> error(String msg, Object... params) {
        return new RpcWrapper<>(RPC_ERROR_CODE, Strings.format(msg, params));
    }

    public static <T> RpcWrapper<T> error(Throwable t) {
        if (t == null) {
            return new RpcWrapper<>(RPC_ERROR_CODE, RPC_ERROR_MESSAGE);
        } else {
            return new RpcWrapper<>(RPC_ERROR_CODE, t.getMessage());
        }
    }

    /**
     * 检查是否成功
     *
     * @return success
     */
    @JSONField(serialize = false)
    @JsonIgnore
    public boolean isSuccess() {
        return !RPC_ERROR_CODE.equals(code) && Lists.isEmpty(errorMessages);
    }

    /**
     * 失败抛出异常
     */
    public void checkIsError() {
        if (!isSuccess()) {
            throw Exceptions.rpcWrapper(this);
        }
    }

    public void checkIsError(Supplier<? extends RuntimeException> e) {
        if (!isSuccess()) {
            throw e.get();
        }
    }

    public RpcWrapper<T> addErrorMessage(String errorMsg) {
        if (errorMessages == null) {
            this.errorMessages = new ArrayList<>();
        }
        errorMessages.add(errorMsg);
        return this;
    }

    public RpcWrapper<T> code(int code) {
        this.code = code;
        return this;
    }

    public RpcWrapper<T> msg(String msg) {
        this.msg = msg;
        return this;
    }

    public RpcWrapper<T> data(T data) {
        this.data = data;
        return this;
    }

    public RpcWrapper<T> trace(Object object) {
        this.traceId = PRC_TRACE_PREFIX + object;
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

    public void setTraceId(String traceId) {
        this.traceId = PRC_TRACE_PREFIX + traceId;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
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

    public String getTraceId() {
        return traceId;
    }

    public List<String> getErrorMessages() {
        return errorMessages == null ? errorMessages = new ArrayList<>() : errorMessages;
    }

    @JSONField(serialize = false)
    @JsonIgnore
    public String getErrorMessageString() {
        return errorMessages == null ? Strings.EMPTY : Lists.join(errorMessages, Const.COMMA);
    }

    private String createTrace() {
        return this.traceId = PRC_TRACE_PREFIX + UUIds.random32();
    }

    @Override
    public String toString() {
        return this.toJsonString();
    }

    @Override
    public String toLogString() {
        boolean success = this.isSuccess();
        StringBuilder builder = new StringBuilder("RpcWrapper: ")
                .append("\n  isSuccess ==> ").append(success)
                .append("\n    traceId ==> ").append(traceId)
                .append("\n       code ==> ").append(code)
                .append("\n        msg ==> ").append(msg)
                .append("\n       data ==> ").append(Jsons.toJsonWriteNull(data));
        if (!success) {
            builder.append("\n   errorMsg ==> ").append(errorMessages);
        }
        return builder.toString();
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>(Const.CAPACITY_8);
        map.put("code", code);
        map.put("msg", msg);
        map.put("data", data);
        map.put("traceId", traceId);
        map.put("errorMessages", errorMessages);
        return map;
    }

    /**
     * @return {@link HttpWrapper}
     */
    public HttpWrapper<T> toHttpWrapper() {
        return HttpWrapper.of(code, msg, data);
    }

    /**
     * 映射
     *
     * @param mapping mapping
     * @param <E>     E
     * @return mapped
     */
    public <E> RpcWrapper<E> map(Function<T, E> mapping) {
        RpcWrapper<E> result = new RpcWrapper<>();
        result.code = this.code;
        result.msg = this.msg;
        result.traceId = this.traceId;
        result.errorMessages = this.errorMessages;
        result.data = Objects1.map(this.data, mapping);
        return result;
    }

    /**
     * @return result 的 Optional
     */
    public Optional<T> optional() {
        return Optional.of(this)
                .filter(RpcWrapper::isSuccess)
                .map(RpcWrapper::getData);
    }

}
