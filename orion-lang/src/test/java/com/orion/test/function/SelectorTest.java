package com.orion.test.function;

import com.orion.lang.function.select.Branches;
import com.orion.lang.function.select.Selector;
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
                .test(Branches.eq(1).then("一"))
                .test(Branches.eq(2).then("二"))
                .test(Branches.eq(3).then("三"))
                .orElse("def");
        System.out.println(va);
    }

    @Test
    public void test2() {
        String va = Selector.<Integer, String>of(6)
                .test(Branches.in(1, 4).then("一 or 四"))
                .test(Branches.compared(2).then(s -> (s + 10) + ""))
                .test(Branches.eq(3).then("三"))
                .test(Branches.eq(5).then(() -> "555"))
                .or(Object::toString);
        System.out.println(va);
    }

}
