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
package cn.orionsec.kit.lang.exception.argument;

/**
 * 带 code 的 exception
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/4/11 18:04
 */
public class CodeArgumentException extends InvalidArgumentException {

    private int code;

    public CodeArgumentException() {
    }

    public CodeArgumentException(String message) {
        super(message);
    }

    public CodeArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodeArgumentException(Throwable cause) {
        super(cause);
    }

    public CodeArgumentException(int code) {
        this.code = code;
    }

    public CodeArgumentException(int code, String message) {
        super(message);
        this.code = code;
    }

    public CodeArgumentException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public CodeArgumentException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
