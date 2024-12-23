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
package cn.orionsec.kit.test.encrypt;

import cn.orionsec.kit.lang.define.wrapper.Pair;
import cn.orionsec.kit.lang.utils.crypto.Keys;
import cn.orionsec.kit.lang.utils.crypto.RSA;
import cn.orionsec.kit.lang.utils.crypto.enums.CipherAlgorithm;
import org.junit.Ignore;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/29 11:48
 */
public class KeyTests {

    @Test
    @Ignore
    public void test1() {
        String k1 = Keys.getKey("C:\\Users\\ljh15\\Desktop\\key\\rsa_public.pem");
        String k2 = Keys.getKey("C:\\Users\\ljh15\\Desktop\\key\\rsa_private_pkcs8.pem");
        PublicKey k3 = Keys.getCerPublicKey("C:\\Users\\ljh15\\Desktop\\key\\openssl.cer");
        Pair<PublicKey, PrivateKey> keys = Keys.getPfxKeys("C:\\Users\\ljh15\\Desktop\\key\\openssl.pfx", "123456");
        PublicKey k4 = keys.getKey();
        PrivateKey k5 = keys.getValue();
        System.out.println("k1 = " + k1);
        System.out.println("k2 = " + k2);
        System.out.println("k3 = " + k3);
        System.out.println("k4 = " + k4);
        System.out.println("k5 = " + k5);
    }

    @Test
    public void aesKey() {
        SecretKey g1 = Keys.generatorKey("qa", CipherAlgorithm.AES);
        SecretKey g2 = Keys.generatorKey("qa", 128, CipherAlgorithm.AES);
        SecretKey g3 = Keys.generatorKey("qa", 192, CipherAlgorithm.AES);
        SecretKey g4 = Keys.generatorKey("qa", 256, CipherAlgorithm.AES);
        System.out.println("g1 = " + g1);
        System.out.println("g2 = " + g2);
        System.out.println("g3 = " + g3);
        System.out.println("g4 = " + g4);
    }

    @Test
    public void desKey() {
        SecretKey g1 = Keys.generatorKey("qa", CipherAlgorithm.DES);
        SecretKey g2 = Keys.generatorKey("qa", 9000, CipherAlgorithm.DES);
        SecretKey g3 = Keys.generatorKey("qa", 192, CipherAlgorithm.DES);
        SecretKey g4 = Keys.generatorKey("qa", 256, CipherAlgorithm.DES);
        System.out.println("g1 = " + g1);
        System.out.println("g2 = " + g2);
        System.out.println("g3 = " + g3);
        System.out.println("g4 = " + g4);
    }

    @Test
    public void des3Key() {
        SecretKey g1 = Keys.generatorKey("qa", CipherAlgorithm.DES3);
        SecretKey g2 = Keys.generatorKey("qa", 9000, CipherAlgorithm.DES3);
        SecretKey g3 = Keys.generatorKey("qa", 192, CipherAlgorithm.DES3);
        SecretKey g4 = Keys.generatorKey("qa", 256, CipherAlgorithm.DES3);
        System.out.println("g1 = " + g1);
        System.out.println("g2 = " + g2);
        System.out.println("g3 = " + g3);
        System.out.println("g4 = " + g4);
    }

    @Test
    public void rsaKey() {
        System.out.println(RSA.generatorKeys(512));
        System.out.println(RSA.generatorKeys(1024));
    }

}
