package com.orion.utils.crypto.enums;

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

    PaddingMode(String mode) {
        this.mode = mode;
    }

    private final String mode;

    public String getMode() {
        return mode;
    }

}
