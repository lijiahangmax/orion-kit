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

import javax.crypto.spec.SecretKeySpec;

/**
 * 密钥规格
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/29 17:01
 */
public enum SecretKeySpecMode {

    /**
     * AES
     */
    AES("AES"),

    /**
     * DES
     */
    DES("DES"),

    /**
     * 3DES
     */
    DES3("DESEDE"),

    /**
     * SM4
     */
    SM4("SM4"),

    /**
     * HmacMD5
     */
    HMAC_MD5("HmacMD5"),

    /**
     * HmacSHA1
     */
    HMAC_SHA1("HmacSHA1"),

    /**
     * HmacSHA224
     */
    HMAC_SHA224("HmacSHA224"),

    /**
     * HmacSHA256
     */
    HMAC_SHA256("HmacSHA256"),

    /**
     * HmacSHA384
     */
    HMAC_SHA384("HmacSHA384"),

    /**
     * HmacSHA512
     */
    HMAC_SHA512("HmacSHA512"),

    ;

    private final String mode;

    SecretKeySpecMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public SecretKeySpec getSecretKeySpec(byte[] bs) {
        return new SecretKeySpec(bs, mode);
    }

}
