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

import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.crypto.DES;
import cn.orionsec.kit.lang.utils.crypto.DES3;
import cn.orionsec.kit.lang.utils.crypto.Keys;
import cn.orionsec.kit.lang.utils.crypto.enums.CipherAlgorithm;
import cn.orionsec.kit.lang.utils.random.Randoms;
import org.junit.Test;

import javax.crypto.SecretKey;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/29 11:48
 */
public class DesTests {

    @Test
    public void des() {
        String e = DES.encrypt("123", "a", "12345678");
        String e1 = DES.encrypt("123", "a");
        System.out.println(e);
        System.out.println(e1);
        String d = DES.decrypt(e, "a", "12345678");
        String d1 = DES.decrypt(e1, "a");
        System.out.println(d);
        System.out.println(d1);
    }

    @Test
    public void des3() {
        String e2 = DES3.encrypt("123", "a", "12345678");
        String e3 = DES3.encrypt("123", "a");
        System.out.println(e2);
        System.out.println(e3);
        String d2 = DES3.decrypt(e2, "a", "12345678");
        String d3 = DES3.decrypt(e3, "a");
        System.out.println(d2);
        System.out.println(d3);
    }

    @Test
    public void test1() {
        SecretKey key = Keys.generatorKey("key", CipherAlgorithm.DES);
        for (int i = 0; i < 1000; i++) {
            String s = Strings.randomChars(Randoms.randomInt(5, 20));
            String enc = DES.encrypt(s, key);
            System.out.println(enc);
            String dec = DES.decrypt(enc, key);
            System.out.println(dec);
            Valid.isTrue(s.equals(dec));
        }
    }

    @Test
    public void test2() {
        SecretKey key = Keys.generatorKey("key", CipherAlgorithm.DES3);
        for (int i = 0; i < 1000; i++) {
            String s = Strings.randomChars(Randoms.randomInt(5, 20));
            String enc = DES3.encrypt(s, key);
            System.out.println(enc);
            String dec = DES3.decrypt(enc, key);
            System.out.println(dec);
            Valid.isTrue(s.equals(dec));
        }
    }

    @Test
    public void testDes() {
        for (int i = 0; i < 5000; i++) {
            String val = Strings.randomChars(Randoms.randomInt(30));
            String key = Randoms.randomAscii(Randoms.randomInt(30));
            String en = DES.encrypt(val, key);
            String de = DES.decrypt(en, key);
            // System.out.println(val);
            // System.out.println(key);
            // System.out.println(en);
            // System.out.println(de);
            // System.out.println();
            Valid.isTrue(val.equals(de));
        }
    }

    @Test
    public void test3Des() {
        for (int i = 0; i < 5000; i++) {
            String val = Strings.randomChars(Randoms.randomInt(30));
            String key = Randoms.randomAscii(Randoms.randomInt(30));
            String en = DES3.encrypt(val, key);
            String de = DES3.decrypt(en, key);
            // System.out.println(val);
            // System.out.println(key);
            // System.out.println(en);
            // System.out.println(de);
            // System.out.println();
            Valid.isTrue(val.equals(de));
        }
    }

}
