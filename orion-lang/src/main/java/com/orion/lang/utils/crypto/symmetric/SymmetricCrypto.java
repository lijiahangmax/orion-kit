package com.orion.lang.utils.crypto.symmetric;

import com.orion.lang.utils.Strings;

import java.util.Arrays;

/**
 * 对称加密
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/7 23:48
 */
public interface SymmetricCrypto {

    /**
     * 加密
     *
     * @param plain 明文
     * @return 密文
     */
    byte[] encrypt(byte[] plain);

    /**
     * 加密
     *
     * @param plain 明文
     * @return 密文
     */
    default byte[] encrypt(String plain) {
        return this.encrypt(Strings.bytes(plain));
    }

    /**
     * 加密
     *
     * @param plain 明文
     * @return 密文
     */
    default String encryptAsString(String plain) {
        return new String(this.encrypt(Strings.bytes(plain)));
    }

    /**
     * 加密
     *
     * @param plain 明文
     * @return 密文
     */
    default String encryptAsString(byte[] plain) {
        return new String(this.encrypt(plain));
    }

    /**
     * 解密
     *
     * @param text 密文
     * @return 明文
     */
    byte[] decrypt(byte[] text);

    /**
     * 解密
     *
     * @param text 密文
     * @return 明文
     */
    default byte[] decrypt(String text) {
        return this.decrypt(Strings.bytes(text));
    }

    /**
     * 解密
     *
     * @param text 密文
     * @return 明文
     */
    default String decryptAsString(String text) {
        return new String(this.decrypt(Strings.bytes(text)));
    }

    /**
     * 解密
     *
     * @param text 密文
     * @return 明文
     */
    default String decryptAsString(byte[] text) {
        return new String(this.decrypt(text));
    }

    /**
     * 验证加密结果
     *
     * @param plain 明文
     * @param text  密文
     * @return 是否成功
     */
    default boolean verify(String plain, String text) {
        try {
            return plain.equals(this.decryptAsString(text));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 验证加密结果
     *
     * @param plain 明文
     * @param text  密文
     * @return 是否成功
     */
    default boolean verify(byte[] plain, byte[] text) {
        try {
            return Arrays.equals(plain, this.decrypt(text));
        } catch (Exception e) {
            return false;
        }
    }

}
