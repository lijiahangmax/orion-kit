package com.orion.http;

import com.orion.lang.able.IHttpResponse;
import com.orion.lang.utils.Valid;

/**
 * http 请求
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/7/8 16:47
 */
public abstract class BaseHttpResponse implements IHttpResponse {

    /**
     * 验证是否成功
     */
    public void validOk() {
        Valid.validHttpOk(this);
    }

}
