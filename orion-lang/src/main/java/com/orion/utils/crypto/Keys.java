package com.orion.utils.crypto;

import com.orion.constant.Const;
import com.orion.lang.wrapper.Args;
import com.orion.utils.Arrays1;
import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.crypto.enums.CipherAlgorithm;
import com.orion.utils.crypto.enums.SecretKeySpecMode;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import static com.orion.utils.codec.Base64s.decode;
import static com.orion.utils.codec.Base64s.encode;

/**
 * key工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/24 18:47
 */
public class Keys {

    private static final String AES_ALGORITHM = "SHA1PRNG";

    private static final String AES_PROVIDER = "SUN";

    private Keys() {
    }

    // -------------------- CER --------------------

    /**
     * 读取cer公钥文件
     *
     * @param file 文件
     * @return PublicKey
     */
    public static PublicKey getCerPublicKey(File file) {
        return getCerPublicKey(Files1.openInputStreamSafe(file), true);
    }

    /**
     * 读取cer公钥文件
     *
     * @param file 文件
     * @return PublicKey
     */
    public static PublicKey getCerPublicKey(String file) {
        return getCerPublicKey(Files1.openInputStreamSafe(file), true);
    }

    /**
     * 读取cer公钥文件
     *
     * @param in 流
     * @return PublicKey
     */
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
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
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

    // -------------------- FPX --------------------

    /**
     * 读取pfx文件提取公私钥
     *
     * @param file     fpx文件
     * @param password 密码
     * @return PublicKey, PrivateKey
     */
    public static Args.Two<PublicKey, PrivateKey> getPfxKeys(File file, String password) {
        return getPfxKeys(Files1.openInputStreamSafe(file), password, true);
    }

    /**
     * 读取pfx文件提取公私钥
     *
     * @param file     fpx文件
     * @param password 密码
     * @return PublicKey, PrivateKey
     */
    public static Args.Two<PublicKey, PrivateKey> getPfxKeys(String file, String password) {
        return getPfxKeys(Files1.openInputStreamSafe(file), password, true);
    }

    /**
     * 读取PFX私钥文件
     *
     * @param in       in
     * @param password 密码
     * @return PublicKey, PrivateKey
     */
    public static Args.Two<PublicKey, PrivateKey> getPfxKeys(InputStream in, String password) {
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
    public static Args.Two<PublicKey, PrivateKey> getPfxKeys(InputStream in, String password, boolean close) {
        try {
            char[] ps = password == null ? null : password.toCharArray();
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(in, ps);
            Enumeration<String> aliases = ks.aliases();
            String keyAlias = null;
            if (aliases.hasMoreElements()) {
                keyAlias = aliases.nextElement();
            }
            Certificate cert = ks.getCertificate(keyAlias);
            return Args.of(cert.getPublicKey(), (PrivateKey) ks.getKey(keyAlias, ps));
        } catch (Exception e) {
            return null;
        } finally {
            if (close) {
                Streams.close(in);
            }
        }
    }

    /**
     * 加载 KeyStore
     *
     * @param file     file
     * @param password password
     * @return KeyStore
     */
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
            KeyStore ks = KeyStore.getInstance("PKCS12");
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

    /**
     * 获取秘钥文件内容
     *
     * @param file 秘钥文件
     * @return Key
     */
    public static String getKey(File file) {
        return getKey(new InputStreamReader(Files1.openInputStreamSafe(file)), true);
    }

    /**
     * 获取秘钥文件内容
     *
     * @param file 秘钥文件
     * @return Key
     */
    public static String getKey(String file) {
        return getKey(new InputStreamReader(Files1.openInputStreamSafe(file)), true);
    }

    /**
     * 获取秘钥文件内容
     *
     * @param in in
     * @return Key
     */
    public static String getKey(InputStream in) {
        return getKey(new InputStreamReader(in), false);
    }

    /**
     * 获取秘钥文件内容
     *
     * @param reader reader
     * @return Key
     */
    public static String getKey(Reader reader) {
        return getKey(reader, false);
    }

    /**
     * 获取秘钥文件内容
     *
     * @param reader reader
     * @param close  是否关闭流
     * @return Key
     */
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
            return key.toString().replaceAll(Const.LF, Strings.EMPTY).replaceAll(Const.CR, Strings.EMPTY);
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

    /**
     * String -> SecretKey
     *
     * @param key  key
     * @param mode CipherAlgorithm
     * @return SecretKey
     */
    public static SecretKey generatorKey(String key, CipherAlgorithm mode) {
        return generatorKey(Strings.bytes(key), 128, mode);
    }

    /**
     * String -> SecretKey
     *
     * @param key  key
     * @param mode CipherAlgorithm
     * @return SecretKey
     */
    public static SecretKey generatorKey(byte[] key, CipherAlgorithm mode) {
        return generatorKey(key, 128, mode);
    }

    /**
     * String -> SecretKey
     *
     * @param key     key
     * @param keySize AES key 位数  128 192 256
     * @param mode    CipherAlgorithm
     * @return SecretKey
     */
    public static SecretKey generatorKey(String key, int keySize, CipherAlgorithm mode) {
        return generatorKey(Strings.bytes(key), keySize, mode);
    }

    /**
     * String -> SecretKey
     *
     * @param key     key
     * @param keySize key 位数
     *                AES 128 192 256
     *                DES 8
     *                3DES 14 ~ 21
     * @param mode    CipherAlgorithm
     * @return SecretKey
     */
    public static SecretKey generatorKey(byte[] key, int keySize, CipherAlgorithm mode) {
        try {
            switch (mode) {
                case AES:
                    KeyGenerator keyGenerator = KeyGenerator.getInstance(mode.getMode());
                    SecureRandom random = SecureRandom.getInstance(AES_ALGORITHM, AES_PROVIDER);
                    random.setSeed(key);
                    keyGenerator.init(keySize, random);
                    return SecretKeySpecMode.AES.getSecretKeySpec(keyGenerator.generateKey().getEncoded());
                case DES:
                    if (key.length < keySize) {
                        key = Arrays1.resize(key, keySize);
                    }
                    return SecretKeyFactory.getInstance(mode.getMode()).generateSecret(new DESKeySpec(key));
                case DES3:
                    if (key.length < keySize) {
                        key = Arrays1.resize(key, keySize);
                    }
                    return SecretKeyFactory.getInstance(mode.getMode()).generateSecret(new DESedeKeySpec(key));
                default:
                    return null;
            }
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }

}
