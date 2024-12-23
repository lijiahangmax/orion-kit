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

import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;

import java.security.Signature;

/**
 * RSA 签名
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/28 15:07
 */
public enum RSASignature {

    /**
     * NONE
     */
    NONE("NONEwithRSA"),

    /**
     * MD5
     */
    MD5("MD5withRSA"),

    /**
     * SHA1
     */
    SHA1("SHA1WithRSA"),

    /**
     * SHA224
     */
    SHA224("SHA224WithRSA"),

    /**
     * SHA256
     */
    SHA256("SHA256WithRSA"),

    /**
     * SHA384
     */
    SHA384("SHA384WithRSA"),

    /**
     * SHA512
     */
    SHA512("SHA512WithRSA");

    private final String model;

    RSASignature(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public Signature getSignature() {
        try {
            return Signature.getInstance(model);
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }

    public static Signature getSignature(String model) {
        if (Strings.isBlank(model)) {
            return null;
        }
        RSASignature[] values = values();
        for (RSASignature value : values) {
            if (value.getModel().equalsIgnoreCase(model.trim())) {
                return value.getSignature();
            }
        }
        return null;
    }

}
