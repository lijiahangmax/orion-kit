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
package cn.orionsec.kit.test.code;

import cn.orionsec.kit.lang.utils.code.QRCodes;
import org.junit.Test;

import java.awt.*;
import java.io.File;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/4/14 10:31
 */
public class QRCodeTests {

    @Test
    public void test1() {
        QRCodes c = new QRCodes();
        String s = c.encodeBase64("www");
        System.out.println(s);
        String d = c.decodeBase64(s);
        System.out.println(d);
    }

    @Test
    public void test2() {
        QRCodes c = new QRCodes();
        String s = c.encodeBase64("www", "www");
        System.out.println(s);
        String d = c.decodeBase64(s);
        System.out.println(d);
    }

    @Test
    public void test3() {
        QRCodes c = new QRCodes();
        c.logo(new File("C:\\Users\\ljh15\\Pictures\\帽子\\1.jpg"));
        c.logoCompress();
        String s = c.encodeBase64("www");
        System.out.println(s);
        String d = c.decodeBase64(s);
        System.out.println(d);
    }

    @Test
    public void test4() {
        QRCodes c = new QRCodes();
        c.logo(new File("C:\\Users\\ljh15\\Pictures\\帽子\\1.jpg"));
        c.logoCompress();
        c.fontColor(Color.RED);
        String s = c.encodeBase64("哦哦哦哦哦!", "哦哦哦哦哦!");
        System.out.println(s);
        String d = c.decodeBase64(s);
        System.out.println(d);
    }

}
