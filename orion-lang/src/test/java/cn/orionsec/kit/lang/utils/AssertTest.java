package cn.orionsec.kit.lang.utils;

import cn.orionsec.kit.lang.exception.argument.InvalidArgumentException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class AssertTest {

    @Test
    public void testNotNullPass() {
        Assert.notNull("hello");
        Assert.notNull("hello", "msg");
    }

    @Test(expected = InvalidArgumentException.class)
    public void testNotNullFail() {
        Assert.notNull(null, "should not be null");
    }

    @Test
    public void testIsTruePass() {
        Assert.isTrue(true);
        Assert.isTrue(true, "msg");
    }

    @Test(expected = InvalidArgumentException.class)
    public void testIsTrueFail() {
        Assert.isTrue(false, "should be true");
    }

    @Test
    public void testNotEmptyStringPass() {
        Assert.notEmpty("abc");
        Assert.notEmpty("abc", "msg");
    }

    @Test(expected = InvalidArgumentException.class)
    public void testNotEmptyStringFail() {
        Assert.notEmpty("", "should not be empty");
    }

    @Test
    public void testNotEmptyCollectionPass() {
        Assert.notEmpty(Arrays.asList("a", "b"));
    }

    @Test(expected = InvalidArgumentException.class)
    public void testNotEmptyCollectionFail() {
        Assert.notEmpty(new ArrayList<>(), "should not be empty");
    }

    @Test
    public void testEqPass() {
        Assert.eq("a", "a");
    }

    @Test(expected = InvalidArgumentException.class)
    public void testEqFail() {
        Assert.eq("a", "b");
    }

    @Test
    public void testGtPass() {
        Assert.gt(5, 3);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testGtFail() {
        Assert.gt(3, 5);
    }
}
