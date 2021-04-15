package com.orion.test.code;

import com.orion.utils.code.BarCodes;
import org.junit.Test;

import java.awt.*;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/4/14 11:37
 */
public class BarCodeTests {

    @Test
    public void test1() {
        BarCodes c = new BarCodes();
        String s = c.encodeBase64("www");
        System.out.println(s);
        String d = c.decodeBase64(s);
        System.out.println(d);
    }

    @Test
    public void test2() {
        BarCodes c = new BarCodes();
        String s = c.encodeBase64("www", "www");
        System.out.println(s);
        String d = c.decodeBase64(s);
        System.out.println(d);
    }

    @Test
    public void test3() {
        BarCodes c = new BarCodes();
        c.fontColor(Color.BLUE);
        c.width(500);
        String s = c.encodeBase64("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh", "哈哈哈");
        System.out.println(s);
        String d = c.decodeBase64(s);
        System.out.println(d);
    }

}
