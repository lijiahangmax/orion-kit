package com.orion.test.checksum;

import com.orion.lang.utils.check.CRC16;
import com.orion.lang.utils.check.CRC8;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/3 22:32
 */
public class CheckSumTests {

    @Test
    public void test1() {
        CRC8 c = new CRC8(100123);
        c.update("这是一串文字".getBytes());
        System.out.println(c.getValue());
    }

    @Test
    public void test2() {
        CRC16 c = new CRC16();
        c.update("这是一串文字".getBytes());
        System.out.println(c.getValue());
    }

}
