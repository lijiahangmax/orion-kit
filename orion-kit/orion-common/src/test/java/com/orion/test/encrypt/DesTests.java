package com.orion.test.encrypt;


import com.orion.utils.crypto.DES;
import com.orion.utils.crypto.enums.CipherAlgorithm;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/9/29 11:48
 */
public class DesTests {

    public static void main(String[] args) throws Exception {
        String e = DES.encrypt("123", "a", "1");
        String e1 = DES.encrypt("123", "a");
        String e2 = DES.encrypt("123", "a", "1", CipherAlgorithm.DES3);
        String e3 = DES.encrypt("123", "a", CipherAlgorithm.DES3);
        String d = DES.decrypt(e, "a", "1");
        String d1 = DES.decrypt(e1, "a");
        String d2 = DES.decrypt(e2, "a", "1", CipherAlgorithm.DES3);
        String d3 = DES.decrypt(e3, "a", CipherAlgorithm.DES3);
        System.out.println(e);
        System.out.println(e1);
        System.out.println(e2);
        System.out.println(e3);
        System.out.println(d);
        System.out.println(d1);
        System.out.println(d2);
        System.out.println(d3);
    }

}
