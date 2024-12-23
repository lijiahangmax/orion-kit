/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.lang.utils.crypto;

import cn.orionsec.kit.lang.utils.crypto.enums.WorkingMode;
import cn.orionsec.kit.lang.utils.crypto.symmetric.SymmetricBuilder;

import javax.crypto.SecretKey;

/**
 * 3DES 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/27 18:54
 */
public class DES3 {

    private DES3() {
    }

    // -------------------- ENC --------------------

    public static String encrypt(String s, String key) {
        return SymmetricBuilder.des3()
                .workingMode(WorkingMode.ECB)
                .generatorSecretKey(key)
                .buildEcb()
                .encryptAsString(s);
    }

    public static String encrypt(String s, SecretKey key) {
        return SymmetricBuilder.des3()
                .workingMode(WorkingMode.ECB)
                .secretKey(key)
                .buildEcb()
                .encryptAsString(s);
    }

    public static byte[] encrypt(byte[] s, byte[] key) {
        return SymmetricBuilder.des3()
                .workingMode(WorkingMode.ECB)
                .secretKey(key)
                .buildEcb()
                .encrypt(s);
    }

    public static byte[] encrypt(byte[] s, SecretKey key) {
        return SymmetricBuilder.des3()
                .workingMode(WorkingMode.ECB)
                .secretKey(key)
                .buildEcb()
                .encrypt(s);
    }

    public static String encrypt(String s, String key, String iv) {
        return SymmetricBuilder.des3()
                .workingMode(WorkingMode.CBC)
                .generatorSecretKey(key)
                .ivSpec(iv)
                .buildParam()
                .encryptAsString(s);
    }

    public static String encrypt(String s, SecretKey key, String iv) {
        return SymmetricBuilder.des3()
                .workingMode(WorkingMode.CBC)
                .secretKey(key)
                .ivSpec(iv)
                .buildParam()
                .encryptAsString(s);
    }

    public static byte[] encrypt(byte[] s, byte[] key, byte[] iv) {
        return SymmetricBuilder.des3()
                .workingMode(WorkingMode.CBC)
                .secretKey(key)
                .ivSpec(iv)
                .buildParam()
                .encrypt(s);
    }

    public static byte[] encrypt(byte[] s, SecretKey key, byte[] iv) {
        return SymmetricBuilder.des3()
                .workingMode(WorkingMode.CBC)
                .secretKey(key)
                .ivSpec(iv)
                .buildParam()
                .encrypt(s);
    }

    public static String encrypt(String s, String key, String gcm, String aad) {
        return SymmetricBuilder.des3()
                .workingMode(WorkingMode.GCM)
                .generatorSecretKey(key)
                .gcmSpec(gcm)
                .aad(aad)
                .buildParam()
                .encryptAsString(s);
    }

    public static String encrypt(String s, SecretKey key, String gcm, String aad) {
        return SymmetricBuilder.des3()
                .workingMode(WorkingMode.GCM)
                .secretKey(key)
                .gcmSpec(gcm)
                .aad(aad)
                .buildParam()
                .encryptAsString(s);
    }

    public static byte[] encrypt(byte[] s, byte[] key, byte[] gcm, byte[] aad) {
        return SymmetricBuilder.des3()
                .workingMode(WorkingMode.GCM)
                .secretKey(key)
                .gcmSpec(gcm)
                .aad(aad)
                .buildParam()
                .encrypt(s);
    }

    public static byte[] encrypt(byte[] s, SecretKey key, byte[] gcm, byte[] aad) {
        return SymmetricBuilder.des3()
                .workingMode(WorkingMode.GCM)
                .secretKey(key)
                .gcmSpec(gcm)
                .aad(aad)
                .buildParam()
                .encrypt(s);
    }

    // -------------------- DEC --------------------

    public static String decrypt(String s, String key) {
        return SymmetricBuilder.des3()
                .workingMode(WorkingMode.ECB)
                .generatorSecretKey(key)
                .buildEcb()
                .decryptAsString(s);
    }

    public static String decrypt(String s, SecretKey key) {
        return SymmetricBuilder.des3()
                .workingMode(WorkingMode.ECB)
                .secretKey(key)
                .buildEcb()
                .decryptAsString(s);
    }

    public static byte[] decrypt(byte[] s, byte[] key) {
        return SymmetricBuilder.des3()
                .workingMode(WorkingMode.ECB)
                .secretKey(key)
                .buildEcb()
                .decrypt(s);
    }

    public static byte[] decrypt(byte[] s, SecretKey key) {
        return SymmetricBuilder.des3()
                .workingMode(WorkingMode.ECB)
                .secretKey(key)
                .buildEcb()
                .decrypt(s);
    }

    public static String decrypt(String s, String key, String iv) {
        return SymmetricBuilder.des3()
                .workingMode(WorkingMode.CBC)
                .generatorSecretKey(key)
                .ivSpec(iv)
                .buildParam()
                .decryptAsString(s);
    }

    public static String decrypt(String s, SecretKey key, String iv) {
        return SymmetricBuilder.des3()
                .workingMode(WorkingMode.CBC)
                .secretKey(key)
                .ivSpec(iv)
                .buildParam()
                .decryptAsString(s);
    }

    public static byte[] decrypt(byte[] s, byte[] key, byte[] iv) {
        return SymmetricBuilder.des3()
                .workingMode(WorkingMode.CBC)
                .secretKey(key)
                .ivSpec(iv)
                .buildParam()
                .decrypt(s);
    }

    public static byte[] decrypt(byte[] s, SecretKey key, byte[] iv) {
        return SymmetricBuilder.des3()
                .workingMode(WorkingMode.CBC)
                .secretKey(key)
                .ivSpec(iv)
                .buildParam()
                .decrypt(s);
    }

    public static String decrypt(String s, String key, String gcm, String aad) {
        return SymmetricBuilder.des3()
                .workingMode(WorkingMode.GCM)
                .generatorSecretKey(key)
                .gcmSpec(gcm)
                .aad(aad)
                .buildParam()
                .decryptAsString(s);
    }

    public static String decrypt(String s, SecretKey key, String gcm, String aad) {
        return SymmetricBuilder.des3()
                .workingMode(WorkingMode.GCM)
                .secretKey(key)
                .gcmSpec(gcm)
                .aad(aad)
                .buildParam()
                .decryptAsString(s);
    }

    public static byte[] decrypt(byte[] s, byte[] key, byte[] gcm, byte[] aad) {
        return SymmetricBuilder.des3()
                .workingMode(WorkingMode.GCM)
                .secretKey(key)
                .gcmSpec(gcm)
                .aad(aad)
                .buildParam()
                .decrypt(s);
    }

    public static byte[] decrypt(byte[] s, SecretKey key, byte[] gcm, byte[] aad) {
        return SymmetricBuilder.des3()
                .workingMode(WorkingMode.GCM)
                .secretKey(key)
                .gcmSpec(gcm)
                .aad(aad)
                .buildParam()
                .decrypt(s);
    }

}
