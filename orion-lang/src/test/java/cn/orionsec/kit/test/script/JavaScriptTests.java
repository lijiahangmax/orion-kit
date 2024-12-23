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
package cn.orionsec.kit.test.script;

import cn.orionsec.kit.lang.constant.StandardContentType;
import cn.orionsec.kit.lang.utils.collect.Maps;
import cn.orionsec.kit.lang.utils.script.Scripts;
import org.junit.Test;

import javax.script.ScriptEngine;
import java.math.BigDecimal;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/2 0:42
 */
public class JavaScriptTests {

    @Test
    public void createScriptEngine() {
        ScriptEngine e1 = Scripts.createJavaScript();
        ScriptEngine e2 = Scripts.createScript("js");
        ScriptEngine e3 = Scripts.createScript("javascript");
        ScriptEngine e4 = Scripts.createScript(StandardContentType.APPLICATION_JAVASCRIPT);
        System.out.println(e1);
        System.out.println(e2);
        System.out.println(e3);
        System.out.println(e4);
    }

    @Test
    public void getScriptEngine() {
        ScriptEngine e1 = Scripts.getJavaScript();
        ScriptEngine e2 = Scripts.getScript("js");
        ScriptEngine e3 = Scripts.getScript("javascript");
        ScriptEngine e4 = Scripts.getScript("javascript");
        ScriptEngine e5 = Scripts.getScript(StandardContentType.APPLICATION_JAVASCRIPT);
        ScriptEngine e6 = Scripts.getScript(StandardContentType.APPLICATION_JAVASCRIPT);
        System.out.println(e1);
        System.out.println(e2);
        System.out.println(e3);
        System.out.println(e4);
        System.out.println(e5);
        System.out.println(e6);
    }

    @Test
    public void eval() {
        System.out.println(Scripts.eval("1+2"));
        System.out.println(Scripts.eval("parseInt(a)+parseInt(b)", Maps.of("a", "1", "b", "2")));

        Exp e = new Exp();
        e.setId(1);
        e.setName("whh");
        e.setNum(BigDecimal.valueOf(2));
        e.setPrice(BigDecimal.valueOf(10));
        System.out.println(Scripts.eval("js", "'用户:' + id + ' 名称: ' + name + ' 总金额: ' + (num * price)", e));
        System.out.println(Scripts.eval("js", "function a(){return 1} a();", e));

    }

}
