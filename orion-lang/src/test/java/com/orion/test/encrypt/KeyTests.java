package com.orion.test.encrypt;

import com.orion.lang.wrapper.Args;
import com.orion.utils.crypto.Keys;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/29 11:48
 */
public class KeyTests {

    public static void main(String[] args) {
        String k1 = Keys.getKey("C:\\Users\\ljh15\\Desktop\\key\\rsa_public.pem");
        String k2 = Keys.getKey("C:\\Users\\ljh15\\Desktop\\key\\rsa_private_pkcs8.pem");
        PublicKey k3 = Keys.getCerPublicKey("C:\\Users\\ljh15\\Desktop\\key\\openssl.cer");
        Args.Two<PublicKey, PrivateKey> keys = Keys.getPfxKeys("C:\\Users\\ljh15\\Desktop\\key\\openssl.pfx", "123456");
        PublicKey k4 = keys.getArg1();
        PrivateKey k5 = keys.getArg2();
        System.out.println("k1 = " + k1);
        System.out.println("k2 = " + k2);
        System.out.println("k3 = " + k3);
        System.out.println("k4 = " + k4);
        System.out.println("k5 = " + k5);

    }

}
