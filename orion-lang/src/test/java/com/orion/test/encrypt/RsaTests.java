/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package com.orion.test.encrypt;

import com.orion.lang.define.wrapper.Pair;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.crypto.Keys;
import com.orion.lang.utils.crypto.RSA;
import com.orion.lang.utils.crypto.enums.RSASignature;
import org.junit.Test;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/27 17:00
 */
public class RsaTests {

    private static String pub = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQChDzcjw/rWgFwnxunbKp7/4e8w/UmXx2jk6qEEn69t6N2R1i/LmcyDT1xr/T2AHGOiXNQ5V8W4iCaaeNawi7aJaRhtVx1uOH/2U378fscEESEG8XDqll0GCfB1/TjKI2aitVSzXOtRs8kYgGU78f7VmDNgXIlk3gdhnzh+uoEQywIDAQAB";
    private static String pri = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKEPNyPD+taAXCfG6dsqnv/h7zD9SZfHaOTqoQSfr23o3ZHWL8uZzINPXGv9PYAcY6Jc1DlXxbiIJpp41rCLtolpGG1XHW44f/ZTfvx+xwQRIQbxcOqWXQYJ8HX9OMojZqK1VLNc61GzyRiAZTvx/tWYM2BciWTeB2GfOH66gRDLAgMBAAECgYBp4qTvoJKynuT3SbDJY/XwaEtmu768SF9P0GlXrtwYuDWjAVue0VhBI9WxMWZTaVafkcP8hxX4QZqPh84td0zjcq3jDLOegAFJkIorGzq5FyK7ydBoU1TLjFV459c8dTZMTu+LgsOTD11/V/Jr4NJxIudoMBQ3c4cHmOoYv4uzkQJBANR+7Fc3e6oZgqTOesqPSPqljbsdF9E4x4eDFuOecCkJDvVLOOoAzvtHfAiUp+H3fk4hXRpALiNBEHiIdhIuX2UCQQDCCHiPHFd4gC58yyCM6Leqkmoa+6YpfRb3oxykLBXcWx7DtbX+ayKy5OQmnkEG+MW8XB8wAdiUl0/tb6cQFaRvAkBhvP94Hk0DMDinFVHlWYJ3xy4pongSA8vCyMj+aSGtvjzjFnZXK4gIjBjA2Z9ekDfIOBBawqp2DLdGuX2VXz8BAkByMuIh+KBSv76cnEDwLhfLQJlKgEnvqTvXTB0TUw8avlaBAXW34/5sI+NUB1hmbgyTK/T/IFcEPXpBWLGO+e3pAkAGWLpnH0ZhFae7oAqkMAd3xCNY6ec180tAe57hZ6kS+SYLKwb4gGzYaCxc22vMtYksXHtUeamo1NMLzI2ZfUoX";
    private static File pubFile = new File("C:\\Users\\ljh15\\Desktop\\key\\rsa_public.pem");
    private static File priFile = new File("C:\\Users\\ljh15\\Desktop\\key\\rsa_private_pkcs8.pem");
    private static RSAPublicKey publicKey = RSA.getPublicKey(Strings.bytes(pub));
    private static RSAPrivateKey privateKey = RSA.getPrivateKey(Strings.bytes(pri));
    private static RSAPublicKey publicKey1 = RSA.getPublicKey(pubFile);
    private static RSAPrivateKey privateKey1 = RSA.getPrivateKey(priFile);
    private static Pair<RSAPublicKey, RSAPrivateKey> keys = RSA.generatorKeys();
    private static Pair<RSAPublicKey, RSAPrivateKey> keys1 = RSA.generatorKeys(512);
    private static Pair<PublicKey, PrivateKey> keys2 = Keys.getPfxKeys(new File("C:\\Users\\ljh15\\Desktop\\key\\openssl.pfx"), "123456");

    @Test
    public void ed() {
        String d = "123";
        String e = RSA.encrypt(d, pub);
        String e1 = "J9Idok39JBzp+y8jHTxoTBrHwQ4lQFFGklTwMRMQKvnLyRvRZmmraDogzqKgIPihdveMdxauPRh+JitmTHdSBEsVwl2if5gjtL8Dndpqp9CiwzJMTmeq0ncGcsqHSMg3E+s/d4ZKctY/LL4O5u6Btu2KYF6CtVuegQS/Ohe6kjE=";
        String sign1 = RSA.sign(d, pri, RSASignature.SHA1);
        String sign2 = RSA.sign(d, pri, RSASignature.SHA224);
        String sign3 = RSA.sign(d, pri, RSASignature.SHA256);
        String sign4 = RSA.sign(d, pri, RSASignature.SHA384);
        String sign5 = RSA.sign(d, pri, RSASignature.SHA512);
        String sign6 = RSA.sign(d, pri, RSASignature.MD5);
        boolean v1 = RSA.verify(d, pub, sign1, RSASignature.SHA1);
        boolean v2 = RSA.verify(d, pub, sign2, RSASignature.SHA224);
        boolean v3 = RSA.verify(d, pub, sign3, RSASignature.SHA256);
        boolean v4 = RSA.verify(d, pub, sign4, RSASignature.SHA384);
        boolean v5 = RSA.verify(d, pub, sign5, RSASignature.SHA512);
        boolean v6 = RSA.verify(d, pub, sign6, RSASignature.MD5);

        System.out.println("DE: " + RSA.decrypt(e, pri));
        System.out.println("DE: " + RSA.decrypt(e1, pri));
        System.out.println("EN: " + e);
        System.out.println("S1: " + sign1);
        System.out.println("S2: " + sign2);
        System.out.println("S3: " + sign3);
        System.out.println("S4: " + sign4);
        System.out.println("S5: " + sign5);
        System.out.println("S6: " + sign6);
        System.out.println("V1: " + v1);
        System.out.println("V2: " + v2);
        System.out.println("V3: " + v3);
        System.out.println("V4: " + v4);
        System.out.println("V5: " + v5);
        System.out.println("V6: " + v6);
    }

    @Test
    public void pfx() {
        String d = "123";
        String e = RSA.encrypt(d, keys2.getKey());
        String sign1 = RSA.sign(d, keys2.getValue(), RSASignature.MD5);
        boolean v1 = RSA.verify(d, keys2.getKey(), sign1, RSASignature.MD5);
        System.out.println();
        System.out.println(d);
        System.out.println(e);
        System.out.println(sign1);
        System.out.println(v1);
    }

    @Test
    public void key() {
        System.out.println(publicKey);
        System.out.println(privateKey);
        System.out.println(RSA.getPublicKey(privateKey));
        System.out.println(publicKey1);
        System.out.println(privateKey1);
        System.out.println(keys.getKey());
        System.out.println(keys.getValue());
        System.out.println(keys1.getKey());
        System.out.println(keys1.getValue());
        System.out.println("pfx: " + keys2.getKey());
        System.out.println("pfx: " + keys2.getValue());
        System.out.println(Keys.getPublicKey(publicKey));
        System.out.println(Keys.getPrivateKey(privateKey));
        System.out.println(Keys.getPublicKey(publicKey1));
        System.out.println(Keys.getPrivateKey(privateKey1));
        System.out.println();
    }

}
