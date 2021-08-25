package com.orion.test.encrypt;

import com.orion.utils.Strings;
import com.orion.utils.Valid;
import com.orion.utils.crypto.AES;
import com.orion.utils.crypto.Keys;
import com.orion.utils.crypto.enums.CipherAlgorithm;
import com.orion.utils.random.Randoms;
import org.junit.Test;

import javax.crypto.SecretKey;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/29 11:48
 */
public class AesTests {

    @Test
    public void e() {
        String s = "123";
        String k1 = "123";
        String k2 = "345";

        String e1 = AES.encrypt(s, k1);
        String e2 = AES.encrypt(s, k2);
        System.out.println("e1 = " + e1);
        System.out.println("e2 = " + e2);

        String d1 = AES.decrypt(e1, k1);
        String d2 = AES.decrypt(e1, k2);
        String d3 = AES.decrypt(e2, k1);
        String d4 = AES.decrypt(e2, k2);
        System.out.println();
        System.out.println("d1 = " + d1);
        System.out.println("d2 = " + d2);
        System.out.println("d3 = " + d3);
        System.out.println("d4 = " + d4);

        String s3 = AES.encrypt(s, k1, "xx");
        String s4 = AES.encrypt(s, k2, "xx");
        System.out.println();
        System.out.println("s3 = " + s3);
        System.out.println("s4 = " + s4);

        String d5 = AES.decrypt(s3, k1, "xx");
        String d6 = AES.decrypt(s3, k2, "xx");
        String d7 = AES.decrypt(s4, k1, "xx");
        String d8 = AES.decrypt(s4, k2, "xx");
        System.out.println();
        System.out.println("d5 = " + d5);
        System.out.println("d6 = " + d6);
        System.out.println("d7 = " + d7);
        System.out.println("d8 = " + d8);

        SecretKey g1 = Keys.generatorKey("qa", CipherAlgorithm.AES);
        SecretKey g2 = Keys.generatorKey("qa", 192, CipherAlgorithm.AES);
        System.out.println();
        System.out.println("g1 = " + g1);
        System.out.println("g2 = " + g2);
    }

    @Test
    public void test1() {
        SecretKey key = Keys.generatorKey("key", CipherAlgorithm.AES);
        for (int i = 0; i < 1000; i++) {
            String s = Strings.randomChars(Randoms.randomInt(5, 20));
            String enc = AES.encrypt(s, key);
            System.out.println(enc);
            String dec = AES.decrypt(enc, key);
            System.out.println(dec);
            Valid.isTrue(s.equals(dec));
        }
    }

}
