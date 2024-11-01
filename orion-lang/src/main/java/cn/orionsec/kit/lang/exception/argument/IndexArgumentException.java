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
 * 对象验证索引异常
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/18 21:45
 */
public class IndexArgumentException extends InvalidArgumentException {

    private int index;

    private int size;

    public IndexArgumentException() {
    }

    public IndexArgumentException(String message) {
        super(message);
    }

    public IndexArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public IndexArgumentException(Throwable cause) {
        super(cause);
    }

    public IndexArgumentException(int index) {
        this.index = index;
    }

    public IndexArgumentException(int index, String message) {
        super(message);
        this.index = index;
    }

    public IndexArgumentException(int index, String message, Throwable cause) {
        super(message, cause);
        this.index = index;
    }

    public IndexArgumentException(int index, Throwable cause) {
        super(cause);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
