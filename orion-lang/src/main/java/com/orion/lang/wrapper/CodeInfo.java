package com.orion.lang.wrapper;

import java.io.Serializable;

/**
 * wrapper code & message
 * <p>
 * 可以使用枚举对象定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/2/20 15:13
 */
public interface CodeInfo extends Serializable {

    /**
     * 获取code
     *
     * @return code
     */
    int code();

    /**
     * 获取message
     *
     * @return message
     */
    String message();

}
