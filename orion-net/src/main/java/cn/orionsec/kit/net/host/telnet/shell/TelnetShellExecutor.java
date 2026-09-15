/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.net.host.telnet.shell;

import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.net.host.telnet.BaseTelnetExecutor;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Telnet shell 执行器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/29
 */
public class TelnetShellExecutor extends BaseTelnetExecutor {

    /**
     * @param client       client
     * @param inputStream  inputStream
     * @param outputStream outputStream
     * @param prompt       prompt
     * @param charset      charset
     * @param readTimeout  readTimeout
     */
    public TelnetShellExecutor(TelnetClient client,
                               InputStream inputStream,
                               OutputStream outputStream,
                               String prompt,
                               String charset,
                               int readTimeout) {
        super(client, inputStream, outputStream, prompt, charset, readTimeout);
    }

    @Override
    protected void listenerOutput() {
        this.streamReading = true;
        try {
            // 监听输出流
            streamHandler.accept(inputStream);
        } finally {
            // 回调
            this.done = true;
            if (callback != null) {
                callback.run();
            }
        }
    }

    @Override
    public void exec() {
        if (streamHandler == null) {
            throw Exceptions.runtime("telnet std output stream handler is null");
        }
        if (!this.isConnected()) {
            throw Exceptions.runtime("telnet session is not connected");
        }
        this.checkStreamReading();
        // 监听输出流
        this.listenerOutput();
    }

}
