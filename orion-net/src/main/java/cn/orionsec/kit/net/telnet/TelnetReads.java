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
package cn.orionsec.kit.net.telnet;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.Charsets;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Telnet 流读取工具
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/29
 */
final class TelnetReads {

    /**
     * ANSI CSI 转义序列
     * <p>
     * shell 的行编辑会在提示符前后输出 ESC[6n / ESC[0m 之类的控制序列,
     * 这些内容不属于命令输出 需要剔除
     */
    private static final Pattern ANSI_CSI = Pattern.compile("\\u001B\\[[0-9;?]*[A-Za-z]");

    private TelnetReads() {
    }

    /**
     * 读取直到命中指定内容
     * <p>
     * 按块读取以避免逐字节读取的开销, 命中后返回内容截至 pattern 末尾
     * <p>
     * 注意: 与 pattern 同批到达的后续字节会被一并消费, 不再留给下一次读取,
     * 因此调用方应保证 pattern 之后不紧跟需要保留的数据
     * <p>
     * 返回内容中的 ANSI CSI 转义序列会被剔除
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
        int patternLength = patternBytes.length;
        // 缓冲区上限 非法值退化为默认上限
        int limit = maxBuffer > 0 ? maxBuffer : Const.BUFFER_KB_32;
        byte[] data = new byte[Math.min(limit, Const.BUFFER_KB_4)];
        byte[] chunk = new byte[Const.BUFFER_KB_4];
        int size = 0;
        long startTime = System.currentTimeMillis();
        try {
            while (true) {
                int read = in.read(chunk);
                if (read == -1) {
                    // 对端已关闭
                    break;
                }
                if (size + read > limit) {
                    throw Exceptions.runtime("telnet read buffer overflow");
                }
                if (size + read > data.length) {
                    data = Arrays.copyOf(data, Math.min(limit, Math.max(data.length << 1, size + read)));
                }
                // 上一次已读取的尾部字节需要参与匹配 避免 pattern 跨越块边界时漏判
                int searchFrom = Math.max(size - patternLength + 1, 0);
                System.arraycopy(chunk, 0, data, size, read);
                size += read;
                // 命中 pattern 后截断到 pattern 末尾, 与逐字节读取的语义保持一致
                int matched = indexOf(data, searchFrom, size, patternBytes);
                if (matched != -1) {
                    size = matched + patternLength;
                    break;
                }
                if (timeout > 0 && System.currentTimeMillis() - startTime > timeout) {
                    throw Exceptions.timeout("telnet read timeout");
                }
            }
        } catch (SocketTimeoutException e) {
            // soTimeout 阻塞超时
            throw Exceptions.timeout("telnet read timeout", e);
        }
        return ANSI_CSI.matcher(new String(data, 0, size, Charsets.of(charset)))
                .replaceAll(Const.EMPTY);
    }

    /**
     * 在缓冲区 [from, size) 范围内查找 pattern
     *
     * @param data    数据缓冲区
     * @param from    起始位置
     * @param size    已读取字节数
     * @param pattern pattern
     * @return pattern 的起始下标, 未命中返回 -1
     */
    private static int indexOf(byte[] data, int from, int size, byte[] pattern) {
        int last = size - pattern.length;
        for (int i = from; i <= last; i++) {
            int j = 0;
            while (j < pattern.length && data[i + j] == pattern[j]) {
                j++;
            }
            if (j == pattern.length) {
                return i;
            }
        }
        return -1;
    }

}
