package cn.orionsec.kit.lang.exception;

import org.junit.Assert;
import org.junit.Test;

/**
 * RuntimeException 子类单元测试
 */
public class RuntimeExceptionsTest {

    private static final String MSG = "test message";
    private static final Throwable CAUSE = new RuntimeException("cause");

    // -------------------- ApplicationException --------------------

    @Test
    public void testApplicationException() {
        ApplicationException e1 = new ApplicationException();
        Assert.assertNull(e1.getMessage());

        ApplicationException e2 = new ApplicationException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        ApplicationException e3 = new ApplicationException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        ApplicationException e4 = new ApplicationException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- AuthenticationException --------------------

    @Test
    public void testAuthenticationException() {
        AuthenticationException e1 = new AuthenticationException();
        Assert.assertNull(e1.getMessage());

        AuthenticationException e2 = new AuthenticationException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        AuthenticationException e3 = new AuthenticationException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        AuthenticationException e4 = new AuthenticationException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- ConnectionRuntimeException --------------------

    @Test
    public void testConnectionRuntimeException() {
        ConnectionRuntimeException e1 = new ConnectionRuntimeException();
        Assert.assertNull(e1.getMessage());

        ConnectionRuntimeException e2 = new ConnectionRuntimeException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        ConnectionRuntimeException e3 = new ConnectionRuntimeException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        ConnectionRuntimeException e4 = new ConnectionRuntimeException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- ConvertException --------------------

    @Test
    public void testConvertException() {
        ConvertException e1 = new ConvertException();
        Assert.assertNull(e1.getMessage());

        ConvertException e2 = new ConvertException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        ConvertException e3 = new ConvertException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        ConvertException e4 = new ConvertException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- DecryptException --------------------

    @Test
    public void testDecryptException() {
        DecryptException e1 = new DecryptException();
        Assert.assertNull(e1.getMessage());

        DecryptException e2 = new DecryptException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        DecryptException e3 = new DecryptException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        DecryptException e4 = new DecryptException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- DisabledException --------------------

    @Test
    public void testDisabledException() {
        DisabledException e1 = new DisabledException();
        Assert.assertNull(e1.getMessage());

        DisabledException e2 = new DisabledException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        DisabledException e3 = new DisabledException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        DisabledException e4 = new DisabledException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- EnabledException --------------------

    @Test
    public void testEnabledException() {
        EnabledException e1 = new EnabledException();
        Assert.assertNull(e1.getMessage());

        EnabledException e2 = new EnabledException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        EnabledException e3 = new EnabledException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        EnabledException e4 = new EnabledException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- EncryptException --------------------

    @Test
    public void testEncryptException() {
        EncryptException e1 = new EncryptException();
        Assert.assertNull(e1.getMessage());

        EncryptException e2 = new EncryptException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        EncryptException e3 = new EncryptException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        EncryptException e4 = new EncryptException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- FtpException --------------------

    @Test
    public void testFtpException() {
        FtpException e1 = new FtpException();
        Assert.assertNull(e1.getMessage());

        FtpException e2 = new FtpException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        FtpException e3 = new FtpException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        FtpException e4 = new FtpException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- HttpUnsupportedMethodException --------------------

    @Test
    public void testHttpUnsupportedMethodException() {
        HttpUnsupportedMethodException e1 = new HttpUnsupportedMethodException();
        Assert.assertNull(e1.getMessage());

        HttpUnsupportedMethodException e2 = new HttpUnsupportedMethodException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        HttpUnsupportedMethodException e3 = new HttpUnsupportedMethodException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        HttpUnsupportedMethodException e4 = new HttpUnsupportedMethodException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- InitializeException --------------------

    @Test
    public void testInitializeException() {
        InitializeException e1 = new InitializeException();
        Assert.assertNull(e1.getMessage());

        InitializeException e2 = new InitializeException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        InitializeException e3 = new InitializeException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        InitializeException e4 = new InitializeException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- InterruptedRuntimeException --------------------

    @Test
    public void testInterruptedRuntimeException() {
        InterruptedRuntimeException e1 = new InterruptedRuntimeException();
        Assert.assertNull(e1.getMessage());

        InterruptedRuntimeException e2 = new InterruptedRuntimeException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        InterruptedRuntimeException e3 = new InterruptedRuntimeException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        InterruptedRuntimeException e4 = new InterruptedRuntimeException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- InvokeRuntimeException --------------------

    @Test
    public void testInvokeRuntimeException() {
        InvokeRuntimeException e1 = new InvokeRuntimeException();
        Assert.assertNull(e1.getMessage());

        InvokeRuntimeException e2 = new InvokeRuntimeException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        InvokeRuntimeException e3 = new InvokeRuntimeException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        InvokeRuntimeException e4 = new InvokeRuntimeException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- IORuntimeException --------------------

    @Test
    public void testIORuntimeException() {
        IORuntimeException e1 = new IORuntimeException();
        Assert.assertNull(e1.getMessage());

        IORuntimeException e2 = new IORuntimeException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        IORuntimeException e3 = new IORuntimeException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        IORuntimeException e4 = new IORuntimeException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- LoadException --------------------

    @Test
    public void testLoadException() {
        LoadException e1 = new LoadException();
        Assert.assertNull(e1.getMessage());

        LoadException e2 = new LoadException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        LoadException e3 = new LoadException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        LoadException e4 = new LoadException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- LockException --------------------

    @Test
    public void testLockException() {
        LockException e1 = new LockException();
        Assert.assertNull(e1.getMessage());

        LockException e2 = new LockException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        LockException e3 = new LockException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        LockException e4 = new LockException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- MoneyFormatException --------------------

    @Test
    public void testMoneyFormatException() {
        MoneyFormatException e1 = new MoneyFormatException();
        Assert.assertNull(e1.getMessage());

        MoneyFormatException e2 = new MoneyFormatException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        MoneyFormatException e3 = new MoneyFormatException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        MoneyFormatException e4 = new MoneyFormatException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- NotFoundException --------------------

    @Test
    public void testNotFoundException() {
        NotFoundException e1 = new NotFoundException();
        Assert.assertNull(e1.getMessage());

        NotFoundException e2 = new NotFoundException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        NotFoundException e3 = new NotFoundException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        NotFoundException e4 = new NotFoundException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- SftpException --------------------

    @Test
    public void testSftpException() {
        SftpException e1 = new SftpException();
        Assert.assertNull(e1.getMessage());

        SftpException e2 = new SftpException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        SftpException e3 = new SftpException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        SftpException e4 = new SftpException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- SignInvalidException --------------------

    @Test
    public void testSignInvalidException() {
        SignInvalidException e1 = new SignInvalidException();
        Assert.assertNull(e1.getMessage());

        SignInvalidException e2 = new SignInvalidException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        SignInvalidException e3 = new SignInvalidException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        SignInvalidException e4 = new SignInvalidException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- TaskExecuteException --------------------

    @Test
    public void testTaskExecuteException() {
        TaskExecuteException e1 = new TaskExecuteException();
        Assert.assertNull(e1.getMessage());

        TaskExecuteException e2 = new TaskExecuteException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        TaskExecuteException e3 = new TaskExecuteException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        TaskExecuteException e4 = new TaskExecuteException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- TimeoutException --------------------

    @Test
    public void testTimeoutException() {
        TimeoutException e1 = new TimeoutException();
        Assert.assertNull(e1.getMessage());

        TimeoutException e2 = new TimeoutException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        TimeoutException e3 = new TimeoutException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        TimeoutException e4 = new TimeoutException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- UnsafeException --------------------

    @Test
    public void testUnsafeException() {
        UnsafeException e1 = new UnsafeException();
        Assert.assertNull(e1.getMessage());

        UnsafeException e2 = new UnsafeException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        UnsafeException e3 = new UnsafeException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        UnsafeException e4 = new UnsafeException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- UnsupportedEncodingRuntimeException --------------------

    @Test
    public void testUnsupportedEncodingRuntimeException() {
        UnsupportedEncodingRuntimeException e1 = new UnsupportedEncodingRuntimeException();
        Assert.assertNull(e1.getMessage());

        UnsupportedEncodingRuntimeException e2 = new UnsupportedEncodingRuntimeException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        UnsupportedEncodingRuntimeException e3 = new UnsupportedEncodingRuntimeException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        UnsupportedEncodingRuntimeException e4 = new UnsupportedEncodingRuntimeException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- VcsException --------------------

    @Test
    public void testVcsException() {
        VcsException e1 = new VcsException();
        Assert.assertNull(e1.getMessage());

        VcsException e2 = new VcsException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        VcsException e3 = new VcsException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        VcsException e4 = new VcsException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- WatchException --------------------

    @Test
    public void testWatchException() {
        WatchException e1 = new WatchException();
        Assert.assertNull(e1.getMessage());

        WatchException e2 = new WatchException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        WatchException e3 = new WatchException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        WatchException e4 = new WatchException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- LogException --------------------

    @Test
    public void testLogException() {
        LogException e1 = new LogException();
        Assert.assertNull(e1.getMessage());
        Assert.assertFalse(e1.hasCause());

        LogException e2 = new LogException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());
        Assert.assertFalse(e2.hasCause());

        LogException e3 = new LogException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());
        Assert.assertTrue(e3.hasCause());

        LogException e4 = new LogException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
        Assert.assertTrue(e4.hasCause());
    }

    // -------------------- HttpRequestException --------------------

    @Test
    public void testHttpRequestException() {
        String url = "http://example.com";

        HttpRequestException e1 = new HttpRequestException(url);
        Assert.assertEquals(url, e1.getUrl());
        Assert.assertNull(e1.getMessage());

        HttpRequestException e2 = new HttpRequestException(url, MSG);
        Assert.assertEquals(url, e2.getUrl());
        Assert.assertEquals(MSG, e2.getMessage());

        HttpRequestException e3 = new HttpRequestException(url, CAUSE);
        Assert.assertEquals(url, e3.getUrl());
        Assert.assertEquals(CAUSE, e3.getCause());

        HttpRequestException e4 = new HttpRequestException(url, MSG, CAUSE);
        Assert.assertEquals(url, e4.getUrl());
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());
    }

    // -------------------- ParseRuntimeException --------------------

    @Test
    public void testParseRuntimeException() {
        ParseRuntimeException e1 = new ParseRuntimeException();
        Assert.assertNull(e1.getMessage());
        Assert.assertEquals(0, e1.getErrorOffset());

        ParseRuntimeException e2 = new ParseRuntimeException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        ParseRuntimeException e3 = new ParseRuntimeException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        ParseRuntimeException e4 = new ParseRuntimeException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());

        ParseRuntimeException e5 = new ParseRuntimeException(10);
        Assert.assertEquals(10, e5.getErrorOffset());

        ParseRuntimeException e6 = new ParseRuntimeException(MSG, 5);
        Assert.assertEquals(MSG, e6.getMessage());
        Assert.assertEquals(5, e6.getErrorOffset());

        ParseRuntimeException e7 = new ParseRuntimeException(7, CAUSE);
        Assert.assertEquals(CAUSE, e7.getCause());
        Assert.assertEquals(7, e7.getErrorOffset());

        ParseRuntimeException e8 = new ParseRuntimeException(MSG, 3, CAUSE);
        Assert.assertEquals(MSG, e8.getMessage());
        Assert.assertEquals(CAUSE, e8.getCause());
        Assert.assertEquals(3, e8.getErrorOffset());
    }

    // -------------------- ParseCronException --------------------

    @Test
    public void testParseCronException() {
        ParseCronException e1 = new ParseCronException();
        Assert.assertNull(e1.getMessage());

        ParseCronException e2 = new ParseCronException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        ParseCronException e3 = new ParseCronException(10);
        Assert.assertEquals(10, e3.getErrorOffset());

        ParseCronException e4 = new ParseCronException(MSG, 5);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(5, e4.getErrorOffset());
    }

    // -------------------- ParseDateException --------------------

    @Test
    public void testParseDateException() {
        ParseDateException e1 = new ParseDateException();
        Assert.assertNull(e1.getMessage());

        ParseDateException e2 = new ParseDateException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        ParseDateException e3 = new ParseDateException(10);
        Assert.assertEquals(10, e3.getErrorOffset());

        ParseDateException e4 = new ParseDateException(MSG, 5);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(5, e4.getErrorOffset());
    }

    // -------------------- ScriptExecuteException --------------------

    @Test
    public void testScriptExecuteException() {
        ScriptExecuteException e1 = new ScriptExecuteException();
        Assert.assertNull(e1.getMessage());

        ScriptExecuteException e2 = new ScriptExecuteException(MSG);
        Assert.assertEquals(MSG, e2.getMessage());

        ScriptExecuteException e3 = new ScriptExecuteException(CAUSE);
        Assert.assertEquals(CAUSE, e3.getCause());

        ScriptExecuteException e4 = new ScriptExecuteException(MSG, CAUSE);
        Assert.assertEquals(MSG, e4.getMessage());
        Assert.assertEquals(CAUSE, e4.getCause());

        // test ScriptException constructor
        javax.script.ScriptException se = new javax.script.ScriptException("script error", "test.js", 10, 5);
        ScriptExecuteException e5 = new ScriptExecuteException(se);
        Assert.assertEquals(se, e5.getCause());
        Assert.assertTrue(e5.getMessage().contains("test.js"));
        Assert.assertTrue(e5.getMessage().contains("10"));
        Assert.assertTrue(e5.getMessage().contains("5"));
    }

    // -------------------- Inheritance checks --------------------

    @Test
    public void testInheritance() {
        Assert.assertTrue(new ApplicationException() instanceof RuntimeException);
        Assert.assertTrue(new AuthenticationException() instanceof RuntimeException);
        Assert.assertTrue(new ConnectionRuntimeException() instanceof RuntimeException);
        Assert.assertTrue(new ConvertException() instanceof RuntimeException);
        Assert.assertTrue(new DecryptException() instanceof RuntimeException);
        Assert.assertTrue(new DisabledException() instanceof RuntimeException);
        Assert.assertTrue(new EnabledException() instanceof RuntimeException);
        Assert.assertTrue(new EncryptException() instanceof RuntimeException);
        Assert.assertTrue(new FtpException() instanceof RuntimeException);
        Assert.assertTrue(new HttpRequestException("url") instanceof RuntimeException);
        Assert.assertTrue(new HttpUnsupportedMethodException() instanceof RuntimeException);
        Assert.assertTrue(new InitializeException() instanceof RuntimeException);
        Assert.assertTrue(new InterruptedRuntimeException() instanceof RuntimeException);
        Assert.assertTrue(new InvokeRuntimeException() instanceof RuntimeException);
        Assert.assertTrue(new IORuntimeException() instanceof RuntimeException);
        Assert.assertTrue(new LoadException() instanceof RuntimeException);
        Assert.assertTrue(new LockException() instanceof RuntimeException);
        Assert.assertTrue(new LogException() instanceof RuntimeException);
        Assert.assertTrue(new MoneyFormatException() instanceof RuntimeException);
        Assert.assertTrue(new NotFoundException() instanceof RuntimeException);
        Assert.assertTrue(new ParseCronException() instanceof ParseRuntimeException);
        Assert.assertTrue(new ParseDateException() instanceof ParseRuntimeException);
        Assert.assertTrue(new ParseRuntimeException() instanceof RuntimeException);
        Assert.assertTrue(new ScriptExecuteException() instanceof RuntimeException);
        Assert.assertTrue(new SftpException() instanceof RuntimeException);
        Assert.assertTrue(new SignInvalidException() instanceof RuntimeException);
        Assert.assertTrue(new TaskExecuteException() instanceof RuntimeException);
        Assert.assertTrue(new TimeoutException() instanceof RuntimeException);
        Assert.assertTrue(new UnsafeException() instanceof RuntimeException);
        Assert.assertTrue(new UnsupportedEncodingRuntimeException() instanceof RuntimeException);
        Assert.assertTrue(new VcsException() instanceof RuntimeException);
        Assert.assertTrue(new WatchException() instanceof RuntimeException);
    }

}
