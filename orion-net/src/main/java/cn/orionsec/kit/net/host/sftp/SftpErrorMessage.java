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
package cn.orionsec.kit.net.host.sftp;

import cn.orionsec.kit.lang.utils.Strings;

/**
 * SFTP 错误信息
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/10 16:45
 */
public enum SftpErrorMessage {

    /**
     * 未找到文件
     */
    NO_SUCH_FILE("no such file"),

    BAD_MESSAGE("bad message");

    private final String message;

    SftpErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 是否为此错误原因
     *
     * @param e e
     * @return isCause
     */
    public boolean isCause(Exception e) {
        String message = e.getMessage();
        if (Strings.isEmpty(message)) {
            return false;
        }
        return message.toLowerCase().contains(this.message);
    }

}
