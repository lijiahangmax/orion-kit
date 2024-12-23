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
package cn.orionsec.kit.lang.utils.codec;

import cn.orionsec.kit.lang.utils.Arrays1;
import cn.orionsec.kit.lang.utils.Strings;

import java.io.ByteArrayOutputStream;

/**
 * Base62 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/10/24 21:25
 */
public class Base62s {

    private static final int STANDARD_BASE = 256;

    private static final int TARGET_BASE = 62;

    /**
     * GMP风格
     */
    private static final byte[] GMP = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
            'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
            'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z'
    };

    /**
     * GMP反转风格
     */
    private static final byte[] INVERTED = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    private static final Base62s GMP_ENCODE = new Base62s(GMP);

    private static final Base62s GMP_INVERTED_ENCODE = new Base62s(INVERTED);

    private final byte[] alphabet;

    private final byte[] lookup;

    private Base62s(byte[] alphabet) {
        this.alphabet = alphabet;
        lookup = new byte[256];
        for (int i = 0; i < alphabet.length; i++) {
            lookup[alphabet[i]] = (byte) (i & 0xFF);
        }
    }

    /**
     * 编码
     *
     * @param s s
     * @return ignore
     */
    public static String encode(String s) {
        return new String(encode(Strings.bytes(s), true));
    }

    public static String encode(String s, boolean gmp) {
        return new String(encode(Strings.bytes(s), gmp));
    }

    public static byte[] encode(byte[] bs) {
        return encode(bs, true);
    }

    public static byte[] encode(byte[] bs, boolean gmp) {
        return gmp ? GMP_ENCODE.encodeBytes(bs) : GMP_INVERTED_ENCODE.encodeBytes(bs);
    }

    /**
     * 解码
     *
     * @param s s
     * @return ignore
     */
    public static String decode(String s) {
        return new String(decode(Strings.bytes(s), true));
    }

    public static String decode(String s, boolean gmp) {
        return new String(decode(Strings.bytes(s), gmp));
    }

    public static byte[] decode(byte[] bs) {
        return decode(bs, true);
    }

    public static byte[] decode(byte[] bs, boolean gmp) {
        return gmp ? GMP_ENCODE.decodeBytes(bs) : GMP_INVERTED_ENCODE.decodeBytes(bs);
    }

    /**
     * 编码
     *
     * @param bs byte
     * @return ignore
     */
    private byte[] encodeBytes(byte[] bs) {
        byte[] indices = convert(bs, STANDARD_BASE, TARGET_BASE);
        return translate(indices, alphabet);
    }

    /**
     * 解码
     *
     * @param encoded ignore
     * @return ignore
     */
    private byte[] decodeBytes(byte[] encoded) {
        byte[] prepared = translate(encoded, lookup);
        return convert(prepared, TARGET_BASE, STANDARD_BASE);
    }

    private byte[] translate(byte[] indices, byte[] dictionary) {
        byte[] translation = new byte[indices.length];
        for (int i = 0; i < indices.length; i++) {
            translation[i] = dictionary[indices[i]];
        }
        return translation;
    }

    private byte[] convert(byte[] message, int sourceBase, int targetBase) {
        int estimatedLength = estimateOutputLength(message.length, sourceBase, targetBase);
        ByteArrayOutputStream out = new ByteArrayOutputStream(estimatedLength);
        byte[] source = message;
        while (source.length > 0) {
            ByteArrayOutputStream quotient = new ByteArrayOutputStream(source.length);
            int remainder = 0;
            for (byte b : source) {
                int accumulator = (b & 0xFF) + remainder * sourceBase;
                int digit = (accumulator - (accumulator % targetBase)) / targetBase;
                remainder = accumulator % targetBase;
                if (quotient.size() > 0 || digit > 0) {
                    quotient.write(digit);
                }
            }
            out.write(remainder);
            source = quotient.toByteArray();
        }
        for (int i = 0; i < message.length - 1 && message[i] == 0; i++) {
            out.write(0);
        }
        byte[] bytes = out.toByteArray();
        Arrays1.reverse(bytes);
        return bytes;
    }

    private int estimateOutputLength(int inputLength, int sourceBase, int targetBase) {
        return (int) Math.ceil((Math.log(sourceBase) / Math.log(targetBase)) * inputLength);
    }

}