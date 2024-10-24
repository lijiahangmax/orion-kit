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
package com.orion.test.mutable;

import com.orion.lang.define.mutable.MutableString;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/13 18:26
 */
public class MutableStringTests {

    @Test
    public void s() {
        MutableString e = new MutableString("1");
        System.out.println("----------byte----------");
        System.out.println(e.toByte());
        System.out.println("----------short----------");
        System.out.println(e.toShort());
        System.out.println("----------int----------");
        System.out.println(e.toInteger());
        System.out.println("----------long----------");
        System.out.println(e.toLong());
        System.out.println("----------float----------");
        System.out.println(e.toFloat());
        System.out.println("----------double----------");
        System.out.println(e.toDouble());
        System.out.println("----------boolean----------");
        System.out.println(e.toBoolean());
        System.out.println("----------char----------");
        System.out.println(e.toCharacter());
        System.out.println("--------------------");
        System.out.println(e.get());
        System.out.println(e.isEmpty());
        System.out.println(e.isNotEmpty());
        System.out.println(e.isBlank());
        System.out.println(e.isNotBlank());
        e.set("<1#3{}");
        System.out.println(e.cleanXss());
        System.out.println(e.recodeXss());
        System.out.println(e.urlEncode());
        System.out.println(e.urlDecode());
        System.out.println(e.sign("SHA-1"));
        System.out.println(e.sign("MD5"));
        System.out.println(e.concat("1", "2"));
        System.out.println(e.concatBefore("1", "2"));
        System.out.println(e.format(1));
    }

}
