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
package com.orion.lang.utils.crypto.enums;

import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.Provider;
import java.security.Security;

/**
 * 加密算法
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/28 15:05
 */
public enum CipherAlgorithm {

    /**
     * RSA
     */
    RSA("RSA"),

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
    SM4("SM4") {
        @Override
        public Cipher getCipher(String work, String padding) {
            return super.getCipher(work, padding, BouncyCastleProvider.PROVIDER_NAME);
        }

        @Override
        public Cipher getCipher(WorkingMode work, PaddingMode padding) {
            return super.getCipher(work, padding, BouncyCastleProvider.PROVIDER_NAME);
        }
    },

    ;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private final String mode;

    CipherAlgorithm(String model) {
        this.mode = model;
    }

    public String getMode() {
        return mode;
    }

    public Cipher getCipher() {
        try {
            return Cipher.getInstance(mode);
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }

    public Cipher getCipher(String work, String padding) {
        try {
            return Cipher.getInstance(this.getCipherMode(work, padding));
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }

    public Cipher getCipher(WorkingMode work, PaddingMode padding) {
        try {
            return Cipher.getInstance(this.getCipherMode(work, padding));
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }

    public Cipher getCipher(String work, String padding, String provider) {
        try {
            return Cipher.getInstance(this.getCipherMode(work, padding), provider);
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }

    public Cipher getCipher(WorkingMode work, PaddingMode padding, String provider) {
        try {
            return Cipher.getInstance(this.getCipherMode(work, padding), provider);
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }

    public Cipher getCipher(String work, String padding, Provider provider) {
        try {
            return Cipher.getInstance(this.getCipherMode(work, padding), provider);
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }

    /**
     * 解密算法/工作模式/填充方式
     *
     * @param work    工作模式
     * @param padding 填充方式
     * @return Cipher
     */
    public Cipher getCipher(WorkingMode work, PaddingMode padding, Provider provider) {
        try {
            return Cipher.getInstance(this.getCipherMode(work, padding), provider);
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }

    protected String getCipherMode(WorkingMode work, PaddingMode padding) {
        return this.getCipherMode(work.getMode(), padding.getMode());
    }

    /**
     * 获取算法 解密算法/工作模式/填充方式
     *
     * @param work    工作模式
     * @param padding 填充方式
     * @return mode
     */
    protected String getCipherMode(String work, String padding) {
        return mode + "/" + work + "/" + padding;
    }

    public static Cipher getCipher(String model) {
        if (Strings.isBlank(model)) {
            return null;
        }
        CipherAlgorithm[] values = values();
        for (CipherAlgorithm value : values) {
            if (value.getMode().equalsIgnoreCase(model.trim())) {
                return value.getCipher();
            }
        }
        return null;
    }

}
