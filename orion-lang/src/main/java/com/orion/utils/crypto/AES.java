package com.orion.utils.crypto;

import com.orion.utils.crypto.enums.CipherAlgorithm;
import com.orion.utils.crypto.enums.WorkingMode;
import com.orion.utils.crypto.symmetric.EcbSymmetric;
import com.orion.utils.crypto.symmetric.GcmSymmetric;
import com.orion.utils.crypto.symmetric.IvSymmetric;

import javax.crypto.SecretKey;

/**
 * AES 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/27 18:54
 */
public class AES {

    private static final EcbSymmetric ECB = new EcbSymmetric(CipherAlgorithm.AES);

    private static final IvSymmetric CBC = new IvSymmetric(CipherAlgorithm.AES, WorkingMode.CBC);

    private static final GcmSymmetric GCM = new GcmSymmetric();

    private AES() {
    }

    // -------------------- ENC --------------------

    public static String encrypt(String s, String key) {
        return ECB.encrypt(s, key);
    }

    public static String encrypt(String s, SecretKey key) {
        return ECB.encrypt(s, key);
    }

    public static byte[] encrypt(byte[] bs, byte[] key) {
        return ECB.encrypt(bs, key);
    }

    public static byte[] encrypt(byte[] bs, SecretKey key) {
        return ECB.encrypt(bs, key);
    }

    public static String encrypt(String s, String key, String iv) {
        return CBC.encrypt(s, key, iv);
    }

    public static String encrypt(String s, SecretKey key, String iv) {
        return CBC.encrypt(s, key, iv);
    }

    public static byte[] encrypt(byte[] bs, byte[] key, byte[] iv) {
        return CBC.encrypt(bs, key, iv);
    }

    public static byte[] encrypt(byte[] bs, SecretKey key, byte[] iv) {
        return CBC.encrypt(bs, key, iv);
    }

    public static String encrypt(String s, String key, String iv, String aad) {
        return GCM.encrypt(s, key, iv, aad);
    }

    public static String encrypt(String s, SecretKey key, String iv, String aad) {
        return GCM.encrypt(s, key, iv, aad);
    }

    public static byte[] encrypt(byte[] bs, byte[] key, byte[] iv, byte[] aad) {
        return GCM.encrypt(bs, key, iv, aad);
    }

    public static byte[] encrypt(byte[] bs, SecretKey key, byte[] iv, byte[] aad) {
        return GCM.encrypt(bs, key, iv, aad);
    }

    // -------------------- DEC --------------------

    public static String decrypt(String s, String key) {
        return ECB.decrypt(s, key);
    }

    public static String decrypt(String s, SecretKey key) {
        return ECB.decrypt(s, key);
    }

    public static byte[] decrypt(byte[] bs, byte[] key) {
        return ECB.decrypt(bs, key);
    }

    public static byte[] decrypt(byte[] bs, SecretKey key) {
        return ECB.decrypt(bs, key);
    }

    public static String decrypt(String s, String key, String iv) {
        return CBC.encrypt(s, key, iv);
    }

    public static String decrypt(String s, SecretKey key, String iv) {
        return CBC.encrypt(s, key, iv);
    }

    public static byte[] decrypt(byte[] bs, byte[] key, byte[] iv) {
        return CBC.encrypt(bs, key, iv);
    }

    public static byte[] decrypt(byte[] bs, SecretKey key, byte[] iv) {
        return CBC.encrypt(bs, key, iv);
    }

    public static String decrypt(String s, String key, String iv, String aad) {
        return GCM.encrypt(s, key, iv, aad);
    }

    public static String decrypt(String s, SecretKey key, String iv, String aad) {
        return GCM.encrypt(s, key, iv, aad);
    }

    public static byte[] decrypt(byte[] bs, byte[] key, byte[] iv, byte[] aad) {
        return GCM.encrypt(bs, key, iv, aad);
    }

    public static byte[] decrypt(byte[] bs, SecretKey key, byte[] iv, byte[] aad) {
        return GCM.encrypt(bs, key, iv, aad);
    }

}
