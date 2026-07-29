package cn.orionsec.kit.lang.utils.script;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Scripts 和 ScriptType 单元测试
 * <p>
 * JS 引擎由 org.openjdk.nashorn:nashorn-core 提供 (JDK 15+ 移除了内置 Nashorn)
 */
public class ScriptsTest {

    @Test
    public void testScriptTypeValues() {
        assertEquals("js", ScriptType.JAVA_SCRIPT.getType());
        assertEquals("lua", ScriptType.LUA.getType());
        assertEquals("groovy", ScriptType.GROOVY.getType());
        assertEquals("python", ScriptType.PYTHON.getType());
    }

    @Test
    public void testScriptTypeEnum() {
        ScriptType[] values = ScriptType.values();
        assertEquals(4, values.length);
        assertEquals(ScriptType.JAVA_SCRIPT, ScriptType.valueOf("JAVA_SCRIPT"));
        assertEquals(ScriptType.LUA, ScriptType.valueOf("LUA"));
        assertEquals(ScriptType.GROOVY, ScriptType.valueOf("GROOVY"));
        assertEquals(ScriptType.PYTHON, ScriptType.valueOf("PYTHON"));
    }

    @Test(expected = Exception.class)
    public void testCreateUnsupportedScript() {
        Scripts.createScript("nonexistent_script_type");
    }

    @Test
    public void testCreateJavaScript() {
        javax.script.ScriptEngine engine = Scripts.createJavaScript();
        assertNotNull(engine);
    }

    @Test
    public void testGetJavaScript() {
        javax.script.ScriptEngine engine = Scripts.getJavaScript();
        assertNotNull(engine);
    }

    @Test
    public void testEvalWithEngine() {
        javax.script.ScriptEngine engine = Scripts.createJavaScript();
        Object result = Scripts.eval(engine, "1 + 1");
        assertNotNull(result);
        assertEquals(2, ((Number) result).intValue());
    }

    @Test
    public void testEvalString() {
        javax.script.ScriptEngine engine = Scripts.createJavaScript();
        Object result = Scripts.eval(engine, "'hello' + ' ' + 'world'");
        assertEquals("hello world", result);
    }

    @Test
    public void testEvalWithBindings() {
        javax.script.ScriptEngine engine = Scripts.createJavaScript();
        java.util.Map<String, Object> bindings = new java.util.HashMap<>();
        bindings.put("x", 10);
        bindings.put("y", 32);
        Object result = Scripts.eval(engine, "x + y", bindings);
        assertEquals(42, ((Number) result).intValue());
    }

    @Test
    public void testEvalJsFunction() {
        javax.script.ScriptEngine engine = Scripts.createJavaScript();
        Object result = Scripts.eval(engine, "function add(a, b) { return a + b; } add(20, 22);");
        assertEquals(42, ((Number) result).intValue());
    }
}
