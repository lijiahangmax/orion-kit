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
package cn.orionsec.kit.lang.utils.crypto.symmetric;

import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.crypto.enums.CipherAlgorithm;
import cn.orionsec.kit.lang.utils.crypto.enums.CryptoCodec;
import cn.orionsec.kit.lang.utils.crypto.enums.PaddingMode;
import cn.orionsec.kit.lang.utils.crypto.enums.WorkingMode;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/**
 * 非对称加密父类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/11/2 18:27
 */
public abstract class BaseSymmetric implements SymmetricCrypto {

    /**
     * 加密算法
     */
    protected CipherAlgorithm algorithm;

    /**
     * 工作模式
     */
    protected WorkingMode workingMode;

    /**
     * 填充模式
     */
    protected PaddingMode paddingMode;

    /**
     * 密钥
     */
    protected SecretKey secretKey;

    /**
     * 输入输出编解码器
     */
    protected CryptoCodec codec;

    protected BaseSymmetric(CipherAlgorithm cipherAlgorithm, WorkingMode workingMode, PaddingMode paddingMode, SecretKey secretKey, CryptoCodec codec) {
        this.algorithm = Assert.notNull(cipherAlgorithm, "cipherAlgorithm is null");
        this.workingMode = Assert.notNull(workingMode, "workingMode is null");
        this.paddingMode = Assert.notNull(paddingMode, "paddingMode is null");
        this.secretKey = Assert.notNull(secretKey, "secretKey is null");
        this.codec = Assert.notNull(codec, "codec is null");
    }

    /**
     * 获取 cipher
     *
     * @return cipher
     */
    protected Cipher getCipher() {
        return algorithm.getCipher(workingMode, paddingMode);
    }

    public CipherAlgorithm getAlgorithm() {
        return algorithm;
    }

    public WorkingMode getWorkingMode() {
        return workingMode;
    }

    public PaddingMode getPaddingMode() {
        return paddingMode;
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public CryptoCodec getCodec() {
        return codec;
    }

}
