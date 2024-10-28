/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package cn.orionsec.kit.http;

import cn.orionsec.kit.lang.able.IHttpResponse;
import cn.orionsec.kit.lang.utils.Valid;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * http 请求
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/7/8 16:47
 */
public abstract class BaseHttpResponse implements IHttpResponse {

    /**
     * 获取 body
     *
     * @return body
     */
    public abstract byte[] getBody();

    /**
     * 获取 body string
     *
     * @return body string
     */
    public abstract String getBodyString();

    /**
     * 验证是否成功
     */
    public void validOk() {
        Valid.validHttpOk(this);
    }

    /**
     * 获取 json body
     *
     * @param ref ref
     * @param <T> T
     * @return json
     */
    public <T> T getJsonObjectBody(TypeReference<T> ref) {
        this.validOk();
        return JSON.parseObject(this.getBodyString(), ref);
    }

}
