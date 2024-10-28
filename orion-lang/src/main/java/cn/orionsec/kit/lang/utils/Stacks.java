/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.lang.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 当前栈内信息
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/12/24 18:33
 */
public class Stacks {

    private Stacks() {
    }

    public static List<StackTrace> currentStacks() {
        return currentStacks(current());
    }

    public static List<StackTrace> currentStacks(Exception e) {
        return currentStacks(e.getStackTrace());
    }

    /**
     * 获取当前所有栈内信息
     *
     * @param stackTrace stackTrace
     * @return 栈内信息
     */
    public static List<StackTrace> currentStacks(StackTraceElement[] stackTrace) {
        List<StackTrace> r = new ArrayList<>();
        for (StackTraceElement traceElement : stackTrace) {
            r.add(new StackTrace(traceElement));
        }
        return r;
    }

    public static StackTrace currentStack(int index) {
        return currentStack(current(), index);
    }

    public static StackTrace currentStack(Exception e, int index) {
        return currentStack(e.getStackTrace(), index);
    }

    /**
     * 获取当前栈内信息
     *
     * @param index      栈下标
     * @param stackTrace stackTrace
     * @return 栈内信息
     */
    public static StackTrace currentStack(StackTraceElement[] stackTrace, int index) {
        return new StackTrace(stackTrace[index]);
    }

    public static StackTrace currentStack() {
        return currentStack(current());
    }

    public static StackTrace currentStack(Exception e) {
        return currentStack(e.getStackTrace());
    }

    /**
     * 获取当前栈内信息
     *
     * @param stackTrace stackTrace
     * @return 栈内信息
     */
    public static StackTrace currentStack(StackTraceElement[] stackTrace) {
        return new StackTrace(Arrays1.last(stackTrace));
    }

    public static String currentFile() {
        return currentFile(current());
    }

    public static String currentFile(Exception e) {
        return currentFile(e.getStackTrace());
    }

    /**
     * 获取当前文件
     *
     * @param stackTrace stackTrace
     * @return 当前文件
     */
    public static String currentFile(StackTraceElement[] stackTrace) {
        return Arrays1.last(stackTrace).getFileName();
    }

    public static String currentClass() {
        return currentClass(current());
    }

    public static String currentClass(Exception e) {
        return currentClass(e.getStackTrace());
    }

    /**
     * 获取当前类
     *
     * @param stackTrace stackTrace
     * @return 当前类
     */
    public static String currentClass(StackTraceElement[] stackTrace) {
        return Arrays1.last(stackTrace).getClassName();
    }

    public static String currentMethod() {
        return currentMethod(current());
    }

    public static String currentMethod(Exception e) {
        return currentMethod(e.getStackTrace());
    }

    /**
     * 获取当前方法
     *
     * @param stackTrace stackTrace
     * @return 栈内当前方法
     */
    public static String currentMethod(StackTraceElement[] stackTrace) {
        return Arrays1.last(stackTrace).getMethodName();
    }

    public static int currentLineNumber() {
        return currentLineNumber(current());
    }

    public static int currentLineNumber(Exception e) {
        return currentLineNumber(e.getStackTrace());
    }

    /**
     * 获取当前行
     *
     * @param stackTrace stackTrace
     * @return 栈内信息
     */
    public static int currentLineNumber(StackTraceElement[] stackTrace) {
        return Arrays1.last(stackTrace).getLineNumber();
    }

    public static boolean currentNative() {
        return currentNative(current());
    }

    public static boolean currentNative(Exception e) {
        return currentNative(e.getStackTrace());
    }

    /**
     * 获取当前方法是否是本地方法
     *
     * @param stackTrace stackTrace
     * @return true 本地方法
     */
    public static boolean currentNative(StackTraceElement[] stackTrace) {
        return Arrays1.last(stackTrace).isNativeMethod();
    }

    /**
     * StackTraceElement -> StackTrace
     *
     * @param e StackTraceElement
     * @return StackTrace
     */
    public static StackTrace toStackTrace(StackTraceElement e) {
        return new StackTrace(e);
    }

    /**
     * StackTraceElement -> StackTrace
     *
     * @param es StackTraceElement
     * @return StackTrace
     */
    public static List<StackTrace> toStackTraces(StackTraceElement[] es) {
        List<StackTrace> list = new ArrayList<>();
        for (StackTraceElement e : es) {
            list.add(new StackTrace(e));
        }
        return list;
    }

    private static StackTraceElement[] current() {
        return Thread.currentThread().getStackTrace();
    }

    /**
     * 栈内信息
     */
    public static class StackTrace implements Serializable {

        private final StackTraceElement e;

        private final String className;

        private final String fileName;

        private final Integer lineNumber;

        private final String methodName;

        private final boolean nativeMethod;

        private StackTrace(StackTraceElement e) {
            this.e = e;
            this.className = e.getClassName();
            this.fileName = e.getFileName();
            this.lineNumber = e.getLineNumber();
            this.methodName = e.getMethodName();
            this.nativeMethod = e.isNativeMethod();
        }

        public String getClassName() {
            return className;
        }

        public String getFileName() {
            return fileName;
        }

        public Integer getLineNumber() {
            return lineNumber;
        }

        public String getMethodName() {
            return methodName;
        }

        public boolean isNativeMethod() {
            return nativeMethod;
        }

        public String toRawString() {
            return e.toString();
        }

        @Override
        public String toString() {
            return e +
                    "\n     className ==> '" + className + '\'' +
                    "\n      fileName ==> '" + fileName + '\'' +
                    "\n    lineNumber ==> " + lineNumber +
                    "\n    methodName ==> '" + methodName + '\'' +
                    "\n  nativeMethod ==> " + nativeMethod;
        }

    }

}
