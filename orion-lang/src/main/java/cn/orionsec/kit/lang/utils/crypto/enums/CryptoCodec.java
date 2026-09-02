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
package cn.orionsec.kit.lang.utils.crypto.enums;

import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.codec.Base64s;
import cn.orionsec.kit.lang.utils.math.Hex;

import java.nio.charset.StandardCharsets;

/**
 * 加解密输入输出编解码器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/9/2 15:30
 */
public enum CryptoCodec {

    /**
     * HEX 编码
     */
    HEX {
        @Override
        public byte[] encode(byte[] bytes) {
            if (bytes == null || bytes.length == 0) {
                return bytes;
            }
            return Hex.bytesToHex(bytes).getBytes(StandardCharsets.US_ASCII);
        }

        @Override
        public byte[] decode(byte[] bytes) {
            if (bytes == null || bytes.length == 0) {
                return bytes;
            }
            return Hex.hexToBytes(new String(bytes, StandardCharsets.US_ASCII));
        }
    },

    /**
     * BASE64 编码
     */
    BASE64 {
        @Override
        public byte[] encode(byte[] bytes) {
            if (bytes == null || bytes.length == 0) {
                return bytes;
            }
            return Base64s.encode(bytes);
        }

        @Override
        public byte[] decode(byte[] bytes) {
            if (bytes == null || bytes.length == 0) {
                return bytes;
            }
            return Base64s.decode(bytes);
        }
    },

    /**
     * 不编码 输入输出均为原始字节
     */
    NONE {
        @Override
        public byte[] encode(byte[] bytes) {
            return bytes;
        }

        @Override
        public byte[] decode(byte[] bytes) {
            return bytes;
        }
    };

    /**
     * 编码
     *
     * @param bytes 原始字节
     * @return 编码后的字节
     */
    public abstract byte[] encode(byte[] bytes);

    /**
     * 解码
     *
     * @param bytes 编码后的字节
     * @return 原始字节
     */
    public abstract byte[] decode(byte[] bytes);

    /**
     * 编码为字符串
     *
     * @param bytes 原始字节
     * @return 编码后的字符串
     */
    public String encodeToString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return new String(this.encode(bytes), StandardCharsets.US_ASCII);
    }

    /**
     * 解码字符串
     *
     * @param s 编码后的字符串
     * @return 原始字节
     */
    public byte[] decodeToBytes(String s) {
        if (s == null) {
            return null;
        }
        return this.decode(Strings.bytes(s));
    }

}
