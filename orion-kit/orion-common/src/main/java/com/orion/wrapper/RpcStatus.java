package com.orion.wrapper;

/**
 * rpc返回值
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/8/8 19:31
 */
public enum RpcStatus {

    /**
     * 成功
     */
    SUCCESS(2000, "success"),

    /**
     * 失败
     */
    ERROR(5000, "error");

    /**
     * 状态码
     */
    private int code;

    /**
     * 信息
     */
    private String msg;

    RpcStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public RpcStatus setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public RpcStatus setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public <T> RpcWrapper<T> wrap() {
        return RpcWrapper.wrap(this);
    }

    @Override
    public String toString() {
        return "RestfulData{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

}
