package com.orion.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 当前栈内信息
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2019/12/24 18:33
 */
public class Stacks {

    private Stacks() {
    }

    /**
     * 获取当前所有栈内信息
     *
     * @return 栈内信息
     */
    public static List<StackTrace> currentStacks() {
        StackTraceElement[] stackTrace = new Exception().getStackTrace();
        List<StackTrace> r = new ArrayList<>();
        for (StackTraceElement traceElement : stackTrace) {
            r.add(new StackTrace(traceElement));
        }
        return r;
    }

    /**
     * 获取当前栈内信息
     *
     * @param index 栈下标
     * @return 栈内信息
     */
    public static StackTrace currentStack(int index) {
        StackTraceElement[] stackTrace = new Exception().getStackTrace();
        return new StackTrace(stackTrace[index]);
    }

    /**
     * 获取当前栈内信息
     *
     * @return 栈内信息
     */
    public static StackTrace currentStack() {
        StackTraceElement[] stackTrace = new Exception().getStackTrace();
        return new StackTrace(stackTrace[stackTrace.length - 1]);
    }

    /**
     * 获取当前文件
     *
     * @return 当前文件
     */
    public static String currentFile() {
        StackTraceElement[] stackTrace = new Exception().getStackTrace();
        return stackTrace[stackTrace.length - 1].getFileName();
    }

    /**
     * 获取当前类
     *
     * @return 当前类
     */
    public static String currentClass() {
        StackTraceElement[] stackTrace = new Exception().getStackTrace();
        return stackTrace[stackTrace.length - 1].getClassName();
    }

    /**
     * 获取当前方法
     *
     * @return 栈内当前方法
     */
    public static String currentMethod() {
        StackTraceElement[] stackTrace = new Exception().getStackTrace();
        return stackTrace[stackTrace.length - 1].getMethodName();
    }

    /**
     * 获取当前行
     *
     * @return 栈内信息
     */
    public static int currentLineNumber() {
        StackTraceElement[] stackTrace = new Exception().getStackTrace();
        return stackTrace[stackTrace.length - 1].getLineNumber();

    }

    /**
     * 获取当前方法是否是本地方法
     *
     * @return true 本地方法
     */
    public static boolean currentNetive() {
        StackTraceElement[] stackTrace = new Exception().getStackTrace();
        return stackTrace[stackTrace.length - 1].isNativeMethod();
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

    /**
     * 栈内信息
     */
    public static class StackTrace implements Serializable {

        private StackTraceElement e;

        private String className;
        private String fileName;
        private Integer lineNumber;
        private String methodName;
        private boolean nativeMethod;

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
            return e + "\n\t className ==> '" + className + '\'' +
                    "\n\t fileName ==> '" + fileName + '\'' +
                    "\n\t lineNumber ==> " + lineNumber +
                    "\n\t methodName ==> '" + methodName + '\'' +
                    "\n\t nativeMethod ==> " + nativeMethod;
        }

    }

}
