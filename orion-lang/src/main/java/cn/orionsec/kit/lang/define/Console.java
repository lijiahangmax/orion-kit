/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.lang.define;

import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.Stacks;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.io.Streams;

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
     *
     * @param os objects
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
