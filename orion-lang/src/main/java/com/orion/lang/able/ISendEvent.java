package com.orion.lang.able;

/**
 * 发送信息接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/11/18 18:16
 */
public interface ISendEvent<T> {

    /**
     * 发送接口
     *
     * @param msg msg
     */
    void send(T msg);

}
