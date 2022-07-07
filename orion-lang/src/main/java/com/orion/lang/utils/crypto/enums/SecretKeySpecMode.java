package com.orion.lang.utils.crypto.enums;

import javax.crypto.spec.SecretKeySpec;

/**
 * 秘钥规格
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

    SecretKeySpecMode(String mode) {
        this.mode = mode;
    }

    private final String mode;

    public String getMode() {
        return mode;
    }

    public SecretKeySpec getSecretKeySpec(byte[] bs) {
        return new SecretKeySpec(bs, mode);
    }

}
