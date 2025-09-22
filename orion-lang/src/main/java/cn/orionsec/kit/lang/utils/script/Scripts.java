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
package cn.orionsec.kit.lang.utils.script;

import cn.orionsec.kit.lang.define.collect.ConcurrentReferenceHashMap;
import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.reflect.Annotations;
import cn.orionsec.kit.lang.utils.reflect.Methods;

import javax.script.*;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 脚本执行工具
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/3/1 23:29
 */
// FIXME JDK17 移除了 js 的支持
public class Scripts {

    private static final ScriptEngineManager MANAGER = new ScriptEngineManager();

    private static final ConcurrentReferenceHashMap<String, ScriptEngine> CACHE = new ConcurrentReferenceHashMap<>(ConcurrentReferenceHashMap.ReferenceType.SOFT);

    private Scripts() {
    }

    // -------------------- create --------------------

    public static ScriptEngine createJavaScript() {
        return createScript(ScriptType.JAVA_SCRIPT.getType());
    }

    public static ScriptEngine createLuaScript() {
        return createScript(ScriptType.LUA.getType());
    }

    public static ScriptEngine createGroovyScript() {
        return createScript(ScriptType.GROOVY.getType());
    }

    public static ScriptEngine createPythonScript() {
        return createScript(ScriptType.PYTHON.getType());
    }

    public static ScriptEngine createScript(ScriptType type) {
        return createScript(type.getType());
    }

    /**
     * 创建脚本
     *
     * @param type 脚本类型
     * @return 脚本引擎
     */
    public static ScriptEngine createScript(String type) {
        if (ScriptType.PYTHON.getType().equals(type)) {
            System.setProperty("python.import.site", "false");
        }
        ScriptEngine engine = MANAGER.getEngineByName(type);
        if (engine == null) {
            engine = MANAGER.getEngineByExtension(type);
        }
        if (engine == null) {
            engine = MANAGER.getEngineByMimeType(type);
        }
        if (engine == null) {
            throw Exceptions.unsupported("unsupported script type [" + type + "]");
        }
        return engine;
    }

    // -------------------- get --------------------

    public static ScriptEngine getJavaScript() {
        return getScript(ScriptType.JAVA_SCRIPT.getType());
    }

    public static ScriptEngine getLuaScript() {
        return getScript(ScriptType.LUA.getType());
    }

    public static ScriptEngine getGroovyScript() {
        return getScript(ScriptType.GROOVY.getType());
    }

    public static ScriptEngine getPythonScript() {
        return getScript(ScriptType.PYTHON.getType());
    }

    public static ScriptEngine getScript(ScriptType type) {
        return getScript(type.getType());
    }

    /**
     * 获取单例脚本
     *
     * @param type 脚本类型
     * @return 脚本引擎
     */
    public static ScriptEngine getScript(String type) {
        return CACHE.computeIfAbsent(type, Scripts::createScript);
    }

    // -------------------- eval --------------------

    public static Object eval(String script) {
        return eval(createJavaScript(), script);
    }

    public static Object eval(String script, Bindings bindings) {
        return eval(createJavaScript(), script, bindings);
    }

    public static Object eval(String script, Map<String, Object> bindings) {
        return eval(createJavaScript(), script, bindings);
    }

    public static Object eval(String script, ScriptContext context) {
        return eval(createJavaScript(), script, context);
    }

    /**
     * 执行脚本
     *
     * @param engine 引擎
     * @param script 脚本
     * @return 脚本结果
     */
    public static Object eval(ScriptEngine engine, String script) {
        try {
            return engine.eval(script);
        } catch (ScriptException e) {
            throw Exceptions.script(e);
        }
    }

    /**
     * 执行脚本
     *
     * @param engine   引擎
     * @param script   脚本
     * @param bindings 绑定的参数
     * @return 脚本结果
     */
    public static Object eval(ScriptEngine engine, String script, Map<String, Object> bindings) {
        try {
            return engine.eval(script, new SimpleBindings(bindings));
        } catch (ScriptException e) {
            throw Exceptions.script(e);
        }
    }

    /**
     * 执行脚本
     *
     * @param engine   引擎
     * @param script   脚本
     * @param bindings 绑定的参数
     * @return 脚本结果
     */
    public static Object eval(ScriptEngine engine, String script, Bindings bindings) {
        try {
            return engine.eval(script, bindings);
        } catch (ScriptException e) {
            throw Exceptions.script(e);
        }
    }

    /**
     * 执行脚本
     *
     * @param engine  引擎
     * @param script  脚本
     * @param context 上下文
     * @return 脚本结果
     */
    public static Object eval(ScriptEngine engine, String script, ScriptContext context) {
        try {
            return engine.eval(script, context);
        } catch (ScriptException e) {
            throw Exceptions.script(e);
        }
    }

    /**
     * 执行脚本
     *
     * @param engine 引擎
     * @param script 脚本
     * @param args   参数
     * @param <T>    T
     * @return 脚本结果
     */
    public static <T> Object eval(String engine, String script, T args) {
        return eval(createScript(engine), script, args);
    }

    /**
     * 执行脚本
     *
     * @param engine 引擎
     * @param script 脚本
     * @param args   参数
     * @param <T>    T
     * @return 脚本结果
     */
    public static <T> Object eval(ScriptEngine engine, String script, T args) {
        Assert.notNull(args, "eval args is null");
        try {
            Map<Method, Bind> methods = Annotations.getAnnotatedGetterMethodsMergeField(args.getClass(), Bind.class, true);
            Map<String, Object> binds = new LinkedHashMap<>();
            methods.forEach((m, a) -> {
                binds.put(a.value(), Methods.invokeMethod(args, m));
            });
            return engine.eval(script, new SimpleBindings(binds));
        } catch (ScriptException e) {
            throw Exceptions.script(e);
        }
    }

    /**
     * 解析脚本为可执行脚本
     *
     * @param engine 引擎
     * @param script script
     * @return Invocable
     */
    public static Invocable evalInvocable(ScriptEngine engine, String script) {
        Object eval = eval(engine, script);
        return Assert.isInstanceOf(eval, Invocable.class, "script cannot invocable");
    }

    /**
     * 执行脚本函数
     *
     * @param engine 引擎
     * @param script script
     * @param func   函数
     * @param args   参数
     * @return 返回值
     */
    public static Object invoke(ScriptEngine engine, String script, String func, Object... args) {
        return invoke(evalInvocable(engine, script), func, args);
    }

    /**
     * 执行脚本函数
     *
     * @param invocable 可执行脚本
     * @param func      函数
     * @param args      参数
     * @return 返回值
     */
    public static Object invoke(Invocable invocable, String func, Object... args) {
        try {
            return invocable.invokeFunction(func, args);
        } catch (ScriptException | NoSuchMethodException e) {
            throw Exceptions.script(e);
        }
    }

    // -------------------- compile --------------------

    public static CompiledScript compile(String script) {
        return compile(createJavaScript(), script);
    }

    /**
     * 编译脚本引擎
     *
     * @param engine 引擎
     * @param script 脚本
     * @return CompiledScript
     */
    public static CompiledScript compile(ScriptEngine engine, String script) {
        try {
            Assert.isInstanceOf(engine, Compilable.class, "engine does not support compilable");
            Compilable compEngine = (Compilable) engine;
            return compEngine.compile(script);
        } catch (ScriptException e) {
            throw Exceptions.script(e);
        }
    }

}
