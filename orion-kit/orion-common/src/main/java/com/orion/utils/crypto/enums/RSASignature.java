package com.orion.utils.crypto.enums;

import com.orion.utils.Exceptions;
import com.orion.utils.Strings;

import java.security.Signature;

/**
 * RSA 签名
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/28 15:07
 */
public enum RSASignature {

    /**
     * MD5
     */
    MD5("MD5withRSA"),

    /**
     * SHA1
     */
    SHA1("SHA1WithRSA"),

    /**
     * SHA224
     */
    SHA224("SHA224WithRSA"),

    /**
     * SHA256
     */
    SHA256("SHA256WithRSA"),

    /**
     * SHA384
     */
    SHA384("SHA384WithRSA"),

    /**
     * SHA512
     */
    SHA512("SHA512WithRSA");

    String model;

    RSASignature(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public Signature getSignature() {
        try {
            return Signature.getInstance(model);
        } catch (Exception e) {
            // impossible
            throw Exceptions.runtime(e);
        }
    }

    public static Signature getSignature(String model) {
        if (Strings.isBlank(model)) {
            return null;
        }
        RSASignature[] values = values();
        for (RSASignature value : values) {
            if (value.getModel().equalsIgnoreCase(model.trim())) {
                return value.getSignature();
            }
        }
        return null;
    }

}
