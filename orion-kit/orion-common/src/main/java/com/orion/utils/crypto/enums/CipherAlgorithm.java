package com.orion.utils.crypto.enums;

import com.orion.utils.Exceptions;
import com.orion.utils.Strings;

import javax.crypto.Cipher;

/**
 * 对称加密算法
 *
 * @author ljh15
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
    DES3("DESEDE");

    CipherAlgorithm(String model) {
        this.mode = model;
    }

    String mode;

    public String getMode() {
        return mode;
    }

    public Cipher getCipher() {
        try {
            return Cipher.getInstance(mode);
        } catch (Exception e) {
            // impossible
            throw Exceptions.runtime(e);
        }
    }

    /**
     * 如: DES/ECB/PKCS5Padding
     * 解密算法/工作模式/填充方式
     *
     * @param work    工作模式
     * @param padding 填充方式
     * @return Cipher
     */
    public Cipher getCipher(String work, String padding) {
        try {
            return Cipher.getInstance(mode + "/" + work + "/" + padding);
        } catch (Exception e) {
            // impossible
            throw Exceptions.runtime(e);
        }
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
