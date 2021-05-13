package com.orion.test.function;

import com.orion.function.select.Branches;
import com.orion.function.select.Selector;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/5/13 16:52
 */
public class SelectorTest {

    @Test
    public void test1() {
        String va = Selector.<Integer, String>of(4)
                .test(Branches.<Integer, String>eq(1).then("一"))
                .test(Branches.<Integer, String>eq(2).then("二"))
                .test(Branches.<Integer, String>eq(3).then("三"))
                .or("def");
        System.out.println(va);
    }

    @Test
    public void test2() {
        String va = Selector.<Integer, String>of(2)
                .test(Branches.<Integer, String>in(1, 4).then("一 or 四"))
                .test(Branches.<Integer, String>compared(2).then(s -> (s + 10) + ""))
                .test(Branches.<Integer, String>eq(3).then("三"))
                .or("def");
        System.out.println(va);
    }

}
