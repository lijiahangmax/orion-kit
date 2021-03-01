package com.orion.test.script;

import com.orion.utils.script.Scripts;
import org.junit.Test;

import javax.script.ScriptEngine;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2021/3/2 0:48
 */
public class PythonScriptTests {

    @Test
    public void createScriptEngine() {
        ScriptEngine e1 = Scripts.createPythonScript();
        ScriptEngine e2 = Scripts.createScript("python");
        System.out.println(e1);
        System.out.println(e2);
    }

    @Test
    public void getScriptEngine() {
        ScriptEngine e1 = Scripts.getPythonScript();
        ScriptEngine e2 = Scripts.getScript("python");
        System.out.println(e1);
        System.out.println(e2);
    }

    @Test
    public void eval() {
        ScriptEngine e1 = Scripts.createPythonScript();
        System.out.println(Scripts.eval(e1, "1+2"));
    }

}
