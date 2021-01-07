package com.orion.utils.crypto.enums;

/**
 * 填充模式
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/11/3 10:22
 */
public enum PaddingMode {

    /**
     * PKCS5
     */
    PKCS5_PADDING("PKCS5Padding"),

    /**
     * ISO10126
     */
    ISO_10126_PADDING("ISO10126Padding"),

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

    private String mode;

    public String getMode() {
        return mode;
    }

}
