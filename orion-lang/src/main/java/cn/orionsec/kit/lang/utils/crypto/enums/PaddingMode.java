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

/**
 * 填充模式
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/3 10:22
 */
public enum PaddingMode {

    /**
     * PKCS1
     */
    PKCS1_PADDING("PKCS1Padding"),

    /**
     * PKCS5
     */
    PKCS5_PADDING("PKCS5Padding"),

    /**
     * PKCS7
     */
    PKCS7_PADDING("PKCS7Padding"),

    /**
     * ISO10126
     */
    ISO_10126_PADDING("ISO10126Padding"),

    /**
     * ANSI_X_923_PADDING
     */
    ANSI_X_923_PADDING("X9.23PADDING"),

    /**
     * SSL3Padding
     */
    SSL3_PADDING("SSL3Padding"),

    /**
     * 不填充
     */
    NO_PADDING("NoPadding"),

    /**
     * 0填充 需要自己实现
     */
    ZERO_PADDING("NoPadding");

    private final String mode;

    PaddingMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

}
