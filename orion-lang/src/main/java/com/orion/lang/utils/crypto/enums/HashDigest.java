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

import java.security.MessageDigest;

/**
 * 散列签名
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/28 15:59
 */
public enum HashDigest {

    /**
     * MD5
     */
    MD5("MD5"),

    /**
     * SHA-1
     */
    SHA1("SHA-1"),

    /**
     * SHA-224
     */
    SHA224("SHA-224"),

    /**
     * SHA-256
     */
    SHA256("SHA-256"),

    /**
     * SHA-384
     */
    SHA384("SHA-384"),

    /**
     * SHA-512
     */
    SHA512("SHA-512");

    private final String digest;

    HashDigest(String digest) {
        this.digest = digest;
    }

    public String getDigest() {
        return digest;
    }

    public MessageDigest getMessageDigest() {
        try {
            return MessageDigest.getInstance(digest);
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }

    public static MessageDigest getMessageDigest(String digest) {
        if (Strings.isBlank(digest)) {
            return null;
        }
        HashDigest[] values = values();
        for (HashDigest value : values) {
            if (value.getDigest().equalsIgnoreCase(digest.trim())) {
                return value.getMessageDigest();
            }
        }
        return null;
    }

}
