package com.orion.test.encrypt;

import com.orion.utils.crypto.AES;

import javax.crypto.SecretKey;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/29 11:48
 */
public class AesTests {

    public static void main(String[] args) {
        e();
    }

    private static void e() {
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

        SecretKey g1 = AES.generatorKey("qa");
        SecretKey g2 = AES.generatorKey("qa", 192);
        System.out.println();
        System.out.println("g1 = " + g1);
        System.out.println("g2 = " + g2);


    }

}
