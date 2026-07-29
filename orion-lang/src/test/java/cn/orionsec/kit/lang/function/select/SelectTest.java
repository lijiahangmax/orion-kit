package cn.orionsec.kit.lang.function.select;

import org.junit.Assert;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * select 子包测试
 * 覆盖: Branch, Branches, Selector
 */
public class SelectTest {

    // ==================== Branch ====================

    @Test
    public void testBranchOf() {
        Branch<Integer, String> branch = Branch.of(i -> i > 0, Object::toString);
        Assert.assertNotNull(branch.tester());
        Assert.assertNotNull(branch.factory());
        Assert.assertTrue(branch.tester().test(1));
        Assert.assertFalse(branch.tester().test(-1));
        Assert.assertEquals("5", branch.factory().apply(5));
    }

    // ==================== Branches ====================

    @Test
    public void testBranchesWhenThen() {
        Branch<Integer, String> branch = Branches.<Integer>when(i -> i > 10).then(i -> "big:" + i);
        Assert.assertTrue(branch.tester().test(11));
        Assert.assertFalse(branch.tester().test(5));
        Assert.assertEquals("big:20", branch.factory().apply(20));
    }

    @Test
    public void testBranchesWhenThenSupplier() {
        Branch<Integer, String> branch = Branches.<Integer>when(i -> i == 1).then(() -> "one");
        Assert.assertTrue(branch.tester().test(1));
        Assert.assertEquals("one", branch.factory().apply(1));
    }

    @Test
    public void testBranchesWhenThenValue() {
        Branch<Integer, String> branch = Branches.<Integer>when(i -> i == 2).then("two");
        Assert.assertTrue(branch.tester().test(2));
        Assert.assertEquals("two", branch.factory().apply(2));
    }

    @Test
    public void testBranchesEq() {
        Branch<Integer, String> branch = Branches.<Integer>eq(5).then("five");
        Assert.assertTrue(branch.tester().test(5));
        Assert.assertFalse(branch.tester().test(3));
        Assert.assertEquals("five", branch.factory().apply(5));
    }

    @Test
    public void testBranchesCompared() {
        Branch<Integer, String> branch = Branches.<Integer>compared(10).then("ten");
        Assert.assertTrue(branch.tester().test(10));
        Assert.assertFalse(branch.tester().test(9));
    }

    @Test
    public void testBranchesIn() {
        Branch<Integer, String> branch = Branches.<Integer>in(1, 2, 3).then("small");
        Assert.assertTrue(branch.tester().test(1));
        Assert.assertTrue(branch.tester().test(2));
        Assert.assertTrue(branch.tester().test(3));
        Assert.assertFalse(branch.tester().test(4));
    }

    // ==================== Selector ====================

    @Test
    public void testSelectorOfHit() {
        String result = Selector.<Integer, String>of(2)
                .test(Branches.eq(1).then("one"))
                .test(Branches.eq(2).then("two"))
                .test(Branches.eq(3).then("three"))
                .get();
        Assert.assertEquals("two", result);
    }

    @Test
    public void testSelectorOfSupplier() {
        String result = Selector.<Integer, String>of(() -> 3)
                .test(Branches.eq(1).then("one"))
                .test(Branches.eq(3).then("three"))
                .get();
        Assert.assertEquals("three", result);
    }

    @Test(expected = NoSuchElementException.class)
    public void testSelectorGetMissed() {
        Selector.<Integer, String>of(99)
                .test(Branches.eq(1).then("one"))
                .get();
    }

    @Test
    public void testSelectorOrElse() {
        String result = Selector.<Integer, String>of(99)
                .test(Branches.eq(1).then("one"))
                .orElse("default");
        Assert.assertEquals("default", result);
    }

    @Test
    public void testSelectorOrElseHit() {
        String result = Selector.<Integer, String>of(1)
                .test(Branches.eq(1).then("one"))
                .orElse("default");
        Assert.assertEquals("one", result);
    }

    @Test
    public void testSelectorOr() {
        String result = Selector.<Integer, String>of(42)
                .test(Branches.eq(1).then("one"))
                .or(p -> "val:" + p);
        Assert.assertEquals("val:42", result);
    }

    @Test
    public void testSelectorOrHit() {
        String result = Selector.<Integer, String>of(1)
                .test(Branches.eq(1).then("one"))
                .or(p -> "val:" + p);
        Assert.assertEquals("one", result);
    }

    @Test
    public void testSelectorOrGet() {
        String result = Selector.<Integer, String>of(50)
                .test(Branches.eq(1).then("one"))
                .orGet(() -> "fallback");
        Assert.assertEquals("fallback", result);
    }

    @Test
    public void testSelectorOrGetHit() {
        String result = Selector.<Integer, String>of(1)
                .test(Branches.eq(1).then("one"))
                .orGet(() -> "fallback");
        Assert.assertEquals("one", result);
    }

    @Test(expected = IllegalStateException.class)
    public void testSelectorOrThrow() {
        Selector.<Integer, String>of(99)
                .test(Branches.eq(1).then("one"))
                .orThrow(IllegalStateException::new);
    }

    @Test
    public void testSelectorOrThrowHit() {
        String result = Selector.<Integer, String>of(1)
                .test(Branches.eq(1).then("one"))
                .orThrow(IllegalStateException::new);
        Assert.assertEquals("one", result);
    }

    @Test
    public void testSelectorIsSelected() {
        Selector<Integer, String> selector = Selector.<Integer, String>of(1)
                .test(Branches.eq(1).then("one"));
        Assert.assertTrue(selector.isSelected());
    }

    @Test
    public void testSelectorIsNotSelected() {
        Selector<Integer, String> selector = Selector.<Integer, String>of(99)
                .test(Branches.eq(1).then("one"));
        Assert.assertFalse(selector.isSelected());
    }

    @Test
    public void testSelectorIfPresent() {
        AtomicReference<String> ref = new AtomicReference<>();
        Selector.<Integer, String>of(1)
                .test(Branches.eq(1).then("one"))
                .ifPresent(ref::set);
        Assert.assertEquals("one", ref.get());
    }

    @Test
    public void testSelectorIfPresentNotSelected() {
        AtomicReference<String> ref = new AtomicReference<>();
        Selector.<Integer, String>of(99)
                .test(Branches.eq(1).then("one"))
                .ifPresent(ref::set);
        Assert.assertNull(ref.get());
    }

    @Test
    public void testSelectorFirstHitWins() {
        // 验证第一个匹配的分支被使用
        String result = Selector.<Integer, String>of(1)
                .test(Branches.eq(1).then("first"))
                .test(Branches.eq(1).then("second"))
                .get();
        Assert.assertEquals("first", result);
    }

    @Test
    public void testSelectorWithIn() {
        String result = Selector.<Integer, String>of(3)
                .test(Branches.in(1, 2).then("group1"))
                .test(Branches.in(3, 4).then("group2"))
                .get();
        Assert.assertEquals("group2", result);
    }

    @Test
    public void testSelectorWithCompared() {
        String result = Selector.<Integer, String>of(10)
                .test(Branches.<Integer>compared(10).then("ten"))
                .get();
        Assert.assertEquals("ten", result);
    }

}
