package com.orion.wrapper;

import com.orion.able.Jsonable;
import com.orion.able.Logable;
import com.orion.able.Mapable;
import com.orion.utils.json.Jsons;
import com.orion.utils.Objects1;
import com.orion.utils.Strings;
import com.orion.utils.UUIds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * rpc远程调用结果封装
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/5/29 11:27
 */
public class RpcWrapper<T> implements Wrapper<T>, Jsonable, Logable, Mapable {

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
    private List<String> errorMsg;

    /**
     * 会话追踪标识
     */
    private String traceID = this.createTrace();

    /**
     * 跟踪号前缀
     */
    private static final String TRACE_PREFIX = "SESSION-TRACE-";

    private RpcWrapper() {
    }

    private RpcWrapper(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private RpcWrapper(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
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
    public static <T> RpcWrapper<T> wrap(int code) {
        return new RpcWrapper<>(code, null);
    }

    public static <T> RpcWrapper<T> wrap(int code, String msg) {
        return new RpcWrapper<>(code, msg);
    }

    public static <T> RpcWrapper<T> wrap(int code, String tpl, Object... args) {
        return new RpcWrapper<>(code, Strings.format(tpl, args));
    }

    public static <T> RpcWrapper<T> wrap(int code, String msg, T data) {
        return new RpcWrapper<>(code, msg, data);
    }

    public static <T> RpcWrapper<T> wrap(T data, int code, String tpl, Object... args) {
        return new RpcWrapper<>(code, Strings.format(tpl, args), data);
    }

    public static <T> RpcWrapper<T> wrap(RpcStatus rpcStatus) {
        return new RpcWrapper<>(rpcStatus.getCode(), rpcStatus.getMsg());
    }

    public static <T> RpcWrapper<T> wrap(RpcStatus rpcStatus, T data) {
        return new RpcWrapper<>(rpcStatus.getCode(), rpcStatus.getMsg(), data);
    }

    public static <T> RpcWrapper<T> wrap(RpcStatus rpcStatus, Object... args) {
        return new RpcWrapper<>(rpcStatus.getCode(), Strings.format(rpcStatus.getMsg(), args));
    }

    public static <T> RpcWrapper<T> wrap(T data, RpcStatus rpcStatus, Object... args) {
        return new RpcWrapper<>(rpcStatus.getCode(), Strings.format(rpcStatus.getMsg(), args), data);
    }

    /**
     * 成功
     */
    public static <T> RpcWrapper<T> success() {
        return new RpcWrapper<>(RpcStatus.SUCCESS.getCode(), RpcStatus.SUCCESS.getMsg());
    }

    public static <T> RpcWrapper<T> success(String msg) {
        return new RpcWrapper<>(RpcStatus.SUCCESS.getCode(), msg);
    }

    public static <T> RpcWrapper<T> success(String tpl, Object... args) {
        return new RpcWrapper<>(RpcStatus.SUCCESS.getCode(), Strings.format(tpl, args));
    }

    public static <T> RpcWrapper<T> success(T data) {
        return new RpcWrapper<>(RpcStatus.SUCCESS.getCode(), RpcStatus.SUCCESS.getMsg(), data);
    }

    public static <T> RpcWrapper<T> success(String msg, T data) {
        return new RpcWrapper<>(RpcStatus.SUCCESS.getCode(), msg, data);
    }

    public static <T> RpcWrapper<T> success(T data, String tpl, Object... args) {
        return new RpcWrapper<>(RpcStatus.SUCCESS.getCode(), Strings.format(tpl, args), data);
    }

    /**
     * 失败
     */
    public static <T> RpcWrapper<T> error() {
        return new RpcWrapper<>(RpcStatus.ERROR.getCode(), RpcStatus.ERROR.getMsg());
    }

    public static <T> RpcWrapper<T> error(String msg) {
        return new RpcWrapper<>(RpcStatus.ERROR.getCode(), msg);
    }

    public static <T> RpcWrapper<T> error(String tpl, Object... args) {
        return new RpcWrapper<>(RpcStatus.ERROR.getCode(), Strings.format(tpl, args));
    }

    public static <T> RpcWrapper<T> error(T data) {
        return new RpcWrapper<>(RpcStatus.ERROR.getCode(), RpcStatus.ERROR.getMsg(), data);
    }

    public static <T> RpcWrapper<T> error(String msg, T data) {
        return new RpcWrapper<>(RpcStatus.ERROR.getCode(), msg, data);
    }

    public static <T> RpcWrapper<T> error(T data, String tpl, Object... args) {
        return new RpcWrapper<>(RpcStatus.ERROR.getCode(), Strings.format(tpl, args), data);
    }

    /**
     * 检查是否成功
     */
    public boolean isSuccess() {
        return RpcStatus.ERROR.getCode() != code && (errorMsg == null || errorMsg.isEmpty());
    }

    /**
     * 失败抛出异常
     */
    public void errorThrows() {
        errorThrows("", true);
    }

    public void errorThrows(String msg) {
        errorThrows(msg, true);
    }

    public void errorThrows(String tpl, Object... args) {
        errorThrows(Strings.format(tpl, args), true);
    }

    public void errorThrows(boolean appendErrMsg, String tpl, Object... args) {
        errorThrows(Strings.format(tpl, args), appendErrMsg);
    }

    public void errorThrows(String msg, boolean appendErrMsg) {
        if (!isSuccess()) {
            if (appendErrMsg) {
                throw new RuntimeException(msg + " " + errorMsg);
            }
            throw new RuntimeException(msg);
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
        traceID = TRACE_PREFIX + object;
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

    public RpcWrapper<T> addErrorMsg(String errorMsg) {
        if (errorMsg == null) {
            this.errorMsg = new ArrayList<>();
        }
        this.errorMsg.add(errorMsg);
        return this;
    }

    public List<String> getErrorMsg() {
        return errorMsg;
    }

    public RpcWrapper<T> setErrorMsg(List<String> errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public String getTraceID() {
        return traceID;
    }

    public RpcWrapper<T> setTraceID(String traceID) {
        this.traceID = TRACE_PREFIX + traceID;
        return this;
    }

    private String createTrace() {
        return traceID = TRACE_PREFIX + UUIds.random32();
    }

    @Override
    public String toString() {
        return "RpcWrapper{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + Objects1.toString(data) +
                ", errorMsg=" + errorMsg +
                ", traceID='" + traceID + '\'' +
                '}';
    }

    @Override
    public String toJsonString() {
        return Jsons.toJSONWriteNull(this);
    }

    @Override
    public String toLogString() {
        StringBuilder builder = new StringBuilder();
        boolean success = isSuccess();
        builder.append("RpcWrapper:\n\tisSuccess ==> ").append(success).append("\n\t")
                .append("traceID ==> ").append(traceID).append("\n\t")
                .append("code ==> ").append(code).append("\n\t")
                .append("msg ==> ").append(msg).append("\n\t")
                .append("data ==> ").append(Jsons.toJSONWriteNull(data));
        if (!success) {
            builder.append("errorMsg ==> ").append(errorMsg);
        }
        return builder.toString();
    }

    @Override
    public Map toMap() {
        Map<String, Object> map = new HashMap<>(8);
        map.put("code", code);
        map.put("msg", msg);
        map.put("data", data);
        map.put("traceID", traceID);
        map.put("errorMsg", errorMsg);
        return map;
    }

}
