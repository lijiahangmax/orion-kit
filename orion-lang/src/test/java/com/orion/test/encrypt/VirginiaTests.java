package com.orion.test.encrypt;

import com.orion.lang.utils.crypto.Virginia;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/3 23:07
 */
public class VirginiaTests {

    @Test
    public void test1() {
        String e = Virginia.encrypt("123!@#//\\", "key");
        System.out.println(e);
        String d = Virginia.decrypt(e, "key");
        System.out.println(d);
    }

}
