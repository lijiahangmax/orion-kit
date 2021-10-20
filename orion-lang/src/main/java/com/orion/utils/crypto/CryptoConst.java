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

    /**
     * AES KEY长度 128 192 256
     */
    public static final int AES_KEY_LENGTH = 128;

    /**
     * DES KEY长度 8
     */
    public static final int DES_KEY_LENGTH = 8;

    /**
     * 3DES KEY长度 8
     */
    public static final int DES3_KEY_LENGTH = 24;

    /**
     * SM4 KEY长度 16
     */
    public static final int SM4_KEY_LENGTH = 16;

    /**
     * RSA KEY长度 512 ~ 16384
     */
    public static final int RSA_KEY_LENGTH = 1024;

    /**
     * AES IV长度 16
     */
    public static final int AES_IV_LENGTH = 16;

    /**
     * DES IV长度 8
     */
    public static final int DES_IV_LENGTH = 8;

    /**
     * 3DES IV长度 8
     */
    public static final int DES3_IV_LENGTH = 8;

    /**
     * SM4 IV长度 16
     */
    public static final int SM4_IV_LENGTH = 16;

    /**
     * AES GCM长度 96 104 112 120 128
     */
    public static final int GCM_SPEC_LENGTH = 128;

    public static final String AES_ALGORITHM = "SHA1PRNG";

    public static final String AES_PROVIDER = "SUN";

    public static final String PKCS12 = "PKCS12";

    public static final String X_509 = "X.509";

    private CryptoConst() {
    }

}
