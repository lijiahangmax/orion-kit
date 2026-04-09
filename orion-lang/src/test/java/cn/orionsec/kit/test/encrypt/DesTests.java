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

import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.crypto.CryptoConst;
import cn.orionsec.kit.lang.utils.crypto.DES;
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

    private final String key = Randoms.randomAscii(CryptoConst.DES_KEY_LENGTH_BITS / 8);
    private final String iv = Randoms.randomAscii(CryptoConst.DES_IV_LENGTH_BITS / 8);

    @Test
    public void des() {
        String e = DES.encrypt("123", key, iv);
        String e1 = DES.encrypt("123", key);
        System.out.println(e);
        System.out.println(e1);
        String d = DES.decrypt(e, key, iv);
        String d1 = DES.decrypt(e1, key);
        System.out.println(d);
        System.out.println(d1);
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
            Assert.isTrue(s.equals(dec));
        }
    }


    @Test
    public void testDes() {
        for (int i = 0; i < 5000; i++) {
            String val = Strings.randomChars(Randoms.randomInt(30));
            String key = Randoms.randomAscii(CryptoConst.DES_IV_LENGTH_BITS / 8);
            String en = DES.encrypt(val, key);
            String de = DES.decrypt(en, key);
            // System.out.println(val);
            // System.out.println(key);
            // System.out.println(en);
            // System.out.println(de);
            // System.out.println();
            Assert.isTrue(val.equals(de));
        }
    }

}
