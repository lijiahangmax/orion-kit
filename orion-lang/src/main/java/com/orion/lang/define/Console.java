package com.orion.lang.define;

import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Objects1;
import com.orion.lang.utils.Stacks;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.io.Streams;

import java.util.StringJoiner;

import static java.lang.System.err;
import static java.lang.System.out;

/**
 * console
 *
 * @author Jiahang Li
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
        if (t != null) {
            Exceptions.printStacks(t);
            Streams.flush(out);
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
        if (t != null) {
            Exceptions.printStacks(t);
            Streams.flush(err);
        }
    }

    /**
     * 输出 trace
     */
    public static void trace(Object... os) {
        StringJoiner joiner = new StringJoiner(Strings.SPACE);
        if (os != null) {
            for (Object o : os) {
                joiner.add(Objects1.toString(o));
            }
        }
        out.println(joiner);
    }

    /**
     * @return 返回当前栈位置
     */
    public static String stack() {
        return Stacks.currentStack().toRawString();
    }

    /**
     * @return 返回当前行号
     */
    public static Integer line() {
        return Stacks.currentLineNumber();
    }

}
