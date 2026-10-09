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
import cn.orionsec.kit.lang.utils.collect.Lists;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Telnet 流读取工具
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/29
 */
public final class TelnetReads {

    /**
     * ANSI CSI 转义序列
     */
    private static final Pattern ANSI_CSI = Pattern.compile("\\u001B\\[[0-9:;<=>?]*[\\u0020-\\u002F]*[@-~]");

    /**
     * ANSI CSI 未闭合片段 (至末尾仍无终止字节)
     */
    private static final Pattern ANSI_CSI_INCOMPLETE = Pattern.compile("\\u001B\\[[0-9:;<=>?]*[\\u0020-\\u002F]*$");

    /**
     * ANSI 字符串类序列 OSC/DCS/APC/PM/SOS
     */
    private static final Pattern ANSI_STRING = Pattern.compile("\\u001B[]P^_X][^\\u0007\\u001B]*(?:\\u0007|\\u001B\\\\)");

    /**
     * ANSI 字符串类未闭合片段 (至末尾仍无终止符)
     */
    private static final Pattern ANSI_STRING_INCOMPLETE = Pattern.compile("\\u001B[]P^_X][^\\u0007\\u001B]*$");

    /**
     * ANSI 非 CSI 转义序列 (ESC + 中间字节 + 终止字节, 如 ESC 7/8/=/>, ESC # 8, 字符集选择)
     */
    private static final Pattern ANSI_ESC = Pattern.compile("\\u001B[\\u0020-\\u002F]*[\\u0030-\\u007E]");

    private TelnetReads() {
    }

    /**
     * 剔除 ANSI 转义序列
     *
     * @param text 文本
     * @return 清理后的文本
     */
    private static String stripAnsi(String text) {
        text = ANSI_CSI.matcher(text).replaceAll(Const.EMPTY);
        text = ANSI_CSI_INCOMPLETE.matcher(text).replaceAll(Const.EMPTY);
        text = ANSI_STRING.matcher(text).replaceAll(Const.EMPTY);
        text = ANSI_STRING_INCOMPLETE.matcher(text).replaceAll(Const.EMPTY);
        return ANSI_ESC.matcher(text).replaceAll(Const.EMPTY);
    }

    /**
     * 读取直到命中指定内容
     *
     * @param in        in
     * @param pattern   pattern
     * @param charset   charset
     * @param timeout   超时时间 ms (0 为不超时)
     * @param maxBuffer 最大读取缓冲区字节数
     * @return 读取内容
     * @throws IOException IOException
     */
    public static String readUntil(InputStream in, String pattern, String charset, int timeout, int maxBuffer) throws IOException {
        return readUntil(in, Lists.singleton(pattern), charset, timeout, maxBuffer);
    }

    /**
     * 读取直到命中任意一个指定内容
     *
     * @param in        in
     * @param patterns  patterns (空值会被忽略)
     * @param charset   charset
     * @param timeout   超时时间 ms (0 为不超时)
     * @param maxBuffer 最大读取缓冲区字节数
     * @return 读取内容
     * @throws IOException IOException
     */
    public static String readUntil(InputStream in, Collection<String> patterns, String charset, int timeout, int maxBuffer) throws IOException {
        TelnetReadResult result = readUntilResult(in, patterns, charset, timeout, maxBuffer);
        if (result.isTimeout()) {
            throw Exceptions.timeout("telnet read timeout");
        }
        return result.getContent();
    }

    /**
     * 读取直到命中任意一个指定内容并返回结束状态
     *
     * @param in        in
     * @param patterns  patterns (空值会被忽略)
     * @param charset   charset
     * @param timeout   超时时间 ms (0 为不超时)
     * @param maxBuffer 最大读取缓冲区字节数
     * @return 读取结果
     * @throws IOException IOException
     */
    public static TelnetReadResult readUntilResult(InputStream in, Collection<String> patterns, String charset, int timeout, int maxBuffer) throws IOException {
        List<String> targets = new ArrayList<>(patterns.size());
        for (String pattern : patterns) {
            if (Strings.isNotBlank(pattern)) {
                targets.add(pattern);
            }
        }
        if (targets.isEmpty()) {
            return new TelnetReadResult(Const.EMPTY, false, false);
        }
        // timeout 为总耗时上限 (0 为不限)
        TelnetReadResult result = readUntilDecided(in, charset, timeout > 0 ? timeout : Integer.MAX_VALUE, maxBuffer,
                text -> matchEnd(text, targets) != -1,
                text -> true);
        // 未命中且对端未关闭即为超时
        String text = result.getContent();
        int matchedEnd = matchEnd(text, targets);
        if (matchedEnd == -1) {
            return new TelnetReadResult(text, result.isEof(), !result.isEof());
        }
        return new TelnetReadResult(text.substring(0, matchedEnd), result.isEof(), false);
    }

    /**
     * 读取直到输出静默或达到最大等待
     *
     * @param in        in
     * @param charset   charset
     * @param maxWaitMs 最大等待 ms
     * @param maxBuffer 最大读取缓冲区字节数
     * @return 读取内容
     * @throws IOException IOException
     */
    public static String readUntilIdle(InputStream in, String charset, int maxWaitMs, int maxBuffer) throws IOException {
        return readUntilIdleResult(in, charset, maxWaitMs, maxBuffer).getContent();
    }

    /**
     * 读取直到输出静默或达到最大等待并返回结束状态
     *
     * @param in        in
     * @param charset   charset
     * @param maxWaitMs 最大等待 ms
     * @param maxBuffer 最大读取缓冲区字节数
     * @return 读取结果
     * @throws IOException IOException
     */
    public static TelnetReadResult readUntilIdleResult(InputStream in, String charset, int maxWaitMs, int maxBuffer) throws IOException {
        // 收到数据后的首次静默即结算
        return readUntilDecided(in, charset, maxWaitMs, maxBuffer, null, text -> true);
    }

    /**
     * 读取直到判定回调可结算或达到最大等待
     *
     * @param in           in
     * @param charset      charset
     * @param maxWaitMs    最大等待 ms
     * @param maxBuffer    最大读取缓冲区字节数
     * @param decided      数据到达判定回调, true 表示可结算
     * @param settleOnIdle 静默判定回调 (可为 null), true 表示可结算
     * @return 读取结果
     * @throws IOException IOException
     */
    public static TelnetReadResult readUntilDecided(InputStream in, String charset, int maxWaitMs, int maxBuffer,
                                                    Predicate<String> decided,
                                                    Predicate<String> settleOnIdle) throws IOException {
        int limit = maxBuffer > 0 ? maxBuffer : Const.BUFFER_KB_32;
        byte[] data = new byte[Math.min(limit, Const.BUFFER_KB_4)];
        byte[] chunk = new byte[Const.BUFFER_KB_4];
        int size = 0;
        boolean received = false;
        boolean eof = false;
        long startTime = System.currentTimeMillis();
        while (true) {
            if (System.currentTimeMillis() - startTime >= maxWaitMs) {
                break;
            }
            try {
                int read = in.read(chunk);
                if (read == -1) {
                    eof = true;
                    break;
                }
                data = ensureCapacity(data, size, read, limit);
                System.arraycopy(chunk, 0, data, size, read);
                size += read;
                received = true;
                if (decided != null && decided.test(readText(data, size, charset))) {
                    break;
                }
            } catch (SocketTimeoutException e) {
                if (settleOnIdle != null && received && settleOnIdle.test(readText(data, size, charset))) {
                    break;
                }
            }
        }
        boolean timeout = !received && !eof;
        return new TelnetReadResult(readText(data, size, charset), eof, timeout);
    }

    /**
     * 读取直到数据到达判定回调可结算或达到最大等待
     *
     * @param in        in
     * @param charset   charset
     * @param maxWaitMs 最大等待 ms
     * @param maxBuffer 最大读取缓冲区字节数
     * @param decided   数据到达判定回调, true 表示可结算
     * @return 读取结果
     * @throws IOException IOException
     */
    public static TelnetReadResult readUntilDecided(InputStream in, String charset, int maxWaitMs, int maxBuffer, Predicate<String> decided) throws IOException {
        return readUntilDecided(in, charset, maxWaitMs, maxBuffer, decided, null);
    }

    /**
     * 校验缓冲区容量, 不足则扩容
     *
     * @param data  数据缓冲区
     * @param size  已读取字节数
     * @param read  单次读取字节数
     * @param limit 最大读取缓冲区字节数
     * @return 可写入的缓冲区
     */
    private static byte[] ensureCapacity(byte[] data, int size, int read, int limit) {
        if (size + read > limit) {
            throw Exceptions.runtime("telnet read buffer overflow");
        }
        if (size + read > data.length) {
            return Arrays.copyOf(data, Math.clamp((long) data.length << 1, size + read, limit));
        }
        return data;
    }

    /**
     * 读取缓冲区文本 (剔除 ANSI)
     *
     * @param data    数据缓冲区
     * @param size    已读取字节数
     * @param charset charset
     * @return 文本
     */
    private static String readText(byte[] data, int size, String charset) {
        return stripAnsi(new String(data, 0, size, Charsets.of(charset)));
    }

    /**
     * 查找最靠前命中 (起点最小, 同起点取更长的结束下标)
     *
     * @param text    文本
     * @param targets 目标内容
     * @return 结束下标, 未命中返回 -1
     */
    private static int matchEnd(String text, List<String> targets) {
        int matchedStart = -1;
        int matchedEnd = -1;
        for (String target : targets) {
            int matched = text.indexOf(target);
            if (matched == -1) {
                continue;
            }
            int end = matched + target.length();
            if (matchedStart == -1 || matched < matchedStart || (matched == matchedStart && end > matchedEnd)) {
                matchedStart = matched;
                matchedEnd = end;
            }
        }
        return matchedEnd;
    }

}
