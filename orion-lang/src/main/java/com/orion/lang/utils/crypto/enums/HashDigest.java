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
