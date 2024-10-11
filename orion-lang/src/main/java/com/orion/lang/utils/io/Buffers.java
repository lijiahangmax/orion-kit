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

import com.orion.lang.constant.Letters;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;

import java.nio.ByteBuffer;

/**
 * buffer 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/3 23:30
 */
public class Buffers {

    private Buffers() {
    }

    /**
     * 拷贝到一个新的 ByteBuffer
     *
     * @param src   源
     * @param start 起始位置 包括
     * @param end   结束位置 不包括
     * @return ByteBuffer
     */
    public static ByteBuffer copy(ByteBuffer src, int start, int end) {
        return copy(src, ByteBuffer.allocate(end - start));
    }

    /**
     * 拷贝 ByteBuffer
     *
     * @param src  源
     * @param dest 目标
     * @return 目标
     */
    public static ByteBuffer copy(ByteBuffer src, ByteBuffer dest) {
        return copy(src, dest, Math.min(src.limit(), dest.remaining()));
    }

    /**
     * 拷贝 ByteBuffer
     *
     * @param src    源
     * @param dest   目标
     * @param length 长度
     * @return 目标
     */
    public static ByteBuffer copy(ByteBuffer src, ByteBuffer dest, int length) {
        return copy(src, src.position(), dest, dest.position(), length);
    }

    /**
     * 拷贝 ByteBuffer
     *
     * @param src       源
     * @param srcStart  源开始的位置
     * @param dest      目标
     * @param destStart 目标开始的位置
     * @param length    长度
     * @return 目标
     */
    public static ByteBuffer copy(ByteBuffer src, int srcStart, ByteBuffer dest, int destStart, int length) {
        System.arraycopy(src.array(), srcStart, dest.array(), destStart, length);
        return dest;
    }

    /**
     * 读取剩余部分并转为字符串
     *
     * @param buffer ByteBuffer
     * @return 字符串
     */
    public static String readStr(ByteBuffer buffer) {
        return new String(readBytes(buffer));
    }

    /**
     * 读取剩余部分 bytes
     *
     * @param buffer ByteBuffer
     * @return bytes
     */
    public static byte[] readBytes(ByteBuffer buffer) {
        int remaining = buffer.remaining();
        byte[] arr = new byte[remaining];
        buffer.get(arr);
        return arr;
    }

    /**
     * 读取指定长度的 bytes
     * <br>
     * 如果长度不足则读取剩余部分 此时buffer必须为读模式
     *
     * @param buffer    ByteBuffer
     * @param maxLength 最大长度
     * @return bytes
     */
    public static byte[] readBytes(ByteBuffer buffer, int maxLength) {
        int remaining = buffer.remaining();
        if (maxLength > remaining) {
            maxLength = remaining;
        }
        byte[] arr = new byte[maxLength];
        buffer.get(arr);
        return arr;
    }

    /**
     * 读取指定区间的数据
     *
     * @param buffer buffer
     * @param start  开始位置
     * @param end    结束位置
     * @return bytes
     */
    public static byte[] readBytes(ByteBuffer buffer, int start, int end) {
        byte[] bs = new byte[end - start];
        System.arraycopy(buffer.array(), start, bs, 0, bs.length);
        return bs;
    }

    /**
     * 一行的末尾位置 查找位置时位移 ByteBuffer 到结束位置
     *
     * @param buffer ByteBuffer
     * @return 末尾位置 未找到或达到最大长度返回 -1
     */
    public static int lineEnd(ByteBuffer buffer) {
        return lineEnd(buffer, buffer.remaining());
    }

    /**
     * 一行的末尾位置 查找位置时位移 ByteBuffer 到结束位置
     *
     * @param buffer    ByteBuffer
     * @param maxLength 读取最大长度
     * @return 末尾位置 未找到或达到最大长度返回 -1
     */
    public static int lineEnd(ByteBuffer buffer, int maxLength) {
        int primitivePosition = buffer.position();
        boolean canEnd = false;
        int charIndex = primitivePosition;
        byte b;
        while (buffer.hasRemaining()) {
            b = buffer.get();
            charIndex++;
            if (b == Letters.CR) {
                canEnd = true;
            } else if (b == Letters.LF) {
                return canEnd ? charIndex - 2 : charIndex - 1;
            } else {
                // 只有\r无法确认换行
                canEnd = false;
            }
            if (charIndex - primitivePosition > maxLength) {
                // 查找到尽头未找到 还原位置
                buffer.position(primitivePosition);
                throw Exceptions.index(Strings.format("position is out of maxLength: {}", maxLength));
            }
        }
        // 查找到尽头未找到 还原位置
        buffer.position(primitivePosition);
        return -1;
    }

    /**
     * 读取一行 如果 buffer 中最后一部分并非完整一行则返回 null
     *
     * @param buffer ByteBuffer
     * @return line
     */
    public static String readLine(ByteBuffer buffer) {
        int startPosition = buffer.position();
        int endPosition = lineEnd(buffer);
        if (endPosition > startPosition) {
            return new String(readBytes(buffer, startPosition, endPosition));
        } else if (endPosition == startPosition) {
            return Strings.EMPTY;
        }
        return null;
    }

    /**
     * 创建新 Buffer
     *
     * @param data 数据
     * @return ByteBuffer
     */
    public static ByteBuffer create(byte[] data) {
        return ByteBuffer.wrap(data);
    }

    /**
     * 从字符串创建新 Buffer
     *
     * @param data 数据
     * @return ByteBuffer
     */
    public static ByteBuffer create(String data) {
        return ByteBuffer.wrap(Strings.bytes(data));
    }

}
