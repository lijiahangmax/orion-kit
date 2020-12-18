package com.orion.lang;

import com.orion.utils.Objects1;
import com.orion.utils.Stacks;
import com.orion.utils.Strings;

import static java.lang.System.err;
import static java.lang.System.out;

/**
 * console
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/15 10:11
 */
public class Console {

    private Console() {
    }

    /**
     * println
     */
    public static void log() {
        out.println();
    }

    /**
     * println
     *
     * @param obj object
     */
    public static void log(Object obj) {
        if (obj instanceof Throwable) {
            Throwable e = (Throwable) obj;
            log(e, e.getMessage());
        } else {
            log((Throwable) null, Strings.str(obj));
        }
    }

    /**
     * println
     *
     * @param tpl    {}
     * @param values args
     */
    public static void log(String tpl, Object... values) {
        log(null, tpl, values);
    }

    /**
     * println
     *
     * @param t      Throwable
     * @param tpl    {}
     * @param values args
     */
    public static void log(Throwable t, String tpl, Object... values) {
        out.println(Strings.format(tpl, values));
        if (null != t) {
            t.printStackTrace();
            out.flush();
        }
    }

    /**
     * println
     */
    public static void error() {
        err.println();
    }

    /**
     * println
     *
     * @param obj object
     */
    public static void error(Object obj) {
        if (obj instanceof Throwable) {
            Throwable e = (Throwable) obj;
            error(e, e.getMessage());
        } else {
            error((Throwable) null, Strings.str(obj));
        }
    }

    /**
     * println
     *
     * @param tpl    {}
     * @param values args
     */
    public static void error(String tpl, Object... values) {
        error(null, tpl, values);
    }

    /**
     * println
     *
     * @param t      Throwable
     * @param tpl    {}
     * @param values args
     */
    public static void error(Throwable t, String tpl, Object... values) {
        err.println(Strings.format(tpl, values));
        if (null != t) {
            t.printStackTrace(err);
            err.flush();
        }
    }

    /**
     * 输出 trace
     */
    public static void trace(Object... os) {
        StringBuilder sb = new StringBuilder();
        if (os != null) {
            for (Object o : os) {
                sb.append(Objects1.toString(o)).append(Strings.SPACE);
            }
        }
        out.println(sb);
    }

    /**
     * @return 返回当前栈位置
     */
    public static String where() {
        return Stacks.currentStack().toRawString();
    }

    /**
     * @return 返回当前行号
     */
    public static Integer lineNumber() {
        return Stacks.currentLineNumber();
    }

}
