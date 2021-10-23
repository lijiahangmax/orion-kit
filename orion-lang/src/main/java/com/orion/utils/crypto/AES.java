package com.orion.utils.crypto;

import com.orion.utils.crypto.enums.WorkingMode;
import com.orion.utils.crypto.symmetric.SymmetricBuilder;

import javax.crypto.SecretKey;

/**
 * AES 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/27 18:54
 */
public class AES {

    private AES() {
    }

    // -------------------- ENC --------------------

    public static String encrypt(String s, String key) {
        return SymmetricBuilder.aes()
                .workingMode(WorkingMode.ECB)
                .generatorSecretKey(key)
                .buildEcb()
                .encryptAsString(s);
    }

    public static String encrypt(String s, SecretKey key) {
        return SymmetricBuilder.aes()
                .workingMode(WorkingMode.ECB)
                .secretKey(key)
                .buildEcb()
                .encryptAsString(s);
    }

    public static byte[] encrypt(byte[] s, byte[] key) {
        return SymmetricBuilder.aes()
                .workingMode(WorkingMode.ECB)
                .secretKey(key)
                .buildEcb()
                .encrypt(s);
    }

    public static byte[] encrypt(byte[] s, SecretKey key) {
        return SymmetricBuilder.aes()
                .workingMode(WorkingMode.ECB)
                .secretKey(key)
                .buildEcb()
                .encrypt(s);
    }

    public static String encrypt(String s, String key, String iv) {
        return SymmetricBuilder.aes()
                .workingMode(WorkingMode.CBC)
                .generatorSecretKey(key)
                .ivSpec(iv)
                .buildParam()
                .encryptAsString(s);
    }

    public static String encrypt(String s, SecretKey key, String iv) {
        return SymmetricBuilder.aes()
                .workingMode(WorkingMode.CBC)
                .secretKey(key)
                .ivSpec(iv)
                .buildParam()
                .encryptAsString(s);
    }

    public static byte[] encrypt(byte[] s, byte[] key, byte[] iv) {
        return SymmetricBuilder.aes()
                .workingMode(WorkingMode.CBC)
                .secretKey(key)
                .ivSpec(iv)
                .buildParam()
                .encrypt(s);
    }

    public static byte[] encrypt(byte[] s, SecretKey key, byte[] iv) {
        return SymmetricBuilder.aes()
                .workingMode(WorkingMode.CBC)
                .secretKey(key)
                .ivSpec(iv)
                .buildParam()
                .encrypt(s);
    }

    public static String encrypt(String s, String key, String gcm, String aad) {
        return SymmetricBuilder.aes()
                .workingMode(WorkingMode.GCM)
                .generatorSecretKey(key)
                .gcmSpec(gcm)
                .aad(aad)
                .buildParam()
                .encryptAsString(s);
    }

    public static String encrypt(String s, SecretKey key, String gcm, String aad) {
        return SymmetricBuilder.aes()
                .workingMode(WorkingMode.GCM)
                .secretKey(key)
                .gcmSpec(gcm)
                .aad(aad)
                .buildParam()
                .encryptAsString(s);
    }

    public static byte[] encrypt(byte[] s, byte[] key, byte[] gcm, byte[] aad) {
        return SymmetricBuilder.aes()
                .workingMode(WorkingMode.GCM)
                .secretKey(key)
                .gcmSpec(gcm)
                .aad(aad)
                .buildParam()
                .encrypt(s);
    }

    public static byte[] encrypt(byte[] s, SecretKey key, byte[] gcm, byte[] aad) {
        return SymmetricBuilder.aes()
                .workingMode(WorkingMode.GCM)
                .secretKey(key)
                .gcmSpec(gcm)
                .aad(aad)
                .buildParam()
                .encrypt(s);
    }

    // -------------------- DEC --------------------

    public static String decrypt(String s, String key) {
        return SymmetricBuilder.aes()
                .workingMode(WorkingMode.ECB)
                .generatorSecretKey(key)
                .buildEcb()
                .decryptAsString(s);
    }

    public static String decrypt(String s, SecretKey key) {
        return SymmetricBuilder.aes()
                .workingMode(WorkingMode.ECB)
                .secretKey(key)
                .buildEcb()
                .decryptAsString(s);
    }

    public static byte[] decrypt(byte[] s, byte[] key) {
        return SymmetricBuilder.aes()
                .workingMode(WorkingMode.ECB)
                .secretKey(key)
                .buildEcb()
                .decrypt(s);
    }

    public static byte[] decrypt(byte[] s, SecretKey key) {
        return SymmetricBuilder.aes()
                .workingMode(WorkingMode.ECB)
                .secretKey(key)
                .buildEcb()
                .decrypt(s);
    }

    public static String decrypt(String s, String key, String iv) {
        return SymmetricBuilder.aes()
                .workingMode(WorkingMode.CBC)
                .generatorSecretKey(key)
                .ivSpec(iv)
                .buildParam()
                .decryptAsString(s);
    }

    public static String decrypt(String s, SecretKey key, String iv) {
        return SymmetricBuilder.aes()
                .workingMode(WorkingMode.CBC)
                .secretKey(key)
                .ivSpec(iv)
                .buildParam()
                .decryptAsString(s);
    }

    public static byte[] decrypt(byte[] s, byte[] key, byte[] iv) {
        return SymmetricBuilder.aes()
                .workingMode(WorkingMode.CBC)
                .secretKey(key)
                .ivSpec(iv)
                .buildParam()
                .decrypt(s);
    }

    public static byte[] decrypt(byte[] s, SecretKey key, byte[] iv) {
        return SymmetricBuilder.aes()
                .workingMode(WorkingMode.CBC)
                .secretKey(key)
                .ivSpec(iv)
                .buildParam()
                .decrypt(s);
    }

    public static String decrypt(String s, String key, String gcm, String aad) {
        return SymmetricBuilder.aes()
                .workingMode(WorkingMode.GCM)
                .generatorSecretKey(key)
                .gcmSpec(gcm)
                .aad(aad)
                .buildParam()
                .decryptAsString(s);
    }

    public static String decrypt(String s, SecretKey key, String gcm, String aad) {
        return SymmetricBuilder.aes()
                .workingMode(WorkingMode.GCM)
                .secretKey(key)
                .gcmSpec(gcm)
                .aad(aad)
                .buildParam()
                .decryptAsString(s);
    }

    public static byte[] decrypt(byte[] s, byte[] key, byte[] gcm, byte[] aad) {
        return SymmetricBuilder.aes()
                .workingMode(WorkingMode.GCM)
                .secretKey(key)
                .gcmSpec(gcm)
                .aad(aad)
                .buildParam()
                .decrypt(s);
    }

    public static byte[] decrypt(byte[] s, SecretKey key, byte[] gcm, byte[] aad) {
        return SymmetricBuilder.aes()
                .workingMode(WorkingMode.GCM)
                .secretKey(key)
                .gcmSpec(gcm)
                .aad(aad)
                .buildParam()
                .decrypt(s);
    }

}
