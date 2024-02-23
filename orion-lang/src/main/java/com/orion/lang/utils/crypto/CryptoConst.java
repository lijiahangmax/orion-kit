package com.orion.lang.utils.crypto;

/**
 * 加密常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/23 10:55
 */
public interface CryptoConst {

    String RSA = "RSA";

    /**
     * AES KEY长度 128 192 256
     */
    int AES_KEY_LENGTH = 128;

    /**
     * DES KEY长度 8
     */
    int DES_KEY_LENGTH = 8;

    /**
     * 3DES KEY长度 8
     */
    int DES3_KEY_LENGTH = 24;

    /**
     * SM4 KEY长度 16
     */
    int SM4_KEY_LENGTH = 16;

    /**
     * RSA KEY长度 512 ~ 16384
     */
    int RSA_KEY_LENGTH = 1024;

    /**
     * AES IV长度 16
     */
    int AES_IV_LENGTH = 16;

    /**
     * DES IV长度 8
     */
    int DES_IV_LENGTH = 8;

    /**
     * 3DES IV长度 8
     */
    int DES3_IV_LENGTH = 8;

    /**
     * SM4 IV长度 16
     */
    int SM4_IV_LENGTH = 16;

    /**
     * AES GCM长度 96 104 112 120 128
     */
    int GCM_SPEC_LENGTH = 128;

    String AES_ALGORITHM = "SHA1PRNG";

    String AES_PROVIDER = "SUN";

    String PKCS12 = "PKCS12";

    String X_509 = "X.509";

}
