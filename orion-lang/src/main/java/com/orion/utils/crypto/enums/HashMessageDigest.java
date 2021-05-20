package com.orion.utils.crypto.enums;

import com.orion.utils.Exceptions;
import com.orion.utils.Strings;

import java.security.MessageDigest;

/**
 * 散列签名
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/28 15:59
 */
public enum HashMessageDigest {

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

    HashMessageDigest(String digest) {
        this.digest = digest;
    }

    String digest;

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
        HashMessageDigest[] values = values();
        for (HashMessageDigest value : values) {
            if (value.getDigest().equalsIgnoreCase(digest.trim())) {
                return value.getMessageDigest();
            }
        }
        return null;
    }

}
