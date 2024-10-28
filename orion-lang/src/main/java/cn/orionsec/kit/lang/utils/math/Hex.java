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
package cn.orionsec.kit.lang.utils.math;

import cn.orionsec.kit.lang.utils.Strings;

/**
 * 进制转换
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/16 10:26
 */
public class Hex {

    private Hex() {
    }

    /**
     * byte转十六进制
     *
     * @param b byte
     * @return 十六进制
     */
    public static String byteToHex(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * byte[]转十六进制
     *
     * @param bs byte[]
     * @return 十六进制
     */
    public static String bytesToHex(byte[] bs) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bs) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 十六进制转byte
     *
     * @param s hexString
     * @return byte
     */
    public static byte hexToByte(String s) {
        return (byte) Integer.parseInt(s, 16);
    }

    /**
     * 十六进制转byte[]
     *
     * @param s hexString
     * @return byte[]
     */
    public static byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] result;
        if (len % 2 == 1) {
            // 奇数
            len++;
            result = new byte[(len / 2)];
            s = "0" + s;
        } else {
            // 偶数
            result = new byte[(len / 2)];
        }
        int j = 0;
        for (int i = 0; i < len; i += 2) {
            result[j] = hexToByte(s.substring(i, i + 2));
            j++;
        }
        return result;
    }

    public static String encode(String s) {
        return bytesToHex(Strings.bytes(s));
    }

    public static String decode(String s) {
        return new String(hexToBytes(s));
    }

}
