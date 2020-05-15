package com.orion.test;

import com.orion.lang.wrapper.Arg;
import com.orion.utils.Encrypts;
import com.orion.utils.Streams;
import org.junit.Test;

import java.io.FileInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author ljh15
 * @version 1.0.0
 * @date 2020/5/14 13:44
 */
public class EncryptTests {

    @Test
    public void hashEncrypt() {
        String s = "阿苏诋毁";
        System.out.println(Encrypts.sha1(s));
        System.out.println(Encrypts.sha224(s));
        System.out.println(Encrypts.sha256(s));
        System.out.println(Encrypts.sha384(s));
        System.out.println(Encrypts.sha512(s));
        System.out.println(Encrypts.md5(s));
        System.out.println(Encrypts.md5(s, "asd"));
        System.out.println(Encrypts.md5(s, "asd", 2));
    }

    @Test
    public void base64() throws Exception {
        System.out.println(Encrypts.base64Encode("奥斯卡经典1"));
        System.out.println(Encrypts.img64Encode(Streams.toByteArray(new FileInputStream("C:\\Users\\ljh15\\Desktop\\tmp\\1.jpg"))));
        System.out.println(Encrypts.base64Decode(Encrypts.base64Encode("奥斯卡经典1")));
    }

    @Test
    public void aes() {
        String m = Encrypts.aesEncrypt("哈哈哈", "666", true);
        System.out.println(m);
        System.out.println(Encrypts.aesDecrypt(m, "666", true));
    }

    @Test
    public void des() {
        String m = Encrypts.desEncrypt("哈哈哈", "123123123123123123123123123", true, "11111233");
        System.out.println(m);
        System.out.println(Encrypts.desEncrypt(m, "123123123123123123123123123", true, "11111233"));
    }

    @Test
    public void rsa() {
        Arg.Two<PublicKey, PrivateKey> keys = Encrypts.generatorRSAKey();
        String e = Encrypts.rsaEncrypt("哈哈哈", keys.getArg1());
        System.out.println(e);
        System.out.println(Encrypts.rsaDecrypt(e, keys.getArg2()));
        String v = Encrypts.rsaSign("哈哈哈", keys.getArg2());
        System.out.println(v);
        System.out.println(Encrypts.rsaVerify("哈哈哈", keys.getArg1(), v));
    }

}
