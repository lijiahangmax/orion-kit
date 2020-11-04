package com.orion.able;

/**
 * 发送信息接口
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/11/18 18:16
 */
public interface SendAble<T> {

    /**
     * 发送接口
     *
     * @param msg msg
     * @return send result
     */
    boolean send(T msg);

}
