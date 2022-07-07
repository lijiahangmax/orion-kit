package com.orion.test.encrypt;

import com.orion.lang.utils.crypto.RC4;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/9/3 23:02
 */
public class Rc4Tests {

    @Test
    public void test() {
        RC4 r = new RC4("12443");
        String xxx = r.encrypt("xxx");
        System.out.println(xxx);
        System.out.println(r.decrypt(xxx));
    }

}
