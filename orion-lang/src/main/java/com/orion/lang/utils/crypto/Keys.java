/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package com.orion.lang.utils.crypto;

import com.orion.lang.constant.Const;
import com.orion.lang.define.wrapper.Pair;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.crypto.enums.CipherAlgorithm;
import com.orion.lang.utils.crypto.enums.SecretKeySpecMode;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.Streams;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.*;
import java.io.*;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import static com.orion.lang.utils.codec.Base64s.decode;
import static com.orion.lang.utils.codec.Base64s.encode;

/**
 * key 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/24 18:47
 */
public class Keys {

    private Keys() {
    }

    // -------------------- CER --------------------

    public static PublicKey getCerPublicKey(File file) {
        return getCerPublicKey(Files1.openInputStreamSafe(file), true);
    }

    public static PublicKey getCerPublicKey(String file) {
        return getCerPublicKey(Files1.openInputStreamSafe(file), true);
    }

    public static PublicKey getCerPublicKey(InputStream in) {
        return getCerPublicKey(in, false);
    }

    /**
     * 读取cer公钥文件
     *
     * @param in    流
     * @param close 是否关闭流
     * @return PublicKey
     */
    public static PublicKey getCerPublicKey(InputStream in, boolean close) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance(CryptoConst.X_509);
            X509Certificate cert = (X509Certificate) cf.generateCertificate(in);
            return cert.getPublicKey();
        } catch (Exception e) {
            return null;
        } finally {
            if (close) {
                Streams.close(in);
            }
        }
    }

    // -------------------- PFX --------------------

    public static Pair<PublicKey, PrivateKey> getPfxKeys(File file, String password) {
        return getPfxKeys(Files1.openInputStreamSafe(file), password, true);
    }

    public static Pair<PublicKey, PrivateKey> getPfxKeys(String file, String password) {
        return getPfxKeys(Files1.openInputStreamSafe(file), password, true);
    }

    public static Pair<PublicKey, PrivateKey> getPfxKeys(InputStream in, String password) {
        return getPfxKeys(in, password, false);
    }

    /**
     * 读取PFX私钥文件
     *
     * @param in       in
     * @param password 密码
     * @param close    是否关闭流
     * @return PublicKey, PrivateKey
     */
    public static Pair<PublicKey, PrivateKey> getPfxKeys(InputStream in, String password, boolean close) {
        try {
            char[] ps = password == null ? null : password.toCharArray();
            KeyStore ks = KeyStore.getInstance(CryptoConst.PKCS12);
            ks.load(in, ps);
            Enumeration<String> aliases = ks.aliases();
            String keyAlias = null;
            if (aliases.hasMoreElements()) {
                keyAlias = aliases.nextElement();
            }
            Certificate cert = ks.getCertificate(keyAlias);
            return Pair.of(cert.getPublicKey(), (PrivateKey) ks.getKey(keyAlias, ps));
        } catch (Exception e) {
            return null;
        } finally {
            if (close) {
                Streams.close(in);
            }
        }
    }

    public static KeyStore getKeyStore(File file, String password) {
        return getKeyStore(Files1.openInputStreamSafe(file), password);
    }

    /**
     * 加载 KeyStore
     *
     * @param in       in
     * @param password password
     * @return KeyStore
     */
    public static KeyStore getKeyStore(InputStream in, String password) {
        char[] ps = password == null ? null : password.toCharArray();
        try {
            KeyStore ks = KeyStore.getInstance(CryptoConst.PKCS12);
            ks.load(in, ps);
            return ks;
        } catch (Exception e) {
            throw Exceptions.runtime("could not be loaded key");
        }
    }

    // -------------------- KEY --------------------

    /**
     * PublicKey -> StringKey
     *
     * @param key PublicKey
     * @return StringKey
     */
    public static String getPublicKey(PublicKey key) {
        return new String(encode(key.getEncoded()));
    }

    /**
     * PrivateKey -> StringKey
     *
     * @param key PrivateKey
     * @return StringKey
     */
    public static String getPrivateKey(PrivateKey key) {
        return new String(encode(key.getEncoded()));
    }

    /**
     * SecretKey -> StringKey
     *
     * @param key SecretKey
     * @return StringKey
     */
    public static String getSecretKey(SecretKey key) {
        return new String(encode(key.getEncoded()));
    }

    public static String getKey(File file) {
        return getKey(new InputStreamReader(Files1.openInputStreamSafe(file)), true);
    }

    public static String getKey(String file) {
        return getKey(new InputStreamReader(Files1.openInputStreamSafe(file)), true);
    }

    public static String getKey(InputStream in) {
        return getKey(new InputStreamReader(in), false);
    }

    public static String getKey(Reader reader) {
        return getKey(reader, false);
    }

    public static String getKey(Reader reader, boolean close) {
        StringBuilder key = new StringBuilder();
        int c = 0;
        BufferedReader r = new BufferedReader(reader);
        try {
            String s = r.readLine();
            while (s != null) {
                if (s.contains("--")) {
                    if (++c == 2) {
                        break;
                    }
                } else {
                    key.append(s);
                }
                s = r.readLine();
            }
            return key.toString()
                    .replaceAll(Const.LF, Strings.EMPTY)
                    .replaceAll(Const.CR, Strings.EMPTY);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            if (close) {
                Streams.close(reader);
            }
        }
    }

    // -------------------- AES DES 3DES KEY --------------------

    /**
     * 获取 key 规格长度
     *
     * @param mode mode
     * @return key 长度
     */
    public static int getKeySpecLength(CipherAlgorithm mode) {
        switch (mode) {
            case AES:
                return CryptoConst.AES_KEY_LENGTH;
            case DES:
                return CryptoConst.DES_KEY_LENGTH;
            case DES3:
                return CryptoConst.DES3_KEY_LENGTH;
            case SM4:
                return CryptoConst.SM4_KEY_LENGTH;
            case RSA:
                return CryptoConst.RSA_KEY_LENGTH;
            default:
                throw Exceptions.unsupported("unsupported get " + mode + " key spec length");
        }
    }

    /**
     * 获取 IV 规格长度
     *
     * @param mode mode
     * @return IV 长度
     */
    public static int getIvSpecLength(CipherAlgorithm mode) {
        switch (mode) {
            case AES:
                return CryptoConst.AES_IV_LENGTH;
            case DES:
                return CryptoConst.DES_IV_LENGTH;
            case DES3:
                return CryptoConst.DES3_IV_LENGTH;
            case SM4:
                return CryptoConst.SM4_IV_LENGTH;
            default:
                throw Exceptions.unsupported("unsupported get " + mode + "iv spec length");
        }
    }

    /**
     * 获取 GCM 规格长度
     *
     * @param mode mode
     * @return GCM 长度
     */
    public static int getGcmSpecLength(CipherAlgorithm mode) {
        switch (mode) {
            case AES:
                return CryptoConst.GCM_SPEC_LENGTH;
            default:
                throw Exceptions.unsupported("unsupported get " + mode + "gcm spec length");
        }
    }

    public static IvParameterSpec getIvSpec(byte[] iv) {
        return new IvParameterSpec(iv);
    }

    public static IvParameterSpec getIvSpec(CipherAlgorithm mode, byte[] iv) {
        return getIvSpec(iv, getIvSpecLength(mode));
    }

    /**
     * 获取向量
     *
     * @param iv        向量
     * @param ivSpecLen 向量长度
     * @return 填充后的向量
     */
    public static IvParameterSpec getIvSpec(byte[] iv, int ivSpecLen) {
        return new IvParameterSpec(Arrays1.resize(iv, ivSpecLen));
    }

    public static GCMParameterSpec getGcmSpec(byte[] gcm) {
        return new GCMParameterSpec(gcm.length, gcm);
    }

    public static GCMParameterSpec getGcmSpec(CipherAlgorithm mode, byte[] gcm) {
        return new GCMParameterSpec(getGcmSpecLength(mode), gcm);
    }

    /**
     * 生成 GCM 规范参数
     *
     * @param gcm        gcm
     * @param gcmSpecLen 长度
     * @return GCMParameterSpec
     */
    public static GCMParameterSpec getGcmSpec(byte[] gcm, int gcmSpecLen) {
        return new GCMParameterSpec(gcmSpecLen, gcm);
    }

    /**
     * StringKey -> SecretKey
     *
     * @param key  StringKey
     * @param mode CipherAlgorithm
     * @return SecretKey
     */
    public static SecretKey getSecretKey(byte[] key, CipherAlgorithm mode) {
        return new SecretKeySpec(decode(key), mode.getMode());
    }

    /**
     * StringKey -> SecretKey
     *
     * @param key  StringKey
     * @param mode CipherAlgorithm
     * @return SecretKey
     */
    public static SecretKey getSecretKey(String key, CipherAlgorithm mode) {
        return new SecretKeySpec(decode(Strings.bytes(key)), mode.getMode());
    }

    public static SecretKey generatorKey(String key, CipherAlgorithm mode) {
        return generatorKey(Strings.bytes(key), getKeySpecLength(mode), mode);
    }

    public static SecretKey generatorKey(byte[] key, CipherAlgorithm mode) {
        return generatorKey(key, getKeySpecLength(mode), mode);
    }

    public static SecretKey generatorKey(String key, int keySize, CipherAlgorithm mode) {
        return generatorKey(Strings.bytes(key), keySize, mode);
    }

    public static SecretKey generatorKey(byte[] key, int keySize, CipherAlgorithm mode) {
        return generatorKey(key, keySize, mode, CryptoConst.AES_ALGORITHM, CryptoConst.AES_PROVIDER);
    }

    /**
     * String -> SecretKey
     *
     * @param key     key
     * @param keySize key 位数
     *                AES 128 192 256  {@link CryptoConst#AES_KEY_LENGTH}
     *                DES 8            {@link CryptoConst#DES_KEY_LENGTH}
     *                3DES 24          {@link CryptoConst#DES3_KEY_LENGTH}
     *                SM4  16          {@link CryptoConst#SM4_KEY_LENGTH}
     * @param mode    CipherAlgorithm
     * @return SecretKey
     */
    public static SecretKey generatorKey(byte[] key, int keySize, CipherAlgorithm mode, String algorithm, String provider) {
        try {
            switch (mode) {
                case AES:
                    KeyGenerator keyGenerator = KeyGenerator.getInstance(mode.getMode());
                    SecureRandom random = SecureRandom.getInstance(algorithm, provider);
                    random.setSeed(key);
                    keyGenerator.init(keySize, random);
                    return SecretKeySpecMode.AES.getSecretKeySpec(keyGenerator.generateKey().getEncoded());
                case DES:
                    if (key.length != keySize) {
                        key = Arrays1.resize(key, keySize);
                    }
                    return SecretKeyFactory.getInstance(mode.getMode()).generateSecret(new DESKeySpec(key));
                case DES3:
                    if (key.length != keySize) {
                        key = Arrays1.resize(key, keySize);
                    }
                    return SecretKeyFactory.getInstance(mode.getMode()).generateSecret(new DESedeKeySpec(key));
                case SM4:
                    if (key.length != keySize) {
                        key = Arrays1.resize(key, keySize);
                    }
                    return SecretKeySpecMode.SM4.getSecretKeySpec(key);
                default:
                    throw Exceptions.unsupported("unsupported generator " + mode + " key");
            }
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }

}
