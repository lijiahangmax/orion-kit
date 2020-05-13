package com.orion.wrapper;

/**
 * restful返回值
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/8/8 17:25
 */
public enum HttpStatus {

    /**
     * 成功
     */
    OK(200, "success"),

    /**
     * 失败
     */
    ERROR(500, "error"),

    /**
     * 未登录
     */
    NO_LOGIN(700, "Not signed in"),

    /**
     * 无权限
     */
    No_PERMISSION(701, "No permission");

    /**
     * 状态码
     */
    private int code;

    /**
     * 信息
     */
    private String msg;

    HttpStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public HttpStatus setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public <T> HttpWrapper<T> wrap() {
        return HttpWrapper.wrap(this);
    }

    @Override
    public String toString() {
        return "RestfulData{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

}
