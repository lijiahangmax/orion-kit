package cn.orionsec.kit.lang.exception;

import org.junit.Assert;
import org.junit.Test;

/**
 * Checked Exception 子类单元测试
 */
public class CheckedExceptionsTest {

    private static final String MSG = "test message";
    private static final Throwable CAUSE = new RuntimeException("cause");

    // -------------------- ConfigException --------------------

    @Test
    public void testConfigExceptionNoArg() {
        ConfigException e = new ConfigException();
        Assert.assertNull(e.getMessage());
    }

    @Test
    public void testConfigExceptionMessage() {
        ConfigException e = new ConfigException(MSG);
        Assert.assertEquals(MSG, e.getMessage());
    }

    @Test
    public void testConfigExceptionCause() {
        ConfigException e = new ConfigException(CAUSE);
        Assert.assertEquals(CAUSE, e.getCause());
    }

    @Test
    public void testConfigExceptionMessageAndCause() {
        ConfigException e = new ConfigException(MSG, CAUSE);
        Assert.assertEquals(MSG, e.getMessage());
        Assert.assertEquals(CAUSE, e.getCause());
    }

    // -------------------- ExecuteException --------------------

    @Test
    public void testExecuteExceptionNoArg() {
        ExecuteException e = new ExecuteException();
        Assert.assertNull(e.getMessage());
    }

    @Test
    public void testExecuteExceptionMessage() {
        ExecuteException e = new ExecuteException(MSG);
        Assert.assertEquals(MSG, e.getMessage());
    }

    @Test
    public void testExecuteExceptionCause() {
        ExecuteException e = new ExecuteException(CAUSE);
        Assert.assertEquals(CAUSE, e.getCause());
    }

    @Test
    public void testExecuteExceptionMessageAndCause() {
        ExecuteException e = new ExecuteException(MSG, CAUSE);
        Assert.assertEquals(MSG, e.getMessage());
        Assert.assertEquals(CAUSE, e.getCause());
    }

    // -------------------- Inheritance checks --------------------

    @Test
    public void testInheritance() {
        Assert.assertTrue(Exception.class.isAssignableFrom(ConfigException.class));
        Assert.assertFalse(RuntimeException.class.isAssignableFrom(ConfigException.class));

        Assert.assertTrue(Exception.class.isAssignableFrom(ExecuteException.class));
        Assert.assertFalse(RuntimeException.class.isAssignableFrom(ExecuteException.class));
    }

}
