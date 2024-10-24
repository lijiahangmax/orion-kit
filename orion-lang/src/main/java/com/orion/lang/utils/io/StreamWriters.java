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
package com.orion.lang.utils.io;

import com.orion.lang.utils.Strings;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * 输出流工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/27 14:03
 */
public class StreamWriters {

    private StreamWriters() {
    }

    /**
     * 写入数组
     *
     * @param out out
     * @param bs  bytes
     * @param off offset
     * @param len length
     */
    public static void write(OutputStream out, byte[] bs, int off, int len) throws IOException {
        out.write(bs, off, len);
    }

    /**
     * 写入字符
     *
     * @param out out
     * @param str str
     */
    public static void write(OutputStream out, String str) throws IOException {
        out.write(Strings.bytes(str));
    }

    /**
     * 写入字符
     *
     * @param out out
     * @param str str
     */
    public static void write(OutputStream out, String str, String charset) throws IOException {
        if (charset == null) {
            out.write(Strings.bytes(str));
        } else {
            out.write(Strings.bytes(str, charset));
        }
    }

    public static void write(Writer writer, byte[] bs) throws IOException {
        writer.write(new String(bs));
    }

    public static void write(Writer writer, byte[] bs, int off, int len) throws IOException {
        writer.write(new String(bs, off, len));
    }

    public static void write(Writer writer, char[] cs) throws IOException {
        writer.write(cs);
    }

    /**
     * 写入字符
     *
     * @param writer writer
     * @param str    str
     */
    public static void write(Writer writer, String str) throws IOException {
        writer.write(str);
    }

}
