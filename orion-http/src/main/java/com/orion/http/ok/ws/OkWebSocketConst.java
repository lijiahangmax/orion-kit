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
package com.orion.http.ok.ws;

/**
 * OkHttp WebSocket 常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/10 11:25
 */
public interface OkWebSocketConst {

    String CLIENT_SESSION_ID_HEADER = "Mock-Client-Session-Id";

    String SERVER_TEMP_SESSION_ID = "TEMP-";

    int SERVER_FAIL_CLOSE_CODE = 3001;

    String SERVER_FAIL_CLOSE_REASON = "Server 关闭Fail连接";

    int SERVER_CLOSE_CODE = 3002;

    String SERVER_CLOSE_REASON = "Server 关闭连接";

    int SERVER_CLIENT_FULL_CLOSE_CODE = 3002;

    String SERVER_CLIENT_FULL_CLOSE_REASON = "Server 连接已满 关闭连接";

    int CLIENT_FAIL_CLOSE_CODE = 3004;

    String CLIENT_FAIL_CLOSE_REASON = "Client 关闭Fail连接";

    int CLIENT_CLOSE_CODE = 3005;

    String CLIENT_CLOSE_REASON = "Client 关闭连接";

}
