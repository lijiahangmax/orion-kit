package com.orion.utils;

import com.orion.exception.*;
import com.orion.exception.argument.IndexArgumentException;
import com.orion.exception.argument.InvalidArgumentException;
import com.orion.exception.argument.NullArgumentException;
import com.orion.exception.argument.UnMatchArgumentException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * 异常工具类
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/10/9 10:16
 */
public class Exceptions {

    private Exceptions() {
    }

    public static void impossible() {
        throw new RuntimeException("Impossible exceptions...");
    }

    /**
     * Throwable ->  RuntimeException
     *
     * @param e Throwable
     * @return RuntimeException
     */
    public static RuntimeException unchecked(Throwable e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else {
            return new RuntimeException(e);
        }
    }

    /**
     * 打印异常
     *
     * @param t Throwable
     */
    public static void printStacks(Throwable t) {
        t.printStackTrace(System.err);
    }

    /**
     * 获取异常栈内信息
     *
     * @param t 异常
     * @return 栈
     */
    public static List<Stacks.StackTrace> getStackTrace(Throwable t) {
        return Stacks.toStackTraces(t.getStackTrace());
    }

    /**
     * 获取异常栈内信息
     *
     * @param t 异常
     * @return 栈信息
     */
    public static String getStackTraceAsString(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        t.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    /**
     * 判断异常是否由某些底层的异常引起
     *
     * @param r                     异常
     * @param causeThrowableClasses checkClass
     * @return ignore
     */
    @SafeVarargs
    public static boolean isCausedBy(Throwable r, Class<? extends Exception>... causeThrowableClasses) {
        Throwable cause = r.getCause();
        while (cause != null) {
            for (Class<? extends Exception> causeClass : causeThrowableClasses) {
                if (causeClass.isInstance(cause)) {
                    return true;
                }
            }
            cause = cause.getCause();
        }
        return false;
    }

    // ------------------ new ------------------

    public static IOException io() {
        return new IOException();
    }

    public static IOException io(String s) {
        return new IOException(s);
    }

    public static IOException io(Throwable t) {
        return new IOException(t);
    }

    public static IOException io(String s, Throwable t) {
        return new IOException(s, t);
    }

    public static NullPointerException nullPoint() {
        return new NullPointerException();
    }

    public static NullPointerException nullPoint(String s) {
        return new NullPointerException(s);
    }

    public static IllegalArgumentException argument() {
        return new IllegalArgumentException();
    }

    public static IllegalArgumentException argument(Throwable t) {
        return new IllegalArgumentException(t);
    }

    public static IllegalArgumentException argument(String s) {
        return new IllegalArgumentException(s);
    }

    public static IllegalArgumentException argument(String s, Throwable t) {
        return new IllegalArgumentException(s, t);
    }

    public static IndexOutOfBoundsException index() {
        return new IndexOutOfBoundsException();
    }

    public static IndexOutOfBoundsException index(String s) {
        return new IndexOutOfBoundsException(s);
    }

    public static Exception exception() {
        return new Exception();
    }

    public static Exception exception(Throwable t) {
        return new Exception(t);
    }

    public static Exception exception(String s) {
        return new Exception(s);
    }

    public static Exception exception(String s, Throwable t) {
        return new Exception(s, t);
    }

    public static RuntimeException runtime() {
        return new RuntimeException();
    }

    public static RuntimeException runtime(Throwable t) {
        return new RuntimeException(t);
    }

    public static RuntimeException runtime(String s) {
        return new RuntimeException(s);
    }

    public static RuntimeException runtime(String s, Throwable t) {
        return new RuntimeException(s, t);
    }

    public static InvokeRuntimeException invoke() {
        return new InvokeRuntimeException();
    }

    public static InvokeRuntimeException invoke(Throwable t) {
        return new InvokeRuntimeException(t);
    }

    public static InvokeRuntimeException invoke(String s) {
        return new InvokeRuntimeException(s);
    }

    public static InvokeRuntimeException invoke(String s, Throwable t) {
        return new InvokeRuntimeException(s, t);
    }

    public static ParseDateException parseDate() {
        return new ParseDateException();
    }

    public static ParseDateException parseDate(Throwable t) {
        return new ParseDateException(t);
    }

    public static ParseDateException parseDate(String s) {
        return new ParseDateException(s);
    }

    public static ParseDateException parseDate(String s, Throwable t) {
        return new ParseDateException(s, t);
    }

    public static ConfigException config() {
        return new ConfigException();
    }

    public static ConfigException config(Throwable t) {
        return new ConfigException(t);
    }

    public static ConfigException config(String s) {
        return new ConfigException(s);
    }

    public static ConfigException config(String s, Throwable t) {
        return new ConfigException(s, t);
    }

    public static InitializeException init() {
        return new InitializeException();
    }

    public static InitializeException init(Throwable t) {
        return new InitializeException(t);
    }

    public static InitializeException init(String s) {
        return new InitializeException(s);
    }

    public static InitializeException init(String s, Throwable t) {
        return new InitializeException(s, t);
    }

    public static LockException lock() {
        return new LockException();
    }

    public static LockException lock(Throwable t) {
        return new LockException(t);
    }

    public static LockException lock(String s) {
        return new LockException(s);
    }

    public static LockException lock(String s, Throwable t) {
        return new LockException(s, t);
    }

    public static IORuntimeException ioRuntime() {
        return new IORuntimeException();
    }

    public static IORuntimeException ioRuntime(Throwable t) {
        return new IORuntimeException(t);
    }

    public static IORuntimeException ioRuntime(String s) {
        return new IORuntimeException(s);
    }

    public static IORuntimeException ioRuntime(String s, Throwable t) {
        return new IORuntimeException(s, t);
    }

    public static UnsupportedEncodingRuntimeException unCoding() {
        return new UnsupportedEncodingRuntimeException();
    }

    public static UnsupportedEncodingRuntimeException unCoding(Throwable t) {
        return new UnsupportedEncodingRuntimeException(t);
    }

    public static TimeoutException unCoding(String s) {
        return new TimeoutException(s);
    }

    public static TimeoutException unCoding(String s, Throwable t) {
        return new TimeoutException(s, t);
    }

    public static TimeoutException timeout() {
        return new TimeoutException();
    }

    public static TimeoutException timeout(Throwable t) {
        return new TimeoutException(t);
    }

    public static TimeoutException timeout(String s) {
        return new TimeoutException(s);
    }

    public static TimeoutException timeout(String s, Throwable t) {
        return new TimeoutException(s, t);
    }

    public static ConnectionRuntimeException connection() {
        return new ConnectionRuntimeException();
    }

    public static ConnectionRuntimeException connection(Throwable t) {
        return new ConnectionRuntimeException(t);
    }

    public static ConnectionRuntimeException connection(String s) {
        return new ConnectionRuntimeException(s);
    }

    public static ConnectionRuntimeException connection(String s, Throwable t) {
        return new ConnectionRuntimeException(s, t);
    }

    public static FtpException ftp() {
        return new FtpException();
    }

    public static FtpException ftp(Throwable t) {
        return new FtpException(t);
    }

    public static FtpException ftp(String s) {
        return new FtpException(s);
    }

    public static FtpException ftp(String s, Throwable t) {
        return new FtpException(s, t);
    }

    public static SftpException sftp() {
        return new SftpException();
    }

    public static SftpException sftp(Throwable t) {
        return new SftpException(t);
    }

    public static SftpException sftp(String s) {
        return new SftpException(s);
    }

    public static SftpException sftp(String s, Throwable t) {
        return new SftpException(s, t);
    }

    public static ParseRuntimeException parse() {
        return new ParseRuntimeException();
    }

    public static ParseRuntimeException parse(Throwable t) {
        return new ParseRuntimeException(t);
    }

    public static ParseRuntimeException parse(String s) {
        return new ParseRuntimeException(s);
    }

    public static ParseRuntimeException parse(String s, Throwable t) {
        return new ParseRuntimeException(s, t);
    }

    public static AuthenticationException authentication() {
        return new AuthenticationException();
    }

    public static AuthenticationException authentication(Throwable t) {
        return new AuthenticationException(t);
    }

    public static AuthenticationException authentication(String s) {
        return new AuthenticationException(s);
    }

    public static AuthenticationException authentication(String s, Throwable t) {
        return new AuthenticationException(s, t);
    }

    public static ExecuteException execute() {
        return new ExecuteException();
    }

    public static ExecuteException execute(Throwable t) {
        return new ExecuteException(t);
    }

    public static ExecuteException execute(String s) {
        return new ExecuteException(s);
    }

    public static ExecuteException execute(String s, Throwable t) {
        return new ExecuteException(s, t);
    }

    public static ConvertException convert() {
        return new ConvertException();
    }

    public static ConvertException convert(Throwable t) {
        return new ConvertException(t);
    }

    public static ConvertException convert(String s) {
        return new ConvertException(s);
    }

    public static ConvertException convert(String s, Throwable t) {
        return new ConvertException(s, t);
    }

    public static UnsupportedOperationException unSupport() {
        return new UnsupportedOperationException();
    }

    public static UnsupportedOperationException unSupport(Throwable t) {
        return new UnsupportedOperationException(t);
    }

    public static UnsupportedOperationException unSupport(String s) {
        return new UnsupportedOperationException(s);
    }

    public static UnsupportedOperationException unSupport(String s, Throwable t) {
        return new UnsupportedOperationException(s, t);
    }

    public static IllegalStateException state() {
        return new IllegalStateException();
    }

    public static IllegalStateException state(Throwable t) {
        return new IllegalStateException(t);
    }

    public static IllegalStateException state(String s) {
        return new IllegalStateException(s);
    }

    public static IllegalStateException state(String s, Throwable t) {
        return new IllegalStateException(s, t);
    }

    public static NotFoundException notFound() {
        return new NotFoundException();
    }

    public static NotFoundException notFound(Throwable t) {
        return new NotFoundException(t);
    }

    public static NotFoundException notFound(String s) {
        return new NotFoundException(s);
    }

    public static NotFoundException notFound(String s, Throwable t) {
        return new NotFoundException(s, t);
    }

    public static WatchException watch() {
        return new WatchException();
    }

    public static WatchException watch(Throwable t) {
        return new WatchException(t);
    }

    public static WatchException watch(String s) {
        return new WatchException(s);
    }

    public static WatchException watch(String s, Throwable t) {
        return new WatchException(s, t);
    }

    public static SignInvalidException sign() {
        return new SignInvalidException();
    }

    public static SignInvalidException sign(Throwable t) {
        return new SignInvalidException(t);
    }

    public static SignInvalidException sign(String s) {
        return new SignInvalidException(s);
    }

    public static SignInvalidException sign(String s, Throwable t) {
        return new SignInvalidException(s, t);
    }

    public static Error error() {
        return new Error();
    }

    public static Error error(Throwable t) {
        return new Error(t);
    }

    public static Error error(String s) {
        return new Error(s);
    }

    public static Error error(String s, Throwable t) {
        return new Error(s, t);
    }

    // ------------------------ ValidException ------------------------

    public static InvalidArgumentException invalidArgument() {
        return new InvalidArgumentException();
    }

    public static InvalidArgumentException invalidArgument(Throwable t) {
        return new InvalidArgumentException(t);
    }

    public static InvalidArgumentException invalidArgument(String s) {
        return new InvalidArgumentException(s);
    }

    public static InvalidArgumentException invalidArgument(String s, Throwable t) {
        return new InvalidArgumentException(s, t);
    }

    public static NullArgumentException nullArgument() {
        return new NullArgumentException();
    }

    public static NullArgumentException nullArgument(Throwable t) {
        return new NullArgumentException(t);
    }

    public static NullArgumentException nullArgument(String s) {
        return new NullArgumentException(s);
    }

    public static NullArgumentException nullArgument(String s, Throwable t) {
        return new NullArgumentException(s, t);
    }

    public static IndexArgumentException indexArgument() {
        return new IndexArgumentException();
    }

    public static IndexArgumentException indexArgument(Throwable t) {
        return new IndexArgumentException(t);
    }

    public static IndexArgumentException indexArgument(String s) {
        return new IndexArgumentException(s);
    }

    public static IndexArgumentException indexArgument(String s, Throwable t) {
        return new IndexArgumentException(s, t);
    }

    public static UnMatchArgumentException unMatch() {
        return new UnMatchArgumentException();
    }

    public static UnMatchArgumentException unMatch(Throwable t) {
        return new UnMatchArgumentException(t);
    }

    public static UnMatchArgumentException unMatch(String s) {
        return new UnMatchArgumentException(s);
    }

    public static UnMatchArgumentException unMatch(String s, Throwable t) {
        return new UnMatchArgumentException(s, t);
    }

}
