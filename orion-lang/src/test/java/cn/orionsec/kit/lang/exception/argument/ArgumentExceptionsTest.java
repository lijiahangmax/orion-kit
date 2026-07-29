package cn.orionsec.kit.lang.exception.argument;

import cn.orionsec.kit.lang.define.wrapper.HttpWrapper;
import cn.orionsec.kit.lang.define.wrapper.RpcWrapper;
import org.junit.Assert;
import org.junit.Test;

/**
 * argument 包异常类单元测试
 */
public class ArgumentExceptionsTest {

    private static final String MSG = "test message";
    private static final Throwable CAUSE = new RuntimeException("cause");

    // -------------------- InvalidArgumentException --------------------

    @Test
    public void testInvalidArgumentException() {
        InvalidArgumentException e1 = new InvalidArgumentException();
        Assert.assertNull(e1.getMessage());

        InvalidArgumentException e2 = new InvalidArgumentException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        InvalidArgumentException e3 = new InvalidArgumentException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        InvalidArgumentException e4 = new InvalidArgumentException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- NullArgumentException --------------------

    @Test
    public void testNullArgumentException() {
        NullArgumentException e1 = new NullArgumentException();
        Assert.assertNull(e1.getMessage());

        NullArgumentException e2 = new NullArgumentException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        NullArgumentException e3 = new NullArgumentException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        NullArgumentException e4 = new NullArgumentException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- UnMatchArgumentException --------------------

    @Test
    public void testUnMatchArgumentException() {
        UnMatchArgumentException e1 = new UnMatchArgumentException();
        Assert.assertNull(e1.getMessage());

        UnMatchArgumentException e2 = new UnMatchArgumentException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        UnMatchArgumentException e3 = new UnMatchArgumentException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        UnMatchArgumentException e4 = new UnMatchArgumentException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- CodeArgumentException --------------------

    @Test
    public void testCodeArgumentExceptionStandard() {
        CodeArgumentException e1 = new CodeArgumentException();
        Assert.assertNull(e1.getMessage());

        CodeArgumentException e2 = new CodeArgumentException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        CodeArgumentException e3 = new CodeArgumentException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        CodeArgumentException e4 = new CodeArgumentException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    @Test
    public void testCodeArgumentExceptionWithCode() {
        CodeArgumentException e1 = new CodeArgumentException(100);
        Assert.assertEquals(100, e1.getCode());

        CodeArgumentException e2 = new CodeArgumentException(200, MSG);
        Assert.assertEquals(200, e2.getCode());
        Assert.assertEquals(MSG, e2.getMessage());

        CodeArgumentException e3 = new CodeArgumentException(300, MSG, CAUSE);
        Assert.assertEquals(300, e3.getCode());
        Assert.assertEquals(MSG, e3.getMessage());
        Assert.assertEquals(CAUSE, e3.getCause());

        CodeArgumentException e4 = new CodeArgumentException(400, CAUSE);
        Assert.assertEquals(400, e4.getCode());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    @Test
    public void testCodeArgumentExceptionSetCode() {
        CodeArgumentException e = new CodeArgumentException();
        e.setCode(999);
        Assert.assertEquals(999, e.getCode());
    }

    // -------------------- IndexArgumentException --------------------

    @Test
    public void testIndexArgumentExceptionStandard() {
        IndexArgumentException e1 = new IndexArgumentException();
        Assert.assertNull(e1.getMessage());

        IndexArgumentException e2 = new IndexArgumentException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        IndexArgumentException e3 = new IndexArgumentException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        IndexArgumentException e4 = new IndexArgumentException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    @Test
    public void testIndexArgumentExceptionWithIndex() {
        IndexArgumentException e1 = new IndexArgumentException(5);
        Assert.assertEquals(5, e1.getIndex());

        IndexArgumentException e2 = new IndexArgumentException(10, MSG);
        Assert.assertEquals(10, e2.getIndex());
        Assert.assertEquals(MSG, e2.getMessage());

        IndexArgumentException e3 = new IndexArgumentException(15, MSG, CAUSE);
        Assert.assertEquals(15, e3.getIndex());
        Assert.assertEquals(MSG, e3.getMessage());
        Assert.assertEquals(CAUSE, e3.getCause());

        IndexArgumentException e4 = new IndexArgumentException(20, CAUSE);
        Assert.assertEquals(20, e4.getIndex());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    @Test
    public void testIndexArgumentExceptionSetters() {
        IndexArgumentException e = new IndexArgumentException();
        e.setIndex(7);
        e.setSize(100);
        Assert.assertEquals(7, e.getIndex());
        Assert.assertEquals(100, e.getSize());
    }

    // -------------------- WrapperException --------------------

    @Test
    public void testWrapperException() {
        HttpWrapper<String> wrapper = HttpWrapper.ok("data");
        WrapperException e1 = new WrapperException(wrapper);
        Assert.assertNotNull(e1.getWrapper());
        Assert.assertSame(wrapper, e1.getWrapper());

        WrapperException e2 = new WrapperException(wrapper, CAUSE);
        Assert.assertSame(wrapper, e2.getWrapper());
        Assert.assertEquals(CAUSE, e2.getCause());
    }

    @Test(expected = cn.orionsec.kit.lang.exception.argument.InvalidArgumentException.class)
    public void testWrapperExceptionNullWrapper() {
        new WrapperException(null);
    }

    // -------------------- HttpWrapperException --------------------

    @Test
    public void testHttpWrapperException() {
        HttpWrapper<String> wrapper = HttpWrapper.error("error msg");
        HttpWrapperException e1 = new HttpWrapperException(wrapper);
        Assert.assertNotNull(e1.getWrapper());
        Assert.assertSame(wrapper, e1.getWrapper());
        Assert.assertTrue(e1.getWrapper() instanceof HttpWrapper);

        HttpWrapperException e2 = new HttpWrapperException(wrapper, CAUSE);
        Assert.assertSame(wrapper, e2.getWrapper());
        Assert.assertEquals(CAUSE, e2.getCause());
    }

    // -------------------- RpcWrapperException --------------------

    @Test
    public void testRpcWrapperException() {
        RpcWrapper<String> wrapper = RpcWrapper.error("rpc error");
        RpcWrapperException e1 = new RpcWrapperException(wrapper);
        Assert.assertNotNull(e1.getWrapper());
        Assert.assertSame(wrapper, e1.getWrapper());
        Assert.assertTrue(e1.getWrapper() instanceof RpcWrapper);

        RpcWrapperException e2 = new RpcWrapperException(wrapper, CAUSE);
        Assert.assertSame(wrapper, e2.getWrapper());
        Assert.assertEquals(CAUSE, e2.getCause());
    }

    // -------------------- Inheritance checks --------------------

    @Test
    public void testInheritance() {
        Assert.assertTrue(new InvalidArgumentException() instanceof RuntimeException);
        Assert.assertTrue(new NullArgumentException() instanceof InvalidArgumentException);
        Assert.assertTrue(new UnMatchArgumentException() instanceof InvalidArgumentException);
        Assert.assertTrue(new CodeArgumentException() instanceof InvalidArgumentException);
        Assert.assertTrue(new IndexArgumentException() instanceof InvalidArgumentException);

        HttpWrapper<String> wrapper = HttpWrapper.ok();
        Assert.assertTrue(new WrapperException(wrapper) instanceof InvalidArgumentException);
        Assert.assertTrue(new HttpWrapperException(wrapper) instanceof WrapperException);

        RpcWrapper<String> rpcWrapper = RpcWrapper.success();
        Assert.assertTrue(new RpcWrapperException(rpcWrapper) instanceof WrapperException);
    }

}
