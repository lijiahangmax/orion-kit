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
package cn.orionsec.kit.net.host.telnet;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Charsets;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;

/**
 * Telnet 流读取工具
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/29
 */
final class TelnetReads {

    private TelnetReads() {
    }

    /**
     * 读取直到命中指定内容
     * <p>
     * 按字节累积 按 charset 解码 支持多字节编码
     * 读取阻塞超时依赖 socket soTimeout 触发 {@link SocketTimeoutException}
     *
     * @param in        in
     * @param pattern   pattern
     * @param charset   charset
     * @param timeout   超时时间 ms (0 为不超时)
     * @param maxBuffer 最大读取缓冲区字节数
     * @return 读取内容
     * @throws IOException IOException
     */
    static String readUntil(InputStream in, String pattern, String charset, int timeout, int maxBuffer) throws IOException {
        if (Strings.isBlank(pattern)) {
            return Const.EMPTY;
        }
        byte[] patternBytes = Strings.bytes(pattern, charset);
        // 尾部环形缓冲区 用于字节级尾部匹配
        byte[] tail = new byte[patternBytes.length];
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        long startTime = System.currentTimeMillis();
        int read;
        try {
            while ((read = in.read()) != -1) {
                buffer.write(read);
                tail[(buffer.size() - 1) % tail.length] = (byte) read;
                // 尾部命中 pattern
                if (buffer.size() >= patternBytes.length && tailMatches(tail, buffer.size(), patternBytes)) {
                    break;
                }
                if (timeout > 0 && System.currentTimeMillis() - startTime > timeout) {
                    throw Exceptions.timeout("telnet read timeout");
                }
                if (buffer.size() > maxBuffer) {
                    throw Exceptions.runtime("telnet read buffer overflow");
                }
            }
        } catch (SocketTimeoutException e) {
            // soTimeout 阻塞超时
            throw Exceptions.timeout("telnet read timeout", e);
        }
        return new String(buffer.toByteArray(), Charsets.of(charset));
    }

    /**
     * 环形缓冲区尾部是否命中 pattern
     *
     * @param tail    tail
     * @param total   已读取总字节数
     * @param pattern pattern
     * @return 是否命中
     */
    private static boolean tailMatches(byte[] tail, int total, byte[] pattern) {
        int length = pattern.length;
        for (int i = 0; i < length; i++) {
            if (tail[(total - length + i) % length] != pattern[i]) {
                return false;
            }
        }
        return true;
    }

}
