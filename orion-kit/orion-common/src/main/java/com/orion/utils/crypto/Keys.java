package com.orion.utils.crypto;

import com.orion.lang.wrapper.Args;
import com.orion.utils.Exceptions;
import com.orion.utils.io.Files1;
import com.orion.utils.io.Streams;

import javax.crypto.SecretKey;
import java.io.*;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import static com.orion.utils.codec.Base64s.encode;

/**
 * key工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/24 18:47
 */
public class Keys {

    private Keys() {
    }

    // ------------------ CER ------------------

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
    private static PublicKey getCerPublicKey(InputStream in, boolean close) {
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

    // ------------------ FPX ------------------

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
    private static Args.Two<PublicKey, PrivateKey> getPfxKeys(InputStream in, String password, boolean close) {
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

    // ------------------ KEY ------------------

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
    private static String getKey(Reader reader, boolean close) {
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
            return key.toString().replaceAll("\n", "").replaceAll("\r", "");
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        } finally {
            if (close) {
                Streams.close(reader);
            }
        }
    }

}
