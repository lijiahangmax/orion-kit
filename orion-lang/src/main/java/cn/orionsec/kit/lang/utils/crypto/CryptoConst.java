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
package cn.orionsec.kit.lang.utils.crypto;

/**
 * 加密常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/23 10:55
 */
public interface CryptoConst {

    String RSA = "RSA";

    /**
     * AES KEY长度 128 192 256
     */
    int AES_KEY_LENGTH = 128;

    /**
     * DES KEY长度 8
     */
    int DES_KEY_LENGTH = 8;

    /**
     * 3DES KEY长度 8
     */
    int DES3_KEY_LENGTH = 24;

    /**
     * SM4 KEY长度 16
     */
    int SM4_KEY_LENGTH = 16;

    /**
     * RSA KEY长度 512 ~ 16384
     */
    int RSA_KEY_LENGTH = 1024;

    /**
     * AES IV长度 16
     */
    int AES_IV_LENGTH = 16;

    /**
     * DES IV长度 8
     */
    int DES_IV_LENGTH = 8;

    /**
     * 3DES IV长度 8
     */
    int DES3_IV_LENGTH = 8;

    /**
     * SM4 IV长度 16
     */
    int SM4_IV_LENGTH = 16;

    /**
     * AES GCM长度 96 104 112 120 128
     */
    int GCM_SPEC_LENGTH = 128;

    String AES_ALGORITHM = "SHA1PRNG";

    String AES_PROVIDER = "SUN";

    String PKCS12 = "PKCS12";

    String X_509 = "X.509";

}
