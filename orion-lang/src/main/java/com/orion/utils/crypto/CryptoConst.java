package com.orion.utils.crypto;

/**
 * 加密常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/23 10:55
 */
public class CryptoConst {

    public static final String RSA = "RSA";

    public static final int AES_KEY_LENGTH = 128;

    public static final int DES_KEY_LENGTH = 8;

    public static final int DES3_KEY_LENGTH = 24;

    public static final int RSA_KEY_LENGTH = 1024;

    public static final int AES_IV_LENGTH = 16;

    public static final int DES_IV_LENGTH = 8;

    public static final int DES3_IV_LENGTH = 8;

    public static final int GCM_SPEC_LENGTH = 128;

    public static final String AES_ALGORITHM = "SHA1PRNG";

    public static final String AES_PROVIDER = "SUN";

    public static final String PKCS12 = "PKCS12";

    public static final String X_509 = "X.509";

    private CryptoConst() {
    }

}
