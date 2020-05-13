package com.orion.utils;

import com.orion.exception.*;

import java.io.IOException;

/**
 * 异常工具类
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/10/9 10:16
 */
public class Exceptions {

    private Exceptions() {
    }

    public static void impossible() {
        throw new RuntimeException("Impossible exceptions...");
    }

    // ------------------ throw ------------------

    public static void throwIO() throws IOException {
        throw new IOException();
    }

    public static void throwIO(String s) throws IOException {
        throw new IOException(s);
    }

    public static void throwIO(Throwable t) throws IOException {
        throw new IOException(t);
    }

    public static void throwIO(String s, Throwable t) throws IOException {
        throw new IOException(s, t);
    }

    public static void throwNullPoint() {
        throw new NullPointerException();
    }

    public static void throwNullPoint(String s) {
        throw new NullPointerException(s);
    }

    public static void throwArgument() {
        throw new IllegalArgumentException();
    }

    public static void throwArgument(String s) {
        throw new IllegalArgumentException(s);
    }

    public static void throwIndex() {
        throw new IndexOutOfBoundsException();
    }

    public static void throwIndex(String s) {
        throw new IndexOutOfBoundsException(s);
    }

    public static void throwException() throws Exception {
        throw new Exception();
    }

    public static void throwException(Throwable t) throws Exception {
        throw new Exception(t);
    }

    public static void throwException(String s) throws Exception {
        throw new Exception(s);
    }

    public static void throwException(String s, Throwable t) throws Exception {
        throw new Exception(s, t);
    }

    public static void throwRuntime() {
        throw new RuntimeException();
    }

    public static void throwRuntime(Throwable t) {
        throw new RuntimeException(t);
    }

    public static void throwRuntime(String s) {
        throw new RuntimeException(s);
    }

    public static void throwRuntime(String s, Throwable t) {
        throw new RuntimeException(s, t);
    }

    public static void throwError() {
        throw new Error();
    }

    public static void throwError(Throwable t) {
        throw new Error(t);
    }

    public static void throwError(String s) {
        throw new Error(s);
    }

    public static void throwError(String s, Throwable t) {
        throw new Error(s, t);
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

    public static IllegalArgumentException argument(String s) {
        return new IllegalArgumentException(s);
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

    public static UnsupportedEncodingRuntimeException unEnding() {
        return new UnsupportedEncodingRuntimeException();
    }

    public static UnsupportedEncodingRuntimeException unEnding(Throwable t) {
        return new UnsupportedEncodingRuntimeException(t);
    }

    public static TimeoutRuntimeException unEnding(String s) {
        return new TimeoutRuntimeException(s);
    }

    public static TimeoutRuntimeException unEnding(String s, Throwable t) {
        return new TimeoutRuntimeException(s, t);
    }

    public static TimeoutRuntimeException timeout() {
        return new TimeoutRuntimeException();
    }

    public static TimeoutRuntimeException timeout(Throwable t) {
        return new TimeoutRuntimeException(t);
    }

    public static TimeoutRuntimeException timeout(String s) {
        return new TimeoutRuntimeException(s);
    }

    public static TimeoutRuntimeException timeout(String s, Throwable t) {
        return new TimeoutRuntimeException(s, t);
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

    public static FTPException ftp() {
        return new FTPException();
    }

    public static FTPException ftp(Throwable t) {
        return new FTPException(t);
    }

    public static FTPException ftp(String s) {
        return new FTPException(s);
    }

    public static FTPException ftp(String s, Throwable t) {
        return new FTPException(s, t);
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

}
