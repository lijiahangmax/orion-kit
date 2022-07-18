package com.orion.lang.utils;

import com.orion.lang.define.wrapper.HttpWrapper;
import com.orion.lang.define.wrapper.RpcWrapper;
import com.orion.lang.define.wrapper.Wrapper;
import com.orion.lang.exception.*;
import com.orion.lang.exception.argument.*;

import javax.script.ScriptException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 异常工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/10/9 10:16
 */
public class Exceptions {

    private Exceptions() {
    }

    public static void impossible() {
        throw runtime("impossible exceptions...");
    }

    /**
     * 获取异常摘要信息
     *
     * @param e e
     * @return 摘要
     */
    public static String getDigest(Throwable e) {
        if (e.getMessage() == null) {
            return e.getClass().getName();
        } else {
            return e.getClass().getName() + Strings.SPACE + e.getMessage();
        }
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
        t.printStackTrace();
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

    // -------------------- new --------------------

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

    public static ApplicationException app() {
        return new ApplicationException();
    }

    public static ApplicationException app(Throwable t) {
        return new ApplicationException(t);
    }

    public static ApplicationException app(String s) {
        return new ApplicationException(s);
    }

    public static ApplicationException app(String s, Throwable t) {
        return new ApplicationException(s, t);
    }

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

    public static NumberFormatException numberFormat() {
        return new NumberFormatException();
    }

    public static NumberFormatException numberFormat(String s) {
        return new NumberFormatException(s);
    }

    public static IndexOutOfBoundsException index() {
        return new IndexOutOfBoundsException();
    }

    public static IndexOutOfBoundsException index(String s) {
        return new IndexOutOfBoundsException(s);
    }

    public static ArrayIndexOutOfBoundsException arrayIndex() {
        return new ArrayIndexOutOfBoundsException();
    }

    public static ArrayIndexOutOfBoundsException arrayIndex(String s) {
        return new ArrayIndexOutOfBoundsException(s);
    }

    public static ArrayIndexOutOfBoundsException arrayIndex(int index) {
        return new ArrayIndexOutOfBoundsException(index);
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

    public static ParseRuntimeException parse() {
        return new ParseRuntimeException();
    }

    public static ParseRuntimeException parse(int index) {
        return new ParseRuntimeException(index);
    }

    public static ParseRuntimeException parse(String s) {
        return new ParseRuntimeException(s);
    }

    public static ParseRuntimeException parse(String s, int index) {
        return new ParseRuntimeException(s, index);
    }

    public static ParseRuntimeException parse(Throwable t) {
        return new ParseRuntimeException(t);
    }

    public static ParseRuntimeException parse(int index, Throwable t) {
        return new ParseRuntimeException(index, t);
    }

    public static ParseRuntimeException parse(String s, Throwable t) {
        return new ParseRuntimeException(s, t);
    }

    public static ParseRuntimeException parse(String s, int index, Throwable t) {
        return new ParseRuntimeException(s, index, t);
    }

    public static ParseDateException parseDate() {
        return new ParseDateException();
    }

    public static ParseDateException parseDate(int index) {
        return new ParseDateException(index);
    }

    public static ParseDateException parseDate(String s) {
        return new ParseDateException(s);
    }

    public static ParseDateException parseDate(String s, int index) {
        return new ParseDateException(s, index);
    }

    public static ParseCronException parseCron() {
        return new ParseCronException();
    }

    public static ParseCronException parseCron(int index) {
        return new ParseCronException(index);
    }

    public static ParseCronException parseCron(String s) {
        return new ParseCronException(s);
    }

    public static ParseCronException parseCron(String s, int index) {
        return new ParseCronException(s, index);
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

    public static LoadException load() {
        return new LoadException();
    }

    public static LoadException load(Throwable t) {
        return new LoadException(t);
    }

    public static LoadException load(String s) {
        return new LoadException(s);
    }

    public static LoadException load(String s, Throwable t) {
        return new LoadException(s, t);
    }

    public static UnsafeException unsafe() {
        return new UnsafeException();
    }

    public static UnsafeException unsafe(Throwable t) {
        return new UnsafeException(t);
    }

    public static UnsafeException unsafe(String s) {
        return new UnsafeException(s);
    }

    public static UnsafeException unsafe(String s, Throwable t) {
        return new UnsafeException(s, t);
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

    public static InterruptedException interrupted() {
        return new InterruptedException();
    }

    public static InterruptedException interrupted(String s) {
        return new InterruptedException(s);
    }

    public static InterruptedRuntimeException interruptedRuntime() {
        return new InterruptedRuntimeException();
    }

    public static InterruptedRuntimeException interruptedRuntime(Throwable t) {
        return new InterruptedRuntimeException(t);
    }

    public static InterruptedRuntimeException interruptedRuntime(String s) {
        return new InterruptedRuntimeException(s);
    }

    public static InterruptedRuntimeException interruptedRuntime(String s, Throwable t) {
        return new InterruptedRuntimeException(s, t);
    }

    public static UnsupportedEncodingRuntimeException unsupportedEncoding() {
        return new UnsupportedEncodingRuntimeException();
    }

    public static UnsupportedEncodingRuntimeException unsupportedEncoding(Throwable t) {
        return new UnsupportedEncodingRuntimeException(t);
    }

    public static UnsupportedEncodingRuntimeException unsupportedEncoding(String s) {
        return new UnsupportedEncodingRuntimeException(s);
    }

    public static UnsupportedEncodingRuntimeException unsupportedEncoding(String s, Throwable t) {
        return new UnsupportedEncodingRuntimeException(s, t);
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

    public static HttpRequestException httpRequest(String url) {
        return new HttpRequestException(url);
    }

    public static HttpRequestException httpRequest(String url, Throwable t) {
        return new HttpRequestException(url, t);
    }

    public static HttpRequestException httpRequest(String url, String msg) {
        return new HttpRequestException(url, msg);
    }

    public static HttpRequestException httpRequest(String url, String msg, Throwable t) {
        return new HttpRequestException(url, msg, t);
    }

    public static HttpUnsupportedMethodException httpUnsupportedMethod() {
        return new HttpUnsupportedMethodException();
    }

    public static HttpUnsupportedMethodException httpUnsupportedMethod(Throwable t) {
        return new HttpUnsupportedMethodException(t);
    }

    public static HttpUnsupportedMethodException httpUnsupportedMethod(String s) {
        return new HttpUnsupportedMethodException(s);
    }

    public static HttpUnsupportedMethodException httpUnsupportedMethod(String s, Throwable t) {
        return new HttpUnsupportedMethodException(s, t);
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

    public static EncryptException encrypt() {
        return new EncryptException();
    }

    public static EncryptException encrypt(Throwable t) {
        return new EncryptException(t);
    }

    public static EncryptException encrypt(String s) {
        return new EncryptException(s);
    }

    public static EncryptException encrypt(String s, Throwable t) {
        return new EncryptException(s, t);
    }

    public static DecryptException decrypt() {
        return new DecryptException();
    }

    public static DecryptException decrypt(Throwable t) {
        return new DecryptException(t);
    }

    public static DecryptException decrypt(String s) {
        return new DecryptException(s);
    }

    public static DecryptException decrypt(String s, Throwable t) {
        return new DecryptException(s, t);
    }

    public static UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException();
    }

    public static UnsupportedOperationException unsupported(Throwable t) {
        return new UnsupportedOperationException(t);
    }

    public static UnsupportedOperationException unsupported(String s) {
        return new UnsupportedOperationException(s);
    }

    public static UnsupportedOperationException unsupported(String s, Throwable t) {
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

    public static NoSuchElementException noSuchElement() {
        return new NoSuchElementException();
    }

    public static NoSuchElementException noSuchElement(String s) {
        return new NoSuchElementException(s);
    }

    public static TaskExecuteException task() {
        return new TaskExecuteException();
    }

    public static TaskExecuteException task(Throwable t) {
        return new TaskExecuteException(t);
    }

    public static TaskExecuteException task(String s) {
        return new TaskExecuteException(s);
    }

    public static TaskExecuteException task(String s, Throwable t) {
        return new TaskExecuteException(s, t);
    }

    public static ScriptExecuteException script() {
        return new ScriptExecuteException();
    }

    public static ScriptExecuteException script(Throwable t) {
        return new ScriptExecuteException(t);
    }

    public static ScriptExecuteException script(String s) {
        return new ScriptExecuteException(s);
    }

    public static ScriptExecuteException script(String s, Throwable t) {
        return new ScriptExecuteException(s, t);
    }

    public static ScriptExecuteException script(ScriptException e) {
        return new ScriptExecuteException(e);
    }

    public static VcsException vcs() {
        return new VcsException();
    }

    public static VcsException vcs(Throwable t) {
        return new VcsException(t);
    }

    public static VcsException vcs(String s) {
        return new VcsException(s);
    }

    public static VcsException vcs(String s, Throwable t) {
        return new VcsException(s, t);
    }

    public static LogException log() {
        return new LogException();
    }

    public static LogException log(Throwable t) {
        return new LogException(t);
    }

    public static LogException log(String s) {
        return new LogException(s);
    }

    public static LogException log(String s, Throwable t) {
        return new LogException(s, t);
    }

    public static EnabledException enabled() {
        return new EnabledException();
    }

    public static EnabledException enabled(Throwable t) {
        return new EnabledException(t);
    }

    public static EnabledException enabled(String s) {
        return new EnabledException(s);
    }

    public static EnabledException enabled(String s, Throwable t) {
        return new EnabledException(s, t);
    }

    public static DisabledException disabled() {
        return new DisabledException();
    }

    public static DisabledException disabled(Throwable t) {
        return new DisabledException(t);
    }

    public static DisabledException disabled(String s) {
        return new DisabledException(s);
    }

    public static DisabledException disabled(String s, Throwable t) {
        return new DisabledException(s, t);
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

    // -------------------- invalidException --------------------

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

    public static IndexArgumentException indexArgument(int index) {
        return new IndexArgumentException(index);
    }

    public static IndexArgumentException indexArgument(int index, Throwable t) {
        return new IndexArgumentException(index, t);
    }

    public static IndexArgumentException indexArgument(int index, String s) {
        return new IndexArgumentException(index, s);
    }

    public static IndexArgumentException indexArgument(int index, String s, Throwable t) {
        return new IndexArgumentException(index, s, t);
    }

    public static UnMatchArgumentException unMatchArgument() {
        return new UnMatchArgumentException();
    }

    public static UnMatchArgumentException unMatchArgument(Throwable t) {
        return new UnMatchArgumentException(t);
    }

    public static UnMatchArgumentException unMatchArgument(String s) {
        return new UnMatchArgumentException(s);
    }

    public static UnMatchArgumentException unMatchArgument(String s, Throwable t) {
        return new UnMatchArgumentException(s, t);
    }

    public static CodeArgumentException codeArgument() {
        return new CodeArgumentException();
    }

    public static CodeArgumentException codeArgument(Throwable t) {
        return new CodeArgumentException(t);
    }

    public static CodeArgumentException codeArgument(String s) {
        return new CodeArgumentException(s);
    }

    public static CodeArgumentException codeArgument(String s, Throwable t) {
        return new CodeArgumentException(s, t);
    }

    public static CodeArgumentException codeArgument(int code) {
        return new CodeArgumentException(code);
    }

    public static CodeArgumentException codeArgument(int code, Throwable t) {
        return new CodeArgumentException(code, t);
    }

    public static CodeArgumentException codeArgument(int code, String s) {
        return new CodeArgumentException(code, s);
    }

    public static CodeArgumentException codeArgument(int code, String s, Throwable t) {
        return new CodeArgumentException(code, s, t);
    }

    public static WrapperException wrapper(Wrapper<?> wrapper) {
        return new WrapperException(wrapper);
    }

    public static WrapperException wrapper(Wrapper<?> wrapper, Throwable t) {
        return new WrapperException(wrapper, t);
    }

    public static WrapperException wrapper(Wrapper<?> wrapper, String s) {
        return new WrapperException(wrapper, s);
    }

    public static WrapperException wrapper(Wrapper<?> wrapper, String s, Throwable t) {
        return new WrapperException(wrapper, s, t);
    }

    public static HttpWrapperException httpWrapper(HttpWrapper<?> wrapper) {
        return new HttpWrapperException(wrapper);
    }

    public static HttpWrapperException httpWrapper(HttpWrapper<?> wrapper, Throwable t) {
        return new HttpWrapperException(wrapper, t);
    }

    public static HttpWrapperException httpWrapper(HttpWrapper<?> wrapper, String s) {
        return new HttpWrapperException(wrapper, s);
    }

    public static HttpWrapperException httpWrapper(HttpWrapper<?> wrapper, String s, Throwable t) {
        return new HttpWrapperException(wrapper, s, t);
    }

    public static RpcWrapperException rpcWrapper(RpcWrapper<?> wrapper) {
        return new RpcWrapperException(wrapper);
    }

    public static RpcWrapperException rpcWrapper(RpcWrapper<?> wrapper, Throwable t) {
        return new RpcWrapperException(wrapper, t);
    }

    public static RpcWrapperException rpcWrapper(RpcWrapper<?> wrapper, String s) {
        return new RpcWrapperException(wrapper, s);
    }

    public static RpcWrapperException rpcWrapper(RpcWrapper<?> wrapper, String s, Throwable t) {
        return new RpcWrapperException(wrapper, s, t);
    }

}
