package com.orion.test.script;

import com.orion.constant.StandardContentType;
import com.orion.utils.collect.Maps;
import com.orion.utils.script.Scripts;
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
