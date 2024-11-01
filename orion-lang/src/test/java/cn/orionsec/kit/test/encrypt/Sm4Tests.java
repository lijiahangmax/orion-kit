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
package cn.orionsec.kit.test.encrypt;

import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.crypto.Keys;
import cn.orionsec.kit.lang.utils.crypto.SM4;
import cn.orionsec.kit.lang.utils.crypto.enums.CipherAlgorithm;
import cn.orionsec.kit.lang.utils.random.Randoms;
import org.junit.Test;

import javax.crypto.SecretKey;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/29 11:48
 */
public class Sm4Tests {

    private String s = "123";
    private String k1 = "345";

    @Test
    public void ecb() {
        String e1 = SM4.encrypt(s, k1);
        String d1 = SM4.decrypt(e1, k1);
        System.out.println("e1 = " + e1);
        System.out.println("d1 = " + d1);
    }

    @Test
    public void cbc() {
        String s1 = SM4.encrypt(s, k1, "1234567812345678");
        String d1 = SM4.decrypt(s1, k1, "1234567812345678");
        System.out.println("s1 = " + s1);
        System.out.println("d1 = " + d1);
    }

    @Test
    public void test1() {
        SecretKey key = Keys.generatorKey("key", CipherAlgorithm.SM4);
        for (int i = 0; i < 1000; i++) {
            String s = Strings.randomChars(Randoms.randomInt(5, 20));
            String enc = SM4.encrypt(s, key);
            System.out.println(enc);
            String dec = SM4.decrypt(enc, key);
            System.out.println(dec);
            Valid.isTrue(s.equals(dec));
        }
    }

    @Test
    public void testSM4() {
        for (int i = 0; i < 5000; i++) {
            String val = Strings.randomChars(Randoms.randomInt(30));
            String key = Randoms.randomAscii(Randoms.randomInt(30));
            String en = SM4.encrypt(val, key);
            String de = SM4.decrypt(en, key);
            // System.out.println(val);
            // System.out.println(key);
            // System.out.println(en);
            // System.out.println(de);
            // System.out.println();
            Valid.isTrue(val.equals(de));
        }
    }

}
