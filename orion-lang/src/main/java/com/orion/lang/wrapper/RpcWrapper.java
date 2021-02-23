package com.orion.lang.wrapper;

import com.orion.able.JsonAble;
import com.orion.able.Logable;
import com.orion.able.Mapable;
import com.orion.id.UUIds;
import com.orion.lang.support.CloneSupport;
import com.orion.utils.Objects1;
import com.orion.utils.Strings;
import com.orion.utils.collect.Lists;
import com.orion.utils.json.Jsons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * rpc远程调用结果封装
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/5/29 11:27
 */
public class RpcWrapper<T> extends CloneSupport<RpcWrapper<T>> implements Wrapper<T>, JsonAble, Logable, Mapable<String, Object> {

    private static final long serialVersionUID = 7940497300629314L;

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

    /**
     * 错误信息
     */
    private List<String> errorMessages;

    /**
     * 会话追踪标识
     */
    private String traceId = this.createTrace();

    public RpcWrapper() {
        this(RPC_SUCCESS_CODE, RPC_SUCCESS_MESSAGE, null);
    }

    public RpcWrapper(int code) {
        this(code, RPC_SUCCESS_MESSAGE, null);
    }

    public RpcWrapper(int code, String msg) {
        this(code, msg, null);
    }

    public RpcWrapper(int code, T data) {
        this(code, RPC_SUCCESS_MESSAGE, data);
    }

    public RpcWrapper(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public RpcWrapper(CodeInfo info) {
        this(info.code(), info.message(), null);
    }

    public RpcWrapper(CodeInfo info, T data) {
        this(info.code(), info.message(), data);
    }

    /**
     * 初始化
     */
    public static <T> RpcWrapper<T> get() {
        return new RpcWrapper<>();
    }

    /**
     * 定义
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

    public static <T> RpcWrapper<T> of(int code, String msg, Object... args) {
        return new RpcWrapper<>(code, Strings.format(msg, args));
    }

    public static <T> RpcWrapper<T> of(int code, String msg, T data) {
        return new RpcWrapper<>(code, msg, data);
    }

    public static <T> RpcWrapper<T> of(int code, T data) {
        return new RpcWrapper<>(code, data);
    }

    public static <T> RpcWrapper<T> of(T data, int code, String msg, Object... args) {
        return new RpcWrapper<>(code, Strings.format(msg, args), data);
    }

    /**
     * 成功
     */
    public static <T> RpcWrapper<T> success() {
        return new RpcWrapper<>(RPC_SUCCESS_CODE, RPC_SUCCESS_MESSAGE);
    }

    public static <T> RpcWrapper<T> success(String msg) {
        return new RpcWrapper<>(RPC_SUCCESS_CODE, msg);
    }

    public static <T> RpcWrapper<T> success(String msg, Object... args) {
        return new RpcWrapper<>(RPC_SUCCESS_CODE, Strings.format(msg, args));
    }

    public static <T> RpcWrapper<T> success(T data) {
        return new RpcWrapper<>(RPC_SUCCESS_CODE, RPC_SUCCESS_MESSAGE, data);
    }

    public static <T> RpcWrapper<T> success(String msg, T data) {
        return new RpcWrapper<>(RPC_SUCCESS_CODE, msg, data);
    }

    public static <T> RpcWrapper<T> success(T data, String msg, Object... args) {
        return new RpcWrapper<>(RPC_SUCCESS_CODE, Strings.format(msg, args), data);
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

    public static <T> RpcWrapper<T> error(String msg, Object... args) {
        return new RpcWrapper<>(RPC_ERROR_CODE, Strings.format(msg, args));
    }

    public static <T> RpcWrapper<T> error(T data) {
        return new RpcWrapper<>(RPC_ERROR_CODE, RPC_ERROR_MESSAGE, data);
    }

    public static <T> RpcWrapper<T> error(String msg, T data) {
        return new RpcWrapper<>(RPC_ERROR_CODE, msg, data);
    }

    public static <T> RpcWrapper<T> error(T data, String msg, Object... args) {
        return new RpcWrapper<>(RPC_ERROR_CODE, Strings.format(msg, args), data);
    }

    public static <T> RpcWrapper<T> error(Throwable t) {
        return new RpcWrapper<>(RPC_ERROR_CODE, t.getMessage());
    }

    public static <T> RpcWrapper<T> error(Throwable t, T data) {
        return new RpcWrapper<>(RPC_ERROR_CODE, t.getMessage(), data);
    }

    /**
     * 检查是否成功
     */
    public boolean isSuccess() {
        return RPC_ERROR_CODE != code && Lists.isEmpty(errorMessages);
    }

    /**
     * 失败抛出异常
     */
    public void errorThrows() {
        errorThrows(null, true);
    }

    public void errorThrows(String msg) {
        errorThrows(msg, false);
    }

    public void errorThrows(String msg, Object... args) {
        errorThrows(Strings.format(msg, args), false);
    }

    public void errorThrowsAppend(String msg, Object... args) {
        errorThrows(Strings.format(msg, args), true);
    }

    public void errorThrows(String msg, boolean appendErrMsg) {
        if (!isSuccess()) {
            if (appendErrMsg) {
                if (msg == null) {
                    throw new RuntimeException(Objects1.toString(errorMessages));
                } else {
                    throw new RuntimeException(msg + Strings.SPACE + Objects1.toString(errorMessages));
                }
            }
            if (msg == null) {
                throw new RuntimeException();
            } else {
                throw new RuntimeException(msg);
            }
        }
    }

    public void errorThrows(Supplier<? extends RuntimeException> e) {
        if (!isSuccess()) {
            throw e.get();
        }
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
        traceId = PRC_TRACE_PREFIX + object;
        return this;
    }

    public int getCode() {
        return code;
    }

    public RpcWrapper<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public RpcWrapper<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public RpcWrapper<T> setData(T data) {
        this.data = data;
        return this;
    }

    public String getTraceId() {
        return traceId;
    }

    public RpcWrapper<T> setTraceId(String traceId) {
        this.traceId = PRC_TRACE_PREFIX + traceId;
        return this;
    }

    public RpcWrapper<T> addErrorMessage(String errorMsg) {
        if (errorMsg == null) {
            this.errorMessages = new ArrayList<>();
        }
        this.errorMessages.add(errorMsg);
        return this;
    }

    public List<String> getErrorMessages() {
        return errorMessages == null ? errorMessages = new ArrayList<>() : errorMessages;
    }

    public RpcWrapper<T> setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
        return this;
    }

    public String getErrorMessageString() {
        return errorMessages == null ? Strings.EMPTY : errorMessages.toString();
    }

    private String createTrace() {
        return traceId = PRC_TRACE_PREFIX + UUIds.random32();
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
        StringBuilder builder = new StringBuilder();
        boolean success = isSuccess();
        builder.append("RpcWrapper:\n\tisSuccess ==> ").append(success).append("\n\t")
                .append("traceId ==> ").append(traceId).append("\n\t")
                .append("code ==> ").append(code).append("\n\t")
                .append("msg ==> ").append(msg).append("\n\t")
                .append("data ==> ").append(Jsons.toJsonWriteNull(data));
        if (!success) {
            builder.append("errorMsg ==> ").append(errorMessages);
        }
        return builder.toString();
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>(8);
        map.put("code", code);
        map.put("msg", msg);
        map.put("data", data);
        map.put("traceId", traceId);
        map.put("errorMsg", errorMessages);
        return map;
    }

}